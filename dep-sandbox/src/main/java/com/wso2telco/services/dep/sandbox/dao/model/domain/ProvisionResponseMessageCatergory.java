package com.wso2telco.services.dep.sandbox.dao.model.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "sbxresponsemessagecategory")

public class ProvisionResponseMessageCatergory implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "category")
	private String messageCategory;

	/**
	 * @return the id
	 */

	public int getId() {
		return id;
	}

	/**
	 * @param id  to set
	 *            
	 */

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return the messageCategory
	 */
	public String getMessageCategory() {
		return messageCategory;
	}

	/**
	 * 
	 * @param messageCategory to set
	 *            
	 */
	public void setMessageCategory(String messageCategory) {
		this.messageCategory = messageCategory;
	}
}


