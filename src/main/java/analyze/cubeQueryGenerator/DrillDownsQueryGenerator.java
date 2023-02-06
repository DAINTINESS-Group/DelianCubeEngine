package analyze.cubeQueryGenerator;

import java.rmi.RemoteException;
import java.util.ArrayList;
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
	private String[][] getChildrenValues(String tableName,String child,String currentLevel,String currentLevelValue) {
		Result queryResult = new Result();
		
		// set-up tableName and WHERE clause expression
		tableName = "pkdd99_star." + tableName;
		String whereClauseExpression = currentLevel + "=" + currentLevelValue;
		
		// create SQL query, execute it and get the result
		String SQLQuery = "SELECT DISTINCT " + child + " FROM " + tableName + " WHERE " + whereClauseExpression + ";";
		queryResult = cubeManager.getCubeBase().executeQueryToProduceResult(SQLQuery, queryResult);
		String[][] result = queryResult.getResultArray();
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
			                                        HashMap<String, String> sigmaExpressionsValues,
			                                        ArrayList<String> gammaExpressions, 
			                                        HashMap<String, String> dimensions, 
			                                        HashMap<String, String> childrenLevels,
			                                        HashMap<String, String> parentLevels,
			                                        HashMap<String, String> tableName,
			                                        HashMap<String, String> currentLevelDescriptions) {
		ArrayList<CubeQuery> drillDownQueries = new ArrayList<CubeQuery>();
		HashMap<String,String> queryParams = new HashMap<String,String>();
		CubeQuery analyzeDrillDownQuery = null;
		int counter = 1;
		
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
			String[][] childrenValues = getChildrenValues(tableName.get(expression),childrenLevels.get(expression),currentLevelDescriptions.get(expression),sigmaExpressionsValues.get(expression));
			
			// if the sigma expression dimension is the same with the dimension of some
			// gamma dimension, create the gamma expression
			// if there is no gamma dimension equal to the sigma dimension then we create no drill-down
			// for that sigma expression
			for(int j = 0;j < gammaExpressions.size();j++) {
				String gammaExpression = gammaExpressions.get(j);
				String gammaDimension = dimensions.get(gammaExpression);
				if(gammaDimension.equals(sigmaDimension)) {
					createDrillDown = true;
					gamma += gammaDimension + "." + childrenLevels.get(gammaExpression) + ",";
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
						String sigma = "Sigma:" + sigmaDimension + "." + childrenLevels.get(expression) + "=" + "\"" + childrenValues[j][k] + "\"";
						tempSigmas.add(sigma);
					}
				}
				for(String s: tempSigmas) {
					String temp = s;
					for(int j = 0;j < sigmaExpressions.size();j++) {
						if(i!=j) {
							temp += "," + dimensions.get(sigmaExpressions.get(j)) + "." + sigmaExpressions.get(j) + "=" + sigmaExpressionsValues.get(sigmaExpressions.get(j));
						}
					}
					sigmas.add(temp + "\n");
				}
				
				// finally, create the cube queries with the gamma and sigma expressions created above
				for(int j = 0;j < sigmas.size();j++) {
					String name = "Name:AnalyzeDrillDownQuery-" + counter + "\n";
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
					counter++;
				}
				// clear the sigma expression ArrayList
				sigmas.clear();
			}
		}

		return drillDownQueries;
	}
}
