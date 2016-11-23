package com.wso2telco.services.dep.sandbox.servicefactory.wallet;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wso2telco.core.dbutils.exception.PolicyError;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.WalletDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ChargingInformation;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ChargingMetaData;
import com.wso2telco.services.dep.sandbox.dao.model.custom.MakePaymentDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.MakePaymentRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.MakePaymentRequestBean.makePayment;
import com.wso2telco.services.dep.sandbox.dao.model.custom.MakePaymentRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.MakePaymentResponseBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.PaymentAmount;
import com.wso2telco.services.dep.sandbox.dao.model.custom.PaymentAmountResponse;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.MessageLogHandler;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import com.wso2telco.services.dep.sandbox.util.TableName;


public class MakePaymentRequestHandler extends
		AbstractRequestHandler<MakePaymentRequestWrapperDTO> {
	
	private WalletDAO walletDAO;
	private MakePaymentRequestWrapperDTO requestWrapperDTO;
	private MakePaymentResponseWrapper responseWrapper;
    private MessageLogHandler logHandler;
    private String serviceCallPayment;
	public static final String serverReferenceCode = "SERVER0001";

	
	{
		LOG = LogFactory.getLog(MakePaymentRequestHandler.class);
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
		address.add(requestWrapperDTO.getEndUserId());
		return address;
	}
	
	@Override
	protected void init(MakePaymentRequestWrapperDTO extendedRequestDTO)
			throws Exception {
		responseWrapper = new MakePaymentResponseWrapper();
		requestWrapperDTO = extendedRequestDTO;
		
	}

	@Override
	protected boolean validate(MakePaymentRequestWrapperDTO wrapperDTO)
			throws Exception {
		MakePaymentRequestBean requestBean = wrapperDTO.getMakePaymentRequestBean();
		makePayment request = requestBean.getmakePayment();
		PaymentAmount paymentAmount = request.getPaymentAmount();
		ChargingInformation chargingInformation = paymentAmount.getChargingInformation();
		ChargingMetaData metaData = paymentAmount.getChargingMetaData();				
	
					String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
					String endUserID = CommonUtil.getNullOrTrimmedValue(request.getEndUserId());
					String amount = CommonUtil.getNullOrTrimmedValue(chargingInformation.getAmount().toString());
					String currency = CommonUtil.getNullOrTrimmedValue(chargingInformation.getCurrency());
					String decsription = CommonUtil.getNullOrTrimmedValue(chargingInformation.getDescription());
					String onBehalfOf = CommonUtil.getNullOrTrimmedValue(metaData.getOnBehalfOf());
					String categoryCode = CommonUtil.getNullOrTrimmedValue(metaData.getPurchaseCategoryCode());
					String channel = CommonUtil.getNullOrTrimmedValue(metaData.getChannel());
					String referenceCode = CommonUtil.getNullOrTrimmedValue(request.getReferenceCode());
					String notifyURL = CommonUtil.getNullOrTrimmedValue(request.getNotifyURL());
					
					List<ValidationRule> validationRulesList = new ArrayList<>();
					
					try {
						validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator));
						validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserID", endUserID));
						validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "amount", amount));
						validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_CURRENCY, "currency", currency));
						validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "decsription", decsription));		
						if(metaData != null){
							validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf", onBehalfOf));
							validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "categoryCode", categoryCode));
							validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,"channel",channel));
						}
						validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "referenceCode", referenceCode));
						validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notifyURL", notifyURL));
						
					    ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
					    validationRules = validationRulesList.toArray(validationRules);

					    Validation.checkRequestParams(validationRules);
					    
				}catch(CustomException ex){
				    LOG.error("###WALLET### Error in validations", ex);
				    String errorMessage = "";
				    if (ex.getErrvar() != null && ex.getErrvar().length > 0) {
					errorMessage = ex.getErrvar()[0];
				    }
				    responseWrapper.setRequestError(
					    constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), errorMessage));
				}catch (Exception e) {
				    LOG.error("###WALLET### Error in validations", e);
				    responseWrapper
					    .setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
				}		
		return true;
	}

	@Override
	protected Returnable process(MakePaymentRequestWrapperDTO extendedRequestDTO)
			throws Exception {

		if(responseWrapper.getRequestError() != null){
			return responseWrapper;
		}	
		try{
		MakePaymentRequestBean requestBean = extendedRequestDTO.getMakePaymentRequestBean();
		makePayment request = requestBean.getmakePayment();
		PaymentAmount paymentAmount = request.getPaymentAmount();
		ChargingInformation chargingInformation = paymentAmount.getChargingInformation();
		ChargingMetaData metadata = paymentAmount.getChargingMetaData();
		
		String clientCorrelator = CommonUtil.getNullOrTrimmedValue(request.getClientCorrelator());
		String endUserIdPath = extendedRequestDTO.getEndUserId();
		String endUserIdRequest = request.getEndUserId();
		String endUserId = getLastMobileNumber(endUserIdPath);
		Double chargeAmount =  Double.parseDouble(CommonUtil.getNullOrTrimmedValue(chargingInformation.getAmount().toString()));
		String currency = CommonUtil.getNullOrTrimmedValue(chargingInformation.getCurrency());
		String description = CommonUtil.getNullOrTrimmedValue(chargingInformation.getDescription());
		String onBehalfOf = CommonUtil.getNullOrTrimmedValue(metadata.getOnBehalfOf());
		String categoryCode = CommonUtil.getNullOrTrimmedValue(metadata.getPurchaseCategoryCode());
		String channel = CommonUtil.getNullOrTrimmedValue(metadata.getChannel());
		String referenceCode = CommonUtil.getNullOrTrimmedValue(request.getReferenceCode());
		String notifyURL = CommonUtil.getNullOrTrimmedValue(request.getNotifyURL());
		serviceCallPayment = ServiceName.MakePayment.toString();
		
		MakePaymentResponseBean responseBean = new MakePaymentResponseBean();
		PaymentAmountResponse payAmount = new PaymentAmountResponse();
		ChargingInformation chargeInformation = new ChargingInformation();
		ChargingMetaData chargeMetaData = new ChargingMetaData();

		//Save Request Log
		APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
        APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), serviceCallPayment);
        JSONObject object = new JSONObject();
		Gson gson = new Gson();		
		JsonElement je = new JsonParser().parse(gson.toJson(requestBean));
	    JsonObject asJsonObject = je.getAsJsonObject();
        object.put("PaymentTransaction", asJsonObject);
        
        logHandler.saveMessageLog(apiServiceCalls.getApiServiceCallId(), extendedRequestDTO.getUser().getId(), "msisdn", endUserIdPath, object);

        //check path param endUserId and request body endUserId
        if(!(endUserIdPath.equals(endUserIdRequest))){
		    LOG.error("###WALLET### two different endUserId provided");
		    responseWrapper
			    .setRequestError(constructRequestError(SERVICEEXCEPTION,
				    ServiceError.INVALID_INPUT_VALUE,
				    "two different endUserId provided"));
		    responseWrapper.setHttpStatus(Status.BAD_REQUEST);
		    return responseWrapper;
        }
       
        //check account amount decimal format
        NumberFormat formatter = new DecimalFormat("#.00");
        String formatChargeAmount = formatter.format(chargeAmount);
        if(!(chargeAmount % 1 == 0) && !(formatChargeAmount.equals(chargeAmount.toString()))){
		    LOG.error("###WALLET### amount should be a whole number or two digit decimal");
		    responseWrapper
			    .setRequestError(constructRequestError(SERVICEEXCEPTION,
				    ServiceError.INVALID_INPUT_VALUE,
				    "amount should be a whole number or two digit decimal"));
		    responseWrapper.setHttpStatus(Status.BAD_REQUEST);
		    return responseWrapper;        }
        
		//check valid account currency for endUserId
		boolean isValidCurrency = currencySymbol(currency);
		if(!isValidCurrency){
			LOG.error("###WALLET### currency code not valid accorfing to ISO 4217");
		    responseWrapper
			    .setRequestError(constructRequestError(SERVICEEXCEPTION,
				    ServiceError.INVALID_INPUT_VALUE,
				    "currency code not valid accorfing to ISO 4217"));
		    responseWrapper.setHttpStatus(Status.BAD_REQUEST);
		    return responseWrapper;
		
		}
        
		String  serviceCallBalanceLookUp = ServiceName.BalanceLookup.toString();
		String accountCurrency = AttributeName.Currency.toString().toLowerCase();
		String accountCurrencyValue = walletDAO.getAttributeValue(endUserId, serviceCallBalanceLookUp, accountCurrency);
		if(accountCurrencyValue != null && !(currency.equals(accountCurrencyValue))){
		    LOG.error("###WALLET### Valid currecy doesn't exists for the given inputs");
		    responseWrapper
			    .setRequestError(constructRequestError(SERVICEEXCEPTION,
				    ServiceError.INVALID_INPUT_VALUE,
				    "Valid currency does not exist for the given input parameters"));
		    responseWrapper.setHttpStatus(Status.BAD_REQUEST);
		    return responseWrapper;
		}
		

		//check already charge request against client correlator
		if(clientCorrelator != null){
			boolean isDuplicate = walletDAO.checkClientCorrelator(endUserId, serviceCallPayment, clientCorrelator);
			if(isDuplicate){
			    LOG.error("###WALLET### Already charged for this client correlator");
			    responseWrapper
				    .setRequestError(constructRequestError(SERVICEEXCEPTION,
					    ServiceError.INVALID_INPUT_VALUE,
					    "Already charged for this client correlator"));
			    responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			    return responseWrapper;
			}
		}
		responseBean.setClientCorrelator(clientCorrelator);
		responseBean.setEndUserId(endUserIdPath);
			
			chargeInformation.setAmount(formatChargeAmount);
			chargeInformation.setCurrency(currency);
			chargeInformation.setDescription(description);
			
			if(onBehalfOf != null || categoryCode != null || channel != null){
				chargeMetaData.setPurchaseCategoryCode(categoryCode);
				chargeMetaData.setChannel(channel);
				chargeMetaData.setOnBehalfOf(onBehalfOf);
			}
		
			responseBean.setReferenceCode(referenceCode);
			responseBean.setServerReferenceCode(serverReferenceCode);
			responseBean.setNotifyURL(notifyURL);
		
		Double balance = walletDAO.checkBalance(endUserId);
		String transactionStatus = walletDAO.getAttributeValue(endUserId, serviceCallPayment,AttributeName.transactionStatus.toString());
		
		//transaction operation status as denied
		if((balance <  chargeAmount)){
			LOG.error("###WALLET### Denied : Account balance insufficient to charge request " );
			responseWrapper.setHttpStatus(Status.FORBIDDEN);
			responseWrapper
					.setRequestError(constructRequestError(POLICYEXCEPTION, PolicyError.NO_VALID_SERVICES_AVAILABLE, "Denied : Account balance insufficient to charge request"));
			return responseWrapper;
		}		
		
		//set transaction operation status as refused
		else if( transactionStatus != null && transactionStatus.equals(TransactionStatus.Refused.toString())){
			responseBean.setTransactionOperationStatus(TransactionStatus.Refused.toString());	
			
		//set transaction status as charged
		} else if(balance >=  chargeAmount){
			balance = balance - chargeAmount;
			walletDAO.updateBalance(endUserId, balance);
			responseBean.setTransactionOperationStatus(TransactionStatus.Charged.toString());
		} 

		MakePaymentDTO makePaymentDTO = new MakePaymentDTO();
		payAmount.setChargingInformation(chargeInformation);
		boolean isContainsMetaData = false;
		if(onBehalfOf != null || categoryCode != null || channel != null){
			payAmount.setChargingMetaData(chargeMetaData);
			isContainsMetaData = true;
		}
		responseBean.setPaymentAmount(payAmount);
		makePaymentDTO.setmakePayment(responseBean);
		responseWrapper.setMakePaymentDTO(makePaymentDTO);
		responseWrapper.setHttpStatus(Response.Status.OK);

		//save payment transaction
		saveTransaction(responseBean, endUserId, isContainsMetaData);
		
		//save client correlator
		if(clientCorrelator != null){
		saveClientCorrelator(endUserId, clientCorrelator);
		}
		} catch(Exception ex){
			LOG.error("###WALLET### Error Occured in WALLET Service. " + ex);
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
		}
		return responseWrapper;	
	}

	public void saveTransaction(MakePaymentResponseBean responseBean , String endUserId, boolean metaData) throws Exception{
	    AttributeDistribution distributionId = null;
	    Integer ownerId = null;
		try{
		    AttributeValues valueObj = new AttributeValues();
		    String tableName = TableName.NUMBERS.toString().toLowerCase();
		    String attributeName = AttributeName.Payment.toString().toLowerCase();
		    distributionId = walletDAO.getDistributionValue(serviceCallPayment,attributeName, RequestType.WALLET.toString());
		    ownerId =  walletDAO.getNumber(endUserId);
			
		    String jsonInString = null;
			Gson gson = new Gson();
			
			JsonElement je = new JsonParser().parse(gson.toJson(responseBean));
		    JsonObject asJsonObject = je.getAsJsonObject();
			if(metaData){
				JsonElement get = asJsonObject.get("paymentAmount");
			    JsonObject asJsonObjectPayment = get.getAsJsonObject();
			    asJsonObjectPayment.remove("chargingMetaData");
			}
		    asJsonObject.remove("clientCorrelator");
			asJsonObject.remove("notifyURL");	
		    jsonInString = asJsonObject.toString();
		    
			valueObj = new AttributeValues();
			valueObj.setAttributedid(distributionId);
			valueObj.setOwnerdid(ownerId);
			valueObj.setTobject(tableName);
			valueObj.setValue(jsonInString);
			dao.saveAttributeValue(valueObj);

		}catch(Exception ex){
		    LOG.error("###WALLET### Error in processing save transaction. ", ex);
		    responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
		}
	}
	public void saveClientCorrelator(String endUserId, String clientCorrelator) throws Exception{
	    AttributeDistribution distributionId = null;
	    Integer ownerId = null;
		try{
		    AttributeValues valueObj = new AttributeValues();
		    String tableName = TableName.NUMBERS.toString().toLowerCase();
		    String attributeName = AttributeName.clientCorrelator.toString();
		    String apiType = RequestType.WALLET.toString();
		    distributionId = walletDAO.getDistributionValue(serviceCallPayment,attributeName, apiType );
		    ownerId =  walletDAO.getNumber(endUserId);
		    
			valueObj = new AttributeValues();
			valueObj.setAttributedid(distributionId);
			valueObj.setOwnerdid(ownerId);
			valueObj.setTobject(tableName);
			valueObj.setValue(clientCorrelator);
			dao.saveAttributeValue(valueObj);

		}catch(Exception ex){
		    LOG.error("###WALLET### Error in processing save insertion of clientCorrelator request. ", ex);
		    responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
		}
	}
	
    private static boolean currencySymbol(@Nonnull final String currencyCode)
    {
    	try
    	{
    		final Currency currency = Currency.getInstance(currencyCode);
    		return true;
    	}
    	catch (final IllegalArgumentException x)
    	{
    		return false;
    	}
    }
}
