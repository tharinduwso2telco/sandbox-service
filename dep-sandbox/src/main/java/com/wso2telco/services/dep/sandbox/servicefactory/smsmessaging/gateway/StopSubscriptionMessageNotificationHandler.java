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
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;
import com.wso2telco.services.dep.sandbox.servicefactory.*;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StopSubscriptionMessageNotificationHandler extends AbstractRequestHandler<StopSubscriptionMessageNotificationRequestWrapper> implements AddressIgnorerable {


    private StopSubscriptionMessageNotificationRequestWrapper requestWrapper;
    private StopSubscriptionMessageNotificationResponseWrapper responseWrapper;
    private SMSMessagingDAO smsMessagingDAO;


    {
        LOG = LogFactory.getLog(StopSubscriptionMessageNotificationHandler.class);
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
    protected boolean validate(StopSubscriptionMessageNotificationRequestWrapper wrapperDTO) throws Exception {

        String subscriptionID = wrapperDTO.getSubscriptionID();

        List<ValidationRule> validationRulesList = new ArrayList<>();

        try {
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "subscriptionID", subscriptionID));
            ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
            validationRules = validationRulesList.toArray(validationRules);
            Validation.checkRequestParams(validationRules);

        } catch (CustomException ex) {
            LOG.error("###SMS### Error in Validations. ", ex);
            responseWrapper.setRequestError(
                    constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            return false;
        }
        return true;
    }


    @Override
    protected Returnable process(StopSubscriptionMessageNotificationRequestWrapper extendedRequestDTO) throws Exception {

        if (responseWrapper.getRequestError() != null) {
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            return responseWrapper;
        }

        String subscriptionID = extendedRequestDTO.getSubscriptionID();

        APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
        String serviceCallPayment = ServiceName.SubscribeToApplication.toString();
        APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCallPayment);


        boolean delete = smsMessagingDAO.removeSubscriptionToMessage(subscriptionID);
        if (delete) {
            responseWrapper.setStatus("NO CONTENT");
            responseWrapper.setHttpStatus(Response.Status.NO_CONTENT);

            // Save Success Response
            saveResponse(subscriptionID, apiServiceCalls, MessageProcessStatus.Success);

        } else {
            LOG.error("###SMS SUBSCRIPTION### NO Subscriber found");
            responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                    ServiceError.INVALID_INPUT_VALUE, "NO Subscriber found"));
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);

        }
        return responseWrapper;
    }

    @Override
    protected void init(StopSubscriptionMessageNotificationRequestWrapper extendedRequestDTO) throws Exception {

        responseWrapper = new StopSubscriptionMessageNotificationResponseWrapper();
        this.requestWrapper = extendedRequestDTO;

    }


    private void saveResponse(String subscriptionID, APIServiceCalls apiServiceCalls, MessageProcessStatus status) throws Exception {

        String jsonString = "Stop the subscription to subscriptionID: "+subscriptionID;

        //setting messagelog responses
        new MessageLog();
        MessageLog messageLog;
        messageLog = new MessageLog();
        messageLog.setRequest(jsonString);
        messageLog.setStatus(status.getValue());
        messageLog.setType(MessageType.Response.getValue());
        messageLog.setServicenameid(apiServiceCalls.getApiServiceCallId());
        messageLog.setUserid(user.getId());
        messageLog.setReference("subscriptionID");
        messageLog.setValue(subscriptionID);
        messageLog.setMessageTimestamp(new Date());

        loggingDAO.saveMessageLog(messageLog);
    }


}
