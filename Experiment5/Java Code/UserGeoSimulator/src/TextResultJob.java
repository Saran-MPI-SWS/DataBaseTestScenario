
import java.io.*;
import java.math.BigInteger;
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
		  
		  
			case 1:
				setOutputFile("TextBuffer.txt");
				break;
			case 2:
				setOutputFile("CopyIntoDB.txt");
				break;
			case 3:
				setOutputFile("RequestTimeLocation.txt");
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



		  private void createContent(DataPack DataObject) throws FileNotFoundException  {
			  switch (DataObject.type) {
			  //1 = CreateTextFileThread , 2 = InsertRowThread, 3= ReuestTimeLocation
			  
				case 1:

					writer.println(DataObject.CycleCount+","+DataObject.CreateTextBufferStartTime+","+ DataObject.CreateTextBufferFinishTime);
				
					
					break;
				case 2:
					//without dropping index
					writer.println(DataObject.CycleCount+","+DataObject.CopyStartTime+","+DataObject.CopyFinishTime+","
							+DataObject.AnalyzeStartTime+","+DataObject.AnalyzeFinishTime);
					
					break;
				case 3:
					if(MainClass.RequestTextFileFlag ==0){
						// change the flag to busy
						MainClass.RequestTextFileFlag= 1;
						writer.println(DataObject.CycleCount+","+DataObject.LocationBlockNo+","+DataObject.UserRequestStartTime+","+ DataObject.UserRequestFinishTime+","
								+DataObject.UserRequestResultCount);
						// change the flag to free
						MainClass.RequestTextFileFlag =0;
					}else{
						PrintWriter writer2 = new PrintWriter(new FileOutputStream(new File("RequestTimeLocation2.txt"),true));
						if (MainClass.SecondaryRequestTextFileFlag ==0){
							
							MainClass.SecondaryRequestTextFileFlag= 1;
							writer2.println(DataObject.CycleCount+","+DataObject.LocationBlockNo+","+DataObject.UserRequestStartTime+","+ DataObject.UserRequestFinishTime+","
									+DataObject.UserRequestResultCount);
							// change the flag to free
							MainClass.SecondaryRequestTextFileFlag =0;
						}
					}
					break;

				default:
					break;
				}
			  writer.flush();
			  writer.close();
			  
			  
		  }


	
}
