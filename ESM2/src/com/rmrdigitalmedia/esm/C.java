package com.rmrdigitalmedia.esm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.common.collect.ImmutableMap;

public final class C {

	// system properties
	public static String OS = System.getProperty("os.name");
	public static String ARCHITECTURE =  
	OS.toLowerCase().contains("win") ? System.getenv("PROCESSOR_ARCHITECTURE") != null && System.getenv("PROCESSOR_ARCHITECTURE").endsWith("64") || 
	System.getenv("PROCESSOR_ARCHITEW6432") != null && System.getenv("PROCESSOR_ARCHITEW6432").endsWith("64") ? "64-bit" : "32-bit" :
	OS.toLowerCase().contains("mac") ? System.getProperty("os.arch") :
	"";
	public static String JVM = System.getProperty("java.vm.version");
	public static String JVM_ARCHITECTURE = System.getProperty("sun.arch.data.model") + "-bit";		

	// app setup properties
	public static String PLATFORM = (SWT.getPlatform());
	public static String HOME_DIR = System.getProperty("user.home");
	public static String INSTALL_DIR = (PLATFORM.equals("cocoa")) ? "Shared" : "All Users";
	public static String SEP = (PLATFORM.equals("cocoa")) ? "/" : "\\";;
	public static String DATA_DIR_NAME = "ESM Data";
	public static String DOC_DIR_NAME = "docs";
	public static String LOG_DIR_NAME = "logs";
	public static String TMP_DIR_NAME = "temp";
	public static String USER_DOCS_DIR = new File(HOME_DIR).getParentFile().getAbsolutePath();
	public static String DATA_DIR = USER_DOCS_DIR + SEP + INSTALL_DIR + SEP + DATA_DIR_NAME;
	public static String DOC_DIR = DATA_DIR + SEP + DOC_DIR_NAME;
	public static String LOG_DIR = DATA_DIR + SEP + LOG_DIR_NAME;
	public static String TMP_DIR = DATA_DIR + SEP + TMP_DIR_NAME;
	public static String SALT = "RMRtheKing";
	public static int SPLASH_DEFAULT_WIDTH = 500;
	public static int SPLASH_DEFAULT_HEIGHT = 250;
	

	// database properties
	public static String DB_NAME = "ESM";
	public static String DB_CONN_STR = "jdbc:h2:~/../" + INSTALL_DIR + "/" + DATA_DIR_NAME + "/" + DB_NAME + ";MODE=MYSQL;IFEXISTS=TRUE;CACHE_SIZE=200000";
	public static String DB_CONN_STR_SETUP = "jdbc:h2:~/../" + INSTALL_DIR + "/" + DATA_DIR_NAME + "/" + DB_NAME + ";MODE=MYSQL";
	public static String DB_SETUP_FILE = "SETUP.sql";

	// image properties
	public static int IMG_WIDTH = 800;
	public static int IMG_HEIGHT = 600;
	public static int THUMB_WIDTH = 125;
	public static int THUMB_HEIGHT = 125;

	// pdf properties
	public static String BLANK_SPACE_FORM = "blank_space_form.pdf";
	public static String BLANK_ENTRY_FORM = "blank_entry_form.pdf";

	// UI images
	public static String SPLASH_IMAGE = "splash.jpg";
	public static String APP_ICON_16 = "appicon16.png";
	public static String APP_ICON_32 = "appicon32.png";

