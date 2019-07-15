package com.mezyapps.ournetworth.MVP;

import java.util.HashMap;
import java.util.Map;

public class AllAskResponse {
    private String success;
    private String message;
    private String full_name;
    private Integer g_a_id;
    private Integer user_id;
    private  String date_time;
    private String day;
    private String contact_name;
    private String category;
    private String company_name;
    private Integer like_count;
    private Integer connected_by_user_id;
    private String cb_name;
    private String cb_category;
    private String cb_company_name;
    private String cb_mobile;

    public String getCb_name() {
        return cb_name;
    }

    public void setCb_name(String cb_name) {
        this.cb_name = cb_name;
    }

    public String getCb_category() {
        return cb_category;
    }

    public void setCb_category(String cb_category) {
        this.cb_category = cb_category;
    }

    public String getCb_company_name() {
        return cb_company_name;
    }

    public void setCb_company_name(String cb_company_name) {
        this.cb_company_name = cb_company_name;
    }

    public String getCb_mobile() {
        return cb_mobile;
    }

    public void setCb_mobile(String cb_mobile) {
        this.cb_mobile = cb_mobile;
    }

    public String getCb_email() {
        return cb_email;
    }

    public void setCb_email(String cb_email) {
        this.cb_email = cb_email;
    }

    public String getCb_address() {
        return cb_address;
    }

    public void setCb_address(String cb_address) {
        this.cb_address = cb_address;
    }

    public String getCb_image() {
        return cb_image;
    }

    public void setCb_image(String cb_image) {
        this.cb_image = cb_image;
    }

    private String cb_email;
    private String cb_address;
    private String cb_image;


    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    public Integer getConnected_by_user_id() {
        return connected_by_user_id;
    }

    public void setConnected_by_user_id(Integer connected_by_user_id) {
        this.connected_by_user_id = connected_by_user_id;
    }

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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Integer getG_a_id() {
        return g_a_id;
    }

    public void setG_a_id(Integer g_a_id) {
        this.g_a_id = g_a_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public Integer getLike_count() {
        return like_count;
    }

    public void setLike_count(Integer like_count) {
        this.like_count = like_count;
    }





    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}

