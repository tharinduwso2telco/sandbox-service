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

package com.wso2telco.dep.tpservice.util;

public enum Status {
	REGENARATE_FAIL(4000, "REGENARATE_FAIL"), 
	REGENARATE_SUCSESS(4001, "REGENARATE_SUCESS"),
	INVALIDATE_SUCCESS(4002, "INVALIDATE_SUCCESS"),
	REGENERATE_TOKEN_SAVE(4003, "REGENERATE_TOKEN_SAVED");
	
	private Integer key;
	private String code;

	Status(Integer key, String code) {
		this.key = key;
		this.code = code;
	}

	public Integer getKey() {
		return this.key;
	}

}
