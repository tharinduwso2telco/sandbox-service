package com.wso2telco.services.dep.sandbox.servicefactory.user;

import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.CustomerInfoDAO;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.model.custom.AttributeRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.AttributeRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.AddressIgnorerable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.provisioning.ProvisionRequestedServiceHandler;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;

public class AttributeServiceHandler extends
	AbstractRequestHandler<AttributeRequestWrapperDTO>  implements AddressIgnorerable{

    private CustomerInfoDAO customerInfoDao;
    private AttributeRequestWrapperDTO requestWrapperDTO;
    private AttributeResponseWrapper responseWrapper;

    {
	LOG = LogFactory.getLog(ProvisionRequestedServiceHandler.class);
	customerInfoDao = DaoFactory.getCustomerInfoDAO();
	dao = DaoFactory.getGenaricDAO();
    }

    @Override
    protected Returnable getResponseDTO() {
	return responseWrapper;
    }

    @Override
    protected List<String> getAddress() {
	return null;
    }

    @Override
    protected boolean validate(AttributeRequestWrapperDTO wrapperDTO)
	    throws Exception {
	AttributeRequestBean attribute = wrapperDTO.getAttribute();
	if (attribute != null) {
	    String name = CommonUtil.getNullOrTrimmedValue(attribute.getAttributeName());
	    String value = CommonUtil.getNullOrTrimmedValue(attribute.getAttributeValue());

	    try {
		ValidationRule[] validationRules = {
			new ValidationRule(
				ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO,
				"offset", name),
			new ValidationRule(
				ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO,
				"limit", value) };

		Validation.checkRequestParams(validationRules);
	    } catch (CustomException ex) {
		LOG.error("###PROVISION### Error in Validation : " + ex);
		responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(),wrapperDTO.getRequestPath()));
		responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
	    }
	}
	responseWrapper.setRequestError(constructRequestError(SERVICEEXCEPTION,
		ServiceError.INVALID_INPUT_VALUE, "Empty Body Sent"));
	responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
	return false;
    }

    @Override
    protected Returnable process(AttributeRequestWrapperDTO extendedRequestDTO)
	    throws Exception {/*
	if (responseWrapper.getRequestError() != null) {
		return responseWrapper;
	}
	AttributeRequestBean attribute = extendedRequestDTO.getAttribute();
	String name = CommonUtil.getNullOrTrimmedValue(attribute.getAttributeName());
	String value = CommonUtil.getNullOrTrimmedValue(attribute.getAttributeValue());
	try{
	    //provisioningDao.
	    
	    Attribute attributeObject =customerInfoDao.getAttribute(name);
	    if(attributeObject!= null){
		AttributeMap attributeMapObject = new AttributeMap();
		attributeMapObject.setAttributedid(attributeObject);
		attributeMapObject.setValue(value);
		attributeMapObject.setOwnerdid(ownerdid);
		attributeMapObject.setTobject(tobject);
		customerInfoDao.saveAttributeMap(attributeMapObject);
	    }
	    responseWrapper.setResponseMessage("Successfully Added the requested Attribute");
	    responseWrapper.setHttpStatus(Response.Status.CREATED);

	}catch(Exception ex){
		LOG.error("###USER### Error in processing attribute insertion service request. ", ex);
		responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);

	}
	*/
	return responseWrapper;

    }

    @Override
    protected void init(AttributeRequestWrapperDTO extendedRequestDTO)
	    throws Exception {
	requestWrapperDTO = extendedRequestDTO;
	responseWrapper = new AttributeResponseWrapper();
	
    }

    

  

}
