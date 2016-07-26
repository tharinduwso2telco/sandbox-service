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
import org.skife.jdbi.v2.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.TokenSearchDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.util.Constants.Tables;
import com.wso2telco.dep.tpservice.util.Validator;
import com.wso2telco.dep.tpservice.util.exception.TokenException;
import com.wso2telco.dep.tpservice.util.exception.TokenException.TokenError;

public class TokenDAO {

	private static Logger log = LoggerFactory.getLogger(TokenDAO.class);


	public ArrayList<TokenDTO> getAllTokensForOwner(String ownerId) throws SQLException {
		if (Validator.isInvalidString(ownerId)) {
			log.debug("ownerId cannot be null or empty");
			throw new IllegalArgumentException("ownerId cannot be null or empty");
		}
		return (ArrayList<TokenDTO>) loadTokenDTOs(ownerId,null);
	}

	private List<TokenDTO> loadTokenDTOs(final String ownerId, final String parentToken) throws SQLException {
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
			if (!Validator.isInvalidString(parentToken)) {
				sb.append("AND T.parentTokendid = :parentToken ");
			}

			Query<Map<String, Object>> query = h.createQuery(sb.toString()).bind("valid", 1).bind("ownerId", ownerId);
			if (!Validator.isInvalidString(parentToken)) {
				query = query.bind("parentToken", parentToken);
			}
			List<Map<String, Object>> resultSet = query.list();

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
	
	public List<TokenDTO> getGenaratedToken(TokenSearchDTO searchDTO) throws SQLException {
		if (Validator.isInvalidString(searchDTO.getOwnerId())) {
			log.debug("ownerId cannot be null or empty");
			throw new IllegalArgumentException("ownerId cannot be null or empty");
		}
		return (ArrayList<TokenDTO>) loadTokenDTOs(searchDTO.getOwnerId(),searchDTO.getAccessToken());
	}

	
 
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
			//Timestamp lastRefreshDone = (Timestamp)resultsMap.get("lastrefreshdone");
			boolean isValid = (Boolean)resultsMap.get("isvalid");
			int uc = (Integer)resultsMap.get("uc");
			Timestamp createdTime = (Timestamp)resultsMap.get("createdtime");

			tokenDTO.setId(id);
			tokenDTO.setWhoId(whoId);
			tokenDTO.setTokenAuth(tokenAuth);
			tokenDTO.setTokenValidity(tokenValidity);
			tokenDTO.setAccessToken(accessToken);
			tokenDTO.setRefreshToken(refreshToken);
			//tokenDTO.setLastRefreshDone(lastRefreshDone.getTime());
			tokenDTO.setValid(isValid);
			tokenDTO.setUc(uc);
			tokenDTO.setCreatedTime(createdTime.getTime());
		} //end if (resultsMap != null)
		return tokenDTO;
	}

	private TokenDTO getTokenDetails(int ID) {
		TokenDTO returnTokenDto = null;
		DBI dbi = JDBIUtil.getInstance();
		Handle h = dbi.open();
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * ");
			sb.append("FROM ").append(Tables.TABLE_TSTTOKEN.toString()).append(" ");
			sb.append("WHERE tokendid = :newTokenDid");

			Map<String, Object> resultID = h.createQuery(sb.toString())
					.bind("newTokenDid", ID)
					.first();

			if (resultID == null) {
				throw new TokenException(TokenError.INVALID_TOKEN);
			}
			returnTokenDto = getTokenDTOFromResultsMap(resultID);

		} catch (Exception e) {
			log.error("getTokenDetails() failed ", e);
		} finally {
			h.close();
		}
		return returnTokenDto;

	}
	
	// insertion for newly generated access token 
	public void saveNewToken(WhoDTO who_obj, TokenDTO token_obj) throws SQLException {
		log.debug(" INsert new Token for :"+who_obj+" old Token :"+token_obj.getParentTokenId() +" New Token :"+token_obj);
		DBI dbi = JDBIUtil.getInstance();
		TokenHandler tokenHandler = dbi.onDemand(TokenHandler.class);
		
		int newTokenDid = tokenHandler.createNewToken(token_obj, who_obj);
		//Generated token did set in to new token dto
		token_obj.setId(newTokenDid);
		
		//retrieval of created time for next schedule set
		TokenDTO tokenDTO = getTokenDetails(newTokenDid);
		token_obj.setCreatedTime(tokenDTO.getCreatedTime());
		
	}
	
	//TokenDAO responsible for invalidating token 
	public void invalidatingToken(TokenDTO obj) throws SQLException{
		log.debug(" InValidate token :"+obj);
		DBI dbi = JDBIUtil.getInstance();
		TokenHandler tokenHandler = dbi.onDemand(TokenHandler.class);
		
		tokenHandler.invalidateToken(obj);
	}
}