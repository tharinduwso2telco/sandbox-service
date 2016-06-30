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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.manager.WhoManager;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.pool.session.SessionHolder;
import com.wso2telco.dep.tpservice.util.exception.BusinessException;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

public class PoolManager {
	static Logger log = LoggerFactory.getLogger(PoolManager.class);	
	
	public static void init() throws BusinessException {
	
		log.info("loading initializing token pool ");
		List<WhoDTO> whoDTOs =  new WhoManager().getAllOwners();
		
		if(whoDTOs==null ||whoDTOs.isEmpty()){
			throw new TokenException(TokenException.TokenError.NO_VALID_ENDPONT);
		}
		for (WhoDTO whoDTO : whoDTOs) {
			new PoolEntryService().addTokePool(whoDTO);
			
			
			SessionHolder.init(whoDTO.getOwnerId(), whoDTO.getDefaultConnectionRestTime());
		}
		log.info("Token pool initialized");
	}

	
	
	
}
