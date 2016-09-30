package com.wso2telco.services.dep.sandbox.dao.model.custom;

import java.math.BigDecimal;

import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

public class ServiceDetail {
	
	private String serviceCode;
	
	private String serviceName;

	private String serviceType;

	private String description;

	private BigDecimal serviceCharge;
	
	

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(BigDecimal serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	
	

}
