package com.wso2telco.services.dep.sandbox.dao;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.wso2telco.services.dep.sandbox.dao.model.domain.LocationRequestLog;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Locationparam;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

public class LocationDAO extends AbstractDAO {

	public ManageNumber getWhitelisted(int userid, String num) {

		Session sess = getSession();

		ManageNumber whitelisted = null;
		try {
			whitelisted = (ManageNumber) sess.createQuery("from ManageNumber where user.id = ? and number = ?")
					.setInteger(0, userid).setString(1, num).uniqueResult();

		} catch (Exception e) {
			System.out.println("getUserWhitelist: " + e);
		} finally {
			sess.close();
		}
		return whitelisted;
	}

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

	public boolean saveTransaction(String address, Double requestedAccuracy, String tranStatus, User user) throws Exception {

		LocationRequestLog locationRequestLog = new LocationRequestLog();
		locationRequestLog.setAddress(address);
		locationRequestLog.setRequestedAccuracy(requestedAccuracy);
		locationRequestLog.setTransactionstatus(tranStatus);
		locationRequestLog.setDate(new Date());
		locationRequestLog.setUser(user);

		saveAnyObject(locationRequestLog);

		return true;
	}

}
