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

import org.slf4j.Logger;

import com.wso2telco.dep.tpservice.conf.ConfigReader;
import com.wso2telco.dep.tpservice.manager.TokenManager;
import com.wso2telco.dep.tpservice.model.ConfigDTO;
import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.pool.TokenPool;
import com.wso2telco.dep.tpservice.pool.TokenPoolImplimentable;
import com.wso2telco.dep.tpservice.util.exception.GenaralError;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

abstract class AbstractTokenPool implements TokenPool,TokenPoolImplimentable {
	protected Logger log;
	protected ConfigReader configReader;
	protected WhoDTO whoDTO;
	protected SessionHolder sessionHolder;
	protected TokenManager tokenManager;
	
	protected AbstractTokenPool(final WhoDTO whoDTO) throws TokenException{
		this.whoDTO = whoDTO;
		this.configReader = ConfigReader.getInstance();
		this.sessionHolder = SessionHolder.createInstance(whoDTO);
		this.tokenManager = new TokenManager();
	}

	protected List<TokenDTO> tokenList = new ArrayList<TokenDTO>();

	public synchronized void pool(TokenDTO tokenDTO) throws TokenException {
		if (tokenDTO == null) {
			log.warn("try to add null to token pool ");
			throw new TokenException(TokenException.TokenError.INVALID_TOKEN);

		}

		if (tokenDTO.isValid() == false) {
			log.warn("try to add Invalid to token pool ");
			throw new TokenException(TokenException.TokenError.INVALID_TOKEN);

		}
		if (tokenDTO.getAccessToken() == null || tokenDTO.getAccessToken().length() == 0) {
			log.warn("token object without access token try to pool ");
			throw new TokenException(TokenException.TokenError.NULL_ACCESS_TOKEN);

		}
		if (tokenDTO.getRefreshToken() == null || tokenDTO.getRefreshToken().length() == 0) {
			log.warn("token object without access token try to pool ");
			throw new TokenException(TokenException.TokenError.NULL_REFRESH_TOKEN);

		}

		if (tokenList.contains(tokenDTO)) {
			log.debug("token added to pool " + tokenDTO);
			tokenList.add(tokenDTO);
		}

	}

	public TokenDTO accqureToken() throws TokenException {
		int waitattempt = 0;

		TokenDTO validTokenDTO = waitUntilPoolfill(waitattempt++);

		if (validTokenDTO == null) {
			throw new TokenException(TokenException.TokenError.TOKENPOOL_EMPTY);
		}
		return validTokenDTO;

	}
	
	

	protected TokenDTO waitUntilPoolfill(int waitattempt) throws  TokenException {
		log.debug("Calling waitUntilPoolfill " + whoDTO + " retry attempt :" + waitattempt);
		synchronized (tokenList) {
			for (TokenDTO tokenDTO : tokenList) {
				if (tokenDTO.isValid()) {
					sessionHolder.acquireSession(tokenDTO);
					log.info("Valid token found " + tokenDTO);
					return tokenDTO;
				}

			}

		}

		/**
		 * sleep for pre defined attempts. if still fail throws exception
		 */
		log.debug("No valid token found for " + whoDTO + " ,Look up attempt :" + waitattempt);
		ConfigDTO configDTO = ConfigReader.getInstance().getConfigDTO();
		try {
			Thread.sleep(configDTO.getWaitingTimeForToken());
		} catch (InterruptedException e) {
			log.error("",e);
			throw new TokenException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		// re try attempt
		if (configDTO.getRetryAttempt() <= waitattempt) {
			throw new TokenException(TokenException.TokenError.TOKENPOOL_EMPTY);
		}
		// recursively lookup for valid token ,
		// this will continue until valid token found or defined attempt pass
		waitUntilPoolfill(waitattempt++);

		log.warn("", "Token pool empty :" + whoDTO);
		throw new TokenException(TokenException.TokenError.TOKENPOOL_EMPTY);

	}
	
	
	
	
	public void removeToken(final TokenDTO token) throws TokenException {
		log.info(" Try to remove Token : " + token + " from token pool of :" + whoDTO);

		boolean isTokenRemoved = false, isTokenExists = false;
		ConfigDTO configDTO = configReader.getConfigDTO();
		
		// validate the given token exists at the token pool
		isTokenExists = tokenList.contains(token);

		// if token is invalid throw exception
		if (!isTokenExists) {
			log.warn("Invaid token unable to remove token:" + token);
			throw new TokenException(TokenException.TokenError.INVALID_TOKEN);

		}
		
		// Invalidate the token, so that re issuing is restricted
		synchronized (tokenList) {
			isTokenRemoved = tokenList.remove(token);
		}
		

		if (!isTokenRemoved) {
			log.warn("Token already removed from the pool :" + whoDTO + " token :" + token);

			throw new TokenException(TokenException.TokenError.TOKEN_ALREDY_REMOVED);
		}

		log.debug("Token removed locally");
		
	}
	
	
	protected boolean validateToken(final TokenDTO token) throws TokenException{
		boolean isTokenExists = false;

		isTokenExists = tokenList.contains(token);

		// if token is invalid throw exception
		if (!isTokenExists) {
			log.warn("Invaid token unable to remove token:" + token);
			throw new TokenException(TokenException.TokenError.INVALID_TOKEN);

		}
		return true;
	}
	


}
