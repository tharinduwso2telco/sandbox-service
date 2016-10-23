package com.wso2telco.services.dep.sandbox.servicefactory.user;

import org.json.JSONObject;

import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisionErrorResponseDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class AttributeResponseWrapper extends AbstractReturnWrapperDTO {

    public JSONObject responseMessage;

    @Override
    public Object getResponse() {
	if(getRequestError()==null)		
		return responseMessage;
	else{
		ProvisionErrorResponseDTO response= new ProvisionErrorResponseDTO(getRequestError());
		return response;
	}	
	
    }
    
    public void setResponseMessage(JSONObject message) {
  	this.responseMessage = message;
      }
}
