package com.wso2telco.services.dep.sandbox.dao;

import java.util.Date;
import java.util.List;

import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;



public interface LoggingDAO {
	
	public int saveMessageLog(MessageLog messageLog) throws Exception;
	
	public List<MessageLog> getMessageLogs(int userid, List<Integer> serviceNameIds, String reference, String value, Date startTimeStamp, Date endTimeStamp) throws Exception;
	
}