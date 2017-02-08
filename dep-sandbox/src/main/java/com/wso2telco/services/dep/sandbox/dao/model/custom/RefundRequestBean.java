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
