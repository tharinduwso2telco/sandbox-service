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
package com.wso2telco.services.dep.sandbox.servicefactory.customerinfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.CustomerInfoDAO;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.model.custom.Customer;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CustomerInfoDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.customerinfo.GetProfileResponseWrapper.CustomerDTOWrapper;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;

public class GetProfileRequestHandler extends AbstractRequestHandler<GetProfileRequestWrapper> {

    private CustomerInfoDAO customerInfoDAO;
    private GetProfileRequestWrapper requestWrapperDTO;
    private GetProfileResponseWrapper responseWrapperDTO;

    public static final String DOB_DATE_FORMAT = "dd/MM/yyyy";

    {
	LOG = LogFactory.getLog(GetProfileRequestHandler.class);
	customerInfoDAO = DaoFactory.getCustomerInfoDAO();
	dao = DaoFactory.getGenaricDAO();

    }

    @Override
    protected Returnable getResponseDTO() {
	return null;
    }

    @Override
    protected List<String> getAddress() {
	List<String> addressList = new ArrayList<String>();
	String msisdn = CommonUtil.getNullOrTrimmedValue(requestWrapperDTO.getMsisdn());
	if (msisdn != null) {
	    addressList.add(msisdn);
	}

	return addressList;
    }

    @Override
    protected boolean validate(GetProfileRequestWrapper wrapperDTO) throws Exception {
	String msisdn = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMsisdn());
	String imsi = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getImsi());
	String mcc = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMcc());
	String mnc = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMnc());

	List<ValidationRule> validationRulesList = new ArrayList<>();

	try {
	    if (msisdn == null && imsi == null) {
		responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
			ServiceError.INVALID_INPUT_VALUE, "MSISDN and IMSI are missing"));
		responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
	    }

	    if (msisdn != null) {
		validationRulesList.add(
			new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "msisdn", msisdn));
	    }

	    if (imsi != null) {
		validationRulesList
			.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_NUMBER, "imsi", imsi));
	    }

	    if (mcc != null) {
		validationRulesList
			.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "mcc", mcc));
		validationRulesList
			.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO, "mnc", mnc));
	    } else if (mnc != null) {
		validationRulesList
			.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "mnc", mnc));
	    }

	    ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
	    validationRules = validationRulesList.toArray(validationRules);

	    Validation.checkRequestParams(validationRules);
	} catch (CustomException ex) {
	    LOG.error("###CUSTOMERINFO### Error in validations", ex);
	    String errorMessage = "";
	    if (ex.getErrvar() != null && ex.getErrvar().length > 0) {
		errorMessage = ex.getErrvar()[0];
	    }
	    responseWrapperDTO.setRequestError(
		    constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), errorMessage));
	    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
	} catch (Exception ex) {
	    LOG.error("###CUSTOMERINFO### Error in validations", ex);
	    responseWrapperDTO
		    .setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
	    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
	}

	return true;
    }

    @Override
    protected Returnable process(GetProfileRequestWrapper extendedRequestDTO) throws Exception {
	if (responseWrapperDTO.getRequestError() != null) {
	    return responseWrapperDTO;
	}

	String msisdn = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getMsisdn());
	String imsi = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getImsi());
	String mcc = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getMcc());
	String mnc = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getMnc());
	String phoneNumber = null;

	if (msisdn != null) {
	    phoneNumber = CommonUtil.extractNumberFromMsisdn(msisdn);
	}

	ManageNumber number = customerInfoDAO.getMSISDN(phoneNumber, imsi, mcc, mnc);

	if (number == null) {
	    LOG.error("###CUSTOMERINFO### Valid MSISDN doesn't exists for the given inputs");
	    responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Valid MSISDN does not exist for the given input parameters"));
	    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
	    return responseWrapperDTO;
	} else {
	    phoneNumber = number.getNumber();
	}

	CustomerInfoDTO customerInfoDTO = customerInfoDAO.getProfileData(phoneNumber, extendedRequestDTO.getUser());

	if (customerInfoDTO == null) {
	    LOG.error("###CUSTOMERINFO### Customer information does not available");
	    responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    " No Valid Customer information available for the given input parameters"));
	    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
	    return responseWrapperDTO;
	}

	populateResponse(number, customerInfoDTO);
	responseWrapperDTO.setHttpStatus(Status.OK);

	return responseWrapperDTO;
    }

    @Override
    protected void init(GetProfileRequestWrapper extendedRequestDTO) throws Exception {
	responseWrapperDTO = new GetProfileResponseWrapper();
	requestWrapperDTO = extendedRequestDTO;
    }

    private void populateResponse(ManageNumber number, CustomerInfoDTO customerInfoDTO) {
	Customer customer = new Customer();
	CustomerDTOWrapper customerDTOWrapper = new CustomerDTOWrapper();

	customer.setMsisdn(number.getNumber());
	customer.setTitle(customerInfoDTO.getTitle());
	customer.setFirstName(customerInfoDTO.getFirstName());
	customer.setLastName(customerInfoDTO.getLastName());
	//customer.setImsi(number.getIMSI());

	Date dateOfBirth = customerInfoDTO.getDob();
	if (dateOfBirth != null) {
	    String dob = new SimpleDateFormat(DOB_DATE_FORMAT).format(dateOfBirth);
	    customer.setDob(dob);
	}

	customer.setIdentificationType(customerInfoDTO.getIdentificationType());
	customer.setIdentificationNumber(customerInfoDTO.getIdentificationNumber());
	customer.setAccountType(customerInfoDTO.getAccountType());
	customer.setOwnderType(customerInfoDTO.getOwnderType());
	customer.setStatus(customerInfoDTO.getOwnderType());
	customer.setAddress(customerInfoDTO.getAddress());
	customer.setAdditionalInfo(customerInfoDTO.getAdditionalInfo());
	customer.setResourceURL(CommonUtil.getResourceUrl(requestWrapperDTO));

	customerDTOWrapper.setCustomer(customer);

	responseWrapperDTO.setCustomerDTOWrapper(customerDTOWrapper);

    }

}