package com.rmrdigitalmedia.esm.dialogs;

import java.io.IOException;
import java.io.InputStreamReader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.controllers.LogController;

public class UserAgreementDialog {

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			new UserAgreementDialog(shell);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public UserAgreementDialog(final Shell appwin) {
		LogController.log("Running class " + this.getClass().getName());	
		
		Display display = Display.getDefault();
		final Shell shell = new Shell(SWT.NONE | SWT.ON_TOP);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		shell.setText(C.USER_AGREEMENT_TITLE);
		shell.setImage(C.getImage(C.APP_ICON_16));
		shell.setSize(640, 480);
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
		
		GridLayout gl_shell = new GridLayout(2, true);
		gl_shell.marginTop = 10;
		gl_shell.marginRight = 0;
		gl_shell.marginLeft = 10;
		gl_shell.marginBottom = 2;
		shell.setLayout(gl_shell);

		String html = "";
		try {
			html += CharStreams.toString(new InputStreamReader(this.getClass().getResourceAsStream("/htm/agreement.html"), Charsets.UTF_8));

			Browser browser;
			try {
				browser = new Browser(shell, SWT.BORDER);
				browser.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 2, 1));
			} catch (SWTError e) {
				LogController.logEvent(this, C.ERROR, "Could not instantiate Browser" + e.getMessage());
				shell.dispose();
				return;
			}
			browser.setText(html);	
			browser.setBounds(rect);

		} catch (IOException e) {
			LogController.logEvent(this,C.WARNING,e);
		}	

		Button btnOK = new Button(shell, SWT.NONE);
		btnOK.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));		
		btnOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		btnOK.setText("Agree");

		Button btnX = new Button(shell, SWT.NONE);
		GridData gd_btnX = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnX.horizontalIndent = 10;
		btnX.setLayoutData(gd_btnX);
		btnX.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				appwin.close();
				LogController.log("User cancelled User agreement dialog");
				System.exit(0);
			}
		});
		btnX.setText("Disagree");
		
		shell.setDefaultButton(btnOK);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		LogController.log("User agreement dialog closed");
		shell.dispose();

	}
}
