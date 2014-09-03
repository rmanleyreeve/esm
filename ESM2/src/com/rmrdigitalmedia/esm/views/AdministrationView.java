package com.rmrdigitalmedia.esm.views;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import com.rmrdigitalmedia.esm.AppData;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.DatabaseController;
import com.rmrdigitalmedia.esm.controllers.InternetController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.forms.AddUserForm;
import com.rmrdigitalmedia.esm.forms.ApprovePhotoCommentForm;
import com.rmrdigitalmedia.esm.forms.DeleteUserDialog;
import com.rmrdigitalmedia.esm.forms.EditAdminForm;
import com.rmrdigitalmedia.esm.forms.EditUserForm;
import com.rmrdigitalmedia.esm.forms.EditUserPasswordForm;
import com.rmrdigitalmedia.esm.forms.EditVesselForm;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.PhotoMetadataTable;
import com.rmrdigitalmedia.esm.models.SpaceCommentsTable;

public class AdministrationView {

	private static Label sep;
	static EsmUsersTable.Row user = WindowController.user;
	static int selectedUser = 0;
	static SpaceCommentsTable.Row[] _rows;

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			shell.setSize(1380, 750);
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			Composite comp = new Composite(shell, SWT.BORDER);
			user = EsmUsersTable.getRow(1);
			EsmApplication.appData = new AppData();
			AdministrationView.buildPage(comp);
			shell.open();
			while (!shell.isDisposed()) {
				if (!Display.getDefault().readAndDispatch()) {
					Display.getDefault().sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void refreshCombo(Combo combo) {
		combo.removeAll();
		combo.add("Select...");
		combo.setData("Select...", 0);
		EsmUsersTable.Row[] uRows = null;
		try {
			uRows = EsmUsersTable.getRows("DELETED=FALSE AND ID <> "+user.getID());
		} catch (SQLException ex) { }
		for(EsmUsersTable.Row uRow:uRows) {
			String un = uRow.getSurname().toUpperCase() + ", " + uRow.getForename() + " : "+ uRow.getRank();
			combo.add(un);
			combo.setData(un, uRow.getID());
		}
		combo.select(0);
	}

	static void refreshPhotoCommentsList(List list){
		list.setVisible(false);
		list.removeAll();
		PhotoMetadataTable.Row[] cRows = null;
		try {
			cRows = PhotoMetadataTable.getRows("DELETED=FALSE AND APPROVED=FALSE");
		} catch (SQLException ex) {
			LogController.logEvent(AdministrationView.class, C.ERROR, ex);
		}
		if(cRows != null && cRows.length>0) {
			int c = 0;
			for(PhotoMetadataTable.Row cRow:cRows) {
				int cID = cRow.getID();
				String author = "";
				try {
					author = EsmUsersTable.getRow(cRow.getAuthorID()).getSurname().toUpperCase() + ", " + EsmUsersTable.getRow(cRow.getAuthorID()).getForename();
				} catch (SQLException ex) {	}
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
				list.add("#" + cID + ": Posted " + sdf.format(cRow.getUpdateDate()) + " by " + author );
				list.setData(""+c, cID);
				c++;
			}
			list.setVisible(true);
			list.redraw();
		}
	}

	public static void buildPage(final Composite parent) {
		LogController.log("Building Administration page");

		for (Control c : parent.getChildren()) {
			c.dispose();
		}

		parent.setLayout(new FillLayout(SWT.VERTICAL));
		SashForm panels = new SashForm(parent, SWT.NONE);
		panels.setBackground(C.TITLEBAR_BGCOLOR);
		panels.setLayout(new FillLayout());

		// scrolling frame to hold the LH panel
		final ScrolledComposite scrollPanelLeft = new ScrolledComposite(panels, SWT.V_SCROLL | SWT.BORDER);

		// the panel that holds the various info rows
		final Composite compL = new Composite(scrollPanelLeft, SWT.NONE);
		GridLayout gl_compL = new GridLayout(1, true);
		gl_compL.marginBottom = 50;
		gl_compL.marginRight = 10;
		compL.setLayout(gl_compL);
		compL.setBackground(C.APP_BGCOLOR);


		// row - vessel management		
		final String type = (String) EsmApplication.appData.getField("LOCATION_TYPE");
		Group rowVessel = new Group(compL, SWT.NONE);
		rowVessel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_rowVessel = new GridLayout(3, true);
		gl_rowVessel.marginBottom = 5;
		gl_rowVessel.marginHeight = 0;
		rowVessel.setLayout(gl_rowVessel);
		rowVessel.setBackground(C.APP_BGCOLOR);

		CLabel lblVessel = new CLabel(rowVessel, SWT.NONE);
		GridData gd_lblVessel = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_lblVessel.widthHint = 120;
		lblVessel.setLayoutData(gd_lblVessel);
		lblVessel.setFont(C.FONT_12B);
		lblVessel.setBackground(C.APP_BGCOLOR);
		lblVessel.setImage(C.getImage("vessel.png"));
		lblVessel.setText("Manage " + type + " Details");	

		Button btnAddVessel = new Button(rowVessel, SWT.NONE);
		btnAddVessel.setToolTipText("Edit " + type + " Details");
		GridData gd_btnAddVessel = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		gd_btnAddVessel.verticalIndent = 3;
		btnAddVessel.setLayoutData(gd_btnAddVessel);
		btnAddVessel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				EditVesselForm evf = new EditVesselForm();					
				if(evf.complete()) {
					LogController.log(type + " edited in database");
					WindowController.setHeaderLabelText();
					WindowController.showAdministration();				
				}
			}
		});
		btnAddVessel.setImage(C.getImage("edit.png"));
		btnAddVessel.setText("Edit " + type + " Details");

