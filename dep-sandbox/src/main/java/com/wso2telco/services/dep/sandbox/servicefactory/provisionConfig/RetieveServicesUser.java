package com.wso2telco.services.dep.sandbox.servicefactory.provisionConfig;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.ProvisioningDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ProvisioningServicesRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RetrieveServiceUserRequestDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceDetail;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ServiceInfo;
import com.wso2telco.services.dep.sandbox.dao.model.custom.UserServiceList;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ProvisionAllService;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;

public class RetieveServicesUser extends AbstractRequestHandler<RetrieveServiceUserRequestDTO> {

    private ProvisioningDAO provisioningDao;
    private RetrieveServiceUserRequestDTO requestWrapperDTO;
    private RetrieveServicesUserResponseWrapper responseWrapperDTO;

    {
	LOG = LogFactory.getLog(RetieveServicesUser.class);
	provisioningDao = DaoFactory.getProvisioningDAO();
	dao = DaoFactory.getGenaricDAO();
    }

    @Override
    protected Returnable getResponseDTO() {
	return responseWrapperDTO;
    }

    @Override
    protected List<String> getAddress() {
	List<String> address = new ArrayList<String>();
	return address;
    }

    @Override
    protected void init(RetrieveServiceUserRequestDTO extendedRequestDTO)
	    throws Exception {
	requestWrapperDTO = extendedRequestDTO;
	responseWrapperDTO = new RetrieveServicesUserResponseWrapper();

    }

    @Override
    protected boolean validate(RetrieveServiceUserRequestDTO wrapperDTO)
	    throws Exception {
	return true;
    }

    @Override
    protected Returnable process(
	    RetrieveServiceUserRequestDTO extendedRequestDTO) throws Exception {

	try {
	    
	    List<ProvisionAllService> serviceList = provisioningDao
		    .getProvisionServices(extendedRequestDTO.getUser().getId());
	    UserServiceList service = new UserServiceList();

	    if (serviceList != null && !serviceList.isEmpty()) {

		for (ProvisionAllService allService : serviceList) {
		    ServiceInfo serviceInfo = service.addNewServiceInfo();
		    serviceInfo.setServiceCode(allService.getServiceCode());
		    serviceInfo.setDescription(allService.getDescription());
		    serviceInfo.setServiceCharge(allService.getServiceCharge());
		    serviceInfo.setServiceType(allService.getServiceType());
		}
	    } else {
		LOG.info("No any service exist for this user");
		responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
		responseWrapperDTO.setRequestError(constructRequestError(
			SERVICEEXCEPTION, ServiceError.INVALID_INPUT_VALUE,
			"No any service exist for this user"));
		return responseWrapperDTO;
	    }
	    responseWrapperDTO.setServiceListDTO(service);
	    responseWrapperDTO.setHttpStatus(Response.Status.OK);

	} catch (Exception ex) {
	    LOG.error("###PROVISION### Error in Process : " + ex);
	    responseWrapperDTO.setHttpStatus(Response.Status.BAD_REQUEST);
	    responseWrapperDTO
		    .setRequestError(constructRequestError(SERVICEEXCEPTION,
			    ServiceError.SERVICE_ERROR_OCCURED, null));
	}
	return responseWrapperDTO;

    }
}
