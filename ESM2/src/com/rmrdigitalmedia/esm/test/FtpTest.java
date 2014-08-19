package com.rmrdigitalmedia.esm.test;

import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.DatabaseController;
import com.rmrdigitalmedia.esm.controllers.InternetController;
import com.rmrdigitalmedia.esm.controllers.LogController;

public class FtpTest {

	public static int _id = 2;

	public static void main(String[] args) {
		new FtpTest();
	}


	public FtpTest() {
		Display display = Display.getDefault();
		final Shell shell = new Shell(SWT.DIALOG_TRIM );
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		shell.setText(C.HINT_TITLE);
		shell.setImage(C.getImage(C.APP_ICON_16));
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.marginTop = 2;
		gl_shell.marginRight = 2;
		gl_shell.marginLeft = 2;
		gl_shell.marginBottom = 2;
		shell.setLayout(gl_shell);

		Button btnAdd = new Button(shell, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));		
		btnAdd.setText("Go");		
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				File f = DatabaseController.exportDataFile();
				if (f.exists() && InternetController.checkNetAccess()) {					
					if(InternetController.uploadFileFTP(f.getPath(), f.getName())) {
						EsmApplication.alert("File uploaded successfully");
					} else {
						LogController.logEvent(this, C.ERROR, "Error uploading file");
					}					
				} else {
					LogController.logEvent(this, C.ERROR, "No net access or file missing");
				}
			}
		});


		shell.open();
		shell.pack();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		shell.dispose();

	}

}
