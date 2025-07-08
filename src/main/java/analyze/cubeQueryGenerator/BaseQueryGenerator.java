package analyze.cubeQueryGenerator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import analyze.AnalyzeQuery;
import analyze.AnalyzeQuery.TypeOfAnalyzeQuery;
import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;

/**
 * Class that implements the straightforward translation(Base Query) from Analyze query to cube query
 * @author mariosjkb
 *
 */
public class BaseQueryGenerator implements CubeQueryGenerator {

	private CubeManager cubeManager;
	
	public BaseQueryGenerator(CubeManager cubeManager) {
		this.cubeManager = cubeManager;
	}
	
	/**
	 * Method that generates Base cube query
	 * @return ArrayList&ltCubeQuery&gt that contains the Base query
	 */
	@Override
	public ArrayList<AnalyzeQuery> generateCubeQueries(String aggrFunc,
													 String measure,
													 String cubeName,
													 ArrayList<String> sigmaExpressions,
													 HashMap<String,String> sigmaExpressionsToValues,
													 ArrayList<String> gammaExpressions,
													 String queryAlias,
													 HashMap<String,String> dimensions,
													 HashMap<String,String> childToLevelById,
													 HashMap<String,String> childToLevelByName,
													 HashMap<String,String> parentToLevelById,
													 HashMap<String,String> parentToLevelByName,
													 HashMap<String,String> expressionsToTableName,
													 HashMap<String,String> currentLevelToDescriptions,
													 String schemaName,
													 String connectionType) {
		
		ArrayList<AnalyzeQuery> baseQuery = new ArrayList<AnalyzeQuery>();
		HashMap<String,String> queryParams = new HashMap<String,String>();
		CubeQuery analyzeCubeQuery = null;
		
		// set-up the query parameters
		cubeName = "CubeName:" + cubeName + "\n";
		String name = "Name:" + queryAlias + "-AnalyzeBaseQuery\n";
		aggrFunc = "AggrFunc:" + StringUtils.capitalize(aggrFunc) + "\n";
		measure = "Measure:" + measure + "\n";
		String gamma = "Gamma:";
		String sigma = "Sigma:";
		
		// set-up the gamma expressions
		for(String g:gammaExpressions) {
			gamma = gamma +  dimensions.get(g) + "." + g + ",";
		}
		// do this to erase the spare comma at the end of the expression
		gamma = gamma.substring(0,gamma.length()-1) + "\n";
		System.out.println("TEST B : " + dimensions);
		// set-up sigma expressions
		for(String s:sigmaExpressions) {
			sigma += dimensions.get(s) + "." + s + "=" + sigmaExpressionsToValues.get(s) + ",";
		}
		// do this to erase the spare comma at the end of the expression
		sigma = sigma.substring(0,sigma.length()-1) + "\n";
		
		// create cubeString and queryParams and create the query
		String cubeQString = cubeName + name + aggrFunc + measure + gamma + sigma;
		queryParams.put("CubeName", cubeName);
		queryParams.put("Name", "AnalyzeBaseQuery");
		queryParams.put("AggrFunc", aggrFunc);
		queryParams.put("Measure", measure);
		queryParams.put("Gamma", gamma);
		queryParams.put("Sigma", sigma);
		System.out.println("TEST TEST : " + queryParams.get("Sigma"));
		try {
			analyzeCubeQuery = cubeManager.createCubeQueryFromString(cubeQString, queryParams);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		// build AnalyzeQuery
		AnalyzeQuery analyzeBaseQuery = new AnalyzeQuery(analyzeCubeQuery,TypeOfAnalyzeQuery.Base,"none","not modified","none","not modified");
		// add the query to an ArrayList
		baseQuery.add(analyzeBaseQuery);
		
		return baseQuery;
	}

}
