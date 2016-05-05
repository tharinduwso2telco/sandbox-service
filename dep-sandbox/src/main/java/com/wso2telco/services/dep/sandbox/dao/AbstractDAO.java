package com.wso2telco.services.dep.sandbox.dao;

import java.io.File;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

class AbstractDAO {

	private static final SessionFactory sessionFactory;
	private final static String FILE_NAME = "./hibernate.cfg.xml";
	static {

		try {

			File f = new File(FILE_NAME);
			sessionFactory = new Configuration().configure(f).buildSessionFactory();
		} catch (Throwable ex) {

			System.out.println("error --------------------------- : " + ex.getMessage());
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
	}
}
