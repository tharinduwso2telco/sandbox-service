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

import org.apache.commons.logging.LogFactory;

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
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.exception.SandboxException.SandboxErrorType;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.ProvisioningUtil;
import com.wso2telco.services.dep.sandbox.util.ProvisioningUtil.ProvisionRequestTypes;
/**
 * 
 * @author WSO2Telco
 *
 */
public class ListActiveProvisionedServices extends AbstractRequestHandler<ListProvisionedRequestWrapperDTO> {

	private ProvisioningDAO provisioningDao;
	private ListProvisionedRequestWrapperDTO requestWrapperDTO;
	private ListActiveProvisionedServicesResponseWrapper responseWrapper;

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
	protected boolean validate(ListProvisionedRequestWrapperDTO wrapperDTO) throws Exception {

		String msisdn = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMsisdn());
		String offset = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getOffSet());
		String limit = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getLimit());

		try {
			ValidationRule[] validationRules = {
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "msisdn", msisdn),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "offset", offset),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "limit", limit) };

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
	protected Returnable process(ListProvisionedRequestWrapperDTO extendedRequestDTO) throws Exception {
		try {
			User user = extendedRequestDTO.getUser();

			ProvisioningUtil.saveProvisioningRequestDataLog(ProvisionRequestTypes.LIST_ACTIVE_PROVISIONED_SERVICES.toString(), extendedRequestDTO.getMsisdn(), user,
					null, null, null, null, new Date());
			LOG.debug(extendedRequestDTO.getMsisdn());
			
			String msisdn =getLastMobileNumber(extendedRequestDTO.getMsisdn());
			Integer offset = CommonUtil.convertStringToInteger(extendedRequestDTO.getOffSet());
			Integer limit = CommonUtil.convertStringToInteger(extendedRequestDTO.getLimit());

			List<ListProvisionedDTO> provisionedServices = provisioningDao.getActiveProvisionedServices(msisdn,user.getUserName(),offset, limit);
			
			
			ServiceListProvisioned serviceList = new ServiceListProvisioned();
		
			if (provisionedServices != null && !provisionedServices.isEmpty()) {
				
				for (ListProvisionedDTO service : provisionedServices) {
					
					ArrayList<ServiceMetaInfoListProvisionedDTO> metaServiceInfoList = new ArrayList<ServiceMetaInfoListProvisionedDTO>();
				
					ServiceMetaInfoListProvisionedDTO metaServiceInfoMap =new ServiceMetaInfoListProvisionedDTO();
					metaServiceInfoMap.setTag(service.getTag());
					metaServiceInfoMap.setValue(service.getValue());
					metaServiceInfoList.add(metaServiceInfoMap);
					
					ServiceInfoListProvisionedDTO serviceInfo = serviceList.addNewServiceInfo();
					serviceInfo.setServiceCode(service.getServiceCode());
					serviceInfo.setDescription(service.getDescription());
					serviceInfo.setTimeStamp(service.getCreatedDate());
					serviceInfo.setServiceInfo(metaServiceInfoList);
				}
			}else {
				LOG.error("###PROVISION### Valid Provisioned Services Not Available for msisdn: " + msisdn);
				throw new SandboxException(SandboxErrorType.NO_VALID_SERVICES_AVAILABLE);
			}

			serviceList.setResourceURL(ProvisioningUtil.getResourceUrl(extendedRequestDTO));

			responseWrapper.setHttpStatus(Response.Status.OK);
			
			ServiceListProvisionedDTO serviceListDTO = new ServiceListProvisionedDTO();
			serviceListDTO.setServiceList(serviceList);
			
			responseWrapper.setServiceListDTO(serviceListDTO);

		} catch (Exception ex) {
			LOG.error("###PROVISION### Error Occured in List Provisioned Service. " + ex);
			throw new SandboxException(SandboxErrorType.SERVICE_ERROR);
		}
		return responseWrapper;
	}

	@Override
	protected void init(ListProvisionedRequestWrapperDTO extendedRequestDTO) throws Exception {
		requestWrapperDTO = extendedRequestDTO;
		responseWrapper = new ListActiveProvisionedServicesResponseWrapper();
	}

}
