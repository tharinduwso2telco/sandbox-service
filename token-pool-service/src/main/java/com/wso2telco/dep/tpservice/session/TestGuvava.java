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

package com.wso2telco.dep.tpservice.session;

import java.util.Timer;
import java.util.TimerTask;

public class TestGuvava extends TimerTask{
public static int count=1;
	public static void main(String[] args) {/*
		Cache<String, String> employeeCache = CacheBuilder.newBuilder()
				.removalListener(new RemovalListener<String, String>() {

					@Override
					public void onRemoval(RemovalNotification<String, String> arg0) {
						System.out.println("removal: " + arg0.getKey() + "/" + arg0.getValue());

					}
				}).expireAfterWrite(10, TimeUnit.SECONDS).build();
		
		employeeCache.put("ID1", "ID 1 removed");
		employeeCache.put("ID2", "ID 2 removed");
		employeeCache.put("ID3", "ID 3 removed");
		employeeCache.put("ID4", "ID 4 removed");
		
		 
		 
	*/
		TestGuvava task =new TestGuvava();
		
		Timer timer = new Timer();
		timer.schedule(task, 10000l);
		while(task.count<=222){
			System.out.println(" i am out  i am out  i am out  i am out  i am out  i am out  i am out  i am out  i am out  i am out  i am out  i am out ");
		}
		 
		
	}

	@Override
	public void run() {
		while(true){
		System.out.println("Time expired ---------------------" +count);
		count++;
		}
		
	}

}
