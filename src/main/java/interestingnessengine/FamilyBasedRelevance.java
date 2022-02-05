package interestingnessengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import cubemanager.cubebase.CubeQuery;

public class FamilyBasedRelevance {
	
	private HashMap<String, Integer> gammaMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> sigmaMap = new HashMap<String, Integer>();
	private int historySize;
	private CubeQuery query1;
	private CubeQuery query2;
	private ArrayList<String[]> sigma1;
	private ArrayList<String[]> sigma2;	
	private long durationConstructingQuery;
	
	/** Computes the family based relevance of two queries.
	 * @param q1 The string of {@link #query1}.
	 * @param q2 The string of {@link #query2}.
	 * @param inputManager The only {@link InputManager} object, from the {@link InterestingnessManager} class.
	 * @param helpingQueryResult The result of the helping query we need to ask first (by calling the getHelpingQuery method).
	 * @return 0.0 if the two queries are family related (Ancestor-Descendant OR Mother-Child OR Siblings), else return 1.0.
	 * @author SpyridonKaloudis
	*/
	
	public double computeMeasure(String q1, String q2, InputManager inputManager, String helpingQueryResult) {
		
		long startAlgorithm = System.nanoTime();
		
		historySize = inputManager.getQueryHistory().size();
		query1 = inputManager.getCurrentQuery();
		query2 = inputManager.getQueryHistory().get(historySize-1);
		sigma1 = query1.getSigmaExpressions();
		sigma2 = query2.getSigmaExpressions();
		
		//if helpingQuery returned an empty string, then q1 and q2 are not related. Gamma or Sigma size didn't match OR there are more than 2 different Sigmas.
		if(helpingQueryResult=="") {
			
			long endAlgorithm = System.nanoTime();
			computeAlgorithmTime(startAlgorithm,endAlgorithm);
			
			return 1.0;
		}else {
			//helpingQueryResult contains the ancestor of the lower level 
			//eg. If we were searching for the ancestor of date_dim.lvl1 = '1952-02-09', helpingQueryResult will be equal to 1952 for date_dim.lvl3
			
			//Add apostrophes at the beginning and the end so that it matches the queries' sigmas.
			helpingQueryResult = "'" + helpingQueryResult + "'" ;
			
			//Search for helpingQueryResult in both queries' sigmas. If 1 sigma is equal to helpingQueryResult, then we have a Mother-Child, Ancestor or Sibling relation
			for(String[] sigma : sigma1) {
				if(sigma[2].trim().equals(helpingQueryResult)) {
					
					long endAlgorithm = System.nanoTime();
					computeAlgorithmTime(startAlgorithm,endAlgorithm);
					
					return 0.0;
				}
			}
			for(String[] sigma : sigma2) {
				if(sigma[2].trim().equals(helpingQueryResult)) {
					
					long endAlgorithm = System.nanoTime();
					computeAlgorithmTime(startAlgorithm,endAlgorithm);
					
					return 0.0;
				}
			}
			
			//If the helpingQueryResult didn't match any of the Sigmas, then the query is not related
			long endAlgorithm = System.nanoTime();
			computeAlgorithmTime(startAlgorithm,endAlgorithm);
			
			return 1.0;
		}
	}
	
