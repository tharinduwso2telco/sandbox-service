/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.services.dep.sandbox.dao.model.custom;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author WSO2.Telco
 *
 */
@JsonInclude(value=Include.NON_NULL)
public class ProvisionCommonResponseDTO {

	/** The service code **/
	private String serviceCode;
	
	/** The client correlator **/
	private String clientCorrelator;

	/** The client reference code **/
	private String clientReferenceCode;

	/** The reference for server callback **/
	private String serverReferenceCode;
	
	private String onBehalfOf;
    
    private String purchaseCatergoryCode;

	/** The reference for callback **/
	private CallbackReference callbackReference;

	/** The status of transaction **/
	private String transactionStatus;

	/**
	 * @return the serviceCode
	 */
	public String getServiceCode() {
		return serviceCode;
	}

	/**
	 * @param serviceCode
	 *            to set
	 */
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	/**
	 * @return the clientCorrelator
	 */
	public String getClientCorrelator() {
		return clientCorrelator;
	}

	/**
	 * @param clientCorrelator
	 *            to set
	 */
	public void setClientCorrelator(String clientCorrelator) {
		this.clientCorrelator = clientCorrelator;
	}

	/**
	 * @return the clientReferenceCode
	 */
	public String getClientReferenceCode() {
		return clientReferenceCode;
	}

	/**
	 * @param clientReferenceCode
	 *            to set
	 */
	public void setClientReferenceCode(String clientReferenceCode) {
		this.clientReferenceCode = clientReferenceCode;
	}

	/**
	 * @return the serverReferenceCode
	 */
	public String getServerReferenceCode() {
		return serverReferenceCode;
	}

	/**
	 * @param serverReferenceCode
	 *            to set
	 */
	public void setServerReferenceCode(String serverReferenceCode) {
		this.serverReferenceCode = serverReferenceCode;
	}

	public String getOnBehalfOf() {
		return onBehalfOf;
	}

	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}

	public String getPurchaseCatergoryCode() {
		return purchaseCatergoryCode;
	}

	public void setPurchaseCatergoryCode(String purchaseCatergoryCode) {
		this.purchaseCatergoryCode = purchaseCatergoryCode;
	}

	/**
	 * @return the callbackReference
	 */
	public CallbackReference getCallbackReference() {
		return callbackReference;
	}

	/**
	 * @param callbackReference
	 *            to set
	 */
	public void setCallbackReference(CallbackReference callbackReference) {
		this.callbackReference = callbackReference;
	}

	/**
	 * @return the transactionStatus
	 */
	public String getTransactionStatus() {
		return transactionStatus;
	}

	/**
	 * @param transactionStatus
	 *            to set
	 */
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

}
