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

package com.wso2telco.dep.tpservice.util.exception;

public class TokenException extends BusinessException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -55836217257748455L;

	public TokenException(ThrowableError error) {
		super(error);
	}
	
	public enum  TokenError  implements ThrowableError{

		TOKENPOOL_EMPTY("TPS0005","NO pool entry for this Owner"),
		NO_VALID_ENDPONT("TPS0006","No Valid token owner defind at the db"),
		NO_VALID_WHO("TPS0007","No Valid owner defind"),
		NO_TOKENRESET_TIME("TPS0008","No token reset time specified for the owner"),
		TOKEN_ALREDY_REMOVED("TPS0009","Token alredy removed from the token pool"),
		INVALID_TOKEN("TPS0010","Invalid token"),
		INVALID_TOKEN_REFRESH("TPS0012","Only master node allow to re-fesh the token"),
		POOL_NOT_READY("TPS0013","Token pool not initialized yet"),
		NULL_ACCESS_TOKEN("TPS0014","Access Token is null"),
		NULL_REFRESH_TOKEN("TPS0015","Refresh Token is null"),
		NO_TOKEN_POOL_IMLIMENTATION("TPS0016","No token pool implimentation initialized"),
		NO_TOKEN_POOL_MANAGABLE("TPS0017","No token Token Pool Managable initialized"),
		NO_VALID_TOKEN_URL("TPS0018", "No valid token url defined at the db"),
		INVALID_AUTH_HEADER("TPS0019"," Invalid Authentication Header at the db"),
		TOKEN_REGENERATE_FAIL("TPS0020"," Token Regeneration Failed "),
		INVALID_REFRESH_CREDENTIALS("TPS0021"," Invalid credentials passed for Token Regeneration "),
		SHEDULED_TIME_ALREADY_EXPIERD("TPS0022","Scheduled  time already expired"),
		INVALID_OPARATION("TPS0023","Token pool not initialized for this owner"),
		NULL_TOKEN("TPS0024","Token DTO NOT YET ASSIGNED")
		;
		
		TokenError(final String coded,final String msgf){
			this.msg =msgf;
			this.code = coded;
		}
		@Override
		public String getMessage() {
			// TODO Auto-generated method stub
			return this.msg;
		}

		@Override
		public String getCode() {
			// TODO Auto-generated method stub
			return this.code;
		}
		
		private String msg;
		private String code;
		
	}
	
}
