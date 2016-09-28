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

package com.wso2telco.services.dep.sandbox.servicefactory.admin;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.UserDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RegisterUserServiceRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.exception.SandboxException.SandboxErrorType;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import org.apache.commons.logging.LogFactory;

class AdminRequstHandler extends AbstractRequestHandler<RegisterUserServiceRequestWrapperDTO> {

    private UserDAO userDAO;
    private RegisterUserServiceRequestWrapperDTO requestWrapper;
    private RegisterUserServiceResponseWrapper responseWrapper;

    {
	LOG = LogFactory.getLog(AdminRequstHandler.class);
	userDAO = DaoFactory.getUserDAO();
	dao = DaoFactory.getGenaricDAO();
    }

    @Override
    protected Returnable getResponseDTO() {
	return responseWrapper;
    }

    @Override
    protected List<String> getAddress() {
	List<String> address = new ArrayList<String>();
	return address;
    }

    @Override

    protected boolean validate(RegisterUserServiceRequestWrapperDTO wrapperDTO) throws Exception {

	String userName = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getUserName());
	try {
	    ValidationRule[] validationRules = {
		    new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "userName", userName) };
	    Validation.checkRequestParams(validationRules);
	} catch (CustomException ex) {
	    responseWrapper.setHttpStatus(Status.BAD_REQUEST);
	    responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg() , ex.getErrvar().toString()));
	    LOG.error("###REGISTER_USER### Validation Failed...!",ex);
	} catch (Exception ex) {
	    responseWrapper.setHttpStatus(Status.BAD_REQUEST);
	    responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED , null));
	    LOG.error("###REGISTER_USER### Validation Failed...!",ex);
	}
	return true;
    }

    @Override
    protected Returnable process(RegisterUserServiceRequestWrapperDTO extendedRequestDTO) throws Exception {
	try {
	    if (responseWrapper.getRequestError() != null) {
		return responseWrapper;
	    }
	    
	    User user = new User();
	    user.setUserName(extendedRequestDTO.getUserName());
	    user.setUserStatus(1);
	    User userFromDB = userDAO.getUser(extendedRequestDTO.getUserName());
	    if (userFromDB == null) {
		userDAO.saveUser(user);
		responseWrapper.setResponseMessage("User Successfully Registered...!");
	    } else {
		responseWrapper.setResponseMessage("Already Registered User...!");
		throw new SandboxException(SandboxErrorType.SERVICE_ERROR);
	    }
	    responseWrapper.setHttpStatus(Response.Status.OK);
	} catch (Exception ex) {
	    LOG.error("###REGISTER_USER### Error in User Registration...!",ex);
	    responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
	}
	return responseWrapper;
    }

    @Override
    protected void init(RegisterUserServiceRequestWrapperDTO extendedRequestDTO) throws Exception {
	requestWrapper = extendedRequestDTO;
	responseWrapper = new RegisterUserServiceResponseWrapper();
    }

}
