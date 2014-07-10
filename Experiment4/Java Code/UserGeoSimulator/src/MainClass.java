import java.io.*;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class MainClass {
	public static volatile int statusFlag;
	public static volatile int stopFlag;
	public static volatile long startTime;
	public static volatile int cycleNo;
	public static volatile long startPoint;
	public static volatile long EndPoint;
	public static volatile int RequestTextFileFlag;
	public static volatile int SecondaryRequestTextFileFlag;
	
	// startPoint and EndPoint show when copy procedure is started and its over, we create next inputs in this time 

	public static void main(String args[]) throws FileNotFoundException {
		statusFlag = 0;
		RequestTextFileFlag = 0; // 0 == free , 1 == in use
		SecondaryRequestTextFileFlag = 0;// 0 == free , 1 == in use
		cycleNo = 0;
		startTime = System.currentTimeMillis();
		stopFlag = 2; // 2 is default , will be minus 1 by buffer and copy thread , stop when 0
		EndPoint = System.currentTimeMillis();
		startPoint = EndPoint-4000;

		//statusFlag = 0 = can create file ,can not copy
		//statusFlag = 1 = file is busy
		//statusFlag = 2 = can copy, can not text file
		
		
		try {
			PrintWriter pw1 = new PrintWriter("TextBuffer.txt", "UTF-8");//file including duration for creating buffer text file
			PrintWriter pw2 = new PrintWriter("CopyIntoDB.txt", "UTF-8");//file including operation duration for loading into DB
			PrintWriter pw3 = new PrintWriter("RequestTimeLocation.txt", "UTF-8");//file including duration for queries
			PrintWriter pw4 = new PrintWriter("RequestTimeLocation2.txt", "UTF-8");//file including duration for queries
			pw1.println();pw2.println();pw3.println(); pw4.println();
			pw1.close();pw2.close();pw3.close();pw4.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		CreateTextFileThread thread1 = new CreateTextFileThread("BufferTextFile10TR10C");
		thread1.start();
		
		InsertRowsThread thread2 = new InsertRowsThread("CopyBufferTextFile","BufferTextFile10TR10C");
		thread2.start();
		// create 20 userAgents to request info for past 10 seconds in each of 20 regions
		for (int k = 0 ; k <20 ; k++){
			RequestRowTimeLocation UserThread = new RequestRowTimeLocation("UserAgent"+k,k);
			UserThread.start();
			}
		
	}
		
	
}
