package com.wso2telco.services.dep.sandbox.dao.model.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sbxapiservicecalls")
public class APIServiceCalls implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int apiServiceCallId;

    @ManyToOne
    @JoinColumn(name = "apitypesdid", referencedColumnName = "id")
    private APITypes apiType;

    @Column(name = "service")
    private String serviceName;

    public int getApiServiceCallId() {
	return apiServiceCallId;
    }

    public void setApiServiceCallId(int apiServiceCallId) {
	this.apiServiceCallId = apiServiceCallId;
    }

    public APITypes getAPIType() {
	return apiType;
    }

    public void setAPIType(APITypes apiType) {
	this.apiType = apiType;
    }

    public String getServiceName() {
	return serviceName;
    }

    public void setServiceName(String serviceName) {
	this.serviceName = serviceName;
    }
}
