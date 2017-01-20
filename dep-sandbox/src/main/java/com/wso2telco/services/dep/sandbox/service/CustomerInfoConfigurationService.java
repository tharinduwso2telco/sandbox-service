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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import com.wso2telco.services.dep.sandbox.dao.model.custom.AttributeRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.GetProfileConfigRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.GetAttributeConfigRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.GetAttributeConfigRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.GetProfileConfigRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisioningServicesRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RetrieveServiceUserRequestDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceDetail;
import com.wso2telco.services.dep.sandbox.exception.SandboxException.SandboxErrorType;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.RequestType;

@Path("/customerinfo/{v1}")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
@Produces({ MediaType.APPLICATION_JSON })
@Api(value = "/customerinfo/{v1}/config", description = "Rest Services for CustomerInfo API related Configurations")
public class CustomerInfoConfigurationService {

    Log LOG = LogFactory.getLog(CustomerInfoConfigurationService.class);

    @PUT
    @Path("/customer/profile")
    @ApiOperation(value = "addProfile", notes = "Add new Profiles for user", response = Response.class)
    @ApiImplicitParams({ @ApiImplicitParam(name = "sandbox", value = "username", required = true, dataType = "string", paramType = "header") })
    public Response addProfile(GetProfileConfigRequestBean requestbody,
	    @Context HttpServletRequest request) {

	GetProfileConfigRequestWrapperDTO requestDTO = new GetProfileConfigRequestWrapperDTO();
	requestDTO.setRequestObject(requestbody);
	requestDTO.setHttpRequest(request);
	requestDTO.setRequestType(RequestType.CUSTOMERINFOCONFIG);

	RequestHandleable<RequestDTO> handler = RequestBuilderFactory
		.getInstance(requestDTO);
	Returnable returnable = null;

	try {
	    returnable = handler.execute(requestDTO);
	    Response response = Response.status(returnable.getHttpStatus())
		    .entity(returnable.getResponse()).build();
	    return response;
	} catch (Exception ex) {
	    LOG.error(
		    "###CUSTOMERINFOCONFIG### Error in CustomerInfo Configuration Profile Service",
		    ex);
	    Response response = Response
		    .status(Status.BAD_REQUEST)
		    .entity(SandboxErrorType.SERVICE_ERROR.getCode() + " "
			    + SandboxErrorType.SERVICE_ERROR.getMessage())
		    .build();
	    return response;
	}
    }

    @PUT
    @Path("/customer/attribute")
    @ApiOperation(value = "addAttribute", notes = "Add new Attributes for user", response = Response.class)
    @ApiImplicitParams({ @ApiImplicitParam(name = "sandbox", value = "username", required = true, dataType = "string", paramType = "header") })
    public Response addAttribute(GetAttributeConfigRequestBean requestbody,
	    @Context HttpServletRequest request) {

	GetAttributeConfigRequestWrapperDTO requestDTO = new GetAttributeConfigRequestWrapperDTO();
	requestDTO.setRequestObject(requestbody);
	requestDTO.setHttpRequest(request);
	requestDTO.setRequestType(RequestType.CUSTOMERINFOCONFIG);

	RequestHandleable<RequestDTO> handler = RequestBuilderFactory
		.getInstance(requestDTO);
	Returnable returnable = null;

	try {
	    returnable = handler.execute(requestDTO);
	    Response response = Response.status(returnable.getHttpStatus())
		    .entity(returnable.getResponse()).build();
	    return response;
	} catch (Exception ex) {
	    LOG.error(
		    "###CUSTOMERINFOCONFIG### Error in CustomerInfo Configuration Attribute Service",
		    ex);
	    Response response = Response
		    .status(Status.BAD_REQUEST)
		    .entity(SandboxErrorType.SERVICE_ERROR.getCode() + " "
			    + SandboxErrorType.SERVICE_ERROR.getMessage())
		    .build();
	    return response;
	}
    }
}
