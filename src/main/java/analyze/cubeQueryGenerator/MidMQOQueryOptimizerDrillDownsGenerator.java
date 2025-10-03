package analyze.cubeQueryGenerator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import analyze.AnalyzeQuery;
import analyze.AnalyzeQuery.TypeOfAnalyzeQuery;
import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;

public class MidMQOQueryOptimizerDrillDownsGenerator implements CubeQueryGenerator{
	
	private CubeManager cubeManager;
	
	public MidMQOQueryOptimizerDrillDownsGenerator(CubeManager cubeManager) {
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
		
		ArrayList<AnalyzeQuery> baseAndDrillDownQuery = new ArrayList<AnalyzeQuery>();
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

		String gamma = "Gamma:";
		String prevGamma = "";
		for(int i=0;i<gammaExpressions.size();i++) {		
			String gammaExpression = gammaExpressions.get(i);
			String gammaDimension = dimensions.get(gammaExpression);
			if(gammaDimension == null) {
				return baseAndDrillDownQuery;
			}
			gamma += gammaDimension + "." + childToLevelByName.get(gammaExpression) + ", ";
		}
		
		for(int i=0; i<gammaExpressions.size(); i++) {
			String gammaExpression = gammaExpressions.get(i);
			String gammaDimension = dimensions.get(gammaExpression);
			if(gammaDimension == null) {
				return baseAndDrillDownQuery;
			}
			prevGamma += gammaDimension + "." + gammaExpression + ", ";
			gamma += gammaDimension + "." + gammaExpression + ", ";
			
		}

		prevGamma = prevGamma.substring(0,prevGamma.length()-2) + "\n";
		gamma = gamma.substring(0,gamma.length()-2) + "\n";
		
		String name = "Name:" + queryAlias + "-BaseAndDrillDownDuoQueryOptimizer" + "\n";
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
		AnalyzeQuery optimizerQuery = new AnalyzeQuery(analyzeCubeQuery,TypeOfAnalyzeQuery.DUOQUERYDRILLDOWNSOPTIMIZER,"none","not modified",prevGamma.substring(0,prevGamma.length()-1), gamma.substring(6,gamma.length()-1));
		baseAndDrillDownQuery.add(optimizerQuery);
				
		
		return baseAndDrillDownQuery;
		
	}

}
