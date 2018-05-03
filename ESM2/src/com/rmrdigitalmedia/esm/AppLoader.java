package com.rmrdigitalmedia.esm;

import java.io.IOException;
import java.io.InputStreamReader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.rmrdigitalmedia.esm.controllers.AuditController;
import com.rmrdigitalmedia.esm.controllers.DatabaseController;
import com.rmrdigitalmedia.esm.controllers.FilesystemController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.dialogs.LicenseDialog;
import com.rmrdigitalmedia.esm.forms.AddAdminForm;
import com.rmrdigitalmedia.esm.forms.AddVesselForm;

@SuppressWarnings("unused")
public class AppLoader {

	private static Object me;
	public static ProgressBar pbar;
	public static Label pmsg;
	private final static int SPLASH_MAX = 100;
	static Shell myshell;
	private static int pc = 15;

	public static void update() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e1) {
		}
		pbar.setSelection(pbar.getSelection() + pc);
		LogController.log("Progress: " + pbar.getSelection() + "%");
	}

	public static void update(int amt) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
		}
		if(pbar.getSelection()< SPLASH_MAX ) {
			pbar.setSelection(pbar.getSelection() + amt);
			//LogController.log("Progress: " + pbar.getSelection() + "%");
		}
	}

	public static void splashMessage(String txt) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
		}
		//LogController.log("SPLASH MSG: " + txt);
		pmsg.setText(txt);
	}

	public static void die(String msg) {
		MessageBox mb = new MessageBox(myshell, SWT.OK);
		mb.setText("Fatal Error");
		mb.setMessage(msg);
		mb.open();
		System.exit(0);
	}

	public AppLoader(final Display display) {
		me = this;
		LogController.log("Running class " + this.getClass().getName());

		String[] vtxt = {"0.0.0","build 001"};
		try {
			vtxt = CharStreams.toString(new InputStreamReader(this.getClass().getResourceAsStream("/txt/version.txt"), Charsets.UTF_8)).split("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] varr = vtxt[0].trim().split("\\.");
		String build = vtxt[1];
		EsmApplication.appData.setField("VERSION", vtxt[0].trim());
		EsmApplication.appData.setField("MAJOR", Integer.parseInt(varr[0]));
		EsmApplication.appData.setField("MINOR", Integer.parseInt(varr[1]));
		EsmApplication.appData.setField("POINT", Integer.parseInt(varr[2]));
		EsmApplication.appData.setField("BUILD", build);

		final Shell splash = new Shell(SWT.ON_TOP);
		AppLoader.myshell = splash;
		FormLayout layout = new FormLayout();
		splash.setLayout(layout);
		splash.setBackgroundMode(SWT.INHERIT_FORCE);

		ProgressBar bar = new ProgressBar(splash, SWT.NONE);
		AppLoader.pbar = bar;
		bar.setMaximum(SPLASH_MAX);
		FormData progressData = new FormData();
		progressData.left = new FormAttachment(0, -5);
		progressData.right = new FormAttachment(100, 0);
		progressData.bottom = new FormAttachment(100, 0);
		bar.setLayoutData(progressData);

		Label msg = new Label(splash, SWT.WRAP | SWT.RIGHT);
		msg.setForeground(C.TITLEBAR_BGCOLOR);
		AppLoader.pmsg = msg;
		msg.setFont(C.FONT_8);
		msg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		FormData msgData = new FormData();
		msgData.width = 200;
		msgData.right = new FormAttachment(100, -5);
		msgData.top = new FormAttachment(100, -55);
		msgData.bottom = new FormAttachment(100, -20);
		msg.setLayoutData(msgData);

		Label label = new Label(splash, SWT.NONE);
		label.setImage(C.getImage(C.SPLASH_IMAGE));
		FormData labelData = new FormData();
		labelData.right = new FormAttachment(100, 0);
		labelData.bottom = new FormAttachment(100, 0);
		label.setLayoutData(labelData);

		splash.pack();
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = splash.getBounds();
		LogController.log("Splash Screen Size: " + rect.width + ":" + rect.height);
		EsmApplication.appData.setField("SPLASH_W", rect.width);
		EsmApplication.appData.setField("SPLASH_H", rect.height);		
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		splash.setLocation(x, y);
		splash.open();

		display.asyncExec(new Runnable() {
			@Override
			public void run() {

				// check/set up filesystem
				LogController.log("Starting filesystem check");
				splashMessage("Checking file system integrity");
				FilesystemController fs = new FilesystemController();
				fs.checkFS();
				splashMessage("File system integrity check complete");
				update(); // 1

				// check/set up database
				LogController.log("Starting database check");
				splashMessage("Checking database integrity");
				DatabaseController db = new DatabaseController();
				db.checkDB();
				splashMessage("Database integrity check complete");
				update(); // 2

				// background thread
				Thread auditInit = new Thread() {
					@Override
					public void run() {
						// do expensive processing
						AuditController.init();
						display.syncExec(new Runnable() {
							@Override
							public void run() {
								// notify GUI
							}

						});
					}
				};
				// do initial audit calculations in background thread
				EsmApplication.appData.setField("INIT", 0);
				auditInit.start();

				// check license key in DB
				splashMessage("Checking license key");
				if (!DatabaseController.checkLicenseKey()) {
					// open license key dialog
					EsmApplication.alert(myshell, "License key not found!");
					LicenseDialog nld = new LicenseDialog();
					if (nld.complete()) {
						LogController.log("License saved in database");
					} else {
						EsmApplication.alert("License Key required. Exiting program...");
						LogController.log(C.EXIT_MSG);
						System.exit(0);
					}
				}
				update(); // 3

				// check/set up new admin user
				splashMessage("Checking administrator account");
				if (!DatabaseController.checkAdmin()) {
					// open admin user dialog
					EsmApplication.alert(myshell, "System Administrator not found!");
					AddAdminForm naf = new AddAdminForm();
					if (naf.complete()) {
						LogController.log("System Administrator saved in database");
					} else {
						EsmApplication.alert("System Administrator account required. Exiting program...");
						LogController.log(C.EXIT_MSG);
						System.exit(0);
					}
				}
				update(); // 4

				// check/set up new admin user
				splashMessage("Checking Vessel/Installation details");
				if (!DatabaseController.checkVessel()) {
					// open admin user dialog
					EsmApplication.alert(myshell, "Vessel/Installation info not found!");
					AddVesselForm avf = new AddVesselForm();
					if (avf.complete()) {
						LogController.log("Vessel/Installation saved in database");
					} else {
						EsmApplication.alert("Vessel/Installation Info required. Exiting program...");
						LogController.log(C.EXIT_MSG);
						System.exit(0);
					}
				}
				update(); // 5

				// wait for audits
				splashMessage("Calculating audit status");
				while ( (Integer) EsmApplication.appData.getField("INIT")==0 ) {	update(1); }
				// init setup OK, now go to login screen
				// LOADER IS DISPOSED NOW
				update();
				splashMessage("Audit status complete");
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {}
				EsmApplication.appLogin(splash);
			}
		});

		while (!bar.isDisposed() && bar.getSelection() != SPLASH_MAX) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
