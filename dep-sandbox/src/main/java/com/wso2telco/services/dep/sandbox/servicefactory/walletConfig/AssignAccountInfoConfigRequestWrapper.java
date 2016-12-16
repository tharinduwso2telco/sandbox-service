package com.wso2telco.services.dep.sandbox.servicefactory.walletConfig;

import com.wso2telco.services.dep.sandbox.dao.model.custom.AssignAccountInfoConfigRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;

public class AssignAccountInfoConfigRequestWrapper extends RequestDTO{

	private String endUserId;
	
	private AssignAccountInfoConfigRequestBean requestBean;
	
	public String getEndUserId() {
		return endUserId;
	}
	public void setEndUserId(String endUserId) {
		this.endUserId = endUserId;
	}
	public AssignAccountInfoConfigRequestBean getRequestBean() {
		return requestBean;
	}
	public void setRequestBean(AssignAccountInfoConfigRequestBean requestBean) {
		this.requestBean = requestBean;
	}
	
}
