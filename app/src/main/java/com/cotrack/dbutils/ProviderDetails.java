package com.cotrack.dbutils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROVIDER")
public class ProviderDetails {
    @Id
    @Column(name = "SP_ID")
    private String spId;

    @Column(name = "SP_SIGNON_ID")
    private String spSignOnId;

    @Column(name = "LOGIN_PASSWORD")
    private String loginPassword;

    @Column(name = "SP_NAME")
    private String spName;

    @Column(name = "SP_ADDRESS")
    private String spAddress;

    @Column(name = "CITY")
    private String spCity;

    @Column(name = "STATE_NAME")
    private String spState;

    @Column(name = "ZIP_CODE")
    private String spZipCode;

    @Column(name = "EMAIL")
    private String spEmail;

    @Column(name = "CONTACT_NUMBER")
    private String spContact;

    //@OneToOne(cascade = CascadeType.ALL)
    @Column(name = "SERVICE_NAME")
    private String serviceOffered;

    @Column(name = "NO_OF_ASSETS")
    private int noOfAssets;

    @Column(name = "ASSET_ID")
    private String spAssetId;

    @Column(name = "SR_NO")
    private String srNo;

    public ProviderDetails() {
    }

    public ProviderDetails(String spId, String spSignOnId, String loginPassword, String spName, String spAddress,
                           String spCity, String spState, String spZipCode, String spEmail, String spContact, String serviceOffered,
                           int noOfAssets, String spAssetId, String srNo) {
        super();
        this.spId = spId;
        this.spSignOnId = spSignOnId;
        this.loginPassword = loginPassword;
        this.spName = spName;
        this.spAddress = spAddress;
        this.spCity = spCity;
        this.spState = spState;
        this.spZipCode = spZipCode;
        this.spEmail = spEmail;
        this.spContact = spContact;
        this.serviceOffered = serviceOffered;
        this.noOfAssets = noOfAssets;
        this.spAssetId = spAssetId;
        this.srNo = srNo;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getSpSignOnId() {
        return spSignOnId;
    }

    public void setSpSignOnId(String spSignOnId) {
        this.spSignOnId = spSignOnId;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getSpAddress() {
        return spAddress;
    }

    public void setSpAddress(String spAddress) {
        this.spAddress = spAddress;
    }

    public String getSpCity() {
        return spCity;
    }

    public void setSpCity(String spCity) {
        this.spCity = spCity;
    }

    public String getSpState() {
        return spState;
    }

    public void setSpState(String spState) {
        this.spState = spState;
    }

    public String getSpZipCode() {
        return spZipCode;
    }

    public void setSpZipCode(String spZipCode) {
        this.spZipCode = spZipCode;
    }

    public String getSpEmail() {
        return spEmail;
    }

    public void setSpEmail(String spEmail) {
        this.spEmail = spEmail;
    }

    public String getSpContact() {
        return spContact;
    }

    public void setSpContact(String spContact) {
        this.spContact = spContact;
    }

    public String getServiceOffered() {
        return serviceOffered;
    }

    public void setServiceOffered(String serviceOffered) {
        this.serviceOffered = serviceOffered;
    }

    public int getNoOfAssets() {
        return noOfAssets;
    }

    public void setNoOfAssets(int noOfAssets) {
        this.noOfAssets = noOfAssets;
    }

    public String getSpAssetId() {
        return spAssetId;
    }

    public void setSpAssetId(String spAssetId) {
        this.spAssetId = spAssetId;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    @Override
    public String toString() {
        return "ProviderDetails [spId=" + spId + ", spSignOnId=" + spSignOnId + ", loginPassword=" + loginPassword
                + ", spName=" + spName + ", spAddress=" + spAddress + ", spCity=" + spCity + ", spState=" + spState
                + ", spZipCode=" + spZipCode + ", spEmail=" + spEmail + ", spContact=" + spContact + ", serviceOffered="
                + serviceOffered + ", noOfAssets=" + noOfAssets + ", spAssetId=" + spAssetId + ", srNo=" + srNo + "]";
    }

}
