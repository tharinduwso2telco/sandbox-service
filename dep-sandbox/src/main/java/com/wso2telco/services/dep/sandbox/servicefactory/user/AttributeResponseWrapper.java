package com.wso2telco.services.dep.sandbox.servicefactory.user;

import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisionErrorResponseDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class AttributeResponseWrapper extends AbstractReturnWrapperDTO {

    public String responseMessage;

    @Override
    public Object getResponse() {
	if(getRequestError()==null)		
		return responseMessage;
	else{
		ProvisionErrorResponseDTO response= new ProvisionErrorResponseDTO(getRequestError());
		return response;
    }
    }
    public void setResponseMessage(String message) {
  	this.responseMessage = message;
      }
}
