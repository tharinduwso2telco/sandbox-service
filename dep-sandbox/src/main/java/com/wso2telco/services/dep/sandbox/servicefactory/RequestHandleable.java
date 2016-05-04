package com.wso2telco.services.dep.sandbox.servicefactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;

public interface RequestHandleable<E extends RequestDTO> {

	public Returnable execute(final E requestDTO) throws Exception;
}
