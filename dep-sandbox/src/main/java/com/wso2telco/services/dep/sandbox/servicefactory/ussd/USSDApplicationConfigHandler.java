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


import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.USSDDAO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.AddressIgnorerable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class USSDApplicationConfigHandler extends AbstractRequestHandler<USSDApplicationConfigRequestWrapper>
        implements AddressIgnorerable {


    private USSDApplicationConfigRequestWrapper requestWrapper;
    private USSDApplicationConfigResponseWrapper responseWrapper;
    private USSDDAO ussdDAO;

    {
        LOG = LogFactory.getLog(USSDApplicationConfigHandler.class);
        ussdDAO = DaoFactory.getUSSDDAO();
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
    protected boolean validate(USSDApplicationConfigRequestWrapper wrapperDTO) throws Exception {

        USSDApplicationConfigRequestBean requestBean = wrapperDTO.getUssdApplicationConfigRequestBean();

        String shortCode = CommonUtil.getNullOrTrimmedValue(requestBean.getShortCode());
        String keyWord = CommonUtil.getNullOrTrimmedValue(requestBean.getKeyWord());
        int userId = Integer.parseInt(CommonUtil.getNullOrTrimmedValue(String.valueOf(requestBean.getUserID())));
        List<ValidationRule> validationRulesList = new ArrayList<>();

        try {
            validationRulesList
                    .add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "shortCode", shortCode));
            validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
                    "keyWord", keyWord));
            validationRulesList
                    .add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO, "userId", userId));

            ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
            validationRules = validationRulesList.toArray(validationRules);

            Validation.checkRequestParams(validationRules);

        } catch (CustomException ex) {
            LOG.error("###WALLETCONFIG### Error in Validations. ", ex);
            responseWrapper.setRequestError(
                    constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
            return false;
        }
        return true;
    }

    @Override
    protected Returnable process(USSDApplicationConfigRequestWrapper extendedRequestDTO) throws Exception {
        if (responseWrapper.getRequestError() != null) {
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            return responseWrapper;
        }

        try {
            USSDApplicationConfigRequestBean requestBean = extendedRequestDTO.getUssdApplicationConfigRequestBean();

            String shortCode = requestBean.getShortCode();
            String keyword = requestBean.getKeyWord();
            int userId = requestBean.getUserID();

            boolean result = ussdDAO.saveUSSDApplications(shortCode, keyword, userId);

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
    protected void init(USSDApplicationConfigRequestWrapper extendedRequestDTO) throws Exception {

        requestWrapper = extendedRequestDTO;
        responseWrapper = new USSDApplicationConfigResponseWrapper();

    }
}
