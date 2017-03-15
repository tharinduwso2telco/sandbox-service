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
import com.wso2telco.services.dep.sandbox.exception.SandboxException;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wso2telco.services.dep.sandbox.dao.model.custom.QuerySMSDeliveryStatusRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactoryGateway;
import com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.gateway.*;
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
    @Api(value = "/v1_2/sms", description = " Rest Service for SMS API")
    public class SmsServiceGateway {

        Log LOG = LogFactory.getLog(SmsServiceGateway.class);

        @POST
        @Path("/v1_2/outbound/{shortCode}/requests")
        @ApiOperation(value = "Send SMS Service", notes = "Send SMS service in Gateway", response = Response.class)
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
                return Response.status(Response.Status.BAD_REQUEST).entity(returnable.getResponse()).build();
            }
        }


    @GET
    @Path("/v1_2/inbound/registrations/{registrationId}/messages")
    @ApiOperation(value = "Receiving SMS Service", notes = "Receiving SMS API", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sandbox", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
    public Response ReceivingSMS(
            @ApiParam(value = "registrationId", required = true) @PathParam("registrationId") String registrationID, @ApiParam(value = "maxBatchSize", required = true) @QueryParam("maxBatchSize") int maxBatchSize,
            @Context HttpServletRequest request) {

        LOG.debug("registrationId={registrationId}&maxBatchSize={maxBatchSize} invorked :" + registrationID +" " +maxBatchSize);

        ReceivingSMSRequestWrapperGateway requestDTO = new ReceivingSMSRequestWrapperGateway();
        requestDTO.setHttpRequest(request);
        requestDTO.setRegistrationID(registrationID);
        requestDTO.setMaxBatchSize(maxBatchSize);
        requestDTO.setRequestType(RequestType.SMSMESSAGING);

        RequestHandleable handler = RequestBuilderFactoryGateway.getInstance(requestDTO);
        Returnable returnable = null;

        try {
            returnable = handler.execute(requestDTO);
            Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
            LOG.debug("SMS RECEIVING  SERVICE RESPONSE : " + response);
            return response;
        } catch (Exception ex) {
            LOG.error("SMS RECEIVING SERVICE ERROR : ", ex);
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    SandboxException.SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxException.SandboxErrorType.SERVICE_ERROR.getMessage())
                    .build();
        }

    }

    @POST
    @Path("/v1_2/inbound/subscriptions")
    @ApiOperation(value = "Subscribe to Notifications of Messages Sent to Your Application Service", notes = "SMS subscriptions service in Gateway", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sandbox", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public Response subscribeToApplication(@Context HttpServletRequest httpRequest, SubscribeApplicationNotificationsRequestBeanGateway subscribeApplicationNotificationsRequestBean) {

        SubscribeApplicationNotificationsRequestWrapperGateway requestDTO = new SubscribeApplicationNotificationsRequestWrapperGateway();
        requestDTO.setHttpRequest(httpRequest);
        requestDTO.setRequestType(RequestType.SMSMESSAGING);
        requestDTO.setApiVersion("v1_2");
        requestDTO.setSubscribeApplicationNotificationsRequestBean(subscribeApplicationNotificationsRequestBean);
        RequestHandleable handler = RequestBuilderFactoryGateway.getInstance(requestDTO);
        Returnable returnable = null;

            try {

                returnable = handler.execute(requestDTO);
                return Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
            } catch (Exception e) {
                return Response.status(Response.Status.BAD_REQUEST).entity(returnable.getResponse()).build();
            }
        }

    @DELETE
    @Path("/v1_2/inbound/subscriptions/{subscriptionID}")
    @ApiOperation(value = "Stop the Subscription to Message Notifications", notes = "SMS subscriptions service in Gateway", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sandbox", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public Response deleteSubscribeToApplication(@ApiParam(value = "subscriptionID", required = true) @PathParam("subscriptionID") String subscriptionID,@Context HttpServletRequest httpRequest) {

        StopSubscriptionMessageNotificationRequestWrapper requestDTO = new StopSubscriptionMessageNotificationRequestWrapper();
        requestDTO.setHttpRequest(httpRequest);
        requestDTO.setRequestType(RequestType.SMSMESSAGING);
        requestDTO.setApiVersion("v1_2");
        requestDTO.setSubscriptionID(subscriptionID);
        RequestHandleable handler = RequestBuilderFactoryGateway.getInstance(requestDTO);
        Returnable returnable = null;

        try {

            returnable = handler.execute(requestDTO);
            return Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(returnable.getResponse()).build();
        }
    }

    @POST
    @Path("/v1_2/outbound/{senderAddress}/subscriptions")
    @ApiOperation(value = "Subscribe to delivery status notifications sent by SMS", notes = "Subscribe SMS service in Gateway", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sandbox", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public Response handleSubscribeToDeliveryNotifications(
            @Context HttpServletRequest httpRequest,
            @ApiParam(value = "senderAddress", required = true)
            @PathParam("senderAddress") String senderAddress,
            SubscribeToDeliveryNotificationRequestBeanGateway subscribeToDeliveryNotificationRequestBeanGateway) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("###SMS### /{senderAddress} invoked : senderAddress - " + senderAddress);
        }
        if (LOG.isDebugEnabled() && subscribeToDeliveryNotificationRequestBeanGateway != null) {
            LOG.debug(subscribeToDeliveryNotificationRequestBeanGateway);
        }

        SubscribeToDeliveryNotificationWrapperDTOGateway requestDTO = new
                SubscribeToDeliveryNotificationWrapperDTOGateway();
        requestDTO.setHttpRequest(httpRequest);
        requestDTO.setRequestType(RequestType.SMSMESSAGING);
        requestDTO.setApiVersion("v1_2");
        requestDTO.setSenderAddress(senderAddress);
        requestDTO.setSubscribeToDeliveryNotificationRequestBeanGateway
                (subscribeToDeliveryNotificationRequestBeanGateway);

        RequestHandleable handler = RequestBuilderFactoryGateway.getInstance(requestDTO);

        Returnable returnable = null;

        try {
            returnable = handler.execute(requestDTO);
            Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
            return response;
        } catch (Exception e) {
            LOG.error("Subscribe to Notification SERVICE ERROR", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    SandboxException.SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxException
                            .SandboxErrorType.SERVICE_ERROR.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/v1_2/outbound/{senderAddress}/subscription/{subscriptionID}")
    @ApiOperation(value = "Stop subscribing to delivery status notifications", notes = "Stop subscribing to delivery status notifications", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sandbox", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public Response handleDeleteSubscriptionNotification(
            @Context HttpServletRequest httpRequest,
            @ApiParam(value = "senderAddress", required = true)
            @PathParam("senderAddress") String senderAddress, @PathParam("subscriptionID") int subscriptionID) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("###SMS### /{senderAddress} invoked : senderAddress - " + senderAddress);
        }

        StopSubscriptionNotificationRequestWrapper requestDTO = new StopSubscriptionNotificationRequestWrapper();

        requestDTO.setHttpRequest(httpRequest);
        requestDTO.setApiVersion("v1_2");
        requestDTO.setSenderAddress(senderAddress);
        requestDTO.setSubscriptionID(subscriptionID);
        requestDTO.setRequestType(RequestType.SMSMESSAGING);

        RequestHandleable handler = RequestBuilderFactoryGateway.getInstance(requestDTO);

        Returnable returnable = null;

        try {
            returnable = handler.execute(requestDTO);
            Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
            return response;
        } catch (Exception e) {
            LOG.error("Subscribe to Notification SERVICE ERROR", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    SandboxException.SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxException
                            .SandboxErrorType.SERVICE_ERROR.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/v1_2/outbound/{shortCode}/requests/{smsTransactionId}/deliveryInfos")
    @ApiOperation(value = "querySMSDeliveryStatusRequest", notes = "Request SMS Delivary Status", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sandbox", value = "Authorization token", required = true, dataType = "string", paramType = "header")})

    public Response handleQuerySmsDelivaryStatusRequest(@Context HttpServletRequest httpServletRequest, @PathParam("shortCode") String shortCode, @PathParam("smsTransactionId") String smsTransactionId)
    {
        QuerySMSDeliveryStatusRequestWrapperDTO requestWrapperDTO = new QuerySMSDeliveryStatusRequestWrapperDTO();
        requestWrapperDTO.setShortCode(shortCode);
        requestWrapperDTO.setApiVersion("v1_2");
        requestWrapperDTO.setMtSMSTransactionId(smsTransactionId);
        requestWrapperDTO.setHttpRequest(httpServletRequest);
        requestWrapperDTO.setRequestType(RequestType.SMSMESSAGING);
        RequestHandleable requestHandler = RequestBuilderFactory.getInstance(requestWrapperDTO);
        Returnable returnable = null;
        try {
            returnable = requestHandler.execute(requestWrapperDTO);
            return  Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
        } catch (Exception e) {
           LOG.error("SMS QUERY DELIVERY STATUS ERROR"+e);
            return Response.status(Response.Status.BAD_REQUEST).entity(returnable.getResponse()).build();
        }


    }



}

