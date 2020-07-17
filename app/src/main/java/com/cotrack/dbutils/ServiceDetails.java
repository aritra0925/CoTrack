package com.cotrack.dbutils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SERVICES")
public class ServiceDetails {
    @Id
    @Column(name = "SERVICE_NAME")
    private String serviceName;

    @Column(name = "SERVICE_ID")
    private String serviceId;

    @Column(name = "ASSET_ID")
    private String assetId;

    @Column(name = "SR_NO")
    private String srNo;

    public ServiceDetails() {
    }

    public ServiceDetails(String serviceName, String serviceId, String assetId, String srNo) {
        this.serviceName = serviceName;
        this.serviceId = serviceId;
        this.assetId = assetId;
        this.srNo = srNo;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    @Override
    public String toString() {
        return "ServiceDetails [serviceName=" + serviceName + ", serviceId=" + serviceId + ", assetId=" + assetId
                + ", srNo=" + srNo + "]";
    }

}
