package com.wso2telco.services.dep.sandbox.dao;

import com.wso2telco.services.dep.sandbox.dao.hibernate.HibernateFactory;

public class DaoFactory {

    public static GenaricDAO getGenaricDAO() {
	return new HibernateFactory().getHibernateCommonDAO();
    }

    public static LocationDAO getLocationDAO() {
	return new HibernateFactory().getHibernateLocationDAO();
    }

    public static SMSMessagingDAO getSMSMessagingDAO() {
	return new HibernateFactory().getHibernateSMSMessagingDAO();
    }

    public static ProvisioningDAO getProvisioningDAO() {
	return new HibernateFactory().getHibernateProvisioningDAO();
    }

    public static UserDAO getUserDAO() {
	return new HibernateFactory().getHibernateUserDAO();
    }

    public static CustomerInfoDAO getCustomerInfoDAO() {
	return new HibernateFactory().getHibernateCustomerInfoDAO();
    }
    
    public static NumberDAO getNumberDAO() {
    return new HibernateFactory().getHibernateNumberDAO();
    }

    public static WalletDAO getWalletDAO(){
    	return new HibernateFactory().getHibernateWalletDAO();
    }
    
    public static LoggingDAO getLoggingDAO(){
    	return new HibernateFactory().getHibernateLoggingDao();
    }
    
    public static CreditDAO getCreditDAO(){
    	return new HibernateFactory().getHibernateCreditDAO();

    }
}
