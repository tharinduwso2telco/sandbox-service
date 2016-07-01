package com.wso2telco.dep.tpservice.util.exception;

import com.wso2telco.dep.tpservice.util.exception.ThrowableError;

public enum GenaralError implements ThrowableError {

	UNDEFINED("TP0001", "Undefined Error"),
	INTERNAL_SERVER_ERROR("TP0002", "Internal Server Error"),
	INPUT_PARAMETER_ERROR("TP0003", "Input Parameter Error");

	private String code;
	private String desc;

	GenaralError(final String code, final String desc) {
		this.desc = desc;
		this.code = code;
	}

	@Override
	public String getMessage() {
		return this.desc;
	}

	@Override
	public String getCode() {
		return this.code;
	}
}
