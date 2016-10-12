package com.wso2telco.services.dep.sandbox.dao.model.custom;


public class AttributeRequestWrapperDTO extends RequestDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 7071157967778389826L;

    private AttributeRequestBean attributeRequestBean;
    
    public AttributeRequestBean getAttribute() {
	return attributeRequestBean;
    }

    public void setAttribute(AttributeRequestBean attribute) {
	this.attributeRequestBean = attribute;
    }
}
