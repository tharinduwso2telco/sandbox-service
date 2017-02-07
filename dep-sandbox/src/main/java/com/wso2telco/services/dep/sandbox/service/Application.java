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

package com.wso2telco.services.dep.sandbox.service;

import java.util.ArrayList;
import java.util.List;

import com.wso2telco.core.mi.AbstractApplication;
import com.wso2telco.core.mi.ConfigDTO;

import io.dropwizard.setup.Environment;

public class Application extends AbstractApplication<SandboxDTO> {

	@Override
	protected List<Object> getRestFulComponents() {
		List<Object>  listOfObject =new ArrayList<Object>();
		listOfObject.add(new UserService());
		listOfObject.add(new SandboxService());
		listOfObject.add(new ProvisionService());
		listOfObject.add(new ProvisionConfigurationService());
		listOfObject.add(new CustomerInfoService());
		listOfObject.add(new CustomerInfoConfigurationService());
		listOfObject.add(new CreditService());
		listOfObject.add(new WalletService());
		listOfObject.add(new WalletConfigurationService());
		listOfObject.add(new PaymentService());
		return listOfObject;
	}


	public static void main(String[] args) {
		try {
			new Application().run(args);
		}   catch (Exception e) {
			System.out.println("Unable to start the server " + e.getMessage());
		}
	}

	@Override
	protected void runInit(SandboxDTO e, Environment env) {
		// TODO Auto-generated method stub
		
	}
	 
}
