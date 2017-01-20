package com.wso2telco.services.dep.sandbox.dao.model.custom;

import com.wso2telco.services.dep.sandbox.util.RequestError;

public class ErrorResponseDTO {

	private RequestError requestError;
	
	
	public ErrorResponseDTO(RequestError requestError){
		this.requestError=requestError;
	}
	
	public RequestError getRequestError() {
		return requestError;
	}
	public void setRequestError(RequestError requestError) {
		this.requestError = requestError;
	}
}