	//Simple method to check if all Gammas match and have a pair.
	private boolean checkIfGammasMatch(ArrayList<String[]> gamma1, ArrayList<String[]> gamma2) {
		String tempGamma = "";
		Integer value = null;
		
	    //Map gammas to find pairs
		for(int i=0; i<gamma1.size(); i++) {
			tempGamma = gamma1.get(i)[0] + "." + gamma1.get(i)[1];

			if(gammaMap.containsKey(tempGamma)) {
				gammaMap.replace(tempGamma, gammaMap.get(tempGamma)+1);
			}else {
				gammaMap.put(tempGamma, 1);
			}
			tempGamma = gamma2.get(i)[0] + "." + gamma2.get(i)[1];
			if(gammaMap.containsKey(tempGamma)) {
				gammaMap.replace(tempGamma, gammaMap.get(tempGamma)+1);
			}else {
				gammaMap.put(tempGamma, 1);
			}
		}
		
		//Check the if all Gammas are matching (in pairs of 2)
		for(Entry<String, Integer> entry : gammaMap.entrySet()) {
		    value = entry.getValue();
		    if(!value.equals(2)) {
		    	return false;
		    }
		}

		return true;
		
	}
	
	
	/** Determines if a helping query is needed and returns it in a String format.
	 * @param q1 The string of {@link #query1}.
	 * @param q2 The string of {@link #query2}.
	 * @param inputManager The only {@link InputManager} object, from the {@link InterestingnessManager} class.
	 * @return "" if no helping query is needed, else return the helping query we need to ask in a String format.
	 * @author SpyridonKaloudis
	*/
	
