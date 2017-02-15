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
