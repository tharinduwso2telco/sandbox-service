package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;

import io.netty.handler.codec.http.HttpMethod;

public class SMSRequestFactory {

	private static Log LOG = LogFactory.getLog(SMSRequestFactory.class);

	public static RequestHandleable getInstance(final RequestDTO requestDTO) {

		final String QUERY_DELIVERY_STATUS = "deliveryInfos";
		final String SEND_MT_SMS = "requests";
		final String RETRIVE_SMS = "registrations";
		final String OUTBOUND_REQUEST = "outbound";
		final String INBOUND_REQUEST = "inbound";

		if (requestDTO.getRequestPath().toLowerCase().contains(QUERY_DELIVERY_STATUS)
				&& requestDTO.getHttpMethod() == HttpMethod.GET) {

			LOG.debug("LOADING QUERY SMS DELIVERY STATUS SERVICE");
			return new QuerySMSDeliveryStatusService();
		} else if (requestDTO.getRequestPath().toLowerCase().contains(SEND_MT_SMS)
				&& requestDTO.getHttpMethod() == HttpMethod.POST) {

			LOG.debug("LOADING SEND MT SMS SERVICE");
			return new SendMTSMSService();
		} else if (requestDTO.getRequestPath().toLowerCase().contains(RETRIVE_SMS)
				&& requestDTO.getHttpMethod() == HttpMethod.GET) {

			LOG.debug("LOADING RETRIEVE SMS MESSAGES SERVICE");
			return new RetrieveSMSMessagesService();
		} else if (requestDTO.getRequestPath().toLowerCase().contains(OUTBOUND_REQUEST)
				&& requestDTO.getHttpMethod() == HttpMethod.POST) {

			LOG.debug("LOADING SUBSCRIBE TO DELIVERY NOTIFICATION SERVICE");
			return new SubscribeToDeliveryNotificationService();
		} else if (requestDTO.getRequestPath().toLowerCase().contains(OUTBOUND_REQUEST)
				&& requestDTO.getHttpMethod() == HttpMethod.DELETE) {

			LOG.debug("LOADING STOP SUBSCRIBE TO DELIVERY NOTIFICATION SERVICE");
			return new StopSubscribeToDeliveryNotificationService();
		} else if (requestDTO.getRequestPath().toLowerCase().contains(INBOUND_REQUEST)
				&& requestDTO.getHttpMethod() == HttpMethod.POST) {

			LOG.debug("LOADING SUBSCRIBE TO MO NOTIFICATION SERVICE");
			return new SubscribeToMONotificationService();
		} else if (requestDTO.getRequestPath().toLowerCase().contains(INBOUND_REQUEST)
				&& requestDTO.getHttpMethod() == HttpMethod.DELETE) {

			LOG.debug("LOADING STOP SUBSCRIBE TO MO NOTIFICATION SERVICE");
			return new StopSubscribeToMONotificationService();
		} else {

			// should return unsupported api exception
		}

		return null;
	}
}
