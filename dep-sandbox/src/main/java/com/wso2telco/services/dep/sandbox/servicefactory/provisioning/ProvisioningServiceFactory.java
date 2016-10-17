/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.services.dep.sandbox.servicefactory.provisioning;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;

public class ProvisioningServiceFactory {

    private static Log LOG = LogFactory.getLog(ProvisioningServiceFactory.class);

    public static RequestHandleable getInstance(final RequestDTO requestDTO) {
	final String QUERY_APPLICABLE_SERVICES = "list/applicable";
	final String REMOVE_SERVICE = "remove";
	final String LIST_SERVICE_BY_CUSTOMER = "list/active";
	final String SERVICE = "addservice";
	final String NEW_SERVICE = "newservice";

	if (requestDTO.getRequestPath().toLowerCase().contains(QUERY_APPLICABLE_SERVICES) && requestDTO.isGet()) {
	    LOG.debug("###PROVISION### LOADING QUERY APPLICABLE PROVISIONING SERVICES");
	    return new QueryApplicableProvisioningService();
	} else if (requestDTO.getRequestPath().toLowerCase().contains(LIST_SERVICE_BY_CUSTOMER) && requestDTO.isGet()) {
	    LOG.debug("###PROVISION### LOADING LIST OF PROVISIONED SERVICES");
	    return new ListActiveProvisionedServices();
	} else if (requestDTO.isPost()) {
	    if (requestDTO.getRequestPath().toLowerCase().contains(REMOVE_SERVICE)) {
		LOG.debug("###PROVISION### LOADING REMOVE GIVEN PROVISIONED SERVICE");
		return new RemoveProvisionedServices();
	    }  if (requestDTO.getRequestPath().toLowerCase().contains(SERVICE)) {
		LOG.debug("###PROVISION### LOADING ADD SERVICE FOR MSISDN");
		return new AssignServiceForMsisdn();
	    }else if (requestDTO.getRequestPath().toLowerCase().contains(NEW_SERVICE)){
		LOG.debug("LOADING ADD NEW SERVICE");
		return new NewProvisioningService();
	}else {
		LOG.debug("###PROVISION### LOADING PROVISION REQUESTED SERVICE");
		return new ProvisionRequestedServiceHandler();
	    }
	}
	LOG.debug("###PROVISION### APPROPIATE SERVICE CLASS NOT FOUND ");
	return null;

    }
}

