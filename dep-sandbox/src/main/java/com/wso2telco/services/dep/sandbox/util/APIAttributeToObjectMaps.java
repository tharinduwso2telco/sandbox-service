package com.wso2telco.services.dep.sandbox.util;

public enum APIAttributeToObjectMaps {

	CUSTOMER("user"),
	PROVISIONING("number");
	
	private String tableName;

	APIAttributeToObjectMaps(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return this.tableName;
	}

}
