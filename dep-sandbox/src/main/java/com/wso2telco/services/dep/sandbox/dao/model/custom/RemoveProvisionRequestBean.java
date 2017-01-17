package com.wso2telco.services.dep.sandbox.dao.model.custom;

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

public class RemoveProvisionRequestBean {

	private ServiceRemoveRequest serviceRemoveRequest;

	/**
	 * @return the serviceRemoveRequest
	 */
	public ServiceRemoveRequest getServiceRemoveRequest() {
		return serviceRemoveRequest;
	}

	/**
	 * 
	 * @param serviceRemoveRequest
	 *            to set
	 */
	public void setServiceRemoveRequest(ServiceRemoveRequest serviceRemoveRequest) {
		this.serviceRemoveRequest = serviceRemoveRequest;
	}

	public static class ServiceRemoveRequest {

		private String serviceCode;

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
		 * @param serviceCode
		 *            to set
		 */
		public void setServiceCode(String serviceCode) {
			this.serviceCode = serviceCode;
		}

		/**
		 * @return the clientCorrelator
		 */
		public String getClientCorrelator() {
			return clientCorrelator;
		}

		/**
		 * @param clientCorrelator
		 *            to set
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
		 * @param clientReferenceCode
		 *            to set
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
		 * @param callbackReference
		 *            to set
		 */
		public void setCallbackReference(CallbackReference callbackReference) {
			this.callbackReference = callbackReference;
		}
		
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Service Code : " + getServiceCode());
			builder.append(" Client Correlator : " + getClientCorrelator());
			builder.append(" Client Reference Code : " + getClientReferenceCode());

			if (getCallbackReference() != null) {
				builder.append(" " + getCallbackReference().toString());
			}

			return builder.toString();
		}
		
	}

}
