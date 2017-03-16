package com.wso2telco.services.dep.sandbox.servicefactory.credit;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.CreditDAO;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.LoggingDAO;
import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CallbackReference;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CallbackReferenceRequest;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CreditApplyRequestWrapper;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CreditApplyResponseBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CreditApplyResponseBean.CreditApplyResponse;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CreditRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CreditRequestBean.CreditApplyRequest;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Attributes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.credit.AttributeName;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.CreditStatusCodes;
import com.wso2telco.services.dep.sandbox.util.MessageLogHandler;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import com.wso2telco.services.dep.sandbox.util.TableName;

public class CreditApplyRequestHandler extends AbstractRequestHandler<CreditApplyRequestWrapper> {

	private NumberDAO numberDao;
	private LoggingDAO loggingDao;
	private CreditDAO creditDAO;
	private MessageLogHandler logHandler;
	private CreditApplyRequestWrapper requestWrapperDTO;
	private CreditApplyResponseWrapper responseWrapperDTO;
	private Integer correlatorid;
	Integer clientCorrelatorid ;
	
	final String TYPE_MONEY = "money";
	final String NUMBERS_TABLE = "numbers";
	final String CREDIT_REQUEST = "creditApplyRequest";
	final String MSISDN = "msisdn";
	final String TYPE = "type";
	final String AMOUNT = "amount";
	final String CLIENTCORRELATOR = "clientCorrelator";
	final String REASON = "reasonForCredit";
	final String ID = "merchantIdentification";
	final String RECEIPT = "receiptRequest";
	final String NOTIFYURL = "notifyURL";
	final String CALLBACKDATA = "callbackData";
	final String REFERENCECODE = "referenceCode";
	final String SERVERREFERENCECODE = "serverReferenceCode";
	

