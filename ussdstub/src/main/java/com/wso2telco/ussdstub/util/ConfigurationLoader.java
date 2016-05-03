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

package com.wso2telco.ussdstub.util;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wso2telco.ussdstub.exception.ErrorCodes;
import com.wso2telco.ussdstub.exception.USSDException;


/**
 * The Class ConfigurationLoader.
 * @author Yasith Lokuge
 */
public class ConfigurationLoader {
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(ConfigurationLoader.class);
	
	/** The http port. */
	private static Integer httpPort = null;
	
	/** The https port. */
	private static Integer httpsPort = null;
	
	/** The keystore_path. */
	private static String keystore_path = null;
	
	/** The keystore_password. */
	private static String keystore_password = null;
	
	/** The userinput_pin. */
	private static String userinput_pin = null;
	
	/** The userinput_ok. */
	private static String userinput_ok = null;
	
	/** The delay. */
	private static String delay = null;
	
	
	
	/**
	 * Load all configurations.
	 *
	 * @throws USSDException the USSD exception
	 */
	public static void loadAllConfigurations() throws USSDException {

		log.debug("Loading all configurations........");
		try {
			loadUssdConfiguration();
			setHttpPort();
			setHttpsPort();
			
		} catch (USSDException e) {

			throw e;
		}
	}
	
	/**
	 * Load ussd configuration.
	 *
	 * @throws USSDException the USSD exception
	 */
	private static void loadUssdConfiguration() throws USSDException{
		
		log.debug("Locading USSD Configuration.....");
		
		Map yamlMap = null;
		
		JSONObject objJSONObject = null;
		
			try {
				yamlMap = YAMLReader.getYamlMap();
				objJSONObject = new JSONObject(yamlMap);
				JSONArray listenerConfigurationsArray = objJSONObject.getJSONArray("ussdStubConfigurations");

				if (listenerConfigurationsArray.length() != 0) {

					JSONObject conf = listenerConfigurationsArray.getJSONObject(0);

					if (!conf.isNull("keystore_path")) {
						
						String keyPath = nullOrTrimmed(conf.getString("keystore_path"));
						setKeyStorePath(keyPath);
						log.debug("ConfigurationLoader keystore_path -> keystore_path : " + keyPath);
						
					} else {

						log.error("Error in ConfigurationLoader loadUssdConfiguration : throw keystore path not found exception");
						throw new USSDException(ErrorCodes.ERROR_KEYSTORE_PATH_NOT_FOUND);
					}

					if (!conf.isNull("keystore_password")) {

						String keyPass = nullOrTrimmed(conf.getString("keystore_password"));
						setKeyStorePassword(keyPass);
						log.debug("ConfigurationLoader keystore password loaded");
					} else {

						log.error("Error in ConfigurationLoader loadUssdConfiguration : throw http password not found exception");
						throw new USSDException(ErrorCodes.ERROR_KEYSTORE_PASSWORD_NOT_FOUND);
					}
					
					if (!conf.isNull("userinput_pin")) {
						int userInputPinInt = conf.getInt("userinput_pin");
						String userInputPin = nullOrTrimmed(Integer.toString(userInputPinInt));
						setUserInputPin(userInputPin);
						log.debug("ConfigurationLoader user input pin loaded");
					} else {

						log.error("Error in ConfigurationLoader loadUssdConfiguration : throw user input pin not found exception");
						throw new USSDException(ErrorCodes.ERROR_USER_INPUT_PIN_NOT_FOUND);
					}
					
					if (!conf.isNull("userinput_ok")) {
						int userInputOkInt = conf.getInt("userinput_ok");
						String userInputOk = nullOrTrimmed(Integer.toString(userInputOkInt));
						setUserInputOk(userInputOk);
						log.debug("ConfigurationLoader user input ok loaded");
					} else {

						log.error("Error in ConfigurationLoader loadUssdConfiguration : throw user input ok not found exception");
						throw new USSDException(ErrorCodes.ERROR_USER_INPUT_OK_NOT_FOUND);
					}
					
					if (!conf.isNull("delay")) {
						
						int delayInt = conf.getInt("delay");
						String delay = nullOrTrimmed(Integer.toString(delayInt));
						setDelay(delay);
						log.debug("ConfigurationLoader delay loaded");
					} else {

						log.error("Error in ConfigurationLoader loadUssdConfiguration : throw user delay found exception");
						throw new USSDException(ErrorCodes.ERROR_DELAY_NOT_FOUND);
					}
										
				} else {

					log.error("Error in ConfigurationLoader loadUssdConfiguration : throw invalid file format exception");
					throw new USSDException(ErrorCodes.ERROR_INVALID_NETTY_TRANSPORTS_YAML_FORMAT);
				}
			} catch (JSONException e) {		
				log.error("Error in ConfigurationLoader -> throw unreadable file format exception : ", e);
				e.printStackTrace();
			}catch (USSDException e) {
				throw e;
			} 		
	} 
	
