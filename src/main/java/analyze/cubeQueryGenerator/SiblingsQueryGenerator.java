package analyze.cubeQueryGenerator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import result.Result;

/**
 * Class that implements the Sibling translation(Sibling Queries) from Analyze query to cube query.
 * @author mariosjkb
 *
 */
public class SiblingsQueryGenerator implements CubeQueryGenerator {
	
	private CubeManager cubeManager;
	
	public SiblingsQueryGenerator(CubeManager cubeManager) {
		this.cubeManager = cubeManager;
	}
	
	/**
	 * Method that queries the relational database and returns the parent value.
	 * @param tableName
	 * @param child
	 * @param currentLevel
	 * @param currentLevelValue
	 * @return A String[][] that contains the child values of a given current level
	 */
	private String getParentValue(String tableName,String parent,String currentLevel,String currentLevelValue) {
		Result queryResult = new Result();
		String retString;
		
		// set-up tableName and WHERE clause expression
		tableName = "pkdd99_star." + tableName;
		String whereClauseExpression = currentLevel + "=" + currentLevelValue;
		
		// if the parant is at the top of the hierarchy do not bother quering just return ALL value
		if(parent.contains("All")) {
			retString = "ALL";
			return retString;
		}
		
		// create the SQL Query, execute it and get the result
		String SQLQuery = "SELECT DISTINCT " + parent + " FROM " + tableName + " WHERE " + whereClauseExpression + ";";
		queryResult = cubeManager.getCubeBase().executeQueryToProduceResult(SQLQuery, queryResult);
		String[][] result = queryResult.getResultArray();
		retString = result[2][0];
		return retString;
	}

	/**
	 * Method that generates Sibling cube queries
	 * @return ArrayList&ltCubeQuery&gt that contains the Sibling queries
	 */
	@Override
	public ArrayList<CubeQuery> generateCubeQueries(String aggrFunc, 
													String measure, 
													String cubeName,
													ArrayList<String> sigmaExpressions, 
													HashMap<String, String> sigmaExpressionsValues,
													ArrayList<String> gammaExpressions, 
													HashMap<String, String> dimensions,
													HashMap<String, String> childrenLevels,
													HashMap<String, String> parentLevels,
													HashMap<String, String> tableNames,
													HashMap<String,String> currentLevelDescriptions) {
		
		ArrayList<CubeQuery> siblingQueries = new ArrayList<CubeQuery>();
		HashMap<String,String> queryParams = new HashMap<String,String>();
		CubeQuery analyzeSiblingQuery = null;
		int counter = 1;
		int indexGammaExpressions = gammaExpressions.size() - 1;
		
		// set-up query parameters
		cubeName = "CubeName:" + cubeName + "\n";
		aggrFunc = "AggrFunc:" + StringUtils.capitalize(aggrFunc) + "\n";
		measure = "Measure:" + measure + "\n";
		ArrayList<String> gammas = new ArrayList<String>();
		ArrayList<String> sigmas = new ArrayList<String>();
		
		// for each gamma expression, create the gamma expression string and check
		// the sigma expressions to see if any of them belongs to the same dimension
		// if a sigma expression belongs to the same dimension as the current gamma expression
		// then find its parent value and create the sigma expression string
		for(String g: gammaExpressions) {
			String gammaDimension = dimensions.get(g);
			String gamma = "Gamma:" + gammaDimension + "." + parentLevels.get(g) + "," +dimensions.get(gammaExpressions.get(indexGammaExpressions)) + "." + gammaExpressions.get(indexGammaExpressions) + "\n";
			String sigma = "Sigma:";
			gammas.add(gamma);
			indexGammaExpressions--;
			for(int i = 0;i < sigmaExpressions.size();i++) {
				String expression = sigmaExpressions.get(i);
				String sigmaDimension = dimensions.get(expression);
				if(gammaDimension.equals(sigmaDimension)) {
					String parentValue = getParentValue(tableNames.get(expression),parentLevels.get(expression),currentLevelDescriptions.get(expression),sigmaExpressionsValues.get(expression));
					sigma += sigmaDimension + "." + parentLevels.get(expression) + "=" + "\"" + parentValue + "\"" + ",";
				}else {
					sigma += sigmaDimension + "." + expression + "=" + sigmaExpressionsValues.get(expression) + ",";
				}
			}
			// do this to erase the spare comma at the end of the expression
			sigmas.add(sigma.substring(0,sigma.length()-1) + "\n");
		}
		
		// finally, create the cube queries with the gamma and sigma expressions created above
		for(int i = 0;i < gammas.size();i++) {
			String name = "Name:AnalyzeSiblingQuery-" + counter + "\n";
			String cubeQString = cubeName + name + aggrFunc + measure + gammas.get(i) + sigmas.get(i);
			queryParams.put("CubeName", cubeName);
			queryParams.put("Name", name);
			queryParams.put("AggrFunc", aggrFunc);
			queryParams.put("Measure", measure);
			queryParams.put("Gamma", gammas.get(i));
			queryParams.put("Sigma", sigmas.get(i));
	
			try {
				analyzeSiblingQuery = cubeManager.createCubeQueryFromString(cubeQString, queryParams);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			siblingQueries.add(analyzeSiblingQuery);
			counter++;
		}
		return siblingQueries;
	}

}
