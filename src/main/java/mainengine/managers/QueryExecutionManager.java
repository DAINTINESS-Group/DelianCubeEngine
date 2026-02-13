package mainengine.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.QueryHistoryManager;
import interestingnessengine.InterestingnessManager;
import mainengine.ModelManager;
import mainengine.ModelSelector;
import result.Result;
import result.ResultFileMetadata;
import setup.ModeOfWork;
import setup.ModeOfWork.WorkMode;

public class QueryExecutionManager implements IBuilder {

    private CubeQuery currentCubeQuery;
    private Result currentResult;
    private String currentQueryName;
    private int historyCounter;

    @Override
    public ResponseDTO execute(RequestCTO cto) throws Exception {
        Map<String, Object> inputParams = (Map<String, Object>) cto.getInput();
        CubeManager cubeManager = cto.getCubeManager();
        QueryHistoryManager historyManager = (QueryHistoryManager) inputParams.get("historyManager");
        InterestingnessManager interestManager = (InterestingnessManager) inputParams.get("interestManager");
        
        Object resultPayload = routeCommand(cto.getCommandAlias(), inputParams, cubeManager, historyManager, interestManager);
        
        return createResponse(resultPayload);
    }
    
    private Object routeCommand(String command, Map<String, Object> params, CubeManager cubeManager, QueryHistoryManager historyManager, InterestingnessManager interestManager) throws Exception {
        switch (command) {
            case "answer_from_string":
            	return answerCubeQueryFromString((String) params.get("query"), cubeManager, historyManager);
            case "answer_with_metadata":
            	return answerCubeQueryFromStringWithMetadata((String) params.get("query"), cubeManager, historyManager);
            case "answer_from_file":
            	return answerCubeQueriesFromFile((File) params.get("file"), cubeManager, historyManager);
            case "answer_with_models":
            	return answerCubeQueryFromStringWithModels((String) params.get("query"), (String[]) params.get("models"), cubeManager, historyManager);
            case "answer_with_interest_measures":
            	if (interestManager == null) throw new Exception("InterestManager is null in parameters");
                return computeMeasuresSingle((String) params.get("query"), (List<String>) params.get("measures"), cubeManager, historyManager, interestManager);
            case "answer_queries_with_interest_measures":
            	if (interestManager == null) throw new Exception("InterestManager is null in parameters");
                return computeMeasuresComparison((String) params.get("query1"), (String) params.get("query2"), (List<String>) params.get("measures"), cubeManager, historyManager, interestManager);
            default:
                throw new IllegalArgumentException("Unknown Command: " + command);
        }
    }
    
    private ResponseDTO createResponse(Object payload) {
    	ResponseDTO dto = new ResponseDTO(true);
        Map<String, Object> fullResponse = new HashMap<>();
        fullResponse.put("returnValue", payload);
        fullResponse.put("currentCubeQuery", this.currentCubeQuery);
        fullResponse.put("currentResult", this.currentResult);
        
        dto.setPayload(fullResponse);
        return dto;
    }

