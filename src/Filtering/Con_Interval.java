package Filtering;
import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.stream.events.StartDocument;



public class Con_Interval implements Comparable<Con_Interval>{
	public Con_Interval_Variable  start;
	public Con_Interval_Variable end ;
	public Boolean left_open = true;
	public Boolean right_open = true;
	
	public Con_Interval(){
		start = Con_Interval_Variable.MIN;
		end = Con_Interval_Variable.MAX;
		
	}
	public Con_Interval(Con_Interval i){
		if(i != null){
			start = new Con_Interval_Variable(i.start);
			end = new Con_Interval_Variable(i.end);
			left_open = i.left_open;
			right_open = i.right_open;
		}
	}
	
	public Con_Interval(Con_Interval_Variable s, Con_Interval_Variable e, Boolean lo, Boolean ro) {
		start = s;
		end = e;
		left_open = lo;
		right_open = ro;
	}
	
	public boolean isBounded(){
		return !isEmptyInterval() 
				&& start.compareTo(Con_Interval_Variable.MIN) != 0
				&& end.compareTo(Con_Interval_Variable.MAX) != 0;
				
	}
	
	public String toString(){
		String l_bracket = "";
		String r_bracket = "";
		if(left_open){
			l_bracket = "(";
		}else{
			l_bracket = "[";
		}
		
		if(right_open){
			r_bracket = ")";
		}else{
			r_bracket = "]";
		}
		return l_bracket + start.toString() + "-" 
		+ end.toString() + r_bracket;
	}
	public boolean isEmptyInterval(){
		return start.variable.trim().equals("") && end.variable.trim().equals("");
	}
	
	public boolean notContainsStarSymbol(){
		return !start.variable.trim().contains("*") && !end.variable.trim().contains("*");
	}
	
	
	
	public Con_Interval(String s, String e, Boolean lo, Boolean ro) {
	
		this.start = new Con_Interval_Variable(s);
		this.end = new Con_Interval_Variable(e);
		this.left_open = lo;
		this.right_open = ro;
	}

    public Con_Interval(String str){
    	//parse a string into an interval
    }
    
    public static  ArrayList<Con_Interval> to_intervals(String op, String value){
    
    	
    	 ArrayList<Con_Interval> results = new ArrayList<Con_Interval>();
    	 
    		if(value.trim().isEmpty())
        		return results;
    	 
    	if (op.equals("=")){
    		Con_Interval new_interval = new Con_Interval(value, value, false, false);
    		results.add(new_interval);
        }
        else if(op.equals("<>")){
        	Con_Interval new_interval1 = new Con_Interval("MIN", value, false, true);
        	results.add(new_interval1);
        	Con_Interval new_interval2 = new Con_Interval(value, "MAX", true, false);
        	results.add(new_interval2);
        }
        else if (op.equals(">=")){
        	Con_Interval new_interval = new Con_Interval(value, "MAX", false, false);
        	results.add(new_interval);
        }
        else if (op.equals("<=")) {
        	Con_Interval new_interval = new Con_Interval("MIN", value, false, false);
        	results.add(new_interval);
        }
        else if(op.equals(">")) {
        	Con_Interval new_interval = new Con_Interval(value, "MAX", true, false);
        	results.add(new_interval);
        }
        
        else if(op.equals("<")) {
        	Con_Interval new_interval = new Con_Interval("MIN", value, false, true);
        	results.add(new_interval);
        }
    	return results;
        
    }


    

	@Override
	public int compareTo(Con_Interval interval2) {
		// TODO Auto-generated method stub
		return start.compareTo(interval2.start);
	}
	
	public boolean isEqual(Con_Interval interval){
		return (start.compareTo(interval.start)==0) && (end.compareTo(interval.end)==0)
				&& (left_open == interval.left_open )&& (right_open == interval.right_open);
	}
	
