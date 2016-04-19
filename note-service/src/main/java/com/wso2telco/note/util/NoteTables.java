package com.wso2telco.note.util;

public enum NoteTables {

	NSNOTES("nstnotes"), NSXNOTETYPE("nsxnotetype"), NSXRESLUTIONTYPE("nsxreslutiontype");

	private String tableName;

	NoteTables(String tableName) {
		
		this.tableName = tableName;
	}

	public String getTableName() {

		return this.tableName;
	}
}
