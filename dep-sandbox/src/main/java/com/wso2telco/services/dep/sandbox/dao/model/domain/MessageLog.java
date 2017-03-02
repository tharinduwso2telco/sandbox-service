package com.wso2telco.services.dep.sandbox.dao.model.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;


@Entity
@Table(name = "sbtmessagelog")
public class MessageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "request")
    private String request;

    @Column(name = "status")
    private int status;

    @Column(name = "type")
    private int type;

    @Column(name = "servicenameid")
    private int servicenameid;

    @Column(name = "userid")
    private int userid;

    @Column(name = "reference")
    private String reference;

    @Column(name = "value")
    private String value;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "messagetimestamp")
    private Date messageTimestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getServicenameid() {
        return servicenameid;
    }

    public void setServicenameid(int servicenameid) {
        this.servicenameid = servicenameid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getMessageTimestamp() {
        return messageTimestamp;
    }

    public void setMessageTimestamp(Date messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}