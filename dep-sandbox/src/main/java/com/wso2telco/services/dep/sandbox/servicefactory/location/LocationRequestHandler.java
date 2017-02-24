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
import com.wso2telco.dep.oneapivalidation.service.impl.location.ValidateLocation;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.LocationDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.LocationRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.*;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.MessageType;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestResponseRequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import javax.ws.rs.core.Response.Status;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LocationRequestHandler  extends AbstractRequestHandler<LocationRequestWrapperDTO> implements RequestResponseRequestHandleable<LocationRequestWrapperDTO>  {
	
	final static String RETRIEVED = "Retrieved";
	final static String NOT_RETRIEVED="NotRetrieved";
	final static String SIMPLE_DATE_FORMAT= "yyyy-MM-dd HH:mm:ss";
	  LocationResponseWrapperDTO responseWrapperDTO =null;
	  LocationRequestWrapperDTO extendedRequestDTO=null;
	
	private  LocationDAO locationDao=null;
	{
		LOG = LogFactory.getLog(LocationRequestHandler.class);
		locationDao=  DaoFactory.getLocationDAO();
	}
	
	@Override
	protected Returnable process(LocationRequestWrapperDTO extendedRequestDTO) throws Exception {

		
        User user=extendedRequestDTO.getUser();
        APITypes apiTypes = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
        APIServiceCalls apiServiceCalls = dao.getServiceCall(apiTypes.getId(), ServiceName.Location.toString());
        //Temp status variable
        Locationparam locparam = locationDao.queryLocationParam(user.getId());

        try {
			if (locparam != null) {


				if (locparam.getLocationRetrieveStatus().equals(RETRIEVED)) {
					LOG.debug("Location retrieve status : " + RETRIEVED);

					locationDao.saveTransaction(extendedRequestDTO.getAddress(),
							Double.valueOf(extendedRequestDTO.getRequestedAccuracy()),
							"Retrieved", user);

					TerminalLocationList objTerminalLocationList = new TerminalLocationList();

					TerminalLocation objTerminalLocation = new TerminalLocation();
					objTerminalLocation.setAddress(extendedRequestDTO.getAddress());

					TerminalLocation.CurrentLocation objCurrentLocation = new TerminalLocation.CurrentLocation();
					objCurrentLocation.setAccuracy(Double.valueOf(extendedRequestDTO.getRequestedAccuracy()));
					objCurrentLocation.setAltitude(Double.parseDouble(locparam.getAltitude()));
					objCurrentLocation.setLatitude(Double.parseDouble(locparam.getLatitude()));
					objCurrentLocation.setLongitude(Double.parseDouble(locparam.getLongitude()));

					SimpleDateFormat dateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
					Date currentDate = new Date();
					objCurrentLocation.setTimestamp(dateFormat.format(currentDate));

					objTerminalLocation.setCurrentLocation(objCurrentLocation);
					objTerminalLocation.setLocationRetrievalStatus(RETRIEVED);

					objTerminalLocationList.setTerminalLocation(objTerminalLocation);


					responseWrapperDTO.setTerminalLocationList(objTerminalLocationList);
					responseWrapperDTO.setHttpStatus(Status.OK);

                    saveResponse(objTerminalLocation,extendedRequestDTO.getAddress(),apiServiceCalls,"1");

				} else if (locparam.getLocationRetrieveStatus().equals(NOT_RETRIEVED)) {

					LOG.debug("Location retrieve status :" + NOT_RETRIEVED);

					locationDao.saveTransaction(extendedRequestDTO.getAddress(),
							Double.valueOf(extendedRequestDTO.getRequestedAccuracy()),
							NOT_RETRIEVED, user);

					TerminalLocationList objTerminalLocationList = new TerminalLocationList();

					TerminalLocation objTerminalLocation = new TerminalLocation();
					objTerminalLocation.setAddress(extendedRequestDTO.getAddress());
					objTerminalLocation.setLocationRetrievalStatus("NotRetrieved");

					objTerminalLocationList.setTerminalLocation(objTerminalLocation);

					responseWrapperDTO.setTerminalLocationList(objTerminalLocationList);
					responseWrapperDTO.setHttpStatus(Status.OK);

				} else if (locparam.getLocationRetrieveStatus().equals("Error")) {

					LOG.debug("Location retrieve status : Error");
					responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", extendedRequestDTO.getAddress()));
					responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
				}
			} else {
				LOG.debug("Location parameters are empty");
				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", extendedRequestDTO.getAddress()));
				responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			}
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
		 ValidateLocation validator = new ValidateLocation();
		 String[] params ={wrapperDTO.getAddress(),wrapperDTO.getRequestedAccuracy()};
	     validator.validate(params);
		return false;
	}


	@Override
	protected Returnable getResponseDTO() {
		return responseWrapperDTO;
	}


	@Override
	protected List<String> getAddress() {
		List<String> address =new ArrayList<String>();
		address.add(extendedRequestDTO.getAddress());
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
	private void saveResponse(TerminalLocation terminalLocation,String endUserId, APIServiceCalls apiServiceCalls, String status) throws Exception {

		String jsonInString = null;
		Gson resp = new Gson();
		JsonElement je = new JsonParser().parse(resp.toJson(terminalLocation));
		JsonObject asJsonObject = je.getAsJsonObject();
		jsonInString = asJsonObject.toString();

       //setting messagelog responses
		MessageLog messageLog = new MessageLog();
		messageLog = new MessageLog();
		messageLog.setRequest(jsonInString);
		messageLog.setStatus(status);
		messageLog.setType(MessageType.Response.getValue());
		messageLog.setServicenameid(apiServiceCalls.getApiServiceCallId());
		messageLog.setUserid(extendedRequestDTO.getUser().getId());
		messageLog.setReference("msisdn");
		messageLog.setValue(endUserId);
		messageLog.setMessageTimestamp(new Date());

		loggingDAO.saveMessageLog(messageLog);
	}

}
