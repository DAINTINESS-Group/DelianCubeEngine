package analyze.cubeQueryGenerator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import result.Result;

/**
 * Class that implements the Drill-Down translation(Drill-Down Queries) from Analyze query to cube query.
 * @author mariosjkb
 *
 */
public class DrillDownsQueryGenerator implements CubeQueryGenerator{
	
	private CubeManager cubeManager;
	
	public DrillDownsQueryGenerator(CubeManager cubeManager) {
		this.cubeManager = cubeManager;
	}
	
	/**
	 * Method that queries the relational database and returns the child values of a current level and its value.
	 * @param tableName
	 * @param child
	 * @param currentLevel
	 * @param currentLevelValue
	 * @return A String[][] that contains the child values of a given current level
	 */
	private String[][] getChildrenValues(String schemaName,String tableName,String child,String currentLevel,String currentLevelValue,String connectionType) {
		Result queryResult = new Result();
		
		// set-up tableName and WHERE clause expression
		// if RDBMS is used, add schemaName to the tableName
		if(connectionType.equals("RDBMS")){
			tableName = schemaName + "." + tableName;
		}
		String whereClauseExpression = currentLevel + "=" + currentLevelValue;
		
		// create SQL query, execute it and get the result
		String SQLQuery = "SELECT DISTINCT " + child + " FROM " + tableName + " WHERE " + whereClauseExpression + ";";
		queryResult = cubeManager.getCubeBase().executeQueryToProduceResult(SQLQuery, queryResult);
		String[][] result = queryResult.getResultArray();
		
		// if spark connection is used, sort the result to make testing easier
		// and modify the array so as the bottom 2 values that contain the attribute
		// info to be on top of the array in order to be processed later
		if(connectionType.equals("Spark")) {
			Arrays.sort(result ,new Comparator<String[]>(){
			    @Override
			    public int compare(String[] first, String[] second){
			    	int comparedTo = 0;
			        // compare the first element
			    	if(first[0] != null && second[0] != null) {
			    		comparedTo = first[0].compareTo(second[0]);
			    	}
			 
			        return comparedTo;
			    }
			});
			
			String lastElement = result[result.length-1][0];
			String secondLastElement = result[result.length-2][0];
			
			for(int i = result.length-1;i > 1;i--) {
				result[i][0] = result[i-2][0];
			}
			
			result[0][0] = lastElement;
			result[1][0] = secondLastElement;
		}
		return result;
	}

	/**
	 * Method that generates Drill-Down cube queries
	 * @return ArrayList&ltCubeQuery&gt that contains the Drill-Down queries
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
			                                        HashMap<String, String> currentLevelToDescriptions,
			                                        String schemaName,
			                                        String connectionType) {
		ArrayList<CubeQuery> drillDownQueries = new ArrayList<CubeQuery>();
		HashMap<String,String> queryParams = new HashMap<String,String>();
		CubeQuery analyzeDrillDownQuery = null;
		
		// set-up query parameters
		cubeName = "CubeName:" + cubeName + "\n";
		aggrFunc = "AggrFunc:" + StringUtils.capitalize(aggrFunc) + "\n";
		measure = "Measure:" + measure + "\n";
		ArrayList<String> sigmas = new ArrayList<String>();
		
		// for each sigma expression, get its child level values and if
		// there is a gamma expression that belongs to the same dimension
		// create the gamma and sigma expressions and then the cube queries
		for(int i = 0;i < sigmaExpressions.size();i++) {
			boolean createDrillDown = false;
			ArrayList<String> tempSigmas = new ArrayList<String>();
			String gamma = "Gamma:";
			String expression = sigmaExpressions.get(i);
			String sigmaDimension = dimensions.get(expression);
			String[][] childrenValues = getChildrenValues(schemaName,expressionToTableName.get(expression),childToLevel.get(expression),currentLevelToDescriptions.get(expression),sigmaExpressionsToValues.get(expression),connectionType);
			
			// if the sigma expression dimension is the same with the dimension of some
			// gamma dimension, create the gamma expression
			// if there is no gamma dimension equal to the sigma dimension then we create no drill-down
			// for that sigma expression
			for(int j = 0;j < gammaExpressions.size();j++) {
				String gammaExpression = gammaExpressions.get(j);
				String gammaDimension = dimensions.get(gammaExpression);
				if(gammaDimension.equals(sigmaDimension)) {
					createDrillDown = true;
					gamma += gammaDimension + "." + childToLevel.get(gammaExpression) + ",";
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
			if(createDrillDown == true) {
				for(int j = 2;j < childrenValues.length;j++) {
					for(int k = 0;k < childrenValues[k].length;k++) {
						String sigma = "Sigma:" + sigmaDimension + "." + childToLevel.get(expression) + "=" + "\'" + childrenValues[j][k] + "\'";
						tempSigmas.add(sigma);
					}
				}
				for(String s: tempSigmas) {
					String temp = s;
					for(int j = 0;j < sigmaExpressions.size();j++) {
						if(i!=j) {
							temp += "," + dimensions.get(sigmaExpressions.get(j)) + "." + sigmaExpressions.get(j) + "=" + sigmaExpressionsToValues.get(sigmaExpressions.get(j));
						}
					}
					sigmas.add(temp + "\n");
				}
				
				// finally, create the cube queries with the gamma and sigma expressions created above
				for(int j = 0;j < sigmas.size();j++) {
					String name = "Name:" + queryAlias + "-AnalyzeDrillDownQuery_" + dimensions.get(expression) + "_" + (j+1) + "\n";
					String cubeQString = cubeName + name + aggrFunc + measure + gamma + sigmas.get(j);
					queryParams.put("CubeName", cubeName);
					queryParams.put("Name", name);
					queryParams.put("AggrFunc", aggrFunc);
					queryParams.put("Measure", measure);
					queryParams.put("Gamma", gamma);
					queryParams.put("Sigma", sigmas.get(j));
			
					try {
						analyzeDrillDownQuery = cubeManager.createCubeQueryFromString(cubeQString, queryParams);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					drillDownQueries.add(analyzeDrillDownQuery);
				}
				// clear the sigma expression ArrayList
				sigmas.clear();
			}
		}

		return drillDownQueries;
	}
}
