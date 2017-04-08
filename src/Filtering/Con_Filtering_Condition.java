package Filtering;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.activation.DataSource;

import Util.Util_String;




public class Con_Filtering_Condition {
	public HashMap<String, Con_Intervals> conditions = new HashMap<String, Con_Intervals>();
	public String original_string = "";
	
	public Con_Filtering_Condition(String condition){
		original_string = condition;
		parse(condition);	
	}
	
	public Con_Filtering_Condition(Con_Filtering_Condition condition){
		if(conditions == null){
			conditions = new HashMap<String, Con_Intervals>();
		}
		original_string = condition.original_string;
		Iterator<Entry<String, Con_Intervals>> iterator = condition.conditions.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, Con_Intervals> entry = iterator.next();
			Con_Intervals new_intervals = new Con_Intervals(entry.getValue());
			conditions.put(entry.getKey(), new_intervals);
		}
	}
	
	
	public String getOriginalString (){
		return original_string;
	}
	
	public String key2String(){
		String resultString = "";
		Iterator<String> iterator = conditions.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			resultString = resultString+key+", " ;
		}
		return resultString;
	}
	
	public boolean isEmptyCondition(){
		Iterator<Entry<String, Con_Intervals>> iterator = conditions.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, Con_Intervals> entry = iterator.next();
			if(!entry.getKey().equals("DATATIME")  && !entry.getKey().equals("PRIORITY") && !entry.getValue().isEmptyIntervals())
				return false;
		}
		return true;
	}
	
	/*
	public  boolean isSatisfied_restrict(Firewall firewall){
		for(int i =0; i<10; i++){
			String field = cleanValue(firewall.getFieldName(i+1)).toUpperCase();
			String value = firewall.getFieldValue(i+1);
			
			if(conditions.containsKey(field)){
				
				Con_Intervals intervals1 = conditions.get(field.toUpperCase());
				
			
				Con_Filtering_Condition condition2 = new Con_Filtering_Condition(field+"="+value);
				
				Con_Intervals intervals2 = condition2.conditions.get(field);
				
				int rela = Con_Intervals.relationship_intervals_intervals(intervals1, intervals2);
				if( rela== 3 || rela ==-1)
					return false;
			}
		}
		return true;
	}
	*/
	

	/*
	//issatisfied: has at least one condition that new_field+value is issub or isequal
	public static boolean connection_isSatisfied_restrict(Con_Filtering_Condition condition1, Con_Connection connection){
		
		boolean hassatisfied_field = false;
		
			for(int i =0; i<connection.attrNum(); i++){
				String field = Util_String.cleanValue(connection.getAttrName(i)).toUpperCase();
				String value = Util_String.cleanValue(connection.getAttrValue(i));
				
				if(condition1.conditions.containsKey(field)){
					
					if(!hassatisfied_field)
						hassatisfied_field = true;
					
					Con_Intervals intervals1 = condition1.conditions.get(field.toUpperCase());
								
					Con_Filtering_Condition condition2 = new Con_Filtering_Condition(field+"="+value);
					
					Con_Intervals intervals2 = condition2.conditions.get(field);
					
					int rela2 = Con_Intervals.relationship_intervals_intervals(intervals2, intervals1);
					
					if( rela2== 1 || rela2 == 2){
						continue;
					}else{
						return false;
					}		
				
			}
		}
	
		return hassatisfied_field;
	}
	*/
	
	

    public void parse(String condition)	{

    	String buffer = new String(condition);
   		buffer = ("OR" + buffer).trim();
   		
   		
   		while(buffer.length() > 0){
	   			String logic_conn = "";
	   			buffer = buffer.trim();
	   			
	   			//to  identify the first_logic _connective
	   			if(buffer.substring(0, 2).equals("OR")){
	   				logic_conn = "OR";
	   			}else if(buffer.substring(0, 3).equals("AND")){
	   			    logic_conn="AND";
	   			}
	
		   		buffer = buffer.replaceFirst(logic_conn, "");
		   		buffer = buffer.trim();
		   		
		   		String first_term = "";
		   		
		   		int first_AND_index = buffer.indexOf("AND ");
		   		int first_OR_index = buffer.indexOf("OR ");
		   		
		   		//to identify the corresponding term following op
		   		if(first_AND_index == -1 && first_OR_index == -1){
		   			first_term = new String(buffer);
		   		}
		   		else if(first_AND_index != -1 && first_OR_index == -1)	{
		   			first_term  = buffer.substring(0, first_AND_index);
		   		}else if(first_AND_index == -1 && first_OR_index != -1)	{
		   			first_term  = buffer.substring(0, first_OR_index);
		   		}else if(first_AND_index != -1 && first_OR_index != -1)	{
		   			int minimize_index = (first_AND_index<first_OR_index)?first_AND_index:first_OR_index;
		   			first_term  = buffer.substring(0, minimize_index);
		   		}
		   		
		   		addOneTerm(first_term, logic_conn);
		   		
		   		//remove the term from buffer
		   		buffer = buffer.replace(first_term, "");
		   		buffer =  buffer.trim();
	   	
	   		}
	
   		}
       
    public void addOneTerm(String term, String logic_conn){
 			String op ="";
 			term = term.trim();
 			
 	        if(term.contains("<>")) {
 	            op = "<>";
 	        }
 	        else if (term.contains(">=")) {
 	            op = ">=";
 	        }
 	        else if (term.contains("<=")) {
 	            op = "<=";
 	        }
 	        else if (term.contains("=")) {
	            op = "=";
	        }
 	        else if(term.contains(">")){
 	            op = ">";
 	        }
 	        else if(term.contains("<")){
 	            op ="<";
 	        }
 	        else{
 	        	return;
 	        }
 			String[] array = term.split(op);
             if(array.length >1)
             {
                String field = Util_String.cleanValue(array[0].trim()).toUpperCase();
                		
                
                if(!conditions.containsKey(field)){
             	   Con_Intervals intervals = new Con_Intervals();
             	   conditions.put(field, intervals);
                }
                
                String value = Util_String.cleanValue(array[1].trim());
                
                if(field.contains("IP")){
                		if(!field.contains("*")){
                			value =Util_String.cutIPStr(value); 		
                		}
                }
                
               ArrayList<Con_Interval> new_intervals = Con_Interval.to_intervals(op, value);
            
               /*
               boolean firstlogic = true;
               for(Con_Interval new_interval : new_intervals){
	            	   if(firstlogic){
	            		   conditions.get(field).add_interval(new_interval, logic_conn);
	            		   firstlogic = false;
	            	   }else{
	            		   conditions.get(field).add_interval(new_interval, "OR");
	            	   }
               }
               */
               ArrayList<Con_Interval> original = conditions.get(field).intervals;
               conditions.get(field).mergeIntervals(new_intervals, logic_conn);
               
             }
      }
   		
       public  boolean hasEqualDimension(Con_Filtering_Condition condition2){
   		return conditions.keySet().equals(condition2.conditions.keySet());
   	}
   	
       
   	public boolean atLeastOneEqual(Con_Filtering_Condition condition2){

   		Iterator iterator = conditions.keySet().iterator();
			
			while(iterator.hasNext()){
				String key = (String)iterator.next();
				
				if(condition2.conditions.keySet().contains(key)){
					
					Con_Intervals intervals1 = conditions.get(key);
	   				Con_Intervals intervals2 = condition2.conditions.get(key);
	   				boolean isequal = Con_Intervals.relationship_intervals_intervals(intervals1, intervals2) == 1;
	   				if(isequal)
	   					return true;				
				}
			}
			return false;

   	}
   	
   	
   	public boolean isEqual(Con_Filtering_Condition condition2){
   		if(hasEqualDimension(condition2)){
		
   			boolean isequal = true;
   			
   			Iterator iterator = conditions.keySet().iterator();
   			
   			while(iterator.hasNext()){
   				//for each dimension
   				String key = (String)iterator.next();

   				Con_Intervals intervals1 = conditions.get(key);
   				
   				if(condition2.conditions.containsKey(key)){
   					Con_Intervals intervals2 = condition2.conditions.get(key);
   					isequal= Con_Intervals.relationship_intervals_intervals(intervals1, intervals2) == 1;
   				}else {
   					isequal = false;
   				}

   				if(!isequal)
   					return false;
   			}
   			
   			return true;
   			
   			
   		}else {
   			return false;
   		}
   	}
   	
   	//is subsumed
   	public boolean isSub(Con_Filtering_Condition condition2){
   		//the subset has larger keyset
   		if(conditions.keySet().containsAll(condition2.conditions.keySet())){

   			boolean isSub = true;
   			
   			Iterator iterator = condition2.conditions.keySet().iterator();
   			
   			while(iterator.hasNext()){
   				//for each dimension
   				String key = (String)iterator.next();

   				Con_Intervals intervals1 = conditions.get(key);
  
   				Con_Intervals intervals2 = condition2.conditions.get(key);
   				
   				int rela = Con_Intervals.relationship_intervals_intervals(intervals1, intervals2);
   				isSub = ( rela == 2 || rela == 1);
   				if(!isSub){
   					return false;
   				}
   							
   			}
   			return true;
   				
   			
   		}else {
   			return false;
   		}
   	}
   	
  //subsumes
	public boolean Sub(Con_Filtering_Condition condition2){
		return condition2.isSub(this);
		
		/*
		//the subset has larger keyset
		boolean Sub = true;	
		Iterator iterator = conditions.keySet().iterator();	
		while(iterator.hasNext()){
			//for each dimension
			String key = (String)iterator.next();
			if(!condition2.conditions.containsKey(key)){
				return false;
			}

			Con_Intervals intervals1 = conditions.get(key);
			Con_Intervals intervals2 = condition2.conditions.get(key);
			
			//identify relationships between two list of intervals
			/*  1: equal to  
			 * 	2: is subsumed by 
			 *  3: is complementary to 
			 *  -1: other
			 */
		/*
			int rela = Con_Intervals.relationship_intervals_intervals(intervals2, intervals1);
			Sub= ( rela == 2 || rela == 1);
			
			if(!Sub)
				return false;
		}
		
		return true;
		*/
	}
   	
	//is complementary:
   	//iscom+iscom+iscom
   	//iscom+isequal...
   	//iscom+issub
   	//1: iscom,  2: iscom_exact, 0: not
   	//ip=34, port > 6667
	// exactcom_def = 1: at least one exactcom
	//exactcom_def = 1: all exactcom
   	public int isCom(Con_Filtering_Condition condition2, int exactcom_def){
   		
   		boolean iscom = false;
   		boolean iscom_exact2 =  conditions.keySet().containsAll(condition2.conditions.keySet())
   				&& condition2.conditions.keySet().containsAll(conditions.keySet());
   		boolean iscom_exact1 = false;
   		Iterator iterator = conditions.keySet().iterator();		
		while(iterator.hasNext()){
			String key = (String)iterator.next();	
			if(condition2.conditions.keySet().contains(key)){		
				Con_Intervals intervals1 = conditions.get(key);
   				Con_Intervals intervals2 = condition2.conditions.get(key);
   				iscom = Con_Intervals.relationship_intervals_intervals(intervals1, intervals2) == 3;
				if(iscom){
					if(iscom_exact2){
						iscom_exact2 = Con_Intervals.isExactCom(intervals1.intervals, intervals2.intervals);
					}
					iscom_exact1 = Con_Intervals.isExactCom(intervals1.intervals, intervals2.intervals);
				}
			}else{
				iscom_exact1 = false;
			}
			//if(iscom_exact)
				//return 2;
			//if(iscom){
			//	return 1;
			//}
			
		}
		

			
			
		if(exactcom_def == 1){
			if(iscom_exact1)
				return 2;
		} else if(exactcom_def == 2){
			if(iscom_exact2)
				return 2;
		}
		
		if(iscom){
			return 1;
		}
		
		return 0;

   	}
   	
   	
   	public static void adjustCondition_toRemoveOverlap(Con_Filtering_Condition toadjust, Con_Filtering_Condition reference){
		if(toadjust.conditions.isEmpty() || reference.conditions.isEmpty())
			return;
		
		Object[] key_array = toadjust.conditions.keySet().toArray();
		
		for(int i=0; i<key_array.length; i++){
			String key = (String)key_array[i];
			
			if(reference.conditions.keySet().contains(key)){
				Con_Intervals current_intervals = toadjust.conditions.get(key);
   				Con_Intervals ref_intervals = reference.conditions.get(key);
   				current_intervals = Con_Intervals.removeOverlap(current_intervals, ref_intervals);
   				toadjust.conditions.remove(key);
   				toadjust.conditions.put(key, current_intervals);
   				
			}
		}
		return;
	}

   	
   	
   	
	}


