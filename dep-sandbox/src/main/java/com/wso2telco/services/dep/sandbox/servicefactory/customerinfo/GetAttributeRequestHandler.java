package com.wso2telco.services.dep.sandbox.servicefactory.customerinfo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wso2telco.services.dep.sandbox.dao.model.custom.Customer;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CustomerInfoDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.*;
import com.wso2telco.services.dep.sandbox.servicefactory.MessageProcessStatus;
import com.wso2telco.services.dep.sandbox.servicefactory.MessageType;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.LogFactory;


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
import com.wso2telco.services.dep.sandbox.service.SandboxDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.Attribute;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.MessageLogHandler;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.simple.parser.JSONParser;

public class GetAttributeRequestHandler extends
	AbstractRequestHandler<GetAttributeRequestWrapper> {

    private CustomerInfoDAO customerInfoDao;
    private GetAttributeRequestWrapper requestWrapperDTO;
    private GetAttributeResponseWrapper responseWrapperDTO;
    private static String schemaValues = null;
    private MessageLogHandler logHandler;
	private  String requestIdentifierCode ;


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
	String schema = CommonUtil.getNullOrTrimmedValue((wrapperDTO.getSchema()));
	String onBehalfOf = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getOnBehalfOf());
	String purchaseCategoryCode = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getPurchaseCategoryCode());
	String requestIdentifier = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getRequestIdentifier());
	APITypes apiTypes = dao.getAPIType(wrapperDTO.getRequestType().toString().toLowerCase());
	APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), ServiceName.GetAttribute.toString().toLowerCase());


		List<ValidationRule> validationRulesList = new ArrayList<>();

	try {
	    if (msisdn == null && imsi == null) {
			
			validationRulesList.add(new ValidationRule(
					ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO,
					"mcc", mcc));
			
			validationRulesList.add(new ValidationRule(
					ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO,
					"mnc", mnc));
	    	
		}
	   
	    validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "schema", schema));
	  
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
		} else if (mnc != null) {
			validationRulesList.add(new ValidationRule(
					ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO,
					"mnc", mnc));
			validationRulesList.add(new ValidationRule(
					ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO,
					"mcc", mcc));
		}
    

	    validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf", onBehalfOf));
	    validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "purchaseCategoryCode",
				purchaseCategoryCode));
	    validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "requestIdentifier", requestIdentifier));

		String duplicateRequestId = checkDuplicateRequestCode(wrapperDTO.getUser().getId(),apiServiceCalls.getApiServiceCallId(),msisdn,MessageProcessStatus.Success, MessageType.Response,requestIdentifier);

		if (requestIdentifier != null && checkRequestIdentifierSize(requestIdentifier)) {

			validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "requestIdentifier",
					requestIdentifier));

			if((duplicateRequestId != null))
			{
				LOG.error("###CUSTOMERINFO### Already used requestIdentifier code is entered");
				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "An already used requestIdentifier code is entered"));
				responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);

			}


		} else {
			responseWrapperDTO.setRequestError(constructRequestError(
				    SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1",
				    "requestIdentifier"));			
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
	
	String msisdn = null;
	ObjectMapper mapper = new ObjectMapper();
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
		    "No valid schema available as "
			    + extendedRequestDTO.getSchema()));
	    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
	    return responseWrapperDTO;
	}
	// check msisdn,imsi,mcc&mnc has the matching values

	if ((dao.getMSISDN(msisdn, imsi, mcc, mnc,extendedRequestDTO.getUser().getUserName() ) == null)) {

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
	
	
	if (customerInfoServices.isEmpty()) {
	    LOG.error("###CUSTOMERINFO### Customer information does not available");
	    responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    " No Valid Customer schema information configured for the given input parameters"));
	    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
	    return responseWrapperDTO;
	}
	
	 boolean isNullObject = true;

	for (AttributeValues values : customerInfoServices) {
		
	    schemaValues = ((values.getAttributedid()).getAttribute())
		    .getAttributeName().toString();
	    if (values.getValue()!=null && (Attribute.basic.toString()).equals(schemaValues)) {
	    	isNullObject = false;
			customerInfo.setBasic(mapper.readValue(values.getValue().toString(), JsonNode.class));

	    } else if (values.getValue()!=null && (Attribute.billing.toString()).equals(schemaValues)) {
	    	isNullObject = false;
		customerInfo.setBilling(mapper.readValue(values.getValue(), JsonNode.class));
	    } else if (values.getValue()!=null && Attribute.account.toString().equals(schemaValues)) {
	    	isNullObject = false;
		customerInfo.setAccount(mapper.readValue(values.getValue(), JsonNode.class));
	    } else if (values.getValue()!=null &&  Attribute.identification.toString().equals(schemaValues)) {
	    	isNullObject = false;
		customerInfo.setIdentification(mapper.readValue(values.getValue(), JsonNode.class));
	    }
	}
	if(isNullObject){
		 LOG.error("###CUSTOMERINFO### Customer information does not available");
		    responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
			    " No Valid Customer schema information configured for the given input parameters"));
		    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
		    return responseWrapperDTO;
	}
	customerInfo.setMsisdn(msisdn);
	if (imsi != null) {
	    customerInfo.setImsi(imsi);
	}
	customerInfo.setResourceURL(CommonUtil
		.getResourceUrl(extendedRequestDTO));
	
	
	customerInfo.setOnBehalfOf(CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getOnBehalfOf()));
	customerInfo.setPurchaseCategoryCode(CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getPurchaseCategoryCode()));
	customerInfo.setRequestIdentifier( CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getRequestIdentifier()));
	customerInfo.setResponseIdentifier("RES" + RandomStringUtils.randomAlphabetic(8));
	ListCustomerInfoDTO customer = new ListCustomerInfoDTO();
	customer.setCustomer(customerInfo);
	responseWrapperDTO.setListCustomerInfoDTO(customer);
	responseWrapperDTO.setHttpStatus(Response.Status.OK);
	saveResponse(extendedRequestDTO.getMsisdn(),customer,apiServiceCalls,MessageProcessStatus.Success);
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

	private String checkDuplicateRequestCode(int userId, int serviceNameId, String telNumber, MessageProcessStatus status, MessageType type, String requestIdentityCode)
	{
		List<Integer> serviceNameIdList = new ArrayList();
		serviceNameIdList.add(serviceNameId);
		String requestCode = null;
		try {
			List<MessageLog> messageLogs = loggingDAO.getMessageLogs(userId,serviceNameIdList,"msisdn",telNumber,null,null);
			for(int i=0;i<messageLogs.size();i++)
			{
				if(messageLogs!=null)
				{
					int logStatus =  messageLogs.get(i).getStatus();
					int logType = messageLogs.get(i).getType();
					if(logStatus == status.getValue() && logType == type.getValue())
					{
						String logRequest = messageLogs.get(i).getRequest();


						JSONObject jsonObject = new JSONObject(logRequest);
						org.json.JSONObject childJsonObject = jsonObject.getJSONObject("customer");

						requestIdentifierCode = childJsonObject.getString("requestIdentifier");
						int logUserId = messageLogs.get(i).getUserid();
						String logTelNumber = messageLogs.get(i).getValue();

						if(logUserId == userId && logTelNumber.equals(telNumber) && requestIdentifierCode.equals(requestIdentityCode))
						{
							requestCode = requestIdentifierCode;
							break;
						}

					}
				}
			}

		} catch (Exception e) {
			LOG.error("an Error occurred while retrieving messagelog table values "+e);
		}
		return requestCode;
	}



	private void saveResponse(String endUserId, ListCustomerInfoDTO responseWrapperDTO, APIServiceCalls serviceCalls, MessageProcessStatus status) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonRequestString = null;
		try {
			jsonRequestString = mapper.writeValueAsString(responseWrapperDTO);
		} catch (JsonProcessingException e) {
			LOG.error("an error occurred while converting JsonNode to string"+e);
		}
		MessageLog messageLog = new MessageLog();
		messageLog.setRequest(jsonRequestString);
		messageLog.setUserid(user.getId());
		messageLog.setStatus(status.getValue());
		messageLog.setType(MessageType.Response.getValue());
		messageLog.setReference("msisdn");
		messageLog.setValue(endUserId);
		messageLog.setServicenameid(serviceCalls.getApiServiceCallId());
		try {
			loggingDAO.saveMessageLog(messageLog);
		} catch (Exception e) {
			LOG.error("An error occured while saving the response"+e);
		}

	}
}
