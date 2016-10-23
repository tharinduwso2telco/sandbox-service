/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licenses this file to you under  the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.services.dep.sandbox.servicefactory.user;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
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
import com.wso2telco.services.dep.sandbox.util.APIAttributeToObjectMaps;
import com.wso2telco.services.dep.sandbox.util.AttributeEnum;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.AccountField;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.AdditionalInfoField;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.AddressField;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.BasicField;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.BillingField;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.IdentificationField;
import com.wso2telco.services.dep.sandbox.util.AttributeValueJsonObject;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;

;

public class AttributeInsertionServiceHandler extends
	AbstractRequestHandler<AttributeRequestWrapperDTO> implements
	AddressIgnorerable {

    private AttributeRequestWrapperDTO requestWrapperDTO;
    private static AttributeInsertionResponseWrapper responseWrapper;
    private APITypes apiObj;
    private APIServiceCalls serviceObj;
    private Attributes attributeObj;
    private AttributeDistribution distributionObj;
    private AttributeRequestBean attributeBean;
    private AttributeProperties attributeProperty;
    private final String MANDATORY = "Mandatory";
    private final String OPTIONAL = "Optional";
    List<ValidationRule> validationRule = new ArrayList<ValidationRule>();

    {
	LOG = LogFactory.getLog(AttributeInsertionServiceHandler.class);
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
		    .getValue().toString());

	    ValidationRule[] validationRules = defineCommonValidationRules(api,
		    service, name, value);
	    Validation.checkRequestParams(validationRules);

	    checkApiExist(api);
	    checkApiServiceExist(service);
	    checkAttributeExist(name);
	    checkAttributeDistributionExist();
	    checkAttributeValueObjectMapsName(name.toLowerCase(), value);
	    checkFieldsForJsonAttribute(name.toUpperCase(), value);

	} catch (CustomException ex) {
	    LOG.error("###USER### Error in Validation of Mandotary/Optional values : "
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

    private void checkFieldsForJsonAttribute(String lowerCase, String value)
	    throws Exception {

	if (AttributeValueJsonObject.valueOf(lowerCase) != null) {
	    String methodName = lowerCase.substring(0, 1)
		    + lowerCase.substring(1).toLowerCase();
	    Method method = AttributeInsertionServiceHandler.class
		    .getDeclaredMethod("validate" + methodName, String.class);
	    method.invoke(this, value);
	    checkValidationForParams();
	}

    }

    private void validateAdditionalInfo(String attributeValue) throws Exception {

	try {

	    JSONObject valueObj = new JSONObject(attributeValue);
	    checkMandatoryfields(AdditionalInfoField.values(),
		    attributeValue.toString());

	} catch (JSONException ex) {
	    LOG.error("###USER### Provided Additional Info Attribute Value is not a json object");
	    JSONArray jsonValueArray = new JSONArray(attributeValue);

	    for (int jsonObjectIndex = 0; jsonObjectIndex < jsonValueArray
		    .length(); jsonObjectIndex++) {
		JSONObject jsonValueObject = jsonValueArray
			.getJSONObject(jsonObjectIndex);
		checkMandatoryfields(AdditionalInfoField.values(),
			jsonValueObject.toString());
	    }
	}
    }

    private void validateAccount(String value) throws Exception {

	checkMandatoryfields(AccountField.values(), value);

    }

    private void validateBasic(String value) throws Exception {

	checkMandatoryfields(BasicField.values(), value);

    }

    private void validateBilling(String value) throws Exception {

	checkMandatoryfields(BillingField.values(), value);

    }

    private void validateIdentification(String value) throws Exception {

	checkMandatoryfields(IdentificationField.values(), value);

    }

    private void validateAddress(String value) throws Exception {

	checkMandatoryfields(AddressField.values(), value);

    }

    private void checkFieldNameExist(JSONObject valueObj, String field)
	    throws Exception {
	if (!valueObj.has(field)) {
	    responseWrapper.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Attribute value should have mandatory field " + field));
	    LOG.error("###USER### Attribute value should have mandatory field "
		    + field);

	    throw new Exception();
	}

    }

    private List<ValidationRule> checkMandatoryfields(AttributeEnum[] class1,
	    String value) throws Exception {

	JSONObject valueObj = new JSONObject(value);

	for (AttributeEnum attribute : class1) {
	    if (attribute.getFieldType().equals(MANDATORY)) {
		checkFieldNameExist(valueObj, attribute.toString());
		String fieldValue = CommonUtil.getNullOrTrimmedValue(valueObj
			.getString(attribute.toString()));
		validationRule.add(new ValidationRule(
			ValidationRule.VALIDATION_TYPE_MANDATORY, attribute
				.toString(), fieldValue));

	    } else if (attribute.getFieldType().equals(OPTIONAL)) {
		if (valueObj.has(BasicField.address.toString()) && attribute.toString().equals(BasicField.address.toString())) {
		    validateAddress(valueObj.getJSONObject(attribute.toString()).toString());
		} else {
		    String fieldValue = CommonUtil
			    .getNullOrTrimmedValue(valueObj.optString(attribute
				    .toString()));
		    validationRule.add(new ValidationRule(
			    ValidationRule.VALIDATION_TYPE_OPTIONAL, attribute
				    .toString(), fieldValue));
		}

	    }

	}
	return validationRule;
    }

    private void checkValidationForParams() throws Exception {

	ValidationRule[] validationRules = new ValidationRule[validationRule
		.size()];
	validationRules = validationRule.toArray(validationRules);

	Validation.checkRequestParams(validationRules);

    }

    private boolean checkAttributeValueObjectMapsName(String name, String value)
	    throws Exception {

	for (AttributeValueJsonObject attribute : AttributeValueJsonObject
		.values()) {
	    if (attribute.toString().equals(name)) {
		try {
		    new JSONObject(value);
		    return true;
		} catch (JSONException ex) {
		    responseWrapper.setRequestError(constructRequestError(
			    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
			    "Attribute value for " + name
				    + " should be a JSON Object"));
		    if (AttributeValueJsonObject.ADDITIONALINFO.toString()
			    .equals(name)) {
			try {
			    new JSONArray(value);
			    return true;
			} catch (JSONException exc) {
			    responseWrapper
				    .setRequestError(constructRequestError(
					    SERVICEEXCEPTION,
					    ServiceError.INVALID_INPUT_VALUE,
					    "Attribute value for "
						    + name
						    + " should be a JSON Object/Array"));
			}
		    }
		    throw new Exception();
		}
	    }
	}
	return true;

    }

    private ValidationRule[] defineCommonValidationRules(String api,
	    String service, String name, String value) {

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
	    responseWrapper.setResponseMessage("Success");
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
	    LOG.error("###USER### Error in finding  which Table (ToObject) the attribute should go for this API "
		    + api.getAPIName());
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
		    "Given Attribute name " + attributeObj.getAttributeName()
			    + " is invalid for " + serviceObj.getServiceName()
			    + " Service Call"));
	    LOG.error("###USER### Given Attribute name "
		    + attributeObj.getAttributeName() + " is invalid for "
		    + serviceObj.getServiceName() + " Service Call");
	    throw new Exception();
	}
    }

    private void checkAttributeExist(String attribute) throws Exception {
	attributeObj = dao.getAttribute(attribute.toLowerCase());
	if (attributeObj == null) {
	    responseWrapper.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Given Attribute " + attribute + " is invalid"));
	    LOG.error("###USER### Error in Validation of Given Attribute "
		    + attribute + " is invalid");
	    throw new Exception();
	}
    }

    private void checkApiServiceExist(String service) throws Exception {
	serviceObj = dao.getServiceCall(apiObj.getId(), service.toLowerCase());
	if (serviceObj == null) {
	    responseWrapper.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Service " + service + " is Invalid for given API "
			    + apiObj.getAPIName()));
	    LOG.error("###USER### Error in Validation due to Service "
		    + service + " is Invalid for given API "
		    + apiObj.getAPIName());

	    throw new Exception();
	}
    }

    private void checkApiExist(String api) throws Exception {
	apiObj = dao.getAPIType(api.toLowerCase());
	if (apiObj == null) {
	    responseWrapper.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Given API " + api + " is invalid"));
	    LOG.error("###USER### Error in Validation of Given API " + api
		    + " is invalid");
	    throw new Exception();
	}
    }

    @Override
    protected void init(AttributeRequestWrapperDTO extendedRequestDTO)
	    throws Exception {
	requestWrapperDTO = extendedRequestDTO;
	responseWrapper = new AttributeInsertionResponseWrapper();
    }

}
