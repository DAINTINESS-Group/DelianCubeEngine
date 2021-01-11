package mainengine.nlq;

import java.util.ArrayList;

/** 
 * @author DimosGkitsakis
 *
 */
public interface ITranslator {
	
	/**
	 * Produces an object with the cube query components in its fields from a natural language query.
	 * It produces the equivalent Cube Query without analyzing the gamma and sigma components for errors.
	 * @param NLQuery A String with the natural language query.
	 * @return A QueryForm object with the cube query in its components.
	 * @author DimosGkitsakis
	 */
	QueryForm produceCubeQuery(String NLQuery);
	
	
	/**
	 * Analyzes a natural language query, producing a QueryForm object which contains the equivalent cube query in its fields.
	 * @param NLQuery	The natural language query.
	 * @return	A String with the cube query.
	 * @author DimosGkitsakis
	 */
	String analyzeNLQuery(String NLQuery);
	
	
	
	/**
	 * Gets all the needed metadata and the natural language query in order to check the query for possible errors.
	 *
	 * 
	 * @param cubeNames An ArrayList of String with the cube names given by server.
	 * @param aggrFunctions An ArrayList of Strings with aggregate functions.
	 * @param measures An ArrayList of Strings with the measure names.
	 * @param queryString A String with the natural language query.
	 * @return A NLQValidationResults object with the results of the validation process.
	 * @author DimosGkitsakis
	 */
	NLQValidationResults prepareCubeQuery(ArrayList<String> cubeNames, ArrayList<String> aggrFunctions,
										ArrayList<String> measures, String queryString); 
	
}
