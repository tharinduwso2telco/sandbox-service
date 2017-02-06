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
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CallbackReference;
import com.wso2telco.services.dep.sandbox.dao.model.custom.PatialRefundRequestWrapper;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundRequestBean.RefundRequest;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundResponseBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundResponseBean.RefundResponse;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.MessageLogHandler;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;

public class PatialRefundRequestHandler extends AbstractRequestHandler<PatialRefundRequestWrapper> {

	private NumberDAO numberDao;
	private MessageLogHandler logHandler;
	private PatialRefundRequestWrapper requestWrapperDTO;
	private PatialRefundResponseWrapper responseWrapperDTO;
	
	final String REFUND_REQUEST = "refundRequest";
	final String MSISDN = "msisdn";
	final String AMOUNT = "amount";
	final String CLIENTCORRELATOR = "clientCorrelator";
	final String REASON = "reasonForRefund";
	final String ID = "merchantIdentification";
	final String REFERENCE = "serverTransactionReference";
	final String RECEIPT = "receiptRequest";
	final String NOTIFYURL = "notifyURL";
	final String CALLBACKDATA = "callbackData";

	{
		LOG = LogFactory.getLog(PatialRefundRequestHandler.class);
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
	protected boolean validate(PatialRefundRequestWrapper wrapperDTO) throws Exception {

		RefundRequestBean requestBean = wrapperDTO.getRefundRequestBean();
		RefundRequest request = requestBean.getRefundRequest();

		if (requestBean != null && request != null) {

			CallbackReference callRef = request.getReceiptRequest();

			if (callRef != null) {
				double amount = request.getAmount();
				String msisdn = CommonUtil.getNullOrTrimmedValue("+" + wrapperDTO.getMsisdn());
				String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
				String reasonForRefund = CommonUtil.getNullOrTrimmedValue(request.getReasonForRefund());
				String merchantIdentification = CommonUtil.getNullOrTrimmedValue(request.getMerchantIdentification());
				String serverTransactionReference = CommonUtil
						.getNullOrTrimmedValue(request.getServerTransactionReference());
				String notifyURL = CommonUtil.getNullOrTrimmedValue(callRef.getNotifyURL());
				String callbackData = CommonUtil.getNullOrTrimmedValue(callRef.getCallbackData());

				try {
					ValidationRule[] validationRules = {
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO, "amount",
									amount),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "msisdn",
									msisdn),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator",
									clientCorrelator),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "reasonForRefund",
									reasonForRefund),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "merchantIdentification",
									merchantIdentification),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "serverTransactionReference",
									serverTransactionReference),
							new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL, "notifyURL", notifyURL),
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
	protected Returnable process(PatialRefundRequestWrapper extendedRequestDTO) throws Exception {
		if (responseWrapperDTO.getRequestError() != null) {
			return responseWrapperDTO;
		}

		RefundRequestBean requestBean = extendedRequestDTO.getRefundRequestBean();
		RefundRequest request = requestBean.getRefundRequest();
		
		APITypes apiType = dao.getAPIType(RequestType.CREDIT.toString().toLowerCase());
		APIServiceCalls serviceType = dao.getServiceCall(apiType.getId(), ServiceName.PatialRefund.toString());
		JSONObject obj = buildJSONObject(request);
		logHandler.saveMessageLog(serviceType.getApiServiceCallId(), extendedRequestDTO.getUser().getId(), MSISDN, extendedRequestDTO.getMsisdn(), obj);

		double amount = request.getAmount();
		String msisdn = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getMsisdn());
		String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
		String reasonForRefund = CommonUtil.getNullOrTrimmedValue(request.getReasonForRefund());
		String merchantIdentification = CommonUtil.getNullOrTrimmedValue(request.getMerchantIdentification());
		String serverTransactionReference = CommonUtil.getNullOrTrimmedValue(request.getServerTransactionReference());
		String notifyURL = CommonUtil.getNullOrTrimmedValue(request.getReceiptRequest().getNotifyURL());
		String callbackData = CommonUtil.getNullOrTrimmedValue(request.getReceiptRequest().getCallbackData());
		try {
			ManageNumber manageNumber = numberDao.getNumber(msisdn, extendedRequestDTO.getUser().getUserName());
			if (manageNumber != null) {
				updateBalance(manageNumber, amount);
				buildJsonResponseBody(amount, clientCorrelator, merchantIdentification, reasonForRefund,
						serverTransactionReference, callbackData, notifyURL);
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

	private void buildJsonResponseBody(double amount, String clientCorrelator, String merchantIdentification,
			String reason, String serverTransactionReference, String callbackData, String notifyURL) {

		CallbackReference receiptResponse = new CallbackReference();
		receiptResponse.setCallbackData(callbackData);
		receiptResponse.setNotifyURL(notifyURL);
		receiptResponse.setResourceURL(CommonUtil.getResourceUrl(requestWrapperDTO));
		RefundResponse refundResponse = new RefundResponse();
		refundResponse.setAmount(amount);
		refundResponse.setServerTransactionReference(serverTransactionReference);
		refundResponse.setClientCorrelator(clientCorrelator);
		refundResponse.setMerchantIdentification(merchantIdentification);
		refundResponse.setReasonForRefund(reason);
		refundResponse.setReceiptResponse(receiptResponse);
		RefundResponseBean refundResponseBean = new RefundResponseBean();
		refundResponseBean.setRefundResponse(refundResponse);
		responseWrapperDTO.setRefundResponseBean(refundResponseBean);

	}
	
	@SuppressWarnings("unchecked")
	private JSONObject buildJSONObject(RefundRequest request){
		
		JSONObject obj = new JSONObject();
		JSONObject refundRequest = new JSONObject();
		JSONObject receiptRequest = new JSONObject();
		
		receiptRequest.put(NOTIFYURL, request.getReceiptRequest().getNotifyURL());
		receiptRequest.put(CALLBACKDATA, request.getReceiptRequest().getCallbackData());
		
		refundRequest.put(AMOUNT, request.getAmount());
		refundRequest.put(CLIENTCORRELATOR, request.getClientCorrelator());
		refundRequest.put(REASON, request.getReasonForRefund());
		refundRequest.put(ID, request.getMerchantIdentification());
		refundRequest.put(REFERENCE, request.getServerTransactionReference());
		refundRequest.put(RECEIPT, receiptRequest);
		
		obj.put(REFUND_REQUEST, refundRequest);
		return obj;
	}

}
