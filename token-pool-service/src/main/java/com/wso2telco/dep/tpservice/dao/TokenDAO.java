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
import com.wso2telco.dep.tpservice.util.Constants;
import com.wso2telco.dep.tpservice.util.Constants.TableTstTokenColumns;
import com.wso2telco.dep.tpservice.util.Constants.TableTsxWhoColumns;
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
			throw new IllegalArgumentException("ownerId cannot be null or empty");
		}
		ArrayList<TokenDTO> tokenList = new ArrayList<TokenDTO>();
		DBI dbi = JDBIUtil.getInstance();
		Handle h = dbi.open();
		try {
			StringBuilder sb = new StringBuilder();			
			sb.append("SELECT " + Constants.TABLE_TSTTOKEN + ".* ");
			sb.append("FROM " + Constants.TABLE_TSTTOKEN + " ");
			sb.append("INNER JOIN " + Constants.TABLE_TSXWHO + " ");
			sb.append("ON " + Constants.TABLE_TSTTOKEN + "." + TableTstTokenColumns.TSXWHODID.toString() + " = " + Constants.TABLE_TSXWHO + "." + TableTsxWhoColumns.TSXWHODID.toString() + " ");
			sb.append("WHERE " + Constants.TABLE_TSXWHO + "." + TableTsxWhoColumns.OWNER_ID.toString() + " = :ownerId ");
			sb.append("AND " + Constants.TABLE_TSTTOKEN + "." + TableTstTokenColumns.IS_VALID.toString() + " = :valid ");
	
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
			int id = (Integer)resultsMap.get(TableTstTokenColumns.TOKENDID.toString());
			int whoId = (Integer)resultsMap.get(TableTstTokenColumns.TSXWHODID.toString());
			String tokenAuth = (String)resultsMap.get(TableTstTokenColumns.TOKEN_AUTH.toString());
			int tokenValidity = (Integer)resultsMap.get(TableTstTokenColumns.TOKEN_VALIDITY.toString());
			String accessToken = (String)resultsMap.get(TableTstTokenColumns.ACCESS_TOKEN.toString());
			String refreshToken = (String)resultsMap.get(TableTstTokenColumns.REFRESH_TOKEN.toString());
			Timestamp lastRefreshDone = (Timestamp)resultsMap.get(TableTstTokenColumns.LAST_REFRESH_DONE.toString());
			boolean isValid = (Boolean)resultsMap.get(TableTstTokenColumns.IS_VALID.toString());
			int uc = (Integer)resultsMap.get(TableTstTokenColumns.UC.toString());
			Timestamp createdTime = (Timestamp)resultsMap.get(TableTstTokenColumns.CREATED_TIME.toString());

			tokenDTO.setId(id);
			tokenDTO.setWhoId(whoId);
			tokenDTO.setTokenAuth(tokenAuth);
			tokenDTO.setTokenValidity(tokenValidity);
			tokenDTO.setAccessToken(accessToken);
			tokenDTO.setRefreshToken(refreshToken);
			tokenDTO.setLastRefreshDone(lastRefreshDone);
			tokenDTO.setValid(isValid);
			tokenDTO.setUc(uc);
			tokenDTO.setCreatedTime(createdTime.getTime());
		} else {
			//resultsMap is null
		}
		return tokenDTO;
	}
}
