/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.dep.tpservice.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.model.ConfigDTO;
import com.wso2telco.dep.tpservice.rest.TokenPoolService;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class Appinitializer extends Application<ConfigDTO> {

	private static final Logger log = LoggerFactory.getLogger(Appinitializer.class);

	@Override
	public void run(ConfigDTO arg0, Environment env) throws Exception {
		/**
		 * initialize configuration reading
		 */
		ConfigReader.getInstance().init();
		/**
		 * initialize token pool service
		 */
		TokenPoolManager.getInstance().init();

		env.jersey().register(new TokenPoolService());

	}

	public static void main(String[] args) {
		try {
			new Appinitializer().run(args);
		} catch (Exception e) {
			log.error("", e);
		}
	}

}
