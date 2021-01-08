package mainengine.nlq;

/** 
 * @author DimosGkitsakis
 *
 */
public interface ITranslator {
	
	
	/**
	 * Analyzes a natural language query, producing a QueryForm object which contains the equivalent cube query in its fields
	 * @param NLQuery	The natural language query
	 * @return	QueryForm object with the cube query components in its fields
	 */
	QueryForm analyzeNLQuery(String NLQuery);
	
}
