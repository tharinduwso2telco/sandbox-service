package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class ProvisioningServicesRequestWrapperDTO extends RequestDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ServiceDetail serviceDetail;

	public ServiceDetail getServiceDetail() {
		return serviceDetail;
	}

	public void setServiceDetail(ServiceDetail serviceDetail) {
		this.serviceDetail = serviceDetail;
	}
	
	

}
