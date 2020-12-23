package mainengine.nlq;

public class NLQProcessingResultsReturnedToClient {
	
	//TODO ask if it is ok to have public attributes
	public String hashKey;
	public boolean foundError;
	public String errorCode;
	public String details;
	
	public NLQProcessingResultsReturnedToClient(String hashKey, boolean foundError, String errorCode, String details) {
		this.hashKey = hashKey;
		this.foundError = foundError;
		this.errorCode = errorCode;
		this.details = details;
	}


}
