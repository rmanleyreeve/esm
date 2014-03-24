package com.rmrdigitalmedia.esm.controllers;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;

@SuppressWarnings("unused")
public class InternetController {
	
	private static Object me = new InternetController();

	public InternetController() {
	}

	public static boolean checkNetAccess() {
		boolean online = false;
				
		/*
		Enumeration<NetworkInterface> interfaces = null;		
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		while (interfaces.hasMoreElements()) {
		  NetworkInterface interf = interfaces.nextElement();
		  try {
				if (interf.isUp() && !interf.isLoopback())
				  online = true;
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
		*/
		
		try {
			if ("127.0.0.1".equals(InetAddress.getLocalHost().getHostAddress().toString()) == false) {
				online = true;
			}
		} catch (UnknownHostException e) {
			LogController.logEvent(me, 2, e);
		}
		EsmApplication.appData.setField("ONLINE", online);
		LogController.log("NET ACCESS = "+online);
		return online;
	}
	
	public static boolean verifyLicense(String key) throws Exception {	 
		URL obj = new URL(C.LICENSE_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();	 
		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");	 
		String urlParameters = "key=" + key;	 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();	 
		int responseCode = con.getResponseCode();
		LogController.log("Sending 'POST' request to URL : " + C.LICENSE_URL);
		LogController.log("Post parameters : " + urlParameters);
		LogController.log("Response Code : " + responseCode);	 
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();	 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();	 
		//print result
		LogController.log("Response: " + response.toString());	
		return (response.toString().equals("OK"));
	}

	public static void getUpdates() throws IOException {		
		String currentVersion = (String)EsmApplication.appData.getField("VERSION");
		URL u = new URL(C.LATEST_VERSION_URL);
		InputStream is = u.openStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String inputLine;
		StringBuffer response = new StringBuffer();	 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		String latestVersion = response.toString();
		// compare versions
		String[] lvarr = latestVersion.split("\\.");
		String[] cvarr = currentVersion.split("\\.");
		if(
			Integer.parseInt(lvarr[0]) > Integer.parseInt(cvarr[0]) ||
			Integer.parseInt(lvarr[1]) > Integer.parseInt(cvarr[1]) ||
			Integer.parseInt(lvarr[2]) > Integer.parseInt(cvarr[2])
		) {
			// new version available
			LogController.log("NEW VERSION AVAILABLE: " + latestVersion);
			//open license key dialog
			//EsmApplication.alert(C.NEW_VERSION_ALERT);
			UpdateController ud = new UpdateController(latestVersion);
		}		
	}	
	
	public static void open(String url) {
		if(Desktop.isDesktopSupported()){
		  try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (Exception e1) {
				LogController.logEvent(me,2,"Error loading URL: "+ url,e1);
				
			}
		}	
	}

	public static void doGet(String url) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", C.USER_AGENT); 
		int responseCode = con.getResponseCode();
		LogController.log("\nSending 'GET' request to URL : " + url);
		LogController.log("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer(); 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		//print result
		renderHTML(response.toString());	 
	}		
	
	public static void renderHTML(String html) {
		Shell shell = new Shell(Display.getDefault());
		shell.setLayout(new FillLayout());
		Browser browser;
		try {
			browser = new Browser(shell, SWT.NONE);
		} catch (SWTError e) {
			LogController.logEvent(new InternetController(),2,"Could not instantiate Browser" + e.getMessage());
			shell.dispose();
			return;
		}
		browser.setText(html);
		shell.open();
		while (!shell.isDisposed()) {
			if (!Display.getDefault().readAndDispatch())
				Display.getDefault().sleep();
		}
		//display.dispose();
	}

	
}
