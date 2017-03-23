package com.wso2telco.services.dep.sandbox.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.NoResultException;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.wso2telco.services.dep.sandbox.dao.GenaricDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Attributes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.SenderAddress;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

class HibernateCommonDAO extends AbstractDAO implements GenaricDAO {

    {
	LOG = LogFactory.getLog(HibernateCommonDAO.class);
    }

    public boolean isWhiteListedSenderAddress(int userId, String shortCode) {

	SenderAddress senderAddress = null;

	Session session = getSession();

	senderAddress = (SenderAddress) session
		.createQuery(
			"from SenderAddress where user.id = ? and shortCode = ?")
		.setParameter(0, userId).setParameter(1, shortCode)
		.uniqueResult();
	if (senderAddress != null) {

	    return true;
	}

	return false;
    }


    @SuppressWarnings("deprecation")
    public List<ManageNumber> getWhitelisted(int userid, List numbers) {

	Session sess = getSession();

	List<ManageNumber> whitelisted = null;
	try {
	    whitelisted = sess
		    .createQuery(
			    "from ManageNumber where user.id = :userid and number  in(:numbers)")
		    .setParameter("userid", Integer.valueOf(userid))
		    .setParameterList("numbers", numbers).list();
	    /*
	     * query.setP query.setParameter("userid", userid);
	     * query.setParameterList( "numbers", numbers);
	     * whitelisted=query.list();
	     */
	} catch (Exception e) {
	    System.out.println("getUserWhitelist: " + e);
	    LOG.error("getWhitelisted", e);
	} finally {
	    // sess.close();
	}
	return whitelisted;
    }

    @Override
    public APITypes getAPIType(String api) throws Exception {
	Session sess = getSession();
	APITypes apis = null;
	try {
	    apis = (APITypes) sess
		    .createQuery("from APITypes WHERE lower(apiname) = :api")
		    .setParameter("api", api).uniqueResult();

	} catch (Exception e) {
	    LOG.error("getAPITypes", e);
	    throw e;
	}

	return apis;
    }

    @Override
    public APIServiceCalls getServiceCall(int apiId, String service)
	    throws Exception {
	Session sess = getSession();
	APIServiceCalls services = null;
	try {
	    services = (APIServiceCalls) sess
		    .createQuery(
			    "from APIServiceCalls WHERE apiType.id = ? AND lower(serviceName) = ?")
		    .setParameter(0, apiId).setParameter(1, service)
		    .uniqueResult();

	} catch (Exception e) {
	    LOG.error("getServiceCall", e);
	    throw e;
	}

	return services;
    }

    @Override
    public Attributes getAttribute(String attribute) throws Exception {
	Session sess = getSession();
	Attributes attributeObj = null;
	try {
	    attributeObj = (Attributes) sess
		    .createQuery(
			    "from Attributes WHERE lower(attributeName) = :attribute")
		    .setParameter("attribute", attribute).uniqueResult();

	} catch (Exception e) {
	    LOG.error("getAttribute", e);
	    throw e;
	}

	return attributeObj;
    }

    @Override
    public AttributeDistribution getAttributeDistribution(int apiServiceId,
	    int attributeId) throws Exception {
	Session sess = getSession();
	AttributeDistribution distributionObj = null;
	try {
	    distributionObj = (AttributeDistribution) sess
		    .createQuery(
			    "from AttributeDistribution WHERE attribute.attributeId = ? AND serviceCall.apiServiceCallId = ?")
		    .setParameter(0, attributeId).setParameter(1, apiServiceId)
		    .uniqueResult();

	} catch (Exception e) {
	    LOG.error("getServiceCall", e);
	    throw e;
	}

	return distributionObj;
    }

    @Override
    public void saveAttributeValue(AttributeValues valueObj) throws Exception {

	try {
	    saveOrUpdate(valueObj);
	} catch (Exception e) {
	    LOG.error("saveAttributeValue", e);
	    throw e;
	}

    }
    
