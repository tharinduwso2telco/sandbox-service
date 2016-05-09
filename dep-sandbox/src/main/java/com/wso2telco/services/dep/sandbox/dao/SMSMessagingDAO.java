package com.wso2telco.services.dep.sandbox.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Date;
import com.wso2telco.services.dep.sandbox.dao.model.domain.SMSDeliveryStatus;
import com.wso2telco.services.dep.sandbox.dao.model.domain.SMSMessagingParam;
import com.wso2telco.services.dep.sandbox.dao.model.domain.SMSRequestLog;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

public class SMSMessagingDAO extends CommonDAO {

	public SMSMessagingParam getSMSMessagingParam (int userId){
	
		SMSMessagingParam smsMessagingParam = null;
		
		Session session = getSession();
        
		smsMessagingParam = (SMSMessagingParam) session.createQuery("from SMSMessagingParam where userid = ?").setInteger(0, userId).uniqueResult();
				
		return smsMessagingParam;
	}
	
	
	public String saveSendSMSTransaction(String senderAddress, String addresses, String message,
			String clientCorrelator, String senderName, String notifyURL, String callbackData, Integer batchsize,
			String status, Integer txntype, String criteria, String notificationFormat, User user,
			String deliveryStatus) {

		Session session = null;
		Transaction tx = null;
		String mtSMSTransactionId = null;
		SMSRequestLog smsRequest = new SMSRequestLog();

		try {
			session = getSession();
			tx = session.beginTransaction();

			smsRequest.setSenderAddress(senderAddress);
			smsRequest.setAddresses((String) addresses);
			smsRequest.setMessage(message);
			smsRequest.setClientCorrelator(clientCorrelator);
			smsRequest.setSenderName(senderName);
			smsRequest.setNotifyURL(notifyURL);
			smsRequest.setCallbackData(callbackData);
			smsRequest.setUser(user);
			smsRequest.setDate(new Date());
			smsRequest.setBatchsize(batchsize);
			smsRequest.setTransactionstatus(status);
			smsRequest.setTxntype(txntype);
			smsRequest.setCriteria(criteria);
			smsRequest.setNotificationFormat(notificationFormat);
			session.save(smsRequest);

			int smsId = smsRequest.getSmsId();
			SMSRequestLog lastSendSMSRequest = (SMSRequestLog) session.get(SMSRequestLog.class, smsId);
			if (lastSendSMSRequest == null) {

				throw new Exception("Last transaction not found");
			}

			mtSMSTransactionId = "smstran-" + smsId;
			lastSendSMSRequest.setTransactionId(mtSMSTransactionId);
			session.update(lastSendSMSRequest);

			SMSDeliveryStatus smsDeliveryStatus = new SMSDeliveryStatus();
			smsDeliveryStatus.setTransactionId(mtSMSTransactionId);
			smsDeliveryStatus.setSenderAddress(senderAddress);
			smsDeliveryStatus.setDeliveryStatus(deliveryStatus);
			smsDeliveryStatus.setDate(new Date());
			smsDeliveryStatus.setUser(user);
			session.save(smsDeliveryStatus);

			tx.commit();
		} catch (Exception e) {

			tx.rollback();
			return null;
		} finally {
			session.close();
		}

		return mtSMSTransactionId;
	}
}