	/**
	 * Gets the key store path.
	 *
	 * @return the key store path
	 */
	public static String getKeyStorePath() {
		return keystore_path;
	}

	/**
	 * Sets the key store path.
	 *
	 * @param keystore_path the new key store path
	 */
	public static void setKeyStorePath(String keystore_path) {
		ConfigurationLoader.keystore_path = keystore_path;
	}

	/**
	 * Gets the key store password.
	 *
	 * @return the key store password
	 */
	public static String getKeyStorePassword() {
		return keystore_password;
	}

	/**
	 * Sets the key store password.
	 *
	 * @param keystore_password the new key store password
	 */
	public static void setKeyStorePassword(String keystore_password) {
		ConfigurationLoader.keystore_password = keystore_password;
	}

	/**
	 * Gets the user input pin.
	 *
	 * @return the user input pin
	 */
	public static String getUserInputPin() {
		return userinput_pin;
	}

	/**
	 * Sets the user input pin.
	 *
	 * @param userinput_pin the new user input pin
	 */
	public static void setUserInputPin(String userinput_pin) {
		ConfigurationLoader.userinput_pin = userinput_pin;
	}

	/**
	 * Gets the delay.
	 *
	 * @return the delay
	 */
	public static String getDelay() {
		return delay;
	}

	/**
	 * Sets the delay.
	 *
	 * @param delay the new delay
	 */
	public static void setDelay(String delay) {
		ConfigurationLoader.delay = delay;
	}

	/**
	 * Sets the http port.
	 *
	 * @throws USSDException the USSD exception
	 */
	private static void setHttpPort() throws USSDException {

		log.debug("Initilazing http port........");
		Map yamlMap = null;
		JSONObject objJSONObject = null;
		String id = null;

		try {

			yamlMap = YAMLReader.getYamlMap();
			objJSONObject = new JSONObject(yamlMap);
			JSONArray listenerConfigurationsArray = objJSONObject.getJSONArray("listenerConfigurations");

			if (listenerConfigurationsArray.length() != 0) {

				JSONObject msf4jHTTP = listenerConfigurationsArray.getJSONObject(0);

				if (!msf4jHTTP.isNull("id")) {

					id = nullOrTrimmed(msf4jHTTP.getString("id"));
					log.debug("ConfigurationLoader setHttpPort -> id : " + id);

					if (!id.equals("msf4j-http")) {

						log.error("Error in ConfigurationLoader setHttpPort : throw invalid http port id exception");
						throw new USSDException(ErrorCodes.ERROR_INVALID_HTTP_PORT_ID);
					}
				} else {

					log.error("Error in ConfigurationLoader setHttpPort : throw http port id found exception");
					throw new USSDException(ErrorCodes.ERROR_HTTP_PORT_ID_NOT_FOUND);
				}

				if (!msf4jHTTP.isNull("port")) {

					httpPort = msf4jHTTP.getInt("port");
					log.debug("ConfigurationLoader setHttpPort -> http port : " + httpPort);
				} else {

					log.error("Error in ConfigurationLoader setHttpPort : throw http port not found exception");
					throw new USSDException(ErrorCodes.ERROR_HTTP_PORT_NOT_FOUND);
				}
			} else {

				log.error("Error in ConfigurationLoader setHttpPort : throw invalid file format exception");
				throw new USSDException(ErrorCodes.ERROR_INVALID_NETTY_TRANSPORTS_YAML_FORMAT);
			}
		} catch (JSONException e) {

			log.error("Error in ConfigurationLoader setHttpPort -> throw unreadable file format exception : ", e);
			throw new USSDException(ErrorCodes.ERROR_UNREADABLE_NETTY_TRANSPORTS_YAML, e);
		} catch (USSDException e) {

			throw e;
		} catch (Exception e) {

			log.error("Error in ConfigurationLoader setHttpPort -> throw unexpected error exception : ", e);
			throw new USSDException(ErrorCodes.ERROR_HTTP_PORT_INITIALIZATION, e);
		}
	}

