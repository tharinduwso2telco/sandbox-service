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
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.SMSMessagingDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.InboundSMSMessage;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;
import com.wso2telco.services.dep.sandbox.dao.model.domain.SendSMSToApplication;
import com.wso2telco.services.dep.sandbox.servicefactory.*;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import org.apache.commons.logging.LogFactory;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

 class ReceivingSMSHandler extends AbstractRequestHandler<ReceivingSMSRequestWrapperGateway> implements
        AddressIgnorerable {

    private ReceivingSMSResponseWrapper responseWrapper;
    private ReceivingSMSRequestWrapperGateway requestWrapper;
    private SMSMessagingDAO smsMessagingDAO;
    private String resourceURL;


    {
        LOG = LogFactory.getLog(ReceivingSMSHandler.class);
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
    protected boolean validate(ReceivingSMSRequestWrapperGateway wrapperDTO) throws Exception {


        String registrationId = wrapperDTO.getRegistrationID();
        int maxBatchSize = wrapperDTO.getMaxBatchSize();

        try {
            ValidationRule[] rules = {
                    new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "registrationId", registrationId),
                    new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "maxBatchSize",
                            maxBatchSize)};


            Validation.checkRequestParams(rules);

        } catch (CustomException ex) {
            LOG.error("###SMS### Error in Validations. ", ex);
            responseWrapper.setRequestError(
                    constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
            return false;
        }
        return true;
    }

    @Override
    protected Returnable process(ReceivingSMSRequestWrapperGateway extendedRequestDTO) throws Exception {

        if (responseWrapper.getRequestError() != null) {
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            return responseWrapper;
        }

        try {
            String registrationId = extendedRequestDTO.getRegistrationID();
            int maxBatchSize = extendedRequestDTO.getMaxBatchSize();

            APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
            String serviceCallPayment = ServiceName.ReceivingSMS.toString();
            APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCallPayment);

            if (!dao.isWhiteListedSenderAddress(user.getId(), registrationId)) {
                LOG.error("###SMS### Destination address is not WhiteListed ");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Destination Address is not WhiteListed"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
                return responseWrapper;
            }


            List<SendSMSToApplication> smsMessageList = smsMessagingDAO.getMessageInbound(registrationId, user.getId());
            List<InboundSMSMessage> inboundSMSMessageList = new ArrayList<>();

            if (maxBatchSize > smsMessageList.size()) {

               maxBatchSize = smsMessageList.size();
            }

            for (int i = 0; i < maxBatchSize; i++) {

                InboundSMSMessage inboundSMSMessage = new InboundSMSMessage();
                inboundSMSMessage.setDateTime((smsMessageList.get(i).getDate()).toString());
                inboundSMSMessage.setDestinationAddress(smsMessageList.get(i).getDestinationAddress());
                inboundSMSMessage.setMessage(smsMessageList.get(i).getMessage());
                inboundSMSMessage.setMessageId(smsMessageList.get(i).getSmsId());
                inboundSMSMessage.setSenderAddress(smsMessageList.get(i).getSenderAddress());
                inboundSMSMessage.setResourceURL(resourceURL + "/" + smsMessageList.get(i).getSmsId());
                inboundSMSMessageList.add(inboundSMSMessage);
            }

            ReceivingSMSResponseBean responseBean = new ReceivingSMSResponseBean();
            ReceivingSMSResponseBean.InboundSMSMessageList list = new ReceivingSMSResponseBean
                    .InboundSMSMessageList();
            list.setInboundSMSMessages(inboundSMSMessageList);

            responseBean.setInboundSMSMessageList(list);
            responseBean.setNumberOfMessagesInThisBatch(maxBatchSize);
            responseBean.setResourceURL(resourceURL);
            responseBean.setTotalNumberOfPendingMessages(smsMessageList.size()-maxBatchSize);

            responseWrapper.setReceivingSMSResponseBean(responseBean);
            responseWrapper.setHttpStatus(Response.Status.OK);

            // Save Success Response
            saveResponse(registrationId, responseBean, apiServiceCalls, MessageProcessStatus.Success);

        }catch (Exception ex){
            LOG.error("###SMS### Error Occurred in SMS Receiving Service. ", ex);
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            responseWrapper
                    .setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED,
                            "Error Occurred in SMS Receiving Service."));
        }

        return responseWrapper;
    }

    @Override
    protected void init(ReceivingSMSRequestWrapperGateway extendedRequestDTO) throws Exception {

        requestWrapper = extendedRequestDTO;
        responseWrapper = new ReceivingSMSResponseWrapper();
        resourceURL = CommonUtil.getResourceUrl(requestWrapper);

    }


    // Save Response in messageLog table
    private void saveResponse(String endUserIdPath, ReceivingSMSResponseBean responseBean, APIServiceCalls
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
        messageLog.setReference("shortCode");
        messageLog.setValue(endUserIdPath);
        messageLog.setMessageTimestamp(new Date());

        loggingDAO.saveMessageLog(messageLog);
    }

}
