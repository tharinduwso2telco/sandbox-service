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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.conf.ConfigReader;
import com.wso2telco.dep.tpservice.manager.WhoManager;
import com.wso2telco.dep.tpservice.model.ConfigDTO;
import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.pool.TokenPoolImplimentable;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

/**
 * All Time First - pool implementation issue pool which always return first valid
 * token from the pool .Schedule the token expire service.
 *
 *
 */
class TokenSheduler  {
	private Logger log = LoggerFactory.getLogger(TokenSheduler.class);

	private WhoManager adminService;
	private TokenPoolImplimentable poolImpl;
	private ConfigReader configReader;
	WhoDTO whoDTO;

	private TokenSheduler(final WhoDTO whoDTO) throws TokenException {
		this.configReader = ConfigReader.getInstance();
		adminService = new WhoManager();
		ConfigDTO configDTO = configReader.getConfigDTO();
		
		if(configDTO.isMaster()){
			MasterModeTp tempI = new MasterModeTp(whoDTO);
			poolImpl = tempI	;
		}else{
			SlaveTokenPool tempI= new SlaveTokenPool(whoDTO);
			poolImpl =tempI;
		}
		this.whoDTO = whoDTO;
		 
	}

	public static TokenSheduler createInstance(final WhoDTO whoDTO) throws TokenException {
		if (whoDTO == null) {
			throw new TokenException(TokenException.TokenError.NO_VALID_WHO);
		}
		return new TokenSheduler(whoDTO);
	}
	
	// Each token at the persistence layer goes through initialization process. 
	public void initializePool() throws TokenException {
		List<TokenDTO> tokenDTos = adminService.loadTokens(whoDTO.getOwnerId());
		for (TokenDTO tokenDTO : tokenDTos) {
			log.debug("Initialize the token ");
			poolImpl.init(tokenDTO);
		}
	}
	
	
	

	public TokenPoolImplimentable getTokenPoolImpl ()throws TokenException{
		if(poolImpl==null){
			throw new TokenException(TokenException.TokenError.NO_TOKEN_POOL_IMLIMENTATION);
		}
		return	poolImpl;
	}

}
