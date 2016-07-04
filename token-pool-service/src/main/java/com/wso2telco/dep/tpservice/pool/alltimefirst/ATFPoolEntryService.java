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

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.conf.ConfigReader;
import com.wso2telco.dep.tpservice.manager.EventService;
import com.wso2telco.dep.tpservice.manager.WhoManager;
import com.wso2telco.dep.tpservice.model.ConfigDTO;
import com.wso2telco.dep.tpservice.model.EventHistoryDTO;
import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.pool.TokenPool;
import com.wso2telco.dep.tpservice.pool.TokenPoolImplimentable;
import com.wso2telco.dep.tpservice.pool.TokenPoolInitializer;
import com.wso2telco.dep.tpservice.util.Constants;
import com.wso2telco.dep.tpservice.util.Event;
import com.wso2telco.dep.tpservice.util.Status;
import com.wso2telco.dep.tpservice.util.exception.BusinessException;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

/**
 * All Time First - pool implementation issue pool which always return first valid
 * token from the pool .Schedule the token expire service.
 *
 *
 */
public class ATFPoolEntryService implements TokenPoolInitializer {
	private Logger log = LoggerFactory.getLogger(ATFPoolEntryService.class);

	private WhoManager adminService;
	private TokenPoolImplimentable poolImpl;
	private TokenPool pool;
	private EventService eventService;
	private ConfigReader configReader;
	WhoDTO whoDTO;

	private ATFPoolEntryService(final WhoDTO whoDTO) throws TokenException {
		adminService = new WhoManager();
		ConfigDTO configDTO = configReader.getConfigDTO();
		
		if(configDTO.isMaster()){
			MasterModeTp tempI = new MasterModeTp(whoDTO);
			poolImpl = tempI	;
			pool =tempI;
		}else{
			SlaveTokenPool tempI= new SlaveTokenPool(whoDTO);
			poolImpl =tempI;
			pool = tempI;
		}
		
		eventService = new EventService();
		this.whoDTO = whoDTO;
		this.configReader = ConfigReader.getInstance();
	}

	public static TokenPoolInitializer createInstance(final WhoDTO whoDTO) throws TokenException {
		if (whoDTO == null) {
			throw new TokenException(TokenException.TokenError.NO_VALID_WHO);
		}
		return new ATFPoolEntryService(whoDTO);
	}

	public void initializePool() throws BusinessException {
		List<TokenDTO> tokenDTos = adminService.loadTokens(whoDTO.getOwnerId());
		ConfigDTO configDTO = configReader.getConfigDTO();
		for (TokenDTO tokenDTO : tokenDTos) {
			final long tokenExpiory = (tokenDTO.getCreatedTime() + tokenDTO.getTokenValidity());
			
			shedule(tokenDTO);
			poolImpl.pool(tokenDTO);
		}
	}
	
	
	private void shedule(final TokenDTO tokenDTO) throws BusinessException {

		/**
		 * the seducer trigger monitoring service before the token expires
		 * trigges two times early the default connection reset.
		 */
		final long tokenExpiory = (tokenDTO.getCreatedTime() + tokenDTO.getTokenValidity())
				- 2 * whoDTO.getDefaultConnectionRestTime();

		try {
			Timer timer = new Timer();
			// Schedule the re - generate process
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					refreshToken(tokenDTO);

				}

			}, tokenExpiory);
		} catch (IllegalArgumentException e) {

			refreshToken(tokenDTO);
		}

	}

	private void refreshToken(TokenDTO tokenDTO) {
		try {
			poolImpl.removeToken(tokenDTO);

			poolImpl.refreshToken(tokenDTO);
			
			EventHistoryDTO history = new EventHistoryDTO();
			history.setContext(Constants.CONTEXT_TOKEN);
			history.setText(tokenDTO.getAccessToken()+"|"+tokenDTO.getRefreshToken());
			history.setEvent(Event.RE_GENARATE_TOKEN);
			history.setStatus(Status.REGENARATE_SUCSESS);

			eventService.recordEvent(history);
			

		} catch (BusinessException e) {
			log.error(" ERROR occured at token regenarate process at tak sheduler ", e);

			try {
				EventHistoryDTO history = new EventHistoryDTO();
				history.setContext(Constants.CONTEXT_TOKEN);
				history.setText(tokenDTO.getAccessToken()+"|"+tokenDTO.getRefreshToken());
				history.setEvent(Event.RE_GENARATE_TOKEN);
				history.setStatus(Status.REGENARATE_FAIL);

				eventService.recordEvent(history);
			} catch (TokenException e1) {
				log.error(" error occurd on recording event ", e1);
			}
		}

	}

	@Override
	public TokenPool getTokenPool() throws TokenException {
		return pool;
	}
}
