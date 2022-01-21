package interestingnessengine;

import java.util.ArrayList;
import java.util.HashMap;

import cubemanager.cubebase.CubeQuery;

public class SyntacticPeculiarity implements IInterestingnessMeasureWithHistory{

	private String tempMeasure;
	private String tempAggr;
	private ArrayList<String[]> tempGamma;
	private ArrayList<String[]> tempSigma;
	private double intersetionGamma;
	private double intersetionSigma;
	private double unionGamma;
	private double unionSigma;
	private double totalJaccardGammaDistance = 0.0;
	private double totalJaccardSigmaDistance = 0.0;
	private double totalJaccardMeasureDistance = 0.0;
	private double totalJaccardDistance = 0.0;

	public double computeMeasure(IHistoryInput inputManager) {
		CubeQuery query = inputManager.getCurrentQuery();
		ArrayList<String[]> gamma = query.getGammaExpressions();
		ArrayList<String[]> sigma = query.getSigmaExpressions();
		String aggr = query.getAggregateFunction();
		String measure = query.getListMeasure().get(0).getName();
		double weightSigma = 0.5;
		double weightGamma = 0.35;
		double weightMeasure = 0.15;
		ArrayList<CubeQuery> pastQueries = inputManager.getQueryHistory();
		HashMap<String, Integer> gammaMap = new HashMap<String, Integer>();
		HashMap<String, Integer> sigmaMap = new HashMap<String, Integer>();
		
		// Set up two HashMaps to easily compare expressions between queries
		
		// query.getGammaExpressions return gammas split in half (eg. account_dim.lvl1 becomes account_dim on index 0 and lvl1 on index 1. 
		// meanwhile query.getSigmaExpressions returm the whole thing on a single index
		// so I had to improvise with these HaspMaps to do the right checks.
				
		for(String[] s : sigma) {
			sigmaMap.put(s[0], 0);
		}
				
		for(int i=0; i<gamma.size(); i++) {
			gammaMap.put(gamma.get(i)[0] + gamma.get(i)[1],0);
		}

		for(CubeQuery pastQuery : pastQueries) {
			tempMeasure = pastQuery.getListMeasure().get(0).getName();
			tempAggr = pastQuery.getAggregateFunction();
			tempGamma = pastQuery.getGammaExpressions();
			tempSigma = pastQuery.getSigmaExpressions();
		
			intersetionGamma = 0.0;
			intersetionSigma = 0.0;

			//Calculate Gamma Jaccard component
			for(int i=0; i<tempGamma.size();i++) {
				if(gammaMap.containsKey(tempGamma.get(i)[0]+tempGamma.get(i)[1])) {
					intersetionGamma++;
				}
			unionGamma = gamma.size() + tempGamma.size();
			}
			totalJaccardGammaDistance += (1 - (intersetionGamma/unionGamma));
			
			//Calculate Sigma Jaccard component
			for(int i=0; i<tempSigma.size();i++) {
				if(sigmaMap.containsKey(tempSigma.get(i)[0])) {
					intersetionSigma++;
				}
			unionSigma = sigma.size() + tempSigma.size();
			}				
			totalJaccardSigmaDistance += (1 - (intersetionSigma/unionSigma));
			
			//Calculate Measure Jaccard component
			if(measure.equals(tempMeasure) && aggr.equals(tempAggr)) {
				totalJaccardMeasureDistance++;
			}
			
		}
		
		//calculate the mean of each Jaccard distance component
		totalJaccardGammaDistance = totalJaccardGammaDistance/pastQueries.size();
		totalJaccardSigmaDistance = totalJaccardSigmaDistance/pastQueries.size();
		totalJaccardMeasureDistance = totalJaccardMeasureDistance/pastQueries.size();
		
		//apply weights and find the final Jaccard distance
		totalJaccardDistance = (weightSigma * totalJaccardSigmaDistance) + (weightGamma * totalJaccardGammaDistance) + (weightMeasure * totalJaccardMeasureDistance);
		return totalJaccardDistance;
	
	}
}