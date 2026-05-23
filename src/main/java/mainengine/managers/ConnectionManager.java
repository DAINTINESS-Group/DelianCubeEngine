package mainengine.managers;

import java.util.HashMap;
import java.util.Map;

import chartManagement.ChartManager;
import cubemanager.CubeManager;
import cubemanager.cubebase.QueryHistoryManager;
import interestingnessengine.InterestingnessManager;
import mainengine.Session;

public class ConnectionManager implements IBuilder {

    @Override
    public ResponseDTO execute(RequestCTO cto) throws Exception {
        Map<String, Object> inputParams = (Map<String, Object>) cto.getInput();
        HashMap<String, String> userInputList = (HashMap<String, String>) inputParams.get("userInputList");
        String typeOfConnection = (String) inputParams.get("typeOfConnection");

        CubeManager cubeManager = new CubeManager(typeOfConnection, userInputList);
        Session session = new Session(cubeManager);
        String sessionId = session.initialize(typeOfConnection, userInputList);
        QueryHistoryManager queryHistoryMng = new QueryHistoryManager(sessionId);

        SessionContext context = new SessionContext();
        context.setCubeManager(cubeManager);
        context.setSession(session);
        context.setSessionId(sessionId);
        context.setQueryHistoryMng(queryHistoryMng);
        context.setSchemaName(userInputList.get("schemaName"));
        context.setConnectionType(typeOfConnection);
        context.setRegisteredDimensions(cubeManager.getDimensions());
        context.setRegisteredCubesList(cubeManager.getCubes());
		context.setInputFolder(userInputList.get("inputFolder"));
		context.setCubeName(userInputList.get("cubeName"));

        switch (cto.getCommandAlias()) {
	        case "initialize":
	            System.out.println("DONE WITH INIT CENTRAL (Manager)");
	            break;
	
	        case "initialize_with_interests":
	            String historyFolder = (String) inputParams.get("historyFolder");
	            String expValuesFolder = (String) inputParams.get("expValuesFolder");
	            String expLabelsFolder = (String) inputParams.get("expLabelsFolder");
	            String beliefFolder = (String) inputParams.get("beliefFolder");
	            int k = (int) inputParams.get("k");
	
	            InterestingnessManager interestMng;
	            if(historyFolder.equals("") && expValuesFolder.equals("") && expLabelsFolder.equals("") && beliefFolder.equals("")) {
	                interestMng = new InterestingnessManager(session.getCubeManager(), k);
	            } else {
	                interestMng = new InterestingnessManager(historyFolder, expValuesFolder, expLabelsFolder, beliefFolder, session.getCubeManager(), k);
	            }
	            
	            context.setInterestMng(interestMng);
	            
	            System.out.println("DONE WITH INIT INTERESTS (Manager)");
	            break;
	
	        case "initialize_chart":
	            ChartManager chartManager = new ChartManager(session.getCubeManager());
	           
	            context.setChartManager(chartManager);
	            
	            System.out.println("DONE WITH INIT CHART (Manager)");
	            break;
	
	        default:
	            throw new IllegalArgumentException("Unknown Init Command: " + cto.getCommandAlias());
        }

        ResponseDTO response = new ResponseDTO(true); 
        response.setPayload(context);                 
        return response;
    }
}