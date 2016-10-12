package com.wso2telco.services.dep.sandbox.dao.model.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sbxattribute")
public class Attributes implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attributeId;

    @Column(name = "name")
    private String attributeName;

    public int getAttributeId() {
	return attributeId;
    }

    public void setAttributeId(int attributeId) {
	this.attributeId = attributeId;
    }

    public String getAttributeName() {
	return attributeName;
    }

    public void eetAttributeName(String attributeName) {
	this.attributeName = attributeName;
    }
}