	/**
	 * Sets the https port.
	 *
	 * @throws USSDException the USSD exception
	 */
	private static void setHttpsPort() throws USSDException {

		log.debug("Initilazing https port........");
		Map yamlMap = null;
		JSONObject objJSONObject = null;
		String id = null;

		try {

			yamlMap = YAMLReader.getYamlMap();
			objJSONObject = new JSONObject(yamlMap);

			JSONArray listenerConfigurationsArray = objJSONObject.getJSONArray("listenerConfigurations");

			if (listenerConfigurationsArray.length() != 0) {

				JSONObject msf4jHTTPS = listenerConfigurationsArray.getJSONObject(1);

				if (!msf4jHTTPS.isNull("id")) {

					id = nullOrTrimmed(msf4jHTTPS.getString("id"));
					log.debug("ConfigurationLoader setHttpsPort -> id : " + id);

					if (!id.equals("msf4j-https")) {

						log.error("Error in ConfigurationLoader setHttpsPort : throw invalid https port id exception");
						throw new USSDException(ErrorCodes.ERROR_INVALID_HTTPS_PORT_ID);
					}
				} else {

					log.error("Error in ConfigurationLoader setHttpsPort : throw https port id not found exception");
					throw new USSDException(ErrorCodes.ERROR_HTTPS_PORT_ID_NOT_FOUND);
				}

				if (!msf4jHTTPS.isNull("port")) {

					httpsPort = msf4jHTTPS.getInt("port");
					log.debug("ConfigurationLoader setHttpsPort -> https port : " + httpsPort);
				} else {

					log.error("Error in ConfigurationLoader setHttpsPort : throw https port not found exception");
					throw new USSDException(ErrorCodes.ERROR_HTTPS_PORT_NOT_FOUND);
				}
			} else {

				log.error("Error in ConfigurationLoader setHttpsPort : throw invalid file format exception");
				throw new USSDException(ErrorCodes.ERROR_INVALID_NETTY_TRANSPORTS_YAML_FORMAT);
			}
		} catch (JSONException e) {

			log.error("Error in ConfigurationLoader setHttpsPort -> throw unreadable file format exception : ", e);
			throw new USSDException(ErrorCodes.ERROR_UNREADABLE_NETTY_TRANSPORTS_YAML, e);
		} catch (USSDException e) {

			throw e;
		} catch (Exception e) {

			log.error("Error in ConfigurationLoader setHttpsPort -> throw unexpected error exception : ", e);
			throw new USSDException(ErrorCodes.ERROR_HTTPS_PORT_INITIALIZATION, e);
		}
	}
	
	
	/**
	 * Gets the http port.
	 *
	 * @return the http port
	 */
	public static Integer getHttpPort() {
		return httpPort;
	}

	/**
	 * Gets the https port.
	 *
	 * @return the https port
	 */
	public static Integer getHttpsPort() {
		return httpsPort;
	}
	
	/**
	 * Null or trimmed.
	 *
	 * @param s the s
	 * @return the string
	 */
	private static String nullOrTrimmed(String s) {

		String rv = null;
		if (s != null && s.trim().length() > 0) {

			rv = s.trim();
		}

		return rv;
	}

	/**
	 * Gets the user input ok.
	 *
	 * @return the user input ok
	 */
	public static String getUserInputOk() {
		return userinput_ok;
	}

	/**
	 * Sets the user input ok.
	 *
	 * @param userinput_ok the new user input ok
	 */
	public static void setUserInputOk(String userinput_ok) {
		ConfigurationLoader.userinput_ok = userinput_ok;
	}
}
