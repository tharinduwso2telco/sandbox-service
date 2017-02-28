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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wso2telco.services.dep.sandbox.dao.CreditDAO;
import com.wso2telco.services.dep.sandbox.dao.LoggingDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.*;
import com.wso2telco.services.dep.sandbox.dao.model.domain.*;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestResponseRequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.*;
import com.wso2telco.services.dep.sandbox.util.*;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundRequestBean.RefundRequest;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundResponseBean.RefundResponse;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;

public class PatialRefundRequestHandler extends AbstractRequestHandler<PatialRefundRequestWrapper> implements RequestResponseRequestHandleable<PatialRefundRequestWrapper> {

    final String REFUND_AMOUNT = "refundAmount";
    final String MSISDN = "msisdn";
    final String CLIENTCORRELATOR = "clientCorrelator";
    final String REASON = "reasonForRefund";
    final String REFERENCE = "originalServerReferenceCode";
    final String REFERENCE_CODE = "referenceCode";
    final String PAYMENT_AMOUNT = "paymentAmount";
    final String CHANGING_INFO = "chargingInformation";
    final String CHARGING_META_DATA = "chargingMetaData";
    final String REFUND_REQUEST = "refundRequest";
    final String ON_BEHALF_OF = "onBehalfOf";
    final String CATEGORY_CODE="categoryCode";
    final String CHANNEL = "channel";
    final String TAX_AMOUNT = "taxAmount";

    private NumberDAO numberDao;
    private CreditDAO creditDAO;
    private MessageLogHandler logHandler;
    private PatialRefundRequestWrapper requestWrapperDTO;
    private PatialRefundResponseWrapper responseWrapperDTO;
    private Integer correlatorid;
    private LoggingDAO loggingDao;
    private String responseReferenceCode;


