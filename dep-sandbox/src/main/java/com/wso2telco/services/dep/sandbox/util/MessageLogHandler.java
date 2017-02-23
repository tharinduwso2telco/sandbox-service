package com.wso2telco.services.dep.sandbox.util;


import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wso2telco.services.dep.sandbox.dao.LoggingDAO;
import com.wso2telco.services.dep.sandbox.dao.hibernate.HibernateLoggingDao;
import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;



public class MessageLogHandler {

	private Log log = LogFactory.getLog(MessageLogHandler.class);
    private ExecutorService executorService;
    private static MessageLogHandler instance;
    private LoggingDAO dbservice;

    private MessageLogHandler() {
        executorService = Executors.newFixedThreadPool(10);
        dbservice = new HibernateLoggingDao();
    }

    public synchronized static MessageLogHandler getInstance() {
        if (instance == null) {
            instance = new MessageLogHandler();
        }
        return instance;
    }

    public void saveMessageLog(final int servicenameid, final int userid, final String reference, final String value, JSONObject obj) throws Exception {
    	MessageLog messageLog = new MessageLog();
        executorService.execute(new Runnable() {
            public void run() {
            	StringWriter out = new StringWriter();
                try {
                	obj.writeJSONString(out);
                	String jsonString = out.toString();
                	log.debug("JSON Sting" + jsonString);

                	messageLog.setServicenameid(servicenameid);
                	messageLog.setUserid(userid);
                	messageLog.setReference(reference);
                	messageLog.setValue(value);
                	messageLog.setRequest(jsonString);
                	messageLog.setMessageTimestamp(new Date());
                	log.debug("messagelog object has been created");
                	dbservice.saveMessageLog(messageLog);

                } catch (Exception e) {
                    log.debug("error while inserting data into database", e);
                }
            }
        });
    }
    public List<MessageLog> getMessageLogs(final int userid, final List<Integer> serviceNameIds, final String reference, final String value, final Date startTimeStamp,
			final Date endTimeStamp) throws Exception {

		return dbservice.getMessageLogs(userid, serviceNameIds, reference, value, startTimeStamp, endTimeStamp);
    }

}