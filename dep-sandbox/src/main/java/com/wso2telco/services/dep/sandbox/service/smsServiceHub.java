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
import com.wso2telco.services.dep.sandbox.dao.model.custom.QuerySMSDeliveryStatusRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactoryHub;
import com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.hub.outboundSMSMessageRequestBeanHub;
import com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.hub.sendMTSMSRequestWrapperDTOHub;
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

import static com.wso2telco.services.dep.sandbox.service.SandboxDTO.getBehaveType;

@Path("smsmessaging")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/v1_3/sms", description = "sms")
public class smsServiceHub {

    Log LOG = LogFactory.getLog(smsServiceHub.class);
    String mode = getBehaveType();

    @POST
    @Path("/v1_3/outbound/{shortCode}/requests")
    @ApiOperation(value = "sms", notes = "sms ", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sandbox", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public Response handleSendMTSMSRequest(@Context HttpServletRequest httpRequest,
                                           @PathParam("shortCode") String shortCode, outboundSMSMessageRequestBeanHub outboundSMSMessageRequestBean_hub) {

        sendMTSMSRequestWrapperDTOHub requestDTO = new sendMTSMSRequestWrapperDTOHub();
        requestDTO.setHttpRequest(httpRequest);
        requestDTO.setRequestType(RequestType.SMSMESSAGING);
        requestDTO.setApiVersion("v1_3");
        requestDTO.setShortCode(shortCode);
        requestDTO.setOutboundSMSMessageRequestBean(outboundSMSMessageRequestBean_hub);

        RequestHandleable handler = RequestBuilderFactoryHub.getInstance(requestDTO);

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
