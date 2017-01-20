/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.services.dep.sandbox.servicefactory.customerinfoConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CommonSuccessResponse;
import com.wso2telco.services.dep.sandbox.dao.model.custom.GetAttributeConfigRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.GetAttributeConfigRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.GetProfileConfigRequestBean.Address;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.service.addedattrib.AttributeService;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.AddressIgnorerable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.APIAttributeToObjectMaps;
import com.wso2telco.services.dep.sandbox.util.AttributeEnum;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.AccountField;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.AddressField;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.Attribute;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.BasicField;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.BillingField;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.IdentificationField;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.Profile;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import com.wso2telco.services.dep.sandbox.util.TableName;

public class GetAttributeConfigHandler extends
	AbstractRequestHandler<GetAttributeConfigRequestWrapperDTO> implements
	AddressIgnorerable {

    private GetAttributeConfigRequestWrapperDTO requestWrapperDTO;
    private GetAttributeConfigResponseWrapper responseWrapperDTO;

    private final String MANDATORY = "Mandatory";

    List<ValidationRule> validationRule = new ArrayList<ValidationRule>();
    Map<String, String> valuesInRequset = new HashMap<String, String>();
    Map<AttributeDistribution, String> attributeValueDistribution = new HashMap<AttributeDistribution, String>();

    {
	LOG = LogFactory.getLog(GetAttributeConfigHandler.class);
	dao = DaoFactory.getGenaricDAO();
    }

    @Override
    protected Returnable getResponseDTO() {
	return responseWrapperDTO;
    }

    @Override
    protected List<String> getAddress() {
	return null;
    }

    @Override
    protected boolean validate(GetAttributeConfigRequestWrapperDTO wrapperDTO)
	    throws Exception {
	if (wrapperDTO.getRequestObject() == null) {
	    responseWrapperDTO.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Empty or Invalid Request Body"));
	    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
	    return false;
	}

	GetAttributeConfigRequestBean requestBean = wrapperDTO
		.getRequestObject();

	for (Attribute eachAttribute : Attribute.values()) {

	    String beanMethodName = eachAttribute.toString().toUpperCase()
		    .substring(0, 1)
		    + eachAttribute.toString().substring(1);

	    Method beanMethod = GetAttributeConfigRequestBean.class
		    .getDeclaredMethod("get" + beanMethodName);

	    Object Jsonvalue = beanMethod.invoke(requestBean);

	    if (Jsonvalue != null) {

		valuesInRequset.put(eachAttribute.toString(), new JSONObject(
			Jsonvalue).toString());

		switch (eachAttribute) {

		case basic:
		    checkEachJsonParameters(BasicField.values(), Jsonvalue);
		    break;

		case account:
		    checkEachJsonParameters(AccountField.values(), Jsonvalue);
		    break;

		case billing:
		    checkEachJsonParameters(BillingField.values(), Jsonvalue);
		    break;

		case identification:
		    checkEachJsonParameters(IdentificationField.values(),
			    Jsonvalue);
		    break;

		}

	    }

	}

	ValidationRule[] validationRules = new ValidationRule[validationRule
		.size()];

	validationRules = validationRule.toArray(validationRules);

	try {

	    Validation.checkRequestParams(validationRules);

	} catch (CustomException ex) {

	    LOG.error("###CUSTOMERINFOCONFIG### Error in Validation of Mandotary/Optional values : "
		    + ex);

	    responseWrapperDTO.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(),
		    ex.getErrvar()[0]));

	    responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);

	    return false;

	} catch (Exception ex) {

	    LOG.error("###CUSTOMERINFOCONFIG### Error in Validation of doue to Non-existing data "
		    + ex);
	    return false;
	}

	return true;
    }

    private void checkEachJsonParameters(AttributeEnum[] totalFieldValues,
	    Object parsedJson) throws Exception {
	for (AttributeEnum eachField : totalFieldValues) {
	    checkEachJsonParametersValue(eachField, parsedJson);
	}
    }

    private void checkEachJsonParametersValue(AttributeEnum eachField,
	    Object parentObj) throws Exception {

	String jsonMethodName = eachField.toString().toUpperCase()
		.substring(0, 1)
		+ eachField.toString().substring(1);

	Method jsonMethod = parentObj.getClass().getDeclaredMethod(
		"get" + jsonMethodName);

	Object jsonValue = jsonMethod.invoke(parentObj);

	if (jsonValue instanceof Address) {
	    checkEachJsonParameters(AddressField.values(), jsonValue);
	} else {
	    defineRules(eachField,
		    CommonUtil.getNullOrTrimmedValue((String) jsonValue));
	}

    }

    private void defineRules(AttributeEnum eachAttribute, String val) {

	if (eachAttribute.getFieldType().equals(MANDATORY)) {
	    validationRule.add(new ValidationRule(
		    ValidationRule.VALIDATION_TYPE_MANDATORY, eachAttribute
			    .toString(), val));

	} else {
	    validationRule.add(new ValidationRule(
		    ValidationRule.VALIDATION_TYPE_OPTIONAL, eachAttribute
			    .toString(), val));
	}

    }

    @Override
    protected Returnable process(
	    GetAttributeConfigRequestWrapperDTO extendedRequestDTO)
	    throws Exception {

	if (responseWrapperDTO.getRequestError() != null) {
	    responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
	    return responseWrapperDTO;
	}

	final Map<String,String> attributeVsValueMap = populateAttributeValues(extendedRequestDTO.getRequestObject());
	
	AttributeService attribService = new AttributeService();
	attribService.saveOrUpdate(attributeVsValueMap, 
								ServiceName.GetAttribute,
								RequestType.CUSTOMERINFO, 
								TableName.USER.toString(),
								extendedRequestDTO.getUser().getId());

	CommonSuccessResponse success = new CommonSuccessResponse();
	success.setStatus("Successfully Updated user attribute!!!");
	responseWrapperDTO.setMessage(success);
	responseWrapperDTO.setHttpStatus(Response.Status.OK);

	return responseWrapperDTO;
    }

	private Map<String, String> populateAttributeValues(
			GetAttributeConfigRequestBean requestObject) {
		Map<String, String> valueMap = new HashMap<String, String>();

		valueMap.put(Attribute.basic.toString(), new JSONObject(requestObject.getBasic())
				.toString());
		valueMap.put(Attribute.billing.toString(), new JSONObject(requestObject.getBilling())
				.toString());
		valueMap.put(Attribute.account.toString(), new JSONObject(requestObject.getAccount())
				.toString());
		valueMap.put(Attribute.identification.toString(),new JSONObject( requestObject
				.getIdentification()).toString());
		return valueMap;
	}

	private String getToObject(APITypes api) throws Exception {

	String apiEnum = api.getAPIName().toUpperCase();
	String toObj = "";

	try {
	    toObj = APIAttributeToObjectMaps.valueOf(apiEnum).getTableName();
	} catch (Exception ex) {
	    LOG.error("###CUSTOMERINFOCONFIG### Error in finding  which Table (ToObject) the attribute should go for this API "
		    + api.getAPIName());
	    throw new Exception();
	}
	return toObj;
    }

    @Override
    protected void init(GetAttributeConfigRequestWrapperDTO extendedRequestDTO)
	    throws Exception {
	requestWrapperDTO = extendedRequestDTO;
	responseWrapperDTO = new GetAttributeConfigResponseWrapper();

    }

}
