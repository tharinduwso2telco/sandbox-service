package com.wso2telco.services.dep.sandbox.servicefactory.walletConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.wallet.WalletServiceFactory;

public class WalletConfigServiceFactory {
	
    private static Log LOG = LogFactory.getLog(WalletServiceFactory.class);

    public static RequestHandleable getInstance(final RequestDTO requestDTO) {
    final String ADD_ACCOUNT_INFO = "addAccountInfo";
    final String ADD_TRANSACTION_STATUS = "addTransactionStatus";
    final String GET_TRANSACTION_STATUS = "getTransactionStatus";
    final String GET_ACCOUNT_STATUS = "getAccountStatus";
    
	if (requestDTO.getRequestPath().contains(ADD_ACCOUNT_INFO)) {
	    LOG.debug("LOADING  ASSIGN ACCOUNT INFO SERVICE");
	    return new AssignAccountInfoConfigRequestHandler();
	}
	else if(requestDTO.getRequestPath().contains(ADD_TRANSACTION_STATUS)){
	    LOG.debug("LOADING ASSIGN TRANSACTION STATUS SERVICE");
	    return new AssignTransactionStatusConfigRequestHandler();
	}else if(requestDTO.getRequestPath().contains(GET_TRANSACTION_STATUS)){
	    LOG.debug("LOADING RETRIEVE TRANSACTION STATUS SERVICE");
	    return new RetrieveTransactionStatusConfigRequestHandler();
	}else if(requestDTO.getRequestPath().contains(GET_ACCOUNT_STATUS)){
	    LOG.debug("LOADING RETRIEVE TRANSACTION STATUS SERVICE");
	    return new RetrieveAccountStatusConfigRequestHandler();
	}
	else
	return null;
    }

}
