package com.rmrdigitalmedia.esm.controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.rmrdigitalmedia.esm.Constants;

public class LogController {
	
	static Calendar cal = Calendar.getInstance();
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	static String today = sdf.format(cal.getTime()); 
	
	private static String logfilename = "ESM_LOG_" + today + ".txt";
	
	public static String logfile = 
			Constants.USER_DOCS_DIR + Constants.SEP + 
			Constants.DATA_DIR + Constants.SEP + 
			Constants.DATA_DIR_NAME + Constants.SEP + 
			Constants.LOG_DIR + Constants.SEP +
			logfilename;
	
	public static void log(Object o) {
		@SuppressWarnings("deprecation")
		String t = new java.util.Date().toGMTString();
		String msg = "["+t+"]\tINFO\t" + o.toString();
		try {
			write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}	  
	}
	
  public static void logEvent(Object c, int severity, Object o) {
	  // need to handle severity
	  String classname =c.getClass().getName();	  
		if (severity > 0) {
			@SuppressWarnings("deprecation")
			String t = new java.util.Date().toGMTString();
			String i = (severity > 1) ? "ERROR\t" : "NOTICE\t";
			String msg = "["+t+"]\t" + i + classname + "\t" + o.toString();
			try {
				write(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
  }

  public static void logEvent(Object c, int severity, Exception e) {
	  // need to handle severity
	  String classname =c.getClass().getName();	  
		if (severity > 0) {
			@SuppressWarnings("deprecation")
			String t = new java.util.Date().toGMTString();
			String i = (severity > 1) ? "ERROR\t" : "NOTICE\t";
			String msg = "\n["+t+"]\t" + i + classname + "\t" + e.getMessage()+"\n";
			try {
				write(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//e.printStackTrace();
		}
  }

  public static void logEvent(Object c, int severity, String _msg, Exception e) {
	  // need to handle severity
	  String classname =c.getClass().getName();	  
		if (severity > 0) {
			@SuppressWarnings("deprecation")
			String t = new java.util.Date().toGMTString();
			String i = (severity > 1) ? "ERROR\t" : "NOTICE\t";
			String msg = "\n["+t+"]\t" + i + classname + "\t" + _msg + "\t" + e.getMessage()+"\n";
			try {
				write(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//e.printStackTrace();
		}
  }  
  
  public static void write(String msg) throws IOException {  	
		FileWriter fstream = new FileWriter(logfile, true);
		BufferedWriter out = new BufferedWriter(fstream); 
		out.write(msg);
		out.newLine();
		out.close();  
		System.out.println(msg); //DEBUG
  }
  
}
