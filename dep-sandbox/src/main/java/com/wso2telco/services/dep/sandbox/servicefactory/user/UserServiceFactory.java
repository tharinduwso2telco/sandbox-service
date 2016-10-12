package com.wso2telco.services.dep.sandbox.servicefactory.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.provisioning.ProvisioningServiceFactory;

public class UserServiceFactory {

    
    private static Log LOG = LogFactory.getLog(UserServiceFactory.class);

    public static RequestHandleable getInstance(RequestDTO requestDTO) {
	final String ADD_USER_PATH = "register";
	final String ADD_ATTRIBUTE_PATH = "addattribute";
	

	if(requestDTO.getRequestPath().contains(ADD_USER_PATH) && requestDTO.isPost()){
	    return new RegisterServiceHandler();
	}
	
	if(requestDTO.getRequestPath().contains(ADD_ATTRIBUTE_PATH) && requestDTO.isPost()){
	    return new AttributeServiceHandler();
	}
	
	return null;
    }

}
