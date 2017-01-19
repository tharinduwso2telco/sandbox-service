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

import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.msisdnvalidator.InvalidMSISDNException;
import com.wso2telco.core.msisdnvalidator.MSISDN;
import com.wso2telco.core.msisdnvalidator.MSISDNUtil;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.exception.SandboxException.SandboxErrorType;

public class CommonUtil {

	static Log LOG = LogFactory.getLog(CommonUtil.class);
	
	public static final String QUERY_STRING_SEPARATOR = "?";
	
	public static void validateMsisdn(String msisdn) throws SandboxException {

		MSISDNUtil msisdnUtil = new MSISDNUtil();
		try {
			MSISDN parsedMsisdn = msisdnUtil.parse(msisdn);
		} catch (InvalidMSISDNException ex) {
			LOG.info(ex);
			String errorMessage = "Invalid MSISDN";
			throw new SandboxException(SandboxErrorType.INVALID_MSISDN);
		}

	}

	public static void validatePositiveNumber(String number, String parameterName) throws SandboxException {
		if (!StringUtils.isEmpty(number) && !NumberUtils.isDigits(number)) {
			throw new SandboxException(SandboxErrorType.INVALID_INPUT_VALUE);
		}
	}
	

	public static Integer convertStringToInteger(String number) {
		Integer value = Integer.valueOf(0);

		if (!StringUtils.isEmpty(number) && NumberUtils.isDigits(number)) {
			value = Integer.valueOf(number);
		}

		return value;
	}
	
	public static String getNullOrTrimmedValue(String value) {
		String outputValue = null;

		if (value != null && value.trim().length() > 0) {
			outputValue = value.trim();
		}

		return outputValue;
	}
	

	public static String extractNumberFromMsisdn(String msisdn)
			throws InvalidMSISDNException {
		String phoneNumber = "";
		MSISDNUtil msisdnUtil = new MSISDNUtil();
		try {
			MSISDN parsedMsisdn = msisdnUtil.parse(msisdn);
			phoneNumber = Integer.toString(parsedMsisdn.getCountryCode())
					+ Long.toString(parsedMsisdn.getNationalNumber());
		} catch (InvalidMSISDNException e) {
			LOG.error("Error in Parsing MSISDN", e);
			throw e;
		}

	return phoneNumber;
    }
    
	public static String getResourceUrl(RequestDTO extendedRequestDTO) {
		StringBuilder resourceUrlBuilder = new StringBuilder();
		String protocolVersion = extendedRequestDTO.getHttpRequest()
				.getProtocol();
		String[] protocolDetail = protocolVersion.split("/");
		resourceUrlBuilder.append(protocolDetail[0].toLowerCase() + "://");
		resourceUrlBuilder.append(extendedRequestDTO.getHttpRequest()
				.getHeader("Host"));
		resourceUrlBuilder.append(extendedRequestDTO.getHttpRequest()
				.getPathInfo() + "/" + RandomStringUtils.randomNumeric(5));
		
		/*if (extendedRequestDTO.getHttpRequest().getQueryString() != null) {
			resourceUrlBuilder.append(QUERY_STRING_SEPARATOR);
			resourceUrlBuilder.append(extendedRequestDTO.getHttpRequest()
					.getQueryString());
		}*/

		return resourceUrlBuilder.toString();
	}
	
	public static String getPostResourceUrl(RequestDTO extendedRequestDTO) {
		StringBuilder resourceUrlBuilder = new StringBuilder();
		String protocolVersion = extendedRequestDTO.getHttpRequest()
				.getProtocol();
		String[] protocolDetail = protocolVersion.split("/");
		resourceUrlBuilder.append(protocolDetail[0].toLowerCase() + "://");
		resourceUrlBuilder.append(extendedRequestDTO.getHttpRequest()
				.getHeader("Host"));
		resourceUrlBuilder.append(extendedRequestDTO.getHttpRequest()
				.getPathInfo());
		
		/*if (extendedRequestDTO.getHttpRequest().getQueryString() != null) {
			resourceUrlBuilder.append(QUERY_STRING_SEPARATOR);
			resourceUrlBuilder.append(extendedRequestDTO.getHttpRequest()
					.getQueryString());
		}*/

		return resourceUrlBuilder.toString();
	}

	public static String[] getStringToArray(String schema) {
		String[] schemaList = schema.split(",", -1);
		
		return schemaList;
	}

}
