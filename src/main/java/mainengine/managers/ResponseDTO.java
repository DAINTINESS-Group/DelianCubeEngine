package mainengine.managers;

import result.ResultFileMetadata;

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