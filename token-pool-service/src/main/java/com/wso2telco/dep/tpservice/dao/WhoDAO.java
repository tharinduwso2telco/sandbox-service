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

import com.wso2telco.dep.tpservice.util.Constants;
import com.wso2telco.dep.tpservice.util.Constants.TableTsxWhoColumns;
import com.wso2telco.dep.tpservice.model.WhoDTO;

public class WhoDAO {

	private static Logger log = LoggerFactory.getLogger(WhoDAO.class);

	/**
	 * Get All Owners
	 * @return ArrayList of WhoDTO
	 * @throws Exception, return when an error is occurred.
	 */
	public ArrayList<WhoDTO> getAllOwners() throws SQLException {
		ArrayList<WhoDTO> ownersList = new ArrayList<WhoDTO>();
		DBI dbi = JDBIUtil.getInstance();
		Handle h = dbi.open();
		try {
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM " + Constants.TABLE_TSXWHO + " ");
			sb.append("WHERE " + TableTsxWhoColumns.IS_VALID.toString() + " = 1 ");
			sb.append("ORDER BY " + TableTsxWhoColumns.OWNER_ID.toString());
	
			List<Map<String, Object>> resultSet = h.select(sb.toString());
	
			for (int i = 0; i < resultSet.size(); i++) {
				WhoDTO whoDTO = getWhoDTOFromResultsMap(resultSet.get(i));
				ownersList.add(whoDTO);
			}
		} catch (Exception e) {
			log.error("getAllOwners() failed ", e);
			throw new SQLException("Could not get all valid owners");
		} finally {
			h.close();
		}
		return ownersList;
	}

	/**
	 * Get WhoDTO object from results map
	 * @return WhoDTO object
	 * @throws Exception, return when an error is occurred.
	 */
	private WhoDTO getWhoDTOFromResultsMap(Map<String, Object> resultsMap) throws SQLException {
		WhoDTO whoDTO = null;
		if (resultsMap != null) {
			whoDTO = new WhoDTO();
			int id = (Integer)resultsMap.get(TableTsxWhoColumns.TSXWHODID.toString());
			String ownerId = (String)resultsMap.get(TableTsxWhoColumns.OWNER_ID.toString());
			String tokenUrl = (String)resultsMap.get(TableTsxWhoColumns.TOKEN_URL.toString());
			long defaultConnectionResetTime = (Long)resultsMap.get(TableTsxWhoColumns.DEFAULT_CONNECTION_RESET_TIME.toString());
			boolean isValid = (Boolean)resultsMap.get(TableTsxWhoColumns.IS_VALID.toString());
			Timestamp createdDate = (Timestamp)resultsMap.get(TableTsxWhoColumns.CREATED_DATE.toString());
			int uc = (Integer)resultsMap.get(TableTsxWhoColumns.UC.toString());

			whoDTO.setId(id);
			whoDTO.setOwnerId(ownerId);
			whoDTO.setTokenUrl(tokenUrl);
			whoDTO.setDefaultConnectionRestTime(defaultConnectionResetTime);
			whoDTO.setValid(isValid);
			whoDTO.setCreatedDate(createdDate);
			whoDTO.setUc(uc);
		} else {
			//resultsMap is null
		}
		return whoDTO;
	}
}
