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
package com.wso2telco.services.dep.sandbox.servicefactory.location;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.LocationDAO;
import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.LocationRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.*;
import com.wso2telco.services.dep.sandbox.servicefactory.*;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class LocationRequestHandler  extends AbstractRequestHandler<LocationRequestWrapperDTO> implements RequestResponseRequestHandleable<LocationRequestWrapperDTO>  {
	
	final static String RETRIEVED = "Retrieved";
	final static String NOT_RETRIEVED="NotRetrieved";
	final static String SIMPLE_DATE_FORMAT= "yyyy-MM-dd HH:mm:ss";
	  LocationResponseWrapperDTO responseWrapperDTO =null;
	  LocationRequestWrapperDTO extendedRequestDTO=null;
    private NumberDAO numberDAO;
	
	private  LocationDAO locationDao=null;
	{
		LOG = LogFactory.getLog(LocationRequestHandler.class);
		locationDao=  DaoFactory.getLocationDAO();
        numberDAO = DaoFactory.getNumberDAO();
    }
	
	@Override
	protected Returnable process(LocationRequestWrapperDTO extendedRequestDTO) throws Exception {

        if (responseWrapperDTO.getRequestError() != null) {
            responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
            return responseWrapperDTO;
        }
        User user=extendedRequestDTO.getUser();
        APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
        APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), ServiceName.Location.toString());
        ManageNumber manageNumber;

        try {

            TerminalLocationList objTerminalLocationList = new TerminalLocationList();
            List<TerminalLocation> terminalLocationArrayList = new ArrayList<>();
            List address = Arrays.asList(extendedRequestDTO.getAddress().split(","));

            for (int i = 0; i < address.size(); i++) {

                manageNumber = numberDAO.getNumber(getLastMobileNumber(address.get(i).toString()), user.getUserName());

                if (manageNumber != null) {

                    if (manageNumber.getLocationRetrieveStatus().equals(RETRIEVED)) {
                        LOG.debug("Location retrieve status : " + RETRIEVED);

                        TerminalLocation objTerminalLocation = new TerminalLocation();
                        objTerminalLocation.setAddress(address.get(i).toString());

                        TerminalLocation.CurrentLocation objCurrentLocation = new TerminalLocation.CurrentLocation();
                        objCurrentLocation.setAccuracy(Double.valueOf(extendedRequestDTO.getRequestedAccuracy()));
                        objCurrentLocation.setAltitude(Double.parseDouble(manageNumber.getAltitude()));
                        objCurrentLocation.setLatitude(Double.parseDouble(manageNumber.getLatitude()));
                        objCurrentLocation.setLongitude(Double.parseDouble(manageNumber.getLongitude()));

                        SimpleDateFormat dateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
                        Date currentDate = new Date();
                        objCurrentLocation.setTimestamp(dateFormat.format(currentDate));

                        objTerminalLocation.setCurrentLocation(objCurrentLocation);
                        objTerminalLocation.setLocationRetrievalStatus(RETRIEVED);

                        terminalLocationArrayList.add(objTerminalLocation);


                    } else if (manageNumber.getLocationRetrieveStatus().equals(NOT_RETRIEVED)) {

                        LOG.debug("Location retrieve status :" + NOT_RETRIEVED);

                        locationDao.saveTransaction(extendedRequestDTO.getAddress(),
                                Double.valueOf(extendedRequestDTO.getRequestedAccuracy()),
                                NOT_RETRIEVED, user);

                        TerminalLocation objTerminalLocation = new TerminalLocation();
                        objTerminalLocation.setAddress(address.get(i).toString());
                        objTerminalLocation.setLocationRetrievalStatus("NotRetrieved");
                        terminalLocationArrayList.add(objTerminalLocation);
                        responseWrapperDTO.setTerminalLocationList(objTerminalLocationList);
                        responseWrapperDTO.setHttpStatus(Status.OK);

                    }

                    if (manageNumber.getLocationRetrieveStatus().equals("Error")) {

                        LOG.debug("Location retrieve status : Error");
                        responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0002",
                                "Invalid input value for message part %1", extendedRequestDTO.getAddress()));
                        responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
                        return responseWrapperDTO;
                    }

                }
            }
            objTerminalLocationList.setTerminalLocation(terminalLocationArrayList);

            responseWrapperDTO.setTerminalLocationList(objTerminalLocationList);
            responseWrapperDTO.setHttpStatus(Status.OK);
            saveResponse(objTerminalLocationList, extendedRequestDTO.getAddress(), apiServiceCalls,
                    MessageProcessStatus.Success);


		}catch (Exception ex){
			LOG.debug("Error occurs in location retriever");
			responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", extendedRequestDTO.getAddress()));
			responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			throw ex;
		}

		return responseWrapperDTO;
	}


	@Override
	protected boolean validate(LocationRequestWrapperDTO wrapperDTO) throws Exception {
        List<ValidationRule> validationRulesList = new ArrayList<>();
        List address = Arrays.asList(extendedRequestDTO.getAddress().split(","));

        try {

            for (Object addres : address) {
                validationRulesList.add(
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "Address",
                                addres.toString()));
            }
            validationRulesList.add(
                    new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GE_ZERO, "Accuracy",extendedRequestDTO.getRequestedAccuracy()));
            ValidationRule[] validationRules = new ValidationRule[validationRulesList.size()];
            validationRules = validationRulesList.toArray(validationRules);
            Validation.checkRequestParams(validationRules);

        } catch (CustomException ex) {
            LOG.error("###LOCATION### Error in Validations. ", ex);
            responseWrapperDTO.setRequestError(
                    constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
            return false;
        }
        return true;
	}


	@Override
	protected Returnable getResponseDTO() {
		return responseWrapperDTO;
	}


	@Override
	protected List<String> getAddress() {
		List<String> address =new ArrayList<String>();
        address = Arrays.asList(extendedRequestDTO.getAddress().split(","));
		return address;
	}


	@Override
	protected void init(LocationRequestWrapperDTO extendedRequestDTO) throws Exception {
		responseWrapperDTO  = new LocationResponseWrapperDTO();
		this.extendedRequestDTO =extendedRequestDTO;
	}

    @Override
    public String getApiServiceCalls() {
        try {
            return ServiceName.Location.toString();
        }catch (Exception ex){
            return null;
        }
    }

	@Override
	public String getJosonString(LocationRequestWrapperDTO requestDTO) {
		JSONObject obj = new JSONObject();
		obj.put("msisdn",requestDTO.getAddress().toString());
		return obj.toString();
	}

	@Override
	public String getnumber(LocationRequestWrapperDTO requestDTO) {
		return requestDTO.getAddress();
	}

	/**
	 * This method is use to save response.
	 * @param terminalLocation location object.
	 * @param endUserId mobile number.
	 * @param apiServiceCalls
	 * @param status
	 * @throws Exception
	 */
	private void saveResponse(TerminalLocationList terminalLocation,String endUserId, APIServiceCalls apiServiceCalls, MessageProcessStatus status) throws Exception {

		String jsonInString = null;
		Gson resp = new Gson();
		JsonElement je = new JsonParser().parse(resp.toJson(terminalLocation));
		JsonObject asJsonObject = je.getAsJsonObject();
		jsonInString = asJsonObject.toString();

       //setting messagelog responses
		MessageLog messageLog = new MessageLog();
		messageLog = new MessageLog();
		messageLog.setRequest(jsonInString);
		messageLog.setStatus(status.getValue());
		messageLog.setType(MessageType.Response.getValue());
		messageLog.setServicenameid(apiServiceCalls.getApiServiceCallId());
		messageLog.setUserid(extendedRequestDTO.getUser().getId());
		messageLog.setReference("msisdn");
		messageLog.setValue(getLastMobileNumber(endUserId));
		messageLog.setMessageTimestamp(new Date());

		loggingDAO.saveMessageLog(messageLog);
	}

}
