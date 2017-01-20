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
package com.wso2telco.services.dep.sandbox.servicefactory.customerinfoConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.provisionConfig.ProvisioningConfigServiceFactory;
import com.wso2telco.services.dep.sandbox.util.ServiceName;

public class CustomerInfoConfigServiceFactory {

    private static Log LOG = LogFactory
	    .getLog(ProvisioningConfigServiceFactory.class);
   

    public static RequestHandleable getInstance(final RequestDTO requestDTO) {
    	
    	 final String GET_PROFILE = "profile";
    	 final String GET_ATTRIBUTE = "attribute";
    	    
	if (requestDTO.isPost()) {
	    if (requestDTO.getRequestPath().toLowerCase().contains(GET_PROFILE)) {
		LOG.debug("###CUSTOMERINFOCONFIG### LOADING PROFILE CONFIG SERVICES FOR USER");
		return new GetProfileConfigHandler();
	    } else if (requestDTO.getRequestPath().toLowerCase()
		    .contains(GET_ATTRIBUTE)) {
		LOG.debug("###CUSTOMERINFOCONFIG### LOADING ATTRIBUTE CONFIG SERVICES FOR USER");
		return new GetAttributeConfigHandler();
	    }
	}
	LOG.debug("###CUSTOMERINFOCONFIG### APPROPIATE SERVICE CLASS NOT FOUND ");
	return null;
    }
}
