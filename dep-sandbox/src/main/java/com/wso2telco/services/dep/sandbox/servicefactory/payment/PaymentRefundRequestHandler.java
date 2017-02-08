/*
 * Copyright (c) 2017, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.services.dep.sandbox.servicefactory.payment;

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
import com.wso2telco.services.dep.sandbox.dao.PaymentDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.*;
import com.wso2telco.services.dep.sandbox.dao.model.domain.*;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.AttributeName;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.Channel;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.RefundTransactionRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.TransactionStatus;
import com.wso2telco.services.dep.sandbox.util.*;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class PaymentRefundRequestHandler extends AbstractRequestHandler<PaymentRefundRequestWrapperDTO> {

    private PaymentDAO paymentDAO;
    private LoggingDAO loggingDAO;
    private NumberDAO numberDAO;
    private PaymentRefundRequestWrapperDTO requestWrapperDTO;
    private PaymentRefundResponseWrapper responseWrapper;
    private MessageLogHandler logHandler;
    private String serviceCallRefund;
    private boolean isContainsMetaData;
    private Integer transactionId;

    {
        LOG = LogFactory.getLog(RefundTransactionRequestHandler.class);
        paymentDAO = DaoFactory.getPaymentDAO();
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
    protected void init(PaymentRefundRequestWrapperDTO extendedRequestDTO) throws Exception {
        responseWrapper = new PaymentRefundResponseWrapper();
        requestWrapperDTO = extendedRequestDTO;
    }

    @Override
    protected boolean validate(PaymentRefundRequestWrapperDTO wrapperDTO) throws Exception {

        PaymentRefundTransactionRequestBean requestBean = wrapperDTO.getRefundRequestBean();
        //Getting Refund Transaction
        PaymentRefundTransactionRequestBean.AmountTransaction request = requestBean.getAmountTransaction();
        ChargePaymentAmount paymentAmount = request.getPaymentAmount();

        PaymentChargingInformation chargingInformation = paymentAmount.getChargingInformation();
        PaymentChargingMetaData metaData = paymentAmount.getChargingMetaData();

        String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
        String endUserID = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getEndUserId());
        String msisdn = CommonUtil.getNullOrTrimmedValue(request.getEndUserId());
        String amount = CommonUtil.getNullOrTrimmedValue(chargingInformation.getAmount().toString());
        String currency = CommonUtil.getNullOrTrimmedValue(chargingInformation.getCurrency().toString());
        String description = CommonUtil.getNullOrTrimmedValue(chargingInformation.getDescription());
        String onBehalfOf = CommonUtil.getNullOrTrimmedValue(metaData.getOnBehalfOf());
        String categoryCode = CommonUtil.getNullOrTrimmedValue(metaData.getPurchaseCategoryCode());
        String channel = CommonUtil.getNullOrTrimmedValue(metaData.getChannel());
        String taxAmount = CommonUtil.getNullOrTrimmedValue(metaData.getTaxAmount());
        String referenceCode = CommonUtil.getNullOrTrimmedValue(request.getReferenceCode());
        String transactionOperationStatus = CommonUtil.getNullOrTrimmedValue(request.getTransactionOperationStatus());

        List<ValidationRule> validationRulesList = new ArrayList<>();

        try {

            validationRulesList.add(
                    new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID,
                    "endUserID", endUserID));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID,
                    "endUserID", msisdn));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
                    "amount", amount));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
                    "currency", currency));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
                    "description", description));
            if (metaData != null) {
                validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,
                        "onBehalfOf", onBehalfOf));
                validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,
                        "categoryCode", categoryCode));
                validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,
                        "channel", channel));
                validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,
                        "taxAmount", taxAmount));
            }
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
                    "referenceCode", referenceCode));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,
                    "transactionOperationStatus", transactionOperationStatus));

            ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
            validationRules = validationRulesList.toArray(validationRules);

            Validation.checkRequestParams(validationRules);

        } catch (CustomException ex) {
            LOG.error("###REFUND### Error in Validations. ", ex);
            responseWrapper.setRequestError(
                    constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
            return false;
        }
        return true;
    }

    @Override
    protected Returnable process(PaymentRefundRequestWrapperDTO extendedRequestDTO) throws Exception {
        if (responseWrapper.getRequestError() != null) {
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            return responseWrapper;
        }

        try {
            PaymentRefundTransactionRequestBean requestBean = extendedRequestDTO.getRefundRequestBean();
            PaymentRefundTransactionRequestBean.AmountTransaction request = requestBean.getAmountTransaction();
            ChargePaymentAmount paymentAmount = request.getPaymentAmount();
            PaymentChargingInformation chargingInformation = paymentAmount.getChargingInformation();
            PaymentChargingMetaData metadata = paymentAmount.getChargingMetaData();

            String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
            String endUserIdPath = extendedRequestDTO.getEndUserId();
            String endUserIdRequest = request.getEndUserId();
            String endUserId = getLastMobileNumber(endUserIdPath);
//            String originalReferenceCode = CommonUtil.getNullOrTrimmedValue(request.getOriginalServerReferenceCode());
            String originalServerReferenceCode = CommonUtil
                    .getNullOrTrimmedValue(request.getOriginalServerReferenceCode());
            String amount = CommonUtil.getNullOrTrimmedValue(chargingInformation.getAmount());
            String currency = CommonUtil.getNullOrTrimmedValue(chargingInformation.getCurrency());
            String description = CommonUtil.getNullOrTrimmedValue(chargingInformation.getDescription());
            String onBehalfOf = CommonUtil.getNullOrTrimmedValue(metadata.getOnBehalfOf());
            String categoryCode = CommonUtil.getNullOrTrimmedValue(metadata.getPurchaseCategoryCode());
            String channel = CommonUtil.getNullOrTrimmedValue(metadata.getChannel());
            String taxAmount = CommonUtil.getNullOrTrimmedValue(metadata.getTaxAmount());
            String referenceCode = CommonUtil.getNullOrTrimmedValue(request.getReferenceCode());
            String transactionOperationStatus = CommonUtil.getNullOrTrimmedValue(request.getTransactionOperationStatus());
            serviceCallRefund = ServiceName.RefundPayment.toString();
            // Attribute Name Used in wallet
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
            String serverReferenceCode = "PAYMENT_REF" + serverReferenceCodeFormat;

            // check already charge request against client correlator
            if (clientCorrelator != null) {
                String clientCorrelatorAttribute = AttributeName.clientCorrelatorPayment.toString();
                String tableAttributeValue = TableName.SBXATTRIBUTEVALUE.toString().toLowerCase();
                AttributeValues duplicateClientCorrelator = paymentDAO.checkDuplicateValue(serviceCallRefund,
                        clientCorrelator, clientCorrelatorAttribute, tableAttributeValue);
                if (duplicateClientCorrelator != null) {
                    APIServiceCalls apiServiceCall = duplicateClientCorrelator.getAttributedid().getAPIServiceCall();
                    String serviceCall = apiServiceCall.getServiceName();
                    ManageNumber manageNumber = numberDAO.getNumber(endUserId, userName);
                    Integer id = duplicateClientCorrelator.getOwnerdid();
                    AttributeValues response = paymentDAO.getResponse(id);
                    if (serviceCall.equals(serviceCallRefund) && (response.getOwnerdid() == manageNumber.getId())) {
                        PaymentRefundTransactionResponseBean obj = null;
                        obj = gson.fromJson(response.getValue(), PaymentRefundTransactionResponseBean.class);
                        RefundPaymentDTO dto = new RefundPaymentDTO();
                        dto.setAmountTransaction(obj);
                        responseWrapper.setRefundPaymentDTO(dto);
                        responseWrapper.setHttpStatus(Response.Status.OK);
                        return responseWrapper;
                    } else {
                        responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                                ServiceError.INVALID_INPUT_VALUE, "Clientcorrelator is already used"));
                        responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                        return responseWrapper;
                    }
                }
            }

            // check path param endUserId and request body endUserId
            if (!(endUserIdPath.equals(endUserIdRequest))) {
                LOG.error("###REFUND### two different endUserId provided");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "two different endUserId provided"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            // check valid amount format
            if ((NumberUtils.isNumber(amount) != true)) {
                LOG.error("###WALLET### amount should be a valid number");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "amount should be a valid number"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            // check account amount decimal format
            Double chargeAmount = Double.parseDouble(amount);
            BigDecimal bigDecimal = new BigDecimal(amount);
            Integer decimalDigits = bigDecimal.scale();
            if (!((decimalDigits <= 2) && (decimalDigits >= 0)) || chargeAmount < 0) {
                LOG.error("###REFUND### amount should be a whole number or two digit decimal");
                responseWrapper
                        .setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
                                "amount should be a whole or two digit decimal positive number"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            // check valid account currency for endUserId
            boolean isValidCurrency = currencySymbol(currency);
            if (!isValidCurrency) {
                LOG.error("###WALLET### currency code not as per ISO 4217");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "currency code not as per ISO 4217"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;

            }
            AttributeValues accountCurrencyValue = paymentDAO.getAttributeValue(endUserId, serviceCallBalanceLookUp,
                    accountCurrencyAttribute, userId);
            if (accountCurrencyValue != null) {
                String accountCurrency = accountCurrencyValue.getValue();
                if (!(currency.equals(accountCurrency))) {
                    LOG.error("###REFUND### Valid currecy doesn't exists for the given inputs");
                    responseWrapper
                            .setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
                                    "Valid currency does not exist for the given input parameters"));
                    responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                    return responseWrapper;
                }
            }

            // check channel
            if (channel != null && !containsChannel(channel)) {
                LOG.error("###WALLET### Valid channel doesn't exists for the given inputs");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Valid channel doesn't exists for the given inputs"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            String referenceCodeAttribute = AttributeName.referenceCodePayment.toString();
            String tableNumber = TableName.NUMBERS.toString().toLowerCase();
            AttributeValues duplicateReferenceCode = paymentDAO.checkDuplicateValue(serviceCallRefund, referenceCode,
                    referenceCodeAttribute, tableNumber);
            if (duplicateReferenceCode != null) {
                LOG.error("###REFUND### Already charged for this reference code");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Already charged for this reference code"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            PaymentRefundTransactionResponseBean responseBean = new PaymentRefundTransactionResponseBean();
            ChargeRefundAmountResponse payAmount = new ChargeRefundAmountResponse();
            PaymentChargingInformation chargeInformation = new PaymentChargingInformation();
            PaymentChargingMetaData chargeMetaData = new PaymentChargingMetaData();

            responseBean.setClientCorrelator(clientCorrelator);
            responseBean.setEndUserId(endUserIdPath);
            responseBean.setOriginalServerReferenceCode(originalServerReferenceCode);

            chargeInformation.setAmount(amount);
            chargeInformation.setCurrency(currency);
            chargeInformation.setDescription(description);

            if (onBehalfOf != null || categoryCode != null || channel != null) {
                chargeMetaData.setPurchaseCategoryCode(categoryCode);
                chargeMetaData.setChannel(channel);
                chargeMetaData.setOnBehalfOf(onBehalfOf);
                chargeMetaData.setTaxAmount(taxAmount);
                isContainsMetaData = true;
            }

            responseBean.setReferenceCode(referenceCode);
            responseBean.setServerReferenceCode(serverReferenceCode);
            responseBean.setResourceURL(CommonUtil.getResourceUrl(extendedRequestDTO));

            // Get the Charged Tax Amount

            Double chargedTaxAmount = Double.parseDouble(taxAmount);

            // set transaction operation status as charged
            ManageNumber manageNumber = numberDAO.getNumber(endUserId, extendedRequestDTO.getUser().getUserName());
            Double updateBalance = manageNumber.getBalance() + (chargeAmount + chargedTaxAmount);
            AttributeValues accountStatusValue = paymentDAO.getAttributeValue(endUserId, serviceCallRefund,
                    AttributeName.transactionStatus.toString(), userId);
            // set transaction operation status as refused
            manageNumber.setBalance(updateBalance);

            if (accountStatusValue != null) {
                String accountStatus = accountStatusValue.getValue();
                // set transaction operation status as Refused   // Here wallet Status are Used
                if (accountStatus.equals(TransactionStatus.Refused.toString())) {
                    responseBean.setTransactionOperationStatus(TransactionStatus.Refused.toString());

                }
            }
            // set transaction operation status as Refunded
            else if (paymentDAO.saveManageNumbers(manageNumber)) {
                responseBean.setTransactionOperationStatus(TransactionStatus.Refunded.toString());
            }

            responseWrapper.setHttpStatus(Response.Status.OK);
            RefundPaymentDTO refundPaymentDTO = new RefundPaymentDTO();
            payAmount.setChargingInformation(chargingInformation);
            payAmount.setChargingMetaData(chargeMetaData);
            responseBean.setPaymentAmount(payAmount);
            refundPaymentDTO.setAmountTransaction(responseBean); // RefundTransaction
            responseWrapper.setRefundPaymentDTO(refundPaymentDTO);

            // save transaction
            transactionId = saveTransaction(responseBean, endUserId, userName);

            // save client correlator
            if (clientCorrelator != null) {
                saveClientCorrelator(endUserId, clientCorrelator, userName);
            }
            // save client correlator
            saveReferenceCode(endUserId, referenceCode, userName);
        } catch (Exception ex) {
            LOG.error("###REFUND### Error Occured in PAYMENT Service. ", ex);
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            responseWrapper
                    .setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
        }
        return responseWrapper;
    }

    public Integer saveTransaction(PaymentRefundTransactionResponseBean responseBean, String endUserId, String userName)
            throws Exception {
        Integer transactionId = null;
        try {
            AttributeValues valueObj = new AttributeValues();
            String tableName = TableName.NUMBERS.toString().toLowerCase();
            String attributeName = AttributeName.Refund.toString().toLowerCase();
            APITypes api = dao.getAPIType(RequestType.PAYMENT.toString());
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
            transactionId = paymentDAO.saveAttributeValue(valueObj);

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
            APITypes api = dao.getAPIType(RequestType.PAYMENT.toString());
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
            APITypes api = dao.getAPIType(RequestType.PAYMENT.toString());
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
            LOG.error("###PAYMENT### Error in processing save of referenceCode request. ", ex);
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
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

}
