package com.wso2telco.services.dep.sandbox.dao.hibernate;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.wso2telco.services.dep.sandbox.dao.CreditDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.servicefactory.credit.AttributeName;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import com.wso2telco.services.dep.sandbox.util.TableName;

public class HibernateCreditDAO  extends AbstractDAO implements CreditDAO{
	@Override
	public String getAttributeValue(String endUserId, String serviceCall, String attribute)
			throws Exception {

		Session session = getSession();
		String resultSet = null;

		StringBuilder hql = new StringBuilder();

		hql.append("SELECT ");
		hql.append("val.value ");
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
		hql.append("AND dist.distributionId = val.attributeDistributionId.distributionId ");
		hql.append("AND att.attributeId = dist.attribute.attributeId ");
		hql.append("AND number.id = val.ownerdid ");
		hql.append("AND api.apiname =:apiName ");
		hql.append("AND calls.serviceName =:serviceName ");
		hql.append("AND val.tobject =:tableName ");
		hql.append("AND number.Number =:number ");
		hql.append("AND att.attributeName =:attName");

		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("apiName", RequestType.CREDIT.toString());
			query.setParameter("serviceName", serviceCall);
			query.setParameter("number", endUserId);
			query.setParameter("tableName", TableName.NUMBERS.toString().toLowerCase());
			query.setParameter("attName", attribute);
			resultSet = (String) query.uniqueResult();

		} catch (NoResultException e) {
			return null;
		} catch (Exception ex) {
			LOG.error("###WALLET### Error in Make Payment Service " + ex);
			throw ex;
		}
		return resultSet;
	}
	@Override
	public AttributeDistribution getDistributionValue(
			String serviceCall, String attributeName, String apiType) throws Exception {

		Session session = getSession();
		AttributeDistribution distributionId = null;
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT ");
		hql.append("dist ");
		hql.append("FROM ");
		hql.append("APIServiceCalls AS calls, ");
		hql.append("APITypes AS api, ");
		hql.append("AttributeDistribution AS dist, ");
		hql.append("Attributes AS att ");
		hql.append("WHERE ");
		hql.append("api.id = calls.apiType.id ");
		hql.append("AND calls.apiServiceCallId = dist.serviceCall.apiServiceCallId ");
		hql.append("AND dist.attribute = att.attributeId ");
		hql.append("AND api.apiname =:apiName ");
		hql.append("AND calls.serviceName =:serviceName ");
		hql.append("AND att.attributeName =:attName ");

		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("apiName", apiType);
			query.setParameter("serviceName", serviceCall);
			query.setParameter("attName", attributeName);

			distributionId = (AttributeDistribution) query.uniqueResult();

		} catch (NoResultException e) {
			return null;
			
	} catch (Exception ex) {
			LOG.error("###WALLET### Errorr in WALLET Service " + ex);
			throw ex;
		}
		return distributionId;
	}

	@Override
	public Integer getNumber(String endUserId) throws Exception {
		StringBuilder hqlBuilder = new StringBuilder();
		Session session = getSession();
		Integer number = null;

		hqlBuilder.append("SELECT ");
		hqlBuilder.append("number.id ");
		hqlBuilder.append("FROM ");
		hqlBuilder.append("ManageNumber AS number ");
		hqlBuilder.append("WHERE ");
		hqlBuilder.append("number.Number =:endUserId");

		try {
			Query query = session.createQuery(hqlBuilder.toString());
			query.setParameter("endUserId", endUserId);
			number = (Integer) query.uniqueResult();

		} catch (NoResultException e) {
return null;
} catch (Exception ex) {
			LOG.error("###WALLET### Errorr in WALLET Service " + ex);
			throw ex;
		}
		return number;
	}

	@Override
	public AttributeValues getAttributeValue(AttributeDistribution distributionObj, Integer endUserId)
			throws Exception {
		Session session = getSession();
		AttributeValues value = null;
		

		StringBuilder hql = new StringBuilder();
		hql.append("SELECT ");
		hql.append("val ");
		hql.append("FROM ");
		hql.append("AttributeValues as val ");
		hql.append("WHERE ");
		hql.append("val.attributeDistributionId =:attributeDistributionId ");
		hql.append("AND ");
		hql.append("val.ownerdid =:endUserId ");
		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("attributeDistributionId", distributionObj);
			query.setParameter("endUserId", endUserId);
			value = (AttributeValues)query.uniqueResult();
			
		} catch (Exception e) {
		    LOG.error("getAttributeValue", e);
		    throw e;
		}
		return value;
	}

	@Override
	public AttributeValues checkClientCorrelator(String endUserId, String serviceCall, String clientCorrelator)
			throws Exception {


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
		hql.append("AND dist.distributionId = val.attributeDistributionId.distributionId ");
		hql.append("AND att.attributeId = dist.attribute.attributeId ");
		hql.append("AND number.id = val.ownerdid ");
		hql.append("AND api.apiname =:apiName ");
		hql.append("AND calls.serviceName =:serviceName ");
		hql.append("AND val.tobject =:tableName ");
		hql.append("AND number.Number =:number ");
		hql.append("AND val.value =:clientCorrelator");

		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("apiName", RequestType.CREDIT.toString());
			query.setParameter("serviceName", serviceCall);
			query.setParameter("number", endUserId);
			query.setParameter("tableName", TableName.NUMBERS.toString().toLowerCase());
			query.setParameter("clientCorrelator", clientCorrelator);
			resultSet = (AttributeValues) query.uniqueResult();
/*			
			if(resultSet != null){
				return resultSet;
			}*/

		} catch (NoResultException e) {
return null;		} catch (Exception ex) {
			LOG.error("###WALLET### Error in Make Payment Service " + ex);
			throw ex;
		}
		return resultSet;	
	}

	@Override
	public boolean checkDupplicateValue(String endUserId, String serviceCall, String referenceCode) throws Exception {


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
		hql.append("AND dist.distributionId = val.attributeDistributionId.distributionId ");
		hql.append("AND att.attributeId = dist.attribute.attributeId ");
		//hql.append("AND number.id = val.ownerdid ");
		hql.append("AND api.apiname =:apiName ");
		hql.append("AND calls.serviceName =:serviceName ");
		hql.append("AND val.tobject =:tableName ");
		//hql.append("AND number.Number =:number ");
		hql.append("AND val.value =:clientCorrelator");

		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("apiName", RequestType.CREDIT.toString());
			query.setParameter("serviceName", serviceCall);
			//query.setParameter("number", endUserId);
			query.setParameter("tableName", TableName.NUMBERS.toString().toLowerCase());
			query.setParameter("clientCorrelator", referenceCode);
			resultSet = (AttributeValues) query.uniqueResult();
			
			if(resultSet != null){
				return true;
			}

		} catch (NoResultException e) {
return false;		} catch (Exception ex) {
			LOG.error("###CREDIT### Error in Make Refund Payment Service " + ex);
			throw ex;
		}
		return false;	
	}
	@Override
	public AttributeValues getTransactionValue(String endUserId, Integer attributeValue, String serviceCall) throws Exception {

		Session session = getSession();
		AttributeValues resultSet = null;
		String service = ServiceName.ApplyCredit.toString();

		StringBuilder hql = new StringBuilder();
		hql.append("SELECT ");
		hql.append("val ");
		hql.append("FROM ");
		hql.append("AttributeValues AS val, ");
		hql.append("APIServiceCalls AS calls, ");
		hql.append("APITypes AS api, ");
		hql.append("AttributeDistribution AS dist, ");
		hql.append("Attributes AS att ");
		//hql.append("ManageNumber AS number ");
		hql.append("WHERE ");
		hql.append("api.id = calls.apiType.id ");
		hql.append("AND calls.apiServiceCallId = dist.serviceCall.apiServiceCallId ");
		hql.append("AND dist.distributionId = val.attributeDistributionId.distributionId ");
		hql.append("AND att.attributeId = dist.attribute.attributeId ");
		hql.append("AND api.apiname =:apiName ");
		hql.append("AND val.tobject =:tableName ");
		//hql.append("AND number.id = val.ownerdid ");
		//hql.append("AND number.Number =:number ");
		hql.append("AND att.attributeName =:attributeName ");
		///hql.append("AND val.value =:attributeValue ");
		hql.append("AND calls.serviceName =:serviceCall ");
		hql.append("AND val.ownerdid =:attributeValue ");
		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("apiName", RequestType.CREDIT.toString());
			//query.setParameter("number", endUserId);
			query.setParameter("tableName", TableName.SBXATTRIBUTEVALUE.toString()
					.toLowerCase());
			query.setParameter("attributeName", serviceCall);
			query.setParameter("attributeValue", attributeValue);
			query.setParameter("serviceCall",service );

			resultSet = (AttributeValues) query.uniqueResult();

		} catch (NoResultException e) {
			return null;
		} catch (Exception ex) {
			LOG.error("###WALLET### Error in getListTransaction Service " + ex);
			throw ex;
		}
		return resultSet;
	}
    @Override
    public Integer saveAttributeValue(AttributeValues valueObj) throws Exception {
    	Integer values ;
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
