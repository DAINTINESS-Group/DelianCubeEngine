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


@SuppressWarnings("unused")
public class NLQProcessor  {
	
	
	private NLTranslator translator;
	public HashMap<String, String> incomingQueries = new HashMap<String, String>();
	public HashMap<String, String> errorDetails = new HashMap<String, String>();

	private ArrayList<String> cubeNames;
	private ArrayList<String> aggrFunctions;
	private ArrayList<String> measures;
	private ArrayList<String> dimensions;
	private ArrayList<ArrayList<String>> levels;
	
	public NLQProcessor(ArrayList<String> cubeNames,  ArrayList<String> aggrFunctions, ArrayList<String> measures,
						ArrayList<String> dimensions, ArrayList<ArrayList<String>> levels) {
		//incomingQueries = new HashMap<String, String>();
		this.cubeNames = cubeNames;
		this.aggrFunctions = aggrFunctions;
		this.measures = measures;
		this.dimensions = dimensions;
		this.levels = levels;
	}
	

	
	//TODO edw tha ginei to preprocessing twn nlqueries.
	//errors. ask how to bring hierarchies etc here


	public NLQProcessingResultsReturnedToClient prepareCubeQuery(String NLQuery) {
		translator = new NLTranslator();
		
		//1. Produce cube query string
		QueryForm query = translator.analyzeNLQuery(NLQuery);
		String cubeQuery = query.toString();
		
		//2. Save it to the hashmap
		saveCubeQuery(query.queryName, cubeQuery);
		//System.out.println(incomingQueries);
		
		//3. Check for errors
		
		fillErrorHashMap();
		String errorCode = errorChecking(query);
		//System.out.println(errorCode);

		if (errorCode.equals("No Error Found")) {
			return new NLQProcessingResultsReturnedToClient(query.queryName, false, "", "");
		}else {
			return new NLQProcessingResultsReturnedToClient(query.queryName, true, errorCode, errorDetails.get(errorCode));
		}
		
	}
	
	
	private void saveCubeQuery(String queryName, String cubeQuery) {
		incomingQueries.put(queryName, cubeQuery);
		//System.out.println(incomingQueries);	
	}
	
	private void fillErrorHashMap() {
		errorDetails.put("Cube Name Error", "Cube Name Error found. The given cube name was not recognized. "
				+ "Please check again. \n "
				+ "The query should look like 'Show me the *aggregate function* of *cube name* *measure name* per *grouper* for *selection* as *query name*' " );
		
		errorDetails.put("Aggregate Function Error", "Aggregate Function Error found. The given aggragate function was not recognized. "
				+ "Please check again. \n "
				+ "The query should look like 'Show me the *aggregate function* of *cube name* *measure name* per *grouper* for *selection* as *query name*' " );
		
		errorDetails.put("Measure Error", "Measure Name Error found. The given measure name was not recognized. "
				+ "Please check again. \n "
				+ "The query should look like 'Show me the *aggregate function* of *cube name* *measure name* per *grouper* for *selection* as *query name*' " );
		
		errorDetails.put("Dimension Name Error", "Dimension Name Error found. The given dimension name was not recognized. "
				+ "Please check again. \n "
				+ "The query should look like 'Show me the *aggregate function* of *cube name* *measure name* per *grouper* for *selection* as *query name*' " );
		
		errorDetails.put("Level Name Error", "Level Name Error found. The given level name was not recognized. "
				+ "Please check again. \n "
				+ "The query should look like 'Show me the *aggregate function* of *cube name* *measure name* per *grouper* for *selection* as *query name*' " );
		
	}
	
	private String errorChecking(QueryForm query) {
		String cubeName = query.cubeName.split(":")[1] + "_cube";
		String aggrFunc = query.aggregateFunction.split(":")[1];
		String measure = query.cubeName.split(":")[1] + "." + query.measure.split(":")[1];
		
		//System.out.println(cubeName);
		//System.out.println(cubeNames);
		//System.out.println(aggrFunc);
		//System.out.println(measure);
		if(!(cubeNames.contains(cubeName))) {
			return "Cube Error Name";
		}else if(!(aggrFunctions.contains(aggrFunc))) {
			return "Aggregate Function Error";
		}else if (!(measures.contains(measure))){
			return "Measure Error";
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
