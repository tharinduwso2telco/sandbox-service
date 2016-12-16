package com.wso2telco.services.dep.sandbox.servicefactory.walletConfig;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;

public class RetrieveTransactionStatusConfigRequestWrapper extends RequestDTO{
	
	private String apiType;
	
	private String serviceCall;
	
	public String getApiType() {
		return apiType;
	}

	public void setApiType(String apiType) {
		this.apiType = apiType;
	}

	public String getServiceCall() {
		return serviceCall;
	}

	public void setServiceCall(String serviceCall) {
		this.serviceCall = serviceCall;
	}
}
