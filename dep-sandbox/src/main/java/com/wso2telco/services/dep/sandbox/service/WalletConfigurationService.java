package com.wso2telco.services.dep.sandbox.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
import com.wso2telco.services.dep.sandbox.dao.model.custom.AssignAccountInfoConfigRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.AssignTransactionStatusConfigRequestBean;
import com.wso2telco.services.dep.sandbox.exception.SandboxException.SandboxErrorType;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.walletConfig.AssignAccountInfoConfigRequestWrapper;
import com.wso2telco.services.dep.sandbox.servicefactory.walletConfig.RetrieveAccountStatusConfigRequestWrapper;
import com.wso2telco.services.dep.sandbox.servicefactory.walletConfig.RetrieveTransactionStatusConfigRequestWrapper;
import com.wso2telco.services.dep.sandbox.servicefactory.walletConfig.AssignTransactionStatusConfigRequestWrapper;
import com.wso2telco.services.dep.sandbox.util.RequestType;

@Path("/wallet/{v1}/config")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
@Produces({ MediaType.APPLICATION_JSON })
@Api(value = "/wallet/{v1}/config", description = "Rest Services for Wallet API related Configurations")
public class WalletConfigurationService {

	Log log = LogFactory.getLog(WalletConfigurationService.class);

	@POST
	@Path("/{endUserId}/addAccountInfo")
	@ApiOperation(value = "addAccountInfo", notes = "Add new account info for user", response = Response.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sandbox", value = "username", required = true, dataType = "string", paramType = "header") })
	public Response addAccountInfo(
			@ApiParam(value = "endUserId", required = true) @PathParam("endUserId") String endUserId,
			AssignAccountInfoConfigRequestBean accountInfoRequestBean, @Context HttpServletRequest request) {

		AssignAccountInfoConfigRequestWrapper requestDTO = new AssignAccountInfoConfigRequestWrapper();
		requestDTO.setHttpRequest(request);
		requestDTO.setEndUserId(endUserId);
		requestDTO.setRequestBean(accountInfoRequestBean);
		requestDTO.setRequestType(RequestType.WALLETCONFIG);

		RequestHandleable<RequestDTO> handler = RequestBuilderFactory.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
			return response;
		} catch (Exception ex) {
			log.error("###WALLET### Error in Wallet Configuration add Account info service", ex);
			Response response = Response.status(Status.BAD_REQUEST).entity(
					SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxErrorType.SERVICE_ERROR.getMessage())
					.build();
			return response;
		}
	}

	@POST
	@Path("/{endUserId}/addTransactionStatus")
	@ApiOperation(value = "addTransactionStatus", notes = "Add Transaction Status for endUserId", response = Response.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sandbox", value = "username", required = true, dataType = "string", paramType = "header") })
	public Response addTransactionStatus(
			@ApiParam(value = "endUserId", required = true) @PathParam("endUserId") String endUserId,
			AssignTransactionStatusConfigRequestBean transactionStatusRequestBean,
			@Context HttpServletRequest request) {

		AssignTransactionStatusConfigRequestWrapper requestDTO = new AssignTransactionStatusConfigRequestWrapper();
		requestDTO.setHttpRequest(request);
		requestDTO.setEndUserId(endUserId);
		requestDTO.setRequestBean(transactionStatusRequestBean);
		requestDTO.setRequestType(RequestType.WALLETCONFIG);

		RequestHandleable<RequestDTO> handler = RequestBuilderFactory.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
			return response;
		} catch (Exception ex) {
			log.error("###WALLET### Error in Wallet Configuration add Transaction Status Service", ex);
			Response response = Response.status(Status.BAD_REQUEST).entity(
					SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxErrorType.SERVICE_ERROR.getMessage())
					.build();
			return response;
		}
	}

	@GET
	@Path("/{apiType}/{serviceType}/getTransactionStatus")
	@ApiOperation(value = "getTransactionStatus", notes = "Transaction status for serviceCall", response = Response.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sandbox", value = "username", required = true, dataType = "string", paramType = "header") })
	public Response getTransactionVallue(
			@ApiParam(value = "apiType", required = true) @PathParam("apiType") String apiType,
			@ApiParam(value = "serviceType", required = true) @PathParam("serviceType") String serviceType,
			@Context HttpServletRequest httpRequest) {
		RetrieveTransactionStatusConfigRequestWrapper requestDTO = new RetrieveTransactionStatusConfigRequestWrapper();
		requestDTO.setApiType(apiType);
		requestDTO.setServiceCall(serviceType);
		requestDTO.setRequestType(RequestType.WALLETCONFIG);
		requestDTO.setHttpRequest(httpRequest);
		RequestHandleable handler = RequestBuilderFactory.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
			return response;
		} catch (Exception ex) {
			log.error("###WALLET### Error encountered in get Transaction status Service : ", ex);
			Response response = Response.status(Status.BAD_REQUEST).entity(
					SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxErrorType.SERVICE_ERROR.getMessage())
					.build();
			return response;
		}

	}

	@GET
	@Path("/{apiType}/{serviceType}/getAccountStatus")
	@ApiOperation(value = "getAccountStatus", notes = "Account status for serviceCall", response = Response.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sandbox", value = "username", required = true, dataType = "string", paramType = "header") })
	public Response getAccountStatus(@ApiParam(value = "apiType", required = true) @PathParam("apiType") String apiType,
			@ApiParam(value = "serviceType", required = true) @PathParam("serviceType") String serviceType,
			@Context HttpServletRequest httpRequest) {
		RetrieveAccountStatusConfigRequestWrapper requestDTO = new RetrieveAccountStatusConfigRequestWrapper();
		requestDTO.setApiType(apiType);
		requestDTO.setServiceCall(serviceType);
		requestDTO.setRequestType(RequestType.WALLETCONFIG);
		requestDTO.setHttpRequest(httpRequest);
		RequestHandleable handler = RequestBuilderFactory.getInstance(requestDTO);
		Returnable returnable = null;

		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
			return response;
		} catch (Exception ex) {
			log.error("###WALLET### Error encountered in get Account status Service : ", ex);
			Response response = Response.status(Status.BAD_REQUEST).entity(
					SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxErrorType.SERVICE_ERROR.getMessage())
					.build();
			return response;
		}

	}
}
