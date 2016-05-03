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
 * The Class USSDException.
 */
public class USSDException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7101686903987680125L;
	
	/** The error. */
	private ErrorCodes error = null;

	/**
	 * Instantiates a new USSD exception.
	 *
	 * @param error the error
	 */
	public USSDException(ErrorCodes error) {

		this.error = error;
	}

	/**
	 * Instantiates a new USSD exception.
	 *
	 * @param error the error
	 * @param e the e
	 */
	public USSDException(ErrorCodes error, Exception e) {
		super(e);

		this.error = error;
	}

	/**
	 * Gets the error.
	 *
	 * @return the error
	 */
	public ErrorCodes getError() {

		return error;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		
		return "USSDException [error=" + error + "]" + super.toString();
	}
}
