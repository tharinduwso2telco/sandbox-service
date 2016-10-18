package com.wso2telco.services.dep.sandbox.util;

public enum AttributeValueJsonObject {

    BASIC, BILLING, ACCOUNT, ADDRESS, IDENTIFICATION, ADDITIONALINFO;

    public String toString() {
	return name().toLowerCase();

    }
}
