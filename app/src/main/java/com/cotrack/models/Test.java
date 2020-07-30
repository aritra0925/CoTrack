package com.cotrack.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Test {

    @JsonProperty(value = "test_type")
    public String test_type;
    @JsonProperty(value = "test_status")
    public String test_status;

    public String getTest_type() {
        return test_type;
    }

    public void setTest_type(String test_type) {
        this.test_type = test_type;
    }

    public String getTest_status() {
        return test_status;
    }

    public void setTest_status(String test_status) {
        this.test_status = test_status;
    }
}