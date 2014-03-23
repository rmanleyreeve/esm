package com.rmrdigitalmedia.esm.forms;

import java.sql.Timestamp;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

import com.rmrdigitalmedia.esm.Constants;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.models.LicenseTable;

public class NewLicenseDialog {
	
	boolean formOK = false;

	public static void main (String [] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			NewLicenseDialog ld = new NewLicenseDialog();
			ld.complete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public NewLicenseDialog() {
	  	LogController.log("Running class " + this.getClass().getName());		
	}
	
	public boolean complete() {
		Display display = Display.getDefault();
		final Shell dialog = new Shell (display,SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.ON_TOP);
		dialog.setText("ESM Setup");
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10;
		formLayout.spacing = 10;
		dialog.setLayout (formLayout);

		Label label = new Label (dialog, SWT.NONE);
		label.setText ("Enter License Key:");
		FormData data = new FormData ();
		label.setLayoutData (data);

		Button cancel = new Button (dialog, SWT.PUSH);
		cancel.setFont(Constants.FONT_10);
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
		msgLabel.setFont(Constants.FONT_8);
		FormData fd_msgLabel = new FormData();
		fd_msgLabel.top = new FormAttachment(cancel, 0, SWT.TOP);
		fd_msgLabel.left = new FormAttachment(label, 0, SWT.LEFT);
		fd_msgLabel.height = 30;
		fd_msgLabel.bottom = new FormAttachment(100, 10);
		fd_msgLabel.width = 260;
		msgLabel.setLayoutData(fd_msgLabel);

		final Text text = new Text (dialog, SWT.BORDER);
		data = new FormData ();
		data.width = 300;
		data.left = new FormAttachment (label, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (label, 0, SWT.CENTER);
		data.bottom = new FormAttachment (cancel, 0, SWT.DEFAULT);
		text.setLayoutData (data);
		text.setText("");
		text.setFocus();

		Button ok = new Button (dialog, SWT.PUSH);
		ok.setFont(Constants.FONT_10);
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
				if(!key.equals("")) {
					LogController.log("User entered key: " + text.getText ());					
					// insert key into database;
					try {
						LicenseTable.Row row = LicenseTable.getAllRows()[0];
						String fbk = row.getFallbackKey();
						if(key.equals(fbk)) {
							LogController.log("Fallback Key MATCH - License Verified");
							row.setVerifiedDate(new Timestamp(new Date().getTime()));
						}
						row.setLicensekey(key);
						row.update("FALLBACK_KEY", row.getFallbackKey());
						formOK = true;
						EsmApplication.appData.setField("LICENSE", key);
						msgLabel.setText("Registering license key...");
						LogController.log("Registering license key");
					} catch (Exception e1) {
						LogController.logEvent(this, 2, e1);
					}					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {}
					dialog.close ();
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
