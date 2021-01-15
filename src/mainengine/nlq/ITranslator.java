package mainengine.nlq;

import java.util.ArrayList;

/** 
 * @author DimosGkitsakis
 *
 */
public interface ITranslator {
	/**
	 * Produces an assessment of the syntactic correctness of a candidate NL query, given its components as Lists of Strings.
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
	
	
	/**
	 * Produces a QueryForm object  from a natural language query, with the cube query components in its fields.
	 * 
	 * An IproduceCubeQuery() invocation produces the equivalent Query Form object (modeling all the components of a query as strings)
	 * without analyzing the gamma and sigma components for errors.
	 * 
	 * @param NLQuery A String with the natural language query.
	 * @return A QueryForm object with the cube query in its components.
	 * @author DimosGkitsakis
	 */
	QueryForm produceCubeQuery(String NLQuery);
	
	
	/**
	 * Translates a natural language query to a string expressing the cube query 
	 * 
	 * @param NLQuery	The natural language query.
	 * @return	A String with the cube query.
	 * @author DimosGkitsakis
	 */
	String analyzeNLQuery(String NLQuery);
 
	
}
