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
package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.gateway;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.SMSMessagingDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.*;
import com.wso2telco.services.dep.sandbox.dao.model.domain.*;
import com.wso2telco.services.dep.sandbox.servicefactory.*;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class SendMTSMSService extends AbstractRequestHandler<SendMTSMSRequestWrapperDTOGateway> implements
        RequestResponseRequestHandleable<SendMTSMSRequestWrapperDTOGateway> {

    private Gson gson = new GsonBuilder().serializeNulls().create();
    private SendMTSMSRequestWrapperDTOGateway extendedRequestDTO;
    private SendMTSMSResponseWrapper responseWrapper;
    private SMSMessagingDAO smsMessagingDAO;
    private String resourceURL;


    {
        LOG = LogFactory.getLog(SendMTSMSService.class);
        smsMessagingDAO = DaoFactory.getSMSMessagingDAO();
    }


    @Override
    protected Returnable getResponseDTO() {
        return responseWrapper;
    }

    @Override
    protected List<String> getAddress() {
        List<String> addresses;
        addresses = extendedRequestDTO.getOutboundSMSMessageRequestBeanGw().getOutboundSMSMessageRequest().getAddress();
        return addresses;
    }

    @Override
    protected boolean validate(SendMTSMSRequestWrapperDTOGateway wrapperDTO) throws Exception {
        OutboundSMSMessageRequestBeanGateway outboundSMSMessageRequestBeanGateway = wrapperDTO
                .getOutboundSMSMessageRequestBeanGw();

        String address = CommonUtil.getNullOrTrimmedValue(outboundSMSMessageRequestBeanGateway
                .getOutboundSMSMessageRequest().getAddress().toString());
        String callBackData = CommonUtil.getNullOrTrimmedValue(outboundSMSMessageRequestBeanGateway
                .getOutboundSMSMessageRequest().getReceiptRequest().getCallbackData());
        String clientCorrelator = CommonUtil.getNullOrTrimmedValue(outboundSMSMessageRequestBeanGateway
                .getOutboundSMSMessageRequest().getClientCorrelator());
        String message = CommonUtil.getNullOrTrimmedValue(outboundSMSMessageRequestBeanGateway
                .getOutboundSMSMessageRequest().getOutboundSMSTextMessage().getMessage());
        String notifyURL = CommonUtil.getNullOrTrimmedValue(outboundSMSMessageRequestBeanGateway
                .getOutboundSMSMessageRequest().getReceiptRequest().getNotifyURL());
        String senderAddress = CommonUtil.getNullOrTrimmedValue(outboundSMSMessageRequestBeanGateway
                .getOutboundSMSMessageRequest().getSenderAddress());
        String senderName = CommonUtil.getNullOrTrimmedValue(outboundSMSMessageRequestBeanGateway
                .getOutboundSMSMessageRequest().getSenderName());


        List<ValidationRule> validationRulesList = new ArrayList<>();

        try {
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "address", address));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callBackData", callBackData));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "message", message));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notifyURL", notifyURL));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "senderName", senderName));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "senderAddress", senderAddress));

            ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
            validationRules = validationRulesList.toArray(validationRules);

            Validation.checkRequestParams(validationRules);

        } catch (CustomException ex) {
            LOG.error("###SMS### Error in Validations. ", ex);
            responseWrapper.setRequestError(
                    constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
            return false;
        }
        return true;
    }

    @Override
    protected Returnable process(SendMTSMSRequestWrapperDTOGateway extendedRequestDTO) throws Exception {

        if (responseWrapper.getRequestError() != null) {
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            return responseWrapper;
        }
        try {
            User user = extendedRequestDTO.getUser();
            SMSMessagingParam smsMessagingParam = smsMessagingDAO.getSMSMessagingParam(user.getId());
            APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
            String serviceCallPayment = ServiceName.SendSMS.toString();
            APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCallPayment);
            String clientCorrelator = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO
                    .getOutboundSMSMessageRequestBeanGw().getOutboundSMSMessageRequest().getClientCorrelator());
            Integer userId = extendedRequestDTO.getUser().getId();
            int serviceNameId = apiServiceCalls.getApiServiceCallId();
            String endUserId = extendedRequestDTO.getOutboundSMSMessageRequestBeanGw().getOutboundSMSMessageRequest()
                    .getAddress().toString();

            String senderAddress = extendedRequestDTO.getOutboundSMSMessageRequestBeanGw().getOutboundSMSMessageRequest().getSenderAddress();
            String senderAddressOnly= senderAddress.replaceAll("[^0-9]", "");



            OutboundSMSMessageRequestBeanGateway requestBean = extendedRequestDTO.getOutboundSMSMessageRequestBeanGw();

            OutboundSMSMessageResponseBean responseBean = new OutboundSMSMessageResponseBean();

            OutboundSMSMessageResponseBean.OutboundSMSMessageResponseGateway smsMessageResponse = new
                    OutboundSMSMessageResponseBean.OutboundSMSMessageResponseGateway();


            if (clientCorrelator != null) {
                String response = checkDuplicateClientCorrelator(clientCorrelator, userId, serviceNameId, endUserId,
                        MessageProcessStatus.Success, MessageType.Response);
                if (response != null) {

                    // return already sent response
                    OutboundSMSMessageResponseBean obj;
                    obj = gson.fromJson(response, OutboundSMSMessageResponseBean.class);
                    responseWrapper.setOutboundSMSMessageResponseBeanGw(obj);
                    responseWrapper.setHttpStatus(Response.Status.OK);
                    return responseWrapper;

                }
            }

            if(!dao.isWhiteListedSenderAddress(user.getId(), senderAddressOnly)){
                LOG.error("###SMS### sender address is not WhiteListed ");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Sender Address is not WhiteListed"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            smsMessageResponse.setResourceURL(getoutSideResourceURL(getReferenceNumber()));
            smsMessageResponse.setSenderAddress(requestBean.getOutboundSMSMessageRequest().getSenderAddress());
            smsMessageResponse.setReceiptRequest(requestBean.getOutboundSMSMessageRequest().getReceiptRequest());
            smsMessageResponse.setSenderAddress(requestBean.getOutboundSMSMessageRequest().getSenderAddress());
            smsMessageResponse.setOutboundSMSTextMessage(requestBean.getOutboundSMSMessageRequest()
                    .getOutboundSMSTextMessage());
            smsMessageResponse.setClientCorrelator(requestBean.getOutboundSMSMessageRequest().getClientCorrelator());
            smsMessageResponse.setAddress(requestBean.getOutboundSMSMessageRequest().getAddress());
            smsMessageResponse.setSenderName(requestBean.getOutboundSMSMessageRequest().getSenderName());

            DeliveryInfoList deliveryInfoList = new DeliveryInfoList();
            deliveryInfoList.setResourceURL(getinSideResourceURL(getReferenceNumber()));
            List<DeliveryInfoList.DeliveryInfo> infoList = new ArrayList();

            for (String s : requestBean.getOutboundSMSMessageRequest().getAddress()) {

                DeliveryInfoList.DeliveryInfo deliveryInfo = new DeliveryInfoList.DeliveryInfo();
                deliveryInfo.setAddress(s);
                deliveryInfo.setDeliveryStatus(smsMessagingParam.getDeliveryStatus());
                infoList.add(deliveryInfo);
            }

            deliveryInfoList.setDeliveryInfo(infoList);
            smsMessageResponse.setDeliveryInfoList(deliveryInfoList);
            responseBean.setOutboundSMSMessageRequest(smsMessageResponse);

            responseWrapper.setOutboundSMSMessageResponseBeanGw(responseBean);
            responseWrapper.setHttpStatus(Response.Status.CREATED);

            // Save Success Response
            saveResponse(endUserId, responseBean, apiServiceCalls, MessageProcessStatus.Success);

        } catch (Exception ex) {
            LOG.error("###SMS### Error Occurred in SMS Service. ", ex);
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            responseWrapper
                    .setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED,
                            "Error Occurred in SMS Service."));
        }

        return responseWrapper;


    }

    @Override
    protected void init(SendMTSMSRequestWrapperDTOGateway extendedRequestDTO) throws Exception {

        responseWrapper = new SendMTSMSResponseWrapper();
        this.extendedRequestDTO = extendedRequestDTO;
        resourceURL = CommonUtil.getResourceUrl(extendedRequestDTO);

    }


    /**
     * Generating inside resource URL.
     *
     * @param mtSMSTransactionId Unique ID
     * @return URL
     */
    private String getinSideResourceURL(int mtSMSTransactionId) {

        int index = resourceURL.lastIndexOf('/');
        String resourceURLs = resourceURL.substring(0, index) + "/" + (mtSMSTransactionId + 1);
        return resourceURLs + "/deliveryInfos";
    }

    /**
     * Genarating outside resource URL.
     *
     * @param mtSMSTransactionId Unique Id.
     * @return URL
     */
    private String getoutSideResourceURL(int mtSMSTransactionId) {

        int index = resourceURL.lastIndexOf('/');
        String resourceURLs = resourceURL.substring(0, index) + "/" + (mtSMSTransactionId + 1);
        return resourceURLs;
    }


    // Save Response in messageLog table
    private void saveResponse(String endUserIdPath, OutboundSMSMessageResponseBean responseBean, APIServiceCalls
            apiServiceCalls, MessageProcessStatus status) throws Exception {

        Gson gson = new Gson();
        String jsonString = gson.toJson(responseBean);

        //setting messagelog responses
        new MessageLog();
        MessageLog messageLog;
        messageLog = new MessageLog();
        messageLog.setRequest(jsonString);
        messageLog.setStatus(status.getValue());
        messageLog.setType(MessageType.Response.getValue());
        messageLog.setServicenameid(apiServiceCalls.getApiServiceCallId());
        messageLog.setUserid(extendedRequestDTO.getUser().getId());
        messageLog.setReference("msisdn");
        messageLog.setValue(endUserIdPath);
        messageLog.setMessageTimestamp(new Date());

        loggingDAO.saveMessageLog(messageLog);
    }


    private String checkDuplicateClientCorrelator(String clientCorrelator, int userId, int serviceNameId, String tel,
                                                  MessageProcessStatus status, MessageType type) throws Exception {

        List<Integer> list = new ArrayList<>();
        list.add(serviceNameId);

        List<MessageLog> response = loggingDAO.getMessageLogs(userId, list, "msisdn", tel, null, null);

        String jsonString = null;

        for (MessageLog aResponse : response) {

            int responseStatus = aResponse.getStatus();
            int responseType = aResponse.getType();
            String responseClientCorrelator = null;

            if (responseType == type.getValue() && responseStatus == status.getValue()) {
                String request = aResponse.getRequest();
                JSONObject json = new JSONObject(request);

                if(json.getJSONObject("outboundSMSMessageRequest").has("clientCorrelator")) {
                    responseClientCorrelator = json.getJSONObject("outboundSMSMessageRequest").get
                            ("clientCorrelator").toString();
                }
                int responseUserId = aResponse.getUserid();
                String responseTel = aResponse.getValue();

                // check for duplicate clientCorrelators
                if ((responseClientCorrelator != null && responseClientCorrelator.equals(clientCorrelator)) &&
                        responseUserId == userId && responseTel.equals(tel)) {
                    jsonString = json.toString();
                    break;
                }
            }

        }
        return jsonString;
    }


    @Override
    public String getApiServiceCalls() {
        try {
            return ServiceName.SendSMS.toString();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String getJosonString(SendMTSMSRequestWrapperDTOGateway requestDTO) {
        Gson gson = new Gson();
        return gson.toJson(requestDTO.getOutboundSMSMessageRequestBeanGw());
    }

    @Override
    public String getnumber(SendMTSMSRequestWrapperDTOGateway requestDTO) {
        return requestDTO.getOutboundSMSMessageRequestBeanGw().getOutboundSMSMessageRequest().getAddress().toString();
    }
}
