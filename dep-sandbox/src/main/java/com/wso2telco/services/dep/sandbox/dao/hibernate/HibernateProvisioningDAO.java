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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.NoResultException;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;

import com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ListProvisionedDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionAllService;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionMSISDNServicesMap;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionRequestLog;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionResponseMessage;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionedServices;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Status;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.util.ProvisioningStatusCodes;

/**
 * @author WSO2.Telco
 *
 */

public class HibernateProvisioningDAO extends AbstractDAO implements ProvisioningDAO {

	{
		LOG = LogFactory.getLog(HibernateProvisioningDAO.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO#
	 * saveProvisionRequestLog
	 * (com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionRequestLog)
	 */
	public void saveProvisionRequestLog(ProvisionRequestLog provisionRequestLog) throws Exception {
		try {
			saveOrUpdate(provisionRequestLog);
		} catch (Exception ex) {
			LOG.error("###PROVISION### Error in saveProvisionRequestLog " + ex);
			throw ex;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO#
	 * getApplicableProvisionServices(java.lang.String, java.lang.String, int,
	 * int)
	 */
	public List<ProvisionAllService> getApplicableProvisionServices(String number, String username, int offset,
			int limit) throws Exception {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO#
	 * getActiveProvisionedServices(java.lang.String, java.lang.String, int,
	 * int)
	 */
	public List<ListProvisionedDTO> getActiveProvisionedServices(String msisdn, String username, int offset, int limit)
			throws Exception {
		Session session = getSession();
		List<ListProvisionedDTO> resultSet = null;
		StringBuilder hql = new StringBuilder();

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
		hql.append(" AND user.id = num.user.id");
		hql.append(" AND user.id = services.user.id");
		hql.append(" AND map.msisdnId.id = num.id");
		hql.append(" AND map.id = prservice.msisdnServiceMap.id");
		hql.append(" AND map.servicesId.id = services.id");
		hql.append(" AND stat.id = prservice.status.id");
		hql.append(" AND stat.code= :status");

		try {
			Query query = session.createQuery(hql.toString());

			query.setParameter("status",ProvisioningStatusCodes.PRV_PROVISION_SUCCESS.toString());
			query.setParameter("number", msisdn);
			query.setParameter("username", username);

			if (offset > 0) {
				query.setFirstResult(offset);
			}

			if (limit > 0) {
				query.setMaxResults(limit);
			}

			resultSet = query.setResultTransformer(Transformers.aliasToBean(ListProvisionedDTO.class)).getResultList();
		} catch (Exception ex) {
			LOG.error("###PROVISION### Error in getActiveProvisionedServices " + ex);
			throw ex;
		}
		return resultSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO#getErrorResponse
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	public ProvisionResponseMessage getErrorResponse(String msisdn, String username,String serviceCode) throws Exception {
		
		Session session = getSession();
		ProvisionResponseMessage expectedErrorList = null;
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
		hql.append(" user.id = service.user AND");
		hql.append(" service.serviceCode = :code AND");
		hql.append(" number.id = expect.msisdnId AND");
		hql.append(" service.id = expect.servicesId AND");
		hql.append(" expect.requestType = :type AND");
		hql.append(" expect.messageId = response.id");
		
		try {
			
			Query query = session.createQuery(hql.toString());

			query.setParameter("msisdn", msisdn);
			query.setParameter("username", username);
			query.setParameter("code", serviceCode);
			query.setParameter("type", "DELETE");

			expectedErrorList = (ProvisionResponseMessage) query.uniqueResult();
		} catch (Exception ex) {
		LOG.error("###PROVISION### Error in getErrorResponse " + ex);
		throw ex;
	}
		return expectedErrorList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO#getAlreadyProvisioned
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	public ProvisionedServices getAlreadyProvisioned(String msisdn, String userName,String serviceCode) throws Exception{
		
		Session session = getSession();
		ProvisionedServices provisionedCheckList = null;
		StringBuilder hql=new StringBuilder();
		
		hql.append(" SELECT");
		hql.append(" prservice");
		hql.append(" FROM");
		hql.append(" ProvisionedServices AS prservice,");
		hql.append(" ManageNumber AS number,");
		hql.append(" ProvisionAllService AS service,");
		hql.append(" User AS user,");
		hql.append(" ProvisionMSISDNServicesMap AS map,");
		hql.append(" Status AS stat");
		hql.append(" WHERE");
		hql.append(" number.Number = :msisdn AND");
		hql.append(" user.userName= :username AND");
		hql.append(" user.id = number.user AND");
		hql.append(" user.id = service.user AND");
		hql.append(" service.serviceCode = :code AND");
		hql.append(" map.servicesId = service.id AND");
		hql.append(" map.msisdnId = number.id AND");
		hql.append(" map.id = prservice.msisdnServiceMap AND");
		hql.append(" stat.id = prservice.status AND");
		hql.append(" stat.code= :status");
		
		try {
			
			Query query = session.createQuery(hql.toString());

			query.setParameter("status",ProvisioningStatusCodes.PRV_PROVISION_SUCCESS.toString());
			query.setParameter("msisdn", msisdn);
			query.setParameter("username", userName);
			query.setParameter("code", serviceCode);

			provisionedCheckList = (ProvisionedServices) query.uniqueResult();
			
		} catch (Exception ex) {
			LOG.error("###PROVISION### Error in getAlreadyProvisioned " + ex);
			throw ex;
		}
		
		return provisionedCheckList;
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO#updateDeleteStatus
	 * (com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionedServices)
	 */
	public void updateDeleteStatus( ProvisionedServices prvDeletePending) throws Exception{
		try {		
			super.saveOrUpdate(prvDeletePending);		
		} catch (Exception ex) {
			LOG.error(ex);
			LOG.error("###PROVISION### Error in updateDeleteStatus " + ex);
		}

	}

	public void saveProvisionService(ProvisionAllService provisionAllService) throws Exception {
			try {
				saveOrUpdate(provisionAllService);
			} catch (Exception ex) {
				LOG.error("###PROVISION### Error in saveProvisionService " + ex);
				throw ex;
			}
		
		
	}
	
	public List<ProvisionAllService> getProvisionServices(int userid) throws Exception {
		Session session = getSession();
		List<ProvisionAllService> serviceList = new ArrayList<ProvisionAllService>();
		try {
			StringBuilder hqlQueryBuilder = new StringBuilder();
			hqlQueryBuilder
					.append("from ProvisionAllService pas ");
			hqlQueryBuilder.append("where pas.user.id = :id");
			
			
			Query query = session.createQuery(hqlQueryBuilder.toString());
			query.setParameter("id", userid);
			serviceList = (List<ProvisionAllService>) query.getResultList();
		} catch (Exception ex) {
			LOG.error("###PROVISION### Error in getProvisionServices " + ex);
			throw ex;
		}
		return serviceList;
	}
		
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO#getTransactionStatus
	 * ()
	 */
	public List<Status> getTransactionStatus() throws Exception {

		Session session = getSession();
		List<Status> statusList = null;
		try {
			statusList = (List<Status>) session.createQuery("from Status ")
					.getResultList();

		} catch (Exception ex) {
			LOG.error("###PROVISION### Error in getTransactionStatus " + ex);
			throw ex;
		}
		return statusList;

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO#getNumber(java.
	 * lang.String, java.lang.String)
	 */
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
			LOG.error("###PROVISION### Error in getNumber", ex);
			throw ex;
		}

		return userNumber;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO#getNumber()
	 */
	@Override
	public ProvisionAllService getProvisionService(String serviceCode, String ServiceName, User user) throws Exception {
		Session session = getSession();
		ProvisionAllService provisionService = null;
		Map<String, String> parameterMap = new HashMap<>();

		try {
			StringBuilder hqlQueryBuilder = new StringBuilder();
			hqlQueryBuilder.append("from ProvisionAllService service where ");
			hqlQueryBuilder.append(" service.user = :user");
			hqlQueryBuilder.append(" and ");

			if (serviceCode != null) {
				hqlQueryBuilder.append("lower(service.serviceCode) = :serviceCode");
				parameterMap.put("serviceCode", serviceCode.toLowerCase());
			}

			if (serviceCode != null && ServiceName != null) {
				hqlQueryBuilder.append(" and");
				hqlQueryBuilder.append(" lower(service.serviceName) = :serviceName");
				parameterMap.put("serviceName", ServiceName.toLowerCase());
			} else if (ServiceName != null) {
				hqlQueryBuilder.append(" lower(service.serviceName) = :serviceName");
				parameterMap.put("serviceName", ServiceName.toLowerCase());
			}

			Query query = session.createQuery(hqlQueryBuilder.toString());

			Set<Entry<String, String>> entrySet = parameterMap.entrySet();

			for (Entry<String, String> entry : entrySet) {
				query.setParameter(entry.getKey(), entry.getValue());
			}

			query.setParameter("user", user);

			provisionService = (ProvisionAllService) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception ex) {
			LOG.error("###PROVISION### Error in getProvisionService", ex);
			throw ex;
		}

		return provisionService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO#
	 * getProvisionedService()
	 */
	@Override
	public ProvisionedServices getAlreadyProvisionedService(User user, List<String> statusCodes,
			ProvisionAllService provisionService, String phoneNumber) throws Exception {
		Session session = getSession();
		ProvisionedServices provisionedService = null;

		try {
			StringBuilder hqlQueryBuilder = new StringBuilder();
			hqlQueryBuilder.append("select provisionedService ");
			hqlQueryBuilder.append(" from ProvisionedServices provisionedService,");
			hqlQueryBuilder.append(" Status status,");
			hqlQueryBuilder.append(" ProvisionMSISDNServicesMap map,");
			hqlQueryBuilder.append(" ManageNumber AS number,");
			hqlQueryBuilder.append(" ProvisionAllService provisionServices,");
			hqlQueryBuilder.append(" User user ");
			hqlQueryBuilder.append(" where ");
			hqlQueryBuilder.append(" provisionedService.status.id = status.id");
			hqlQueryBuilder.append(" AND provisionedService.msisdnServiceMap.id = map.id");
			hqlQueryBuilder.append(" AND map.msisdnId.id = number.id");
			hqlQueryBuilder.append(" AND map.servicesId.id = provisionServices.id");
			hqlQueryBuilder.append(" AND number.user.id = user.id");
			hqlQueryBuilder.append(" AND number.user = :spUser");
			hqlQueryBuilder.append(" AND status.code in (:statusCodeList)");
			hqlQueryBuilder.append(" AND number.Number = :phoneNumber");
			hqlQueryBuilder.append(" AND provisionServices.id = :provisionServiceId");

			Query query = session.createQuery(hqlQueryBuilder.toString());

			query.setParameter("spUser", user);
			query.setParameterList("statusCodeList", statusCodes);
			query.setParameter("phoneNumber", phoneNumber);
			query.setParameter("provisionServiceId", provisionService.getId());

			provisionedService = (ProvisionedServices) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception ex) {
			LOG.error("###PROVISION### Error in getAlreadyProvisionedService", ex);
			throw ex;
		}

		return provisionedService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO#
	 * getStatusFromStatusCode
	 * (com.wso2telco.services.dep.sandbox.util.ProvisioningStatusCodes)
	 */
	public Status getStatusFromStatusCode(ProvisioningStatusCodes statusCode) throws Exception {
		Session session = getSession();
		Status status = null;

		try {
			StringBuilder hqlQueryBuilder = new StringBuilder();
			hqlQueryBuilder.append("from Status where code = :statusCode");

			Query query = session.createQuery(hqlQueryBuilder.toString());
			query.setParameter("statusCode", statusCode.toString());

			status = (Status) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception ex) {
			LOG.error("###PROVISION### Error in getStatusFromStatusCode", ex);
			throw ex;
		}

		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO#
	 * getProvisionMsisdnService(com.wso2telco.services.dep.sandbox.dao.model.
	 * domain.ManageNumber,
	 * com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionAllService)
	 */
	@Override
	public ProvisionMSISDNServicesMap getProvisionMsisdnService(ManageNumber number, ProvisionAllService service)
			throws Exception {
		Session session = getSession();
		ProvisionMSISDNServicesMap serviceMapEntry = null;

		try {
			StringBuilder hqlQueryBuilder = new StringBuilder();

			hqlQueryBuilder.append("from ProvisionMSISDNServicesMap serviceMap");
			hqlQueryBuilder.append(" where ");
			hqlQueryBuilder.append(" serviceMap.msisdnId = :number ");
			hqlQueryBuilder.append(" and serviceMap.servicesId = :service ");
			hqlQueryBuilder.append(" and serviceMap.msisdnId.status = '1'");

			Query query = session.createQuery(hqlQueryBuilder.toString());

			query.setParameter("number", number);
			query.setParameter("service", service);

			serviceMapEntry = (ProvisionMSISDNServicesMap) query.getSingleResult();

		} catch (NoResultException e) {
			return null;
		} catch (Exception ex) {
			LOG.error("###PROVISION### Error in getProvisionMsisdnService", ex);
			throw ex;
		}

		return serviceMapEntry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO#
	 * saveProvisionedService(com.wso2telco.services.dep.sandbox.dao.model.
	 * domain.ProvisionedServices)
	 */
	@Override
	public void saveProvisionedService(ProvisionedServices provisionedService) throws Exception {
		try {
			saveOrUpdate(provisionedService);
		} catch (Exception ex) {
			LOG.error("###PROVISION### Error in saveProvisionRequestLog " + ex);
			throw ex;
		}

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO#checkClientCorrelator
	 * (java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ProvisionedServices checkClientCorrelator(String msisdn, String userName, String serviceCode, String clientCorrelator)throws Exception {
		Session session = getSession();
		ProvisionedServices provisionedCheckList = null;
		StringBuilder hql=new StringBuilder();
		
		hql.append(" SELECT");
		hql.append(" prservice");
		hql.append(" FROM");
		hql.append(" ProvisionedServices AS prservice,");
		hql.append(" ManageNumber AS number,");
		hql.append(" ProvisionAllService AS service,");
		hql.append(" User AS user,");
		hql.append(" ProvisionMSISDNServicesMap AS map,");
		hql.append(" Status AS stat");
		hql.append(" WHERE");
		hql.append(" number.Number = :msisdn AND");
		hql.append(" user.userName= :username AND");
		hql.append(" user.id = number.user.id AND");
		hql.append(" user.id = service.user.id AND");
		hql.append(" service.serviceCode = :code AND");
		hql.append(" map.servicesId.id = service.id AND");
		hql.append(" map.msisdnId.id = number.id AND");
		hql.append(" map.id = prservice.msisdnServiceMap.id AND");
		hql.append(" stat.id = prservice.status.id AND");	
		hql.append(" prservice.clientCorrelator= :clientCorrelator AND");
		hql.append(" ( stat.code= :status1 OR stat.code= :status2 )");
		
		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("status1", ProvisioningStatusCodes.PRV_DELETE_SUCCESS.toString());
			query.setParameter("status2", ProvisioningStatusCodes.PRV_DELETE_PENDING.toString());
			query.setParameter("msisdn", msisdn);
			query.setParameter("username", userName);
			query.setParameter("code", serviceCode);
			query.setParameter("clientCorrelator", clientCorrelator);	

			provisionedCheckList = (ProvisionedServices) query.uniqueResult();
		}catch (Exception ex) {
			LOG.error("###PROVISION### Error in checkClientCorrelator " + ex);
			throw ex;
		}
		return provisionedCheckList;
	}
	
	public ProvisionedServices checkProvisionClientCorrelator(String msisdn, String userName, String serviceCode, String clientCorrelator)throws Exception {
		Session session = getSession();
		ProvisionedServices provisionedCheckList = null;
		StringBuilder hql=new StringBuilder();
		
		hql.append(" SELECT");
		hql.append(" prservice");
		hql.append(" FROM");
		hql.append(" ProvisionedServices AS prservice,");
		hql.append(" ManageNumber AS number,");
		hql.append(" ProvisionAllService AS service,");
		hql.append(" User AS user,");
		hql.append(" ProvisionMSISDNServicesMap AS map,");
		hql.append(" Status AS stat");
		hql.append(" WHERE");
		hql.append(" number.Number = :msisdn AND");
		hql.append(" user.userName= :username AND");
		hql.append(" user.id = number.user.id AND");
		hql.append(" user.id = service.user.id AND");
		hql.append(" service.serviceCode = :code AND");
		hql.append(" map.servicesId.id = service.id AND");
		hql.append(" map.msisdnId.id = number.id AND");
		hql.append(" map.id = prservice.msisdnServiceMap.id AND");
		hql.append(" stat.id = prservice.status.id AND");	
		hql.append(" prservice.clientCorrelator= :clientCorrelator AND");
		hql.append(" stat.code= :status1 ");
		
		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("status1", ProvisioningStatusCodes.PRV_DELETE_SUCCESS.toString());
			//query.setParameter("status2", ProvisioningStatusCodes.PRV_DELETE_PENDING.toString());
			query.setParameter("msisdn", msisdn);
			query.setParameter("username", userName);
			query.setParameter("code", serviceCode);
			query.setParameter("clientCorrelator", clientCorrelator);	

			provisionedCheckList = (ProvisionedServices) query.uniqueResult();
		}catch (Exception ex) {
			LOG.error("###PROVISION### Error in checkClientCorrelator " + ex);
			throw ex;
		}
		return provisionedCheckList;
	}

	@Override
    public void saveServiceForMsisdn(ProvisionMSISDNServicesMap map)
	    throws Exception {
	try {
	    saveOrUpdate(map);
	} catch (Exception ex) {
	    LOG.error("###PROVISION### Error in saveServiceForMsisdn " + ex);
	    throw ex;
	}
    }

	
}
