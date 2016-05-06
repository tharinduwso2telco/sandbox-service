package com.wso2telco.services.dep.sandbox.servicefactory;

import javax.ws.rs.core.Response.Status;

import com.wso2telco.oneapivalidation.exceptions.RequestError;

public abstract class AbstractReturnWrapperDTO implements Returnable {
	private Status httpStatus;
	private RequestError requestError;

	@Override
	public Status getHttpStatus() {
		// TODO Auto-generated method stub
		return httpStatus;
	}

	@Override
	public void setHttpStatus(Status httpStatus) {
		this.httpStatus=httpStatus;
		
	}

	public RequestError getRequestError() {
		return requestError;
	}

	public void setRequestError(RequestError requestError) {
		this.requestError = requestError;
	}
	
}
