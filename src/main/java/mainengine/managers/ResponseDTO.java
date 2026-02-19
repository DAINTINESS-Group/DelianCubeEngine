package mainengine.managers;

import result.ResultFileMetadata;

/**
 * A Data Transfer Object (DTO) representing the standard response from any manager
 * This class ensures a uniform response structure across the system since it encapsulates the result of an operation
 * (payload), it's success status and any error messages in case the operation failed
 * 
 * Using this DTO the Director class and the SeesionQueryProcessorEngine handle results without the need to know a specific return type from every manager  
 * 
 * @author Nikos
 *
 */
public class ResponseDTO {
    private ResultFileMetadata metadata;
    private String stringResult;
    private boolean success;
    private String error;
    private Object payload;

    public ResponseDTO(ResultFileMetadata metadata) {
        this.metadata = metadata;
        this.success = true;
    }
    
    public ResponseDTO(boolean success) {
        this.success = success;
    }
    
    public ResponseDTO(String stringResult) {
        this.stringResult = stringResult;
        this.success = true;
    }
    
    public static ResponseDTO error(String message) {
        ResponseDTO dto = new ResponseDTO(false);
        dto.error = message;
        return dto;
    }

    //Getters and Setters
    public ResultFileMetadata getMetadata() {
        return metadata;
    }

    public String getStringResult() {
        return stringResult;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Object getPayload() {
        return payload;
    }
}