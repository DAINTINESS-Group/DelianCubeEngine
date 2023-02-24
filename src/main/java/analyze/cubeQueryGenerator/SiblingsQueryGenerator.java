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
	private String getParentValue(String schemaName,String tableName,String parent,String currentLevel,String currentLevelValue) {
		Result queryResult = new Result();
		String retString;
		
		// set-up tableName and WHERE clause expression
		tableName = schemaName + "." + tableName;
		String whereClauseExpression = currentLevel + "=" + currentLevelValue;
		
		// if the parent is at the top of the hierarchy do not bother quering just return ALL value
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
													HashMap<String, String> sigmaExpressionsToValues,
													ArrayList<String> gammaExpressions,
													String queryAlias,
													HashMap<String, String> dimensions,
													HashMap<String, String> childToLevel,
													HashMap<String, String> parentToLevel,
													HashMap<String, String> expressionToTableName,
													HashMap<String,String> currentLevelToDescriptions,
													String schemaName) {
		
		ArrayList<CubeQuery> siblingQueries = new ArrayList<CubeQuery>();
		HashMap<String,String> queryParams = new HashMap<String,String>();
		CubeQuery analyzeSiblingQuery = null;
		
		// set-up query parameters
		cubeName = "CubeName:" + cubeName + "\n";
		aggrFunc = "AggrFunc:" + StringUtils.capitalize(aggrFunc) + "\n";
		measure = "Measure:" + measure + "\n";
		
		// for each sigma expression, get its parent level values and if
		// there is a gamma expression that belongs to the same dimension
		// create the gamma and sigma expressions and then the cube queries
		for(int i = 0;i < sigmaExpressions.size();i++) {
			boolean createSibling = false;
			String gamma = "Gamma:";
			String expression = sigmaExpressions.get(i);
			String sigmaDimension = dimensions.get(expression);
			String parentValue = getParentValue(schemaName,expressionToTableName.get(expression),parentToLevel.get(expression),currentLevelToDescriptions.get(expression),sigmaExpressionsToValues.get(expression));
					
			// if the sigma expression dimension is the same with the dimension of some
			// gamma dimension, create the gamma expression
			// if there is no gamma dimension equal to the sigma dimension then we create no siblings
			// for that sigma expression
			for(int j = 0;j < gammaExpressions.size();j++) {
				String gammaExpression = gammaExpressions.get(j);
				String gammaDimension = dimensions.get(gammaExpression);
				if(gammaDimension.equals(sigmaDimension)) {
					createSibling = true;
					gamma += gammaDimension + "." + parentToLevel.get(gammaExpression) + ",";
				}else {
					gamma +=  gammaDimension + "." + gammaExpression + ",";
				}
			}
					
			// do this to erase the spare comma at the end of the expression
			gamma = gamma.substring(0,gamma.length()-1) + "\n";
					
			// then create the sigma expression string by adding the child level values and then pace it to a temp collection
			// with the correct string format. Then add the rest of the sigma expression to the items 
			// of the temp collection to create the sigma expressions. Note that the j counter starts from 2
			// because the result's first 2 rows contains the field name of the result.
			if(createSibling == true) {
				String sigma = "Sigma:" + sigmaDimension + "." + parentToLevel.get(expression) + "=" + "\'" + parentValue + "\'";

				for(int j = 0;j < sigmaExpressions.size();j++) {
					if(i!=j) {
						sigma += "," + dimensions.get(sigmaExpressions.get(j)) + "." + sigmaExpressions.get(j) + "=" + sigmaExpressionsToValues.get(sigmaExpressions.get(j));
					}
				}
				
				// finally, create the cube queries with the gamma and sigma expressions created above
				String name = "Name:" + queryAlias + "-AnalyzeSiblingQuery_" + dimensions.get(expression) + "\n";
				String cubeQString = cubeName + name + aggrFunc + measure + gamma + sigma;
				queryParams.put("CubeName", cubeName);
				queryParams.put("Name", name);
				queryParams.put("AggrFunc", aggrFunc);
				queryParams.put("Measure", measure);
				queryParams.put("Gamma", gamma);
				queryParams.put("Sigma", sigma);
	
				try {
					analyzeSiblingQuery = cubeManager.createCubeQueryFromString(cubeQString, queryParams);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				siblingQueries.add(analyzeSiblingQuery);
			}
		}
		return siblingQueries;
	}

}
