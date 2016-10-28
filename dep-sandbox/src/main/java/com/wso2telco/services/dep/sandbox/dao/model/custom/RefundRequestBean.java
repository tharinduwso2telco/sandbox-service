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
		
		private double amount;
		
		private String clientCorrelator;
		
		private String reasonForRefund;
		
		private String merchantIdentification;
		
		private String serverTransactionReference;
		
		private CallbackReference receiptRequest;

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
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

		public String getServerTransactionReference() {
			return serverTransactionReference;
		}

		public void setServerTransactionReference(String serverTransactionReference) {
			this.serverTransactionReference = serverTransactionReference;
		}

		public CallbackReference getReceiptRequest() {
			return receiptRequest;
		}

		public void setReceiptRequest(CallbackReference receiptRequest) {
			this.receiptRequest = receiptRequest;
		}
		
	}

}