	//Use this method to find out if we need to ask a helping query
	//If we need to ask a helping query, return the query itself as a String
	public String getHelpingQuery(String q1, String q2, InputManager inputManager) {
		long startConstructingQuery = System.nanoTime();
		historySize = inputManager.getQueryHistory().size();
		CubeQuery query1 = inputManager.getCurrentQuery();
		CubeQuery query2 = inputManager.getQueryHistory().get(historySize-1);
		ArrayList<String[]> gamma1 = query1.getGammaExpressions();
		ArrayList<String[]> gamma2 = query2.getGammaExpressions();
		sigma1 = query1.getSigmaExpressions();
		sigma2 = query2.getSigmaExpressions();
		String aggr1 = query1.getAggregateFunction();
		String aggr2 = query2.getAggregateFunction();
		String cubeName1 = query1.getReferCube().getName();
		String cubeName2 = query2.getReferCube().getName();
		String measure1 = query1.getListMeasure().get(0).getName();
		String measure2 = query2.getListMeasure().get(0).getName();
		//A bunch of nested if-s to determine if we need to ask a query for help.
		if(cubeName1.equals(cubeName2)) {	//check if the queries are about the same cube.
			if(aggr1.equals(aggr2)) {	//check if the queries have the same aggr function.
				if(measure1.equals(measure2)) {		//check if the queries are about the same measure.
					if(gamma1.size()==gamma2.size()) {	//check if the queries have the same amount of Gamma.
						if(checkIfGammasMatch(gamma1, gamma2)) {	//check if the queries have the same Gamma expressions.
							
							if(sigma1.size()!=sigma2.size()) {	//check if the queries have the same amount of Sigma expressions.
								long endConstructingQuery = System.nanoTime();
								durationConstructingQuery = endConstructingQuery - startConstructingQuery;
								try {
									String outputTxt = "\n\nFamily Based Relevance \n"+
							    			"\tConstructing Query:\t" + durationConstructingQuery+ " ns\n";
								    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
								    		outputTxt.getBytes(), StandardOpenOption.APPEND);
								}catch (IOException e) {}
								
								return "";	//If the queries don't have the same amount of Sigma, we don't need to ask a helping query, so return "";
							}
							
							String tempSigma = "";
							Integer value = null;
						    String key = "";
						    ArrayList <String> differentSigmas = new ArrayList<>();
						    
						    //Map Sigmas to find pairs
							for (int i=0; i<sigma1.size(); i++) {
								tempSigma = sigma1.get(i)[0]+ "@" + sigma1.get(i)[1]+ "@" + sigma1.get(i)[2];
								if(sigmaMap.containsKey(tempSigma)) {
									sigmaMap.replace(tempSigma, sigmaMap.get(tempSigma)+1);
								}else {
									sigmaMap.put(tempSigma, 1);
								}
								
								tempSigma = sigma2.get(i)[0]+ "@" + sigma2.get(i)[1]+ "@" + sigma2.get(i)[2];
								if(sigmaMap.containsKey(tempSigma)) {
									sigmaMap.replace(tempSigma, sigmaMap.get(tempSigma)+1);
								}else {
									sigmaMap.put(tempSigma, 1);
								}
							}
							
							//Find if all Sigmas have pairs. If not, add the un-paired ones to the differentSigmas ArrayList
							for(Entry<String, Integer> entry : sigmaMap.entrySet()) {
							    key = entry.getKey();
							    value = entry.getValue();
							    //if this Sigma was not paired, add it to differentSigmas ArrayList
							    if(!value.equals(2)) {
							    	differentSigmas.add(key);
							    }
							}
							
							//If there are 2 Sigmas without a pair (One in each query), start constructing the heping query
							if (differentSigmas.size()==2){
								String [] s1 = differentSigmas.get(0).split("@");
								String [] s2 = differentSigmas.get(1).split("@");
								//Check to see if the un-paired Sigmas are refering to the same dimension (eg. check if both are about account_dim)
								if(s1[0].split("[.]")[0].equals(s2[0].split("[.]")[0])) {
									//using trim() to get rid of any whitespace
									String sigma1Trimmed = s1[0].trim();
									String sigma2Trimmed = s2[0].trim();
									//get the last character of trimmed Sigmas to see what level they are on
									int level1 = Integer.valueOf(sigma1Trimmed.charAt(sigma1Trimmed.length()-1));
									int level2 = Integer.valueOf(sigma2Trimmed.charAt(sigma1Trimmed.length()-1));

									if(level1>level2) {
										if(s1[1].equals(s2[1])) { //Check if they are using the sane comparison symbol
											//Start constructing the helping query
											String aggr = query1.getAggregateFunction();
											String name = query1.getReferCube().getName().replace("_cube", "");
											String measure = query1.getMsr().get(0).getName();
											String gamma = s1[0];
											String sigma = s2[0]+s2[1]+s2[2];									
											//return the helping query
											
											long endConstructingQuery = System.nanoTime();
											durationConstructingQuery = endConstructingQuery - startConstructingQuery;

											return("CubeName:" + name + "\n" + 
													"Name: helpingQuery\n" + 
													"AggrFunc:" + aggr +"\n" + 
													"Measure:" + measure + "\n" + 
													"Gamma:" + gamma + "\n" + 
													"Sigma:" + sigma);
										}else {
											//or else return "" if the don't need to ask a helping query
											long endConstructingQuery = System.nanoTime();
											durationConstructingQuery = endConstructingQuery - startConstructingQuery;
											return "";
										}
									}else { //if level of query 2 > level of query 1
										if(s1[1].equals(s2[1])) { //Check if they are using the sane comparison symbol
											//Start constructing the helping query
											String aggr = query2.getAggregateFunction();
											String name = query2.getReferCube().getName().replace("_cube", "");
											String measure = query2.getMsr().get(0).getName();
											String gamma = s2[0];
											String sigma = s1[0]+s1[1]+s1[2];
											//return the helping query
											
											
											long endConstructingQuery = System.nanoTime();
											durationConstructingQuery = endConstructingQuery - startConstructingQuery;

											try {
												String outputTxt = "\n\nFamily Based Relevance \n"+
										    			"\tConstructing Query:\t" + durationConstructingQuery+ " ns\n";
											    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
											    		outputTxt.getBytes(), StandardOpenOption.APPEND);
											}catch (IOException e) {}

											return("CubeName:" + name + "\n" + 
													"Name: helpingQuery\n" + 
													"AggrFunc:" + aggr +"\n" + 
													"Measure:" + measure + "\n" + 
													"Gamma:" + gamma + "\n" + 
													"Sigma:" + sigma);
										}else {
											//or else return "" if the don't need to ask a helping query
											long endConstructingQuery = System.nanoTime();
											durationConstructingQuery = endConstructingQuery - startConstructingQuery;
											try {
												String outputTxt = "\n\nFamily Based Relevance \n"+
										    			"\tConstructing Query:\t" + durationConstructingQuery+ " ns\n";
											    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
											    		outputTxt.getBytes(), StandardOpenOption.APPEND);
											}catch (IOException e) {}

											return "";
										}
									}
								}else {
									long endConstructingQuery = System.nanoTime();
									durationConstructingQuery = endConstructingQuery - startConstructingQuery;
									try {
										String outputTxt = "\n\nFamily Based Relevance \n"+
								    			"\tConstructing Query:\t" + durationConstructingQuery+ " ns\n";
									    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
									    		outputTxt.getBytes(), StandardOpenOption.APPEND);
									}catch (IOException e) {}
									
									return "" ;
								}
							}else {
								long endConstructingQuery = System.nanoTime();
								durationConstructingQuery = endConstructingQuery - startConstructingQuery;
								try {
									String outputTxt = "\n\nFamily Based Relevance \n"+
							    			"\tConstructing Query:\t" + durationConstructingQuery+ " ns\n";
								    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
								    		outputTxt.getBytes(), StandardOpenOption.APPEND);
								}catch (IOException e) {}
								
								return "" ;
							}
						}else {
							long endConstructingQuery = System.nanoTime();
							durationConstructingQuery = endConstructingQuery - startConstructingQuery;
							try {
								String outputTxt = "\n\nFamily Based Relevance \n"+
						    			"\tConstructing Query:\t" + durationConstructingQuery+ " ns\n";
							    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
							    		outputTxt.getBytes(), StandardOpenOption.APPEND);
							}catch (IOException e) {}
							
