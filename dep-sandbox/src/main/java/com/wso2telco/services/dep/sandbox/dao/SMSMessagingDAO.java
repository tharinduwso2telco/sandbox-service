package com.wso2telco.services.dep.sandbox.dao;

import com.wso2telco.services.dep.sandbox.dao.model.domain.SMSDeliveryStatus;
import com.wso2telco.services.dep.sandbox.dao.model.domain.SMSMessagingParam;
import com.wso2telco.services.dep.sandbox.dao.model.domain.SMSRequestLog;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

public interface SMSMessagingDAO {
	public SMSDeliveryStatus getPreviousSMSDeliveryDetailsByMtSMSTransactionId(String mtSMSTransactionId);
	public SMSRequestLog getPreviousSMSRequestDetailsBySMSId(int smsId);
	public boolean saveQueryDeliveryStatusTransaction(String senderAddress, String addresses, String message,
			String clientCorrelator, String senderName, String notifyURL, String callbackData, Integer batchsize,
			String status, Integer txntype, String criteria, String notificationFormat, User user, String requestId) ;
	public SMSMessagingParam getSMSMessagingParam(int userId);
	
	public String saveSendSMSTransaction(String senderAddress, String addresses, String message,
			String clientCorrelator, String senderName, String notifyURL, String callbackData, Integer batchsize,
			String status, Integer txntype, String criteria, String notificationFormat, User user,
			String deliveryStatus) ;

}
