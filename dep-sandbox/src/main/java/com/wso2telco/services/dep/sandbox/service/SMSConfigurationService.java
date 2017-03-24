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

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.SMSParameterConfigRequestBean;
import com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.SMSParameterConfigRequestWrapper;
import com.wso2telco.services.dep.sandbox.servicefactory.ussd.USSDApplicationConfigRequestBean;
import com.wso2telco.services.dep.sandbox.servicefactory.ussd.USSDApplicationConfigRequestWrapper;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/sms/{v1}/config")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
@Produces({ MediaType.APPLICATION_JSON })
@Api(value = "/sms/{v1}/config", description = "Rest Services for SMS API related Configurations")
public class SMSConfigurationService {

    Log log = LogFactory.getLog(SMSConfigurationService.class);

    @POST
    @Path("/addSMSParameters")
    @ApiOperation(value = "addApplicationInfo", notes = "Add new application for ussd", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sandbox", value = "username", required = true, dataType = "string", paramType = "header") })
    public Response addAccountInfo(
            SMSParameterConfigRequestBean smsParameterConfigRequestBean, @Context HttpServletRequest request) {

        SMSParameterConfigRequestWrapper requestDTO = new SMSParameterConfigRequestWrapper();
        requestDTO.setHttpRequest(request);
        requestDTO.setParameterConfigRequestBean(smsParameterConfigRequestBean);
        requestDTO.setRequestType(RequestType.SMSCONFIG);

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
