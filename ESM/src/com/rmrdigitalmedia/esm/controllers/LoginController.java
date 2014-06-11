package com.rmrdigitalmedia.esm.controllers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.EsmUsersTable.Row;

public class LoginController {

	Shell shell;
	private static Text txt_Password, txt_Username;
	private Display display;
	private Rectangle rect;
	private Label alertTxt;

	public LoginController(Display display,org.eclipse.swt.graphics.Rectangle rect) {
		this.display = display;
		this.rect = rect;
		LogController.log("Running class " + this.getClass().getName());
	}

	void submit() {
		//submit login info for verification, if OK then run main App
		String un = txt_Username.getText();
		String pw = txt_Password.getText();
		if(un != "" && pw != "") {
			LogController.log("LoginController: " + un + "/" + pw);				
			if(DatabaseController.verifyLogin(un,pw)) {	
				LogController.log("OK");
				//User user = null;
				Row user = null;
				try {
					user = EsmUsersTable.getRow("USERNAME", un);
					EsmApplication.appData.setField("ACCESS", user.getAccessLevel());
				} catch (SQLException uex) {
					LogController.logEvent(this,C.FATAL,uex);						
				}
				EsmApplication.appPreLoad(user,shell);
				// LoginController will be disposed from now
			} else {
				LogController.log("FAIL");
				alertTxt.setText(C.LOGIN_FAIL_MSG);
				txt_Username.setText("");
				txt_Password.setText("");
			}
		}
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createContents() {
		shell = new Shell(display, SWT.ON_TOP);
		final FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 1;
		shell.setLayout(fillLayout);
		Rectangle s = C.getImage("/img/splash2.jpg").getBounds();
		shell.setSize(s.width, s.height);

		//Create a composite with grid layout.
		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		composite.setLayout(gridLayout);

		//Setting the background of the composite with the image background for login dialog
		final Label img_Label = new Label(composite, SWT.NONE);
		img_Label.setLayoutData(new GridData(300, 250));
		img_Label.setImage(C.getImage("/img/splash3.jpg"));

		//Creating the composite which will contain the login related widgets
		final Composite cmp_Login = new Composite(composite, SWT.NONE);
		cmp_Login.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		final RowLayout rowLayout = new RowLayout();
		rowLayout.marginLeft = 0;
		rowLayout.marginRight = 0;
		rowLayout.fill = true;
		cmp_Login.setLayout(rowLayout);
		final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false);
		gridData.widthHint = 196;
		cmp_Login.setLayoutData(gridData);

		//Label for the heading
		final CLabel clbl_UserLogin = new CLabel(cmp_Login, SWT.NONE);
		clbl_UserLogin.setFont(C.FONT_10B);
		clbl_UserLogin.setAlignment(SWT.CENTER);
		clbl_UserLogin.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		final RowData rowData = new RowData();
		rowData.height = 42;
		rowData.width = 180;
		clbl_UserLogin.setLayoutData(rowData);
		String txt = "";
		try {
			txt += (String)EsmApplication.appData.getField("VESSEL") + ": ";
		} catch (Exception e1) { }
		clbl_UserLogin.setText(txt + "User Login");

		//Label for the username
		final CLabel clbl_Username = new CLabel(cmp_Login, SWT.NONE);
		clbl_Username.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		final RowData rowData_1 = new RowData();
		rowData_1.width = 180;
		clbl_Username.setLayoutData(rowData_1);
		clbl_Username.setText("Username:");

		//Textfield for the username
		txt_Username = new Text(cmp_Login, SWT.BORDER);
		final RowData rowData_2 = new RowData();
		rowData_2.width = 170;
		txt_Username.setLayoutData(rowData_2);

		//Label for the password
		final CLabel clbl_Password = new CLabel(cmp_Login, SWT.NONE);
		clbl_Password.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		final RowData rowData_3 = new RowData();
		rowData_3.width = 180;
		clbl_Password.setLayoutData(rowData_3);
		clbl_Password.setText("Password:");

		//Textfield for the password
		txt_Password = new Text(cmp_Login, SWT.BORDER);
		txt_Password.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				if(ke.keyCode==SWT.CR) {
					submit();
				}
			}
		});
		final RowData rowData_4 = new RowData();
		rowData_4.width = 170;
		txt_Password.setLayoutData(rowData_4);
		txt_Password.setEchoChar('*');

		//Composite to hold button as I want the
		//button to be positioned to my choice.
		final Composite cmp_ButtonBar = new Composite(cmp_Login, SWT.NONE);
		cmp_ButtonBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		final RowData rowData_5 = new RowData();
		rowData_5.height = 85;
		rowData_5.width = 200;
		cmp_ButtonBar.setLayoutData(rowData_5);
		cmp_ButtonBar.setLayout(new FormLayout());

		//Button for login
		final Button btnLogin = new Button(cmp_ButtonBar, SWT.FLAT | SWT.CENTER);
		btnLogin.setFont(C.FONT_8B);
		final FormData formData = new FormData();
		formData.bottom = new FormAttachment(0, 28);
		formData.top = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -25);
		formData.left = new FormAttachment(100, -75);
		btnLogin.setLayoutData(formData);
		btnLogin.setText("Login");
		btnLogin.setToolTipText("Click to log in to the system");

		Composite alert = new Composite(cmp_ButtonBar, SWT.NONE);
		alert.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		FormData fd_alert = new FormData();
		fd_alert.bottom = new FormAttachment(btnLogin, 60, SWT.BOTTOM);
		fd_alert.top = new FormAttachment(btnLogin, 5);
		fd_alert.left = new FormAttachment(0);
		fd_alert.right = new FormAttachment(100);
		alert.setLayoutData(fd_alert);
		alertTxt = new Label(alert, SWT.NONE);
		alertTxt.setFont(C.FONT_8);
		alertTxt.setLocation(0, 0);
		alertTxt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		alertTxt.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		alertTxt.setAlignment(SWT.CENTER);
		alertTxt.setSize(195, 55);

		btnLogin.addListener(SWT.Selection, new Listener() { 
			@Override
			public void handleEvent(Event e) {
				submit();
			}
		});

		//Label for copyright info
		final CLabel clbl_Message = new CLabel(cmp_Login, SWT.NONE);
		clbl_Message.setFont(C.FONT_8);
		clbl_Message.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		clbl_Message.setAlignment(SWT.RIGHT);
		final RowData rowData_7 = new RowData();
		rowData_7.width = 188;
		clbl_Message.setLayoutData(rowData_7);
		String vtxt = "";
		try {
			vtxt = "Version " + CharStreams.toString(new InputStreamReader(this.getClass().getResourceAsStream("/txt/version.txt"), Charsets.UTF_8));
		} catch (IOException e) {
			LogController.logEvent(this,C.WARNING,e);
		}		
		clbl_Message.setText(vtxt + C.COPYRIGHT);

		// render window
		shell.setDefaultButton(btnLogin);
		LogController.log("LoginController Window Size: " + rect.width + ":" + rect.height);
		shell.setBounds(rect);	 
		shell.open();
		LogController.log("Awaiting user input...");

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}
