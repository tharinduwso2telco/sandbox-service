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
@Table(name = "sbtprmsisdnservicessmap")
public class ProvisionMSISDNServicesMap implements Serializable {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@ManyToOne
    @JoinColumn(name="numbersid", referencedColumnName="id")
    private ManageNumber msisdnId;
	 
	@ManyToOne
    @JoinColumn(name="servicesid", referencedColumnName="id")
    private ProvisionAllService servicesId;

	
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
	
	public ManageNumber getMsisdnId() {
        return msisdnId;
    }

    /**
     * @param msisdnId to set
     */
    public void setMsisdnId(ManageNumber msisdnId) {
        this.msisdnId = msisdnId;
    }
    public ProvisionAllService getServiceId() {
        return servicesId;
    }

    /**
     * @param servicesId to set
     */
    public void setServiceId(ProvisionAllService servicesId) {
        this.servicesId = servicesId;
    }
    
    
	
}
