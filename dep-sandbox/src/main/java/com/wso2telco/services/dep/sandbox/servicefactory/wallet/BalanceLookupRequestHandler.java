package com.wso2telco.services.dep.sandbox.servicefactory.wallet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.LoggingDAO;
import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.WalletDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.AccountInfo;
import com.wso2telco.services.dep.sandbox.dao.model.custom.BalanceLookupDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.BalanceLookupResponseBean;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.MessageLogHandler;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import com.wso2telco.services.dep.sandbox.util.TableName;

public class BalanceLookupRequestHandler extends AbstractRequestHandler<BalanceLookupRequestWrapper> {

	private WalletDAO walletDAO;
	private LoggingDAO loggingDAO;
	private NumberDAO numberDAO;
	private BalanceLookupRequestWrapper requestWrapper;
	private BalanceLookupResponseWrapper responseWrapper;
	private MessageLogHandler logHandler;

	{
		LOG = LogFactory.getLog(BalanceLookupRequestHandler.class);
		walletDAO = DaoFactory.getWalletDAO();
		loggingDAO = DaoFactory.getLoggingDAO();
		numberDAO = DaoFactory.getNumberDAO();
		dao = DaoFactory.getGenaricDAO();
		logHandler = MessageLogHandler.getInstance();
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
	protected boolean validate(BalanceLookupRequestWrapper wrapperDTO) throws Exception {

		String endUserId = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getEndUserId());

		try {
			ValidationRule[] validationRules = { new ValidationRule(
					ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId) };

			Validation.checkRequestParams(validationRules);

		} catch (CustomException ex) {
			LOG.error("###WALLET### Error in Validations. ", ex);
			responseWrapper.setRequestError(
					constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			return false;
		}
		return true;
	}

	@Override
	protected Returnable process(BalanceLookupRequestWrapper extendedRequestDTO) throws Exception {
		if (responseWrapper.getRequestError() != null) {
			return responseWrapper;

		}
		try {
			String msisdn = extendedRequestDTO.getEndUserId();
			String endUserId = getLastMobileNumber(msisdn);
			String serviceCall = ServiceName.BalanceLookup.toString();
			String userName = extendedRequestDTO.getUser().getUserName();
			Integer userId = extendedRequestDTO.getUser().getId();

			// Save Request Log
			APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
			APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCall);

			Gson gson = new Gson();
			JSONObject object = new JSONObject();
			object.put("endUserId", msisdn);
			String jsonString = gson.toJson(object);
			MessageLog messageLog = new MessageLog();
			messageLog.setServicenameid(apiServiceCalls.getApiServiceCallId());
			messageLog.setUserid(extendedRequestDTO.getUser().getId());
			messageLog.setReference("msisdn");
			messageLog.setValue(endUserId);
			messageLog.setRequest(jsonString);
			messageLog.setMessageTimestamp(new Date());

			int ref_number = loggingDAO.saveMessageLog(messageLog);
			String serverReferenceCodeFormat = String.format("%06d", ref_number);
			String serverReferenceCode = "WALLET_REF_" + serverReferenceCodeFormat;
			ManageNumber numberBalance = numberDAO.getNumber(endUserId,
					extendedRequestDTO.getUser().getUserName().toString());
			Double accountBalance = numberBalance.getBalance();
			String attributeName = null;
			List<AttributeValues> accountValue = new ArrayList<AttributeValues>();
			List<String> attribute = new ArrayList<String>();
			attribute.add(AttributeName.Currency.toString());
			attribute.add(AttributeName.accountStatus.toString());

			String tableName = TableName.NUMBERS.toString().toLowerCase();
			accountValue = walletDAO.getTransactionValue(endUserId, attribute, tableName, userId);
			if (accountValue.isEmpty()) {
				LOG.error("###WALLET### Account currecy and status not configured ");
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.SERVICE_ERROR_OCCURED, "Account currecy and status not configured"));
				return responseWrapper;
			}
			BalanceLookupResponseBean responseBean = new BalanceLookupResponseBean();
			AccountInfo accountInfo = new AccountInfo();
			responseBean.setEndUserId(msisdn);
			accountInfo.setAccountBalance(accountBalance.toString());
			for (AttributeValues values : accountValue) {
				attributeName = ((values.getAttributedid()).getAttribute()).getAttributeName().toString().toLowerCase();

				if (AttributeName.Currency.toString().toLowerCase().equals(attributeName)) {
					accountInfo.setAccountCurrency(values.getValue());

				} else if (AttributeName.accountStatus.toString().toLowerCase().equals(attributeName)) {

					accountInfo.setAccountStatus(values.getValue());
				}
			}

			responseBean.setResourceURL(CommonUtil.getResourceUrl(extendedRequestDTO));
			responseBean.setServerReferenceCode(serverReferenceCode);
			BalanceLookupDTO lookupDTO = new BalanceLookupDTO();
			responseBean.setAccountInfo(accountInfo);
			lookupDTO.setaccountBalance(responseBean);
			responseWrapper.setBalanceLookupDTO(lookupDTO);
			responseWrapper.setHttpStatus(Response.Status.OK);

		} catch (Exception ex) {
			LOG.error("###WALLET### Error Occured in Wallet Service. ", ex);
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
			return responseWrapper;
		}
		return responseWrapper;
	}

	@Override
	protected void init(BalanceLookupRequestWrapper extendedRequestDTO) throws Exception {
		requestWrapper = extendedRequestDTO;
		responseWrapper = new BalanceLookupResponseWrapper();
	}
}