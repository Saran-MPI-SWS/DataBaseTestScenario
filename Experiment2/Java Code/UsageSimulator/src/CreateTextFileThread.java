import java.io.*;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;



public class CreateTextFileThread implements Runnable{
	DataPack bufferDataObj;
	int cycleCounter;
	private Thread t;
	int randomNo1, randomNo2;
	double randtemp;
	private String fileName;
	Random randNo;
	CreateTextFileThread(String textFileName){
		fileName = textFileName;
		randNo = new Random();
	}
	
	public void run(){
		
		bufferDataObj = new DataPack(); // this object is only used to save durations
		PrintWriter writer;
		while (true){
			randomNo1 = 0;
			randomNo2 = 0;
			randtemp = 0;
			
			try {
				
				if (MainClass.statusFlag == 0){
					MainClass.statusFlag = 1;
					if (MainClass.stopFlag == 0){
			        	System.out.println("TextBuffer Thread will be killed now at"+System.currentTimeMillis());
						t.sleep(1000000);
					
			        }
					if (cycleCounter == 500){
						MainClass.stopFlag--;
					}
					
					bufferDataObj.type = 1;
					bufferDataObj.CycleCount = cycleCounter+1;
					bufferDataObj.CreateTextBufferStartTime = System.currentTimeMillis();
					
					
					writer = new PrintWriter(fileName+".txt", "UTF-8");
					
					for (int i=1; i<= 10000 ; i++){
						randtemp = randNo.nextGaussian();
						randomNo1 = (int) (Math.abs(randtemp)*100+100);
						randomNo2 = (int) (Math.abs(randtemp)*10+10);
						//create partly-random row as user input data
						writer.println("UserName,"+randomNo1+",col4Text,"+randomNo2+",column6Text,column7Text,column8Text,column9Text,column10Text");
						
					}
					writer.flush();
					writer.close();
					bufferDataObj.CreateTextBufferFinishTime = System.currentTimeMillis();
					
					TextResultJob txtr = new TextResultJob(bufferDataObj);
					if(cycleCounter%100 == 0){
			        	   System.out.println("Cycle count in CreateText thread: "+cycleCounter);
			           }
					cycleCounter++;
					
					MainClass.statusFlag = 2;// done creating buffer, can copy now
					
				}else{
					System.out.println("File is used by copying thread, creating text buffer thread will spleep for 1s at "+System.currentTimeMillis());
		    	    	t.sleep(1000);
				}
				
				

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		}
			
	}
	public void start ()
	   {
		if (t == null)
	      {
	         t = new Thread (this, fileName);
	         t.start ();
	         
	         cycleCounter = 0;
	      }
	   }
	
}
