/*******************************************************************************
 * Copyright (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) 
 * 
 *  All Rights Reserved. WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.ussdstub.exception;

// TODO: Auto-generated Javadoc
/**
 * The Enum ErrorCodes.
 */
public enum ErrorCodes {

	/** The error database connection initialization. */
	ERROR_DATABASE_CONNECTION_INITIALIZATION("NS-0001", "error in database connection initialization"),
	
	/** The error database connection close. */
	ERROR_DATABASE_CONNECTION_CLOSE("NS-0002", "error in database connection closing"),
	
	/** The error resultset close. */
	ERROR_RESULTSET_CLOSE("NS-0003", "error in resultset closing"),
	
	/** The error statement close. */
	ERROR_STATEMENT_CLOSE("NS-0004", "error in statement closing"),
	
	/** The error netty transports yaml not found. */
	ERROR_NETTY_TRANSPORTS_YAML_NOT_FOUND("NS-0005", "netty-transports.yml file not found"),
	
	/** The error netty transports yaml read. */
	ERROR_NETTY_TRANSPORTS_YAML_READ("NS-0006", "error in reading netty-transports.yml"),
	
	/** The error database operation. */
	ERROR_DATABASE_OPERATION("NS-0007", "error in database operation"),
	
	/** The error invalid http port id. */
	ERROR_INVALID_HTTP_PORT_ID("NS-0008", "invalid http port id"),
	
	/** The error http port id not found. */
	ERROR_HTTP_PORT_ID_NOT_FOUND("NS-0009", "http port id not found"),
	
	/** The error http port not found. */
	ERROR_HTTP_PORT_NOT_FOUND("NS-0010", "http port not found"),
	
	/** The error invalid netty transports yaml format. */
	ERROR_INVALID_NETTY_TRANSPORTS_YAML_FORMAT("NS-0011", "invalid format of netty-transports.yml"),
	
	/** The error unreadable netty transports yaml. */
	ERROR_UNREADABLE_NETTY_TRANSPORTS_YAML("NS-0012", "unreadable format of netty-transports.yml"),
	
	/** The error http port initialization. */
	ERROR_HTTP_PORT_INITIALIZATION("NS-0013", "error in http port initialization"),
	
	/** The error invalid https port id. */
	ERROR_INVALID_HTTPS_PORT_ID("NS-0014", "invalid https port id"),
	
	/** The error https port id not found. */
	ERROR_HTTPS_PORT_ID_NOT_FOUND("NS-0015", "https port id not found"),
	
	/** The error https port not found. */
	ERROR_HTTPS_PORT_NOT_FOUND("NS-0016", "https port not found"),
	
	/** The error https port initialization. */
	ERROR_HTTPS_PORT_INITIALIZATION("NS-0017", "error in https port initialization"),
	
	/** The error invalid datasource. */
	ERROR_INVALID_DATASOURCE("NS-0018", "invalid datasource"),
	
	/** The error datasource not found. */
	ERROR_DATASOURCE_NOT_FOUND("NS-0019", "datasource not found"),
	
	/** The error database driver not found. */
	ERROR_DATABASE_DRIVER_NOT_FOUND("NS-0020", "database driver not found"),
	
	/** The error database connection url not found. */
	ERROR_DATABASE_CONNECTION_URL_NOT_FOUND("NS-0021", "database connection url not found"),
	
	/** The error database host not found. */
	ERROR_DATABASE_HOST_NOT_FOUND("NS-0022", "database host not found"),
	
	/** The error database port not found. */
	ERROR_DATABASE_PORT_NOT_FOUND("NS-0023", "database port not found"),
	
	/** The error database name not found. */
	ERROR_DATABASE_NAME_NOT_FOUND("NS-0024", "database name not found"),
	
	/** The error database auto reconnect configuration not found. */
	ERROR_DATABASE_AUTO_RECONNECT_CONFIGURATION_NOT_FOUND("NS-0025", "database autoReconnect configuration not found"),
	
	/** The error database username not found. */
	ERROR_DATABASE_USERNAME_NOT_FOUND("NS-0026", "database username not found"),
	
	/** The error database password not found. */
	ERROR_DATABASE_PASSWORD_NOT_FOUND("NS-0027", "database password not found"),
	
	/** The error database configuration initialization. */
	ERROR_DATABASE_CONFIGURATION_INITIALIZATION("NS-0028", "error in database configuration initialization"),
	
	/** The error database connection instantiation. */
	ERROR_DATABASE_CONNECTION_INSTANTIATION("NS-0029", "error in database connection instantiation"),
	
	/** The error database access. */
	ERROR_DATABASE_ACCESS("NS-0030", "error in database access"),
	
	/** The error database class not found. */
	ERROR_DATABASE_CLASS_NOT_FOUND("NS-0031", "database class not found"),
	
	/** The error database requested resource not found. */
	ERROR_DATABASE_REQUESTED_RESOURCE_NOT_FOUND("NS-0032", "requested resource not found in database"), 
	
	/** The error keystore path not found. */
	ERROR_KEYSTORE_PATH_NOT_FOUND("NS-0033", "keystore path not found"), 
	
	/** The error keystore password not found. */
	ERROR_KEYSTORE_PASSWORD_NOT_FOUND("NS-0034", "keystore password not found"), 
	
	/** The error user input pin not found. */
	ERROR_USER_INPUT_PIN_NOT_FOUND("NS-0035", "keystore user input pin not found"), 
	
	/** The error user input ok not found. */
	ERROR_USER_INPUT_OK_NOT_FOUND("NS-0036", "keystore user input ok not found"), 
	
	/** The error delay not found. */
	ERROR_DELAY_NOT_FOUND("NS-0037", "delay not found");
	
	/** The error code. */
	private String errorCode;
	
	/** The error description. */
	private String errorDescription;
	
	/**
	 * Instantiates a new error codes.
	 *
	 * @param errorCode the error code
	 * @param errorDescription the error description
	 */
	ErrorCodes(String errorCode, String errorDescription){
		
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
	}
	
	/**
	 * Gets the error code.
	 *
	 * @return the error code
	 */
	public String getErrorCode(){
		
		return this.errorCode;
	}
	
	/**
	 * Gets the error description.
	 *
	 * @return the error description
	 */
	public String getErrorDescription(){
		
		return this.errorDescription;
	}
}
