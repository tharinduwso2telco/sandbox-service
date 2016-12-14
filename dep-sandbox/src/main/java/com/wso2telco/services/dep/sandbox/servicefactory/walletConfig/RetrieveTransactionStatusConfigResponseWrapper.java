package com.wso2telco.services.dep.sandbox.servicefactory.walletConfig;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RetrieveTransactionStatusDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class RetrieveTransactionStatusConfigResponseWrapper extends AbstractReturnWrapperDTO{
	
	private RetrieveTransactionStatusDTO statusValueDTO;
	
	
	@Override
	public Object getResponse() {
		if (getRequestError() != null) {
		    return getRequestError();
		}
		return geStatusValueDTO();
	    }

	public RetrieveTransactionStatusDTO geStatusValueDTO() {
		return statusValueDTO;
	}

	public void setStatusValueDTO(RetrieveTransactionStatusDTO statusValueDTO) {
		this.statusValueDTO = statusValueDTO;
	}
	
	
	
}
