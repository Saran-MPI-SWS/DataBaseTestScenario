/////****************CHANGE USER/PASS/DB AND POSTGRES'S CONNECTION PARAMETER INTO YOUR OWN*************//


import java.sql.*;
import java.util.Date;
import java.util.Random;
import java.io.*;



public class RequestRowIDSleep1s implements Runnable{
	DataPack Req1DataObj;
	private Thread th;
	int cycleCounter;
	private String threadName;
	Connection PostConnection = null;
    Statement stmt = null;
    String User,Pass;
    int randTemp;
    String SQLCommand;
    
	
    RequestRowIDSleep1s(String name){
		threadName = name;
    }
	
	@Override
	public void run() {

		int n, t;
		PrintWriter fileCleaner;
	    User = "postgres";
	    Pass= "--UrPass--";
	    n= 1;
	    t= 1;
	    
	    Req1DataObj = new DataPack();
	    
	    while (true){
	    	
	    	randTemp = 0;
	    	
	    	if (MainClass.stopFlag == 0){
				System.out.println("Req1 Thread will be killed now at"+System.currentTimeMillis());
				try {
					th.sleep(1000000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    	
	    	Req1DataObj.UserRequestFinishTime =0;
	    	Req1DataObj.UserRequestStartTime =0;
	    	Req1DataObj.UserRequestResultCount =0;
	    	Req1DataObj.type = 3;
	    	 try {
		    	  	Class.forName("org.postgresql.Driver");
		    	 
			        for (int i=0 ; i < n ; i++){

						Req1DataObj.CycleCount = cycleCounter+1;
				         Req1DataObj.UserRequestStartTime = System.currentTimeMillis();
				         // Open DB and
				         PostConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/--UrDBName--","postgres", "--UrPass--");
				         
				         for(int j = 0 ; j < t ; j++){
				        	 
					           stmt = null;
					           stmt = PostConnection.createStatement();
					           Random randReq = new Random();
					           randTemp = Math.abs(randReq.nextInt(MainClass.cycleNo+1));
					           SQLCommand = "Select * from Sample0 where id <= "+randTemp;

					           ResultSet rs = stmt.executeQuery( SQLCommand );
					           int countResults = 0;
					           while ( rs.next() ) {
					        	   countResults++;
					           }
					          Req1DataObj.UserRequestFinishTime = System.currentTimeMillis();
					          Req1DataObj.UserRequestResultCount = countResults;
					          
					           countResults = 0;
					           rs.close();
					           stmt.close();
					           TextResultJob tr = new TextResultJob(Req1DataObj);
					           if(cycleCounter%100 == 0){
					        	   System.out.println("Cycle count in Req1 thread: "+cycleCounter);
					           }
					           
					           cycleCounter++;
					           			         
				         }

				         PostConnection.close();
		         
				         
			        }
			        th.sleep(1000);
			        
		      } catch (Exception e) {
			         e.printStackTrace();
			         System.err.println(e.getClass().getName()+": "+e.getMessage());
			         System.exit(0);
			      }
		      
	    	
	    }
	      
	     
	}
	public void start ()
	   {
	      if (th == null)
	      {
	         th = new Thread (this, threadName);
	         th.start ();
	         
	         cycleCounter = 0;
	      }
	   }

}


