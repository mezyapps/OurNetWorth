package com.mezyapps.ournetworth.MVP;

import java.util.HashMap;
import java.util.Map;

public class ICanConnectResponse {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    private String message;
    private String success;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}

