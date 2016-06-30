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

package com.wso2telco.dep.tpservice.pool.alltimefirst;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.wso2telco.dep.tpservice.model.SessionDTO;
import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

public class SessionHolder {
	
	private Logger log = LoggerFactory.getLogger(SessionHolder.class);
	private Cache<String,SessionDTO> tokenSessions;
	private  WhoDTO whoDTO;
	
	
	/**
	 * cache map is initialized with expiry time defined at the tpxwho . 
	 * @param ownerID
	 * @param sesionExpioryTime
	 */
	private SessionHolder(final  WhoDTO whoDTO ){
		log.debug("New token Session created for :"+whoDTO);
		this.whoDTO =whoDTO;
		tokenSessions =CacheBuilder.newBuilder()
				.removalListener(new SessionRemovalListener ())
				.expireAfterWrite(whoDTO.getDefaultConnectionRestTime(), TimeUnit.MILLISECONDS)
				.build();
	}
	/**
	 * token owner id and the default session expire time is mandatory to create session instance
	 * @param whoDTO
	 * @return
	 * @throws TokenException
	 */
	public static SessionHolder createInstance(final WhoDTO whoDTO) throws TokenException{
		if(whoDTO==null||whoDTO.getOwnerId()== null){
			throw new TokenException(TokenException.TokenError.NO_VALID_WHO);
		}
		
		if(whoDTO.getDefaultConnectionRestTime()==0){
			throw new TokenException(TokenException.TokenError.NO_TOKENRESET_TIME);
		}
		return new SessionHolder(  whoDTO);
	}
	
	
	/**
	 * keep a token usage entry 
	 * once the session added to the session pool it will remove from the session 
	 * @param tokenId
	 */
	public synchronized void  acquireSession(final TokenDTO tokenDTO){
		log.debug(" acquire new Session "+tokenDTO.getId());
		
		SessionDTO dto = new SessionDTO();
		dto.setOwnerID(whoDTO.getOwnerId());;
		dto.setSessionId(tokenDTO.getId() +":"+String.valueOf(System.currentTimeMillis()));
		dto.setTokenDTO(tokenDTO);
		dto.setCreatedTimeInMl(System.currentTimeMillis());
		tokenSessions.put(dto.getSessionId(), dto);
		
	}
	
	public boolean isInUse(final TokenDTO tokenDTO)throws TokenException{
		log.debug("check for usability of :"+tokenDTO);
		boolean isInuse =false;
		//If token pool size is 0 then it will return not in use
		if(tokenSessions.size()==0){
			isInuse= Boolean.FALSE;
		}
		return isInuse;
	}
}
