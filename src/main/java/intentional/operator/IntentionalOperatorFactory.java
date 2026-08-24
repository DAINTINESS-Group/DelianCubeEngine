package intentional.operator;

import java.util.Map;

import intentional.analyze.AnalyzeOperatorByIakovidis;
import intentional.analyze.AnalyzeOperatorMaxMQO;
import intentional.analyze.AnalyzeOperatorMidMQO;
import intentional.analyze.AnalyzeOperatorMinMQO;
import intentional.analyze.AnalyzeOperatorOptimizer;
import intentional.analyze.AnalyzeTranslationManager;
import intentional.assess.AssessOperator;
import cubemanager.CubeManager;
import intentional.describe.DescribeOperator;

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
                return new AnalyzeOperatorMinMQO(cubeManager, translation);
            case MAX_MQO:
                return new AnalyzeOperatorMaxMQO(cubeManager, translation);
            case MID_MQO:
                return new AnalyzeOperatorMidMQO(cubeManager, translation);
            default:
                throw new IllegalArgumentException("Unknown analyze strategy: " + strategy);
        }
    }
}
