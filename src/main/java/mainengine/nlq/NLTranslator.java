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
	 * Creates a NLQValidator object and gives it the cubeNames, aggregate functions,
	 * measure names and the results of {@link #parseGamma(String)} and {@link #parseSigma(String)},
	 * in order to produce a set of messages if any errors are found.
	 * 
	 * @author DimosGkitsakis
	 */
	public NLQValidationResults prepareCubeQuery(ArrayList<String> cubeNames, ArrayList<String> aggrFunctions, 
												ArrayList<String> measures, String queryString) {
		
		NLQValidator validator = new NLQValidator(cubeNames, aggrFunctions, measures);
		long t0 = System.nanoTime();
		CubeQueryForm query = produceCubeQuery(queryString);
		long tF = System.nanoTime();
		long duration = tF - t0;
		System.out.println("Components splitting time:" +duration + " nanoseconds");
		
		
		String gamma = query.gamma.split(":")[1];
		ArrayList<String> gammaErrorHandling = new ArrayList<String>();
		t0 = System.nanoTime();
		gammaErrorHandling = parseGamma(gamma);
		tF = System.nanoTime();
		duration = tF - t0;
		System.out.println("Gamma time:" +duration + " nanoseconds");
		
		
		String sigma = query.sigma.split(":")[1];
		ArrayList<String> sigmaErrorHandling = new ArrayList<String>();
		t0 = System.nanoTime();
		sigmaErrorHandling = parseSigma(sigma);
		tF = System.nanoTime();
		duration = tF - t0;
		System.out.println("Sigma time:" +duration + " nanoseconds");
		
		NLQValidationResults results = validator.prepareCubeQuery(query, gammaErrorHandling, sigmaErrorHandling);
		return results;
	}
	
	/**
	 * Produces an object with the cube query components in its fields from a natural language query.
	 * It produces the equivalent Cube Query without analyzing the gamma and sigma components for errors.
	 * @param NLQuery A String with the natural language query
	 * @return A QueryForm object with the cube query components
	 * @author DimosGkitsakis
	 */
	public CubeQueryForm produceCubeQuery(String NLQuery) {

		findQueryComponents(NLQuery);
		CubeQueryForm query = new CubeQueryForm("CubeName:" + cubeName, "Name:" + queryName, "AggrFunc:" + aggrFunc, "Measure:" + measure, "Gamma:" + gamma, "Sigma:" + sigma);

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
	 * 4. Return the produced cube query as a String.
	 * 
	 * @param NLQuery A String with the natural language query.
	 * @return A String which contains the cube query.
	 * @author DimosGkitsakis
	 * 
	 */
	
	public String produceCubeQueryString(String NLQuery){
		
		//1. find the cube query components from the nlquery given as parameter
		long t0 = System.nanoTime();
		findQueryComponents(NLQuery);
		long tF = System.nanoTime();
		long duration = tF - t0;
		System.out.println("Components splitting time:" +duration + " nanoseconds");
		if(aggrFunc.equals("maximum")) {
			aggrFunc = "max";
		}else if(aggrFunc.equals("minimum")) {
			aggrFunc = "min";
		}else if(aggrFunc.equals("average")) {
			aggrFunc = "avg";
		}
		
		//2. parse gamma and sigma to produce proper cube query gamma and sigma
		t0 = System.nanoTime();
		ArrayList<String> gammaArrayList = parseGamma(gamma);
		tF = System.nanoTime();
		duration = tF - t0;
		System.out.println("Gamma time:" +duration + " nanoseconds");
		if(gammaArrayList.get(0).equals("Null")) {
			gamma = gammaArrayList.get(1);
			
		}
		
		t0 = System.nanoTime();
		ArrayList<String> sigmaArrayList = parseSigma(sigma);
		tF = System.nanoTime();
		duration = tF - t0;
		System.out.println("Sigma time:" +duration + " nanoseconds");
		if(sigmaArrayList.get(0).equals("Null")) {
			sigma = sigmaArrayList.get(1);
		}

		//3. produce QueryForm object
		CubeQueryForm query = new CubeQueryForm("CubeName:" + cubeName, "Name:" + queryName, "AggrFunc:" + aggrFunc, "Measure:" + measure, "Gamma:" + gamma, "Sigma:" + sigma);

		
		//System.out.println(query.getCubeName()+query.getQueryName()+query.getAggregateFunction()+query.getMeasure()+query.getGamma()+query.getSigma());
		
		//4. return the String
		String cubeQuery = query.toString();		
		return cubeQuery;
		
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
	 * 1. Split the gamma component in tokens
	 * 2. Find if gamma is in dimension.level form or just level form
	 * 3. Find if the given dimensions and/or levels truly exist.
	 * 4. Return proper message if any errors found, plus the final form of the gamma component of the query
	 * 
	 * 
	 * @param gamma A String with the gamma component of the natural language query.
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
		
		//1. Split in Tokens
		String[] gammaTokens = gamma.split("\\s+and\\s+");

		
		for(int i =0; i<gammaTokens.length; i++) {
			gammaTokens[i] = gammaTokens[i].split("\\s+")[0];			
			checkIfDimensionIsGiven = gammaTokens[i].split("\\.");
			
			//2. Check for dimension.lvl query form 
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
			
			//3. Check for just level query form
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
		
		//4. Form the final gamma to be returned
		finalGamma = gammaTokens[0];
		for(int i =1; i<gammaTokens.length; i++) {
			finalGamma += "," + gammaTokens[i];
			
		}
		arrayListToBeReturned.add("Null");
		arrayListToBeReturned.add(finalGamma);
		return arrayListToBeReturned;
		
	}
	

	/**
	 * This method analyzes the selection component (sigma component) of a natural language query.
	 * The main idea is:
	 * 1. Split the values in '...' so they remain intact throughout the process and save them to an ArrayList.
	 * 2. Split the rest sigma to produce another ArrayList with the levels.
	 * 3. Check if every level has a value to be equaled in the query.
	 * 4. Find if sigma is in dimension.level form or just level form
	 * 5. Find if the given dimensions and/or levels truly exist.
	 * 6. If in just level form, find if the given levels are referred to more than 1 dimension.
	 * 7. Return proper message if any errors found, plus the final form of the sigma component of the query.
	 * 
	 * @param sigma A String with the sigma component of the natural language query.
	 * @return An ArrayList of Strings with information about the parse sigma process.
	 * Its' first element is a String with the error code if any found or "Null" if none errors found.
	 * Its' second element is a String with a null String if any error is found or with the final sigma form if no error is found.
	 * 
	 * @author DimosGkitsakis
	 */
	public ArrayList<String> parseSigma(String sigma) {
		ArrayList<String> levels = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		String finalSigma = null;
		ArrayList<String> arrayListToBeReturned = new ArrayList<String>();
		
		//1. Split values so they dont get splitted
		String[] splitValues = sigma.split("\\'");
		for(int i=1; i<splitValues.length; i+=2) {
			String v = "'" + splitValues[i] + "'";
			values.add(v);
		}
		
		sigma = "";
		for(int i=0; i<splitValues.length; i+=2) {
			sigma += splitValues[i];	
		}
		
		//2. Split the rest sigma to produce 2 arraylists, one with levels and one with values
		String[] splitSigma = sigma.split("\\s+and\\s+");
		for(int i=0; i<splitSigma.length; i++) {
			String level = null;
			//TODO: important: we need to check that there is a second '
			//problem if ' is in the value
			String[] tmp = splitSigma[i].split("\\s+");
			level = tmp[0];
			levels.add(level);
		}
		
		//3. Check if the sizes of the 2 arraylists are equal
		if(levels.size() > values.size()) {
			arrayListToBeReturned.add("Sigma Field Error: More levels than values");
			arrayListToBeReturned.add(finalSigma);
			return arrayListToBeReturned;
		}else if(levels.size() < values.size()) {
			arrayListToBeReturned.add("Sigma Field Error: More values than levels");
			arrayListToBeReturned.add(finalSigma);
			return arrayListToBeReturned;
		}
		
		
		for(int i=0; i<levels.size(); i++) {
			String[] checkIfDimensionIsGiven = levels.get(i).split("\\.");
			
			//4. Check for dimension.lvl query form
			if(checkIfDimensionIsGiven.length > 1) {
				if(dimensionsToLevelsHashmap.containsKey(checkIfDimensionIsGiven[0])) {
					if(levelsToDimensionsHashmap.containsKey(checkIfDimensionIsGiven[1])) {
						continue;
					}else {
						arrayListToBeReturned.add("Sigma Field Error: Level Name Not Found");
						arrayListToBeReturned.add(finalSigma);
						return arrayListToBeReturned;
					}
					
				}else {
					arrayListToBeReturned.add("Sigma Field Error: Dimension Name Not Found");
					arrayListToBeReturned.add(finalSigma);
					return arrayListToBeReturned;
				}
			}
			
			//5. Check for just level query form
			if(levelsToDimensionsHashmap.containsKey(levels.get(i))) {
				ArrayList<String> tmpDim = new ArrayList<String>();
				tmpDim = levelsToDimensionsHashmap.get(levels.get(i));
				//6. If size = 1 just 1 dimension for this level, everything ok
				if(tmpDim.size() == 1) {
					String finalLevelForm = tmpDim.get(0) + "." + levels.get(i);
					levels.set(i, finalLevelForm);
				}else {
					arrayListToBeReturned.add("Sigma Field Error: Many Same Level Names"); //TODO: needs to be tested with many same lvl names
					arrayListToBeReturned.add(finalSigma);
					return arrayListToBeReturned;
				}
			}else {
				arrayListToBeReturned.add("Sigma Field Error: Level Name Not Found");
				arrayListToBeReturned.add(finalSigma);
				return arrayListToBeReturned;
			}
		
		}
		//7. Produce final sigma to be returned
		finalSigma = levels.get(0) + "=" + values.get(0);
		for(int i=1; i<levels.size(); i++) {
			finalSigma += "," + levels.get(i) + "=" + values.get(i);
		}
		
		arrayListToBeReturned.add("Null");
		arrayListToBeReturned.add(finalSigma);
		return arrayListToBeReturned;
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
