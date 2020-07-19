package com.cotrack.models;

//import java.util.Random;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Random;

// A Java type that can be serialized to JSON
public class UserDetails {

    @JsonProperty(value = "_id")
    private String _id;

    @JsonProperty(value = "user_id")
    private String user_id = "U" + Integer.toString(new Random().nextInt(900));

    @JsonProperty(value = "_rev")
    private String _rev = null;

    @JsonProperty(value = "type")
    private String type;

    @JsonProperty(value = "user_name")
    private String user_name;

    @JsonProperty(value = "user_signonid")
    private String user_signonid;

    @JsonProperty(value = "user_password")
    private String user_password;

    @JsonProperty(value = "user_address1")
    private String user_address1;

    @JsonProperty(value = "user_city")
    private String user_city;

    @JsonProperty(value = "user_state")
    private String user_state;

    @JsonProperty(value = "user_zip")
    private String user_zip;

    @JsonProperty(value = "user_contact")
    private String user_contact;

    @JsonProperty(value = "user_email")
    private String user_email;

    @JsonProperty(value = "service_name")
    private String service_name;

    @JsonProperty(value = "sr_id")
    private String sr_id;

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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_signonid() {
        return user_signonid;
    }

    public void setUser_signonid(String user_signonid) {
        this.user_signonid = user_signonid;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_address1() {
        return user_address1;
    }

    public void setUser_address1(String user_address1) {
        this.user_address1 = user_address1;
    }

    public String getUser_city() {
        return user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    public String getUser_state() {
        return user_state;
    }

    public void setUser_state(String user_state) {
        this.user_state = user_state;
    }

    public String getUser_zip() {
        return user_zip;
    }

    public void setUser_zip(String user_zip) {
        this.user_zip = user_zip;
    }

    public String getUser_contact() {
        return user_contact;
    }

    public void setUser_contact(String user_contact) {
        this.user_contact = user_contact;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getSr_id() {
        return sr_id;
    }

    public void setSr_id(String sr_id) {
        this.sr_id = sr_id;
    }

    public UserDetails(String _id, String type, String user_name, String user_signonid, String user_password, String user_address1, String user_city, String user_state, String user_zip, String user_contact, String user_email, String service_name, String sr_id) {
        this._id = _id;
        this.type = type;
        this.user_name = user_name;
        this.user_signonid = user_signonid;
        this.user_password = user_password;
        this.user_address1 = user_address1;
        this.user_city = user_city;
        this.user_state = user_state;
        this.user_zip = user_zip;
        this.user_contact = user_contact;
        this.user_email = user_email;
        this.service_name = service_name;
        this.sr_id = sr_id;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "_id='" + _id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", _rev='" + _rev + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_signonid='" + user_signonid + '\'' +
                ", user_password='" + user_password + '\'' +
                ", user_address1='" + user_address1 + '\'' +
                ", user_city='" + user_city + '\'' +
                ", user_state='" + user_state + '\'' +
                ", user_zip='" + user_zip + '\'' +
                ", user_contact='" + user_contact + '\'' +
                ", user_email='" + user_email + '\'' +
                ", service_name='" + service_name + '\'' +
                ", sr_id='" + sr_id + '\'' +
                '}';
    }
}