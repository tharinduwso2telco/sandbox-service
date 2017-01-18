package com.wso2telco.services.dep.sandbox.servicefactory.wallet;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.icu.math.BigDecimal;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.LoggingDAO;
import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.WalletDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ChargingInformation;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ChargingMetaData;
import com.wso2telco.services.dep.sandbox.dao.model.custom.MakePaymentDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.MakePaymentResponseBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.PaymentAmount;
import com.wso2telco.services.dep.sandbox.dao.model.custom.PaymentAmountResponse;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundTransactionDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundTransactionRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundTransactionRequestBean.RefundTransaction;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundTransactionResponseBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundResponseBean;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Attributes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.AttributeName;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.MessageLogHandler;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import com.wso2telco.services.dep.sandbox.util.TableName;

public class RefundTransactionRequestHandler extends AbstractRequestHandler<RefundRequestWrapperDTO> {

	private WalletDAO walletDAO;
	private LoggingDAO loggingDAO;
	private NumberDAO numberDAO;
	private RefundRequestWrapperDTO requestWrapperDTO;
	private RefundTransactionResponseWrapper responseWrapper;
	private MessageLogHandler logHandler;
	private String serviceCallRefund;
	private boolean isContainsMetaData;
	private Integer transactionId;

	{
		LOG = LogFactory.getLog(RefundTransactionRequestHandler.class);
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
		address.add(requestWrapperDTO.getEndUserId());
		return address;
	}

	@Override
	protected void init(RefundRequestWrapperDTO extendedRequestDTO) throws Exception {
		responseWrapper = new RefundTransactionResponseWrapper();
		requestWrapperDTO = extendedRequestDTO;
	}

