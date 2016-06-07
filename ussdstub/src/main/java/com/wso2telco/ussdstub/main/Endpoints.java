/*******************************************************************************
 * Copyright (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) 
 * 
 * All Rights Reserved. WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.ussdstub.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Arrays;

import javax.ws.rs.core.Response;

import org.apache.cxf.helpers.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.ussdstub.dto.InboundMessage;
import com.wso2telco.ussdstub.dto.InboundUSSDMessageRequest;
import com.wso2telco.ussdstub.dto.OutboundUSSDMessageRequest;
import com.wso2telco.ussdstub.dto.ResponseRequest;
import com.wso2telco.ussdstub.dto.USSDAction;
import com.wso2telco.ussdstub.dto.USSDRequest;
import com.wso2telco.ussdstub.util.ConfigurationLoader;
import com.wso2telco.ussdstub.util.YAMLReader;


// TODO: Auto-generated Javadoc
/**
 * The Class Endpoints.
 */
public class Endpoints {
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger((String) Endpoints.class.getName());

	/**
	 * Send ussd one api.
	 *
	 * @param jsonBody the json body
	 * @param senderAddress the sender address
	 * @return the response
	 * @throws SQLException the SQL exception
	 * @throws RemoteException the remote exception
	 * @throws Exception the exception
	 */
	public Response sendUSSDOneAPI(String jsonBody, String senderAddress)
			throws SQLException, RemoteException, Exception {
		JSONObject jsonObj = new JSONObject(jsonBody);
		LOG.info("JSON body Recieved " + jsonBody);
		String notifyURL = jsonObj.getJSONObject("outboundUSSDMessageRequest").getJSONObject("responseRequest")
				.getString("notifyURL");
		String delims = "/";
		String[] tokens = notifyURL.split(delims);
		boolean pinAuthenticator = false;
		pinAuthenticator = Arrays.asList(tokens).contains("pin")
				|| (Arrays.asList(tokens).contains("user-registration") && Arrays.asList(tokens).contains("ussd")
						&& Arrays.asList(tokens).contains("receive") && !Arrays.asList(tokens).contains("push"));
		String userinput = null;
		LOG.info("notifyURL " + Arrays.asList(tokens).toString());
		LOG.info("arr " + notifyURL);
		if (pinAuthenticator) {
			userinput = ConfigurationLoader.getUserInputPin();
			LOG.info("Authenicator: pinAuthenticator");
		} else {
			userinput = ConfigurationLoader.getUserInputOk();
			LOG.info("Authenicator: USSDAuthenticator");
		}

		Gson gson = new GsonBuilder().serializeNulls().create();
		InboundMessage inboundMsg = new InboundMessage();
		ResponseRequest inboundRepReq = new ResponseRequest();
		JSONObject outboundUSSDMessageRequest = jsonObj.getJSONObject("outboundUSSDMessageRequest");
		JSONObject responseRequest = outboundUSSDMessageRequest.getJSONObject("responseRequest");
		inboundRepReq.setCallbackData(responseRequest.getString("callbackData"));
		inboundRepReq.setNotifyURL(responseRequest.getString("notifyURL"));
		InboundUSSDMessageRequest inboundReq = new InboundUSSDMessageRequest();
		inboundReq.setAddress(senderAddress);
		inboundReq.setShortCode(outboundUSSDMessageRequest.getString("shortCode"));
		inboundReq.setKeyword(outboundUSSDMessageRequest.getString("keyword"));
		inboundReq.setInboundUSSDMessage(userinput);
		inboundReq.setUssdAction(USSDAction.mtcont);
		inboundReq.setClientCorrelator(outboundUSSDMessageRequest.getString("clientCorrelator"));
		inboundReq.setResponseRequest(inboundRepReq);
		inboundMsg.setInboundUSSDMessageRequest(inboundReq);
		String jsonObjIDGW = gson.toJson((Object) inboundMsg);
		USSDRequest ussdReq = new USSDRequest();
		ResponseRequest repReq = new ResponseRequest();
		repReq.setCallbackData(responseRequest.getString("callbackData"));
		repReq.setNotifyURL(responseRequest.getString("notifyURL"));
		OutboundUSSDMessageRequest outboundReq = new OutboundUSSDMessageRequest();
		outboundReq.setAddress(senderAddress);
		outboundReq.setShortCode(outboundUSSDMessageRequest.getString("shortCode"));
		outboundReq.setKeyword(outboundUSSDMessageRequest.getString("keyword"));
		outboundReq.setOutboundUSSDMessage(outboundUSSDMessageRequest.getString("outboundUSSDMessage"));
		outboundReq.setUssdAction(outboundUSSDMessageRequest.getString("ussdAction"));
		outboundReq.setClientCorrelator(outboundUSSDMessageRequest.getString("clientCorrelator"));
		outboundReq.setDelivaryStatus("SENT");
		outboundReq.setResponseRequest(repReq);
		ussdReq.setOutboundUSSDMessageRequest(outboundReq);
		String responseString = gson.toJson((Object) ussdReq);
		LOG.info((Object) ("JSON String Recieved: " + responseString));
		String path = ConfigurationLoader.getKeyStorePath();
		LOG.info((Object) ("keystore path: " + path));
		LOG.info("JSON Body sent :" + jsonObjIDGW);
		System.setProperty("javax.net.ssl.trustStore", path);
		System.setProperty("javax.net.ssl.trustStorePassword",ConfigurationLoader.getKeyStorePassword());
		if (this.postToNotifyURL(jsonObjIDGW, notifyURL)) {
			if (this.postToNotifyURL(jsonObjIDGW, notifyURL)) {
				this.postToNotifyURL(jsonObjIDGW, notifyURL);
			}
		}
		return Response.status((int) 200).entity((Object) responseString).build();
	}

	/**
	 * Post to notify url.
	 *
	 * @param jsonObj the json obj
	 * @param notifyURL the notify url
	 * @return true, if successful
	 */
	private boolean postToNotifyURL(String jsonObj, String notifyURL) {
		try {
			HttpPost post = new HttpPost(notifyURL);
			CloseableHttpClient httpclient = null;
			CloseableHttpResponse response = null;
			httpclient = HttpClients.createDefault();
			StringEntity strEntity = new StringEntity(jsonObj.toString(), "UTF-8");
			strEntity.setContentType("application/json");
			post.setEntity((HttpEntity) strEntity);
			LOG.info((Object) ("notify URL: " + notifyURL));
			response = httpclient.execute((HttpUriRequest) post);
			HttpEntity entity = response.getEntity();
			InputStream instream = entity.getContent();
			StringWriter writer = new StringWriter();
			IOUtils.copy((Reader) new InputStreamReader(instream), (Writer) writer, (int) 1024);
			String body = writer.toString();
			LOG.info((Object) ("jsonbody returned: " + body));
			if (!body.isEmpty()) {
				LOG.info((Object) "json body is not empty");
				return true;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		return false;
	}
}