/*
*    DelianCubeEngine. A simple cube query engine.
*    Copyright (C) 2018  Panos Vassiliadis
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU Affero General Public License as published
*    by the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU Affero General Public License for more details.
*
*    You should have received a copy of the GNU Affero General Public License
*    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*
*/


package mainengine;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.spark.sql.AnalysisException;

import chartManagement.utils.ChartResponse;
import chartRequestManagement.ChartRequest;

import cubemanager.cubebase.CubeQuery;
import decisiontree.labeling.RuleSet;
import intentional.operator.IntentionalStrategy;
import intentional.analyze.AnalyzeOperatorOptimizer.AnalyzeStrategy;
import intentional.operator.IntentionalOperatorType;
import mainengine.managers.Director;
import mainengine.managers.ManagerType;
import mainengine.managers.OptimizationProfile;
import mainengine.managers.RequestCTO;
import mainengine.managers.ResponseDTO;
import mainengine.managers.SessionContext;
import mainengine.rmiTransfer.RMIInputStream;
import mainengine.rmiTransfer.RMIInputStreamImpl;
import mainengine.rmiTransfer.RMIOutputStream;
import mainengine.rmiTransfer.RMIOutputStreamImpl;
import decisiontree.services.DatasetManager;
import result.Result;
import result.ResultFileMetadata;




/**
 * @author pvassil
 *
 * USed to be MainEngine in Cinecubes. Refactored to address the needs of SimpleOLAP Engine.
 * The class is the main server component of answering _cube_ queries.
 *
 * Use {@link #answerCubeQueriesFromFile(File file)} to answer the queries contained in a file
 */
//@SuppressWarnings("serial")
public class SessionQueryProcessorEngine extends UnicastRemoteObject implements IMainEngine {

	private static final long serialVersionUID = 313553263459485366L;
	private CubeQuery currentCubeQuery;
	private Result currentResult;
	private String currentQueryName;
	private SessionContext context;
	private Director director;

/**
 * Simple constructor for the class
 * @throws RemoteException
 */
	public SessionQueryProcessorEngine() throws RemoteException {
		super();
		currentResult = null;
		currentCubeQuery = null;
		currentQueryName = null;
		this.director = new Director();
		this.context = new SessionContext();
	}

	@Override
	public OutputStream getOutputStream(File f) throws IOException {
	    return new RMIOutputStream(new RMIOutputStreamImpl(new
	    FileOutputStream(f)));
	}
	@Override
	public InputStream getInputStream(File f) throws IOException {
	    return new RMIInputStream(new RMIInputStreamImpl(new FileInputStream(f)));
	}//end method
	
	public SessionContext getSessionContext() {
		return this.context;
	}


	/*
	 * TODO: at some point, connections must be initialized at the beginning and NOT on a per query basis!!
	 *
	 * (non-Javadoc)
	 * @see mainengine.IMainEngine#initializeConnection(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 
	 * Refactored into ConnectionManager
	 */
	@Override
	public void initializeConnection(String typeOfConnection, HashMap<String, String> userInputList) {
		delegateInit("initialize", typeOfConnection, userInputList, null);
	}

	public void initializeConnectionWithIntrMng(String typeOfConnection, HashMap<String, String> userInputList, String historyFolder,
			String expValuesFolder, String expLabelsFolder, String beliefFolder, int k){
		HashMap<String, Object> extras = new HashMap<>();
        extras.put("historyFolder", historyFolder);
        extras.put("expValuesFolder", expValuesFolder);
        extras.put("expLabelsFolder", expLabelsFolder);
        extras.put("beliefFolder", beliefFolder);
        extras.put("k", k);
        
        delegateInit("initialize_with_interests", typeOfConnection, userInputList, extras);
	}
	
	public void initializeConnectionWithChartQueryMng(String typeOfConnection, HashMap<String, String> userInputList, String historyFolder,
			String expValuesFolder, String expLabelsFolder, String beliefFolder, int k){
		delegateInit("initialize_chart", typeOfConnection, userInputList, null);
	}
	
