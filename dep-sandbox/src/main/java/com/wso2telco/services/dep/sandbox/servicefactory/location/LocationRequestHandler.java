package com.wso2telco.services.dep.sandbox.servicefactory.location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.dep.oneapivalidation.service.impl.location.ValidateLocation;
import com.wso2telco.services.dep.sandbox.dao.LocationDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.LocationRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Locationparam;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;


class LocationRequestHandler  extends AbstractRequestHandler<LocationRequestWrapperDTO>{
	
	final static String RETRIEVED = "Retrieved";
	final static String NOT_RETRIEVED="NotRetrieved";
	final static String SIMPLE_DATE_FORMAT= "yyyy-MM-dd HH:mm:ss";
	  LocationResponseWrapperDTO responseWrapperDTO =null;
	  LocationRequestWrapperDTO extendedRequestDTO=null;
	
	private LocationDAO locationDao=null;
	{
		LOG = LogFactory.getLog(LocationRequestHandler.class);
		locationDao=  new LocationDAO();
		dao =locationDao;
	}
	
	@Override
	protected Returnable process(LocationRequestWrapperDTO extendedRequestDTO) throws Exception {

		
        User user=extendedRequestDTO.getUser();
       
        //Temp status variable
        Locationparam locparam = locationDao.queryLocationParam(user.getId());

        if (locparam != null) {
        	
        	
            if (locparam.getLocationRetrieveStatus().equals(RETRIEVED)) {
            	LOG.debug("Location retrieve status : "+ RETRIEVED);
            	
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

            } else if (locparam.getLocationRetrieveStatus().equals(NOT_RETRIEVED)) {
            	
                LOG.debug("Location retrieve status :"+NOT_RETRIEVED);
                
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
            responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION,"SVC0002", "Invalid input value for message part %1",extendedRequestDTO.getAddress()));
            responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
        }
    
        /////////////
        
        
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

}
