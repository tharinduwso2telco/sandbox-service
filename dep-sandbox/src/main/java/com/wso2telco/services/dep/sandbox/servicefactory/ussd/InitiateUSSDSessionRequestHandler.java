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

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.model.custom.USSDSessionInitiatorRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.USSDSessionRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.USSDSessionResponseRequest;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class InitiateUSSDSessionRequestHandler extends AbstractRequestHandler<USSDSessionRequestWrapperDTO> {

    private USSDSessionRequestWrapperDTO requestWrapperDTO;
    private USSDSessionResponseWrapper responseWrapper;


    {
        LOG = LogFactory.getLog(InitiateUSSDSessionRequestHandler.class);
    }

    @Override
    protected Returnable getResponseDTO() {
        return responseWrapper;
    }

    @Override
    protected List<String> getAddress() {
        List<String> address = new ArrayList<String>();
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
        return null;
    }
}
