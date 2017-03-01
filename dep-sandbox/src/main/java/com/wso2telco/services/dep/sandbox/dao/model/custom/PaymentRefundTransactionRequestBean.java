/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.services.dep.sandbox.dao.model.custom;


public class PaymentRefundTransactionRequestBean {

	private AmountTransaction amountTransaction;

	public AmountTransaction getAmountTransaction() {
		return amountTransaction;
	}

	public void setAmountTransaction(AmountTransaction amountTransaction) {
		this.amountTransaction = amountTransaction;
	}


	public static class AmountTransaction {

		private String clientCorrelator;

		private String endUserId;
		
		private String originalServerReferenceCode;

		private ChargePaymentAmount paymentAmount;

		private String referenceCode;

		private String transactionOperationStatus;

		private String callbackData;

		private String mandateId;

		private String notificationFormat;

		private String notifyURL;

		private String productID;

		private String serviceID;

		public String getClientCorrelator() {
			return clientCorrelator;
		}

		public void setClientCorrelator(String clientCorrelator) {
			this.clientCorrelator = clientCorrelator;
		}

		public String getEndUserId() {
			return endUserId;
		}

		public void setEndUserId(String endUserId) {
			this.endUserId = endUserId;
		}

		public String getOriginalServerReferenceCode() {
			return originalServerReferenceCode;
		}

		public void setOriginalServerReferenceCode(String originalServerReferenceCode) {
			this.originalServerReferenceCode = originalServerReferenceCode;
		}

		public String getReferenceCode() {
			return referenceCode;
		}

		public void setReferenceCode(String referenceCode) {
			this.referenceCode = referenceCode;
		}

		public String getTransactionOperationStatus() {
			return transactionOperationStatus;
		}

		public void setTransactionOperationStatus(String transactionOperationStatus) {
			this.transactionOperationStatus = transactionOperationStatus;
		}

		public ChargePaymentAmount getPaymentAmount() {
			return paymentAmount;
		}

		public void setPaymentAmount(ChargePaymentAmount paymentAmount) {
			this.paymentAmount = paymentAmount;
		}

		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Client Correlator : " + getClientCorrelator());
			builder.append("EndUserId : " + getEndUserId());
			builder.append("Reference Code : " + getReferenceCode());

			if (getPaymentAmount() != null) {
				builder.append(" " + getPaymentAmount().toString());
			}

			return builder.toString();
		}

		public String getCallbackData() {
			return callbackData;
		}

		public void setCallbackData(String callbackData) {
			this.callbackData = callbackData;
		}

		public String getMandateId() {
			return mandateId;
		}

		public void setMandateId(String mandateId) {
			this.mandateId = mandateId;
		}

		public String getNotificationFormat() {
			return notificationFormat;
		}

		public void setNotificationFormat(String notificationFormat) {
			this.notificationFormat = notificationFormat;
		}

		public String getNotifyURL() {
			return notifyURL;
		}

		public void setNotifyURL(String notifyURL) {
			this.notifyURL = notifyURL;
		}

		public String getProductID() {
			return productID;
		}

		public void setProductID(String productID) {
			this.productID = productID;
		}

		public String getServiceID() {
			return serviceID;
		}

		public void setServiceID(String serviceID) {
			this.serviceID = serviceID;
		}
	}
}
