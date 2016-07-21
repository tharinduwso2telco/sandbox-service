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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.model.SessionDTO;
import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

public class SessionHolder {
	
	private Logger log = LoggerFactory.getLogger(SessionHolder.class);
	private List<SessionDTO> tokenSessions;
	private  WhoDTO whoDTO;
	private  TokenDTO tokenDTO;
	
	
 
	private SessionHolder(final  WhoDTO whoDTO ,final TokenDTO tokenDTO){
		log.debug("New token Session created for :"+whoDTO);
		this.whoDTO =whoDTO;
		this.tokenDTO = tokenDTO;
		tokenSessions =new ArrayList<SessionDTO>();
	/*	tokenSessions =CacheBuilder.newBuilder()
				.removalListener(new SessionRemovalListener ())
				.expireAfterWrite(whoDTO.getDefaultConnectionRestTime(), TimeUnit.MILLISECONDS)
				.build();*/
	}
	
	//token owner id and the default session expire time is mandatory to create session instance
	public static SessionHolder createInstance(final WhoDTO whoDTO,final TokenDTO tokenDTO) throws TokenException{
		if(whoDTO==null||whoDTO.getOwnerId()== null){
			throw new TokenException(TokenException.TokenError.NO_VALID_WHO);
		}
		
		if(whoDTO.getDefaultConnectionRestTime()==0){
			throw new TokenException(TokenException.TokenError.NO_TOKENRESET_TIME);
		}
		return new SessionHolder(  whoDTO,tokenDTO);
	}
	
	
	
	// keep a token usage entry 
	// once the session added to the session pool it will remove from the session 
	public synchronized void  acquireSession(){
		log.debug(" acquire new Session "+tokenDTO.getId());
		
		SessionDTO dto = new SessionDTO();
		dto.setOwnerID(whoDTO.getOwnerId());;
		dto.setSessionId(tokenDTO.getId() +":"+String.valueOf(System.currentTimeMillis()));
		dto.setTokenDTO(tokenDTO);
		dto.setCreatedTimeInMl(System.currentTimeMillis());
		synchronized (tokenSessions) {
			log.debug("Token"+ tokenDTO+" acuire from the pool ");
			tokenSessions.add( dto);
		}
		
		Timer timer = new Timer();
		// Schedule the re - generate process
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// remove the token
				log.debug("Token remove from the session "+dto);
				synchronized (tokenSessions) {
					tokenSessions.remove(dto);
					
				}
			}

		}, whoDTO.getDefaultConnectionRestTime());
	
		
	}
	//check the given token in use or not
	public boolean isInUse()throws TokenException{
		log.debug("check for usability of :"+tokenDTO);
		boolean isInuse =true;
		//If token pool size is 0 then it will return not in use
		if(tokenSessions.size()==0){
			isInuse= Boolean.FALSE;
		}
		return isInuse;
	}
}
