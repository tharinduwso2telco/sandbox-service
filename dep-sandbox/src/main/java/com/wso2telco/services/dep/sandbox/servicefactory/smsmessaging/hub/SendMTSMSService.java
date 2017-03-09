package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.hub;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.google.gson.*;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.model.custom.*;
import com.wso2telco.services.dep.sandbox.dao.model.domain.*;
import com.wso2telco.services.dep.sandbox.servicefactory.*;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.SMSMessagingDAO;
import org.json.JSONObject;

  class SendMTSMSService extends AbstractRequestHandler<sendMTSMSRequestWrapperDTOHub> implements
        RequestResponseRequestHandleable<sendMTSMSRequestWrapperDTOHub> {

    private Gson gson = new GsonBuilder().serializeNulls().create();
    private sendMTSMSRequestWrapperDTOHub extendedRequestDTO;
    private SendMTSMSResponseWrapper responseWrapper;
    private SMSMessagingDAO smsMessagingDAO;

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
        addresses = extendedRequestDTO.getOutboundSMSMessageRequestBean().getOutboundSMSMessageRequest().getAddress();
        return addresses;
    }

    @Override
    protected void init(sendMTSMSRequestWrapperDTOHub extendedRequestDTO) throws Exception {

        responseWrapper = new SendMTSMSResponseWrapper();
        this.extendedRequestDTO = extendedRequestDTO;
    }


    @Override
    protected boolean validate(sendMTSMSRequestWrapperDTOHub wrapperDTO) throws Exception {

        outboundSMSMessageRequestBeanHub outboundSMSMessageRequestBean_hub = wrapperDTO
                .getOutboundSMSMessageRequestBean();

        String address = CommonUtil.getNullOrTrimmedValue(outboundSMSMessageRequestBean_hub
                .getOutboundSMSMessageRequest().getAddress().toString());
        String callBackData = CommonUtil.getNullOrTrimmedValue(outboundSMSMessageRequestBean_hub
                .getOutboundSMSMessageRequest().getReceiptRequest().getCallbackData());
        String clientCorrelator = CommonUtil.getNullOrTrimmedValue(outboundSMSMessageRequestBean_hub
                .getOutboundSMSMessageRequest().getClientCorrelator());
        String message = CommonUtil.getNullOrTrimmedValue(outboundSMSMessageRequestBean_hub
                .getOutboundSMSMessageRequest().getOutboundSMSTextMessage().getMessage());
        String notifyURL = CommonUtil.getNullOrTrimmedValue(outboundSMSMessageRequestBean_hub
                .getOutboundSMSMessageRequest().getReceiptRequest().getNotifyURL());


        List<ValidationRule> validationRulesList = new ArrayList<>();

        try {
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "address", address));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callBackData",
                    callBackData));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator",
                    clientCorrelator));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "message", message));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notifyURL",
                    notifyURL));

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
    protected Returnable process(sendMTSMSRequestWrapperDTOHub extendedRequestDTO) throws Exception {

        try {
            User user = extendedRequestDTO.getUser();
            SMSMessagingParam smsMessagingParam = smsMessagingDAO.getSMSMessagingParam(user.getId());
            APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
            String serviceCallPayment = ServiceName.SendSMS.toString();
            APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCallPayment);
            String clientCorrelator = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO
                    .getOutboundSMSMessageRequestBean().getOutboundSMSMessageRequest().getClientCorrelator());
            Integer userId = extendedRequestDTO.getUser().getId();
            int serviceNameId = apiServiceCalls.getApiServiceCallId();
            String endUserId = extendedRequestDTO.getOutboundSMSMessageRequestBean().getOutboundSMSMessageRequest()
                    .getAddress().toString();

            outboundSMSMessageRequestBeanHub requestBean = extendedRequestDTO.getOutboundSMSMessageRequestBean();

            OutboundSMSMessageResponseBean responseBean = new OutboundSMSMessageResponseBean();

            OutboundSMSMessageResponseBean.OutboundSMSMessageResponse smsMessageResponse = new
                    OutboundSMSMessageResponseBean.OutboundSMSMessageResponse();


            if (clientCorrelator != null) {
                String response = checkDuplicateClientCorrelator(clientCorrelator, userId, serviceNameId, endUserId,
                        MessageProcessStatus.Success, MessageType.Response);
                if (response != null) {

                    // return already sent response
                    OutboundSMSMessageResponseBean obj;
                    obj = gson.fromJson(response, OutboundSMSMessageResponseBean.class);
                    responseWrapper.setOutboundSMSMessageResponseBeanHub(obj);
                    responseWrapper.setHttpStatus(Response.Status.OK);
                    return responseWrapper;

                }
            }

            smsMessageResponse.setResourceURL(getoutSideResourceURL(Integer.toString(getReferenceNumber())));
            smsMessageResponse.setSenderAddress(requestBean.getOutboundSMSMessageRequest().getSenderAddress());
            smsMessageResponse.setReceiptRequest(requestBean.getOutboundSMSMessageRequest().getReceiptRequest());
            smsMessageResponse.setSenderAddress(requestBean.getOutboundSMSMessageRequest().getSenderAddress());
            smsMessageResponse.setOutboundSMSTextMessage(requestBean.getOutboundSMSMessageRequest()
                    .getOutboundSMSTextMessage());
            smsMessageResponse.setClientCorrelator(requestBean.getOutboundSMSMessageRequest().getClientCorrelator());
            smsMessageResponse.setAddress(requestBean.getOutboundSMSMessageRequest().getAddress());

            DeliveryInfoList deliveryInfoList = new DeliveryInfoList();
            deliveryInfoList.setResourceURL(getinSideResourceURL(Integer.toString(getReferenceNumber())));
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

            responseWrapper.setOutboundSMSMessageResponseBeanHub(responseBean);
            responseWrapper.setHttpStatus(Status.CREATED);

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

    /**
     * Generating inside resource URL.
     *
     * @param mtSMSTransactionId Unique ID
     * @return URL
     */
    private String getinSideResourceURL(final String mtSMSTransactionId) {

        return "http://wso2telco.sandbox.com" +
                "/smsmessaging/" +
                extendedRequestDTO.getApiVersion() +
                "/outbound/" +
                extendedRequestDTO.getShortCode() +
                "/requests/" +
                mtSMSTransactionId +
                "/deliveryInfos";
    }

    /**
     * Genarating outside resource URL.
     *
     * @param mtSMSTransactionId Unique Id.
     * @return URL
     */
    private String getoutSideResourceURL(final String mtSMSTransactionId) {

        return "http://wso2telco.sandbox.com" +
                "/smsmessaging/" +
                extendedRequestDTO.getApiVersion() +
                "/outbound/" +
                extendedRequestDTO.getShortCode() +
                "/requests/" +
                mtSMSTransactionId;
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
            String responseClientCorrelator;

            if (responseType == type.getValue() && responseStatus == status.getValue()) {
                String request = aResponse.getRequest();
                JSONObject json = new JSONObject(request);

                responseClientCorrelator = json.getJSONObject("outboundSMSMessageRequest").get
                        ("clientCorrelator").toString();

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
    public String getJosonString(sendMTSMSRequestWrapperDTOHub requestDTO) {
        Gson gson = new Gson();
        return gson.toJson(requestDTO.getOutboundSMSMessageRequestBean());
    }

    @Override
    public String getnumber(sendMTSMSRequestWrapperDTOHub requestDTO) {
        return requestDTO.getOutboundSMSMessageRequestBean().getOutboundSMSMessageRequest().getAddress().toString();
    }
}
