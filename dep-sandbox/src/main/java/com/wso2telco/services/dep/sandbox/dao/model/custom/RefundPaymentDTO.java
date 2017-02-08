package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class RefundPaymentDTO {
	
	private PaymentRefundTransactionResponseBean amountTransaction;

	public PaymentRefundTransactionResponseBean getAmountTransaction() {
		return amountTransaction;
	}

	public void setAmountTransaction(PaymentRefundTransactionResponseBean amountTransaction) {
		this.amountTransaction = amountTransaction;
	}
}
