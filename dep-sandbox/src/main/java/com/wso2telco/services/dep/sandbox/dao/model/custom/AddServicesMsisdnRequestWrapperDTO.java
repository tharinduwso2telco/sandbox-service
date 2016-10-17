package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class AddServicesMsisdnRequestWrapperDTO extends RequestDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 4572267977614995390L;

    private String msisdn;
    private String serviceCode;

    public String getMsisdn() {
	return msisdn;
    }

    public void setMsisdn(String msisdn) {
	this.msisdn = msisdn;
    }

    public String getServiceCode() {
	return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
	this.serviceCode = serviceCode;
    }

}
