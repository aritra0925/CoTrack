package com.cotrack.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LatLangDetails {
    @JsonProperty(value = "latitude")
    public String latitude;
    @JsonProperty(value = "longitude")
    public String longitude;
    @JsonProperty(value = "upload_time")
    public String upload_time;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }
}
