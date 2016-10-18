package com.wso2telco.services.dep.sandbox.servicefactory.provisionConfig;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.AddServicesMsisdnRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionAllService;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionMSISDNServicesMap;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.ProvisioningStatusCodes;

public class AssignServiceForMsisdn extends
	AbstractRequestHandler<AddServicesMsisdnRequestWrapperDTO> {

    private ProvisioningDAO provisioningDao;
    private AddServicesMsisdnRequestWrapperDTO requestWrapperDTO;
    private AssignServiceForMsisdnResponseWrapper responseWrapperDTO;

    {
	LOG = LogFactory.getLog(AssignServiceForMsisdn.class);
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
	address.add(requestWrapperDTO.getMsisdn());
	return address;
    }

    @Override
    protected boolean validate(AddServicesMsisdnRequestWrapperDTO wrapperDTO)
	    throws Exception {
	String msisdn = CommonUtil
		.getNullOrTrimmedValue(wrapperDTO.getMsisdn());
	String serviceCode = CommonUtil.getNullOrTrimmedValue(wrapperDTO
		.getServiceCode());

	try {
	    ValidationRule[] validationRules = {
		    new ValidationRule(
			    ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID,
			    "msisdn", msisdn),
		    new ValidationRule(
			    ValidationRule.VALIDATION_TYPE_MANDATORY,
			    "serviceCode", serviceCode)

	    };
	    Validation.checkRequestParams(validationRules);
	} catch (CustomException ex) {
	    LOG.error("###PROVISION### Error in Validation : " + ex);
	    responseWrapperDTO.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(),
		    wrapperDTO.getMsisdn()));
	}

	return true;
    }

    @Override
    protected Returnable process(
	    AddServicesMsisdnRequestWrapperDTO extendedRequestDTO)
	    throws Exception {
	String msisdn = getLastMobileNumber(extendedRequestDTO.getMsisdn());
	User user = extendedRequestDTO.getUser();
	String serviceCode = CommonUtil
		.getNullOrTrimmedValue(extendedRequestDTO.getServiceCode());
	try {

	    ManageNumber number = provisioningDao.getNumber(msisdn,
		    user.getUserName());
	    ProvisionAllService availableService = checkServiceCodeValidity(
		    serviceCode, user);
	    checkIfServiceAlreadyExistForMSISDN(number, user.getUserName(),
		    availableService);
	    ProvisionMSISDNServicesMap map = new ProvisionMSISDNServicesMap();
	    map.setMsisdnId(number);
	    map.setServiceId(availableService);
	    provisioningDao.saveServiceForMsisdn(map);
	    responseWrapperDTO.setHttpStatus(Response.Status.CREATED);
	} catch (Exception ex) {
	    LOG.error(
		    "###PROVISION### Error in processing add service for msisdn. ",
		    ex);
	    if (responseWrapperDTO.getRequestError() != null) {
		responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
		return responseWrapperDTO;
	    }
	}
	return responseWrapperDTO;
    }

    private ProvisionAllService checkServiceCodeValidity(String serviceCode,
	    User user) throws Exception {

	ProvisionAllService availableService = provisioningDao
		.getProvisionService(serviceCode, null, user);
	if (availableService == null) {
	    responseWrapperDTO.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Provided Service Code is not valid"));
	    throw new SandboxException(ServiceError.INVALID_INPUT_VALUE);
	}
	return availableService;

    }

    private void checkIfServiceAlreadyExistForMSISDN(ManageNumber number,
	    String userName, ProvisionAllService availableService)
	    throws Exception {

	ProvisionMSISDNServicesMap serviceCheckList = provisioningDao
		.getProvisionMsisdnService(number, availableService);
	if (serviceCheckList != null) {
	    LOG.info("Already exist service for given msisdn");
	    responseWrapperDTO.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Already exist service for given msisdn"));
	    throw new SandboxException(ServiceError.INVALID_INPUT_VALUE);
	}
    }

    @Override
    protected void init(AddServicesMsisdnRequestWrapperDTO extendedRequestDTO)
	    throws Exception {
	requestWrapperDTO = extendedRequestDTO;
	responseWrapperDTO = new AssignServiceForMsisdnResponseWrapper();
    }

}
