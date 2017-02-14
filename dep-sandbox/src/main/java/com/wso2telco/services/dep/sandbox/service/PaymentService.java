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
import com.wso2telco.services.dep.sandbox.dao.model.custom.*;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.payment.PaymentRefundRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.payment.PaymentRequestHandler;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("payment/{v1}")
@Produces({MediaType.APPLICATION_JSON})
@Api(value = "payment/{v1}", description = "Rest Service for Payment API")
public class PaymentService {

    Log LOG = LogFactory.getLog(PaymentService.class);

    @POST
    @Path("/{endUserId}/transactions/amount")
    @ApiOperation(value = "makePaymentService", notes = "make payment service", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sandbox", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    public Response makePayment(
            @ApiParam(value = "endUserId", required = true) @PathParam("endUserId") String endUserId,
            PaymentRefundTransactionRequestBean makePaymentRequestBean, @Context HttpServletRequest request) {
        LOG.debug("###PAYMENT### /{endUserId} invoked : endUserId - " + endUserId);
        if (makePaymentRequestBean != null) {
            LOG.debug(makePaymentRequestBean);
        }
        //Separate Charged and Refunded request calls
        if (makePaymentRequestBean.getAmountTransaction().getTransactionOperationStatus().equals("Charged")) {

            ChargePaymentRequestWrapperDTO requestDTO = new ChargePaymentRequestWrapperDTO();
            requestDTO.setHttpRequest(request);
            requestDTO.setEndUserId(endUserId);
            requestDTO.setPaymentRefundTransactionRequestBean(makePaymentRequestBean);
            requestDTO.setRequestType(RequestType.PAYMENT);

            RequestHandleable<RequestDTO> handler = RequestBuilderFactory.getInstance(requestDTO);
            Returnable returnable = null;

            try {
                returnable = handler.execute(requestDTO);
                Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
                return response;
            } catch (Exception ex) {
                LOG.error("Make Payment SERVICE ERROR", ex);
                Response response = Response.status(Response.Status.BAD_REQUEST).entity(
                        SandboxException.SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxException.SandboxErrorType.SERVICE_ERROR.getMessage())
                        .build();
                return response;
            }

        } else {

            PaymentRefundRequestWrapperDTO requestDTO = new PaymentRefundRequestWrapperDTO();
            requestDTO.setHttpRequest(request);
            requestDTO.setEndUserId(endUserId);
            requestDTO.setRefundRequestBean(makePaymentRequestBean);
            requestDTO.setRequestType(RequestType.PAYMENT);

            RequestHandleable<RequestDTO> handler = RequestBuilderFactory.getInstance(requestDTO);
            Returnable returnable = null;

            try {
                returnable = handler.execute(requestDTO);
                Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
                return response;
            } catch (Exception ex) {
                LOG.error("Make Payment SERVICE ERROR", ex);
                Response response = Response.status(Response.Status.BAD_REQUEST).entity(
                        SandboxException.SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxException.SandboxErrorType.SERVICE_ERROR.getMessage())
                        .build();
                return response;
            }
        }
    }
}
