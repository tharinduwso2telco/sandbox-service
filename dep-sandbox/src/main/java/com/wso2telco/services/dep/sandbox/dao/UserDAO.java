package com.wso2telco.services.dep.sandbox.dao;

import org.hibernate.Session;

import com.wso2telco.services.dep.sandbox.dao.model.domain.User;


public class UserDAO extends AbstractDAO {

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
