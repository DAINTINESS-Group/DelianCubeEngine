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
                DescribeOperator descOp = new DescribeOperator(cto.getCubeManager());
                results = descOp.executeToReport(query);
                break;

            case "assess":
                AssessOperator assessOp = new AssessOperator(cto.getCubeManager());
                results = assessOp.executeToReport(query);
                break;

            case "analyze_iakovidis":
                AnalyzeOperatorByIakovidis iakOp = new AnalyzeOperatorByIakovidis(
                    query, 
                    cto.getCubeManager(), 
                    (String) params.get("schemaName"), 
                    (String) params.get("connectionType")
                );
                results = iakOp.execute();
                break;

            case "analyze_min_mqo":
                AnalyzeTranslationManager minTrans = new AnalyzeTranslationManager(
                    query, 
                    cto.getCubeManager(), 
                    (String) params.get("schemaName"), 
                    (String) params.get("connectionType")
                );
                AnalyzeOperatorMinMultiQueryOptimizer minOp = new AnalyzeOperatorMinMultiQueryOptimizer(
                    query, 
                    cto.getCubeManager(), 
                    (String) params.get("connectionType"), 
                    minTrans
                );
                results = minOp.executeToReport(query);
                break;

            case "analyze_max_mqo":
                AnalyzeTranslationManager maxTrans = new AnalyzeTranslationManager(
                    query, 
                    cto.getCubeManager(), 
                    (String) params.get("schemaName"), 
                    (String) params.get("connectionType")
                );
                AnalyzeOperatorMaxMultiQueryOptimizer maxOp = new AnalyzeOperatorMaxMultiQueryOptimizer(
                    query, 
                    cto.getCubeManager(), 
                    (String) params.get("connectionType"), 
                    maxTrans
                );
                results = maxOp.executeAnalyzeWithMaxMQO();
                break;

            case "analyze_mid_mqo":
                AnalyzeTranslationManager midTrans = new AnalyzeTranslationManager(
                    query, 
                    cto.getCubeManager(), 
                    (String) params.get("schemaName"), 
                    (String) params.get("connectionType")
                );
                AnalyzeOperatorMidMultiQueryOptimizer midOp = new AnalyzeOperatorMidMultiQueryOptimizer(
                    query, 
                    cto.getCubeManager(), 
                    (String) params.get("connectionType"), 
                    midTrans
                );
                results = midOp.executeAnalyzeWithMidMQO();
                break;

            default:
                throw new IllegalArgumentException("Unknown Intentional Command: " + cto.getCommandAlias());
        }

        return new ResponseDTO(results);
    }

}