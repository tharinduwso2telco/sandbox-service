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
package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class ProvisionRequestBean {

	private ServiceProvisionRequest serviceProvisionRequest;

	/**
	 * @return the serviceProvisionRequest
	 */
	public ServiceProvisionRequest getServiceProvisionRequest() {
		return serviceProvisionRequest;
	}

	/**
	 * @param serviceProvisionRequest
	 *            the serviceProvisionRequest to set
	 */
	public void setServiceProvisionRequest(ServiceProvisionRequest serviceProvisionRequest) {
		this.serviceProvisionRequest = serviceProvisionRequest;
	}

	public static class ServiceProvisionRequest {
		
		private String serviceCode;

		private String serviceName;
		
		private String clientCorrelator;
		
		private String clientReferenceCode;
		
		private String onBehalfOf;

		private String purchaseCategoryCode;
		
		private CallbackReference callbackReference;

		/**
		 * @return the serviceCode
		 */
		public String getServiceCode() {
			return serviceCode;
		}

		/**
		 * @param serviceCode the serviceCode to set
		 */
		public void setServiceCode(String serviceCode) {
			this.serviceCode = serviceCode;
		}

		/**
		 * @return the serviceName
		 */
		public String getServiceName() {
			return serviceName;
		}

		/**
		 * @param serviceName
		 *            the serviceName to set
		 */
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}

		/**
		 * @return the clientCorrelator
		 */
		public String getClientCorrelator() {
			return clientCorrelator;
		}

		/**
		 * @param clientCorrelator the clientCorrelator to set
		 */
		public void setClientCorrelator(String clientCorrelator) {
			this.clientCorrelator = clientCorrelator;
		}

		/**
		 * @return the clientReferenceCode
		 */
		public String getClientReferenceCode() {
			return clientReferenceCode;
		}

		/**
		 * @param clientReferenceCode the clientReferenceCode to set
		 */
		public void setClientReferenceCode(String clientReferenceCode) {
			this.clientReferenceCode = clientReferenceCode;
		}
		
		public String getOnBehalfOf() {
			return onBehalfOf;
		}

		public void setOnBehalfOf(String onBehalfOf) {
			this.onBehalfOf = onBehalfOf;
		}

		public String getPurchaseCategoryCode() {
			return purchaseCategoryCode;
		}

		public void setPurchaseCategoryCode(String purchaseCategoryCode) {
			this.purchaseCategoryCode = purchaseCategoryCode;
		}


		/**
		 * @return the callbackReference
		 */
		public CallbackReference getCallbackReference() {
			return callbackReference;
		}

		/**
		 * @param callbackReference the callbackReference to set
		 */
		public void setCallbackReference(CallbackReference callbackReference) {
			this.callbackReference = callbackReference;
		}

		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Service Code : " + getServiceCode());
			builder.append(" Service Name : " + serviceName);
			builder.append(" Client Correlator : " + getClientCorrelator());
			builder.append(" Client Reference Code : " + getClientReferenceCode());

			if (getCallbackReference() != null) {
				builder.append(" " + getCallbackReference().toString());
			}

			return builder.toString();
		}
	}
}
