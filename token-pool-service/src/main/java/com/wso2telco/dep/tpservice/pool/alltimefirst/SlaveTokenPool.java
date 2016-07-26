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

class SlaveTokenPool extends AbstrController {
	
	
	
	protected SlaveTokenPool(WhoDTO whoDTO,TokenDTO tokenDTO) throws TokenException {
		super(whoDTO,tokenDTO);
		log = LoggerFactory.getLogger(SlaveTokenPool.class);
	}

	@Override
	protected TokenDTO reGenarate() throws TokenException {
		log.info(" Try to remove Token : " + tokenDTO + " from token pool of :" + whoDTO);
		TokenDTO newTokenDTO =null;
		try {
			
			ConfigDTO configDto = configReader.getConfigDTO();
			
			int waitattempt =0;
			
			
			
			//load configuration repeatedly until retry attempt expires or record found
			while(configDto.getTokenReadretrAttempts() <= waitattempt){
				newTokenDTO =tokenManager.loadNewChild(whoDTO, tokenDTO); //load token form db
				if(newTokenDTO !=null){
					break;
				}
				try {
					Thread.sleep(configDto.getTokenReadretrAfter() );
				} catch (InterruptedException e) {
					throw new TokenException(GenaralError.INTERNAL_SERVER_ERROR);
				}
			}
			
			if(newTokenDTO ==null){
				log.warn("token refresh faild :"+tokenDTO);
				throw new TokenException(GenaralError.INTERNAL_SERVER_ERROR);
			}
			
		} catch (BusinessException e) {
			throw new TokenException(e.getErrorType());
		}
		return newTokenDTO; 
	}
}
