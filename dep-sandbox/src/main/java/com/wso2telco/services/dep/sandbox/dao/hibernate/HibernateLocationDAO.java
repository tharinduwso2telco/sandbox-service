package com.wso2telco.services.dep.sandbox.dao.hibernate;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.wso2telco.services.dep.sandbox.dao.LocationDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.LocationRequestLog;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Locationparam;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

class HibernateLocationDAO extends AbstractDAO implements LocationDAO{


	/**
	 * 
	 * @param userId
	 * @return
	 */
	public Locationparam queryLocationParam(Integer userId) {

		Session sess = null;
		Transaction tx = null;
		Locationparam locparam = null;

		sess = getSession();
		locparam = (Locationparam) sess.createQuery("from Locationparam where user = ?").setInteger(0, userId)
				.uniqueResult();

		return locparam;
	}

	public boolean saveTransaction(String address, Double requestedAccuracy, String tranStatus, User user)
			throws Exception {
		LocationRequestLog locationRequestLog = new LocationRequestLog();
		locationRequestLog.setAddress(address);
		locationRequestLog.setRequestedAccuracy(requestedAccuracy);
		locationRequestLog.setTransactionstatus(tranStatus);
		locationRequestLog.setDate(new Date());
		locationRequestLog.setUser(user);

		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			tx.begin();
			session.beginTransaction();
			session.save(locationRequestLog);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw e;
		}

		return true;
	}

}
