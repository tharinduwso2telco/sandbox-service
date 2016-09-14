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

import com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionAllService;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionRequestLog;

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

}
