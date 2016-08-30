package com.wso2telco.dep.tpservice.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.print.attribute.standard.SheetCollate;


public class ScheduledTask extends TimerTask {
	private static int numberOfOwners;
	private static ExecutorService executorService  ;
	private static int numberOfParallelRequest;
	private static String url ;
	public static int total=0;
	public static String userName;
	public static String passWord;
	public static String myUrl;
	public static AtomicInteger totalcount =new AtomicInteger();
	
	public ScheduledTask()
	{
		readProperty();
		  
	}
	
	@Override
    public void run() {
		System.out.println(numberOfOwners);
		try
	    {
			executorService= Executors.newFixedThreadPool(numberOfOwners);
	      // create our mysql database connection
	      Connection conn = DriverManager.getConnection(myUrl, "root", "root");
	      String query = "SELECT ownerid FROM token_service.tsxwho WHERE isvalid=1";
	      Statement st = conn.createStatement();
	      ResultSet rs = st.executeQuery(query);
	      while (rs.next())
	      {  
	    	  RequestOwnerTest owner = new RequestOwnerTest(url,rs.getString("ownerid"), numberOfParallelRequest);
				executorService.execute(owner);
	    	  
	       }
	      System.out.println("Timer task started at:"+new Date());
	        completeTask();
	        System.out.println("Timer task finished at:"+new Date());

	        st.close();
	    }
	    catch (Exception e)
		{
	    	e.printStackTrace();
	    }finally{
	    	while(true){
	    		System.out.println("Final value is:"+totalcount.get());
	    		if(totalcount.get()>=((numberOfParallelRequest*numberOfOwners)-5)){
	    			return;
	    		}
	    	}
	    }
    }

    private void completeTask() {
        try {
            //assuming it takes 10 secs to complete the task
            Thread.sleep(1000*60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
   public void readProperty(){
   Properties prop = new Properties();
   InputStream input = null;

   try {

      input = getClass().getClassLoader().getResourceAsStream("Pro.properties");
       prop.load(input);

       // get the property value and print it out
      String url=prop.getProperty("URL");
      String myUrl=prop.getProperty("myURL");
      int numberOfOwners=Integer.parseInt(prop.getProperty("NumberOfOwners"));
      int numberOfParallelRequest=Integer.parseInt(prop.getProperty("NumberOfParallelRequest"));
      String userName=prop.getProperty("Username");
      String passWord=prop.getProperty("Password");
   
      ScheduledTask.url=url;
	   ScheduledTask.numberOfOwners=numberOfOwners;
	   ScheduledTask.numberOfParallelRequest=numberOfParallelRequest;
	   ScheduledTask.userName=userName;
	   ScheduledTask.passWord=passWord;
	   ScheduledTask.myUrl=myUrl;
      //System.out.println(numberOfOwners);
   } catch (IOException ex) {
       ex.printStackTrace();
   } finally {
       if (input != null) {
           try {
               input.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }
   }
}