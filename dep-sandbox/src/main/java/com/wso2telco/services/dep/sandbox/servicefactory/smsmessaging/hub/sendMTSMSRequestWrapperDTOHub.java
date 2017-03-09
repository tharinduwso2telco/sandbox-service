package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.hub;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;

public class sendMTSMSRequestWrapperDTOHub extends RequestDTO {

	private static final long serialVersionUID = 796868921376883562L;
	private String shortCode;
	private com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.hub.outboundSMSMessageRequestBeanHub outboundSMSMessageRequestBeanHub;

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.hub.outboundSMSMessageRequestBeanHub getOutboundSMSMessageRequestBean() {
		return outboundSMSMessageRequestBeanHub;
	}

	public void setOutboundSMSMessageRequestBean(com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.hub.outboundSMSMessageRequestBeanHub outboundSMSMessageRequestBeanHub) {
		this.outboundSMSMessageRequestBeanHub = outboundSMSMessageRequestBeanHub;
	}
}
