package com.wso2telco.services.dep.sandbox.servicefactory.location;

import com.wso2telco.dep.oneapivalidation.exceptions.RequestError;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

class LocationResponseWrapperDTO extends AbstractReturnWrapperDTO {

	private  TerminalLocationList terminalLocationList ;
	private String location;
	
	
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


	public TerminalLocationList getTerminalLocationList() {
		return terminalLocationList;
	}

	public void setTerminalLocationList(TerminalLocationList terminalLocationList) {
		this.terminalLocationList = terminalLocationList;
	}

	@Override
	public Object getResponse() {
		LocationResponse response= new LocationResponse(getRequestError(),terminalLocationList);
		return response;
	}



	class LocationResponse{
		
		private RequestError requestError;
		private TerminalLocationList terminalLocationList ;
		
		LocationResponse(RequestError requestError,TerminalLocationList terminalLocationList ){
			this.requestError=requestError;
			this.terminalLocationList=terminalLocationList;
		}
		
		public RequestError getRequestError() {
			return requestError;
		}
		public void setRequestError(RequestError requestError) {
			this.requestError = requestError;
		}
		public TerminalLocationList getTerminalLocationList() {
			return terminalLocationList;
		}
		public void setTerminalLocationList(TerminalLocationList terminalLocationList) {
			this.terminalLocationList = terminalLocationList;
		}
		
	}
	
}
