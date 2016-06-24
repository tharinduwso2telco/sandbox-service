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

import com.wso2telco.dep.tpservice.model.WhoDTO;

public class WhoDAO {

	static Logger log = LoggerFactory.getLogger(WhoDAO.class);

	/**
	 * Get All Owners
	 * @return ArrayList of WhoDTO
	 * @throws Exception, return when an error is occurred.
	 */
	public static ArrayList<WhoDTO> getAllOwners() throws Exception {
		ArrayList<WhoDTO> ownersList = new ArrayList<WhoDTO>();
		DBI dbi = JDBIUtil.getInstance();
		Handle h = dbi.open();
		try {
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM tsxwho ");
			sb.append("WHERE isvalid = 1 ");
			sb.append("ORDER BY ownerid");
	
			List<Map<String, Object>> rs = h.select(sb.toString());
	
			for (int i = 0; i < rs.size(); i++) {
				WhoDTO whoDTO = getWhoDTOFromResultsMap(rs.get(i));
				ownersList.add(whoDTO);
			}
		} catch (Exception e) {
			log.debug("getAllOwners() failed");
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
	private static WhoDTO getWhoDTOFromResultsMap(Map<String, Object> resultsMap) throws SQLException {
		WhoDTO whoDTO = new WhoDTO();

		int id = (Integer)resultsMap.get("tsxwhodid");
		String ownerId = (String)resultsMap.get("ownerid");
		String tokenUrl = (String)resultsMap.get("tokenurl");
		long defaultConnectionResetTime = (Long)resultsMap.get("defaultconnectionresettime");
		boolean isValid = (Boolean)resultsMap.get("isvalid");
		Timestamp createdDate = (Timestamp)resultsMap.get("createddate");
		int uc = (Integer)resultsMap.get("uc");

		whoDTO.setId(id);
		whoDTO.setOwnerId(ownerId);
		whoDTO.setTokenUrl(tokenUrl);
		whoDTO.setDefaultConnectionRestTime(defaultConnectionResetTime);
		whoDTO.setValid(isValid);
		whoDTO.setCreatedDate(createdDate);
		whoDTO.setUc(uc);

		return whoDTO;
	}
}
