package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class CreditApplyResponseBean {
	
	private ServiceCreditResponse serviceCreditResponse;
	
	
	public ServiceCreditResponse getServiceCreditResponse() {
		return serviceCreditResponse;
	}


	public void setServiceCreditResponse(ServiceCreditResponse serviceCreditResponse) {
		this.serviceCreditResponse = serviceCreditResponse;
	}


	public static class ServiceCreditResponse {
		
		private String type;
		
		private double amount;
		
		private String clientCorrelator;
		
		private String reasonForCredit;
		
		private String merchantIdentification;
		
		private String status;
		
		private CallbackReference receiptResponse;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

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

		public String getReasonForCredit() {
			return reasonForCredit;
		}

		public void setReasonForCredit(String reasonForCredit) {
			this.reasonForCredit = reasonForCredit;
		}

		public String getMerchantIdentification() {
			return merchantIdentification;
		}

		public void setMerchantIdentification(String merchantIdentification) {
			this.merchantIdentification = merchantIdentification;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public CallbackReference getReceiptResponse() {
			return receiptResponse;
		}

		public void setReceiptResponse(CallbackReference receiptResponse) {
			this.receiptResponse = receiptResponse;
		}
		
		
	}

}