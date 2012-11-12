package aic12.project3.importer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class dateParseTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		  String date  = "Fri Jul 29 15:26:36 +0000 2011";
		
		  final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		  SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
		  sf.setLenient(true);
		  try {
			System.out.println(sf.parse(date));
			Calendar cal = sf.getCalendar();
			System.out.println(cal.getTime().getMonth());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  

	}

}
