package com.rmrdigitalmedia.esm.test;

import java.io.File;
import java.io.IOException;

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

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.controllers.DatabaseController;

public class DocDBTest {

	public static int _id = 2;

	public static void main(String[] args) {
		new DocDBTest();
	}


	public DocDBTest() {
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
		btnAdd.setText("Add");		

		Button btnShow = new Button(shell, SWT.NONE);
		btnShow.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));		
		btnShow.setText("Show");		


		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				File f = new File("/Users/rich/Desktop/test.pdf");
				int id=0;
				try {
					id = DatabaseController.insertDocument(f,1,1);
					_id = id;
				} catch (IOException ex) {	}
				System.out.println(id);
			}
		});

		btnShow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					File f = DatabaseController.readDocument(_id);
					System.out.println(f);
					Program.launch(f.getPath());
					f.deleteOnExit();
				} catch (IOException ex1) {
					ex1.printStackTrace();
				}
			}
		});

		shell.open();
		//shell.pack();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		shell.dispose();

	}

}