	// UI style properties
	public static Color APP_BGCOLOR = SWTResourceManager.getColor(222, 224, 226); // #DEE0E2
	public static Color TITLEBAR_BGCOLOR = SWTResourceManager.getColor(122, 130, 137); // #7A8289
	public static Color FIELD_BGCOLOR = SWTResourceManager.getColor(238, 238, 238); // #EEEEEE
	public static Color BAR_BGCOLOR = SWTResourceManager.getColor(182, 186, 190); // #B6BABE
	public static Color AUDIT_COLHEADER_BGCOLOR = SWTResourceManager.getColor(38, 147, 255); // #2693FF
	public static Color RED = SWTResourceManager.getColor(237, 28, 36); // #ED1C24
	public static Color AMBER = SWTResourceManager.getColor(241, 89, 42); // #F1592A
	public static Color GREEN = SWTResourceManager.getColor(11, 148, 68); // #0B9444
	public static Color NULL = SWTResourceManager.getColor(100, 100, 100); // #646464
	public static Map<String, Color> TRAFFICLIGHTS = ImmutableMap.of("null", C.NULL, "red", C.RED, "amber", C.AMBER, "green", C.GREEN);
	public static Color ROW_HIGHLIGHT = SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND);
	public static Color ROW_SELECTED = SWTResourceManager.getColor(165, 200, 250); // #A5C8FA

	// UI fonts
	public static String FONT = (PLATFORM.equals("cocoa")) ? "Lucida Grande" : "Arial";
	private static int FONT_ADD = (PLATFORM.equals("cocoa")) ? 2 : 0;
	public static Font BUTTON_FONT = SWTResourceManager.getFont(FONT, 9 + FONT_ADD, SWT.NORMAL);
	public static Font HEADER_FONT = SWTResourceManager.getFont(FONT, 12 + FONT_ADD, SWT.NORMAL);
	public static Font FORM_HEADER_FONT = SWTResourceManager.getFont(FONT, 10 + FONT_ADD, SWT.BOLD);
	public static Font FONT_7 = SWTResourceManager.getFont(FONT, 7 + FONT_ADD, SWT.NORMAL);
	public static Font FONT_8 = SWTResourceManager.getFont(FONT, 8 + FONT_ADD, SWT.NORMAL);
	public static Font FONT_8B = SWTResourceManager.getFont(FONT, 8 + FONT_ADD, SWT.BOLD);
	public static Font FONT_8i = SWTResourceManager.getFont(FONT, 8 + FONT_ADD, SWT.ITALIC);
	public static Font FONT_9 = SWTResourceManager.getFont(FONT, 9 + FONT_ADD, SWT.NORMAL);
	public static Font FONT_9i = SWTResourceManager.getFont(FONT, 9 + FONT_ADD, SWT.ITALIC);
	public static Font FONT_9B = SWTResourceManager.getFont(FONT, 9 + FONT_ADD, SWT.BOLD);
	public static Font FONT_10 = SWTResourceManager.getFont(FONT, 10 + FONT_ADD, SWT.NORMAL);
	public static Font FONT_10B = SWTResourceManager.getFont(FONT, 10 + FONT_ADD, SWT.BOLD);
	public static Font FONT_11 = SWTResourceManager.getFont(FONT, 11 + FONT_ADD, SWT.NORMAL);
	public static Font FONT_11B = SWTResourceManager.getFont(FONT, 11 + FONT_ADD, SWT.BOLD);
	public static Font FONT_12 = SWTResourceManager.getFont(FONT, 12 + FONT_ADD, SWT.NORMAL);
	public static Font FONT_12B = SWTResourceManager.getFont(FONT, 12 + FONT_ADD, SWT.BOLD);
	public static Font FONT_ALERT_TITLE = SWTResourceManager.getFont(FONT, 18 + FONT_ADD, SWT.BOLD);

	// internet properties
	public static String USER_AGENT = "Mozilla/5.0";
	public static String REMOTE_URL = "http://www.esm-system.com/";
	public static String LATEST_VERSION_URL = REMOTE_URL + "version.txt";
	public static String UPDATE_URL = REMOTE_URL + "updates.php";
	public static String FTP_SERVER = "213.171.193.5";
	public static String FTP_USER = "softwareconn";
	public static String FTP_PASS = "f1letransfer@001";

	// message text
	public static String NEW_VERSION_ALERT = "New version available!";
	public static String EXIT_MSG = "***** PROGRAM EXIT ****\n\n\n";
	public static String LOGIN_FAIL_MSG = "Username or Password not recognised.\nIf you have forgotten your username or password, please contact your administrator.";
	public static String SPACE_ALERT_RED = "The space you have selected has been classified as RED.\nBe extra cautious as you proceed.";
	public static String SPACE_NOT_AUTH = "This space has NOT been authorized.";
	public static String COPYRIGHT = "   \u00a9Videotel 2014";
	public static String SIGNOFF_REVOKE_MESSAGE = "This audit was previously signed off.\nAs you have made changes to the audit, it will need to be signed off again.";
	public static String SPACE_AUDIT_Q7_ALERT = "This is a potentially dangerous situation, please make sure that you understand why and how this can affect the condition of the space. "
			+ "Read the hint that accompanies this question for more information and if necessary consult the officer in charge";
	public static String SPACE_AUDIT_Q8_ALERT = "This is a potentially dangerous situation, please make sure that you understand why and how this can affect the condition of the space. "
			+ "Read the hint that accompanies this question for more information and if necessary consult the officer in charge";
	public static String DISCLAIMER = "This program is intended to reflect the best available "
			+ "techniques and practices at the time of production. It is intended purely as comment. "
			+ "No responsibility is accepted by Videotel, or by any firm, corporation or organisation "
			+ "who or which has been in any way concerned with the production, supply or sale of this program "
			+ "for accuracy of any information given hereon or for any omission here from.";
	public static final String FIRST_LOGIN_TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
			+ "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. "
			+ "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. "
			+ "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
	public static final String USER_AGREEMENT_TITLE = "ESM Application End User Terms and Conditions";
	public static final String LOG_OUT_MSG = "Are you sure you wish to log out of the ESM System?";

	// app screen titles
	public static String APP_NAME = "Enclosed Spaces Management System";
	public static String SPACES_LIST_TITLE = "Classified Enclosed Spaces";
	public static String ADMIN_PAGE_TITLE = "ESM System Administration";
	public static String SPACE_AUDIT_CHECKLIST_PAGE_TITLE = "Internal Space Audit: Checklist";
	public static String ENTRY_AUDIT_CHECKLIST_PAGE_TITLE = "Entry Point Audit: Checklist";
	public static String SPACE_AUDIT_CLASSIFICATION_PAGE_TITLE = "Internal Space Audit: Classification Questions";
	public static String ENTRY_AUDIT_CLASSIFICATION_PAGE_TITLE = "Entry Point Audit: Classification Questions";
	public static String HINT_TITLE = "Hint";
	public static String SHELL_TITLE = "Videotel ESM";
	public static String ALERT_TITLE = "ESM Alert";
	public static String SETUP_TITLE = "Videotel ESM Setup";
	public static String HELP_TITLE = "Videotel ESM Help";

	// error severity
	public static int NOTICE = 1;
	public static int WARNING = 2;
	public static int ERROR = 3;
	public static int FATAL = 4;

	// help file names
	public static String HELPFILE_ADMIN = "administration_help.html";
	public static String HELPFILE_SPACESLIST = "spaces_list_help.html";
	public static String HELPFILE_SPACEDETAIL = "space_detail_help.html";
	public static String HELPFILE_SPACE_AUDIT_CHECK = "audit_help.html";
	public static String HELPFILE_SPACE_AUDIT_CLASS = "audit_help.html";
	public static String HELPFILE_EP_AUDIT_CHECK = "audit_help.html";
	public static String HELPFILE_EP_AUDIT_CLASS = "audit_help.html";
	public static String HELPFILE_GENERIC = "help.html";

	// PDF report properties
	public static String SPACE_OVERALL_STATUS_MSG_RED = "It is advised that extreme caution is observed when using this space.";
	public static String SPACE_OVERALL_STATUS_MSG_AMBER = "It is advised that caution is observed when using this space.";
	public static String SPACE_OVERALL_STATUS_MSG_GREEN = "There are no significant hazards reported for this space.";
	public static String ENTRY_OVERALL_STATUS_MSG_RED = "It is advised that extreme caution is observed when using this entry point.";
	public static String ENTRY_OVERALL_STATUS_MSG_AMBER = "It is advised that extreme caution is observed when using this entry point.";
	public static String ENTRY_OVERALL_STATUS_MSG_GREEN = "There are no significant hazards reported for this entry point.";

	private C() {
		throw new AssertionError();
	}



	// public utility methods ======================================================================

	public static void sop(Object s) {
		System.out.println(s.toString());
	}

	public static Image getExtImage(String imgpath) {
		// test for non-image or system files
		try {
			BufferedImage image = ImageIO.read(new File(imgpath));
			if (image == null) {
				return null;
			}
		} catch (IOException ex) {
			return null;
		}
		return SWTResourceManager.getImage(imgpath);
	}

	public static Image progressImage(int progress) {
		return C.getImage("Percent_" + progress + ".png");
	}

	public static Image getImage(String imgpath) {
		return SWTResourceManager.getImage(EsmApplication.class, "/img/"+ imgpath);
	}

	public static String notNull(String s) {
		return (s != null) ? s : "";
	}

	public static boolean notNullOrEmpty(String s) {
		return (s != null && !s.equals(""));
	}

	public static boolean isNullOrEmpty(String s) {
		return (s == null || s.equals(""));
	}

	public static String getRB(Button yes, Button no) {
		if (yes.getSelection()) {
			return "Y";
		} else if (no.getSelection()) {
			return "N";
		} else {
			return "";
		}
	}

	public static String doMD5(String str) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(str.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {}
		return null;
	}

}
