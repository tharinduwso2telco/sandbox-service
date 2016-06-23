package com.wso2telco.dep.tpservice.conf;

import java.io.Serializable;

import com.wso2telco.dep.tpservice.model.ConfigDTO;

public class ConfigReader implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8945346314273239957L;
	private ConfigDTO configDTO;
	private static ConfigReader reader;


	private ConfigReader() {

	}

	public void init() throws Exception {
		// TODO: need to initialize the YML configuration
	}

	public static ConfigReader getInstance() {
		if (reader == null) {
			reader = new ConfigReader();
		}
		return reader;
	}
	
	public ConfigDTO getConfigDTO(){
		return configDTO;
	}
}
