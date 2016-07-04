package com.wso2telco.dep.tpservice.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

public class ConfigDTO extends Configuration implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8356032431902326005L;
	

	@JsonProperty
	private DataSourceFactory database = new DataSourceFactory();
	
	@JsonProperty
	private String host;
	
	@JsonProperty
	private int port;
	
	@JsonProperty
	private long waitingTimeForToken;
	

	@JsonProperty
	private int retrAttempts;
	
	@JsonProperty
	private Boolean isMaster;
	
	@JsonProperty
	private int tokenReadretrAttempts = 3; //default 3 times 
	
	@JsonProperty
	private long tokenReadretrAfter=60000; //one minit
	
	
	
	
	public int getRetrAttempts() {
		return retrAttempts;
	}

	public void setRetrAttempts(int retrAttempts) {
		this.retrAttempts = retrAttempts;
	}

	public int getTokenReadretrAttempts() {
		return tokenReadretrAttempts;
	}

	public void setTokenReadretrAttempts(int tokenReadretrAttempts) {
		if (tokenReadretrAttempts > 3) {
			this.tokenReadretrAttempts = tokenReadretrAttempts;
		}
	}

	public long getTokenReadretrAfter() {
		return tokenReadretrAfter;
	}

	//token read retry mini limit is 1 min
	public void setTokenReadretrAfter(long tokenReadretrAfter) {
		if (tokenReadretrAfter > 60000) {
			this.tokenReadretrAfter = tokenReadretrAfter;
		}
	}

	public Boolean getIsMaster() {
		return isMaster;
	}

	public Boolean isMaster() {
		return isMaster;
	}

	public void setIsMaster(Boolean isMaster) {
		this.isMaster = isMaster;
	}

	public int getRetryAttempt() {
		return retrAttempts;
	}

	public void setRetryAttempt(int maxNumberOfAttemptForToken) {
		this.retrAttempts = maxNumberOfAttemptForToken;
	}

	public long getWaitingTimeForToken() {
		return waitingTimeForToken;
	}

	public void setWaitingTimeForToken(long waitingTimeForToken) {
		this.waitingTimeForToken = waitingTimeForToken;
	}

	public DataSourceFactory getDataSourceFactory() {
		return database;
	}
	
	public String getHost(){
		return host;
	}
	
	public int getPort(){
		return port;
	}

}
