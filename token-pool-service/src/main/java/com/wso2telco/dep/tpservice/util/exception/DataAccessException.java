package com.wso2telco.dep.tpservice.util.exception;

public class DataAccessException extends Exception {

	private static final long serialVersionUID = 7218345714828200646L;

	private String code;
	private String message;

	public DataAccessException(final String code, final String message) {
		this.setMessage(message);
		this.setCode(code);
	}

	public String getCode() {
		return this.code;
	}

	private void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return this.message;
	}

	private void setMessage(String message) {
		this.message = message;
	}
}
