/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.services.dep.sandbox.dao.hibernate;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.logging.LogFactory;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.wso2telco.services.dep.sandbox.dao.WalletDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.servicefactory.credit.AttributeName;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import com.wso2telco.services.dep.sandbox.util.TableName;

public class HibernateWalletDAO extends AbstractDAO implements WalletDAO {
	{
		LOG = LogFactory.getLog(HibernateWalletDAO.class);
	}

	@Override
	public List<AttributeValues> getTransactionValue(String endUserId, List<String> attribute, String serviceCall) throws Exception {

		Session session = getSession();
		List<AttributeValues> resultSet = null;

		StringBuilder hql = new StringBuilder();
		hql.append("SELECT ");
		hql.append("val ");
		hql.append("FROM ");
		hql.append("AttributeValues AS val, ");
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
		hql.append("AND api.apiname =:apiName ");
		hql.append("AND val.tobject =:tableName ");
		hql.append("AND number.id = val.ownerdid ");
		hql.append("AND number.Number =:number ");
		hql.append("AND att.attributeName IN ( :attributeName)");

		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("apiName", RequestType.WALLET.toString()
					.toLowerCase());
			query.setParameter("number", endUserId);
			query.setParameter("tableName", TableName.NUMBERS.toString()
					.toLowerCase());
			query.setParameterList("attributeName", attribute);
/*			query.setFirstResult(0);
			query.setMaxResults(10); */
			resultSet = (List<AttributeValues>) query.getResultList();

		} catch (NoResultException e) {
			return null;
		} catch (Exception ex) {
			LOG.error("###WALLET### Error in getListTransaction Service " + ex);
			throw ex;
		}
		return resultSet;
	}

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
			query.setParameter("apiName", RequestType.WALLET.toString());
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
	public Double checkBalance(String msisdn) throws Exception {

		Double balance ;
		boolean result = false;
		Session session = getSession();

		StringBuilder hql = new StringBuilder();
		hql.append("SELECT ");
		hql.append("number.balance ");
		hql.append("FROM ");
		hql.append("ManageNumber AS number ");
		hql.append("WHERE ");
		hql.append("number.Number =:msisdn");

		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("msisdn", msisdn);
			balance = (Double) query.uniqueResult();

		} catch (NoResultException e) {
return null;
		} catch (Exception ex) {
			LOG.error("###WALLET### Error in Make Payment Service " + ex);
			throw ex;
		}
		return balance;
	}

	@Override
	public boolean updateBalance(String msisdn, Double updateBalance)
			throws Exception {

		Session session = getSession();
		Transaction tx = null;

		StringBuilder hql = new StringBuilder();
		hql.append("UPDATE ");
		hql.append("ManageNumber as number ");
		hql.append("SET ");
		hql.append("number.balance =:updateBalance ");
		hql.append("WHERE ");
		hql.append("number.Number =:msisdn");

		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("msisdn", msisdn);
			query.setParameter("updateBalance", updateBalance);

			tx = session.beginTransaction();
			query.executeUpdate();
			tx.commit();

		} catch (NoResultException e) {
			
			return false;
		} catch (Exception ex) {
			LOG.error("###WALLET### Error in Make Payment Service " + ex);
			throw ex;
		}
		return true;
	}

	@Override
	public AttributeValues getAttributeValue(
			AttributeDistribution distributionObj, Integer endUserId)
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
	public boolean checkDuplicateValue(String endUserId, String serviceCall,
			String value, String attributeName) throws Exception {


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
		hql.append("AND val.value =:value ");
		hql.append("AND att.attributeName =:attributeName");


		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("apiName", RequestType.WALLET.toString());
			query.setParameter("serviceName", serviceCall);
			query.setParameter("number", endUserId);
			query.setParameter("tableName", TableName.NUMBERS.toString().toLowerCase());
			query.setParameter("value", value);
			query.setParameter("attributeName", attributeName);

			resultSet = (AttributeValues) query.uniqueResult();
			
			if(resultSet != null){
				return true;
			}

		} catch (NoResultException e) {
return false;		} catch (Exception ex) {
			LOG.error("###WALLET### Error in Make Payment Service " + ex);
			throw ex;
		}
		return false;	
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
}