							return "" ;
						}
					}else {
						long endConstructingQuery = System.nanoTime();
						durationConstructingQuery = endConstructingQuery - startConstructingQuery;
						try {
							String outputTxt = "\n\nFamily Based Relevance \n"+
					    			"\tConstructing Query:\t" + durationConstructingQuery+ " ns\n";
						    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
						    		outputTxt.getBytes(), StandardOpenOption.APPEND);
						}catch (IOException e) {}
						
						return "" ;
					}
				}else {
					long endConstructingQuery = System.nanoTime();
					durationConstructingQuery = endConstructingQuery - startConstructingQuery;
					try {
						String outputTxt = "\n\nFamily Based Relevance \n"+
				    			"\tConstructing Query:\t" + durationConstructingQuery+ " ns\n";
					    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
					    		outputTxt.getBytes(), StandardOpenOption.APPEND);
					}catch (IOException e) {}
					
					return "" ;
				}
			}else {
				long endConstructingQuery = System.nanoTime();
				durationConstructingQuery = endConstructingQuery - startConstructingQuery;
				try {
					String outputTxt = "\n\nFamily Based Relevance \n"+
			    			"\tConstructing Query:\t" + durationConstructingQuery+ " ns\n";
				    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
				    		outputTxt.getBytes(), StandardOpenOption.APPEND);
				}catch (IOException e) {}
				
				return "" ;
			}
		}else {
			long endConstructingQuery = System.nanoTime();
			durationConstructingQuery = endConstructingQuery - startConstructingQuery;
			try {
				String outputTxt = "\n\nFamily Based Relevance \n"+
		    			"\tConstructing Query:\t" + durationConstructingQuery+ " ns\n";
			    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
			    		outputTxt.getBytes(), StandardOpenOption.APPEND);
			}catch (IOException e) {}
			
			return "";
		}			
	}
	
	private void computeAlgorithmTime(long startAlgorithm, long endAlgorithm) {
		long durationAlgorithm = endAlgorithm - startAlgorithm;
		
		try {
			String outputTxt =
	    			 "\tCompute Algorithm:\t" +durationAlgorithm + " ns\n";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}
	}
}