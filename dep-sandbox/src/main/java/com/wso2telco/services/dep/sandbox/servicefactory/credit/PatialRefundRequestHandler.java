/*******************************************************************************
 * Copyright (c) 2015-2017, WSO2.Telco Inc. (http://www.wso2telco.com)
 *
 * All Rights Reserved. WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.services.dep.sandbox.servicefactory.credit;

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
import com.wso2telco.services.dep.sandbox.dao.CreditDAO;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.LoggingDAO;
import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.*;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundRequestBean.RefundRequest;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundResponseBean.RefundResponse;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;
import com.wso2telco.services.dep.sandbox.servicefactory.*;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.Channel;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.MessageLogHandler;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class PatialRefundRequestHandler extends AbstractRequestHandler<PatialRefundRequestWrapper> implements RequestResponseRequestHandleable<PatialRefundRequestWrapper> {

    final String REFUND_AMOUNT = "refundAmount";
    final String MSISDN = "msisdn";
    final String CLIENTCORRELATOR = "clientCorrelator";
    final String REASON = "reasonForRefund";
    final String REFERENCE = "originalServerReferenceCode";
    final String REFERENCE_CODE = "referenceCode";
    final String ON_BEHALF_OF = "onBehalfOf";
    final String CATEGORY_CODE = "categoryCode";
    final String CHANNEL = "channel";
    final String TAX_AMOUNT = "taxAmount";
    final String CURRENCY = "currency";
    final String AMOUNT = "amount";
    final String TAX = "tax";
    final String CALL_BACK_DATA = "callbackData";
    final String NOTIFY_URL = "notifyURL";
    final String MERCHANT_IDENTIFICATION = "merchantIdentification";
    final String DESCRIPTION = "description";
    final String PURCHASE_CATEGORY_CODE = "purchaseCategoryCode";

    private NumberDAO numberDao;
    private CreditDAO creditDAO;
    private MessageLogHandler logHandler;
    private PatialRefundRequestWrapper requestWrapperDTO;
    private PatialRefundResponseWrapper responseWrapperDTO;
    private Integer correlatorid;
    private LoggingDAO loggingDao;
    private String responseReferenceCode;
    private boolean isOriginalServerReferenceCode;
    private double amount;
    private double paymentAmount = 0.0;


    {
        LOG = LogFactory.getLog(PatialRefundRequestHandler.class);
        loggingDao = DaoFactory.getLoggingDAO();
        numberDao = DaoFactory.getNumberDAO();
        creditDAO = DaoFactory.getCreditDAO();
        dao = DaoFactory.getGenaricDAO();
        logHandler = MessageLogHandler.getInstance();
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
        return responseWrapperDTO;
    }

    @Override
    protected List<String> getAddress() {
        List<String> address = new ArrayList<String>();
        address.add(requestWrapperDTO.getMsisdn());
        return address;
    }

    @Override
    protected boolean validate(PatialRefundRequestWrapper wrapperDTO) throws Exception {

        RefundRequestBean requestBean = wrapperDTO.getRefundRequestBean();
        RefundRequest request = requestBean.getRefundRequest();
        PaymentAmountWithTax paymentAmountWithTax = request.getPaymentAmount();
        ChargingInformation chargingInformation = paymentAmountWithTax.getChargingInformation();
        ChargingMetaDataWithTax metadata = paymentAmountWithTax.getChargingMetaData();
        String callbackData = null;
        String notifyURL = null;

        if (requestBean != null && request != null) {

            String RefundAmount = CommonUtil.getNullOrTrimmedValue(request.getRefundAmount());
            String msisdn = CommonUtil.getNullOrTrimmedValue(request.getMsisdn());
            String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
            String reasonForRefund = CommonUtil.getNullOrTrimmedValue(request.getReasonForRefund());
            String originalServerReferenceCode = CommonUtil.getNullOrTrimmedValue(request.getOriginalServerReferenceCode());
            String onBehalfOf = CommonUtil.getNullOrTrimmedValue(metadata.getOnBehalfOf());
            String categoryCode = CommonUtil.getNullOrTrimmedValue(metadata.getPurchaseCategoryCode());
            String channel = CommonUtil.getNullOrTrimmedValue(metadata.getChannel());
            String taxAmount = CommonUtil.getNullOrTrimmedValue(metadata.getTax());
            String referenceCode = CommonUtil.getNullOrTrimmedValue(request.getReferenceCode());
            String currency = CommonUtil.getNullOrTrimmedValue(chargingInformation.getCurrency());
            String amount = CommonUtil.getNullOrTrimmedValue(String.valueOf(chargingInformation.getAmount()));

            if(request.getReceiptRequest()!=null) {
                 callbackData = CommonUtil.getNullOrTrimmedValue(request.getReceiptRequest().getCallbackData());
                 notifyURL = CommonUtil.getNullOrTrimmedValue(request.getReceiptRequest().getNotifyURL());
            }

            String merchantIdentification = CommonUtil.getNullOrTrimmedValue(request.getMerchantIdentification());
            String description = CommonUtil.getNullOrTrimmedValue(chargingInformation.getDescription());
            String purchaseCategoryCode = CommonUtil.getNullOrTrimmedValue(metadata.getPurchaseCategoryCode());

            try {
                ValidationRule[] validationRules = {
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO, REFUND_AMOUNT, RefundAmount),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, MSISDN, msisdn),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, CLIENTCORRELATOR, clientCorrelator),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, REFERENCE, originalServerReferenceCode),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, REASON, reasonForRefund),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, ON_BEHALF_OF, onBehalfOf),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, CATEGORY_CODE, categoryCode),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, CHANNEL, channel),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO, TAX_AMOUNT, taxAmount),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, REFERENCE_CODE, referenceCode),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, CURRENCY, currency),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GE_ZERO, AMOUNT, amount),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, CALL_BACK_DATA, callbackData),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, NOTIFY_URL, notifyURL),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, MERCHANT_IDENTIFICATION, merchantIdentification),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, DESCRIPTION, description),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, PURCHASE_CATEGORY_CODE, purchaseCategoryCode)

                };

                Validation.checkRequestParams(validationRules);
            } catch (CustomException ex) {
                LOG.error("###CREDIT### Error in Validation : " + ex);
                responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ex.getErrcode(),
                        ex.getErrmsg(), ex.getErrvar()[0]));
                responseWrapperDTO.setHttpStatus(javax.ws.rs.core.Response.Status.BAD_REQUEST);
            }
            return true;
        } else {
            responseWrapperDTO.setRequestError(
                    constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE.getCode(),
                            ServiceError.INVALID_INPUT_VALUE.getMessage(), wrapperDTO.getMsisdn()));
            responseWrapperDTO.setHttpStatus(javax.ws.rs.core.Response.Status.BAD_REQUEST);
        }
        return false;
    }

    @Override
    protected Returnable process(PatialRefundRequestWrapper extendedRequestDTO) throws Exception {

        if (responseWrapperDTO.getRequestError() != null) {
            responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
            return responseWrapperDTO;
        }

        RefundRequestBean requestBean = extendedRequestDTO.getRefundRequestBean();
        RefundRequest request = requestBean.getRefundRequest();
        PaymentAmountWithTax paymentAmountWithTax = request.getPaymentAmount();
        ChargingInformation chargingInformation = paymentAmountWithTax.getChargingInformation();
        ChargingMetaDataWithTax metadata = paymentAmountWithTax.getChargingMetaData();

        String RefundAmount =  CommonUtil.getNullOrTrimmedValue(request.getRefundAmount());
        String msisdn = CommonUtil.getNullOrTrimmedValue(request.getMsisdn());
        String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
        String reasonForRefund = CommonUtil.getNullOrTrimmedValue(request.getReasonForRefund());
        String serverTransactionReference = CommonUtil.getNullOrTrimmedValue(request.getOriginalServerReferenceCode());
        String userName = extendedRequestDTO.getUser().getUserName();
        String serviceCreditApply = ServiceName.PartialRefund.toString();
        String channel = CommonUtil.getNullOrTrimmedValue(metadata.getChannel());
        String referenceCode = CommonUtil.getNullOrTrimmedValue(String.valueOf(request.getReferenceCode()));
        String endUserID = getLastMobileNumber(extendedRequestDTO.getMsisdn());
        String currency = CommonUtil.getNullOrTrimmedValue(chargingInformation.getCurrency());
        String endUserIdPath = extendedRequestDTO.getMsisdn();
        amount = Double.parseDouble(CommonUtil.getNullOrTrimmedValue(String.valueOf(chargingInformation.getAmount())));
        String callbackData = CommonUtil.getNullOrTrimmedValue(request.getReceiptRequest().getCallbackData());
        String notifyURL =  CommonUtil.getNullOrTrimmedValue(request.getReceiptRequest().getNotifyURL());
        String merchantIdentification = CommonUtil.getNullOrTrimmedValue(request.getMerchantIdentification());


        String serverReferenceCodeFormat = String.format("%06d", getReferenceNumber());
        String serverReferenceCode = "PAYMENT_REF" + serverReferenceCodeFormat;
        // Save Request Log
        APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
        APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCreditApply);

        try {

            Integer userId = extendedRequestDTO.getUser().getId();
            int serviceNameId = apiServiceCalls.getApiServiceCallId();

            if (clientCorrelator != null) {

                String response = checkDuplicateClientCorrelator(clientCorrelator, userId, serviceNameId, endUserID, MessageProcessStatus.Success, MessageType.Response, referenceCode);

                if (response != null) {
                    RefundResponseBean bean = new RefundResponseBean();
                    org.json.JSONObject json = new org.json.JSONObject(response);
                    String jasonString = json.getJSONObject("refundResponse").toString();
                    ObjectMapper mapper = new ObjectMapper();
                    RefundResponse res = mapper.readValue(jasonString, RefundResponse.class);
                    bean.setRefundResponse(res);
                    responseWrapperDTO.setRefundResponseBean(bean);
                    responseWrapperDTO.setHttpStatus(Response.Status.OK);
                    return responseWrapperDTO;
                }

            }

            String result = checkReferenceCode(userId, serviceNameId, endUserID, MessageProcessStatus.Success, MessageType.Response, referenceCode);

            if ((result != null)) {
                LOG.error("###REFUND### Already charged for this reference code");
                responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Already used reference code"));
                responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapperDTO;
            }

            // check serverReferenceCode against OriginalServerReferenceCode
            Double validTransaction = checkOriginalServerReferenceWithServerReference(userId, endUserID, MessageProcessStatus.Success, MessageType.Response, serverTransactionReference);

            // check account amount decimal format
            Double partialRefundAmount = Double.parseDouble(RefundAmount);
            BigDecimal bigDecimal = new BigDecimal(RefundAmount);
            Integer decimalDigits = bigDecimal.scale();
            if (!((decimalDigits <= 2) && (decimalDigits >= 0)) || partialRefundAmount < 0) {
                LOG.error("###REFUND### amount should be a whole number or two digit decimal");
                responseWrapperDTO
                        .setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
                                "amount should be a whole or two digit decimal positive number"));
                responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapperDTO;
            }

            if(currency!=null) {
                // check valid account currency for endUserId
                boolean isValidCurrency = currencySymbol(currency);
                if (!isValidCurrency) {
                    LOG.error("###REFUND### currency code not as per ISO 4217");
                    responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
                            ServiceError.INVALID_INPUT_VALUE, "currency code not as per ISO 4217"));
                    responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
                    return responseWrapperDTO;

                }
            }

            // check path param endUserId and request body endUserId
            if (!(getLastMobileNumber(endUserIdPath).equals(getLastMobileNumber(msisdn)))) {
                LOG.error("###REFUND### two different endUserId provided");
                responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "two different endUserId provided"));
                responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
                return responseWrapperDTO;
            }

            //Check channel.
            if (channel != null && !containsChannel(channel)) {
                LOG.error("###REFUND### Valid channel doesn't exists for the given inputs");
                responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Valid channel doesn't exists for the given inputs"));
                responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
                return responseWrapperDTO;
            }

            if (!isOriginalServerReferenceCode) {
                LOG.error("###REFUND### no payment matching for this Refund");
                responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "no payment matching for this Refund"));
                responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapperDTO;
            }

            if (paymentAmount != amount) {
                LOG.error("###REFUND### Charging Information amount miss match");
                responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Charging Information amount miss match"));
                responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapperDTO;
            }

            // Check for exceeded Amount
            if (validTransaction == 0.0) {
                LOG.error("###REFUND### exceeds refund Amount");
                responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "exceeds refund Amount"));
                responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapperDTO;
            }

            ReceiptResponse receiptResponse = new ReceiptResponse();
            receiptResponse.setCallbackData(callbackData);
            receiptResponse.setNotifyURL(notifyURL);

            if (partialRefundAmount <= validTransaction) {


                ManageNumber manageNumber = numberDao.getNumber(endUserID, userName);
                if (manageNumber != null) {
                    updateBalance(manageNumber, Double.parseDouble(RefundAmount));
                    RefundResponseBean responseBean = buildJsonResponseBody(RefundAmount, clientCorrelator, msisdn, reasonForRefund,
                            serverTransactionReference, OperationStatus.Refunded.toString(), referenceCode, serverReferenceCode, chargingInformation, metadata,receiptResponse,merchantIdentification);

                    saveResponse(extendedRequestDTO, endUserID, responseBean, apiServiceCalls, MessageProcessStatus.Success);
                    responseWrapperDTO.setHttpStatus(Response.Status.CREATED);
                    return responseWrapperDTO;
                } else {
                    responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
                            ServiceError.INVALID_INPUT_VALUE, "Number is not Registered for the Service"));
                    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
                    return responseWrapperDTO;
                }

            } else {
                LOG.error("###REFUND### Cannot Proceed Refund");
                responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Cannot Proceed Refund"));
                responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapperDTO;

            }


        } catch (Exception ex) {
            LOG.error("###REFUND### Error in processing credit service request. ", ex);
            responseWrapperDTO
                    .setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
            responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
            return responseWrapperDTO;
        }
    }

    @Override
    protected void init(PatialRefundRequestWrapper extendedRequestDTO) throws Exception {
        requestWrapperDTO = extendedRequestDTO;
        responseWrapperDTO = new PatialRefundResponseWrapper();
    }

    private void updateBalance(ManageNumber manageNumber, double amount) throws Exception {

        double newBalance = manageNumber.getBalance() + amount;
        manageNumber.setBalance(newBalance);
        numberDao.saveManageNumbers(manageNumber);
    }

    private RefundResponseBean buildJsonResponseBody(String amount, String clientCorrelator, String enduserID,
                                                     String reason, String serverTransactionReference, String
                                                             operationStatus, String referenceCode, String
                                                             serverReferenceCode, ChargingInformation
                                                             chargingInformation, ChargingMetaDataWithTax
                                                             chargingMetaDataWithTax, ReceiptResponse receiptResponse,
                                                     String merchantIdentification) {

        PaymentAmountWithTax paymentAmountWithTax = new PaymentAmountWithTax();
        paymentAmountWithTax.setChargingInformation(chargingInformation);
        paymentAmountWithTax.setChargingMetaData(chargingMetaDataWithTax);

        RefundResponse refundResponse = new RefundResponse();
        refundResponse.setRefundAmount(Double.parseDouble(amount));
        refundResponse.setOriginalServerReferenceCode(serverTransactionReference);
        refundResponse.setClientCorrelator(clientCorrelator);
        refundResponse.setEndUserID(enduserID);
        refundResponse.setReasonForRefund(reason);
        refundResponse.setMerchantIdentification(merchantIdentification);
        refundResponse.setPaymentAmount(paymentAmountWithTax);
        refundResponse.setReceiptResponse(receiptResponse);
        refundResponse.setReferenceCode(referenceCode);
        refundResponse.setServerReferanceCode(serverReferenceCode);
        refundResponse.setResourceURL(CommonUtil.getResourceUrl(requestWrapperDTO));
        refundResponse.setTransactionOperationStatus(operationStatus);

        RefundResponseBean refundResponseBean = new RefundResponseBean();
        refundResponseBean.setRefundResponse(refundResponse);
        responseWrapperDTO.setRefundResponseBean(refundResponseBean);
        return refundResponseBean;

    }

    public boolean containsChannel(String channelValue) {

        for (Channel channel : Channel.values()) {
            if (channel.name().toLowerCase().equals(channelValue.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    // Check already existing clientcorrelator return response body
    private String checkDuplicateClientCorrelator(String clientCorrelator, int userId, int serviceNameId, String tel, MessageProcessStatus status, MessageType type, String referenceCode) throws Exception {

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
                    org.json.JSONObject json = new org.json.JSONObject(request);
                    responseClientCorrelator = null;

                    if (json.getJSONObject("refundResponse").has("clientCorrelator")) {
                        responseClientCorrelator = json.getJSONObject("refundResponse").get("clientCorrelator").toString();
                    }

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
    private String checkReferenceCode(int userId, int serviceNameId, String tel, MessageProcessStatus status, MessageType type, String referenceCode) throws Exception {

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
                    org.json.JSONObject json = new org.json.JSONObject(request);
                    responseReferenceCode = json.getJSONObject("refundResponse").get("referenceCode").toString();

                    int responseUserId = response.get(i).getUserid();
                    String responseTel = response.get(i).getValue();

                    // Check reference CodeDuplication
                    if (responseUserId == userId && responseTel.equals("tel:+" + tel) && responseReferenceCode.equals(referenceCode)) {
                        jsonString = responseReferenceCode;
                        break;
                    }
                }

            }

        }

        return jsonString;
    }


    private double checkOriginalServerReferenceWithServerReference(int userId, String tel, MessageProcessStatus status, MessageType type, String originalServerReferenceCode) throws Exception {

        double totalAmountRefunded = 0.0;

        APITypes apiTypes = dao.getAPIType(RequestType.PAYMENT.toString());
        APIServiceCalls apiServiceCallsForMakePayment = dao.getServiceCall(apiTypes.getId(), ServiceName.ChargeUser.toString());
        List<Integer> list = new ArrayList<>();
        list.add(apiServiceCallsForMakePayment.getApiServiceCallId());
        List<MessageLog> servercodeList = loggingDAO.getMessageLogs(userId, list, "msisdn", "tel:+" + tel, null, null);

        for (int i = 0; i < servercodeList.size(); i++) {

            if (servercodeList != null) {

                int responseStatus = servercodeList.get(i).getStatus();
                int responseType = servercodeList.get(i).getType();

                if (responseType == type.getValue() && responseStatus == status.getValue()) {
                    String request = servercodeList.get(i).getRequest();
                    org.json.JSONObject json = new org.json.JSONObject(request);
                    String serverReferenceCode = json.get("serverReferenceCode").toString();

                    int responseUserId = servercodeList.get(i).getUserid();
                    String responseTel = servercodeList.get(i).getValue();

                    if (responseUserId == userId && responseTel.equals("tel:+" + tel) && serverReferenceCode.equals(originalServerReferenceCode)) {
                        isOriginalServerReferenceCode = true;
                        paymentAmount = Double.valueOf(json.getJSONObject("paymentAmount").getJSONObject("chargingInformation").get("amount").toString());
                    }
                }
            }
        }

        if (isOriginalServerReferenceCode) {

            APITypes adiTypesForCredit = dao.getAPIType(RequestType.CREDIT.toString());
            APIServiceCalls apiServiceCalls = dao.getServiceCall(adiTypesForCredit.getId(), ServiceName.PartialRefund.toString());
            int refundId = apiServiceCalls.getApiServiceCallId();
            List<Integer> PartialRefundIdList = new ArrayList<>();
            PartialRefundIdList.add(refundId);

            List<MessageLog> originalServerReferenceCodeList = loggingDAO.getMessageLogs(userId, PartialRefundIdList, "msisdn", "tel:+" + tel, null, null);

            for (int i = 0; i < originalServerReferenceCodeList.size(); i++) {

                int responseStatus = originalServerReferenceCodeList.get(i).getStatus();
                int responseType = originalServerReferenceCodeList.get(i).getType();

                if (responseType == type.getValue() && responseStatus == status.getValue()) {
                    String request = originalServerReferenceCodeList.get(i).getRequest();
                    org.json.JSONObject json = new org.json.JSONObject(request);
                    String responseOriginalServerReferenceCode = json.getJSONObject("refundResponse").get("originalServerReferenceCode").toString();

                    int responseUserId = originalServerReferenceCodeList.get(i).getUserid();
                    String responseTel = originalServerReferenceCodeList.get(i).getValue();

                    // Check reference CodeDuplication
                    if (responseUserId == userId && responseTel.equals("tel:+" + tel) && responseOriginalServerReferenceCode.equals(originalServerReferenceCode)) {
                        totalAmountRefunded += Double.valueOf(json.getJSONObject("refundResponse").get("refundAmount").toString());
                    }
                }
            }

            APIServiceCalls apiServiceCall = dao.getServiceCall(apiTypes.getId(), ServiceName.RefundUser.toString());
            List<Integer> refundIdList = new ArrayList<>();
            refundIdList.add(apiServiceCall.getApiServiceCallId());

            List<MessageLog> messageLogsForRefundList = loggingDAO.getMessageLogs(userId, refundIdList, "msisdn", "tel:+" + tel, null, null);

            for (int i = 0; i < messageLogsForRefundList.size(); i++) {
                int responseStatus = messageLogsForRefundList.get(i).getStatus();
                int responseType = messageLogsForRefundList.get(i).getType();

                if (responseType == type.getValue() && responseStatus == status.getValue()) {

                    String request = messageLogsForRefundList.get(i).getRequest();
                    org.json.JSONObject json = new org.json.JSONObject(request);
                    String responseOriginalServerReferenceCode = json.get("originalServerReferenceCode").toString();
                    int responseUserId = messageLogsForRefundList.get(i).getUserid();
                    String responseTel = messageLogsForRefundList.get(i).getValue();

                    // Check reference CodeDuplication
                    if (responseUserId == userId && responseTel.equals("tel:+" + tel) && responseOriginalServerReferenceCode.equals(originalServerReferenceCode)) {
                        totalAmountRefunded += Double.valueOf(json.getJSONObject("paymentAmount").getJSONObject("chargingInformation").get("amount").toString());
                    }
                }
            }

        }
        return (paymentAmount - totalAmountRefunded);
    }

    // Save Response in messageLog table
    private void saveResponse(PatialRefundRequestWrapper extendedRequestDTO,
                              String endUserIdPath, RefundResponseBean responseBean, APIServiceCalls apiServiceCalls, MessageProcessStatus status) throws Exception {

        Gson resp = new Gson();
        JsonElement je = new JsonParser().parse(resp.toJson(responseBean));
        JsonObject asJsonObject = je.getAsJsonObject();
        String jsonInString = asJsonObject.toString();
        //setting messagelog responses
        MessageLog messageLog = new MessageLog();
        messageLog = new MessageLog();
        messageLog.setRequest(jsonInString);
        messageLog.setStatus(status.getValue());
        messageLog.setType(1);
        messageLog.setServicenameid(apiServiceCalls.getApiServiceCallId());
        messageLog.setUserid(extendedRequestDTO.getUser().getId());
        messageLog.setReference("msisdn");
        messageLog.setValue("tel:+"+endUserIdPath);
        messageLog.setMessageTimestamp(new Date());

        loggingDAO.saveMessageLog(messageLog);
    }


    @Override
    public String getApiServiceCalls() {
        return ServiceName.PartialRefund.toString();
    }

    @Override
    public String getJosonString(PatialRefundRequestWrapper requestDTO) {
        Gson gson = new Gson();
        String jasonString = gson.toJson(requestDTO.getRefundRequestBean());
        return jasonString;
    }

    @Override
    public String getnumber(PatialRefundRequestWrapper requestDTO) throws Exception {
        return getLastMobileNumber(requestDTO.getMsisdn());
    }
}
