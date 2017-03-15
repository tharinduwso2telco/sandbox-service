package com.wso2telco.services.dep.sandbox.dao.hibernate;

import java.util.Date;
import java.util.List;

import com.wso2telco.services.dep.sandbox.dao.model.domain.*;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.wso2telco.services.dep.sandbox.dao.SMSMessagingDAO;

class HibernateSMSMessagingDAO extends HibernateCommonDAO  implements SMSMessagingDAO{

	{
		LOG = LogFactory.getLog(HibernateSMSMessagingDAO.class);
	}
	
	
	public SMSMessagingParam getSMSMessagingParam(int userId) {

		SMSMessagingParam smsMessagingParam = null;

		Session session = getSession();

		smsMessagingParam = (SMSMessagingParam) session.createQuery("from SMSMessagingParam where userid = ?")
				.setInteger(0, userId).uniqueResult();

		return smsMessagingParam;
	}

	public SMSDeliveryStatus getPreviousSMSDeliveryDetailsByMtSMSTransactionId(String mtSMSTransactionId) {

		SMSDeliveryStatus smsDeliveryStatus = null;

		Session session = getSession();

		smsDeliveryStatus = (SMSDeliveryStatus) session.get(SMSDeliveryStatus.class, mtSMSTransactionId);

		return smsDeliveryStatus;
	}

	public SMSRequestLog getPreviousSMSRequestDetailsBySMSId(int smsId) {

		SMSRequestLog smsRequestLog = null;

		Session session = getSession();

		smsRequestLog = (SMSRequestLog) session.get(SMSRequestLog.class, smsId);

		return smsRequestLog;
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

	@Override
	public List<SendSMSToApplication> getMessageInbound(String registrationID, Integer userid) {

		List<SendSMSToApplication> sendSMSToApplicationList;
		Session session = getSession();

		sendSMSToApplicationList = session.createQuery("from SendSMSToApplication where destinationAddress = ? and user.id = ?").setString(0, registrationID).setInteger(1, userid).list();

		return sendSMSToApplicationList;

	}

    @Override
    public int saveSubscribeSMSRequest(String destinationAddress, String notifyURL, String callbackData, String
            criteria, String clientCorrelator, User user) throws Exception {


        Session session = null;
        Transaction transaction = null;
        Integer subsid = null;

        try {

            session = getSession();
            transaction = session.beginTransaction();

            SubscribeSMSRequest subscribeSMSRequest = new SubscribeSMSRequest();
            subscribeSMSRequest.setCallbackData(callbackData);
            subscribeSMSRequest.setClientCorrelator(clientCorrelator);
            subscribeSMSRequest.setCriteria(criteria);
            subscribeSMSRequest.setDestinationAddress(destinationAddress);
            subscribeSMSRequest.setNotifyURL(notifyURL);
            subscribeSMSRequest.setUser(user);
            subscribeSMSRequest.setDate(new Date());
            subscribeSMSRequest.setNotificationFormat("JSON");
            session.save(subscribeSMSRequest);
            subsid = subscribeSMSRequest.getSubscribeId();

            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return subsid;

    }


	public boolean saveQueryDeliveryStatusTransaction(String senderAddress, String addresses, String message,
			String clientCorrelator, String senderName, String notifyURL, String callbackData, Integer batchsize,
			String status, Integer txntype, String criteria, String notificationFormat, User user, String requestId) {

		Session session = null;
		Transaction tx = null;
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
			smsRequest.setRequestId(requestId);
			session.save(smsRequest);

			tx.commit();
		} catch (Exception e) {

			tx.rollback();
			return false;
		} finally {

			session.close();
		}

		return true;
	}
}
