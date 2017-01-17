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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.NoResultException;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.wso2telco.services.dep.sandbox.dao.CustomerInfoDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.CustomerInfoDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Attributes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.servicefactory.customerinfo.CustomerInfoRequestType;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.TableName;

public class HibernateCustomerInfoDAO extends AbstractDAO implements CustomerInfoDAO {
    {
	LOG = LogFactory.getLog(HibernateCustomerInfoDAO.class);
    }

    @Override

    public ManageNumber getMSISDN(String msisdn, String imsi, String mcc, String mnc,String user) throws Exception {
	Session session = getSession();
	ManageNumber number = null;
	Map<String, String> parameterMap = new HashMap<>();

	try {
	    StringBuilder hqlBuilder = new StringBuilder();
	    hqlBuilder.append("from ManageNumber number where ");
	    if (msisdn != null && imsi != null) {
		hqlBuilder.append(" number.Number = :msisdn ");
		hqlBuilder.append(" and ");
		hqlBuilder.append(" number.imsi = :imsi");
		parameterMap.put("msisdn", msisdn);
		parameterMap.put("imsi", imsi);
	    } else if (msisdn != null) {
		hqlBuilder.append(" number.Number = :msisdn ");
		parameterMap.put("msisdn", msisdn);
	    } else if (imsi != null) {
		hqlBuilder.append(" number.imsi = :imsi");
		parameterMap.put("imsi", imsi);
	    }

	    if (mcc != null) {
		hqlBuilder.append(" and ");
		hqlBuilder.append(" number.mcc=:mcc ");
		parameterMap.put("mcc", mcc);
	    }

	    if (mnc != null) {
		hqlBuilder.append(" and ");
		hqlBuilder.append(" number.mnc=:mnc ");
		parameterMap.put("mnc", mnc);
	    }
	    
	    hqlBuilder.append(" and lower(number.user.userName) = :user ");
	    parameterMap.put("user", user.toLowerCase());

	    Query query = session.createQuery(hqlBuilder.toString());

	    Set<Entry<String, String>> entrySet = parameterMap.entrySet();

	    for (Entry<String, String> parameterEntry : entrySet) {
		if (parameterEntry.getKey().equals("mcc") || parameterEntry.getKey().equals("mnc") ) {
		    query.setParameter(parameterEntry.getKey(), Integer.parseInt(parameterEntry.getValue()));
		} else {
		    query.setParameter(parameterEntry.getKey(), parameterEntry.getValue());
		}

	    }

	    number = (ManageNumber) query.getSingleResult();
	} catch (NoResultException e) {
	    return null;
	} catch (Exception ex) {
	    LOG.error("###CUSTOMERINFO### Error While Retriving MSISDN", ex);
	    throw ex;
	}

	return number;
    }

    @Override

