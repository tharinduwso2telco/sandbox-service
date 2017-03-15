package com.wso2telco.services.dep.sandbox.dao;

import com.wso2telco.services.dep.sandbox.dao.model.domain.*;


import java.util.List;


public interface SMSMessagingDAO {

	public MessageLog getPreviousSMSDeliveryDetailsByMtSMSTransactionId(int mtSMSTransactionId);
	public SMSRequestLog getPreviousSMSRequestDetailsBySMSId(int smsId);
	public boolean saveQueryDeliveryStatusTransaction(String senderAddress, String addresses, String message,
			String clientCorrelator, String senderName, String notifyURL, String callbackData, Integer batchsize,
			String status, Integer txntype, String criteria, String notificationFormat, User user, String requestId) ;
	public SMSMessagingParam getSMSMessagingParam(int userId);
	/*public boolean saveDeliveryStatusResponse(String requestUrl,int status, int type, int serviceId, int userId,
											 String reference, String referenceValue);*/

	public String saveSendSMSTransaction(String senderAddress, String addresses, String message,
			String clientCorrelator, String senderName, String notifyURL, String callbackData, Integer batchsize,
			String status, Integer txntype, String criteria, String notificationFormat, User user,
			String deliveryStatus) ;
	public List<SendSMSToApplication> getMessageInbound(String regid, Integer userid);
	boolean saveDeliverySubscription(User userId, String sender_address, int sub_status, String notify_url, String filter,
									 String callbackData, String clinetCorrelator, String request);

	boolean isSubscriptionExists(String filterCriteria, String notifyUrl, String callbackData, String clientCorrelator);
	boolean removeSubscription(int userId, String senderAddress);
	public int saveSubscribeSMSRequest(SubscribeSMSRequest subscribeSMSRequest) throws Exception;

	public boolean removeSubscriptionToMessage(String subscriptionID) throws Exception;


	public int saveSubscribeSMSRequest(String destinationAddress, String notifyURL, String callbackData, String criteria, String clientCorrelator, User user) throws Exception;


}
