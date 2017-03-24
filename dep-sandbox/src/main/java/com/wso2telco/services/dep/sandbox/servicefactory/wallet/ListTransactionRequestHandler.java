package com.wso2telco.services.dep.sandbox.servicefactory.wallet;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.wso2telco.services.dep.sandbox.dao.model.domain.*;
import com.wso2telco.services.dep.sandbox.servicefactory.MessageProcessStatus;
import com.wso2telco.services.dep.sandbox.servicefactory.MessageType;
import com.wso2telco.services.dep.sandbox.util.*;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.WalletDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ListTransactionResponseBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ListTransactionDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;

public class ListTransactionRequestHandler extends AbstractRequestHandler<ListTransactionRequestWrapper> {

	private WalletDAO walletDAO;
	private ListTransactionResponseWrapper responseWrapper;
	private ListTransactionRequestWrapper requestWrapper;
	private MessageLogHandler logHandler;

	{
		LOG = LogFactory.getLog(ListTransactionRequestHandler.class);
		walletDAO = DaoFactory.getWalletDAO();
		dao = DaoFactory.getGenaricDAO();
		logHandler = MessageLogHandler.getInstance();

	}

	@Override
	protected Returnable getResponseDTO() {
		return responseWrapper;
	}

	@Override
	protected List<String> getAddress() {
		List<String> address = new ArrayList<String>();
		address.add(requestWrapper.getEndUserId());
		return address;
	}

	@Override
	protected boolean validate(ListTransactionRequestWrapper wrapperDTO) throws Exception {

		String endUserId = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getEndUserId());

		try {
			ValidationRule[] validationRules = { new ValidationRule(
					ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId) };

			Validation.checkRequestParams(validationRules);

		} catch (CustomException ex) {
			LOG.error("###WALLET### Error in Validations. ", ex);
			responseWrapper.setRequestError(
					constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
			return false;
		}
		return true;
	}

	@Override
	protected Returnable process(ListTransactionRequestWrapper extendedRequestDTO) throws Exception {

		if (responseWrapper.getRequestError() != null) {
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			return responseWrapper;
		}
		try {
			String msisdn = extendedRequestDTO.getEndUserId();
			String endUserId = getLastMobileNumber(msisdn);
			APITypes walletApi = dao.getAPIType(RequestType.WALLET.toString());

			String serviceCallMakePayment = ServiceName.MakePayment.toString();
			String serviceCallRefundPayment = ServiceName.RefundPayment.toString();
			APIServiceCalls apiServiceCallPayment = dao.getServiceCall(walletApi.getId(), serviceCallMakePayment);
			APIServiceCalls apiServiceCallRefund = dao.getServiceCall(walletApi.getId(), serviceCallRefundPayment);

			int paymentId = apiServiceCallPayment.getApiServiceCallId();
			int refundId = apiServiceCallRefund.getApiServiceCallId();

			List<Integer> attributeName = new ArrayList<>();
			attributeName.add(paymentId);
			attributeName.add(refundId);

			Integer userId = extendedRequestDTO.getUser().getId();

			// Save Request Log
			APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
			APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), ServiceName.ListPayment.toString());
			JSONObject object = new JSONObject();
			object.put("endUserId", msisdn);
			logHandler.saveMessageLog(apiServiceCalls.getApiServiceCallId(), extendedRequestDTO.getUser().getId(),
					"msisdn", msisdn, object);

			ListTransactionResponseBean paymentTransaction = new ListTransactionResponseBean();
			List<MessageLog> responses = loggingDAO.getMessageLogs(userId, attributeName, "msisdn", "tel:+" + endUserId, null, null);


			List<JsonNode> listNodes = new ArrayList<JsonNode>();
            String jsonString = null;

            if (responses != null && !responses.isEmpty()) {
                for (int i = 0; i < responses.size(); i++) {

                    int responseStatus = responses.get(i).getStatus();
                    int responseType = responses.get(i).getType();

                    if (responseType == MessageType.Response.getValue() && responseStatus == MessageProcessStatus.Success.getValue()) {
                        String request = responses.get(i).getRequest();
                        org.json.JSONObject json = new org.json.JSONObject(request);

                        int responseUserId = responses.get(i).getUserid();
                        String responseTel = responses.get(i).getValue();

                        if (responseUserId == userId && responseTel.equals("tel:+" + endUserId)) {
                            jsonString = json.toString();
                            JsonParser parser = new JsonParser();
                            JsonObject jsonObject = (JsonObject) parser.parse(jsonString);
                            JsonElement get = jsonObject.get("paymentAmount");
                            JsonObject asJsonObjectPayment = get.getAsJsonObject();
                            asJsonObjectPayment.remove("chargingMetaData");
                            jsonObject.remove("clientCorrelator");
                            jsonObject.remove("notifyURL");
                            jsonObject.remove("originalReferenceCode");
                            jsonObject.remove("originalServerReferenceCode");
                            jsonObject.remove("resourceURL");
                            String jsonInString = null;
                            jsonInString = jsonObject.toString();

                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode node = mapper.readValue(jsonInString, JsonNode.class);
                            listNodes.add(node);
                        }
                    }
                }
				paymentTransaction.setAmountTransaction(listNodes);
			} else {
				LOG.error("###WALLET### Valid Transaction List Not Available for msisdn: " + endUserId);
				responseWrapper.setHttpStatus(Status.NO_CONTENT);
				responseWrapper.setHttpStatus(Response.Status.OK);
				return responseWrapper;
			}
			paymentTransaction.setResourceURL(CommonUtil.getResourceUrl(extendedRequestDTO));

			ListTransactionDTO listTransactionDTO = new ListTransactionDTO();

			listTransactionDTO.setPaymentTransactionList(paymentTransaction);
			responseWrapper.setListPaymentDTO(listTransactionDTO);
			responseWrapper.setHttpStatus(Response.Status.OK);

		} catch (Exception ex) {
			LOG.error("###WALLET### Error Occured in Wallet Service. ", ex);
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
		}
		return responseWrapper;
	}

	@Override
	protected void init(ListTransactionRequestWrapper extendedRequestDTO) throws Exception {
		requestWrapper = extendedRequestDTO;
		responseWrapper = new ListTransactionResponseWrapper();
	}

}