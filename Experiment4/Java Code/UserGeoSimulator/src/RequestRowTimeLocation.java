/////****************CHANGE USER/PASS/DB AND POSTGRES'S CONNECTION PARAMETER INTO YOUR OWN*************//

import java.sql.*;
import java.util.Date;
import java.util.Random;
import java.io.*;



public class RequestRowTimeLocation implements Runnable{
	DataPack Req1DataObj;
	private Thread th;
	int cycleCounter;
	private String threadName;
	Connection PostConnection = null;
    Statement stmt = null;
    String User,Pass;
    int randTemp;
    int LocationBlock;
    String SQLCommand;
    
	
    RequestRowTimeLocation(String name, int BlockNo){
    	// BlockNo shows which location block should this info-requester agent ask for
		threadName = name;
		LocationBlock = BlockNo;
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
	    	Req1DataObj.LocationBlockNo = LocationBlock;
	    	
		      try {
		    	  	Class.forName("org.postgresql.Driver");
		    	 
			        for (int i=0 ; i < n ; i++){

						Req1DataObj.CycleCount = cycleCounter+1;
				         Req1DataObj.UserRequestStartTime = System.currentTimeMillis();
				         // Open DB and
				         PostConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/--UrDBNAme--","postgres", "--UrPass--");
				         
				         for(int j = 0 ; j < t ; j++){
				        	 
					           stmt = null;
					           stmt = PostConnection.createStatement();
					           
					           // random location and time request
					           Random randReq = new Random();
					           randTemp = Math.abs(randReq.nextInt(MainClass.cycleNo+1));
					           
					           Random randNoxy = new Random();
					           double randtempxy = randNoxy.nextGaussian();
								int randx1xy = (int) (Math.abs(randtempxy)*1000);
								randtempxy = randNoxy.nextGaussian();
								int randy1xy = (int) (Math.abs(randtempxy)*800);
								randtempxy = randNoxy.nextGaussian();
								int randx2xy = (int) (Math.abs(randtempxy)*1000);
								randtempxy = randNoxy.nextGaussian();
								int randy2xy = (int) (Math.abs(randtempxy)*800);
								if(randx1xy>randx2xy){int jaX = randx1xy; randx1xy=randx2xy; randx2xy=jaX;}
								if(randy1xy>randy2xy){int jaY = randy1xy; randy1xy=randy2xy; randy2xy=jaY;}

								//long randTime = randNoxy.nextLong(MainClass.startTime);
								long randomTimeValue = MainClass.startTime + (long)(randNoxy.nextDouble()*(System.currentTimeMillis() - MainClass.startTime));
								//java.sql.Timestamp myrandTime =  new java.sql.Timestamp(randomTimeValue);
								
								
					           
								java.sql.Timestamp TenSecAgo =  new java.sql.Timestamp(System.currentTimeMillis()-10000);
								java.sql.Timestamp myCurrTime =  new java.sql.Timestamp(System.currentTimeMillis());
								
								//random location - in past 10 seconds
								PreparedStatement myPreparedStatement = PostConnection.prepareStatement("Select * FROM Sample0 WHERE Sample0.CurrentLocation && ST_MakeEnvelope("+randx1xy+"," +randy1xy+","+randx2xy+"," +randy2xy+", 4326) AND Sample0.CurrentTime> ? AND Sample0.CurrentTime< ?");
								/*
								int upLeftx,upLefty, downRightx, downRighty;
								upLeftx= 0; upLefty= 0; downRightx= 0; downRighty= 0;
								//set locations for each of 20 blocks
								switch (LocationBlock) {
								case 1:
									upLeftx= 0; upLefty= 0; downRightx= 200; downRighty= 200;
									break;
								case 2:
									upLeftx= 200; upLefty= 0; downRightx= 400; downRighty= 200 ;
									break;
								case 3:
									upLeftx= 400; upLefty= 0; downRightx= 600; downRighty= 200;
									break;
								case 4:
									upLeftx= 600; upLefty= 0; downRightx= 800; downRighty= 200;
									break;
								case 5:
									upLeftx= 800; upLefty= 0; downRightx= 1000; downRighty= 200;
									break;
								case 6:
									upLeftx= 0; upLefty= 200; downRightx= 200; downRighty= 400;
									break;
								case 7:
									upLeftx= 200; upLefty= 200; downRightx= 400; downRighty= 400;
									break;
								case 8:
									upLeftx= 400; upLefty= 200; downRightx= 600; downRighty= 400;
									break;
								case 9:
									upLeftx= 600; upLefty= 200; downRightx= 800; downRighty=400;
									break;
								case 10:
									upLeftx= 800; upLefty= 200; downRightx= 1000; downRighty= 400;
									break;
								case 11:
									upLeftx= 0; upLefty= 400; downRightx= 200; downRighty= 600;
									break;
								case 12:
									upLeftx= 200; upLefty= 400; downRightx= 400; downRighty= 600;
									break;
								case 13:
									upLeftx= 400; upLefty= 400; downRightx= 600; downRighty= 600;
									break;
								case 14:
									upLeftx= 600; upLefty= 400; downRightx= 800; downRighty= 600;
									break;
								case 15:
									upLeftx= 800; upLefty= 400; downRightx= 1000; downRighty= 600;
									break;
								case 16:
									upLeftx= 0; upLefty= 600; downRightx= 200; downRighty= 800;
									break;
								case 17:
									upLeftx= 200; upLefty= 600; downRightx= 400; downRighty= 800;
									break;
								case 18:
									upLeftx= 400; upLefty= 600; downRightx= 600; downRighty= 800;
									break;
								case 19:
									upLeftx= 600; upLefty= 600; downRightx= 800; downRighty= 800;
									break;
								case 20:
									upLeftx= 800; upLefty= 600; downRightx= 1000; downRighty= 800;
									break;
								default:
									break;
								}
								
								
								// request for userdata which are within fixed location - in past 10 seconds
								PreparedStatement myPreparedStatement = PostConnection.prepareStatement("Select * FROM Sample0 WHERE Sample0.CurrentLocation && ST_MakeEnvelope("+upLeftx+"," +upLefty+","+downRightx+"," +downRighty+", 4326) AND Sample0.CurrentTime> ? AND Sample0.CurrentTime< ?");
								
								myPreparedStatement.setTimestamp(1, TenSecAgo);
								myPreparedStatement.setTimestamp(2,myCurrTime );
								*/
					           SQLCommand = "Select * FROM Sample0 WHERE Sample0.CurrentLocation && ST_MakeEnvelope("+","+"," +","+", 4326)";
					           
					           ResultSet rs = myPreparedStatement.executeQuery();
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


