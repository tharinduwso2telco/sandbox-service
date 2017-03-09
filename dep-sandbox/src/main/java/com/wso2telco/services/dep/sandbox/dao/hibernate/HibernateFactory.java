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
	
	public HibernateProvisioningDAO getHibernateProvisioningDAO() {
		return new HibernateProvisioningDAO();
	}
	
	public HibernateUserDAO getHibernateUserDAO() {
		return new HibernateUserDAO();
	}
	
	public HibernateCustomerInfoDAO getHibernateCustomerInfoDAO() {
		return new HibernateCustomerInfoDAO();
	}
	
	public HibernateNumberDAO getHibernateNumberDAO() {
		return new HibernateNumberDAO();
	}
	
	public HibernateWalletDAO getHibernateWalletDAO() {
		return new HibernateWalletDAO();
	}

	public HibernatePaymentDAO getHibernatePaymentDAO() {
		return new HibernatePaymentDAO();
	}
	
	public HibernateLoggingDao getHibernateLoggingDao() {
		return new HibernateLoggingDao();
	}
	
	public HibernateCreditDAO getHibernateCreditDAO() {
		return new HibernateCreditDAO();
	}

	public HibernateUSSDDAO getHibernateUSSDDAO() {
		return new HibernateUSSDDAO();
	}
}