	{
		LOG = LogFactory.getLog(CreditApplyRequestHandler.class);
		numberDao = DaoFactory.getNumberDAO();
		creditDAO = DaoFactory.getCreditDAO();
		dao = DaoFactory.getGenaricDAO();
		loggingDao = DaoFactory.getLoggingDAO();
		logHandler = MessageLogHandler.getInstance();
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
	protected boolean validate(CreditApplyRequestWrapper wrapperDTO) throws Exception {

		CreditRequestBean requestBean = wrapperDTO.getCreditRequestBean();
		CreditApplyRequest request = requestBean.getCreditApplyRequest();
		if (requestBean != null && request != null) {
			CallbackReferenceRequest callRef = request.getReceiptRequest();

			if (callRef != null) {
				double amount = request.getAmount();
				String type = CommonUtil.getNullOrTrimmedValue(request.getType());
				String msisdn = CommonUtil.getNullOrTrimmedValue( wrapperDTO.getMsisdn());
				String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
				String reasonForCredit = CommonUtil.getNullOrTrimmedValue(request.getReasonForCredit());
				String merchantIdentification = CommonUtil.getNullOrTrimmedValue(request.getMerchantIdentification());
				String notifyURL = CommonUtil.getNullOrTrimmedValue(callRef.getNotifyURL());
				String callbackData = CommonUtil.getNullOrTrimmedValue(callRef.getCallbackData());
				String referenceCode = CommonUtil.getNullOrTrimmedValue(request.getReferenceCode());

				try {
					ValidationRule[] validationRules = {
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO, "amount",
									amount),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "type", type),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "msisdn",
									msisdn),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator",
									clientCorrelator),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "reasonForCredit",
									reasonForCredit),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "merchantIdentification",
									merchantIdentification),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_URL, "notifyURL", notifyURL),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData), 
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "referenceCode", referenceCode)};

					Validation.checkRequestParams(validationRules);
				} catch (CustomException ex) {
					LOG.error("###CREDIT### Error in Validation : " + ex);
					responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ex.getErrcode(),
							ex.getErrmsg(), wrapperDTO.getMsisdn()));
					responseWrapperDTO.setHttpStatus(javax.ws.rs.core.Response.Status.BAD_REQUEST);
				}
				return true;
			} else {
				responseWrapperDTO.setRequestError(
						constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE.getCode(),
								ServiceError.INVALID_INPUT_VALUE.getMessage(), wrapperDTO.getMsisdn()));
				responseWrapperDTO.setHttpStatus(javax.ws.rs.core.Response.Status.BAD_REQUEST);
			}
		}
		return false;
	}

	@Override
	protected Returnable process(CreditApplyRequestWrapper extendedRequestDTO) throws Exception {
		if (responseWrapperDTO.getRequestError() != null) {
			return responseWrapperDTO;
		}
		
		CreditRequestBean requestBean = extendedRequestDTO.getCreditRequestBean();
		CreditApplyRequest request = requestBean.getCreditApplyRequest();
		String  serviceCreditApply = ServiceName.ApplyCredit.toString();
		
		APITypes apiType = dao.getAPIType(RequestType.CREDIT.toString().toLowerCase());
		APIServiceCalls serviceType = dao.getServiceCall(apiType.getId(), ServiceName.ApplyCredit.toString());
		JSONObject obj = buildJSONObject(request);
		StringWriter out = new StringWriter();
		obj.writeJSONString(out);
    	String jsonString = out.toString();
    	
		MessageLog messageLog = new MessageLog();
		messageLog.setServicenameid(serviceType.getApiServiceCallId());
    	messageLog.setUserid(extendedRequestDTO.getUser().getId());
    	messageLog.setReference(MSISDN);
    	messageLog.setValue(extendedRequestDTO.getMsisdn());
    	messageLog.setRequest(jsonString);
    	messageLog.setMessageTimestamp(new Date());
		
		int ref_number = loggingDao.saveMessageLog(messageLog);
		String serverReferenceCode = String.format("%06d",ref_number );
		
		
		double amount = request.getAmount();
		String type = CommonUtil.getNullOrTrimmedValue(request.getType());
		String msisdn = getLastMobileNumber(extendedRequestDTO.getMsisdn());
		String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
		String reasonForCredit = CommonUtil.getNullOrTrimmedValue(request.getReasonForCredit());
		String merchantIdentification = CommonUtil.getNullOrTrimmedValue(request.getMerchantIdentification());
		String notifyURL = CommonUtil.getNullOrTrimmedValue(request.getReceiptRequest().getNotifyURL());
		String callbackData = CommonUtil.getNullOrTrimmedValue(request.getReceiptRequest().getCallbackData());
		String referenceCode = CommonUtil.getNullOrTrimmedValue(request.getReferenceCode());

		try {	
			String clientCorrelatorAttribute = AttributeName.clientCorrelator.toString();
			Integer userId = extendedRequestDTO.getUser().getId();
			 String userName = extendedRequestDTO.getUser().getUserName();
			if(clientCorrelator != null){
				 AttributeValues values	= creditDAO.checkDuplication(userId, serviceCreditApply, clientCorrelator, clientCorrelatorAttribute);
				 if(values != null){
				 Integer ownerId = values.getOwnerdid();
				 ManageNumber manageNumber = numberDao.getNumber(msisdn, userName);
				 if(ownerId == manageNumber.getId()){
					//send the already sent response
					AttributeValues applyCreditResponse = creditDAO.getTransactionValue(msisdn,values.getAttributeValueId() ,AttributeName.applyCredit.toString(), ServiceName.ApplyCredit.toString());
					CreditApplyResponseBean bean = new CreditApplyResponseBean();

					ObjectMapper mapper = new ObjectMapper();
					String responseString = applyCreditResponse.getValue();
					CreditApplyResponse res =  mapper.readValue(responseString, CreditApplyResponse.class);
					bean.setCreditApplyResponse(res);
					responseWrapperDTO.setCreditApplyResponseBean(bean);
					responseWrapperDTO.setHttpStatus(Response.Status.OK);
					return responseWrapperDTO;					
				 	}else{
						buildJsonResponseBody(amount, type, clientCorrelator, merchantIdentification, reasonForCredit,
								CreditStatusCodes.FAILED.toString(), callbackData, notifyURL, referenceCode, serverReferenceCode);
						responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
								ServiceError.INVALID_INPUT_VALUE, "Clientcorrelator is already used for different msisdn"));
						responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
						return responseWrapperDTO;
					}
				 }
			}		
			//check reference code duplication
			String referenceCodeAttribute =  AttributeName.referenceCodeCredit.toString();
			AttributeValues value =	creditDAO.checkDuplication(userId, serviceCreditApply, referenceCode, referenceCodeAttribute);
			if(value != null){
				buildJsonResponseBody(amount, type, clientCorrelator, merchantIdentification, reasonForCredit,
						CreditStatusCodes.FAILED.toString(), callbackData, notifyURL, referenceCode, serverReferenceCode);
				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "Already used reference code for the request"));
				responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapperDTO;	
			}else{
				saveReferenceCode(msisdn, referenceCode, userName);
			}

			ManageNumber manageNumber = numberDao.getNumber(msisdn, userName);

			if (type.equalsIgnoreCase(TYPE_MONEY)) {
				if (manageNumber != null) {
					updateBalance(manageNumber, amount);
					CreditApplyResponseBean responseBean = buildJsonResponseBody(amount, type, clientCorrelator, merchantIdentification, reasonForCredit,
							CreditStatusCodes.SUCCESS.toString(), callbackData, notifyURL,referenceCode, serverReferenceCode);			
					if(clientCorrelator != null){
					clientCorrelatorid = saveClientCorrelator(msisdn, clientCorrelator, userName);				
					saveTransaction(responseBean);
					}
					responseWrapperDTO.setHttpStatus(Response.Status.CREATED);
					return responseWrapperDTO;
					
				} else {
					buildJsonResponseBody(amount, type, clientCorrelator, merchantIdentification, reasonForCredit,
							CreditStatusCodes.FAILED.toString(), callbackData, notifyURL, referenceCode, serverReferenceCode);
					responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
							ServiceError.INVALID_INPUT_VALUE, "Number is not Registered for the Service"));
					responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
					return responseWrapperDTO;
				}
			} else {

				Attributes attributes = dao.getAttribute(type.toLowerCase());
				AttributeDistribution attributeDistribution = dao
						.getAttributeDistribution(serviceType.getApiServiceCallId(), attributes.getAttributeId());
				AttributeValues attributeValues = dao.getAttributeValue(attributeDistribution, manageNumber.getId());
				if (attributeValues != null) {
					updateValue(attributeValues, amount);
					CreditApplyResponseBean responseBean = buildJsonResponseBody(amount, type, clientCorrelator, merchantIdentification, reasonForCredit,
							CreditStatusCodes.SUCCESS.toString(), callbackData, notifyURL, referenceCode, serverReferenceCode);
					if(clientCorrelator != null){
					clientCorrelatorid = saveClientCorrelator(msisdn, clientCorrelator,userName);				
					saveTransaction(responseBean);
					}			
					responseWrapperDTO.setHttpStatus(Response.Status.CREATED);
					return responseWrapperDTO;
				} else {
					attributeValues = new AttributeValues();
					attributeValues.setAttributedid(attributeDistribution);
					attributeValues.setOwnerdid(manageNumber.getId());
					attributeValues.setTobject(NUMBERS_TABLE);
					attributeValues.setValue(Double.toString(amount));
					dao.saveAttributeValue(attributeValues);
					CreditApplyResponseBean responseBean = buildJsonResponseBody(amount, type, clientCorrelator, merchantIdentification, reasonForCredit,
							CreditStatusCodes.SUCCESS.toString(), callbackData, notifyURL, referenceCode, serverReferenceCode);
					if(clientCorrelator != null){
					clientCorrelatorid = saveClientCorrelator(msisdn, clientCorrelator,userName);				
					saveTransaction(responseBean);
					}
					responseWrapperDTO.setHttpStatus(Response.Status.CREATED);
					return responseWrapperDTO;
				}
			}

		} catch (Exception ex) {
			LOG.error("###CREDIT### Error in processing credit service request. ", ex);
			buildJsonResponseBody(amount, type, clientCorrelator, merchantIdentification, reasonForCredit,
					CreditStatusCodes.ERROR.toString(), callbackData, notifyURL, referenceCode, serverReferenceCode);
			responseWrapperDTO
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
			responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			return responseWrapperDTO;
		}
	}

	@Override
	protected void init(CreditApplyRequestWrapper extendedRequestDTO) throws Exception {
		requestWrapperDTO = extendedRequestDTO;
		responseWrapperDTO = new CreditApplyResponseWrapper();

	}

	private void updateBalance(ManageNumber manageNumber, double amount) throws Exception {

		double newBalance = manageNumber.getBalance() + amount;
		manageNumber.setBalance(newBalance);
		numberDao.saveManageNumbers(manageNumber);
	}

	private void updateValue(AttributeValues attributeValues, double amount) throws Exception {

		double newValue = Double.parseDouble(attributeValues.getValue()) + amount;
		attributeValues.setValue(Double.toString(newValue));
		dao.saveAttributeValue(attributeValues);
	}

	private CreditApplyResponseBean buildJsonResponseBody(double amount, String type, String clientCorrelator,
			String merchantIdentification, String reason, String status, String callbackData, String notifyURL, String referenceCode, String serverReferenceCode) {

		CallbackReference receiptResponse = new CallbackReference();
		receiptResponse.setCallbackData(callbackData);
		receiptResponse.setNotifyURL(notifyURL);
		receiptResponse.setResourceURL(CommonUtil.getResourceUrl(requestWrapperDTO));
		CreditApplyResponse creditApplyResponse = new CreditApplyResponse();
		creditApplyResponse.setAmount(amount);
		creditApplyResponse.setType(type);
		creditApplyResponse.setClientCorrelator(clientCorrelator);
		creditApplyResponse.setMerchantIdentification(merchantIdentification);
		creditApplyResponse.setReasonForCredit(reason);
		creditApplyResponse.setStatus(status);
		creditApplyResponse.setReceiptResponse(receiptResponse);
		creditApplyResponse.setReferenceCode(referenceCode);
		creditApplyResponse.setServerReferenceCode(serverReferenceCode);
		CreditApplyResponseBean creditApplyResponseBean = new CreditApplyResponseBean();
		creditApplyResponseBean.setCreditApplyResponse(creditApplyResponse);
		responseWrapperDTO.setCreditApplyResponseBean(creditApplyResponseBean);
		return creditApplyResponseBean;

	}
	
	@SuppressWarnings("unchecked")
	private JSONObject buildJSONObject(CreditApplyRequest request){
		
		JSONObject obj = new JSONObject();
		JSONObject creditRequest = new JSONObject();
		JSONObject receiptRequest = new JSONObject();
		
		receiptRequest.put(NOTIFYURL, request.getReceiptRequest().getNotifyURL());
		receiptRequest.put(CALLBACKDATA, request.getReceiptRequest().getCallbackData());
		
		creditRequest.put(TYPE, request.getType());
		creditRequest.put(AMOUNT, request.getAmount());
		creditRequest.put(CLIENTCORRELATOR, request.getClientCorrelator());
		creditRequest.put(REASON, request.getReasonForCredit());
		creditRequest.put(ID, request.getMerchantIdentification());
		creditRequest.put(RECEIPT, receiptRequest);
		creditRequest.put(REFERENCECODE, request.getReferenceCode());
		
		obj.put(CREDIT_REQUEST, creditRequest);
		return obj;
	}
	
	public void saveReferenceCode(String endUserId, String referenceCode, String userName) throws Exception {
		try {
			AttributeValues valueObj = new AttributeValues();
			String tableName = TableName.NUMBERS.toString().toLowerCase();
			String attributeName = AttributeName.referenceCodeCredit.toString();
			String apiType = RequestType.CREDIT.toString();
			String serviceCallApplyCredit = ServiceName.ApplyCredit.toString();
			APITypes api = dao.getAPIType(apiType);
			APIServiceCalls serviceCall = dao.getServiceCall(api.getId(), serviceCallApplyCredit);
			Attributes attribute = dao.getAttribute(attributeName);
			AttributeDistribution distribution = dao.getAttributeDistribution(serviceCall.getApiServiceCallId(), attribute.getAttributeId());
			ManageNumber manageNumber = numberDao.getNumber(endUserId, userName);
			Integer ownerId = manageNumber.getId();
			
			valueObj = new AttributeValues();
			valueObj.setAttributedid(distribution);
			valueObj.setOwnerdid(ownerId);
			valueObj.setTobject(tableName);
			valueObj.setValue(referenceCode);
			dao.saveAttributeValue(valueObj);

		} catch (Exception ex) {
			LOG.error("###CREDIT### Error in processing save of referenceCode request. ", ex);
			responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
		}
	}
	
	public Integer saveClientCorrelator(String endUserId, String correlator, String userName) throws Exception {
		try {
			AttributeValues valueObj = new AttributeValues();
			String tableName = TableName.NUMBERS.toString().toLowerCase();
			String attributeName = AttributeName.clientCorrelator.toString();
			String apiType = RequestType.CREDIT.toString();
			String serviceCallApplyCredit = ServiceName.ApplyCredit.toString();
			APITypes api = dao.getAPIType(apiType);
			APIServiceCalls serviceCall = dao.getServiceCall(api.getId(), serviceCallApplyCredit);
			Attributes attribute = dao.getAttribute(attributeName);
			AttributeDistribution distribution = dao.getAttributeDistribution(serviceCall.getApiServiceCallId(), attribute.getAttributeId());
			ManageNumber manageNumber = numberDao.getNumber(endUserId, userName);
			Integer ownerId = manageNumber.getId();
			//ownerId = creditDAO.getNumber(endUserId);

			valueObj = new AttributeValues();
			valueObj.setAttributedid(distribution);
			valueObj.setOwnerdid(ownerId);
			valueObj.setTobject(tableName);
			valueObj.setValue(correlator);
			correlatorid = creditDAO.saveAttributeValue(valueObj);

		} catch (Exception ex) {
			LOG.error("###CREDIT### Error in processing save of clientCorrelator request. ", ex);
			responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
		}
		return correlatorid;
	}
	
	public void saveTransaction(CreditApplyResponseBean responseBean)
			throws Exception {

		AttributeDistribution distributionId = null;
		Integer ownerId = null;
		try {
			AttributeValues valueObj = new AttributeValues();
			String tableName = TableName.SBXATTRIBUTEVALUE.toString().toLowerCase();
			String attributeName = AttributeName.applyCredit.toString();
			String apiType = RequestType.CREDIT.toString();
			String serviceCallApplyCredit = ServiceName.ApplyCredit.toString();
			APITypes api = dao.getAPIType(apiType);
			APIServiceCalls serviceCall = dao.getServiceCall(api.getId(), serviceCallApplyCredit);
			Attributes attribute = dao.getAttribute(attributeName);
			AttributeDistribution distribution = dao.getAttributeDistribution(serviceCall.getApiServiceCallId(), attribute.getAttributeId());
			ownerId = clientCorrelatorid;
			
			Gson gson = new Gson();
			JsonElement je =new JsonParser().parse(gson.toJson(responseBean));
			JsonObject asJsonObject = je.getAsJsonObject();
			JsonElement get = asJsonObject.get("creditApplyResponse");
			JsonObject asJsonObjectPayment = get.getAsJsonObject();
			String jsonString = null;
			jsonString = gson.toJson(asJsonObjectPayment);
			
			valueObj = new AttributeValues();
			valueObj.setAttributedid(distribution);
			valueObj.setOwnerdid(ownerId);
			valueObj.setTobject(tableName);
			valueObj.setValue(jsonString);
			dao.saveAttributeValue(valueObj);

		} catch (Exception ex) {
			LOG.error("###CREDIT### Error in processing save transaction. ", ex);
			responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
		}
	}
}
