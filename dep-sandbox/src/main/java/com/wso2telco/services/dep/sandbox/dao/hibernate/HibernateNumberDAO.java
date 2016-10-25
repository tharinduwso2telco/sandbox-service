package com.wso2telco.services.dep.sandbox.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;

public class HibernateNumberDAO extends AbstractDAO implements NumberDAO {

	{
		LOG = LogFactory.getLog(HibernateCommonDAO.class);
	}

	@Override
	public void saveManageNumbers(ManageNumber manageNumber) throws Exception {

		try {
			saveOrUpdate(manageNumber);
		} catch (Exception e) {
			LOG.error("saveManageNumbers", e);
		}

	}

	@Override
	public List<ManageNumber> getManageNumbers(int userId) throws Exception {
		Session sess = getSession();

		List<ManageNumber> manageNumbers = null;
		try {
			manageNumbers = sess.createQuery("from ManageNumber where user.id = :userid").setParameter("userid", userId)
					.list();
		} catch (Exception e) {
			LOG.error("getManageNumbers", e);
		} finally {
			sess.close();
		}
		return manageNumbers;
	}

}
