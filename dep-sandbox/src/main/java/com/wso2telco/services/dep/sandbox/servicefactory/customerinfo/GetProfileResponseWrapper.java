/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.services.dep.sandbox.servicefactory.customerinfo;

import com.wso2telco.services.dep.sandbox.dao.model.custom.Customer;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ErrorResponseDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class GetProfileResponseWrapper extends AbstractReturnWrapperDTO {

    CustomerDTOWrapper customerDTOWrapper;

    @Override
    public Object getResponse() {
		
		if(getRequestError()==null)		
			return customerDTOWrapper;
		else{
			ErrorResponseDTO response= new ErrorResponseDTO(getRequestError());
			return response;
		}
	}

    /**
     * @return the customerDTOWrapper
     */
    public CustomerDTOWrapper getCustomerDTOWrapper() {
	return customerDTOWrapper;
    }

    /**
     * @param customerDTOWrapper
     *            the customerDTOWrapper to set
     */
    public void setCustomerDTOWrapper(CustomerDTOWrapper customerDTOWrapper) {
	this.customerDTOWrapper = customerDTOWrapper;
    }

    public static class CustomerDTOWrapper {
	private Customer customer;

	/**
	 * @return the customer
	 */
	
	public Customer getCustomer() {
	    return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
	    this.customer = customer;
	}

    }

}
