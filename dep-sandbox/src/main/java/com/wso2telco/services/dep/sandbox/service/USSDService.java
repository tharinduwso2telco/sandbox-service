/*******************************************************************************
 * Copyright (c) 2015-2017, WSO2.Telco Inc. (http://www.wso2telco.com)
 *
 * All Rights Reserved. WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.USSDSessionInitiatorRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.USSDSessionRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("ussd/{v1}")
@Produces({MediaType.APPLICATION_JSON})
@Api(value = "ussd/v1", description = "Rest Service for USSD API")
public class USSDService {

    Log LOG = LogFactory.getLog(USSDService.class);

    @POST
    @Path("/ussd/v1/outbound/{endUserId}")
    @ApiOperation(value = "ussdSession", notes = "initiate ussd session with endUser", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sandbox", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    public Response ussdSession(
            @ApiParam(value = "endUserId", required = true) @PathParam("endUserId") String endUserId,
            USSDSessionInitiatorRequestBean ussdSessionRequestBean, @Context HttpServletRequest request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("###USSD### /{endUserId} invoked : endUserId - " + endUserId);
        }
        if (LOG.isDebugEnabled() && ussdSessionRequestBean != null) {
            LOG.debug(ussdSessionRequestBean);
        }

        USSDSessionRequestWrapperDTO requestDTO = new USSDSessionRequestWrapperDTO();
        requestDTO.setHttpRequest(request);
        requestDTO.setEndUserId(endUserId);
        requestDTO.setUssdSessionRequestBean(ussdSessionRequestBean);
        requestDTO.setRequestType(RequestType.USSD);

        RequestHandleable<RequestDTO> handler = RequestBuilderFactory.getInstance(requestDTO);
        Returnable returnable;

        try {
            returnable = handler.execute(requestDTO);
            Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
            return response;
        } catch (Exception ex) {
            LOG.error("USSD initializing session Error", ex);
            Response response = Response.status(Response.Status.BAD_REQUEST).entity(
                    SandboxException.SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxException.SandboxErrorType.SERVICE_ERROR.getMessage())
                    .build();
            return response;
        }
    }

}
