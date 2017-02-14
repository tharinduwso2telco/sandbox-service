/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licenses this file to you under  the Apache License, Version 2.0 (the "License");
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

import com.wordnik.swagger.annotations.*;
import com.wso2telco.services.dep.sandbox.dao.model.custom.*;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/user", description = "Rest Service for User Configurations in sandbox")
public class UserService {


    Log LOG = LogFactory.getLog(UserService.class);

    @POST
    @Path("/{userName}/register")
    @ApiOperation(value = "registerUser", notes = "Register New User", response = Response.class)
    public Response registerUser(@PathParam("userName") String userName,
	    @Context HttpServletRequest httpRequest) {
	RegisterUserServiceRequestWrapperDTO requestDTO = new RegisterUserServiceRequestWrapperDTO();
	requestDTO.setUserName(userName);
	requestDTO.setRequestType(RequestType.USER);
	requestDTO.setHttpRequest(httpRequest);
	RequestHandleable handler = RequestBuilderFactory
		.getInstance(requestDTO);
	Returnable returnable = null;

	try {
	    returnable = handler.execute(requestDTO);
	    Response response = Response.status(returnable.getHttpStatus())
		    .entity(returnable.getResponse()).build();
	    LOG.debug("REGISTER USER SERVICE RESPONSE : " + response);
	    return response;
	} catch (SandboxException ex) {
	    LOG.error(
		    "###USER### Error encountered in registerUser Service : ",
		    ex);
	    return Response
		    .status(Response.Status.BAD_REQUEST)
		    .entity(ex.getErrorType().getCode() + " "
			    + ex.getErrorType().getMessage()).build();
	} catch (Exception ex) {
	    LOG.error(
		    "###USER### Error encountered in registerUser Service : ",
		    ex);
	    return Response.status(Response.Status.BAD_REQUEST)
		    .entity(ex.getMessage()).build();
	}

    }

    @GET
    @Path("/{apiType}/{serviceType}/attribute")
    @ApiOperation(value = "getAttribute", notes = " Get User Defined Attributes ", response = Response.class)
    @ApiImplicitParams({ @ApiImplicitParam(name = "sandbox", value = "user name", required = true, dataType = "string", paramType = "header") })
    public Response getAttribute(
	    @ApiParam(value = "apiType", required = true) @PathParam("apiType") String apiType,
	    @ApiParam(value = "serviceType", required = true) @PathParam("serviceType") String serviceType,
	    @Context HttpServletRequest httpRequest) {
	AttributeRequestWrapperDTO requestDTO = new AttributeRequestWrapperDTO();
	requestDTO.setApiType(apiType);
	requestDTO.setServiceType(serviceType);
	requestDTO.setRequestType(RequestType.USER);
	requestDTO.setHttpRequest(httpRequest);
	RequestHandleable handler = RequestBuilderFactory
		.getInstance(requestDTO);
	Returnable returnable = null;

	try {
	    returnable = handler.execute(requestDTO);
	    Response response = Response.status(returnable.getHttpStatus())
		    .entity(returnable.getResponse()).build();
	    LOG.debug("GET ATTRIBUTE USER SERVICE RESPONSE : " + response);
	    return response;
	} catch (SandboxException ex) {
	    LOG.error(
		    "###USER### Error encountered in getAttribute Service : ",
		    ex);
	    return Response
		    .status(Response.Status.BAD_REQUEST)
		    .entity(ex.getErrorType().getCode() + " "
			    + ex.getErrorType().getMessage()).build();
	} catch (Exception ex) {
	    LOG.error(
		    "###USER### Error encountered in getAttribute Service : ",
		    ex);
	    return Response.status(Response.Status.BAD_REQUEST)
		    .entity(ex.getMessage()).build();
	}

    }

    @GET
    @Path("/apiType")
    @ApiOperation(value = "listApiTypes", notes = "List of Available API Types", response = Response.class)
    @ApiImplicitParams({ @ApiImplicitParam(name = "sandbox", value = "username", required = true, dataType = "string", paramType = "header") })
    public Response getApiTypes(@Context HttpServletRequest httpRequest) {
	APITypeRequestWrapperDTO requestDTO = new APITypeRequestWrapperDTO();
	requestDTO.setRequestType(RequestType.USER);
	requestDTO.setHttpRequest(httpRequest);
	RequestHandleable handler = RequestBuilderFactory
		.getInstance(requestDTO);
	Returnable returnable = null;

	try {
	    returnable = handler.execute(requestDTO);
	    Response response = Response.status(returnable.getHttpStatus())
		    .entity(returnable.getResponse()).build();
	    LOG.debug("GET API TYPES SERVICE RESPONSE : " + response);
	    return response;
	} catch (SandboxException ex) {
	    LOG.error("###USER### Error encountered in getApiTypes Service : ",
		    ex);
	    return Response
		    .status(Response.Status.BAD_REQUEST)
		    .entity(ex.getErrorType().getCode() + " "
			    + ex.getErrorType().getMessage()).build();
	} catch (Exception ex) {
	    LOG.error("###USER### Error encountered in getApiTypes Service : ",
		    ex);
	    return Response.status(Response.Status.BAD_REQUEST)
		    .entity(ex.getMessage()).build();
	}

    }

