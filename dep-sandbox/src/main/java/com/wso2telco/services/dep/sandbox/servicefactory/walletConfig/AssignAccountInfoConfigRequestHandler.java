package com.wso2telco.services.dep.sandbox.servicefactory.walletConfig;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
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
import com.wso2telco.services.dep.sandbox.dao.model.custom.AssignAccountInfoConfigRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import com.wso2telco.services.dep.sandbox.util.TableName;

public class AssignAccountInfoConfigRequestHandler
		extends AbstractRequestHandler<AssignAccountInfoConfigRequestWrapper> {

	private AssignAccountInfoConfigRequestWrapper requestWrapper;
	private AssignAccountInfoConfigResponseWrapper responseWrapper;
	private WalletDAO walletDAO;
	private NumberDAO numberDAO;

	private Map<String, String> valuesInRequset = new HashMap<String, String>();
	private Map<AttributeDistribution, String> attributeValueDistribution = new HashMap<AttributeDistribution, String>();

	{
		LOG = LogFactory.getLog(AssignAccountInfoConfigRequestHandler.class);
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
	protected boolean validate(AssignAccountInfoConfigRequestWrapper wrapperDTO) throws Exception {

		AssignAccountInfoConfigRequestBean requestBean = wrapperDTO.getRequestBean();

		String endUserId = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getEndUserId());
		String accountCurrency = CommonUtil.getNullOrTrimmedValue(requestBean.getCurrency());
		String accountStatus = CommonUtil.getNullOrTrimmedValue(requestBean.getStatus());

		List<ValidationRule> validationRulesList = new ArrayList<>();

		try {
			validationRulesList
					.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "endUserId", endUserId));
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_CURRENCY,
					"accountCurrency", accountCurrency));
			validationRulesList
					.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "accountStatus", accountStatus));

			ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
			validationRules = validationRulesList.toArray(validationRules);

			Validation.checkRequestParams(validationRules);

		} catch (CustomException ex) {
			LOG.error("###WALLETCONFIG### Error in Validations. ", ex);
			responseWrapper.setRequestError(
					constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			return false;
		}

		return true;
	}

	@Override
	protected Returnable process(AssignAccountInfoConfigRequestWrapper extendedRequestDTO) throws Exception {
		if (responseWrapper.getRequestError() != null) {
			return responseWrapper;
		}
		try {

			AssignAccountInfoConfigRequestBean requestBean = extendedRequestDTO.getRequestBean();

			String endUserId = CommonUtil.extractNumberFromMsisdn(extendedRequestDTO.getEndUserId());
			String currency = CommonUtil.getNullOrTrimmedValue(requestBean.getCurrency());
			String status = CommonUtil.getNullOrTrimmedValue(requestBean.getStatus());
			String activeStatus = AccountStatus.ACTIVE.toString().toLowerCase();
			String suspendedStatus = AccountStatus.SUSPENDED.toString().toLowerCase();
			String terminatedStatus = AccountStatus.TERMINATED.toString().toLowerCase();
			String userName = extendedRequestDTO.getUser().getUserName();
			String accountStatus = status.toLowerCase();
			String accountStatusCapital = WordUtils.capitalize(status);
			// check currency valid currency according to ISO 4217
			boolean isCurrency = currencySymbol(currency);
			if (!isCurrency) {
				LOG.error("###WALLETCONFIG### Invalid currency code");
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "Invalid currency code"));
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapper;
			}
			// check valid account status
			if (!(accountStatus.equals(activeStatus) || accountStatus.equals(suspendedStatus))
					|| accountStatus.equals(terminatedStatus)) {
				LOG.error("###WALLETCONFIG### Invalid status");
				responseWrapper.setRequestError(
						constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE, "Invalid status"));
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapper;
			}

			APITypes apiType = dao.getAPIType(RequestType.WALLET.toString().toLowerCase());
			APIServiceCalls serviceType = dao.getServiceCall(apiType.getId(), ServiceName.BalanceLookup.toString());
			ManageNumber manageNumber = numberDAO.getNumber(endUserId, userName);
			Integer ownerId = manageNumber.getId();
			List<AttributeDistribution> availableDistribution = dao
					.getAttributeDistributionByServiceCall(apiType.getId(), serviceType.getApiServiceCallId());
			valuesInRequset.put("currency", currency);
			valuesInRequset.put("accountStatus", accountStatusCapital);

			for (Map.Entry<String, String> eachMapValue : valuesInRequset.entrySet()) {
				for (AttributeDistribution eachDistribution : availableDistribution) {
					if (eachDistribution.getAttribute().getAttributeName().toLowerCase()
							.equals(eachMapValue.getKey().toLowerCase())) {
						attributeValueDistribution.put(eachDistribution, eachMapValue.getValue());
						break;
					}
				}
			}

			AttributeValues valueObj = new AttributeValues();
			List<AttributeValues> attributeValues = new ArrayList<AttributeValues>();

			for (Map.Entry<AttributeDistribution, String> eachValuedDistribution : attributeValueDistribution
					.entrySet()) {
				valueObj = dao.getAttributeValue(eachValuedDistribution.getKey(), ownerId);

				if (valueObj != null) {

					valueObj.setValue(eachValuedDistribution.getValue());
					attributeValues.add(valueObj);

				} else {

					valueObj = new AttributeValues();
					valueObj.setAttributedid(eachValuedDistribution.getKey());
					valueObj.setOwnerdid(ownerId);
					valueObj.setTobject(TableName.NUMBERS.toString().toLowerCase());
					valueObj.setValue(eachValuedDistribution.getValue());
					attributeValues.add(valueObj);

				}
			}

			dao.saveAttributeValue(attributeValues);
			responseWrapper.setStatus("Successfully Updated attribute for accountInfo!!");
			responseWrapper.setHttpStatus(Response.Status.CREATED);

		} catch (CustomException ex) {
			LOG.error("###WALLETCONFIG### Error Occured in WALLETCONFIG Service. ", ex);
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
			return responseWrapper;
		}
		return responseWrapper;
	}

	private static boolean currencySymbol(@Nonnull final String currencyCode) {
		try {
			final Currency currency = Currency.getInstance(currencyCode);
			return true;
		} catch (final IllegalArgumentException x) {
			return false;
		}
	}

	@Override
	protected void init(AssignAccountInfoConfigRequestWrapper extendedRequestDTO) throws Exception {
		requestWrapper = extendedRequestDTO;
		responseWrapper = new AssignAccountInfoConfigResponseWrapper();

	}
}