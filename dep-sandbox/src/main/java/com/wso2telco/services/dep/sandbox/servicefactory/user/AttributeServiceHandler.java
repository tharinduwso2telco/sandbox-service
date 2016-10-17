package com.wso2telco.services.dep.sandbox.servicefactory.user;

import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Objects;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.CustomerInfoDAO;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.model.custom.AttributeRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.AttributeRequestBean.AttributeProperties;
import com.wso2telco.services.dep.sandbox.dao.model.custom.AttributeRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Attributes;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.AddressIgnorerable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.servicefactory.provisioning.ProvisionRequestedServiceHandler;
import com.wso2telco.services.dep.sandbox.util.APIAttributeToObjectMaps;
import com.wso2telco.services.dep.sandbox.util.AttributeValueJsonObject;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;

public class AttributeServiceHandler extends
	AbstractRequestHandler<AttributeRequestWrapperDTO> implements
	AddressIgnorerable {

    private AttributeRequestWrapperDTO requestWrapperDTO;
    private AttributeResponseWrapper responseWrapper;
    private APITypes apiObj;
    private APIServiceCalls serviceObj;
    private Attributes attributeObj;
    private AttributeDistribution distributionObj;
    private AttributeRequestBean attributeBean;
    private AttributeProperties attributeProperty;

    {
	LOG = LogFactory.getLog(ProvisionRequestedServiceHandler.class);
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
	String api = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getApiType());
	String service = CommonUtil.getNullOrTrimmedValue(wrapperDTO
		.getServiceType());
	try {
	    checkIfBeanExists(wrapperDTO);
	    checkIfAttributePropertyExists();
	    String name = CommonUtil.getNullOrTrimmedValue(attributeProperty
		    .getName());
	    String value = CommonUtil.getNullOrTrimmedValue(attributeProperty
		    .getValue());
	    ValidationRule[] validationRules = defineValidationRules(api,
		    service, name, value);
	    Validation.checkRequestParams(validationRules);
	    
	    checkAttributeValueObjectMapsName(name.toLowerCase(),value);
	    
	    checkApiExist(api);
	    checkApiServiceExist(service);
	    checkAttributeExist(name);
	    checkAttributeDistributionExist();

	} catch (CustomException ex) {
	    LOG.error("###USER### Error in Validation of Mandotary/Optional params : "
		    + ex);

	    responseWrapper.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(),
		    wrapperDTO.getRequestPath()));
	    return false;

	} catch (Exception ex) {
	    LOG.error("###USER### Error in Validation of doue to Non-existing data "
		    + ex);
	    return false;
	}
	return true;
    }

    private void checkAttributeValueObjectMapsName(String name, String value)
	    throws Exception {

	for (AttributeValueJsonObject attribute : AttributeValueJsonObject
		.values()) {
	    if (attribute.name().equals(name)) {
		try {
		    new JSONObject(value);
		} catch (JSONException ex) {
		    responseWrapper.setRequestError(constructRequestError(
			    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
			    "Attribute value for" + name
				    + "should be a JSON Object"));
		    if (name.toLowerCase() == AttributeValueJsonObject.ADDITIONALINFO
			    .toString()) {
			try {
			    new JSONArray(value);
			} catch (JSONException exc) {
			    responseWrapper
				    .setRequestError(constructRequestError(
					    SERVICEEXCEPTION,
					    ServiceError.INVALID_INPUT_VALUE,
					    "Attribute value for"
						    + name
						    + "should be a JSON Object/Array"));
			}
		    }
		    throw new Exception();
		}
	    }
	}

    }

    private ValidationRule[] defineValidationRules(String api, String service,
	    String name, String value) {

	ValidationRule[] validationRules = {
		new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
			"apiType", api),
		new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
			"serviceType", service),
		new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
			"name", name),
		new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
			"value", value), };
	return validationRules;
    }

    private void checkIfAttributePropertyExists() {
	attributeProperty = attributeBean.getAttribute();
	if (attributeProperty == null) {
	    responseWrapper.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Empty Attribute within Body sent"));
	}

    }

    private void checkIfBeanExists(AttributeRequestWrapperDTO wrapperDTO)
	    throws Exception {
	attributeBean = wrapperDTO.getAttributeBean();
	if (attributeBean == null) {
	    responseWrapper.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Empty Body Sent"));
	    throw new Exception();
	}
    }

    @Override
    protected Returnable process(AttributeRequestWrapperDTO extendedRequestDTO)
	    throws Exception {
	if (responseWrapper.getRequestError() != null) {
	    responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
	    return responseWrapper;
	}
	
	try {
	    
	    AttributeValues valueObj = new AttributeValues();
	    valueObj = dao.getAttributeValue(distributionObj);
	    if (valueObj != null) {
		valueObj.setValue(attributeProperty.getValue());
	    } else {
		valueObj = new AttributeValues();
		valueObj.setAttributedid(distributionObj);
		valueObj.setOwnerdid(extendedRequestDTO.getUser().getId());
		valueObj.setTobject(getToObject(apiObj));
		valueObj.setValue(attributeProperty.getValue());
	    }
	    dao.saveAttributeValue(valueObj);
	    responseWrapper.setHttpStatus(Response.Status.CREATED);

	} catch (Exception ex) {
	    LOG.error(
		    "###USER### Error in processing attribute insertion service request. ",
		    ex);
	    responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
	}
	return responseWrapper;
    }

    private String getToObject(APITypes api) throws Exception {
	String apiEnum = api.getAPIName().toUpperCase();
	String toObj = "";
	try {
	    toObj = APIAttributeToObjectMaps.valueOf(apiEnum).getTableName();
	} catch (Exception ex) {
	    LOG.error("###USER### Error in finding  which Table the attribute should go for this API ");
	    throw new Exception();
	}
	return toObj;
    }

    private void checkAttributeDistributionExist() throws Exception {
	distributionObj = dao
		.getAttributeDistribution(serviceObj.getApiServiceCallId(),
			attributeObj.getAttributeId());
	if (distributionObj == null) {
	    responseWrapper.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Given Attribute name is invalid for Service Call"));
	    LOG.error("###USER### Error in Validation of Given Attribute name Service Call");
	    throw new Exception();
	}
    }

    private void checkAttributeExist(String attribute) throws Exception {
	attributeObj = dao.getAttribute(attribute.toLowerCase());
	if (attributeObj == null) {
	    responseWrapper.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Given Attribute is invalid" + attribute));
	    LOG.error("###USER### Error in Validation of Given Attribute is invalid");
	    throw new Exception();
	}
    }

    private void checkApiServiceExist(String service) throws Exception {
	serviceObj = dao.getServiceCall(apiObj.getId(), service.toLowerCase());
	if (serviceObj == null) {
	    responseWrapper.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Service " + service + " is Invalid for given API"));
	    LOG.error("###USER### Error in Validation due to Service "
		    + service + " is Invalid for given API");

	    throw new Exception();
	}
    }

    private void checkApiExist(String api) throws Exception {
	apiObj = dao.getAPIType(api.toLowerCase());
	if (apiObj == null) {
	    responseWrapper.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Given API is invalid" + api));
	    LOG.error("###USER### Error in Validation of Given API is invalid");
	    throw new Exception();
	}
    }

    @Override
    protected void init(AttributeRequestWrapperDTO extendedRequestDTO)
	    throws Exception {
	requestWrapperDTO = extendedRequestDTO;
	responseWrapper = new AttributeResponseWrapper();
    }
}
