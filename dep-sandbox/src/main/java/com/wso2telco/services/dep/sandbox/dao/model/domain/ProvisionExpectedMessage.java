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
@Table(name = "sbtprspexpectmessage")

public class ProvisionExpectedMessage implements Serializable {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
    @JoinColumn(name="numberid", referencedColumnName="id")
    private ManageNumber msisdnId;
	
	@ManyToOne
    @JoinColumn(name="messageid", referencedColumnName="id")
    private ProvisionResponseMessage messageId;
	
	@ManyToOne
    @JoinColumn(name="servicesid", referencedColumnName="id")
    private ProvisionAllService servicesId;
	
	@Column(name = "requesttype")
	private String requestType;	
	
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
	
	public ManageNumber getMsisdn(){
		return msisdnId;
	}
	 public void setMsisdn(ManageNumber msisdnId ){
		 this.msisdnId=msisdnId;
	 }
	 
	public ProvisionResponseMessage getProvisionResponseMessage() {
		return messageId;
	}

	public void setProvisionResponseMessagen(ProvisionResponseMessage messageId) {
		this.messageId = messageId;
	}
		 
	public ProvisionAllService getServiceId() {
		return servicesId;
	}

	public void setServiceId(ProvisionAllService servicesId) {
		this.servicesId = servicesId;
	}
	public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
    

}
