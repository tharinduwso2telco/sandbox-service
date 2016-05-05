package com.wso2telco.services.dep.sandbox.servicefactory.location;

import javax.ws.rs.core.Response.Status;

import com.wso2telco.oneapivalidation.exceptions.RequestError;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;

class LocationResponseWrapperDTO implements Returnable {

	private RequestError requestError;
	private  TerminalLocationList terminalLocationList ;
	private Status httpStatus;
	private String location;
	
	
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Status getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(Status httpStatus) {
		this.httpStatus = httpStatus;
	}

	public TerminalLocationList getTerminalLocationList() {
		return terminalLocationList;
	}

	public void setTerminalLocationList(TerminalLocationList terminalLocationList) {
		this.terminalLocationList = terminalLocationList;
	}

	@Override
	public Object getResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public RequestError getRequestError() {
		return requestError;
	}

	public void setRequestError(RequestError requestError) {
		this.requestError = requestError;
	}

	
	
}
