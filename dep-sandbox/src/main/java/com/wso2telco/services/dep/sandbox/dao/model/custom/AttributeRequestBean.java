package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class AttributeRequestBean {

    private AttributeProperties requestObject;

    public AttributeProperties getAttribute() {
	return requestObject;
    }

    public void setAttribute(AttributeProperties requestObject) {
	this.requestObject = requestObject;
    }

    public static class AttributeProperties {

	private String attributeName;

	private String attributeValue;

	public String getName() {
	    return attributeName;
	}

	public void setName(String attributeName) {
	    this.attributeName = attributeName;
	}

	public String getValue() {
	    return attributeValue;
	}

	public void setValue(String attributeValue) {
	    this.attributeValue = attributeValue;
	}

    }
}
