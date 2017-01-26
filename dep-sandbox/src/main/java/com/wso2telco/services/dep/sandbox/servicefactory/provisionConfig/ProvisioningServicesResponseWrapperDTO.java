package com.wso2telco.services.dep.sandbox.servicefactory.provisionConfig;

import com.wso2telco.services.dep.sandbox.dao.model.custom.CommonSuccessResponse;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ErrorResponseDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class ProvisioningServicesResponseWrapperDTO extends AbstractReturnWrapperDTO{
	
	private CommonSuccessResponse message;
	   
    public CommonSuccessResponse getMessage() {
		return message;
	}


	public void setMessage(CommonSuccessResponse message) {
		this.message = message;
	}


	@Override
    public Object getResponse() {
		
		if(getRequestError()==null)		
			return message;
		else{
			ErrorResponseDTO response= new ErrorResponseDTO(getRequestError());
			return response;
		}
	}

}
