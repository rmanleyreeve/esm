package com.rmrdigitalmedia.esm.test;

import java.io.File;
import java.io.IOException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
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

public class ImageDBTest {

	public static void main(String[] args) {
		new ImageDBTest();
	}


	public ImageDBTest() {
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

		Button btnOK = new Button(shell, SWT.NONE);
		btnOK.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));		
		btnOK.setText("Browse");		
		
		final CLabel lblIcon = new CLabel(shell, SWT.NONE);
		lblIcon.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblIcon.setImage(DatabaseController.readImageDataThumb(1));

		final CLabel lblIcon2 = new CLabel(shell, SWT.NONE);
		lblIcon2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblIcon2.setImage(DatabaseController.readImageDataFull(1));

		
		btnOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				File f = new File("/Users/rich/Desktop/brentmason.jpg");
				int id=0;
				try {
					id = DatabaseController.insertImageData(f);
				} catch (IOException ex) {	}
				System.out.println(id);
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
