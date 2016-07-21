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

package com.wso2telco.dep.tpservice.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.wso2telco.dep.tpservice.dao.TokenDAO;
import com.wso2telco.dep.tpservice.dao.WhoDAO;
import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.util.exception.BusinessException;
import com.wso2telco.dep.tpservice.util.exception.GenaralError;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

public class WhoManager {

	private static Logger log = LoggerFactory.getLogger(WhoManager.class);

	/**
	 * Get All Owners
	 * @return list of owners as whodto
	 * @throws TokenException INTERNAL_SERVER_ERROR server error thrown  
	 */
	public List<WhoDTO> getAllOwners() throws TokenException {
		List<WhoDTO> ownersList = null;
		try {
			WhoDAO whoDao = new WhoDAO();
			ownersList = whoDao.getAllOwners();
		} catch (Exception e) {
			log.error("getAllOwners() failed ", e);
			throw new TokenException(GenaralError.INTERNAL_SERVER_ERROR);
		}
		return ownersList;
	}

	public JSONObject getAllOwnersJSON() throws BusinessException {
		ArrayList<WhoDTO> ownersList = null;
		JSONObject result = new JSONObject();
		try {
			// get list of objects
			WhoDAO whoDao = new WhoDAO();
			ownersList = whoDao.getAllOwners();

			// get json string from list
			Gson gson = new Gson();
			String jsonString = gson.toJson(ownersList);

			// convert to json array and set to result object
			JSONArray resultObject = new JSONArray(jsonString);
			result.put("owners", resultObject);
		} catch (Exception e) {
			log.error("getAllOwnersJSON() failed ", e);
			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}
		return result;
	}
	/**
	 * load all the token for given owner
	 * @param ownerID for filter
	 * @return list of token dtos
	 * @throws TokenException INTERNAL_SERVER_ERROR thrown if unable to load all tokens
	 */ 
	public List<TokenDTO> loadTokens(final String ownerID) throws TokenException {
		
		List<TokenDTO> response;
		TokenDAO tokenLoad = new TokenDAO();
		
		try {
			response = tokenLoad.getAllTokensForOwner(ownerID);
		} catch (SQLException e) {
			log.error("WhoManager","loadTokens()",e.getMessage());
			throw new TokenException(GenaralError.INTERNAL_SERVER_ERROR);
		}
		
	    return response;

	}

	/**
	 * load owner dto for given ownerid
	 * @param ownerid to be filtered
	 * @return valid owner DTO
	 */
	public WhoDTO getOwner(String ownerid){
			
		// get owner details
		WhoDAO ownerDetail = new WhoDAO();
		WhoDTO owner = ownerDetail.getOwner(ownerid);
		log.debug(owner.toString());
		return owner;
	}

}
