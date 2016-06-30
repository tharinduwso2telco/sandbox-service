/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.dep.tpservice.model;

import java.io.Serializable;

public class SessionDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6459528347307197532L;

	private String ownerID;
	private String sessionId;
	private TokenDTO tokenDTO;
	
	
	
	public TokenDTO getTokenDTO() {
		return tokenDTO;
	}

	public void setTokenDTO(TokenDTO tokenDTO) {
		this.tokenDTO = tokenDTO;
	}

	private long createdTimeInMl;

	public String getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public long getCreatedTimeInMl() {
		return createdTimeInMl;
	}

	public void setCreatedTimeInMl(long createdTimeInMl) {
		this.createdTimeInMl = createdTimeInMl;
	}

	@Override
	public String toString() {
		return "SessionDTO [ownerID=" + ownerID + ", sessionId=" + sessionId + ", tokenDTO=" + tokenDTO
				+ ", createdTimeInMl=" + createdTimeInMl + "]";
	} 

	
	
}
