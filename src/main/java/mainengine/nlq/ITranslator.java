package mainengine.nlq;

import java.util.ArrayList;

/** 
 * An interface used for the natural language query translation.
 * 
 * 1. First prepareCubeQuery is called which prepares the cube query by checking the natural language query for any errors.
 * 2. produceCubeQueryForm is called in prepareCubeQuery to help the process of error checking. Also used in testing.
 * 3. produceCubeQueryString is called last to produce the final equivalent cube query.
 * 
 * 
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
	 * Produces a CubeQueryForm object  from a natural language query, with the cube query components in its fields.
	 * 
	 * An IproduceCubeQuery() invocation produces the equivalent Cube Query Form object (modeling all the components of a query as strings)
	 * without analyzing the gamma and sigma components for errors.
	 * 
	 * @param NLQuery A String with the natural language query.
	 * @return A CubeQueryForm object with the cube query in its components.
	 * @author DimosGkitsakis
	 */
	CubeQueryForm produceCubeQuery(String NLQuery);
	
	
	/**
	 * Translates a natural language query to a string expressing the cube query 
	 * 
	 * @param NLQuery	The natural language query.
	 * @return	A String with the cube query.
	 * @author DimosGkitsakis
	 */
	String produceCubeQueryString(String NLQuery);
 
	
}
