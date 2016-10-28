package com.wso2telco.services.dep.sandbox.servicefactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.credit.CreditServiceFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.customerinfo.CustomerInfoServiceFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.customerinfoConfig.CustomerInfoConfigServiceFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.location.LocationRequestFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.payment.PaymentRequestFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.provisionConfig.ProvisioningConfigServiceFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.provisioning.ProvisioningServiceFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.SMSRequestFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.user.UserServiceFactory;

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
		case PROVISIONING:
			LOG.debug("LOADING PROVISIONING FACTORY");
			requestHandler = ProvisioningServiceFactory.getInstance(requestDTO);
			break;
		case PROVISIONINGCONFIG:
			LOG.debug("LOADING PROVISIONING CONFIGURATION FACTORY");
			requestHandler = ProvisioningConfigServiceFactory.getInstance(requestDTO);
			break;
		case USER:
			LOG.debug("LOADING USER FACTORY");
			requestHandler = UserServiceFactory.getInstance(requestDTO);
			break;
		case CUSTOMERINFO:
		    	LOG.debug("LOADING CUSTOMERINFO FACTORY");
		    	requestHandler = CustomerInfoServiceFactory.getInstance(requestDTO);
		    	break;
		case CUSTOMERINFOCONFIG:
		    	LOG.debug("LOADING CUSTOMERINFO CONFIGURATION FACTORY");
		    	requestHandler = CustomerInfoConfigServiceFactory.getInstance(requestDTO);
		    	break;
		case CREDIT:
	    	LOG.debug("LOADING CREDIT CONFIGURATION FACTORY");
	    	requestHandler = CreditServiceFactory.getInstance(requestDTO);
	    	break;
		default:
			LOG.debug("APPROPIATE FACTORY CLASS NOT FOUND");
			break;
		}

		return requestHandler;
	}
}
