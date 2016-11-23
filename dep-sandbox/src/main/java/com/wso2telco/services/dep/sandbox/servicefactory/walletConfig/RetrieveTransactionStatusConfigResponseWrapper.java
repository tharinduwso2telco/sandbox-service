package com.wso2telco.services.dep.sandbox.servicefactory.walletConfig;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RetrieveTransactionStatusDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class RetrieveTransactionStatusConfigResponseWrapper extends AbstractReturnWrapperDTO{
	
	private RetrieveTransactionStatusDTO statusValueDTO;
	
	//private String status;
	
	@Override
	public Object getResponse() {
		if (getRequestError() != null) {
		    return getRequestError();
		}
		return geStatusValueDTO();
	    }

//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}

	public RetrieveTransactionStatusDTO geStatusValueDTO() {
		return statusValueDTO;
	}

	public void setStatusValueDTO(RetrieveTransactionStatusDTO statusValueDTO) {
		this.statusValueDTO = statusValueDTO;
	}
	
	
	
}