		// row - admin management		
		Group rowUser = new Group(compL, SWT.NONE);
		rowUser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_rowUser = new GridLayout(3, false);
		gl_rowUser.marginBottom = 5;
		gl_rowUser.marginHeight = 0;
		rowUser.setLayout(gl_rowUser);
		rowUser.setBackground(C.APP_BGCOLOR);

		CLabel lblUser = new CLabel(rowUser, SWT.NONE);
		GridData gd_lblUser = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_lblUser.widthHint = 120;
		lblUser.setLayoutData(gd_lblUser);
		lblUser.setFont(C.FONT_12B);
		lblUser.setBackground(C.APP_BGCOLOR);
		lblUser.setImage(C.getImage("users_icon.png"));
		lblUser.setText("My Details");	

		Button btnEditMe = new Button(rowUser, SWT.NONE);
		btnEditMe.setToolTipText("Edit my Details");
		GridData gd_btnEditMe = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnEditMe.verticalIndent = 3;
		btnEditMe.setLayoutData(gd_btnEditMe);
		btnEditMe.setImage(C.getImage("add.png"));
		btnEditMe.setText("Edit My Details");
		btnEditMe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				EditAdminForm eaf = new EditAdminForm(user.getID());
				if(eaf.complete()) {
					EsmApplication.alert("Your user record was updated!");
				}
			}
		});

		Button btnEditMyPass = new Button(rowUser, SWT.NONE);
		btnEditMyPass.setToolTipText("Edit my Password");
		GridData gd_btnEditMyPass = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_btnEditMyPass.horizontalIndent = 10;
		gd_btnEditMyPass.verticalIndent = 3;
		btnEditMyPass.setLayoutData(gd_btnEditMyPass);
		btnEditMyPass.setImage(C.getImage("add.png"));
		btnEditMyPass.setText("Change Password");
		btnEditMyPass.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				EditUserPasswordForm eupf = new EditUserPasswordForm(user.getID());
				if(eupf.complete()) {
					EsmApplication.alert("Your user password was updated!");
				}
			}
		});
		
		// row - user management		
		Group rowUsers = new Group(compL, SWT.NONE);
		rowUsers.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_rowUsers = new GridLayout(3, false);
		gl_rowUsers.marginBottom = 5;
		gl_rowUsers.marginHeight = 0;
		rowUsers.setLayout(gl_rowUsers);
		rowUsers.setBackground(C.APP_BGCOLOR);

		CLabel lblUsers = new CLabel(rowUsers, SWT.NONE);
		GridData gd_lblUsers = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_lblUsers.widthHint = 120;
		lblUsers.setLayoutData(gd_lblUsers);
		lblUsers.setFont(C.FONT_12B);
		lblUsers.setBackground(C.APP_BGCOLOR);
		lblUsers.setImage(C.getImage("users_icon.png"));
		lblUsers.setText("Manage Users");	

		Button btnAddUser = new Button(rowUsers, SWT.NONE);
		btnAddUser.setToolTipText("Add a new user to the system");
		GridData gd_btnAddUser = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		gd_btnAddUser.verticalIndent = 3;
		btnAddUser.setLayoutData(gd_btnAddUser);
		btnAddUser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				AddUserForm auf = new AddUserForm();					
				if(auf.complete()) {
					LogController.log("New User saved in database");
					WindowController.showAdministration();				
				}
			}
		});
		btnAddUser.setImage(C.getImage("add.png"));
		btnAddUser.setText("Add User");

		sep = new Label(rowUsers, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));	

		Label lblSelect = new Label(rowUsers, SWT.NONE);
		lblSelect.setText("Manage Existing User:");
		lblSelect.setBackground(C.APP_BGCOLOR);

		final Combo comboUsers = new Combo(rowUsers, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboUsers.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		refreshCombo(comboUsers);	
		
		final Button btnEditUser = new Button(rowUsers, SWT.NONE);
		btnEditUser.setToolTipText("Edit the details for this User");
		btnEditUser.setEnabled(false);
		btnEditUser.setText("Edit Details");

		final Button btnEditUserPass = new Button(rowUsers, SWT.NONE);
		btnEditUserPass.setToolTipText("Change the password for this User");
		btnEditUserPass.setEnabled(false);
		btnEditUserPass.setText("Change Password");
		
		final Button btnDelUser = new Button(rowUsers, SWT.NONE);
		btnDelUser.setToolTipText("Delete this User from the system");
		GridData gd_btnDelUser = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDelUser.horizontalIndent = 50;
		btnDelUser.setLayoutData(gd_btnDelUser);
		btnDelUser.setEnabled(false);
		btnDelUser.setText("Delete");
		
		// change password button behaviour
		btnEditUserPass.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				EditUserPasswordForm eupf = new EditUserPasswordForm(selectedUser);
				if(eupf.complete()) {
					EsmApplication.alert("The user password was updated!");
				}
			
			}			
		});
		// edit button behaviour
		btnEditUser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				EditUserForm euf = new EditUserForm(selectedUser);
				if(euf.complete()) {
					EsmApplication.alert("The user record was updated!");
					refreshCombo(comboUsers);
					btnEditUser.setEnabled(false);
					btnDelUser.setEnabled(false);
				}
			}
		});
		// delete button behaviour
		btnDelUser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				DeleteUserDialog dud = new DeleteUserDialog();					
				if(dud.deleteOK(selectedUser)) {
					LogController.log("User " + selectedUser + " marked as deleted in database");
					EsmApplication.alert("The user record was deleted!");
					refreshCombo(comboUsers);
					btnEditUser.setEnabled(false);
					btnDelUser.setEnabled(false);
				} else {
					LogController.log("User " + selectedUser + " not deleted");
				}
			}			
		});
		// dropdown behaviour
		comboUsers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectedUser = (Integer) comboUsers.getData(comboUsers.getText());
				LogController.log("Selected user ID: "+selectedUser);
				btnEditUser.setEnabled(selectedUser > 0);
				btnEditUserPass.setEnabled(selectedUser > 0);
				btnDelUser.setEnabled(selectedUser > 0);
			}
		});


		// row - comment management		
		Group rowComments = new Group(compL, SWT.NONE);
		rowComments.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_rowComments = new GridLayout(4, false);
		gl_rowComments.marginBottom = 5;
		gl_rowComments.marginHeight = 0;
		rowComments.setLayout(gl_rowComments);
		rowComments.setBackground(C.APP_BGCOLOR);

		CLabel lblComments = new CLabel(rowComments, SWT.NONE);
		lblComments.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		lblComments.setFont(C.FONT_12B);
		lblComments.setBackground(C.APP_BGCOLOR);
		lblComments.setImage(C.getImage("comments_icon.png"));
		lblComments.setText("Manage Comments");	

		Label lblSpaceComments = new Label(rowComments, SWT.NONE);
		lblSpaceComments.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1));
		lblSpaceComments.setText("Space Comments Requiring Approval:");
		lblSpaceComments.setBackground(C.APP_BGCOLOR);

		final List listSpaceComments = new List(rowComments, SWT.BORDER | SWT.V_SCROLL);
		GridData gd_listSpaceComments = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		SpaceCommentsTable.Row[] cRows = null;
		try {
			cRows = SpaceCommentsTable.getRows("DELETED=FALSE AND APPROVED=FALSE");
		} catch (SQLException ex) {
			LogController.logEvent(AdministrationView.class, C.ERROR, ex);
		}
		if(cRows != null && cRows.length>0) {
			int c = 0;
			for(SpaceCommentsTable.Row cRow:cRows) {
				int cID = cRow.getID();
				String author = "";
				try {
					author = EsmUsersTable.getRow(cRow.getAuthorID()).getSurname().toUpperCase() + ", " + EsmUsersTable.getRow(cRow.getAuthorID()).getForename();
				} catch (SQLException ex) {	}
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
				listSpaceComments.add("#" + cID + ": Posted " + sdf.format(cRow.getUpdateDate()) + " by " + author );
				listSpaceComments.setData(""+c, cID);
				c++;
			}
		}
		if(cRows.length > 10) {
			gd_listSpaceComments.heightHint = (cRows.length * 20);
		}
		gd_listSpaceComments.exclude = (cRows.length==0);
		listSpaceComments.setLayoutData(gd_listSpaceComments);

		final Button viewBtn = new Button(rowComments, SWT.NONE);
		viewBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int sel = listSpaceComments.getSelectionIndex();
				int id = (Integer) listSpaceComments.getData(""+sel);
				try {
					WindowController.showSpaceDetail(SpaceCommentsTable.getRow(id).getSpaceID());
				} catch (SQLException ex) {
					//
				}

			}
		});
		viewBtn.setAlignment(SWT.LEFT);
		viewBtn.setEnabled(false);
		viewBtn.setText("View Comment");
		GridData gd_viewBtn = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_viewBtn.exclude = (cRows.length==0);
		viewBtn.setLayoutData(gd_viewBtn);

		listSpaceComments.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				int sel = listSpaceComments.getSelectionIndex();
				int id = (Integer) listSpaceComments.getData(""+sel);
				try {
					WindowController.showSpaceDetail(SpaceCommentsTable.getRow(id).getSpaceID());
				} catch (SQLException ex) {
					//
				}
				/*
				ApproveSpaceCommentForm ascf = new ApproveSpaceCommentForm(id);
				if(ascf.complete()) {
					EsmApplication.alert("The comment was approved!");
					refreshSpaceCommentsList(listSpaceComments);
				}
				 */
			}
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				viewBtn.setEnabled(true);
			}
		});


		Label lblPhotoComments = new Label(rowComments, SWT.NONE);
		lblPhotoComments.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1));
		lblPhotoComments.setText("Photo Comments Requiring Approval:");
		lblPhotoComments.setBackground(C.APP_BGCOLOR);

		final List listPhotoComments = new List(rowComments, SWT.BORDER | SWT.V_SCROLL);
		listPhotoComments.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1));
		listPhotoComments.setVisible(false);
		refreshPhotoCommentsList(listPhotoComments);
		listPhotoComments.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				int sel = listPhotoComments.getSelectionIndex();
				int id = (Integer) listPhotoComments.getData(""+sel);
				ApprovePhotoCommentForm apcf = new ApprovePhotoCommentForm(id);
				if(apcf.complete()) {
					EsmApplication.alert("The comment was approved!");
					refreshPhotoCommentsList(listPhotoComments);
				}
			}
		});












		scrollPanelLeft.setContent(compL);
		scrollPanelLeft.setExpandVertical(true);
		scrollPanelLeft.setExpandHorizontal(true);
		scrollPanelLeft.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle r = scrollPanelLeft.getClientArea();
				scrollPanelLeft.setMinSize(compL.computeSize(r.width, SWT.DEFAULT));
			}
		});

		//===================================================================================================================

		// scrolling frame to hold the RH panel
		final ScrolledComposite scrollPanelRight = new ScrolledComposite(panels, SWT.V_SCROLL | SWT.BORDER);

		// the panel that holds the various info rows
		final Composite compR = new Composite(scrollPanelRight, SWT.NONE);
		GridLayout gl_compR = new GridLayout(1, true);
		gl_compR.horizontalSpacing = 0;
		gl_compR.marginLeft = 10;
		compR.setLayout(gl_compR);
		compR.setBackground(C.APP_BGCOLOR);

		// row - DB export
		Group rowRight1 = new Group(compR, SWT.NONE);
		rowRight1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_rowRight1 = new GridLayout(2, false);
		gl_rowRight1.marginBottom = 5;
		gl_rowRight1.marginHeight = 0;
		rowRight1.setLayout(gl_rowRight1);
		rowRight1.setBackground(C.APP_BGCOLOR);

		CLabel lblDB = new CLabel(rowRight1, SWT.NONE);
		gd_lblVessel.widthHint = 120;
		lblDB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		lblDB.setFont(C.FONT_12B);
		lblDB.setBackground(C.APP_BGCOLOR);
		lblDB.setImage(C.getImage("db.png"));
		lblDB.setText("Data Export Functions");	

		// db dump
		Button btnDump = new Button(rowRight1, SWT.NONE);
		btnDump.setText("Database Backup");
		btnDump.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (DatabaseController.dumpDatabase()) {
					//Program.launch(C.TMP_DIR);
					EsmApplication.alert("Backup completed successfully");
				} else {
					LogController.logEvent(this, C.ERROR, "Error dumping data");
				}

			}
		});
		Label lblDump = new Label(rowRight1, SWT.NONE);
		lblDump.setText("Create a complete backup of the database");
		lblDump.setBackground(C.APP_BGCOLOR);
		btnDump.setToolTipText(lblDump.getText());

		// create local file
		Button btnExport = new Button(rowRight1, SWT.NONE);
		btnExport.setText("Data Export File");
		btnExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (DatabaseController.generateZipFile().exists()) {
					EsmApplication.alert("Data Export file created successfully");
					Program.launch(C.TMP_DIR);
				} else {
					LogController.logEvent(this, C.ERROR, "Error exporting data");
				}

			}
		});
		Label lblExport = new Label(rowRight1, SWT.NONE);
		lblExport.setText("Generate a data export file to send manually by email");
		lblExport.setBackground(C.APP_BGCOLOR);
		btnExport.setToolTipText(lblExport.getText());

		// create file and ftp to server
		Button btnSend = new Button(rowRight1, SWT.NONE);
		btnSend.setText("Send Data File");
		btnSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_WAIT));
				if (InternetController.checkNetAccess()) {
					File f = DatabaseController.generateZipFile();
					if (f.exists()) {					
						if(InternetController.uploadFileFTP(f.getPath(), f.getName())) {
							EsmApplication.alert("File uploaded successfully");
						} else {
							LogController.logEvent(this, C.ERROR, "Error uploading file");
						}					
					} else {
						LogController.logEvent(this, C.ERROR, "Data file missing");
					}
				} else {
					LogController.logEvent(this, C.ERROR, "No internet connection for FTP export");
					EsmApplication.alert("ERROR: No Internet connection");
				}
				parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_ARROW));
			}
		});
		Label lblSend = new Label(rowRight1, SWT.NONE);
		lblSend.setText("Generate data export file and send to Videotel server");
		lblSend.setBackground(C.APP_BGCOLOR);
		btnSend.setToolTipText(lblSend.getText());






		scrollPanelRight.setContent(compR);
		scrollPanelRight.setExpandVertical(true);
		scrollPanelRight.setExpandHorizontal(true);
		scrollPanelRight.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle r = scrollPanelRight.getClientArea();
				scrollPanelRight.setMinSize(compR.computeSize(r.width, SWT.DEFAULT));
			}
		});


		//===================================================================================================================


		// final layout settings
		panels.setWeights(new int[] { 10, 10 });
		parent.layout();
		parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_ARROW));
	}
}
