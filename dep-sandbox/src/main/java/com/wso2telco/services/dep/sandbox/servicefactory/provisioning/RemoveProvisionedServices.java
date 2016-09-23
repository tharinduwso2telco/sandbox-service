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
import com.wso2telco.core.dbutils.exception.ThrowableError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CallbackReference;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RemoveProvisionRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RemoveProvisionRequestBean.ServiceRemoveRequest;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RemoveProvisionedRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionMSISDNServicesMap;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionResponseMessage;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionedServices;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.exception.SandboxException.SandboxErrorType;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.provisioning.RemoveProvisionedResponseBean.ServiceRemovalResponse;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.ProvisioningStatusCodes;
import com.wso2telco.services.dep.sandbox.util.ProvisioningUtil;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Status;
//import com.wso2telco.core.dbutils.exception.;

public class RemoveProvisionedServices extends AbstractRequestHandler<RemoveProvisionedRequestWrapperDTO> {

	private ProvisioningDAO provisioningDao;
	private RemoveProvisionedRequestWrapperDTO requestWrapperDTO;
	private RemoveProvisionedServicesResponseWrapper responseWrapper;
	
	{
		LOG = LogFactory.getLog(RemoveProvisionedServices.class);
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
	protected boolean validate(RemoveProvisionedRequestWrapperDTO wrapperDTO) throws Exception {

		RemoveProvisionRequestBean requestBean =wrapperDTO.getRemoveProvisionRequestBean();
		ServiceRemoveRequest request = requestBean.getServiceRemovalRequest();
		
		if (requestBean != null && request != null ) {
			
			CallbackReference callRef = request.getCallbackReference();
			
			if (callRef != null){
				
				String msisdn = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMsisdn());
				String serviceCode= CommonUtil.getNullOrTrimmedValue(request.getServiceCode());
				String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
				String clientReferenceCode = CommonUtil.getNullOrTrimmedValue(request.getClientReferenceCode());			
				String notifyURL = CommonUtil.getNullOrTrimmedValue(callRef.getNotifyURL());
				String callbackData =CommonUtil.getNullOrTrimmedValue(callRef.getCallbackData());
			
				try {
					ValidationRule[] validationRules = {
						new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "msisdn", msisdn),
						new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "serviceCode", serviceCode),
						new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
						new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "clientReferenceCode", clientReferenceCode),
						new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL, "notifyURL", notifyURL),
						new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "callbackData", callbackData) 
						};

					Validation.checkRequestParams(validationRules);
				} catch (CustomException ex) {
					LOG.error("###PROVISION### Error in Validation : " + ex);
					responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(),wrapperDTO.getMsisdn()));
					responseWrapper.setHttpStatus(javax.ws.rs.core.Response.Status.BAD_REQUEST);
				}
				return true;
			} else {
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,ServiceError.INVALID_INPUT_VALUE.getCode(),ServiceError.INVALID_INPUT_VALUE.getMessage(),wrapperDTO.getMsisdn()));
				responseWrapper.setHttpStatus(javax.ws.rs.core.Response.Status.BAD_REQUEST);
			}

		}
		return false;
	}

	@Override
	protected Returnable process(RemoveProvisionedRequestWrapperDTO extendedRequestDTO) throws Exception {
		
		if (responseWrapper.getRequestError() == null) {
			try{
				ServiceRemoveRequest request = extendedRequestDTO.getRemoveProvisionRequestBean().getServiceRemovalRequest();
				
				User user = extendedRequestDTO.getUser();
				String serviceCode = CommonUtil.getNullOrTrimmedValue(request.getServiceCode());
				String msisdn = getLastMobileNumber(extendedRequestDTO.getMsisdn());
				String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
				String clientReferenceCode = CommonUtil.getNullOrTrimmedValue(request.getClientReferenceCode());
				String notifyURL = CommonUtil.getNullOrTrimmedValue(request.getCallbackReference().getNotifyURL());
				String callbackData = CommonUtil.getNullOrTrimmedValue(request.getCallbackReference().getCallbackData());
				
				ServiceRemovalResponse serviceRemovalResponse = new ServiceRemovalResponse();
				Map<ProvisioningStatusCodes, Status> statusMap = new HashMap<ProvisioningStatusCodes, Status>();
				
				//checks service assigned for number
				ProvisionMSISDNServicesMap serviceCheckList =provisioningDao.checkService(msisdn, user.getUserName(),serviceCode);
				
				if (serviceCheckList!= null){
				
					// checks whether fail status is expected by SP
					List<ProvisionResponseMessage> errorResponse = provisioningDao.getErrorResponse(msisdn, user.getUserName(),serviceCode);
					if (errorResponse != null && !errorResponse.isEmpty()) {
	
						for (ProvisionResponseMessage response : errorResponse) {
	
							responseWrapper.setRequestError(constructRequestError(response.getResponseMessageCatergory().getId(),response.getResponseCode(),
									response.getResponseMessage(),extendedRequestDTO.getMsisdn()));
							responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
							return responseWrapper;
						}
					}
					
					// getting the successfully provisioned service in order to remove
					ProvisionedServices provisionedCheckList = provisioningDao.getAlreadyProvisioned(msisdn, user.getUserName(),serviceCode);
					
					// getting all available Transaction Status
					List<Status> status = provisioningDao.getTransactionStatus();
					for (Status states : status) {
						statusMap.put(ProvisioningStatusCodes.valueOf(states.getCode()),states);
					}
	
					if (provisionedCheckList != null) {
	
						provisionedCheckList.setCallbackData(callbackData);
						provisionedCheckList.setClientCorrelator(clientCorrelator);
						provisionedCheckList.setClientReferenceCode(clientReferenceCode);
						provisionedCheckList.setNotifyURL(notifyURL);
						provisionedCheckList.setStatus(statusMap.get(ProvisioningStatusCodes.PRV_DELETE_PENDING));
						provisionedCheckList.setCreatedDate(new Date());
						// remove provisioned db update 
						provisioningDao.updateDeleteStatus(provisionedCheckList);
						
						//response json body creation
						CallbackReference ref = new CallbackReference();
						ref.setCallbackData(callbackData);
						ref.setNotifyURL(notifyURL);
						ref.setResourceURL(ProvisioningUtil.getResourceUrl(extendedRequestDTO));
	
						serviceRemovalResponse.setCallbackReference(ref);
						serviceRemovalResponse.setServiceCode(serviceCode);
						serviceRemovalResponse.setClientCorrelator(clientCorrelator);
						serviceRemovalResponse.setServerReferenceCode("serverReferenceCode");
						serviceRemovalResponse.setClientReferenceCode(clientReferenceCode);
						
						serviceRemovalResponse.setTransactionStatus(statusMap.get(ProvisioningStatusCodes.PRV_DELETE_PENDING).getStatus().toString());
						
						responseWrapper.setHttpStatus(Response.Status.OK);
	
							
						} else {
							// if no any success provisioned service available, check for client correlator 
							ProvisionedServices service = provisioningDao.checkClientCorrelator(msisdn, user.getUserName(),serviceCode, clientCorrelator);
		
							if (service != null) {
								// runs when same client correlator found to resend the previous response
								CallbackReference ref = new CallbackReference();
								ref.setCallbackData(service.getCallbackData());
								ref.setNotifyURL(service.getNotifyURL());
								ref.setResourceURL(ProvisioningUtil.getResourceUrl(extendedRequestDTO));
		
								serviceRemovalResponse.setCallbackReference(ref);
								serviceRemovalResponse.setServiceCode(serviceCode);
								serviceRemovalResponse.setClientCorrelator(clientCorrelator);
								serviceRemovalResponse.setServerReferenceCode("serverReferenceCode");
								serviceRemovalResponse.setClientReferenceCode(service.getClientReferenceCode());
								serviceRemovalResponse.setTransactionStatus(service.getStatus().getStatus());
								
								responseWrapper.setHttpStatus(Response.Status.OK);

							} else {
							// runs when client correlator matching failed, so prepare for notactive status
								CallbackReference ref = new CallbackReference();
								ref.setCallbackData(callbackData);
								ref.setNotifyURL(notifyURL);
								ref.setResourceURL(ProvisioningUtil.getResourceUrl(extendedRequestDTO));
		
								serviceRemovalResponse.setCallbackReference(ref);
								serviceRemovalResponse.setServiceCode(serviceCode);
								serviceRemovalResponse.setClientCorrelator(clientCorrelator);
								serviceRemovalResponse.setServerReferenceCode("serverReferenceCode");
								serviceRemovalResponse.setClientReferenceCode(clientReferenceCode);
								serviceRemovalResponse.setTransactionStatus(statusMap.get(ProvisioningStatusCodes.PRV_DELETE_NOT_ACTIVE).getStatus());
							
								responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
						}
					}

				RemoveProvisionedResponseBean removeProvisionResponseBean = new RemoveProvisionedResponseBean();
				removeProvisionResponseBean.setServiceRemovalResponse(serviceRemovalResponse);
				
				responseWrapper.setRemoveProvisionedResponseBean(removeProvisionResponseBean);
				}else{
					responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,ServiceError.INVALID_INPUT_VALUE, "Service Not Available For "+extendedRequestDTO.getMsisdn() ));
					responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
				}
			} catch (Exception ex) {
				LOG.error("###PROVISION### Error Occured in Remove Provisioned Service. " + ex);
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, "Error Occured in Remove Provisioned Service for " +extendedRequestDTO.getMsisdn()));
				responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
			}
		}
		return responseWrapper;
	}

	@Override
	protected void init(RemoveProvisionedRequestWrapperDTO extendedRequestDTO) throws Exception {
		requestWrapperDTO = extendedRequestDTO;
		responseWrapper = new RemoveProvisionedServicesResponseWrapper();
		
	}

}
