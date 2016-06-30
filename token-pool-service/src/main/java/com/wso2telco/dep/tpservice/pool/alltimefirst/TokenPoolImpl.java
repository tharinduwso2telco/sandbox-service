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
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.conf.ConfigReader;
import com.wso2telco.dep.tpservice.manager.TokenManager;
import com.wso2telco.dep.tpservice.model.ConfigDTO;
import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.pool.TokenPool;
import com.wso2telco.dep.tpservice.pool.TokenReGenarator;
import com.wso2telco.dep.tpservice.util.exception.BusinessException;
import com.wso2telco.dep.tpservice.util.exception.GenaralError;
import com.wso2telco.dep.tpservice.util.exception.TokenException;
import com.wso2telco.dep.tpservice.util.exception.TokenException.TokenError;

class TokenPoolImpl implements TokenPool {
	Logger log = LoggerFactory.getLogger(TokenPoolImpl.class);

	private List<TokenDTO> tokenList = new ArrayList<TokenDTO>();
	private SessionHolder sessionHolder;
	private TokenReGenarator regenarator;
	private TokenManager tokenManager;
	private ConfigReader configReader;

	private WhoDTO whoDTO;

	private TokenPoolImpl(final WhoDTO whoDTO) throws TokenException {
		this.sessionHolder = SessionHolder.createInstance(whoDTO);
		this.whoDTO = whoDTO;

		this.regenarator = new TokenReGenarator();
		this.tokenManager = new TokenManager();
		this.configReader = ConfigReader.getInstance();
	}

	
	public static  TokenPoolImpl createInstance(final WhoDTO whoDTO)throws TokenException {
		return new TokenPoolImpl(whoDTO);
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
		
	/*	//wait until all the session issued for the token getting expired or maximum re try count exceed	
		int currentAttempCount=0;
		while (sessionHolder.isInUse(token)) {
			// thread sleep for 2* default connection reset time
			try {
				Thread.sleep(whoDTO.getDefaultConnectionRestTime());
			} catch (InterruptedException e) {
				log.error("TokenPool.removeToken", e);
				throw new TokenException(GenaralError.INTERNAL_SERVER_ERROR);
			}
			if(currentAttempCount>=configDTO.getRetryAttempt()){
				
			}
		}*/
		

		if (!isTokenRemoved) {
			log.warn("Token already removed from the pool :" + whoDTO + " token :" + token);

			throw new TokenException(TokenException.TokenError.TOKEN_ALREDY_REMOVED);
		}

		log.debug("Token removed locally");
		

		// If the server start as master , it need to invalidate the token from
		// other nodes as well
		if (configDTO.isMaster()) {
			tokenManager.invalidate(whoDTO, token);
		}
	}

	/**
	 * This will trigger the token refresh and persist the new valid token
	 * @param token
	 * @throws TokenException
	 */
	public void refreshToken(final TokenDTO token) throws TokenException {
		log.info(" Try to remove Token : " + token + " from token pool of :" + whoDTO);

		ConfigDTO configDTO = configReader.getConfigDTO();
		if (!configDTO.isMaster()) {
			log.warn("Invalid Token refresh request ");
			throw new TokenException(TokenError.INVALID_TOKEN_REFRESH);
		}
		
		boolean isTokenExists = false;

		isTokenExists = tokenList.contains(token);

		// if token is invalid throw exception
		if (!isTokenExists) {
			log.warn("Invaid token unable to remove token:" + token);
			throw new TokenException(TokenException.TokenError.INVALID_TOKEN);

		}
		
		try {
			// generating new token
			TokenDTO newTokenDTO = regenarator.reGenarate(whoDTO, token);
			
			if(newTokenDTO ==null){
				log.warn("token refresh faild :"+token);
				throw new TokenException(GenaralError.INTERNAL_SERVER_ERROR);
			}
			
			log.debug("add New token to pool "+ newTokenDTO);
			synchronized (tokenList) {
				tokenList.add(newTokenDTO);
			}
			
			
			tokenManager.saveToken(whoDTO, newTokenDTO);
			
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public TokenDTO accqureToken() throws  TokenException {
		int waitattempt = 0;

		TokenDTO validTokenDTO = waitUntilPoolfill(waitattempt++);

		if (validTokenDTO == null) {
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
	private TokenDTO waitUntilPoolfill(int waitattempt) throws  TokenException {
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

}
