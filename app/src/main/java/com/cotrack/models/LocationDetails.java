package com.cotrack.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LocationDetails {

    @JsonProperty(value = "_rev")
    public String _rev;
    @JsonProperty(value = "_id")
    public String _id;
    @JsonProperty(value = "user_id")
    public String user_id;
    @JsonProperty(value = "")
    public List<LatLangDetails> latLang_details;

    @JsonProperty(value = "covid_test_status")
    public String covid_test_status;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getCovid_test_status() {
        return covid_test_status;
    }

    public void setCovid_test_status(String covid_test_status) {
        this.covid_test_status = covid_test_status;
    }

    public List<LatLangDetails> getLatLang_details() {
        return latLang_details;
    }

    public void setLatLang_details(List<LatLangDetails> latLang_details) {
        this.latLang_details = latLang_details;
    }

    public String get_rev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }
}
