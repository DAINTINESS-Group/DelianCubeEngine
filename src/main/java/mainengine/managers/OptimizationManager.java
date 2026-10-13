package mainengine.managers;

import java.util.HashMap;
import java.util.Map;

import cubemanager.CubeManager;
import intentional.analyze.AnalyzeManager;
import intentional.analyze.AnalyzeOperatorOptimizer;
import intentional.analyze.AnalyzeOperatorOptimizer.AnalyzeStrategy;
import intentional.analyze.optimizer.AnalyzeOperatorOptimizerQueryGenerator;
import result.ResultFileMetadata;

public class OptimizationManager implements IBuilder{

	@Override
	public ResponseDTO execute(RequestCTO cto) throws Exception {
		CubeManager cubeManager = cto.getCubeManager();
		String incomingExpression = (String) cto.getInput();
		String commandAlias = cto.getCommandAlias();
		ResultFileMetadata result= null;
		
		switch(commandAlias) {
			case "analyze":
				result = (ResultFileMetadata) executeAnalyzeOptimizer(incomingExpression, cubeManager);
		}
		
		ResponseDTO dto = new ResponseDTO(result);
		return dto;
	}
	
	public Object executeAnalyzeOptimizer(String incomingExpression, CubeManager cubeManager) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("schemaName", cubeManager.getSchemaName());
		params.put("connectionType", cubeManager.getTypeOfConnection());
		
		
		AnalyzeManager analyzeManager = new AnalyzeManager(incomingExpression, cubeManager, cubeManager.getSchemaName(), cubeManager.getTypeOfConnection());
		ResultFileMetadata results = analyzeManager.optimize();
		return results;
	}

}
