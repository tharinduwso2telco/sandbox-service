package com.wso2telco.services.dep.servicefactory;

import com.wso2telco.services.dep.model.RequestDTO;

public interface RequestHandleable {

	public Returnable execute(final RequestDTO dto) throws Exception;
	
}
