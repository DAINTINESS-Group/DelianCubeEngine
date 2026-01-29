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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.spark.sql.AnalysisException;

import chartManagement.ChartManager;
import chartManagement.utils.ChartResponse;
import chartRequestManagement.ChartRequest;

import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.QueryHistoryManager;
import model.decisiontree.labeling.RuleSet;
import cubemanager.CubeManager;
import interestingnessengine.InterestingnessManager;
import mainengine.managers.Director;
import mainengine.managers.ManagerType;
import mainengine.managers.RequestCTO;
import mainengine.managers.ResponseDTO;
import mainengine.managers.SessionContext;
import mainengine.nlq.ITranslatorFactory;
import mainengine.rmiTransfer.RMIInputStream;
import mainengine.rmiTransfer.RMIInputStreamImpl;
import mainengine.rmiTransfer.RMIOutputStream;
import mainengine.rmiTransfer.RMIOutputStreamImpl;
import model.decisiontree.services.DatasetManager;
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
            System.out.println("Initialization Successful via ConnectionManager.");
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


	@Override
	public ResultFileMetadata answerCubeQueryFromStringWithModels(String queryRawString, String[] modelsToGenerate)
			throws RemoteException {

		HashMap<String, Object> params = new HashMap<>();
        params.put("query", queryRawString);
        params.put("models", modelsToGenerate);
        return (ResultFileMetadata) delegateExecution("answer_with_models", params);
	}//end method answerCubeQueryFromStringWithModels
	
	
	//Helper method to call the Director and update local state
    private Object delegateExecution(String command, Map<String, Object> params) throws RemoteException {
        try {
            params.put("historyManager", context.getQueryHistoryMng());
            
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

    
    //Helper method to bundle arguments and send them to the Director -> OLAPManager
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
            inputParams.put("director", this.director);

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
        return dispatchAnalyze("analyze_iakovidis", incomingExpression);
    }

    @Override
    public ResultFileMetadata analyzeWithMinMQO(String incomingExpression) throws RemoteException {
        return dispatchAnalyze("analyze_min_mqo", incomingExpression);
    }

    @Override
    public ResultFileMetadata analyzeWithMaxMQO(String incomingExpression) throws RemoteException {
        return dispatchAnalyze("analyze_max_mqo", incomingExpression);
    }

    @Override
    public ResultFileMetadata analyzeWithMidMQO(String incomingExpression) throws RemoteException {
        return dispatchAnalyze("analyze_mid_mqo", incomingExpression);
    }
	
	@Override
	public ResultFileMetadata assess(String incomingExpression) throws Exception {
		return dispatch(ManagerType.INTENTIONAL, "assess", incomingExpression);
	}
	
	@Override
	public ResultFileMetadata describe(String incomingExpression) throws Exception{
		System.out.println("Received Describe request: " + incomingExpression);
		return dispatch(ManagerType.INTENTIONAL, "describe", incomingExpression);
	}
	
	//Helper methods
	private ResultFileMetadata dispatch(ManagerType type, String commandAlias, Object input) throws Exception {
        RequestCTO cto = new RequestCTO(type, commandAlias, input, context.getCubeManager());
        
        ResponseDTO dto = director.serve(cto);
        return dto.getMetadata();
    }

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
	
	
	//Helper to delegate NL operations.
    private Object delegateNL(String command, String query) {
        try {
            HashMap<String, Object> input = new HashMap<>();
            input.put("query", query);
            input.put("historyManager", context.getQueryHistoryMng());
            input.put("director", this.director);

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

    //Helper method to remove duplicate logic for Chart/Visualization calls.
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
