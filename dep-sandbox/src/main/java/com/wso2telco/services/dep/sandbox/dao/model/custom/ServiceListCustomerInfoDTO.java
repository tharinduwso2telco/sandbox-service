package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class ServiceListCustomerInfoDTO 
{

	/** The list of provisioned services **/
	private  ServiceListCustomerInfoDTO serviceList;

	/**
	 * @return the serviceList
	 */
	public ServiceListCustomerInfoDTO getServiceList() {
		return serviceList;
	}

	/**
	 * @param serviceList
	 *            to set
	 */
	public void setServiceList(ServiceListCustomerInfoDTO serviceList) {
		this.serviceList = serviceList;
	}
}
