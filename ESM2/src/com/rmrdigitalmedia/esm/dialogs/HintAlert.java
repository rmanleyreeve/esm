package com.rmrdigitalmedia.esm.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.controllers.LogController;

public class HintAlert {

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			new HintAlert(shell, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HintAlert(Shell appwin, String text) {
		LogController.log("Running class " + this.getClass().getName());		
		Display display = Display.getDefault();
		final Shell shell = new Shell(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.ON_TOP);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		shell.setText(C.HINT_TITLE);
		shell.setImage(C.getImage(C.APP_ICON_16));
		Rectangle bounds = appwin.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + 200;// (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
		GridLayout gl_shell = new GridLayout(2, false);
		gl_shell.marginTop = 2;
		gl_shell.marginRight = 2;
		gl_shell.marginLeft = 2;
		gl_shell.marginBottom = 2;
		shell.setLayout(gl_shell);

		CLabel lblIcon = new CLabel(shell, SWT.NONE);
		lblIcon.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblIcon.setImage(C.getImage("hint.png"));
		lblIcon.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));

		Text txtMsg = new Text(shell, SWT.WRAP | SWT.MULTI);
		txtMsg.setEditable(false);
		GridData gd_txtMsg = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_txtMsg.verticalIndent = 3;
		gd_txtMsg.widthHint = 400;
		txtMsg.setLayoutData(gd_txtMsg);
		txtMsg.setText(text);
		txtMsg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));

		Button btnOK = new Button(shell, SWT.NONE);
		btnOK.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));		
		btnOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		btnOK.setText("OK");

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		LogController.log("Hint alert closed");
		shell.dispose();

	}
}
