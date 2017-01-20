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
package com.wso2telco.services.dep.sandbox.util;

public class AttributeMetaInfo {

    private final static String MANDATORY = "Mandatory";
    private final static String OPTIONAL = "Optional";

    public enum BasicField implements AttributeEnum {

	title(OPTIONAL), firstName(MANDATORY), lastName(MANDATORY), dob(
		OPTIONAL), address(OPTIONAL);

	private String fieldType;

	BasicField(String fieldType) {
	    this.fieldType = fieldType;
	}

	@Override
	public String getFieldType() {
	    return this.fieldType;
	}
    }

    public enum AddressField implements AttributeEnum {

	line1(MANDATORY), line2(OPTIONAL), line3(OPTIONAL), city(MANDATORY), country(
		MANDATORY);

	private String fieldType;

	AddressField(String fieldType) {
	    this.fieldType = fieldType;
	}

	@Override
	public String getFieldType() {
	    return this.fieldType;
	}
    }

    public enum AccountField implements AttributeEnum {

	type(MANDATORY), status(MANDATORY);

	private String fieldType;

	AccountField(String fieldType) {
	    this.fieldType = fieldType;
	}

	@Override
	public String getFieldType() {
	    return this.fieldType;
	}
    }

    public enum BillingField implements AttributeEnum {

	creditLimit(OPTIONAL), balance(OPTIONAL), outStanding(OPTIONAL), currency(
		OPTIONAL);

	private String fieldType;

	BillingField(String fieldType) {
	    this.fieldType = fieldType;
	}

	@Override
	public String getFieldType() {
	    return this.fieldType;
	}
    }

    public enum IdentificationField implements AttributeEnum {

	type(MANDATORY), number(MANDATORY), expiry(MANDATORY);

	private String fieldType;

	IdentificationField(String fieldType) {
	    this.fieldType = fieldType;
	}

	@Override
	public String getFieldType() {
	    return this.fieldType;
	}
    }

    public enum AdditionalInfoField implements AttributeEnum {

	tag(OPTIONAL), value(OPTIONAL);

	private String fieldType;

	AdditionalInfoField(String fieldType) {
	    this.fieldType = fieldType;
	}

	@Override
	public String getFieldType() {
	    return this.fieldType;
	}
    }

    public enum Profile implements AttributeEnum {
	title(OPTIONAL), firstName(MANDATORY), lastName(MANDATORY), dob(
		OPTIONAL), address(OPTIONAL), identificationType(OPTIONAL), identificationNumber(
		OPTIONAL), accountType(OPTIONAL), ownerType(OPTIONAL), status(
		OPTIONAL), additionalInfo(OPTIONAL);

	private String fieldType;

	Profile(String fieldType) {
	    this.fieldType = fieldType;
	}

	@Override
	public String getFieldType() {
	    return this.fieldType;
	}

    }

    public enum Attribute implements AttributeEnum {
	basic, billing, account, identification;

	@Override
	public String getFieldType() {
	    // TODO Auto-generated method stub
	    return null;
	}

    }

}