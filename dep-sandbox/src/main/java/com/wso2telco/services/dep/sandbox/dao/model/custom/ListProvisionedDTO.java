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

import java.util.Date;
/**
 * 
 * @author WSO2Telco
 *
 */
public class ListProvisionedDTO {
	
	/** The service code **/
	private String serviceCode;
	
	/** The service description **/
	private String description;
	
	/** Optional service param tag **/
	private String tag;
	
	/** Optional service param value **/
	private String value;
	
	/** The provisioned date **/
	private Date createdDate;

	/**
	 * @return the serviceCode
	 */
	public String getServiceCode() {
		return serviceCode;
	}

	/**
	 * @param serviceCode
	 *            to set
	 * 
	 */
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            to set
	 * 
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * 
	 * @param tag
	 *            to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;

	}

	/**
	 * 
	 * @param value
	 *            to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 
	 * @return the createdDate
	 */

	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * 
	 * @param createdDate
	 *            to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
