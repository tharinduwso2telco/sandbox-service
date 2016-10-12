package com.wso2telco.services.dep.sandbox.servicefactory.admin;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;

public class AdminRequestFactory {

    public static RequestHandleable getInstance(RequestDTO requestDTO) {
	final String ADD_USER_PATH = "register";
	
	if(requestDTO.getRequestPath().contains(ADD_USER_PATH) && requestDTO.isPost()){
	    return new AdminRequstHandler();
	}
	
	return null;
    }

}
