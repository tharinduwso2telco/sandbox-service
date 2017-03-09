package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.hub;

import com.wso2telco.services.dep.sandbox.dao.model.custom.OutboundSMSMessageRequest;
import com.wso2telco.services.dep.sandbox.dao.model.custom.SenderDetails;

import java.util.ArrayList;
import java.util.List;

public class outboundSMSMessageRequestBeanHub {

	private OutboundSMSMessageRequest_Hub outboundSMSMessageRequest;

	public OutboundSMSMessageRequest_Hub getOutboundSMSMessageRequest() {
		return outboundSMSMessageRequest;
	}

	public void setOutboundSMSMessageRequest(OutboundSMSMessageRequest_Hub outboundSMSMessageRequestHub) {
		this.outboundSMSMessageRequest = outboundSMSMessageRequestHub;
	}

	public class OutboundSMSMessageRequest_Hub extends OutboundSMSMessageRequest {


		private List<SenderDetails> senderAddress;

        public List<SenderDetails> getSenderAddress() {
            return senderAddress;
        }

        public void setSenderAddress(List<SenderDetails> senderAddress) {
            this.senderAddress = senderAddress;
        }
    }
}
