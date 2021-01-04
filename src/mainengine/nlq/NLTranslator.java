package mainengine.nlq;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used for the transformation from natural language queries to cube queries.
 * 
 * @author DimosGkitsakis
 * 
 *
 */

public class NLTranslator implements ITranslator {
	private ArrayList<String> tokens;
	private String cubeName;
	private String queryName;
	private String aggrFunc;
	private String measure;
	private String gamma;
	private String sigma;
	private HashMap<String, ArrayList<String>> dimensionsToLevelsHashmap;
	private HashMap<String, ArrayList<String>> levelsToDimensionsHashmap;
	
	public NLTranslator() {
		tokens = new ArrayList<String>();
		cubeName = "";
		queryName = "";
		aggrFunc = "";
		measure = "";
		gamma = "";
		sigma = "";
	}
	
	public NLTranslator(HashMap<String, ArrayList<String>> dimensionsToLevelsHashmap, HashMap<String, ArrayList<String>> levelsToDimensionsHashmap) {
		tokens = new ArrayList<String>();
		cubeName = "";
		queryName = "";
		aggrFunc = "";
		measure = "";
		gamma = "";
		sigma = "";
		this.dimensionsToLevelsHashmap = dimensionsToLevelsHashmap;
		this.levelsToDimensionsHashmap = levelsToDimensionsHashmap;
	
	}
	
	
	/**
	 * Mainly used for testing. Produces an object with the cube query components in its fields from a natural language query.
	 * It just produces the equivalent Cube Query without analyzing the gamma and sigma components.
	 * @param NLQuery A String with the natural language query
	 * @return A QueryForm object with the cube query components
	 */
	public QueryForm produceNLQuery(String NLQuery) {
		findQueryComponents(NLQuery);
		QueryForm query = new QueryForm("CubeName:" + cubeName, "Name:" + queryName, "AggrFunc:" + aggrFunc, "Measure:" + measure, "Gamma:" + gamma, "Sigma:" + sigma);

		return query;
	}
	
	/**
	 * This method is used for the parsing of the natural language query and the transformation of it to cube query.
	 * The main idea is:
	 * 
	 * 1a. Splits the natural language query into tokens.
	 * 1b. Finds every component of the equivalent cube query.
	 * 2. Parse natural language query to produce proper gamma and sigma fields for the equivalent cube query.
	 * 3. Produce a QueryForm object which contains the cube query components in its fields.
	 * 4. Return the produced QueryForm Object.
	 * 
	 * @param NLQuery A String with the natural language query
	 * @return A QueryForm object which contains the cube query in its fields {@link QueryForm}
	 * @author DimosGkitsakis
	 * 
	 */
	
	public QueryForm analyzeNLQuery(String NLQuery){
		
		//1. find the cube query components from the nlquery given as parameter
		findQueryComponents(NLQuery);
		

		//TODO: ftiakse ta sigma
		//gia to sigma prepei na paraksw 3ades
		
		//2. parse gamma and sigma to produce proper cube query gamma and sigma
		ArrayList<String> gammaArrayList = parseGamma(gamma);
		if(gammaArrayList.get(0).equals("Null")) {
			gamma = gammaArrayList.get(1);
			
		}
		parseSigma(sigma);
		
		
		//3. produce QueryForm object
		QueryForm query = new QueryForm("CubeName:" + cubeName, "Name:" + queryName, "AggrFunc:" + aggrFunc, "Measure:" + measure, "Gamma:" + gamma, "Sigma:" + sigma);

		
		//System.out.println(query.getCubeName()+query.getQueryName()+query.getAggregateFunction()+query.getMeasure()+query.getGamma()+query.getSigma());
		
		//4. return the object
		return query;
		
	}
	
	
	
