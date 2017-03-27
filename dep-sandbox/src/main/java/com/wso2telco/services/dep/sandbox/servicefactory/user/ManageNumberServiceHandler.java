package com.wso2telco.services.dep.sandbox.servicefactory.user;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ManageNumberRequest;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ManageNumberRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;

public class ManageNumberServiceHandler extends AbstractRequestHandler<ManageNumberRequestWrapperDTO> {
	
	private NumberDAO numberDAO;
	private ManageNumberRequestWrapperDTO requestWrapper;
	private ManageNumberResponseWrapper responseWrapper;

	{
		LOG = LogFactory.getLog(ManageNumberServiceHandler.class);
		numberDAO = DaoFactory.getNumberDAO();
		dao = DaoFactory.getGenaricDAO();
	}

	@Override
	protected Returnable getResponseDTO() {
		return responseWrapper;
	}

	@Override
	protected List<String> getAddress() {
		List<String> address = new ArrayList<String>();
		return address;
	}

	@Override
	protected boolean validate(ManageNumberRequestWrapperDTO wrapperDTO) throws Exception {
		ManageNumberRequest manageNumberRequest = wrapperDTO.getManageNumberRequest();
		String number = checkMSISDN(manageNumberRequest.getNumber());
		number = CommonUtil.getNullOrTrimmedValue(number);
		String imsi = CommonUtil.getNullOrTrimmedValue(manageNumberRequest.getImsi());
		double numberBalance = manageNumberRequest.getNumberBalance();
		double reservedAmount = manageNumberRequest.getReservedAmount();
		int mcc = manageNumberRequest.getMcc();
		int mnc = manageNumberRequest.getMnc();
		CommonUtil.validatePositiveNumber(imsi, null);
		CommonUtil.validatePositiveNumber(Integer.toString(mcc), null);
		CommonUtil.validatePositiveNumber(Integer.toString(mnc), null);
		try {
			ValidationRule[] validationRules = {
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "number", number),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_NUMBER, "numberBalance", numberBalance),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_NUMBER, "reservedAmount",
							reservedAmount),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "mcc", mcc),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "mnc", mnc) };
			Validation.checkRequestParams(validationRules);
		} catch (CustomException ex) {
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(),
					ex.getErrvar().toString()));
			LOG.error("###MANAGE_NUMBER### Validation Failed...!", ex);
		} catch (Exception ex) {
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
			LOG.error("###MANAGE_NUMBER### Validation Failed...!", ex);
		}

		wrapperDTO.getManageNumberRequest().setNumber(CommonUtil.extractNumberFromMsisdn(number));
		return true;

	}

	@Override
	protected Returnable process(ManageNumberRequestWrapperDTO extendedRequestDTO) throws Exception {
		if (responseWrapper.getRequestError() != null) {
			responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
			return responseWrapper;
		}
		try {
			int userId = extendedRequestDTO.getUser().getId();
			List<String> numbers = new ArrayList<>();
			for (ManageNumber manageNumber : numberDAO.getManageNumbers(userId)) {
				numbers.add(manageNumber.getNumber());
			}
			if (!numbers.contains(extendedRequestDTO.getManageNumberRequest().getNumber())) {
				ManageNumber manageNumber = new ManageNumber();
				manageNumber.setNumber(extendedRequestDTO.getManageNumberRequest().getNumber());
				manageNumber.setBalance(extendedRequestDTO.getManageNumberRequest().getNumberBalance());
				manageNumber.setReserved_amount(extendedRequestDTO.getManageNumberRequest().getReservedAmount());
				manageNumber.setDescription(extendedRequestDTO.getManageNumberRequest().getDescription());
				manageNumber.setStatus(extendedRequestDTO.getManageNumberRequest().getStatus());
				manageNumber.setUser(extendedRequestDTO.getUser());
				manageNumber.setIMSI(extendedRequestDTO.getManageNumberRequest().getImsi());
				manageNumber.setMCC(extendedRequestDTO.getManageNumberRequest().getMcc());
				manageNumber.setMNC(extendedRequestDTO.getManageNumberRequest().getMnc());
				manageNumber.setAltitude(extendedRequestDTO.getManageNumberRequest().getAltitude());
				manageNumber.setLatitude(extendedRequestDTO.getManageNumberRequest().getLatitude());
				manageNumber.setLongitude(extendedRequestDTO.getManageNumberRequest().getLongitude());
				manageNumber.setLocationRetrieveStatus(extendedRequestDTO.getManageNumberRequest().getLocationRetrieveStatus());
				numberDAO.saveManageNumbers(manageNumber);
				responseWrapper.setHttpStatus(Response.Status.CREATED);
				responseWrapper.setStatus("Successful");
			} else {
				LOG.info("Already exist number");
				responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
				responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "Already exist number"));
				return responseWrapper;
			}
		} catch (Exception ex) {
			LOG.error("###MANAGE_NUMBER### Error in processing number insertion service request. ", ex);
			responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
		}

		return responseWrapper;
	}

	@Override
	protected void init(ManageNumberRequestWrapperDTO extendedRequestDTO) throws Exception {
		requestWrapper = extendedRequestDTO;
		responseWrapper = new ManageNumberResponseWrapper();
	}

	private String checkMSISDN(String msisdn) {
		if (msisdn.charAt(0) != '+') {
			msisdn = "+" + msisdn;
		}
		return msisdn;
	}

}
