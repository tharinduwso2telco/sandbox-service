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
import com.wso2telco.services.dep.sandbox.dao.model.custom.APIServiceCallRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.AddressIgnorerable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.user.RetreiveAPIServiceCallResponseWrapper.APIServices;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;

public class RetrieveAPIServicecallHandler extends
	AbstractRequestHandler<APIServiceCallRequestWrapperDTO> implements
	AddressIgnorerable {

    private APIServiceCallRequestWrapperDTO requestWrapperDTO;
    private RetreiveAPIServiceCallResponseWrapper responseWrapper;
    private APITypes apiObj;

    {
	LOG = LogFactory.getLog(RetrieveAPIServicecallHandler.class);
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
    protected boolean validate(APIServiceCallRequestWrapperDTO wrapperDTO)
	    throws Exception {
	String api = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getApiType());
	try {

	    ValidationRule[] validationRules = defineValidationRules(api);
		
	    Validation.checkRequestParams(validationRules);

	    checkApiExist(api);

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

    private ValidationRule[] defineValidationRules(String api) {

	ValidationRule[] validationRules = {
		new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
			"apiType", api)
		};
		
	return validationRules;
    }

    @Override
    protected Returnable process(APIServiceCallRequestWrapperDTO extendedRequestDTO)
	    throws Exception {
	if (responseWrapper.getRequestError() != null) {
	    responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
	    return responseWrapper;
	}
	
	List<APIServiceCalls> serviceonObj = new ArrayList<APIServiceCalls>();
	List<String> serviceList = new ArrayList<String>();
	APIServices serviceCall = new APIServices();
	try {

	    serviceonObj = dao.getAllServiceCall(apiObj.getId());
	    if (serviceonObj != null && !serviceonObj.isEmpty()) {
		for (APIServiceCalls eachServiceonObj : serviceonObj) {
		    serviceList.add(eachServiceonObj.getServiceName());
		}
		serviceCall.setApiServiceCallTypes(serviceList);
		responseWrapper.setAPIServicesList(serviceCall);
		responseWrapper.setHttpStatus(Response.Status.OK);
	    } else {
		responseWrapper
			.setRequestError(constructRequestError(
				SERVICEEXCEPTION,
				ServiceError.INVALID_INPUT_VALUE,
				"No any service call is provided for api " + apiObj.getAPIName()));
		responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);

		LOG.error("###USER### No any service call is provided for given api "+ apiObj.getAPIName());
	    }

	} catch (Exception ex) {
	    LOG.error(
		    "###USER### Error in processing service calls for api retrieval request. ",
		    ex);
	    responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
	}
	return responseWrapper;
    }


    private void checkApiExist(String api) throws Exception {
	apiObj = dao.getAPIType(api.toLowerCase());
	if (apiObj == null) {
	    responseWrapper.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Given API is invalid" + api));
	    LOG.error("###USER### Error in Validation of Given API "+ api +" is invalid");
	    throw new Exception();
	}
    }

    @Override
    protected void init(APIServiceCallRequestWrapperDTO extendedRequestDTO)
	    throws Exception {
	responseWrapper = new RetreiveAPIServiceCallResponseWrapper();
    }
}
