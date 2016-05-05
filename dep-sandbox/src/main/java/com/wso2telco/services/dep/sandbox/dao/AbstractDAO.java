package com.wso2telco.services.dep.sandbox.dao;

import java.io.File;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

public class AbstractDAO {

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
	
	
	 public User getUser(String username) {

	        Session sess = getSession();
	        User usr = null;
	        try {
	            usr = (User) sess.createQuery("from User where userName = ?").setString(0, username).uniqueResult();
	            if (usr == null) {
	                throw new Exception("User Not Found");
	            }

	        } catch (Exception e) {
	            //LOG.error("[ApplicationManager], RemveInbound + " + "Error " + e.getMessage());
	            e.printStackTrace();
	        } finally {
	            sess.close();
	        }

	        return usr;
	    }
	 
	 public void saveAnyObject(Object obj)throws Exception{
		 
		 Session session = getSession();
		 Transaction tx = session.beginTransaction();
		 try {
			tx.begin();
			 session.beginTransaction();
			 session.save(obj);
			 tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw e;
		}
		 
	 }
}
