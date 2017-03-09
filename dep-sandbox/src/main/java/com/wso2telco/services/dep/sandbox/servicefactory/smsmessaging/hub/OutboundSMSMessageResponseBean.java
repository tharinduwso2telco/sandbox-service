package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.hub;

import com.wso2telco.services.dep.sandbox.dao.model.custom.DeliveryInfoList;
import com.wso2telco.services.dep.sandbox.dao.model.custom.OutboundSMSMessageRequest;
import com.wso2telco.services.dep.sandbox.dao.model.custom.SenderDetails;

import java.util.ArrayList;
import java.util.List;

 class OutboundSMSMessageResponseBean {
	
	private OutboundSMSMessageResponse outboundSMSMessageRequest;

    public OutboundSMSMessageResponse getOutboundSMSMessageRequest() {
        return outboundSMSMessageRequest;
    }

    public void setOutboundSMSMessageRequest(OutboundSMSMessageResponse outboundSMSMessageResponse) {
        this.outboundSMSMessageRequest = outboundSMSMessageResponse;
    }

     static class OutboundSMSMessageResponse extends OutboundSMSMessageRequest {


		private DeliveryInfoList deliveryInfoList;
		private List<SenderDetails> senderAddress;
        private String resourceURL;


		public DeliveryInfoList getDeliveryInfoList() {
			return deliveryInfoList;
		}

		public void setDeliveryInfoList(DeliveryInfoList deliveryInfoList) {
			this.deliveryInfoList = deliveryInfoList;
		}

        public List<SenderDetails> getSenderAddress() {
            return senderAddress;
        }

        public void setSenderAddress(List<SenderDetails> senderAddress) {
            this.senderAddress = senderAddress;
        }

        public String getResourceURL() {
            return resourceURL;
        }

        public void setResourceURL(String resourceURL) {
            this.resourceURL = resourceURL;
        }
    }
}
