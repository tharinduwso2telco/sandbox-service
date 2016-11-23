package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class RefundTransactionDTO {
	
	private RefundTransactionResponseBean refundPayment;

	public RefundTransactionResponseBean getRefundTransaction() {
		return refundPayment;
	}

	public void setRefundTransaction(RefundTransactionResponseBean refundPayment) {
		this.refundPayment = refundPayment;
	}
	

}