    public void saveAttributeValue(List<AttributeValues> valueObj) throws Exception {
	
	List<Object> objectList = new ArrayList<Object>(valueObj);
	try {
	    saveOrUpdateList(objectList);
	} catch (Exception e) {
	    LOG.error("saveAttributeValue", e);
	    throw e;
	}

    }

    @Override
    public AttributeValues getAttributeValue(
	    AttributeDistribution distributionObj) throws Exception {
	Session sess = getSession();
	AttributeValues value = null;
	try {
	    value = (AttributeValues) sess
		    .createQuery(
			    "from AttributeValues where attributeDistribution = ?")
		    .setParameter(0, distributionObj).uniqueResult();
	} catch (Exception e) {
	    LOG.error("getAttributeValue", e);
	    throw e;
	}
	return value;
    }

    @Override
    public List<AttributeDistribution> getAttributeDistributionByServiceCall(
	    int api, int serviceName) throws Exception {
	Session sess = getSession();
	List<AttributeDistribution> distributionList = new ArrayList<AttributeDistribution>();

	try {
	    StringBuilder hqlQueryBuilder = new StringBuilder();
	    hqlQueryBuilder.append("select dist ");
	    hqlQueryBuilder
		    .append("from AttributeDistribution dist,APITypes type,APIServiceCalls call");
	    hqlQueryBuilder.append(" WHERE");
	    hqlQueryBuilder.append(" type.id = :api AND");
	    hqlQueryBuilder.append(" type.id = call.apiType.id AND");
	    hqlQueryBuilder.append(" call.apiServiceCallId = :serviceName AND");
	    hqlQueryBuilder
		    .append(" call.apiServiceCallId = dist.serviceCall.apiServiceCallId ");

	    distributionList = (List<AttributeDistribution>) sess
		    .createQuery(hqlQueryBuilder.toString())
		    .setParameter("api", api)
		    .setParameter("serviceName", serviceName).getResultList();
	} catch (Exception e) {
	    LOG.error("getAttributeDistributionByServiceCall", e);
	    throw e;
	}
	return distributionList;
    }

    @Override
    public List<APITypes> getAllAPIType() throws Exception {
	Session sess = getSession();
	List<APITypes> apiList = new ArrayList<APITypes>();

	try {
	    apiList = (List<APITypes>) sess.createQuery("from APITypes ")
		    .getResultList();
	} catch (Exception e) {
	    LOG.error("getAllAPIType", e);
	    throw e;
	}
	return apiList;
    }

    @Override
    public List<APIServiceCalls> getAllServiceCall(int apiId) throws Exception {
	Session sess = getSession();
	List<APIServiceCalls> apiServiceList = new ArrayList<APIServiceCalls>();

	try {
	    apiServiceList = (List<APIServiceCalls>) sess
		    .createQuery(
			    "from APIServiceCalls where apiType.id = :apiId")
		    .setParameter("apiId", apiId).getResultList();
	} catch (Exception e) {
	    LOG.error("getAllServiceCall", e);
	    throw e;
	}
	return apiServiceList;
    }

    @Override
    public List<AttributeValues> getAttributeListValue(
	    List<AttributeDistribution> distributionObj) throws Exception {
	Session sess = getSession();
	List<AttributeValues> value = null;
	try {
	    value = (List<AttributeValues>) sess
		    .createQuery(
			    "from AttributeValues where attributeDistribution IN ( :distributionObj)")
		    			.setParameterList("distributionObj", distributionObj).list();
	} catch (Exception e) {
	    LOG.error("getAttributeValue", e);
	    throw e;
	}
	return value;
    }
    
    @Override
    public AttributeValues getAttributeValue(
	    AttributeDistribution distributionObj, int ownerdid) throws Exception {
	Session sess = getSession();
	AttributeValues value = null;
	try {
	    value = (AttributeValues) sess
		    .createQuery(
			    "from AttributeValues where attributeDistribution = ? and ownerdid = ?")
		    .setParameter(0, distributionObj).setParameter(1, ownerdid).uniqueResult();
	} catch (Exception e) {
	    LOG.error("getAttributeValue", e);
	    throw e;
	}
	return value;
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

}
