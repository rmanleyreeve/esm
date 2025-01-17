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
	public static String IMG_DIR_NAME = "images";
	public static String DOC_DIR_NAME = "docs";
	public static String LOG_DIR_NAME = "logs";
	public static String USER_DOCS_DIR = new File(HOME_DIR).getParentFile().getAbsolutePath();
	public static String DATA_DIR = USER_DOCS_DIR + SEP + INSTALL_DIR + SEP + DATA_DIR_NAME;
	public static String IMG_DIR = DATA_DIR + SEP + IMG_DIR_NAME;
	public static String DOC_DIR = DATA_DIR + SEP + DOC_DIR_NAME;
	public static String LOG_DIR = DATA_DIR + SEP + LOG_DIR_NAME;

	// database properties
	public static String DB_NAME = "ESM";
	public static String DB_CONN_STR = "jdbc:h2:~/../" + INSTALL_DIR + "/" + DATA_DIR_NAME + "/" + DB_NAME + ";IFEXISTS=TRUE";
	public static String DB_CONN_STR_SETUP = "jdbc:h2:~/../" + INSTALL_DIR + "/" + DATA_DIR_NAME + "/" + DB_NAME;
	public static String DB_SETUP_FILE = "SETUP.sql";

	// image properties
	public static int IMG_WIDTH = 800;
	public static int IMG_HEIGHT = 600;
	public static int THUMB_WIDTH = 150;
	public static int THUMB_HEIGHT = 150;
	
	// pdf properties
	public static String BLANK_SPACE_FORM = "blank_space_form.pdf";
	public static String BLANK_ENTRY_FORM = "blank_entry_form.pdf";

	// UI images
	public static String SPLASH_IMAGE = "splash.jpg";
	public static String APP_ICON_16 = "appicon16.png";
	public static String APP_ICON_32 = "appicon32.png";

	// UI style properties
	public static Color APP_BGCOLOR = SWTResourceManager.getColor(222, 224, 226);
	public static Color TITLEBAR_BGCOLOR = SWTResourceManager.getColor(122, 130, 137);
	public static Color FIELD_BGCOLOR = SWTResourceManager.getColor(238, 238, 238);
	public static Color BAR_BGCOLOR = SWTResourceManager.getColor(182, 186, 190);
	public static Color AUDIT_COLHEADER_BGCOLOR = SWTResourceManager.getColor(38, 147, 255);
	public static Color RED = SWTResourceManager.getColor(237, 28, 36);
	public static Color AMBER = SWTResourceManager.getColor(241, 89, 42);
	public static Color GREEN = SWTResourceManager.getColor(11, 148, 68);
	public static Map<String, Color> TRAFFICLIGHTS = ImmutableMap.of("red", C.RED, "amber", C.AMBER, "green", C.GREEN);
	public static Color ROW_HIGHLIGHT = SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND);
	public static Color ROW_SELECTED = SWTResourceManager.getColor(0, 128, 255);


	// UI fonts
	public static String FONT = (OS.equals("cocoa")) ? "Lucida Grande" : "Arial";
	private static int FONT_ADD = (OS.equals("cocoa")) ? 2 : 0;
	public static Font BUTTON_FONT = SWTResourceManager.getFont(FONT, 9 + FONT_ADD, SWT.NORMAL);
	public static Font HEADER_FONT = SWTResourceManager.getFont(FONT, 14 + FONT_ADD, SWT.NORMAL);
	public static Font FORM_HEADER_FONT = SWTResourceManager.getFont(FONT, 10 + FONT_ADD, SWT.BOLD);
	public static Font FONT_8 = SWTResourceManager.getFont(FONT, 8 + FONT_ADD, SWT.NORMAL);
	public static Font FONT_8B = SWTResourceManager.getFont(FONT, 8 + FONT_ADD, SWT.BOLD);
	public static Font FONT_9 = SWTResourceManager.getFont(FONT, 9 + FONT_ADD, SWT.NORMAL);
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

	// message text
	public static String NEW_VERSION_ALERT = "New version available!";
	public static String EXIT_MSG = "***** PROGRAM EXIT ****\n\n\n";
	public static String LOGIN_FAIL_MSG = "Username or Password not recognised.\nPlease try again.";
	public static String SPACE_ALERT_RED = "The space you have selected has been classified as RED.\nBe extra cautious as you proceed.";
	public static String SPACE_NOT_AUTH = "This space has NOT been authorized.";
	public static String COPYRIGHT = "\t\u00a9 Videotel 2014";

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

	/*
	 * public static void makeHoverButton(final Button b) { // NOT SUPPORTED ON
	 * WINDOWS :-( b.addMouseTrackListener(new MouseTrackAdapter() {
	 * 
	 * @Override public void mouseEnter(MouseEvent arg0) {
	 * //System.out.println(b);
	 * b.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED)); }
	 * 
	 * @Override public void mouseExit(MouseEvent arg0) {
	 * b.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK)); } }); }
	 */

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

}
