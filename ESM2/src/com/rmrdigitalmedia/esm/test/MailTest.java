package com.rmrdigitalmedia.esm.test;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.SQLException;
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
import com.rmrdigitalmedia.esm.controllers.DatabaseController;
import com.rmrdigitalmedia.esm.controllers.FilesystemController;
import com.rmrdigitalmedia.esm.models.VesselTable;

public class MailTest {

	public static int _id = 2;

	public static void main(String[] args) {
		new MailTest();
	}

	@SuppressWarnings("unused")
	private static String enc(String p) {
		if (p == null) {
			p = "";
		}
		try {
			p = URLEncoder.encode(p, "UTF-8").replace("+", "%20");
		} catch (Exception e) { }
		return p;
	}


	public MailTest() {
		FilesystemController.createLogDir();
		Display display = Display.getDefault();
		final Shell shell = new Shell(SWT.DIALOG_TRIM );
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		shell.setText("TEST");
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
			@SuppressWarnings("unused")
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				File f = DatabaseController.generateZipFile();
				if (f.exists()) {
					String vn = "";
					try {
						vn = VesselTable.getAllRows()[0].getName();
					} catch (SQLException ex) {	}					
					System.out.println("zip file created successfully");
					String body = "ESM Export file from "+ vn +" is attached";
					String subject = "ESM Export";
					String mailto = "mailto:export@esm-system.com?subject=TEST&body=BODY&attachment=" + f.getAbsolutePath();	
					
					//'export@esm-system.com?subject=ESM&body=see attachment&attachment="C:/abc/def/qwertyp.zip"'
					System.out.println(mailto);
					
					Desktop desktop = Desktop.getDesktop();
			        //String message = "mailto:username@domain.com?subject=New_Profile&body=seeAttachment&attachment=c:/Update8.txt";
			        URI uri = URI.create(mailto);
			        try {
						desktop.mail(uri);
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}

			        
					//Program.launch(mailto);
				} else {
					System.out.println("Error creating zip");
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
