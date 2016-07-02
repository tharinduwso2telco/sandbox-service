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

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.util.Constants;
import com.wso2telco.dep.tpservice.util.Constants.Tables;
import com.wso2telco.dep.tpservice.util.Validator;

public class TokenDAO {

	private static Logger log = LoggerFactory.getLogger(TokenDAO.class);

	/**
	 * Get all valid tokens for given owner id
	 * @return ArrayList of TokenDTO
	 * @throws Exception, return when an error is occurred.
	 */
	public ArrayList<TokenDTO> getAllTokensForOwner(String ownerId) throws SQLException {
		if (Validator.isInvalidString(ownerId)) {
			log.debug("ownerId cannot be null or empty");
			throw new IllegalArgumentException("ownerId cannot be null or empty");
		}
		ArrayList<TokenDTO> tokenList = new ArrayList<TokenDTO>();
		DBI dbi = JDBIUtil.getInstance();
		Handle h = dbi.open();
		try {
			StringBuilder sb = new StringBuilder();			
			sb.append("SELECT T.* ");
			sb.append("FROM ").append(Tables.TABLE_TSTTOKEN.toString()).append(" T ");
			sb.append("INNER JOIN ").append(Tables.TABLE_TSXWHO.toString()).append(" W ");
			sb.append("ON T.tsxwhodid = W.tsxwhodid ");
			sb.append("WHERE W.ownerid = :ownerId ");
			sb.append("AND T.isvalid = :valid ");
	
			List<Map<String, Object>> resultSet = h.createQuery(sb.toString())
					.bind("valid", 1)
					.bind("ownerId", ownerId)
					.list();
	
			for (int i = 0; i < resultSet.size(); i++) {
				TokenDTO tokenDTO = getTokenDTOFromResultsMap(resultSet.get(i));
				tokenList.add(tokenDTO);
			}
		} catch (Exception e) {
			log.error("getAllTokensForOwner() failed ", e);
			throw new SQLException("Could not get all valid tokens for owner id");
		} finally {
			h.close();
		}
		return tokenList;
	}

	/**
	 * Get TokenDTO object from results map
	 * @return TokenDTO object
	 * @throws Exception, return when an error is occurred.
	 */
	private TokenDTO getTokenDTOFromResultsMap(Map<String, Object> resultsMap) throws SQLException {
		TokenDTO tokenDTO = null;
		if (resultsMap != null) {
			tokenDTO = new TokenDTO();
			int id = (Integer)resultsMap.get("tokendid");
			int whoId = (Integer)resultsMap.get("tsxwhodid");
			String tokenAuth = (String)resultsMap.get("tokenauth");
			int tokenValidity = (Integer)resultsMap.get("tokenvalidity");
			String accessToken = (String)resultsMap.get("accesstoken");
			String refreshToken = (String)resultsMap.get("refreshtoken");
			Timestamp lastRefreshDone = (Timestamp)resultsMap.get("lastrefreshdone");
			boolean isValid = (Boolean)resultsMap.get("isvalid");
			int uc = (Integer)resultsMap.get("uc");
			Timestamp createdTime = (Timestamp)resultsMap.get("createdtime");

			tokenDTO.setId(id);
			tokenDTO.setWhoId(whoId);
			tokenDTO.setTokenAuth(tokenAuth);
			tokenDTO.setTokenValidity(tokenValidity);
			tokenDTO.setAccessToken(accessToken);
			tokenDTO.setRefreshToken(refreshToken);
			tokenDTO.setLastRefreshDone(lastRefreshDone.getTime());
			tokenDTO.setValid(isValid);
			tokenDTO.setUc(uc);
			tokenDTO.setCreatedTime(createdTime.getTime());
		} //end if (resultsMap != null)
		return tokenDTO;
	}
	
	
	public void saveNewToken(WhoDTO who_obj, TokenDTO token_obj) throws SQLException {
	 
		int whoId = who_obj.getId();
		String tokenAuth = token_obj.getTokenAuth();
		Long tokenValidity = token_obj.getTokenValidity();
		String accessToken = token_obj.getAccessToken();
		String refreshToken = token_obj.getRefreshToken();
		boolean isValid = token_obj.isValid();
		//long creation_new = token_obj.getCreatedTime();
		
		
		DBI dbi = JDBIUtil.getInstance();
		Handle h = dbi.open();
		
		try {
			StringBuilder sb = new StringBuilder();			
			sb.append(" INSERT ");
			sb.append(" INTO ").append(Tables.TABLE_TSTTOKEN.toString());
			sb.append(" ( tsxwhodid , tokenauth, tokenvalidity , isvalid , accesstoken , refreshtoken ) ");
			sb.append("VALUES ");
			sb.append("(? , ? , ? , ? , ? , ? )");
			
			h.execute(sb.toString() , whoId ,  tokenAuth , tokenValidity , isValid , accessToken , refreshToken);

			log.debug("successfully saved the new token for " + who_obj + " with token " + token_obj);
			
		} catch (Exception e) {
			log.error("TokenDAO","saveNewToken()", e);
			throw new SQLException("Could not add the newly created token");
		} finally {
			h.close();
		}
	}
}
