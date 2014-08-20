package com.rmrdigitalmedia.esm.controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.rmrdigitalmedia.esm.C;

public class LogController {

	static Calendar cal = Calendar.getInstance();
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	static String today = sdf.format(cal.getTime());
	static String[] errNames = { "", "NOTICE", "WARNING", "ERROR", "FATAL" };

	private static String logfilename = "ESM_LOG_" + today + ".txt";

	public static String logfile = C.USER_DOCS_DIR + C.SEP
			+ C.INSTALL_DIR + C.SEP
			+ C.DATA_DIR_NAME + C.SEP
			+ C.LOG_DIR_NAME + C.SEP
			+ logfilename;

	public static void log(Object o) {
		@SuppressWarnings("deprecation")
		String t = new java.util.Date().toGMTString();
		String msg = "[" + t + "]\tINFO\t" + o.toString();
		try {
			write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logEvent(Object c, int severity, Object o) {
		// need to handle severity
		String classname = c.getClass().getName();
		if (severity > 0) {
			@SuppressWarnings("deprecation")
			String t = new java.util.Date().toGMTString();
			String e = errNames[severity];
			String msg = "[" + t + "]\t" + e + "\t" + classname + "\t" + o.toString();
			try {
				write(msg);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void logEvent(Object c, int severity, Exception ex) {
		// need to handle severity
		String classname = c.getClass().getName();
		if (severity > 0) {
			@SuppressWarnings("deprecation")
			String t = new java.util.Date().toGMTString();
			String e = errNames[severity];
			String msg = "\n[" + t + "]\t" + e + "\t" + classname + "\t" + ex.getMessage() + "\n";
			try {
				write(msg);
				if (severity > 3) {
					ex.printStackTrace();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void logEvent(Object c, int severity, String _msg, Exception ex) {
		// need to handle severity
		String classname = c.getClass().getName();
		if (severity > 0) {
			@SuppressWarnings("deprecation")
			String t = new java.util.Date().toGMTString();
			String e = errNames[severity];
			String msg = "\n[" + t + "]\t" + e + "\t" + classname + "\t" + _msg + "\t" + ex.getMessage() + "\n";
			try {
				write(msg);
				if (severity > 3) {
					ex.printStackTrace();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void write(String msg) throws IOException {
		try {
			FileWriter fstream = new FileWriter(logfile, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(msg);
			out.newLine();
			out.close();
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		System.out.println(msg); 

	}

}
