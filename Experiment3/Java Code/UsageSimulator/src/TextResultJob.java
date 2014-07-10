
import java.io.*;
import java.math.BigInteger;
import java.util.Locale;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class TextResultJob {

	

 
    private String inputFile;
    DataPack DataObj;
    PrintWriter writer;
    
    public TextResultJob (DataPack DP){
    	DataObj = DP;
    	switch (DataObj.type) {
		  //1 = CreateTextFileThread , 2 = InsertRowThread, 3= ReuestIDsleep1
		  // 4= RequestSalarySleep1, 5 = RequestSalarySleep2.3
		  
			case 1:
				setOutputFile("TextBuffer.txt");
				break;
			case 2:
				setOutputFile("CopyIntoDB.txt");
				break;
			case 3:
				setOutputFile("RequestIDsleep1.txt");
				break;
			case 4:
				setOutputFile("RequestSalarySleep1.txt");
				break;
			case 5:
				setOutputFile("RequestSalarySleep2300.txt");
				break;
			default:
				break;
			}

    	
    	
    	try {
			write(DataObj);
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    public void setOutputFile(String inputFile) {
    
      this.inputFile = inputFile;
      }
	
	  public void write(DataPack MyData) throws IOException{
		  writer = new PrintWriter(new FileOutputStream(new File(inputFile),true));
		   
			    createContent(MyData);
		    
		  }



		  private void createContent(DataPack DataObject)  {
			  switch (DataObject.type) {
			  //1 = CreateTextFileThread , 2 = InsertRowThread, 3= ReuestIDsleep1
			  // 4= RequestSalarySleep1, 5 = RequestSalarySleep2.3
			  
				case 1:

					writer.println(DataObject.CycleCount+","+DataObject.CreateTextBufferStartTime+","+ DataObject.CreateTextBufferFinishTime);

					break;
				case 2:
					//without dropping index
					writer.println(DataObject.CycleCount+","+DataObject.CopyStartTime+","+DataObject.CopyFinishTime+","
							+DataObject.AnalyzeStartTime+","+DataObject.AnalyzeFinishTime);
//					// with dropping index
//					writer.println(DataObject.CycleCount+","+DataObject.DropIndexStartTime+","+ DataObject.DropIndexFinishTime+","
//					+DataObject.CopyStartTime+","+DataObject.CopyFinishTime+","
//					+DataObject.AddIndexStartTime+","+DataObject.AddIndexFinishTime+","
//					+DataObject.AnalyzeStartTime+","+DataObject.AnalyzeFinishTime);
										
					break;
				case 3:
					writer.println(DataObject.CycleCount+","+DataObject.UserRequestStartTime+","+ DataObject.UserRequestFinishTime+","
							+DataObject.UserRequestResultCount);
					break;
				case 4:
					writer.println(DataObject.CycleCount+","+DataObject.UserRequestStartTime+","+ DataObject.UserRequestFinishTime+","
							+DataObject.UserRequestResultCount);
					break;
				case 5:
					writer.println(DataObject.CycleCount+","+DataObject.UserRequestStartTime+","+ DataObject.UserRequestFinishTime+","
							+DataObject.UserRequestResultCount);
					break;
				default:
					break;
				}
			  writer.flush();
			  writer.close();
			  
			  
		  }


	
}
