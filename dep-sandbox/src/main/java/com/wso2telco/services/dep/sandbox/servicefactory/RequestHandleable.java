package com.wso2telco.services.dep.sandbox.servicefactory;

import com.wso2telco.services.dep.sandbox.dao.model.RequestDTO;

public interface RequestHandleable {

	public Returnable execute(final RequestDTO dto) throws Exception;
	
}
