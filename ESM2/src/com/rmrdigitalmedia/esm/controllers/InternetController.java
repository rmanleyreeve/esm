package com.rmrdigitalmedia.esm.controllers;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.dialogs.UpdateDialog;

@SuppressWarnings("unused")
public class InternetController {

	private static Object me = new InternetController();

	public InternetController() {
	}

	public static boolean checkNetAccess() {
		boolean online = false;

		/*
		 * Enumeration<NetworkInterface> interfaces = null; try { interfaces =
		 * NetworkInterface.getNetworkInterfaces(); } catch (SocketException e)
		 * { e.printStackTrace(); } while (interfaces.hasMoreElements()) {
		 * NetworkInterface interf = interfaces.nextElement(); try { if
		 * (interf.isUp() && !interf.isLoopback()) online = true; } catch
		 * (SocketException e) { e.printStackTrace(); } }
		 */

		try {
			if ("127.0.0.1".equals(InetAddress.getLocalHost().getHostAddress().toString()) == false) {
				online = true;
			}
		} catch (UnknownHostException e) {
			LogController.logEvent(me, C.NOTICE, e);
		}
		try {
			EsmApplication.appData.setField("ONLINE", online);
		} catch (Exception e) {
			//
		}
		LogController.log("NET ACCESS = " + online);
		return online;
	}

	public static void getUpdates() throws IOException {
		String currentVersion = (String) EsmApplication.appData.getField("VERSION");
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
		if (Integer.parseInt(lvarr[0]) > Integer.parseInt(cvarr[0])
				|| Integer.parseInt(lvarr[1]) > Integer.parseInt(cvarr[1])
				|| Integer.parseInt(lvarr[2]) > Integer.parseInt(cvarr[2])) {
			// new version available
			LogController.log("NEW VERSION AVAILABLE: " + latestVersion);
			// EsmApplication.alert(C.NEW_VERSION_ALERT);
			UpdateDialog ud = new UpdateDialog(latestVersion);
		} else {
			LogController.log("No updates found");
		}
	}

	public static void open(String url) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (Exception e1) {
				LogController.logEvent(me, C.WARNING, "Error loading URL: " + url, e1);
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
		// print result
		renderHTML(response.toString());
	}

	public static void renderHTML(String html) {
		// http://dataurl.net/#dataurlmaker - use this for images
		Shell shell = new Shell(Display.getDefault());
		shell.setLayout(new FillLayout());
		shell.setText(C.HELP_TITLE);
		shell.setImages(new Image[] { C.getImage(C.APP_ICON_16), C.getImage(C.APP_ICON_32) }); // 16x16 & 32x32		
		Browser browser;
		try {
			browser = new Browser(shell, SWT.NONE);
		} catch (SWTError e) {
			LogController.logEvent(InternetController.class, C.ERROR, "Could not instantiate Browser" + e.getMessage());
			shell.dispose();
			return;
		}
		browser.setText(html);		
		// set size
		Monitor primary = Display.getCurrent().getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		shell.setSize(700, bounds.height-100); 
		shell.setLocation (20, 20);				
		shell.open();
		while (!shell.isDisposed()) {
			if (!Display.getDefault().readAndDispatch())
				Display.getDefault().sleep();
		}
	}

	public static boolean uploadFileFTP(String filePath, String fileName) {
		//netsh advfirewall set global StatefulFTP disable
		boolean ok = false; 
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(C.FTP_SERVER, 21);
			ftpClient.login(C.FTP_USER, C.FTP_PASS);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setUseEPSVwithIPv4(true);
			InputStream inputStream = new FileInputStream(filePath);
			LogController.log("Upload started...");
			boolean done = ftpClient.storeFile(fileName, inputStream);
			inputStream.close();
			if (done) {
				LogController.log("Upload completed.");
				ok = true;
			}
		} catch (IOException ex) {
			LogController.logEvent(me, C.FATAL, ex);
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				LogController.logEvent(me, C.FATAL, ex);
			}
		}
		return ok;
	}





}
