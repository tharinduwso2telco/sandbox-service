package com.wso2telco.services.dep.sandbox.servicefactory.location;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;

public class LocationRequestFactory {
	
	private static LocationRequestHandler instance =null;
	// TODO: based on the json body need to implement request handle
	@SuppressWarnings("rawtypes")
	public static synchronized RequestHandleable getInstance(final RequestDTO requestDTO) {
		if (instance==null) {
				instance = new LocationRequestHandler();
			}
		return instance;
	}
}
