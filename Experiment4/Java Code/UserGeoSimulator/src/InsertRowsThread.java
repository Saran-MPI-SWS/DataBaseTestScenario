/////****************CHANGE USER/PASS/DB/FilePath AND POSTGRES'S CONNECTION PARAMETER INTO YOUR OWN*************//

import java.sql.*;
import java.util.Date;
import java.io.*;



public class InsertRowsThread implements Runnable{
	DataPack CopyDataObj;
	int cycleCounter;
	private Thread th;
	private String threadName;
	Connection PostConnection = null;
    Statement stmt = null;
    String User,Pass;
    String SQLCommand;
    String txtFileName;
    Date CurrentDate;
    
	
	InsertRowsThread(String name, String textFileName){
		threadName = name;
		txtFileName = textFileName;
	}
	
	@Override
	public void run() {
		
			CopyDataObj = new DataPack();
		
		int n, t;
		PrintWriter fileCleaner;
	    User = "postgres";
	    Pass= "--UrPass--";
	    n= 1;
	    t= 1;
	    
	    
	    
	    while (true){
		      try {
		    	  
		    	  	Class.forName("org.postgresql.Driver");
		    	  	if (MainClass.statusFlag == 2){
				        MainClass.statusFlag = 1; // file in use by copy
				        //give start point to main class , to use it for creating buffer sample info
				        MainClass.startPoint = System.currentTimeMillis();
				        
				        // if finish condition is true , kill the thread
				        if (MainClass.stopFlag == 0){
				        	System.out.println("CopyRows Thread will be killed now at"+System.currentTimeMillis());
							th.sleep(1000000);
						
				        }
				        if(cycleCounter==500){
				        	MainClass.stopFlag--;
				        	
				        }
				        
				        CopyDataObj.type = 2;
				        CopyDataObj.CycleCount = cycleCounter+1;
						
		    	  		for (int i=0 ; i < n ; i++){
					     

					         
					         // Open DB and
					         PostConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/--UrDBName--","postgres", "--UrPass--");
					         
					         for(int j = 0 ; j < t ; j++){
					        	 
					        	   stmt = PostConnection.createStatement();
						           stmt = null;
						           stmt = PostConnection.createStatement();
						           
						           

							       // copy rows from Maintxt file to table
						           
						           SQLCommand = "copy Sample"+j+" (Name, Salary, Column4, Column5, Column6, Column7, Column8, CurrentTime) FROM '--UrPath--"+txtFileName+".txt' WITH DELIMITER ',';"; 
						           CopyDataObj.CopyStartTime = System.currentTimeMillis();
						           stmt.executeUpdate(SQLCommand);
						           
						           //empty tempxy table 
						           stmt.executeUpdate("TRUNCATE TempXY");
						           
						           // copy buffer txtfileXY into tempxy table
						           SQLCommand = "copy TempXY (Longitude, Latitude) FROM '--UrPath--"+txtFileName+"XY.txt' WITH DELIMITER ',';"; 
						           stmt.executeUpdate(SQLCommand);
						           
						           //in TempXY table , create point out of x,y 
						           SQLCommand = "UPDATE TempXY SET LocationPoint = ST_SetSRID(ST_MakePoint(Longitude,Latitude),4326)";
						           stmt.executeUpdate(SQLCommand);
						           
						           
						           //copy all tempXY info last 10000 rows in Table Sample0 (the main one)
						           int lastRowInMainTable=0;
						           ResultSet myresult = stmt.executeQuery("SELECT id from Sample"+j+" order by id desc limit 1");
						           if (myresult.next()){lastRowInMainTable = myresult.getInt(1);}
						           System.out.println("The last Row in Sample Table is: "+lastRowInMainTable);
						           int startFromRowNumber = lastRowInMainTable-9999;
						           
						           SQLCommand = "UPDATE Sample"+j+" SET CurrentLocation = TempXY.LocationPoint FROM TempXY WHERE Sample"+j+".id = TempXY.id";//"-1+"+startFromRowNumber;
						           stmt.executeUpdate(SQLCommand);
						           
						           CopyDataObj.CopyFinishTime = System.currentTimeMillis();
   		       
						           //analyze
						           CopyDataObj.AnalyzeStartTime = System.currentTimeMillis();
						           stmt.executeUpdate("ANALYZE Sample0");
						           CopyDataObj.AnalyzeFinishTime = System.currentTimeMillis();

						           stmt.close();
						           
						           TextResultJob tres = new TextResultJob(CopyDataObj);
						           //if(cycleCounter%100 == 0){
						           //	System.out.println("Cycle count in CopyRows thread: "+cycleCounter);
						           //}
						           
						           cycleCounter++;	
						           MainClass.cycleNo = cycleCounter;
					         }

					         PostConnection.close();
					         if (i == n-1 ){
				   	        	 CurrentDate = new Date();
					         }
					         
					         
				        }
		    	  		
		    	  		//give end point to main class , to use it for creating buffer sample info
				        MainClass.EndPoint = System.currentTimeMillis();
				        // done with copy , can start text creation
		    	  		MainClass.statusFlag = 0; 
				        //th.sleep(2000);
				        
		    	    }else{
		    	    	//File is used by creating buffer thread, copying thread will spleep for 1s 
		    	    	th.sleep(500);
		    	    	}

			        
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


