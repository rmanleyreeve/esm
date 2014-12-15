package com.rmrdigitalmedia.esm;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.rmrdigitalmedia.esm.controllers.FilesystemController;
import com.rmrdigitalmedia.esm.controllers.InternetController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.dialogs.HintAlert;
import com.rmrdigitalmedia.esm.models.EsmUsersTable.Row;
import com.rmrdigitalmedia.esm.views.LoginView;

public class EsmApplication {

	private static Object me;
	public static AppData appData;
	public AppLoader loader;
	public static WindowController wc;

	public EsmApplication() {
		me = this;
		final Display display = new Display();

		// create log dir first
		FilesystemController.createLogDir();
		System.out.println("LOGFILE: " + LogController.logfile + "\n");
		LogController.log("OS: " + C.OS  +", "+  C.ARCHITECTURE);
		LogController.log("JVM: " + C.JVM +", "+ C.JVM_ARCHITECTURE);
		LogController.log("STARTING " + C.APP_NAME + "...\n");
		LogController.log("Running class " + me.getClass().getName());
		// dynamic data store
		appData = new AppData();
		// application loader
		loader = new AppLoader(display);
		while (!display.isDisposed() && display.getShells().length != 0 && !Display.getCurrent().getShells()[0].isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	public static void main(String[] args) {
		new EsmApplication();
	}

	public static void alert(String msg) {
		try {
			Shell sh = new Shell(Display.getCurrent() == null ? Display.getDefault() : Display.getCurrent());
			MessageBox mb = new MessageBox(sh, SWT.OK);
			mb.setText("Alert");
			mb.setMessage(msg);
			mb.open();
		} catch (Exception e) {
			LogController.logEvent(me, C.NOTICE, e);
		}
	}

	public static Shell modalWait() {
		Display display = Display.getCurrent() == null ? Display.getDefault() : Display.getCurrent();
		Shell shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.ON_TOP);
		shell.setSize(300,50);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
		Label lblMsg = new Label(shell, SWT.NONE);
		lblMsg.setBounds(0, 15, 300, 40);
		lblMsg.setAlignment(SWT.CENTER);
		lblMsg.setFont(C.FONT_12B);
		lblMsg.setText("Working, please wait...");
		return shell;
	}

	public static void alert(String msg, String title) {
		try {
			Shell sh = new Shell(Display.getCurrent() == null ? Display.getDefault() : Display.getCurrent());
			MessageBox mb = new MessageBox(sh, SWT.OK);
			mb.setText(title);
			mb.setMessage(msg);
			mb.open();
		} catch (Exception e) {
			LogController.logEvent(me, C.NOTICE, e);
		}
	}

	public static void alert(Shell sh, String msg) {
		try {
			MessageBox mb = new MessageBox(sh, SWT.OK);
			mb.setText("Alert");
			mb.setMessage(msg);
			mb.open();
		} catch (Exception e) {
			LogController.logEvent(me, C.NOTICE, e);
		}
	}

	public static boolean info(String msg, String title) {
		Shell sh = new Shell();
		MessageBox mb = new MessageBox(sh, SWT.ICON_INFORMATION | SWT.YES | SWT.NO);
		mb.setText(title);
		mb.setMessage(msg);
		int returnCode = mb.open();
		return (returnCode == 32);
	}

	public static boolean confirm(String msg) {
		Shell sh = new Shell();
		MessageBox mb = new MessageBox(sh, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		mb.setText("Attention");
		mb.setMessage(msg);
		int returnCode = mb.open();
		return (returnCode == SWT.YES);
	}

	public static void hint(String text) {
		Shell sh = Display.getCurrent().getActiveShell();
		new HintAlert(sh, text);
	}

	public static void appLogin(Shell loader) {
		// ** APP LOADER IS DISPOSED NOW **
		// display login form
		Rectangle rect = loader.getBounds();
		Display display = loader.getDisplay();
		loader.close();
		loader.dispose();
		LoginView login = new LoginView(display, rect);
		login.createContents();
	}


	public static void appLogout(Shell appwin) {
		Display display = Display.getDefault();//appwin.getDisplay();
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		int splashW = C.SPLASH_DEFAULT_WIDTH;
		int splashH = C.SPLASH_DEFAULT_HEIGHT;					
		try {
			splashW = (Integer) EsmApplication.appData.getField("SPLASH_W");
			splashH = (Integer) EsmApplication.appData.getField("SPLASH_H");
		} catch (Exception ex) {					}					
		int x = bounds.x + (bounds.width - splashW) / 2;
		int y = bounds.y + (bounds.height - splashH) / 2;
		Rectangle rect = new Rectangle(x,y,splashW,splashH);
		wc.killUser();
		wc = null;
		appwin.close();
		appwin.dispose();
		LoginView login = new LoginView(display, rect);
		login.createContents();					
	}

	public static void appPreLoad(Row user, Shell loginWindow) {
		// login window still open
		// check for net access
		if (C.CHECK_UPDATES) {
			if (InternetController.checkNetAccess()) {
				// check for updates
				LogController.log("Checking for updates...");
				try {
					InternetController.getUpdates();
				} catch (IOException e) {
					LogController.logEvent(me, C.WARNING, e);
				}
			}
		}
		runApp(user, loginWindow);
	}

	public static void runApp(Row user, Shell loginWindow) {
		// close login window now - all program setup is complete - open main app window
		LogController.log("PROGRAM SETUP COMPLETED");
		loginWindow.close();
		loginWindow.dispose();
		wc = new WindowController(user);
		wc.open();
	}

}