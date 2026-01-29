package mainengine.managers;

import java.util.HashMap;
import java.util.Map;

import cubemanager.cubealgebra.DrillDownOperator;
import cubemanager.cubealgebra.RollUpOperator;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.QueryHistoryManager;
import result.ResultFileMetadata;

public class OLAPManager implements IBuilder{

	@Override
	public ResponseDTO execute(RequestCTO cto) throws Exception {
		Map<String, Object> inputParams = (Map<String, Object>) cto.getInput();
        
        QueryHistoryManager queryHistoryMng = (QueryHistoryManager) inputParams.get("historyManager");
        Director director = (Director) inputParams.get("director");
        
        String oldQueryName = (String) inputParams.get("oldQueryName");
        String newQueryName = (String) inputParams.get("newQueryName");
        String dimensionName = (String) inputParams.get("dimensionName");
        String targetLevelName = (String) inputParams.get("targetLevelName");
        
        ResultFileMetadata resMetadata = null;

        switch (cto.getCommandAlias().toLowerCase()) {
            case "rollup":
                resMetadata = performRollUp(queryHistoryMng, director, oldQueryName, newQueryName, dimensionName, targetLevelName, cto);
                break;
                
            case "drilldown":
                resMetadata = performDrillDown(queryHistoryMng, director, oldQueryName, newQueryName, dimensionName, targetLevelName, cto);
                break;
                
            default:
                throw new IllegalArgumentException("Unknown OLAP Command: " + cto.getCommandAlias());
        }

        return new ResponseDTO(resMetadata);
    }
	
	private ResultFileMetadata performRollUp(QueryHistoryManager queryHistoryMng, Director director, String oldQueryName, String newQueryName, String dimensionName, String targetLevelName, RequestCTO contextCto) throws Exception{
		CubeQuery oldCubeQuery = queryHistoryMng.getQueryByName(oldQueryName);
		
		RollUpOperator operator = new RollUpOperator(oldCubeQuery);

		//1. Perform the Roll-Up
		ResultFileMetadata resMetadata = operator.executeRollUp(oldQueryName, newQueryName, dimensionName, targetLevelName);
		if(resMetadata.getErrorCheckingStatus()==null) {
			CubeQuery newQuery = operator.getRollUpCubeQuery();
			System.out.println("Roll-Up Cube Query:\n"+newQuery.toString());
			
			//2. Execute the Roll-Up Query
			resMetadata = executeGeneratedQuery(director, newQuery.toString(), queryHistoryMng, contextCto);
		}
		return resMetadata;
	}
	
	private ResultFileMetadata performDrillDown(QueryHistoryManager queryHistoryMng, Director director, String oldQueryName, String newQueryName, String dimensionName, String targetLevelName, RequestCTO contextCto) throws Exception{
		CubeQuery oldCubeQuery = queryHistoryMng.getQueryByName(oldQueryName);
		DrillDownOperator operator = new DrillDownOperator(oldCubeQuery);

		//1. Perform the Drill-Down
		ResultFileMetadata resMetadata = operator.executeDrillDown(oldQueryName, newQueryName, dimensionName, targetLevelName);
		if(resMetadata.getErrorCheckingStatus()==null) {
			CubeQuery newQuery= operator.getDrillDownCubeQuery();
			System.out.println("Drill-Down Cube Query:\n"+newQuery.toString());
			
			//2. Execute the Drill-Down Query
			resMetadata = executeGeneratedQuery(director, newQuery.toString(), queryHistoryMng, contextCto);
		}
		return resMetadata;
		
	}
	
	//Helper method to call the answerCubeQueryFromStringWithMetadata from the QueryExecutionManager
	private ResultFileMetadata executeGeneratedQuery(Director director, String queryString, QueryHistoryManager historyManager, RequestCTO contextCto) throws Exception {
        Map<String, Object> execParams = new HashMap<>();
        execParams.put("query", queryString);
        execParams.put("historyManager", historyManager); 

        RequestCTO execRequest = new RequestCTO(
            ManagerType.EXECUTION, 
            "answer_with_metadata", 
            execParams, 
            contextCto.getCubeManager()
        );
        
        ResponseDTO response = director.serve(execRequest);

        if (response.isSuccess()) {
            Map<String, Object> payload = (Map<String, Object>) response.getPayload();
            return (ResultFileMetadata) payload.get("returnValue");
        } else {
            return createError("Execution of generated OLAP query failed: " + response.getError());
        }
    }
    
    private ResultFileMetadata createError(String msg) {
        ResultFileMetadata err = new ResultFileMetadata();
        err.setErrorCheckingStatus(msg);
        return err;
    }
}
