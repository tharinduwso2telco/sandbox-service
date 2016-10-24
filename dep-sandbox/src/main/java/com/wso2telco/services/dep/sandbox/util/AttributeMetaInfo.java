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

	creditLimit(OPTIONAL), balance(OPTIONAL), outstanding(OPTIONAL), currency(
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

}