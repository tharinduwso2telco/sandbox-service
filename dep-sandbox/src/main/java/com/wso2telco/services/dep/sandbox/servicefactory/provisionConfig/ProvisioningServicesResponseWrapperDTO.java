package com.wso2telco.services.dep.sandbox.servicefactory.provisionConfig;

import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class ProvisioningServicesResponseWrapperDTO extends AbstractReturnWrapperDTO{
	
	private String status;
	
	
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public Object getResponse() {
				
		if(getRequestError() != null){
			return getRequestError();
		}
		return getStatus();
	}

}
