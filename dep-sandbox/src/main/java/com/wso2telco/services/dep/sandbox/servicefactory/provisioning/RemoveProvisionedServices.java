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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CallbackReference;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RemoveProvisionRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RemoveProvisionRequestBean.ServiceRemoveRequest;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RemoveProvisionedRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestCallbackReference;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionAllService;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionMSISDNServicesMap;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionResponseMessage;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionedServices;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Status;
//import com.wso2telco.core.dbutils.exception.;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.provisioning.RemoveProvisionedResponseBean.ServiceRemoveResponse;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.ProvisioningStatusCodes;
import com.wso2telco.services.dep.sandbox.util.ProvisioningUtil;
import com.wso2telco.services.dep.sandbox.util.ProvisioningUtil.ProvisionRequestTypes;

public class RemoveProvisionedServices extends AbstractRequestHandler<RemoveProvisionedRequestWrapperDTO> {

	private ProvisioningDAO provisioningDao;
	private RemoveProvisionedRequestWrapperDTO requestWrapperDTO;
	private RemoveProvisionedServicesResponseWrapper responseWrapper;
	
	{
		LOG = LogFactory.getLog(RemoveProvisionedServices.class);
		provisioningDao = DaoFactory.getProvisioningDAO();
		dao = DaoFactory.getGenaricDAO();
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler
	 * #getResponseDTO()
	 */
	@Override
	protected Returnable getResponseDTO() {
		return responseWrapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler
	 * #getAddress()
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
	 * com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler
	 * #validate(com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO)
	 */
	@Override
	protected boolean validate(RemoveProvisionedRequestWrapperDTO wrapperDTO) throws Exception {

		RemoveProvisionRequestBean requestBean =wrapperDTO.getRemoveProvisionRequestBean();
		ServiceRemoveRequest request = requestBean.getServiceRemoveRequest();
		
		if (requestBean != null && request != null ) {
			
			RequestCallbackReference callRef = request.getCallbackReference();
			
			if (callRef != null) {

				String msisdn = CommonUtil.getNullOrTrimmedValue(wrapperDTO
						.getMsisdn());
				String serviceCode = CommonUtil.getNullOrTrimmedValue(request
						.getServiceCode());
				String clientCorrelator = CommonUtil
						.getNullOrTrimmedValue(request.getClientCorrelator());
				String clientReferenceCode = CommonUtil
						.getNullOrTrimmedValue(request.getClientReferenceCode());
				String onBehalfOf = CommonUtil.getNullOrTrimmedValue(request
						.getOnBehalfOf());
				String purchaseCategoryCode = CommonUtil
						.getNullOrTrimmedValue(request
								.getPurchaseCategoryCode());
				String notifyURL = CommonUtil.getNullOrTrimmedValue(callRef
						.getNotifyURL());
				String callbackData = CommonUtil.getNullOrTrimmedValue(callRef
						.getCallbackData());
				String mcc = CommonUtil.getNullOrTrimmedValue(wrapperDTO
						.getMcc());
				String mnc = CommonUtil.getNullOrTrimmedValue(wrapperDTO
						.getMnc());
				List<ValidationRule> validationRules = new ArrayList<>();

				try {

					if (mcc != null) {
						validationRules
								.add(new ValidationRule(
										ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO,
										"mcc", mcc));
						validationRules
								.add(new ValidationRule(
										ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO,
										"mnc", mnc));
					} else if (mnc != null) {
						validationRules
								.add(new ValidationRule(
										ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO,
										"mnc", mnc));
						validationRules
								.add(new ValidationRule(
										ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO,
										"mcc", mcc));
					}

					validationRules
							.add(new ValidationRule(
									ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID,
									"msisdn", msisdn));
					validationRules.add(new ValidationRule(
							ValidationRule.VALIDATION_TYPE_MANDATORY,
							"serviceCode", serviceCode));
					validationRules.add(new ValidationRule(
							ValidationRule.VALIDATION_TYPE_OPTIONAL,
							"clientCorrelator", clientCorrelator));
					validationRules.add(new ValidationRule(
							ValidationRule.VALIDATION_TYPE_MANDATORY,
							"clientReferenceCode", clientReferenceCode));
					validationRules.add(new ValidationRule(
							ValidationRule.VALIDATION_TYPE_OPTIONAL,
							"onBehalfOf", onBehalfOf));
					validationRules.add(new ValidationRule(
							ValidationRule.VALIDATION_TYPE_OPTIONAL,
							"purchaseCategoryCode", purchaseCategoryCode));
					validationRules.add(new ValidationRule(
							ValidationRule.VALIDATION_TYPE_MANDATORY_URL,
							"notifyURL", notifyURL));
					validationRules.add(new ValidationRule(
							ValidationRule.VALIDATION_TYPE_MANDATORY,
							"callbackData", callbackData));

					ValidationRule[] validationRuleArray = new ValidationRule[validationRules
							.size()];

					Validation.checkRequestParams(validationRules
							.toArray(validationRuleArray));
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
			} else {
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,ServiceError.INVALID_INPUT_VALUE.getCode(),ServiceError.INVALID_INPUT_VALUE.getMessage(),wrapperDTO.getMsisdn()));
			}

		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler
	 * #process(com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO)
	 */
	@Override
	protected Returnable process(RemoveProvisionedRequestWrapperDTO extendedRequestDTO) throws Exception {
		
		if (responseWrapper.getRequestError() != null) {
			responseWrapper.setHttpStatus(javax.ws.rs.core.Response.Status.BAD_REQUEST);
			return responseWrapper;
		}
			
		RemoveProvisionRequestBean requestBean =extendedRequestDTO.getRemoveProvisionRequestBean();
		ServiceRemoveRequest request = requestBean.getServiceRemoveRequest();
		
		User user = extendedRequestDTO.getUser();
		String serviceCode = CommonUtil.getNullOrTrimmedValue(request.getServiceCode());
		String msisdn = extendedRequestDTO.getMsisdn();
		String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
		String clientReferenceCode = CommonUtil.getNullOrTrimmedValue(request.getClientReferenceCode());
		String notifyURL = CommonUtil.getNullOrTrimmedValue(request.getCallbackReference().getNotifyURL());
		String callbackData = CommonUtil.getNullOrTrimmedValue(request.getCallbackReference().getCallbackData());
		Map<ProvisioningStatusCodes, Status> statusMap = new HashMap<ProvisioningStatusCodes, Status>();
		String mcc = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getMcc());
		String mnc = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getMnc());
		String phoneNumber = null;
		
		try{	
			if (msisdn != null) {
			    phoneNumber = CommonUtil.extractNumberFromMsisdn(msisdn);
			}
			ManageNumber mappedNumber = dao.getMSISDN(phoneNumber, null, mcc, mnc);

			if (mappedNumber == null) {
			    LOG.error("###PROVISION### Valid MSISDN doesn't exists for the given inputs");
			    responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
				    "Valid MSISDN does not exist for the given mnc,mcc parameters"));
			    responseWrapper.setHttpStatus(javax.ws.rs.core.Response.Status.BAD_REQUEST);
			    return responseWrapper;
			} else {
			    phoneNumber = mappedNumber.getNumber();
			}
			ProvisioningUtil.saveProvisioningRequestDataLog(ProvisionRequestTypes.DELETE_PROVISION_SERVICE.toString(),
					extendedRequestDTO.getMsisdn(), user, clientCorrelator, clientReferenceCode, notifyURL, callbackData,
					new Date());
			// check whether the requested service is valid
			ProvisionAllService availableService = checkServiceCodeValidity(serviceCode,user);
			// check whether the msisdn is registered for the requested service
			checkServiceValidityForMSISDN(phoneNumber, user.getUserName(),availableService);
			// check whether the SP desire any Fail Error Messages
			checkIfErrorMessageSet(phoneNumber, user.getUserName(),serviceCode);
			// Returns map of all available transaction status
			statusMap = getAllTransactionStatus();
			// this returns the servie which is eligible for removing
			ProvisionedServices provisionedCheckList = provisioningDao.getAlreadyProvisioned(phoneNumber, user.getUserName(),serviceCode);
			
			if (provisionedCheckList != null) {	
				// update db for removal of provisioned service
				ProvisionedServices deletedServiceList = removeProvisionedService(provisionedCheckList, clientCorrelator, clientReferenceCode, notifyURL, callbackData,statusMap);
				// create json body from updated service object
				buildJsonResponseBody(deletedServiceList);
				responseWrapper.setHttpStatus(Response.Status.OK);
				return responseWrapper;
			} else {
				// check for already removed provisioned service for request duplication against client correlator
				boolean isDuplicate = checkRequestDuplication(phoneNumber, user.getUserName(),serviceCode, clientCorrelator);
				// non matching client correlator
				if(!isDuplicate){
					buildJsonResponseBody(serviceCode,clientCorrelator,clientReferenceCode,callbackData,notifyURL,statusMap.get(ProvisioningStatusCodes.PRV_DELETE_NOT_ACTIVE).getStatus());
					return responseWrapper;
				}
			}
		} catch(Exception ex) {
			LOG.error("###PROVISION### Error in processing provision service request. ", ex);
			if (responseWrapper.getRequestError() != null) {
				return responseWrapper;
			}
			buildJsonResponseBody(serviceCode,clientCorrelator,clientReferenceCode,callbackData,notifyURL,statusMap.get(ProvisioningStatusCodes.PRV_DELETE_FAILED).getStatus());
			return responseWrapper;
		}
		return responseWrapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler#
	 * init(com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO)
	 */
	@Override
	protected void init(RemoveProvisionedRequestWrapperDTO extendedRequestDTO) throws Exception {
		requestWrapperDTO = extendedRequestDTO;
		responseWrapper = new RemoveProvisionedServicesResponseWrapper();
		
	}
	
