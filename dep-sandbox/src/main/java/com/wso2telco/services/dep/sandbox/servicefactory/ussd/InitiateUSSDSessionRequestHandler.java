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
package com.wso2telco.services.dep.sandbox.servicefactory.ussd;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.USSDDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.*;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;
import com.wso2telco.services.dep.sandbox.dao.model.domain.UssdApplication;
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

public class InitiateUSSDSessionRequestHandler extends AbstractRequestHandler<USSDSessionRequestWrapperDTO>
        implements RequestResponseRequestHandleable<USSDSessionRequestWrapperDTO> {

    private USSDSessionRequestWrapperDTO requestWrapperDTO;
    private USSDSessionResponseWrapper responseWrapper;
    private USSDDAO ussdDAO;
    private String serviceCallUSSD;
    private static final String DELIVERY_STATUS = "SENT";

    {
        LOG = LogFactory.getLog(InitiateUSSDSessionRequestHandler.class);
        ussdDAO = DaoFactory.getUSSDDAO();
    }

    @Override
    protected Returnable getResponseDTO() {
        return responseWrapper;
    }

    @Override
    protected List<String> getAddress() {
        List<String> address = new ArrayList<>();
        address.add(requestWrapperDTO.getEndUserId());
        return address;
    }

    @Override
    protected void init(USSDSessionRequestWrapperDTO extendedRequestDTO) throws Exception {
        responseWrapper = new USSDSessionResponseWrapper();
        requestWrapperDTO = extendedRequestDTO;
    }

    @Override
    protected boolean validate(USSDSessionRequestWrapperDTO wrapperDTO) throws Exception {

        USSDSessionInitiatorRequestBean requestBean = wrapperDTO.getUssdSessionRequestBean();
        USSDSessionInitiatorRequestBean.OutboundUSSDMessageRequest request = requestBean
                .getOutboundUSSDMessageRequest();
        USSDSessionResponseRequest responseRequest = request.getResponseRequest();

        String address = CommonUtil.getNullOrTrimmedValue(request.getAddress());
        String shortCode = CommonUtil.getNullOrTrimmedValue(request.getShortCode());
        String keyword = CommonUtil.getNullOrTrimmedValue(request.getKeyword());
        String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
        String endUserId = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getEndUserId());
        String outboundUSSDMessage = CommonUtil.getNullOrTrimmedValue(request.getOutboundUSSDMessage());
        String notifyURL = CommonUtil.getNullOrTrimmedValue(responseRequest.getNotifyURL());
        String callbackData = CommonUtil.getNullOrTrimmedValue(responseRequest.getCallbackData());
        String ussdAction = CommonUtil.getNullOrTrimmedValue(request.getUssdAction());

        List<ValidationRule> validationRulesList = new ArrayList<>();

        try {

            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
                    "address", address));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
                    "shortCode", shortCode));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,
                    "clientCorrelator", clientCorrelator));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,
                    "keyword", keyword));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
                    "outboundUSSDMessage", outboundUSSDMessage));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
                    "ussdAction", ussdAction));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,
                    "notifyURL", notifyURL));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
                    "callbackData", callbackData));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID,
                    "endUserId", endUserId));

            ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
            validationRules = validationRulesList.toArray(validationRules);

            Validation.checkRequestParams(validationRules);

        } catch (CustomException ex) {
            LOG.error("###USSD### Error in Validations", ex);
            responseWrapper.setRequestError(
                    constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
            return false;
        }

        return true;
    }

    @Override
    protected Returnable process(USSDSessionRequestWrapperDTO extendedRequestDTO) throws Exception {
        if (responseWrapper.getRequestError() != null) {
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            return responseWrapper;
        }

        USSDSessionInitiatorRequestBean requestBean = extendedRequestDTO.getUssdSessionRequestBean();
        USSDSessionInitiatorRequestBean.OutboundUSSDMessageRequest request = requestBean
                .getOutboundUSSDMessageRequest();
        USSDSessionResponseRequest responseRequest = request.getResponseRequest();

        String shortCode = CommonUtil.getNullOrTrimmedValue(request.getShortCode());
        String keyword = CommonUtil.getNullOrTrimmedValue(request.getKeyword());
        String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
        String endUserIdPath = extendedRequestDTO.getEndUserId();
//        String endUserIdRequest = request.getAddress();
        String address = getLastMobileNumber(endUserIdPath);
        String outboundUSSDMessage = CommonUtil.getNullOrTrimmedValue(request.getOutboundUSSDMessage());
        String notifyURL = CommonUtil.getNullOrTrimmedValue(responseRequest.getNotifyURL());
        String callbackData = CommonUtil.getNullOrTrimmedValue(responseRequest.getCallbackData());
        String ussdAction = CommonUtil.getNullOrTrimmedValue(request.getUssdAction());
        Integer userId = extendedRequestDTO.getUser().getId();
        serviceCallUSSD = ServiceName.InitUSSD.toString();

        USSDSessionInitiatorResponseBean responseBean = new USSDSessionInitiatorResponseBean();
        USSDSessionResponseRequest responseRequestBean = new USSDSessionResponseRequest();


        APITypes apiTypes = dao.getAPIType(RequestType.USSD.toString());
        APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCallUSSD);

        // Todo: Exception Handling

        // Todo: Handle ussdAction

        int serviceNameId = apiServiceCalls.getApiServiceCallId();
        Gson gson = new Gson();

        String response = checkDuplicateClientCorrelator(clientCorrelator, userId, serviceNameId, endUserIdPath,
                MessageProcessStatus.Success, MessageType.Response);

        if (response != null) {
            // Send already sent response
            USSDSessionInitiatorResponseBean object;
            object = gson.fromJson(response, USSDSessionInitiatorResponseBean.class);
            USSDSessionDTO dto = new USSDSessionDTO();
            dto.setOutboundUSSDMessageRequest(object);
            responseWrapper.setUssdSessionDTO(dto);
            responseWrapper.setHttpStatus(Response.Status.OK);
            return responseWrapper;
        }

        boolean test = checkForRegisteredApplications(userId, shortCode, keyword);

        if (!test) {
            LOG.error("###USSD### No Applications Registered for Provided ShortCode");
            responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                    ServiceError.INVALID_INPUT_VALUE, "No Applications Registered for Provided ShortCode"));
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            return responseWrapper;

        }

        responseBean.setAddress(address);
        responseBean.setKeyword(keyword);
        responseBean.setShortCode(shortCode);
        responseBean.setOutboundUSSDMessage(outboundUSSDMessage);
        responseBean.setClientCorrelator(clientCorrelator);
        responseBean.setUssdAction(ussdAction);

        responseRequestBean.setNotifyURL(notifyURL);
        responseRequestBean.setCallbackData(callbackData);
        responseBean.setResponseRequest(responseRequestBean);
        responseWrapper.setHttpStatus(Response.Status.OK);
        USSDSessionDTO ussdSessionDTO = new USSDSessionDTO();
        responseBean.setDeliveryStatus(DELIVERY_STATUS);
        ussdSessionDTO.setOutboundUSSDMessageRequest(responseBean);
        responseWrapper.setUssdSessionDTO(ussdSessionDTO);

        saveResponse(userId, endUserIdPath, responseBean, apiServiceCalls, MessageProcessStatus.Success);

        return responseWrapper;
    }

    private void saveResponse(Integer userid, String endUserIdPath, USSDSessionInitiatorResponseBean responseBean,
                              APIServiceCalls apiServiceCalls, MessageProcessStatus status) throws Exception {

        String jsonInString;
        Gson gson = new Gson();

        JsonElement je = new JsonParser().parse(gson.toJson(responseBean));
        JsonObject asJsonObject = je.getAsJsonObject();
        jsonInString = asJsonObject.toString();

        MessageLog messageLog = new MessageLog();
        messageLog.setRequest(jsonInString);
        messageLog.setStatus(status.getValue());
        messageLog.setType(MessageType.Response.getValue());
        messageLog.setServicenameid(apiServiceCalls.getApiServiceCallId());
        messageLog.setUserid(userid);
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

        for (int i = 0; i < response.size(); i++) {

            int responseStatus = response.get(i).getStatus();
            int responseType = response.get(i).getType();
            String responseClientCorrelator;

            if (responseType == type.getValue() && responseStatus == status.getValue()) {
                String request = response.get(i).getRequest();
                JSONObject json = new JSONObject(request);

                responseClientCorrelator = json.get("clientCorrelator").toString();

                int responseUserId = response.get(i).getUserid();
                String responseTel = response.get(i).getValue();

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

    private boolean checkForRegisteredApplications(int userId, String shortCode, String keyword) {

        List<UssdApplication> applicationList = ussdDAO.getUssdApplications(userId, shortCode, keyword);
        boolean flag = false;

        if (applicationList.size() == 0) {
            flag = false;

        } else {

            if (applicationList.size() > 1 && keyword == null) {
                flag = false;
            }

            for (int i = 0; i < applicationList.size(); i++) {

                int responseUserId = applicationList.get(i).getUserid();
                String responseKeyWord = applicationList.get(i).getKeyword();
                String responseShortCode = applicationList.get(i).getShortCode();


                if (keyword == null) {
                    if (userId == responseUserId && shortCode.equals(responseShortCode)) {
                        flag = true;
                    }
                } else {
                    if (userId == responseUserId && shortCode.equals(responseShortCode) && keyword.equals
                            (responseKeyWord)) {
                        flag = true;
                    }
                }
            }
        }

        return flag;

    }


    @Override
    public String getApiServiceCalls() {
        return ServiceName.InitUSSD.toString();
    }

    @Override
    public String getJosonString(USSDSessionRequestWrapperDTO requestDTO) {
        Gson gson = new Gson();
        return gson.toJson(requestDTO.getUssdSessionRequestBean());
    }

    @Override
    public String getnumber(USSDSessionRequestWrapperDTO requestDTO) {
        return requestDTO.getEndUserId();
    }
}
