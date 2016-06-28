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

package com.wso2telco.dep.tpservice.pool.session;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.wso2telco.dep.tpservice.model.SessionDTO;

public class SessionHolder {
	
	private Logger log = LoggerFactory.getLogger(SessionHolder.class);
	private Cache<String,SessionDTO> tokenSessions;
	private String ownerID;
	
	private static SessionHolder instance ;
	
	/**
	 * cache map is initialized with expiry time defined at the tpxwho . 
	 * @param ownerID
	 * @param sesionExpioryTime
	 */
	private SessionHolder(final String ownerID,final int sesionExpioryTime){
		tokenSessions =CacheBuilder.newBuilder()
									.removalListener(new SessionRemovalListener ())
									.expireAfterWrite(sesionExpioryTime, TimeUnit.MILLISECONDS)
									.build();
		this.ownerID =ownerID;
	}
	
	public static void init(final String ownerID,final int sesionExpioryTime){
		instance = new SessionHolder(ownerID,sesionExpioryTime);
	}
	
	public static SessionHolder getInstance(){
		return instance;
	}
	
	/**
	 * keep a token usage entry 
	 * once the session added to the session pool it will remove from the session 
	 * @param tokenId
	 */
	public synchronized void  acquireSession(final String tokenId){
		SessionDTO dto = new SessionDTO();
		dto.setOwnerID(tokenId);;
		dto.setSessionId(tokenId +":"+String.valueOf(System.currentTimeMillis()));
		dto.setTokenId(tokenId.trim());
		dto.setCreatedTimeInMl(System.currentTimeMillis());
		tokenSessions.put(tokenId.trim(), dto);
		
	}
}
