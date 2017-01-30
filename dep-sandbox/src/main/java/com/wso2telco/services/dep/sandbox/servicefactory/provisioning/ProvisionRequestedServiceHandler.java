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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.core.msisdnvalidator.InvalidMSISDNException;
import com.wso2telco.core.msisdnvalidator.MSISDN;
import com.wso2telco.core.msisdnvalidator.MSISDNUtil;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CallbackReference;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisionRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisionResponseBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisionResponseBean.ServiceProvisionResponse;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestCallbackReference;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceProvisionRequestWrapper;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionAllService;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionMSISDNServicesMap;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionResponseMessage;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionedServices;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.ProvisioningStatusCodes;
import com.wso2telco.services.dep.sandbox.util.ProvisioningUtil;
import com.wso2telco.services.dep.sandbox.util.ProvisioningUtil.ProvisionRequestTypes;

public class ProvisionRequestedServiceHandler extends AbstractRequestHandler<ServiceProvisionRequestWrapper> {

	private ProvisioningDAO provisioningDao;
	private ServiceProvisionRequestWrapper requestWrapperDTO;
	private ProvisionRequestedServiceResponseWrapper responseWrapperDTO;

	{
		LOG = LogFactory.getLog(ProvisionRequestedServiceHandler.class);
		provisioningDao = DaoFactory.getProvisioningDAO();
		dao = DaoFactory.getGenaricDAO();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler#
	 * getResponseDTO()
	 */
	@Override
	protected Returnable getResponseDTO() {
		// TODO Auto-generated method stub
		return responseWrapperDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler#
	 * getAddress()
	 */
	@Override
	protected List<String> getAddress() {
		List<String> address = new ArrayList<String>();
		address.add(requestWrapperDTO.getMsisdn());
		return address;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler#
	 * validate(com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO)
	 */
	@Override
	protected boolean validate(ServiceProvisionRequestWrapper wrapperDTO) throws Exception {
		try {
			String msisdn = wrapperDTO.getMsisdn();
			String mcc = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMcc());
			String mnc = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMnc());
			
			List<ValidationRule> validationRules = new ArrayList<>();
			
			if ((mcc != null && mcc.trim().length() > 0)
					|| (mnc != null && mnc.trim().length() > 0)) {

				validationRules.add(new ValidationRule(
						ValidationRule.VALIDATION_TYPE_MANDATORY_NUMBER, "mcc",
						mcc));
				validationRules.add(new ValidationRule(
						ValidationRule.VALIDATION_TYPE_MANDATORY_NUMBER, "mnc",
						mnc));
			} else {

				validationRules.add(new ValidationRule(
						ValidationRule.VALIDATION_TYPE_OPTIONAL, "mcc", mcc));
				validationRules.add(new ValidationRule(
						ValidationRule.VALIDATION_TYPE_OPTIONAL, "mnc", mnc));
			}
			ProvisionRequestBean requestBean = wrapperDTO.getProvisionRequestBean();
			
			if (requestBean == null || requestBean.getServiceProvisionRequest() == null) {
				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "Empty or Invalid Request Body"));
				return false;
			}

			if (requestBean.getServiceProvisionRequest().getCallbackReference() == null) {
				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "Empty or Invalid CallbackReference"));
				return false;
			}

			String serviceCode = requestBean.getServiceProvisionRequest().getServiceCode();
			String serviceName = requestBean.getServiceProvisionRequest().getServiceName();
			String clientCorrelator = requestBean.getServiceProvisionRequest().getClientCorrelator();
			String onBehalfOf = CommonUtil.getNullOrTrimmedValue(requestBean.getServiceProvisionRequest().getOnBehalfOf());
			String purchaseCategoryCode = CommonUtil.getNullOrTrimmedValue(requestBean.getServiceProvisionRequest().getPurchaseCategoryCode());
			
			String clientReferenceCode = requestBean.getServiceProvisionRequest().getClientReferenceCode();
			String notifyUrl = requestBean.getServiceProvisionRequest().getCallbackReference().getNotifyURL();
			String callbackData = requestBean.getServiceProvisionRequest().getCallbackReference().getCallbackData();

			validationRules.add(new ValidationRule(
					ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf",
					onBehalfOf));
			validationRules.add(new ValidationRule(
					ValidationRule.VALIDATION_TYPE_OPTIONAL,
					"purchaseCategoryCode", purchaseCategoryCode));

			
			
			if (CommonUtil.getNullOrTrimmedValue(serviceCode) == null
					&& CommonUtil.getNullOrTrimmedValue(serviceName) == null) {
				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "ServiceCode and ServiceName Both are Empty"));
				return false;
			}

			validationRules.add(
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "msisdn", msisdn));

			if (CommonUtil.getNullOrTrimmedValue(serviceCode) == null) {
				validationRules
						.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "serviceName", serviceName));
			}

			if (CommonUtil.getNullOrTrimmedValue(serviceName) == null) {
				validationRules
						.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "serviceCode", serviceCode));
			}

			validationRules.add(
					new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator));
			validationRules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "clientReferenceCode",
					clientReferenceCode));
			validationRules
					.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL, "notifyURL", notifyUrl));
			validationRules
					.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "callbackData", callbackData));

			ValidationRule[] validationRuleArray = new ValidationRule[validationRules.size()];

			Validation.checkRequestParams(validationRules.toArray(validationRuleArray));
		} catch (CustomException ex) {
			LOG.error("###PROVISION### Error in Validation : " + ex);
			 String errorMessage = "";
			    if (ex.getErrvar() != null && ex.getErrvar().length > 0) {
				errorMessage = ex.getErrvar()[0];
			    }
			    responseWrapperDTO.setRequestError(
				    constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), errorMessage));

		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler#
	 * process(com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO)
	 */
	@Override
	protected Returnable process(ServiceProvisionRequestWrapper extendedRequestDTO) throws Exception {

		if (responseWrapperDTO.getRequestError() != null) {
			responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			return responseWrapperDTO;
		}

		ProvisionRequestBean requestBean = extendedRequestDTO.getProvisionRequestBean();

		User user = extendedRequestDTO.getUser();
		String msisdn = extendedRequestDTO.getMsisdn();
		
		String mcc = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getMcc());
		String mnc = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getMnc());
		String phoneNumber = null;

		
		String serviceCode = CommonUtil
				.getNullOrTrimmedValue(requestBean.getServiceProvisionRequest().getServiceCode());
		String serviceName = CommonUtil
				.getNullOrTrimmedValue(requestBean.getServiceProvisionRequest().getServiceName());
		String clientCorrelator = CommonUtil
				.getNullOrTrimmedValue(requestBean.getServiceProvisionRequest().getClientCorrelator());
		String clientReferenceCode = CommonUtil
				.getNullOrTrimmedValue(requestBean.getServiceProvisionRequest().getClientReferenceCode());
		String notifyUrl = CommonUtil
				.getNullOrTrimmedValue(requestBean.getServiceProvisionRequest().getCallbackReference().getNotifyURL());
		String callbackData = CommonUtil.getNullOrTrimmedValue(
				requestBean.getServiceProvisionRequest().getCallbackReference().getCallbackData());
		RequestCallbackReference callbackReference = requestBean.getServiceProvisionRequest().getCallbackReference();

		ProvisioningUtil.saveProvisioningRequestDataLog(ProvisionRequestTypes.PROVISION_REQUESTED_SERVICE.toString(),
				extendedRequestDTO.getMsisdn(), user, clientCorrelator, clientReferenceCode, notifyUrl, callbackData,
				new Date());

		try {

			if (msisdn != null) {
			    phoneNumber = CommonUtil.extractNumberFromMsisdn(msisdn);
			}
			ManageNumber mappedNumber = dao.getMSISDN(phoneNumber, null, mcc, mnc,extendedRequestDTO.getUser().getUserName());

			if (mappedNumber == null) {
			    LOG.error("###PROVISION### Valid MSISDN doesn't exists for the given inputs");
			    responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
				    "Valid MSISDN does not exist for the given mnc,mcc parameters"));
			    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			    return responseWrapperDTO;
			} else {
			    phoneNumber = mappedNumber.getNumber();
			}
			ProvisionAllService availableService = provisioningDao.getProvisionService(serviceCode, serviceName, user);

			checkServiceCodeAndServiceNameValidity(serviceName, serviceCode, availableService);

			ProvisionedServices provisionedService = getAlreadyProvisionedService(availableService, phoneNumber, user);

			if (provisionedService != null) {
				if (provisionedService.getClientCorrelator() != null && clientCorrelator != null
						&& provisionedService.getClientCorrelator().equalsIgnoreCase(clientCorrelator)) {
					com.wso2telco.services.dep.sandbox.dao.model.domain.Status responseStatus = provisionedService
							.getStatus();
					buildProvisionResponse(provisionedService, responseStatus, clientReferenceCode, callbackReference);
					responseWrapperDTO.setHttpStatus(Status.OK);
					return responseWrapperDTO;
				} else {
					com.wso2telco.services.dep.sandbox.dao.model.domain.Status responseStatus = provisioningDao
							.getStatusFromStatusCode(ProvisioningStatusCodes.PRV_PROVISION_ALREADY_ACTIVE);
					buildProvisionResponse(provisionedService, responseStatus, clientReferenceCode, callbackReference);
					responseWrapperDTO.setHttpStatus(Status.OK);
					return responseWrapperDTO;
				}
			} else {
				ManageNumber number = provisioningDao.getNumber(phoneNumber, user.getUserName());
				ProvisionMSISDNServicesMap serviceMapEntry = provisioningDao.getProvisionMsisdnService(number,
						availableService);
				if (serviceMapEntry == null) {
					responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
							ServiceError.INVALID_INPUT_VALUE, "Number is not Registered for the Service"));
					responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
					return responseWrapperDTO;
				}

				ProvisionResponseMessage expectedResponseList = provisioningDao.getErrorResponse(phoneNumber,
						user.getUserName(), availableService.getServiceCode());

				if (expectedResponseList != null) {

					responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
							expectedResponseList.getResponseCode(), expectedResponseList.getResponseMessage(), null));
					responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
					return responseWrapperDTO;
				}

				ProvisionedServices service = provisioningDao.checkProvisionClientCorrelator(
						msisdn, user.getUserName(), serviceCode, clientCorrelator);
				if(service== null){
				ProvisionedServices serviceResponse = provisionRequestedService(serviceMapEntry, clientCorrelator,
						clientReferenceCode, notifyUrl, callbackData);
				buildProvisionResponse(serviceResponse, null, clientReferenceCode, callbackReference);
				responseWrapperDTO.setHttpStatus(Status.OK);
				}else{
					
					ProvisionedServices provisionService = new ProvisionedServices();
					provisionService.setId(service.getId());
					provisionService.setMSISDNServicesMapId(serviceMapEntry);
					provisionService.setClientCorrelator(clientCorrelator);
					provisionService.setClientReferenceCode(clientReferenceCode);
					provisionService.setNotifyURL(notifyUrl);
					provisionService.setCallbackData(callbackData);
					provisionService.setCreatedDate(new Date());

					com.wso2telco.services.dep.sandbox.dao.model.domain.Status status = provisioningDao
							.getStatusFromStatusCode(ProvisioningUtil.DEFAULT_PROVISION_STATUS);

					provisionService.setStatus(status);

					provisioningDao.saveProvisionedService(provisionService);

					buildProvisionResponse(provisionService, null, clientReferenceCode, callbackReference);
					responseWrapperDTO.setHttpStatus(Status.OK);
					
				}

			}

		} catch (InvalidMSISDNException ex) {
			LOG.error("###PROVISION### Error in processing provision service request. ", ex);
			responseWrapperDTO.setRequestError(
					constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE, "Invalid MSISDN"));
			responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			return responseWrapperDTO;
		} catch (SandboxException ex) {
			LOG.error("###PROVISION### Error in processing provision service request. ", ex);
			if (responseWrapperDTO.getRequestError() != null) {
				return responseWrapperDTO;
			}
			responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ex.getErrorType().getCode(),
					ex.getErrorType().getMessage(), null));
			responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			return responseWrapperDTO;
		} catch (Exception ex) {
			LOG.error("###PROVISION### Error in processing provision service request. ", ex);
			responseWrapperDTO
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
			responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			return responseWrapperDTO;
		}

		return responseWrapperDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler#
	 * init(com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO)
	 */
	@Override
	protected void init(ServiceProvisionRequestWrapper extendedRequestDTO) throws Exception {
		requestWrapperDTO = extendedRequestDTO;
		responseWrapperDTO = new ProvisionRequestedServiceResponseWrapper();
	}

	private void checkServiceCodeAndServiceNameValidity(String serviceName, String serviceCode,
			ProvisionAllService service) throws SandboxException {
		if (service == null) {
			if (serviceName != null && serviceCode != null) {
				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "serviceName, serviceCode"));
				responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			} else if (serviceName != null) {
				responseWrapperDTO.setRequestError(
						constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE, "serviceName"));
				responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			} else {
				responseWrapperDTO.setRequestError(
						constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE, "serviceCode"));
				responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			}
			throw new SandboxException(ServiceError.INVALID_INPUT_VALUE);
		}
	}

	private ProvisionedServices getAlreadyProvisionedService(ProvisionAllService service, String phoneNumber, User user)
			throws Exception {
		List<String> statusCodes = new ArrayList<>();
		statusCodes.add(ProvisioningStatusCodes.PRV_PROVISION_SUCCESS.toString());
		statusCodes.add(ProvisioningStatusCodes.PRV_PROVISION_PENDING.toString());

		ProvisionedServices provisionedService = provisioningDao.getAlreadyProvisionedService(user, statusCodes,
				service, phoneNumber);

		return provisionedService;
	}

	private void buildProvisionResponse(ProvisionedServices provisionedService,
			com.wso2telco.services.dep.sandbox.dao.model.domain.Status status, String clientReferenceCode, RequestCallbackReference reference) {
		ProvisionResponseBean responseBean = new ProvisionResponseBean();
		ServiceProvisionResponse serviceProvisionResponse = new ServiceProvisionResponse();
		serviceProvisionResponse
				.setServiceCode(provisionedService.getMSISDNServicesMapId().getServiceId().getServiceCode());
		serviceProvisionResponse.setClientCorrelator(provisionedService.getClientCorrelator());
		serviceProvisionResponse.setClientReferenceCode(clientReferenceCode);
		serviceProvisionResponse.setServerReferenceCode(ProvisioningUtil.SERVER_REFERENCE_CODE);
		serviceProvisionResponse.setOnBehalfOf(CommonUtil
				.getNullOrTrimmedValue(requestWrapperDTO
						.getProvisionRequestBean().getServiceProvisionRequest()
						.getOnBehalfOf()));
		serviceProvisionResponse.setpurchaseCategoryCode(CommonUtil
				.getNullOrTrimmedValue(requestWrapperDTO
						.getProvisionRequestBean().getServiceProvisionRequest()
						.getPurchaseCategoryCode()));

		CallbackReference callbackReference = new CallbackReference();
		callbackReference.setCallbackData(reference.getCallbackData());
		callbackReference.setNotifyURL(reference.getNotifyURL());
		callbackReference.setResourceURL(CommonUtil.getPostResourceUrl(requestWrapperDTO));

		serviceProvisionResponse.setCallbackReference(callbackReference);
		if (status != null) {
			serviceProvisionResponse.setTransactionStatus(status.getStatus());
		} else {
			serviceProvisionResponse.setTransactionStatus(provisionedService.getStatus().getStatus());
		}

		responseBean.setServiceProvisionResponse(serviceProvisionResponse);

		responseWrapperDTO.setProvisionResponseBean(responseBean);
	}

	private ProvisionedServices provisionRequestedService(ProvisionMSISDNServicesMap serviceMapEntry,
			String clientCorrelator, String clientReferenceCode, String notifyUrl, String callbackData)
			throws Exception {
		ProvisionedServices provisionService = new ProvisionedServices();

		provisionService.setMSISDNServicesMapId(serviceMapEntry);
		provisionService.setClientCorrelator(clientCorrelator);
		provisionService.setClientReferenceCode(clientReferenceCode);
		provisionService.setNotifyURL(notifyUrl);
		provisionService.setCallbackData(callbackData);
		provisionService.setCreatedDate(new Date());

		com.wso2telco.services.dep.sandbox.dao.model.domain.Status status = provisioningDao
				.getStatusFromStatusCode(ProvisioningUtil.DEFAULT_PROVISION_STATUS);

		provisionService.setStatus(status);

		provisioningDao.saveProvisionedService(provisionService);

		return provisionService;
	}

}
