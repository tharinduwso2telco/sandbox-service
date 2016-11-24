package com.wso2telco.services.dep.sandbox.dao.hibernate;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

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
			throw e;
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
			throw e;
		} finally {
			sess.close();
		}
		return manageNumbers;
	}

	@Override
	public ManageNumber getNumber(String number, String username) throws Exception {
		Session session = getSession();
		ManageNumber userNumber = null;

		try {
			StringBuilder hqlQueryBuilder = new StringBuilder();
			hqlQueryBuilder.append("from ManageNumber where Number = :number ");
			hqlQueryBuilder.append(" and user.userName=:userName");

			Query query = session.createQuery(hqlQueryBuilder.toString());

			query.setParameter("number", number);
			query.setParameter("userName", username);

			userNumber = (ManageNumber) query.getSingleResult();

		} catch (NoResultException e) {
			return null;
		} catch (Exception ex) {
			LOG.error("###NUMBER### Error in getNumber", ex);
			throw ex;
		}

		return userNumber;
	}

}
