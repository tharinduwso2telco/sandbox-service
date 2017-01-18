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

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.logging.LogFactory;
import org.hibernate.query.Query;
import org.hibernate.Session;

import com.wso2telco.services.dep.sandbox.dao.WalletDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.TableName;

public class HibernateWalletDAO extends AbstractDAO implements WalletDAO {
	{
		LOG = LogFactory.getLog(HibernateWalletDAO.class);
	}

	@Override
	public List<AttributeValues> getTransactionValue(String endUserId, List<String> attribute, String tableName,
			Integer userId) throws Exception {

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
		hql.append("AND number.user.id =:userId ");
		hql.append("AND att.attributeName IN ( :attributeName)");

		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("apiName", RequestType.WALLET.toString().toLowerCase());
			query.setParameter("number", endUserId);
			query.setParameter("tableName", tableName);
			query.setParameterList("attributeName", attribute);
			query.setParameter("userId", userId);
			query.setFirstResult(0);
			query.setMaxResults(20);
			resultSet = (List<AttributeValues>) query.getResultList();

		} catch (NoResultException e) {
			return null;
		} catch (Exception ex) {
			LOG.error("###WALLET### Error in getListTransaction Service ", ex);
			throw ex;
		}
		return resultSet;
	}

	@Override
	public AttributeValues getAttributeValue(String endUserId, String serviceCall, String attribute, Integer userId)
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
		hql.append("AND att.attributeName =:attName ");
		hql.append("AND number.user.id =:userId ");

		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("apiName", RequestType.WALLET.toString());
			query.setParameter("serviceName", serviceCall);
			query.setParameter("number", endUserId);
			query.setParameter("tableName", TableName.NUMBERS.toString().toLowerCase());
			query.setParameter("attName", attribute);
			query.setParameter("userId", userId);
			resultSet = (AttributeValues) query.uniqueResult();

		} catch (NoResultException e) {
			return null;
		} catch (Exception ex) {
			LOG.error("###WALLET### Error in Make Payment Service ", ex);
			throw ex;
		}
		return resultSet;
	}

	@Override
	public AttributeValues checkDuplicateValue(String serviceCall, String value, String attributeName, String tableName)
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
		hql.append("AND api.apiname =:apiName ");
		hql.append("AND val.tobject =:tableName ");
		hql.append("AND val.value =:attributeValue ");
		hql.append("AND att.attributeName =:attributeName ");

		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("apiName", RequestType.WALLET.toString());
			query.setParameter("tableName", tableName);
			query.setParameter("attributeValue", value);
			query.setParameter("attributeName", attributeName);
			resultSet = (AttributeValues) query.uniqueResult();

		} catch (NoResultException e) {
			return null;
		} catch (Exception ex) {
			LOG.error("###WALLET### Error in Credit Apply Service ", ex);
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

	@Override
	public AttributeValues getResponse(Integer id) throws Exception {

		Session session = getSession();
		AttributeValues resultSet = null;

		StringBuilder hql = new StringBuilder();

		hql.append("SELECT ");
		hql.append("val ");
		hql.append("FROM ");
		hql.append("AttributeValues as val ");
		hql.append("WHERE ");
		hql.append("val.attributevalueId =:id ");

		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("id", id);

			resultSet = (AttributeValues) query.uniqueResult();

		} catch (NoResultException e) {
			return null;
		} catch (Exception ex) {
			LOG.error("###WALLET### Error in Make Payment Service ", ex);
			throw ex;
		}
		return resultSet;
	}

	@Override
	public boolean saveManageNumbers(ManageNumber manageNumber) throws Exception {
		try {
			saveOrUpdate(manageNumber);
		} catch (NoResultException e) {

			return false;
		} catch (Exception ex) {
			LOG.error("###WALLET### Error in Wallet Service ", ex);
			throw ex;
		}
		return true;

	}
}