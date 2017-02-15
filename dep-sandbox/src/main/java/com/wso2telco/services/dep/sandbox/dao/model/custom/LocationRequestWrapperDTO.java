package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class LocationRequestWrapperDTO extends RequestDTO {

	private static final long serialVersionUID = -1548321785136669498L;
	private String address;
	private String requestedAccuracy;

	public String getAddress() {
		if (address != null && address.trim().length() > 0) {
			return address.trim().replace(" ", "");
		}else{
			return null;
		}
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRequestedAccuracy() {
		return requestedAccuracy;
	}

	public void setRequestedAccuracy(String requestedAccuracy) {
		this.requestedAccuracy = requestedAccuracy;
	}

}