	private boolean checkRequestDuplication(String msisdn, String userName, String serviceCode, String clientCorrelator) throws Exception{
		ProvisionedServices service = provisioningDao.checkClientCorrelator(msisdn, userName,serviceCode, clientCorrelator);
		// indicates request duplication
		if (service != null) {
			//resend previous response
			buildJsonResponseBody(service);			
			responseWrapper.setHttpStatus(Response.Status.OK);
			return true;
		} 
		return false;
	}
	
	private void buildJsonResponseBody(String serviceCode,
			String clientCorrelator, String clientReferenceCode,
			String callbackData, String notifyURL, String status) {

		CallbackReference ref = new CallbackReference();
		ref.setCallbackData(callbackData);
		ref.setNotifyURL(notifyURL);
		ref.setResourceURL(CommonUtil.getPostResourceUrl(requestWrapperDTO));
		ServiceRemoveResponse serviceRemovalResponse = new ServiceRemoveResponse();
		serviceRemovalResponse.setCallbackReference(ref);
		serviceRemovalResponse.setServiceCode(serviceCode);
		serviceRemovalResponse.setClientCorrelator(clientCorrelator);
		serviceRemovalResponse
				.setServerReferenceCode(ProvisioningUtil.SERVER_REFERENCE_CODE);
		serviceRemovalResponse.setClientReferenceCode(clientReferenceCode);
		serviceRemovalResponse.setTransactionStatus(status);
		serviceRemovalResponse.setOnBehalfOf(CommonUtil
				.getNullOrTrimmedValue(requestWrapperDTO
						.getRemoveProvisionRequestBean()
						.getServiceRemoveRequest().getOnBehalfOf()));
		serviceRemovalResponse.setPurchaseCatergoryCode(CommonUtil
				.getNullOrTrimmedValue(requestWrapperDTO
						.getRemoveProvisionRequestBean()
						.getServiceRemoveRequest().getPurchaseCategoryCode()));
		RemoveProvisionedResponseBean removeProvisionResponseBean = new RemoveProvisionedResponseBean();
		removeProvisionResponseBean
				.setServiceRemoveResponse(serviceRemovalResponse);
		responseWrapper
				.setRemoveProvisionedResponseBean(removeProvisionResponseBean);
		responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);

	}

	private void buildJsonResponseBody(ProvisionedServices deletedServiceList) {

		CallbackReference ref = new CallbackReference();
		ref.setCallbackData(deletedServiceList.getCallbackData());
		ref.setNotifyURL(deletedServiceList.getNotifyURL());
		ref.setResourceURL(CommonUtil.getPostResourceUrl(requestWrapperDTO));
		ServiceRemoveResponse serviceRemovalResponse = new ServiceRemoveResponse();
		serviceRemovalResponse.setCallbackReference(ref);
		serviceRemovalResponse.setServiceCode(deletedServiceList
				.getMSISDNServicesMapId().getServiceId().getServiceCode());
		serviceRemovalResponse.setClientCorrelator(deletedServiceList
				.getClientCorrelator());
		serviceRemovalResponse
				.setServerReferenceCode(ProvisioningUtil.SERVER_REFERENCE_CODE);
		serviceRemovalResponse.setClientReferenceCode(deletedServiceList
				.getClientReferenceCode());
		serviceRemovalResponse.setTransactionStatus(deletedServiceList
				.getStatus().getStatus());
		serviceRemovalResponse.setOnBehalfOf(CommonUtil
				.getNullOrTrimmedValue(requestWrapperDTO
						.getRemoveProvisionRequestBean()
						.getServiceRemoveRequest().getOnBehalfOf()));
		serviceRemovalResponse.setPurchaseCatergoryCode(CommonUtil
				.getNullOrTrimmedValue(requestWrapperDTO
						.getRemoveProvisionRequestBean()
						.getServiceRemoveRequest().getPurchaseCategoryCode()));
		RemoveProvisionedResponseBean removeProvisionResponseBean = new RemoveProvisionedResponseBean();
		removeProvisionResponseBean
				.setServiceRemoveResponse(serviceRemovalResponse);
		responseWrapper
				.setRemoveProvisionedResponseBean(removeProvisionResponseBean);
	}


	private ProvisionedServices removeProvisionedService(ProvisionedServices provisionedCheckList, String clientCorrelator, String clientReferenceCode, String notifyURL, String callbackData,
			Map<ProvisioningStatusCodes, Status> statusMap) throws Exception {
		
		provisionedCheckList.setCallbackData(callbackData);
		provisionedCheckList.setClientCorrelator(clientCorrelator);
		provisionedCheckList.setClientReferenceCode(clientReferenceCode);
		provisionedCheckList.setNotifyURL(notifyURL);
		provisionedCheckList.setStatus(statusMap.get(ProvisioningUtil.DEFAULT_REMOVE_STATUS));
		provisionedCheckList.setCreatedDate(new Date());
		// remove provisioned db update 
		provisioningDao.updateDeleteStatus(provisionedCheckList);
		return provisionedCheckList;
		
	}


	private void checkIfErrorMessageSet(String msisdn, String userName,String serviceCode) throws Exception{
		
		ProvisionResponseMessage errorResponse = provisioningDao.getErrorResponse(msisdn, userName,serviceCode);
		if (errorResponse != null ) {
				responseWrapper.setRequestError(constructRequestError(errorResponse.getResponseMessageCatergory().getId(),errorResponse.getResponseCode(),
						errorResponse.getResponseMessage(),msisdn));
				responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
				throw new SandboxException(ServiceError.SERVICE_ERROR_OCCURED);
			}
		}		
	

	private void checkServiceValidityForMSISDN(String msisdn, String userName,ProvisionAllService availableService) throws Exception {
		
		ManageNumber number = provisioningDao.getNumber(msisdn, userName);
		ProvisionMSISDNServicesMap serviceCheckList = provisioningDao.getProvisionMsisdnService(number,
				availableService);
		if (serviceCheckList == null) {
			responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
					ServiceError.INVALID_INPUT_VALUE, "Number is not Registered for the Service"));
			responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
			throw new SandboxException(ServiceError.INVALID_INPUT_VALUE);
		}
	}

	private ProvisionAllService checkServiceCodeValidity(String serviceCode, User user) throws Exception {
		
		ProvisionAllService availableService = provisioningDao.getProvisionService(serviceCode, null, user);
		if (availableService == null) {
			responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
					ServiceError.INVALID_INPUT_VALUE, "Provided Service Code is not valid"));
			responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
			throw new SandboxException(ServiceError.INVALID_INPUT_VALUE);
		}
		return availableService;
		
	}
	
	private Map<ProvisioningStatusCodes, Status> getAllTransactionStatus() throws Exception{
		
		Map<ProvisioningStatusCodes, Status> statusMap = new HashMap<ProvisioningStatusCodes, Status>();
		List<Status> status = provisioningDao.getTransactionStatus();
		for (Status states : status) {
			statusMap.put(ProvisioningStatusCodes.valueOf(states.getCode()),states);
		}		
		return statusMap;
	}
}
