package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging;

import com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.gateway
        .StopSubscribeToDeliveryNotificationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;


public class SMSRequestFactory {

	private static Log LOG = LogFactory.getLog(SMSRequestFactory.class);

	public static RequestHandleable getInstance(final RequestDTO requestDTO) {

		final String QUERY_DELIVERY_STATUS = "deliveryinfos";
		final String SEND_MT_SMS = "requests";
		final String RETRIVE_SMS = "registrations";
		final String OUTBOUND_REQUEST = "outbound";
		final String INBOUND_REQUEST = "inbound";

		if (requestDTO.getRequestPath().toLowerCase().contains(QUERY_DELIVERY_STATUS)
				&& requestDTO.isGet()) {

			LOG.debug("LOADING QUERY SMS DELIVERY STATUS SERVICE");
			return new QuerySMSDeliveryStatusService();

		} else if (requestDTO.getRequestPath().toLowerCase().contains(RETRIVE_SMS)
				&& requestDTO.isGet()) {

			LOG.debug("LOADING RETRIEVE SMS MESSAGES SERVICE");
			return new RetrieveSMSMessagesService();

		} else if (requestDTO.getRequestPath().toLowerCase().contains(OUTBOUND_REQUEST)
				&& requestDTO.isDelete()) {

			LOG.debug("LOADING STOP SUBSCRIBE TO DELIVERY NOTIFICATION SERVICE");
			return new StopSubscribeToDeliveryNotificationService();
		} else if (requestDTO.getRequestPath().toLowerCase().contains(INBOUND_REQUEST)
				&& requestDTO.isPost()) {

			LOG.debug("LOADING SUBSCRIBE TO MO NOTIFICATION SERVICE");
			return new SubscribeToMONotificationService();
		} else if (requestDTO.getRequestPath().toLowerCase().contains(INBOUND_REQUEST)
				&& requestDTO.isDelete()) {

			LOG.debug("LOADING STOP SUBSCRIBE TO MO NOTIFICATION SERVICE");
			return new StopSubscribeToMONotificationService();
		}

		return null;
	}
}
