/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging;

import com.wso2telco.dep.oneapivalidation.exceptions.RequestError;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ErrorResponseDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class QuerySMSDeliveryStatusResponseWrapper extends AbstractReturnWrapperDTO {

	private QuerySMSDeliveryStatusResponseBean querySMSDeliveryStatusResponseBean = null;

	public QuerySMSDeliveryStatusResponseBean getQuerySMSDeliveryStatusResponseBean() {
		return querySMSDeliveryStatusResponseBean;
	}

	public void setQuerySMSDeliveryStatusResponseBean(
			QuerySMSDeliveryStatusResponseBean querySMSDeliveryStatusResponseBean) {
		this.querySMSDeliveryStatusResponseBean = querySMSDeliveryStatusResponseBean;
	}

	@Override
	public Object getResponse() {


		if(getRequestError() == null)
		{
			return 	getQuerySMSDeliveryStatusResponseBean();
		}
		else
		{
			ErrorResponseDTO response = new ErrorResponseDTO(getRequestError());
			return response;
		}
	}

	class QuerySMSDeliveryStatusResponse {

		private RequestError requestError;
		private QuerySMSDeliveryStatusResponseBean querySMSDeliveryStatusResponseBean;

		public QuerySMSDeliveryStatusResponse(RequestError requestError,
				QuerySMSDeliveryStatusResponseBean querySMSDeliveryStatusResponseBean) {
			this.requestError = requestError;
			this.querySMSDeliveryStatusResponseBean = querySMSDeliveryStatusResponseBean;
		}

		public RequestError getRequestError() {
			return requestError;
		}

		public void setRequestError(RequestError requestError) {
			this.requestError = requestError;
		}

		public QuerySMSDeliveryStatusResponseBean getQuerySMSDeliveryStatusResponseBean() {
			return querySMSDeliveryStatusResponseBean;
		}

		public void setQuerySMSDeliveryStatusResponseBean(
				QuerySMSDeliveryStatusResponseBean querySMSDeliveryStatusResponseBean) {
			this.querySMSDeliveryStatusResponseBean = querySMSDeliveryStatusResponseBean;
		}
	}
}
