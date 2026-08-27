package intentional.assess;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import cubemanager.CubeManager;
import intentional.assess.fetch.FetchStats;
import intentional.assess.fetch.FetchStrategy;
import intentional.assess.syntax.AssessQueryLexer;
import intentional.assess.syntax.AssessQueryParser;
import intentional.model.ModelResult;
import intentional.operator.IntentionalOperator;
import intentional.result.LabeledResult;

/**
 * The top layer class for any assessments done in the intentional model.
 * Given that the CubeManager handles only one cube at a time, instances
 * of this class are created everytime we wish to change cubes.
 */
public class AssessOperator implements IntentionalOperator {
    private final CubeManager cubeManager;
    private final FetchStrategy fetchStrategy;
    private FetchStats lastFetchStats;

    public AssessOperator(CubeManager cubeManager) {
        this(cubeManager, FetchStrategy.SCAN_PER_SLICE);
    }

    public AssessOperator(CubeManager cubeManager, FetchStrategy fetchStrategy) {
        this.cubeManager = cubeManager;
        this.fetchStrategy = fetchStrategy;
    }

    /** The scans the last executed query's cubes cost. */
    public FetchStats lastFetchStats() {
        return lastFetchStats;
    }

    /**
     * Parses the query and runs one comparison of the target cube per benchmark, returning the operator's
     * product as a single-element list. Without an AGAINST clause a single benchmark-less comparison runs.
     *
     * @param assessQuery The user-provided query for assessment reasons
     */
    @Override
    public List<LabeledResult> execute(String assessQuery) {
        AssessQuery parsedQuery = parseQuery(assessQuery);
        lastFetchStats = parsedQuery.fetchStats;

        LabeledResult target = new LabeledResult(parsedQuery.targetCubeQuery, parsedQuery.targetCube,
                Collections.<ModelResult>emptyList());
        for (AssessComparison comparison : parsedQuery.comparisons) {
            new ComparisonModel(comparison.benchmark, comparison.benchmarkLabel, comparison.delta,
                    parsedQuery.labelingScheme).run(target);
        }
        return Collections.singletonList(target);
    }

    private AssessQuery parseQuery(String assessQuery) {
        AssessQueryParser parser = createParser(assessQuery);
        try {
            return parser.parse(new AssessQueryBuilder(cubeManager, fetchStrategy));
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
