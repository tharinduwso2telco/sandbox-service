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

package com.wso2telco.dep.tpservice.model;

import java.io.Serializable;

public class TokenDTO implements Serializable {

	private static final long serialVersionUID = 3164068409562912730L;
	
	private int id;
	
	private int whoId;

	private String tokenAuth;
	
	private long tokenValidity;
	
	private boolean valid;
		
	private String accessToken;
	
	private String refreshToken;
	
	private int uc;
	
	private long createdTime;
	
	private int parentTokenId;

	public long getTokenExpiry(){
		return createdTime +(tokenValidity) ;
	}
	public boolean isExpired(){
		return createdTime +(tokenValidity) <=System.currentTimeMillis();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWhoId() {
		return whoId;
	}

	public void setWhoId(int whoId) {
		this.whoId = whoId;
	}

	public String getTokenAuth() {
		return tokenAuth;
	}

	public void setTokenAuth(String tokenAuth) {
		this.tokenAuth = tokenAuth;
	}

	public long getTokenValidity() {
		return tokenValidity;
	}

	public void setTokenValidity(long tokenValidity) {
		this.tokenValidity = tokenValidity;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public int getUc() {
		return uc;
	}

	public void setUc(int uc) {
		this.uc = uc;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}
	
	public int getParentTokenId() {
		return parentTokenId;
	}

	public void setParentTokenId(int parentTokenId) {
		this.parentTokenId = parentTokenId;
	}
	@Override
	public String toString() {
		return "TokenDTO [id=" + id + ", whoId=" + whoId + ", tokenAuth=" + tokenAuth + ", tokenValidity="
				+ tokenValidity + ", valid=" + valid + ", accessToken=" + accessToken + ", refreshToken=" + refreshToken
				+ ", uc=" + uc + ", createdTime=" + createdTime + ", parentTokenId=" + parentTokenId + "]";
	}
	
	
	
}
