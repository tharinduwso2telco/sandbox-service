package com.wso2telco.services.dep.sandbox.servicefactory.customerinfo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.CustomerInfoDAO;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ListCustomerInfoAttributesDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ListCustomerInfoDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.service.SandboxDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.Attribute;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.MessageLogHandler;
import com.wso2telco.services.dep.sandbox.util.ServiceName;

public class GetAttributeRequestHandler extends
	AbstractRequestHandler<GetAttributeRequestWrapper> {

    private CustomerInfoDAO customerInfoDao;
    private GetAttributeRequestWrapper requestWrapperDTO;
    private GetAttributeResponseWrapper responseWrapperDTO;
    private static String schemaValues = null;
    private MessageLogHandler logHandler;

    {
	LOG = LogFactory.getLog(GetAttributeRequestHandler.class);
	customerInfoDao = DaoFactory.getCustomerInfoDAO();
	dao = DaoFactory.getGenaricDAO();
	logHandler = MessageLogHandler.getInstance();
    }

    @Override
    protected Returnable getResponseDTO() {
	return responseWrapperDTO;
    }

    @Override
    protected List<String> getAddress() {
	List<String> addressList = new ArrayList<String>();
	String msisdn = CommonUtil.getNullOrTrimmedValue(requestWrapperDTO
		.getMsisdn());
	if (msisdn != null) {
	    addressList.add(msisdn);
	}

	return addressList;
    }

    @Override
    protected boolean validate(GetAttributeRequestWrapper wrapperDTO)
	    throws Exception {

	String msisdn = CommonUtil
		.getNullOrTrimmedValue(wrapperDTO.getMsisdn());
	String imsi = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getImsi());
	String mcc = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMcc());
	String mnc = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMnc());
	String schema = CommonUtil.getNullOrTrimmedValue((wrapperDTO
		.getSchema()).replace(",", ""));
	String onBehalfOf = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getOnBehalfOf());
	String purchaseCategoryCode = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getPurchaseCatergoryCode());
	String requestIdentifier = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getRequestIdentifier());
	

	List<ValidationRule> validationRulesList = new ArrayList<>();

	try {
	    if (msisdn == null && imsi == null) {
		responseWrapperDTO.setRequestError(constructRequestError(
			SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
			"MSISDN and IMSI are missing"));
		responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
	    }
	    if (schema != null) {
		validationRulesList.add(new ValidationRule(
			ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO,
			"schema", schema));
	    } else {
		responseWrapperDTO.setRequestError(constructRequestError(
			SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
			"No valid schema provided"));
		responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
	    }
	    if (msisdn != null) {
		validationRulesList.add(new ValidationRule(
			ValidationRule.VALIDATION_TYPE_OPTIONAL_TEL, "msisdn",
			msisdn));
	    }

	    if (imsi != null) {
		validationRulesList.add(new ValidationRule(
			ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO,
			"imsi", imsi));
	    }
	    if (mcc != null) {
		validationRulesList.add(new ValidationRule(
			ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO,
			"mcc", mcc));
		validationRulesList.add(new ValidationRule(
			ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO,
			"mnc", mnc));
	    } else {
		validationRulesList.add(new ValidationRule(
			ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO,
			"mnc", mnc));
	    }

	    validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf", onBehalfOf));
	    validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "purchaseCategoryCode",
				purchaseCategoryCode));

		if (requestIdentifier != null && checkRequestIdentifierSize(requestIdentifier)) {

			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "requestIdentifier",
					requestIdentifier));
		} else {
			responseWrapperDTO.setRequestError(constructRequestError(
				    SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1",
				    "requestIdentifier"));
			    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			
		} 
	    
	    ValidationRule[] validationRules = new ValidationRule[validationRulesList
		    .size()];
	    validationRules = validationRulesList.toArray(validationRules);

	    Validation.checkRequestParams(validationRules);
	} catch (CustomException ex) {
	    LOG.error("###CUSTOMERINFO### Error in validations", ex);
	    String errorMessage = "";
	    if (ex.getErrvar() != null && ex.getErrvar().length > 0) {
		errorMessage = ex.getErrvar()[0];
	    }
	    responseWrapperDTO.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(),
		    errorMessage));
	    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
	} catch (Exception ex) {
	    LOG.error("###CUSTOMERINFO### Error in validations", ex);
	    responseWrapperDTO
		    .setRequestError(constructRequestError(SERVICEEXCEPTION,
			    ServiceError.SERVICE_ERROR_OCCURED, null));
	}

	return true;

    }

    @Override
    protected Returnable process(GetAttributeRequestWrapper extendedRequestDTO)
	    throws Exception {

	if (responseWrapperDTO.getRequestError() != null) {
	    responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
	    return responseWrapperDTO;
	}
	
	APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
	APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), ServiceName.GetAttribute.toString().toLowerCase());
	JSONObject obj = new JSONObject();
	obj.put("msisdn",extendedRequestDTO.getMsisdn());
	obj.put("imsi",extendedRequestDTO.getImsi());
	obj.put("schema",extendedRequestDTO.getSchema());
	obj.put("mcc",extendedRequestDTO.getMcc());
	obj.put("mnc",extendedRequestDTO.getMnc());
	obj.put("userName",extendedRequestDTO.getUser().getUserName());
	logHandler.saveMessageLog(apiServiceCalls.getApiServiceCallId(), extendedRequestDTO.getUser().getId(), "msisdn", extendedRequestDTO.getMsisdn(), obj);
	
	String msisdn = null;
	ObjectMapper mapper = new ObjectMapper();
	JsonNode node = null;
	String number = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO
		.getMsisdn());
	String imsi = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO
		.getImsi());
	String[] schema = CommonUtil.getStringToArray(extendedRequestDTO
		.getSchema());
	String mnc = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO
		.getMnc());
	String mcc = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO
		.getMcc());
	Integer userid = extendedRequestDTO.getUser().getId();

	if (number != null) {
	    msisdn = CommonUtil.extractNumberFromMsisdn(number);
	}

	List<AttributeValues> customerInfoServices = null;

	// check request parameter schema has the matching values
	ListCustomerInfoAttributesDTO customerInfo = new ListCustomerInfoAttributesDTO();

	if (!customerInfoDao.checkSchema(schema)) {
	    responseWrapperDTO.setRequestError(constructRequestError(
		    SERVICEEXCEPTION,
		    ServiceError.INVALID_INPUT_VALUE,
		    "No valid schema provided "
			    + extendedRequestDTO.getSchema()));
	    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
	    return responseWrapperDTO;
	}
	// check msisdn,imsi,mcc&mnc has the matching values
	if ((dao.getMSISDN(msisdn, imsi, mcc, mnc) == null)) {
	    LOG.error("###CUSTOMERINFO### Valid MSISDN doesn't exists for the given inputssss");
	    responseWrapperDTO
		    .setRequestError(constructRequestError(SERVICEEXCEPTION,
			    ServiceError.INVALID_INPUT_VALUE,
			    "Valid MSISDN does not exist for the given input parameters"));
	    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
	    return responseWrapperDTO;
	}

	customerInfoServices = customerInfoDao.getAttributeServices(msisdn,
		userid, imsi, schema);

	for (AttributeValues values : customerInfoServices) {
	    schemaValues = ((values.getAttributedid()).getAttribute())
		    .getAttributeName().toString();
	    if ((Attribute.basic.toString()).equals(schemaValues)) {

		node = mapper.readValue(values.getValue(), JsonNode.class);
		customerInfo.setBasic(node);

	    } else if ((Attribute.billing.toString()).equals(schemaValues)) {
		node = mapper.readValue(values.getValue(), JsonNode.class);
		customerInfo.setBilling(node);
	    } else if (Attribute.account.toString().equals(schemaValues)) {
		node = mapper.readValue(values.getValue(), JsonNode.class);

		customerInfo.setAccount(node);
	    } else if (Attribute.identification.toString().equals(schemaValues)) {
		node = mapper.readValue(values.getValue(), JsonNode.class);

		customerInfo.setIdentification(node);
	    }
	}

	customerInfo.setMsisdn(msisdn);
	if (imsi != null) {
	    customerInfo.setImsi(imsi);
	}
	customerInfo.setResourceURL(CommonUtil
		.getResourceUrl(extendedRequestDTO));
	
	
	customerInfo.setOnBehalfOf(CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getOnBehalfOf()));
	customerInfo.setPurchaseCatergoryCode(CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getPurchaseCatergoryCode()));
	customerInfo.setRequestIdentifier( CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getRequestIdentifier()));
	customerInfo.setResponseIdentifier("RES" + RandomStringUtils.randomAlphabetic(8));
	
	
	ListCustomerInfoDTO customer = new ListCustomerInfoDTO();
	customer.setCustomer(customerInfo);
	responseWrapperDTO.setCustomer(customer);
	responseWrapperDTO.setHttpStatus(Response.Status.OK);
	return responseWrapperDTO;

    }

    @Override
    protected void init(GetAttributeRequestWrapper extendedRequestDTO)
	    throws Exception {
	requestWrapperDTO = extendedRequestDTO;
	responseWrapperDTO = new GetAttributeResponseWrapper();
    }
    
    private boolean checkRequestIdentifierSize(String requestIdentifier) {

		int size = SandboxDTO.getRequestIdentifierSize();

		if (requestIdentifier.length() >= size) {

			return true;
		} else {

			return false;
		}
	}
}
