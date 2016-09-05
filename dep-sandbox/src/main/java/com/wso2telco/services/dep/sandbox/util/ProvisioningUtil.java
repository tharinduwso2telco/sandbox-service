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
package com.wso2telco.services.dep.sandbox.util;

import java.util.Date;

import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionRequestLog;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

public class ProvisioningUtil {

	private static ProvisioningDAO provisioningDAO;
	
	public static final String DEFAULT_CURRENCY_CODE = "USD";
	
	public static final String QUERY_STRING_SEPARATOR = "?";

	static {
		provisioningDAO = DaoFactory.getProvisioningDAO();
	}

	public static void saveProvisioningRequestDataLog(String requestType, String msisdn, User user,
			String clientCorrelator, String clientReferenceCode, String notifyUrl, String callbackData,
			Date requestTimestamp) throws Exception {
		ProvisionRequestLog requestLog = new ProvisionRequestLog();
		requestLog.setRequestType(requestType);
		requestLog.setMsisdn(msisdn);
		requestLog.setUser(user);
		requestLog.setClientCorrelator(clientCorrelator);
		requestLog.setClientReferenceCode(clientReferenceCode);
		requestLog.setNotifyURL(notifyUrl);
		requestLog.setCallbackData(callbackData);
		requestLog.setRequestTimestamp(requestTimestamp);

		provisioningDAO.saveProvisionRequestLog(requestLog);
	}

	public static String getResourceUrl(RequestDTO extendedRequestDTO) {
		StringBuilder resourceUrlBuilder = new StringBuilder();
		String protocolVersion = extendedRequestDTO.getHttpRequest().getProtocol();
		String[] protocolDetail = protocolVersion.split("/");
		resourceUrlBuilder.append(protocolDetail[0].toLowerCase() + "://");
		resourceUrlBuilder.append(extendedRequestDTO.getHttpRequest().getHeader("Host"));
		resourceUrlBuilder.append(extendedRequestDTO.getHttpRequest().getPathInfo());
		if (extendedRequestDTO.getHttpRequest().getQueryString() != null) {
			resourceUrlBuilder.append(QUERY_STRING_SEPARATOR);
			resourceUrlBuilder.append(extendedRequestDTO.getHttpRequest().getQueryString());
		}
		return resourceUrlBuilder.toString();
	}
}
