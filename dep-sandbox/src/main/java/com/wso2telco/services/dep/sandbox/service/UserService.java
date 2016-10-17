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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wso2telco.services.dep.sandbox.dao.UserDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.AttributeRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.AttributeRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.QueryProvisioningServicesRequestWrapper;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RegisterUserServiceRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.RequestType;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/user", description = "Rest Service for user Configurations in sandbox")
public class UserService {

    Log LOG = LogFactory.getLog(UserService.class);

    @POST
    @Path("/{userName}/register")
    @ApiOperation(value = "registerUser", notes = "register New User", response = Response.class)
    @ApiImplicitParams({
	    @ApiImplicitParam(name = "sandbox", value = "Authorization token", 
	                     required = true, dataType = "string", paramType = "header")
	})
    public Response registerUser(@PathParam("userName") String userName, @Context HttpServletRequest httpRequest) {
	RegisterUserServiceRequestWrapperDTO requestDTO = new RegisterUserServiceRequestWrapperDTO();
	requestDTO.setUserName(userName);
	requestDTO.setRequestType(RequestType.ADMIN);
	requestDTO.setHttpRequest(httpRequest);
	RequestHandleable handler = RequestBuilderFactory.getInstance(requestDTO);
	Returnable returnable = null;

	try {
	    returnable = handler.execute(requestDTO);
	    Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
	    LOG.debug("REGISTER USER SERVICE RESPONSE : " + response);
	    return response;
	} catch (SandboxException ex) {
	    LOG.error("###REGISTER_USER### Error encountered in UserService : ", ex);
	    return Response.status(Response.Status.BAD_REQUEST)
		    .entity(ex.getErrorType().getCode() + " " + ex.getErrorType().getMessage()).build();
	} catch (Exception ex) {
	    LOG.error("###REGISTER_USER### Error encountered in UserService : ", ex);
	    return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
	}

    }
    
    @POST
    @Path("/addattribute")
    @ApiOperation(value = "addAttribute", notes = "add new attributes for user", response = Response.class)
    @ApiImplicitParams({
	    @ApiImplicitParam(name = "sandbox", value = "Authorization token", 
	                     required = true, dataType = "string", paramType = "header")
	})
    public Response addAttribute(@Context HttpServletRequest httpRequest, AttributeRequestBean request) {
	AttributeRequestWrapperDTO requestDTO = new AttributeRequestWrapperDTO();
	requestDTO.setRequestType(RequestType.ADMIN);
	requestDTO.setHttpRequest(httpRequest);
	requestDTO.setAttribute(request);;
	RequestHandleable handler = RequestBuilderFactory.getInstance(requestDTO);
	Returnable returnable = null;

	try {
	    returnable = handler.execute(requestDTO);
	    Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
	    LOG.debug("ADD ATTRIBUTE USER SERVICE RESPONSE : " + response);
	    return response;
	} catch (SandboxException ex) {
	    LOG.error("###ADD_ATTRIBUTE### Error encountered in UserService : ", ex);
	    return Response.status(Response.Status.BAD_REQUEST)
		    .entity(ex.getErrorType().getCode() + " " + ex.getErrorType().getMessage()).build();
	} catch (Exception ex) {
	    LOG.error("###ADD_ATTRIBUTE### Error encountered in UserService : ", ex);
	    return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
	}

    }
}