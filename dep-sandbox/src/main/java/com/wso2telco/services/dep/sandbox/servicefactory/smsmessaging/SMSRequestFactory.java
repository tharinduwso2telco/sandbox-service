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
		final String CONFIGAPP = "addsmsparameters";

		if (requestDTO.getRequestPath().toLowerCase().contains(QUERY_DELIVERY_STATUS)
				&& requestDTO.isGet()) {
			LOG.debug("LOADING QUERY SMS DELIVERY STATUS SERVICE");
			return new QuerySMSDeliveryStatusService();

	} else if (requestDTO.getRequestPath().toLowerCase().contains(CONFIGAPP)) {

		LOG.debug("LOADING CONFIGURATION SMS SERVICE");
		return new SMSParameterInsertConfigHandler();
	}
		return null;
	}
}