    public CustomerInfoDTO getProfileData(String msisdn, User user) throws Exception {
	Session session = getSession();
	CustomerInfoDTO customerInfoDTO = null;

	try {
	    StringBuilder hqlBuilder = new StringBuilder();

	    hqlBuilder.append("SELECT msisdn,");
	    hqlBuilder.append(" MAX(IF(column_name = 'title', value, NULL)) title, ");
	    hqlBuilder.append(" MAX(IF(column_name = 'firstName',value,NULL)) firstName, ");
	    hqlBuilder.append(" MAX(IF(column_name = 'lastName',value,NULL)) lastName,");
	    hqlBuilder.append(" MAX(IF(column_name = 'dob', value, NULL)) dob, ");
	    hqlBuilder.append(" MAX(IF(column_name = 'identificationType', value, NULL)) identificationType, ");
	    hqlBuilder.append(" MAX(IF(column_name = 'identificationNumber',value,NULL)) identificationNumber, ");
	    hqlBuilder.append(" MAX(IF(column_name = 'address', value, NULL)) address, ");
	    hqlBuilder.append(" MAX(IF(column_name = 'additionalInfo',value,NULL)) additionalInfo, ");
	    hqlBuilder.append(" MAX(IF(column_name = 'ownerType',value,NULL)) ownerType, ");
	    hqlBuilder.append(" MAX(IF(column_name = 'accountType',value,NULL)) accountType, ");
	    hqlBuilder.append(" MAX(IF(column_name = 'status', value, NULL)) status ");
	    hqlBuilder.append(" FROM ");
	    hqlBuilder.append(" (SELECT numbers.number AS msisdn, ");
	    hqlBuilder.append(" attr.name AS column_name, ");
	    hqlBuilder.append(" attval.value AS value ");
	    hqlBuilder.append(" FROM ");
	    hqlBuilder.append(" sbxapitypes api, sbxapiservicecalls serviceCalls, ");
	    hqlBuilder.append(" sbtattributedistribution attdis, sbxattribute attr, ");
	    hqlBuilder.append(" sbxattributevalue attval, user usr, numbers numbers ");
	    hqlBuilder.append(" WHERE ");
	    hqlBuilder.append(" api.apiname = '" + RequestType.CUSTOMERINFO.toString() + "'");
	    hqlBuilder.append(" AND serviceCalls.service = '" + CustomerInfoRequestType.GETPROFILE.toString() + "'");
	    hqlBuilder.append(" AND api.id = serviceCalls.apitypesdid ");
	    hqlBuilder.append(" AND serviceCalls.sbxapiservicecallsdid= attdis.apiservicecallsdid ");
	    hqlBuilder.append(" AND attdis.attributedid = attr.sbxattributedid ");
	    hqlBuilder.append(" AND attval.attributedistributiondid = attdis.sbtattributedistributiondid ");
	    hqlBuilder.append(" AND usr.user_name =:userName");
	    hqlBuilder.append(" AND usr.id = attval.ownerdid ");
	    hqlBuilder.append(" AND attval.tobject = '" + TableName.USER.toString() + "'");
	    hqlBuilder.append(" AND numbers.user_id = usr.id ");
	    hqlBuilder.append(" AND numbers.number =:msisdn ");
	    hqlBuilder.append(") d GROUP BY msisdn");

	    SQLQuery query = session.createSQLQuery(hqlBuilder.toString());
	    query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

	    query.setParameter("userName", user.getUserName());
	    query.setParameter("msisdn", msisdn);

	    List resultList = query.list();
	    if (resultList.size() > 0) {
		Object result = resultList.get(0);
		customerInfoDTO = new CustomerInfoDTO();
		Map resultMap = (Map) result;
		customerInfoDTO.setMsisdn((String) resultMap.get("msisdn"));
		customerInfoDTO.setTitle((String) resultMap.get("title"));
		customerInfoDTO.setFirstName((String) resultMap.get("firstName"));
		customerInfoDTO.setLastName((String) resultMap.get("lastName"));
		//String dateOfBirth = (String) resultMap.get("dob");
		//if (CommonUtil.getNullOrTrimmedValue(dateOfBirth) != null) {
		//    Date date;
		//    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//    date = dateFormat.parse(dateOfBirth);
		    customerInfoDTO.setDob((String) resultMap.get("dob"));
		
		customerInfoDTO.setMsisdn((String) resultMap.get("msisdn"));
		customerInfoDTO.setAddress((String) resultMap.get("address"));
		customerInfoDTO.setAccountType((String) resultMap.get("accountType"));
		customerInfoDTO.setOwnerType((String) resultMap.get("ownerType"));
		customerInfoDTO.setAdditionalInfo((String) resultMap.get("additionalInfo"));
		customerInfoDTO.setStatus((String) resultMap.get("status"));
		customerInfoDTO.setIdentificationType((String) resultMap.get("identificationType"));
		customerInfoDTO.setIdentificationNumber((String) resultMap.get("identificationNumber"));
	    }

	} catch (Exception ex) {
	    LOG.error("###CUSTOMERINFO### Error in Get Profile Data", ex);
	    throw ex;
	}

	return customerInfoDTO;
    }

	@Override
	public List<AttributeValues> getAttributeServices(String msisdn,
			Integer userID, String imsi, String[] schema) throws Exception {
		Session session = getSession();
		List<AttributeValues> attributeValues = null;

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
		hql.append("AND val.ownerdid = :userId ");
		hql.append("AND api.apiname = :apiName ");
		hql.append("AND calls.serviceName = :apiService ");
		hql.append("AND att.attributeName IN ( :schema)");

		try {
			Query query = session.createQuery(hql.toString());

			query.setParameter("apiName", RequestType.CUSTOMERINFO.toString());
			query.setParameter("apiService", CustomerInfoRequestType.GETATTRIBUTE.toString());
			query.setParameter("userId", userID);
			query.setParameterList("schema", schema);

			attributeValues = (List<AttributeValues>) query.getResultList();

		} catch (Exception ex) {
			LOG.error("###Customer rInfo### Error in get customer attributes "
					+ ex);
			throw ex;
		}

		return attributeValues;
	}

	@Override
	public boolean checkSchema(String[] schema) throws Exception {

		Session session = getSession();
		Attributes resultSet = null;
		List<AttributeValues> attributeValues = null;

		try {

			StringBuilder hql = new StringBuilder();
			hql.append("SELECT ");
			hql.append("att.attributeName ");
			hql.append("FROM ");
			hql.append("Attributes AS att ");
			hql.append("WHERE ");
			hql.append("att.attributeName IN ( :schema)");

			Query query = session.createQuery(hql.toString());
			query.setParameterList("schema", schema);

			attributeValues = (List<AttributeValues>) query.getResultList();

			if (attributeValues.size() == schema.length) {
				return true;
			}

		} catch (Exception ex) {
			LOG.error("###Customer Info### Error in getErrorResponse " + ex);
			throw ex;
		}
		return false;
	}

}
