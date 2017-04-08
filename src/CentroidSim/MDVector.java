package CentroidSim;

import java.util.HashMap;

import Filtering.*;

public class MDVector {
	public HashMap<String, Con_Intervals> mdvector = null;
	public static HashMap<String, Double> weight_map = null;
	//ArrayList<Con_Intervals> mdvector = null;
	/*
	 * Index		Event Attribute
	 * 0			time
	 * 1			priority
	 * 2			operation
	 * 3			protocol
	 * 4			source_ip
	 * 5			destination_ip
	 * 6			source_port
	 * 7			destination_port
	 * 8			service
	 * 9			direction
	 * 10		msg/alert description
	 */
	/*
	public int getIndex (String str){
		int index = -1;
		if(str.toUpperCase().contains("TIME")){
			index = 0;
		}else if(str.toUpperCase().contains("PRIORITY")){
			index = 1;
		}else if(str.toUpperCase().contains("OPERATION")){
			index = 2;
		}else if(str.toUpperCase().contains("PROTOCOL")){
			index = 3;
		}else if(str.toUpperCase().contains("SRCIP")){
			index = 4;
		}else if(str.toUpperCase().contains("DSTIP")){
			index = 5;
		}else if(str.toUpperCase().contains("SRCPORT")){
			index = 6;
		}else if(str.toUpperCase().contains("DSTPORT")){
			index = 7;
		}else if(str.toUpperCase().contains("SERVICE")){
			index = 8;
		}else if(str.toUpperCase().contains("DIRECTION")){
			index = 9;
		}else if(str.toUpperCase().contains("DESCRIPTION") 
				|| str.toUpperCase().contains("MSG")){
			index = 10;
		}
		return index;
	}
	
	

	
	*/
	
	
	
	public MDVector(){	
		mdvector = new HashMap<String, Con_Intervals>();
		initialize();
	}
	


	//TODO: initialize a MDVector using a condition
	public MDVector(Con_Filtering_Condition cond){
		mdvector = new HashMap<String, Con_Intervals>();
		initialize();
		for(String key : cond.conditions.keySet()){
			mdvector.put(key, cond.conditions.get(key));
		}
	}
	
	public MDVector(Con_Filtering_Condition cond, HashMap<String, Double> weight){
		this(cond);
		if(weight != null){
			setWeightMap(weight);
		}	
	}
	
	public void initialize(){
		if(mdvector == null){
			mdvector = new HashMap<String, Con_Intervals>();
		}
	
		mdvector.put("TIME", null);
		mdvector.put("PRIORITY", null);
		mdvector.put("OPERATION", null);
		mdvector.put("PROTOCOL", null);
		mdvector.put("SRCIP", null);
		mdvector.put("DSTIP", null);
		mdvector.put("SRCPORT", null);
		mdvector.put("DSTPORT", null);
		mdvector.put("SERVICE", null);
		mdvector.put("DIRECTION", null);
		mdvector.put("DESCRIPTION", null);
			
			
		
		if(weight_map == null){
			weight_map = new HashMap<String, Double>();
			weight_map = new HashMap<String, Double>();
			weight_map.put("TIME", 1.0);
			weight_map.put("PRIORITY", 1.0);
			weight_map.put("OPERATION", 1.0);
			weight_map.put("PROTOCOL", 1.0);
			weight_map.put("SRCIP", 1.0);
			weight_map.put("DSTIP", 1.0);
			weight_map.put("SRCPORT", 1.0);
			weight_map.put("DSTPORT", 1.0);
			weight_map.put("SERVICE", 1.0);
			weight_map.put("DIRECTION", 1.0);
			weight_map.put("DESCRIPTION", 1.0);
		}
		
		
		
	}
	
	public static void setWeightMap(HashMap<String, Double> map){
		if(weight_map == null){
			weight_map = new HashMap<String, Double>();
		}
		weight_map.put("TIME", map.get("TIME") != null ? map.get("TIME"):1);
		weight_map.put("PRIORITY", map.get("PRIORITY") != null ? map.get("PRIORITY"):1);
		weight_map.put("OPERATION", map.get("OPERATION") != null ? map.get("OPERATION"):1);
		weight_map.put("PROTOCOL", map.get("PROTOCOL") != null ? map.get("PROTOCOL"):1);
		weight_map.put("SRCIP", map.get("SRCIP")!= null ? map.get("SRCIP"):1);
		weight_map.put("DSTIP", map.get("DSTIP")!= null ? map.get("DSTIP"):0);
		weight_map.put("SRCPORT", map.get("SRCPORT")!= null ? map.get("SRCPORT"):1);
		weight_map.put("DSTPORT", map.get("DSTPORT")!= null ? map.get("DSTPORT"):1);
		weight_map.put("SERVICE", map.get("SERVICE")!= null ? map.get("SERVICE"):1);
		weight_map.put("DIRECTION", map.get("DIRECTION")!= null ? map.get("DIRECTION"):1);
		weight_map.put("DESCRIPTION", map.get("DESCRIPTION")!= null ? map.get("DESCRIPTION"):1);
	}
	
	
	public boolean keyHasNullValue (String key){
		return !mdvector.containsKey(key)  
				|| mdvector.get(key) == null
				|| mdvector.get(key).toString().trim().equals("");
	}

	public static double similarity (MDVector v1, MDVector v2, double overlap_value){
		double result = 0;
		double denominator = 0;
		boolean v1_all_empty = true;
		boolean v2_all_empty = true;
		
		for(String key : weight_map.keySet()){
			//System.out.println("key: " + key);

			if(v1.mdvector.containsKey(key) && v2.mdvector.containsKey(key)){
				if (!v1.keyHasNullValue(key) && !v2.keyHasNullValue(key)){
					v1_all_empty = false;
					v2_all_empty = false;
					
					double w = weight_map.get(key);
					denominator += w;
					double overlapping_value = Con_Intervals.overlappingIntervalValue(
							v1.mdvector.get(key), v2.mdvector.get(key), overlap_value);
					if(overlapping_value > 0){
						result += w*overlapping_value;
						//System.out.println("w: "+w);
						//System.out.println("overlap: "+overlapping_value);
						//System.out.println("key: "+key);
						//System.out.println("current result : "+result);
					}
				
			}else if (!v1.keyHasNullValue(key)){
				v1_all_empty = false;
				result += 0;
				denominator += 1;
				//System.out.println("key not contained in vector 2");
			}else if (!v2.keyHasNullValue(key)){
				v2_all_empty = false;
				result += 0;
				denominator += 1;
				//System.out.println("key not contained in vector 1");
			}else{
				result += 0;
				denominator += 0;
				//System.out.println("key not contained in vector 1 and 2");
			}
		}
		}
		//System.out.println("denominator = " + denominator);
		
		if(v1_all_empty && v2_all_empty){
			return -1;
		}
		
		if(result == 0){
			return 0;
		} else if (denominator != 0){
			return result/denominator;
		} else{
			return 0;
		}
		
	}
	
	public void printOut (){
		for(String key : mdvector.keySet()){
			System.out.println("key: "+key);
			if(mdvector.get(key) != null){
				System.out.println(mdvector.get(key).toString());
			}
		}
	}


}
