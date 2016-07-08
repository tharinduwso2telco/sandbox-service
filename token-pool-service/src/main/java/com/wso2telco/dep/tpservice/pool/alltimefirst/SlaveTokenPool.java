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

import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.model.ConfigDTO;
import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.util.exception.BusinessException;
import com.wso2telco.dep.tpservice.util.exception.GenaralError;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

class SlaveTokenPool extends AbstractTokenPool {
	
	
	
	protected SlaveTokenPool(WhoDTO whoDTO) throws TokenException {
		super(whoDTO);
		log = LoggerFactory.getLogger(SlaveTokenPool.class);
	}

	
	protected void reGenarate(final TokenDTO token) throws TokenException {
		log.info(" Try to remove Token : " + token + " from token pool of :" + whoDTO);

		try {
			
			ConfigDTO configDto = configReader.getConfigDTO();
			
			int waitattempt =0;
			
			TokenDTO newTokenDTO =null;
			
			//load configuration repeatedly until retry attempt expires or record found
			while(configDto.getTokenReadretrAttempts() <= waitattempt){
				newTokenDTO =tokenManager.loadNewChild(whoDTO, token); //load token form db
				
				try {
					Thread.sleep(configDto.getTokenReadretrAfter() );
				} catch (InterruptedException e) {
					throw new TokenException(GenaralError.INTERNAL_SERVER_ERROR);
				}
			}
			
			if(newTokenDTO ==null){
				log.warn("token refresh faild :"+token);
				throw new TokenException(GenaralError.INTERNAL_SERVER_ERROR);
			}
			
			log.debug("add New token to pool "+ newTokenDTO);
			synchronized (tokenList) {
				tokenList.put(newTokenDTO.getAccessToken().trim(),newTokenDTO);
			}
			shedule(newTokenDTO);//Schedule for next refresh
			
		} catch (BusinessException e) {
			throw new TokenException(e.getErrorType());
		} 
	}


	

}
