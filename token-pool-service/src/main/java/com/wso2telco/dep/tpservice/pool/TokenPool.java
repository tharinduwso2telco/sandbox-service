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

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.model.TokenDTO;

public class TokenPool {
	 Logger log = LoggerFactory.getLogger(TokenPool.class);
	
	private ConcurrentHashMap<String, TokenDTO> tokenPool =new ConcurrentHashMap<String,TokenDTO > ();
	private static TokenPool instance ;
	
	public synchronized static TokenPool getInstance(){
		if(instance==null){
			instance = new TokenPool();
			
		}
		return instance;
	}
	
	 public void removeToken(final String tokenId){
		 tokenPool.remove(tokenId);
	 }

	
}
