package com.rmrdigitalmedia.esm.views;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;
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
import com.rmrdigitalmedia.esm.controllers.DatabaseController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.dialogs.UserAgreementDialog;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class LoginView {

	Shell shell;
	private static Text txt_Password, txt_Username;
	private Display display;
	private Rectangle rect;
	private Label alertTxt;

	public LoginView(Display display, org.eclipse.swt.graphics.Rectangle rect) {
		this.display = display;
		this.rect = rect;
		LogController.log("Running class " + this.getClass().getName());
	}

	void submit() {
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		// submit login info for verification, if OK then run main App
		String un = txt_Username.getText();
		String pw = C.doMD5(txt_Password.getText());
		shell.setCursor(new Cursor(display, SWT.CURSOR_ARROW));
		if (un != "" && pw != "") {
			LogController.log("LoginView: " + un + "/" + pw);
			if (DatabaseController.verifyLogin(un, pw)) {
				LogController.log("LoginView: LOGIN OK");
				EsmUsersTable.Row user = null;
				try {
					user = EsmUsersTable.getRow("USERNAME", un);
					if(user.getAccessLevel()==0) {
						LogController.log("LoginView: USER DISABLED");
						alertTxt.setText(C.LOGIN_ACCOUNT_DISABLED_MSG);
						txt_Username.setText("");
						txt_Password.setText("");
						return;
					}
					if(user.getLastLogin()==null) {
						// display t&c window on first login
						new UserAgreementDialog(shell);
					}
					user.setLastLogin(new Timestamp(new Date().getTime()));
					user.update();
					EsmApplication.appData.setField("ACCESS", user.getAccessLevel());
				} catch (SQLException uex) {
					LogController.logEvent(this, C.FATAL, uex);
				}
				EsmApplication.appPreLoad(user, shell);
				// LoginController will be disposed from now
			} else {
				LogController.log("LoginView: LOGIN FAILED");
				alertTxt.setText(C.LOGIN_FAIL_MSG);
				txt_Username.setText("");
				txt_Password.setText("");
				txt_Username.setFocus();
			}
		}
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createContents() {
		shell = new Shell(display, SWT.ON_TOP);
		final FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		shell.setLayout(fillLayout);
		Rectangle s = C.getImage(C.SPLASH_IMAGE).getBounds();
		shell.setSize(s.width, s.height);

		// Create a composite with grid layout.
		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		composite.setLayout(gridLayout);

		// Setting the background of the composite with the image background for
		// login dialog
		final Label img_Label = new Label(composite, SWT.NONE);
		GridData gd_img_Label = new GridData(300, 254);
		gd_img_Label.grabExcessHorizontalSpace = true;
		gd_img_Label.grabExcessVerticalSpace = true;
		img_Label.setLayoutData(gd_img_Label);
		img_Label.setBackgroundImage(C.getImage(C.SPLASH_IMAGE));

		// Creating the composite which will contain the login related widgets
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

		// Label for the heading
		final CLabel clbl_UserLogin = new CLabel(cmp_Login, SWT.NONE);
		clbl_UserLogin.setFont(C.FONT_10B);
		clbl_UserLogin.setAlignment(SWT.CENTER);
		clbl_UserLogin.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		final RowData rowData = new RowData();
		rowData.height = 42;
		rowData.width = 195;
		clbl_UserLogin.setLayoutData(rowData);
		String txt = "";
		try {
			txt += (String) EsmApplication.appData.getField("LOCATION_NAME") + "\n";
		} catch (Exception e1) {
		}
		clbl_UserLogin.setText(txt + "User Login");

		// Label for the username
		final CLabel clbl_Username = new CLabel(cmp_Login, SWT.NONE);
		clbl_Username.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		final RowData rowData_1 = new RowData();
		rowData_1.width = 180;
		clbl_Username.setLayoutData(rowData_1);
		clbl_Username.setText("Username:");

		// Textfield for the username
		txt_Username = new Text(cmp_Login, SWT.BORDER);
		final RowData rowData_2 = new RowData();
		rowData_2.width = 170;
		txt_Username.setLayoutData(rowData_2);
		txt_Username.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				if (ke.keyCode == SWT.ESC) {
					LogController.log(C.LOGIN_CANCEL_MSG);					
					shell.dispose();
				}
			}
		});

		// Label for the password
		final CLabel clbl_Password = new CLabel(cmp_Login, SWT.NONE);
		clbl_Password.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		final RowData rowData_3 = new RowData();
		rowData_3.width = 180;
		clbl_Password.setLayoutData(rowData_3);
		clbl_Password.setText("Password:");

		// Textfield for the password
		txt_Password = new Text(cmp_Login, SWT.BORDER);
		txt_Password.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				if (ke.keyCode == SWT.CR) {
					submit();
				}
				if (ke.keyCode == SWT.ESC) {
					LogController.log(C.LOGIN_CANCEL_MSG);					
					shell.dispose();
				}
			}
		});
		final RowData rowData_4 = new RowData();
		rowData_4.width = 170;
		txt_Password.setLayoutData(rowData_4);
		txt_Password.setEchoChar('*');

		// Composite to hold button
		final Composite cmp_ButtonBar = new Composite(cmp_Login, SWT.NONE);
		cmp_ButtonBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		final RowData rowData_5 = new RowData();
		rowData_5.height = 85;
		rowData_5.width = 200;
		cmp_ButtonBar.setLayoutData(rowData_5);
		cmp_ButtonBar.setLayout(new FormLayout());

		// Button for login
		final Button btnLogin = new Button(cmp_ButtonBar, SWT.FLAT | SWT.CENTER);
		btnLogin.setFont(C.FONT_8B);
		final FormData formData = new FormData();
		formData.right = new FormAttachment(100, -14);
		btnLogin.setLayoutData(formData);
		btnLogin.setText("Login");
		btnLogin.setToolTipText("Click to log in to the system");
		
		Button btnCancel = new Button(cmp_ButtonBar, SWT.NONE);
		formData.top = new FormAttachment(btnCancel, 1, SWT.TOP);
		formData.left = new FormAttachment(btnCancel, 6);
		btnCancel.setFont(C.FONT_8B);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.top = new FormAttachment(0);
		fd_btnCancel.left = new FormAttachment(0, 81);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		btnCancel.setToolTipText("Cancel login and exit");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				LogController.log(C.LOGIN_CANCEL_MSG);					
				shell.dispose();
			}
			
		});

		Composite alert = new Composite(cmp_ButtonBar, SWT.NONE);
		alert.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		FormData fd_alert = new FormData();
		fd_alert.top = new FormAttachment(0, 27);
		fd_alert.bottom = new FormAttachment(100, 3);
		fd_alert.left = new FormAttachment(0);
		fd_alert.right = new FormAttachment(100);
		alert.setLayoutData(fd_alert);
		alertTxt = new Label(alert, SWT.WRAP);
		alertTxt.setFont(C.FONT_8);
		alertTxt.setLocation(0, 0);
		alertTxt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		alertTxt.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		alertTxt.setSize(195, 60);

		btnLogin.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				submit();
			}
		});

		// Label for copyright info etc
		final CLabel clbl_Message = new CLabel(cmp_Login, SWT.NONE);
		clbl_Message.setRightMargin(0);
		clbl_Message.setLeftMargin(0);
		clbl_Message.setTopMargin(5);
		clbl_Message.setBottomMargin(0);
		clbl_Message.setFont(C.FONT_7);
		clbl_Message.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		clbl_Message.setAlignment(SWT.CENTER);
		final RowData rd_clbl_Message = new RowData();
		rd_clbl_Message.width = 190;
		clbl_Message.setLayoutData(rd_clbl_Message);
		String vtxt = "";
		try {
			vtxt = "Version " + CharStreams.toString(new InputStreamReader(this.getClass().getResourceAsStream("/txt/version.txt"), Charsets.UTF_8)).replaceAll("(\\r|\\n)", " ");
		} catch (IOException e) {
			LogController.logEvent(this, C.WARNING, e);
		}
		clbl_Message.setText(vtxt);

		// render window
		shell.setDefaultButton(btnLogin);
		//LogController.log("LoginController Window Size: " + rect.width + ":" + rect.height);
		shell.setBounds(rect);
		shell.open();
		LogController.log("Awaiting user input...");

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}
