package com.mezyapps.ournetworth.MVP;

import java.util.HashMap;
import java.util.Map;

public class SignUpResponse {

    private String success;
    private String message;
    private Integer user_id;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUserid() {
        return user_id;
    }

    public void setUserid(Integer userid) {
        this.user_id = userid;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}