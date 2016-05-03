package com.wso2telco.services.dep.sandbox.dao.model;

import java.io.Serializable;

import com.wso2telco.services.dep.sandbox.util.RequestType;

public class RequestDTO implements Serializable{

	private static final long serialVersionUID = -57225936985453608L;
	
	private RequestType requestType;

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}
}
