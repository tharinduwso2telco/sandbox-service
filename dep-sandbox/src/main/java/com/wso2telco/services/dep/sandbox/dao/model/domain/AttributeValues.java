package com.wso2telco.services.dep.sandbox.dao.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sbxattributevalue")
public class AttributeValues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sbxattributevaluedid")
    private int attributevalueId;

    @ManyToOne
    @JoinColumn(name = "attributedistributiondid", referencedColumnName = "sbtattributedistributiondid")
    private AttributeDistribution attributeDistribution;

    
    @Column(name = "tobject")
    private String tobject;

    @Column(name = "ownerdid")
    private int ownerdid;

    @Column(name = "value")
    private String value;

    
    
    public AttributeDistribution getAttributeDistribution() {
		return attributeDistribution;
	}

	public void setAttributeDistribution(AttributeDistribution attributeDistribution) {
		this.attributeDistribution = attributeDistribution;
	}

	public int getAttributeValueId() {
	return attributevalueId;
    }

    public void setAttributeValueId(int attributevalueId) {
	this.attributevalueId = attributevalueId;
    }
    @Deprecated
    /**
     * user getAttributeDistribution
     * @return
     */
    public AttributeDistribution getAttributedid() {
	return attributeDistribution;
    }
    @Deprecated
    /**
     * use setAttributeDistribution
     * @param attributeDistributionId
     */
    public void setAttributedid(AttributeDistribution attributeDistributionId) {
	this.attributeDistribution = attributeDistributionId;
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