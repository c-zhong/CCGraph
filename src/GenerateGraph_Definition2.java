
import java.util.LinkedList;
import java.io.File;
import java.io.IOException;

import Util.Util_ReadCSV;
import Util.Util_WriteCSV;
import Filtering.Con_Filtering;
import Filtering.Con_Graph;

public class GenerateGraph_Definition2 {


	   // happen-after, is complementary (B --> A)
	   //  happen-after, is equal(B --> A)
	   // happen-after, is equal,  corresponds (B --> A)
	   //  happen-after, is subsumed by (B --> A)
	  
		public static void main(String[] args) {
			// TODO Auto-generated method stub

			String trace_dir = "//Users//Chen//Dropbox//Work//Trace Analysis//CCGraph 2017";
			String myDirectoryPath = trace_dir + "//nodes";
			int definition = 3;

			 File dir = new File(myDirectoryPath);
			  File[] directoryListing = dir.listFiles();
			  
			  LinkedList<String> graph_summery = new LinkedList<String> ();
			  graph_summery.add("ID, # of V, # of E, isEql, isSub, isCom");
			  
			  if (directoryListing != null) {
			    for (File child : directoryListing) {
			    		
			    		if(!child.getName().contains(".csv"))
			    			continue;
			    	    
			    	    String subjectID = child.getName().replace(".csv", "");
			    	    System.out.println(subjectID + " begin!");
			    	    
			    	    LinkedList<Con_Filtering> filter_list1 = new LinkedList<Con_Filtering>();
					String csvfile1 = myDirectoryPath +"//"+subjectID+".csv";
								
					Util_ReadCSV.readCSV(csvfile1, filter_list1);
						
					Con_Graph g1 = new Con_Graph(subjectID, filter_list1, definition);

					String outputcsvfile1 = trace_dir + "//graph//" +subjectID.replace("node","graph")+".csv";
					
					
					File outputfile = new File(outputcsvfile1);
					
					
					
					
					int count_issub = g1.countEdges(2);
					int count_isEql = g1.countEdges(1)+g1.countEdges(4);
					int count_isCom=g1.countEdges(3);
					
					String graph = subjectID + ", "+g1.graph.vertexSet().size() +", "+ g1.graph.edgeSet().size() + ", "+count_isEql+", "+count_issub+", "+count_isCom;
					graph_summery.add(graph);
					
					if(!outputfile.exists()) {
						try {
							outputfile.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
					
					Util_WriteCSV.writeGraph2CSV_Details(g1, outputcsvfile1);
					
						
					System.out.println(subjectID + " is done!");
			    }
			    
			    Util_WriteCSV.writeTable2CSV(graph_summery, trace_dir+"//graph//graph_summery.csv");
			  } 
			
			
		}
	
}

