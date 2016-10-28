package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class CreditApplyRequestWrapper extends RequestDTO {
	
	private static final long serialVersionUID = -3609438606240260256L;
	
	private String msisdn;

	private CreditRequestBean creditRequestBean;

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public CreditRequestBean getCreditRequestBean() {
		return creditRequestBean;
	}

	public void setCreditRequestBean(CreditRequestBean creditRequestBean) {
		this.creditRequestBean = creditRequestBean;
	}
	
	

}
