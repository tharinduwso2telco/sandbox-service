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
import javax.ws.rs.core.Response.Status;

import com.wso2telco.services.dep.sandbox.dao.model.custom.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wso2telco.services.dep.sandbox.exception.SandboxException.SandboxErrorType;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.RequestType;

@Path("/credit/{v1}/credit")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@Api(value = "/{v1}/credit", description = "Rest Service for Credit API")
public class CreditService {
	
	Log LOG = LogFactory.getLog(CreditService.class);
	
	@POST
	@Path("/{msisdn}/apply")
	@ApiOperation(value = "creditApplyForRequestedService", notes = "credit apply requested service", response = Response.class)
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "sandbox", value = "Authorization token", 
	                     required = true, dataType = "string", paramType = "header")
	})
	public Response creditApplyForRequestedService(
			@ApiParam(value = "msisdn", required = true) @PathParam("msisdn") String msisdn,
			CreditRequestBean creditApplyRequest, @Context HttpServletRequest request) {
		LOG.debug("###CREDIT### /{msisdn}/apply invoked : msisdn - " + msisdn);
		if (creditApplyRequest != null) {
			LOG.debug(creditApplyRequest);
		}
		CreditApplyRequestWrapper requestDTO = new CreditApplyRequestWrapper();
		requestDTO.setHttpRequest(request);
		requestDTO.setMsisdn(msisdn);
		requestDTO.setCreditRequestBean(creditApplyRequest);
		requestDTO.setRequestType(RequestType.CREDIT);
		
		RequestHandleable<RequestDTO> handler = RequestBuilderFactory.getInstance(requestDTO);
		Returnable returnable = null;
		
		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
			return response;
		} catch (Exception ex) {
			LOG.error("###CREDIT### Error in Credit Service", ex);
			Response response = Response.status(Status.BAD_REQUEST).entity(SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxErrorType.SERVICE_ERROR.getMessage()).build();
			return response;
		}
	}
	
	@POST
	@Path("/{msisdn}/refund")
	@ApiOperation(value = "patialRefundForRequestedService", notes = "patial refund requested service", response = Response.class)
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "sandbox", value = "Authorization token", 
	                     required = true, dataType = "string", paramType = "header")
	})
	public Response patialRefundForRequestedService(
			@ApiParam(value = "msisdn", required = true) @PathParam("msisdn") String msisdn,
			RefundRequestBean refundRequest, @Context HttpServletRequest request) {
		LOG.debug("###CREDIT### /{msisdn}/refund invoked : msisdn - " + msisdn);
		if (refundRequest != null) {
			LOG.debug(refundRequest);
		}
		PatialRefundRequestWrapper requestDTO = new PatialRefundRequestWrapper();
		requestDTO.setHttpRequest(request);
		requestDTO.setMsisdn(msisdn);
		requestDTO.setRefundRequestBean(refundRequest);
		requestDTO.setRequestType(RequestType.CREDIT);
		
		RequestHandleable<RequestDTO> handler = RequestBuilderFactory.getInstance(requestDTO);
		Returnable returnable = null;
		
		try {
			returnable = handler.execute(requestDTO);
			Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
			return response;
		} catch (Exception ex) {
			LOG.error("###CREDIT### Error in Refund Service", ex);
			Response response = Response.status(Status.BAD_REQUEST).entity(SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxErrorType.SERVICE_ERROR.getMessage()).build();
			return response;
		}
	}

}
