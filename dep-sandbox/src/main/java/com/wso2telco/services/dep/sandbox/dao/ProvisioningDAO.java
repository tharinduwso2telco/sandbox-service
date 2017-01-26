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
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionAllService;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionMSISDNServicesMap;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionRequestLog;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionResponseMessage;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionedServices;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Status;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.util.ProvisioningStatusCodes;

public interface ProvisioningDAO {

	public void saveProvisionRequestLog(ProvisionRequestLog provisionRequestLog) throws Exception;

	public List<ProvisionAllService> getApplicableProvisionServices(String number, String username, int offset, int limit) throws Exception;
	
	public List<ListProvisionedDTO> getActiveProvisionedServices(String msisdn,String username,int offset, int limit) throws Exception;
	
	public ManageNumber getNumber(String number, String username) throws Exception;
	
	public ProvisionAllService getProvisionService(String serviceCode, String ServiceName, User user) throws Exception;
	
	public void saveProvisionService(ProvisionAllService provisionAllService) throws Exception;
	
	public List<ProvisionAllService> getProvisionServices(int userid) throws Exception;

	public ProvisionResponseMessage getErrorResponse(String msisdn,String username, String serviceCode) throws Exception;

	public ProvisionedServices getAlreadyProvisioned(String msisdn, String userName,String serviceCode) throws Exception;

	public List<Status> getTransactionStatus() throws Exception;

	public void updateDeleteStatus(ProvisionedServices provisionedCheckList)throws Exception;

	public ProvisionedServices checkClientCorrelator(String msisdn,String userName, String serviceCode, String clientCorrelator) throws Exception;

	public ProvisionedServices checkProvisionClientCorrelator(String msisdn,String userName, String serviceCode, String clientCorrelator) throws Exception;

	public Status getStatusFromStatusCode (ProvisioningStatusCodes statusCode) throws Exception;
	
	public ProvisionMSISDNServicesMap getProvisionMsisdnService(ManageNumber number, ProvisionAllService service) throws Exception;
	
	public void saveProvisionedService(ProvisionedServices provisionedService) throws Exception;

	public void saveServiceForMsisdn(ProvisionMSISDNServicesMap map)throws Exception;
	
	public ProvisionedServices getAlreadyProvisionedService(User user, List<String> statusCodes, ProvisionAllService provisionService, String phoneNumber) throws Exception;
}
