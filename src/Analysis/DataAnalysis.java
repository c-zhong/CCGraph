
package Analysis;


import java.util.LinkedList;
import java.io.File;
import java.io.IOException;

import Filtering.Con_Filtering;
import Filtering.Con_Graph;
import Util.Util_ReadCSV;
import Util.Util_WriteCSV;


public class DataAnalysis {
	
	
	public static void main(String[] args) {
			String trace_dir = "//Users//Chen//SparkleShare//VAST//dataset";
			String myDirectoryPath = trace_dir;
		
			 File dir = new File(myDirectoryPath);
			 File[] directoryListing = dir.listFiles();
			
			 int[] counts = new int[5];
			 
		
			 counts[0] = 0;  //filling one element at a time
			 counts[1] = 0;
			 counts[2] = 0;
			 counts[3] = 0;
			 counts[4] = 0;
		
		
			  if (directoryListing != null) {
			    for (File child : directoryListing) {
			    		
			    	    
			    	    String subjectID = child.getName().replace(".csv", "");
			    	    System.out.println(subjectID + " begin!");
			    	    
			    	    LinkedList<Con_Filtering> filter_list1 = new LinkedList<Con_Filtering>();
					String csvfile1 = myDirectoryPath +"//"+subjectID+".csv";
								
					Util_ReadCSV.countEvents(csvfile1, counts);
					
					System.out.println(counts[0]+ "; "+counts[1]+ "; "+counts[2]+ "; "+counts[3]+ "; "+counts[4]+ ";");
						
					System.out.println(subjectID + " is done!");
			    }
			    }
			  
			  System.out.println(counts[0]+ "; "+counts[1]+ "; "+counts[2]+ "; "+counts[3]+ "; "+counts[4]+ ";");
			    
			  }

}

