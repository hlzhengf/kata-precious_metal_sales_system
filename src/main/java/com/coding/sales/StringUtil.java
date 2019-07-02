package com.coding.sales;

public class StringUtil {
	public static boolean isNullOrEmpty(String str){
		if(null==str || "".equals(str)) return true;
		else return false;
	}
}
