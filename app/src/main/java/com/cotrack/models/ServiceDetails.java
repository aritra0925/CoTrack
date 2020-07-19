package com.cotrack.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Random;

// A Java type that can be serialized to JSON
public class ServiceDetails {
    @JsonProperty(value = "_id")
    private String _id;

    @JsonProperty(value = "_rev")
    private String _rev = null;

    @JsonProperty(value = "type")
    private String type;

    @JsonProperty(value = "service_id")
    private String service_id;

    @JsonProperty(value = "service_name")
    private  String service_name;

    @JsonProperty(value = "asset_id")
    private String asset_id;

    @JsonProperty(value = "sr_id")
    private String sr_id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_rev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(String asset_id) {
        this.asset_id = asset_id;
    }

    public String getSr_id() {
        return sr_id;
    }

    public void setSr_id(String sr_id) {
        this.sr_id = sr_id;
    }

    public ServiceDetails(String _id, String type, String service_id, String service_name, String asset_id, String sr_id) {
        this._id = _id;
        this.type = type;
        this.service_id = service_id;
        this.service_name = service_name;
        this.asset_id = asset_id;
        this.sr_id = sr_id;
    }

    @Override
    public String toString() {
        return "Service{" +
                "_id='" + _id + '\'' +
                ", _rev='" + _rev + '\'' +
                ", service_id='" + service_id + '\'' +
                ", service_name='" + service_name + '\'' +
                ", asset_id='" + asset_id + '\'' +
                ", sr_id='" + sr_id + '\'' +
                '}';
    }
}