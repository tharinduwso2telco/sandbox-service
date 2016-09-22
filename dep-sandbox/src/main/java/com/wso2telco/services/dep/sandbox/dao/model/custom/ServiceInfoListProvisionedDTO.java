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
package com.wso2telco.services.dep.sandbox.dao.model.custom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wso2telco.services.dep.sandbox.util.ProvisioningUtil;
/**
 * 
 * @author WSO2Telco
 *
 */

public class ServiceInfoListProvisionedDTO {

	/** The service code **/
	private String serviceCode;

	/** The service description **/
	private String description;

	/** The provisioned date **/
	private String timestamp;

	/** inner service info list for provisioned services **/
	private ArrayList<ServiceMetaInfoListProvisionedDTO> metaServiceInfo;

	/**
	 * 
	 * @return the serviceCode
	 */
	public String getServiceCode() {
		return serviceCode;
	}

	/**
	 * 
	 * @param serviceCode
	 *            to set
	 */
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	/**
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 *            to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return the timestamp
	 */
	public String getTimeStamp() {
		return timestamp;
	}

	/**
	 * 
	 * @param timestamp
	 *            to set
	 */
	public void setTimeStamp(Date timestamp) {
		
		this.timestamp = new SimpleDateFormat(ProvisioningUtil.DEFAULT_DATE_FORMATE).format(timestamp);;
	}

	/**
	 * 
	 * @return the metaServiceInfo
	 */
	public ArrayList<ServiceMetaInfoListProvisionedDTO> getServiceInfo() {
		return metaServiceInfo;
	}

	/**
	 * 
	 * @param metaServiceInfo
	 *            to set
	 */
	public void setServiceInfo(
			ArrayList<ServiceMetaInfoListProvisionedDTO> metaServiceInfo) {
		this.metaServiceInfo = metaServiceInfo;
	}
}
