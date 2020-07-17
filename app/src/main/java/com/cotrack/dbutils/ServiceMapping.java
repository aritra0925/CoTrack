package com.cotrack.dbutils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MAPPING")
public class ServiceMapping {
    @Id
    //@GeneratedValue
    @Column(name = "SR_NO")
    private String srNo;

    //	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "SERVICE_NAME")
    @Column(name = "SERVICE_NAME")
    private String serviceName;

    //	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "SP_ID")
    @Column(name = "SP_ID")
    private String spId;

    //	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "USER_ID")
    @Column(name = "USER_ID")
    private String userId;

    public ServiceMapping() {
    }

    public ServiceMapping(String srNo, String serviceName, String spId, String userId) {
        this.srNo = srNo;
        this.serviceName = serviceName;
        this.spId = spId;
        this.userId = userId;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ServiceMapping [srNo=" + srNo + ", serviceName=" + serviceName + ", spId=" + spId + ", userId=" + userId
                + "]";
    }
}
