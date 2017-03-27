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
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.LoggingDAO;
import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.WalletDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundTransactionRequestBean.RefundTransaction;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Attributes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.AttributeName;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.MessageLogHandler;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import com.wso2telco.services.dep.sandbox.util.TableName;
import org.json.JSONObject;

public class RefundTransactionRequestHandler extends AbstractRequestHandler<RefundRequestWrapperDTO> implements RequestResponseRequestHandleable<RefundRequestWrapperDTO>{

	private WalletDAO walletDAO;
	private LoggingDAO loggingDAO;
	private NumberDAO numberDAO;
	private RefundRequestWrapperDTO requestWrapperDTO;
	private RefundTransactionResponseWrapper responseWrapper;
	private MessageLogHandler logHandler;
	private String serviceCallRefund;
	private boolean isContainsMetaData;
    private String responseReferenceCode;
    private boolean isOriginalServerReferenceCode;
    private double totalAmountRefunded;

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
		String msisdn = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getEndUserId());
		String endUserID = CommonUtil.getNullOrTrimmedValue(request.getEndUserId());
		String originalReferenceCode = CommonUtil.getNullOrTrimmedValue(request.getOriginalReferenceCode());
		String originalServerReferenceCode = CommonUtil.getNullOrTrimmedValue(request.getOriginalServerReferenceCode());
		String amount = CommonUtil.getNullOrTrimmedValue(String.valueOf(chargingInformation.getAmount()));
		String currency = CommonUtil.getNullOrTrimmedValue(chargingInformation.getCurrency());
		String description = CommonUtil.getNullOrTrimmedValue(chargingInformation.getDescription());
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
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID,
					"endUserID", msisdn));
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
					"originalReferenceCode", originalReferenceCode));
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
					"originalServerReferenceCode", originalServerReferenceCode));
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "amount", amount));
			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "currency", currency));
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
	protected Returnable process(RefundRequestWrapperDTO extendedRequestDTO) throws Exception {
		if (responseWrapper.getRequestError() != null) {
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
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
			String amount = CommonUtil.getNullOrTrimmedValue(String.valueOf(chargingInformation.getAmount()));
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
            int serviceNameId = apiServiceCalls.getApiServiceCallId();

			if (clientCorrelator != null) {

				String response = checkDuplicateClientCorrelator(clientCorrelator, userId, serviceNameId, endUserId,
						MessageProcessStatus.Success, MessageType.Response, referenceCode);

				if (response != null) {

					// return already send response
					RefundTransactionResponseBean obj = null;
					obj = gson.fromJson(response, RefundTransactionResponseBean.class);
					RefundTransactionDTO dto = new RefundTransactionDTO();
					dto.setRefundTransaction(obj);
					responseWrapper.setRefundTransactionDTO(dto);
					responseWrapper.setHttpStatus(Response.Status.OK);
					return responseWrapper;
				}
			}

			//check referenceCode
			String result = checkReferenceCode(userId, serviceNameId, endUserId, MessageProcessStatus.Success,
					MessageType.Response, referenceCode);

			if ((result != null)) {
				LOG.error("###PAYMENT### Already used reference code");
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "Already used reference code"));
				responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
				return responseWrapper;
			}

			APIServiceCalls apiServiceCallForMakePayment = dao.getServiceCall(apiTypes.getId(), ServiceName
					.MakePayment.toString());

			int serviceIdForMakePayment = apiServiceCallForMakePayment.getApiServiceCallId();

			Double validTransaction = checkOriginalServerReferenceWithServerReference(userId, serviceIdForMakePayment,
					endUserId, MessageProcessStatus.Success, MessageType.Response, originalServerReferenceCode,
					originalReferenceCode);

            AttributeValues accountStatusValue = walletDAO.getAttributeValue(endUserId, serviceCallRefund,
                    AttributeName.transactionStatus.toString(), userId);


			// check path param endUserId and request body endUserId
			if (!(getLastMobileNumber(endUserIdPath).equals(getLastMobileNumber(endUserIdRequest)))) {
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
			// check channel
			if (channel != null && !containsChannel(channel)) {
				LOG.error("###WALLET### Valid channel doesn't exists for the given inputs");
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "Valid channel doesn't exists for the given inputs"));
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

			chargeInformation.setAmount(Double.parseDouble(amount));
			chargeInformation.setCurrency(currency);
			chargeInformation.setDescription(description);

			if (onBehalfOf != null || categoryCode != null || channel != null) {
				chargeMetaData.setPurchaseCategoryCode(categoryCode);
				chargeMetaData.setChannel(channel);
				chargeMetaData.setOnBehalfOf(onBehalfOf);
				isContainsMetaData = true;
			}

            String serverReferenceCodeFormat = String.format("%06d", getReferenceNumber());
            String serverReferenceCode = "PAYMENT_REF" + serverReferenceCodeFormat;

            responseBean.setReferenceCode(referenceCode);
			responseBean.setServerReferenceCode(serverReferenceCode);
			responseBean.setResourceURL(CommonUtil.getResourceUrl(extendedRequestDTO));


            if (!isOriginalServerReferenceCode) {
                LOG.error("###REFUND### no payment matching for this Refund");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "no payment matching for this Refund"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            // Check for exceeded Amount
            if (validTransaction == 0.0 || validTransaction < 0) {
                LOG.error("###REFUND### exceeds refund Amount");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "exceeds refund Amount"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            if (chargeAmount <= validTransaction) {
                ManageNumber manageNumber = numberDAO.getNumber(endUserId, extendedRequestDTO.getUser().getUserName());
                Double updateBalance = manageNumber.getBalance() + chargeAmount;
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
            } else {
                LOG.error("###REFUND### Cannot Proceed Refund");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Cannot Proceed Refund"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            RefundTransactionDTO refundPaymentDTO = new RefundTransactionDTO();
            payAmount.setChargingInformation(chargeInformation);
            payAmount.setChargingMetaData(chargeMetaData);
            responseBean.setPaymentAmount(payAmount);
            refundPaymentDTO.setRefundTransaction(responseBean);
            responseWrapper.setRefundTransactionDTO(refundPaymentDTO);
            responseWrapper.setHttpStatus(Response.Status.CREATED);


            saveResponse(userId, endUserId, responseBean, apiServiceCalls, MessageProcessStatus.Success);

		} catch (Exception ex) {
			LOG.error("###WALLET### Error Occured in WALLET Service. ", ex);
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
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

	public boolean containsChannel(String channelValue) {

		for (Channel channel : Channel.values()) {
			if (channel.name().toLowerCase().equals(channelValue.toLowerCase())) {
				return true;
			}
		}

		return false;
	}


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

    private double checkOriginalServerReferenceWithServerReference(int userId, int serviceNameId, String tel,
                                                                   MessageProcessStatus status, MessageType type,
                                                                   String originalServerReferenceCode, String
                                                                           originalReferenceCode) throws Exception {

        List<Integer> list = new ArrayList<>();
        list.add(serviceNameId);
        List<MessageLog> servercodeList = loggingDAO.getMessageLogs(userId, list, "msisdn", "tel:+" + tel, null, null);


        APITypes apiTypes = dao.getAPIType(RequestType.WALLET.toString());
        APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCallRefund);

        int refundId = apiServiceCalls.getApiServiceCallId();
        List<Integer> list1 = new ArrayList<>();
        list1.add(refundId);

        List<MessageLog> originalServerReferenceCodeList = loggingDAO.getMessageLogs(userId, list1, "msisdn", "tel:+"
                + tel, null, null);

        Double paymentAmount = 0.0;


        for (int i = 0; i < servercodeList.size(); i++) {

            if (servercodeList != null) {

                int responseStatus = servercodeList.get(i).getStatus();
                int responseType = servercodeList.get(i).getType();

                if (responseType == type.getValue() && responseStatus == status.getValue()) {
                    String request = servercodeList.get(i).getRequest();
                    JSONObject json = new JSONObject(request);
                    String serverReferenceCode = json.get("serverReferenceCode").toString();
                    String referenceCode = json.get("referenceCode").toString();

                    int responseUserId = servercodeList.get(i).getUserid();
                    String responseTel = servercodeList.get(i).getValue();

                    if (responseUserId == userId && responseTel.equals("tel:+" + tel) && serverReferenceCode.equals
                            (originalServerReferenceCode) && referenceCode.equals(originalReferenceCode)) {
                        isOriginalServerReferenceCode = true;
                        paymentAmount = Double.valueOf(json.getJSONObject("paymentAmount").getJSONObject
                                ("chargingInformation").get("amount").toString());
                    }
                }

            }

        }

        if (isOriginalServerReferenceCode) {

            APITypes apiTypeCredit = dao.getAPIType(RequestType.WALLET.toString());
            APIServiceCalls apiServiceCallCredit = dao.getServiceCall(apiTypeCredit.getId(), ServiceName.RefundPayment
                    .toString());
            List<Integer> refundIdList = new ArrayList<>();
            refundIdList.add(apiServiceCallCredit.getApiServiceCallId());

            List<MessageLog> messageLogForPartialRefund = loggingDAO.getMessageLogs(userId, refundIdList, "msisdn",
                    "tel:+" + tel, null, null);

            if (messageLogForPartialRefund != null) {

                for (int i = 0; i < messageLogForPartialRefund.size(); i++) {

                    int responseStatus = messageLogForPartialRefund.get(i).getStatus();
                    int responseType = messageLogForPartialRefund.get(i).getType();

                    if (responseType == type.getValue() && responseStatus == status.getValue()) {

                        String request = messageLogForPartialRefund.get(i).getRequest();
                        JSONObject json = new JSONObject(request);
                        String responseOriginalServerReferenceCode = json.get("originalServerReferenceCode").toString();

                        int responseUserId = messageLogForPartialRefund.get(i).getUserid();
                        String responseTel = messageLogForPartialRefund.get(i).getValue();

                        // Check reference CodeDuplication
                        if (responseUserId == userId && responseTel.equals("tel:+" + tel) &&
                                responseOriginalServerReferenceCode.equals(originalServerReferenceCode)) {
                            totalAmountRefunded += Double.valueOf(json.getJSONObject("paymentAmount").getJSONObject
                                    ("chargingInformation").get("amount").toString());
                        }
                    }
                }

            }
        }

        return (paymentAmount - totalAmountRefunded);
    }

    // save Response in messageLog table
    private void saveResponse(Integer userId, String endUserIdPath, RefundTransactionResponseBean responseBean, APIServiceCalls apiServiceCalls, MessageProcessStatus status) throws Exception {

        String jsonInString = null;
        Gson resp = new Gson();

        JsonElement je = new JsonParser().parse(resp.toJson(responseBean));
        JsonObject asJsonObject = je.getAsJsonObject();
        jsonInString = asJsonObject.toString();

        //setting messagelog responses
        MessageLog messageLog = new MessageLog();
        messageLog = new MessageLog();
        messageLog.setRequest(jsonInString);
        messageLog.setStatus(status.getValue());
        messageLog.setType(MessageType.Response.getValue());
        messageLog.setServicenameid(apiServiceCalls.getApiServiceCallId());
        messageLog.setUserid(userId);
        messageLog.setReference("msisdn");
        messageLog.setValue("tel:+"+endUserIdPath);
        messageLog.setMessageTimestamp(new Date());

        loggingDAO.saveMessageLog(messageLog);
    }


    @Override
    public String getApiServiceCalls() {
        return ServiceName.RefundPayment.toString();
    }

    @Override
    public String getJosonString(RefundRequestWrapperDTO requestDTO) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(requestDTO.getRefundRequestBean());
        return jsonString;
    }

    @Override
    public String getnumber(RefundRequestWrapperDTO requestDTO) throws Exception {
        return getLastMobileNumber(requestDTO.getEndUserId());
    }
}