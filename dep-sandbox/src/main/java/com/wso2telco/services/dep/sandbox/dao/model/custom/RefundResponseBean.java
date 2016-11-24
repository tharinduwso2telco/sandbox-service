package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class RefundResponseBean {
	
	private RefundResponse refundResponse;
	
	
	public RefundResponse getRefundResponse() {
		return refundResponse;
	}


	public void setRefundResponse(RefundResponse refundResponse) {
		this.refundResponse = refundResponse;
	}


	public static class RefundResponse {
		
		private double amount;
		
		private String serverTransactionReference;
		
		private String clientCorrelator;
		
		private String reasonForRefund;
		
		private String merchantIdentification;
		
		private CallbackReference receiptResponse;

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}

		public String getServerTransactionReference() {
			return serverTransactionReference;
		}

		public void setServerTransactionReference(String serverTransactionReference) {
			this.serverTransactionReference = serverTransactionReference;
		}

		public String getClientCorrelator() {
			return clientCorrelator;
		}

		public void setClientCorrelator(String clientCorrelator) {
			this.clientCorrelator = clientCorrelator;
		}

		public String getReasonForRefund() {
			return reasonForRefund;
		}

		public void setReasonForRefund(String reasonForRefund) {
			this.reasonForRefund = reasonForRefund;
		}

		public String getMerchantIdentification() {
			return merchantIdentification;
		}

		public void setMerchantIdentification(String merchantIdentification) {
			this.merchantIdentification = merchantIdentification;
		}

		public CallbackReference getReceiptResponse() {
			return receiptResponse;
		}

		public void setReceiptResponse(CallbackReference receiptResponse) {
			this.receiptResponse = receiptResponse;
		}
		
	
	}

}
