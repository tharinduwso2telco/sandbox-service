package com.wso2telco.services.dep.sandbox.servicefactory.walletConfig;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RetrieveAccountStatusConfigDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class RetrieveAccountStatusConfigResponseWrapper extends AbstractReturnWrapperDTO{
	
	private RetrieveAccountStatusConfigDTO accountStatusDTO;
	
	
	@Override
	public Object getResponse() {
		if (getRequestError() != null) {
		    return getRequestError();
		}
		return geAccountStatusDTO();
	    }

	public RetrieveAccountStatusConfigDTO geAccountStatusDTO() {
		return accountStatusDTO;
	}

	public void setAccountStatusDTO(RetrieveAccountStatusConfigDTO accountStatusDTO) {
		this.accountStatusDTO = accountStatusDTO;
	}
	
	
	
}
