package com.wso2telco.services.dep.sandbox.servicefactory.credit;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.CreditDAO;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CallbackReference;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CreditApplyRequestWrapper;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CreditApplyResponseBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CreditApplyResponseBean.ServiceCreditResponse;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CreditRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CreditRequestBean.CreditApplyRequest;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Attributes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.CreditStatusCodes;
import com.wso2telco.services.dep.sandbox.util.MessageLogHandler;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;

public class CreditApplyRequestHandler extends AbstractRequestHandler<CreditApplyRequestWrapper> {

	private CreditDAO creditDao;
	private NumberDAO numberDao;
	private MessageLogHandler logHandler;
	private CreditApplyRequestWrapper requestWrapperDTO;
	private CreditApplyResponseWrapper responseWrapperDTO;

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

	{
		LOG = LogFactory.getLog(CreditApplyRequestHandler.class);
		creditDao = DaoFactory.getCreditDAO();
		numberDao = DaoFactory.getNumberDAO();
		dao = DaoFactory.getGenaricDAO();
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
			CallbackReference callRef = request.getReceiptRequest();

			if (callRef != null) {
				double amount = request.getAmount();
				String type = CommonUtil.getNullOrTrimmedValue(request.getType());
				String msisdn = CommonUtil.getNullOrTrimmedValue("+" + wrapperDTO.getMsisdn());
				String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
				String reasonForCredit = CommonUtil.getNullOrTrimmedValue(request.getReasonForCredit());
				String merchantIdentification = CommonUtil.getNullOrTrimmedValue(request.getMerchantIdentification());
				String notifyURL = CommonUtil.getNullOrTrimmedValue(callRef.getNotifyURL());
				String callbackData = CommonUtil.getNullOrTrimmedValue(callRef.getCallbackData());

				try {
					ValidationRule[] validationRules = {
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO, "amount",
									amount),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "type", type),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "msisdn",
									msisdn),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "clientCorrelator",
									clientCorrelator),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "reasonForCredit",
									reasonForCredit),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "merchantIdentification",
									merchantIdentification),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_URL, "notifyURL", notifyURL),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData) };

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
		
		APITypes apiType = dao.getAPIType(RequestType.CREDIT.toString().toLowerCase());
		APIServiceCalls serviceType = dao.getServiceCall(apiType.getId(), ServiceName.ApplyCredit.toString());
		JSONObject obj = buildJSONObject(request);
		logHandler.saveMessageLog(serviceType.getApiServiceCallId(), extendedRequestDTO.getUser().getId(), MSISDN, extendedRequestDTO.getMsisdn(), obj);

		double amount = request.getAmount();
		String type = CommonUtil.getNullOrTrimmedValue(request.getType());
		String msisdn = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getMsisdn());
		String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
		String reasonForCredit = CommonUtil.getNullOrTrimmedValue(request.getReasonForCredit());
		String merchantIdentification = CommonUtil.getNullOrTrimmedValue(request.getMerchantIdentification());
		String notifyURL = CommonUtil.getNullOrTrimmedValue(request.getReceiptRequest().getNotifyURL());
		String callbackData = CommonUtil.getNullOrTrimmedValue(request.getReceiptRequest().getCallbackData());

		try {

			ManageNumber manageNumber = numberDao.getNumber(msisdn, extendedRequestDTO.getUser().getUserName());

			if (type.equalsIgnoreCase(TYPE_MONEY)) {
				if (manageNumber != null) {
					updateBalance(manageNumber, amount);
					buildJsonResponseBody(amount, type, clientCorrelator, merchantIdentification, reasonForCredit,
							CreditStatusCodes.SUCCESS.toString(), callbackData, notifyURL);
					responseWrapperDTO.setHttpStatus(Response.Status.OK);
					return responseWrapperDTO;
				} else {
					buildJsonResponseBody(amount, type, clientCorrelator, merchantIdentification, reasonForCredit,
							CreditStatusCodes.FAILED.toString(), callbackData, notifyURL);
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
					buildJsonResponseBody(amount, type, clientCorrelator, merchantIdentification, reasonForCredit,
							CreditStatusCodes.SUCCESS.toString(), callbackData, notifyURL);
					responseWrapperDTO.setHttpStatus(Response.Status.OK);
					return responseWrapperDTO;
				} else {
					attributeValues = new AttributeValues();
					attributeValues.setAttributedid(attributeDistribution);
					attributeValues.setOwnerdid(manageNumber.getId());
					attributeValues.setTobject(NUMBERS_TABLE);
					attributeValues.setValue(Double.toString(amount));
					dao.saveAttributeValue(attributeValues);
					buildJsonResponseBody(amount, type, clientCorrelator, merchantIdentification, reasonForCredit,
							CreditStatusCodes.SUCCESS.toString(), callbackData, notifyURL);
					responseWrapperDTO.setHttpStatus(Response.Status.OK);
					return responseWrapperDTO;
				}
			}

		} catch (Exception ex) {
			LOG.error("###CREDIT### Error in processing credit service request. ", ex);
			buildJsonResponseBody(amount, type, clientCorrelator, merchantIdentification, reasonForCredit,
					CreditStatusCodes.ERROR.toString(), callbackData, notifyURL);
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
		creditDao.updateNumberBalance(manageNumber);
	}

	private void updateValue(AttributeValues attributeValues, double amount) throws Exception {

		double newValue = Double.parseDouble(attributeValues.getValue()) + amount;
		attributeValues.setValue(Double.toString(newValue));
		dao.saveAttributeValue(attributeValues);
	}

	private void buildJsonResponseBody(double amount, String type, String clientCorrelator,
			String merchantIdentification, String reason, String status, String callbackData, String notifyURL) {

		CallbackReference receiptResponse = new CallbackReference();
		receiptResponse.setCallbackData(callbackData);
		receiptResponse.setNotifyURL(notifyURL);
		receiptResponse.setResourceURL(CommonUtil.getResourceUrl(requestWrapperDTO));
		ServiceCreditResponse serviceCreditResponse = new ServiceCreditResponse();
		serviceCreditResponse.setAmount(amount);
		serviceCreditResponse.setType(type);
		serviceCreditResponse.setClientCorrelator(clientCorrelator);
		serviceCreditResponse.setMerchantIdentification(merchantIdentification);
		serviceCreditResponse.setReasonForCredit(reason);
		serviceCreditResponse.setStatus(status);
		serviceCreditResponse.setReceiptResponse(receiptResponse);
		CreditApplyResponseBean creditApplyResponseBean = new CreditApplyResponseBean();
		creditApplyResponseBean.setServiceCreditResponse(serviceCreditResponse);
		responseWrapperDTO.setCreditApplyResponseBean(creditApplyResponseBean);

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
		
		obj.put(CREDIT_REQUEST, creditRequest);
		return obj;
	}

}
