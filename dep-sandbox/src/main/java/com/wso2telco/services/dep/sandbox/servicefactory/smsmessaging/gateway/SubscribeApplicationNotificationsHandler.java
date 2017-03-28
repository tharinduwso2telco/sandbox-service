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
import com.wso2telco.services.dep.sandbox.dao.model.custom.DeliveryReceiptSubscription;
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

class SubscribeApplicationNotificationsHandler extends
        AbstractRequestHandler<SubscribeApplicationNotificationsRequestWrapperGateway> implements AddressIgnorerable,
        RequestResponseRequestHandleable<SubscribeApplicationNotificationsRequestWrapperGateway> {

    private SubscribeApplicationNotificationsResponseWrapper responseWrapper;
    private SubscribeApplicationNotificationsRequestWrapperGateway requestWrapper;
    private SMSMessagingDAO smsMessagingDAO;
    private Gson gson = new GsonBuilder().serializeNulls().create();


    {
        LOG = LogFactory.getLog(SubscribeApplicationNotificationsHandler.class);
        smsMessagingDAO = DaoFactory.getSMSMessagingDAO();
    }


    @Override
    protected Returnable getResponseDTO() {
        return responseWrapper;
    }

    @Override
    protected List<String> getAddress() {
        return null;
    }

    @Override
    protected boolean validate(SubscribeApplicationNotificationsRequestWrapperGateway wrapperDTO) throws Exception {

        SubscribeApplicationNotificationsRequestBeanGateway subscribeApplicationNotificationsRequestBean = wrapperDTO
                .getSubscribeApplicationNotificationsRequestBean();

        String callbackData = CommonUtil.getNullOrTrimmedValue(subscribeApplicationNotificationsRequestBean
                .getSubscription().getCallbackReference().getCallbackData());
        String clientCorrelator = CommonUtil.getNullOrTrimmedValue(subscribeApplicationNotificationsRequestBean
                .getSubscription().getClientCorrelator());
        String criteria = CommonUtil.getNullOrTrimmedValue(subscribeApplicationNotificationsRequestBean
                .getSubscription().getCriteria());
        String destinationAddress = CommonUtil.getNullOrTrimmedValue(subscribeApplicationNotificationsRequestBean
                .getSubscription().getDestinationAddress());
        String notificationFormat = CommonUtil.getNullOrTrimmedValue(subscribeApplicationNotificationsRequestBean
                .getSubscription().getNotificationFormat());
        String notifyURL = CommonUtil.getNullOrTrimmedValue(subscribeApplicationNotificationsRequestBean
                .getSubscription().getCallbackReference().getNotifyURL());

        List<ValidationRule> validationRulesList = new ArrayList<>();

        try {
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "callbackData",
                    callbackData));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "criteria", criteria));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator",
                    clientCorrelator));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
                    "destinationAddress", destinationAddress));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "notifyURL",
                    notifyURL));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notificationFormat",
                    notificationFormat));

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
    protected Returnable process(SubscribeApplicationNotificationsRequestWrapperGateway extendedRequestDTO) throws
            Exception {

        if (responseWrapper.getRequestError() != null) {
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            return responseWrapper;
        }

        try {

            User user = extendedRequestDTO.getUser();
            String clientCorrelator = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO
                    .getSubscribeApplicationNotificationsRequestBean().getSubscription().getClientCorrelator());
            Integer userId = extendedRequestDTO.getUser().getId();

            APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
            String serviceCallPayment = ServiceName.SubscribeToApplication.toString();
            APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCallPayment);
            int serviceNameId = apiServiceCalls.getApiServiceCallId();
            String endUserId = extendedRequestDTO.getSubscribeApplicationNotificationsRequestBean().getSubscription()
                    .getDestinationAddress();

            String destinationAddress = extendedRequestDTO.getSubscribeApplicationNotificationsRequestBean()
                    .getSubscription().getDestinationAddress();
            String criteria = extendedRequestDTO.getSubscribeApplicationNotificationsRequestBean().getSubscription()
                    .getCriteria();
            String notifyURL = extendedRequestDTO.getSubscribeApplicationNotificationsRequestBean().getSubscription()
                    .getCallbackReference().getNotifyURL();
            String callbackData = extendedRequestDTO.getSubscribeApplicationNotificationsRequestBean()
                    .getSubscription().getCallbackReference().getCallbackData();

            String notificationFormat = extendedRequestDTO.getSubscribeApplicationNotificationsRequestBean()
                    .getSubscription().getNotificationFormat();


            if (clientCorrelator != null) {
                String response = checkDuplicateClientCorrelator(clientCorrelator, userId, serviceNameId, endUserId,
                        MessageProcessStatus.Success, MessageType.Response);
                if (response != null) {
                    // return already sent response
                    SubscribeApplicationNotificationsResponseBean obj;
                    obj = gson.fromJson(response, SubscribeApplicationNotificationsResponseBean.class);
                    responseWrapper.setResponseBean(obj);
                    responseWrapper.setHttpStatus(Response.Status.OK);
                    return responseWrapper;

                }
            }

            if (!dao.isWhiteListedSenderAddress(user.getId(), getLastMobileNumber(destinationAddress))) {
                LOG.error("###SMS### Destination Address is not WhiteListed ");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Destination Address is not WhiteListed"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }

            for (int i = 0; i < user.getSubscribeSMSRequestList().size(); i++) {

                if (destinationAddress.equals(user.getSubscribeSMSRequestList().get(i).getDestinationAddress()) &&
                        criteria.equals(user.getSubscribeSMSRequestList().get(i).getCriteria())) {
                    LOG.error("###SMS### Overlapped criteria ");
                    responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                            ServiceError.INVALID_INPUT_VALUE, "Overlapped criteria"));
                    responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                    return responseWrapper;
                }
            }

            SubscribeSMSRequest subscribeSMSRequest = new SubscribeSMSRequest();
            subscribeSMSRequest.setCallbackData(callbackData);
            subscribeSMSRequest.setClientCorrelator(clientCorrelator);
            subscribeSMSRequest.setCriteria(criteria);
            subscribeSMSRequest.setDestinationAddress(destinationAddress);
            subscribeSMSRequest.setNotifyURL(notifyURL);
            subscribeSMSRequest.setUser(user);
            subscribeSMSRequest.setDate(new Date());
            subscribeSMSRequest.setNotificationFormat("JSON");
            Integer subsId = smsMessagingDAO.saveSubscribeSMSRequest(subscribeSMSRequest);

            String resourceURLMaker = CommonUtil.getResourceUrl(extendedRequestDTO);
            int index = resourceURLMaker.lastIndexOf('/');
            String resourceURL = resourceURLMaker.substring(0, index) + "/" + subsId;

            SubscribeApplicationNotificationsResponseBean responseBean = new
                    SubscribeApplicationNotificationsResponseBean();
            SubscribeApplicationNotificationsResponseBean.SubscribeApplicationNotificationsResponse
                    applicationNotificationsResponse = new SubscribeApplicationNotificationsResponseBean
                    .SubscribeApplicationNotificationsResponse();

            applicationNotificationsResponse.setClientCorrelator(clientCorrelator);
            applicationNotificationsResponse.setCriteria(criteria);
            applicationNotificationsResponse.setDestinationAddress(destinationAddress);
            applicationNotificationsResponse.setResourceURL(resourceURL);
            applicationNotificationsResponse.setNotificationFormat(notificationFormat);

            SubscribeApplicationNotificationsResponseBean.SubscribeApplicationNotificationsResponse.CallbackReference
                    callbackReference = new DeliveryReceiptSubscription.CallbackReference();
            callbackReference.setCallbackData(callbackData);
            callbackReference.setNotifyURL(notifyURL);

            applicationNotificationsResponse.setCallbackReference(callbackReference);

            responseBean.setSubscription(applicationNotificationsResponse);

            responseWrapper.setResponseBean(responseBean);
            responseWrapper.setHttpStatus(Response.Status.CREATED);

            // Save Success Response
            saveResponse(endUserId, responseBean, apiServiceCalls, MessageProcessStatus.Success);

        } catch (Exception ex) {
            LOG.error("###SMS### Error Occurred in Subscribe Application Notifications.", ex);
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            responseWrapper
                    .setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED,
                            "###SMS### Error Occurred in Subscribe Application Notifications."));
        }


        return responseWrapper;
    }

    @Override
    protected void init(SubscribeApplicationNotificationsRequestWrapperGateway extendedRequestDTO) throws Exception {

        responseWrapper = new SubscribeApplicationNotificationsResponseWrapper();
        this.requestWrapper = extendedRequestDTO;

    }


    private String checkDuplicateClientCorrelator(String clientCorrelator, int userId, int serviceNameId, String tel,
                                                  MessageProcessStatus status, MessageType type) throws Exception {

        List<Integer> list = new ArrayList<>();
        list.add(serviceNameId);

        List<MessageLog> response = loggingDAO.getMessageLogs(userId, list, "destinationAddress", getLastMobileNumber(tel), null, null);

        String jsonString = null;

        for (MessageLog aResponse : response) {

            int responseStatus = aResponse.getStatus();
            int responseType = aResponse.getType();
            String responseClientCorrelator = null;

            if (responseType == type.getValue() && responseStatus == status.getValue()) {
                String request = aResponse.getRequest();
                JSONObject json = new JSONObject(request);

                if (json.getJSONObject("subscription").has("clientCorrelator")) {

                    responseClientCorrelator = json.getJSONObject("subscription").get("clientCorrelator").toString();
                }
                int responseUserId = aResponse.getUserid();
                String responseTel = aResponse.getValue();

                // check for duplicate clientCorrelators
                if ((responseClientCorrelator != null && responseClientCorrelator.equals(clientCorrelator)) &&
                        responseUserId == userId && responseTel.equals(getLastMobileNumber(tel))) {
                    jsonString = json.toString();
                    break;
                }
            }

        }
        return jsonString;
    }


    private void saveResponse(String endUserIdPath, SubscribeApplicationNotificationsResponseBean responseBean,
                              APIServiceCalls
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
        messageLog.setUserid(user.getId());
        messageLog.setReference("destinationAddress");
        messageLog.setValue(getLastMobileNumber(endUserIdPath));
        messageLog.setMessageTimestamp(new Date());

        loggingDAO.saveMessageLog(messageLog);
    }

    @Override
    public String getApiServiceCalls() {
        try {
            return ServiceName.SubscribeToApplication.toString();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String getJosonString(SubscribeApplicationNotificationsRequestWrapperGateway requestDTO) {
        Gson gson = new Gson();
        return gson.toJson(requestDTO.getSubscribeApplicationNotificationsRequestBean());
    }

    @Override
    public String getnumber(SubscribeApplicationNotificationsRequestWrapperGateway requestDTO) {
        return requestDTO.getSubscribeApplicationNotificationsRequestBean().getSubscription().getDestinationAddress();
    }
}
