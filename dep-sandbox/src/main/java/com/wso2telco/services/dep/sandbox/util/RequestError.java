package com.wso2telco.services.dep.sandbox.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wso2telco.dep.oneapivalidation.exceptions.PolicyException;
import com.wso2telco.dep.oneapivalidation.exceptions.ServiceException;
@JsonInclude(value=Include.NON_NULL)
public class RequestError {
	
	    /** The service exception. */
	    private ServiceException serviceException;
	    
	    /** The policy exception. */
	    private PolicyException policyException;
	    
	    /**
	     * Gets the service exception.
	     *
	     * @return the service exception
	     */
	    public ServiceException getServiceException() {
	        return serviceException;
	    }

	    /**
	     * Sets the service exception.
	     *
	     * @param serviceException the new service exception
	     */
	    public void setServiceException(ServiceException serviceException) {
	        this.serviceException = serviceException;
	    }
	    

	    /**
	     * Gets the policy exception.
	     *
	     * @return the policy exception
	     */
	    public PolicyException getPolicyException() {
	        return policyException;
	    }

	    /**
	     * Sets the policy exception.
	     *
	     * @param policyException the new policy exception
	     */
	    public void setPolicyException(PolicyException policyException) {
	        this.policyException = policyException;
	    }
	}

