package mainengine.managers;

import cubemanager.CubeManager;
import intentional.analyze.AnalyzeOperatorOptimizer;
import intentional.analyze.AnalyzeOperatorOptimizer.AnalyzeStrategy;
import intentional.analyze.optimizer.AnalyzeOperatorOptimizerQueryGenerator;

public class OptimizationManager implements IBuilder{

	@Override
	public ResponseDTO execute(RequestCTO cto) throws Exception {
		CubeManager cubeManager = cto.getCubeManager();
		String incomingExpression = (String) cto.getInput();
		String commandAlias = cto.getCommandAlias();
		AnalyzeStrategy analyzeStrategy = null;
		
		switch(commandAlias) {
			case "analyze":
				analyzeStrategy = (AnalyzeStrategy) executeAnalyzeOptimizer(incomingExpression, cubeManager);
		}
		
		ResponseDTO dto = new ResponseDTO(true);
		dto.setPayload(analyzeStrategy);
		return dto;
	}
	
	public Object executeAnalyzeOptimizer(String incomingExpression, CubeManager cubeManager) {
		AnalyzeOperatorOptimizerQueryGenerator analyzeOperatorOptimizerQueryGenerator = new AnalyzeOperatorOptimizerQueryGenerator(incomingExpression, cubeManager, cubeManager.getSchemaName(), cubeManager.getTypeOfConnection());
		AnalyzeOperatorOptimizer analyzeOperatorOptimizer = new AnalyzeOperatorOptimizer(cubeManager, analyzeOperatorOptimizerQueryGenerator);
		AnalyzeStrategy analyzeStrategy = analyzeOperatorOptimizer.decideMQOAlgorithmWithIndependenceAssumption();
		return analyzeStrategy;
	}

}
