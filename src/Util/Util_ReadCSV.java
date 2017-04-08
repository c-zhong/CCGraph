package Util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.LoggingPermission;

import javax.swing.text.AbstractDocument.BranchElement;

import org.jgrapht.Graph;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;


import Filtering.Con_Filtering;



public class Util_ReadCSV {

	public static String standarizeCondition(String condition){
		String new_condition = condition.replace("SourcePort", "SRCPORT")
		.replace("DestPort", "DSTPORT")
		.replace("SourceIP", "SRCIP")
		.replace("DestIP", "DSTIP")
		.replace("DesPort", "DSTPORT")
		.replace("DesIP", "DSTIP");
		return new_condition;
	}
	
	public static void readCSV(String csv_file, LinkedList<Con_Filtering> filter_list){
		BufferedReader br =  null;
		String line = "";
		String cvsSplitBy = ",";
		int filtering_index =0;

		try{
			boolean firstline = true;

			br = new BufferedReader(new FileReader(csv_file));
			while ((line = br.readLine()) != null) {

				if(firstline){
					firstline = false;
					continue;
				}
				// use comma as separator
				String[] itemarray = line.trim().split(cvsSplitBy);


				if (itemarray.length >= 5)
				{
					String ID = itemarray[0].trim();
					String index = itemarray[1].trim(); //operation index

					String datasource = itemarray[3].trim();
					String condition = standarizeCondition(itemarray[4].trim());
					boolean hashypo = itemarray[5].trim().equals("1");

					//String hypo = itemarray[3];

					if(!condition.trim().equals("")){
						filtering_index ++;
						
						//here switch operation_index and id (filter.operation_index = id, filter.ID = index
						Con_Filtering new_filtering = new Con_Filtering(ID, index, datasource, condition, hashypo);
						// System.out.println(new_filtering.datasource +"-");
						filter_list.add(new_filtering);
					}
				}

			}
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
			System.out.println("CSV File Reading Done!");

		}
	}


