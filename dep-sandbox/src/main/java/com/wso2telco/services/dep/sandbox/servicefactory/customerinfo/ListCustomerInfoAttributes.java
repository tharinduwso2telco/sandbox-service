package com.wso2telco.services.dep.sandbox.servicefactory.customerinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;
import org.hibernate.mapping.Map;
import org.omg.CORBA.PRIVATE_MEMBER;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.CustomerInfoDAO;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CustomerInfo;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ListCustomerInfoDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ListCustomerInfoRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceInfo;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceList;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ListCustomerInfoDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceListCustomerInfoDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceListDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceListProvisionedDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.CustomerInfoUtil;
import com.wso2telco.services.dep.sandbox.util.ProvisioningUtil;


public class ListCustomerInfoAttributes extends AbstractRequestHandler<ListCustomerInfoRequestWrapperDTO>{

	private CustomerInfoDAO customerInfoDao;
	private ListCustomerInfoRequestWrapperDTO requestWrapperDTO;
	private ListCustomerInfoAttributeServiceResponseWrapper responseWrapperDTO;


	{
	    LOG = LogFactory.getLog(ListCustomerInfoAttributes.class);
		customerInfoDao = DaoFactory.getCustomerInfoDAO();
		dao = DaoFactory.getGenaricDAO();
	}

	protected Returnable getResponseDTO() {
		return responseWrapperDTO;
	}


	protected List<String> getAddress() {
		List<String> address = new ArrayList<String>();
		address.add(requestWrapperDTO.getMsisdn());
		return address;
	}


	protected boolean validate(ListCustomerInfoRequestWrapperDTO wrapperDTO) throws Exception {

		String msisdn = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMsisdn());
		String imsi = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getImsi());
		String mcc = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMcc());
		String mnc = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMnc());
		String schema = CommonUtil.getNullOrTrimmedValue((wrapperDTO.getSchema()).replace(",", ""));

		List<ValidationRule> validationRulesList = new ArrayList<>();

		try {
		    if (msisdn == null && imsi == null) {
		    	responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
				ServiceError.INVALID_INPUT_VALUE, "MSISDN and IMSI are missing"));
		    	responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
		    }
		    if(schema!=null)
		    {
		    validationRulesList.add(
				new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO, "schema", schema));
		    }
		    else{
		    	responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,
				ServiceError.INVALID_INPUT_VALUE, "No valid schema provided"));
		    	responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);	
		    }
		    if (msisdn != null) {
			validationRulesList.add(
				new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_TEL, "msisdn", msisdn));
		    }

		    if (imsi != null) {
			validationRulesList
				.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "imsi", imsi));
		    }
		    if (mcc != null) {
			validationRulesList
				.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "mcc", mcc));
			validationRulesList
				.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO, "mnc", mnc));
		    } else {
			validationRulesList
			    .add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "mnc", mnc));
		    }


		    ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
		    validationRules = validationRulesList.toArray(validationRules);

		    Validation.checkRequestParams(validationRules);
		} catch (CustomException ex) {
		    LOG.error("###CUSTOMERINFO### Error in validations", ex);
		    responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(),
			    ex.getErrvar().toString()));
		    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
		} catch (Exception ex) {
		    LOG.error("###CUSTOMERINFO### Error in validations", ex);
		    responseWrapperDTO
			    .setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, null));
		}

		return true;
	    
	}


	protected Returnable process(ListCustomerInfoRequestWrapperDTO extendedRequestDTO) throws Exception {
		
		if (responseWrapperDTO.getRequestError() == null) {
			try {

				String msisdn = getLastMobileNumber(extendedRequestDTO.getMsisdn());
				String imsi = CommonUtil.getNullOrTrimmedValue(extendedRequestDTO.getImsi());
				String [] schema = CommonUtil.getStringToArray(extendedRequestDTO.getSchema());
				User user = extendedRequestDTO.getUser();

				List<AttributeValues> customerInfoServices = null;
				
				
				//check schema exist
				ListCustomerInfoDTO serviceList = new ListCustomerInfoDTO();

						for(int i=0; i<schema.length; i++){
							if(customerInfoDao.checkSchema(schema[i]))							
							{
							
							}
							else{
								responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
										"No valid schema provided "+ extendedRequestDTO.getSchema()));
							}
							
							
							
							

						}
				customerInfoServices = customerInfoDao.getAttributeServices(msisdn,user.getUserName(), imsi, schema);
				for(AttributeValues random : customerInfoServices){
					/*swithch(random.){
					case 'basic': 
						
				}*/
				}
			
						//ServiceList serviceList = new ServiceList();
					
				if (customerInfoServices != null && !customerInfoServices.isEmpty()) {
					for (AttributeValues service : customerInfoServices) {

					}
				} else {
					LOG.error(" Valid GET Attributes Services Not Available for msisdn: "+ msisdn);
					responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
							"Valid GET attributes Services Not Available for "+ extendedRequestDTO.getMsisdn()));
					responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
					return responseWrapperDTO;
				}
				
				responseWrapperDTO.setHttpStatus(Response.Status.OK);

			} catch (Exception ex) {
				LOG.error("###CUSTOMERINFO### Error Occured in GET attributes Service. " + ex);
				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, ServiceError.SERVICE_ERROR_OCCURED, "Error Occured in List customer info Service for " +extendedRequestDTO.getMsisdn()));
				responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
			}
		}
		return responseWrapperDTO;
		
	}

	@Override
	protected void init(ListCustomerInfoRequestWrapperDTO extendedRequestDTO) throws Exception {
		requestWrapperDTO = extendedRequestDTO;
		responseWrapperDTO = new ListCustomerInfoAttributeServiceResponseWrapper();
	}
}
