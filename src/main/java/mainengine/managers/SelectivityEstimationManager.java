package mainengine.managers;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import cubemanager.queryoptimizer.QueryOptimizer;
import cubemanager.queryoptimizer.selectivityestimation.SelectivityResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectivityEstimationManager implements IBuilder{

	@Override
	public ResponseDTO execute(RequestCTO cto) throws Exception {
		Map<String, Object> inputParams = (Map<String, Object>) cto.getInput();
		String queryString = (String) inputParams.get("queryString");
		QueryOptimizer queryOptimizer = (QueryOptimizer) inputParams.get("optimizer");

		CubeManager cubeManager = cto.getCubeManager();

		CubeQuery query = cubeManager.createCubeQueryFromString(queryString, new HashMap<String, String>());

		List<SelectivityResult> results = queryOptimizer.estimateSelectivity(query);

		ResponseDTO response = new ResponseDTO(true);
		response.setPayload(results);
		return response;
	}
}
