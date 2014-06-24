package com.rmrdigitalmedia.esm;

import java.io.IOException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import com.rmrdigitalmedia.esm.controllers.FilesystemController;
import com.rmrdigitalmedia.esm.controllers.InternetController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.LoginController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.models.EsmUsersTable.Row;

public class EsmApplication {

	private static Object me;
	public static AppData appData;
	public AppLoader loader;
	public static WindowController wc;

	public EsmApplication() {
		me = this;
		final Display display = new Display();


		// create log dir first
		FilesystemController fs = new FilesystemController();
		fs.createLogDir();
		System.out.println("LOGFILE: " + LogController.logfile);
		LogController.log("Running class " + me.getClass().getName());
		LogController.log("Starting ESM Application...");
		appData = new AppData();

		loader = new AppLoader(display);
		while (!display.isDisposed() && display.getShells().length != 0
				&& !Display.getCurrent().getShells()[0].isDisposed()) {
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
			Shell sh = Display.getCurrent().getActiveShell();
			MessageBox mb = new MessageBox(sh, SWT.OK);
			mb.setText("Alert");
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

	public static boolean confirm(String msg) {
		Shell sh = new Shell();
		MessageBox mb = new MessageBox(sh, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		mb.setText("Attention");
		mb.setMessage(msg);

		int returnCode = mb.open();
		return (returnCode == 32);
	}

	public static void appLogin(Shell loader) {
		// ** APP LOADER IS DISPOSED NOW **
		// display login form
		Rectangle rect = loader.getBounds();
		Display display = loader.getDisplay();
		loader.close();
		loader.dispose();
		LoginController login = new LoginController(display, rect);
		login.createContents();
	}

	public static void appPreLoad(Row user, Shell loginWindow) {
		// login window still open
		// check for net access
		if (InternetController.checkNetAccess()) {
			// check for updates
			LogController.log("Checking for updates...");
			try {
				InternetController.getUpdates();
			} catch (IOException e) {
				LogController.logEvent(me, C.WARNING, e);
			}
		}
		runApp(user, loginWindow);
	}

	public static void runApp(Row user, Shell loginWindow) {
		// close login window now - all program setup is complete - open main
		// app window
		loginWindow.close();
		loginWindow.dispose();
		wc = new WindowController(user);
		wc.open();
	}

}