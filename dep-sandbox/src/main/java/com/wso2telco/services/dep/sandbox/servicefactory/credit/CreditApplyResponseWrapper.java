package com.wso2telco.services.dep.sandbox.servicefactory.credit;

import com.wso2telco.services.dep.sandbox.dao.model.custom.CreditApplyResponseBean;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class CreditApplyResponseWrapper extends AbstractReturnWrapperDTO {
	
	private CreditApplyResponseBean creditApplyResponseBean;

	@Override
	public Object getResponse() {
		if (getRequestError() != null) {
			return getRequestError();
		}

		return creditApplyResponseBean;
	}

	public CreditApplyResponseBean getCreditApplyResponseBean() {
		return creditApplyResponseBean;
	}

	public void setCreditApplyResponseBean(CreditApplyResponseBean creditApplyResponseBean) {
		this.creditApplyResponseBean = creditApplyResponseBean;
	}
	
	

}
