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
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StopSubscribeToDeliveryNotificationService extends
        AbstractRequestHandler<StopSubscriptionNotificationRequestWrapper>
        implements AddressIgnorerable {

    private StopSubscriptionNotificationRequestWrapper requestWrapper;
    private SMSMessagingDAO smsMessagingDAO;
    private StopSubscriptionNotificationResponseWrapper responseWrapper;
    private String serviceCallSMS;

    {
        LOG = LogFactory.getLog(StopSubscribeToDeliveryNotificationService.class);
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
    protected void init(StopSubscriptionNotificationRequestWrapper extendedRequestDTO) throws Exception {
        responseWrapper = new StopSubscriptionNotificationResponseWrapper();
        requestWrapper = extendedRequestDTO;
    }

    @Override
    protected boolean validate(StopSubscriptionNotificationRequestWrapper wrapperDTO) throws Exception {

        String senderAddress = requestWrapper.getSenderAddress();
        int subscriptionID = requestWrapper.getSubscriptionID();

        List<ValidationRule> validationRulesList = new ArrayList<>();

        try {
            validationRulesList.add(
                    new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "senderAddress", senderAddress));
            validationRulesList.add(
                    new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "subscriptionID", subscriptionID));

            ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
            validationRules = validationRulesList.toArray(validationRules);

            Validation.checkRequestParams(validationRules);

        } catch (CustomException ex) {
            LOG.error("###SUBSCRIPTION### Error in Validations. ", ex);
            return false;
        }

        return true;
    }

    @Override
    protected Returnable process(StopSubscriptionNotificationRequestWrapper extendedRequestDTO) throws Exception {

        serviceCallSMS = ServiceName.StopSubscriptionDelivery.toString();
        APITypes apiTypes = dao.getAPIType(RequestType.SMSMESSAGING.toString());
        APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCallSMS);


        String senderAddress = requestWrapper.getSenderAddress();
        int subscriptionID = requestWrapper.getSubscriptionID();

        // check for existing subscription
        boolean isDelete = smsMessagingDAO.removeSubscription(subscriptionID, senderAddress);

        String deletedMessage = "Subscriber "+subscriptionID+ " removed from subscription";

        if (isDelete) {
            responseWrapper.setStatus("NO CONTENT");
            responseWrapper.setHttpStatus(Response.Status.NO_CONTENT);
            saveResponse(senderAddress, deletedMessage,apiServiceCalls, extendedRequestDTO.getUser().getId(), MessageProcessStatus.Success);
        } else {
            LOG.error("###SUBSCRIPTION### NO Subscriber found");
            responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                    ServiceError.SERVICE_ERROR_OCCURED, "NO Subscriber found"));
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
        }

        return responseWrapper;
    }

    // Save Response in messageLog table
    private void saveResponse(String senderAddress, String request, APIServiceCalls
            apiServiceCalls, int userId, MessageProcessStatus status) throws Exception {

        new MessageLog();
        MessageLog messageLog;
        messageLog = new MessageLog();
        messageLog.setRequest(request);
        messageLog.setStatus(status.getValue());
        messageLog.setType(MessageType.Response.getValue());
        messageLog.setServicenameid(apiServiceCalls.getApiServiceCallId());
        messageLog.setUserid(userId);
        messageLog.setReference("msisdn");
        messageLog.setValue(senderAddress);
        messageLog.setMessageTimestamp(new Date());

        loggingDAO.saveMessageLog(messageLog);
    }
}
