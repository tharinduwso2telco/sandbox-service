package com.wso2telco.services.dep.sandbox.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.wso2telco.services.dep.sandbox.dao.AttribDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;

public class HibernateAttributeDAO extends AbstractDAO implements AttribDAO {

	
	{
		LOG =LogFactory.getLog(HibernateAttributeDAO.class);
	}
	@Override
	  public List<AttributeDistribution> loadServiceAttributes(final ServiceName serviceName,final RequestType api) throws HibernateException {
			Session sess = getSession();
			List<AttributeDistribution> distributionList = new ArrayList<AttributeDistribution>();

			try {
			    StringBuilder hqlQueryBuilder = new StringBuilder();
			    hqlQueryBuilder.append("from AttributeDistribution dist ");
			    hqlQueryBuilder.append(" WHERE");
			    hqlQueryBuilder.append(" dist.serviceCall.serviceName=:serviceName AND ");
			    hqlQueryBuilder.append(" dist.serviceCall.apiType.apiname= :api ");

			    distributionList =   sess
				    .createQuery(hqlQueryBuilder.toString())
				    .setParameter("api", api.toString())
				    .setParameter("serviceName", serviceName.toString()).getResultList();
			} catch (Exception e) {
			    LOG.error("getAttributeDistributionByServiceCall", e);
			    throw e;
			}
			return distributionList;
		    }

	 public void saveOrUpdateAttributesValues(	final List<AttributeValues> attributeValues ,
			 									final ServiceName serviceType,
			 									final RequestType api,
												final String tObject,
												final int tObjectDid) throws HibernateException {
		Session session=  getSession();

		Transaction tx = session.beginTransaction();
		try {
			
			
			 StringBuilder hql = new StringBuilder();
			 hql.append("from AttributeValues attribVal ");
			 hql.append(" where attribVal.attributeDistribution.serviceCall.serviceName= :serviceName ");
			 hql.append(" and attribVal.attributeDistribution.serviceCall.apiType.apiname=:apiname ");
			 hql.append(" and attribVal.tobject=:tobject");
			 hql.append(" and attribVal.ownerdid=:tObjectDid");
			 
			 
			List<AttributeValues> listofValues = session.createQuery(hql.toString())
					.setParameter("apiname",api.toString())
					.setParameter("serviceName", serviceType.toString())
					.setParameter("tobject", tObject)
					.setParameter("tObjectDid", tObjectDid)
					.getResultList();
			
			for (AttributeValues attributeValues2 : listofValues) {
				session.remove(attributeValues2);
			}
			
			for (AttributeValues attrib : attributeValues) {
				session.save(attrib);
			}
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			LOG.error("",e);
		throw e;
		}
	 }
	  
}
