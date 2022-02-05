package interestingnessengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
	private double totalJaccardGammaDistance;
	private double totalJaccardSigmaDistance;
	private double totalJaccardMeasureDistance;
	private double totalJaccardDistance;
	private double weightSigma;
	private double weightGamma;
	private double weightMeasure;
	private ArrayList<CubeQuery> pastQueries;
	private HashMap<String, Integer> gammaMap;
	private HashMap<String, Integer> sigmaMap;
	private CubeQuery query;
	private ArrayList<String[]> gamma;
	private ArrayList<String[]> sigma;
	private String aggr;
	private String measure;
	
	
	/** Computes the syntactic peculiarity for the current {@link CubeQuery} 
	 * based on it's syntax compared with all past queries.
	 * @param inputManager The current {@link InputManager} object.
	 * @return the peculiarity value in the range of [0.0 , 1.0].
	 * @author SpyridonKaloudis
	*/
	
	public double computeMeasure(IHistoryInput inputManager) {
		query = inputManager.getCurrentQuery();
		gamma = query.getGammaExpressions();
		sigma = query.getSigmaExpressions();
		aggr = query.getAggregateFunction();
		measure = query.getListMeasure().get(0).getName();
		weightSigma = 0.5;
		weightGamma = 0.35;
		weightMeasure = 0.15;
		pastQueries = inputManager.getQueryHistory();
		gammaMap = new HashMap<String, Integer>();
		sigmaMap = new HashMap<String, Integer>();
		totalJaccardGammaDistance = 0.0;
		totalJaccardSigmaDistance = 0.0;
		totalJaccardMeasureDistance = 0.0;
		totalJaccardDistance = 0.0;
		
		long startAnalizeQuery = System.nanoTime();
		// Set up two HashMaps to easily compare expressions between queries
		
		// query.getGammaExpressions return gammas split in half (eg. account_dim.lvl1 becomes account_dim on index 0 and lvl1 on index 1. 
		
		// s[0]+s[1]+s[2] = get the whole sigma expression EG. account_dim.lvl2='Prague'
		for(String[] s : sigma) {
			sigmaMap.put(s[0]+s[1]+s[2], 0);
		}
		
		// gamma.get(i)[0] + "." + gamma.get(i)[1] = get the whole gamma expression EG. account_dim.lvl1
		for(int i=0; i<gamma.size(); i++) {
			gammaMap.put(gamma.get(i)[0] + "." + gamma.get(i)[1],0);
		}
		long endAnalizeQuery = System.nanoTime();
		long durationAnalizeQuery = endAnalizeQuery - startAnalizeQuery;

		long startAlgorithm = System.nanoTime();
		for(CubeQuery pastQuery : pastQueries) {
			tempMeasure = pastQuery.getListMeasure().get(0).getName();
			tempAggr = pastQuery.getAggregateFunction();
			tempGamma = pastQuery.getGammaExpressions();
			tempSigma = pastQuery.getSigmaExpressions();
			intersetionGamma = 0.0;
			intersetionSigma = 0.0;

			//Calculate Gamma Jaccard component for each query
			for(int i=0; i<tempGamma.size();i++) {
				if(gammaMap.containsKey(tempGamma.get(i)[0] + "." + tempGamma.get(i)[1])) {
					intersetionGamma++;
				}
			}
			
			unionGamma = gamma.size() + tempGamma.size();
			
			//Add the the Jaccard distance of each query to the total Gamma distance
			totalJaccardGammaDistance += (1 - (intersetionGamma/unionGamma));
			
			//Calculate Sigma Jaccard component for each query
			for(int i=0; i<tempSigma.size();i++) {
				if(sigmaMap.containsKey(tempSigma.get(i)[0]+tempSigma.get(i)[1]+tempSigma.get(i)[2])) {
					intersetionSigma++;
				}
			}
			
			unionSigma = sigma.size() + tempSigma.size();

			//Add the the Jaccard distance of each query to the total Sigma distance
			totalJaccardSigmaDistance += (1 - (intersetionSigma/unionSigma));
			
			//Calculate Measure Jaccard component
			if(measure.equals(tempMeasure) && aggr.equals(tempAggr)) {
				totalJaccardMeasureDistance++;
			}
			
		}
				
		//apply weights and divide by the number of past queries to find the total Jaccard distance
		totalJaccardDistance = ((weightSigma * totalJaccardSigmaDistance) + (weightGamma * totalJaccardGammaDistance) + (weightMeasure * totalJaccardMeasureDistance)) / pastQueries.size();
		
		long endAlgorithm = System.nanoTime();
		long durationAlgorithm = endAlgorithm - startAlgorithm;
		try {
			String outputTxt = "\n\nSyntactic Peculiarity \n"+
	    			"\tAnalize Query:\t" + durationAnalizeQuery+ " ns\n"+
	    			 "\tUse Algorithm:\t" + durationAlgorithm + " ns \n"+
	    			 "\tTotal Time:\t" + (durationAnalizeQuery+durationAlgorithm) + " ns";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}

		
		return totalJaccardDistance;
	
	}
}