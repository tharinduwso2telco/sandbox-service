package com.wso2telco.services.dep.sandbox.servicefactory;

import javax.ws.rs.core.Response.Status;

import com.wso2telco.dep.oneapivalidation.exceptions.RequestError;

public interface Returnable {

	public Object getResponse(); 
	
	public Status getHttpStatus();
	public void setRequestError(RequestError requestError);
	public void setHttpStatus(Status httpStatus) ;
}
