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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.time.Instant;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import assess.AssessOperator;
import mainengine.nlq.ITranslator;
import mainengine.nlq.ITranslatorFactory;
import mainengine.nlq.NLQValidationResults;
import mainengine.rmiTransfer.RMIInputStream;
import mainengine.rmiTransfer.RMIOutputStream;
import mainengine.rmiTransfer.RMIInputStreamImpl;
import mainengine.rmiTransfer.RMIOutputStreamImpl;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.Level;
import cubemanager.cubebase.QueryHistoryManager;
import cubemanager.cubebase.BasicStoredCube;
//import exctractionmethod.SqlQuery;
/*
import filecreation.FileMgr;
import filecreation.PptxFile;
import filecreation.WordFile;
*/
//import storymgr.FinResult;
//import storymgr.StoryMgr;
import org.antlr.runtime.RecognitionException;
import result.Result;
import setup.ModeOfWork;
import setup.ModeOfWork.WorkMode;



import interestingnessengine.InterestingnessManager;



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
	private CubeManager cubeManager;
	private Session session;
	private String sessionId;
	private QueryHistoryManager queryHistoryMng;
	private CubeQuery currentCubeQuery;
	private Result currentResult;
	private String currentQueryName;
	private ITranslatorFactory translatorFactory;
	private ArrayList<String> cubeNames;
	private ArrayList<String> aggrFunctions;
	private ArrayList<String> measuresFields;
	private ArrayList <String> dimensions;
	private HashMap<String, ArrayList<String>> dimensionsToLevelsHashmap;
	private HashMap<String, ArrayList<String>> levelsToDimensionsHashmap;
	private List<Dimension> registeredDimensions;
	private List<BasicStoredCube> registeredCubesList;
	private InterestingnessManager interestMng;
	private int historyCounter = 0;
	
