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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wso2telco.dep.tpservice.model.ConfigDTO;
import com.wso2telco.dep.tpservice.pool.PoolFactory;
import com.wso2telco.dep.tpservice.rest.TokenPoolService;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

import io.dropwizard.Application;
import io.dropwizard.jetty.ConnectorFactory;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.jetty.HttpsConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.server.ServerFactory;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerDropwizard;

public class Appinitializer extends Application<ConfigDTO> {

//	static Logger log = LoggerFactory.getLogger(Appinitializer.class);

	private final SwaggerDropwizard swaggerDropwizard = new SwaggerDropwizard();

	@Override
	public void initialize(Bootstrap<ConfigDTO> bootstrap) {
		swaggerDropwizard.onInitialize(bootstrap);
	}

	@Override
	public void run(ConfigDTO arg0, Environment env) throws Exception {
		System.out.println("Server start with configuration :" + arg0);
		/**
		 * initialize configuration reading
		 */
		ConfigReader.init(arg0, env);
		/**
		 * initialize token pool service
		 */
		PoolFactory.getInstance().getManagager().initializePool();

		env.jersey().register(new TokenPoolService());

		HttpConnectorFactory connector = getHttpConnectionFactory(arg0);

		swaggerDropwizard.onRun(arg0, env, connector.getBindHost(), connector.getPort());

	}

	public static void main(String[] args) {
		try {
			new Appinitializer().run(args);
		} catch (TokenException e) {
			System.out.println(	"Unable to start the server " + e.getErrorType().getCode() + " TokenManager.java:" + e.getErrorType().getMessage());
		} catch (Exception e) {
			System.out.println("Unable to start the server " + e.getMessage());
		}
	}

	private HttpConnectorFactory getHttpConnectionFactory(ConfigDTO configuration) {
		List<ConnectorFactory> connectorFactories = getConnectorFactories(configuration);
		for (ConnectorFactory connectorFactory : connectorFactories) {
			if (connectorFactory instanceof HttpsConnectorFactory) {
				return (HttpConnectorFactory) connectorFactory; // if we find 
																// https skip
																// the others
			}
		}
		for (ConnectorFactory connectorFactory : connectorFactories) {
			if (connectorFactory instanceof HttpConnectorFactory) {
				return (HttpConnectorFactory) connectorFactory; // if not https
																// pick http
			}
		}

		throw new IllegalStateException("Unable to find an HttpServerFactory");
	}

	private List<ConnectorFactory> getConnectorFactories(ConfigDTO configuration) {
		ServerFactory serverFactory = configuration.getServerFactory();
		if (serverFactory instanceof SimpleServerFactory) {
			return Collections.singletonList(((SimpleServerFactory) serverFactory).getConnector());
		} else if (serverFactory instanceof DefaultServerFactory) {
			return new ArrayList<>(((DefaultServerFactory) serverFactory).getApplicationConnectors());
		} else {
			throw new IllegalStateException("Unknown ServerFactory implementation: " + serverFactory.getClass());
		}
	}
}
