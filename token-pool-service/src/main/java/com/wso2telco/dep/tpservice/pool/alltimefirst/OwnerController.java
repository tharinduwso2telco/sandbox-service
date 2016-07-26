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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.conf.ConfigReader;
import com.wso2telco.dep.tpservice.manager.WhoManager;
import com.wso2telco.dep.tpservice.model.ConfigDTO;
import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.pool.OwnerControllable;
import com.wso2telco.dep.tpservice.pool.TokenControllable;
import com.wso2telco.dep.tpservice.pool.TokenPool;
import com.wso2telco.dep.tpservice.util.exception.GenaralError;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

/**
 * All Time First - pool implementation issue pool which always return first valid
 * token from the pool .Schedule the token expire service.
 *
 *
 */
class OwnerController implements OwnerControllable  {
	private Logger log = LoggerFactory.getLogger(OwnerController.class);

	private WhoManager adminService;
//	private TokenPoolImplimentable poolImpl;
	private ConfigReader configReader;
	private WhoDTO whoDTO;
	private List<TokenControllable> poolImplList;
	private ConfigDTO configDTO;
	
	
	
	/**
	 * this will return the token pool for this owner
	 * 
	 * @return
	 */
	final public TokenPool getTokenPool() {
		return new TokenPool() {

			protected TokenDTO waitUntilPoolfill(int waitattempt) throws TokenException {
				log.debug("Calling waitUntilPoolfill " + whoDTO + " retry attempt :" + waitattempt);

				for (TokenControllable controller : poolImplList ) {
					TokenDTO token = controller.getToken();
					//only valid and not expired token are allow to release
					if (token.isValid() && !token.isExpired()) {
						controller.accqureToken();
						log.info("Valid token found " + token);
						return token;
					}

				}

			

				/**
				 * sleep for pre defined attempts. if still fail throws
				 * exception
				 */
				log.debug("No valid token found for " + whoDTO + " ,Look up attempt :" + waitattempt);
				ConfigDTO configDTO = ConfigReader.getInstance().getConfigDTO();
				try {
					Thread.sleep(configDTO.getWaitingTimeForToken());
				} catch (InterruptedException e) {
					log.error("", e);
					throw new TokenException(GenaralError.INTERNAL_SERVER_ERROR);
				}

				// re try attempt
				if (configDTO.getRetryAttempt() <= waitattempt) {
					throw new TokenException(TokenException.TokenError.TOKENPOOL_EMPTY);
				}
				// recursively lookup for valid token ,
				// this will continue until valid token found or defined attempt
				// pass
				waitUntilPoolfill(++waitattempt);

				log.warn("", "Token pool empty :" + whoDTO);
				throw new TokenException(TokenException.TokenError.TOKENPOOL_EMPTY);

			}

			@Override
			public TokenDTO accqureToken() throws TokenException {
				int waitattempt = 0;

				TokenDTO validTokenDTO = waitUntilPoolfill(++waitattempt);

				if (validTokenDTO == null) {
					throw new TokenException(TokenException.TokenError.TOKENPOOL_EMPTY);
				}
				return validTokenDTO;

			}
		};
	}
	
	private OwnerController(final WhoDTO whoDTO) throws TokenException {
		this.configReader = ConfigReader.getInstance();
		this.adminService = new WhoManager();
		this.configDTO = configReader.getConfigDTO();
		this.poolImplList = new ArrayList<TokenControllable>();
		this.whoDTO = whoDTO;
		 
	}

	private TokenControllable createImplimenter(TokenDTO tokenDTO) throws TokenException {
		if (configDTO.isMaster()) {
			return new MasterModeTp(whoDTO,tokenDTO);
		} else {
			return new SlaveTokenPool(whoDTO,tokenDTO);
		}
	}
	
	public static OwnerController createInstance(final WhoDTO whoDTO) throws TokenException {
		if (whoDTO == null) {
			throw new TokenException(TokenException.TokenError.NO_VALID_WHO);
		}
		return new OwnerController(whoDTO);
	}
	
	// Each token at the persistence layer goes through initialization process. 
	public void initializePool() throws TokenException {
		List<TokenDTO> tokenDTos = adminService.loadTokens(whoDTO.getOwnerId());
		for (TokenDTO tokenDTO : tokenDTos) {
			log.debug("Initialize the token ");
			TokenControllable poolImpl = createImplimenter(tokenDTO);
			poolImpl.init();
			this.poolImplList.add( poolImpl);
			
		}
	}
	

	/*public TokenPoolImplimentable getTokenPoolImpl ()throws TokenException{
		if(poolImpl==null){
			throw new TokenException(TokenException.TokenError.NO_TOKEN_POOL_IMLIMENTATION);
		}
		return	poolImpl;
	}*/

	public void reStart(WhoDTO whoDTO) throws TokenException {
		//If token there is no token controllers the request is invalid
		if(poolImplList.isEmpty()){
			throw new TokenException( TokenException.TokenError.INVALID_OPARATION);
		}
		
		this.whoDTO.setDefaultConnectionRestTime( whoDTO.getDefaultConnectionRestTime());
		this.whoDTO.setTokenUrl(whoDTO.getTokenUrl());
		
		//Stop the existing token controllers
		for (TokenControllable tokenPoolImplimentable : poolImplList) {
			tokenPoolImplimentable.stop();
		}
		//Clear old token controllers
		poolImplList.clear();
		
		List<TokenDTO> tokenDTos = adminService.loadTokens(whoDTO.getOwnerId());
		for (TokenDTO tokenDTO : tokenDTos) {
			log.debug("reStart the token ");
			TokenControllable poolImpl = createImplimenter(tokenDTO);
			poolImpl.init();
			this.poolImplList.add( poolImpl);
		}
	}

	@Override
	public TokenControllable getTokenController(String accessToken) throws TokenException {
		if(this.poolImplList.isEmpty()){
			log.debug(" getTokenController triggerd :"+accessToken);
			throw new  TokenException(TokenException.TokenError.NO_TOKEN_POOL_IMLIMENTATION); 
		}
		TokenControllable returnVal =null;
		
		for (TokenControllable controller : poolImplList ) {
			if(controller.getToken().getAccessToken().equalsIgnoreCase(accessToken.trim())){
				returnVal =controller;
				break;
			}
		}
		
		if(returnVal ==null){
			throw new TokenException(TokenException.TokenError.INVALID_TOKEN);
		}else{
			
			return returnVal;
		}
	}
	
}
