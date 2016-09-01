package com.wso2telco.services.dep.sandbox.dao.hibernate;

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
}
