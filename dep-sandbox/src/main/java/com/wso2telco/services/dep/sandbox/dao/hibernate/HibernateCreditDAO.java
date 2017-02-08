package com.wso2telco.services.dep.sandbox.dao.hibernate;

import javax.persistence.NoResultException;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.wso2telco.services.dep.sandbox.dao.CreditDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import com.wso2telco.services.dep.sandbox.util.TableName;

public class HibernateCreditDAO extends AbstractDAO implements CreditDAO {
	
	{
		LOG = LogFactory.getLog(HibernateCreditDAO.class);
	}
	

	@Override
	public AttributeValues checkDuplication(Integer userId, String serviceCall, String attributeValue,
			String attributeName) throws Exception {

		Session session = getSession();
		AttributeValues resultSet = null;

		StringBuilder hql = new StringBuilder();

		hql.append("SELECT ");
		hql.append("val ");
		hql.append("FROM ");
		hql.append("AttributeValues as val, ");
		hql.append("APIServiceCalls AS calls, ");
		hql.append("APITypes AS api, ");
		hql.append("AttributeDistribution AS dist, ");
		hql.append("Attributes AS att, ");
		hql.append("ManageNumber AS number ");
		hql.append("WHERE ");
		hql.append("api.id = calls.apiType.id ");
		hql.append("AND calls.apiServiceCallId = dist.serviceCall.apiServiceCallId ");
		hql.append("AND dist.distributionId = val.attributeDistribution.distributionId ");
		hql.append("AND att.attributeId = dist.attribute.attributeId ");
		hql.append("AND number.id = val.ownerdid ");
		hql.append("AND api.apiname =:apiName ");
		hql.append("AND calls.serviceName =:serviceName ");
		hql.append("AND val.tobject =:tableName ");
		hql.append("AND val.value =:attributeValue ");
		hql.append("AND att.attributeName =:attributeName ");
		hql.append("AND number.user.id =:userId ");

		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("apiName", RequestType.CREDIT.toString());
			query.setParameter("serviceName", serviceCall);
			query.setParameter("tableName", TableName.NUMBERS.toString().toLowerCase());
			query.setParameter("attributeValue", attributeValue);
			query.setParameter("attributeName", attributeName);
			query.setParameter("userId", userId);
			resultSet = (AttributeValues) query.uniqueResult();

		} catch (NoResultException e) {
			return null;
		} catch (Exception ex) {
			LOG.error("###CREDIT### Error in Credit Apply Service " , ex);
			throw ex;
		}
		return resultSet;
	}

	@Override
	public AttributeValues getTransactionValue(String endUserId, Integer attributeValue, String attributeName, String serviceCall)
			throws Exception {

		Session session = getSession();
		AttributeValues resultSet = null;
		//String service = ServiceName.ApplyCredit.toString();

		StringBuilder hql = new StringBuilder();
		hql.append("SELECT ");
		hql.append("val ");
		hql.append("FROM ");
		hql.append("AttributeValues AS val, ");
		hql.append("APIServiceCalls AS calls, ");
		hql.append("APITypes AS api, ");
		hql.append("AttributeDistribution AS dist, ");
		hql.append("Attributes AS att ");
		hql.append("WHERE ");
		hql.append("api.id = calls.apiType.id ");
		hql.append("AND calls.apiServiceCallId = dist.serviceCall.apiServiceCallId ");
		hql.append("AND dist.distributionId = val.attributeDistribution.distributionId ");
		hql.append("AND att.attributeId = dist.attribute.attributeId ");
		hql.append("AND api.apiname =:apiName ");
		hql.append("AND val.tobject =:tableName ");
		hql.append("AND att.attributeName =:attributeName ");
		hql.append("AND calls.serviceName =:serviceCall ");
		hql.append("AND val.ownerdid =:attributeValue ");
		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("apiName", RequestType.CREDIT.toString());
			query.setParameter("tableName", TableName.SBXATTRIBUTEVALUE.toString().toLowerCase());
			query.setParameter("attributeName", attributeName);
			query.setParameter("attributeValue", attributeValue);
			query.setParameter("serviceCall", serviceCall);

			resultSet = (AttributeValues) query.uniqueResult();

		} catch (NoResultException e) {
			return null;
		} catch (Exception ex) {
			LOG.error("###CREDIT### Error in Credit Apply Service " , ex);
			throw ex;
		}
		return resultSet;
	}

	@Override
	public Integer saveAttributeValue(AttributeValues valueObj) throws Exception {
		Integer values;
		try {
			saveOrUpdate(valueObj);
			values = valueObj.getAttributeValueId();
		} catch (Exception e) {
			LOG.error("saveAttributeValue", e);
			throw e;
		}
		return values;
    }
}
