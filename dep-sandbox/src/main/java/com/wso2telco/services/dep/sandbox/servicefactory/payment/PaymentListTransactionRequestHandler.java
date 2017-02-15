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
package com.wso2telco.services.dep.sandbox.servicefactory.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.PaymentDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.PaymentListTransactionDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.PaymentListTransactionResponseBean;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.AttributeName;
import com.wso2telco.services.dep.sandbox.util.*;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class PaymentListTransactionRequestHandler extends AbstractRequestHandler<PaymentListTransactionRequestWrapper> {

    private PaymentDAO paymentDAO;
    private PaymentListTransactionResponseWrapper responseWrapper;
    private PaymentListTransactionRequestWrapper requestWrapper;
    private MessageLogHandler logHandler;

    {
        LOG = LogFactory.getLog(PaymentListTransactionRequestHandler.class);
        paymentDAO = DaoFactory.getPaymentDAO();
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
    protected boolean validate(PaymentListTransactionRequestWrapper wrapperDTO) throws Exception {

        String endUserId = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getEndUserId());

        try {
            ValidationRule[] validationRules = {new ValidationRule(
                    ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId)};

            Validation.checkRequestParams(validationRules);
        } catch (CustomException ex) {
            LOG.error("###PAYMENT### Error in Validations. ", ex);
            responseWrapper.setRequestError(
                    constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
            return false;
        }

        return true;
    }

    @Override
    protected Returnable process(PaymentListTransactionRequestWrapper extendedRequestDTO) throws Exception {

        if (responseWrapper.getRequestError() != null) {
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            return responseWrapper;
        }

        try {
            String msisdn = extendedRequestDTO.getEndUserId();
            String endUserId = getLastMobileNumber(msisdn);
            List<AttributeValues> amountTransaction = null;
            List<String> attributeName = new ArrayList<String>();
            attributeName.add(AttributeName.makePayment.toString().toLowerCase());
            attributeName.add(AttributeName.refundUser.toString().toLowerCase());
            Integer userId = extendedRequestDTO.getUser().getId();

            // Save Request Log
            APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
            APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), ServiceName.ListPayment.toString());
            JSONObject object = new JSONObject();
            object.put("endUserId", msisdn);
            logHandler.saveMessageLog(apiServiceCalls.getApiServiceCallId(), extendedRequestDTO.getUser().getId(),
                    "msisdn", msisdn, object);

            String tableName = TableName.NUMBERS.toString().toLowerCase();
            PaymentListTransactionResponseBean paymentTransaction = new PaymentListTransactionResponseBean();
            amountTransaction = paymentDAO.getTransactionValue(endUserId, attributeName, tableName, userId);

            List<JsonNode> listNodes = new ArrayList<JsonNode>();

            if (amountTransaction != null && !amountTransaction.isEmpty()) {
                for (AttributeValues values : amountTransaction) {
                    JsonParser parser = new JsonParser();
                    JsonObject jsonObject = parser.parse(values.getValue()).getAsJsonObject();
                    JsonElement get = jsonObject.get("paymentAmount");
                    JsonObject asJsonObjectPayment = get.getAsJsonObject();
                    asJsonObjectPayment.remove("totalAmountCharged");
                    asJsonObjectPayment.remove("totalAmountRefunded");
                    asJsonObjectPayment.remove("chargingMetaData");
                    jsonObject.remove("clientCorrelator");
                    jsonObject.remove("notifyURL");
                    jsonObject.remove("originalReferenceCode");
                    jsonObject.remove("originalServerReferenceCode");
                    jsonObject.remove("resourceURL");
                    String jsonInString = null;
                    jsonInString = jsonObject.toString();

                    JsonNode node = null;
                    ObjectMapper mapper = new ObjectMapper();
                    node = mapper.readValue(jsonInString, JsonNode.class);
                    listNodes.add(node);
                }
                paymentTransaction.setAmountTransaction(listNodes);
            } else {
                LOG.error("###PAYMENT### Valid Transaction List Not Available for msisdn: " + endUserId);
                responseWrapper.setHttpStatus(Response.Status.NO_CONTENT);
                responseWrapper.setHttpStatus(Response.Status.OK);
                return responseWrapper;
            }
            paymentTransaction.setResourceURL(CommonUtil.getResourceUrl(extendedRequestDTO));

            PaymentListTransactionDTO listTransactionDTO = new PaymentListTransactionDTO();

            listTransactionDTO.setPaymentTransactionList(paymentTransaction);
            responseWrapper.setListPaymentDTO(listTransactionDTO);
            responseWrapper.setHttpStatus(Response.Status.OK);

        } catch (Exception ex) {
            LOG.error("###PAYMENT### Error Occurred in PAYMENT Service. ", ex);
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            responseWrapper
                    .setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
            throw ex;
        }

        return responseWrapper;
    }


    @Override
    protected void init(PaymentListTransactionRequestWrapper extendedRequestDTO) throws Exception {
        requestWrapper = extendedRequestDTO;
        responseWrapper = new PaymentListTransactionResponseWrapper();
    }
}
