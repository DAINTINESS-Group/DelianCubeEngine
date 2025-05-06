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

public class DuoQueryOptimizerSiblingsGenerator implements CubeQueryGenerator{
	
	private CubeManager cubeManager;
	
	public DuoQueryOptimizerSiblingsGenerator(CubeManager cubeManager) {
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
		
		// if the parent is at the top of the hierarchy do not bother quering just return ALL value
		if(parent.contains("All")) {
			retString = "ALL";
			return retString;
		}
		
		// create the SQL Query, execute it and get the result
		String SQLQuery = "SELECT DISTINCT " + parent + " FROM " + tableName + " WHERE " + whereClauseExpression + ";";
		System.out.println(SQLQuery);
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

	@Override
	public ArrayList<AnalyzeQuery> generateCubeQueries(String aggrFunc, String measure, String cubeName,
			ArrayList<String> sigmaExpressions, HashMap<String, String> sigmaExpressionsToValues,
			ArrayList<String> gammaExpressions, String queryAlias, HashMap<String, String> dimensions,
			HashMap<String, String> childToLevelById, HashMap<String, String> childToLevelByName,
			HashMap<String, String> parentToLevelById, HashMap<String, String> parentToLevelByName,
			HashMap<String, String> expressionToTableName, HashMap<String, String> currentLevelToDescriptions,
			String schemaName, String connectionType) {
		
		ArrayList<AnalyzeQuery> siblingsQuery = new ArrayList<AnalyzeQuery>();
		HashMap<String,String> queryParams = new HashMap<String,String>();
		CubeQuery analyzeCubeQuery = null;
		
		// set-up query parameters
		cubeName = "CubeName:" + cubeName + "\n";
		aggrFunc = "AggrFunc:" + StringUtils.capitalize(aggrFunc) + "\n";
		measure = "Measure:" + measure + "\n";
		String prevGamma = "";
		String prevSigma = "";
		String gamma = "Gamma:";
		String sigma = "Sigma:";
		
		
		String originalGammaExpression = null;
		String gammaDimension = null;
		String originalSigmaExpression = null;
		String sigmaDimension = null;
		ArrayList<String> dimensionsFound = new ArrayList<>();
		
		for(int i=0; i<gammaExpressions.size(); i++) {
			originalGammaExpression = gammaExpressions.get(i);
			gammaDimension = dimensions.get(originalGammaExpression);
			dimensionsFound.add(gammaDimension);
			prevGamma += gammaDimension + "." + originalGammaExpression + ", ";
			if(gammaDimension == null) {
				return siblingsQuery;
			}
			gamma += gammaDimension + "." + originalGammaExpression + ", ";
		}
		
		
		
		for(int i=0; i<sigmaExpressions.size(); i++) {
			originalSigmaExpression = sigmaExpressions.get(i);
			sigmaDimension = dimensions.get(originalSigmaExpression);
			if(sigmaDimension == null) {
				return siblingsQuery;
			}
			prevSigma += sigmaDimension + "." + originalSigmaExpression + "=" + sigmaExpressionsToValues.get(sigmaExpressions.get(i)) + ", ";
			String parentValue = getParentValue(schemaName,expressionToTableName.get(originalSigmaExpression),parentToLevelById.get(originalSigmaExpression),currentLevelToDescriptions.get(originalSigmaExpression),sigmaExpressionsToValues.get(originalSigmaExpression),connectionType);
			if(parentValue == null) {
				return siblingsQuery;
			}			
			for(int j=0; j<gammaExpressions.size(); j++) {
				originalGammaExpression = gammaExpressions.get(j);
				gammaDimension = dimensions.get(originalGammaExpression);
				
				if(gammaDimension == null) {
					return siblingsQuery;
				}
				if(sigmaDimension.equals(gammaDimension)) {					
					//new gamma level becomes the level of the dimension, found at the respective filter of the base query 
					String newGamma = originalSigmaExpression;
					gamma += gammaDimension + "." + newGamma + ", ";
					sigma += sigmaDimension + "." + parentToLevelByName.get(originalSigmaExpression) + "=" + "\'" + parentValue + "\'" + ", ";
					continue;
				} 	
			}
			if(!dimensionsFound.contains(sigmaDimension)) {
				sigma += sigmaDimension + "." + originalSigmaExpression + "=" + sigmaExpressionsToValues.get(sigmaExpressions.get(i)) + ", ";
			}
		}
		prevSigma = prevSigma.substring(0,prevSigma.length()-2) + "\n";
		prevGamma = prevGamma.substring(0,prevGamma.length()-2) + "\n";
		gamma = gamma.substring(0,gamma.length()-2) + "\n";
		sigma = sigma.substring(0,sigma.length()-2) + "\n";
		
		String name = "Name:" + queryAlias + "-SiblingsDuoQueryOptimizer" + "\n";
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
		AnalyzeQuery optimizerQuery = new AnalyzeQuery(analyzeCubeQuery,TypeOfAnalyzeQuery.DUOQUERYSIBLINGSOPTIMIZER,prevSigma.substring(0,prevSigma.length()-1),sigma.substring(6,sigma.length()-1),prevGamma.substring(0,prevGamma.length()-1), gamma.substring(6,gamma.length()-1));
		siblingsQuery.add(optimizerQuery);
		
		return siblingsQuery;
	}

}
