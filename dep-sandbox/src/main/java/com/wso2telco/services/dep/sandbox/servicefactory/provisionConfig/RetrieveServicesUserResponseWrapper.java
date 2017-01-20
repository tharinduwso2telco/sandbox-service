package com.wso2telco.services.dep.sandbox.servicefactory.provisionConfig;

import com.wso2telco.services.dep.sandbox.dao.model.custom.ErrorResponseDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.UserServiceList;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class RetrieveServicesUserResponseWrapper extends AbstractReturnWrapperDTO{
    
    private UserServiceList serviceListDTO;

	@Override
	public Object getResponse() {
		
		if(getRequestError()==null)		
			return serviceListDTO;
		else{
			ErrorResponseDTO response= new ErrorResponseDTO(getRequestError());
			return response;
		}
	}

	/**
	 * @return the serviceListDTO
	 */
	public UserServiceList getServiceListDTO() {
		return serviceListDTO;
	}

	/**
	 * @param serviceListDTO
	 *            to set
	 */
	public void setServiceListDTO(UserServiceList serviceListDTO) {
		this.serviceListDTO = serviceListDTO;
	}
	

}

