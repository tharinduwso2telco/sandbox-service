package com.wso2telco.note.exception;

public class NoteException extends Exception {

	private static final long serialVersionUID = 7101686903987680125L;
	private String errorCode;
    private String errorMessage;
    private String errorVariable;
    
    public NoteException(String errorCode, String errorMessage, String errorVariable) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorVariable = errorVariable;
    }

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorVariable() {
		return errorVariable;
	}
}
