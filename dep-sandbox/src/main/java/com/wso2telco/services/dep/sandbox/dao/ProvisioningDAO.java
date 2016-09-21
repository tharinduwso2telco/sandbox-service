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
package com.wso2telco.services.dep.sandbox.dao;

import java.util.List;

import com.wso2telco.services.dep.sandbox.dao.model.custom.ListProvisionedDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisionRemoveErrorMessageDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionAllService;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionRequestLog;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionResponseMessage;

public interface ProvisioningDAO {

	public void saveProvisionRequestLog(ProvisionRequestLog provisionRequestLog) throws Exception;

	public List<ProvisionAllService> getApplicableProvisionServices(String number, String username, int offset,
			int limit) throws Exception;
	public List<ListProvisionedDTO> getActiveProvisionedServices(String msisdn,String username,int offset, int limit) throws Exception;

	public List<ProvisionResponseMessage> getErrorResponse(String msisdn,String username, String serviceCode) throws Exception;
}
