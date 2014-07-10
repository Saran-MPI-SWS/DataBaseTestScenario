
public class DataPack {

	int type, CycleCount;
	long  CreateTextBufferStartTime, CreateTextBufferFinishTime;
	long CopyStartTime, CopyFinishTime;
	long AnalyzeStartTime, AnalyzeFinishTime;
	long UserRequestStartTime, UserRequestFinishTime;
	int UserRequestResultCount, LocationBlockNo;
	
	
	public DataPack () {
		// type: 0 = default , 1 = CreateTextFileThread , 2 = InsertRowThread, 3= ReuestIDsleep1
		// 4= RequestSalarySleep1, 5 = RequestSalarySleep2.3
			
		 type =0; CycleCount= 0; LocationBlockNo =0;
		 CreateTextBufferStartTime= -1; CreateTextBufferFinishTime= -1;
		 CopyStartTime= -1; CopyFinishTime= -1;
		 AnalyzeStartTime= -1; AnalyzeFinishTime= -1;
		 UserRequestStartTime = -1; UserRequestFinishTime= -1;
		 UserRequestResultCount = -1;
		
	} 
}

