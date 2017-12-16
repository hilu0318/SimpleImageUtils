package utils;

import java.awt.Color;
import java.util.Date;

public class Log {

	private static Date today;
	
	public static void i(String str){
		today = new Date();
		System.out.println(today.toString() +" :::  "+ str);
	}
	
	public static void i(int str){
		System.out.println(today.toString() +" :::  "+str);
	}
}
