package com.rmrdigitalmedia.esm.dialogs;

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

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.controllers.InternetController;
import com.rmrdigitalmedia.esm.controllers.LogController;

@SuppressWarnings("unused")
public class UpdateDialog {

	private FormData fd_lblAProgramUpdate;
	private String latestVersion;


	public static void main (String [] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			UpdateDialog ud = new UpdateDialog("1.0.2");			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public UpdateDialog(String _latestVersion) {
		LogController.log("Running class " + this.getClass().getName());		
		latestVersion = _latestVersion;
		Display display = Display.getDefault();
		final Shell dialog = new Shell (display,SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.ON_TOP);
		dialog.setImage(C.getImage(C.APP_ICON_16));
		dialog.setSize(234, 131);
		dialog.setText(C.ALERT_TITLE);
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10;
		formLayout.spacing = 10;
		dialog.setLayout (formLayout);

		Label lblAProgramUpdate = new Label (dialog, SWT.NONE);
		lblAProgramUpdate.setFont(C.FONT_10);
		lblAProgramUpdate.setText ("A program update ("+latestVersion+") is available.\nWould you like to download it now?");
		FormData data;
		fd_lblAProgramUpdate = new FormData ();
		lblAProgramUpdate.setLayoutData (fd_lblAProgramUpdate);

		Button cancel = new Button (dialog, SWT.PUSH);
		cancel.setFont(C.FONT_10);
		cancel.setText ("Cancel");
		data = new FormData ();
		data.width = 60;
		data.top = new FormAttachment(lblAProgramUpdate);
		data.right = new FormAttachment(100);
		data.bottom = new FormAttachment(100, 0);
		cancel.setLayoutData(data);
		cancel.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				LogController.log("User cancelled update dialog");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {}
				dialog.close ();
			}
		});		

		Button ok = new Button (dialog, SWT.PUSH);
		ok.setFont(C.FONT_10);
		ok.setText ("OK");
		data = new FormData ();
		data.width = 60;
		data.right = new FormAttachment(cancel);
		data.bottom = new FormAttachment(100, 0);
		ok.setLayoutData(data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				// get URL
				InternetController.open(C.UPDATE_URL);
				dialog.close();
				LogController.log("User selected program update");
				LogController.log(C.EXIT_MSG);
				Display.getDefault().dispose();
				System.exit(0);
			}
		});
		dialog.setDefaultButton (cancel);
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
		LogController.log("Update dialog closed");		
	}


}
