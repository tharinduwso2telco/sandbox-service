package com.wso2telco.services.dep.sandbox.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wso2telco.services.dep.sandbox.dao.model.custom.DeliveryReceiptSubscription;
import com.wso2telco.services.dep.sandbox.dao.model.domain.*;
import com.wso2telco.services.dep.sandbox.dao.model.domain.*;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
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

	public MessageLog getPrevSMSDeliveryDataByTransId(int mtSMSTransactionId) {

		MessageLog smsDeliveryStatus = null;

		Session session = getSession();

		smsDeliveryStatus = (MessageLog) session.get(MessageLog.class, mtSMSTransactionId);

		return smsDeliveryStatus;
	}


	public SMSRequestLog getPrevSMSRequestDataById(int smsId) {

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
    public int saveSubscribeSMSRequest(SubscribeSMSRequest SubscribeSMSRequest) throws Exception {

        Session session = null;
        Transaction transaction = null;
        Integer subsid = null;

        try {
            session = getSession();
            transaction = session.beginTransaction();
            session.save(SubscribeSMSRequest);
            subsid = SubscribeSMSRequest.getSubscribeId();
            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();
            LOG.error("Error occurred When save subscribe SMS request",e);
            throw e;

		} finally {
            session.close();
        }

        return subsid;

    }

	@Override
	public boolean removeSubscriptionToMessage(String subscriptionID) throws Exception {

        boolean isExists = false;
        Session session = null;
        Transaction transaction = null;

        try {
            session = getSession();
            transaction = session.beginTransaction();
            Query query= session.createQuery("delete FROM SubscribeSMSRequest WHERE subscribe_id = :id");
            query.setInteger("id", Integer.parseInt(subscriptionID));
            int i = query.executeUpdate();

            if (i >0) {
                isExists = true;
            }
            transaction.commit();

        } catch (Exception e) {
           transaction.rollback();
            LOG.error("Error occurred when remove subscription to message",e);
            throw e;

        } finally {
            session.close();
        }

        return isExists;
	}


		public boolean saveDeliverySubscription (User userId, String sender_address,int sub_status, String notify_url,
				String filter, String callbackData, String clinetCorrelator, String
		request){

			Session session = null;
			Transaction transaction = null;
			DeliverySubscription deliverySubscription = new DeliverySubscription();

			try {

				session = getSession();
				transaction = session.beginTransaction();

				deliverySubscription.setUser(userId);
				deliverySubscription.setSenderAddress(sender_address);
				deliverySubscription.setSubStatus(sub_status);
				deliverySubscription.setNotifyUrl(notify_url);
				deliverySubscription.setFilterCriteria(filter);
				deliverySubscription.setCallbackData(callbackData);
				deliverySubscription.setClientCorrelator(clinetCorrelator);
				deliverySubscription.setRequestData(request);

				session.save(deliverySubscription);

				transaction.commit();

			} catch (Exception ex) {
				transaction.rollback();
				LOG.error("Error occurred while trying to save subscription ",ex);
				throw ex;
			} finally {
				session.close();
			}

			return true;
		}

		@Override
		public boolean isSubscriptionExists (String filterCriteria, String notifyUrl, String callbackData, String
		clientCorrelator){

			boolean isExists = false;
			Session session = getSession();

			List<DeliverySubscription> resultedList = new ArrayList<>();

			try {

				StringBuilder hqlQueryBuilder = new StringBuilder();
				hqlQueryBuilder.append("FROM DeliverySubscription AS ds WHERE ds.filterCriteria = :filter AND ds" +
						".notifyUrl = :notify AND ds.callbackData = :callback AND ds.clientCorrelator = :client");

				resultedList = session.
						createQuery(hqlQueryBuilder.toString())
						.setParameter("filter", filterCriteria)
						.setParameter("notify", notifyUrl)
						.setParameter("callback", callbackData)
						.setParameter("client", clientCorrelator).getResultList();

				if (resultedList.size() > 0) {
					isExists = true;
				}
			} catch (Exception e) {
				LOG.error("###SUSCRIPTION### Error in retrieving subscriptions", e);
				throw e;
			} finally {
				session.close();
			}
			return isExists;
		}

		@Override
		public boolean removeSubscription ( int userId, String senderAddress){

			boolean isExists = false;
			Session session = null;
			Transaction tx = null;

			try {
				session = getSession();
				tx = session.beginTransaction();
				Query q = session.createQuery("delete FROM DeliverySubscription AS ds WHERE ds.id = :id AND ds" +
						".senderAddress = :sender_address");
				q.setInteger("id", userId);
				q.setParameter("sender_address", senderAddress);

				int i = q.executeUpdate();

				if (i == 1) {
					isExists = true;
				}
				tx.commit();


			} catch (Exception e) {
				tx.rollback();
				LOG.error("Error occurred while removing subscription",e);
				throw e;
			} finally {
				session.close();
			}

			return isExists;
		}






}
