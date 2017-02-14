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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;

public class UserServiceFactory {

    private static Log LOG = LogFactory.getLog(UserServiceFactory.class);

    public static RequestHandleable getInstance(RequestDTO requestDTO) {
	
	final String ADD_USER_PATH = "register";
	final String ATTRIBUTE_PATH = "attribute";
	final String API_TYPE_PATH = "apiType";
	final String API_SERVICE_TYPE_PATH = "serviceType";
	final String ADD_NUMBER_PATH = "managenumber";
	final String NUMBER_DETAIL_PATH = "numberDetails";

	if (requestDTO.getRequestPath().contains(ADD_USER_PATH)
		&& requestDTO.isPost()) {
	    LOG.debug("LOADING REGISTER NEW USER");
	    return new RegisterServiceHandler();
	} else if (requestDTO.getRequestPath().contains(ATTRIBUTE_PATH)
		&& requestDTO.isGet()) {

	    LOG.debug("LOADING AVAILABLE SERVICE CALL SPECIFIC ATTRIBUTES");
	    return new RetrieveAttributesServiceHandler();

	} else if (requestDTO.getRequestPath().contains(API_TYPE_PATH)
		&& requestDTO.isGet()) {
	    LOG.debug("LOADING API TYPES");
	    return new RetrieveAPITypeServiceHandler();
	} else if (requestDTO.getRequestPath().contains(API_SERVICE_TYPE_PATH)
		&& requestDTO.isGet()) {
	    LOG.debug("LOADING API SERVICE CALL TYPES");
	    return new RetrieveAPIServicecallHandler();
	} else if (requestDTO.getRequestPath().contains(ADD_NUMBER_PATH)
		&& requestDTO.isPost()) {
	    LOG.debug("LOADING NUMBER SERVICE CALL");
	    return new ManageNumberServiceHandler();
	} else if (requestDTO.getRequestPath().contains(NUMBER_DETAIL_PATH)
		&& requestDTO.isGet()) {
		LOG.debug("LOADING NUMBER SERVICE CALL");
		return new RetrieveNumberDetailsServiceHandler();
	}

	LOG.debug("NO ANY SERVICE FOUND FOR REQUESTED PATH");
	return null;
    }

}
