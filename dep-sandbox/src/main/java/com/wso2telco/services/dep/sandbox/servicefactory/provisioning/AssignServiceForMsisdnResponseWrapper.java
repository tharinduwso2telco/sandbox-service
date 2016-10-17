package com.wso2telco.services.dep.sandbox.servicefactory.provisioning;

import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class AssignServiceForMsisdnResponseWrapper extends
	AbstractReturnWrapperDTO {
    private String status;

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    @Override
    public Object getResponse() {

	if (getRequestError() != null) {
	    return getRequestError();
	}
	return getStatus();
    }

}
