package assess;

import result.highlights.CubeSchemaResolver;
import result.highlights.HighlightExtractor;
import result.highlights.HighlightSet;
import result.highlights.IntentionalOperator;
import result.highlights.OperatorResult;
import result.highlights.metamodel.ArchetypeProperty;
import result.highlights.instance.ElementaryHighlight;
import result.highlights.instance.Highlight;
import result.highlights.instance.HolisticHighlight;
import assess.syntax.AssessQueryLexer;
import assess.syntax.AssessQueryParser;
import assess.utils.ComparedCell;
import assess.utils.LabeledCell;
import cubemanager.CubeManager;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import result.ResultFileMetadata;
import model.abstracts.AbstractModel;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The top layer class for any assessments done in the intentional model.
 * Given that the CubeManager handles only one cube at a time, instances
 * of this class are created everytime we wish to change cubes.
 *
 * <p>The operator is a Stage-1 producer: {@link #execute(String)} runs the query-defined
 * {@link AssessModel} (benchmark + delta + labeling) over the cube data and returns an
 * {@link OperatorResult}, registering the archetypes worth testing on it. It does not extract or
 * build highlights — Stage-2 evaluation ({@link HighlightExtractor}) runs on top of the result,
 * externally.
 */
public class AssessOperator implements IntentionalOperator {
    private final CubeManager cubeManager;

    private OperatorResult operatorResult;
    private List<ArchetypeProperty> registeredArchetypes = new ArrayList<>();
    private String outputFileName;

    public AssessOperator(CubeManager cubeManager) {
        this.cubeManager = cubeManager;
    }

    /**
     * Stage 1: parses the query, runs the {@link AssessModel} over the cube data, and returns the
     * operator's product with ASSESS's candidate archetypes registered.
     *
     * @param assessQuery The user-provided query for assessment reasons
     * @throws RecognitionException If the query does not follow the defined syntax
     */
    public OperatorResult execute(String assessQuery) throws RecognitionException {
        AssessQuery parsedQuery = parseQuery(assessQuery);
        outputFileName = parsedQuery.outputName;

        AssessModel assessModel = new AssessModel(
                parsedQuery.benchmark, parsedQuery.deltaFunction, parsedQuery.labelingScheme,
                parsedQuery.targetCube);

        if (assessModel.compute() != 0) {
            throw new RuntimeException("No cells collected from the target cube query");
        }

        operatorResult = new OperatorResult(
                parsedQuery.targetCubeQuery, parsedQuery.targetCube,
                Collections.<AbstractModel>singletonList(assessModel));

        return operatorResult;
    }

    /**
     * Produces the result, extracts highlights over it, and writes the ASSESS Markdown report.
     * The file-producing entry point for the RMI path; the extraction itself is external.
     */
    public ResultFileMetadata execute(String assessQuery, String metadataFilename) {
        ResultFileMetadata results = new ResultFileMetadata();
        results.setComponentResultFiles(null);
        results.setComponentResultInfoFiles(null);
        results.setResultInfoFile(metadataFilename);
        CubeSchemaResolver schemaResolver = CubeSchemaResolver.from(cubeManager);

        try {
            OperatorResult result = execute(assessQuery);
            HighlightSet highlights = new HighlightExtractor()
                    .extract(result, registeredArchetypes, schemaResolver);
            results.setResultFile(AssessReport.write(assessQuery, result, highlights, outputFileName));
        } catch (RecognitionException | RuntimeException e) {
            results.setErrorCheckingStatus(e.toString());
        }
        return results;
    }

    @Override
    public OperatorResult toOperatorResult() { return operatorResult; }

    private AssessQuery parseQuery(String assessQuery) throws RecognitionException {
        AssessQueryParser parser = createParser(assessQuery);
        return parser.parse(new AssessQueryBuilder(cubeManager));
    }

    private AssessQueryParser createParser(String incomingExpression) {
        try {
            InputStream stream = new ByteArrayInputStream(incomingExpression.getBytes(StandardCharsets.UTF_8));
            ANTLRInputStream input = new ANTLRInputStream(stream);
            AssessQueryLexer lexer = new AssessQueryLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            return new AssessQueryParser(tokens);
        } catch (IOException e) {
            throw new RuntimeException("There was an error while creating the Assess Query parser");
        }
    }
}



/**
 * Renders an ASSESS {@link OperatorResult} and the highlights extracted from it to a Markdown report.
 * ASSESS-specific presentation (the comparisons and the labeling), kept out of the operator and off the
 * highlight pipeline: it consumes the operator's product and an already-extracted {@link HighlightSet}.
 */
final class AssessReport {

    private AssessReport() {}

    public static String write(String query, OperatorResult result, HighlightSet highlights, String outputName) {
        AssessModel model = (AssessModel) result.model(AssessModel.NAME);
        List<ComparedCell> comparedCells = model.getComparedCells();
        List<LabeledCell> labeledCells = model.getLabeledCells();

        File dir = new File("OutputFiles/assessments");
        dir.mkdirs();
        File out = new File(dir, outputName + ".md");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(out))) {
            writer.append("## Query\n").append(query).append("\n\n");

            writer.append("## Comparisons Made (")
                    .append(Integer.toString(comparedCells.size())).append(" in total)\n");
            for (ComparedCell comparedCell : comparedCells) {
                writer.append(comparedCell.toString()).append("\n\n");
            }

            writer.append("## Labeling Results (")
                    .append(Integer.toString(labeledCells.size())).append(" in total)\n");
            for (LabeledCell cell : labeledCells) {
                writer.append(cell.toString()).append("\n\n");
            }

            if (!highlights.isEmpty()) {
                writer.append("## Highlights\n");
                for (Highlight h : highlights.highlights()) {
                    writer.append("### ").append(h.toText()).append("\n");
                    if (h instanceof HolisticHighlight) {
                        for (ElementaryHighlight eh : ((HolisticHighlight) h).elementary) {
                            writer.append("- ").append(eh.toText()).append("\n");
                        }
                    }
                    writer.append("\n");
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to export to MarkDown");
        }
        return out.getPath();
    }
}
