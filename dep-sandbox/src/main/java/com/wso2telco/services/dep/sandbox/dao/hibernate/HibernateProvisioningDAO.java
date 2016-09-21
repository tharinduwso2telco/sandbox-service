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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;

import com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ListProvisionedDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisionRemoveErrorMessageDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionAllService;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionRequestLog;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionResponseMessage;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionResponseMessageCatergory;
import com.wso2telco.services.dep.sandbox.util.ProvisioningStatusCodes;

public class HibernateProvisioningDAO extends AbstractDAO implements ProvisioningDAO{
	
	{
		LOG = LogFactory.getLog(HibernateProvisioningDAO.class);
	}

	
	public void saveProvisionRequestLog(ProvisionRequestLog provisionRequestLog) throws Exception {
		Session session = getSession();
		Transaction tx = session.beginTransaction();

		try {
			session.save(provisionRequestLog);
			tx.commit();
		} catch (Exception ex) {
			LOG.error(ex);
			tx.rollback();
			throw ex;
		}

	}

	public List<ProvisionAllService> getApplicableProvisionServices(String number, String username, int offset,
			int limit) throws Exception{
		Session session = getSession();
		List<ProvisionAllService> applicableServiceList = new ArrayList<ProvisionAllService>();

		try {
			StringBuilder hqlQueryBuilder = new StringBuilder();
			hqlQueryBuilder.append("select pas ");
			hqlQueryBuilder
					.append("from ProvisionAllService pas,ProvisionMSISDNServicesMap pmsm,ManageNumber mn,User u ");
			hqlQueryBuilder.append("where u.userName = :userName ");
			hqlQueryBuilder.append("and u.id = mn.user.id ");
			hqlQueryBuilder.append("and mn.id = pmsm.msisdnId.id ");
			hqlQueryBuilder.append("and pmsm.servicesId.id = pas.id ");
			hqlQueryBuilder.append("and mn.Number = :number ");

			Query query = session.createQuery(hqlQueryBuilder.toString());

			query.setParameter("userName", username);
			query.setParameter("number", number);
			if (offset > 0) {
				query.setFirstResult(offset);
			}

			if (limit > 0) {
				query.setMaxResults(limit);
			}

			applicableServiceList = (List<ProvisionAllService>) query.getResultList();
		} catch (Exception ex) {
			LOG.error("###PROVISION### Error in getApplicableProvisionServices " + ex);
			throw ex;
		}

		return applicableServiceList;
	}
	
	public List<ListProvisionedDTO> getActiveProvisionedServices(String msisdn,String username,int offset, int limit) throws Exception {
		Session session = getSession();
		List<ListProvisionedDTO> resultSet=null;
		StringBuilder hql=new StringBuilder();
		
		hql.append(" SELECT");
		hql.append(" services.serviceCode AS serviceCode,services.description AS description,prservice.createdDate AS createdDate,services.tag AS tag,services.value AS value");
		hql.append(" FROM");
		hql.append(" ManageNumber AS num,");
		hql.append(" ProvisionMSISDNServicesMap AS map,");
		hql.append(" ProvisionedServices AS prservice,");
		hql.append(" Status AS stat,");
		hql.append(" ProvisionAllService AS services,");
		hql.append(" User AS user");
		hql.append(" WHERE num.Number = :number");
		hql.append(" AND user.userName= :username");
		hql.append(" AND user.id = num.user");
		hql.append(" AND map.msisdnId = num.id");
		hql.append(" AND map.id = prservice.msisdnServiceMap");
		hql.append(" AND map.servicesId = services.id");
		hql.append(" AND stat.id = prservice.status");
		hql.append(" AND stat.code= :status");

		Query query = session.createQuery(hql.toString());
		
		query.setParameter("status", ProvisioningStatusCodes.PRV_PROVISION_SUCCESS.toString());
		query.setParameter("number", msisdn);
		query.setParameter("username", username);
		
		if (offset > 0) {
			query.setFirstResult(offset);
		}

		if (limit > 0) {
			query.setMaxResults(limit);
		}
		
		resultSet = query.setResultTransformer( Transformers.aliasToBean(ListProvisionedDTO.class)).getResultList();
		return resultSet;
	}
	
	public List<ProvisionResponseMessage> getErrorResponse(String msisdn, String username,String serviceCode) throws Exception {
		
		Session session = getSession();
		List <ProvisionResponseMessage> expectedErrorList = new ArrayList<ProvisionResponseMessage>();;
		StringBuilder hql=new StringBuilder();
		
		hql.append(" SELECT");
		hql.append(" response");
		hql.append(" FROM");
		hql.append(" ProvisionResponseMessage AS response,");
		hql.append(" ManageNumber AS number,");
		hql.append(" ProvisionExpectedMessage AS expect,");
		hql.append(" ProvisionAllService AS service,");
		hql.append(" User AS user");
		hql.append(" WHERE");
		hql.append(" number.Number = :msisdn AND");
		hql.append(" user.userName= :username AND");
		hql.append(" user.id = number.user AND");
		hql.append(" service.serviceCode = :code AND");
		hql.append(" number.id = expect.msisdnId AND");
		hql.append(" service.id = expect.servicesId AND");
		hql.append(" expect.requestType = :type AND");
		hql.append(" expect.messageId = response.id");
		
		Query query = session.createQuery(hql.toString());

		query.setParameter("msisdn", msisdn);
		query.setParameter("username", username);
		query.setParameter("code", serviceCode);
		query.setParameter("type", "DELETE");
		

		expectedErrorList = (List<ProvisionResponseMessage>) query.getResultList();

		return expectedErrorList;
	}

	

}
