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
import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisioningServicesRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RetrieveServiceUserRequestDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceDetail;
import com.wso2telco.services.dep.sandbox.exception.SandboxException.SandboxErrorType;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.RequestType;

@Path("/provisioning/{v1}/config")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
@Produces({ MediaType.APPLICATION_JSON })
@Api(value = "/provisioning/{v1}/config", description = "Rest Services for Provisionning API related Configurations")
public class ProvisionConfigurationService {

    Log LOG = LogFactory.getLog(ProvisionConfigurationService.class);

    @POST
    @Path("/{msisdn}/service/{serviceCode}")
    @ApiOperation(value = "addServiceForMsisdn", notes = "add user defined services for msisdn", response = Response.class)
    @ApiImplicitParams({ @ApiImplicitParam(name = "sandbox", value = "username", required = true, dataType = "string", paramType = "header") })
    public Response addServiceMsisdn(
	    @ApiParam(value = "msisdn", required = true) @PathParam("msisdn") String msisdn,
	    @ApiParam(value = "serviceCode", required = true) @PathParam("serviceCode") String serviceCode,
	    @Context HttpServletRequest request) {

	AddServicesMsisdnRequestWrapperDTO requestDTO = new AddServicesMsisdnRequestWrapperDTO();
	requestDTO.setHttpRequest(request);
	requestDTO.setMsisdn(msisdn);
	requestDTO.setServiceCode(serviceCode);
	requestDTO.setRequestType(RequestType.PROVISIONINGCONFIG);

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

    @POST
    @Path("/service")
    @ApiOperation(value = "addServiceForUser", notes = "Define New Services For User", response = Response.class)
    @ApiImplicitParams({ @ApiImplicitParam(name = "sandbox", value = "username", required = true, dataType = "string", paramType = "header") })
    public Response addService(@Context HttpServletRequest httpRequest,
	    ServiceDetail serviceDetail) {
	LOG.debug("/service invorked :" + serviceDetail.getServiceCode()
		+ serviceDetail.getServiceType()
		+ serviceDetail.getDescription()
		+ serviceDetail.getServiceCharge());

	ProvisioningServicesRequestWrapperDTO requestDTO = new ProvisioningServicesRequestWrapperDTO();
	requestDTO.setHttpRequest(httpRequest);
	requestDTO.setRequestType(RequestType.PROVISIONINGCONFIG);
	requestDTO.setServiceDetail(serviceDetail);

	RequestHandleable handler = RequestBuilderFactory
		.getInstance(requestDTO);
	Returnable returnable = null;

	ProvisioningDAO provisioningDAO = null;

	try {

	    returnable = handler.execute(requestDTO);

	    Response response = Response.status(returnable.getHttpStatus())
		    .entity(returnable.getResponse()).build();
	    LOG.debug("QUERY NEW SERVICE RESPONSE : " + response);
	    return response;
	} catch (Exception ex) {
	    LOG.error("QUERY NEW SERVICE ERROR : ", ex);
	    return Response
		    .status(Response.Status.BAD_REQUEST)
		    .entity(SandboxErrorType.INVALID_INPUT_VALUE.getCode()
			    + " "
			    + SandboxErrorType.INVALID_INPUT_VALUE.getMessage())
		    .build();
	}

    }

    @GET
    @Path("/service")
    @ApiOperation(value = "getUserServices", notes = "Retrieves User defined Services", response = Response.class)
    @ApiImplicitParams({ @ApiImplicitParam(name = "sandbox", value = "username", required = true, dataType = "string", paramType = "header") })
    public Response getApplicableServices(@Context HttpServletRequest httpRequest) {
	LOG.debug("/service invoked :" );
	RetrieveServiceUserRequestDTO requestDTO = new RetrieveServiceUserRequestDTO();
	requestDTO.setHttpRequest(httpRequest);
	requestDTO.setRequestType(RequestType.PROVISIONINGCONFIG);

	RequestHandleable handler = RequestBuilderFactory
		.getInstance(requestDTO);
	Returnable returnable = null;

	try {
	    returnable = handler.execute(requestDTO);
	    Response response = Response.status(returnable.getHttpStatus())
		    .entity(returnable.getResponse()).build();
	    LOG.debug("QUERY AVAILVABLE SERVICE FOR USER RESPONSE : " + response);
	    return response;
	} catch (Exception ex) {
	    LOG.error("QUERY AVAILVABLE SERVICE FOR USER RESPONSE : ", ex);
	    return Response
		    .status(Response.Status.BAD_REQUEST)
		    .entity(SandboxErrorType.SERVICE_ERROR.getCode() + " "
			    + SandboxErrorType.SERVICE_ERROR.getMessage())
		    .build();
	}

    }

}
