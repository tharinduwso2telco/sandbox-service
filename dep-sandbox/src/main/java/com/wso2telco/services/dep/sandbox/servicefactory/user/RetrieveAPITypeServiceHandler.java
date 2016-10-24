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
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.model.custom.APITypeRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.AddressIgnorerable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.user.RetrieveAPITypeServiceResponseWrapper.APIType;

public class RetrieveAPITypeServiceHandler extends
	AbstractRequestHandler<APITypeRequestWrapperDTO> implements
	AddressIgnorerable {

    private APITypeRequestWrapperDTO requestWrapperDTO;
    private RetrieveAPITypeServiceResponseWrapper responseWrapper;

    {
	LOG = LogFactory.getLog(RetrieveAPITypeServiceHandler.class);
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
    protected boolean validate(APITypeRequestWrapperDTO wrapperDTO)
	    throws Exception {
	return true;
    }

    @Override
    protected Returnable process(APITypeRequestWrapperDTO extendedRequestDTO)
	    throws Exception {
	if (responseWrapper.getRequestError() != null) {
	    responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
	    return responseWrapper;
	}
	List<APITypes> apiObj = new ArrayList<APITypes>();
	List<String> apiList = new ArrayList<String>();
	APIType apiType = new APIType();
	try {

	    apiObj = dao.getAllAPIType();
	    if (apiObj != null && !apiObj.isEmpty()) {
		for (APITypes eachApiObj : apiObj) {
		    apiList.add(eachApiObj.getAPIName());
		}
		apiType.setApiTypes(apiList);
		responseWrapper.setAPIList(apiType);
		responseWrapper.setHttpStatus(Response.Status.OK);
	    } else {
		responseWrapper.setRequestError(constructRequestError(
			SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
			"No any APIs are available"));
		responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);

		LOG.error("###USER### No any APIs are available");
	    }

	} catch (Exception ex) {
	    LOG.error(
		    "###USER### Error in processing api type retrieve request. ",
		    ex);
	    responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
	}
	return responseWrapper;
    }

    @Override
    protected void init(APITypeRequestWrapperDTO extendedRequestDTO)
	    throws Exception {
	requestWrapperDTO = extendedRequestDTO;
	responseWrapper = new RetrieveAPITypeServiceResponseWrapper();
    }
}
