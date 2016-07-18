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

import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.pool.TokenReGenarator;
import com.wso2telco.dep.tpservice.util.exception.GenaralError;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

class MasterModeTp extends AbstrController {

	private TokenReGenarator regenarator;
	
	protected MasterModeTp(WhoDTO whoDTO,TokenDTO tokenDTO) throws TokenException {
		super(whoDTO,tokenDTO);
		log = LoggerFactory.getLogger(MasterModeTp.class);
		this.regenarator = new TokenReGenarator();
	}
	
	public void removeToken(final TokenDTO token) throws TokenException {
			super.removeToken(token);
			log.debug("remove form the DB "+token);
			tokenManager.invalidate(token);
	}
	
	
	@Override
	protected TokenDTO reGenarate( )throws TokenException{
		TokenDTO newTokenDTO=null;
		try {
			// generating new token
			newTokenDTO = regenarator.reGenarate(whoDTO, tokenDTO);
			if(newTokenDTO ==null){
				log.warn("token refresh faild :"+tokenDTO);
				throw new TokenException(GenaralError.INTERNAL_SERVER_ERROR);
			}
			tokenManager.saveToken(whoDTO, newTokenDTO);
			
		} catch (TokenException e) {
			throw new TokenException(e.getErrorType());
		}
		return newTokenDTO;
	}

}
