package com.wso2telco.services.dep.sandbox.dao.model.custom;

import java.io.Serializable;

import com.wso2telco.services.dep.sandbox.util.RequestType;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

public class RequestDTO implements Serializable {

	private static final long serialVersionUID = -57225936985453608L;

	private RequestType requestType;
	private String requestPath;
	private HttpRequest httpRequest;
	

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

	public HttpMethod getHttpMethod() {
		return httpRequest.getMethod();
	}


	public HttpRequest getHttpRequest() {
		return httpRequest;
	}

	public void setHttpRequest(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}
	
}
