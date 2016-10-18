package com.wso2telco.services.dep.sandbox.dao.model.custom;

import java.util.ArrayList;
import java.util.List;

import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionAllService;
import com.wso2telco.services.dep.sandbox.servicefactory.provisionConfig.RetrieveServicesUserResponseWrapper;

public class UserServiceList {
    
    private List<ServiceInfo> serviceInfo = new ArrayList<>();


	/**
	 * @return the serviceInfo
	 */
	public List<ServiceInfo> getServiceInfoList() {
		return serviceInfo;
	}

	/**
	 * @param serviceInfo
	 *            to set
	 */
	public void setServiceInfoList(List<ServiceInfo> serviceInfo) {
		this.serviceInfo = serviceInfo;
		
	}
	/**
	 * 
	 * @return the newly added serviceInfo
	 */
	public ServiceInfo addNewServiceInfo() {
	    ServiceInfo serviceInfo = new ServiceInfo();
		this.serviceInfo.add(serviceInfo);
		return serviceInfo;
	}
}

