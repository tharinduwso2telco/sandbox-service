package com.wso2telco.services.dep.sandbox.servicefactory.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;
import com.wso2telco.services.dep.sandbox.util.RequestError;

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


	@JsonInclude(value=Include.NON_NULL)
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
