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
 package com.wso2telco.services.dep.sandbox.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wso2telco.core.mi.ConfigDTO;

public class SandboxDTO extends ConfigDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4016247712134961725L;
	@JsonProperty
	private static int requestIdentifierSize;

	@JsonProperty
	private static String behaveType;
	
	public static int getRequestIdentifierSize() {
		return requestIdentifierSize;
	}
	public void setRequestIdentifierSize(int requestIdentifierSize) {
		SandboxDTO.requestIdentifierSize = requestIdentifierSize;
	}

    public static String getBehaveType() {
        return behaveType;
    }

    public  void setBehaveType(String behaveType) {
        SandboxDTO.behaveType = behaveType;
    }
}
