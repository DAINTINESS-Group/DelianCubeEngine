package intentional.operator;

import java.util.Map;

import analyze.AnalyzeOperatorByIakovidis;
import analyze.AnalyzeOperatorMaxMultiQueryOptimizer;
import analyze.AnalyzeOperatorMidMultiQueryOptimizer;
import analyze.AnalyzeOperatorMinMultiQueryOptimizer;
import analyze.AnalyzeTranslationManager;
import assess.AssessOperator;
import cubemanager.CubeManager;
import describe.DescribeOperator;

/**
 * Builds the {@link IntentionalOperator} for a command: describe and assess take only the cube manager,
 * while analyze builds its {@link AnalyzeTranslationManager} from the request parameters and selects the
 * variant by {@link IntentionalStrategy}.
 */
public class IntentionalOperatorFactory {

    public IntentionalOperator build(IntentionalOperatorType type, IntentionalStrategy strategy,
            String query, CubeManager cubeManager, Map<String, Object> params) {
        switch (type) {
            case DESCRIBE:
                return new DescribeOperator(cubeManager);
            case ASSESS:
                return new AssessOperator(cubeManager);
            case ANALYZE:
                return buildAnalyze(strategy, query, cubeManager, params);
            default:
                throw new IllegalArgumentException("Unknown intentional operator type: " + type);
        }
    }

    private IntentionalOperator buildAnalyze(IntentionalStrategy strategy, String query,
            CubeManager cubeManager, Map<String, Object> params) {
        AnalyzeTranslationManager translation = new AnalyzeTranslationManager(
                query, cubeManager,
                (String) params.get("schemaName"),
                (String) params.get("connectionType"));
        switch (strategy) {
            case IAKOVIDIS:
                return new AnalyzeOperatorByIakovidis(cubeManager, translation);
            case MIN_MQO:
                return new AnalyzeOperatorMinMultiQueryOptimizer(cubeManager, translation);
            case MAX_MQO:
                return new AnalyzeOperatorMaxMultiQueryOptimizer(cubeManager, translation);
            case MID_MQO:
                return new AnalyzeOperatorMidMultiQueryOptimizer(cubeManager, translation);
            default:
                throw new IllegalArgumentException("Unknown analyze strategy: " + strategy);
        }
    }
}
