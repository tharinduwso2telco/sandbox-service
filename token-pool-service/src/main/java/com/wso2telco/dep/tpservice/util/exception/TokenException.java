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

		TOKENPOOL_EMPTY("TOKEN_POOL_EMPTY","NO pool entry for this Owner"),
		NO_VALID_ENDPONT("NO_VALID_ENDPONT","No Valid token owner defind at the db");
		
		TokenError(final String msgf,final String coded){
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
