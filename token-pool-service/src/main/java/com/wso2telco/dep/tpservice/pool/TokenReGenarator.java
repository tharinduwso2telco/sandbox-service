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

package com.wso2telco.dep.tpservice.pool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.util.exception.BusinessException;
import com.wso2telco.dep.tpservice.util.exception.GenaralError;

public class TokenReGenarator {

	private static Logger log = LoggerFactory.getLogger(TokenReGenarator.class);

	/**
	 * 
	 * @param who
	 * @param oldToken
	 * @return
	 * @throws BusinessException
	 */
	public TokenDTO reGenarate(final WhoDTO who, final TokenDTO oldToken) throws BusinessException {
		TokenDTO token = new TokenDTO();
		String Strtoken = makeTokenrequest( who.getTokenUrl(), "grant_type=refresh_token&refresh_token="	+ oldToken.getRefreshToken(), ("" + oldToken.getTokenAuth()));

		if (Strtoken != null && Strtoken.length() > 0) {
				try{
					
				JSONObject jsontoken = new JSONObject(Strtoken);
				String newToken = jsontoken.getString("access_token");
				String newRefreshToken = jsontoken.getString("refresh_token");
				Long newTokenValidity = jsontoken.getLong("expires_in");
				token.setAccessToken(newToken);
				token.setCreatedTime(new Date().getTime());
				token.setTokenAuth(oldToken.getTokenAuth());
				token.setRefreshToken(newRefreshToken);
				token.setTokenValidity(newTokenValidity);
				token.setValid(true);
				token.setWhoId(oldToken.getWhoId());
				log.debug("Refresh token re-generation success");

				}
				catch(Exception e){
					log.error("Invalid Authorization Grant Type"+e.getMessage(),e);
					throw new BusinessException(GenaralError.INVALID_GRANT_ERROR);
					
				}
			
		} else {
			log.error("Token regeneration response of "	+ oldToken.getRefreshToken() + " is invalid.");
		}
		return token;

	}

	/**
	 * 
	 * @param tokenurl
	 * @param urlParameters
	 * @param authheader
	 * @return
	 */
	protected String makeTokenrequest(String tokenurl, String urlParameters, String authheader) {
		String retStr =  "";
		HttpURLConnection connection = null;
		InputStream is = null;
		BufferedReader br=null;


		log.debug("url : " + tokenurl + " | urlParameters : " + urlParameters	+ " | authheader : " + authheader);

		if ((tokenurl != null && tokenurl.length() > 0)
				&& (urlParameters != null && urlParameters.length() > 0)
				&& (authheader != null && authheader.length() > 0)) {
			try {

				byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
				int postDataLength = postData.length;
				URL url = new URL(tokenurl);
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setInstanceFollowRedirects(false);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Authorization", authheader);
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("charset", "utf-8");
				connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
				connection.setUseCaches(false);

				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.write(postData);
				wr.flush();
				wr.close();

				if ((connection.getResponseCode() != 200)
						&& (connection.getResponseCode() != 201)
						&& (connection.getResponseCode() != 400)
						&& (connection.getResponseCode() != 401)) {
					log.debug("Failed : HTTP error code : "+ connection.getResponseCode());
					throw new RuntimeException("Failed : HTTP error code : "+ connection.getResponseCode());
				}

				if ((connection.getResponseCode() == 200)
						|| (connection.getResponseCode() == 201)) {
					is = connection.getInputStream();
				} else {
					is = connection.getErrorStream();
				}

				br = new BufferedReader(new InputStreamReader(is));
				String output;
				while ((output = br.readLine()) != null) {
					retStr+=output;

				}
				br.close();
			} catch (Exception e) {
				log.error("[WSRequestService ], makerequest, " + e.getMessage(),e);
				e.printStackTrace();
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
		} else {
			log.error("Token refresh details are invalid.");
		}

		return retStr;
	}

}
