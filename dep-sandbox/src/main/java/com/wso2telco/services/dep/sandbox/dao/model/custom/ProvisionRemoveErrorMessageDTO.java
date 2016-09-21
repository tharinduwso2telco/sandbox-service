package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class ProvisionRemoveErrorMessageDTO {
	
	private String responseMessage;
	private String responseCode;
	private int messageCategoryid;

	/**
	 * 
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * 
	 * @param responseCode
	 *            to set
	 */
	public void setResponseCode(String code) {
		this.responseCode = code;
	}

	/**
	 * 
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return responseMessage;
	}

	/**
	 * 
	 * @param responseMessage
	 *            to set
	 */
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	/**
	 * 
	 * @return the messageCategory
	 */
	public int getMessageCategory() {
		return messageCategoryid;
	}

	/**
	 * 
	 * @param messageCategory to set
	 *            
	 */
	public void setMessageCategory(int messageCategoryid) {
		this.messageCategoryid = messageCategoryid;
	}

}
