package com.wso2telco.services.dep.sandbox.dao;

import com.wso2telco.services.dep.sandbox.dao.model.domain.*;


import java.util.List;


public interface SMSMessagingDAO {

	public MessageLog getPrevSMSDeliveryDataByTransId(int transactionId);
	public SMSRequestLog getPrevSMSRequestDataById(int smsId);
	public SMSMessagingParam getSMSMessagingParam(int userId);
	public List<SendSMSToApplication> getMessageInbound(String regid, Integer userid);

	boolean saveDeliverySubscription(User userId, String sender_address, int sub_status, String notify_url, String filter,
									 String callbackData, String clinetCorrelator, String request);

	boolean isSubscriptionExists(String filterCriteria, String notifyUrl, String callbackData, String clientCorrelator);
	boolean removeSubscription(int userId, String senderAddress);
	public int saveSubscribeSMSRequest(SubscribeSMSRequest subscribeSMSRequest) throws Exception;
	public boolean removeSubscriptionToMessage(String subscriptionID) throws Exception;
	boolean saveSMSParameters(SMSMessagingParam messagingParam);

}
