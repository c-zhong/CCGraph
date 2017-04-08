package Util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util_Date {
	

	
	public static Date StringtoDate(String firewall_ids, String str) throws ParseException{
		
		Date date = null;
		
		if(firewall_ids.toLowerCase().equals("firewall")){
			//06/Apr/2012 17:40:11
			date =   new SimpleDateFormat( "dd/MMM/yyyy HH:mm:ss" ).parse(str);
			return date;
		}else if(firewall_ids.toLowerCase().equals("ids")){
			 date = new SimpleDateFormat("M/d/yyyy HH:mm").parse(str);
		}
		return date;
	}
	
	//time difference in terms of second
	public static double diff(Date date1, Date date2){
		long diff = date1.getTime() - date2.getTime();
		return diff/1000;
	}

}
