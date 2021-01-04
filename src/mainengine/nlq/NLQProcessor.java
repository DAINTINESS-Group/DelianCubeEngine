package mainengine.nlq;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;

import cubemanager.CubeManager;
import mainengine.IMainEngine;
import mainengine.SimpleQueryProcessorEngine;
import mainengine.nlq.NLTranslator;
import mainengine.nlq.QueryForm;

/**
 * This class is used for the error checking of natural language queries.
 * 
 * @author DimosGkitsakis 
 *
 */

public class NLQProcessor  {
	
	
	private NLTranslator translator;
	public HashMap<String, String> incomingQueries = new HashMap<String, String>();
	public HashMap<String, String> errorDetails = new HashMap<String, String>();

	private ArrayList<String> cubeNames;
	private ArrayList<String> aggrFunctions;
	private ArrayList<String> measures;
	private ArrayList<String> dimensions;
	private HashMap<String, ArrayList<String>> dimensionsToLevelsHashmap;
	private HashMap<String, ArrayList<String>> levelsToDimensionsHashmap;
	
	public NLQProcessor(ArrayList<String> cubeNames,  ArrayList<String> aggrFunctions, ArrayList<String> measures,
						ArrayList<String> dimensions, HashMap<String, ArrayList<String>> dimensionsToLevelsHashmap, 
						HashMap<String, ArrayList<String>> levelsToDimensionsHashmap) {
		this.cubeNames = cubeNames;
		this.aggrFunctions = aggrFunctions;
		this.measures = measures;
		this.dimensions = dimensions;
		this.dimensionsToLevelsHashmap = dimensionsToLevelsHashmap;
		this.levelsToDimensionsHashmap = levelsToDimensionsHashmap;
	}
	
	
	/**
	 * This method is responsible for the pre-processing of the natural language query.
	 * It takes as an input the natural language query, analyzes it and produces a set of errors if any found.
	 * 
	 * @param NLQuery A String with the natural language query
	 * @return A NLQProcessingResultsReturnedToClient object which contains information about the error checking process, which are used for
	 * the production of error messages to be presented to the end-user in the front-end.
	 * @author DimosGkitsakis
	 */
	public NLQProcessingResultsReturnedToClient prepareCubeQuery(String NLQuery) {
		translator = new NLTranslator(dimensionsToLevelsHashmap, levelsToDimensionsHashmap);
		
		//1. Produce cube query string
		QueryForm query = translator.produceNLQuery(NLQuery);
		String cubeQuery = query.toString();
		
		//2. Save it to the hashmap
		saveCubeQuery(query.queryName, cubeQuery);
		
		//3. Check for errors		
		fillErrorHashMap();
		String errorCode = errorChecking(query, translator);

		if (errorCode.equals("No Error Found")) {
			return new NLQProcessingResultsReturnedToClient(query.queryName, false, null, null);
		}else {
			return new NLQProcessingResultsReturnedToClient(query.queryName, true, errorCode, errorDetails.get(errorCode));
		}
		
	}
	
	
	private void saveCubeQuery(String queryName, String cubeQuery) {
		incomingQueries.put(queryName, cubeQuery);
	}
	
	private void fillErrorHashMap() {
		errorDetails.put("Cube Name Error", "Cube Name Error found. The given cube name was not recognized. "
				+ "Please check again. \n "
				+ "The query should look like: \n'Describe the *aggregate function* of *cube name* *measure name* per *grouper* for *selection* as *query name*' " );
		
		errorDetails.put("Aggregate Function Error", "Aggregate Function Error found. The given aggragate function was not recognized. "
				+ "Please check again. \n "
				+ "The query should look like: \n'Describe the *aggregate function* of *cube name* *measure name* per *grouper* for *selection* as *query name*' " );
		
		errorDetails.put("Measure Error", "Measure Name Error found. The given measure name was not recognized. "
				+ "Please check again. \n "
				+ "The query should look like: \n'Describe the *aggregate function* of *cube name* *measure name* per *grouper* for *selection* as *query name*' " );
			
		errorDetails.put("Gamma Field Error: Level Name Not Found", "Gamma Field: One or more of the dimension level names given were not found. "
				+ "Please check again. \n "
				+ "The query should look like: \n'Describe the *aggregate function* of *cube name* *measure name* per *grouper* for *selection* as *query name*' " );
		
		errorDetails.put("Gamma Field Error: Dimension Name Not Found", "Gamma Field: One or more of the dimension names given were not found. "
				+ "Please check again. \n "
				+ "The query should look like: \n'Describe the *aggregate function* of *cube name* *measure name* per *grouper* for *selection* as *query name*' " );
		
		errorDetails.put("Gamma Field Error: Many Same Level Names", "Gamma Field: More than 1 dimensions with the same level were found. "
				+ "Please check again. \n "
				+ "The query should look like: \n'Describe the *aggregate function* of *cube name* *measure name* per *grouper* for *selection* as *query name*' " );
		
		
	}
	
	private String errorChecking(QueryForm query, NLTranslator translator) {
		String cubeName = query.cubeName.split(":")[1] + "_cube";
		String aggrFunc = query.aggregateFunction.split(":")[1];
		String measure = query.cubeName.split(":")[1] + "." + query.measure.split(":")[1];
		
		String gamma = query.gamma.split(":")[1];
		ArrayList<String> gammaErrorHandling = new ArrayList<String>();
		gammaErrorHandling = translator.parseGamma(gamma);
		

		if(!(cubeNames.contains(cubeName))) {
			return "Cube Name Error";
		}else if(!(aggrFunctions.contains(aggrFunc))) {
			return "Aggregate Function Error";
		}else if (!(measures.contains(measure))){
			return "Measure Error";
		}else if (!(gammaErrorHandling.get(0).equals("Null"))) {
			return gammaErrorHandling.get(0);
		}
		
		return "No Error Found";
	}
	
	public String getQuery(String hashKey) {
		return incomingQueries.get(hashKey);
	}
	
	public ArrayList<String> getCubeNames(){
		return cubeNames;
	}
}
