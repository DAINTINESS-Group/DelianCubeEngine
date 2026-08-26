package intentional.analyze;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cubemanager.CubeManager;
import intentional.analyze.AnalyzeOperatorOptimizer.AnalyzeStrategy;
import intentional.analyze.optimizer.AnalyzeOperatorOptimizerQueryGenerator;
import intentional.operator.IntentionalOperator;
import intentional.operator.IntentionalOperatorFactory;
import intentional.operator.IntentionalOperatorType;
import intentional.operator.IntentionalStrategy;
import intentional.result.LabeledResult;
import mainengine.managers.IntentionalPipeline;
import mainengine.managers.IntentionalProfile;
import result.ResultFileMetadata;

public class AnalyzeManager {
	
private CubeManager cubeManager;

	private String incomingExpression;
	
	private String schemaName;
	
	private String connectionType;
	
	private AnalyzeOperatorOptimizer analyzeOperatorOptimizer;
	
	private List<AnalyzeQuery> analyzeQueriesForEstimation;
			
	private AnalyzeOperatorOptimizerQueryGenerator analyzeOperatorOptimizerQueryGenerator;
	
	public AnalyzeManager(String incomingExpression, CubeManager cubeManager, String schemaName, String connectionType) {
		this.incomingExpression = incomingExpression;
		this.cubeManager = cubeManager;
		this.schemaName = schemaName;
		this.connectionType = connectionType;
		this.analyzeOperatorOptimizerQueryGenerator = new AnalyzeOperatorOptimizerQueryGenerator(incomingExpression, cubeManager, schemaName, connectionType);
		this.analyzeOperatorOptimizer = setupAnalyzeOperatorOptimizer();
	}
	
	public AnalyzeOperatorOptimizer getAnalyzeOperatorOptimizer() {
		return analyzeOperatorOptimizer;
	}
	
	public List<AnalyzeQuery> getAnalyzeQueriesForEstimation(){
		return analyzeQueriesForEstimation;
	}
	
	private boolean constructQueries() {
		boolean incomingExpressionIsValid;

		incomingExpressionIsValid = this.analyzeOperatorOptimizerQueryGenerator.validateIncomingExpression();
		if(incomingExpressionIsValid) {
			analyzeQueriesForEstimation = analyzeOperatorOptimizerQueryGenerator.translateToAnalyzeQueries();
			return true;
		}else {
			System.err.println("ANALYZE incoming expression contains syntax errors!Please check.");
			return false;
		}
	}
	
	private AnalyzeOperatorOptimizer setupAnalyzeOperatorOptimizer() {
		constructQueries();
		AnalyzeOperatorOptimizer analyzeOperatorOptimizer = new AnalyzeOperatorOptimizer(cubeManager, analyzeQueriesForEstimation);
		return analyzeOperatorOptimizer;
	}
	
	public ResultFileMetadata optimize() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("schemaName", schemaName);
		params.put("connectionType", connectionType);
		
		IntentionalOperatorFactory intentionalOperatorFactory = new IntentionalOperatorFactory();
		
		long startTime = System.nanoTime();
		IntentionalStrategy intentionalStrategy = this.analyzeOperatorOptimizer.decideMQOAlgorithmWithIndependenceAssumption();
		long endTime = System.nanoTime();
		double totalTimeInMs = (double)(endTime - startTime)/1000000;
    	System.out.println("%%Total Optimizer execution time: " + totalTimeInMs);
    	
		IntentionalOperator optimalAnalyzeOperator = intentionalOperatorFactory.build(IntentionalOperatorType.ANALYZE, intentionalStrategy, incomingExpression, cubeManager, params);
		ResultFileMetadata result = IntentionalPipeline.run(optimalAnalyzeOperator, incomingExpression, IntentionalProfile.forType(IntentionalOperatorType.ANALYZE), cubeManager);

    	
		return result;
	}
}
