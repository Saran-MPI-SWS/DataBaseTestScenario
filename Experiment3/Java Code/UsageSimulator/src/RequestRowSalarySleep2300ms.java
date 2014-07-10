/////****************CHANGE USER/PASS/DB AND POSTGRES'S CONNECTION PARAMETER INTO YOUR OWN*************//

import java.sql.*;
import java.util.Date;
import java.util.Random;
import java.io.*;



public class RequestRowSalarySleep2300ms implements Runnable{
	DataPack Req3DataObj;
	private Thread th;
	int cycleCounter;
	private String threadName;
	Connection PostConnection = null;
    Statement stmt = null;
    String User,Pass;
    double randTemp;
    String SQLCommand;
    
	
    RequestRowSalarySleep2300ms(String name){
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
	    
	    
	    while (true){
	    	Req3DataObj = new DataPack();
	    	if (MainClass.stopFlag == 0){
				System.out.println("Req2 Thread will be killed now at"+System.currentTimeMillis());
				try {
					th.sleep(1000000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    	Req3DataObj.type = 5;
	    	Req3DataObj.UserRequestFinishTime =0;
	    	Req3DataObj.UserRequestStartTime =0;
	    	Req3DataObj.UserRequestResultCount =0;
	    	
	    	randTemp = 0;
	    	try {
		    	  	Class.forName("org.postgresql.Driver");
		    	  	
			        for (int i=0 ; i < n ; i++){


			        	Req3DataObj.CycleCount = cycleCounter+1;
			        	Req3DataObj.UserRequestStartTime = System.currentTimeMillis();
				         // Open DB and
				         PostConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/--UrDBName--","postgres", "--UrPass--");
				         
				         for(int j = 0 ; j < t ; j++){
				        	 
					           stmt = null;
					           stmt = PostConnection.createStatement();
					           Random randReq = new Random();
					           randTemp = randReq.nextGaussian();
					           SQLCommand = "Select * from Sample0 where salary > "+(int) (Math.abs(randTemp)*100+100);

					           ResultSet rs = stmt.executeQuery( SQLCommand );
					           int countResults = 0;
					           while ( rs.next() ) {
					        	   countResults++;
					           }
					           Req3DataObj.UserRequestFinishTime = System.currentTimeMillis();
						       Req3DataObj.UserRequestResultCount = countResults;
					           countResults = 0;
					           rs.close();
					           stmt.close();
					           TextResultJob tx = new TextResultJob(Req3DataObj);
					           if(cycleCounter%100 == 0){
					        	   System.out.println("Cycle count in Req3 thread: "+cycleCounter);
					           }
					           cycleCounter++;	         
				         }

				         PostConnection.close();

				         
				         
			        }
			        th.sleep(2300);
			        
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


