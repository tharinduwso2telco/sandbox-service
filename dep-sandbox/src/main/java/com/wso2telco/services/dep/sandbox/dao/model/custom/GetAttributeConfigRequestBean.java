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

import com.wso2telco.services.dep.sandbox.dao.model.custom.GetProfileConfigRequestBean.Address;

public class GetAttributeConfigRequestBean {

    private Basic basic;

    private Billing billing;

    private Account account;

    private Identification identification;

    public Basic getBasic() {
	return basic;
    }

    public void setBasic(Basic basic) {
	this.basic = basic;
    }

    public Billing getBilling() {
	return billing;
    }

    public void setBilling(Billing billing) {
	this.billing = billing;
    }

    public Account getAccount() {
	return account;
    }

    public void setAccount(Account account) {
	this.account = account;
    }

    public Identification getIdentification() {
	return identification;
    }

    public void setIdentification(Identification identification) {
	this.identification = identification;
    }

    public static class Billing {

	private String creditLimit;

	private String outStanding;

	private String currency;

	private String balance;

	public String getCreditLimit() {
	    return creditLimit;
	}

	public void setCreditLimit(String creditLimit) {
	    this.creditLimit = creditLimit;
	}

	public String getOutStanding() {
	    return outStanding;
	}

	public void setOutStanding(String outStanding) {
	    this.outStanding = outStanding;
	}

	public String getCurrency() {
	    return currency;
	}

	public void setCurrency(String currency) {
	    this.currency = currency;
	}

	public String getBalance() {
	    return balance;
	}

	public void setBalance(String balance) {
	    this.balance = balance;
	}

    }

    public static class Account {

	private String type;

	private String status;

	public String getType() {
	    return type;
	}

	public void setType(String type) {
	    this.type = type;
	}

	public String getStatus() {
	    return status;
	}

	public void setStatus(String status) {
	    this.status = status;
	}

    }

    public static class Identification {

	private String type;

	private String number;

	private String expiry;

	public String getType() {
	    return type;
	}

	public void setType(String type) {
	    this.type = type;
	}

	public String getNumber() {
	    return number;
	}

	public void setNumber(String number) {
	    this.number = number;
	}

	public String getExpiry() {
	    return expiry;
	}

	public void setExpiry(String expiry) {
	    this.expiry = expiry;
	}

    }

    public static class Basic {

	private String title;

	private String firstName;

	private String lastName;

	private String dob;

	private Address address;

	public String getTitle() {
	    return title;
	}

	public void setTitle(String title) {
	    this.title = title;
	}

	public String getFirstName() {
	    return firstName;
	}

	public void setFirstName(String firstName) {
	    this.firstName = firstName;
	}

	public String getLastName() {
	    return lastName;
	}

	public void setLastName(String lastName) {
	    this.lastName = lastName;
	}

	public String getDob() {
	    return dob;
	}

	public void setDob(String dob) {
	    this.dob = dob;
	}

	public Address getAddress() {
	    return address;
	}

	public void setAddress(Address address) {
	    this.address = address;
	}

    }

}
