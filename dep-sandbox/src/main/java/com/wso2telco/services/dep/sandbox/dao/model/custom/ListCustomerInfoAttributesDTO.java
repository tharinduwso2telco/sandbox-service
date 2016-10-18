package com.wso2telco.services.dep.sandbox.dao.model.custom;


public class ListCustomerInfoAttributesDTO 
{
	/** The MSISDN **/
	private String msisdn;
	
	/** The IMSI **/
	private String imsi;
	
	/** The Schema **/
	private String basic;
	private String billing;
	private String account;
	private String identification;
	
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

	
	public String getBasic() {
		return basic;
	}
		

	public void setBasic(String string) {
		this.basic = string;
	}

	
	public String getBilling() {
		return billing;
	}
	

	public void setBilling(String billing) {
		this.billing = billing;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	
	public String getIdentification() {
		return identification;
	}


	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getResourceURL() {
		return resourceURL;
	}

	public void setResourceURL(String resourceURL) {
		this.resourceURL = resourceURL;
	}
}
