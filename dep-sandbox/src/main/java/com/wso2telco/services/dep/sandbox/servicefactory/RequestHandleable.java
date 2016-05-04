package com.wso2telco.services.dep.sandbox.servicefactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;

public interface RequestHandleable {

	public Returnable execute(final RequestDTO requestDTO) throws Exception;
}
