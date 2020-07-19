package com.cotrack.models;

//import java.util.Random;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Random;

// A Java type that can be serialized to JSON
public class ProviderDetails {

    @JsonProperty(value = "_id")
    private String _id;

    @JsonProperty(value = "_rev")
    private String _rev = null;

    @JsonProperty(value = "provider_id")
    private String provider_id = "P" + Integer.toString(new Random().nextInt(900));

    @JsonProperty(value = "type")
    private String type;

    @JsonProperty(value = "provider_name")
    private String provider_name;

    @JsonProperty(value = "provider_signonid")
    private String provider_signonid;

    @JsonProperty(value = "provider_password")
    private String provider_password;

    @JsonProperty(value = "provider_address1")
    private String provider_address1;

    @JsonProperty(value = "provider_city")
    private String provider_city;

    @JsonProperty(value = "provider_state")
    private String provider_state;

    @JsonProperty(value = "provider_zip")
    private String provider_zip;

    @JsonProperty(value = "provider_contact")
    private String provider_contact;

    @JsonProperty(value = "provider_email")
    private String provider_email;

    @JsonProperty(value = "service_name")
    private String service_name;

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

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getProvider_signonid() {
        return provider_signonid;
    }

    public void setProvider_signonid(String provider_signonid) {
        this.provider_signonid = provider_signonid;
    }

    public String getProvider_password() {
        return provider_password;
    }

    public void setProvider_password(String provider_password) {
        this.provider_password = provider_password;
    }

    public String getProvider_address1() {
        return provider_address1;
    }

    public void setProvider_address1(String provider_address1) {
        this.provider_address1 = provider_address1;
    }

    public String getProvider_city() {
        return provider_city;
    }

    public void setProvider_city(String provider_city) {
        this.provider_city = provider_city;
    }

    public String getProvider_state() {
        return provider_state;
    }

    public void setProvider_state(String provider_state) {
        this.provider_state = provider_state;
    }

    public String getProvider_zip() {
        return provider_zip;
    }

    public void setProvider_zip(String provider_zip) {
        this.provider_zip = provider_zip;
    }

    public String getProvider_contact() {
        return provider_contact;
    }

    public void setProvider_contact(String provider_contact) {
        this.provider_contact = provider_contact;
    }

    public String getProvider_email() {
        return provider_email;
    }

    public void setProvider_email(String provider_email) {
        this.provider_email = provider_email;
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


    public ProviderDetails(String _id, String type, String provider_name, String provider_signonid, String provider_password, String provider_address1, String provider_city, String provider_state, String provider_zip, String provider_contact, String provider_email, String service_name, String asset_id, String sr_id) {
        this._id = _id;
        this.type = type;
        this.provider_name = provider_name;
        this.provider_signonid = provider_signonid;
        this.provider_password = provider_password;
        this.provider_address1 = provider_address1;
        this.provider_city = provider_city;
        this.provider_state = provider_state;
        this.provider_zip = provider_zip;
        this.provider_contact = provider_contact;
        this.provider_email = provider_email;
        this.service_name = service_name;
        this.asset_id = asset_id;
        this.sr_id = sr_id;
    }

    @Override
    public String toString() {
        return "ProviderDetails{" +
                "_id='" + _id + '\'' +
                ", _rev='" + _rev + '\'' +
                ", provider_id='" + provider_id + '\'' +
                ", type='" + type + '\'' +
                ", provider_name='" + provider_name + '\'' +
                ", provider_signonid='" + provider_signonid + '\'' +
                ", provider_password='" + provider_password + '\'' +
                ", provider_address1='" + provider_address1 + '\'' +
                ", provider_city='" + provider_city + '\'' +
                ", provider_state='" + provider_state + '\'' +
                ", provider_zip='" + provider_zip + '\'' +
                ", provider_contact='" + provider_contact + '\'' +
                ", provider_email='" + provider_email + '\'' +
                ", service_name='" + service_name + '\'' +
                ", asset_id='" + asset_id + '\'' +
                ", sr_id='" + sr_id + '\'' +
                '}';
    }
}