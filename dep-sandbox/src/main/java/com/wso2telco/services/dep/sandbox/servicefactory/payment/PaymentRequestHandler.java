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

package com.wso2telco.services.dep.sandbox.servicefactory.payment;

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
import com.wso2telco.services.dep.sandbox.dao.PaymentDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.*;

import com.wso2telco.services.dep.sandbox.dao.model.domain.*;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;

import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.AttributeName;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.Channel;
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

public class PaymentRequestHandler extends AbstractRequestHandler<ChargePaymentRequestWrapperDTO> {


    private PaymentDAO paymentDAO;
    private LoggingDAO loggingDAO;
    private NumberDAO numberDAO;
    private ChargePaymentRequestWrapperDTO requestWrapperDTO;
    private PaymentResponseWrapper responseWrapper;
    private MessageLogHandler logHandler;
    private String serviceCallPayment;
    private Integer transactionId;


    {
        LOG = LogFactory.getLog(PaymentRequestHandler.class);
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
    protected void init(ChargePaymentRequestWrapperDTO extendedRequestDTO) throws Exception {
        responseWrapper = new PaymentResponseWrapper();
        requestWrapperDTO = extendedRequestDTO;
    }

    @Override
    protected boolean validate(ChargePaymentRequestWrapperDTO wrapperDTO) throws Exception {
        PaymentRefundTransactionRequestBean requestBean = wrapperDTO.getPaymentRefundTransactionRequestBean();
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
            LOG.error("###PAYMENT### Error in Validations. ", ex);
            responseWrapper.setRequestError(
                    constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
            return false;
        }
        return true;

    }

    @Override
    protected Returnable process(ChargePaymentRequestWrapperDTO extendedRequestDTO) throws Exception {
        if (responseWrapper.getRequestError() != null) {
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            return responseWrapper;
        }

        try {
            PaymentRefundTransactionRequestBean requestBean = extendedRequestDTO.getPaymentRefundTransactionRequestBean();
            PaymentRefundTransactionRequestBean.AmountTransaction request = requestBean.getAmountTransaction();
            ChargePaymentAmount paymentAmount = request.getPaymentAmount();
            PaymentChargingInformation chargingInformation = paymentAmount.getChargingInformation();
            PaymentChargingMetaData metadata = paymentAmount.getChargingMetaData();

            String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
            String endUserIdPath = extendedRequestDTO.getEndUserId();
            String endUserIdRequest = request.getEndUserId();
            String endUserId = getLastMobileNumber(endUserIdPath);
            String amount = CommonUtil.getNullOrTrimmedValue(chargingInformation.getAmount());
            String currency = CommonUtil.getNullOrTrimmedValue(chargingInformation.getCurrency());
            String description = CommonUtil.getNullOrTrimmedValue(chargingInformation.getDescription());
            String onBehalfOf = CommonUtil.getNullOrTrimmedValue(metadata.getOnBehalfOf());
            String categoryCode = CommonUtil.getNullOrTrimmedValue(metadata.getPurchaseCategoryCode());
            String channel = CommonUtil.getNullOrTrimmedValue(metadata.getChannel());
            String taxAmount = CommonUtil.getNullOrTrimmedValue(metadata.getTaxAmount());
            String referenceCode = CommonUtil.getNullOrTrimmedValue(request.getReferenceCode());
            String transactionOperationStatus = CommonUtil.getNullOrTrimmedValue(request.getTransactionOperationStatus());
            serviceCallPayment = ServiceName.ChargeUser.toString();
            String userName = extendedRequestDTO.getUser().getUserName();
            Integer userId = extendedRequestDTO.getUser().getId();

            ChargePaymentResponseBean responseBean = new ChargePaymentResponseBean();
            ChargeAmountResponse payAmount = new ChargeAmountResponse();
            PaymentChargingInformation chargeInformation = new PaymentChargingInformation();
            PaymentChargingMetaData chargeMetaData = new PaymentChargingMetaData();

            // Save Request Log
            APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
            APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCallPayment);

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
                String tableAttributeValue = TableName.SBXATTRIBUTEVALUE.toString().toLowerCase();
                String clientCorrelatorAttribute = AttributeName.clientCorrelatorPayment.toString();
                AttributeValues duplicateClientCorrelator = paymentDAO.checkDuplicateValue(serviceCallPayment,
                        clientCorrelator, clientCorrelatorAttribute, tableAttributeValue);
                if (duplicateClientCorrelator != null) {
                    APIServiceCalls apiServiceCall = duplicateClientCorrelator.getAttributedid().getAPIServiceCall();
                    String serviceCall = apiServiceCall.getServiceName();
                    ManageNumber manageNumber = numberDAO.getNumber(endUserId, userName);
                    Integer id = duplicateClientCorrelator.getOwnerdid();
                    AttributeValues response = paymentDAO.getResponse(id);
                    if (serviceCall.equals(serviceCallPayment) && (response.getOwnerdid() == manageNumber.getId())) {
                        // return already sent response
                        ChargePaymentResponseBean obj = null;
                        obj = gson.fromJson(response.getValue(), ChargePaymentResponseBean.class);
                        ChargePaymentDTO dto = new ChargePaymentDTO();
                        dto.setAmountTransaction(obj);
                        responseWrapper.setMakePaymentDTO(dto);
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
                LOG.error("###PAYMENT### two different endUserId provided");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "two different endUserId provided"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            // check account valid amount
            if (NumberUtils.isNumber(amount) != true) {
                LOG.error("###PAYMENT### amount should be a valid positive number");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "amount should be positive number"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            // check account amount decimal format
            Double chargeAmount = Double.parseDouble(amount);
            BigDecimal bigDecimal = new BigDecimal(amount);
            Integer decimalDigits = bigDecimal.scale();
            if (!((decimalDigits <= 2) && (decimalDigits >= 0)) || chargeAmount < 0) {
                LOG.error("###PAYMENT### amount should be a whole number or two digit decimal");
                responseWrapper
                        .setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
                                "amount should be a whole or two digit decimal positive number"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            // check valid account currency for endUserId
            boolean isValidCurrency = currencySymbol(currency);
            if (!isValidCurrency) {
                LOG.error("###PAYMENT### currency code not valid accorfing to ISO 4217");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "currency code not valid accorfing to ISO 4217"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            String serviceCallBalanceLookUp = ServiceName.BalanceLookup.toString();
            String accountCurrencyAttribute = AttributeName.Currency.toString().toLowerCase();
            AttributeValues accountCurrencyValue = paymentDAO.getAttributeValue(endUserId, serviceCallBalanceLookUp,
                    accountCurrencyAttribute, userId);
            if (accountCurrencyValue != null) {
                String accountCurrency = accountCurrencyValue.getValue();
                if (!(currency.equals(accountCurrency))) {
                    LOG.error("###PAYMENT### Valid currency doesn't exists for the given inputs");
                    responseWrapper
                            .setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
                                    "Valid currency does not exist for the given input parameters"));
                    responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                    return responseWrapper;
                }
            }

            // check channel
            if (channel != null && !containsChannel(channel)) {
                LOG.error("###PAYMENT### Valid channel doesn't exists for the given inputs");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Valid channel doesn't exists for the given inputs"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            // check already charged request against reference code
            String referenceCodeAttribue = AttributeName.referenceCodePayment.toString();
            String tableNumber = TableName.NUMBERS.toString().toLowerCase();
            AttributeValues duplicateReferenceCode = paymentDAO.checkDuplicateValue(serviceCallPayment, referenceCode,
                    referenceCodeAttribue, tableNumber);
            if (duplicateReferenceCode != null) {
                LOG.error("###PAYMENT### Already charged for this reference code");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Already charged for this reference code"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            responseBean.setClientCorrelator(clientCorrelator);
            responseBean.setEndUserId(endUserIdPath);

            chargeInformation.setAmount(amount);
            chargeInformation.setCurrency(currency);
            chargeInformation.setDescription(description);

            // Get the tax Amount
            Double chargeTaxAmount = Double.parseDouble(taxAmount);

            // Setting the Total Amount Charged
            Double total = chargeTaxAmount + chargeAmount;
            payAmount.setTotalAmountCharged(total.toString());

            if (onBehalfOf != null || categoryCode != null || channel != null) {
                chargeMetaData.setPurchaseCategoryCode(categoryCode);
                chargeMetaData.setChannel(channel);
                chargeMetaData.setOnBehalfOf(onBehalfOf);
                chargeMetaData.setTaxAmount(taxAmount);
            }

            responseBean.setReferenceCode(referenceCode);
            responseBean.setServerReferenceCode(serverReferenceCode);
            responseBean.setTransactionOperationStatus(transactionOperationStatus);

            ManageNumber manageNumber = numberDAO.getNumber(endUserId,
                    extendedRequestDTO.getUser().getUserName().toString());
            Double balance = manageNumber.getBalance();
            AttributeValues transactionStatusValue = paymentDAO.getAttributeValue(endUserId, serviceCallPayment,
                    AttributeName.transactionStatus.toString(), userId);

            // transaction operation status as denied
            if ((balance < chargeAmount)) {
                LOG.error("###PAYMENT### Denied : Account balance insufficient to charge request ");
                responseWrapper.setHttpStatus(Response.Status.FORBIDDEN);
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
                // set transaction status as charged
            } else if (balance >= chargeAmount) {
                balance = balance - (chargeAmount + chargeTaxAmount);
                manageNumber.setBalance(balance);
                numberDAO.saveManageNumbers(manageNumber);
                responseBean.setTransactionOperationStatus(TransactionStatus.Charged.toString());
            }

            ChargePaymentDTO makePaymentDTO = new ChargePaymentDTO();
            payAmount.setChargingInformation(chargeInformation);
            if (onBehalfOf != null || categoryCode != null || channel != null) {
                payAmount.setChargingMetaData(chargeMetaData);
            }
            responseBean.setPaymentAmount(payAmount);
            makePaymentDTO.setAmountTransaction(responseBean);
            responseWrapper.setMakePaymentDTO(makePaymentDTO);
            responseWrapper.setHttpStatus(Response.Status.OK);

            // save payment transaction
            transactionId = saveTransaction(responseBean, endUserId, userName);

            // save client correlator
            if (clientCorrelator != null) {
                saveClientCorrelator(endUserId, clientCorrelator);
            }
            saveReferenceCode(endUserId, referenceCode, userName);

        } catch (Exception ex) {
            LOG.error("###PAYMENT### Error Occured in PAYMENT Service. ", ex);
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            responseWrapper
                    .setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
        }
        return responseWrapper;

    }


    private Integer saveTransaction(ChargePaymentResponseBean responseBean, String endUserId, String userName)
            throws Exception {
        Integer transactionId = null;
        try {
            AttributeValues valueObj = new AttributeValues();
            String tableName = TableName.NUMBERS.toString().toLowerCase();
            String attributeName = AttributeName.makePayment.toString().toLowerCase();
            APITypes api = dao.getAPIType(RequestType.PAYMENT.toString());
            APIServiceCalls call = dao.getServiceCall(api.getId(), serviceCallPayment);
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
            LOG.error("###PAYMENT### Error in processing save transaction. ", ex);
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            throw ex;
        }
        return transactionId;
    }

    public void saveClientCorrelator(String endUserId, String clientCorrelator) throws Exception {
        Integer ownerId = null;
        try {
            AttributeValues valueObj = new AttributeValues();
            String tableName = TableName.SBXATTRIBUTEVALUE.toString().toLowerCase();
            String attributeName = AttributeName.clientCorrelatorPayment.toString();
            APITypes api = dao.getAPIType(RequestType.PAYMENT.toString());
            APIServiceCalls call = dao.getServiceCall(api.getId(), serviceCallPayment);
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
            LOG.error("###PAYMENT### Error in processing save insertion of clientCorrelator request. ", ex);
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            throw ex;
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
        //changes made
    public void saveReferenceCode(String endUserId, String referenceCode, String userName) throws Exception {
        try {
            AttributeValues valueObj = new AttributeValues();
            String tableName = TableName.NUMBERS.toString().toLowerCase();
            String attributeName = AttributeName.referenceCodePayment.toString();
            APITypes api = dao.getAPIType(RequestType.PAYMENT.toString());
            APIServiceCalls call = dao.getServiceCall(api.getId(), serviceCallPayment);
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
            throw  ex;
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
