package mainengine;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.commons.lang3.StringUtils;
import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;


/**
 * 
 * A class for the intentional operator Analyze
 *
 */

public class AnalyzeOperator {
	private String incomingExpression;
	private CubeManager cubeManager;
	private HashSet<String> stopWords;
	private ArrayList<CubeQuery> analyzeCubeQueries = new ArrayList<CubeQuery>();
	
	public AnalyzeOperator(String incomingExpression, CubeManager cubeManager) {
		this.incomingExpression = incomingExpression;
		this.cubeManager = cubeManager;
		this.stopWords = new HashSet<>(Arrays.asList("AND"," "));
	}
	
	/** Auxiliary method that prints the cube queries that generated from the analyze input expression.
	 * 
	 * @return a string with the cube queries
	 */
	public String getAnalyzeCubeQueries() {
		String retString = "";
		for(CubeQuery cq: analyzeCubeQueries) {
			retString += cq.toString() + "\n";
		}
		return retString;
	}

	/**
	 * Converts an analyze intentional query to the respective CubeQuery objects via simple string processing.<p>
	 * The syntax of Analyze operator is: ANALYZE Aggregation Function Measure FROM CubeName FOR Sigma Expressions BY Gamma Expressions.<p>
	 * Sigma Expressions are seperated with the word AND. Gamma Expressions are seperated with commas(,).<p>
	 * Sigma Expressions must not contain gaps. For example region=Prague is acceptable but region = Prague is not. Also, if the value of a
	 * sigma expression contains gaps replace them with underscores(_) and the method will replace them back to gaps.
	 * No '' are needed to surround the values of a sigma expression, they are added by the method too.<p>
	 * The method fills the analyzeCubeQueries ArrayList with the base cube query, its drill-downs and its siblings.
	 */
	
