/*******************************************************************************
 * Copyright (c) 2015-2017, WSO2.Telco Inc. (http://www.wso2telco.com)
 *
 * All Rights Reserved. WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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

public class RefundRequestBean {
	
	private RefundRequest refundRequest;
	
	
	public RefundRequest getRefundRequest() {
		return refundRequest;
	}


	public void setRefundRequest(RefundRequest refundRequest) {
		this.refundRequest = refundRequest;
	}


	public static class RefundRequest{
		

		private String clientCorrelator;

		private String msisdn;
		
		private String originalServerReferenceCode;

		private String reasonForRefund;

		private double refundAmount;

		private PaymentAmountWithTax paymentAmount;

		private String referenceCode;

		public String getClientCorrelator() {
			return clientCorrelator;
		}

		public void setClientCorrelator(String clientCorrelator) {
			this.clientCorrelator = clientCorrelator;
		}


		public String getMsisdn() {
			return msisdn;
		}

		public void setMsisdn(String msisdn) {
			this.msisdn = msisdn;
		}

		public String getOriginalServerReferenceCode() {
			return originalServerReferenceCode;
		}

		public void setOriginalServerReferenceCode(String originalServerReferenceCode) {
			this.originalServerReferenceCode = originalServerReferenceCode;
		}

		public String getReasonForRefund() {
			return reasonForRefund;
		}

		public void setReasonForRefund(String reasonForRefund) {
			this.reasonForRefund = reasonForRefund;
		}

		public double getRefundAmount() {
			return refundAmount;
		}

		public void setRefundAmount(double refundAmount) {
			this.refundAmount = refundAmount;
		}


		public PaymentAmountWithTax getPaymentAmount() {
			return paymentAmount;
		}

		public void setPaymentAmount(PaymentAmountWithTax paymentAmount) {
			this.paymentAmount = paymentAmount;
		}




		public String getReferenceCode() {
			return referenceCode;
		}

		public void setReferenceCode(String referenceCode) {
			this.referenceCode = referenceCode;
		}


		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Client Correlator : " + getClientCorrelator());
			builder.append("msisdn : " + getMsisdn());
			builder.append("OriginalServerReferenceCode : " +getOriginalServerReferenceCode());
			builder.append("reasonForRefund : " + getReasonForRefund());
			builder.append("refundAmount : " + getPaymentAmount());

			if (getPaymentAmount() != null) {
				builder.append(" " + getPaymentAmount().toString());
			}

			return builder.toString();
		}

		
	}

}