    {
        LOG = LogFactory.getLog(PatialRefundRequestHandler.class);
        loggingDao = DaoFactory.getLoggingDAO();
        numberDao = DaoFactory.getNumberDAO();
        creditDAO = DaoFactory.getCreditDAO();
        dao = DaoFactory.getGenaricDAO();
        logHandler = MessageLogHandler.getInstance();
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

        if (requestBean != null && request != null) {

            double amount = request.getRefundAmount();
            String msisdn = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMsisdn());
            String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
            String reasonForRefund = CommonUtil.getNullOrTrimmedValue(request.getReasonForRefund());
            String originalServerReferenceCode = CommonUtil.getNullOrTrimmedValue(request.getOriginalServerReferenceCode());
            String onBehalfOf = CommonUtil.getNullOrTrimmedValue(metadata.getOnBehalfOf());
            String categoryCode = CommonUtil.getNullOrTrimmedValue(metadata.getPurchaseCategoryCode());
            String channel = CommonUtil.getNullOrTrimmedValue(metadata.getChannel());
            String taxAmount = CommonUtil.getNullOrTrimmedValue(metadata.getTax());
            String referenceCode = CommonUtil.getNullOrTrimmedValue(request.getReferenceCode());


            try {
                ValidationRule[] validationRules = {
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO, REFUND_AMOUNT, amount),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, MSISDN, msisdn),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, CLIENTCORRELATOR, clientCorrelator),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, REFERENCE, originalServerReferenceCode),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, REASON, reasonForRefund),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, ON_BEHALF_OF, onBehalfOf),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, CATEGORY_CODE, categoryCode),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, CHANNEL, channel),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO, TAX_AMOUNT, taxAmount),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, REFERENCE_CODE, referenceCode)

                };

                Validation.checkRequestParams(validationRules);
            } catch (CustomException ex) {
                LOG.error("###CREDIT### Error in Validation : " + ex);
                responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ex.getErrcode(),
                        ex.getErrmsg(), wrapperDTO.getMsisdn()));
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
        APITypes apiType = dao.getAPIType(RequestType.CREDIT.toString().toLowerCase());
        APIServiceCalls serviceType = dao.getServiceCall(apiType.getId(), ServiceName.PartialRefund.toString());
        JSONObject obj = buildJSONObject(request);


        double amount = request.getRefundAmount();
        String msisdn = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getMsisdn());
        String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
        String reasonForRefund = CommonUtil.getNullOrTrimmedValue(request.getReasonForRefund());
        String merchantIdentification = CommonUtil.getNullOrTrimmedValue(request.getMsisdn());
        String serverTransactionReference = CommonUtil.getNullOrTrimmedValue(request.getOriginalServerReferenceCode());
        String userName = extendedRequestDTO.getUser().getUserName();
        String serviceCreditApply = ServiceName.PartialRefund.toString();
        String channel = CommonUtil.getNullOrTrimmedValue(metadata.getChannel());
        String referenceCode = CommonUtil.getNullOrTrimmedValue(String.valueOf(request.getReferenceCode()));
        String endUserID = getLastMobileNumber(extendedRequestDTO.getMsisdn());


        String serverReferenceCodeFormat = String.format("%06d", getReferenceNumber());
        String serverReferenceCode = "PAYMENT_REF" + serverReferenceCodeFormat;
        // Save Request Log
        APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
        APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCreditApply);

        try {

            Integer userId = extendedRequestDTO.getUser().getId();
            int serviceNameId = apiServiceCalls.getApiServiceCallId();

            if (clientCorrelator != null) {

                String response = checkDuplicateClientCorrelator(clientCorrelator, userId, serviceNameId, endUserID, "1", "1", referenceCode);

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

            String result = checkReferenceCode(userId, serviceNameId, endUserID, "1", "1", referenceCode);

            if ((result != null)) {
                LOG.error("###CREDIT### Already charged for this reference code");
                responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Already used reference code"));
                responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapperDTO;
            }

            //Check channel.
            if (channel != null && !containsChannel(channel)) {
                LOG.error("###CREDIT### Valid channel doesn't exists for the given inputs");
                responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Valid channel doesn't exists for the given inputs"));
                responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
                return responseWrapperDTO;
            }

            ManageNumber manageNumber = numberDao.getNumber(endUserID, userName);
            if (manageNumber != null) {
                updateBalance(manageNumber, amount);
                RefundResponseBean responseBean = buildJsonResponseBody(amount, clientCorrelator, merchantIdentification, reasonForRefund,
                        serverTransactionReference, OperationStatus.Refunded.toString(), referenceCode, serverReferenceCode, chargingInformation, metadata);


                saveResponse(extendedRequestDTO, extendedRequestDTO.getMsisdn(), responseBean, apiServiceCalls, "1");
                responseWrapperDTO.setHttpStatus(Response.Status.OK);
                return responseWrapperDTO;
            } else {
                responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Number is not Registered for the Service"));
                responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
                return responseWrapperDTO;
            }

        } catch (Exception ex) {
            RefundResponseBean responseBean = buildJsonResponseBody(amount, clientCorrelator, merchantIdentification, reasonForRefund,
                    serverTransactionReference, OperationStatus.Refunded.toString(), referenceCode, serverReferenceCode, chargingInformation, metadata);
            saveResponse(extendedRequestDTO, extendedRequestDTO.getMsisdn(), responseBean, apiServiceCalls, "0");
            LOG.error("###CREDIT### Error in processing credit service request. ", ex);
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

    private RefundResponseBean buildJsonResponseBody(double amount, String clientCorrelator, String merchantIdentification,
                                                     String reason, String serverTransactionReference, String operationStatus, String referenceCode, String serverReferenceCode, ChargingInformation chargingInformation, ChargingMetaDataWithTax chargingMetaDataWithTax) {

        PaymentAmountWithTax paymentAmountWithTax = new PaymentAmountWithTax();
        paymentAmountWithTax.setChargingInformation(chargingInformation);
        paymentAmountWithTax.setChargingMetaData(chargingMetaDataWithTax);

        RefundResponse refundResponse = new RefundResponse();
        refundResponse.setRefundAmount(amount);
        refundResponse.setOriginalServerReferenceCode(serverTransactionReference);
        refundResponse.setClientCorrelator(clientCorrelator);
        refundResponse.setEndUserID(merchantIdentification);
        refundResponse.setReasonForRefund(reason);
        refundResponse.setPaymentAmount(paymentAmountWithTax);
        refundResponse.setReferenceCode(referenceCode);
        refundResponse.setServerReferanceCode(serverReferenceCode);
        refundResponse.setResourceURL(CommonUtil.getResourceUrl(requestWrapperDTO));
        refundResponse.setTransactionOperationStatus(operationStatus);
        RefundResponseBean refundResponseBean = new RefundResponseBean();
        refundResponseBean.setRefundResponse(refundResponse);
        responseWrapperDTO.setRefundResponseBean(refundResponseBean);
        return refundResponseBean;

    }

    @SuppressWarnings("unchecked")
    private JSONObject buildJSONObject(RefundRequest request) {

        JSONObject obj = new JSONObject();
        JSONObject refundRequest = new JSONObject();
        JSONObject payment = new JSONObject();

        payment.put(CHANGING_INFO, request.getPaymentAmount().getChargingInformation());
        payment.put(CHARGING_META_DATA, request.getPaymentAmount().getChargingMetaData());

        refundRequest.put(CLIENTCORRELATOR, request.getClientCorrelator());
        refundRequest.put(MSISDN, request.getMsisdn());
        refundRequest.put(REFERENCE, request.getOriginalServerReferenceCode());
        refundRequest.put(REASON, request.getReasonForRefund());
        refundRequest.put(REFUND_AMOUNT, request.getRefundAmount());
        refundRequest.put(PAYMENT_AMOUNT, payment);
        refundRequest.put(REFERENCE_CODE, request.getReferenceCode());

        obj.put(REFUND_REQUEST, refundRequest);
        return obj;
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
    private String checkDuplicateClientCorrelator(String clientCorrelator, int userId, int serviceNameId, String tel, String status, String type, String referenceCode) throws Exception {

        List<Integer> list = new ArrayList<>();
        list.add(serviceNameId);
        List<MessageLog> response = loggingDAO.getMessageLogs(userId, list, "msisdn", "tel:+" + tel, null, null);

        String jsonString = null;

        for (int i = 0; i < response.size(); i++) {

            if (response != null) {

                String responseStatus = response.get(i).getStatus();
                String responseType = response.get(i).getType();
                String responseClientCorrelator;

                if (responseType.equals(type) && responseStatus.equals(status)) {
                    String request = response.get(i).getRequest();
                    org.json.JSONObject json = new org.json.JSONObject(request);
                    responseClientCorrelator = null;

                    if (json.getJSONObject("refundResponse").has("clientCorrelator")) {
                        responseClientCorrelator = json.getJSONObject("refundResponse").get("clientCorrelator").toString();
                    }

                    // responseReferenceCode = json.getJSONObject("refundResponse").get("referenceCode").toString();

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
    private String checkReferenceCode(int userId, int serviceNameId, String tel, String status, String type, String referenceCode) throws Exception {

        List<Integer> list = new ArrayList<>();
        list.add(serviceNameId);
        List<MessageLog> response = loggingDAO.getMessageLogs(userId, list, "msisdn", "tel:+" + tel, null, null);

        String jsonString = null;

        for (int i = 0; i < response.size(); i++) {

            if (response != null) {

                String responseStatus = response.get(i).getStatus();
                String responseType = response.get(i).getType();

                if (responseType.equals(type) && responseStatus.equals(status)) {
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

    // Save Response in messageLog table
    private void saveResponse(PatialRefundRequestWrapper extendedRequestDTO,
                              String endUserIdPath, RefundResponseBean responseBean, APIServiceCalls apiServiceCalls, String status) throws Exception {

        String jsonInString = null;
        Gson resp = new Gson();

        JsonElement je = new JsonParser().parse(resp.toJson(responseBean));
        JsonObject asJsonObject = je.getAsJsonObject();
        jsonInString = asJsonObject.toString();

        //setting messagelog responses
        MessageLog messageLog1 = new MessageLog();
        messageLog1 = new MessageLog();
        messageLog1.setRequest(jsonInString);
        messageLog1.setStatus(status);
        messageLog1.setType("1");
        messageLog1.setServicenameid(apiServiceCalls.getApiServiceCallId());
        messageLog1.setUserid(extendedRequestDTO.getUser().getId());
        messageLog1.setReference("msisdn");
        messageLog1.setValue(endUserIdPath);
        messageLog1.setMessageTimestamp(new Date());

        loggingDAO.saveMessageLog(messageLog1);
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
    public String getnumber(PatialRefundRequestWrapper requestDTO) {
        return requestDTO.getMsisdn();
    }
}
