package com.wso2telco.services.dep.sandbox.servicefactory.walletConfig;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.AssignTransactionStatusConfigRequestBean;

public class AssignTransactionStatusConfigRequestWrapper extends RequestDTO{

	private String endUserId;
	
	private AssignTransactionStatusConfigRequestBean requestBean;
	
	public String getEndUserId() {
		return endUserId;
	}
	public void setEndUserId(String endUserId) {
		this.endUserId = endUserId;
	}
	public AssignTransactionStatusConfigRequestBean getRequestBean() {
		return requestBean;
	}
	public void setRequestBean(AssignTransactionStatusConfigRequestBean requestBean) {
		this.requestBean = requestBean;
	}
	
}
