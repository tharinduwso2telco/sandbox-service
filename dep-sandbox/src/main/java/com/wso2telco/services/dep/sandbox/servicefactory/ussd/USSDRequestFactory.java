package com.wso2telco.services.dep.sandbox.servicefactory.ussd;

import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class USSDRequestFactory {
    private static Log LOG = LogFactory.getLog(USSDRequestFactory.class);

    public static RequestHandleable getInstance(final RequestDTO requestDTO) {

        final String INITIATE_USSD_SESSION = "USSD";

        if (requestDTO.getRequestPath().contains(INITIATE_USSD_SESSION.toLowerCase())) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Loading Initiate USSD Session");
                return new InitiateUSSDSessionRequestHandler();
            }
        }
        return null;
    }
}


