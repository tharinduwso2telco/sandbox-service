package com.wso2telco.services.dep.sandbox.servicefactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.location.LocationRequestFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.payment.PaymentRequestFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.SMSRequestFactory;

public class RequestBuilderFactory {

	static Log  LOG = LogFactory.getLog(RequestBuilderFactory.class);

	@SuppressWarnings("rawtypes")
	public static RequestHandleable getInstance(final RequestDTO requestDTO) {

		RequestHandleable requestHandler = null;

		switch (requestDTO.getRequestType()) {
		case LOCATION:
			LOG.debug("LOADING LOCATION FACTORY");
			requestHandler = LocationRequestFactory.getInstance(requestDTO);
			break;
		case MOBILEID:
			LOG.debug("LOADING MOBILEID FACTORY");
			break;
		case SMSMESSAGING:
			LOG.debug("LOADING SMS MESSAGING FACTORY");
			requestHandler = SMSRequestFactory.getInstance(requestDTO);
			break;
		case USSD:
			LOG.debug("LOADING USSD FACTORY");
			break;
		case PAYMENT:
			LOG.debug("LOADING PAYMENT FACTORY");
			requestHandler = PaymentRequestFactory.getInstance(requestDTO);
			break;
		default:
			break;
		}

		return requestHandler;
	}
}
