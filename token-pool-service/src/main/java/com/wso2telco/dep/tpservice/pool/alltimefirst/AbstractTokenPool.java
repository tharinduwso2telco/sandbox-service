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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

abstract class AbstractTokenPool implements TokenPoolImplimentable {
	protected Logger log;
	protected ConfigReader configReader;
	protected WhoDTO whoDTO;
	protected TokenManager tokenManager;
	private Map<String, TokenInfoWrapperDTO> tokenList = new HashMap<String, TokenInfoWrapperDTO>();
	
	protected TokenDTO tokenDTO;
	protected SessionHolder sessionHolderList;
	
	private ScheduledExecutorService shedulerService;

	protected AbstractTokenPool(final WhoDTO whoDTO) throws TokenException {
		this.whoDTO = whoDTO;
		this.configReader = ConfigReader.getInstance();
		this.tokenManager = new TokenManager();
	}

	protected abstract TokenDTO reGenarate(final TokenDTO token) throws TokenException;

	final protected void addToPool(final TokenDTO tokenDTO) throws TokenException {
		log.debug("add New token to pool " + tokenDTO);
		TokenInfoWrapperDTO tokenWrapper = new TokenInfoWrapperDTO();
		tokenWrapper.setTokenDTO(tokenDTO);
		tokenWrapper.setSessionHolderList(SessionHolder.createInstance(whoDTO, tokenDTO));
		// Add to token map which used to release token to the pool
		synchronized (tokenList) {
			tokenList.put(tokenDTO.getAccessToken().trim(), tokenWrapper);
		}

	}

	/**
	 * This will trigger the token refresh and persist the new valid token
	 * 
	 * @param token
	 * @throws TokenException
	 */
	final public TokenDTO refreshToken(final TokenDTO token) throws TokenException {
		log.info(" Try to remove Token : " + token + " from token pool of :" + whoDTO);

		validateToken(token.getAccessToken());

		TokenDTO newTokenDTo = refreshToken(tokenList.get(token.getAccessToken()));

		return newTokenDTo;
	}

	@Override
	final public TokenDTO refreshToken(final String token) throws TokenException {
		log.info(" refreshToken :" + token + " triggered ");
		validateToken(token);
		TokenInfoWrapperDTO tokenWrapper = tokenList.get(token.trim());
		removeToken(tokenWrapper);
		TokenDTO newtokenDTo = refreshToken(tokenWrapper);
		return newtokenDTo;
	}

	private TokenDTO refreshToken(final TokenInfoWrapperDTO tokenWrapperDTO) throws TokenException {
		log.info(" Try to remove Token : " + tokenWrapperDTO.getTokenDTO() + " from token pool of :" + whoDTO);

		TokenDTO newTokenDTo = reGenarate(tokenWrapperDTO.getTokenDTO());
		return newTokenDTo;
	}

	public void removeToken(final TokenDTO token) throws TokenException {
		log.info(" Try to remove Token : " + token + " from token pool of :" + whoDTO);

		validateToken(token.getAccessToken());
		removeToken(tokenList.get(token.getAccessToken()));

	}

	private void removeToken(final TokenInfoWrapperDTO tokenWrapperDTO) throws TokenException {

		boolean isTokenRemoved = false;

		while (tokenWrapperDTO.getSessionHolderList().isInUse()) {
			log.debug("Token " + tokenWrapperDTO.getTokenDTO() + "still in use wait for "
					+ whoDTO.getDefaultConnectionRestTime());
			try {
				Thread.sleep(whoDTO.getDefaultConnectionRestTime());
			} catch (InterruptedException e) {
				throw new TokenException(GenaralError.INTERNAL_SERVER_ERROR);
			}

		}

		// Invalidate the token, so that re issuing is restricted
		synchronized (tokenList) {
			TokenInfoWrapperDTO wrapper = tokenList.remove(tokenWrapperDTO.getTokenDTO().getAccessToken());
			isTokenRemoved = wrapper != null ? true : false;

			if (!isTokenRemoved) {
				log.warn("Token already removed from the pool :" + whoDTO + " token :" + wrapper.getTokenDTO());

				throw new TokenException(TokenException.TokenError.TOKEN_ALREDY_REMOVED);
			}
		}

		log.debug("Token removed locally");
	}

	@Override
	final public void removeToken(String token) throws TokenException {
		// validate token from existing pool
		validateToken(token);

		// obtain the token from map
		removeToken(tokenList.get(token.trim()));
	}

	protected boolean validateToken(final String accessToken) throws TokenException {
		boolean isTokenExists = false;

		if (accessToken == null || accessToken.trim().length() == 0) {
			log.warn("Null token ");
			throw new TokenException(TokenException.TokenError.INVALID_TOKEN);
		}
		isTokenExists = tokenList.containsKey(accessToken.trim());

		// if token is invalid throw exception
		if (!isTokenExists) {
			log.warn("Invaid token  :" + accessToken);
			throw new TokenException(TokenException.TokenError.INVALID_TOKEN);

		}
		return true;
	}

