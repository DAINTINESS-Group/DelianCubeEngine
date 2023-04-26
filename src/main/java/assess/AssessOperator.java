package assess;

import assess.syntax.AssessQueryLexer;
import assess.syntax.AssessQueryParser;
import assess.utils.LabeledCell;
import cubemanager.CubeManager;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import result.Cell;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The top layer class for any assessments done in the intentional model.
 * Given that the CubeManager handles only one cube at a time, instances
 * of this class are created everytime we wish to change cubes.
 */
public class AssessOperator {
    private final CubeManager cubeManager;

    public AssessOperator(CubeManager cubeManager) {
        this.cubeManager = cubeManager;
    }

    /* Executes the provided query. Returns ?
     * @param assessQuery The user-provided query for assessment reasons
     * @return
     * @throws RecognitionException If the query does not follow the defined syntax
     */
    public List<LabeledCell> execute(String assessQuery) throws RecognitionException {
        AssessQuery parsedQuery = parseQuery(assessQuery);
        HashMap<Cell, Double> comparisonResults = executeComparison(parsedQuery);
        List<LabeledCell> results = labelResults(parsedQuery, comparisonResults);
        exportToMD(assessQuery, results);
        return results;
    }


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
            // No idea what could go wrong here.
            throw new RuntimeException("There was an error while creating the Assess Query parser");
        }
    }

    private HashMap<Cell, Double> executeComparison(AssessQuery parsedQuery) {
        return parsedQuery.deltaFunction.compareTargetToBenchmark(
                cubeManager.executeQuery(parsedQuery.targetCubeQuery),
                parsedQuery.benchmark);
    }

    private List<LabeledCell> labelResults(AssessQuery parsedQuery, HashMap<Cell, Double> comparisonResults) {
        List<LabeledCell> labeledCells = new ArrayList<>();
        for(Cell cell: comparisonResults.keySet()) {
            String label = parsedQuery.labelingScheme.applyLabels(comparisonResults.get(cell));
            labeledCells.add(new LabeledCell(cell, label));
        }
        return labeledCells;
    }

    private void exportToMD(String assessQuery, List<LabeledCell> results) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("OutputFiles/assessments/Assessment_Results.md"));
            writer.append("## Query\n");
            writer.append(assessQuery);


            writer.append("\n## Results");
            for(LabeledCell cell: results) {
                writer.append(cell.toString());
            }
            writer.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to export to MarkDown");
        }
    }
}