	/**
	 * A unified helper method to delegate initialization request to the Director
	 * This method encapsulates the raw initialization parameters into a RequestCTO object. 
	 * 
	 * It performs the following steps:
	 * 1. Aggregates base connection arguments and optional extra settings into a single input map.
	 * 2. Dispatches the request to the Director.
	 * 3. Unwraps the resulting ResponseDTO to retrieve and update the local SessionContext
	 * 
	 * @param command, the specific command alias
	 * @param typeOfConnection, the type of the underlying DB connection
	 * @param userInputList, a map that contains essential user configuration
	 * @param extras, a map that contains additional parameters
	 */
	private void delegateInit(String command, String typeOfConnection, HashMap<String, String> userInputList, Map<String, Object> extras) {
		try {
            HashMap<String, Object> input = new HashMap<>();
            input.put("typeOfConnection", typeOfConnection);
            input.put("userInputList", userInputList);
            
            if (extras != null) {
                input.putAll(extras);
            }

            RequestCTO cto = new RequestCTO(ManagerType.CONNECTION, command, input, null);
            ResponseDTO dto = director.serve(cto);

            this.context = (SessionContext) dto.getPayload();
            System.out.println("\nInitialization Successful via ConnectionManager.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/* (non-Javadoc)
	 * @see mainengine.IMainEngine#answerCubeQueriesFromFile(java.io.File)
	 */
	@Override
	public ArrayList<String> answerCubeQueriesFromFile(File file) throws RemoteException {
		HashMap<String, Object> params = new HashMap<>();
        params.put("file", file);
        return (ArrayList<String>) delegateExecution("answer_from_file", params);
	}


	/**
	 * Refactored into QueryExecutionManager
	 * @see mainengine.managers.QueryExecutionManager#answerCubeQueryFromString(String, CubeManager, QueryHistoryManager)
	 */
	@Override
	public String answerCubeQueryFromString(String queryRawString) throws RemoteException{
		HashMap<String, Object> params = new HashMap<>();
		params.put("query", queryRawString);
		return (String) delegateExecution("answer_from_string", params);
	}//answerCubeQueryFromString


	/**
	 * Refactored into QueryExecutionManager
	 * @see mainengine.managers.QueryExecutionManager#answerCubeQueryFromStringWithMetadata(String, CubeManager, QueryHistoryManager)
	 */
	@Override
	public ResultFileMetadata answerCubeQueryFromStringWithMetadata(String queryRawString) throws RemoteException {
		HashMap<String, Object> params = new HashMap<>();
        params.put("query", queryRawString);
        return (ResultFileMetadata) delegateExecution("answer_with_metadata", params);
	}//answerCubeQueryFromStringWithMetadata


	/**
	 * Helper method to delegate execution requests to the Director
	 * 
	 * This method performs the following steps:
	 * 1. Injects common dependencies (like QueryHistoryManager) into the parameters
	 * 2. Calls the Director's method named serve(RequestCTO) to dispatch the request
	 * 3. Unpacks the ResponseDTO and updates the Facade's (SQP) local state
	 * 
	 * 
	 * @param command, the specific execution command alias
	 * @param params, the map of input parameters for the command
	 * @return the specific object required by the calling method
	 * @throws RemoteException
	 */
    private Object delegateExecution(String command, Map<String, Object> params) throws RemoteException {
        try {
        	
        	//Inject the dependency required
            params.put("historyManager", context.getQueryHistoryMng());
            
            //Create the Command object and dispatch to Director
            RequestCTO cto = new RequestCTO(ManagerType.EXECUTION, command, params, context.getCubeManager());
            ResponseDTO dto = director.serve(cto);
            
            if (dto.isSuccess()) {
                Map<String, Object> payload = (Map<String, Object>) dto.getPayload();
                
                this.currentCubeQuery = (CubeQuery) payload.get("currentCubeQuery");
                this.currentResult = (Result) payload.get("currentResult");
                if (this.currentCubeQuery != null) {
                    this.currentQueryName = this.currentCubeQuery.getName();
                }

                return payload.get("returnValue");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public void produceDecisionTree(String queryName, String path, RuleSet ruleSet) throws RemoteException, AnalysisException {
		DatasetManager datasetManager = new DatasetManager();
		datasetManager.registerDataset(queryName, path);
		datasetManager.computeLabeledColumn(ruleSet);
		
		try {
			datasetManager.computeProfileOfDataset();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	//OLAP BASIC OPERATORS METHODS
	@Override
	public ResultFileMetadata rollUp(String oldQueryName, String newQueryName, String dimensionName, String targetLevelName) throws RemoteException {
        return delegateOlap("rollup", oldQueryName, newQueryName, dimensionName, targetLevelName);
    }

    @Override
    public ResultFileMetadata drillDown(String oldQueryName, String newQueryName, String dimensionName, String targetLevelName) throws RemoteException {
        return delegateOlap("drilldown", oldQueryName, newQueryName, dimensionName, targetLevelName);
    }

    
    /**
     * Helper method to bundle arguments and dispatch OLAP requests to the Director
     * This method acts as a bridge between the Facade and the OLAPManager
     * 
     * It performs the following actions:
     * 1. Bundles the raw String arguments into a parameter map
     * 2. Injects the necessary dependences 
     * 3. Creates a RequestCTO setting the manager type as OLAP and dispatches the request via the Director's method named serve(RequestCTO)
     * 
     * 
     * @param commandAlias, the specific OLAP command
     * @param oldQ
     * @param newQ
     * @param dim
     * @param level
     * @return the metadata of the operation result
     */
    
    private ResultFileMetadata delegateOlap(String commandAlias, String oldQ, String newQ, String dim, String level) {
        try {
            //Pack Arguments into a Map
            HashMap<String, Object> inputParams = new HashMap<>();
            inputParams.put("oldQueryName", oldQ);
            inputParams.put("newQueryName", newQ);
            inputParams.put("dimensionName", dim);
            inputParams.put("targetLevelName", level);
            
            //Inject dependencies required by OLAPManager
            inputParams.put("historyManager", context.getQueryHistoryMng());

            //Create the Request
            RequestCTO cto = new RequestCTO(
                ManagerType.OLAP, 
                commandAlias, 
                inputParams, 
                context.getCubeManager()
            );

            //Execute via Director
            ResponseDTO dto = director.serve(cto);

            return dto.getMetadata();
            

        } catch (Exception e) {
            e.printStackTrace();
            ResultFileMetadata err = new ResultFileMetadata();
            err.setErrorCheckingStatus("System Error: " + e.getMessage());
            return err;
        }
    }

	//INTENTIONAL OPERATORS METHODS
	public ResultFileMetadata analyzeByIakovidis(String incomingExpression) throws RemoteException {
        return dispatchAnalyze(IntentionalStrategy.IAKOVIDIS.alias, incomingExpression);
    }

    @Override
    public ResultFileMetadata analyzeWithMinMQO(String incomingExpression) throws RemoteException {
        return dispatchAnalyze(IntentionalStrategy.MIN_MQO.alias, incomingExpression);
    }

    @Override
    public ResultFileMetadata analyzeWithMaxMQO(String incomingExpression) throws RemoteException {
        return dispatchAnalyze(IntentionalStrategy.MAX_MQO.alias, incomingExpression);
    }

    @Override
    public ResultFileMetadata analyzeWithMidMQO(String incomingExpression) throws RemoteException {
        return dispatchAnalyze(IntentionalStrategy.MID_MQO.alias, incomingExpression);
    }
    
    @Override
    public ResultFileMetadata analyzeWithOptimizer(String incomingExpression) throws Exception{
    	ResponseDTO dto = delegateOptimizer(ManagerType.OPTIMIZATION, OptimizationProfile.ANALYZE.alias, incomingExpression);
    	return dto.getMetadata();
    }
    
    private ResponseDTO delegateOptimizer(ManagerType managerType, String commandAlias, String input) throws Exception {
    	RequestCTO cto = new RequestCTO(managerType, commandAlias, input, context.getCubeManager());
    	ResponseDTO dto = director.serve(cto);
    	return dto;
    }

	@Override
	public ResultFileMetadata assess(String incomingExpression) throws Exception {
		return dispatch(ManagerType.INTENTIONAL, IntentionalOperatorType.ASSESS.alias, incomingExpression);
	}

	@Override
	public ResultFileMetadata describe(String incomingExpression) throws Exception{
		System.out.println("Received Describe request: " + incomingExpression);
		return dispatch(ManagerType.INTENTIONAL, IntentionalOperatorType.DESCRIBE.alias, incomingExpression);
	}
	
	/**
	 * Generic helper method to dispatch Intentional requests to the Director
	 * Creates a RequestCTO setting the manager type as INTENTIONAL and dispatches the request via the Director's method named serve(RequestCTO)
	 * AND assumes the result payload is always type of ResultFileMetdata 
	 * 
	 * @param type, the target ManagerType, in this case INTENTIONAL
	 * @param commandAlias, the specific command
	 * @param input, the input payload
	 * @return the metadata result extracted from the ResponseDTO
	 * @throws Exception
	 */
	private ResultFileMetadata dispatch(ManagerType type, String commandAlias, Object input) throws Exception {
        RequestCTO cto = new RequestCTO(type, commandAlias, input, context.getCubeManager());
        
        ResponseDTO dto = director.serve(cto);
        return dto.getMetadata();
    }

	/**
	 * Specialized helper method for dispatching Analyze requests
	 * Unlike simple dispatching the analyze operations require additional context about the session to 
	 * function properly. This method injects the schema name and the connection type from the SessionContext into the
	 * parameters map before dispatching
	 * 
	 * @param alias, the specific analyze strategy
	 * @param query, the query expression to analyze
	 * @return a ResultFileMetadata object with the results
	 * 
	 * NOTE: Could just be one dispatch method instead of having 2 separate ones
	 */
	private ResultFileMetadata dispatchAnalyze(String alias, String query) {
		try {
		    HashMap<String, Object> input = new HashMap<>();
		    input.put("query", query);
			input.put("schemaName", context.getSchemaName());       
			input.put("connectionType", context.getConnectionType()); 
		    
		    return dispatch(ManagerType.INTENTIONAL, alias, input);
		} catch (Exception e) {
		    e.printStackTrace();
		    return new ResultFileMetadata();
		}
	}

	//NATURAL LANGUAGE QUERYING METHODS
	  
	/**Gets a natural language query as a string, analyzes it, produces a cube query,
	 * executes the query and produces the output of the query.
	 *
	 * The idea is:
	 * 1. Parse and analyze the natural language query via <code>analyzeNLQuery</code> of <code>NLTranslator</code>, see {@link NLTranslator}.
	 * 2. Produces a String with the cube query.
	 * 3. Calls <code>answerCubeQueryFromString</code> with this String as a parameter.
	 * 4. Returns the results in a file with the name of the query and outputs them to the console.
	 *
	 * @param queryRawString a String with the natural language query
	 * @return a String containing the location of output file at the server
	 * @author DimosGkitsakis
	 *
	 * Refactored into NLManager 
	 */
	@Override
	public String answerCubeQueryFromNLString(String queryRawString) throws RemoteException{
		return (String) delegateNL("answer_nl_string", queryRawString);
	}//answerCubeQueryFromNLString



	/**
	 * Same idea as the {@link #answerCubeQueryFromStringWithMetadata(String)}, but for a natural language query instead of cube query.
	 * Gets the natural language query from a string, executes it and produces the output of a query as a ResultFileMetadata object.
	 * Result produced via {@link #answerCubeQueryFromNLString(String)} instead of {@link #answerCubeQueryFromString(String)}.
	 *
	 * @param queryRawString a String with the natural language query
	 * @return a ResultFileMetadata containing info on the location of output files at the server
	 *
	 * Refactored into NLManager 
	 */
	@Override
	public ResultFileMetadata answerCubeQueryFromNLStringWithMetadata(String queryRawString) throws RemoteException {
		return (ResultFileMetadata) delegateNL("answer_nl_metadata", queryRawString);
	}//answerCubeQueryFromNLStringWithMetadata

	/**
	 * Validates a natural language Query String and returns a ResultFileMetadata object with the result.
	 *
	 * Gets the natural language query from a string, passes it to a NLTranslator object, which parses it and produces error messages if any errors where found.
	 * The errors are returned to the client as a ErrorChecking.txt file, so the client can produce the error message to the end-user.
	 *
	 *@author DimosGkitsakis
	 *@param naturalLanguageQueryString A String with the natural language query given by the user.
	 *@return a ResultFileMetadata object containing info on the location of the error checking file at the server
	 *
	 *Refactored into NLManager 
	 *
	 */
	
	@Override
	public ResultFileMetadata prepareCubeQuery(String naturalLanguageQueryString) throws RemoteException {
		return (ResultFileMetadata) delegateNL("prepare_nl_query", naturalLanguageQueryString);
	}
	
	
	/**
	 * Helper to delegate Natural Language (NL) processing requests.
	 * This method acts as a bridge between the Facade and the NLManager. It injects a reference of the Director 
	 * into the parameters because the NLManager needs to perform a two-step process:
	 * 1. Translate the NL String into a Cube Query
	 * 2. Call the Director (using the injected reference) to execute the translated Cube Query  
	 *
	 * @param command, the specific NL command alias
	 * @param query, the raw Natural Language query string
	 * @return the generic payload from the ResponseDTO
	 */
    private Object delegateNL(String command, String query) {
        try {
            HashMap<String, Object> input = new HashMap<>();
            input.put("query", query);
            input.put("historyManager", context.getQueryHistoryMng());

            RequestCTO cto = new RequestCTO(ManagerType.NL, command, input, context.getCubeManager());
            ResponseDTO dto = director.serve(cto);

            return dto.getPayload();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    }


	//INTERESTINGNESS METHODS
    @Override
    public String[] answerCubeQueryWithInterestMeasures(String queryString, List<String> measures) throws RemoteException {
        HashMap<String, Object> params = new HashMap<>();
        params.put("query", queryString);
        params.put("measures", measures);
        params.put("interestManager", context.getInterestMng()); 
        
        Object result = delegateExecution("answer_with_interest_measures", params);
        return (result != null) ? (String[]) result : new String[0];
    }

    @Override
    public String[] answerCubeQueryWithInterestMeasures(String queryString, String queryString1, List<String> measures) throws RemoteException {
        HashMap<String, Object> params = new HashMap<>();
        params.put("query1", queryString);
        params.put("query2", queryString1);
        params.put("measures", measures);
        params.put("interestManager", context.getInterestMng());

        Object result = delegateExecution("answer_queries_with_interest_measures", params);
        return (result != null) ? (String[]) result : new String[0];
    }

	//Chart Query Methods
	@Override
    public ResultFileMetadata answerCubeQueryFromChartRequest(ChartRequest chartRequest) throws Exception {
        return (ResultFileMetadata) delegateChart("answer_chart_metadata", chartRequest);
    }

    @Override
    public ChartResponse answerCubeQueryFromChartRequestAndReturnAsChartResponse(ChartRequest chartRequest) throws Exception {
        return (ChartResponse) delegateChart("answer_chart_response", chartRequest);
    }

    /**
     * Helper method to delegate Visualization/Chart requests
     * This methods encapsulates the chart related parameters and context into a RequestCTO
     * targeting the Visualization subsystem
     * It injects the required (chartManager, schema name, etc..) because the VisualizationManager requires context to build queries and format results correctly
     * 
     * @param command, the specific visualization command alias
     * @param chartRequest, the payload provided 
     * @return the payload from the ResponseDTO which is either ResultFileMetadata or ChartResponse depending on the command
     * @throws Exception
     */
    private Object delegateChart(String command, ChartRequest chartRequest) throws Exception {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("chartRequest", chartRequest);
            params.put("chartManager", context.getChartManager());
            params.put("schemaName", context.getSchemaName());
            params.put("connectionType", context.getConnectionType());

            RequestCTO cto = new RequestCTO(ManagerType.VISUALIZATION, command, params, context.getCubeManager());
            ResponseDTO dto = director.serve(cto);

            return dto.getPayload();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


}//end class
