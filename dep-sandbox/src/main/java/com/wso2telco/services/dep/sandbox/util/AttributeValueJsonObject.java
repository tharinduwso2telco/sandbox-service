package com.wso2telco.services.dep.sandbox.util;

public enum AttributeValueJsonObject {

    BASIC, BILLING, ACCOUNT, ADDRESS, IDENTIFICATION, ADDITIONAL_INFO;

    public String toString() {
	return name().toLowerCase();

    }
}
