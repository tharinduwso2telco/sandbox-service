package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.hub;

import com.wso2telco.services.dep.sandbox.dao.model.custom.ErrorResponseDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

 class SendMTSMSResponseWrapper extends AbstractReturnWrapperDTO {

	private OutboundSMSMessageResponseBean outboundSMSMessageResponseBeanHub;
	
	public OutboundSMSMessageResponseBean getOutboundSMSMessageResponseBeanHub() {
		return outboundSMSMessageResponseBeanHub;
	}

	public void setOutboundSMSMessageResponseBeanHub(OutboundSMSMessageResponseBean outboundSMSMessageResponseBeanHub) {
		this.outboundSMSMessageResponseBeanHub = outboundSMSMessageResponseBeanHub;
	}

	@Override
	public Object getResponse() {


		if (getRequestError() == null) {
			return outboundSMSMessageResponseBeanHub;
		}else{
			ErrorResponseDTO response= new ErrorResponseDTO(getRequestError());
			return response;
		}

	}


	}

