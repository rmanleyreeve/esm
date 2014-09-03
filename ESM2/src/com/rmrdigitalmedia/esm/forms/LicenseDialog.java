package com.rmrdigitalmedia.esm.forms;

import java.sql.Timestamp;
import java.util.Date;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.models.LicenseTable;

public class LicenseDialog {

	boolean formOK = false;
	int privateKey = 8864;

	public static void main (String [] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			LicenseDialog nld = new LicenseDialog();
			nld.complete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LicenseDialog() {
		LogController.log("Running class " + this.getClass().getName());		
	}

	protected boolean validateKey(String key) {
		// should be format xxxxx-xxxxx-xxxxx
		// algo - remove dashes, last 7 digits + privateKey number should = first 7 digits
		// check key format
		if(
				key.length() != 17 || 
				key.charAt(5) != '-' ||
				key.charAt(11) != '-'
				) { 
			return false; 
		}
		String kStr = key.replaceAll("-", "");		
		int numA = Integer.parseInt(kStr.substring(0,7));
		int numB = Integer.parseInt(kStr.substring(8, 15));		
		if(numB + privateKey == numA) {
			LogController.log("License is valid");
			return true;
		}		
		return false;
	}


	public boolean complete() {
		Display display = Display.getDefault();
		final Shell dialog = new Shell (display,SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.ON_TOP);
		dialog.setText("ESM Setup");
		dialog.setImages(new Image[] { C.getImage(C.APP_ICON_16), C.getImage(C.APP_ICON_32) }); // 16x16 & 32x32
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10;
		formLayout.spacing = 10;
		dialog.setLayout (formLayout);

		CLabel label = new CLabel (dialog, SWT.NONE);
		label.setImage(C.getImage("registration.png"));
		label.setText ("Enter License Key:");
		FormData data = new FormData ();
		label.setLayoutData (data);

		Button cancel = new Button (dialog, SWT.PUSH);
		cancel.setFont(C.FONT_10);
		cancel.setText ("Cancel");
		data = new FormData ();
		data.width = 60;
		data.right = new FormAttachment (100, 0);
		data.bottom = new FormAttachment (100, 0);
		cancel.setLayoutData (data);
		cancel.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				LogController.log("User cancelled license dialog");
				dialog.close ();
			}
		});

		final Label msgLabel = new Label(dialog, SWT.NONE);
		msgLabel.setFont(C.FONT_8);
		msgLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		FormData fd_msgLabel = new FormData();
		fd_msgLabel.right = new FormAttachment(label, 220);
		fd_msgLabel.left = new FormAttachment(label, 41, SWT.LEFT);
		fd_msgLabel.top = new FormAttachment(cancel, 0, SWT.TOP);
		fd_msgLabel.height = 30;
		fd_msgLabel.bottom = new FormAttachment(100, 10);
		msgLabel.setLayoutData(fd_msgLabel);

		final Text text = new Text (dialog, SWT.BORDER);
		text.setFont(C.FONT_12);
		data = new FormData ();
		data.width = 180;
		data.left = new FormAttachment (label, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (label, 0, SWT.CENTER);
		data.bottom = new FormAttachment (cancel, 0, SWT.DEFAULT);
		text.setLayoutData (data);
		text.setText("");

		// TODO for development ONLY
		//text.setText("22356-81122-26817");

		text.setFocus();
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setFont(C.FONT_10);
		ok.setText ("OK");
		data = new FormData ();
		data.width = 60;
		data.right = new FormAttachment (cancel, 0, SWT.DEFAULT);
		data.bottom = new FormAttachment (100, 0);
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				String key = text.getText();
				if(validateKey(key)) {
					LogController.log("User entered VALID key: " + key);
					// insert key into database;
					try {
						LicenseTable.Row row = LicenseTable.getRow();
						row.setLicensekey(key);
						row.setVerifiedDate(new Timestamp(new Date().getTime()));
						row.setRemoteIdentifier(key);
						row.insert();						
						formOK = true;
						EsmApplication.appData.setField("LICENSE", key);
						msgLabel.setText("Registering license key...");
						LogController.log("Registering license key");
					} catch (Exception e1) {
						LogController.logEvent(this, C.FATAL, e1);
					}					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {}
					dialog.close ();
				} else {
					msgLabel.setFont(C.FONT_10B);
					msgLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					msgLabel.setText("License Key is not valid!");
					text.setText("");
					text.setFocus();
					LogController.log("User entered INVALID key: " + key);
				}
			}
		});

		dialog.setDefaultButton (ok);
		dialog.pack ();
		Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		Rectangle rect = dialog.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		dialog.setLocation (x, y);		  		
		dialog.open ();

		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		LogController.log("License dialog closed");		
		return formOK;
	}


}
