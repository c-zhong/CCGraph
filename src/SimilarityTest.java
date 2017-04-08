import java.io.File;
import java.io.IOException;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.LinkedList;

import CentroidSim.Centroid;
import CentroidSim.MDVector;
import CentroidSim.Subgraph;
import CentroidSim.Subgraph.SubgraphType;
import Filtering.Con_Filtering;
import Filtering.Con_Graph;
import Util.Util_ReadCSV;
import Util.Util_WriteCSV;


public class SimilarityTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub


		String trace_dir = "//Users//Chen//Dropbox//Work//Trace Analysis//CCGraph 2017";
		String myDirectoryPath = trace_dir + "//nodes";
		
		String trace_file1 = "T1.csv";
		String trace_file2 = "T2.csv";
		
		double overlap_value = 0.5; //this value is from 0 to 1
		
		int definition = 3;

		File dir = new File(myDirectoryPath);
		File[] directoryListing = dir.listFiles();
		Con_Graph g1 = null;
		Con_Graph g2 = null;
	 
		  if (directoryListing != null) {
		    for (File child : directoryListing) {
		    		
		    		if(!child.getName().contains(".csv"))
		    			continue;
		    	    
		    		if(child.getName().contains(trace_file1)){
		    			String subjectID = child.getName().replace(".csv", "");
			    	    System.out.println(subjectID + " begin!");
			    	    
			    	    LinkedList<Con_Filtering> filter_list1 = new LinkedList<Con_Filtering>();
					String csvfile1 = myDirectoryPath +"//"+subjectID+".csv";
								
					Util_ReadCSV.readCSV(csvfile1, filter_list1);
						
					g1 = new Con_Graph(subjectID, filter_list1, definition);

					//String outputcsvfile1 = trace_dir + "//graph//" +subjectID.replace("node","graph")+".csv";
					//File outputfile = new File(outputcsvfile1);
					
		    		}
		    		else if(child.getName().contains(trace_file2)){
		    			String subjectID2 = child.getName().replace(".csv", "");
			    	    System.out.println(subjectID2 + " begin!");
			    	    
			    	    LinkedList<Con_Filtering> filter_list2 = new LinkedList<Con_Filtering>();
					String csvfile2 = myDirectoryPath +"//"+subjectID2+".csv";
								
					Util_ReadCSV.readCSV(csvfile2, filter_list2);
					g2 = new Con_Graph(subjectID2, filter_list2, definition);
		
					//String outputcsvfile1 = trace_dir + "//graph//" +subjectID.replace("node","graph")+".csv";
					//File outputfile = new File(outputcsvfile1);
		    		}
		    }
		  }

		  if(g1 != null && g2 != null){
			  Subgraph g1_isSub = new Subgraph(g1, SubgraphType.ISSUM);
			  Subgraph g1_isCom = new Subgraph(g1, SubgraphType.ISCOM);
			  Subgraph g1_isEql = new Subgraph(g1, SubgraphType.ISEQL);
			  Subgraph g1_Sub = new Subgraph(g1, SubgraphType.SUM);
			  Subgraph g2_isSub = new Subgraph(g2, SubgraphType.ISSUM);
			  Subgraph g2_isCom = new Subgraph(g2, SubgraphType.ISCOM);
			  Subgraph g2_isEql = new Subgraph(g2, SubgraphType.ISEQL);
			  Subgraph g2_Sub = new Subgraph(g2, SubgraphType.SUM);
			  
			  MDVector md1_isSub = Centroid.getIsSubCentroid(g1_isSub, 0.5);
			  MDVector md1_isCom = Centroid.getIsComCentroid(g1_isCom, 0.5);
			  MDVector md1_isEql = Centroid.getIsEqlCentroid(g1_isEql);
			  MDVector md1_Sub = Centroid.getSubCentroid(g1_Sub, 0.5);
			  
			  MDVector md2_isSub = Centroid.getIsSubCentroid(g2_isSub, 0.5);
			  MDVector md2_isCom = Centroid.getIsComCentroid(g2_isCom, 0.5);
			  MDVector md2_isEql = Centroid.getIsEqlCentroid(g2_isEql);
			  MDVector md2_Sub = Centroid.getSubCentroid(g2_Sub, 0.5);
			  
			  ArrayList<MDVector> mdlist1 = new ArrayList<MDVector>();
			  ArrayList<MDVector> mdlist2 = new ArrayList<MDVector>();
			  
			  mdlist1.add(md1_isSub);
			  mdlist1.add(md1_isCom);
			  mdlist1.add(md1_isEql);
			  mdlist1.add(md1_Sub);
			  mdlist2.add(md2_isSub);
			  mdlist2.add(md2_isCom);
			  mdlist2.add(md2_isEql);
			  mdlist2.add(md2_Sub);
			  
			  double[] weights = {1, 1, 1, 1};
			  
			  ArrayList<Double> sim = Centroid.centroidSim_vector(mdlist1, mdlist2, overlap_value);
			  
			  
			  System.out.println("md1 isSub: --------------- ");
			  md1_isSub.printOut();
			  
			  System.out.println("md2 isSub: --------------- ");
			  md2_isSub.printOut();
			  
			  System.out.println("md1 isCom: --------------- ");
			  md1_isCom.printOut();
			  
			  System.out.println("md2 isCom: --------------- ");
			  md2_isCom.printOut();
			  
			  System.out.println("md1 isEql: --------------- ");
			  md1_isEql.printOut();
			  
			  System.out.println("md2 isEql: --------------- ");
			  md2_isEql.printOut();
			  
			  System.out.println("md1 Sub: --------------- ");
			  md1_Sub.printOut();
			  
			  System.out.println("md2 Sub: --------------- ");
			  md2_Sub.printOut();
			  
			  System.out.print("Similarity is " );
			  for(Double d : sim){
				  System.out.print(d + ", ");
			  }
			  
			  System.out.println("Total is "+ Centroid.centroidSim_weighted(sim,weights));
		  }
		    		
		    						
		    	    
		
		
	
	}

}
