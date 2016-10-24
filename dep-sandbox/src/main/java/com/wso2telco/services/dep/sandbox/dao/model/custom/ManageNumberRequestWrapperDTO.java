package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class ManageNumberRequestWrapperDTO extends RequestDTO {

	private static final long serialVersionUID = 7071157967778389826L;

	private ManageNumberRequest manageNumberRequest;

	public ManageNumberRequest getManageNumberRequest() {
		return manageNumberRequest;
	}

	public void setManageNumberRequest(ManageNumberRequest manageNumberRequest) {
		this.manageNumberRequest = manageNumberRequest;
	}

}
