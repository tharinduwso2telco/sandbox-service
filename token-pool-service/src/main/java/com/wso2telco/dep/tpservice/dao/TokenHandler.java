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

package com.wso2telco.dep.tpservice.dao;

import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
import org.skife.jdbi.v2.sqlobject.mixins.GetHandle;

import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.util.Event;
import com.wso2telco.dep.tpservice.util.Status;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

abstract class TokenHandler implements GetHandle{
	
	@CreateSqlObject
	abstract PersistableToken  tokenPersister();/* =getHandle().attach(PersistableToken.class);*/
	@CreateSqlObject
	abstract PersistableEvent  persistableEvent(); /*= getHandle().attach(PersistableEvent.class);*/
	final static String CREATE_NEW_TOKEN="CREATE_NEW_TOKEN";
	final static String EXPIRE_TOKEN="INVALIDATE_TOKEN";
	
/**
 *  create record in token table and create event history
 * @param newTokenDTO
 * @param whoDTO
 * @throws TokenException
 */
	
	public int createNewToken(final TokenDTO newTokenDTO,final WhoDTO whoDTO){
		int newTokenDid=0;
		if(newTokenDTO.getParentTokenId()==0){
			newTokenDid= tokenPersister().inset(whoDTO.getId(), newTokenDTO.getTokenAuth(), 
								newTokenDTO.getTokenValidity(), Boolean.TRUE,
								newTokenDTO.getAccessToken(), newTokenDTO.getRefreshToken());
			
		}else{
			tokenPersister().update(Boolean.FALSE, newTokenDTO.getParentTokenId()); 
			
			newTokenDid=tokenPersister().inset(whoDTO.getId(), newTokenDTO.getTokenAuth(), 
					newTokenDTO.getTokenValidity(), Boolean.TRUE,
					newTokenDTO.getAccessToken(), newTokenDTO.getRefreshToken(),newTokenDTO.getParentTokenId());
		}
		
		persistableEvent().insert(CREATE_NEW_TOKEN, newTokenDTO.getAccessToken()+"|"+newTokenDTO.getRefreshToken(), Event.SAVED_TOKEN.getKey(), Status.REGENERATE_TOKEN_SAVE.getKey());
		return newTokenDid;
	}
	
	public void invalidateToken(final TokenDTO newTokenDTO){
		tokenPersister().update(Boolean.FALSE, newTokenDTO.getId()); 
		persistableEvent().insert(EXPIRE_TOKEN, newTokenDTO.getAccessToken()+"|"+newTokenDTO.getRefreshToken(), Event.INVALIDATE_TOKEN.getKey(), Status.INVALIDATE_SUCCESS.getKey());
		
	}

}
