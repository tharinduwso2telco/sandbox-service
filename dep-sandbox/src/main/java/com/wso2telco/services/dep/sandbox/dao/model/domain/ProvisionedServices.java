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

package com.wso2telco.services.dep.sandbox.dao.model.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * 
 * @author WSO2Telco
 *
 */
@Entity
@Table(name = "sbtprprovisionedservices")
public class ProvisionedServices implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne
	@JoinColumn(name = "msisdnservicesmapid", referencedColumnName = "id")
	private ProvisionMSISDNServicesMap msisdnServiceMap;

	@Column(name = "clientcorrelator")
	private String clientCorrelator;

	@Column(name = "clientreferencecode")
	private String clientReferenceCode;

	@Column(name = "notifyurl")
	private String notifyURL;

	@Column(name = "callbackdata")
	private String callbackData;

	@ManyToOne
	@JoinColumn(name = "statusid", referencedColumnName = "id")
	private Status status;

	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name = "createddate")
	private Date createdDate;

	/**
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            to set
	 */

	public void setId(int id) {
		this.id = id;
	}

	public ProvisionMSISDNServicesMap getMSISDNServicesMapId() {
		return msisdnServiceMap;
	}

	public void setMSISDNServicesMapId(
			ProvisionMSISDNServicesMap msisdnServiceMap) {
		this.msisdnServiceMap = msisdnServiceMap;
	}

	/**
	 * @return the clientCorrelator
	 */
	public String getClientCorrelator() {
		return clientCorrelator;
	}

	/**
	 * @param clientCorrelator
	 *            the clientCorrelator to set
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
	 *            the clientReferenceCode to set
	 */
	public void setClientReferenceCode(String clientReferenceCode) {
		this.clientReferenceCode = clientReferenceCode;
	}

	/**
	 * @return the notifyURL
	 */
	public String getNotifyURL() {
		return notifyURL;
	}

	/**
	 * @param notifyURL
	 *            the notifyURL to set
	 */
	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}

	/**
	 * @return the callbackData
	 */
	public String getCallbackData() {
		return callbackData;
	}

	/**
	 * @param callbackData
	 *            the callbackData to set
	 */
	public void setCallbackData(String callbackData) {
		this.callbackData = callbackData;
	}

	/**
	 * 
	 * @return status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 *            to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * 
	 * @return createdDate
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
