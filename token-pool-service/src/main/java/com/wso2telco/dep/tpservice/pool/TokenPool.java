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

package com.wso2telco.dep.tpservice.pool;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.conf.ConfigReader;
import com.wso2telco.dep.tpservice.model.ConfigDTO;
import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.pool.session.SessionHolder;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

public class TokenPool {
	Logger log = LoggerFactory.getLogger(TokenPool.class);

	private ConcurrentHashMap<String, List<TokenDTO>> tokenPool = new ConcurrentHashMap<String, List<TokenDTO>>();
	private static TokenPool instance;

	public synchronized static TokenPool getInstance() {

		if (instance == null) {
			instance = new TokenPool();

		}
		return instance;
	}

	public void removeToken(final WhoDTO whoDTO, final TokenDTO token) {

		List<TokenDTO> dtos = tokenPool.get(whoDTO.getOwnerId());
		synchronized (dtos) {
			dtos.remove(token);
		}

		log.info(" Token removed form the pool " + token + " ,Owner :" + whoDTO);
	}

	public void addToPool(final TokenDTO tokenDTO, final WhoDTO whoDTO) {
		log.info("New Token Added to the pool :" + tokenDTO);
		if (tokenPool.containsKey(tokenDTO.getId())) {
			List<TokenDTO> dtos = tokenPool.get(tokenDTO.getId());
			synchronized (dtos) {
				dtos.add(tokenDTO);

			}
		}
	}

	public TokenDTO accqureToken(final String ownerID) throws InterruptedException ,TokenException{
		int waitattempt =0;
		/**
		 * if the owner haven't any token at the pool wait for predefined interval 
		 * Specified at the configuration (YML) file.
		 */
		while (!tokenPool.containsKey(ownerID) ) {
			ConfigDTO configDTO = ConfigReader.getInstance().getConfigDTO();
			log.debug("No Token found for ownerID :"+ownerID);
			
			Thread.sleep(configDTO.getWaitingTimeForToken());
			
			/**
			 * sleep for pre defined attempts. if still fail throws exception
			 */
			if(configDTO.getMaxNumberOfAttemptForToken()<=waitattempt){
				log.debug("Still no valid token pool initialized for ownerID :"+ownerID);
				throw new TokenException(TokenException.TokenError.TOKENPOOL_EMPTY);
			}
			
		}
	
		TokenDTO validTokenDTO = waitUntilPoolfill(waitattempt++,ownerID);
		
		
		if(validTokenDTO==null){
			throw new TokenException(TokenException.TokenError.TOKENPOOL_EMPTY);
		}
		return validTokenDTO;

	}
	/**
	 * 
	 * @param waitattempt
	 * @param ownerID
	 * @return
	 * @throws InterruptedException
	 * @throws TokenException
	 */
	private TokenDTO waitUntilPoolfill(int waitattempt,final String ownerID)throws InterruptedException ,TokenException{
		List<TokenDTO> tokenList = tokenPool.get(ownerID);
		synchronized (tokenList) {
			for (TokenDTO tokenDTO : tokenList) {
				if(tokenDTO.isValid()){
					SessionHolder.getInstance().acquireSession(tokenDTO.getId());
					log.info("Valid token found "+tokenDTO);
					return tokenDTO;
				}
				
			}
			
			
		}
		log.info("No valid token found for "+ownerID +" ,Look up attempt :"+waitattempt);
		ConfigDTO configDTO = ConfigReader.getInstance().getConfigDTO();
		Thread.sleep(configDTO.getWaitingTimeForToken());
		
		/**
		 * sleep for pre defined attempts. if still fail throws exception
		 */
		if(configDTO.getMaxNumberOfAttemptForToken()<=waitattempt){
			throw new TokenException(TokenException.TokenError.TOKENPOOL_EMPTY);
		}
		//recursively lookup for valid token ,
		//this will continue until valid token found or defined attempt pass
		waitUntilPoolfill(waitattempt++,  ownerID);
		
		log.error("","Token pool empty");
		throw new TokenException(TokenException.TokenError.TOKENPOOL_EMPTY);
		
	
	}
	
}
