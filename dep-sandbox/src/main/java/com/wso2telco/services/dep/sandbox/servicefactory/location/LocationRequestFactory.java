package com.wso2telco.services.dep.sandbox.servicefactory.location;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;

public class LocationRequestFactory {
	
	private static LocationRequestHandler instance =null;
	static {
		
		synchronized (instance) {
			instance = new LocationRequestHandler();
		}
	}
	// TODO: based on the json body need to implement request handle
	@SuppressWarnings("rawtypes")
	public static RequestHandleable getInstance(final RequestDTO requestDTO) {

		return instance;
	}
}
