package Filtering;
import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Util.Util_String;


public class Con_Interval_Variable implements Comparable<Con_Interval_Variable> {
	
	public String variable = "";
	public static final Con_Interval_Variable MIN = new Con_Interval_Variable("MIN");
	public static final Con_Interval_Variable MAX = new Con_Interval_Variable("MAX");
	
	public  Con_Interval_Variable(String str){
		variable = str.toLowerCase();
	}
	
	public  Con_Interval_Variable(Con_Interval_Variable v){
		variable = v.variable;
	}
	
	//"172.23.4.5(v2)  is consistent with 172.23.*(v1)
		public static boolean consistentIPs(Con_Interval_Variable v1, Con_Interval_Variable v2){
			String[] array1 = v1.variable.split("\\.");
			String[] array2 = v2.variable.split("\\.");
			
			int j=0;
			for(int i=0; i<array1.length; i++){
				String s1 = Util_String.cleanValue(array1[i]);
				
				if(s1.equals("*"))
					break;
				if(s1.equals(""))
					continue;
				
				if(j>=array2.length)
					return false;
				
				String s2 = Util_String.cleanValue(array2[j]);
				j++;
				
				if(!s1.equals(s2))
						return false;
				
			}
			return true;
						
		}
		
	public String toString()
	{
		return variable;
	}
	
	public static boolean isIPString(String str){
		Pattern ptn = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})");
        Matcher mtch = ptn.matcher(str);
        return mtch.find();
	}
	
	@Override
	public int compareTo(Con_Interval_Variable v2)
	{
	
			String vv1 = variable;
			String vv2 = v2.variable;
			
			if(vv1.equals(MIN.toString()) && !vv2.equals(MIN.toString())){
				return -1;
			}else if (vv1.equals(MIN.toString()) && vv2.equals(MIN.toString())){
				return 0;
			}else if(vv1.equals(MAX.toString()) && !vv2.equals(MAX.toString())){
				return 1;
			}else if(vv1.equals(MAX.toString()) && vv2.equals(MAX.toString())){
				return 0;
			}else if(!vv1.equals(MAX.toString()) && vv2.equals(MAX.toString())){
				return -1;
			}else if(!vv1.equals(MIN.toString()) && vv2.equals(MIN.toString())){
				return 1;
			}else {
				if(isIPString(vv1) && isIPString(vv2)){
					if( consistentIPs(this, v2))
						return 0;
				}
				return vv1.compareTo(vv2);
			}

	}
	
	

	

}
