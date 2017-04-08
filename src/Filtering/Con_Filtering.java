package Filtering;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class Con_Filtering {
	//int timeorder = -1;
	public int operation_index = -1;
	public String datasource = "";
	public String ID = "";
	public boolean generate_hypo = false;
	 
	
	public Con_Filtering_Condition condition = null;
	
	public Con_Filtering (String id, String index, String ds, String c, boolean h){
		//System.out.println(index);
		operation_index = Integer.parseInt(index.replace("\"", ""));
		datasource = ds.toUpperCase();
 		condition = new Con_Filtering_Condition(c);
		ID = id;
		generate_hypo = h;
	}
	
	public Con_Filtering (String id, String index, String ds, Con_Filtering_Condition c, boolean h){
		//System.out.println(index);
		operation_index = Integer.parseInt(index.replace("\"", ""));
		datasource = ds.toUpperCase();
 		condition = new Con_Filtering_Condition(c);
		ID = id;
		generate_hypo = h;
	}
	
	
	
	@Override
    public String toString() {
        //return operation_index + "";
		return ID + " ("+operation_index+")" ;
    }
	

	
	/*  1: equal to  
	 * 	2: is subsumed by 
	 *  3: is complementary to 
	 *  4: subsumes
	 *  7: is exact complementary
	 *  -1: other
	 */
	public static int relationship(Con_Filtering filter1, Con_Filtering filter2){
		if( !filter1.datasource.equals(filter2.datasource)){
			return -1;
		}else{	
			return relationship_ignore_datasource(filter1, filter2);
		}		
	}
	
	
	/*  1: equal to  
	 * 	2: is subsumed by 
	 *  3: is complementary to 
	 *  4: subsumes
	 *  7: is exact complementary
	 *  -1: other
	 */
	public static int relationship_ignore_datasource(Con_Filtering filter1, Con_Filtering filter2){
		
		if(filter1.condition.isEqual(filter2.condition))
			return 1;
		else if(filter1.condition.isSub(filter2.condition))
			return 2;
		else if(filter1.condition.Sub(filter2.condition))
			return 4;
		else {
			int iscom = filter1.condition.isCom(filter2.condition,2);
		     if(iscom== 1)
				return 3;
		     else if(iscom == 2)
		    	 	return 7;
		}
		return -1;
		
	}
	
	/*
	public static void identify_relationship(Con_Filtering filter, LinkedList<Con_Filtering> list){
		for(int i=list.size()-1; i>=0; i++){
			Con_Filtering filter = list.get(i);
		}
	}
	*/
	
	public static void adjustCondition_toRemoveOverlap(Con_Filtering toadjust, Con_Filtering reference){
		if(toadjust.condition.conditions.isEmpty() || reference.condition.conditions.isEmpty())
			return;
		
		Object[] key_array = toadjust.condition.conditions.keySet().toArray();
		
		for(int i=0; i<key_array.length; i++){
			String key = (String)key_array[i];
			
			if(reference.condition.conditions.keySet().contains(key)){
				Con_Intervals current_intervals = toadjust.condition.conditions.get(key);
   				Con_Intervals ref_intervals = reference.condition.conditions.get(key);
   				current_intervals = Con_Intervals.removeOverlap(current_intervals, ref_intervals);
   				toadjust.condition.conditions.remove(key);
   				toadjust.condition.conditions.put(key, current_intervals);
			}
		}
		return;
	}
	
	

	/*  relationship
	 * 1: equal to  
	 * 	2: is subsumed by 
	 *  3: is complementary to 
	 *  7: is exact complementary
	 *  -1: other
	 */
	//return adjusted Con_Filtering, 
	//could also change the refer node (if tomerge is subsumed by refer)
	public  static Con_Filtering removeOverlap(Con_Filtering tomerge, Con_Filtering refer){
		int rela_ignore_ds1 = relationship_ignore_datasource(tomerge, refer);
		
		if(rela_ignore_ds1 == 1){
			tomerge = null;
			return null;
		}else if(rela_ignore_ds1 == 3){
			return tomerge;
		}else{
			if(rela_ignore_ds1 == 2){
				refer = tomerge;
				return null;
			}else{
				int rela_ignore_ds2 = relationship_ignore_datasource(refer, tomerge);
				if(rela_ignore_ds2 == 2){
					return null;
				}
				adjustCondition_toRemoveOverlap(tomerge, refer);
				return tomerge;
			}
		}
	}
	
	
}
