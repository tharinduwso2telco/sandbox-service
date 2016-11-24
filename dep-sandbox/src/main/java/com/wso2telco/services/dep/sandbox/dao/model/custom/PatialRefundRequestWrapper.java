package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class PatialRefundRequestWrapper extends RequestDTO{
	
	private static final long serialVersionUID = -3609438606240260256L;
	
	private String msisdn;
	
	private RefundRequestBean refundRequestBean;

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public RefundRequestBean getRefundRequestBean() {
		return refundRequestBean;
	}

	public void setRefundRequestBean(RefundRequestBean refundRequestBean) {
		this.refundRequestBean = refundRequestBean;
	}
	

}
