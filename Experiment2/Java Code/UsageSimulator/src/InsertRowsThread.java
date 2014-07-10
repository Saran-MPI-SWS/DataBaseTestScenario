
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
//**************	    System.out.println("Creating " +  threadName );
	}
	
	@Override
	public void run() {
		
			CopyDataObj = new DataPack();
//			CopyDataObj = new DataPack[1000];
//	        for (int j = 0 ; j <1000; j++){
//	       	 CopyDataObj[j] = new DataPack();
//	        }
			
		
		int n, t;
		PrintWriter fileCleaner;
//	    CopyDataObj.CycleCount = 0;
	    User = "postgres";
	    Pass= "--UrPass--";
	    n= 1;
	    t= 1;
	    
	    
	    
	    while (true){
	    	
//	    	CopyDataObj.AddIndexFinishTime = 0; CopyDataObj.AddIndexStartTime = 0;
//	    	CopyDataObj.AnalyzeFinishTime= 0; CopyDataObj.AnalyzeStartTime =0;
//	    	CopyDataObj.CopyFinishTime = 0; CopyDataObj.CopyStartTime = 0;
//	    	CopyDataObj.DropIndexFinishTime = 0; CopyDataObj.DropIndexStartTime = 0;
//********    System.out.println("Running thread: " +  threadName +" at "+System.currentTimeMillis());
		      try {
		    	  
		    	  	Class.forName("org.postgresql.Driver");
		    	  	if (MainClass.statusFlag == 2){
				        MainClass.statusFlag = 1; // file in use
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
						           
						           //drop index
						           System.out.println("Before Copying buffer to DB, Start Droping Index on Column: ID at "+ System.currentTimeMillis());
						           CopyDataObj.DropIndexStartTime = System.currentTimeMillis();
						           stmt.executeUpdate("DROP INDEX INDEX_ON_ID");
						           System.out.println("Before Copying buffer to DB, Done Droping Index on Column: ID at "+ System.currentTimeMillis());
						           CopyDataObj.DropIndexFinishTime = System.currentTimeMillis();
						           

							       // copy rows from txt file to table
						           
						           SQLCommand = "copy Sample"+j+" (Name, Salary, Column4, Column5, Column6, Column7, Column8, Column9, Column10) FROM '---UrPath---"+txtFileName+".txt' WITH DELIMITER ',';"; 
						           CopyDataObj.CopyStartTime = System.currentTimeMillis();
						           stmt.executeUpdate(SQLCommand);
						           CopyDataObj.CopyFinishTime = System.currentTimeMillis();
						            
						           //create index after copy
						           System.out.println("After Copying buffer to DB, Start Creating Index on Column: ID at "+ System.currentTimeMillis());
						           CopyDataObj.AddIndexStartTime = System.currentTimeMillis();
						           stmt.executeUpdate("CREATE INDEX INDEX_ON_ID ON Sample0 (id)");
						           CopyDataObj.AddIndexFinishTime = System.currentTimeMillis();
						           System.out.println("After Copying buffer to DB, Done Creating Index on Column: ID at "+ System.currentTimeMillis());
						           
						           //analyze
						           System.out.println("After Copying buffer to DB and adding index, start Analyze at "+ System.currentTimeMillis());
						           CopyDataObj.AnalyzeStartTime = System.currentTimeMillis();
						           stmt.executeUpdate("ANALYZE Sample0");
						           CopyDataObj.AnalyzeFinishTime = System.currentTimeMillis();
						           System.out.println("Done Analyzing the table at "+ System.currentTimeMillis());
						           
						           stmt.close();
						           
						           TextResultJob tres = new TextResultJob(CopyDataObj);
						           if(cycleCounter%100 == 0){
						        	   System.out.println("Cycle count in CopyRows thread: "+cycleCounter);
						           }
						           
						           cycleCounter++;	
						           MainClass.cycleNo = cycleCounter;
					         }

					         PostConnection.close();
					         if (i == n-1 ){
				   	        	 CurrentDate = new Date();
					         }
					         
					         
				        }
		    	  		MainClass.statusFlag = 0; // done with copy , can start text creation
				        
				        
		    	    }else{
		    	    	System.out.println("File is used by creating buffer thread, copying thread will spleep for 1s at "+System.currentTimeMillis());
		    	    	th.sleep(1000);
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


