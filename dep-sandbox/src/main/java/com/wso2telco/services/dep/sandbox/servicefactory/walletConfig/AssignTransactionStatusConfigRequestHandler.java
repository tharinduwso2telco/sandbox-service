package com.wso2telco.services.dep.sandbox.servicefactory.walletConfig;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.WalletDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.AssignTransactionStatusConfigRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.AttributeName;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.TransactionStatus;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import com.wso2telco.services.dep.sandbox.util.TableName;

public class AssignTransactionStatusConfigRequestHandler extends AbstractRequestHandler<AssignTransactionStatusConfigRequestWrapper>{
	
	AssignTransactionStatusConfigRequestWrapper requestWrapper;
	AssignTransactionStatusConfigResponseWrapper responseWrapper;
	WalletDAO walletDAO;

	
    {
	LOG = LogFactory.getLog(AssignTransactionStatusConfigRequestHandler.class);
	dao = DaoFactory.getGenaricDAO();
	walletDAO = DaoFactory.getWalletDAO();
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
	protected boolean validate(AssignTransactionStatusConfigRequestWrapper wrapperDTO)
			throws Exception {
		
		AssignTransactionStatusConfigRequestBean requestBean = wrapperDTO.getRequestBean();
			
			String endUserId = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getEndUserId());
			String serviceCall = CommonUtil.getNullOrTrimmedValue(requestBean.getServiceCall());
			String accountStatus = CommonUtil.getNullOrTrimmedValue(requestBean.getStatus());
			
			List<ValidationRule> validationRulesList = new ArrayList<>();

			try {
				
				validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "endUserId", endUserId));
				validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,"",serviceCall));
				validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,"",accountStatus));

			    ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
			    validationRules = validationRulesList.toArray(validationRules);
				
				Validation.checkRequestParams(validationRules);

			} catch (CustomException ex) {
				LOG.error("###WALLET### Error in Validation : " + ex);
				responseWrapper.setRequestError(constructRequestError(
						SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(),
						wrapperDTO.getEndUserId()));
				responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
			}
		
		return true;
	}

	@Override
	protected Returnable process(
			AssignTransactionStatusConfigRequestWrapper extendedRequestDTO)
			throws Exception {
		if(responseWrapper.getRequestError() != null){
			return responseWrapper;
		}
		try{
			
			AssignTransactionStatusConfigRequestBean requestBean = extendedRequestDTO.getRequestBean();
			
			String endUserId = CommonUtil.extractNumberFromMsisdn(extendedRequestDTO.getEndUserId());
			String serviceCall =  CommonUtil.getNullOrTrimmedValue(requestBean.getServiceCall());
			String accountStatus = CommonUtil.getNullOrTrimmedValue(requestBean.getStatus());

			String serviceCallPayment = ServiceName.MakePayment.toString();
			String serviceCallRefund = ServiceName.RefundPayment.toString();
			String statusRefused = TransactionStatus.Refused.toString();
			
			//Check valid service call		
			if(!(serviceCall.equals(serviceCallPayment) || serviceCall.equals(serviceCallRefund))){
				LOG.error("###WALLET### Provide Valid Service Call ");
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, "Provide Valid Service Call"));
			return responseWrapper;
			}
			
			//check valid account status
			if(!(accountStatus.equals(statusRefused))){
				LOG.error("###WALLET### Invalid status ");
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, "Provide Valid status"));
			return responseWrapper;
			}

				Integer ownerId = null;
				ownerId = walletDAO.getNumber(endUserId);
				String tableName = TableName.NUMBERS.toString().toLowerCase();
				String attributeName = AttributeName.transactionStatus.toString();				
				AttributeDistribution distibutionId = walletDAO.getDistributionValue(serviceCall, attributeName,RequestType.WALLET.toString() );
				
				List<AttributeValues> attributeValues = new ArrayList<AttributeValues>();
				AttributeValues valueObj = new AttributeValues();
			    valueObj = walletDAO.getAttributeValue(distibutionId, ownerId);
			    
			    if (valueObj != null) {

				valueObj.setValue(accountStatus);
				attributeValues.add(valueObj);

			    }else{
					valueObj = new AttributeValues();
					valueObj.setAttributedid(distibutionId);
					valueObj.setOwnerdid(ownerId);
					valueObj.setTobject(tableName);
					valueObj.setValue(accountStatus);
					attributeValues.add(valueObj);
			    }
			    dao.saveAttributeValue(attributeValues);

				responseWrapper
					.setStatus("Successfully Updated attribute for transaction status!!");
				responseWrapper.setHttpStatus(Response.Status.CREATED);

				return responseWrapper;
				
		}catch (CustomException ex) {
			LOG.error("###WALLETCONFIG### Error in validations", ex);
			String errorMessage = "";
			if (ex.getErrvar() != null && ex.getErrvar().length > 0) {
				errorMessage = ex.getErrvar()[0];
			}
			responseWrapper.setRequestError(constructRequestError(
					SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(),
					errorMessage));
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
		}catch(Exception ex){
			LOG.error("###WALLET### Error Occured in WALLET Service. " + ex);
			responseWrapper.setHttpStatus(Status.BAD_REQUEST);
			responseWrapper
					.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
			return responseWrapper;
		}
		
		return responseWrapper;
	}
	
	@Override
	protected void init(AssignTransactionStatusConfigRequestWrapper extendedRequestDTO)
			throws Exception {
		requestWrapper = extendedRequestDTO;
		responseWrapper = new AssignTransactionStatusConfigResponseWrapper();
		
	}

}
