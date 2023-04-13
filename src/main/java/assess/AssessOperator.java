package assess;

import assess.syntax.AssessQueryLexer;
import assess.syntax.AssessQueryParser;
import cubemanager.CubeManager;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
    public HashMap<Double, String> execute(String assessQuery) throws RecognitionException {
        AssessQuery parsedQuery = parseQuery(assessQuery);
        List<Double> comparisonResults = executeComparison(parsedQuery);
        return labelResults(parsedQuery, comparisonResults);
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

    private List<Double> executeComparison(AssessQuery parsedQuery) {
        return parsedQuery.deltaFunction.compareTargetToBenchmark(
                cubeManager.executeQuery(parsedQuery.targetCubeQuery),
                parsedQuery.benchmark);
    }

    private HashMap<Double, String> labelResults(AssessQuery parsedQuery, List<Double> comparisonResults) {
        HashMap<Double, String> labeledResults = new HashMap<>();
        for(Double value: comparisonResults) {
            String label = parsedQuery.labelingScheme.applyLabels(value);
            labeledResults.put(value, label);
        }
        return labeledResults;
    }
}