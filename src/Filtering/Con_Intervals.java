package Filtering;


import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import javax.naming.spi.DirStateFactory.Result;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;


public class Con_Intervals {
	public ArrayList<Con_Interval> intervals = new ArrayList<Con_Interval>();
	
	public static final String AND = "AND";
	public static final String OR = "OR";
	
	public Con_Intervals(){
		
	}
	

	
	//used for clone, otherwise this class can't be cloned but just a reference.
		public Con_Intervals(Con_Intervals list)
		{
			
			if(intervals == null){
				intervals = new ArrayList<Con_Interval>(); 
			}
			for(int i=0; i<list.intervals.size(); i++){
				Con_Interval new_interval = new Con_Interval(list.intervals.get(i));
				intervals.add(new_interval);
			}
		}
		
		
		
	//if FIELD is IP, return whether the value range are all absolute IP (not contain *)
	public boolean isCompleteIP(){
		//assume the value intervals are for the FIELD containing IP
		for(int i=0; i<intervals.size(); i++){
			if(!intervals.get(i).notContainsStarSymbol()){
				return false;
			}
		}
		return true;
	}
	
	public boolean isEmptyIntervals(){
		LinkedList<Con_Interval> toremove = new LinkedList<Con_Interval>();
		for(int i=0; i<intervals.size(); i++){
			if(intervals.get(i).isEmptyInterval()){
				toremove.add(intervals.get(i));
			}
		}
		intervals.removeAll(toremove);
		return intervals.isEmpty();
	}
	
	public int size ()
	{
		return intervals.size();
	}
	
	public Con_Intervals(ArrayList<Con_Interval> intlist)
	{
		if(intlist != null)
		{
			intervals = intlist;
		}
	}
	
	
	
public void mergeIntervals(Con_Interval interval, String logic_conn){
		ArrayList<Con_Interval> list = new ArrayList<Con_Interval>();
		list.add(interval);
		mergeIntervals(list, logic_conn);
	}

    
	
