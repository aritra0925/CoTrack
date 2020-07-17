package com.cotrack.dbutils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
public class UserDetails {
    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "SIGNON_ID")
    private String signOnId;

    @Column(name = "LOGIN_PASSWORD")
    private String password;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_ADDRESS1")
    private String userAdd;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE_NAME")
    private String state;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CONTACT_NUMBER")
    private String contactNo;

    //@OneToOne(cascade = CascadeType.ALL)
    @Column(name = "SERVICE_NAME")
    private String serviceAvailed;

    @Column(name = "SR_NO")
    private String serviceRequectNo;

    public UserDetails() {
    }

    public UserDetails(String userId, String signOnId, String password, String userName, String userAdd, String city,
                       String state, String zipCode, String email, String contactNo, String serviceAvailed, String serviceRequectNo) {
        super();
        this.userId = userId;
        this.signOnId = signOnId;
        this.password = password;
        this.userName = userName;
        this.userAdd = userAdd;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.email = email;
        this.contactNo = contactNo;
        this.serviceAvailed = serviceAvailed;
        this.serviceRequectNo = serviceRequectNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSignOnId() {
        return signOnId;
    }

    public void setSignOnId(String signOnId) {
        this.signOnId = signOnId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAdd() {
        return userAdd;
    }

    public void setUserAdd(String userAdd) {
        this.userAdd = userAdd;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getServiceAvailed() {
        return serviceAvailed;
    }

    public void setServiceAvailed(String serviceAvailed) {
        this.serviceAvailed = serviceAvailed;
    }

    public String getServiceRequectNo() {
        return serviceRequectNo;
    }

    public void setServiceRequectNo(String serviceRequectNo) {
        this.serviceRequectNo = serviceRequectNo;
    }

    @Override
    public String toString() {
        return "UserDetails [userId=" + userId + ", signOnId=" + signOnId + ", password=" + password + ", userName="
                + userName + ", userAdd=" + userAdd + ", city=" + city + ", state=" + state + ", zipCode=" + zipCode
                + ", email=" + email + ", contactNo=" + contactNo + ", serviceAvailed=" + serviceAvailed
                + ", serviceRequectNo=" + serviceRequectNo + "]";
    }
}
