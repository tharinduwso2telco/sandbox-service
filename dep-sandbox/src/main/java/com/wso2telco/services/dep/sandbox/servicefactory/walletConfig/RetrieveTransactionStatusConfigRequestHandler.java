package com.wso2telco.services.dep.sandbox.servicefactory.walletConfig;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.WalletDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RetrieveTransactionStatusDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.TransactionStatus;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;

public class RetrieveTransactionStatusConfigRequestHandler
		extends AbstractRequestHandler<RetrieveTransactionStatusConfigRequestWrapper> {

	RetrieveTransactionStatusConfigRequestWrapper requestWrapper;
	RetrieveTransactionStatusConfigResponseWrapper responseWrapper;
	WalletDAO walletDAO;

	{
		LOG = LogFactory.getLog(RetrieveTransactionStatusConfigRequestHandler.class);
		dao = DaoFactory.getGenaricDAO();
		walletDAO = DaoFactory.getWalletDAO();
	}

	@Override
	protected Returnable getResponseDTO() {
		return responseWrapper;
	}

	@Override
	protected List<String> getAddress() {
		List<String> address = new ArrayList<String>();
		return address;
	}

	@Override
	protected boolean validate(RetrieveTransactionStatusConfigRequestWrapper wrapperDTO) throws Exception {

		String apiType = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getApiType());
		String serviceCall = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getServiceCall());

		try {
			ValidationRule[] validationRules = {
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "apiType", apiType),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "serviceCall", serviceCall) };

			Validation.checkRequestParams(validationRules);

		} catch (CustomException ex) {
			LOG.error("###WALLET### Error in Validation : " , ex);
			responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(),
					wrapperDTO.getServiceCall()));
			responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
		}
		return true;
	}

	@Override
	protected Returnable process(RetrieveTransactionStatusConfigRequestWrapper extendedRequestDTO) throws Exception {

		if (responseWrapper.getRequestError() != null) {
			return responseWrapper;
		}
		try {
			String apiType = extendedRequestDTO.getApiType();
			String apiTypeRequest = apiType.toLowerCase();
			String serviceCall = extendedRequestDTO.getServiceCall();
			String serviceCallRequest = serviceCall.toLowerCase();
			String status = TransactionStatus.Refused.toString();

			// check the valid api type
			if (!(apiTypeRequest.equals(RequestType.WALLET.toString().toLowerCase()))) {
				LOG.error("###WALLET### valid api type not provided. ");
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.SERVICE_ERROR_OCCURED, "valid api type not provided"));
				return responseWrapper;
			}

			if (!(serviceCallRequest.equals(ServiceName.MakePayment.toString().toLowerCase())
					|| serviceCallRequest.equals(ServiceName.RefundPayment.toString().toLowerCase()))) {
				LOG.error("###WALLET### valid service call not provided. ");
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.SERVICE_ERROR_OCCURED, "valid service call not provided"));
				return responseWrapper;
			}

			RetrieveTransactionStatusDTO valueDTO = new RetrieveTransactionStatusDTO();
			valueDTO.setStatus(status);
			responseWrapper.setStatusValueDTO(valueDTO);
			responseWrapper.setHttpStatus(Response.Status.OK);

		} catch (Exception ex) {
			LOG.error("###WALLET### Error Occured in Wallet Service. " , ex);
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
			return responseWrapper;
		}
		return responseWrapper;
	}

	@Override
	protected void init(RetrieveTransactionStatusConfigRequestWrapper extendedRequestDTO) throws Exception {
		requestWrapper = extendedRequestDTO;
		responseWrapper = new RetrieveTransactionStatusConfigResponseWrapper();
	}

}
