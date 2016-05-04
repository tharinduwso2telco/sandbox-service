/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.services.dep.sandbox.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.wso2telco.services.dep.sandbox.dao.model.custom.LocationRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;

import io.netty.handler.codec.http.HttpRequest;

/**
 * This is the Microservice resource class. See
 * <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/
 * wso2/msf4j#getting-started</a> for the usage of annotations.
 *
 * @since 1.8.0-SNAPSHOT
 */
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SandboxService {
	@GET
	@Path("/location/{v1}/queries/location")
	public Response getLocation(@QueryParam("address") String address,
			@QueryParam("requestedAccuracy") String requestedAccuracy, @Context HttpRequest httpRequest) {
		System.out.println("GET invoked");
		LocationRequestWrapperDTO requestDTO = new LocationRequestWrapperDTO();
		requestDTO.setAddress(address);
		requestDTO.setRequestedAccuracy(requestedAccuracy);
		requestDTO.setHttpRequest(httpRequest);
		RequestHandleable handler = RequestBuilderFactory.getInstance(requestDTO);
		
		Returnable returnable=null;
		try {
			returnable = handler.execute(requestDTO);
			return Response.status(returnable.getStatus()).entity(returnable.getResponse()).build();
		} catch (Exception e) {
			return Response.status( Response.Status.BAD_REQUEST ).entity(returnable.getResponse()).build();
		}
		
		
		
	}
}
