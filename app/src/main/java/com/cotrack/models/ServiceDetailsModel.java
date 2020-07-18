package com.cotrack.models;

public class ServiceDetailsModel {

    private String serviceType;
    private int serviceCount;
    private int serviceLogo;

    public ServiceDetailsModel(String serviceType, int serviceCount, int serviceLogo){
        this.serviceCount = serviceCount;
        this.serviceLogo = serviceLogo;
        this.serviceType = serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setServiceCount(int serviceCount) {
        this.serviceCount = serviceCount;
    }

    public void setServiceLogo(int serviceLogo) {
        this.serviceLogo = serviceLogo;
    }

    public String getServiceType() {
        return serviceType;
    }

    public int getServiceCount() {
        return serviceCount;
    }

    public int getServiceLogo() {
        return serviceLogo;
    }
}
