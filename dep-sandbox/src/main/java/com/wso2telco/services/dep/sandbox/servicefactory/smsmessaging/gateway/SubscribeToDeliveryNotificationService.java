package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.gateway;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.SMSMessagingDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.DeliveryReceiptSubscription;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.servicefactory.*;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class SubscribeToDeliveryNotificationService extends
        AbstractRequestHandler<SubscribeToDeliveryNotificationWrapperDTOGateway> implements AddressIgnorerable,
        RequestResponseRequestHandleable<SubscribeToDeliveryNotificationWrapperDTOGateway> {

    private SMSMessagingDAO smsMessagingDAO;
    private SubscribeToDeliveryNotificationWrapperDTOGateway requestWrapperDTOGateway;
    private SubscribeToDeliveryNotificationResponseWrapper responseWrapper;
    private String serviceCallSMS;
    private String jsonRequest;
    private static final int SUBSCRIPTION_STATUS = 1;

    {
        LOG = LogFactory.getLog(SubscribeToDeliveryNotificationService.class);
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
    protected void init(SubscribeToDeliveryNotificationWrapperDTOGateway extendedRequestDTO) throws Exception {
        responseWrapper = new SubscribeToDeliveryNotificationResponseWrapper();
        requestWrapperDTOGateway = extendedRequestDTO;
    }

    @Override
    protected boolean validate(SubscribeToDeliveryNotificationWrapperDTOGateway wrapperDTO) throws Exception {
        SubscribeToDeliveryNotificationRequestBeanGateway requestBean = wrapperDTO
                .getSubscribeToDeliveryNotificationRequestBeanGateway();
        SubscribeToDeliveryNotificationRequestBeanGateway.DeliveryReceiptSubscriptionBean request = requestBean
                .getDeliveryReceiptSubscription();
        DeliveryReceiptSubscription.CallbackReference callbackReference = request.getCallbackReference();

        String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
        String filterCriteria = CommonUtil.getNullOrTrimmedValue(request.getFilterCriteria());
        String callbackData = CommonUtil.getNullOrTrimmedValue(callbackReference.getCallbackData());
        String notifyURL = CommonUtil.getNullOrTrimmedValue(callbackReference.getNotifyURL());

        List<ValidationRule> validationRulesList = new ArrayList<>();

        try {

            validationRulesList.add(
                    new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator));
            validationRulesList.add(
                    new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "filterCriteria", filterCriteria));
            validationRulesList.add(
                    new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData));
            validationRulesList.add(
                    new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "notifyURL", notifyURL));

            ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
            validationRules = validationRulesList.toArray(validationRules);

            Validation.checkRequestParams(validationRules);

        } catch (CustomException ex) {
            LOG.error("###SMS SUBSCRIPTION### Error in Validations. ", ex);
            responseWrapper.setRequestError(
                    constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
            return false;
        }

        return true;
    }

    @Override
    protected Returnable process(SubscribeToDeliveryNotificationWrapperDTOGateway extendedRequestDTO) throws Exception {
        if (responseWrapper.getRequestError() != null) {
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            return responseWrapper;
        }

        SubscribeToDeliveryNotificationRequestBeanGateway requestBean = extendedRequestDTO
                .getSubscribeToDeliveryNotificationRequestBeanGateway();
        SubscribeToDeliveryNotificationRequestBeanGateway.DeliveryReceiptSubscriptionBean request = requestBean
                .getDeliveryReceiptSubscription();
        DeliveryReceiptSubscription.CallbackReference callbackReference = request.getCallbackReference();

        serviceCallSMS = ServiceName.SubscribeToSMSDelivery.toString();
        APITypes apiTypes = dao.getAPIType(RequestType.SMSMESSAGING.toString());
        APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCallSMS);

        Gson gson = new Gson();

        jsonRequest = gson.toJson(requestBean);

        int serviceNameId = apiServiceCalls.getApiServiceCallId();

        String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
        String senderAddress = requestWrapperDTOGateway.getSenderAddress();
        String filterCriteria = CommonUtil.getNullOrTrimmedValue(request.getFilterCriteria());
        String callbackData = CommonUtil.getNullOrTrimmedValue(callbackReference.getCallbackData());
        String notifyURL = CommonUtil.getNullOrTrimmedValue(callbackReference.getNotifyURL());
        User user = extendedRequestDTO.getUser();
        Integer userId = extendedRequestDTO.getUser().getId();

        if (clientCorrelator != null) {

            String response = checkDuplicateClientCorrelator(clientCorrelator, userId, serviceNameId, senderAddress,
                    MessageProcessStatus.Success, MessageType.Response);

            if (response != null) {
                // send already saved response
                SubscribeToDeliveryNotificationResponseBean object = null;
                object = gson.fromJson(response, SubscribeToDeliveryNotificationResponseBean.class);
                SubscribeToDeliveryDTO dto = new SubscribeToDeliveryDTO();
                dto.setDeliveryReceiptSubscription(object);
                responseWrapper.setSubcribeToDeliveryDTO(dto);
                responseWrapper.setHttpStatus(Response.Status.OK);
                return responseWrapper;

            }
        }

        SubscribeToDeliveryNotificationResponseBean responseBean = new SubscribeToDeliveryNotificationResponseBean();
        CallReferenceResponse response = new CallReferenceResponse();

        responseBean.setClientCorrelator(clientCorrelator);
        responseBean.setFilterCriteria(filterCriteria);
        responseBean.setResourceURL(CommonUtil.getResourceUrl(extendedRequestDTO));
        response.setCallbackData(callbackData);
        response.setNotifyURL(notifyURL);

        responseWrapper.setHttpStatus(Response.Status.CREATED);
        SubscribeToDeliveryDTO dto = new SubscribeToDeliveryDTO();
        dto.setDeliveryReceiptSubscription(responseBean);
        response.setNotifyURL(notifyURL);
        response.setCallbackData(callbackData);
        responseBean.setCallbackReference(response);
        responseWrapper.setSubcribeToDeliveryDTO(dto);

        SubscribeToDeliveryDTO toDeliveryDTO = new SubscribeToDeliveryDTO();
        toDeliveryDTO.setDeliveryReceiptSubscription(responseBean);

        if (smsMessagingDAO.isSubscriptionExists(filterCriteria, notifyURL, callbackData, clientCorrelator)) {

            String resp = gson.toJson(responseBean);

            SubscribeToDeliveryNotificationResponseBean object = null;

            object = gson.fromJson(resp, SubscribeToDeliveryNotificationResponseBean.class);
            SubscribeToDeliveryDTO dto1 = new SubscribeToDeliveryDTO();
            dto1.setDeliveryReceiptSubscription(object);
            responseWrapper.setSubcribeToDeliveryDTO(dto);
            responseWrapper.setHttpStatus(Response.Status.CREATED);
            return responseWrapper;
        } else {
            smsMessagingDAO.saveDeliverySubscription(user, senderAddress, SUBSCRIPTION_STATUS, notifyURL,
                    filterCriteria,
                    callbackData, clientCorrelator, jsonRequest);
            saveResponse(userId, senderAddress, responseBean, apiServiceCalls, MessageProcessStatus.Success);
        }

        return responseWrapper;
    }

    private void saveResponse(Integer userId, String senderAddress, SubscribeToDeliveryNotificationResponseBean
            responseBean, APIServiceCalls apiServiceCalls, MessageProcessStatus status) throws Exception {

        String jsonInString = null;
        Gson resp = new Gson();

        JsonElement je = new JsonParser().parse(resp.toJson(responseBean));
        JsonObject asJsonObject = je.getAsJsonObject();
        jsonInString = asJsonObject.toString();

        MessageLog messageLog = new MessageLog();
        messageLog = new MessageLog();
        messageLog.setRequest(jsonInString);
        messageLog.setStatus(status.getValue());
        messageLog.setType(MessageType.Response.getValue());
        messageLog.setServicenameid(apiServiceCalls.getApiServiceCallId());
        messageLog.setUserid(userId);
        messageLog.setReference("senderAddress");
        messageLog.setValue(getLastMobileNumber(senderAddress));
        messageLog.setMessageTimestamp(new Date());

        loggingDAO.saveMessageLog(messageLog);
    }

    private String checkDuplicateClientCorrelator(String clientCorrelator, int userId, int serviceNameId, String
            senderAddress, MessageProcessStatus status, MessageType type) throws Exception {

        List<Integer> list = new ArrayList<>();
        list.add(serviceNameId);
        List<MessageLog> response = loggingDAO.getMessageLogs(userId, list, "senderAddress", getLastMobileNumber(senderAddress), null, null);

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

                    int responseUserId = response.get(i).getUserid();

                    // Check client correlator
                    if ((responseClientCorrelator != null && responseClientCorrelator.equals(clientCorrelator)) &&
                            responseUserId == userId) {
                        jsonString = json.toString();
                        break;
                    }
                }

            }
        }
        return jsonString;
    }

    @Override
    public String getApiServiceCalls() {
        return ServiceName.SubscribeToSMSDelivery.toString();
    }

    @Override
    public String getJosonString(SubscribeToDeliveryNotificationWrapperDTOGateway requestDTO) {
        Gson gson = new Gson();
        return gson.toJson(requestDTO.getSubscribeToDeliveryNotificationRequestBeanGateway());
    }

    @Override
    public String getnumber(SubscribeToDeliveryNotificationWrapperDTOGateway requestDTO) {
        return requestDTO.getSenderAddress();
    }
}