	private void translateInputAnalyzeExpressionToCubeQueries() {
		// CubeQuery string attributes
		String cubeName;
		String name;
		String aggregationFunction;
		String measure;
		String sigmaExpressions;
		String gammaExpressions;
		
		String tokens = "";
		String level= "";
		String sigmaExpressionSplitted;
		String sigmaExpressionValue;
		String cubeQString;
		boolean found = false;
		String[] sigmaExpressionsArray;
		String[] gammaExpressionsArray;
		
		// HashMap that will produce the CubeQuery
		HashMap<String,String> queryParams = new HashMap<String,String>();
		CubeQuery analyzeBaseQuery = null;
		
		// find the tokens of the incoming expression
		String[] splittedExpression = this.incomingExpression.split(" ");
		for(int i = 0;i<splittedExpression.length;i++) {
			if(!this.stopWords.contains(splittedExpression[i])) {
				if(splittedExpression[i].charAt(splittedExpression[i].length() - 1) == ',') {
					splittedExpression[i] = splittedExpression[i].substring(0, splittedExpression[i].length() - 1);
				}
				tokens += splittedExpression[i] + " ";
			}
		}
		// extract CubeQuery attributes from token list
		aggregationFunction = tokens.split("FROM")[0].split(" ")[1];
		measure = tokens.split("FROM")[0].split(" ")[2];
		cubeName = tokens.split("FOR")[0].split(" ")[4];
		sigmaExpressionsArray = tokens.split("FOR")[1].split("BY")[0].split(" ");
		gammaExpressionsArray = tokens.split("BY")[1].split(" ");
		
		// find the dimensions of the gamma Expressions
		for(int i = 1;i < gammaExpressionsArray.length;i++)
			for(int j = 0;j < cubeManager.getDimensions().size();j++) {
				for(int k = 0;k < cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().size();k++) {
					level = cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(k).getName();
					if(level.equals(gammaExpressionsArray[i])) {
						gammaExpressionsArray[i] = cubeManager.getDimensions().get(j).getName() + "." + gammaExpressionsArray[i];
						found = true;
						break;
					}
				}
				if(found) {
					found = false;
					break;
				}
			}
		
		// find the dimensions of the sigma expressions
		for(int i = 1;i < sigmaExpressionsArray.length;i++) {
			sigmaExpressionSplitted = sigmaExpressionsArray[i].split("=")[0];
			sigmaExpressionValue =  sigmaExpressionsArray[i].split("=")[1];
			for(int j = 0;j < cubeManager.getDimensions().size();j++) {
				for(int k = 0;k < cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().size();k++) {
					level = cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(k).getName();
					if(level.equals(sigmaExpressionSplitted)) {
						if(sigmaExpressionValue.contains("_")) {
							sigmaExpressionValue = sigmaExpressionValue.replace("_" , " ");
						}
						sigmaExpressionsArray[i] = cubeManager.getDimensions().get(j).getName() + "." + sigmaExpressionSplitted + "=" + "'" + sigmaExpressionValue + "'";
						found = true;
						break;
					}
				}
				if(found) {
					found = false;
					break;
				}
			}
		}
		
		// set up the string to produce the CubeQuery
		cubeName = "CubeName:" + cubeName + "\n";
		name = "Name:AnalyzeBaseQuery" + "\n";
		aggregationFunction = "AggrFunc:" + StringUtils.capitalize(aggregationFunction) + "\n";
		measure = "Measure:" + measure + "\n";
		gammaExpressions = "Gamma:";
		for(int i = 1;i < gammaExpressionsArray.length;i++) {
			if(i <  gammaExpressionsArray.length - 1) {
				gammaExpressions += gammaExpressionsArray[i] + ",";
			}else {
				gammaExpressions += gammaExpressionsArray[i];
			}
		}
		gammaExpressions += "\n";
		sigmaExpressions = "Sigma:";
		for(int i = 1;i < sigmaExpressionsArray.length;i++) {
			if(i <  sigmaExpressionsArray.length - 1) {
				sigmaExpressions += sigmaExpressionsArray[i] + ",";
			}else {
				sigmaExpressions += sigmaExpressionsArray[i];
			}
		}
		sigmaExpressions += "\n";
		cubeQString = cubeName + name + aggregationFunction + measure + gammaExpressions + sigmaExpressions;
		
		// set up the queryParams HashMap
		queryParams.put("CubeName", cubeName);
		queryParams.put("Name", "AnalyzeBaseQuery");
		queryParams.put("AggrFunc", aggregationFunction);
		queryParams.put("Measure", measure);
		queryParams.put("Gamma", gammaExpressions);
		queryParams.put("Sigma", sigmaExpressions);
		
		// create the Cube Query and store it
		try {
			analyzeBaseQuery = cubeManager.createCubeQueryFromString(cubeQString, queryParams);
			analyzeCubeQueries.add(analyzeBaseQuery);
	
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		// calculate BaseQuery drill-downs
		generateDrillDowns(cubeName,aggregationFunction,measure,gammaExpressions,sigmaExpressions);
	}
	
	/**
	 * Generates the drill-down CubeQueries of the Base Analyze Cube Query.<p>
	 * After drill-down CubeQueries creation, the method adds it to the analyzeCubeQueries ArrayList.
	 * @param cubeName
	 * @param aggregationFunction
	 * @param measure:
	 * @param gammaExpressions:
	 * @param sigmaExpressions:
	 */
	
	private void generateDrillDowns(String cubeName,String aggregationFunction, String measure, String gammaExpressions, String sigmaExpressions) {
		String dimension;
		String currentLevel;
		String drillDownGammaExpression;
		String drillDownCubeQString;
		int positionInHierarchy;
		int index = 0;
		int maxIndex;
		boolean firstDimensionInBottom = false;
		CubeQuery drillDownAnalyzeBaseQuery;
		ArrayList<String> drillDownExpressions = new ArrayList<String>();
		ArrayList<String> drillDownGammaExpressions = new ArrayList<String>();
		HashMap<String,String> drillDownQueryParams = new HashMap<String,String>();
		
		// Split gamma expression string to get the actual expressions and save the number of expressions
		String[] expressions = gammaExpressions.split(":")[1].split(",");
		maxIndex = expressions.length;
		
		// for each grouper dimension go one level down in the hierarchy and save the new gamma expression on an ArrayList 
		for(int i = 0;i < expressions.length;i++) {
			dimension = expressions[i].split("\\.")[0];
			currentLevel = expressions[i].split("\\.")[1].replace("\n", "");
			for(int j = 0;j < cubeManager.getDimensions().size();j++) {
				if(cubeManager.getDimensions().get(j).getName().equals(dimension)) {
					for(int k = 0;k < cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().size();k++) {
						if(cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(k).getName().equals(currentLevel)) {
							positionInHierarchy = cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(k).getPositionInHierarchy();
							if(positionInHierarchy != 0) {
								String drillDownExpression = dimension + "." + cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(k-1).getName();
								drillDownExpressions.add(drillDownExpression);
							}else {
								if(i == 0) {
									firstDimensionInBottom = true;
								}
							}
						}
					}
				}
			}
		}
		
		// create the new string expressions for the drill-downs
		for(int i = 0;i < drillDownExpressions.size();i++) {
			if(i == index) {
				if(firstDimensionInBottom) {
					index = i;
					maxIndex -=1;
				}else {
					index++;
				}
				while(index < maxIndex) {
					if(firstDimensionInBottom) {
						drillDownGammaExpression = "Gamma:" + expressions[index] + "," + drillDownExpressions.get(i);
					}else {
						drillDownGammaExpression = "Gamma:" + drillDownExpressions.get(i) + "," + expressions[index];
					}
					drillDownGammaExpression = drillDownGammaExpression.replace("\n","");
					drillDownGammaExpression += "\n";
					drillDownGammaExpressions.add(drillDownGammaExpression);
					index++;
				}
			}else {
				while(index < i) {
					drillDownGammaExpression = "Gamma:" + expressions[index] + "," + drillDownExpressions.get(i);
					drillDownGammaExpression = drillDownGammaExpression.replace("\n","");
					drillDownGammaExpression += "\n";
					drillDownGammaExpressions.add(drillDownGammaExpression);
					index++;
				}
			}
			index = 0;
		}
		
		// set-up the necessary parameters to create the drill-down query
		for(int i = 0;i < drillDownGammaExpressions.size();i++) {
			drillDownQueryParams.clear();
			drillDownCubeQString = cubeName + "Name:AnalyzeBaseQuery-Drill Down " + (i+1) + "\n" + aggregationFunction + measure + drillDownGammaExpressions.get(i) + sigmaExpressions;
			drillDownQueryParams.put("CubeName", cubeName);
			drillDownQueryParams.put("Name", "AnalyzeBaseQuery-Drill Down " + (i+1));
			drillDownQueryParams.put("AggrFunc", aggregationFunction);
			drillDownQueryParams.put("Measure", measure);
			drillDownQueryParams.put("Gamma", drillDownGammaExpressions.get(i));
			drillDownQueryParams.put("Sigma", sigmaExpressions);
			
			// create the query and store it
			try {
				drillDownAnalyzeBaseQuery = cubeManager.createCubeQueryFromString(drillDownCubeQString, drillDownQueryParams);
				analyzeCubeQueries.add(drillDownAnalyzeBaseQuery);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** At this point the method creates the base query, the drill-down and prints them.
	 * 
	 * @return (for now) it returns the status of the method.
	 */
	public int execute() {
		// TODO Auto-generated method stub
		//this must return a Intentional Result object, not null, not void, not int
		int status = -1;
		translateInputAnalyzeExpressionToCubeQueries();
		return status;
	}
}
