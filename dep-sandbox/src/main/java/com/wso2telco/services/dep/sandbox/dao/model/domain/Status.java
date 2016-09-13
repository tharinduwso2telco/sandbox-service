package com.wso2telco.services.dep.sandbox.dao.model.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "sbxstatus")
public class Status implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "status")
	private String status;

	/**
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id to set
	 */

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return the status
	 * 
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}


