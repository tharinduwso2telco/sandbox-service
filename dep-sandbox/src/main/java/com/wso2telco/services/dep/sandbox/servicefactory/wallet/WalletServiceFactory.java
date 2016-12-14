package com.wso2telco.services.dep.sandbox.servicefactory.wallet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;

public class WalletServiceFactory {
	private static Log LOG = LogFactory.getLog(WalletServiceFactory.class);

	public static RequestHandleable getInstance(final RequestDTO requestDTO) {
		final String MAKE_PAYMENT = "payment";
		final String LIST_PAYMENT = "list";
		final String REFUND_PAYMENT = "refund";
		final String BALANCE_LOOKUP = "balance";

		if (requestDTO.getRequestPath().contains(MAKE_PAYMENT)) {
			LOG.debug("LOADING MAKE PAYMENT SERVICE");
			return new MakePaymentRequestHandler();
		} else if (requestDTO.getRequestPath().contains(LIST_PAYMENT)) {
			LOG.debug("LOADING LIST PAYMENT SERVICE");
			return new ListTransactionRequestHandler();
		} else if (requestDTO.getRequestPath().contains(REFUND_PAYMENT)) {
			LOG.debug("LOADING REFUND PAYMENT SERVICE");
			return new RefundTransactionRequestHandler();
		} else if (requestDTO.getRequestPath().contains(BALANCE_LOOKUP)) {
			LOG.debug("LOADING BALANCE LOOKUP SERVICE");
			return new BalanceLookupRequestHandler();
		}
		return null;
	}

}
