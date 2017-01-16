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
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(value=Include.NON_NULL)
public class Customer {
    private String msisdn;

    private String imsi;

    private String title;

    private String firstName;

    private String lastName;

    private String dob;

    private String identificationType;

    private String identificationNumber;
    
    private String onBehalfOf;

	private String purchaseCategoryCode;

    private String accountType;

    private String ownerType;

    private String status;
    
	private String requestIdentifier;

	private String responseIdentifier;

    private JsonNode address;

    private JsonNode additionalInfo;
    
    private String resourceURL;

    /**
     * @return the msisdn
     */
    public String getMsisdn() {
	return msisdn;
    }

    /**
     * @param msisdn
     *            the msisdn to set
     */
    public void setMsisdn(String msisdn) {
	this.msisdn = msisdn;
    }

    /**
     * @return the imsi
     */
    public String getImsi() {
	return imsi;
    }

    /**
     * @param imsi
     *            the imsi to set
     */
    public void setImsi(String imsi) {
	this.imsi = imsi;
    }

    /**
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
	return firstName;
    }

    /**
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
	return lastName;
    }

    /**
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    /**
     * @return the dob
     */
    public String getDob() {
	return dob;
    }

    /**
     * @param dob
     *            the dob to set
     */
    public void setDob(String dob) {
	this.dob = dob;
    }

    /**
     * @return the identificationType
     */
    public String getIdentificationType() {
	return identificationType;
    }

    /**
     * @param identificationType
     *            the identificationType to set
     */
    public void setIdentificationType(String identificationType) {
	this.identificationType = identificationType;
    }

    /**
     * @return the identificationNumber
     */
    public String getIdentificationNumber() {
	return identificationNumber;
    }

    /**
     * @param identificationNumber
     *            the identificationNumber to set
     */
    public void setIdentificationNumber(String identificationNumber) {
	this.identificationNumber = identificationNumber;
    }

    /**
     * @return the accountType
     */
    public String getAccountType() {
	return accountType;
    }

    /**
     * @param accountType
     *            the accountType to set
     */
    public void setAccountType(String accountType) {
	this.accountType = accountType;
    }

    /**
     * @return the ownerType
     */
    public String getOwnerType() {
	return ownerType;
    }

    /**
     * @param ownerType
     *            the ownderType to set
     */
    public void setOwnerType(String ownerType) {
	this.ownerType = ownerType;
    }

    /**
     * @return the status
     */
    public String getStatus() {
	return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(String status) {
	this.status = status;
    }

    /**
     * @return the address
     */
    public JsonNode getAddress() {
	return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(JsonNode address) {
	this.address = address;
    }

    /**
     * @return the additionalInfo
     */
    public JsonNode getAdditionalInfo() {
	return additionalInfo;
    }

    /**
     * @param additionalInfo
     *            the additionalInfo to set
     */
    public void setAdditionalInfo(JsonNode additionalInfo) {
	this.additionalInfo = additionalInfo;
    }

    /**
     * @return the resourceURL
     */
    public String getResourceURL() {
	return resourceURL;
    }

    /**
     * @param resourceURL the resourceURL to set
     */
    public void setResourceURL(String resourceURL) {
	this.resourceURL = resourceURL;
    }

	public String getOnBehalfOf() {
		return onBehalfOf;
	}

	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}

	public String getPurchaseCategoryCode() {
		return purchaseCategoryCode;
	}

	public void setPurchaseCategoryCode(String purchaseCategoryCode) {
		this.purchaseCategoryCode = purchaseCategoryCode;
	}

	public String getRequestIdentifier() {
		return requestIdentifier;
	}

	public void setRequestIdentifier(String requestIdentifier) {
		this.requestIdentifier = requestIdentifier;
	}

	public String getResponseIdentifier() {
		return responseIdentifier;
	}

	public void setResponseIdentifier(String responseIdentifier) {
		this.responseIdentifier = responseIdentifier;
	}
    
    

}
