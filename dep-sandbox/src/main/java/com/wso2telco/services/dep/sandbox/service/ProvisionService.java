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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.AddServicesMsisdnRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ListProvisionedRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisionRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisioningServicesRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.QueryProvisioningServicesRequestWrapper;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RemoveProvisionRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RemoveProvisionedRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceDetail;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceProvisionRequestWrapper;
import com.wso2telco.services.dep.sandbox.exception.SandboxException.SandboxErrorType;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.RequestType;

@Path("/provisioning/{v1}")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@Api(value = "/provisioning/{v1}", description = "Rest Service for Provisionning API")
public class ProvisionService {

	Log LOG = LogFactory.getLog(ProvisionService.class);

	@GET
	@Path("/{msisdn}/list/applicable")
	@ApiOperation(value = "getApplicableServices", notes = "getApplicableServices", response = Response.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "sandbox", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public Response getApplicableServices(
			@ApiParam(value = "msisdn", required = true) @PathParam("msisdn") String msisdn,
			@ApiParam(value = "mcc", required = false) @QueryParam("mcc") String mcc,
			@ApiParam(value = "mnc", required = false) @QueryParam("mnc") String mnc,
			@ApiParam(value = "onBehalfOf", required = false) @QueryParam("onBehalfOf") String onBehalfOf,
			@ApiParam(value = "purchaseCategoryCode", required = false) @QueryParam("purchaseCategoryCode") String purchaseCategoryCode,
			@ApiParam(value = "requestIdentifier", required = true) @QueryParam("requestIdentifier") String requestIdentifier,
			@ApiParam(value = "offset", required = false) @QueryParam("offset") String offSet,
			@ApiParam(value = "limit", required = false) @QueryParam("limit") String limit,
			@Context HttpServletRequest request) {
		LOG.debug("/{msisdn}/list/applicable invorked :" + msisdn + offSet
				+ limit);
		QueryProvisioningServicesRequestWrapper requestDTO = new QueryProvisioningServicesRequestWrapper();
		requestDTO.setHttpRequest(request);
		requestDTO.setOffSet(offSet);
		requestDTO.setLimit(limit);
		requestDTO.setMsisdn(msisdn);
		requestDTO.setRequestType(RequestType.PROVISIONING);
		requestDTO.setMcc(mcc);
		requestDTO.setMnc(mnc);
		requestDTO.setOnBehalfOf(onBehalfOf);
		requestDTO.setPurchaseCategoryCode(purchaseCategoryCode);
		requestDTO.setRequestIdentifier(requestIdentifier);

		RequestHandleable handler = RequestBuilderFactory
				.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus())
					.entity(returnable.getResponse()).build();
			LOG.debug("QUERY APPLICABLE SERVICE RESPONSE : " + response);
			return response;
		} catch (Exception ex) {
			LOG.error("QUERY SERVICE ERROR : ", ex);
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity(SandboxErrorType.SERVICE_ERROR.getCode() + " "
							+ SandboxErrorType.SERVICE_ERROR.getMessage())
					.build();
		}

	}

	@GET
	@Path("/{msisdn}/list/active")
	@ApiOperation(value = "getActiveProvisionedServices", notes = "getActiveProvisionedServices", response = Response.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "sandbox", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public Response getActiveProvisionedServices(
			@ApiParam(value = "msisdn", required = true) @PathParam("msisdn") String msisdn,
			@ApiParam(value = "mcc", required = false) @QueryParam("mcc") String mcc,
			@ApiParam(value = "mnc", required = false) @QueryParam("mnc") String mnc,
			@ApiParam(value = "onBehalfOf", required = false) @QueryParam("onBehalfOf") String onBehalfOf,
			@ApiParam(value = "purchaseCategoryCode", required = false) @QueryParam("purchaseCategoryCode") String purchaseCategoryCode,
			@ApiParam(value = "requestIdentifier", required = true) @QueryParam("requestIdentifier") String requestIdentifier,
			@ApiParam(value = "offset", required = false) @QueryParam("offset") String offSet,
			@ApiParam(value = "limit", required = false) @QueryParam("limit") String limit,
			@Context HttpServletRequest request) {
		LOG.debug("/{msisdn}/list/active invoked :" + msisdn + offSet + limit);
		ListProvisionedRequestWrapperDTO requestDTO = new ListProvisionedRequestWrapperDTO();
		requestDTO.setHttpRequest(request);
		requestDTO.setOffSet(offSet);
		requestDTO.setLimit(limit);
		requestDTO.setMsisdn(msisdn);
		requestDTO.setRequestType(RequestType.PROVISIONING);
		requestDTO.setMcc(mcc);
		requestDTO.setMnc(mnc);
		requestDTO.setOnBehalfOf(onBehalfOf);
		requestDTO.setPurchaseCategoryCode(purchaseCategoryCode);
		requestDTO.setRequestIdentifier(requestIdentifier);
		RequestHandleable handler = RequestBuilderFactory
				.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus())
					.entity(returnable.getResponse()).build();
			LOG.debug("LIST ACTIVE PROVISIONED SERVICES RESPONSE : " + response);
			return response;
		} catch (Exception ex) {
			LOG.error("LIST ACTIVE PROVISIONED SERVICESE ERROR : ", ex);
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity(SandboxErrorType.SERVICE_ERROR.getCode() + " "
							+ SandboxErrorType.SERVICE_ERROR.getMessage())
					.build();
		}

	}

	@POST
	@Path("/{msisdn}/remove")
	@ApiOperation(value = "removeProvisionedServices", notes = "removeProvisionedServices", response = Response.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "sandbox", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public Response removeProvisionedServices(
			@ApiParam(value = "msisdn", required = true) @PathParam("msisdn") String msisdn,
			@ApiParam(value = "mcc", required = false) @QueryParam("mcc") String mcc,
			@ApiParam(value = "mnc", required = false) @QueryParam("mnc") String mnc,
			@Context HttpServletRequest httpRequest,
			RemoveProvisionRequestBean removeProvisionRequestBean) {

		LOG.debug("/{msisdn}/remove invoked :" + msisdn);
		if (removeProvisionRequestBean != null) {
			LOG.debug(removeProvisionRequestBean);
		}
		RemoveProvisionedRequestWrapperDTO requestDTO = new RemoveProvisionedRequestWrapperDTO();
		requestDTO.setHttpRequest(httpRequest);
		requestDTO.setMsisdn(msisdn);
		requestDTO.setRemoveProvisionRequestBean(removeProvisionRequestBean);
		requestDTO.setRequestType(RequestType.PROVISIONING);
		requestDTO.setMcc(mcc);
		requestDTO.setMnc(mnc);

		RequestHandleable handler = RequestBuilderFactory
				.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus())
					.entity(returnable.getResponse()).build();
			LOG.debug("REMOVE PROVISIONED SERVICES RESPONSE : " + response);
			return response;
		} catch (Exception ex) {
			LOG.error("REMOVE PROVISIONED SERVICESE ERROR : ", ex);
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity(SandboxErrorType.SERVICE_ERROR.getCode() + " "
							+ SandboxErrorType.SERVICE_ERROR.getMessage())
					.build();
		}
	}

	@POST
	@Path("/{msisdn}")
	@ApiOperation(value = "provisionForRequestedService", notes = "provision requested service", response = Response.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "sandbox", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public Response provisionForRequestedService(
			@ApiParam(value = "msisdn", required = true) @PathParam("msisdn") String msisdn,
			@ApiParam(value = "mcc", required = false) @QueryParam("mcc") String mcc,
			@ApiParam(value = "mnc", required = false) @QueryParam("mnc") String mnc,
			ProvisionRequestBean provisionRequest,
			@Context HttpServletRequest request) {
		LOG.debug("###PROVISION### /{msisdn} invoked : msisdn - " + msisdn);
		if (provisionRequest != null) {
			LOG.debug(provisionRequest);
		}
		ServiceProvisionRequestWrapper requestDTO = new ServiceProvisionRequestWrapper();
		requestDTO.setHttpRequest(request);
		requestDTO.setMsisdn(msisdn);
		requestDTO.setProvisionRequestBean(provisionRequest);
		requestDTO.setRequestType(RequestType.PROVISIONING);
		requestDTO.setMcc(mcc);
		requestDTO.setMnc(mnc);
		
		RequestHandleable<RequestDTO> handler = RequestBuilderFactory
				.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus())
					.entity(returnable.getResponse()).build();
			return response;
		} catch (Exception ex) {
			LOG.error("###PROVISION### Error in Provision Service", ex);
			Response response = Response
					.status(Status.BAD_REQUEST)
					.entity(SandboxErrorType.SERVICE_ERROR.getCode() + " "
							+ SandboxErrorType.SERVICE_ERROR.getMessage())
					.build();
			return response;
		}
	}
}