	public ArrayList<Con_Interval> AND_merge(Con_Interval interval)
	{
 		ArrayList<Con_Interval> results = new ArrayList<Con_Interval>();
		
	   
			   		   
		//list the relationships between the two intervals
		int end_end_Compareto = end.compareTo(interval.end);
		int start_start_Compareto = start.compareTo(interval.start);
		int end_start_Compareto = end.compareTo(interval.start);
		int start_end_Compareto = start.compareTo(interval.end);
				   

		if(end_start_Compareto < 0){
			return results;
					   
		}else if(end_start_Compareto == 0){
			if(!right_open && !interval.left_open){
				Con_Interval new_interval = new Con_Interval(
								   new Con_Interval_Variable(interval.start.toString()),
								   new Con_Interval_Variable(interval.start.toString()),
								   false, false);
				results.add(new_interval);
			}else{
				return results;
			}
			
		}else if(end_start_Compareto > 0 && end_end_Compareto < 0){
					   
			if(start_start_Compareto < 0){
				Con_Interval new_interval = new Con_Interval(
								   new Con_Interval_Variable(interval.start.toString()),
								   new Con_Interval_Variable(end.toString()),
								   interval.left_open, 
								   right_open);
				results.add(new_interval);			   
			}else if(start_start_Compareto == 0){
				Con_Interval new_interval = new Con_Interval(
						   		   new Con_Interval_Variable(start.toString()),
								   new Con_Interval_Variable(end.toString()),
								   left_open || interval.left_open, 
								   right_open);
				results.add(new_interval);			   
			}else if (start_start_Compareto > 0){
				Con_Interval new_interval = new Con_Interval(
								   new Con_Interval_Variable(start.toString()),
								   new Con_Interval_Variable(end.toString()),
								   left_open, 
								   right_open);
				results.add(new_interval);
			}
					   
		}else if(end_end_Compareto == 0){
			if(start_start_Compareto < 0){
				Con_Interval new_interval = new Con_Interval(
						   new Con_Interval_Variable(interval.start.toString()),
						   new Con_Interval_Variable(end.toString()),
						   interval.left_open, 
						   right_open || interval.right_open );
				results.add(new_interval);
			}else if (start_start_Compareto == 0){
				Con_Interval new_interval = new Con_Interval(
						   new Con_Interval_Variable(start.toString()),
						   new Con_Interval_Variable(end.toString()),
						   left_open || interval.left_open, 
						   right_open || interval.right_open );
				results.add(new_interval);	
			}else if(start_start_Compareto > 0){
				Con_Interval new_interval = new Con_Interval(
						   new Con_Interval_Variable(start.toString()),
						   new Con_Interval_Variable(end.toString()),
						   left_open, 
						   right_open || interval.right_open );
				results.add(new_interval);
			}
			
		}else if(end_end_Compareto > 0 ){
			
			if(start_start_Compareto < 0){
				Con_Interval new_interval = new Con_Interval(
						   new Con_Interval_Variable(interval.start.toString()),
						   new Con_Interval_Variable(interval.end.toString()),
						   interval.left_open, 
						   interval.right_open );
				results.add(new_interval);
			}else if (start_start_Compareto == 0){
				Con_Interval new_interval = new Con_Interval(
						   new Con_Interval_Variable(interval.start.toString()),
						   new Con_Interval_Variable(interval.end.toString()),
						   left_open || interval.left_open, 
						   interval.right_open );
				results.add(new_interval);	
			}else if(start_start_Compareto > 0 && start_end_Compareto < 0){
				Con_Interval new_interval = new Con_Interval(
						   new Con_Interval_Variable(start.toString()),
						   new Con_Interval_Variable(interval.end.toString()),
						   left_open, 
						   interval.right_open );
				results.add(new_interval);
			}else if(start_start_Compareto > 0 && start_end_Compareto == 0){
				if(!left_open && !interval.right_open){
					Con_Interval new_interval = new Con_Interval(
									   new Con_Interval_Variable(start.toString()),
									   new Con_Interval_Variable(start.toString()),
									   false, 
									   false);
					results.add(new_interval);
				}else{
					return results;
				}
			}else if(start_start_Compareto > 0 && start_end_Compareto > 0){
				return results;
			}
		}
		
			
			return results;
					   
		}
				   
			
	
	
}