	/**
	 * this will return the token pool for this owner
	 * 
	 * @return
	 */
	final public TokenPool getTokenPool() {
		return new TokenPool() {

			protected TokenDTO waitUntilPoolfill(int waitattempt) throws TokenException {
				log.debug("Calling waitUntilPoolfill " + whoDTO + " retry attempt :" + waitattempt);
				synchronized (tokenList) {
					for (TokenInfoWrapperDTO tokenDTOWrapper : tokenList.values()) {
						if (tokenDTOWrapper.getTokenDTO().isValid()) {
							tokenDTOWrapper.getSessionHolderList().acquireSession();
							log.info("Valid token found " + tokenDTOWrapper.getTokenDTO());
							return tokenDTOWrapper.getTokenDTO();
						}

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

	protected void shedule(final TokenDTO newTokenDTO) throws TokenException {
		// Timer timer = new Timer();
		ConfigDTO configDTO = configReader.getConfigDTO();

		final long sheduledTime = newTokenDTO.getCreatedTime()
				+ (newTokenDTO.getTokenValidity() - configDTO.getRefreshWakeUpLeadTime());
		if (shedulerService != null) {
			shedulerService.shutdownNow();
		}
		shedulerService = Executors.newScheduledThreadPool(1);

		shedulerService.schedule(new Runnable() {

			@Override
			public void run() {
				try {
					// remove the token
					removeToken(newTokenDTO);
					reGenarate(newTokenDTO);

				} catch (TokenException e) {
					log.error("token sheudle expired - ", e);
				}

			}
		}, sheduledTime, TimeUnit.MILLISECONDS);

		// scheduler.scheduleAtFixedRate(yourRunnable, 8, 8, TimeUnit.HOURS);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date sheduleTimed = new Date(sheduledTime);
		log.debug(newTokenDTO + "Token Refresh will fire on  " + sdf.format(sheduleTimed));
		/*
		 * timer.purge(); // Schedule the re - generate process
		 * timer.schedule(new TimerTask() {
		 * 
		 * 
		 * @Override public void run() { try { // remove the token
		 * removeToken(newTokenDTO); reGenarate(newTokenDTO);
		 * 
		 * } catch (TokenException e) { log.error("token sheudle expired - ",
		 * e); }
		 * 
		 * }
		 * 
		 * },sheduledTime );
		 */
	}

	/**
	 * Call at very beginning as well as pool restart for this owner.
	 */
	@Override
	public void init(TokenDTO tokenDTO) throws TokenException {
		log.debug(" Initializing token :" + tokenDTO);
		

		if (tokenDTO.isExpired()) {// if the token is still valid.if the token
									// is still valid.
			log.debug("Initialization token - token is expired :" + tokenDTO);
			reGenarate(tokenDTO);

		} else {// if the token is still valid.
			log.debug("Initialization token - token is not expired :" + tokenDTO);
			addToPool(tokenDTO);

			shedule(tokenDTO);// Schedule for next refresh
		}

	}

	
	@Override
	final public void reStart(WhoDTO whoDTO, TokenDTO tokenDTO) throws TokenException {
		this.whoDTO = whoDTO;
		// if there are previously added tokens remove from the token pool and
		// reset into zero
		ExecutorService executorService = Executors.newFixedThreadPool(tokenList.size());

		CountDownLatch latch = new CountDownLatch(tokenList.size());
		for (TokenInfoWrapperDTO iterable_element : tokenList.values()) {

			executorService.execute(new Runnable() {
				public void run() {
					try {
						removeToken(iterable_element);
					} catch (TokenException e) {
						log.error("restart fail ", e);
					} finally {
						latch.countDown();
					}
				}
			});

		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			log.error("", e);
			throw new TokenException(GenaralError.INTERNAL_SERVER_ERROR);

		}
		synchronized (tokenList) {
			// tokenList.get(key)
			if (!tokenList.isEmpty()) {
				tokenList.clear();
			}
		}

		init(tokenDTO);
	}
	
	class TokenInfoWrapperDTO {

		protected TokenDTO tokenDTO;
		protected SessionHolder sessionHolderList;

		public TokenDTO getTokenDTO() {
			return tokenDTO;
		}

		public void setTokenDTO(TokenDTO tokenDTO) {
			this.tokenDTO = tokenDTO;
		}

		public SessionHolder getSessionHolderList() {
			return sessionHolderList;
		}

		public void setSessionHolderList(SessionHolder sessionHolderList) {
			this.sessionHolderList = sessionHolderList;
		}

	}
}
