package com.wso2telco.services.dep.sandbox.dao.hibernate;

import com.wso2telco.core.mi.ConfigReader;

public class HibernateFactory {

	public HibernateCommonDAO getHibernateCommonDAO(){
		return new HibernateCommonDAO();
	}
	
	public HibernateLocationDAO getHibernateLocationDAO(){
		return new HibernateLocationDAO();
	}
	
	public HibernateSMSMessagingDAO getHibernateSMSMessagingDAO(){
		return new HibernateSMSMessagingDAO();
	}
	
	public HibernateProvisioningDAO getHibernateProvisioningDAO() {
		return new HibernateProvisioningDAO();
	}
}