    @GET
    @Path("/{apiType}/serviceType")
    @ApiOperation(value = "listApiServiceCallTypes", notes = "List of available API Service Call Types", response = Response.class)
    @ApiImplicitParams({ @ApiImplicitParam(name = "sandbox", value = "username", required = true, dataType = "string", paramType = "header") })
    public Response getApiServices(
	    @ApiParam(value = "apiType", required = true) @PathParam("apiType") String apiType,
	    @Context HttpServletRequest httpRequest) {
	APIServiceCallRequestWrapperDTO requestDTO = new APIServiceCallRequestWrapperDTO();
	requestDTO.setApiType(apiType);
	requestDTO.setRequestType(RequestType.USER);
	requestDTO.setHttpRequest(httpRequest);
	RequestHandleable handler = RequestBuilderFactory
		.getInstance(requestDTO);
	Returnable returnable = null;

	try {
	    returnable = handler.execute(requestDTO);
	    Response response = Response.status(returnable.getHttpStatus())
		    .entity(returnable.getResponse()).build();
	    LOG.debug("GET API SERVICE CALLS RESPONSE : " + response);
	    return response;
	} catch (SandboxException ex) {
	    LOG.error(
		    "###USER### Error encountered in getApiServiceCallTypes Service : ",
		    ex);
	    return Response
		    .status(Response.Status.BAD_REQUEST)
		    .entity(ex.getErrorType().getCode() + " "
			    + ex.getErrorType().getMessage()).build();
	} catch (Exception ex) {
	    LOG.error(
		    "###USER### Error encountered in getApiServiceCallTypes Service : ",
		    ex);
	    return Response.status(Response.Status.BAD_REQUEST)
		    .entity(ex.getMessage()).build();
	}}


	@POST
	@Path("/managenumber")
	@ApiOperation(value = "addManageNumber", notes = "add numbers for user", response = Response.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sandbox", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
	public Response addManageNumber(@Context HttpServletRequest httpRequest, ManageNumberRequest request) {
		LOG.debug(request.getNumber() + " " + request.getNumberBalance() + " " + request.getReservedAmount() + " "
				+ request.getDescription() + " " + request.getStatus() + " " + request.getImsi() + " "
				+ request.getMnc() + " " + request.getMcc());
		ManageNumberRequestWrapperDTO requestDTO = new ManageNumberRequestWrapperDTO();
		requestDTO.setRequestType(RequestType.USER);
		requestDTO.setHttpRequest(httpRequest);
		requestDTO.setManageNumberRequest(request);
		RequestHandleable handler = RequestBuilderFactory.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
			LOG.debug("ADD MANAGE NUMBER SERVICE RESPONSE : " + response);
			return response;
		} catch (SandboxException ex) {
			LOG.error("###ADD MANAGE NUMBER### Error encountered in UserService : ", ex);
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(ex.getErrorType().getCode() + " " + ex.getErrorType().getMessage()).build();
		} catch (Exception ex) {
			LOG.error("###ADD MANAGE NUMBER### Error encountered in UserService : ", ex);
			return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
		}

	}


	@GET
	@Path("/numberDetails")
	@ApiOperation(value = "listNumberDetails", notes = "List of Available Number Details", response = Response.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "sandbox", value = "username", required = true, dataType = "string", paramType = "header") })
	public Response getNumberDetails(@Context HttpServletRequest httpRequest) {
		NumberDetailsRequestWrapperDTO requestDTO = new NumberDetailsRequestWrapperDTO();
		requestDTO.setRequestType(RequestType.USER);
		requestDTO.setHttpRequest(httpRequest);
		RequestHandleable handler = RequestBuilderFactory.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus())
					.entity(returnable.getResponse()).build();
			LOG.debug("GET NUMBER DETAILS SERVICE RESPONSE : " + response);
			return response;
		} catch (SandboxException ex) {
			LOG.error("###USER### Error encountered in getNumberDetails Service : ",
					ex);
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity(ex.getErrorType().getCode() + " "
							+ ex.getErrorType().getMessage()).build();
		} catch (Exception ex) {
			LOG.error("###USER### Error encountered in getNumberDetails Service : ",
					ex);
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(ex.getMessage()).build();
		}

	}

}