	private void findQueryComponents(String NLQuery) {
		clear();
		tokens = splitIntoTokens(NLQuery);
		
		cubeName = tokens.get(4);
		queryName = findQueryName(NLQuery);
		aggrFunc = tokens.get(2);
		measure = tokens.get(5);
		gamma = findGamma(NLQuery);
		sigma = findSigma(NLQuery);
	}
	
	
	/**
	 * This method analyzes the group by component (gamma component) of a natural language query.
	 * The main idea is:
	 * 1. Find if gamma is in dimension.level form or just level form
	 * 2. Find if the given dimensions and/or levels truly exist.
	 * 3. Return proper message if any errors found, plus the final form of the gamma component of the query
	 * 
	 * 
	 * @param gamma A String with the gamma field of the natural language query.
	 * @return An ArrayList of Strings with information about the parse gamma process.
	 * Its' first element is a String with the error code if any found or "Null" if none errors found.
	 * Its' second element is a String with a null String if any error is found or with the final gamma form if no error is found. 
	 * 
	 * @author DimosGkitsakis
	 */
	public ArrayList<String> parseGamma(String gamma) {
		//1st element of the returned ArrayList, foundError or null
		//2nd element of the returned ArrayList, gamma
		String finalGamma = null;
		ArrayList<String> arrayListToBeReturned = new ArrayList<String>();
		ArrayList<String> tmp;
		String[] checkIfDimensionIsGiven;
		String[] gammaTokens = gamma.split("\\s+and\\s+");

		
		for(int i =0; i<gammaTokens.length; i++) {
			gammaTokens[i] = gammaTokens[i].split("\\s+")[0];			
			checkIfDimensionIsGiven = gammaTokens[i].split("\\.");
			
			//1. Check for dimension.lvl query form 
			if(checkIfDimensionIsGiven.length > 1) {				
				if(dimensionsToLevelsHashmap.containsKey(checkIfDimensionIsGiven[0])) {
					if(levelsToDimensionsHashmap.containsKey(checkIfDimensionIsGiven[1])) {
						continue;
					}else {
						arrayListToBeReturned.add("Gamma Field Error: Level Name Not Found");
						arrayListToBeReturned.add(finalGamma);
						return arrayListToBeReturned;
					}
				}else {
					arrayListToBeReturned.add("Gamma Field Error: Dimension Name Not Found");
					arrayListToBeReturned.add(finalGamma);
					return arrayListToBeReturned;
				}
			}
			
			//2. Check for just level query form
			if(levelsToDimensionsHashmap.containsKey(gammaTokens[i])){
				tmp = new ArrayList<String>();
				tmp = levelsToDimensionsHashmap.get(gammaTokens[i]);
				//if size = 1 just 1 dimension for this level, everything ok
				if(tmp.size() == 1) {
					gammaTokens[i] = tmp.get(0) +"." +gammaTokens[i];					
				}else {
					arrayListToBeReturned.add("Gamma Field Error: Many Same Level Names"); //TODO: needs to be tested with many same lvl names
					arrayListToBeReturned.add(finalGamma);
					return arrayListToBeReturned;
				}
			}else {
				arrayListToBeReturned.add("Gamma Field Error: Level Name Not Found");
				arrayListToBeReturned.add(finalGamma);
				return arrayListToBeReturned;
			}
		}
		finalGamma = gammaTokens[0];
		for(int i =1; i<gammaTokens.length; i++) {
			finalGamma += "," + gammaTokens[i];
			
		}
		arrayListToBeReturned.add("Null");
		arrayListToBeReturned.add(finalGamma);
		return arrayListToBeReturned;
		
	}
	
	
	
	private void parseSigma(String sigma) {
		
	}
	

	
	private void clear() {
		this.cubeName = "";
		this.queryName = "";
		this.aggrFunc = "";
		this.measure = "";
		this.gamma = "";
		this.sigma = "";
	}
	
	
	private ArrayList<String> splitIntoTokens(String NLQuery) {
		ArrayList<String> producedTokens = new ArrayList<String>();
		String[] splitIntoTokens = NLQuery.split("\\s+");
		for (int i=0; i<splitIntoTokens.length; i++) {
			producedTokens.add(splitIntoTokens[i]);
		}
		return producedTokens;
	}
	
	private String findSigma(String NLQuery) {
		String[] splitIntoTokens = NLQuery.split("\\s+");
		for (int i=0; i<splitIntoTokens.length; i++) {
			if (splitIntoTokens[i].equals("for")) {
				for(int j=i+1; j<splitIntoTokens.length; j++) {
					if(!(splitIntoTokens[j].equals("as"))) {
						sigma += splitIntoTokens[j] + " ";
					}
					else {
						break;
					}
				}
			}
		}
		return sigma;
	}//end findSigma()
	
	private String findGamma(String NLQuery) {
		String[] splitIntoTokens = NLQuery.split("\\s+");
		for (int i=0; i<splitIntoTokens.length; i++) {
			if (splitIntoTokens[i].equals("per")) {
				for(int j=i+1; j<splitIntoTokens.length; j++) {
					if(!(splitIntoTokens[j].equals("for"))) {
						gamma += splitIntoTokens[j] + " ";
					}
					else {
						break;
					}
				}
			}
		}
		return gamma;
	}//end findGamma()
	
	private String findQueryName(String NLQuery) {
		String[] splitIntoTokens = NLQuery.split("\\s+");
		for (int i=0; i<splitIntoTokens.length; i++) {
			if (splitIntoTokens[i].equals("as")) {
				queryName = splitIntoTokens[i+1];
			}
		}
		return queryName;
		
	}//end findQueryName()
	
}//end class
