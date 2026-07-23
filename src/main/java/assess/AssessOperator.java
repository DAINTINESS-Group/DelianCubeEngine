package assess;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import assess.models.AssessModel;
import assess.syntax.AssessQueryLexer;
import assess.syntax.AssessQueryParser;
import cubemanager.CubeManager;
import intentionaloperator.IntentionalOperator;
import labeling.LabeledResult;
import labeling.LabelingModel;

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
     * Parses the query, runs the {@link AssessModel} (benchmark + delta + labeling) over the cube data,
     * and returns the operator's product as a single-element list.
     *
     * @param assessQuery The user-provided query for assessment reasons
     */
    @Override
    public List<LabeledResult> execute(String assessQuery) {
        AssessQuery parsedQuery = parseQuery(assessQuery);

        AssessModel assessModel = new AssessModel(
                parsedQuery.benchmark, parsedQuery.deltaFunction, parsedQuery.labelingScheme,
                parsedQuery.targetCube);

        if (assessModel.compute() != 0) {
            throw new RuntimeException("No cells collected from the target cube query");
        }

        LabeledResult operatorResult = new LabeledResult(
                parsedQuery.targetCubeQuery, parsedQuery.targetCube,
                Collections.<LabelingModel>singletonList(assessModel));

        return Collections.singletonList(operatorResult);
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