/**
 * Simple constructor for the class
 * @throws RemoteException
 */
	public SessionQueryProcessorEngine() throws RemoteException {
		super();
		currentResult = null;
		currentCubeQuery = null;
		currentQueryName = null;
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
	 */
	@Override
	public void initializeConnection(String typeOfConnection, HashMap<String, String> userInputList)
			throws RemoteException {
		cubeManager = new CubeManager(typeOfConnection, userInputList);
		session = new Session(cubeManager);
		sessionId = session.initialize(typeOfConnection, userInputList);
		registeredDimensions = cubeManager.getDimensions();
		registeredCubesList = cubeManager.getCubes();
		queryHistoryMng = new QueryHistoryManager(sessionId);
		System.out.println("DONE WITH INIT");
	}
	
	public void initializeConnectionWithIntrMng(String typeOfConnection, HashMap<String, String> userInputList, String historyFolder,
			String expValuesFolder, String expLabelsFolder, int k) throws RemoteException {
		cubeManager = new CubeManager(typeOfConnection, userInputList);
		session = new Session(cubeManager);
		sessionId = session.initialize(typeOfConnection, userInputList);
		registeredDimensions = cubeManager.getDimensions();
		registeredCubesList = cubeManager.getCubes();
		queryHistoryMng = new QueryHistoryManager(sessionId);
		initializeInterestMgr(historyFolder, expValuesFolder, expLabelsFolder, k);
		System.out.println("DONE WITH INIT");
	}
	private void initializeInterestMgr(String historyFolder, String expValuesFolder, String expLabelsFolder, int k) throws RemoteException {
		if(historyFolder.equals("") && expValuesFolder.equals("") && expLabelsFolder.equals("")) {
			interestMng = new InterestingnessManager(session.getCubeManager(), k);
		}else {
			interestMng = new InterestingnessManager(historyFolder, expValuesFolder, expLabelsFolder, session.getCubeManager(), k);
		}
	}
	
	/* (non-Javadoc)
	 * @see mainengine.IMainEngine#answerCubeQueriesFromFile(java.io.File)
	 */
	@Override
	public ArrayList<String> answerCubeQueriesFromFile(File file) throws RemoteException {
		ArrayList<String> fileLocations = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(file).useDelimiter("@");
			while (scanner.hasNext()) {
				String filename;
				String queryString = scanner.next();
				filename = answerCubeQueryFromString(queryString);
				fileLocations.add(filename);
			}
			scanner.close();
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
	@Override
	public String answerCubeQueryFromString(String queryRawString) throws RemoteException{
		//Use a hashmap to get any useful data (like queryname) from the raw query string
		HashMap<String, String> queryParams = new HashMap<String, String>();
		Instant t0 = Instant.now();
		cubeManager = session.getCubeManager();

		//1. parse query and produce a CubeQuery
		CubeQuery currentCubeQuery = cubeManager.createCubeQueryFromString(queryRawString, queryParams); 
		this.currentCubeQuery = currentCubeQuery;
		
		//2. execute the query AND populate Result with a 2D string
		//Result res = cubeManager.getCubeBase().executeQuery(currentCubQuery);
		Result res = cubeManager.executeQuery(currentCubeQuery);
		this.currentResult = res;
		Instant tExecuted = Instant.now();
		long durationExecution = Duration.between(t0, tExecuted).toMillis();

		//3a. print result to file and screen
		String queryName = queryParams.get("QueryName");
		this.currentQueryName = queryName;
				
		//3b. print result to file
		String outputLocation = this.printToTabTextFile(currentCubeQuery,  "OutputFiles" + File.separator);

		//Replaced all printing of String[][] with printing of Cells which seems to be identical 
		//TODO SUPER MUST: devise a nice way to handle the output to console when in development mode
		if ((ModeOfWork.mode == WorkMode.DEBUG_GLOBAL)||(ModeOfWork.mode == WorkMode.DEBUG_QUERY)) {
			res.printCellsToStream(System.out);
		}
		queryHistoryMng.addQuery(currentCubeQuery);
		Instant tOutputed = Instant.now();	
		long durationExecToOutput = Duration.between(tExecuted, tOutputed).toMillis();
		long durationExecTotal = Duration.between(t0, tOutputed).toMillis();
		
		System.out.println("\n\n@TIMER\tQuery\t" + queryName + "\tQuery Execution:\t" + durationExecution
				+ "\tQuery Output:\t" + durationExecToOutput + "\tQuery Total:\t" + durationExecTotal);
		
		System.out.println("------- Done with " + queryName + " --------------------------"+"\n");
		
		return outputLocation;
	}//answerCubeQueryFromString
	

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
	@Override
	public ResultFileMetadata answerCubeQueryFromStringWithMetadata(String queryRawString) throws RemoteException {

		String outputLocation = answerCubeQueryFromString(queryRawString);
		String outputInfoLocation = this.printQueryInfo(this.currentCubeQuery,  "OutputFiles" + File.separator);
		
		ResultFileMetadata resMetadata = new ResultFileMetadata();		
		resMetadata.setComponentResultFiles(null);
		resMetadata.setComponentResultInfoFiles(null);
		resMetadata.setLocalFolder("OutputFiles" + File.separator);
		resMetadata.setResultFile(outputLocation);
		resMetadata.setResultInfoFile(outputInfoLocation);
		
		System.out.println("@SRV: FOLDER\t" + resMetadata.getLocalFolder());
		System.out.println("@SRV: DATA FILE\t" + resMetadata.getResultFile());
		System.out.println("@SRV: INFO FILE\t" + resMetadata.getResultInfoFile());		
		
		return resMetadata;
	}//answerCubeQueryFromStringWithMetadata
	
	
	@Override
	public ResultFileMetadata answerCubeQueryFromStringWithModels(String queryRawString, String[] modelsToGenerate)
			throws RemoteException {
		
		int numOfModelsGenerated = 0;
		int numOfModelsRequested = 0;
		
		//0. answer the query and get its result and info files
		ResultFileMetadata resMetadata = answerCubeQueryFromStringWithMetadata(queryRawString);
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
		}
		else 
			numOfModelsRequested = modelsToGenerate.length;
		
		String [] modelNames;
		ModelSelector modelSelector = new ModelSelector(currentQueryName);
		modelNames = modelSelector.decideModelsToExecute(currentQueryName, modelsToGenerate);

		
		//2. select the models to fire
		ModelManager modelManager = new ModelManager(this.currentResult);
		modelManager.selectModelsToLaunch(modelNames);
		//3. execute the selected models
		int modelGenFlag = modelManager.executeModelConstruction(this.currentQueryName);
		//4.Populate resMetadata with the outcome of model generation
		if (modelGenFlag == 0) { 			//all went OK
			numOfModelsGenerated = modelManager.addComponentsToResultMetadata(resMetadata);
						

			if( (numOfModelsRequested > 0) &&(numOfModelsGenerated < numOfModelsRequested)) {
				System.err.println("\nSIMPLEQUERYPROCESSORENGINE # answerCubeQueryFromStringWithModels\nModel generation of " + numOfModelsGenerated + " models, for " + numOfModelsRequested + " requested models");
				System.err.println("Shutting down server\n");
				System.exit(-1);
			}
			
		}//end if modelGenFlag

		
		return resMetadata;
	}//end method answerCubeQueryFromStringWithModels

	
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
	 */
	public String printToTabTextFile(CubeQuery cubequery, String outputFolder) {
		Result res = cubequery.getResult();
		String fileName = outputFolder + cubequery.getName() + ".tab";
		File file=new File(fileName);
		FileOutputStream fileOutputStream=null;
		PrintStream printStream=null;
		try {
			fileOutputStream=new FileOutputStream(file);
			printStream=new PrintStream(fileOutputStream);
			
			res.printCellsToStream(printStream);
			
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
		
		return fileName;
	}//end method
	
	
	//OLAP BASIC OPERATORS METHODS
	@Override
	public ResultFileMetadata rollUp(String oldQueryName, String newQueryName, String dimensionName, String targetLevelName) throws RemoteException {
		
		CubeQuery oldCubeQuery = queryHistoryMng.getQueryByName(oldQueryName);
		cubeManager = session.getCubeManager();
		RollUpOperator operator = new RollUpOperator(oldCubeQuery, cubeManager);
		
		//1. Perform the Roll-Up	
		Instant t0 = Instant.now();
		ResultFileMetadata resMetadata = operator.executeRollUp(oldQueryName, newQueryName, dimensionName, targetLevelName);
		Instant tExecuted = Instant.now();
		long durationExecution = Duration.between(t0, tExecuted).toMillis();
		
		if(resMetadata.getErrorCheckingStatus()==null) {
			
			this.currentCubeQuery = operator.getCubeQuery();
			this.currentQueryName = newQueryName;
			this.currentResult = operator.getResult();
			queryHistoryMng.addQuery(currentCubeQuery);
			
			//2. print result to file
			String outputLocation = this.printToTabTextFile(currentCubeQuery,  "OutputFiles" + File.separator);
			
			if ((ModeOfWork.mode == WorkMode.DEBUG_GLOBAL)||(ModeOfWork.mode == WorkMode.DEBUG_QUERY)) {
				this.currentResult.printCellsToStream(System.out);
			}
			String outputInfoLocation = this.printQueryInfo(this.currentCubeQuery,  "OutputFiles" + File.separator);
			resMetadata.setComponentResultFiles(null);
			resMetadata.setComponentResultInfoFiles(null);
			resMetadata.setLocalFolder("OutputFiles" + File.separator);
			resMetadata.setResultFile(outputLocation);
			resMetadata.setResultInfoFile(outputInfoLocation);
			
			Instant tOutputed = Instant.now();			
			long durationExecToOutput = Duration.between(tExecuted, tOutputed).toMillis();
			long durationExecTotal = Duration.between(t0, tOutputed).toMillis();
			
			//3. Print to screen
			System.out.println("\n\n@TIMER\tQuery\t" + newQueryName + "\tQuery Execution:\t" + durationExecution
					+ "\tQuery Output:\t" + durationExecToOutput + "\tQuery Total:\t" + durationExecTotal);			
			System.out.println("------- Done with " + newQueryName + " --------------------------"+"\n");
			System.out.println("@SRV: FOLDER\t" + resMetadata.getLocalFolder());
			System.out.println("@SRV: DATA FILE\t" + resMetadata.getResultFile());
			System.out.println("@SRV: INFO FILE\t" + resMetadata.getResultInfoFile());
		}
		return resMetadata;
	}
	
	
	@Override
	public ResultFileMetadata drillDown(String oldQueryName, String newQueryName, String dimensionName, String targetLevelName) throws RemoteException {
		
		CubeQuery oldCubeQuery = queryHistoryMng.getQueryByName(oldQueryName);
		cubeManager = session.getCubeManager();
		DrillDownOperator operator = new DrillDownOperator(oldCubeQuery, cubeManager);
		
		//1.Perform the Drill-Down
		Instant t0 = Instant.now();
		ResultFileMetadata resMetadata = operator.executeDrillDown(oldQueryName, newQueryName, dimensionName, targetLevelName);
		Instant tExecuted = Instant.now();
		long durationExecution = Duration.between(t0, tExecuted).toMillis();
		
		if(resMetadata.getErrorCheckingStatus()==null) {
			
			this.currentCubeQuery = operator.getCubeQuery();
			this.currentQueryName = newQueryName;
			this.currentResult = operator.getResult();
			queryHistoryMng.addQuery(currentCubeQuery);
					
			//2. print result to file
			String outputLocation = this.printToTabTextFile(currentCubeQuery,  "OutputFiles" + File.separator);
			
			if ((ModeOfWork.mode == WorkMode.DEBUG_GLOBAL)||(ModeOfWork.mode == WorkMode.DEBUG_QUERY)) {
				this.currentResult.printCellsToStream(System.out);
			}
			String outputInfoLocation = this.printQueryInfo(this.currentCubeQuery,  "OutputFiles" + File.separator);
			resMetadata.setComponentResultFiles(null);
			resMetadata.setComponentResultInfoFiles(null);
			resMetadata.setLocalFolder("OutputFiles" + File.separator);
			resMetadata.setResultFile(outputLocation);
			resMetadata.setResultInfoFile(outputInfoLocation);
			
			Instant tOutputed = Instant.now();		
			long durationExecToOutput = Duration.between(tExecuted, tOutputed).toMillis();
			long durationExecTotal = Duration.between(t0, tOutputed).toMillis();
			
			//3. Print to screen
			System.out.println("\n\n@TIMER\tQuery\t" + newQueryName + "\tQuery Execution:\t" + durationExecution
					+ "\tQuery Output:\t" + durationExecToOutput + "\tQuery Total:\t" + durationExecTotal);			
			System.out.println("------- Done with " + newQueryName + " --------------------------"+"\n");
			System.out.println("@SRV: FOLDER\t" + resMetadata.getLocalFolder());
			System.out.println("@SRV: DATA FILE\t" + resMetadata.getResultFile());
			System.out.println("@SRV: INFO FILE\t" + resMetadata.getResultInfoFile());
		}
		return resMetadata;
	}
	
	//INTENTIONAL OPERATORS METHODS
	@Override
	public ResultFileMetadata analyze(String incomingExpression) throws RemoteException {
		cubeManager = session.getCubeManager();
		AnalyzeOperator operator = new AnalyzeOperator(incomingExpression, cubeManager);
		operator.execute();
		return null;
	}

	@Override
	public ResultFileMetadata assess(String incomingExpression) throws RemoteException {
		cubeManager = session.getCubeManager();
		AssessOperator operator = new AssessOperator(incomingExpression, cubeManager);
		try {
			operator.execute();
		} catch (RecognitionException e) {
			throw new RuntimeException(e);
		}
		return null;
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
	 */
	@Override
	public String answerCubeQueryFromNLString(String queryRawString) throws RemoteException{
		//1. parse and analyse natural language query and produce a CubeQuery
		extractCubeMetadata();
		ITranslator translator = getNLTranslator();
		long t0 = System.nanoTime();
		String analysedString = translator.produceCubeQueryString(queryRawString);
		long tF = System.nanoTime();
		long duration = tF - t0;
		System.out.println("NLQuery Transformation Time: " + duration + " nanoseconds");
		
		System.out.println(analysedString);
		//2. call answerCubeQueryFromString() with the analysedNLString as parameter
		t0 = System.nanoTime();
		String outputLocation = answerCubeQueryFromString(analysedString);
		tF = System.nanoTime();
		duration = tF - t0;
		System.out.println("Query Execution Time " + duration + " nanoseconds");
		return outputLocation;
		
	}//answerCubeQueryFromNLString
	
	
	
	/**
	 * Same idea as the {@link #answerCubeQueryFromStringWithMetadata(String)}, but for a natural language query instead of cube query.
	 * Gets the natural language query from a string, executes it and produces the output of a query as a ResultFileMetadata object.
	 * Result produced via {@link #answerCubeQueryFromNLString(String)} instead of {@link #answerCubeQueryFromString(String)}.
	 * 
	 * @param queryRawString a String with the natural language query
	 * @return a ResultFileMetadata containing info on the location of output files at the server
	 * 
	 * 
	 */
	@Override
	public ResultFileMetadata answerCubeQueryFromNLStringWithMetadata(String queryRawString) throws RemoteException {

		String outputLocation = answerCubeQueryFromNLString(queryRawString);
		String outputInfoLocation = this.printQueryInfo(this.currentCubeQuery,  "OutputFiles" + File.separator);
		
		ResultFileMetadata resMetadata = new ResultFileMetadata();
		resMetadata.setComponentResultFiles(null);
		resMetadata.setComponentResultInfoFiles(null);
		resMetadata.setLocalFolder("OutputFiles" + File.separator);
		resMetadata.setResultFile(outputLocation);
		resMetadata.setResultInfoFile(outputInfoLocation);
		System.out.println("@SRV: FOLDER\t" + resMetadata.getLocalFolder());
		System.out.println("@SRV: DATA FILE\t" + resMetadata.getResultFile());
		System.out.println("@SRV: INFO FILE\t" + resMetadata.getResultInfoFile());		

		return resMetadata;
	}//answerCubeQueryFromNLStringWithMetadata
	
	private void extractCubeMetadata() {
		cubeNames = new ArrayList<String>();
		measuresFields = new ArrayList<String>();
		dimensions = new ArrayList<String>();
		dimensionsToLevelsHashmap = new HashMap<String, ArrayList<String>>();
		levelsToDimensionsHashmap = new HashMap<String, ArrayList<String>>();
		
		for(int i=0; i<registeredCubesList.size(); i++) {
			//1.Bring data for CubeName
			cubeNames.add(registeredCubesList.get(i).getName());
			//2.Bring data for Measure Fields
			for(int j=0; j<registeredCubesList.get(i).getMeasuresList().size(); j++) {
				measuresFields.add(registeredCubesList.get(i).getFactTable().getTableName()
						+"."+registeredCubesList.get(i).getMeasuresList().get(j).getAttribute().getName());
			}
		}
		
		//3.Bring data for Dimensions		
		for(int i =0; i<registeredDimensions.size(); i++) {
			dimensions.add(registeredDimensions.get(i).getName());	
		}
		
		//4.Create data for AggrFunc
		aggrFunctions = new ArrayList<String>();
		aggrFunctions.add("max");
		aggrFunctions.add("maximum");
		aggrFunctions.add("min");
		aggrFunctions.add("minimum");
		aggrFunctions.add("count");
		aggrFunctions.add("avg");
		aggrFunctions.add("average");
		aggrFunctions.add("sum");	

		
		//5.Bring data for levels
		dimensionsToLevelsHashmap = new HashMap<String, ArrayList<String>>();
		levelsToDimensionsHashmap = new HashMap<String, ArrayList<String>>();
		List<Dimension> dimensionsList = registeredDimensions;
		ArrayList<String> tmpLvls = null; 
		ArrayList<String> tmpDim = null;
		for (int i=0;i < registeredDimensions.size();i ++) {
			Dimension dim = registeredDimensions.get(i);
			for(int j=0; j<dim.getHierarchy().size(); j++) {
				List<Level> l = dim.getHierarchy().get(j).getLevels();
				tmpLvls = new ArrayList<String>();
				for (int k=0; k<l.size(); k++) {
					tmpLvls.add(l.get(k).getName());
					String tmpLevelKey = l.get(k).getName();
					if(levelsToDimensionsHashmap.containsKey(tmpLevelKey)) {
						tmpDim = levelsToDimensionsHashmap.get(tmpLevelKey);
						tmpDim.add(dimensionsList.get(i).getName());
						levelsToDimensionsHashmap.put(tmpLevelKey, tmpDim);
					}else {
						tmpDim = new ArrayList<String>();
						tmpDim.add(dimensionsList.get(i).getName());
						levelsToDimensionsHashmap.put(tmpLevelKey, tmpDim);
					}
				}
				dimensionsToLevelsHashmap.put(dimensionsList.get(i).getName(), tmpLvls);
			}
		}
	}
	
	/**
	 * Validates a natural language Query String and returns a ResultFileMetadata object with the result.
	 * 
	 * Gets the natural language query from a string, passes it to a NLTranslator object, which parses it and produces error messages if any errors where found.
	 * The errors are returned to the client as a ErrorChecking.txt file, so the client can produce the error message to the end-user.
	 *
	 *@author DimosGkitsakis
	 *@param naturalLanguageQueryString A String with the natural language query given by the user.
	 *@return a ResultFileMetadata object containing info on the location of the error checking file at the server
	 */
	@Override
	public ResultFileMetadata prepareCubeQuery(String naturalLanguageQueryString) throws RemoteException {
		extractCubeMetadata();
		ITranslator translator = getNLTranslator();
		long t0 = System.nanoTime();
		NLQValidationResults results = translator.prepareCubeQuery(cubeNames, aggrFunctions, measuresFields, naturalLanguageQueryString);
		long tF = System.nanoTime();
		long duration = tF - t0;
		System.out.println("NLQuery Preprocessing Time: " + duration + " nanoseconds");
		
		String errorCheckingInfoOutput = this.printErrorCheckingResultsToFile(results,"OutputFiles" + File.separator + "ErrorChecking.txt");
		
		ResultFileMetadata resMetadata = new ResultFileMetadata();
		resMetadata.setErrorCheckingStatus(errorCheckingInfoOutput);
		return resMetadata;		
	}

	private ITranslator getNLTranslator() {
		translatorFactory = new ITranslatorFactory();
		ITranslator translator = translatorFactory.getITranslator("NLTranslator", dimensionsToLevelsHashmap, levelsToDimensionsHashmap);
		return translator;
	}
	
	private String printErrorCheckingResultsToFile(NLQValidationResults results, String filePath) {
		String fileName = filePath;
		File file=new File(fileName);
		FileOutputStream fileOutputStream=null;
		PrintStream printStream=null;
		try {
			fileOutputStream=new FileOutputStream(file);
			printStream=new PrintStream(fileOutputStream);
			printStream.print(results.getHashKey() +";\n");
			printStream.print(results.getFoundError() +";\n");
			printStream.print(results.getErrorCode() + ";\n");
			printStream.print(results.getDetails());
			
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
		return fileName;
	}
	
	
	
	/**
	 * Populates a file, XXX_info.txt, XXX being the query name, containing information for the query launched and returns its location.
	 * 
	 * @param cubequery   the CubeQuery whose info is recorded 
	 * @param outputFolder the folder to which the file is going to be stored
	 * @return  the String with the location of the produced file
	 */
	public String printQueryInfo(CubeQuery cubequery, String outputFolder) {
		String fileName = computeQueryInfoName(cubequery, outputFolder);
		File file=new File(fileName);
		FileOutputStream fileOutputStream=null;
		PrintStream printStream=null;
		try {
			fileOutputStream=new FileOutputStream(file);
			printStream=new PrintStream(fileOutputStream);
			
			printStream.print(cubequery.toString()+"\n\n");
			
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
		
		return fileName;
	}//end method
	
	
	/**
	 * Given an query (and thus its name), and an output folder, computes the path of the _info.txt file of the query
	 * 
	 * @param cubequery a CubeQuery whose info file name is requested
	 * @param outputFolder  a String representing a target folder, if the form of "MyFolder/" where the output will be placed. Don't forget the "/"
	 * @return the path of the _info.txt file of the query
	 */
	private String computeQueryInfoName(CubeQuery cubequery, String outputFolder) {
		String fileName = outputFolder + cubequery.getName() + "_info.txt";
		return fileName;
	}//end method
	
	

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


	//INTERESTINGNESS METHODS
	public String[] answerCubeQueryWithInterestMeasures(String queryString, List<String> measures)
			throws RemoteException {

		answerCubeQueryFromString(queryString); 
		this.interestMng.updateState(currentCubeQuery, currentResult);
		double result;
		String[] results = new String[measures.size()];

		for(int i = 0; i < measures.size(); i++) {
			result = interestMng.computeMeasure(measures.get(i), currentCubeQuery, currentResult);
			results[i] = Double.toString(result);
		}

		saveQueryHistory(queryString,currentResult);

		return results;

	}//end answerCubeQueryWithInterestMeasures
	
	public String[] answerCubeQueryWithInterestMeasures(String queryString, String queryString1, List<String> measures)
			throws RemoteException, FileNotFoundException {
		// answer query
		answerCubeQueryFromString(queryString);
		this.interestMng.updateState(currentCubeQuery, currentResult);
		answerCubeQueryFromString(queryString1); 
		this.interestMng.updateState(currentCubeQuery, currentResult);

		double result;
		String[] results = new String[measures.size()];
		for(int i = 0; i < measures.size(); i++) {
			//compute each measure
			if(measures.get(i).equals("FamilyBasedRelevance")) {
				//Get the helping query String
				String helpingQuery = interestMng.getHelpingQuery(queryString,queryString1);
				if(!helpingQuery.equals("")) {
					//Answer the helping query
					String file = answerCubeQueryFromString(helpingQuery);
					  try {
					      File myObj = new File(file);
					      Scanner myReader = new Scanner(myObj);
					      while (myReader.hasNextLine()) {
					        String data = myReader.nextLine();
					        if(!myReader.hasNextLine()) {
					        	//Get the result we need from the answer file and save it in helpingQuery
						        helpingQuery = data.split("	")[0];
					        }
					      }
					      myReader.close();
					    } catch (FileNotFoundException e) {
					      System.out.println("An error occurred while reading the helping query file (Family based relevance).");
					      e.printStackTrace();
					    }
				}
				

				result = interestMng.computeMeasure(measures.get(0), currentCubeQuery, currentResult, helpingQuery);

				results[0] = Double.toString(result);

			}else {
				//compute each measure
				result = interestMng.computeMeasure(measures.get(i), currentCubeQuery, currentResult);
				results[i] = Double.toString(result);

			}

		}


		saveQueryHistory(queryString,currentResult);

		return results;

	}//end answerCubeQueryWithInterestMeasures


	

	
}//end class
