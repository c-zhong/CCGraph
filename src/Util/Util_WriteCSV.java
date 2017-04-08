package Util;
import java.io.File;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.omg.PortableServer.POAManagerPackage.State;

import Filtering.Con_Filtering;
import Filtering.Con_Graph;


public class Util_WriteCSV {
	//Delimiter used in CSV file
		private static final String COMMA_DELIMITER = ",";
		private static final String NEW_LINE_SEPARATOR = "\n";
		
		//CSV file header
		private static final String FILE_HEADER = "source_node,target_node,edge_type";
		private static final String FILE_HEADER_DETAIL = "source_id,source, target_id, target, edge_type";

		public static void writeGraph2CSV(Con_Graph g, String filename) {
			FileWriter fileWriter = null;
			
		try{

			fileWriter = new FileWriter(filename);
			
			//Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			
			 int nodenum = g.graph.vertexSet().size();
			 for(Con_Graph.MyEdge edge : g.graph.edgeSet() ){
				 if(edge != null){
				 Con_Filtering source = g.graph.getEdgeSource(edge);
				 Con_Filtering target = g.graph.getEdgeTarget(edge);
				 Double edge_type = edge.weight();
				 
				 fileWriter.append(source.ID+"");
				 fileWriter.append(COMMA_DELIMITER);
				 fileWriter.append(target.ID+"");
				 fileWriter.append(COMMA_DELIMITER);
				 fileWriter.append(edge_type+"");
				fileWriter.append(NEW_LINE_SEPARATOR);
				 }
			 }
			 

				System.out.println("CSV file was created successfully !");
				
			} catch (Exception e) {
				System.out.println("Error in CsvFileWriter !");
				e.printStackTrace();
			} finally {		
					try {
						fileWriter.flush();
						fileWriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			}
		}
		
		
		public static void writeGraph2CSV_Details(Con_Graph g, String filename) {
			FileWriter fileWriter = null;
			
		try{

			fileWriter = new FileWriter(filename);
			
			//Write the CSV file header
			fileWriter.append(FILE_HEADER_DETAIL.toString());
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			
			 int nodenum = g.graph.vertexSet().size();
			 for(Con_Graph.MyEdge edge : g.graph.edgeSet() ){
				 if(edge != null){
				 Con_Filtering source = g.graph.getEdgeSource(edge);
				 Con_Filtering target = g.graph.getEdgeTarget(edge);
				 Double edge_type = edge.weight();
				 
				 fileWriter.append(source.ID+"");
				 fileWriter.append(COMMA_DELIMITER);
				 fileWriter.append(source.condition.original_string+"");
				 fileWriter.append(COMMA_DELIMITER);
				 fileWriter.append(target.ID+"");
				 fileWriter.append(COMMA_DELIMITER);
				 fileWriter.append(target.condition.original_string+"");
				 fileWriter.append(COMMA_DELIMITER);
				 fileWriter.append(edge_type+"");
				fileWriter.append(NEW_LINE_SEPARATOR);
				 }
			 }
			 

				System.out.println("CSV file was created successfully !");
				
			} catch (Exception e) {
				System.out.println("Error in CsvFileWriter !");
				e.printStackTrace();
			} finally {		
					try {
						fileWriter.flush();
						fileWriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			}
		}
		
		/*
		public static void writeStates2CSV(LinkedList<PState> states, String dir) {

			PState lastState = null;
		 try {
			 
			for(int k =0; k < states.size(); k++){
				String filename = dir+"_state"+(k+1)+".csv";
				
				File output_dir = new File(dir);
				if(!output_dir.exists()){
					output_dir.mkdir();
				}
				
				File file = new File(filename);
				if(!file.exists()){
					file.createNewFile();
				}
				
				FileWriter fileWriter = new FileWriter(filename);
				int count = 0;
				
				if(lastState == null){
					lastState = states.get(k);
					continue;
				}
				PState currentState = states.get(k);
				
				for(int i =0; i<lastState.satified_entrieID_list.size(); i++){
					String entryi = lastState.satified_entrieID_list.get(i);
					for(int j=0; j<currentState.satified_entrieID_list.size(); j++){
						String entryj =currentState.satified_entrieID_list.get(j);
						
					
						fileWriter.append(entryi.toString());
						 fileWriter.append(COMMA_DELIMITER);
						 fileWriter.append(entryj.toString());
						 fileWriter.append(NEW_LINE_SEPARATOR);
						 count ++;
						
					}
				
				}
				System.out.println(filename+"  was created successfully: "+count+" entries in total");
				fileWriter.flush();
				fileWriter.close();
			}
			 } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
			
				
				
		}
		
		
		*/
		
		public static void writeTable2CSV(LinkedList<String> data, String filename) {
		 try {
			 
				FileWriter fileWriter = new FileWriter(filename);
				int count = 0;
			
				for(int i =0; i<data.size(); i++){
					
						fileWriter.append(data.get(i));
						 fileWriter.append(NEW_LINE_SEPARATOR);
						 count ++;		
					}
				
				
				System.out.println(filename+"  was created successfully: "+count+" entries in total");
				fileWriter.flush();
				fileWriter.close();
			 } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
				
				
		}
		
		
		
	

			public static void FGraph_writeVset(LinkedList<Con_Filtering> filter_list, String filename) {
					FileWriter fileWriter = null;
				try{
					
					if(filter_list == null)
						return;

					fileWriter = new FileWriter(filename);
					
					//Write the CSV file header
					fileWriter.append("ID, Index, DataSource, Condition");
					//Add a new line separator after the header
					fileWriter.append(NEW_LINE_SEPARATOR);
					
					
					for(int i =0; i < filter_list.size(); i ++){
						Con_Filtering filter = filter_list.get(i);
						fileWriter.append(filter.ID);
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(filter.operation_index+"");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(filter.datasource);
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(filter.condition.getOriginalString());
						fileWriter.append(NEW_LINE_SEPARATOR);
						
					}//for
					
				
						System.out.println("CSV file was created successfully !");
						
					} catch (Exception e) {
						System.out.println("Error in CsvFileWriter !");
						e.printStackTrace();
					} finally {		
							try {
								fileWriter.flush();
								fileWriter.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
					}
				}
			
			
		
				


}
