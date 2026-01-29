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
import mainengine.managers.IBuilder;
import mainengine.managers.RequestCTO;
import mainengine.managers.ResponseDTO;
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
        
        Object resultPayload = null;

        switch (cto.getCommandAlias()) {
            case "answer_from_string":
                String queryStr = (String) inputParams.get("query");
                resultPayload = answerCubeQueryFromString(queryStr, cubeManager, historyManager);
                break;

            case "answer_with_metadata":
                String metaQuery = (String) inputParams.get("query");
                resultPayload = answerCubeQueryFromStringWithMetadata(metaQuery, cubeManager, historyManager);
                break;

            case "answer_from_file":
                File file = (File) inputParams.get("file");
                resultPayload = answerCubeQueriesFromFile(file, cubeManager, historyManager);
                break;

            case "answer_with_models":
                String modelQuery = (String) inputParams.get("query");
                String[] models = (String[]) inputParams.get("models");
                resultPayload = answerCubeQueryFromStringWithModels(modelQuery, models, cubeManager, historyManager);
                break;
                
            case "answer_with_interest_measures":
                String queryInt = (String) inputParams.get("query");
                List<String> measures = (List<String>) inputParams.get("measures");
                
                if (interestManager == null) throw new Exception("InterestManager is null in parameters");
                
                resultPayload = computeMeasuresSingle(queryInt, measures, cubeManager, historyManager, interestManager);
                break;

            case "answer_queries_with_interest_measures":
                String query1 = (String) inputParams.get("query1");
                String query2 = (String) inputParams.get("query2");
                
                List<String> measuresComp = (List<String>) inputParams.get("measures");
                
                if (interestManager == null) throw new Exception("InterestManager is null in parameters");
                resultPayload = computeMeasuresComparison(query1, query2, measuresComp, cubeManager, historyManager, interestManager);
                break;
                
            default:
                throw new IllegalArgumentException("Unknown Command: " + cto.getCommandAlias());
        }

        ResponseDTO dto = new ResponseDTO(true);
        Map<String, Object> fullResponse = new HashMap<>();
        fullResponse.put("returnValue", resultPayload);
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
        String outputLocation = executeCubeQuery(this.currentCubeQuery, cubeManager, historyManager);
        return outputLocation;
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
        
        Instant tOutputed = Instant.now();
        long durationExecToOutput = Duration.between(tExecuted, tOutputed).toMillis();
        long durationExecTotal = Duration.between(t0, tOutputed).toMillis();

        System.out.println("\n\n@TIMER\tQuery\t" + queryName + "\tQuery Execution:\t" + durationExecution
                + "\tQuery Output:\t" + durationExecToOutput + "\tQuery Total:\t" + durationExecTotal);
        System.out.println("------- Done with " + queryName + " --------------------------"+"\n");
        
        return outputLocation;
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
            	//Get the helping query String
                String helpingQuery = interestManager.getHelpingQuery(query1, query2);
                String helpingValue = "";
                
                if(!helpingQuery.equals("")) {
                	//Answer the helping query
                    String file = answerCubeQueryFromString(helpingQuery, cubeManager, historyManager);
                    
                    try {
                        File myObj = new File(file);
                        Scanner myReader = new Scanner(myObj);
                        while (myReader.hasNextLine()) {
                            String data = myReader.nextLine();
                            if(!myReader.hasNextLine()) {
                            	//Get the result we need from the answer file and save it in helpingQuery
                                helpingValue = data.split("\t")[0];
                            }
                        }
                        myReader.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("An error occurred while reading the helping query file (Family based relevance).");
                        e.printStackTrace();
                    }
                }

                String finalParam = (!helpingValue.equals("")) ? helpingValue : helpingQuery;

                double res = interestManager.computeMeasure(measures.get(0), this.currentCubeQuery, this.currentResult, finalParam);
                results[0] = Double.toString(res);

            } else {
                double res = interestManager.computeMeasure(measures.get(i), this.currentCubeQuery, this.currentResult);
                results[i] = Double.toString(res);
            }
        }

        // Save History for the *primary* query (query1) passed as first arg
        saveQueryHistory(query1, this.currentResult);

        return results;
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
		try {
			List<String> filesInFolder = Files.walk(Paths.get("InputFiles/ServerRegisteredInfo/Interestingness/History/Queries"))
					.filter(Files::isRegularFile)
					.map(Path::toString)
					.collect(Collectors.toList());
			if(filesInFolder.size() > 0) {
				int max = 0;
				for(int i=0; i< filesInFolder.size(); i++) {

					if(String.valueOf(filesInFolder.get(i).charAt(66)).equals(".")) {
						if(Integer.parseInt(String.valueOf(filesInFolder.get(i).charAt(65))) > max){
							max = Integer.parseInt(String.valueOf(filesInFolder.get(i).charAt(65)));
						}
					}else{
						if(Integer.parseInt(String.valueOf(filesInFolder.get(i).charAt(65)) + String.valueOf(filesInFolder.get(i).charAt(66)))>max){
							max = Integer.parseInt(String.valueOf(filesInFolder.get(i).charAt(65)) + String.valueOf(filesInFolder.get(i).charAt(66)));
						}
					}
				}

				historyCounter = max;
			}else {
				historyCounter = 0;
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		this.historyCounter += 1;
		FileOutputStream fileOutputStream=null;
		PrintStream printStream=null;

		String queryFileName = "InputFiles/ServerRegisteredInfo/Interestingness/History/Queries/q" + historyCounter + ".txt";

		File queryFile=new File(queryFileName);

		try {
			fileOutputStream=new FileOutputStream(queryFile);
			printStream=new PrintStream(fileOutputStream);

			printStream.print(queryString+"\n\n");

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(fileOutputStream!=null){
					fileOutputStream.close();
				}
				if(printStream!=null){
					printStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}//end finally try
		}//end finally

		fileOutputStream = null;
		printStream = null;
		String resultFileName = "InputFiles/ServerRegisteredInfo/Interestingness/History/Results/q" + historyCounter + ".tab";
		File resultFile=new File(resultFileName);

		try {
			fileOutputStream=new FileOutputStream(resultFile);
			printStream=new PrintStream(fileOutputStream);

			queryResult.printCellsToStream(printStream);

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(fileOutputStream!=null){
					fileOutputStream.close();
				}
				if(printStream!=null){
					printStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}//end finally try
		}//end finally
	}//end saveQueryHistory
}