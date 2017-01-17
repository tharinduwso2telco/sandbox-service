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

import com.wso2telco.services.dep.sandbox.util.CommonUtil;

public class ServiceProvisionRequestWrapper extends RequestDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3609438606240260256L;

	private String msisdn;

	private String mcc;

	private String mnc;

	private ProvisionRequestBean provisionRequestBean;

	/**
	 * @return the msisdn
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * @param msisdn
	 *            the msisdn to set
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	/**
	 * @return the provisionRequestBean
	 */
	public ProvisionRequestBean getProvisionRequestBean() {
		return provisionRequestBean;
	}

	/**
	 * @param provisionRequestBean
	 *            the provisionRequestBean to set
	 */
	public void setProvisionRequestBean(
			ProvisionRequestBean provisionRequestBean) {
		this.provisionRequestBean = provisionRequestBean;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMnc() {
		return mnc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
	}
}
