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
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactoryGateway;
import com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.gateway.OutboundSMSMessageRequestBeanGateway;
import com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.gateway.SendMTSMSRequestWrapperDTOGateway;
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


@Path("smsmessaging")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Api(value = "/v1_2/sms", description = "sms")
    public class SmsServiceGateway {

        Log LOG = LogFactory.getLog(SmsServiceGateway.class);

        @POST
        @Path("/v1_2/outbound/{shortCode}/requests")
        @ApiOperation(value = "sms", notes = "Send SMS service in Gateway", response = Response.class)
        @ApiImplicitParams({
                @ApiImplicitParam(name = "sandbox", value = "Authorization token",
                        required = true, dataType = "string", paramType = "header")
        })
        public Response handleSendMTSMSRequest(@Context HttpServletRequest httpRequest,
                                               @PathParam("shortCode") String shortCode, OutboundSMSMessageRequestBeanGateway outboundSMSMessageRequestBean_gateway) {

            SendMTSMSRequestWrapperDTOGateway requestDTO = new SendMTSMSRequestWrapperDTOGateway();
            requestDTO.setHttpRequest(httpRequest);
            requestDTO.setRequestType(RequestType.SMSMESSAGING);
            requestDTO.setApiVersion("v1_2");
            requestDTO.setShortCode(shortCode);
            requestDTO.setOutboundSMSMessageRequestBeanGw(outboundSMSMessageRequestBean_gateway);

            RequestHandleable handler = RequestBuilderFactoryGateway.getInstance(requestDTO);

            Returnable returnable = null;

            try {

                returnable = handler.execute(requestDTO);
                return Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
            } catch (Exception e) {
                e.printStackTrace();
                return Response.status(Response.Status.BAD_REQUEST).entity(returnable.getResponse()).build();
            }
        }



    }
