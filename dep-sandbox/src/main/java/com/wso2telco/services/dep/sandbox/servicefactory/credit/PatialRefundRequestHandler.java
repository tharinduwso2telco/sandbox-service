/*******************************************************************************
 * Copyright (c) 2015-2017, WSO2.Telco Inc. (http://www.wso2telco.com)
 *
 * All Rights Reserved. WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.services.dep.sandbox.servicefactory.credit;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wso2telco.services.dep.sandbox.dao.CreditDAO;
import com.wso2telco.services.dep.sandbox.dao.LoggingDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.*;
import com.wso2telco.services.dep.sandbox.dao.model.domain.*;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.*;
import com.wso2telco.services.dep.sandbox.util.*;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundRequestBean.RefundRequest;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundResponseBean.RefundResponse;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;

public class PatialRefundRequestHandler extends AbstractRequestHandler<PatialRefundRequestWrapper> {

	private NumberDAO numberDao;
	private CreditDAO creditDAO;
	private MessageLogHandler logHandler;
	private PatialRefundRequestWrapper requestWrapperDTO;
	private PatialRefundResponseWrapper responseWrapperDTO;
	private Integer correlatorid;
    private LoggingDAO loggingDao;
	final String REFUND_AMOUNT = "refundAmount";
	final String MSISDN = "msisdn";
	final String CLIENTCORRELATOR = "clientCorrelator";
	final String REASON = "reasonForRefund";
	final String REFERENCE = "originalServerReferenceCode";
	final String REFERENCE_CODE = "referenceCode";
	final String PAYMENT_AMOUNT = "paymentAmount";
	final String CHANGING_INFO = "chargingInformation";
	final String CHARGING_META_DATA = "chargingMetaData";
	final String REFUND_REQUEST = "refundRequest";

	{
		LOG = LogFactory.getLog(PatialRefundRequestHandler.class);
        loggingDao = DaoFactory.getLoggingDAO();
		numberDao = DaoFactory.getNumberDAO();
		creditDAO = DaoFactory.getCreditDAO();
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
	protected boolean validate(PatialRefundRequestWrapper wrapperDTO) throws Exception {

		RefundRequestBean requestBean = wrapperDTO.getRefundRequestBean();
		RefundRequest request = requestBean.getRefundRequest();
		PaymentAmountWithTax paymentAmountWithTax = request.getPaymentAmount();
		ChargingInformation chargingInformation = paymentAmountWithTax.getChargingInformation();
		ChargingMetaDataWithTax metadata = paymentAmountWithTax.getChargingMetaData();

		if (requestBean != null && request != null) {

				double amount = request.getRefundAmount();
				String msisdn = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMsisdn());
				String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
				String reasonForRefund = CommonUtil.getNullOrTrimmedValue(request.getReasonForRefund());
				String originalServerReferenceCode =  CommonUtil.getNullOrTrimmedValue(request.getOriginalServerReferenceCode());

				String serverTransactionReference = CommonUtil
						.getNullOrTrimmedValue(request.getOriginalServerReferenceCode());
				String onBehalfOf = CommonUtil.getNullOrTrimmedValue(metadata.getOnBehalfOf());
				String categoryCode = CommonUtil.getNullOrTrimmedValue(metadata.getPurchaseCategoryCode());
				String channel = CommonUtil.getNullOrTrimmedValue(metadata.getChannel());
				String taxAmount = CommonUtil.getNullOrTrimmedValue(metadata.getTax());
				String referenceCode = CommonUtil.getNullOrTrimmedValue(request.getReferenceCode());


				try {
					ValidationRule[] validationRules = {
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO, "refundAmount",
									amount),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "msisdn",
									msisdn),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator",
									clientCorrelator),
                            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "originalServerReferenceCode",
                                    originalServerReferenceCode),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "reasonForRefund",
									reasonForRefund),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "serverTransactionReference",
									serverTransactionReference),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf", onBehalfOf),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "categoryCode", categoryCode),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "channel", channel),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,"taxAmount",taxAmount),
                            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,"referenceCode",referenceCode)

					};

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
		return false;
	}

	@Override
	protected Returnable process(PatialRefundRequestWrapper extendedRequestDTO) throws Exception {

		if (responseWrapperDTO.getRequestError() != null) {
			responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			return responseWrapperDTO;
		}

		RefundRequestBean requestBean = extendedRequestDTO.getRefundRequestBean();
		RefundRequest request = requestBean.getRefundRequest();
		PaymentAmountWithTax paymentAmountWithTax = request.getPaymentAmount();
		ChargingInformation chargingInformation = paymentAmountWithTax.getChargingInformation();
		ChargingMetaDataWithTax metadata = paymentAmountWithTax.getChargingMetaData();
		APITypes apiType = dao.getAPIType(RequestType.CREDIT.toString().toLowerCase());
		APIServiceCalls serviceType = dao.getServiceCall(apiType.getId(), ServiceName.PartialRefund.toString());
		JSONObject obj = buildJSONObject(request);
		logHandler.saveMessageLog(serviceType.getApiServiceCallId(), extendedRequestDTO.getUser().getId(), MSISDN, extendedRequestDTO.getMsisdn(), obj);


		double amount = request.getRefundAmount();
		String msisdn = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getMsisdn());
		String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
		String reasonForRefund = CommonUtil.getNullOrTrimmedValue(request.getReasonForRefund());
		String merchantIdentification = CommonUtil.getNullOrTrimmedValue(request.getMsisdn());
		String serverTransactionReference = CommonUtil.getNullOrTrimmedValue(request.getOriginalServerReferenceCode());
		String userName = extendedRequestDTO.getUser().getUserName();
		String  serviceCreditApply = ServiceName.PartialRefund.toString();
		String channel = CommonUtil.getNullOrTrimmedValue(metadata.getChannel());
		String referenceCode = CommonUtil.getNullOrTrimmedValue(String.valueOf(request.getReferenceCode()));
		String endUserID = getLastMobileNumber(extendedRequestDTO.getMsisdn());

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

        //Genarate server reference number.
        int ref_number = loggingDao.saveMessageLog(messageLog);
        String serverReferenceCode = String.format("%06d",ref_number );

		try {

			String clientCorrelatorAttribute = AttributeName.clientCorrelator.toString();
			Integer userId = extendedRequestDTO.getUser().getId();
			//Null check for client correlator.
			if(clientCorrelator != null){
				AttributeValues duplicateClientCorrelator	= creditDAO.checkDuplication(userId, serviceCreditApply, clientCorrelator, clientCorrelatorAttribute);

				if(duplicateClientCorrelator != null){
					Integer ownerId = duplicateClientCorrelator.getOwnerdid();
					ManageNumber manageNumber = numberDao.getNumber(endUserID, userName);
					if(ownerId == manageNumber.getId()){
						//send the already sent response
						AttributeValues partialRefundResponse = creditDAO.getTransactionValue(endUserID,duplicateClientCorrelator.getAttributeValueId() ,AttributeName.patialRefund.toString(),ServiceName.PartialRefund.toString());
						RefundResponseBean bean = new RefundResponseBean();

						ObjectMapper mapper = new ObjectMapper();
						String responseString = partialRefundResponse.getValue();
						RefundResponse res =  mapper.readValue(responseString, RefundResponse.class);
						bean.setRefundResponse(res);
						responseWrapperDTO.setRefundResponseBean(bean);
						responseWrapperDTO.setHttpStatus(Response.Status.OK);
						return responseWrapperDTO;
					}else{
						buildJsonResponseBody(amount, clientCorrelator, merchantIdentification, reasonForRefund,
								serverTransactionReference, OperationStatus.Refunded.toString(),referenceCode,serverReferenceCode,chargingInformation,metadata );
						responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
								ServiceError.INVALID_INPUT_VALUE, "Clientcorrelator is already used for different msisdn"));
						responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
						return responseWrapperDTO;
					}
				}
			}

			//Check channel.
			if (channel != null && !containsChannel(channel)) {
				LOG.error("###WALLET### Valid channel doesn't exists for the given inputs");
				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "Valid channel doesn't exists for the given inputs"));
				responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapperDTO;
			}

			//Save reference code.
            String referenceCodeAttribute =  AttributeName.referenceCodeCredit.toString();
            AttributeValues value =	creditDAO.checkDuplication(userId, serviceCreditApply, referenceCode, referenceCodeAttribute);
            if(value != null){
                buildJsonResponseBody(amount, clientCorrelator, merchantIdentification, reasonForRefund,
                        serverTransactionReference, OperationStatus.Refunded.toString(),referenceCode,serverReferenceCode,chargingInformation,metadata );
                responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
                        ServiceError.INVALID_INPUT_VALUE, "Already used reference code for the request"));
                responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
                return responseWrapperDTO;
            }else{
                saveReferenceCode(endUserID, referenceCode, userName);
            }

			ManageNumber manageNumber = numberDao.getNumber(endUserID, userName);
			if (manageNumber != null) {
				updateBalance(manageNumber, amount);
				RefundResponseBean  responseBean = buildJsonResponseBody(amount, clientCorrelator, merchantIdentification, reasonForRefund,
						serverTransactionReference, OperationStatus.Refunded.toString(),referenceCode,serverReferenceCode,chargingInformation,metadata );
				if(clientCorrelator != null){
					correlatorid = saveClientCorrelator(endUserID, clientCorrelator, userName);
					saveTransaction(responseBean);
				}

				responseWrapperDTO.setHttpStatus(Response.Status.OK);
				return responseWrapperDTO;
			} else {
				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
						ServiceError.INVALID_INPUT_VALUE, "Number is not Registered for the Service"));
				responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
				return responseWrapperDTO;
			}

		} catch (Exception ex) {
			LOG.error("###CREDIT### Error in processing credit service request. ", ex);
			responseWrapperDTO
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
			responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			return responseWrapperDTO;
		}

	}
	@Override
	protected void init(PatialRefundRequestWrapper extendedRequestDTO) throws Exception {
		requestWrapperDTO = extendedRequestDTO;
		responseWrapperDTO = new PatialRefundResponseWrapper();
	}

	private void updateBalance(ManageNumber manageNumber, double amount) throws Exception {

		double newBalance = manageNumber.getBalance() + amount;
		manageNumber.setBalance(newBalance);
		numberDao.saveManageNumbers(manageNumber);
	}

	private RefundResponseBean buildJsonResponseBody(double amount, String clientCorrelator, String merchantIdentification,
			String reason, String serverTransactionReference,String operationStatus, String referenceCode, String serverReferenceCode, ChargingInformation chargingInformation, ChargingMetaDataWithTax chargingMetaDataWithTax) {

		PaymentAmountWithTax paymentAmountWithTax = new PaymentAmountWithTax();
		paymentAmountWithTax.setChargingInformation(chargingInformation);
		paymentAmountWithTax.setChargingMetaData(chargingMetaDataWithTax);

		RefundResponse refundResponse = new RefundResponse();
		refundResponse.setRefundAmount(amount);
		refundResponse.setOriginalServerReferenceCode(serverTransactionReference);
		refundResponse.setClientCorrelator(clientCorrelator);
		refundResponse.setEndUserID(merchantIdentification);
		refundResponse.setReasonForRefund(reason);
		refundResponse.setPaymentAmount(paymentAmountWithTax);
		refundResponse.setReferenceCode(referenceCode);
		refundResponse.setServerReferanceCode(serverReferenceCode);
		refundResponse.setResourceURL(CommonUtil.getResourceUrl(requestWrapperDTO));
		refundResponse.setTransactionOperationStatus(operationStatus);
		RefundResponseBean refundResponseBean = new RefundResponseBean();
		refundResponseBean.setRefundResponse(refundResponse);
		responseWrapperDTO.setRefundResponseBean(refundResponseBean);
		return refundResponseBean;

	}
	
	@SuppressWarnings("unchecked")
	private JSONObject buildJSONObject(RefundRequest request){
		
		JSONObject obj = new JSONObject();
		JSONObject refundRequest = new JSONObject();
		JSONObject payment = new JSONObject();

		payment.put(CHANGING_INFO, request.getPaymentAmount().getChargingInformation());
		payment.put(CHARGING_META_DATA, request.getPaymentAmount().getChargingMetaData());

		refundRequest.put(CLIENTCORRELATOR, request.getClientCorrelator());
		refundRequest.put(MSISDN, request.getMsisdn());
		refundRequest.put(REFERENCE,request.getOriginalServerReferenceCode());
		refundRequest.put(REASON, request.getReasonForRefund());
		refundRequest.put(REFUND_AMOUNT, request.getRefundAmount());
		refundRequest.put(PAYMENT_AMOUNT, payment);
		refundRequest.put(REFERENCE_CODE,request.getReferenceCode());
		
		obj.put(REFUND_REQUEST, refundRequest);
		return obj;
	}


	public Integer saveClientCorrelator(String endUserId, String correlator, String userName) throws Exception {
		try {
			AttributeValues valueObj = new AttributeValues();
			String tableName = TableName.NUMBERS.toString().toLowerCase();
			String attributeName = AttributeName.clientCorrelator.toString();
			String apiType = RequestType.CREDIT.toString();
			String serviceCallApplyCredit = ServiceName.PartialRefund.toString();
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
			valueObj.setValue(correlator);
			correlatorid = creditDAO.saveAttributeValue(valueObj);

		} catch (Exception ex) {
			LOG.error("###PATIAL_REFUND_CREDIT### Error in processing save of clientCorrelator request. ", ex);
			responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
			throw ex;
		}
		return correlatorid;
	}


	public boolean containsChannel(String channelValue) {

		for (Channel channel : Channel.values()) {
			if (channel.name().toLowerCase().equals(channelValue.toLowerCase())) {
				return true;
			}
		}

		return false;
	}



	public void saveTransaction(RefundResponseBean responseBean)
			throws Exception {

		AttributeDistribution distributionId = null;
		Integer ownerId = null;
		try {
			AttributeValues valueObj = new AttributeValues();
			String tableName = TableName.SBXATTRIBUTEVALUE.toString().toLowerCase();
			String attributeName = AttributeName.patialRefund.toString();
			String apiType = RequestType.CREDIT.toString();
			String serviceCallApplyCredit = ServiceName.PartialRefund.toString();
			APITypes api = dao.getAPIType(apiType);
			APIServiceCalls serviceCall = dao.getServiceCall(api.getId(), serviceCallApplyCredit);
			Attributes attribute = dao.getAttribute(attributeName);
			AttributeDistribution distribution = dao.getAttributeDistribution(serviceCall.getApiServiceCallId(), attribute.getAttributeId());
			ownerId = correlatorid;

			Gson gson = new Gson();
			JsonElement je =new JsonParser().parse(gson.toJson(responseBean));
			JsonObject asJsonObject = je.getAsJsonObject();
			JsonElement get = asJsonObject.get("refundResponse");
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
			LOG.error("###PARTIAL_REFUND### Error in processing save transaction. ", ex);
			responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
			throw ex;
		}
	}



	public void saveReferenceCode(String endUserId, String referenceCode, String userName) throws Exception {
		try {
			AttributeValues valueObj = new AttributeValues();
			String tableName = TableName.NUMBERS.toString().toLowerCase();
			String attributeName = AttributeName.referenceCodeCredit.toString();
			String apiType = RequestType.CREDIT.toString();
			String serviceCallApplyCredit = ServiceName.PartialRefund.toString();
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
			throw ex;
		}
	}


}
