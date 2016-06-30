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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.manager.WhoManager;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.util.exception.BusinessException;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

public class PoolFactory {
	private Logger log = LoggerFactory.getLogger(PoolFactory.class);	
	private static PoolFactory instance;
	private Map<String,TokenPool> tokenPoolMap ;
	
	PoolFactory(){
		tokenPoolMap = new HashMap<String,TokenPool>();
	}
	
	public synchronized static PoolFactory getInstance(){
		if(instance==null){
			instance = new PoolFactory();
		}
		return instance;
		
	}
	public  void initializeTokenPools() throws BusinessException {
	
		log.info("loading initializing token pool ");
		
		List<WhoDTO> whoDTOs =  new WhoManager().getAllOwners();
		
		if(whoDTOs==null ||whoDTOs.isEmpty()){
			throw new TokenException(TokenException.TokenError.NO_VALID_ENDPONT);
		}
		for (WhoDTO whoDTO : whoDTOs) {
//			new PoolEntryService().addTokePool(whoDTO);
			TokenPoolInitializer initializer = TokenFactory.getPoolInitImplimenter(whoDTO);
		
			initializer.initializePool();
			TokenPool tp = initializer.getTokenPool();
			if(tp==null){
				log.warn(" UNable to initialize the token pool for "+whoDTO);
			}else{
				tokenPoolMap.put(whoDTO.getOwnerId(), tp);
			}
		}
		log.info("Token pool initialized");
	}

	/**
	 * return valid token pool. the Owner and the owner id  is mandatory
	 * @param ownerDTO
	 * @return
	 * @throws TokenException
	 */
	public TokenPool getTokenPool(final String owner) throws TokenException{
		
		log.debug("Token pool request for owner :"+owner);
		
		if(owner==null ||  owner.trim().length()<1  ){
			log.warn("Pool Owner cannt  be null ");
			throw new TokenException(TokenException.TokenError.NO_VALID_WHO);
		}
		
		if (tokenPoolMap==null ||tokenPoolMap.isEmpty()){
			log.warn("pool not yet initialized ");
			throw new TokenException(TokenException.TokenError.POOL_NOT_READY);
		}
		if(tokenPoolMap.containsKey(owner.trim())){
			log.debug(" token pool released ");
			return tokenPoolMap.get(owner.trim());
		}else{
			log.warn("Invalid pool owner. ");
			throw new TokenException(TokenException.TokenError.POOL_NOT_READY);
		}
	}
	
	
}
