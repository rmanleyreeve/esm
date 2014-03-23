package com.rmrdigitalmedia.esm;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.SWTResourceManager;

public final class Constants {
	
	// app setup properties
	public static String OS = (SWT.getPlatform());
	public static String HOME_DIR = System.getProperty("user.home");	
	public static String DATA_DIR = (OS.equals("cocoa")) ? "Shared":"All Users";
	public static String SEP = (OS.equals("cocoa")) ? "/":"\\";;
	public static String DATA_DIR_NAME = "ESM Data";
	public static String IMG_DIR = "images";
	public static String DOC_DIR = "docs";
	public static String LOG_DIR = "logs";
	public static String USER_DOCS_DIR = new File(HOME_DIR).getParentFile().getAbsolutePath();

	public static String DB_NAME = "ESM";
	public static String DB_CONN_STR = "jdbc:h2:~/../"+DATA_DIR+"/" + DATA_DIR_NAME + "/"+DB_NAME+";IFEXISTS=TRUE";
	public static String DB_CONN_STR_SETUP = "jdbc:h2:~/../"+DATA_DIR+"/" + DATA_DIR_NAME + "/" + DB_NAME;
	public static String DB_SETUP_FILE = "SETUP.sql";
	public static String APP_NAME = "Enclosed Spaces Management System";
	
	// styling properties
	public static Color APP_BGCOLOR = SWTResourceManager.getColor(213, 217, 220);
	public static Color TITLEBAR_BGCOLOR = SWTResourceManager.getColor(102, 111, 118);
	
	public static String FONT = (OS.equals("cocoa")) ? "Lucida Grande" : "Arial";
	private static int FONT_ADD = (OS.equals("cocoa")) ? 2 : 0;
	public static Font BUTTON_FONT = SWTResourceManager.getFont(FONT, 9+FONT_ADD,SWT.NORMAL);
	public static Font HEADER_FONT = SWTResourceManager.getFont(FONT, 14+FONT_ADD,SWT.NORMAL);
	public static Font FORM_HEADER_FONT = SWTResourceManager.getFont(FONT, 10+FONT_ADD,SWT.BOLD);
	public static Font FONT_8 = SWTResourceManager.getFont(FONT, 8+FONT_ADD,SWT.NORMAL);
	public static Font FONT_8B = SWTResourceManager.getFont(FONT, 8+FONT_ADD,SWT.BOLD);
	public static Font FONT_9 = SWTResourceManager.getFont(FONT, 9+FONT_ADD, SWT.NORMAL);
	public static Font FONT_9B = SWTResourceManager.getFont(FONT, 9+FONT_ADD, SWT.BOLD);
	public static Font FONT_10 = SWTResourceManager.getFont(FONT, 10+FONT_ADD,SWT.NORMAL);
	public static Font FONT_10B = SWTResourceManager.getFont(FONT, 10+FONT_ADD,SWT.BOLD);
	public static Font FONT_12 = SWTResourceManager.getFont(FONT, 12+FONT_ADD,SWT.NORMAL);
	public static Font FONT_12B = SWTResourceManager.getFont(FONT, 12+FONT_ADD,SWT.BOLD);
	
	
	// web properties
	public static String USER_AGENT = "Mozilla/5.0";
	public static String REMOTE_URL = "http://www.rmrdigitalmedia.co.uk/esm/";
	public static String LICENSE_URL = REMOTE_URL + "check_license.php";
	public static String LATEST_VERSION_URL = REMOTE_URL + "version.txt";
	public static String UPDATE_URL = REMOTE_URL + "updates.php";
	
	// message properties
	public static String NEW_VERSION_ALERT = "New version available!";
	public static String EXIT_MSG = "***** PROGRAM EXIT ****\n\n\n";
	public static String LOGIN_FAIL_MSG = "Username or Password not recognised.\nPlease try again.";
	
	// app screen titles
	public static String SPACES_LIST_TITLE = "Classified Enclosed Spaces";
	public static String ADMIN_PAGE_TITLE = "ESM System Administration";

	
	public static Image getImage(String imgpath) {
		return SWTResourceManager.getImage(Constants.class, imgpath);	
	}
	
	
	private Constants() {
		throw new AssertionError();
	}
}
