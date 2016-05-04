package com.wso2telco.services.dep.sandbox.servicefactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;

 public abstract class AbstractRequestHandler<E2 extends RequestDTO> implements RequestHandleable<RequestDTO> {
	public final Returnable execute(final RequestDTO requestDTO) throws Exception{
		
		return process((E2)requestDTO );
		
	}

	protected abstract Returnable process(final E2 extendedRequestDTO)throws Exception;
}
