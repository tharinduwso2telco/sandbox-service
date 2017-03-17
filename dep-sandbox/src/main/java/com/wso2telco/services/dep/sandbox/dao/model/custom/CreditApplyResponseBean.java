package com.wso2telco.services.dep.sandbox.dao.model.custom;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CreditApplyResponseBean {
	
	private CreditApplyResponse creditApplyResponse;
	
	
	public CreditApplyResponse getCreditApplyResponse() {
		return creditApplyResponse;
	}


	public void setCreditApplyResponse(CreditApplyResponse creditApplyResponse) {
		this.creditApplyResponse = creditApplyResponse;
	}

	@JsonInclude(value=Include.NON_NULL)
	public static class CreditApplyResponse {
		
		private double amount;
		
		private String type;
		
		private String clientCorrelator;
		
		private String reasonForCredit;
		
		private String merchantIdentification;
		
		private String status;
		
		private CallbackReference receiptResponse;
		
		private String referenceCode;
		
		private String serverReferenceCode;

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}
		
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
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

		public String getReferenceCode() {
			return referenceCode;
		}

		public void setReferenceCode(String referenceCode) {
			this.referenceCode = referenceCode;
		}

		public String getServerReferenceCode() {
			return serverReferenceCode;
		}

		public void setServerReferenceCode(String serverReferenceCode) {
			this.serverReferenceCode = serverReferenceCode;
		}
	}
}