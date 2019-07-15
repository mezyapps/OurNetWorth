package com.mezyapps.ournetworth.MVP;

import java.util.HashMap;
import java.util.Map;

public class UserImageResponse {
    private String image;
    private String success;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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


}
