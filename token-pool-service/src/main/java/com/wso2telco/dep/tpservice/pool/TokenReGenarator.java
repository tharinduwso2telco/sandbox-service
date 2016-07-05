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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.ws.rs.core.Response.Status;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.util.Constants;
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
		final String grant_type = "grant_type=refresh_token&refresh_token=";
		TokenDTO token = new TokenDTO();
		
		//for the response containing new access & refresh token
		String Strtoken = makeTokenrequest(who.getTokenUrl(), grant_type + oldToken.getRefreshToken(), ("" + oldToken.getTokenAuth()));

		if (Strtoken != null && Strtoken.length() > 0) {

			try {

				JSONObject jsontoken = new JSONObject(Strtoken);
				
				String newToken = jsontoken.getString("access_token");
				String newRefreshToken = jsontoken.getString("refresh_token");
				Long newTokenValidity = jsontoken.getLong("expires_in");
				
				token.setAccessToken(newToken);
				token.setTokenAuth(oldToken.getTokenAuth());
				token.setRefreshToken(newRefreshToken);
				token.setTokenValidity(newTokenValidity);
				token.setValid(true);
				token.setWhoId(oldToken.getWhoId());
				
				log.debug("Refresh token re-generation success");

			} catch (JSONException e) {
				log.error("Invalid Refresh Token for Authorization Grant Type " ,e);
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
	 * @throws BusinessException 
	 */
	protected String makeTokenrequest(String tokenurl, String urlParameters, String authheader) throws BusinessException {
		String retStr = "";
		HttpURLConnection connection = null;
		InputStream is = null;
		BufferedReader br = null;

		log.debug("url : " + tokenurl + " | urlParameters : " + urlParameters + " | authheader : " + authheader);

		if ((tokenurl != null && tokenurl.length() > 0)	&& (urlParameters != null && urlParameters.length() > 0) && (authheader != null && authheader.length() > 0)) {
			try {

				byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
				int postDataLength = postData.length;
				URL url = new URL(tokenurl);
				connection = (HttpURLConnection) url.openConnection();
				
				connection.setDoOutput(true);
				connection.setInstanceFollowRedirects(false);
				connection.setRequestMethod(Constants.URLProperties.URL_METHOD.getValue());
				connection.setRequestProperty(Constants.URLProperties.AUTHORIZATION_GRANT_TYPE.getValue(), authheader);
				connection.setRequestProperty(Constants.URLTypes.CONTENT.getType(), Constants.URLTypes.CONTENT.getValue());
				connection.setRequestProperty(Constants.URLTypes.ENCODING.getType(), Constants.URLTypes.ENCODING.getValue());
				connection.setRequestProperty(Constants.URLProperties.LENGTH.getValue(), Integer.toString(postDataLength));
				connection.setUseCaches(false);

				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.write(postData);
				wr.flush();
				wr.close();

				//filter out invalid http codes
				if ((connection.getResponseCode() == Status.OK.getStatusCode()) || (connection.getResponseCode() == Status.CREATED.getStatusCode())) {
					is = connection.getInputStream();
				} else {
					is = connection.getErrorStream();
				}

				br = new BufferedReader(new InputStreamReader(is));
				String output;
				while ((output = br.readLine()) != null) {
					retStr += output;
				}
			} catch (Exception e) {
				log.error( "[TokenReGenarator ], makerequest, " , e);
				throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
			} finally {
				try {
					br.close();
				} catch (IOException e) {
				}
			
				if (connection != null) {
					connection.disconnect();
				}
			}
		
		}
		return retStr;
	}

}
