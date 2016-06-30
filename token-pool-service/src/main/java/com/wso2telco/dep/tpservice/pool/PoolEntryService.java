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
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.manager.WhoManager;
import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.util.exception.BusinessException;

/**
 * Schedule the token expire service.
 *
 *
 */
class PoolEntryService {

	private WhoManager adminService;
	Logger log = LoggerFactory.getLogger(PoolEntryService.class);

	{
		adminService = new WhoManager();
	}

	public void addTokePool(final WhoDTO whoDTO) throws BusinessException {
		List<TokenDTO> tokenDTos = adminService.loadTokens(whoDTO.getOwnerId());

		for (TokenDTO tokenDTO : tokenDTos) {
			
			shedule(whoDTO, tokenDTO);

		}
	}

	private void shedule(final WhoDTO whoDTO, final TokenDTO tokenDTO) throws BusinessException {

		/**
		 * the seducer trigger monitoring service before the token expires
		 * trigges two times early the default connection reset.
		 */
		final long tokenExpiory = (tokenDTO.getCreatedTime()+ tokenDTO.getTokenValidity())
				- 2 * whoDTO.getDefaultConnectionRestTime();

		try {
			Timer timer = new Timer();
			// Schedule the re - generate process
			timer.schedule(new TimerTask() {
				TokenReGenarator tokenGenarator = new TokenReGenarator();
				TokenPool pool =TokenPool.getInstance();
				@Override
				public void run() {

					try {
						pool.removeToken(whoDTO,tokenDTO);

						// thread sleep for 2* default connection reset time to continue the operation
						Thread.sleep(2 * whoDTO.getDefaultConnectionRestTime());

						TokenDTO newToken=tokenGenarator.reGenarate(whoDTO, tokenDTO);
						pool.addToPool(tokenDTO,whoDTO);

					} catch (BusinessException | InterruptedException e) {
						log.error(" ERROR occured at token regenarate process at tak sheduler ", e);
					}

				}
			}, tokenExpiory);
		} catch (IllegalArgumentException e) {
			TokenPool.getInstance().removeToken(whoDTO,tokenDTO);
			/**
			 * Schedule time already expired
			 */
			log.error(" token already expired tokenID :" + tokenDTO.getId() + " | owner id :" + whoDTO.getId());
			new TokenReGenarator().reGenarate(whoDTO, tokenDTO);
			TokenPool.getInstance().addToPool(tokenDTO, whoDTO);
		}

	}

}
