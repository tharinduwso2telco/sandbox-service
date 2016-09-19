/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.services.dep.sandbox.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ListProvisionedRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.QueryProvisioningServicesRequestWrapper;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.RequestType;


@Path("/{v1}/provision")
//@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@Api(value = "/{v1}/provision", description = "Rest Service for provisionning API")
public class ProvisionService {

	Log LOG = LogFactory.getLog(ProvisionService.class);

	@GET
	@Path("/{msisdn}/list/applicable")
	 @ApiOperation(value = "getApplicableServices", notes = "getApplicableServices", response = Response.class)
	public Response getApplicableServices( @ApiParam(value = "msisdn", required = true) @PathParam("msisdn") String msisdn, 
			@ApiParam(value = "offset", required = true) @QueryParam("offset") String offSet,
			@ApiParam(value = "limit", required = true)  @QueryParam("limit") String limit, @Context HttpServletRequest request) {
		LOG.debug("/{msisdn}/list/applicable invorked :" + msisdn + offSet + limit);
		QueryProvisioningServicesRequestWrapper requestDTO = new QueryProvisioningServicesRequestWrapper();
		requestDTO.setHttpRequest(request);
		requestDTO.setOffSet(offSet);
		requestDTO.setLimit(limit);
		requestDTO.setMsisdn(msisdn);
		requestDTO.setRequestType(RequestType.PROVISIONING);

		RequestHandleable handler = RequestBuilderFactory.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
			LOG.debug("QUERY APPLICABLE SERVICE RESPONSE : " + response);
			return response;
		} catch (SandboxException ex) {
			LOG.error("QUERY SERVICE ERROR : " , ex);
			return Response.status(Response.Status.BAD_REQUEST).entity(ex.getErrorType().getCode() + " " + ex.getErrorType().getMessage()).build();
		} catch (Exception ex) {
			LOG.error("QUERY SERVICE ERROR : " , ex);
			return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
		}

	}
	
	@GET
	@Path("/{msisdn}/list/active")
	@ApiOperation(value = "getActiveProvisionedServices", notes = "getActiveProvisionedServices", response = Response.class)
	public Response getActiveProvisionedServices( @ApiParam(value = "msisdn", required = true) @PathParam("msisdn") String msisdn, 
			@ApiParam(value = "offset", required = true) @QueryParam("offset") String offSet,
			@ApiParam(value = "limit", required = true)  @QueryParam("limit") String limit, @Context HttpServletRequest request) {
		LOG.debug("/{msisdn}/list/active invoked :" + msisdn + offSet + limit);
		ListProvisionedRequestWrapperDTO requestDTO = new ListProvisionedRequestWrapperDTO();
		requestDTO.setHttpRequest(request);
		requestDTO.setOffSet(offSet);
		requestDTO.setLimit(limit);
		requestDTO.setMsisdn(msisdn);
		requestDTO.setRequestType(RequestType.PROVISIONING);

		RequestHandleable handler = RequestBuilderFactory.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
			LOG.debug("ACTIVE PROVISIONED SERVICES RESPONSE : " + response);
			return response;
		} catch (Exception ex) {
			LOG.error("ACTIVE PROVISIONED SERVICES ERROR : " , ex);
			return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
		}

	}

}
