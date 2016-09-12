package com.wso2telco.services.dep.sandbox.dao.model.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "sbxapitypes")
public class APITypes implements Serializable {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@Column(name="apiname")
    private String apiname;
	
	 /**
     * @return the id
     */
	
	public int getId() {
		return id;
	}
	 /**
     * @param id the id to set
     */
	
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * 
	 * @return the apiname
	 */
	public String getAPIName() {
		return apiname;
	}

	/**
	 * 
	 * @param apiname the api name to set
	 */
	public void setAPIName(String apiname) {
		this.apiname = apiname;
	}
}
