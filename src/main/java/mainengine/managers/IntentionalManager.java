package mainengine.managers;

import java.util.HashMap;
import java.util.Map;

import analyze.AnalyzeOperatorByIakovidis;
import analyze.AnalyzeOperatorMaxMultiQueryOptimizer;
import analyze.AnalyzeOperatorMidMultiQueryOptimizer;
import analyze.AnalyzeOperatorMinMultiQueryOptimizer;
import analyze.AnalyzeTranslationManager;
import assess.AssessOperator;
import describe.DescribeOperator;
import result.ResultFileMetadata;

public class IntentionalManager implements IBuilder {

    @Override
    public ResponseDTO execute(RequestCTO cto) throws Exception {
        String query = "";
        Map<String, Object> params = new HashMap<>();
        
        if (cto.getInput() instanceof String) {
            query = (String) cto.getInput();
        } else if (cto.getInput() instanceof Map) {
            params = (Map<String, Object>) cto.getInput();
            query = (String) params.getOrDefault("query", "");
        }

        ResultFileMetadata results = null;

        switch (cto.getCommandAlias().toLowerCase()) {
            
            case "describe":
                results = IntentionalPipeline.run(new DescribeOperator(cto.getCubeManager()),
                        query, IntentionalProfile.DESCRIBE, cto.getCubeManager());
                break;

            case "assess":
                results = IntentionalPipeline.run(new AssessOperator(cto.getCubeManager()),
                        query, IntentionalProfile.ASSESS, cto.getCubeManager());
                break;

            case "analyze_iakovidis":
                AnalyzeTranslationManager iakOpTrans = new AnalyzeTranslationManager(
                        query,
                        cto.getCubeManager(),
                        (String) params.get("schemaName"),
                        (String) params.get("connectionType")
                );
                AnalyzeOperatorByIakovidis iakOp = new AnalyzeOperatorByIakovidis(
                    cto.getCubeManager(), 
                    iakOpTrans
                );
                results = IntentionalPipeline.run(iakOp, query, IntentionalProfile.ANALYZE, cto.getCubeManager());
                break;

            case "analyze_min_mqo":
                AnalyzeTranslationManager minTrans = new AnalyzeTranslationManager(
                    query, 
                    cto.getCubeManager(), 
                    (String) params.get("schemaName"), 
                    (String) params.get("connectionType")
                );
                AnalyzeOperatorMinMultiQueryOptimizer minOp = new AnalyzeOperatorMinMultiQueryOptimizer(
                    cto.getCubeManager(),
                    minTrans
                );
                results = IntentionalPipeline.run(minOp, query, IntentionalProfile.ANALYZE, cto.getCubeManager());
                break;

            case "analyze_max_mqo":
                AnalyzeTranslationManager maxTrans = new AnalyzeTranslationManager(
                    query, 
                    cto.getCubeManager(), 
                    (String) params.get("schemaName"), 
                    (String) params.get("connectionType")
                );
                AnalyzeOperatorMaxMultiQueryOptimizer maxOp = new AnalyzeOperatorMaxMultiQueryOptimizer(
                    cto.getCubeManager(),
                    maxTrans
                );
                results = IntentionalPipeline.run(maxOp, query, IntentionalProfile.ANALYZE, cto.getCubeManager());
                break;

            case "analyze_mid_mqo":
                AnalyzeTranslationManager midTrans = new AnalyzeTranslationManager(
                    query,
                    cto.getCubeManager(),
                    (String) params.get("schemaName"),
                    (String) params.get("connectionType")
                );
                AnalyzeOperatorMidMultiQueryOptimizer midOp = new AnalyzeOperatorMidMultiQueryOptimizer(
                    cto.getCubeManager(),
                    midTrans
                );
                results = IntentionalPipeline.run(midOp, query, IntentionalProfile.ANALYZE, cto.getCubeManager());
                break;

            default:
                throw new IllegalArgumentException("Unknown Intentional Command: " + cto.getCommandAlias());
        }

        return new ResponseDTO(results);
    }

}