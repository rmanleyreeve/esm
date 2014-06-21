package com.rmrdigitalmedia.esm.forms;

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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;

public class AddAdminForm {

	Shell myshell;
	boolean formOK = false;
	Text username,password,forename,surname,rank,jobtitle,workid;
	int month, accesslevel = 9;
	Combo dd,mm,yyyy;
	// form layout  guides
	int headerH = 40;
	Label sep;

	public static void main (String [] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			AddAdminForm naf = new AddAdminForm();
			naf.complete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AddAdminForm() {
		LogController.log("Running class " + this.getClass().getName());
	}

	public boolean complete() {	

		Display display = Display.getDefault();
		final Shell shell = new Shell (display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.myshell = shell;
		shell.setSize(450, 420);
		shell.setImages(new Image[] { C.getImage(C.APP_ICON_16), C.getImage(C.APP_ICON_32) }); // 16x16 & 32x32
		shell.setText("ESM Setup");
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
		lblImg.setImage(C.getImage("user.png"));
		FormData fd_lblImg = new FormData();
		fd_lblImg.top = new FormAttachment(0);
		fd_lblImg.left = new FormAttachment(0);
		lblImg.setLayoutData(fd_lblImg);

		Label lblTitle = new Label(header, SWT.NONE);
		lblTitle.setForeground(C.APP_BGCOLOR);
		lblTitle.setFont(C.FORM_HEADER_FONT);
		FormData fd_lblTitle = new FormData();
		fd_lblTitle.top = new FormAttachment(0, 10);
		fd_lblTitle.left = new FormAttachment(lblImg, 16);
		lblTitle.setLayoutData(fd_lblTitle);
		lblTitle.setBackground(C.TITLEBAR_BGCOLOR);
		lblTitle.setText("CREATE ADMINISTRATOR");

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

		//FORM LABELS & FIELDS ==================================================================	
		Label lblUsername = new Label(form, SWT.NONE);
		lblUsername.setBackground(C.APP_BGCOLOR);
		lblUsername.setText("Username:");		
		username = new Text(form, SWT.BORDER);
		GridData gd_username = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_username.widthHint = 100;
		username.setLayoutData(gd_username);
		username.setText("admin");
		//username.setFocus();

		Label lblPassword = new Label(form, SWT.NONE);
		lblPassword.setBackground(C.APP_BGCOLOR);
		lblPassword.setText("Password:");		
		password = new Text(form, SWT.BORDER);
		GridData gd_password = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_password.widthHint = 100;
		password.setLayoutData(gd_password);
		password.setEchoChar('*');
		password.setFocus();

		Label lblForename = new Label(form, SWT.NONE);
		lblForename.setBackground(C.APP_BGCOLOR);
		lblForename.setText("Forename:");		
		forename = new Text(form, SWT.BORDER);
		GridData gd_forename = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		gd_forename.widthHint = 250;
		forename.setLayoutData(gd_forename);

		Label lblSurname = new Label(form, SWT.NONE);
		lblSurname.setBackground(C.APP_BGCOLOR);
		lblSurname.setText("Surname:");		
		surname = new Text(form, SWT.BORDER);
		GridData gd_surname = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_surname.widthHint = 250;
		surname.setLayoutData(gd_surname);

		Label lblRank = new Label(form, SWT.NONE);
		lblRank.setBackground(C.APP_BGCOLOR);
		lblRank.setText("Rank:");		
		rank = new Text(form, SWT.BORDER);
		GridData gd_rank = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_rank.widthHint = 250;
		rank.setLayoutData(gd_rank);

		Label lblJobTitle = new Label(form, SWT.NONE);
		lblJobTitle.setBackground(C.APP_BGCOLOR);
		lblJobTitle.setText("Job Title:");		
		jobtitle = new Text(form, SWT.BORDER);
		GridData gd_jobtitle = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_jobtitle.widthHint = 250;
		jobtitle.setLayoutData(gd_jobtitle);

		Label lblWorkID = new Label(form, SWT.NONE);
		lblWorkID.setBackground(C.APP_BGCOLOR);
		lblWorkID.setText("Work ID:");		
		workid = new Text(form, SWT.BORDER);
		GridData gd_workid = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_workid.widthHint = 250;
		workid.setLayoutData(gd_workid);

		Label lblDOB = new Label(form, SWT.NONE);
		lblDOB.setBackground(C.APP_BGCOLOR);
		lblDOB.setText("Date of Birth:");		
		dd = new Combo(form, SWT.DROP_DOWN | SWT.READ_ONLY);
		dd.add("DAY");
		for(int i=1;i<32;i++) {
			dd.add(""+i);
		}
		dd.select(0);
		mm = new Combo(form, SWT.DROP_DOWN | SWT.READ_ONLY);
		String[] months = {"MONTH","January" , "February" , "March" , "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		for(int i=0;i<months.length;i++) {
			mm.add(months[i]);
			mm.setData(months[i],i);
		}
		mm.select(0);
		yyyy = new Combo(form, SWT.DROP_DOWN | SWT.READ_ONLY);
		yyyy.add("YEAR");
		@SuppressWarnings("deprecation")
		int now = new Date().getYear() + 1900 - 16;
		for(int i=now;i>(now-65);i--) {
			yyyy.add(""+i);
		}
		yyyy.select(0);

		sep = new Label(form, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));		

		//==================================================================		

		Button ok = new Button (form, SWT.NONE);
		ok.setToolTipText("Click to save details");
		ok.setFont(C.FONT_10);
		ok.setText ("Submit");
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				Text[] fields = {username,password,forename,surname,rank,jobtitle,workid}; Validation.validateFields(fields);
				Combo[] dates = {dd,mm,yyyy}; Validation.validateDates(dates);			
				month = (Integer)mm.getData(mm.getText());				
				if( Validation.validateFields(fields) && Validation.validateDates(dates) ) {
					try {
						EsmUsersTable.Row row = EsmUsersTable.getRow();
						row.setUsername(username.getText());
						row.setPassword(password.getText());
						row.setForename(forename.getText());
						row.setSurname(surname.getText());
						row.setRank(rank.getText());
						row.setJobTitle(jobtitle.getText());
						row.setWorkIdentifier(workid.getText());
						row.setAccessLevel(accesslevel);
						row.setDob(yyyy.getText() + "-" + month + "-" + dd.getText());
						row.setCreatedDate(new Timestamp(new Date().getTime()));
						row.setUpdateDate(new Timestamp(new Date().getTime()));
						row.setDeleted("FALSE");
						row.insert();
						formOK = true;
						LogController.log("Admin User added to database");
						EsmApplication.appData.setField("ADMIN",username.getText());
					} catch (Exception e1) {
						LogController.logEvent(this, C.WARNING, e1);
					}					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {}
					shell.close ();
				} else {
					Validation.validateError(myshell);;
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
		LogController.log("User form closed");	
		return formOK;
	}
}
