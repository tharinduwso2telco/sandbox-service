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
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.ussd.USSDApplicationConfigRequestBean;
import com.wso2telco.services.dep.sandbox.servicefactory.ussd.USSDApplicationConfigRequestWrapper;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ussd/{v1}/config")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
@Produces({ MediaType.APPLICATION_JSON })
@Api(value = "/ussd/{v1}/config", description = "Rest Services for USSD API related Configurations")
public class USSSDConfigurationService {


    Log log = LogFactory.getLog(USSSDConfigurationService.class);

    @POST
    @Path("/addApplicationInfo")
    @ApiOperation(value = "addApplicationInfo", notes = "Add new application for ussd", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sandbox", value = "username", required = true, dataType = "string", paramType = "header") })
    public Response addAccountInfo(
            USSDApplicationConfigRequestBean ussdApplicationConfigRequestBean, @Context HttpServletRequest request) {

        USSDApplicationConfigRequestWrapper requestDTO = new USSDApplicationConfigRequestWrapper();
        requestDTO.setHttpRequest(request);
        requestDTO.setUssdApplicationConfigRequestBean(ussdApplicationConfigRequestBean);
        requestDTO.setRequestType(RequestType.USSDCONFIG);

        RequestHandleable<RequestDTO> handler = RequestBuilderFactory.getInstance(requestDTO);
        Returnable returnable = null;

        try {
            returnable = handler.execute(requestDTO);
            Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
            return response;
        } catch (Exception ex) {
            log.error("###USSD### Error in USSD Configuration add Application info service", ex);
            Response response = Response.status(Response.Status.BAD_REQUEST).entity(
                    SandboxException.SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxException.SandboxErrorType.SERVICE_ERROR.getMessage())
                    .build();
            return response;
        }
    }













}




