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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.wso2telco.dep.tpservice.conf.ConfigReader;
import com.wso2telco.dep.tpservice.manager.TokenManager;
import com.wso2telco.dep.tpservice.model.ConfigDTO;
import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.pool.TokenControllable;
import com.wso2telco.dep.tpservice.util.exception.GenaralError;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

abstract class AbstrController implements TokenControllable {
	protected Logger log;
	protected ConfigReader configReader;
	protected WhoDTO whoDTO;
	protected TokenManager tokenManager;
	protected TokenDTO tokenDTO;
	protected SessionHolder sessionHolderList;
	private ScheduledExecutorService shedulerService;
	
	
	protected AbstrController(final WhoDTO whoDTO,final TokenDTO tokenDTO) throws TokenException {
		this.whoDTO = whoDTO;
		this.tokenDTO = tokenDTO;
		this.configReader = ConfigReader.getInstance();
		this.tokenManager = new TokenManager();
		this.sessionHolderList = SessionHolder.createInstance(whoDTO, tokenDTO);
	}

	protected abstract TokenDTO reGenarate( ) throws TokenException;
	/**
	 * This will trigger the token refresh and persist the new valid token
	 * 
	 * @param token
	 * @throws TokenException
	 */
	final public TokenDTO refreshToken(final TokenDTO token) throws TokenException {
		log.info(" Try to remove Token : " + token + " from token pool of :" + whoDTO);

		validateToken(token.getAccessToken());

		removeToken( );
		TokenDTO newtokenDTo = reGenarate();
		//Swap oldtoken with newly generated one
		setNewToken(newtokenDTo);

		return newtokenDTo;
	}

	@Override
	final public TokenDTO refreshToken(final String token) throws TokenException {
		log.info(" refreshToken :" + token + " triggered ");
		validateToken(token);
		removeToken( );
		TokenDTO newtokenDTo = reGenarate();
		//Swap oldtoken with newly generated one
		setNewToken(newtokenDTo);
		return newtokenDTo;
	}
	//Swap oldtoken with newly generated one
	protected void setNewToken(final TokenDTO newtokenDTo) throws TokenException{
		this.tokenDTO.setAccessToken(newtokenDTo.getAccessToken() );
		this.tokenDTO.setCreatedTime(newtokenDTo.getCreatedTime());
		this.tokenDTO.setId(newtokenDTo.getId());
		this.tokenDTO.setParentTokenId(newtokenDTo.getParentTokenId());
		this.tokenDTO.setRefreshToken(newtokenDTo.getRefreshToken());
		this.tokenDTO.setTokenAuth(newtokenDTo.getTokenAuth());
		this.tokenDTO.setTokenValidity(newtokenDTo.getTokenValidity());
		this.tokenDTO.setValid(newtokenDTo.isValid());
		this.tokenDTO.setWhoId(newtokenDTo.getWhoId());
		
	}
	
	

	public void removeToken(final TokenDTO token) throws TokenException {
		log.info(" Try to remove Token : " + token + " from token pool of :" + whoDTO);

		validateToken(token.getAccessToken());
		removeToken( );

	}
	/**
	 * this will invalidate the token so that the the token will not re issue.
	 * remove all the scheduled task immediately from the scheduler.
	 * hold the session until all session getting cleared only if token is not expired ,
	 * if expired no waiting 
	 * This only done for locally/not in cluster
	 * @throws TokenException
	 */
	private void removeToken() throws TokenException {

		boolean tokenAlreadyInvalidated = false;

		// Invalidate the token, so that re issuing is restricted
		synchronized (tokenDTO) {
			tokenAlreadyInvalidated = tokenDTO.isValid() ? false : true;

			if (tokenAlreadyInvalidated) {
				log.warn("Token already removed from the pool :" + whoDTO + " token :" + tokenDTO);

				throw new TokenException(TokenException.TokenError.TOKEN_ALREDY_REMOVED);
			} else {
				tokenDTO.setValid(false);
			}

		}
		// Scheduler service shut down ,All the previous scheduled jobs need to
		// cancel
		shedulerService.shutdownNow();
		log.debug("Token removed locally");
		
		// wait until session pool getting cleared and token is not expired
		while (sessionHolderList.isInUse() && !tokenDTO.isExpired()) {
			log.debug("Token " + tokenDTO + "still in use wait for " + whoDTO.getDefaultConnectionRestTime() + " MS "
					+ whoDTO.getDefaultConnectionRestTime());
			try {
				Thread.sleep(whoDTO.getDefaultConnectionRestTime());
			} catch (InterruptedException e) {
				log.error("removeToken intrrupted ", e);
				throw new TokenException(GenaralError.INTERNAL_SERVER_ERROR);
			}

		}
		 

	}

	
	@Override
	final public void removeToken(String token) throws TokenException {
		// validate token from existing pool
		validateToken(token);

		// obtain the token from map
		removeToken();
	}

