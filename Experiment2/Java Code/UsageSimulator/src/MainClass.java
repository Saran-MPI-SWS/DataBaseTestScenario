import java.io.*;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class MainClass {
	public static volatile int statusFlag;
	public static volatile int stopFlag;

	public static volatile int cycleNo;

	public static void main(String args[]) throws FileNotFoundException {
		statusFlag = 0; // used for turn taking of creating buffer and loading data to DB
		cycleNo = 0;
		stopFlag = 2; // 2 is default , will be minus 1 by buffer and copy thread when they are done (500 cycles done), stop when 0
		//statusFlag = 0 = can create file ,can not copy
		//statusFlag = 1 = file is busy
		//statusFlag = 2 = can copy, can not text file
		try {
			
			PrintWriter pw1 = new PrintWriter("TextBuffer.txt", "UTF-8"); //file including duration for creating buffer text file 
			PrintWriter pw2 = new PrintWriter("CopyIntoDB.txt", "UTF-8"); //file including operation duration for loading into DB
			PrintWriter pw3 = new PrintWriter("RequestIDsleep1.txt", "UTF-8"); //file including duration for queries 
			PrintWriter pw4 = new PrintWriter("RequestSalarySleep1.txt", "UTF-8");//file including duration for queries
			PrintWriter pw5 = new PrintWriter("RequestSalarySleep2300.txt", "UTF-8");//file including duration for queries
			pw1.println();pw2.println();pw3.println();pw4.println();pw5.println();
			pw1.close();pw2.close();pw3.close();pw4.close();pw5.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		CreateTextFileThread thread1 = new CreateTextFileThread("BufferTextFile10TR10C");
		thread1.start();
		
		InsertRowsThread thread2 = new InsertRowsThread("CopyBufferTextFile","BufferTextFile10TR10C");
		thread2.start();
		
		RequestRowIDSleep1s UserThread1 = new RequestRowIDSleep1s("UserAgent1");
		UserThread1.start();
		
		RequestRowSalarySleep1s UserThread2 = new RequestRowSalarySleep1s("UserAgent2");
		UserThread2.start();
		
		RequestRowSalarySleep2300ms UserThread3 = new RequestRowSalarySleep2300ms("UserAgent3");
		UserThread3.start();
	}
	
}
