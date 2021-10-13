package mainengine.nlq;

/**
 * This class is used to return error details produced by {@link mainengine.nlq.NLQValidator}
 * 
 * @author DimosGkitsakis 
 *
 */

public class NLQValidationResults {
	
	private String hashKey;
	private boolean foundError;
	private String errorCode;
	private String details;
	
	public NLQValidationResults(String hashKey, boolean foundError, String errorCode, String details) {
		this.hashKey = hashKey;
		this.foundError = foundError;
		this.errorCode = errorCode;
		this.details = details;
	}
	
	public String getHashKey() {
		return hashKey;
	}
	
	public boolean getFoundError() {
		return foundError;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public String getDetails() {
		return details;
	}
	


}