	protected boolean validateToken(final String accessToken) throws TokenException {
		boolean isTokenExists = false;

		if (accessToken == null || accessToken.trim().length() == 0) {
			log.warn("Null token ");
			throw new TokenException(TokenException.TokenError.INVALID_TOKEN);
		}
		isTokenExists = tokenDTO.getAccessToken().equals(accessToken.trim());

		// if token is invalid throw exception
		if (!isTokenExists) {
			log.warn("Invaid token  :" + accessToken);
			throw new TokenException(TokenException.TokenError.INVALID_TOKEN);

		}
		return true;
	}

	protected void shedule() throws TokenException {
		// Timer timer = new Timer();
		ConfigDTO configDTO = configReader.getConfigDTO();

		final long sheduledTime = tokenDTO.getTokenValidity() - configDTO.getRefreshWakeUpLeadTime()
				-(System.currentTimeMillis() - tokenDTO.getCreatedTime());

		shedulerService = Executors.newScheduledThreadPool(1);
		shedulerService.schedule(new Runnable() {

			@Override
			public void run() {
				try {
					log.debug("sheduler started ");
					// remove the token
					removeToken();
					setNewToken(reGenarate());
					shedule();// Schedule for next refresh

				} catch (TokenException e) {
					log.error("token sheudle expired - ", e);
				}

			}
		}, sheduledTime, TimeUnit.MILLISECONDS);

		// scheduler.scheduleAtFixedRate(yourRunnable, 8, 8, TimeUnit.HOURS);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date sheduleTimed = new Date(System.currentTimeMillis() + sheduledTime);
		log.debug(tokenDTO + "Token Refresh will fire on  " + sdf.format(sheduleTimed));
	}

	/**
	 * Call at very beginning as well as pool restart for this owner.
	 */
	@Override
	public void init() throws TokenException {
		log.debug(" Initializing token :" + tokenDTO);
		

		if (tokenDTO.isExpired()) {// if the token is still valid.if the token
									// is still valid.
			log.debug("Initialization token - token is expired :" + tokenDTO);
			TokenDTO newtokenDTO = reGenarate();
			setNewToken(newtokenDTO);
			shedule();// Schedule for next refresh

		} else {// if the token is still valid.
			log.debug("Initialization token - token is not expired :" + tokenDTO);

			shedule();// Schedule for next refresh
		}

	}

	@Override
	public void stop() throws TokenException {
		log.info(" Token removing from the pool "+tokenDTO);
		removeToken();
	}
	@Override
	public TokenDTO getToken() throws TokenException {
		if(tokenDTO==null){
			throw new TokenException(TokenException.TokenError.NULL_TOKEN);
		}
		return this.tokenDTO;
	}


	@Override
	public void accqureToken() throws TokenException {
		if(sessionHolderList ==null){
			sessionHolderList = SessionHolder.createInstance(whoDTO, tokenDTO);
		}
		sessionHolderList.acquireSession();
		
	}
	
}
