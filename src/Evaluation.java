import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.w3c.dom.css.Counter;

import CentroidSim.Centroid;
import CentroidSim.MDVector;
import CentroidSim.Subgraph;
import CentroidSim.Subgraph.SubgraphType;
import Filtering.Con_Filtering;
import Filtering.Con_Graph;
import Util.Util_ReadCSV;


public class Evaluation {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		double isSub_threshold = 0.5;
		double isCom_threshold = 0.5;
		double Sub_threshold = 0.5;
		double overlap_value = 1;
		
		double[] weights = {1.5, 0.5, 1, 1};

		
		
		HashMap<String, Double> weight_map = new HashMap<String, Double>();
		weight_map.put("TIME", 1.0);
		weight_map.put("PRIORITY", 1.0);
		weight_map.put("OPERATION", 1.0);
		weight_map.put("PROTOCOL", 1.0);
		weight_map.put("SRCIP", 0.5);
		weight_map.put("DSTIP", 0.5);
		weight_map.put("SRCPORT", 1.5);
		weight_map.put("DSTPORT", 1.5);
		weight_map.put("SERVICE", 1.0);
		weight_map.put("DIRECTION", 1.0);
		weight_map.put("DESCRIPTION", 1.0);
		MDVector.setWeightMap(weight_map); 
		
		int count = 0;
		
		int numMatchGT = 0;
		int numNonMatchGT = 0;
		int numMatch = 0;
		int numNonMatch = 0;
		
		String trace_dir = "//Users//Chen//Dropbox//Work//Trace Analysis//CCGraph 2017";
		String tracePath = trace_dir + "//samples2//";
		String pair_file = trace_dir + "//pairs//match_labels2.csv";
		
		
		BufferedReader br =  null;
		String line = "";
		String cvsSplitBy = ",";
		int filtering_index =0;

		try{
			boolean firstline = true;

			br = new BufferedReader(new FileReader(pair_file));
			while ((line = br.readLine()) != null) {

				if(firstline){
					firstline = false;
					continue;
				}
				
				count ++;
				if(count > 1){
					break;
				}
				// use comma as separator
				String[] itemarray = line.trim().split(cvsSplitBy);

				if(itemarray.length < 4){
					continue;
				}
				String trace_file1 = itemarray[1];
				String trace_file2 = itemarray[2];
				String matched = itemarray[3];
				
				
				if(trace_file1 == null
						|| trace_file2 == null
						|| matched == null
						|| trace_file1.trim().equals("")
						|| trace_file2.trim().equals("")
						|| matched.trim().equals("")){
					continue;
				}
				
				
				int definition = 3;

				
				Con_Graph g1 = null;
				Con_Graph g2 = null;
				

	
		    		String subjectID = trace_file1.replace(".csv", "");
			    	System.out.println(subjectID + " begin!");
			    	    
			    	LinkedList<Con_Filtering> filter_list1 = new LinkedList<Con_Filtering>();
								
			    	Util_ReadCSV.readCSV(tracePath+trace_file1, filter_list1);
						
				g1 = new Con_Graph(subjectID, filter_list1, definition);

				
		    		String subjectID2 = trace_file2.replace(".csv", "");
			    	System.out.println(subjectID2 + " begin!");
			    	    
			    	LinkedList<Con_Filtering> filter_list2 = new LinkedList<Con_Filtering>();
								
			    	Util_ReadCSV.readCSV(tracePath+trace_file2, filter_list2);
						
				g2 = new Con_Graph(subjectID2, filter_list2, definition);
		
	
		  
		  if(g1 != null && g2 != null){
			  Subgraph g1_isSub = new Subgraph(g1, SubgraphType.ISSUM);
			  Subgraph g1_isCom = new Subgraph(g1, SubgraphType.ISCOM);
			  Subgraph g1_isEql = new Subgraph(g1, SubgraphType.ISEQL);
			  Subgraph g1_Sub = new Subgraph(g1, SubgraphType.SUM);
			  Subgraph g2_isSub = new Subgraph(g2, SubgraphType.ISSUM);
			  Subgraph g2_isCom = new Subgraph(g2, SubgraphType.ISCOM);
			  Subgraph g2_isEql = new Subgraph(g2, SubgraphType.ISEQL);
			  Subgraph g2_Sub = new Subgraph(g2, SubgraphType.SUM);
			  
			  MDVector md1_isSub = Centroid.getIsSubCentroid(g1_isSub, isSub_threshold);
			  MDVector md1_isCom = Centroid.getIsComCentroid(g1_isCom, isCom_threshold);
			  MDVector md1_isEql = Centroid.getIsEqlCentroid(g1_isEql);
			  MDVector md1_Sub = Centroid.getSubCentroid(g1_Sub, Sub_threshold);
			  
			  
			  MDVector md2_isSub = Centroid.getIsSubCentroid(g2_isSub, isSub_threshold);
			  MDVector md2_isCom = Centroid.getIsComCentroid(g2_isCom, isCom_threshold);
			  MDVector md2_isEql = Centroid.getIsEqlCentroid(g2_isEql);
			  MDVector md2_Sub = Centroid.getSubCentroid(g2_Sub, Sub_threshold);
			  
			  ArrayList<MDVector> mdlist1 = new ArrayList<MDVector>();
			  ArrayList<MDVector> mdlist2 = new ArrayList<MDVector>();
			  
			  mdlist1.add(md1_isSub);
			  mdlist1.add(md1_isCom);
			  mdlist1.add(md1_isEql);
			  mdlist1.add(md1_Sub);
			  mdlist2.add(md2_isSub);
			  mdlist2.add(md2_isCom);
			  mdlist2.add(md2_isEql);
			  mdlist2.add	(md2_Sub);
			  
			  
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
			  
			  /*
			  System.out.print("Similarity is " );
			  for(Double d : sim){
				  System.out.print(d + ", ");
			  }
			  */
			 
			  
			  System.out.println("-----------------------------------------------------");
			  
			  System.out.println(trace_file1 + "---" + trace_file2);
			  
			  System.out.println(
					  "Total is "+ Centroid.centroidSim_weighted(sim,weights));
			  
			  System.out.println("ground truth: " + matched);
			  System.out.println("-----------------------------------------------------");
			  
		  }//if g1 != null
			}//while

		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//System.out.println("CSV File Reading Done!");

		}
		
	}
}
