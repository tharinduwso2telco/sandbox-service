package com.wso2telco.services.dep.sandbox.dao.model.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sbxresponsemessage")
public class ProvisionResponseMessage implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "categoryid", referencedColumnName = "id")
	private ProvisionResponseMessageCatergory responseMessageCatergory;

	@Column(name = "code")
	private String responseCode;

	@Column(name = "message")
	private String responseMessage;

	@ManyToOne
	@JoinColumn(name = "apitypeid", referencedColumnName = "id")
	private APITypes apiType;

	/**
	 * @return the id
	 */

	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */

	public void setId(int id) {
		this.id = id;
	}

	public ProvisionResponseMessageCatergory getResponseMessageCatergory() {
		return responseMessageCatergory;
	}

	public void setResponseMessageCatergory(
			ProvisionResponseMessageCatergory responseMessageCatergory) {
		this.responseMessageCatergory = responseMessageCatergory;
	}

	/**
	 * 
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * 
	 * @param responseCode
	 *            to set
	 */
	public void setResponseCode(String code) {
		this.responseCode = code;
	}

	/**
	 * 
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return responseMessage;
	}

	/**
	 * 
	 * @param responseMessage
	 *            to set
	 */
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public APITypes getApiType() {
		return apiType;
	}

	public void setApiType(APITypes apiTypes) {
		this.apiType = apiTypes;
	}

}
