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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import com.wso2telco.services.dep.sandbox.dao.model.custom.MakePaymentRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.MakePaymentRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundTransactionRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.exception.SandboxException.SandboxErrorType;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.BalanceLookupRequestWrapper;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.ListTransactionRequestWrapper;
import com.wso2telco.services.dep.sandbox.util.RequestType;

@Path("wallet/{v1}")
@Produces({ MediaType.APPLICATION_JSON })
@Api(value = "wallet/{v1}", description = "Rest Service for Wallet API")
public class WalletService {

	Log LOG = LogFactory.getLog(WalletService.class);

	@POST
	@Path("/transaction/{endUserId}/payment")
	@ApiOperation(value = "makePaymentService", notes = "make payment service", response = Response.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sandbox", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public Response makePayment(
			@ApiParam(value = "endUserId", required = true) @PathParam("endUserId") String endUserId,
			MakePaymentRequestBean makePaymentRequestBean, @Context HttpServletRequest request) {
		LOG.debug("###WALLET### /{endUserId} invoked : endUserId - " + endUserId);
		if (makePaymentRequestBean != null) {
			LOG.debug(makePaymentRequestBean);
		}
		MakePaymentRequestWrapperDTO requestDTO = new MakePaymentRequestWrapperDTO();
		requestDTO.setHttpRequest(request);
		requestDTO.setEndUserId(endUserId);
		requestDTO.setMakePaymentRequestBean(makePaymentRequestBean);
		requestDTO.setRequestType(RequestType.WALLET);

		RequestHandleable<RequestDTO> handler = RequestBuilderFactory.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
			return response;
		} catch (Exception ex) {
			LOG.error("Make Payment SERVICE ERROR", ex);
			Response response = Response.status(Status.BAD_REQUEST).entity(
					SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxErrorType.SERVICE_ERROR.getMessage())
					.build();
			return response;
		}
	}

	@GET
	@Path("/transaction/{endUserId}/list")
	@ApiOperation(value = "listTransactionService", notes = "listTransactionService", response = Response.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sandbox", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public Response getAttributeServices(
			@ApiParam(value = "endUserId", required = false) @PathParam("endUserId") String endUserId,
			@Context HttpServletRequest request) {
		LOG.debug("/{endUserId}/list invorked :" + endUserId);
		ListTransactionRequestWrapper requestDTO = new ListTransactionRequestWrapper();
		requestDTO.setHttpRequest(request);
		requestDTO.setEndUserId(endUserId);
		requestDTO.setRequestType(RequestType.WALLET);

		RequestHandleable handler = RequestBuilderFactory.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
			LOG.debug("List Transaction SERVICE RESPONSE : " + response);
			return response;
		} catch (Exception ex) {
			LOG.error("List Transaction SERVICE ERROR : ", ex);
			return Response.status(Response.Status.BAD_REQUEST).entity(
					SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxErrorType.SERVICE_ERROR.getMessage())
					.build();
		}

	}

	@POST
	@Path("/transaction/{endUserId}/refund")
	@ApiOperation(value = "refundService", notes = "refundService service", response = Response.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sandbox", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public Response refundUser(@ApiParam(value = "endUserId", required = true) @PathParam("endUserId") String endUserId,
			RefundTransactionRequestBean refundRequestBean, @Context HttpServletRequest request) {
		LOG.debug("###WALLET### /{endUserId}/refund invoked : endUserId - " + endUserId);
		if (refundRequestBean != null) {
			LOG.debug(refundRequestBean);
		}
		RefundRequestWrapperDTO requestDTO = new RefundRequestWrapperDTO();
		requestDTO.setHttpRequest(request);
		requestDTO.setEndUserId(endUserId);
		requestDTO.setRefundRequestBean(refundRequestBean);
		requestDTO.setRequestType(RequestType.WALLET);

		RequestHandleable<RequestDTO> handler = RequestBuilderFactory.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
			return response;
		} catch (Exception ex) {
			LOG.error("Refund User SERVICE ERROR", ex);
			Response response = Response.status(Status.BAD_REQUEST).entity(
					SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxErrorType.SERVICE_ERROR.getMessage())
					.build();
			return response;
		}
	}

	@GET
	@Path("/transaction/{endUserId}/balance")
	@ApiOperation(value = "balanceLookupService", notes = "balanceLookupService", response = Response.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sandbox", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public Response balanceLookup(
			@ApiParam(value = "endUserId", required = false) @PathParam("endUserId") String endUserId,
			@Context HttpServletRequest request) {
		LOG.debug("/{endUserId}/balance invorked :" + endUserId);
		BalanceLookupRequestWrapper requestDTO = new BalanceLookupRequestWrapper();
		requestDTO.setHttpRequest(request);
		requestDTO.setEndUserId(endUserId);
		requestDTO.setRequestType(RequestType.WALLET);

		RequestHandleable handler = RequestBuilderFactory.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
			LOG.debug("Balance Lookup SERVICE RESPONSE : " + response);
			return response;
		} catch (Exception ex) {
			LOG.error("Balance Lookup SERVICE ERROR : ", ex);
			return Response.status(Response.Status.BAD_REQUEST).entity(
					SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxErrorType.SERVICE_ERROR.getMessage())
					.build();
		}

	}

}
