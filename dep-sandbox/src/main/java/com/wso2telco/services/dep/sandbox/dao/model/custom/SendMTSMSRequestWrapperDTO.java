package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class SendMTSMSRequestWrapperDTO extends RequestDTO {

	private static final long serialVersionUID = 796868921376883562L;
	private String shortCode;
	private OutboundSMSMessageRequestBean outboundSMSMessageRequestBean;

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public OutboundSMSMessageRequestBean getOutboundSMSMessageRequestBean() {
		return outboundSMSMessageRequestBean;
	}

	public void setOutboundSMSMessageRequestBean(OutboundSMSMessageRequestBean outboundSMSMessageRequestBean) {
		this.outboundSMSMessageRequestBean = outboundSMSMessageRequestBean;
	}
}
