import java.awt.datatransfer.StringSelection;
import java.awt.print.Printable;
import java.nio.MappedByteBuffer;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.omg.CORBA.PUBLIC_MEMBER;

import Util.Util_Date;
import Util.Util_String;
import Filtering.Con_Filtering;
import Filtering.Con_Filtering_Condition;
import Filtering.Con_Interval;
import Filtering.Con_Interval_Variable;
import Filtering.Con_Intervals;


public class test {
	enum modelEnum{
		MAX,
		MIN,
		dfjeiojfeijfe;
		
	}
	
	
	
	
	public static boolean XOR (boolean v1, boolean v2)
	{
		return !v1 && !v2 || (v1&&v2);
	}
	
	
	

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
    
		
	//	System.out.println(Con_Interval_Variable.isIPString("172.23"));
	//	System.out.println(Con_Interval_Variable.isIPString("172.23.102.2"));
	//	String str = "4/5/12 22:15";
	//	Date date = LogProcessing.CountEvery10Minute.getDateTime(str);

		
	//	System.out.println(date.toLocaleString());
	

		
	//	System.out.println(date.toLocaleString());
		
	//	String dateStop = "12/04/05 22:15";
	    SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm");
        
	    
	//	Date date2 = format.parse(dateStop);
	//	System.out.println(date2.toLocaleString());
	//	System.out.println(date.getTime()-date2.getTime());
		
	//	String condition1 = "DSTPORT = '21' AND Priority = 'warning' AND Operation = 'Deny' AND DSTIP = '10.32.5.51' AND SRCIP = '172.23.235.57'";
	//	String condition2 = "DSTPORT <> '6667' AND SRCIP > '172.23.0.0' AND DSTIP > '10.32.5.50'";
	/*
	    String condition1 = "SRCIP >= '10.32.5.50' AND SRCIP < '10.33.0.0'";
		String condition2 = "DSTIP >= '10.32.5.50' AND DSTIP < '10.33.0.0' AND SRCIP >= '172.23.0.0' AND SRCIP < '172.24.0.0'";
	    Con_Filtering filter1 = new Con_Filtering(1+"", 46+"", "Firewall", condition1);
		 Con_Filtering filter2 = new Con_Filtering(2+"", 47+"", "Firewall", condition2);
         // System.out.println(new_filtering.datasource +"-");
        int rela = Con_Filtering.relationship_ignore_datasource(filter1, filter2);
        int rela2=Con_Filtering.relationship_ignore_datasource(filter2, filter1);
        System.out.println("Filter1: "+condition1);
        System.out.println("Filter2: "+condition2);
        System.out.println("Relation between Filter1 and Fitler2: "+rela);
        System.out.println("Relation between Filter2 and Fitler1: "+rela2);
	*/	
     //   String s1="12";
      //  String s2="08";
      //  System.out.println("string compare: "+s1.compareTo(s2));
        
		
	/*
		Con_Interval interval1 = new Con_Interval("40", "60",(Boolean) true, (Boolean) true);
		Con_Interval interval2 = new Con_Interval("20", "80", (Boolean) true,(Boolean)  true);
		Con_Intervals intervals = new Con_Intervals();
		intervals.add_interval(interval1, "AND");
		int rela = Con_Intervals.relationship_interval_intervals(interval2, intervals.intervals);
		 System.out.println("Relation between interval2 and intervals: "+rela);
		
		 ArrayList<Integer> array1= new ArrayList<>();
		 array1.add(1);
		 array1.add(2);
		 ArrayList<Integer> array2= new ArrayList<>();
		 array2.add(3);
		 array2.add(4);
		 array1.addAll(array2);
		 System.out.println(array1.size());
		 */
        
		
        
 
		
		//Timestamp date2 = Timestamp.valueOf(" 2012-04-15 23:13:14");
		
		//Timestamp date3 = Timestamp.valueOf("4/5/2012 17:55");
		
		//double diff = Util_Date.diff(date2, date3);
		
		
		//System.out.println(diff);
	
		/*
		String string = "31082012";
		Date date = new SimpleDateFormat("ddMMyyyy").parse(string);
		System.out.println(date);
		
		String string1 = "4/5/2012 17:55";
		Date date1 = new SimpleDateFormat("M/d/yyyy HH:mm").parse(string1);
		System.out.println(date1);
		System.out.println(new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss").format(date1));
	
		
		//String string = "31/Aug/2012 23:06:12";
		//Date date = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss").parse(string);
		//System.out.println(date);
		
		/*
		
		String condition1 = "DSTIP = 172.23.0.* AND DSTPORT = 445";
		String condition2 = "PORT>60";
		LogProcessing.Connection connection = new LogProcessing.Connection();
		connection.set_by_Firewall("DSTIP = 172.23.0.* AND DSTPORT = 445");
				
		 Con_Filtering filter1 = new Con_Filtering(1+"", 46+"", "Firewall", condition1);
		 //Con_Filtering filter2 = new Con_Filtering(2+"", 47+"", "Firewall", condition2);
		 boolean satisfied = Con_Filtering_Condition.connection_isSatisfied_restrict(filter1.condition, connection);
		 System.out.println(satisfied);
		 /*
		 
		 Con_Filtering_Condition.adjustCondition_toRemoveOverlap(filter2.condition, filter1.condition);
		 Con_Filtering_Condition condition3 = filter2.condition;
		 System.out.println(condition3.conditions.get("PORT").intervals.get(0).start.toString());
		 System.out.println(condition3.conditions.get("PORT").intervals.get(0).end.toString());
		 System.out.println(condition3.conditions.get("PORT").intervals.get(1).start.toString());
		 System.out.println(condition3.conditions.get("PORT").intervals.get(1).end.toString());
		 
*/
	

		//Date date = new SimpleDateFormat("M/d/yyyy HH:mm").parse("4/6/2012 17:23");
		//System.out.print(date.toLocaleString());
		 
	    HashMap<String, String> hm = new  HashMap<String, String>();
	    hm.put("a", null);
	    hm.put("b",null);
	    for(String key : hm.keySet()){
	    		System.out.println(key + ":" + hm.get(key));
	    }
		 
		 
			
	}



}
