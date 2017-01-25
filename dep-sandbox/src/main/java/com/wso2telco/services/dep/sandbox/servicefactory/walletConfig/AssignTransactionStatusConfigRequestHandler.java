package com.wso2telco.services.dep.sandbox.servicefactory.walletConfig;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.WalletDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.AssignTransactionStatusConfigRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Attributes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.AttributeName;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.TransactionStatus;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import com.wso2telco.services.dep.sandbox.util.TableName;

public class AssignTransactionStatusConfigRequestHandler
		extends AbstractRequestHandler<AssignTransactionStatusConfigRequestWrapper> {

	private AssignTransactionStatusConfigRequestWrapper requestWrapper;
	private AssignTransactionStatusConfigResponseWrapper responseWrapper;
	private WalletDAO walletDAO;
	private NumberDAO numberDAO;

	{
		LOG = LogFactory.getLog(AssignTransactionStatusConfigRequestHandler.class);
		dao = DaoFactory.getGenaricDAO();
		walletDAO = DaoFactory.getWalletDAO();
		numberDAO = DaoFactory.getNumberDAO();
	}

	@Override
	protected Returnable getResponseDTO() {
		return responseWrapper;
	}

	@Override
	protected List<String> getAddress() {
		List<String> address = new ArrayList<String>();
		address.add(requestWrapper.getEndUserId());
		return address;
	}

	@Override
	protected boolean validate(AssignTransactionStatusConfigRequestWrapper wrapperDTO) throws Exception {

		AssignTransactionStatusConfigRequestBean requestBean = wrapperDTO.getRequestBean();

		String endUserId = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getEndUserId());
		String serviceCall = CommonUtil.getNullOrTrimmedValue(requestBean.getServiceCall());
		String accountStatus = CommonUtil.getNullOrTrimmedValue(requestBean.getStatus());

		List<ValidationRule> validationRulesList = new ArrayList<>();

		try {

			validationRulesList
					.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "endUserId", endUserId));
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "", serviceCall));
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "", accountStatus));

			ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
			validationRules = validationRulesList.toArray(validationRules);

			Validation.checkRequestParams(validationRules);

		} catch (CustomException ex) {
			LOG.error("###WALLETCONFIG### Error in Validations. ", ex);
			responseWrapper.setRequestError(
					constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
			return false;
		}

		return true;
	}

	@Override
	protected Returnable process(AssignTransactionStatusConfigRequestWrapper extendedRequestDTO) throws Exception {
		if (responseWrapper.getRequestError() != null) {
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			return responseWrapper;
		}
		try {

			AssignTransactionStatusConfigRequestBean requestBean = extendedRequestDTO.getRequestBean();

			String endUserId = CommonUtil.extractNumberFromMsisdn(extendedRequestDTO.getEndUserId());
			String serviceCall = CommonUtil.getNullOrTrimmedValue(requestBean.getServiceCall());
			String accountStatus = CommonUtil.getNullOrTrimmedValue(requestBean.getStatus());
			String userName = extendedRequestDTO.getUser().getUserName();
			String serviceCallPayment = ServiceName.MakePayment.toString().toLowerCase();
			String serviceCallRefund = ServiceName.RefundPayment.toString().toLowerCase();
			String statusRefused = TransactionStatus.Refused.toString().toLowerCase();
			String serviceCallRequest = serviceCall.toLowerCase();
			// Check valid service call
			if (!(serviceCallRequest.equals(serviceCallPayment) || serviceCallRequest.equals(serviceCallRefund))) {
				LOG.error("###WALLETCONFIG### Provide Valid Service Call ");
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.SERVICE_ERROR_OCCURED, "Provide Valid Service Call"));
				return responseWrapper;
			}

			// check valid account status
			if (!(accountStatus.toLowerCase().equals(statusRefused))) {
				LOG.error("###WALLETCONFIG### Invalid status ");
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.SERVICE_ERROR_OCCURED, "Provide Valid status"));
				return responseWrapper;
			}
			ManageNumber manageNumber = numberDAO.getNumber(endUserId, userName);
			Integer ownerId = manageNumber.getId();
			String tableName = TableName.NUMBERS.toString().toLowerCase();
			String attributeName = AttributeName.transactionStatus.toString();
			APITypes api = dao.getAPIType(RequestType.WALLET.toString());
			APIServiceCalls call = dao.getServiceCall(api.getId(), serviceCall);
			Attributes attributes = dao.getAttribute(attributeName);
			AttributeDistribution dis = dao.getAttributeDistribution(call.getApiServiceCallId(),
					attributes.getAttributeId());
			String accountStatusCapital = WordUtils.capitalize(accountStatus);
			List<AttributeValues> attributeValues = new ArrayList<AttributeValues>();
			AttributeValues valueObj = new AttributeValues();
			valueObj = dao.getAttributeValue(dis, ownerId);
			if (valueObj != null) {

				valueObj.setValue(accountStatusCapital);
				attributeValues.add(valueObj);

			} else {
				valueObj = new AttributeValues();
				valueObj.setAttributedid(dis);
				valueObj.setOwnerdid(ownerId);
				valueObj.setTobject(tableName);
				valueObj.setValue(accountStatusCapital);
				attributeValues.add(valueObj);
			}
			dao.saveAttributeValue(attributeValues);

			responseWrapper.setStatus("Successfully Updated attribute for transaction status!!");
			responseWrapper.setHttpStatus(Response.Status.CREATED);

			return responseWrapper;

		} catch (CustomException ex) {
			LOG.error("###WALLETCONFIG### Error in validations", ex);
			String errorMessage = "";
			if (ex.getErrvar() != null && ex.getErrvar().length > 0) {
				errorMessage = ex.getErrvar()[0];
			}
			responseWrapper.setRequestError(
					constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), errorMessage));
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
		} catch (Exception ex) {
			LOG.error("###WALLETCONFIG### Error Occured in WALLET Service. ", ex);
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
			return responseWrapper;
		}

		return responseWrapper;
	}

	@Override
	protected void init(AssignTransactionStatusConfigRequestWrapper extendedRequestDTO) throws Exception {
		requestWrapper = extendedRequestDTO;
		responseWrapper = new AssignTransactionStatusConfigResponseWrapper();

	}

}