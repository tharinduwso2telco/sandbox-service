package com.wso2telco.services.dep.sandbox.servicefactory.provisionConfig;

import java.util.ArrayList;
import java.util.List;

import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisionErrorResponseDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceDetail;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceInfoListProvisionedDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceListProvisionedDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.UserServiceList;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class RetrieveServicesUserResponseWrapper extends AbstractReturnWrapperDTO{
    
    private UserServiceList serviceListDTO;

	@Override
	public Object getResponse() {
		
		if(getRequestError()==null)		
			return serviceListDTO;
		else{
			ProvisionErrorResponseDTO response= new ProvisionErrorResponseDTO(getRequestError());
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

