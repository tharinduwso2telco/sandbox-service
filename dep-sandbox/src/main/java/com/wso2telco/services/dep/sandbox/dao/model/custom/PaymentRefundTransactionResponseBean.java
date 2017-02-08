package com.wso2telco.services.dep.sandbox.dao.model.custom;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class PaymentRefundTransactionResponseBean {


	private String clientCorrelator;

	private String endUserId;
	
	private String originalReferenceCode;
	
	private String originalServerReferenceCode;

	private ChargeRefundAmountResponse paymentAmount;

	private String referenceCode;
	
	private String serverReferenceCode;

	private String resourceURL;
	
	private String transactionOperationStatus;

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
	
	public String getOriginalReferenceCode() {
		return originalReferenceCode;
	}

	public void setOriginalReferenceCode(String originalReferenceCode) {
		this.originalReferenceCode = originalReferenceCode;
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

	public String getServerReferenceCode() {
		return serverReferenceCode;
	}

	public void setServerReferenceCode(String serverReferenceCode) {
		this.serverReferenceCode = serverReferenceCode;
	}

	public String getResourceURL() {
		return resourceURL;
	}

	public void setResourceURL(String resourceURL) {
		this.resourceURL = resourceURL;
	}

	public String getTransactionOperationStatus() {
		return transactionOperationStatus;
	}

	public void setTransactionOperationStatus(String transactionOperationStatus) {
		this.transactionOperationStatus = transactionOperationStatus;
	}

	public ChargeRefundAmountResponse getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(ChargeRefundAmountResponse paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
}
