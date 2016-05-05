package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging;

import java.util.ArrayList;
import java.util.List;

public class OutboundSMSMessageResponseBean {
	
	private OutboundSMSMessageRequest outboundSMSMessageRequest = null;

	public OutboundSMSMessageRequest getOutboundSMSMessageRequest() {
		return outboundSMSMessageRequest;
	}

	public void setOutboundSMSMessageRequest(OutboundSMSMessageRequest outboundSMSMessageRequest) {
		this.outboundSMSMessageRequest = outboundSMSMessageRequest;
	}

	public static class OutboundSMSMessageRequest {
		
		private List<String> address = new ArrayList<String>();
		private DeliveryInfoList deliveryInfoList = null;
		private String senderAddress = null;
		private OutboundSMSTextMessage outboundSMSTextMessage = null;
		private String clientCorrelator = null;
		private ReceiptRequest receiptRequest = null;
		private String senderName = null;
		private String resourceURL = null;

		public List<String> getAddress() {
			return address;
		}

		public void setAddress(List<String> address) {
			this.address = address;
		}

		public DeliveryInfoList getDeliveryInfoList() {
			return deliveryInfoList;
		}

		public void setDeliveryInfoList(DeliveryInfoList deliveryInfoList) {
			this.deliveryInfoList = deliveryInfoList;
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

		public String getResourceURL() {
			return resourceURL;
		}

		public void setResourceURL(String resourceURL) {
			this.resourceURL = resourceURL;
		}

		public static class DeliveryInfoList {

			private String resourceURL = null;
			private List<DeliveryInfo> deliveryInfo = new ArrayList<DeliveryInfo>();

			public String getResourceURL() {
				return resourceURL;
			}

			public void setResourceURL(String resourceURL) {
				this.resourceURL = resourceURL;
			}

			public List<DeliveryInfo> getDeliveryInfo() {
				return deliveryInfo;
			}

			public void setDeliveryInfo(List<DeliveryInfo> deliveryInfo) {
				this.deliveryInfo = deliveryInfo;
			}

			public static class DeliveryInfo {

				private String address = null;
				private String deliveryStatus = null;

				public String getAddress() {
					return address;
				}

				public void setAddress(String address) {
					this.address = address;
				}

				public String getDeliveryStatus() {
					return deliveryStatus;
				}

				public void setDeliveryStatus(String deliveryStatus) {
					this.deliveryStatus = deliveryStatus;
				}
			}
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
