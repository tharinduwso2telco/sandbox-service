package com.wso2telco.services.dep.sandbox.servicefactory.provisionConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.AddServicesMsisdnRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisioningServicesRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.provisioning.ProvisioningServiceFactory;

public class ProvisioningConfigServiceFactory {
    final static String SERVICE = "service";
    private static Log LOG = LogFactory
	    .getLog(ProvisioningConfigServiceFactory.class);

    public static RequestHandleable getInstance(final RequestDTO requestDTO) {
	if (requestDTO.getRequestPath().toLowerCase().contains(SERVICE)
		&& requestDTO.isGet()) {
	    LOG.debug("###PROVISIONCONFIG### LOADING SERVICES FOR USER");
	    return new RetieveServicesUser();
	} else if (requestDTO.getRequestPath().toLowerCase().contains(SERVICE)
		&& requestDTO.isPost()) {

	    if (requestDTO instanceof AddServicesMsisdnRequestWrapperDTO) {
		LOG.debug("###PROVISIONCONFIG### LOADING ADD NEW SERVICE FOR NUMBER");
		return new AssignServiceForMsisdn();

	    } else if (requestDTO instanceof ProvisioningServicesRequestWrapperDTO) {
		LOG.debug("###PROVISIONCONFIG### LOADING ADD NEW SERVICE FOR USER");
		return new NewProvisioningService();
	    }
	}
	return null;
    }
}