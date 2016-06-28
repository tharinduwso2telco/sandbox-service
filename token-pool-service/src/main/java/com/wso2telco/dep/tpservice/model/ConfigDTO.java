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
	private int maxNumberOfAttemptForToken;
	
	
	
	public int getMaxNumberOfAttemptForToken() {
		return maxNumberOfAttemptForToken;
	}

	public void setMaxNumberOfAttemptForToken(int maxNumberOfAttemptForToken) {
		this.maxNumberOfAttemptForToken = maxNumberOfAttemptForToken;
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
