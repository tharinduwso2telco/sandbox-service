package com.wso2telco.services.dep.sandbox.servicefactory.wallet;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.wso2telco.services.dep.sandbox.dao.WalletDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ChargingInformation;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ChargingMetaData;
import com.wso2telco.services.dep.sandbox.dao.model.custom.PaymentAmount;
import com.wso2telco.services.dep.sandbox.dao.model.custom.PaymentAmountResponse;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundTransactionDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundTransactionRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundTransactionRequestBean.RefundTransaction;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundTransactionResponseBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.AttributeName;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.CreditStatusCodes;
import com.wso2telco.services.dep.sandbox.util.MessageLogHandler;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import com.wso2telco.services.dep.sandbox.util.TableName;

public class RefundTransactionRequestHandler extends AbstractRequestHandler<RefundRequestWrapperDTO> {

	private WalletDAO walletDAO;
	private LoggingDAO loggingDao;
	private RefundRequestWrapperDTO requestWrapperDTO;
	private RefundTransactionResponseWrapper responseWrapper;
	private MessageLogHandler logHandler;
	private String serviceCallRefund;
	private boolean isContainsMetaData;
//	public static final String serverReferenceCode = "SERVER0002";

	{
		LOG = LogFactory.getLog(RefundTransactionRequestHandler.class);
		walletDAO = DaoFactory.getWalletDAO();
		loggingDao = DaoFactory.getLoggingDAO();
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
			LOG.error("###WALLET### Error in validations", ex);
			String errorMessage = "";
			if (ex.getErrvar() != null && ex.getErrvar().length > 0) {
				errorMessage = ex.getErrvar()[0];
			}
			responseWrapper.setRequestError(
					constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), errorMessage));
		} catch (Exception e) {
			LOG.error("###WALLET### Error in validations", e);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
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
			String accountCurrency = AttributeName.Currency.toString().toLowerCase();
			String serviceCallBalanceLookUp = ServiceName.BalanceLookup.toString();

			// Save Request Log
			APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
			APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCallRefund);

			Gson gson = new Gson();
			JsonElement je = new JsonParser().parse(gson.toJson(requestBean));
			JsonObject asJsonObject = je.getAsJsonObject();
			String jsonString = gson.toJson(asJsonObject);
			MessageLog messageLog = new MessageLog();
			messageLog.setServicenameid(apiServiceCalls.getApiServiceCallId());
	    	messageLog.setUserid(extendedRequestDTO.getUser().getId());
	    	messageLog.setReference("msisdn");
	    	messageLog.setValue(endUserIdPath);
	    	messageLog.setRequest(jsonString);
	    	messageLog.setMessageTimestamp(new Date());

			int ref_number = loggingDao.saveMessageLog(messageLog);
			String serverReferenceCodeFormat = String.format("%06d",ref_number );
			String serverReferenceCode = "WALLET_REF_" + serverReferenceCodeFormat;

			// check path param endUserId and request body endUserId
			if (!(endUserIdPath.equals(endUserIdRequest))) {
				LOG.error("###WALLET### two different endUserId provided");
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "two different endUserId provided"));
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapper;
			}

			//check valid amount format
			if ((NumberUtils.isNumber(amount) != true) ){
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
			if (!((decimalDigits <= 2) && (decimalDigits >= 0)) || chargeAmount < 0){
				LOG.error("###WALLET### amount should be a whole number or two digit decimal");
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "amount should be a whole or two digit decimal positive number"));
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
			String accountCurrencyValue = walletDAO.getAttributeValue(endUserId, serviceCallBalanceLookUp,
					accountCurrency);
			if (accountCurrencyValue != null && !(currency.equals(accountCurrencyValue))) {
				LOG.error("###WALLET### Valid currecy doesn't exists for the given inputs");
				responseWrapper
						.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
								"Valid currency does not exist for the given input parameters"));
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapper;
			}

			// check already charge request against client correlator
			if (clientCorrelator != null) {
				boolean isDuplicateClientCorrelator = walletDAO.checkDuplicateValue(endUserId, serviceCallRefund, clientCorrelator);
				if (!isDuplicateClientCorrelator) {
					// save Client Correlator
					saveClientCorrelator(endUserId, clientCorrelator);
				} else {
					LOG.error("###WALLET### Already charged for this client correlator");
					responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
							ServiceError.INVALID_INPUT_VALUE, "Already charged for this client correlator"));
					responseWrapper.setHttpStatus(Status.BAD_REQUEST);
					return responseWrapper;
				}
			}
			
			//check already charged request against reference code
			boolean isDuplicateReferenceCode = walletDAO.checkDuplicateValue(endUserId, serviceCallRefund, referenceCode);
			if(isDuplicateReferenceCode != false){
				LOG.error("###WALLET### Already charged for this reference code");
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "Already charged for this reference code"));
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapper;
			}else{
				saveReferenceCode(endUserId, referenceCode);
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
			Double balance = walletDAO.checkBalance(endUserId);
			Double updateBalance = balance + chargeAmount;
			String accountStatus = walletDAO.getAttributeValue(endUserId, serviceCallRefund,
					AttributeName.transactionStatus.toString());

			// set transaction operation status as refused
			if (accountStatus != null && accountStatus.equals(TransactionStatus.Refused.toString())) {
				responseBean.setTransactionOperationStatus(TransactionStatus.Refused.toString());

				// set transaction operation status as refused
			} else if (walletDAO.updateBalance(endUserId, updateBalance)) {
				responseBean.setTransactionOperationStatus(TransactionStatus.Refunded.toString());
			}
			/*
			 * else { LOG.error("###WALLET### Error Occured in WALLET Service. "
			 * ); responseWrapper.setHttpStatus(Status.FORBIDDEN);
			 * responseWrapper
			 * .setRequestError(constructRequestError(POLICYEXCEPTION,
			 * PolicyError.NO_VALID_SERVICES_AVAILABLE, "Security Isuue"));
			 * return responseWrapper; }
			 */

			responseWrapper.setHttpStatus(Response.Status.OK);
			RefundTransactionDTO refundPaymentDTO = new RefundTransactionDTO();
			payAmount.setChargingInformation(chargeInformation);
			payAmount.setChargingMetaData(chargeMetaData);
			responseBean.setPaymentAmount(payAmount);
			refundPaymentDTO.setRefundTransaction(responseBean);
			responseWrapper.setRefundTransactionDTO(refundPaymentDTO);

			saveTransaction(responseBean, endUserId, isContainsMetaData);

		} catch (Exception ex) {
			LOG.error("###WALLET### Error Occured in WALLET Service. " + ex);
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
		}
		return responseWrapper;
	}

	public void saveTransaction(RefundTransactionResponseBean responseBean, String endUserId, boolean metaData)
			throws Exception {
		AttributeDistribution distributionId = null;
		Integer ownerId = null;
		try {
			AttributeValues valueObj = new AttributeValues();
			String tableName = TableName.NUMBERS.toString().toLowerCase();
			String serviceCall = ServiceName.RefundPayment.toString();
			String attributeName = AttributeName.Refund.toString().toLowerCase();
			distributionId = walletDAO.getDistributionValue(serviceCall, attributeName, RequestType.WALLET.toString());
			ownerId = walletDAO.getNumber(endUserId);

			String jsonInString = null;
			Gson gson = new Gson();

			JsonElement je = new JsonParser().parse(gson.toJson(responseBean));
			JsonObject asJsonObject = je.getAsJsonObject();
			if (isContainsMetaData) {
				JsonElement get = asJsonObject.get("paymentAmount");
				JsonObject asJsonObjectPayment = get.getAsJsonObject();
				asJsonObjectPayment.remove("chargingMetaData");
			}
			asJsonObject.remove("clientCorrelator");
			asJsonObject.remove("“originalReferenceCode”");
			asJsonObject.remove("“originalServReferenceCode”");
			asJsonObject.remove("resourceURL");
			jsonInString = asJsonObject.toString();

			valueObj = new AttributeValues();
			valueObj.setAttributedid(distributionId);
			valueObj.setOwnerdid(ownerId);
			valueObj.setTobject(tableName);
			valueObj.setValue(jsonInString);
			dao.saveAttributeValue(valueObj);

		} catch (Exception ex) {
			LOG.error("###WALLET### Error in processing attribute insertion service request. ", ex);
			responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
		}
	}

	public void saveClientCorrelator(String endUserId, String clientCorrelator) throws Exception {
		AttributeDistribution distributionId = null;
		Integer ownerId = null;
		try {
			AttributeValues valueObj = new AttributeValues();
			String tableName = TableName.NUMBERS.toString().toLowerCase();
			String attributeName = AttributeName.clientCorrelator.toString();
			String serviceCall = ServiceName.RefundPayment.toString();
			distributionId = walletDAO.getDistributionValue(serviceCall, attributeName, RequestType.WALLET.toString());
			ownerId = walletDAO.getNumber(endUserId);

			valueObj = new AttributeValues();
			valueObj.setAttributedid(distributionId);
			valueObj.setOwnerdid(ownerId);
			valueObj.setTobject(tableName);
			valueObj.setValue(clientCorrelator);
			dao.saveAttributeValue(valueObj);

		} catch (Exception ex) {
			LOG.error("###WALLET### Error in processing attribute save client correlator. ", ex);
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
	public void saveReferenceCode(String endUserId, String referenceCode) throws Exception {
		AttributeDistribution distributionId = null;
		Integer ownerId = null;
		try {
			AttributeValues valueObj = new AttributeValues();
			String tableName = TableName.NUMBERS.toString().toLowerCase();
			String attributeName = AttributeName.referenceCodeWallet.toString();
			String apiType = RequestType.WALLET.toString();
			String serviceCallRefund = ServiceName.RefundPayment.toString();
			distributionId = walletDAO.getDistributionValue(serviceCallRefund, attributeName, apiType);
			ownerId = walletDAO.getNumber(endUserId);

			valueObj = new AttributeValues();
			valueObj.setAttributedid(distributionId);
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
