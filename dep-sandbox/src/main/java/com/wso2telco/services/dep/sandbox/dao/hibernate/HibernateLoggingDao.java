package com.wso2telco.services.dep.sandbox.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;


import com.wso2telco.core.mi.hibernate.HibernateAbstractDAO;
import com.wso2telco.services.dep.sandbox.dao.LoggingDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;


public class HibernateLoggingDao extends HibernateAbstractDAO implements LoggingDAO {
	
	{
		LOG = LogFactory.getLog(HibernateProvisioningDAO.class);
	}

	public void saveMessageLog(MessageLog messageLog) throws Exception {
		try {
			saveOrUpdate(messageLog);
		} catch (Exception ex) {
			LOG.error("Error in SaveMessageLog " + ex);
			throw ex;
		}
		
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
			if(userid != 0 || serviceNameIds != null || startTimeStamp != null || endTimeStamp != null || (reference != null && value != null)){
				hqlQueryBuilder.append("from MessageLog ml ");
				hqlQueryBuilder.append("where ");
				if(userid != 0){
					hqlQueryBuilder.append("ml.userid = :id");
					parameterMap.put("id", userid);
					if(serviceNameIds != null)
						hqlQueryBuilder.append(" AND ml.servicenameid in (:servicenameidList)");
					if(reference != null && value != null){
						hqlQueryBuilder.append(" AND ml.reference = :reference AND ml.value = :value");
						parameterMap.put("reference", reference);
						parameterMap.put("value", value);
					}	
					if(startTimeStamp != null){
						hqlQueryBuilder.append(" AND ml.messageTimestamp >= :startTimeStamp");
						parameterMap.put("startTimeStamp", startTimeStamp);
					}
					if(endTimeStamp != null){
						hqlQueryBuilder.append(" AND ml.messageTimestamp <= :endTimeStamp");
						parameterMap.put("endTimeStamp", endTimeStamp);
					}
				}else if(serviceNameIds != null){
					hqlQueryBuilder.append("ml.servicenameid in (:servicenameidList)");
					if(reference != null && value != null){
						hqlQueryBuilder.append(" AND ml.reference = :reference AND ml.value = :value");
						parameterMap.put("reference", reference);
						parameterMap.put("value", value);
					}
					if(startTimeStamp != null){
						hqlQueryBuilder.append(" AND ml.messageTimestamp >= :startTimeStamp");
						parameterMap.put("startTimeStamp", startTimeStamp);
					}
					if(endTimeStamp != null){
						hqlQueryBuilder.append(" AND ml.messageTimestamp <= :endTimeStamp");
						parameterMap.put("endTimeStamp", endTimeStamp);
					}
				}else if(reference != null && value != null){
					hqlQueryBuilder.append("ml.reference = :reference AND ml.value = :value");
					parameterMap.put("reference", reference);
					parameterMap.put("value", value);
					if(startTimeStamp != null){
						hqlQueryBuilder.append(" AND ml.messageTimestamp >= :startTimeStamp");
						parameterMap.put("startTimeStamp", startTimeStamp);
					}
					if(endTimeStamp != null){
						hqlQueryBuilder.append(" AND ml.messageTimestamp <= :endTimeStamp");
						parameterMap.put("endTimeStamp", endTimeStamp);
					}
				}else if(startTimeStamp != null){
					hqlQueryBuilder.append("ml.messageTimestamp >= :startTimeStamp");
					parameterMap.put("startTimeStamp", startTimeStamp);
					if(endTimeStamp != null){
						hqlQueryBuilder.append(" AND ml.messageTimestamp <= :endTimeStamp");
						parameterMap.put("endTimeStamp", endTimeStamp);
					}
				}else if(endTimeStamp != null){
					hqlQueryBuilder.append("ml.messageTimestamp <= :endTimeStamp");
					parameterMap.put("endTimeStamp", endTimeStamp);
				}
				
				Query query = session.createQuery(hqlQueryBuilder.toString());
				
				Set<Entry<String, Object>> entrySet = parameterMap.entrySet();

				for (Entry<String, Object> entry : entrySet) {
					query.setParameter(entry.getKey(), entry.getValue());
				}
				if(serviceNameIds != null)
					query.setParameterList("servicenameidList", serviceNameIds);
				
				messageLogs = (List<MessageLog>) query.getResultList();
			}
		} catch (Exception ex) {
			LOG.error("Error in getMessageLogs " + ex);
			throw ex;
		}
		return messageLogs;
	}
	
	

}