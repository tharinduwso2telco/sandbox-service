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

import org.wso2.msf4j.MicroservicesRunner;

import com.wso2telco.dep.tpservice.rest.TokenPoolService;

public class Application {
    public static void main(String[] args) {
    	
    	try {
    		/**
    		 * initialize configuration reading
    		 */
			ConfigReader.getInstance().init();
			/**
			 * initialize token pool service
			 */
			TokenPoolManager.getInstance().init();
			
			
			new MicroservicesRunner()
			        .deploy(new TokenPoolService())
			        .start();
			
			
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
