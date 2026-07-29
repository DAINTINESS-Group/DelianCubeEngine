package intentional.assess;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import cubemanager.CubeManager;
import intentional.assess.syntax.AssessQueryLexer;
import intentional.assess.syntax.AssessQueryParser;
import intentional.assess.utils.ComparedCell;
import intentional.labeling.Labeling;
import intentional.operator.IntentionalOperator;
import intentional.result.LabeledResult;
import result.Cell;

/**
 * The top layer class for any assessments done in the intentional model.
 * Given that the CubeManager handles only one cube at a time, instances
 * of this class are created everytime we wish to change cubes.
 */
public class AssessOperator implements IntentionalOperator {
    private final CubeManager cubeManager;

    public AssessOperator(CubeManager cubeManager) {
        this.cubeManager = cubeManager;
    }

    /**
     * Parses the query, compares the cube data against its benchmark and labels the deltas under the
     * query's scheme, returning the operator's product as a single-element list.
     *
     * @param assessQuery The user-provided query for assessment reasons
     */
    @Override
    public List<LabeledResult> execute(String assessQuery) {
        AssessQuery parsedQuery = parseQuery(assessQuery);

        LabeledResult operatorResult = new LabeledResult(
                parsedQuery.targetCubeQuery, parsedQuery.targetCube,
                Collections.singletonList(assess(parsedQuery)));

        return Collections.singletonList(operatorResult);
    }

    /**
     * Compares each target cell to the query's benchmark via its delta scheme and labels the deltas: the
     * labeling carries the delta as magnitude and the matched benchmark value as reference; cells without
     * a benchmark match stay unlabeled.
     */
    private static Labeling assess(AssessQuery query) {
        List<Cell> targetCells = query.targetCube.getCells();
        if (targetCells.isEmpty()) {
            throw new RuntimeException("No cells collected from the target cube query");
        }
        List<ComparedCell> comparedCells = new ArrayList<>();
        Map<Cell, Double> deltas = new LinkedHashMap<>(
                query.deltaFunction.compareTargetToBenchmark(targetCells, query.benchmark, comparedCells));

        Map<Cell, Double> benchmarkValues = new LinkedHashMap<>();
        for (ComparedCell compared : comparedCells) {
            if (compared.benchmark != null) {
                benchmarkValues.put(compared.target, compared.benchmark.toDouble());
            }
        }
        return new Labeling(query.labelingScheme, deltas, 0, benchmarkValues);
    }

    private AssessQuery parseQuery(String assessQuery) {
        AssessQueryParser parser = createParser(assessQuery);
        try {
            return parser.parse(new AssessQueryBuilder(cubeManager));
        } catch (RecognitionException e) {
            e.printStackTrace();
            return null;
        }
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
