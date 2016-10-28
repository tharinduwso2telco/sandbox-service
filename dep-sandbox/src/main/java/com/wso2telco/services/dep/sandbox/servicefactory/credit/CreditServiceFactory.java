package com.wso2telco.services.dep.sandbox.servicefactory.credit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;

public class CreditServiceFactory {

	private static Log LOG = LogFactory.getLog(CreditServiceFactory.class);

	public static RequestHandleable getInstance(final RequestDTO requestDTO) {

		final String CREDIT_APPLY = "apply";
		final String PATIAL_REFUND = "refund";

		if (requestDTO.getRequestPath().toLowerCase().contains(CREDIT_APPLY) && requestDTO.isPost()) {
			LOG.debug("###CREDIT### LOADING CREDIT APPLY SERVICE");
			return new CreditApplyRequestHandler();
		} else if (requestDTO.getRequestPath().toLowerCase().contains(PATIAL_REFUND) && requestDTO.isPost()) {
			LOG.debug("###CREDIT### LOADING PATIAL REFUND SERVICE");
			return new PatialRefundRequestHandler();
		}
		LOG.debug("###CREDIT### APPROPIATE SERVICE CLASS NOT FOUND ");
		return null;

	}

}
