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
import com.wso2telco.services.dep.sandbox.dao.model.custom.GetProfileConfigRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.GetProfileConfigRequestBean.AdditionalInfo;
import com.wso2telco.services.dep.sandbox.dao.model.custom.GetProfileConfigRequestBean.Address;
import com.wso2telco.services.dep.sandbox.dao.model.custom.GetProfileConfigRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.AddressIgnorerable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.APIAttributeToObjectMaps;
import com.wso2telco.services.dep.sandbox.util.AttributeEnum;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.AdditionalInfoField;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.AddressField;
import com.wso2telco.services.dep.sandbox.util.AttributeMetaInfo.Profile;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;

public class GetProfileConfigHandler extends
	AbstractRequestHandler<GetProfileConfigRequestWrapperDTO> implements
	AddressIgnorerable {

    private GetProfileConfigRequestWrapperDTO requestWrapperDTO;
    private GetProfileConfigResponseWrapper responseWrapperDTO;

    private final String MANDATORY = "Mandatory";

    private List<ValidationRule> validationRule = new ArrayList<ValidationRule>();
    private Map<String, String> valuesInRequset = new HashMap<String, String>();
    private Map<AttributeDistribution, String> attributeValueDistribution = new HashMap<AttributeDistribution, String>();

    private boolean isJsonObject = false;

    {
	LOG = LogFactory.getLog(GetProfileConfigHandler.class);
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
    protected boolean validate(GetProfileConfigRequestWrapperDTO wrapperDTO)
	    throws Exception {
	if (wrapperDTO.getRequestObject() == null) {
	    responseWrapperDTO.setRequestError(constructRequestError(
		    SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
		    "Empty or Invalid Request Body"));
	    responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
	    return false;
	}

	GetProfileConfigRequestBean requestBean = wrapperDTO.getRequestObject();

	for (Profile eachProfile : Profile.values()) {

	    switch (eachProfile) {

	    case address:
		Address address = requestBean.getAddress();
		if (address != null) {
		    isJsonObject = true;
		    checkEachJsonParameters(AddressField.values(), address);
		    valuesInRequset.put(Profile.address.toString(),
			    new JSONObject(address).toString());
		}
		break;

	    case additionalInfo:
		List<AdditionalInfo> info = requestBean.getAdditionalInfo();
		if (info != null) {
		    isJsonObject = true;
		    splitListToEachJson(AdditionalInfoField.values(), info);
		    valuesInRequset.put(Profile.additionalInfo.toString(),
			    new JSONArray(info).toString());
		}
		break;

	    default:
	    isJsonObject = false;
		checkEachJsonParametersValue(eachProfile, requestBean);

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

    private void splitListToEachJson(AdditionalInfoField[] values,
	    List<AdditionalInfo> additionalInfo) throws Exception {
	for (AdditionalInfo info : additionalInfo) {
	    checkEachJsonParameters(AdditionalInfoField.values(), info);
	}
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

	String jsonValue = (String) jsonMethod.invoke(parentObj);

	defineRules(eachField, CommonUtil.getNullOrTrimmedValue(jsonValue));

	if (!isJsonObject
		&& CommonUtil.getNullOrTrimmedValue(jsonValue) != null)
	    valuesInRequset.put(eachField.toString(), jsonValue);

    }

    private void defineRules(AttributeEnum eachProfile, String val) {

	if (eachProfile.getFieldType().equals(MANDATORY)) {
	    validationRule.add(new ValidationRule(
		    ValidationRule.VALIDATION_TYPE_MANDATORY, eachProfile
			    .toString(), val));

	} else {
	    validationRule.add(new ValidationRule(
		    ValidationRule.VALIDATION_TYPE_OPTIONAL, eachProfile
			    .toString(), val));
	}

    }

    @Override
    protected Returnable process(
	    GetProfileConfigRequestWrapperDTO extendedRequestDTO)
	    throws Exception {

	if (responseWrapperDTO.getRequestError() != null) {
	    responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
	    return responseWrapperDTO;
	}

	APITypes apiType = dao.getAPIType(RequestType.CUSTOMERINFO.toString()
		.toLowerCase());

	APIServiceCalls serviceType = dao.getServiceCall(apiType.getId(),
		ServiceName.GetProfile.toString());

	List<AttributeDistribution> availableDistribution = dao
		.getAttributeDistributionByServiceCall(apiType.getId(),
			serviceType.getApiServiceCallId());

	for (Map.Entry<String, String> eachMapValue : valuesInRequset
		.entrySet()) {
	    for (AttributeDistribution eachDistribution : availableDistribution) {
		if (eachDistribution.getAttribute().getAttributeName()
			.toLowerCase()
			.equals(eachMapValue.getKey().toLowerCase())) {
		    attributeValueDistribution.put(eachDistribution,
			    eachMapValue.getValue());
		    break;
		}
	    }
	}

	AttributeValues valueObj = new AttributeValues();
	List<AttributeValues> attributeValues = new ArrayList<AttributeValues>();

	for (Map.Entry<AttributeDistribution, String> eachValuedDistribution : attributeValueDistribution
		.entrySet()) {
	    valueObj = dao.getAttributeValue(eachValuedDistribution.getKey());

	    if (valueObj != null) {

		valueObj.setValue(eachValuedDistribution.getValue());
		attributeValues.add(valueObj);

	    } else {

		valueObj = new AttributeValues();
		valueObj.setAttributedid(eachValuedDistribution.getKey());
		valueObj.setOwnerdid(extendedRequestDTO.getUser().getId());
		valueObj.setTobject(getToObject(eachValuedDistribution.getKey()
			.getAPIServiceCall().getAPIType()));
		valueObj.setValue(eachValuedDistribution.getValue());
		attributeValues.add(valueObj);

	    }
	}

	dao.saveAttributeValue(attributeValues);
	responseWrapperDTO
		.setStatus("Successfully Updated attribute for getProfile!!");
	responseWrapperDTO.setHttpStatus(Response.Status.CREATED);

	return responseWrapperDTO;
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
    protected void init(GetProfileConfigRequestWrapperDTO extendedRequestDTO)
	    throws Exception {
	requestWrapperDTO = extendedRequestDTO;
	responseWrapperDTO = new GetProfileConfigResponseWrapper();
    }

}
