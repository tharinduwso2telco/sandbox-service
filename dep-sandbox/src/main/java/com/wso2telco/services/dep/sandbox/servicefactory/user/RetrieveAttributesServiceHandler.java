/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licenses this file to you under  the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.services.dep.sandbox.servicefactory.user;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.model.custom.AttributeRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.AddressIgnorerable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.user.RetrieveAttributesServiceResponseWrapper.Attributes;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;

public class RetrieveAttributesServiceHandler extends
	AbstractRequestHandler<AttributeRequestWrapperDTO> implements
	AddressIgnorerable {

    private AttributeRequestWrapperDTO requestWrapperDTO;
    private RetrieveAttributesServiceResponseWrapper responseWrapper;
    private APITypes apiObj;
    private APIServiceCalls serviceObj;

    {
	LOG = LogFactory.getLog(RetrieveAttributesServiceHandler.class);
	dao = DaoFactory.getGenaricDAO();
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
    protected boolean validate(AttributeRequestWrapperDTO wrapperDTO)
	    throws Exception {
	String api = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getApiType());
	String service = CommonUtil.getNullOrTrimmedValue(wrapperDTO
		.getServiceType());
	try {

	    ValidationRule[] validationRules = defineValidationRules(api,
		    service);
	    Validation.checkRequestParams(validationRules);

	    checkApiExist(api);
	    checkApiServiceExist(service);

	} catch (CustomException ex) {
	    LOG.error("###USER### Error in Validation of Mandotary/Optional params : "
		    + ex);

	    responseWrapper.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(),
		    wrapperDTO.getRequestPath()));
	    return false;

	} catch (Exception ex) {
	    LOG.error("###USER### Error in Validation of doue to Non-existing data "
		    + ex);
	    return false;
	}
	return true;
    }

    private ValidationRule[] defineValidationRules(String api, String service) {

	ValidationRule[] validationRules = {
		new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
			"apiType", api),
		new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
			"serviceType", service) };
	return validationRules;
    }

    @Override
    protected Returnable process(AttributeRequestWrapperDTO extendedRequestDTO)
	    throws Exception {
	if (responseWrapper.getRequestError() != null) {
	    responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
	    return responseWrapper;
	}
	List<AttributeDistribution> distributionObj = new ArrayList<AttributeDistribution>();
	List<String> attributeList = new ArrayList<String>();
	Attributes attribute = new Attributes();

	try {

	    distributionObj = dao.getAttributeDistributionByServiceCall(
		    apiObj.getId(), serviceObj.getApiServiceCallId());
	    if (distributionObj != null && !distributionObj.isEmpty()) {
		for (AttributeDistribution eachDistributionObj : distributionObj) {
		    attributeList.add(eachDistributionObj.getAttribute()
			    .getAttributeName());
		}
		attribute.setAttributes(attributeList);
		responseWrapper.setAttribute(attribute);
		responseWrapper.setHttpStatus(Response.Status.OK);
	    } else {
		responseWrapper.setRequestError(constructRequestError(
			SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
			"No any attribute is provided for given service call "
				+ serviceObj.getServiceName()
				+ "  of this api " + apiObj.getAPIName()));
		responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);

		LOG.error("###USER### No any attribute is provided for given service call "
			+ serviceObj.getServiceName()
			+ "  of this api "
			+ apiObj.getAPIName());
	    }

	} catch (Exception ex) {
	    LOG.error(
		    "###USER### Error in processing attribute insertion service request. ",
		    ex);
	    responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
	}
	return responseWrapper;
    }

    private void checkApiServiceExist(String service) throws Exception {
	serviceObj = dao.getServiceCall(apiObj.getId(), service.toLowerCase());
	if (serviceObj == null) {
	    responseWrapper.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Service " + service + " is Invalid for given API"));
	    LOG.error("###USER### Error in Validation due to Service "
		    + service + " is Invalid for given API");
	    throw new Exception();
	}
    }

    private void checkApiExist(String api) throws Exception {
	apiObj = dao.getAPIType(api.toLowerCase());
	if (apiObj == null) {
	    responseWrapper.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Given API is invalid" + api));
	    LOG.error("###USER### Error in Validation of Given API " + api
		    + " is invalid");
	    throw new Exception();
	}
    }

    @Override
    protected void init(AttributeRequestWrapperDTO extendedRequestDTO)
	    throws Exception {
	requestWrapperDTO = extendedRequestDTO;
	responseWrapper = new RetrieveAttributesServiceResponseWrapper();
    }
}