	@Override
	protected boolean validate(RefundRequestWrapperDTO wrapperDTO) throws Exception {
		RefundTransactionRequestBean requestBean = wrapperDTO.getRefundRequestBean();
		RefundTransaction request = requestBean.getRefundTransaction();
		PaymentAmount paymentAmount = request.getPaymentAmount();

		ChargingInformation chargingInformation = paymentAmount.getChargingInformation();
		ChargingMetaData metaData = paymentAmount.getChargingMetaData();

		String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
		String endUserID = CommonUtil.getNullOrTrimmedValue(request.getEndUserId());
		String originalReferenceCode = CommonUtil.getNullOrTrimmedValue(request.getOriginalReferenceCode());
		String originalServerReferenceCode = CommonUtil.getNullOrTrimmedValue(request.getOriginalServerReferenceCode());
		String amount = CommonUtil.getNullOrTrimmedValue(chargingInformation.getAmount());
		String currency = CommonUtil.getNullOrTrimmedValue(chargingInformation.getCurrency());
		String decsription = CommonUtil.getNullOrTrimmedValue(chargingInformation.getDescription());
		String onBehalfOf = CommonUtil.getNullOrTrimmedValue(metaData.getOnBehalfOf());
		String categoryCode = CommonUtil.getNullOrTrimmedValue(metaData.getPurchaseCategoryCode());
		String channel = CommonUtil.getNullOrTrimmedValue(metaData.getChannel());
		String referenceCode = CommonUtil.getNullOrTrimmedValue(request.getReferenceCode());

		List<ValidationRule> validationRulesList = new ArrayList<>();

		try {
			validationRulesList.add(
					new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator));
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID,
					"endUserID", endUserID));
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
					"originalReferenceCode", originalReferenceCode));
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
					"originalServerReferenceCode", originalServerReferenceCode));
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "amount", amount));
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "currency", currency));
			validationRulesList
					.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "decsription", decsription));

			if (metaData != null) {
				validationRulesList
						.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf", onBehalfOf));
				validationRulesList
						.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "categoryCode", categoryCode));
				validationRulesList
						.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "channel", channel));
			}

			validationRulesList
					.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "referenceCode", referenceCode));

			ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
			validationRules = validationRulesList.toArray(validationRules);

			Validation.checkRequestParams(validationRules);
		} catch (CustomException ex) {
			LOG.error("###WALLET### Error in Validation : " + ex);
			responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(),
					wrapperDTO.getEndUserId()));
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
		}
		return true;
	}

	@Override
	protected Returnable process(RefundRequestWrapperDTO extendedRequestDTO) throws Exception {
		if (responseWrapper.getRequestError() != null) {
			return responseWrapper;
		}
		try {
			RefundTransactionRequestBean requestBean = extendedRequestDTO.getRefundRequestBean();
			RefundTransaction request = requestBean.getRefundTransaction();
			PaymentAmount paymentAmount = request.getPaymentAmount();
			ChargingInformation chargingInformation = paymentAmount.getChargingInformation();
			ChargingMetaData metadata = paymentAmount.getChargingMetaData();

			String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
			String endUserIdPath = extendedRequestDTO.getEndUserId();
			String endUserIdRequest = request.getEndUserId();
			String endUserId = getLastMobileNumber(endUserIdPath);
			String originalReferenceCode = CommonUtil.getNullOrTrimmedValue(request.getOriginalReferenceCode());
			String originalServerReferenceCode = CommonUtil
					.getNullOrTrimmedValue(request.getOriginalServerReferenceCode());
			String amount = CommonUtil.getNullOrTrimmedValue(chargingInformation.getAmount());
			String currency = CommonUtil.getNullOrTrimmedValue(chargingInformation.getCurrency());
			String description = CommonUtil.getNullOrTrimmedValue(chargingInformation.getDescription());
			String onBehalfOf = CommonUtil.getNullOrTrimmedValue(metadata.getOnBehalfOf());
			String categoryCode = CommonUtil.getNullOrTrimmedValue(metadata.getPurchaseCategoryCode());
			String channel = CommonUtil.getNullOrTrimmedValue(metadata.getChannel());
			String referenceCode = CommonUtil.getNullOrTrimmedValue(request.getReferenceCode());
			serviceCallRefund = ServiceName.RefundPayment.toString();
			String accountCurrencyAttribute = AttributeName.Currency.toString().toLowerCase();
			String serviceCallBalanceLookUp = ServiceName.BalanceLookup.toString();
			String userName = extendedRequestDTO.getUser().getUserName();
			Integer userId = extendedRequestDTO.getUser().getId();

			// Save Request Log
			APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
			APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCallRefund);

			Gson gson = new Gson();
			String jsonString = gson.toJson(requestBean);
			MessageLog messageLog = new MessageLog();
			messageLog.setServicenameid(apiServiceCalls.getApiServiceCallId());
			messageLog.setUserid(extendedRequestDTO.getUser().getId());
			messageLog.setReference("msisdn");
			messageLog.setValue(endUserIdPath);
			messageLog.setRequest(jsonString);
			messageLog.setMessageTimestamp(new Date());

			int ref_number = loggingDAO.saveMessageLog(messageLog);
			String serverReferenceCodeFormat = String.format("%06d", ref_number);
			String serverReferenceCode = "WALLET_REF_" + serverReferenceCodeFormat;

			// check already charge request against client correlator
			if (clientCorrelator != null) {
				String clientCorrelatorAttribute = AttributeName.clientCorrelatorWallet.toString();
				String tableAttributeValue = TableName.SBXATTRIBUTEVALUE.toString().toLowerCase();
				AttributeValues duplicateClientCorrelator = walletDAO.checkDuplicateValue(serviceCallRefund,
						clientCorrelator, clientCorrelatorAttribute, tableAttributeValue);
				if (duplicateClientCorrelator != null) {
					APIServiceCalls apiServiceCall = duplicateClientCorrelator.getAttributedid().getAPIServiceCall();
					String serviceCall = apiServiceCall.getServiceName();
					ManageNumber manageNumber = numberDAO.getNumber(endUserId, userName);
					Integer id = duplicateClientCorrelator.getOwnerdid();
					AttributeValues response = walletDAO.getResponse(id);
					if (serviceCall.equals(serviceCallRefund) && (response.getOwnerdid() == manageNumber.getId())) {
						RefundTransactionResponseBean obj = null;
						obj = gson.fromJson(response.getValue(), RefundTransactionResponseBean.class);
						RefundTransactionDTO dto = new RefundTransactionDTO();
						dto.setRefundTransaction(obj);
						responseWrapper.setRefundTransactionDTO(dto);
						responseWrapper.setHttpStatus(Response.Status.OK);
						return responseWrapper;
					} else {
						responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
								ServiceError.INVALID_INPUT_VALUE, "Clientcorrelator is already used"));
						responseWrapper.setHttpStatus(Status.BAD_REQUEST);
						return responseWrapper;
					}
				}
			}

			// check path param endUserId and request body endUserId
			if (!(endUserIdPath.equals(endUserIdRequest))) {
				LOG.error("###WALLET### two different endUserId provided");
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "two different endUserId provided"));
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapper;
			}

			// check valid amount format
			if ((NumberUtils.isNumber(amount) != true)) {
				LOG.error("###WALLET### amount should be a valid number");
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "amount should be a valid number"));
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapper;
			}
			// check account amount decimal format
			Double chargeAmount = Double.parseDouble(amount);
			BigDecimal bigDecimal = new BigDecimal(amount);
			Integer decimalDigits = bigDecimal.scale();
			if (!((decimalDigits <= 2) && (decimalDigits >= 0)) || chargeAmount < 0) {
				LOG.error("###WALLET### amount should be a whole number or two digit decimal");
				responseWrapper
						.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
								"amount should be a whole or two digit decimal positive number"));
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapper;
			}

			// check valid account currency for endUserId
			boolean isValidCurrency = currencySymbol(currency);
			if (!isValidCurrency) {
				LOG.error("###WALLET### currency code not as per ISO 4217");
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "currency code not as per ISO 4217"));
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapper;

			}
			AttributeValues accountCurrencyValue = walletDAO.getAttributeValue(endUserId, serviceCallBalanceLookUp,
					accountCurrencyAttribute, userId);
			if (accountCurrencyValue != null) {
				String accountCurrency = accountCurrencyValue.getValue();
				if (!(currency.equals(accountCurrency))) {
					LOG.error("###WALLET### Valid currecy doesn't exists for the given inputs");
					responseWrapper
							.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
									"Valid currency does not exist for the given input parameters"));
					responseWrapper.setHttpStatus(Status.BAD_REQUEST);
					return responseWrapper;
				}
			}

			String referenceCodeAttribute = AttributeName.referenceCodeWallet.toString();
			String tableNumber = TableName.NUMBERS.toString().toLowerCase();
			AttributeValues duplicateReferenceCode = walletDAO.checkDuplicateValue(serviceCallRefund, referenceCode,
					referenceCodeAttribute, tableNumber);
			if (duplicateReferenceCode != null) {
				LOG.error("###WALLET### Already charged for this reference code");
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "Already charged for this reference code"));
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapper;
			}

			RefundTransactionResponseBean responseBean = new RefundTransactionResponseBean();
			PaymentAmountResponse payAmount = new PaymentAmountResponse();
			ChargingInformation chargeInformation = new ChargingInformation();
			ChargingMetaData chargeMetaData = new ChargingMetaData();

			responseBean.setClientCorrelator(clientCorrelator);
			responseBean.setEndUserId(endUserIdPath);
			responseBean.setOriginalReferenceCode(originalReferenceCode);
			responseBean.setOriginalServerReferenceCode(originalServerReferenceCode);

			chargeInformation.setAmount(amount);
			chargeInformation.setCurrency(currency);
			chargeInformation.setDescription(description);

			if (onBehalfOf != null || categoryCode != null || channel != null) {
				chargeMetaData.setPurchaseCategoryCode(categoryCode);
				chargeMetaData.setChannel(channel);
				chargeMetaData.setOnBehalfOf(onBehalfOf);
				isContainsMetaData = true;
			}

			responseBean.setReferenceCode(referenceCode);
			responseBean.setServerReferenceCode(serverReferenceCode);
			responseBean.setResourceURL(CommonUtil.getResourceUrl(extendedRequestDTO));

			// set transaction operation status as charged
			ManageNumber manageNumber = numberDAO.getNumber(endUserId, extendedRequestDTO.getUser().getUserName());
			Double updateBalance = manageNumber.getBalance() + chargeAmount;
			AttributeValues accountStatusValue = walletDAO.getAttributeValue(endUserId, serviceCallRefund,
					AttributeName.transactionStatus.toString(), userId);
			// set transaction operation status as refused
			manageNumber.setBalance(updateBalance);

			if (accountStatusValue != null) {
				String accountStatus = accountStatusValue.getValue();
				// set transaction operation status as Refused
				if (accountStatus.equals(TransactionStatus.Refused.toString())) {
					responseBean.setTransactionOperationStatus(TransactionStatus.Refused.toString());

				}
			}
			// set transaction operation status as Refunded
			else if (walletDAO.saveManageNumbers(manageNumber)) {
				responseBean.setTransactionOperationStatus(TransactionStatus.Refunded.toString());
			}

			responseWrapper.setHttpStatus(Response.Status.OK);
			RefundTransactionDTO refundPaymentDTO = new RefundTransactionDTO();
			payAmount.setChargingInformation(chargeInformation);
			payAmount.setChargingMetaData(chargeMetaData);
			responseBean.setPaymentAmount(payAmount);
			refundPaymentDTO.setRefundTransaction(responseBean);
			responseWrapper.setRefundTransactionDTO(refundPaymentDTO);

			// save transaction
			transactionId = saveTransaction(responseBean, endUserId, userName);

			// save client correlator
			if (clientCorrelator != null) {
				saveClientCorrelator(endUserId, clientCorrelator, userName);
			}
			// save client correlator
			saveReferenceCode(endUserId, referenceCode, userName);
		} catch (Exception ex) {
			LOG.error("###WALLET### Error Occured in WALLET Service. " + ex);
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
		}
		return responseWrapper;
	}

	public Integer saveTransaction(RefundTransactionResponseBean responseBean, String endUserId, String userName)
			throws Exception {
		Integer transactionId = null;
		try {
			AttributeValues valueObj = new AttributeValues();
			String tableName = TableName.NUMBERS.toString().toLowerCase();
			String attributeName = AttributeName.Refund.toString().toLowerCase();
			APITypes api = dao.getAPIType(RequestType.WALLET.toString());
			APIServiceCalls call = dao.getServiceCall(api.getId(), serviceCallRefund);
			Attributes attributes = dao.getAttribute(attributeName);
			AttributeDistribution dis = dao.getAttributeDistribution(call.getApiServiceCallId(),
					attributes.getAttributeId());
			ManageNumber manageNumber = numberDAO.getNumber(endUserId, userName);
			Integer ownerId = manageNumber.getId();
			String jsonInString = null;
			Gson gson = new Gson();

			JsonElement je = new JsonParser().parse(gson.toJson(responseBean));
			JsonObject asJsonObject = je.getAsJsonObject();
			jsonInString = asJsonObject.toString();

			valueObj = new AttributeValues();
			valueObj.setAttributedid(dis);
			valueObj.setOwnerdid(ownerId);
			valueObj.setTobject(tableName);
			valueObj.setValue(jsonInString);
			transactionId = walletDAO.saveAttributeValue(valueObj);

		} catch (Exception ex) {
			LOG.error("###WALLET### Error in processing save transaction. ", ex);
			responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
		}
		return transactionId;
	}

	public void saveClientCorrelator(String endUserId, String clientCorrelator, String userName) throws Exception {
		Integer ownerId = null;
		try {
			AttributeValues valueObj = new AttributeValues();
			String tableName = TableName.SBXATTRIBUTEVALUE.toString().toLowerCase();
			String attributeName = AttributeName.clientCorrelatorWallet.toString();
			APITypes api = dao.getAPIType(RequestType.WALLET.toString());
			APIServiceCalls call = dao.getServiceCall(api.getId(), serviceCallRefund);
			Attributes attributes = dao.getAttribute(attributeName);
			AttributeDistribution dis = dao.getAttributeDistribution(call.getApiServiceCallId(),
					attributes.getAttributeId());
			ownerId = transactionId;

			valueObj = new AttributeValues();
			valueObj.setAttributedid(dis);
			valueObj.setOwnerdid(ownerId);
			valueObj.setTobject(tableName);
			valueObj.setValue(clientCorrelator);
			dao.saveAttributeValue(valueObj);

		} catch (Exception ex) {
			LOG.error("###WALLET### Error in processing save insertion of clientCorrelator request. ", ex);
			responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
		}
	}

	private static boolean currencySymbol(@Nonnull final String currencyCode) {
		try {
			final Currency currency = Currency.getInstance(currencyCode);
			return true;
		} catch (final IllegalArgumentException x) {
			return false;
		}
	}

	public void saveReferenceCode(String endUserId, String referenceCode, String userName) throws Exception {
		try {
			AttributeValues valueObj = new AttributeValues();
			String tableName = TableName.NUMBERS.toString().toLowerCase();
			String attributeName = AttributeName.referenceCodeWallet.toString();
			APITypes api = dao.getAPIType(RequestType.WALLET.toString());
			APIServiceCalls call = dao.getServiceCall(api.getId(), serviceCallRefund);
			Attributes attributes = dao.getAttribute(attributeName);
			AttributeDistribution dis = dao.getAttributeDistribution(call.getApiServiceCallId(),
					attributes.getAttributeId());
			ManageNumber manageNumber = numberDAO.getNumber(endUserId, userName);
			Integer ownerId = manageNumber.getId();

			valueObj = new AttributeValues();
			valueObj.setAttributedid(dis);
			valueObj.setOwnerdid(ownerId);
			valueObj.setTobject(tableName);
			valueObj.setValue(referenceCode);
			dao.saveAttributeValue(valueObj);

		} catch (Exception ex) {
			LOG.error("###WALLET### Error in processing save of referenceCode request. ", ex);
			responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
		}
	}
}