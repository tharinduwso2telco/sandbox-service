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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "sbxprrequesstlog")
public class ProvisionRequestLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "provision_request_log_id")
	private int provisionRequestLogId;

	@Column(name = "requesttype")
	private String requestType;

	@Column(name = "msisdn")
	private String msisdn;

	@Column(name = "clientcorrelator")
	private String clientCorrelator;

	@Column(name = "clientreferencecode")
	private String clientReferenceCode;

	@ManyToOne
	@JoinColumn(name = "userid", referencedColumnName = "id")
	private User user;

	@Column(name = "notifyurl")
	private String notifyURL;

	@Column(name = "callbackdata")
	private String callbackData;
	
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="timestamp")
    private Date requestTimestamp;

	/**
	 * @return the requestTimestamp
	 */
	public Date getRequestTimestamp() {
		return requestTimestamp;
	}

	/**
	 * @param requestTimestamp the requestTimestamp to set
	 */
	public void setRequestTimestamp(Date requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
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
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the provisionRequestLogId
	 */
	public int getProvisionRequestLogId() {
		return provisionRequestLogId;
	}

	/**
	 * @param provisionRequestLogId
	 *            the provisionRequestLogId to set
	 */
	public void setProvisionRequestLogId(int provisionRequestLogId) {
		this.provisionRequestLogId = provisionRequestLogId;
	}

	/**
	 * @return the requestType
	 */
	public String getRequestType() {
		return requestType;
	}

	/**
	 * @param requestType
	 *            the requestType to set
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

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

}
