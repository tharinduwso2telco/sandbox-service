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
@Table(name = "sbtattributedistribution")
public class AttributeDistribution implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int distributionId;

    @ManyToOne
    @JoinColumn(name = "attributedid", referencedColumnName = "sbxattributedid")
    private Attributes attribute;

    @ManyToOne
    @JoinColumn(name = "apiservicecallsdid", referencedColumnName = "sbxapiservicecallsdid")
    private APIServiceCalls serviceCall;

    public int getAttributeDistributionId() {
	return distributionId;
    }

    public void setAttributeDistributionId(int distributionId) {
	this.distributionId = distributionId;
    }

    public Attributes getAttribute() {
	return attribute;
    }

    public void setAttribute(Attributes attribute) {
	this.attribute = attribute;
    }

    public APIServiceCalls getAPIServiceCall() {
	return serviceCall;
    }

    public void setAPIServiceCall(APIServiceCalls serviceCall) {
	this.serviceCall = serviceCall;
    }

}
