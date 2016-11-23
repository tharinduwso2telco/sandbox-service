package com.wso2telco.services.dep.sandbox.servicefactory.walletConfig;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;

public class RetrieveAccountStatusConfigRequestWrapper extends RequestDTO{

	//private String endUserId;
	
	private String apiType;
	
	private String serviceCall;

	public String getServiceCall() {
		return serviceCall;
	}

	public void setServiceCall(String serviceCall) {
		this.serviceCall = serviceCall;
	}

	public String getApiType() {
		return apiType;
	}

	public void setApiType(String apiType) {
		this.apiType = apiType;
	}
}
