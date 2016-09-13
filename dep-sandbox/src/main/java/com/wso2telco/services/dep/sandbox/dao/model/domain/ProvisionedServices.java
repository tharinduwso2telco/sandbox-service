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
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "sbtprprovisionedservices")
public class ProvisionedServices implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@ManyToOne
    @JoinColumn(name="msisdnservicesmapid", referencedColumnName="id")
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
    @JoinColumn(name="statusid", referencedColumnName="id")
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
	 * @param id to set
	 */

	public void setId(int id) {
		this.id = id;
	}

	public ProvisionMSISDNServicesMap getMSISDNServicesMapId(){
		return msisdnServiceMap;
	}
	
	public void setMSISDNServicesMapId(ProvisionMSISDNServicesMap msisdnServiceMap){
		this.msisdnServiceMap = msisdnServiceMap;
	}
	
	/**
     * @return the clientCorrelator
     */
    public String getClientCorrelator() {
        return clientCorrelator;
    }

    /**
     * @param clientCorrelator the clientCorrelator to set
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
     * @param clientReferenceCode the clientReferenceCode to set
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
     * @param notifyURL the notifyURL to set
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
     * @param callbackData the callbackData to set
     */
    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }
    
    public Status getStatus(){
    	return status;
    }
    
    public void setStatus(Status status){
    	this.status=status;
    }
    
}
