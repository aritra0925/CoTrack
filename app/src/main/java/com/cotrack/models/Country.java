package com.cotrack.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Country {

    @JsonProperty(value = "NewConfirmed")
    private String newConfirmed;
    @JsonProperty(value = "TotalConfirmed")
    private String totalConfirmed;
    @JsonProperty(value = "NewRecovered")
    private String newRecovered;
    @JsonProperty(value = "TotalRecovered")
    private String totalRecovered;
    @JsonProperty(value = "NewDeaths")
    private String newDeaths;
    @JsonProperty(value = "TotalDeaths")
    private String totalDeaths;
    @JsonProperty(value = "Country")
    private String country;

    public String getNewConfirmed() {
        return newConfirmed;
    }

    public void setNewConfirmed(String newConfirmed) {
        this.newConfirmed = newConfirmed;
    }

    public String getTotalConfirmed() {
        return totalConfirmed;
    }

    public void setTotalConfirmed(String totalConfirmed) {
        this.totalConfirmed = totalConfirmed;
    }

    public String getNewRecovered() {
        return newRecovered;
    }

    public void setNewRecovered(String newRecovered) {
        this.newRecovered = newRecovered;
    }

    public String getTotalRecovered() {
        return totalRecovered;
    }

    public void setTotalRecovered(String totalRecovered) {
        this.totalRecovered = totalRecovered;
    }

    public String getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(String newDeaths) {
        this.newDeaths = newDeaths;
    }

    public String getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(String totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
