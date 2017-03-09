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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.hub.outboundSMSMessageRequestBeanHub;
import com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.hub.sendMTSMSRequestWrapperDTOHub;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.RequestType;


/**
 * This is the Microservice resource class. See
 * <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/
 * wso2/msf4j#getting-started</a> for the usage of annotations.
 *
 * @since 1.8.0-SNAPSHOT
 */
@Path("/smsmessaging")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/{v1}/sms", description = "sms")
public class SandboxService {
	Log LOG = LogFactory.getLog(SandboxService.class);

/*	@GET
	@Path("/location/{v1}/queries/location")
	@ApiOperation(value = "get Location ", notes = "get Location ", response = Response.class)
	public Response getLocation(@QueryParam("address") String address,
			@QueryParam("requestedAccuracy") String requestedAccuracy, @Context HttpServletRequest httpRequest) {
		LOG.debug("/location/{v1}/queries/location invorked :" + address + requestedAccuracy + httpRequest);

		LocationRequestWrapperDTO requestDTO = new LocationRequestWrapperDTO();
		requestDTO.setAddress(address);
		requestDTO.setRequestedAccuracy(requestedAccuracy);
		requestDTO.setHttpRequest(httpRequest);
		requestDTO.setRequestType(RequestType.LOCATION);
		RequestHandleable handler = RequestBuilderFactory.getInstance(requestDTO);

		Returnable returnable = null;
		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
			LOG.debug("Response :" + response);
			return response;
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(returnable.getResponse()).build();
		}
	}*/

	@POST
	@Path("/{apiVersion}/outbound/{shortCode}/requests")
	@ApiOperation(value = "sms", notes = "sms ", response = Response.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sandbox", value = "Authorization token",
					required = true, dataType = "string", paramType = "header")
	})
	public Response handleSendMTSMSRequest(@Context HttpServletRequest httpRequest, @PathParam("apiVersion") String apiVersion,
			@PathParam("shortCode") String shortCode, outboundSMSMessageRequestBeanHub outboundSMSMessageRequestBean_hub) {

		sendMTSMSRequestWrapperDTOHub requestDTO = new sendMTSMSRequestWrapperDTOHub();
		requestDTO.setHttpRequest(httpRequest);
		requestDTO.setRequestType(RequestType.SMSMESSAGING);
		requestDTO.setApiVersion(apiVersion);
		requestDTO.setShortCode(shortCode);
		requestDTO.setOutboundSMSMessageRequestBean(outboundSMSMessageRequestBean_hub);

		RequestHandleable handler = RequestBuilderFactory.getInstance(requestDTO);

		Returnable returnable = null;

		try {

			returnable = handler.execute(requestDTO);
			return Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
		} catch (Exception e) {

			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).entity(returnable.getResponse()).build();
		}
	}
/*	@GET
	@Path("/smsmessaging/{apiVersion}/outbound/{shortCode}/requests/{mtSMSTransactionId}/deliveryInfos")
	public Response handleQuerySMSDeliveryStatusRequest(@Context HttpServletRequest httpRequest,
			@PathParam("apiVersion") String apiVersion, @PathParam("shortCode") String shortCode,
			@PathParam("mtSMSTransactionId") String mtSMSTransactionId) {

		QuerySMSDeliveryStatusRequestWrapperDTO requestDTO = new QuerySMSDeliveryStatusRequestWrapperDTO();
		requestDTO.setHttpRequest(httpRequest);
		requestDTO.setRequestType(RequestType.SMSMESSAGING);
		requestDTO.setApiVersion(apiVersion);
		requestDTO.setShortCode(shortCode);
		requestDTO.setMtSMSTransactionId(mtSMSTransactionId);

		RequestHandleable handler = RequestBuilderFactory.getInstance(requestDTO);

		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}*/
}