    public ArrayList<String> answerCubeQueriesFromFile(File file, CubeManager cubeManager, QueryHistoryManager historyManager) throws RemoteException {
        ArrayList<String> fileLocations = new ArrayList<String>();
        try (Scanner scanner = new Scanner(file).useDelimiter("@")) {
            while (scanner.hasNext()) {
                String queryString = scanner.next();
                String filename = answerCubeQueryFromString(queryString, cubeManager, historyManager);
                fileLocations.add(filename);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileLocations;
    }
    
    
	/**
	 * Gets the query from a string, executes it and produces the output of a query
	 * <p>
	 * The main idea is:
	 * (1) construct the query via <code>createCubeQueryFromString</code> of <code>CubeManager</code>, see {@link CubeManager}
	 * (2) execute the query again via {@link CubeManager} and obtain a {@link Result}
	 * (3a) produce a file in the directory <code>OutputFiles</code> with the name of the query
	 * (3x) btw., the results are also output to the console
	 *
	 * @param queryRawString a String with the query
	 * @return a String containing the location of output file at the server
	 *
	 * @author pvassil
	 * @since v.0.1
	 * @see mainengine.IMainEngine#answerCubeQueryFromString(java.lang.String)
	 * @see cubemanager.CubeManager#executeQuery(CubeQuery)
	 */
    public String answerCubeQueryFromString(String queryRawString, CubeManager cubeManager, QueryHistoryManager historyManager) throws RemoteException {
    	//Use a hashmap to get any useful data (like queryname) from the raw query string
    	HashMap<String, String> queryParams = new HashMap<String, String>();
        
    	//1. parse query and produce a CubeQuery
        this.currentCubeQuery = cubeManager.createCubeQueryFromString(queryRawString, queryParams);
        
    	//2. execute the query AND populate Result with a 2D string
        return executeCubeQuery(this.currentCubeQuery, cubeManager, historyManager);
    }
    

    public String executeCubeQuery(CubeQuery cubeQuery, CubeManager cubeManager, QueryHistoryManager historyManager) {
    	//Result res = cubeManager.getCubeBase().executeQuery(currentCubQuery);
        Instant t0 = Instant.now();
        Result res = cubeManager.executeQuery(cubeQuery);
        this.currentResult = res;
        Instant tExecuted = Instant.now();
        long durationExecution = Duration.between(t0, tExecuted).toMillis();

        //Print result to file and screen
      	//String queryName = queryParams.get("QueryName");
        this.currentQueryName = cubeQuery.getName();
        String queryName = cubeQuery.getName();

        //Print result to file
        String outputFolder = "OutputFiles" + File.separator;
        String outputLocation = this.printToTabTextFile(cubeQuery, outputFolder);

        //Replaced all printing of String[][] with printing of Cells which seems to be identical
      	//TODO SUPER MUST: devise a nice way to handle the output to console when in development mode
        if ((ModeOfWork.mode == WorkMode.DEBUG_GLOBAL)||(ModeOfWork.mode == WorkMode.DEBUG_QUERY)) {
            res.printCellsToStream(System.out);
        }
        
        if (historyManager != null) {
            historyManager.addQuery(cubeQuery);
        }
        logExecutionTime(cubeQuery.getName(), t0, tExecuted);
        
        return outputLocation;
    }
    
    private void logExecutionTime(String queryName, Instant start, Instant executed) {
    	Instant outputed = Instant.now();
    	long durationExecution = Duration.between(start, executed).toMillis();
    	long durationExecToOutput = Duration.between(executed, outputed).toMillis();
    	long durationExecTotal = Duration.between(start, outputed).toMillis();
    	
    	System.out.println("\n\n@TIMER\tQuery\t" + queryName + "\tQuery Execution:\t" + durationExecution
                + "\tQuery Output:\t" + durationExecToOutput + "\tQuery Total:\t" + durationExecTotal);
        System.out.println("------- Done with " + queryName + " --------------------------"+"\n");
    }
    

	/**
	 * Gets the query from a string, executes it and produces the output of a query as a ResultFileMetadata object
	 * <p>
	 * The main idea is:
	 * (1) construct the query via <code>createCubeQueryFromString</code> of <code>CubeManager</code>, see {@link CubeManager}
	 * (2) execute the query again via {@link CubeManager} and obtain a {@link Result}
	 * (3a) produce a queryName.tab file in the directory <code>OutputFiles</code> with the queryName being name of the query
	 * (3b) produce an queryName_info.txt file that contains the query definition in the same folder
	 * (3x) btw., the results are also output to the console
	 *
	 * @param queryRawString a String with the query
	 * @return a ResultFileMetadata containing info on the location of output files at the server
	 *
	 * @author pvassil
	 * @since v.0.2
	 * @see mainengine.IMainEngine#answerCubeQueryFromString(java.lang.String)
	 * @see cubemanager.CubeManager#executeQuery(CubeQuery)
	 */
    public ResultFileMetadata answerCubeQueryFromStringWithMetadata(String queryRawString, CubeManager cubeManager, QueryHistoryManager historyManager) throws RemoteException {
        String outputLocation = answerCubeQueryFromString(queryRawString, cubeManager, historyManager);
        String outputFolder = "OutputFiles" + File.separator;
        String outputInfoLocation = this.printQueryInfo(this.currentCubeQuery, outputFolder);

        ResultFileMetadata resMetadata = new ResultFileMetadata();
        resMetadata.setLocalFolder(outputFolder);
        resMetadata.setResultFile(outputLocation);
        resMetadata.setResultInfoFile(outputInfoLocation);

        System.out.println("@SRV: FOLDER\t" + resMetadata.getLocalFolder());
        System.out.println("@SRV: DATA FILE\t" + resMetadata.getResultFile());
        System.out.println("@SRV: INFO FILE\t" + resMetadata.getResultInfoFile());

        return resMetadata;
    }

    
    public ResultFileMetadata answerCubeQueryFromStringWithModels(String queryRawString, String[] modelsToGenerate, CubeManager cubeManager, QueryHistoryManager historyManager) throws RemoteException {
        int numOfModelsGenerated = 0;
        int numOfModelsRequested = 0;

        //0. answer the query and get its result and info files
        ResultFileMetadata resMetadata = answerCubeQueryFromStringWithMetadata(queryRawString, cubeManager, historyManager);

        /*
		 * postConditions: Result, cubeQuery and cubeQueryName are populated; resMetadata has info on folder, query results and query info
		*/

		// Used to work fine. Replace so that we can introduce model selection explicitly via a dedicated class
		//		/* 
		//		 * 1. Choosing which models to fire. 
		//		 *    We will work with the modelNames variable; 
		//		 *    if you pass an non-empty parameter it works with your parameter, else it works with the defaults.
		//		*/
		//		String [] modelNames = {"Rank","Outlier"};
		//		if(modelsToGenerate.length > 0) {
		//			modelNames = modelsToGenerate.clone(); 
		//		}
		//		numOfModelsRequested = modelNames.length;
		//		System.out.println("\nModel selection of " + numOfModelsRequested + " models");		
        
        if((modelsToGenerate == null) ||(modelsToGenerate.length == 0)) {
            numOfModelsRequested = 0;
        } else {
            numOfModelsRequested = modelsToGenerate.length;
        }

        String [] modelNames;
        ModelSelector modelSelector = new ModelSelector(currentQueryName);
        modelNames = modelSelector.decideModelsToExecute(currentQueryName, modelsToGenerate);

		//2. select the models to fire
        ModelManager modelManager = new ModelManager(this.currentResult);
        modelManager.selectModelsToLaunch(modelNames);
        
        //3. execute the selected models
        int modelGenFlag = modelManager.executeModelConstruction(this.currentQueryName);
        
        //4.Populate resMetadata with the outcome of model generation
        if (modelGenFlag == 0) {	//all went OK
            numOfModelsGenerated = modelManager.addComponentsToResultMetadata(resMetadata);

            if( (numOfModelsRequested > 0) && (numOfModelsGenerated < numOfModelsRequested)) {
                System.err.println("Warning: Model generation of " + numOfModelsGenerated + " models, for " + numOfModelsRequested + " requested models");
            }
        }
        return resMetadata;
    }
    
    
    private String[] computeMeasuresSingle(String queryString, List<String> measures, CubeManager cubeManager, QueryHistoryManager historyManager, InterestingnessManager interestManager) throws Exception {
        //Execute Query
        answerCubeQueryFromString(queryString, cubeManager, historyManager);
        
        //Update Interest State
        interestManager.updateState(this.currentCubeQuery, this.currentResult);
        
        //Compute Measures
        String[] results = new String[measures.size()];
        for(int i = 0; i < measures.size(); i++) {
            double res = interestManager.computeMeasure(measures.get(i), this.currentCubeQuery, this.currentResult);
            results[i] = Double.toString(res);
        }

        saveQueryHistory(queryString, this.currentResult);
        return results;
    }

    private String[] computeMeasuresComparison(String query1, String query2, List<String> measures, CubeManager cubeManager, QueryHistoryManager historyManager, InterestingnessManager interestManager) throws Exception {
    	// answer query
        answerCubeQueryFromString(query1, cubeManager, historyManager);
        interestManager.updateState(this.currentCubeQuery, this.currentResult);
   
        answerCubeQueryFromString(query2, cubeManager, historyManager);
        interestManager.updateState(this.currentCubeQuery, this.currentResult);
        
        String[] results = new String[measures.size()];
        
        for(int i = 0; i < measures.size(); i++) {
        	//compute each measure
            if(measures.get(i).equals("FamilyBasedRelevance")) {
            	results[i] = computeFamilyBasedRelevance(query1, query2, interestManager, cubeManager, historyManager);
            } else {
                double res = interestManager.computeMeasure(measures.get(i), this.currentCubeQuery, this.currentResult);
                results[i] = Double.toString(res);
            }
        }

        // Save History for the *primary* query (query1) passed as first arg
        saveQueryHistory(query1, this.currentResult);

        return results;
    }
    
    private String computeFamilyBasedRelevance(String query1, String query2, InterestingnessManager interestManager, CubeManager cubeManager, QueryHistoryManager historyManager) throws RemoteException {
    	//Get the helping query String
    	String helpingQuery = interestManager.getHelpingQuery(query1, query2);
    	String helpingValue = "";
    	
    	if(!helpingQuery.equals("")) {
    		//Answer the helping query
    		String fileLocation = answerCubeQueryFromString(helpingQuery, cubeManager, historyManager);
    		helpingValue = extractValueFromAnswerFile(fileLocation);
    	}
    	String finalParam = (!helpingValue.equals("")) ? helpingValue : helpingQuery;
    	double res = interestManager.computeMeasure("FamilyBasedRelevance", this.currentCubeQuery, this.currentResult, finalParam);
    	return Double.toString(res);
    }
    
    private String extractValueFromAnswerFile(String file) {
    	try (Scanner scanner = new Scanner(new File(file))){
    		while (scanner.hasNextLine()) {
    			String data = scanner.nextLine();
	    		if (!scanner.hasNextLine()) {
	    			//Get the result we need from the answer file and save it in helpingQuery
	    			return data.split("\t")[0];
	    		}
    		}	
    	}catch (FileNotFoundException e) {
    		System.out.println("An error occurred while reading the helping query file (Family based relevance).");
            e.printStackTrace();
    	}
    	return "";
    }

	/**
	 * Populates a tab-separated file where the result of a query is stored and returns its location.
	 * <p>
	 * The goal of this method is to output a file containing the result of a query
	 * The name of the file is the name of the query + extension tab
	 *
	 * @param cubequery The query whose result is being outputed
	 * @return  A String with the location of the file holding the results
	 * @author pvassil
	 * @author dgkesouli
	 * @version v.0.1
	 * @since v.0.0 from Cinecubes
	 * 
	 * Removed from SQP during the refactoring and placed here
	 */
    private String printToTabTextFile(CubeQuery cubequery, String outputFolder) {
        Result res = cubequery.getResult();
        String fileName = outputFolder + cubequery.getName() + ".tab";
        File file=new File(fileName);
        
        try (FileOutputStream fileOutputStream=new FileOutputStream(file);
            PrintStream printStream=new PrintStream(fileOutputStream)) {
            res.printCellsToStream(printStream);   
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return fileName;
    }

    
    /**
	 * Populates a file, XXX_info.txt, XXX being the query name, containing information for the query launched and returns its location.
	 *
	 * @param cubequery   the CubeQuery whose info is recorded
	 * @param outputFolder the folder to which the file is going to be stored
	 * @return  the String with the location of the produced file
	 */
    private String printQueryInfo(CubeQuery cubequery, String outputFolder) {
        String fileName = outputFolder + cubequery.getName() + "_info.txt";
        File file=new File(fileName);
        
        try (FileOutputStream fileOutputStream=new FileOutputStream(file);
            PrintStream printStream=new PrintStream(fileOutputStream)) {
            printStream.print(cubequery.toString()+"\n\n");  
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return fileName;
    }
    
    
    /**
	 * Populates a file qX.txt in History/Queries, X being an augmenting number, containing the raw query string
	 * and a file qX.tab in History/Results, containing the results of Xth query.
	 * @param queryString  A String with the query
	 * @param queryResult  The {@link Result} of the query
	 */
	private void saveQueryHistory(String queryString, Result queryResult) {
		this.historyCounter = getLatestHistoryCounter() + 1;
		
		FileOutputStream fileOutputStream=null;
		PrintStream printStream=null;

		String queryFileName = "InputFiles/ServerRegisteredInfo/Interestingness/History/Queries/q" + historyCounter + ".txt";
		String resultFileName = "InputFiles/ServerRegisteredInfo/Interestingness/History/Results/q" + historyCounter + ".tab";

		writeStringToFile(queryFileName, queryString + "\n\n");
		writeResultToFile(resultFileName, queryResult);
	}//end saveQueryHistory
	
	
	private int getLatestHistoryCounter() {
		Path queriesDirectory = Paths.get("InputFiles/ServerRegisteredInfo/Interestingness/History/Queries");
		
		if (!Files.exists(queriesDirectory)) {
			return 0;
		}
		
		try {
			return Files.walk(queriesDirectory)
	                .filter(Files::isRegularFile)
	                //It keeps only the name e.g. q12.txt"
	                .map(path -> path.getFileName().toString())
	                .filter(name -> name.startsWith("q") && name.endsWith(".txt"))
	                .mapToInt(name -> {
	                    try {
	                        //It removes the "q" from the start and the ".txt" from the end of the name inorder to obtain the number
	                        String numStr = name.substring(1, name.lastIndexOf('.'));
	                        return Integer.parseInt(numStr);
	                    } catch (NumberFormatException e) {
	                        return 0;
	                    }
	                })
	                //Automatically finds the maximum number and if it doesnt find anything then it returns 0
	                .max()
	                .orElse(0); 
		}catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private void writeStringToFile(String fileName, String content) {
		try (PrintStream printStream = new PrintStream(new FileOutputStream(new File(fileName)))){
			printStream.print(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void writeResultToFile(String fileName, Result queryResult) {
		try (PrintStream printStream = new PrintStream(new FileOutputStream(new File(fileName)))) {
			queryResult.printCellsToStream(printStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			
}