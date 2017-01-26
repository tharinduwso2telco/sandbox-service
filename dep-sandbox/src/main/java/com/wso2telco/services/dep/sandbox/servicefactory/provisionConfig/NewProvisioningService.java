package com.wso2telco.services.dep.sandbox.servicefactory.provisionConfig;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CommonSuccessResponse;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisioningServicesRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceDetail;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionAllService;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;

public class NewProvisioningService extends AbstractRequestHandler<ProvisioningServicesRequestWrapperDTO> {
	
	
	private ProvisioningDAO provisioningDao;
	private ProvisioningServicesRequestWrapperDTO requestWrapperDTO;
	private ProvisioningServicesResponseWrapperDTO responseWrapperDTO;

	{
		LOG = LogFactory.getLog(NewProvisioningService.class);
		provisioningDao = DaoFactory.getProvisioningDAO();
		dao = DaoFactory.getGenaricDAO();
	}

	@Override
	protected Returnable getResponseDTO() { 
		return responseWrapperDTO;
	}

	@Override
	protected List<String> getAddress() {
		List<String> address = new ArrayList<String>();
		return address;
	}

	@Override
	protected boolean validate(ProvisioningServicesRequestWrapperDTO wrapperDTO) throws Exception {
		
		ServiceDetail serviceDetail = wrapperDTO.getServiceDetail();
		
		String serviceCode = CommonUtil.getNullOrTrimmedValue(serviceDetail.getServiceCode());
		String serviceType = CommonUtil.getNullOrTrimmedValue(serviceDetail.getServiceType());
		String serviceName = CommonUtil.getNullOrTrimmedValue(serviceDetail.getServiceName());
		String description = CommonUtil.getNullOrTrimmedValue(serviceDetail.getDescription());
		BigDecimal serviceCharge = serviceDetail.getServiceCharge();
		
		try {
			ValidationRule[] validationRules = {
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "serviceCode", serviceCode),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "serviceType", serviceType),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "serviceName", serviceName),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "description", description),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_NUMBER, "serviceCharge", serviceCharge) };
			Validation.checkRequestParams(validationRules);
		} catch (CustomException ex) {
			LOG.error("###PROVISION### Error in Validation : " + ex);
			responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(),
					ex.getErrvar().toString()));
		} catch (Exception ex) {
			LOG.error("###PROVISION### Error in Validation : " + ex);
			responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
		}

		return true;
	}

	@Override
	protected Returnable process(ProvisioningServicesRequestWrapperDTO extendedRequestDTO) throws Exception {
		try{
			ProvisionAllService provisionAllService = new ProvisionAllService();
			
			ServiceDetail serviceDetail = extendedRequestDTO.getServiceDetail();
			provisionAllService.setServiceCode(serviceDetail.getServiceCode());
			provisionAllService.setServiceName(serviceDetail.getServiceName());
			provisionAllService.setServiceType(serviceDetail.getServiceType());
			provisionAllService.setDescription(serviceDetail.getDescription());
			provisionAllService.setServiceCharge(serviceDetail.getServiceCharge());
			provisionAllService.setTag(serviceDetail.getTag());
			provisionAllService.setValue(serviceDetail.getValue());
			provisionAllService.setUser(extendedRequestDTO.getUser());
			
			List<ProvisionAllService> serviceList = provisioningDao.getProvisionServices(provisionAllService.getUser().getId());
			List<String> serviceNames = new ArrayList<String>();
			List<String> serviceCodes = new ArrayList<String>();
		
			
			for(ProvisionAllService allService : serviceList){
				serviceCodes.add(allService.getServiceCode());
				serviceNames.add(allService.getServiceName());
			}
			if(!serviceCodes.contains(provisionAllService.getServiceCode()) && !serviceNames.contains(provisionAllService.getServiceName())){
				provisioningDao.saveProvisionService(provisionAllService);
				
				CommonSuccessResponse success = new CommonSuccessResponse();
				success.setStatus("Service added Successfully!!!");
				responseWrapperDTO.setMessage(success);
				responseWrapperDTO.setHttpStatus(Response.Status.OK);
			}else{
				LOG.info("Already exist service");
				responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE, "Already exist service"));
				return responseWrapperDTO;
			}
			
			
		}catch(Exception ex){
			LOG.error("###PROVISION### Error in Process : " + ex);
			responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
			responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
		}
		return responseWrapperDTO;
	}

	@Override
	protected void init(ProvisioningServicesRequestWrapperDTO extendedRequestDTO) throws Exception {
		requestWrapperDTO = extendedRequestDTO;
		responseWrapperDTO = new ProvisioningServicesResponseWrapperDTO();
		
	}

}
