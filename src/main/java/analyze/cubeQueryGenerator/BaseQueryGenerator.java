package analyze.cubeQueryGenerator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

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
	public ArrayList<CubeQuery> generateCubeQueries(String aggrFunc,
													 String measure,
													 String cubeName,
													 ArrayList<String> sigmaExpressions,
													 HashMap<String,String> sigmaExpressionsValues,
													 ArrayList<String> gammaExpressions,
													 HashMap<String,String> dimensions,
													 HashMap<String,String> childrenLevels,
													 HashMap<String,String> parentLevels,
													 HashMap<String,String> tableName,
													 HashMap<String,String> currentLevelDescriptions) {
		
		ArrayList<CubeQuery> baseQuery = new ArrayList<CubeQuery>();
		HashMap<String,String> queryParams = new HashMap<String,String>();
		CubeQuery analyzeBaseQuery = null;
		
		// set-up the query parameters
		cubeName = "CubeName:" + cubeName + "\n";
		String name = "Name:AnalyzeBaseQuery\n";
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
		
		// set-up sigma expressions
		for(String s:sigmaExpressions) {
			sigma += dimensions.get(s) + "." + s + "=" + sigmaExpressionsValues.get(s) + ",";
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
		
		try {
			analyzeBaseQuery = cubeManager.createCubeQueryFromString(cubeQString, queryParams);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		// add the query to an ArrayList
		baseQuery.add(analyzeBaseQuery);
		
		return baseQuery;
	}

}
