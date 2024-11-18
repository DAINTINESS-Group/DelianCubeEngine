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
 * Class that implements the traditional Drill-Down translation(Drill-Down Queries) from Analyze query to cube query.
 * @author DimosGkitsakis
 *
 */
public class TraditionalDrillDownsQueryGenerator implements CubeQueryGenerator{

	private CubeManager cubeManager;
	
	public TraditionalDrillDownsQueryGenerator(CubeManager cubeManager) {
		this.cubeManager = cubeManager;
	}
	

	/**
	 * Method that generates Drill-Down cube queries
	 * @return ArrayList&ltCubeQuery&gt that contains the Drill-Down queries
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
			                                        HashMap<String, String> currentLevelToDescriptions,
			                                        String schemaName,
			                                        String connectionType) {
		ArrayList<AnalyzeQuery> drillDownQueries = new ArrayList<AnalyzeQuery>();
		HashMap<String,String> queryParams = new HashMap<String,String>();
		CubeQuery analyzeCubeQuery = null;
		
		// set-up query parameters
		cubeName = "CubeName:" + cubeName + "\n";
		aggrFunc = "AggrFunc:" + StringUtils.capitalize(aggrFunc) + "\n";
		measure = "Measure:" + measure + "\n";
		
		// create sigma expressions and then the cube queries
		ArrayList<String> sigmas = new ArrayList<String>();
		String prevSigma = null;
		for(int i=0; i<sigmaExpressions.size(); i++) {

			String expression = sigmaExpressions.get(i);
			String sigmaDimension = dimensions.get(expression);
			prevSigma = sigmaExpressionsToValues.get(expression);
			String sigma = sigmaDimension + "." + sigmaExpressions.get(i) + "=" +  prevSigma ;
			sigmas.add(sigma);
		}
		
		String sigma = "Sigma:";
		for(String s: sigmas) {
			sigma += s + ", ";
		}
		sigma = sigma.substring(0,sigma.length()-2) + "\n";		
			
		// gamma dimension, create the gamma expression with traditional drill-down
		
		boolean createDrillDown = false;
		String prevGamma = null;
		String newGamma = null;
		String restGamma = null;
		ArrayList<String> gammas = new ArrayList<String>();
		
		for(int i=0;i<gammaExpressions.size();i++) {
			String gamma = "Gamma:";
			createDrillDown = false;
			prevGamma = null;
			String gammaExpression = gammaExpressions.get(i);
			String gammaDimension = dimensions.get(gammaExpression);
			if(gammaDimension == null) {
				return drillDownQueries;
			}
			createDrillDown = true;
			prevGamma = gammaDimension+ "." +gammaExpression;
			newGamma = gammaDimension + "." +childToLevelByName.get(gammaExpression);
			
			for(int j=0;j<gammaExpressions.size();j++) {
				if(i!=j) {
					gammaExpression = gammaExpressions.get(j);
					restGamma = dimensions.get(gammaExpression) + "." + gammaExpressions.get(j);
					
					//this if clause is to make sure that the gammas will be placed in the same order as the input
					if(i<j) {
						gammas.add(newGamma);
						gammas.add(restGamma);
					}else {
						gammas.add(restGamma);
						gammas.add(newGamma);
					}
				}else {
					continue;
				}
			}
			for(String g: gammas) {
				gamma += g + ",";
			}
			// do this to erase the spare comma at the end of the expression
			gamma = gamma.substring(0,gamma.length()-1) + "\n";
			if(createDrillDown == true) {
				String name = "Name:" + queryAlias + "-AnalyzeDrillDownQuery_" + gammaDimension + "\n";
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
				AnalyzeQuery analyzeDrillDownQuery = new AnalyzeQuery(analyzeCubeQuery,TypeOfAnalyzeQuery.TRADITIONAL_DRILLDOWNS,"none","not modified",prevGamma,newGamma);
				drillDownQueries.add(analyzeDrillDownQuery);
			}
			// clear the gamma expression ArrayList
			gammas.clear();
		}
		return drillDownQueries;
	}

}
