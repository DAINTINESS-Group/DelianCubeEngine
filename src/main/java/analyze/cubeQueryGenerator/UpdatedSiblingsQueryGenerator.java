package analyze.cubeQueryGenerator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import analyze.AnalyzeQuery;
import analyze.AnalyzeQuery.TypeOfAnalyzeQuery;
import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import result.Result;

/**
 * Class that implements the Sibling translation(Sibling Queries) from Analyze query to cube query.
 * @author mariosjkb
 *
 */
public class UpdatedSiblingsQueryGenerator implements CubeQueryGenerator {
	
	private CubeManager cubeManager;
	
	public UpdatedSiblingsQueryGenerator(CubeManager cubeManager) {
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
	private String getParentValue(String schemaName,String tableName,String parent,String currentLevel,String currentLevelValue,String connectionType) {
		Result queryResult = new Result();
		String retString;
		
		if(parent == null) {
			return null;
		}
		// set-up tableName and WHERE clause expression
		// if RDBMS is used, add schemaName to the tableName
		if(connectionType.equals("RDBMS")) {
			tableName = schemaName + "." + tableName;
		}
		String whereClauseExpression = currentLevel + "=" + currentLevelValue;
		
		// if the parent is at the top of the hierarchy do not bother querying, just return ALL value
		if(parent.contains("All")) {
			retString = "ALL";
			return retString;
		}
		
		// create the SQL Query, execute it and get the result
		String SQLQuery = "SELECT DISTINCT " + parent + " FROM " + tableName + " WHERE " + whereClauseExpression + ";";
		System.out.println("The query that gets the parent values: " + SQLQuery);
		queryResult = cubeManager.getCubeBase().executeQueryToProduceResult(SQLQuery, queryResult);
		String[][] result = queryResult.getResultArray();
		if(result == null) {
			return null;
		}
		
		// if spark connection is used modify the result so as the last 2 values of it 
		// that are the attribute info to be on top of the array and not in the bottom
		// in order to be processed later
		if(connectionType.equals("Spark")) {
			
			String lastElement = result[result.length-1][0];
			String secondLastElement = result[result.length-2][0];
			
			for(int i = result.length-1;i > 1;i--) {
				result[i][0] = result[i-2][0];
			}
			
			result[0][0] = lastElement;
			result[1][0] = secondLastElement;
		}
		retString = result[2][0];
		return retString;
	}

	/**
	 * Method that generates Sibling cube queries
	 * @return ArrayList&ltCubeQuery&gt that contains the Sibling queries
	 */
	@Override
	public ArrayList<AnalyzeQuery> generateCubeQueries(String aggrFunc, 
													String measure, 
													String cubeName,
													ArrayList<String> sigmaExpressions, 
													HashMap<String, String> sigmaExpressionsToValues,
													ArrayList<String> gammaExpressions,
													String queryAlias,
													HashMap<String, String> dimensions,
													HashMap<String,String> childToLevelById,
													HashMap<String,String> childToLevelByName,
													HashMap<String,String> parentToLevelById,
													HashMap<String,String> parentToLevelByName,
													HashMap<String, String> expressionToTableName,
													HashMap<String,String> currentLevelToDescriptions,
													String schemaName,
													String connectionType) {
		
		ArrayList<AnalyzeQuery> siblingQueries = new ArrayList<AnalyzeQuery>();
		HashMap<String,String> queryParams = new HashMap<String,String>();
		CubeQuery analyzeCubeQuery = null;
		
		// set-up query parameters
		cubeName = "CubeName:" + cubeName + "\n";
		aggrFunc = "AggrFunc:" + StringUtils.capitalize(aggrFunc) + "\n";
		measure = "Measure:" + measure + "\n";
		
		// for each sigma expression, get its parent level domain values and if
		// there is a gamma expression that belongs to the same dimension
		// then the gamma level becomes equal to the dimension level that the FILTER had at the base query
		// then, create the new gamma and sigma expressions and then the cube queries
		for(int i = 0;i < sigmaExpressions.size();i++) {
			boolean createSibling = false;
			String prevGamma = null;
			String newGamma = null;
			String prevSigma = null;
			String newSigma = null;
			String gamma = "Gamma:";
			String sigmaExpression = sigmaExpressions.get(i);
			String sigmaDimension = dimensions.get(sigmaExpression);
			String parentValue = getParentValue(schemaName,expressionToTableName.get(sigmaExpression),parentToLevelById.get(sigmaExpression),currentLevelToDescriptions.get(sigmaExpression),sigmaExpressionsToValues.get(sigmaExpression),connectionType);
			if(parentValue == null) {
				return siblingQueries;
			}
					
			// if the sigma expression dimension is the same with the dimension of some
			// gamma dimension, create the gamma expression
			// if there is no gamma dimension equal to the sigma dimension then we create no siblings
			// for that sigma expression
			for(int j = 0;j < gammaExpressions.size();j++) {
				String gammaExpression = gammaExpressions.get(j);
				String gammaDimension = dimensions.get(gammaExpression);
				if(gammaDimension == null) {
					return siblingQueries;
				}
				if(gammaDimension.equals(sigmaDimension)) {
					createSibling = true;
					prevGamma = gammaExpression;
					//newGamma = parentToLevelByName.get(gammaExpression);
					
					//new gamma level becomes the level of the dimension, found at the respective filter of the base query 
					newGamma = sigmaExpression;
					gamma += gammaDimension + "." + newGamma + ",";
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
				String sigma = "Sigma:" + sigmaDimension + "." + parentToLevelByName.get(sigmaExpression) + "=" + "\'" + parentValue + "\'";
				newSigma = "'" + parentValue + "'";
				prevSigma = sigmaExpressionsToValues.get(sigmaExpression);
				for(int j = 0;j < sigmaExpressions.size();j++) {
					if(i!=j) {
						sigma += "," + dimensions.get(sigmaExpressions.get(j)) + "." + sigmaExpressions.get(j) + "=" + sigmaExpressionsToValues.get(sigmaExpressions.get(j));
					}
				}
				
				// finally, create the cube queries with the gamma and sigma expressions created above
				String name = "Name:" + queryAlias + "-AnalyzeSiblingQuery_" + dimensions.get(sigmaExpression) + "\n";
				String cubeQString = cubeName + name + aggrFunc + measure + gamma + sigma;
				queryParams.put("CubeName", cubeName);
				queryParams.put("Name", name);
				queryParams.put("AggrFunc", aggrFunc);
				queryParams.put("Measure", measure);
				queryParams.put("Gamma", gamma);
				queryParams.put("Sigma", sigma);
	
				try {
					analyzeCubeQuery = cubeManager.createCubeQueryFromString(cubeQString, queryParams);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				
				//build AnalyzeQuery
				AnalyzeQuery analyzeSiblingQuery = new AnalyzeQuery(analyzeCubeQuery,TypeOfAnalyzeQuery.UPDATED_SIBLINGS,prevSigma,newSigma,prevGamma,newGamma);
				siblingQueries.add(analyzeSiblingQuery);
			}
		}
		return siblingQueries;
	}

}
