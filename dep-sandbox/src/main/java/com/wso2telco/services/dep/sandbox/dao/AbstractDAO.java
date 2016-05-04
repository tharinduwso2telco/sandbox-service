package com.wso2telco.services.dep.sandbox.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

class AbstractDAO {

	private static final SessionFactory sessionFactory;

	static {
		try {
			
			Configuration configuration = new Configuration().configure();
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties());
			sessionFactory = configuration.buildSessionFactory(builder.build());
		} catch (Throwable ex) {
			
			throw new ExceptionInInitializerError(ex);
		}
	}



	/**
	 * 
	 * @return
	 * @throws HibernateException
	 */
	protected static Session getSession() throws HibernateException {
		
		return sessionFactory.openSession();
		//return sessionFactory.getCurrentSession();
	}
}
