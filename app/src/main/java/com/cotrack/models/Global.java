package com.cotrack.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Global {

    @JsonProperty(value = "Global")
    private Countries global;

    @JsonProperty(value = "Countries")
    private List<Country> countryList;


    public Countries getGlobal() {
        return global;
    }

    public void setGlobal(Countries global) {
        this.global = global;
    }


    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }
}
