package com.wso2telco.dep.tpservice.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestOwnerTest implements Runnable {
	private String url;
	private String owner;
	private int numberOfParrelRequest=0;
	private ExecutorService executorService  ;	
	public RequestOwnerTest(String url,String owner,int numberOfParrelRequest) {
		 this.url =url;
		 this.owner =owner;
			this.numberOfParrelRequest =numberOfParrelRequest;	
			executorService= Executors.newFixedThreadPool(numberOfParrelRequest);
	}
	@Override
	public void run() {
		for(int x=0; x<numberOfParrelRequest;x++){
			TokenPool temp = new TokenPool(owner, url);
			//System.out.println(owner);
			executorService.execute(
					temp
					);
		}  
	}
}
