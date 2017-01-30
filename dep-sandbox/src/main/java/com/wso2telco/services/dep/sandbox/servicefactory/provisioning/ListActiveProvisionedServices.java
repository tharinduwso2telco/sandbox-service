/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.core.dbutils.exception.ThrowableError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ListProvisionedDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ListProvisionedRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceInfoListProvisionedDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceListProvisioned;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceListProvisionedDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceMetaInfoListProvisionedDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.exception.SandboxException.SandboxErrorType;
import com.wso2telco.services.dep.sandbox.service.SandboxDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.ProvisioningStatusCodes;
import com.wso2telco.services.dep.sandbox.util.ProvisioningUtil;
import com.wso2telco.services.dep.sandbox.util.ProvisioningUtil.ProvisionRequestTypes;

/**
 * 
 * @author WSO2Telco
 *
 */
public class ListActiveProvisionedServices extends
		AbstractRequestHandler<ListProvisionedRequestWrapperDTO> {

	private ProvisioningDAO provisioningDao;
	private ListProvisionedRequestWrapperDTO requestWrapperDTO;
	private ListActiveProvisionedServicesResponseWrapper responseWrapper;
	private final String NUMERIC_REGEX = "[0-9]+";

	{
		LOG = LogFactory.getLog(ListActiveProvisionedServices.class);
		provisioningDao = DaoFactory.getProvisioningDAO();
		dao = DaoFactory.getGenaricDAO();
	}

	@Override
	protected Returnable getResponseDTO() {
		return responseWrapper;
	}

	@Override
	protected List<String> getAddress() {
		List<String> address = new ArrayList<String>();
		address.add(requestWrapperDTO.getMsisdn());
		return address;
	}

	@Override
	protected boolean validate(ListProvisionedRequestWrapperDTO wrapperDTO)
			throws Exception {

		String msisdn = CommonUtil
				.getNullOrTrimmedValue(wrapperDTO.getMsisdn());
		String offset = CommonUtil
				.getNullOrTrimmedValue(wrapperDTO.getOffSet());
		String limit = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getLimit());
		String mcc = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMcc());
		String mnc = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMnc());
		String onBehalfOf = CommonUtil.getNullOrTrimmedValue(wrapperDTO
				.getOnBehalfOf());
		String purchaseCategoryCode = CommonUtil
				.getNullOrTrimmedValue(wrapperDTO.getPurchaseCategoryCode());
		String requestIdentifier = CommonUtil.getNullOrTrimmedValue(wrapperDTO
				.getRequestIdentifier());

		List<ValidationRule> validationRulesList = new ArrayList<>();

		try {

			validationRulesList.add(new ValidationRule(
					ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID,
					"msisdn", msisdn));
			validationRulesList.add(new ValidationRule(
					ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO,
					"offset", offset));
			validationRulesList.add(new ValidationRule(
					ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO,
					"limit", limit));
			
			
			if ((mcc != null && mcc.trim().length() > 0)
					|| (mnc != null && mnc.trim().length() > 0)) {

				validationRulesList.add(new ValidationRule(
						ValidationRule.VALIDATION_TYPE_MANDATORY_NUMBER, "mcc",
						mcc));
				validationRulesList.add(new ValidationRule(
						ValidationRule.VALIDATION_TYPE_MANDATORY_NUMBER, "mnc",
						mnc));
			} else {

				validationRulesList.add(new ValidationRule(
						ValidationRule.VALIDATION_TYPE_OPTIONAL, "mcc", mcc));
				validationRulesList.add(new ValidationRule(
						ValidationRule.VALIDATION_TYPE_OPTIONAL, "mnc", mnc));
			}

			if (offset != null && isNumeric(offset)) {

				validationRulesList.add(new ValidationRule(
						ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO,
						"offset", Integer.parseInt(offset)));
			} else if (offset != null) {
				responseWrapper
						.setRequestError(constructRequestError(
								SERVICEEXCEPTION, "SVC0002",
								"Invalid input value for message part %1",
								"Parameter offset expected Numeric received "
										+ offset));
			}

			if (limit != null && isNumeric(limit)) {

				validationRulesList.add(new ValidationRule(
						ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO,
						"limit", limit));
			} else if (limit != null) {
				responseWrapper.setRequestError(constructRequestError(
						SERVICEEXCEPTION, "SVC0002",
						"Invalid input value for message part %1",
						"Parameter limit expected Numeric received " + limit));
			}

			validationRulesList.add(new ValidationRule(
					ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf",
					onBehalfOf));
			validationRulesList.add(new ValidationRule(
					ValidationRule.VALIDATION_TYPE_OPTIONAL,
					"purchaseCategoryCode", purchaseCategoryCode));

			validationRulesList.add(new ValidationRule(
					ValidationRule.VALIDATION_TYPE_MANDATORY,
					"requestIdentifier", requestIdentifier));

			if (requestIdentifier != null
					&& checkRequestIdentifierSize(requestIdentifier)) {

				validationRulesList.add(new ValidationRule(
						ValidationRule.VALIDATION_TYPE_MANDATORY,
						"requestIdentifier", requestIdentifier));
			} else {
				responseWrapper.setRequestError(constructRequestError(
						SERVICEEXCEPTION, "SVC0002",
						"Invalid input value for message part %1",
						"requestIdentifier"));

			}
			ValidationRule[] validationRules = new ValidationRule[validationRulesList
					.size()];
			validationRules = validationRulesList.toArray(validationRules);

			Validation.checkRequestParams(validationRules);
		} catch (CustomException ex) {
			LOG.error("###PROVISION### Error in Validation : " + ex);
			String errorMessage = "";
			if (ex.getErrvar() != null && ex.getErrvar().length > 0) {
				errorMessage = ex.getErrvar()[0];
			}
			responseWrapper.setRequestError(constructRequestError(
					SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(),
					errorMessage));

		}

		return true;
	}

	@Override
	protected Returnable process(
			ListProvisionedRequestWrapperDTO extendedRequestDTO)
			throws Exception {

		if (responseWrapper.getRequestError() != null) {
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			return responseWrapper;

		} 
		
		User user = extendedRequestDTO.getUser();

		ProvisioningUtil.saveProvisioningRequestDataLog(
				ProvisionRequestTypes.LIST_ACTIVE_PROVISIONED_SERVICES
						.toString(), extendedRequestDTO.getMsisdn(),
				user, null, null, null, null, new Date());

		String msisdn = extendedRequestDTO.getMsisdn();
		Integer offset = CommonUtil
				.convertStringToInteger(extendedRequestDTO.getOffSet());
		Integer limit = CommonUtil
				.convertStringToInteger(extendedRequestDTO.getLimit());
		String mcc = CommonUtil
				.getNullOrTrimmedValue(extendedRequestDTO.getMcc());
		String mnc = CommonUtil
				.getNullOrTrimmedValue(extendedRequestDTO.getMnc());
		String phoneNumber = null;
		
			try {
				

				if (msisdn != null) {
					phoneNumber = CommonUtil.extractNumberFromMsisdn(msisdn);
				}
				ManageNumber number = dao.getMSISDN(phoneNumber, null, mcc,
						mnc, extendedRequestDTO.getUser().getUserName());

				if (number == null) {
					LOG.error("###PROVISION### Valid MSISDN doesn't exists for the given inputs");
					responseWrapper
							.setRequestError(constructRequestError(
									SERVICEEXCEPTION,
									ServiceError.INVALID_INPUT_VALUE,
									"Valid MSISDN does not exist for the given mnc,mcc parameters"));
					responseWrapper.setHttpStatus(Status.BAD_REQUEST);
					return responseWrapper;
				} else {
					phoneNumber = number.getNumber();
				}

				List<ListProvisionedDTO> provisionedServices = provisioningDao
						.getActiveProvisionedServices(phoneNumber,
								user.getUserName(), offset, limit);
				ServiceListProvisioned serviceList = new ServiceListProvisioned();
				if (provisionedServices != null
						&& !provisionedServices.isEmpty()) {
					for (ListProvisionedDTO service : provisionedServices) {

						ArrayList<ServiceMetaInfoListProvisionedDTO> metaServiceInfoList = new ArrayList<ServiceMetaInfoListProvisionedDTO>();

						ServiceMetaInfoListProvisionedDTO metaServiceInfoMap = new ServiceMetaInfoListProvisionedDTO();
						metaServiceInfoMap.setTag(service.getTag());
						metaServiceInfoMap.setValue(service.getValue());
						metaServiceInfoList.add(metaServiceInfoMap);

						ServiceInfoListProvisionedDTO serviceInfo = serviceList
								.addNewServiceInfo();
						serviceInfo.setServiceCode(service.getServiceCode());
						serviceInfo.setDescription(service.getDescription());
						serviceInfo.setTimestamp(service.getCreatedDate());
						serviceInfo.setServiceInfo(metaServiceInfoList);
					}
				} else {
					LOG.error("###PROVISION### Valid Provisioned Services Not Available for msisdn: "
							+ msisdn);
					responseWrapper.setRequestError(constructRequestError(
							SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
							"Valid Provisioned Services Not Available for "
									+ extendedRequestDTO.getMsisdn()));
					responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
					return responseWrapper;
				}

				serviceList.setResourceURL(CommonUtil
						.getResourceUrl(extendedRequestDTO));
				serviceList.setOnBehalfOf(CommonUtil
						.getNullOrTrimmedValue(extendedRequestDTO
								.getOnBehalfOf()));
				serviceList.setpurchaseCategoryCode(CommonUtil
						.getNullOrTrimmedValue(extendedRequestDTO
								.getPurchaseCategoryCode()));
				serviceList.setRequestIdentifier(CommonUtil
						.getNullOrTrimmedValue(extendedRequestDTO
								.getRequestIdentifier()));
				serviceList.setResponseIdentifier("RES"
						+ RandomStringUtils.randomAlphabetic(8));
				responseWrapper.setHttpStatus(Response.Status.OK);
				ServiceListProvisionedDTO serviceListDTO = new ServiceListProvisionedDTO();
				serviceListDTO.setServiceList(serviceList);
				responseWrapper.setServiceListDTO(serviceListDTO);

			} catch (Exception ex) {
				LOG.error("###PROVISION### Error Occured in List Provisioned Service. "
						+ ex);
				responseWrapper.setRequestError(constructRequestError(
						SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED,
						"Error Occured in List Provisioned Service for "
								+ extendedRequestDTO.getMsisdn()));
				responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
			}
		
		return responseWrapper;
	}

	@Override
	protected void init(ListProvisionedRequestWrapperDTO extendedRequestDTO)
			throws Exception {
		requestWrapperDTO = extendedRequestDTO;
		responseWrapper = new ListActiveProvisionedServicesResponseWrapper();
	}

	private boolean checkRequestIdentifierSize(String requestIdentifier) {

		int size = SandboxDTO.getRequestIdentifierSize();

		if (requestIdentifier.length() >= size) {

			return true;
		} else {

			return false;
		}
	}

	private boolean isNumeric(String input) {

		if (input.matches(NUMERIC_REGEX)) {

			return true;
		}

		return false;
	}

}
