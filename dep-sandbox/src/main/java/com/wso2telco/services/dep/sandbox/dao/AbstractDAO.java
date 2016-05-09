package com.wso2telco.services.dep.sandbox.dao;

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
	 * @return
	 * @throws HibernateException
	 */
	protected static Session getSession() throws HibernateException {

		return sessionFactory.openSession();
	}
	
	public List<ManageNumber>  getWhitelisted(int userid, List numbers) {

		Session sess = getSession();

		List<ManageNumber> whitelisted = null;
		try {
			Query query =sess.createQuery("from ManageNumber where user.id = :userid and number  in(:numbers)");
			query.setParameter("userid", userid);
			query.setParameterList( "numbers", numbers);
			whitelisted=query.list();

		} catch (Exception e) {
			System.out.println("getUserWhitelist: " + e);
		} finally {
			sess.close();
		}
		return whitelisted;
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
	 
	 
}
