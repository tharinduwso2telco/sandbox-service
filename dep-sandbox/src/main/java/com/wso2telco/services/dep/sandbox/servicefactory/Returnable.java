package com.wso2telco.services.dep.sandbox.servicefactory;

import javax.ws.rs.core.Response;

public interface Returnable {

	public Object getResponse();
	public Response.Status getStatus();
}
