package com.wso2telco.services.dep.sandbox.dao.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sbxattributevalue")
public class AttributeValues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sbxattributevaluedid")
    private int attributevalueId;

    @OneToOne
    @JoinColumn(name = "attributedistributiondid", referencedColumnName = "sbtattributedistributiondid")
    private AttributeDistribution attributeDistributionId;

    @Column(name = "tobject")
    private String tobject;

    @Column(name = "ownerdid")
    private int ownerdid;

    @Column(name = "value")
    private String value;

    public int getAttributeValueId() {
	return attributevalueId;
    }

    public void setAttributeValueId(int attributevalueId) {
	this.attributevalueId = attributevalueId;
    }

    public AttributeDistribution getAttributedid() {
	return attributeDistributionId;
    }

    public void setAttributedid(AttributeDistribution attributeDistributionId) {
	this.attributeDistributionId = attributeDistributionId;
    }

    public String getTobject() {
	return tobject;
    }

    public void setTobject(String tobject) {
	this.tobject = tobject;
    }

    public int getOwnerdid() {
	return ownerdid;
    }

    public void setOwnerdid(int ownerdid) {
	this.ownerdid = ownerdid;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

}