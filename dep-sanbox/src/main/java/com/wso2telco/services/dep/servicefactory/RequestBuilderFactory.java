package com.wso2telco.services.dep.servicefactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.model.RequestDTO;
import com.wso2telco.services.dep.servicefactory.location.LocationRequestFactory;
import com.wso2telco.services.dep.servicefactory.payment.PaymentRequestFactory;
import com.wso2telco.services.dep.servicefactory.sms.SMSRequestFactory;

public class RequestBuilderFactory {

	Log LOG = LogFactory.getLog(RequestBuilderFactory.class);

	public RequestHandleable getInstance(final RequestDTO requestDTO) {
		RequestHandleable requestHandler = null;
		switch (requestDTO.getRequestType()) {
		case LOCATION:
			LOG.debug("lOADING LOCATION FACTORY");
			requestHandler = LocationRequestFactory.getInstance(requestDTO);
			break;
		case MOBILEID:
			LOG.debug("lOADING MOBILEID FACTORY");

			break;
		case SMS:
			LOG.debug("lOADING sms FACTORY");
			requestHandler = SMSRequestFactory.getInstance(requestDTO);
			break;
		case USSD:
			LOG.debug("lOADING USSD FACTORY");
			break;
		case PAYMENT:
			LOG.debug("lOADING PAYMENT FACTORY");
			requestHandler = PaymentRequestFactory.getInstance(requestDTO);
			break;
		default:
			break;
		}
		return requestHandler;

	}

}
