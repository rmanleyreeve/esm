package com.rmrdigitalmedia.esm.forms;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;

public class EditUserPasswordForm {
	
	Shell myshell;
	boolean formOK = false;
	Text newpass,confirmnewpass;
	Label sep;	
	int  userID, headerH = 40;
	EsmUsersTable.Row uRow;
	private static Object me;

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main (String [] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			EditUserPasswordForm eupf = new EditUserPasswordForm(1);
			eupf.complete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EditUserPasswordForm(int _userID) {
		me = this;
		LogController.log("Running class " + this.getClass().getName());
		userID = _userID;
	}

	public boolean complete() {	
		
		Display display = Display.getDefault();
		final Shell shell = new Shell (display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.myshell = shell;
		shell.setSize(380, 200);
		shell.setText("ESM Setup");
		shell.setImages(new Image[] { C.getImage(C.APP_ICON_16), C.getImage(C.APP_ICON_32) }); // 16x16 & 32x32
		shell.setLayout(new FillLayout(SWT.VERTICAL));

		Composite container = new Composite(shell,SWT.NONE);
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));	
		container.setLayout(new FormLayout());		

		//set up row elements & positions =======================================================
		Composite header = new Composite(container,SWT.NONE);
		header.setBackground(C.TITLEBAR_BGCOLOR);
		header.setLayout(new FormLayout());		
		FormData fd_header = new FormData();
		fd_header.top = new FormAttachment(container,0);
		fd_header.right = new FormAttachment(100,0);
		fd_header.bottom = new FormAttachment(container,headerH);
		fd_header.left = new FormAttachment(0,0);
		header.setLayoutData(fd_header);

		Label lblImg = new Label(header, SWT.NONE);
		lblImg.setImage(C.getImage("users_icon.png"));
		FormData fd_lblImg = new FormData();
		fd_lblImg.top = new FormAttachment(0);
		fd_lblImg.left = new FormAttachment(0);
		lblImg.setLayoutData(fd_lblImg);
		lblImg.setBackground(C.TITLEBAR_BGCOLOR);

		Label lblTitle = new Label(header, SWT.NONE);
		lblTitle.setForeground(C.APP_BGCOLOR);
		lblTitle.setFont(C.FORM_HEADER_FONT);
		FormData fd_lblTitle = new FormData();
		fd_lblTitle.top = new FormAttachment(0, 10);
		fd_lblTitle.left = new FormAttachment(lblImg, 16);
		lblTitle.setLayoutData(fd_lblTitle);
		lblTitle.setBackground(C.TITLEBAR_BGCOLOR);
		lblTitle.setText("EDIT USER PASSWORD");

		Composite formHolder = new Composite(container,SWT.BORDER);
		FormData fd_formHolder = new FormData();
		fd_formHolder.left = new FormAttachment(0);
		fd_formHolder.top = new FormAttachment(header,0);
		fd_formHolder.right = new FormAttachment(100);
		fd_formHolder.bottom = new FormAttachment(100);
		formHolder.setLayoutData(fd_formHolder);
		formHolder.setLayout(new FillLayout(SWT.VERTICAL));

		Composite form = new Composite(formHolder,SWT.NONE);
		form.setBackground(C.APP_BGCOLOR);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 10;
		gridLayout.marginHeight = 20;
		gridLayout.numColumns = 4;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 10;
		form.setLayout(gridLayout);

		try {
			uRow = EsmUsersTable.getRow(userID);
		} catch (SQLException e2) {
			LogController.logEvent(me, C.ERROR, e2);
		}

		Label lblNewPass = new Label(form, SWT.NONE);
		lblNewPass.setBackground(C.APP_BGCOLOR);
		lblNewPass.setText("New Password:");		
		newpass = new Text(form, SWT.BORDER);
		GridData gd_newpass = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		gd_newpass.widthHint = 200;
		newpass.setLayoutData(gd_newpass);
		newpass.setEchoChar('*');

		Label lblConfirmNewPass = new Label(form, SWT.NONE);
		lblConfirmNewPass.setBackground(C.APP_BGCOLOR);
		lblConfirmNewPass.setText("Confirm Password:");		
		confirmnewpass = new Text(form, SWT.BORDER);
		GridData gd_confirmnewpass = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_confirmnewpass.widthHint = 200;
		confirmnewpass.setLayoutData(gd_confirmnewpass);
		confirmnewpass.setEchoChar('*');
	
		sep = new Label(form, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));		

		//==================================================================		

		Button ok = new Button (form, SWT.PUSH);
		ok.setToolTipText("Click to save these details");
		ok.setFont(C.FONT_10);
		ok.setText ("Submit");
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				Text[] fields = {newpass,confirmnewpass}; Validation.validateFields(fields);
				if( Validation.validateFields(fields) && Validation.checkMatch(fields) ) {
					try {
						uRow.setPassword(C.doMD5(newpass.getText()));
						uRow.setUpdateDate(new Timestamp(new Date().getTime()));
						uRow.update();
						formOK = true;
						LogController.log("User password edited in database.");
					} catch (Exception e1) {
						LogController.logEvent(this, C.ERROR, e1);
					}					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {}
					shell.close ();
				} else {
					Validation.validateError(myshell);
				}
			}
		});	

		Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		Rectangle rect = shell.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation (x, y);		  		
		shell.setDefaultButton (ok);

		shell.open ();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		LogController.log("Edit User Password form closed");	
		return formOK;
		
	}

	
	
	}
