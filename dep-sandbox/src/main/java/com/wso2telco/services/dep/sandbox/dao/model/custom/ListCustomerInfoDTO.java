package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class ListCustomerInfoDTO 
{
	/** The MSISDN **/
	private String msisdn;
	
	/** The IMSI **/
	private String imsi;
	
	/** The Schema **/
	private StringBuilder schema;
	
	/** The resource url **/
	private String resourceURL;
	
	
	/**
	 * @return the msisdn
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * @param msisdn
	 *            to set
	 * 
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	/**
	 * @return the imsi
	 */
	public String getImsi() {
		return imsi;
	}

	/**
	 * @param imsi
	 *            to set
	 * 
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	/**
	 * 
	 * @return the schema
	 */
	public StringBuilder getSchema() {
		return schema;
	}

	/**
	 * 
	 * @param schema
	 *            to set
	 */
	public void setSchema(StringBuilder schema) {
		this.schema = schema;
	}
	

	/**
	 * @return the resourceURL
	 */
	public String getResourceURL() {
		return resourceURL;
	}

	/**
	 * @param resourceURL
	 *            to set
	 * 
	 */
	public void setResourceURL(String resourceURL) {
		this.resourceURL = resourceURL;
	}


}
