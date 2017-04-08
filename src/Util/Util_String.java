package Util;

import java.util.LinkedList;
import java.util.Random;

public class Util_String {
	
	
	public static final String COMMA_DELIMITER = ",";
	public static final String NEW_LINE_SEPARATOR = "\n";
	
	
	public static String getID(){
		Random rand = new Random();
		return System.currentTimeMillis()+rand.nextInt()+"";
	}
	
	public static String getPair(String s1, String s2){
		String item1="", item2="";
		if(s1.compareTo(s2)<=0){
			item1=s1;
			item2=s2;
		}else{
			item1=s2;
			item2=s1;
		}
		return item1+"-"+item2;
	}
	
	public static String cutIPStr(String original){
		/*
		// 10.32.0.0 --> 10.32.0.*
		String[] array = original.split("\\.");
		if(array.length >= 3){
			return array[0]+"."+array[1]+"."+array[2]+".*";
		}
		else
			 return original;
			 */
		return original;
	}
	
	public static boolean equalIP(String val1, String val2){
		String[] array1 = val1.split("\\.");
		String[] array2 = val2.split("\\.");
		
		if(array1.length >= 3 && array2.length >= 3){
			return array1[0] == array2[0] && array1[1] == array2[1] && array1[2] == array2[2];
		}
		return false;
	}
	
	public static String cleanValue(String value)
    {
        return value.trim().replace("\'", "").replace("\"", "");
    }
	
	public static String getConnectionID (String filepath, String index){
		return filepath+"#"+index;
	}
	
	public static void mergeList(LinkedList<String> list1, LinkedList<String> list2){
		for(int i=0; i<list2.size(); i++){
			String item2 = list2.get(i);
			if(!list1.contains(item2))
				list1.add(item2);
		}
	}

}
