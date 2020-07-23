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
    private String service_name;

    @JsonProperty(value = "service_description")
    private String service_description;

    @JsonProperty(value = "asset_id")
    private String asset_id;

    @JsonProperty(value = "sr_id")
    private String sr_id;

    @JsonProperty(value = "city")
    private String city;

    @JsonProperty(value = "address_line")
    private String address_line;

    @JsonProperty(value = "state")
    private String state;

    @JsonProperty(value = "postal_code")
    private String postal_code;

    @JsonProperty(value = "contact")
    private String contact;

    @JsonProperty(value = "isNew")
    private String isNew = "true";

    @JsonProperty(value = "isLoading")
    private String isLoading = "false";

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

    public String getService_description() {
        return service_description;
    }

    public void setService_description(String service_description) {
        this.service_description = service_description;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress_line() {
        return address_line;
    }

    public void setAddress_line(String address_line) {
        this.address_line = address_line;
    }

    public String isLoading() {
        return isLoading;
    }

    public void setLoading(String loading) {
        isLoading = loading;
    }

    public String isNew() {
        return isNew;
    }

    public void setNew(String aNew) {
        isNew = aNew;
    }

    public ServiceDetails(String _id, String type, String service_id, String service_name, String service_description, String asset_id, String sr_id, String address, String city, String state, String postalCode, String contact) {
        this._id = _id;
        this.type = type;
        this.service_id = service_id;
        this.service_name = service_name;
        this.service_description = service_description;
        this.asset_id = asset_id;
        this.sr_id = sr_id;
        this.address_line = address;
        this.city = city;
        this.state = state;
        this.postal_code = postalCode;
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Service{" +
                "_id='" + _id + '\'' +
                ", _rev='" + _rev + '\'' +
                ", service_id='" + service_id + '\'' +
                ", service_name='" + service_name + '\'' +
                ", service_description='" + service_description + '\'' +
                ", asset_id='" + asset_id + '\'' +
                ", sr_id='" + sr_id + '\'' +
                ", address_line='" + address_line + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postal_code='" + postal_code + '\'' +
                ", contact='" + contact + '\'' +
                ", type='" + type + '\'' +
                ", isNew='" + isNew + '\'' +
                ", isLoading='" + isLoading + '\'' +
                '}';
    }
}