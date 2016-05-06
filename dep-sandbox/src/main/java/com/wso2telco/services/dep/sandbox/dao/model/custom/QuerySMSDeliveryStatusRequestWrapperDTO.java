package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class QuerySMSDeliveryStatusRequestWrapperDTO extends RequestDTO {

	private static final long serialVersionUID = 8161165602386932937L;
	private String shortCode;
	private String mtSMSTransactionId;
	
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public String getMtSMSTransactionId() {
		return mtSMSTransactionId;
	}
	public void setMtSMSTransactionId(String mtSMSTransactionId) {
		this.mtSMSTransactionId = mtSMSTransactionId;
	}
}
