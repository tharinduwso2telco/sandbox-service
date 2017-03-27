package com.wso2telco.services.dep.sandbox.servicefactory.wallet;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.wso2telco.services.dep.sandbox.dao.model.custom.*;
import com.wso2telco.services.dep.sandbox.servicefactory.*;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.icu.math.BigDecimal;
import com.wso2telco.core.dbutils.exception.PolicyError;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.LoggingDAO;
import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.WalletDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.MakePaymentRequestBean.makePayment;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Attributes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.CreditStatusCodes;
import com.wso2telco.services.dep.sandbox.util.MessageLogHandler;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import com.wso2telco.services.dep.sandbox.util.TableName;
import org.json.JSONObject;

public class MakePaymentRequestHandler extends AbstractRequestHandler<MakePaymentRequestWrapperDTO> implements RequestResponseRequestHandleable<MakePaymentRequestWrapperDTO> {

	private WalletDAO walletDAO;
	private LoggingDAO loggingDAO;
	private NumberDAO numberDAO;
	private MakePaymentRequestWrapperDTO requestWrapperDTO;
	private MakePaymentResponseWrapper responseWrapper;
	private String serviceCallPayment;
	private String responseReferenceCode;
	{
		LOG = LogFactory.getLog(MakePaymentRequestHandler.class);
		walletDAO = DaoFactory.getWalletDAO();
		loggingDAO = DaoFactory.getLoggingDAO();
		numberDAO = DaoFactory.getNumberDAO();
		dao = DaoFactory.getGenaricDAO();
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
	protected void init(MakePaymentRequestWrapperDTO extendedRequestDTO) throws Exception {
		responseWrapper = new MakePaymentResponseWrapper();
		requestWrapperDTO = extendedRequestDTO;

	}

	@Override
	protected boolean validate(MakePaymentRequestWrapperDTO wrapperDTO) throws Exception {
		MakePaymentRequestBean requestBean = wrapperDTO.getMakePaymentRequestBean();
		makePayment request = requestBean.getmakePayment();
		PaymentAmount paymentAmount = request.getPaymentAmount();
		ChargingInformation chargingInformation = paymentAmount.getChargingInformation();
		ChargingMetaData metaData = paymentAmount.getChargingMetaData();

		String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
		String endUserID = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getEndUserId());
		String msisdn = CommonUtil.getNullOrTrimmedValue(request.getEndUserId());
		String amount = CommonUtil.getNullOrTrimmedValue(String.valueOf(chargingInformation.getAmount()));
		String currency = CommonUtil.getNullOrTrimmedValue(chargingInformation.getCurrency());
		String description = CommonUtil.getNullOrTrimmedValue(chargingInformation.getDescription());
		String onBehalfOf = CommonUtil.getNullOrTrimmedValue(metaData.getOnBehalfOf());
		String categoryCode = CommonUtil.getNullOrTrimmedValue(metaData.getPurchaseCategoryCode());
		String channel = CommonUtil.getNullOrTrimmedValue(metaData.getChannel());
		String referenceCode = CommonUtil.getNullOrTrimmedValue(request.getReferenceCode());
		String notifyURL = CommonUtil.getNullOrTrimmedValue(request.getNotifyURL());

		List<ValidationRule> validationRulesList = new ArrayList<>();

		try {
			validationRulesList.add(
					new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator));
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID,
					"endUserID", endUserID));
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID,
					"endUserID", msisdn));
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "amount", amount));
			validationRulesList
					.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_CURRENCY, "currency", currency));
			validationRulesList
					.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "description", description));
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
			validationRulesList
					.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notifyURL", notifyURL));

			ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
			validationRules = validationRulesList.toArray(validationRules);

			Validation.checkRequestParams(validationRules);

		} catch (CustomException ex) {
			LOG.error("###WALLET### Error in Validations. ", ex);
			responseWrapper.setRequestError(
					constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
			return false;
		}
		return true;
	}

	@Override
	protected Returnable process(MakePaymentRequestWrapperDTO extendedRequestDTO) throws Exception {

		if (responseWrapper.getRequestError() != null) {
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			return responseWrapper;
		}
		try {
			MakePaymentRequestBean requestBean = extendedRequestDTO.getMakePaymentRequestBean();
			makePayment request = requestBean.getmakePayment();
			PaymentAmount paymentAmount = request.getPaymentAmount();
			ChargingInformation chargingInformation = paymentAmount.getChargingInformation();
			ChargingMetaData metadata = paymentAmount.getChargingMetaData();

			String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
			String endUserIdPath = extendedRequestDTO.getEndUserId();
			String endUserIdRequest = request.getEndUserId();
			String endUserId = getLastMobileNumber(endUserIdPath);
			String amount = CommonUtil.getNullOrTrimmedValue(String.valueOf(chargingInformation.getAmount()));
			String currency = CommonUtil.getNullOrTrimmedValue(chargingInformation.getCurrency());
			String description = CommonUtil.getNullOrTrimmedValue(chargingInformation.getDescription());
			String onBehalfOf = CommonUtil.getNullOrTrimmedValue(metadata.getOnBehalfOf());
			String categoryCode = CommonUtil.getNullOrTrimmedValue(metadata.getPurchaseCategoryCode());
			String channel = CommonUtil.getNullOrTrimmedValue(metadata.getChannel());
			String referenceCode = CommonUtil.getNullOrTrimmedValue(request.getReferenceCode());
			String notifyURL = CommonUtil.getNullOrTrimmedValue(request.getNotifyURL());
			serviceCallPayment = ServiceName.MakePayment.toString();
			String userName = extendedRequestDTO.getUser().getUserName();
			Integer userId = extendedRequestDTO.getUser().getId();

			MakePaymentResponseBean responseBean = new MakePaymentResponseBean();
			PaymentAmountResponse payAmount = new PaymentAmountResponse();
			ChargingInformation chargeInformation = new ChargingInformation();
			ChargingMetaData chargeMetaData = new ChargingMetaData();

			// Save Request Log
			APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
            APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCallPayment);
            Gson gson = new Gson();
            int serviceNameId = apiServiceCalls.getApiServiceCallId();

            if (clientCorrelator != null) {

                String response = checkDuplicateClientCorrelator(clientCorrelator, userId, serviceNameId, endUserId,
                        MessageProcessStatus.Success, MessageType.Response, referenceCode);

                if (response != null) {
                    // return already sent response
                    MakePaymentResponseBean obj = null;
                    obj = gson.fromJson(response, MakePaymentResponseBean.class);
                    MakePaymentDTO dto = new MakePaymentDTO();
                    dto.setmakePayment(obj);
                    responseWrapper.setMakePaymentDTO(dto);
                    responseWrapper.setHttpStatus(Response.Status.OK);
                    return responseWrapper;
                }
            }

            //check referenceCode
            String duplicateReferenceCode = checkReferenceCode(userId, serviceNameId, endUserId, MessageProcessStatus
                    .Success, MessageType.Response, referenceCode);

            if ((duplicateReferenceCode != null)) {
                LOG.error("###WALLET### Already used reference code");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Already used reference code"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

			// check path param endUserId and request body endUserId
			if (!(getLastMobileNumber(endUserIdPath).equals(getLastMobileNumber(endUserIdRequest)))) {
				LOG.error("###WALLET### two different endUserId provided");
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "two different endUserId provided"));
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapper;
			}

			// check account valid amount
			if (NumberUtils.isNumber(amount) != true) {
				LOG.error("###WALLET### amount should be a valid positive number");
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "amount should be positive number"));
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
				LOG.error("###WALLET### currency code not valid accorfing to ISO 4217");
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "currency code not valid according to ISO 4217"));
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapper;
			}

			String serviceCallBalanceLookUp = ServiceName.BalanceLookup.toString();
			String accountCurrencyAttribute = AttributeName.Currency.toString().toLowerCase();
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
			// check channel

			if (channel != null && !containsChannel(channel)) {
				LOG.error("###WALLET### Valid channel doesn't exists for the given inputs");
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "Valid channel doesn't exists for the given inputs"));
				responseWrapper.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapper;
			}

			responseBean.setClientCorrelator(clientCorrelator);
			responseBean.setEndUserId(endUserIdPath);

			chargeInformation.setAmount(Double.parseDouble(amount));
			chargeInformation.setCurrency(currency);
			chargeInformation.setDescription(description);

			if (onBehalfOf != null || categoryCode != null || channel != null) {
				chargeMetaData.setPurchaseCategoryCode(categoryCode);
				chargeMetaData.setChannel(channel);
				chargeMetaData.setOnBehalfOf(onBehalfOf);
			}

            // Setting the serverReference Code
            String serverReferenceCodeFormat = String.format("%06d", getReferenceNumber());
            String serverReferenceCode = "WALLET_REF" + serverReferenceCodeFormat;
            responseBean.setServerReferenceCode(serverReferenceCode);

			responseBean.setReferenceCode(referenceCode);
			responseBean.setServerReferenceCode(serverReferenceCode);
			responseBean.setNotifyURL(notifyURL);

			ManageNumber manageNumber = numberDAO.getNumber(endUserId,
					extendedRequestDTO.getUser().getUserName().toString());
			Double balance = manageNumber.getBalance();
			AttributeValues transactionStatusValue = walletDAO.getAttributeValue(endUserId, serviceCallPayment,
					AttributeName.transactionStatus.toString(), userId);

			// transaction operation status as denied
			if ((balance < chargeAmount)) {
				LOG.error("###WALLET### Denied : Account balance insufficient to charge request ");
				responseWrapper.setHttpStatus(Status.FORBIDDEN);
				responseWrapper
						.setRequestError(constructRequestError(POLICYEXCEPTION, PolicyError.NO_VALID_SERVICES_AVAILABLE,
								"Denied : Account balance insufficient to charge request"));
				return responseWrapper;
			}
            // set transaction operation status as refused
            else if (transactionStatusValue != null) {
                String transactionStatus = transactionStatusValue.getValue();
                if (transactionStatus.equals(TransactionStatus.Refused.toString())) {
                    responseBean.setTransactionOperationStatus(TransactionStatus.Refused.toString());
                }

            } else if (balance >= chargeAmount) {
				balance = balance - chargeAmount;
				manageNumber.setBalance(balance);
				numberDAO.saveManageNumbers(manageNumber);
				responseBean.setTransactionOperationStatus(TransactionStatus.Charged.toString());
			}

			MakePaymentDTO makePaymentDTO = new MakePaymentDTO();
			payAmount.setChargingInformation(chargeInformation);
			if (onBehalfOf != null || categoryCode != null || channel != null) {
				payAmount.setChargingMetaData(chargeMetaData);
			}
			responseBean.setPaymentAmount(payAmount);
			makePaymentDTO.setmakePayment(responseBean);
			responseWrapper.setMakePaymentDTO(makePaymentDTO);
			responseWrapper.setHttpStatus(Response.Status.CREATED);

            // Save Success Response
            saveResponse(extendedRequestDTO, endUserId, responseBean, apiServiceCalls, MessageProcessStatus.Success);

		} catch (Exception ex) {
			LOG.error("###WALLET### Error Occured in WALLET Service. ", ex);
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
		}
		return responseWrapper;
	}

	private boolean containsChannel(String channelValue) {

		for (Channel channel : Channel.values()) {
			if (channel.name().toLowerCase().equals(channelValue.toLowerCase())) {
				return true;
			}
		}

		return false;
	}


    // Check already existing clientcorrelator return response body
    private String checkDuplicateClientCorrelator(String clientCorrelator, int userId, int serviceNameId, String tel,
                                                  MessageProcessStatus status, MessageType type, String
                                                          referenceCode) throws Exception {
        List<Integer> list = new ArrayList<>();
        list.add(serviceNameId);
        List<MessageLog> response = loggingDAO.getMessageLogs(userId, list, "msisdn", "tel:+" + tel, null, null);

        String jsonString = null;

        for (int i = 0; i < response.size(); i++) {

            if (response != null) {

                int responseStatus = response.get(i).getStatus();
                int responseType = response.get(i).getType();
                String responseClientCorrelator;

                if (responseType == type.getValue() && responseStatus == status.getValue()) {
                    String request = response.get(i).getRequest();
                    JSONObject json = new JSONObject(request);
                    responseClientCorrelator = null;

                    if (json.has("clientCorrelator")) {
                        responseClientCorrelator = json.get("clientCorrelator").toString();
                    }

                    responseReferenceCode = json.get("referenceCode").toString();

                    int responseUserId = response.get(i).getUserid();
                    String responseTel = response.get(i).getValue();

                    // Check client correlator
                    if ((responseClientCorrelator != null && responseClientCorrelator.equals(clientCorrelator)) &&
                            responseUserId == userId && responseTel.equals("tel:+" + tel)) {
                        jsonString = json.toString();
                        break;
                    }
                }

            }

        }

        return jsonString;
    }

    //check reference code
    private String checkReferenceCode(int userId, int serviceNameId, String tel, MessageProcessStatus status,
                                      MessageType type, String referenceCode) throws Exception {

        List<Integer> list = new ArrayList<>();
        list.add(serviceNameId);
        List<MessageLog> response = loggingDAO.getMessageLogs(userId, list, "msisdn", "tel:+" + tel, null, null);

        String jsonString = null;

        for (int i = 0; i < response.size(); i++) {

            if (response != null) {

                int responseStatus = response.get(i).getStatus();
                int responseType = response.get(i).getType();

                if (responseType == type.getValue() && responseStatus == status.getValue()) {
                    String request = response.get(i).getRequest();
                    JSONObject json = new JSONObject(request);
                    responseReferenceCode = json.get("referenceCode").toString();

                    int responseUserId = response.get(i).getUserid();
                    String responseTel = response.get(i).getValue();

                    // Check reference CodeDuplication
                    if (responseUserId == userId && responseTel.equals("tel:+" + tel) && responseReferenceCode.equals
                            (referenceCode)) {
                        jsonString = responseReferenceCode;
                        break;
                    }
                }

            }

        }

        return jsonString;
    }

    // Save Response in messageLog table
    private void saveResponse(MakePaymentRequestWrapperDTO extendedRequestDTO,
                              String endUserIdPath, MakePaymentResponseBean responseBean, APIServiceCalls
                                      apiServiceCalls, MessageProcessStatus status) throws Exception {

        String jsonInString = null;
        Gson resp = new Gson();

        JsonElement jsonElement = new JsonParser().parse(resp.toJson(responseBean));
        JsonObject asJsonObject = jsonElement.getAsJsonObject();
        jsonInString = asJsonObject.toString();

        //setting messagelog responses
        MessageLog messageLog = new MessageLog();
        messageLog = new MessageLog();
        messageLog.setRequest(jsonInString);
        messageLog.setStatus(status.getValue());
        messageLog.setType(MessageType.Response.getValue());
        messageLog.setServicenameid(apiServiceCalls.getApiServiceCallId());
        messageLog.setUserid(extendedRequestDTO.getUser().getId());
        messageLog.setReference("msisdn");
        messageLog.setValue("tel:+"+endUserIdPath);
        messageLog.setMessageTimestamp(new Date());

        loggingDAO.saveMessageLog(messageLog);
    }

    @Override
    public String getApiServiceCalls() {
        return ServiceName.MakePayment.toString();

    }

    @Override
    public String getJosonString(MakePaymentRequestWrapperDTO requestDTO) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(requestDTO.getMakePaymentRequestBean());
        return jsonString;
    }

    @Override
    public String getnumber(MakePaymentRequestWrapperDTO requestDTO) throws Exception {
        return getLastMobileNumber(requestDTO.getEndUserId());
    }
}