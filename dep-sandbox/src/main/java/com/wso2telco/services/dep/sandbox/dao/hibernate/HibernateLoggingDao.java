package com.wso2telco.services.dep.sandbox.dao.hibernate;

import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


import com.wso2telco.core.mi.hibernate.HibernateAbstractDAO;
import com.wso2telco.services.dep.sandbox.dao.LoggingDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;


public class HibernateLoggingDao extends HibernateAbstractDAO implements LoggingDAO {
	
	{
		LOG = LogFactory.getLog(HibernateLoggingDao.class);
	}

	public int saveMessageLog(MessageLog messageLog) throws Exception {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		int txnReference = 0;
		try {
			session.save(messageLog);
			txnReference = messageLog.getId();
			transaction.commit();
		} catch (Exception ex) {
			transaction.rollback();
			LOG.debug("Error Occured While Saving Object. ", ex);
			throw ex;
		}
		return txnReference;
	}

	public List<MessageLog> getMessageLogs(int userid, List<Integer> serviceNameIds, String ref, String val, Date startTimeStamp,
			Date endTimeStamp) throws Exception {
		Session session = getSession();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		List<MessageLog> messageLogs = new ArrayList<MessageLog>();
		String reference = CommonUtil.getNullOrTrimmedValue(ref);
		String value = CommonUtil.getNullOrTrimmedValue(val);
		try {
			StringBuilder hqlQueryBuilder = new StringBuilder();
			hqlQueryBuilder.append("from MessageLog ml ");
			hqlQueryBuilder.append("where  1=1 ");

			if(userid != 0){
				hqlQueryBuilder.append(" AND ml.userid = :id");
				parameterMap.put("id", userid);

			}

			if(serviceNameIds != null){
				hqlQueryBuilder.append(" AND ml.servicenameid in (:servicenameidList)");
				parameterMap.put("servicenameidList", serviceNameIds);
			}

			if(reference != null && value != null){
				hqlQueryBuilder.append(" AND ml.reference = :reference AND ml.value = :value");
				parameterMap.put("reference", reference);
				parameterMap.put("value", value);
			}

			if(startTimeStamp != null && endTimeStamp != null){
				hqlQueryBuilder.append(" AND ml.messageTimestamp between :startTimeStamp and :endTimeStamp");
				parameterMap.put("startTimeStamp", startTimeStamp);
				parameterMap.put("endTimeStamp", endTimeStamp);

			}else if(startTimeStamp != null){
					hqlQueryBuilder.append("  AND ml.messageTimestamp >= :startTimeStamp");
					parameterMap.put("startTimeStamp", startTimeStamp);
					if(endTimeStamp != null){
						hqlQueryBuilder.append(" AND ml.messageTimestamp <= :endTimeStamp");
						parameterMap.put("endTimeStamp", endTimeStamp);
					}
			}else if(endTimeStamp != null){
					hqlQueryBuilder.append(" AND ml.messageTimestamp <= :endTimeStamp");
					parameterMap.put("endTimeStamp", endTimeStamp);
				}
				
			Query query = session.createQuery(hqlQueryBuilder.toString());

			Set<Entry<String, Object>> entrySet = parameterMap.entrySet();

			for (Entry<String, Object> entry : entrySet) {
				if(entry.getValue() instanceof  List ){
				query.setParameterList(entry.getKey(), (List)entry.getValue());
				}else{
					query.setParameter(entry.getKey(), entry.getValue());
				}

			}

			messageLogs = (List<MessageLog>) query.getResultList();

		} catch (Exception ex) {
			LOG.error("Error in getMessageLogs " , ex);
			throw ex;
		}
		if (messageLogs==null){
			return Collections.emptyList();
		}else{
		return messageLogs;
		}
	}

}