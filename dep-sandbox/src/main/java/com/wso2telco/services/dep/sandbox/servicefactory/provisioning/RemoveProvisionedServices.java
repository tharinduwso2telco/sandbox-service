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
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.ThrowableError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CallbackReference;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ListProvisionedDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisionRemoveErrorMessageDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RemoveProvisionRequestBean.ServiceRemoveRequest;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RemoveProvisionedRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionResponseMessage;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.exception.SandboxException.SandboxErrorType;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.provisioning.RemoveProvisionedResponseBean.ServiceRemovalResponse;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.ProvisioningUtil;

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
	protected boolean validate(RemoveProvisionedRequestWrapperDTO wrapperDTO)
			throws Exception {


		String msisdn = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMsisdn());
		ServiceRemoveRequest request = wrapperDTO.getRemoveProvisionRequestBean().getServiceRemovalRequest();
		String serviceCode= CommonUtil.getNullOrTrimmedValue(request.getServiceCode());
		String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
		String clientReferenceCode = CommonUtil.getNullOrTrimmedValue(request.getClientReferenceCode());
		String notifyURL = CommonUtil.getNullOrTrimmedValue(request.getCallbackReference().getNotifyURL());
		String callbackData =CommonUtil.getNullOrTrimmedValue(request.getCallbackReference().getCallbackData());
		
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
			throw new SandboxException(new ThrowableError() {

				@Override
				public String getMessage() {
					return ex.getErrmsg();
				}

				@Override
				public String getCode() {
					return ex.getErrcode();
				}
			});
		} catch (Exception ex) {
			LOG.error("###PROVISION### Error in Validation : " + ex);
			throw new SandboxException(SandboxErrorType.SERVICE_ERROR);
		}

		return true;
	
	}

	@Override
	protected Returnable process(RemoveProvisionedRequestWrapperDTO extendedRequestDTO) throws Exception {
		
		User user = extendedRequestDTO.getUser();
		ServiceRemoveRequest request =extendedRequestDTO.getRemoveProvisionRequestBean().getServiceRemovalRequest();
		String serviceCode= CommonUtil.getNullOrTrimmedValue(request.getServiceCode());
		String msisdn =getLastMobileNumber(extendedRequestDTO.getMsisdn());

		List<ProvisionResponseMessage> errorResponse = provisioningDao.getErrorResponse(msisdn , user.getUserName(), serviceCode);
		if (errorResponse != null && !errorResponse.isEmpty()) {

			for (ProvisionResponseMessage response : errorResponse) {

				Returnable responseDTO = getResponseDTO();
				responseDTO.setRequestError(constructRequestError(response.getResponseMessageCatergory().getId(), response.getResponseCode(), response.getResponseMessage(),extendedRequestDTO.getMsisdn()));
				responseDTO.setHttpStatus(Status.BAD_REQUEST);
				return responseDTO;
			}
		}
	
		
		
		
		
		
		CallbackReference ref = new CallbackReference();
		ref.setCallbackData("callbackData");
		ref.setNotifyURL("notifyURL");
		ref.setResourceURL(ProvisioningUtil.getResourceUrl(extendedRequestDTO));
		
		ServiceRemovalResponse serviceRemovalResponse = new ServiceRemovalResponse();
		serviceRemovalResponse.setCallbackReference(ref);
		serviceRemovalResponse.setServiceCode("serviceCode");
		serviceRemovalResponse.setClientCorrelator("clientCorrelator");
		serviceRemovalResponse.setServerReferenceCode("serverReferenceCode");
		serviceRemovalResponse.setTransactionStatus("transactionStatus");
		serviceRemovalResponse.setClientReferenceCode("clientReferenceCode");
		
		RemoveProvisionedResponseBean removeProvisionResponseBean = new RemoveProvisionedResponseBean();
		removeProvisionResponseBean.setServiceRemovalResponse(serviceRemovalResponse );
		responseWrapper.setRemoveProvisionedResponseBean(removeProvisionResponseBean );
		
		responseWrapper.setHttpStatus(Response.Status.OK);
		return responseWrapper;
	}

	@Override
	protected void init(RemoveProvisionedRequestWrapperDTO extendedRequestDTO) throws Exception {
		requestWrapperDTO = extendedRequestDTO;
		responseWrapper = new RemoveProvisionedServicesResponseWrapper();
		
	}

}
