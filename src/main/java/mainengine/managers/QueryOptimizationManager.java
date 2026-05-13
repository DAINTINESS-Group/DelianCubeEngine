package mainengine.managers;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import cubemanager.queryoptimizer.IQueryOptimization;

import java.util.HashMap;
import java.util.Map;

/**
 * Retrieves the strategy from the request parameters and delegates the optimization to it
 */
public class QueryOptimizationManager implements IBuilder{

	@Override
	public ResponseDTO execute(RequestCTO cto) throws Exception {
		Map<String, Object> inputParams = (Map<String, Object>) cto.getInput();
		String queryString = (String) inputParams.get("queryString");
		IQueryOptimization queryOptimizer = (IQueryOptimization) inputParams.get("optimizer");

		CubeManager cubeManager = cto.getCubeManager();

		CubeQuery query = cubeManager.createCubeQueryFromString(queryString, new HashMap<String, String>());

		Object result = queryOptimizer.optimize(query);

		ResponseDTO response = new ResponseDTO(true);
		response.setPayload(result);
		return response;
	}
}
