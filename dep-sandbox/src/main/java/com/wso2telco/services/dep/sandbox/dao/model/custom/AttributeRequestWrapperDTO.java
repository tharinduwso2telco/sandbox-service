package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class AttributeRequestWrapperDTO extends RequestDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 7071157967778389826L;

    private AttributeRequestBean attributeRequestBean;
    private String apiType;
    private String serviceType;

    public String getApiType() {
	return apiType;
    }

    public void setApiType(String apiType) {
	this.apiType = apiType;
    }

    public String getServiceType() {
	return serviceType;
    }

    public void setServiceType(String serviceType) {
	this.serviceType = serviceType;
    }

    public AttributeRequestBean getAttributeBean() {
	return attributeRequestBean;
    }

    public void setAttributeBean(AttributeRequestBean attribute) {
	this.attributeRequestBean = attribute;
    }
}
