package com.rmrdigitalmedia.esm.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.rmrdigitalmedia.esm.AppData;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.PdfController;

public class PdfTest {

	public static int id = 3;

	public static void main(String[] args) {
		new PdfTest();
	}

	public static byte[] getBytes(InputStream is) throws IOException {
		int len;
		int size = 1024;
		byte[] buf;
		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			while ((len = is.read(buf, 0, size)) != -1)
				bos.write(buf, 0, len);
			buf = bos.toByteArray();
		}
		return buf;
	}

	public static Image getImage(String path) throws BadElementException, MalformedURLException, IOException {
		Image img;
		InputStream is = PdfTest.class.getResourceAsStream(path);
		img = Image.getInstance( getBytes(is) );		
		return img;
	}

	public PdfTest() {
		//FilesystemController.createLogDir();
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
		btnAdd.setText("Execute");		
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					EsmApplication.appData = new AppData();
					if (PdfController.buildAudit(id, true)) {						
						Program.launch(C.TMP_DIR + C.SEP + "SPACE_"+id+"_AUDIT.pdf");					
					} else {
						System.out.println("Failed to generate PDF");
					}					
				} catch (DocumentException e) {
					LogController.logEvent(PdfTest.class, C.ERROR, "Error getting PDF document", e);
				} catch (SQLException e) {
					LogController.logEvent(PdfTest.class, C.ERROR, "Error getting DB data for PDF document", e);			
				}
				shell.dispose();
			}
		});

		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setText("Blank Space PDF");
		btnNewButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));		
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				PdfController.createBlankSpaceForm(1);
				File pdf =  new File(C.DOC_DIR + C.SEP +  C.BLANK_SPACE_FORM);
				if( Program.launch(pdf.getPath()) ) {
					LogController.log("Opening blank Space Audit Form");
				} else {
					LogController.logEvent(PdfTest.class, C.ERROR, "Cannot open blank Space Audit Form!");
				}
			}
		});		

		Button btnNewButton2 = new Button(shell, SWT.NONE);
		btnNewButton2.setText("Blank Entry PDF");
		btnNewButton2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));		
		btnNewButton2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				PdfController.createBlankEntryForm(1);
				File pdf =  new File(C.DOC_DIR + C.SEP +  C.BLANK_ENTRY_FORM);
				if( Program.launch(pdf.getPath()) ) {
					LogController.log("Opening blank Entry Audit Form");
				} else {
					LogController.logEvent(PdfTest.class, C.ERROR, "Cannot open blank Entry Audit Form!");
				}
			}
		});		


		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		shell.dispose();
	}

}