	//IRC, ftp, ssh, dns, unknown ip
	public static int[] countEvents(String csv_file, int[] counts){
		BufferedReader br =  null;
		String line = "";
		String cvsSplitBy = ",";
		int filtering_index =0;


		boolean firewall = csv_file.toLowerCase().contains("firewall");
		boolean ids = csv_file.toLowerCase().contains("ids");

		try{
			boolean firstline = true;

			br = new BufferedReader(new FileReader(csv_file));
			while ((line = br.readLine()) != null) {

				if(firstline){
					firstline = false;
					continue;
				}
				// use comma as separator
				String[] itemarray = line.trim().split(cvsSplitBy);

				String srcport = "";
				String srcip = "";
				String dstport = "";
				String dstip = "";

				if(ids) //IDS
				{
					srcport = itemarray[2].trim();
					srcip = itemarray[1].trim();
					dstport = itemarray[4].trim();
					dstip = itemarray[3].trim();
				}
				else if(firewall){
					srcport = itemarray[9].trim();
					srcip = itemarray[5].trim();
					dstport = itemarray[10].trim();
					dstip = itemarray[6].trim();
				}
				boolean src_internal = srcip.contains("172.23.") || srcip.equals("10.32.0.1");
				boolean dst_internal = dstip.contains("172.23.") || dstip.equals("10.32.0.1");
				boolean src_external = srcip.contains("10.32.") && !srcip.equals("10.32.0.1");
				boolean dst_external = dstip.contains("10.32.") && !dstip.equals("10.32.0.1");

				if((src_internal && dst_external) ||(src_external && dst_internal) ){
					if(srcport.equals("6667") || dstport.equals("6667") ){
						counts[0] = counts[0]+1;

					}else if(srcport.equals("21") || dstport.equals("21") ){

						counts[1] = counts[1]+1;
					}else if(srcport.equals("22") || dstport.equals("22") ){

						counts[2] = counts[2]+1;
					}
				}else if(src_internal && dstip.contains("172.23.0.10")){
					counts[3] = counts[3]+1;
				}else if(srcip.contains("172.28.29.") || dstip.contains("172.28.29.")){
					counts[4] = counts[4] + 1;
				}
			}
			return counts;

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

		}
		return counts;
	}


/*

	public static void FGraph_readVset(String csv_file, LinkedList<Con_Filtering> filter_list){
		BufferedReader br =  null;
		String line = "";
		String cvsSplitBy = ",";
		int filtering_index =0;

		try{
			boolean firstline = true;

			br = new BufferedReader(new FileReader(csv_file));
			while ((line = br.readLine()) != null) {

				if(firstline){
					firstline = false;
					continue;
				}
				// use comma as separator
				String[] itemarray = line.trim().split(cvsSplitBy);

				if (itemarray.length >= 4)
				{
					String ID = itemarray[0].trim();         
					String index = itemarray[1].trim();
					String datasource = itemarray[2].trim();
					String condition = itemarray[3].trim();

					if(!condition.trim().equals("")){
						filtering_index ++;
						//System.out.println(condition);
						Con_Filtering new_filtering = new Con_Filtering(ID, index, datasource, condition);
						// System.out.println(new_filtering.datasource +"-");
						filter_list.add(new_filtering);
					}
				}

			}
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

			System.out.println("CSV File Reading Done!");

		}
	}



	public static void FGraph_readVset_inIsCom(String csv_file, LinkedList<Con_Filtering> filter_list_inisCom, LinkedList<Con_Filtering> filter_list_outisCom){
		BufferedReader br =  null;
		String line = "";
		String cvsSplitBy = ",";

		try{
			boolean firstline = true;

			br = new BufferedReader(new FileReader(csv_file));
			while ((line = br.readLine()) != null) {

				if(firstline){
					firstline = false;
					continue;
				}
				// use comma as separator
				String[] itemarray = line.trim().split(cvsSplitBy);


				if (itemarray.length >= 5)
				{
					String ID = itemarray[1].trim();         
					String index = itemarray[2].trim();
					String datasource = itemarray[3].trim();
					String condition = itemarray[4].trim();

					String isinCom = itemarray[5].trim();

					if(!condition.trim().equals("")){
						//System.out.println(condition);
						Con_Filtering new_filtering = new Con_Filtering(ID, index, datasource, condition);
						// System.out.println(new_filtering.datasource +"-");

						if(isinCom.trim().equals("1") || isinCom.trim().toUpperCase().equals("TRUE")){
							filter_list_inisCom.add(new_filtering);
						}else{
							filter_list_outisCom.add(new_filtering);
						}
					}
				}

			}
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

			System.out.println("inisCom Vertices File Reading Done!");

		}
	}



	public static void FGraph_readSchemaNode(String csv_file, LogProcessing.Schema schema){
		BufferedReader br =  null;
		String line = "";
		String cvsSplitBy = ",";
		int filtering_index =0;

		try{
			boolean firstline = true;

			br = new BufferedReader(new FileReader(csv_file));
			while ((line = br.readLine()) != null) {

				if(firstline){
					firstline = false;
					continue;
				}
				// use comma as separator
				String[] itemarray = line.trim().split(cvsSplitBy);
				//System.out.println(line);

				if (itemarray.length >= 4)
				{
					String ID = itemarray[0].trim();         
					String index = itemarray[1].trim();
					String datasource = itemarray[2].trim();
					String condition = itemarray[3].trim();

					if(!condition.trim().equals("")){
						filtering_index ++;
						//System.out.println(condition);
						LogProcessing.Schema.MyNode new_node = new LogProcessing.Schema.MyNode(ID, index, datasource, condition);
						if(!new_node.condition.isEmptyCondition())
							schema.addNode(new_node);
					}
				}

			}
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

			System.out.println("Schema Nodes Reading Done!");

		}
	}
	
	
	public static void setSchemaNodeIndexInTaskData(LogProcessing.Schema schema, String filepath, String datasource ) throws ParseException{
		BufferedReader br =  null;
		String line = "";
		int index = -1;
		
		try{
			boolean firstline = true;

			br = new BufferedReader(new FileReader(filepath));
			while ((line = br.readLine()) != null) {
				index ++;
				if(firstline){
					firstline = false;
					continue;
				}
				
				String ID = Util_String.getConnectionID(filepath, index+"");
				LogProcessing.Con_Connection connection_firewall = new LogProcessing.Con_Connection(datasource, line,ID);
				
				Iterator<MyNode> iterator = schema.graph.vertexSet().iterator();
				while(iterator.hasNext()){
					MyNode node =  iterator.next();
					if(node.first_occur_time == null){
						if(Filtering.Con_Filtering_Condition.connection_isSatisfied_restrict(node.condition, connection_firewall)){
							Date date = Util.Util_Date.StringtoDate("firewall", connection_firewall.time);
							node.first_occur_time = date;
							node.first_occur_index = index;
						}
					}
				}		
			}
			
			LinkedList<MyNode> todelete = new LinkedList<MyNode>();
			//delete the nodes without first_occur_time
			Iterator<MyNode> iterator = schema.graph.vertexSet().iterator();
			while(iterator.hasNext()){
				MyNode node =  iterator.next();
				if(node.first_occur_time == null){
					todelete.add(node);
				}
			}
			for(int i=0; i<todelete.size(); i++){
				schema.graph.removeVertex(todelete.get(i));
			}
			
			
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


		}
	}
	
	*/

	
	
	
	
	
	

	
}


