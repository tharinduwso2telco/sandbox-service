package com.wso2telco.services.dep.sandbox.dao.hibernate;

import java.io.File;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

public abstract class AbstractDAO {

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
	 * @return Session
	 * @throws HibernateException exception
	 */
	protected static Session getSession() throws HibernateException {

		return sessionFactory.openSession();
	}
	
	
	
	

	 
	 
}
