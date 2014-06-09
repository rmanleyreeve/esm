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
import com.rmrdigitalmedia.esm.controllers.DatabaseController;
import com.rmrdigitalmedia.esm.controllers.FilesystemController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.forms.NewAdminForm;
import com.rmrdigitalmedia.esm.forms.NewLicenseDialog;
import com.rmrdigitalmedia.esm.forms.NewVesselForm;

@SuppressWarnings("unused")
public class AppLoader {

	private static Object me;
	public static ProgressBar pbar;
	public static Label pmsg;
	private final int SPLASH_MAX = 100;
	static Shell myshell;
	private static int pc = 20;

	public static void update() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
		}
		pbar.setSelection(pbar.getSelection()+pc);
		LogController.log("Progress: " + pbar.getSelection()+"%");		
	}
	public static void message(String txt) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
		}
		LogController.log("SPLASH MSG: "+txt);
		pmsg.setText(txt);
	}
	public static void die(String msg) {		
		MessageBox mb = new MessageBox(myshell, SWT.OK);
		mb.setText("Fatal Error");
		mb.setMessage(msg);
		mb.open();
		System.exit(0);
	}

	public AppLoader(Display display)  {
		me = this;
		LogController.log("Running class " + this.getClass().getName());

		String vtxt = "0.0.0";		
		try {
			vtxt = CharStreams.toString(new InputStreamReader(this.getClass().getResourceAsStream("/txt/version.txt"), Charsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] varr = vtxt.split("\\.");
		EsmApplication.appData.setField("VERSION", vtxt);
		EsmApplication.appData.setField("MAJOR", Integer.parseInt(varr[0]));
		EsmApplication.appData.setField("MINOR", Integer.parseInt(varr[1]));
		EsmApplication.appData.setField("POINT", Integer.parseInt(varr[2]));

		final Shell splash = new Shell(SWT.ON_TOP);
		AppLoader.myshell = splash;
		splash.setBackgroundMode(SWT.INHERIT_DEFAULT);
		FormLayout layout = new FormLayout();
		splash.setLayout(layout);

		ProgressBar bar = new ProgressBar(splash, SWT.NONE);
		AppLoader.pbar = bar;
		bar.setMaximum(SPLASH_MAX);
		FormData progressData = new FormData();
		progressData.left = new FormAttachment(0, -5);
		progressData.right = new FormAttachment(100, 0);
		progressData.bottom = new FormAttachment(100, 0);
		bar.setLayoutData(progressData);

		Label msg = new Label(splash, SWT.RIGHT);
		msg.setForeground(C.TITLEBAR_BGCOLOR);
		AppLoader.pmsg = msg;
		msg.setFont(C.FONT_8);
		msg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		FormData msgData = new FormData();
		msgData.width = 220;
		msgData.right = new FormAttachment(100, -10);
		msgData.top = new FormAttachment(100, -50);
		msgData.bottom = new FormAttachment(100, -25);
		msg.setLayoutData(msgData);   

		Label label = new Label(splash, SWT.NONE);
		label.setImage(C.getImage("/img/splash2.jpg"));
		FormData labelData = new FormData();
		labelData.right = new FormAttachment(100, 0);
		labelData.bottom = new FormAttachment(100, 0);
		label.setLayoutData(labelData);

		splash.pack();
		Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		Rectangle rect = splash.getBounds ();
		LogController.log("Splash Screen Size: " + rect.width + ":" + rect.height);
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		splash.setLocation (x, y);		  		
		splash.open();

		display.asyncExec(new Runnable() {
			@Override
			public void run() {      	

				// check/set up filesystem
				LogController.log("AppLoader: filesystem check");
				message("Checking file system integrity");
				FilesystemController fs = new FilesystemController();
				fs.checkFS();
				message("File system integrity check complete");
				update(); //20%

				// check/set up database
				LogController.log("AppLoader: database check");
				message("Checking database integrity");
				DatabaseController db = new DatabaseController();
				db.checkDB();
				message("Database integrity check complete");
				update(); //40%

				// check license key in DB
				message("Checking License Key");
				if(!DatabaseController.checkLicenseKey()) {
					//open license key dialog
					EsmApplication.alert(myshell,"License key not found!");
					NewLicenseDialog nld = new NewLicenseDialog();
					if (nld.complete()) {
						LogController.log("License saved in database");
					} else {				
						EsmApplication.alert("License Key required. Exiting program...");
						LogController.log(C.EXIT_MSG);
						System.exit(0);						
					}
				}
				update(); //60%

				//check/set up new admin user
				message("Checking System Administration Access");
				if(!DatabaseController.checkAdmin()) {
					//open admin user dialog
					EsmApplication.alert(myshell,"System Administrator not found!");
					NewAdminForm naf = new NewAdminForm();					
					if(naf.complete()) {
						LogController.log("System Administrator saved in database");
					} else {
						EsmApplication.alert("System Administrator required. Exiting program...");
						LogController.log(C.EXIT_MSG);
						System.exit(0);
					}
				}
				update(); //80%

				//check/set up new admin user
				message("Checking Vessel Details");
				if(!DatabaseController.checkVessel()) {
					//open admin user dialog
					EsmApplication.alert(myshell,"Vessel info not found!");
					NewVesselForm nvf = new NewVesselForm();					
					if(nvf.complete()) {
						LogController.log("Vessel saved in database");
					} else {
						EsmApplication.alert("Vessel Info required. Exiting program...");
						LogController.log(C.EXIT_MSG);
						System.exit(0);
					}
				}
				update(); //100%											

				// init setup OK, now go to login screen
				// LOADER IS DISPOSED NOW
				EsmApplication.appLogin(splash);          
			}
		});

		while(!bar.isDisposed() && bar.getSelection() != SPLASH_MAX) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
