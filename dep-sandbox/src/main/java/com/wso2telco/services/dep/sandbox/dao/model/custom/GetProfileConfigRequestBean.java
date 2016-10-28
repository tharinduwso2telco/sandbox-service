/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licenses this file to you under  the Apache License, Version 2.0 (the "License");
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

import java.util.ArrayList;
import java.util.List;

public class GetProfileConfigRequestBean {

    private String title;

    private String firstName;

    private String lastName;

    private String dob;

    private String identificationType;

    private String identificationNumber;

    private String accountType;

    private String ownerType;

    private String status;

    private Address address;

    private List<AdditionalInfo> additionalInfo;

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
     *            the ownerType to set
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
    public Address getAddress() {
	return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(Address address) {
	this.address = address;
    }

    /**
     * @return the additionalInfo
     */
    public List<AdditionalInfo> getAdditionalInfo() {
	return additionalInfo;
    }

    public void setAdditionalInfo(List<AdditionalInfo> additionalInfo) {
	this.additionalInfo = additionalInfo;
    }

    public static class AdditionalInfo {

	private String tag;

	private String value;

	public String getTag() {
	    return tag;
	}

	public void setTag(String tag) {
	    this.tag = tag;
	}

	public String getValue() {
	    return value;
	}

	public void setValue(String value) {
	    this.value = value;
	}
    }

    public static class Address {

	private String line1;

	private String line2;

	private String line3;

	private String city;

	public String getCity() {
	    return city;
	}

	public void setCity(String city) {
	    this.city = city;
	}

	public String getCountry() {
	    return country;
	}

	public void setCountry(String country) {
	    this.country = country;
	}

	private String country;

	public String getLine1() {
	    return line1;
	}

	public void setLine1(String line1) {
	    this.line1 = line1;
	}

	public String getLine2() {
	    return line2;
	}

	public void setLine2(String line2) {
	    this.line2 = line2;
	}

	public String getLine3() {
	    return line3;
	}

	public void setLine3(String line3) {
	    this.line3 = line3;
	}
    }
}
