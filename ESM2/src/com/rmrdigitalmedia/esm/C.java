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

	// app setup properties
	public static String OS = (SWT.getPlatform());
	public static String HOME_DIR = System.getProperty("user.home");
	public static String INSTALL_DIR = (OS.equals("cocoa")) ? "Shared" : "All Users";
	public static String SEP = (OS.equals("cocoa")) ? "/" : "\\";;
	public static String DATA_DIR_NAME = "ESM Data";
	public static String DOC_DIR_NAME = "docs";
	public static String LOG_DIR_NAME = "logs";
	public static String TMP_DIR_NAME = "temp";
	public static String USER_DOCS_DIR = new File(HOME_DIR).getParentFile().getAbsolutePath();
	public static String DATA_DIR = USER_DOCS_DIR + SEP + INSTALL_DIR + SEP + DATA_DIR_NAME;
	public static String DOC_DIR = DATA_DIR + SEP + DOC_DIR_NAME;
	public static String LOG_DIR = DATA_DIR + SEP + LOG_DIR_NAME;
	public static String TMP_DIR = DATA_DIR + SEP + TMP_DIR_NAME;

	// database properties
	public static String DB_NAME = "ESM";
	public static String DB_CONN_STR = "jdbc:h2:~/../" + INSTALL_DIR + "/" + DATA_DIR_NAME + "/" + DB_NAME + ";MODE=MYSQL;IFEXISTS=TRUE";
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
	public static String FONT = (OS.equals("cocoa")) ? "Lucida Grande" : "Arial";
	private static int FONT_ADD = (OS.equals("cocoa")) ? 2 : 0;
	public static Font BUTTON_FONT = SWTResourceManager.getFont(FONT, 9 + FONT_ADD, SWT.NORMAL);
	public static Font HEADER_FONT = SWTResourceManager.getFont(FONT, 13 + FONT_ADD, SWT.NORMAL);
	public static Font FORM_HEADER_FONT = SWTResourceManager.getFont(FONT, 10 + FONT_ADD, SWT.BOLD);
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
	public static Font ALERT_TITLE = SWTResourceManager.getFont(FONT, 18 + FONT_ADD, SWT.BOLD);

	// web properties
	public static String USER_AGENT = "Mozilla/5.0";
	public static String REMOTE_URL = "http://www.rmrdigitalmedia.co.uk/esm/";
	public static String LATEST_VERSION_URL = REMOTE_URL + "version.txt";
	public static String UPDATE_URL = REMOTE_URL + "updates.php";
	public static String FTP_SERVER = "ftp.rmrdigitalmedia.co.uk";
	public static String FTP_USER = "esm@rmrdigitalmedia.co.uk";
	public static String FTP_PASS = "Vide0tel";


	// message text
	public static String NEW_VERSION_ALERT = "New version available!";
	public static String EXIT_MSG = "***** PROGRAM EXIT ****\n\n\n";
	public static String LOGIN_FAIL_MSG = "Username or Password not recognised.\nPlease try again.";
	public static String SPACE_ALERT_RED = "The space you have selected has been classified as RED.\nBe extra cautious as you proceed.";
	public static String SPACE_NOT_AUTH = "This space has NOT been authorized.";
	public static String COPYRIGHT = "   \u00a9Videotel 2014";

	// app screen titles
	public static String APP_NAME = "Enclosed Spaces Management System";
	public static String SPACES_LIST_TITLE = "Classified Enclosed Spaces";
	public static String ADMIN_PAGE_TITLE = "ESM System Administration";
	public static String SPACE_AUDIT_CHECKLIST_PAGE_TITLE = "Internal Space Audit: Checklist";
	public static String ENTRY_AUDIT_CHECKLIST_PAGE_TITLE = "Entry Point Audit: Checklist";
	public static String SPACE_AUDIT_CLASSIFICATION_PAGE_TITLE = "Internal Space Audit: Classification Questions";
	public static String ENTRY_AUDIT_CLASSIFICATION_PAGE_TITLE = "Entry Point Audit: Classification Questions";
	public static String HINT_TITLE = "Hint";

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
	public static String DISCLAIMER = "This program is intended to reflect the best available "
			+ "techniques and practices at the time of production. It is intended purely as comment. "
			+ "No responsibility is accepted by Videotel, or by any firm, corporation or organisation "
			+ "who or which has been in any way concerned with the production, supply or sale of this program "
			+ "for accuracy of any information given hereon or for any omission here from.";
	public static String SPACE_OVERALL_STATUS_MSG_RED = "It is advised that extreme caution is observed when using this space.";
	public static String SPACE_OVERALL_STATUS_MSG_AMBER = "It is advised that caution is observed when using this space.";
	public static String SPACE_OVERALL_STATUS_MSG_GREEN = "There are no significant hazards reported for this space.";
	public static String ENTRY_OVERALL_STATUS_MSG_RED = "It is advised that extreme caution is observed when using this entry point.";
	public static String ENTRY_OVERALL_STATUS_MSG_AMBER = "It is advised that extreme caution is observed when using this entry point.";
	public static String ENTRY_OVERALL_STATUS_MSG_GREEN = "There are no significant hazards reported for this entry point.";

	private C() {
		throw new AssertionError();
	}

	// public utility methods
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
			return null;
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
