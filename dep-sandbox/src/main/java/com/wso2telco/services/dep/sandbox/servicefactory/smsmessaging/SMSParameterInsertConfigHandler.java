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
package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging;


import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.SMSMessagingDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.SMSMessagingParam;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.AddressIgnorerable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;

import org.apache.commons.logging.LogFactory;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SMSParameterInsertConfigHandler extends AbstractRequestHandler<SMSParameterConfigRequestWrapper>
        implements AddressIgnorerable {


    private SMSParameterConfigRequestWrapper requestWrapper;
    private SMSParameterConfigResponseWrapper responseWrapper;
    private SMSMessagingDAO smsMessagingDAO;


    {
        LOG = LogFactory.getLog(SMSParameterInsertConfigHandler.class);
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
    protected boolean validate(SMSParameterConfigRequestWrapper wrapperDTO) throws Exception {

        SMSParameterConfigRequestBean requestBean = wrapperDTO.getParameterConfigRequestBean();


        String deliveryStatus = requestBean.getDeliveryStatus();
        String maxNotifications = requestBean.getMaxNotifications();
        String notificationDelay = requestBean.getNotificationDelay();

        List<ValidationRule> validationRulesList = new ArrayList<>();

        try {
            validationRulesList
                    .add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "deliveryStatus",
                            deliveryStatus));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
                    "maxNotifications", maxNotifications));
            validationRulesList
                    .add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO,
                            "notificationDelay", notificationDelay));

            ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
            validationRules = validationRulesList.toArray(validationRules);

            Validation.checkRequestParams(validationRules);

        } catch (CustomException ex) {
            LOG.error("###SMSCONFIG### Error in Validations. ", ex);
            responseWrapper.setRequestError(
                    constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
            return false;
        }
        return true;
    }

    @Override
    protected Returnable process(SMSParameterConfigRequestWrapper extendedRequestDTO) throws Exception {

        if (responseWrapper.getRequestError() != null) {
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            return responseWrapper;
        }


        try {
            SMSParameterConfigRequestBean requestBean = extendedRequestDTO.getParameterConfigRequestBean();

            String deliveryStatus = requestBean.getDeliveryStatus();
            String maxNotifications = requestBean.getMaxNotifications();
            String notificationDelay = requestBean.getNotificationDelay();

            SMSMessagingParam smsMessagingParam = new SMSMessagingParam();
            smsMessagingParam.setDeliveryStatus(deliveryStatus);
            smsMessagingParam.setMaxNotifications(maxNotifications);
            smsMessagingParam.setNotificationDelay(notificationDelay);
            smsMessagingParam.setUserid(user.getId());
            smsMessagingParam.setCreated("1");
            smsMessagingParam.setCreatedDate(new Date());

            boolean result = smsMessagingDAO.saveSMSParameters(smsMessagingParam);


            if (!result) {
                LOG.error("###USSD CONFIG### Error occur in save applications");
                responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Error occur in save applications"));
                responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);

            } else {
                responseWrapper.setStatus("Successful");
                responseWrapper.setHttpStatus(Response.Status.OK);
            }

        } catch (Exception ex) {

            LOG.error("###USSD CONFIG### Error occur in save applications");
            responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
                    ServiceError.INVALID_INPUT_VALUE, "Error occur in save applications"));
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
        }

        return responseWrapper;

    }

    @Override
    protected void init(SMSParameterConfigRequestWrapper extendedRequestDTO) throws Exception {

        requestWrapper = extendedRequestDTO;
        responseWrapper = new SMSParameterConfigResponseWrapper();
    }
}
