package demo.test;

import java.text.SimpleDateFormat;

public class Test {

	public static String showTime(){
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String TimeString = time.format(new java.util.Date());
	//	System.out.println(TimeString);
		
		return TimeString;
	}
	
	public static void main(String[] args) {
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String TimeString = time.format(new java.util.Date());
		System.out.println(TimeString);
	}
}