	//merge intervals 2 to intervals 1
	public void mergeIntervals(ArrayList<Con_Interval> intervals2, String logic_conn){
		
		//Con_Intervals result = new Con_Intervals();
	
		try{
			//Con_Intervals intervals1 = (Con_Intervals) this.clone();
			Con_Intervals intervals1= new Con_Intervals(this);
			Con_Intervals result_list = new Con_Intervals();
			
			if(logic_conn.equals("AND")){
				int i =0;
				for( i=0; i<intervals2.size(); i++){
					Con_Interval item = intervals2.get(i);
					
					//Con_Intervals new_list = (Con_Intervals) intervals1.clone();
					Con_Intervals new_list = new Con_Intervals(intervals1);
					
					new_list.add_interval(item, logic_conn);
					
					if(result_list.intervals.size() == 0){
						result_list.intervals.addAll(new_list.intervals);
					}else{
						for(int j=0; j<new_list.intervals.size(); j++){
							result_list.add_interval(new_list.intervals.get(j), "OR");
						}
					}
					
				}
				this.intervals.clear();
				intervals.addAll(result_list.intervals);
				
			}else if(logic_conn.equals("OR")){
				for(int i=0; i<intervals2.size(); i++){
					Con_Interval item = intervals2.get(i);
					add_interval(item, "OR");
				}
			}
		} 
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	//identify relationships between two list of intervals
	/*  1: equal to  
	 * 	2: is subsumed by 
	 *  3: is complementary to 
	 *  -1: other
	 */
	public static int relationship_intervals_intervals(Con_Intervals list1, Con_Intervals list2){
		Collections.sort(list1.intervals);
		Collections.sort(list2.intervals);
		
		boolean isequal = true;
		boolean issub = true;
		boolean iscom = true;
		
		if(list1.size() != list2.size())
			isequal = false;
		
		for(int i=0; i<list1.size(); i++){	
			if(issub || isequal || iscom){
				Con_Interval interval1 = list1.intervals.get(i);
				int relation_interval1_list2 = relationship_interval_intervals(interval1, list2.intervals);
				if(relation_interval1_list2==3){//other
					issub = false;
					iscom = false;
					isequal = false;
				}else if(relation_interval1_list2 == 1){// no overlap
					issub = false;
					isequal = false;
				}else if(relation_interval1_list2 == 2){// is contained
					iscom = false;
				}
			
				if(isequal){
					Con_Interval interval2 = list2.intervals.get(i);
					if(!interval1.isEqual(interval2))
						isequal = false;
				}
				
			}else {
				break;
			}
		}
		
		if(isequal)
			return 1;
		if(issub)
			return 2;
		if(iscom)
			return 3;
		
 		return -1;

	}
	
	/*
	 * 1: no overlap
	 * 2: is contained 
	 * 3: other
	 */
	
	//6667,6667
	//min80
public static int relationship_interval_intervals(Con_Interval new_interval, ArrayList<Con_Interval> intervals){
	 //add new_interval 
	 int start_index = 0;
	 int end_index = 0;
	 for(start_index =0; start_index<intervals.size(); start_index++){
		 //on the left of the new_interval, is no overlap, then continue, otherwise, break
		 int compare_start_end = new_interval.start.compareTo(intervals.get(start_index).end);
		 if(compare_start_end>0)
			 continue;
		 else if(compare_start_end == 0){
			 if(new_interval.left_open || intervals.get(start_index).right_open)
				 continue;
			 else {
				 break;
			 }
		 }else{
			 break;
		 }
	 }
			 
			
	 for(end_index = start_index; end_index<intervals.size(); end_index++){
		 //after then, is has overlap, then continue, otherwise, break
		 int compare_end_start = new_interval.end.compareTo(intervals.get(end_index).start);
		 if(compare_end_start>0){
			 continue;
		 }else if(compare_end_start == 0){
			 if(!new_interval.right_open && !intervals.get(end_index).left_open){
				 continue;
			 }else{
				 break;
			 }
		 }else{
			 break;
		 }
	 }
			 
			 
			// System.out.println("start_index="+start_index+"; end_index="+end_index);
			 
			 if(start_index > end_index){
				 return 1;				 
			 }else if(start_index == end_index){
				 return 1;
			 }else{

				 Con_Interval start_index_interval = intervals.get(start_index);
				 
				 if(start_index_interval.start.compareTo(new_interval.start)>0){
				 //	System.out.println("start > newinterval.start");
					 int start_newend_compare = start_index_interval.start.compareTo(new_interval.end);
					 if(start_newend_compare < 0){
						 return 3;
					 }else if(start_newend_compare == 0){
						 if(start_index_interval.left_open || new_interval.right_open){
							 return 1;
						 }else{
							 return 3;
						 }
					 }else{
						 return 1;
					 }
					 
				 }else if(start_index_interval.start.compareTo(new_interval.start) == 0){
				 //	System.out.println("start = newinterval.start");
					if(start_index_interval.left_open != new_interval.left_open){
						return 3;
					}else{
						int end_newend_compare = start_index_interval.end.compareTo(new_interval.end);
						if(end_newend_compare==0){
							if(new_interval.right_open || (!new_interval.right_open && !start_index_interval.right_open)){
								return 2;
							}
						}else if(end_newend_compare>0){
							return 2;
						}else {
							return 3;
						}
					}
					
				 }else{
					 //System.out.println("start < newinterval.start");
					 //start < new_interval.start
					 if(start_index_interval.end.compareTo(new_interval.end)>0){
						 return 2;
					 }else if(start_index_interval.end.compareTo(new_interval.end)==0){
						 if(new_interval.right_open || !new_interval.right_open && !start_index_interval.right_open){
								return 2;
							}					 
					 }else{
						 return 3;
					 }
				 }
			 }
			return 3;	 			
	}
	
	
	//[TODO]
	public void add_interval(Con_Interval new_interval, String logic_conn){
		if(intervals.size() == 0){
			 intervals.add(new_interval);
			 return;
		 }
			 
		 if(logic_conn.equals(AND)){
			 //add new_interval
			 
				 ArrayList<Con_Interval> result_list = new ArrayList<Con_Interval>();
				 
				 for(int i=0; i<intervals.size(); i++) {
					 Con_Interval item = intervals.get(i);
					 ArrayList<Con_Interval> list = item.AND_merge(new_interval);
					 
					 if(list.size() >0){
						  result_list.addAll(list);
					 }
				 }
				 
				 intervals.clear();
				 intervals.addAll(result_list);
			 
			
		 }else if(logic_conn.equals(OR)){
			 
			  //add new_interval
			 
			 int start_index = 0;
			 int end_index = 0;
			 
			 for(start_index =0; start_index<intervals.size(); start_index++){
				 //on the left of the new_interval, is no overlap, then continue, otherwise, break
				 int compare_start_end = new_interval.start.compareTo(intervals.get(start_index).end);
				 if(compare_start_end>0)
					 continue;
				 else if(compare_start_end == 0){
					 if(new_interval.left_open || intervals.get(start_index).right_open)
						 continue;
					 else {
						 break;
					 }
				 }else{
					 break;
				 }
			 }
			 
			
			 for(end_index = start_index; end_index<intervals.size(); end_index++){
				 //after then, is has overlap, then continue, otherwise, break
				 int compare_end_start = new_interval.end.compareTo(intervals.get(end_index).start);
				 if(compare_end_start>0){
					 continue;
				 }else if(compare_end_start == 0){
					 if(!new_interval.right_open && !intervals.get(end_index).left_open){
						 continue;
					 }else{
						 break;
					 }
				 }else{
					 break;
				 }
			 }
			 
			 
			 if(start_index > end_index){//reach the end of the list
				intervals.add(new_interval); 
			 }else if(start_index == end_index){
				 intervals.add(start_index, new_interval);
			 }else{
				 Con_Interval_Variable start_variable = new_interval.start;
				 Con_Interval_Variable end_variable = new_interval.end;
				 
				 Con_Interval start_index_interval = intervals.get(start_index);
				 Con_Interval end_index_interval = intervals.get(end_index-1);
				 
				 if(start_index_interval.start.compareTo(new_interval.start)>0){
					 start_index_interval.start = new_interval.start;
					 start_index_interval.left_open = new_interval.left_open;
					 
				 }else if(start_index_interval.start.compareTo(new_interval.start) == 0){
					 start_index_interval.left_open = new_interval.left_open && start_index_interval.left_open;
				 }else{
					 //do nothing
				 }
				 
				 
				 if(end_index_interval.end.compareTo(new_interval.end)>0){
					 start_index_interval.end = end_index_interval.end;
					 start_index_interval.right_open = end_index_interval.right_open;
					 
				 }else if(end_index_interval.end.compareTo(new_interval.end) == 0){
					 start_index_interval.end = end_index_interval.end;
					 start_index_interval.right_open = new_interval.right_open && end_index_interval.right_open;
				 }else{
					 start_index_interval.end = new_interval.end;
					 start_index_interval.right_open = new_interval.right_open;
				 }
				 
				 for(int i=start_index+1;i<end_index;i++)
		                intervals.remove(start_index+1);
				 
			 }
			 return;
		 }
	}
	
	
	//assume intervals1 is com with intervals2
	//6667, 6667
	//min 80, 80 max
	
public static boolean isExactCom(ArrayList<Con_Interval> intervals1, ArrayList<Con_Interval> intervals2){
		int index1 = 0;
		int index2 = 0;
		Collections.sort(intervals1);
		Collections.sort(intervals2);
		
		Con_Interval_Variable last_value = Con_Interval_Variable.MIN ;
		
		while(index1 < intervals1.size() && index2 < intervals2.size()){
			Con_Interval current1 = intervals1.get(index1);
			Con_Interval current2 = intervals2.get(index2);

			//current1.start < current2.start : current1.start = last_value && current.left_open = false
			if(current1.start.compareTo(current2.start) < 0){
				if(current1.start.compareTo(last_value) !=0 || current1.left_open)
					return false;
				last_value = current1.end;
				index1 ++;
			}else if(current1.start.compareTo(current2.start) > 0){
				if(current2.start.compareTo(last_value) !=0 || current2.left_open)
					return false;
				last_value = current2.end;
				index2++;
			}else{
				if(current1.end.compareTo(current2.end) > 0){
					if(current2.end.compareTo(last_value) != 0 || current2.right_open)
						return false;
					last_value = current2.end;
					index2++;
				}else{
					if(current1.end.compareTo(last_value) != 0 || current1.right_open)
						return false;
					last_value = current1.end;
					index1 ++;
				}
			}
			
			
			
		}//while
		
		//min 80, 80 max, 
		//80, 80 , 
		//min 40;  50 max
		//40, 50
		if(index1 < intervals1.size()){
			if(intervals1.get(index1).end.compareTo(Con_Interval_Variable.MAX) == 0)
				return true;
		}else if(index2 < intervals2.size()){
			if(intervals2.get(index2).end.compareTo(Con_Interval_Variable.MAX) == 0)
				return true;
		}

		return false;
	}
	


public static ArrayList<Con_Interval>  adjust_interval_intervals(Con_Interval new_interval, ArrayList<Con_Interval> intervals){
	
	  //add new_interval
	ArrayList<Con_Interval>  result = new ArrayList<>();
	
	 int start_index = 0;
	 int end_index = 0;
	 for(start_index =0; start_index<intervals.size(); start_index++){
		 //on the left of the new_interval, is no overlap, then continue, otherwise, break
		 int compare_start_end = new_interval.start.compareTo(intervals.get(start_index).end);
		 if(compare_start_end>0)
			 continue;
		 else if(compare_start_end == 0){
			 if(new_interval.left_open || intervals.get(start_index).right_open)
				 continue;
			 else {
				 break;
			 }
		 }else{
			 break;
		 }
	 }
	 
	
	 for(end_index = start_index; end_index<intervals.size(); end_index++){
		 //after then, is has overlap, then continue, otherwise, break
		 int compare_end_start = new_interval.end.compareTo(intervals.get(end_index).start);
		 if(compare_end_start>0){
			 continue;
		 }else if(compare_end_start == 0){
			 if(!new_interval.right_open && !intervals.get(end_index).left_open){
				 continue;
			 }else{
				 break;
			 }
		 }else{
			 break;
		 }
	 }
	 
	 if(start_index > end_index){
		 result.add(new_interval);
		 return result;		 
	 }else if(start_index == end_index){
		 new_interval.left_open = true;
		 new_interval.right_open = true;
		 result.add(new_interval);
		 return result;
	 }else{
		 
		 Con_Interval start_index_interval = intervals.get(start_index);
		 if(start_index_interval.start.compareTo(new_interval.start)>0){
			 int start_newend_compare = start_index_interval.start.compareTo(new_interval.end);
			 if(start_newend_compare < 0){
				 Con_Interval_Variable temp = new_interval.end;
				 new_interval.end = start_index_interval.start;
				 new_interval.right_open = true;
				 result.add(new_interval);
				 Con_Interval new2 = new Con_Interval(start_index_interval.end, temp, true, true);
				 result.add(new2);
				 return result;
			 }else if(start_newend_compare == 0){
				 if(start_index_interval.left_open || new_interval.right_open){
					 result.add(new_interval);
					 return result;
				 }else{
					 new_interval.right_open = true;
					 result.add(new_interval);
					 return result;
				 }
			 }else{
				 result.add(new_interval);
				 return result;
			 }
			 
		 }else if(start_index_interval.start.compareTo(new_interval.start) == 0){
			if(start_index_interval.left_open != new_interval.left_open){
				 new_interval.left_open = true;
				 result.add(new_interval);
				 return result;
			}else{
				int end_newend_compare = start_index_interval.end.compareTo(new_interval.end);
				if(end_newend_compare==0){
					if(new_interval.right_open || (!new_interval.right_open && !start_index_interval.right_open)){
						return result;
					}
				}else if(end_newend_compare>0){
					return result;
				}else {
					new_interval.start = start_index_interval.end;
					new_interval.left_open = true;
					result.add(new_interval);
					return result;
				}
			}
			
		 }else{
			 //start < new_interval.start
			 if(start_index_interval.end.compareTo(new_interval.end)>0){
				 return result;
			 }else if(start_index_interval.end.compareTo(new_interval.end)==0){
				 if(new_interval.right_open || !new_interval.right_open && !start_index_interval.right_open){
						return result;
					}
				 
			 }else{
				 new_interval.start = start_index_interval.end;
				 new_interval.left_open = true;
				 result.add(new_interval);
				 return result;
			 }
		 }
	 }
	return result;	 
		
}

public static Con_Intervals  removeOverlap(Con_Intervals cur, Con_Intervals ref){
	Con_Intervals new_intervals = new Con_Intervals();
	for(int i=0; i<cur.intervals.size(); i++){
		ArrayList<Con_Interval> new_items = adjust_interval_intervals(cur.intervals.get(i), ref.intervals);
		if(new_items != null && !new_items.isEmpty())
			new_intervals.intervals.addAll(new_items);
	}
	return new_intervals;
	
}
	
//at least one isbounded
public  boolean isBounded(double threshold){
	int count = 0;
	for(Con_Interval iv : intervals){
		if(iv.isBounded()){
			count ++;
			if(count >= threshold){
				return true;
			}
		}
	}
	return false;
}


//the output is in the range [0,1]
public static double overlappingIntervalValue(
		Con_Intervals list1, 
		Con_Intervals list2,
		double overlap_value){
	if(list1 == null || list2 == null){
		return -1;
	}
	if(list1.size() == 0){
		return 0;
	}
	Collections.sort(list1.intervals);
	Collections.sort(list2.intervals);
	double result = 0;
	int i = 0;

	double counter = 0; 
	
	for(i=0; i<list1.size(); i++){	
		Con_Interval interval1 = list1.intervals.get(i);
		int relation_interval1_list2 = relationship_interval_intervals(interval1, list2.intervals);
		if(relation_interval1_list2==3){//other
			result += overlap_value;
			counter ++;
		}else if(relation_interval1_list2 == 1){// no overlap
			result += 0;
		}else if(relation_interval1_list2 == 2){// is contained
			result += 1;
			counter++;
		}
	}
	//return result/(double)i;
	return result/counter;
}

public String toString(){
	String result = "";
	for(Con_Interval iv : intervals){
		result += iv.toString() + ";\n";
	}
	return result;
}
	
}
