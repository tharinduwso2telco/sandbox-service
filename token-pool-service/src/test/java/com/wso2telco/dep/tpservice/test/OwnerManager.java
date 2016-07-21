package com.wso2telco.dep.tpservice.test;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;



public class OwnerManager {
	//private String owner;
	private static int numberOfOwners=1;
	private static ExecutorService executorService  ;
	private static int numberOfParrelRequest=10;
	private static String url = "http://localhost:8181/tokenservice/";
	public static int total=0;
	private static Map<String,String> ownerMap =new HashMap<String,String>();
	public static AtomicInteger totalcount =new AtomicInteger();
	public OwnerManager()
	{
		executorService= Executors.newFixedThreadPool(numberOfOwners);
	}
	public static void start(){
		for (Entry<String, String> entry : ownerMap.entrySet()) {
			
		}
	}
	public static void main(String args[])
	{
		//OwnerManager manager = new OwnerManager();
		try
	    {
	      // create our mysql database connection
		  String myUrl = "jdbc:mysql://localhost:3306/token_service";
	      Connection conn = DriverManager.getConnection(myUrl, "root", "root");
	      String query = "SELECT ownerid FROM token_service.tsxwho WHERE isvalid=1";
	      Statement st = conn.createStatement();
	      ResultSet rs = st.executeQuery(query);
	      OwnerManager manager = new OwnerManager();
	      while (rs.next())
	      {  
	    	  RequestOwnerTest owner = new RequestOwnerTest(url,rs.getString("ownerid"), numberOfParrelRequest);
				executorService.execute(owner);
	    	  
	       }

	      st.close();
	    }
	    catch (Exception e)
		{
	    	e.printStackTrace();
	    }finally{
	    	while(true){
	    		System.out.println("Final value is:"+totalcount.get());
	    		if(totalcount.get()>=((numberOfParrelRequest*numberOfOwners)-5)){
	    			return;
	    		}
	    	
	    	}
	    }
	}
}



