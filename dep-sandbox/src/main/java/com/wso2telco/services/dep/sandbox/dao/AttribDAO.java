package com.wso2telco.services.dep.sandbox.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;

public interface AttribDAO {
	public List<AttributeDistribution> loadServiceAttributes(final ServiceName serviceName,final RequestType api) throws HibernateException ;
	
	 public void saveOrUpdateAttributesValues(	final List<AttributeValues> attributeValues ,
				final ServiceName serviceType,
				final RequestType api,
				final String tObject,
				final int tObjectDid) throws HibernateException;

}
