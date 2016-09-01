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

import javax.ws.rs.core.Response;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.QueryProvisioningServicesRequestWrapper;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceInfo;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceList;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceListDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Service;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.ProvisioningUtil;

public class QueryApplicableProvisioningService
		extends AbstractRequestHandler<QueryProvisioningServicesRequestWrapper> {

	private ProvisioningDAO provisioningDao;
	private QueryProvisioningServicesRequestWrapper requestWrapperDTO;
	private QueryApplicableProvisioningServiceResponseWrapper responseWrapper;

	{
		LOG = LogFactory.getLog(QueryApplicableProvisioningService.class);
		provisioningDao = DaoFactory.getProvisioningDAO();
		dao = DaoFactory.getGenaricDAO();
	}

	@Override
	protected Returnable getResponseDTO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<String> getAddress() {
		List<String> address = new ArrayList<String>();
		return address;
	}

	@Override
	protected boolean validate(QueryProvisioningServicesRequestWrapper wrapperDTO) throws Exception {

		CommonUtil.validateMsisdn(wrapperDTO.getMsisdn());
		CommonUtil.validatePositiveNumber(wrapperDTO.getOffSet(), "offset");
		CommonUtil.validatePositiveNumber(wrapperDTO.getLimit(), "limit");

		return true;
	}

	@Override
	protected Returnable process(QueryProvisioningServicesRequestWrapper extendedRequestDTO) throws Exception {
		try {
			User user = extendedRequestDTO.getUser();

			ProvisioningUtil.saveProvisioningRequestDataLog("QUERY_APPLICABLE", extendedRequestDTO.getMsisdn(), user,
					null, null, null, null, new Date());

			Integer offset = CommonUtil.convertStringToInteger(extendedRequestDTO.getOffSet());
			Integer limit = CommonUtil.convertStringToInteger(extendedRequestDTO.getLimit());

			List<Service> applicableServices = provisioningDao.getApplicableProvisionServices(offset, limit);

			ServiceList serviceList = new ServiceList();

			if (applicableServices != null && !applicableServices.isEmpty()) {
				for (Service service : applicableServices) {
					ServiceInfo serviceInfo = serviceList.addNewServiceInfo();
					serviceInfo.setServiceType(service.getServiceType());
					serviceInfo.setServiceCode(service.getServiceCode());
					serviceInfo.setDescription(service.getDescription());
					serviceInfo.setServiceCharge(service.getServiceCharge());
				}
			}

			serviceList.setCurrencyCode(ProvisioningUtil.DEFAULT_CURRENCY_CODE);
			serviceList.setResourceURL(ProvisioningUtil.getResourceUrl(extendedRequestDTO));

			responseWrapper.setHttpStatus(Response.Status.OK);
			ServiceListDTO serviceListDTO = new ServiceListDTO();
			serviceListDTO.setServiceList(serviceList);
			responseWrapper.setServiceListDTO(serviceListDTO);

		} catch (Exception ex) {
			LOG.info(ex);
		}
		return responseWrapper;
	}

	@Override
	protected void init(QueryProvisioningServicesRequestWrapper extendedRequestDTO) throws Exception {
		requestWrapperDTO = extendedRequestDTO;
		responseWrapper = new QueryApplicableProvisioningServiceResponseWrapper();
	}

}
