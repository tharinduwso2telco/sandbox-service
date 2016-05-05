package com.wso2telco.services.dep.sandbox.dao.model.custom;

import java.util.ArrayList;
import java.util.List;

public class OutboundSMSMessageRequestBean {

	private OutboundSMSMessageRequest outboundSMSMessageRequest = null;

	public OutboundSMSMessageRequest getOutboundSMSMessageRequest() {
		return outboundSMSMessageRequest;
	}

	public void setOutboundSMSMessageRequest(OutboundSMSMessageRequest outboundSMSMessageRequest) {
		this.outboundSMSMessageRequest = outboundSMSMessageRequest;
	}

	public static class OutboundSMSMessageRequest {

		private List<String> address = new ArrayList<String>();
		private String senderAddress = null;
		private OutboundSMSTextMessage outboundSMSTextMessage = null;
		private String clientCorrelator = null;
		private ReceiptRequest receiptRequest = null;
		private String senderName = null;

		public List<String> getAddress() {
			return address;
		}

		public void setAddress(List<String> address) {
			this.address = address;
		}

		public String getSenderAddress() {
			return senderAddress;
		}

		public void setSenderAddress(String senderAddress) {
			this.senderAddress = senderAddress;
		}

		public OutboundSMSTextMessage getOutboundSMSTextMessage() {
			return outboundSMSTextMessage;
		}

		public void setOutboundSMSTextMessage(OutboundSMSTextMessage outboundSMSTextMessage) {
			this.outboundSMSTextMessage = outboundSMSTextMessage;
		}

		public String getClientCorrelator() {
			return clientCorrelator;
		}

		public void setClientCorrelator(String clientCorrelator) {
			this.clientCorrelator = clientCorrelator;
		}

		public ReceiptRequest getReceiptRequest() {
			return receiptRequest;
		}

		public void setReceiptRequest(ReceiptRequest receiptRequest) {
			this.receiptRequest = receiptRequest;
		}

		public String getSenderName() {
			return senderName;
		}

		public void setSenderName(String senderName) {
			this.senderName = senderName;
		}

		public static class OutboundSMSTextMessage {

			private String message = null;

			public String getMessage() {
				return message;
			}

			public void setMessage(String message) {
				this.message = message;
			}
		}

		public static class ReceiptRequest {

			private String notifyURL = null;
			private String callbackData = null;

			public String getNotifyURL() {
				return notifyURL;
			}

			public void setNotifyURL(String notifyURL) {
				this.notifyURL = notifyURL;
			}

			public String getCallbackData() {
				return callbackData;
			}

			public void setCallbackData(String callbackData) {
				this.callbackData = callbackData;
			}
		}
	}
}
