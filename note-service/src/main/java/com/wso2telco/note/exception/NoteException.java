package com.wso2telco.note.exception;

import com.wso2telco.note.util.ErrorCodes;

public class NoteException extends Exception {

	private static final long serialVersionUID = 7101686903987680125L;
	private ErrorCodes error = null;

	public NoteException(ErrorCodes error) {

		this.error = error;
	}

	public NoteException(ErrorCodes error, Exception e) {
		super(e);

		this.error = error;
	}

	public ErrorCodes getError() {

		return error;
	}

	@Override
	public String toString() {
		
		return "NoteException [error=" + error + "]" + super.toString();
	}
}
