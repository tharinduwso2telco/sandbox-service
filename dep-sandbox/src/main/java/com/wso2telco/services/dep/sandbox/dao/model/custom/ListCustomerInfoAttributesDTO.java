package com.wso2telco.services.dep.sandbox.dao.model.custom;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(value=Include.NON_NULL)
public class ListCustomerInfoAttributesDTO 
{
	/** The MSISDN **/
	private String msisdn;
	
	/** The IMSI **/
	private String imsi;
	private String onBehalfOf;
	private String purchaseCategoryCode;
	private String requestIdentifier;
	private String responseIdentifier;
	
	/** The Schema **/
	private JsonNode basic;
	private JsonNode billing;
	private JsonNode account;
	private JsonNode identification;
	
	/** The resource url **/
	private String resourceURL;
	
	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}


	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	
	public JsonNode getBasic() {
		return basic;
	}
		

	public void setBasic(JsonNode string) {
		this.basic = string;
	}

	
	public JsonNode getBilling() {
		return billing;
	}
	

	public void setBilling(JsonNode billing) {
		this.billing = billing;
	}

	public JsonNode getAccount() {
		return account;
	}

	public void setAccount(JsonNode account) {
		this.account = account;
	}
	
	
	public JsonNode getIdentification() {
		return identification;
	}


	public void setIdentification(JsonNode identification) {
		this.identification = identification;
	}

	public String getResourceURL() {
		return resourceURL;
	}

	public void setResourceURL(String resourceURL) {
		this.resourceURL = resourceURL;
	}

	public String getOnBehalfOf() {
		return onBehalfOf;
	}

	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}

	public String getPurchaseCategoryCode() {
		return purchaseCategoryCode;
	}

	public void setPurchaseCategoryCode(String purchaseCategoryCode) {
		this.purchaseCategoryCode = purchaseCategoryCode;
	}

	public String getRequestIdentifier() {
		return requestIdentifier;
	}

	public void setRequestIdentifier(String requestIdentifier) {
		this.requestIdentifier = requestIdentifier;
	}

	public String getResponseIdentifier() {
		return responseIdentifier;
	}

	public void setResponseIdentifier(String responseIdentifier) {
		this.responseIdentifier = responseIdentifier;
	}
	
	
}
