package analyze;

import cubemanager.cubebase.CubeQuery;
import result.Result;

/**
 * Class that contains the information about a Query that is necessary for the production
 * of the ANALYZE operator result
 * @author mariosjkb
 */
public class AnalyzeQuery {
	// Enum with the types of AnalyzeQuery
	public enum TypeOfAnalyzeQuery{
		Base,
		Sibling,
		Drill_Down, 
		UPDATED_SIBLINGS,
		CINECUBES_DRILLDOWNS, 
		TRADITIONAL_DRILLDOWNS
	};
	private CubeQuery analyzeCubeQuery;
	
	private TypeOfAnalyzeQuery type;
	
	private String originalSigmaValue;
	
	private String modifiedSigmaValue;
	
	private String originalGammaValue;
	
	private String modifiedGammaValue;
	
	private Result analyzeQueryResult;
	
	public AnalyzeQuery(CubeQuery analyzeCubeQuery,TypeOfAnalyzeQuery type,String originalSigmaValue,String modifiedSigmaValue,String originalGammaValue,String modifiedGammaValue) {
		this.analyzeCubeQuery = analyzeCubeQuery;
		this.type = type;
		this.originalSigmaValue = originalSigmaValue;
		this.modifiedSigmaValue = modifiedSigmaValue;
		this.originalGammaValue = originalGammaValue;
		this.modifiedGammaValue = modifiedGammaValue;
	}
	
	public void setAnalyzeQueryResult(Result result) {
		this.analyzeQueryResult = result;
	}

	public CubeQuery getAnalyzeCubeQuery() {
		return analyzeCubeQuery;
	}

	public TypeOfAnalyzeQuery getType() {
		return type;
	}

	public String getOriginalSigmaValue() {
		return originalSigmaValue;
	}

	public String getModifiedSigmaValue() {
		return modifiedSigmaValue;
	}

	public String getOriginalGammaValue() {
		return originalGammaValue;
	}

	public String getModifiedGammaValue() {
		return modifiedGammaValue;
	}

	public Result getAnalyzeQueryResult() {
		return analyzeQueryResult;
	}
	
	public String toString(){
		String retString;
		retString = analyzeCubeQuery.toString() + "\n" +"Type: " +  type.toString() + "\n" + "Original Sigma Value: " + originalSigmaValue + "\n" + "Modified Sigma Value: " + modifiedSigmaValue + "\n" + "Original Gamma Value: " + originalGammaValue + "\n" + "Modified Gamma Value: " + modifiedGammaValue;
		return retString;
	}
}
