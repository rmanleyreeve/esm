package com.rmrdigitalmedia.esm.views;

import java.sql.SQLException;

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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.forms.AddUserForm;
import com.rmrdigitalmedia.esm.forms.DeleteUserDialog;
import com.rmrdigitalmedia.esm.forms.EditUserForm;
import com.rmrdigitalmedia.esm.forms.EditVesselForm;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;

public class AdministrationView {

	private static Label sep;
	static EsmUsersTable.Row user = WindowController.user;
	static int selectedUser = 0;

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			shell.setSize(1380, 750);
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			Composite comp = new Composite(shell, SWT.BORDER);
			user = EsmUsersTable.getRow(1);
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

	public static void buildPage(Composite parent) {

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


		// row 1 - vessel management		
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
		lblVessel.setText("Manage Vessel Info");	
		
		Button btnAddVessel = new Button(rowVessel, SWT.NONE);
		btnAddVessel.setToolTipText("Edit Vessel Information");
		GridData gd_btnAddVessel = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		gd_btnAddVessel.verticalIndent = 3;
		btnAddVessel.setLayoutData(gd_btnAddVessel);
		btnAddVessel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				EditVesselForm evf = new EditVesselForm();					
				if(evf.complete()) {
					LogController.log("Vessel edited in database");
					WindowController.showAdministration();				
				}
			}
		});
		btnAddVessel.setImage(C.getImage("16_edit.png"));
		btnAddVessel.setText("Edit Vessel Info");

		sep = new Label(rowVessel, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));	
		
		

		// row 2 - user management		
		Group rowUsers = new Group(compL, SWT.NONE);
		rowUsers.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_rowUsers = new GridLayout(3, true);
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
		lblUsers.setImage(C.getImage("users.png"));
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
		btnAddUser.setImage(C.getImage("16_add.png"));
		btnAddUser.setText("Add User");

		sep = new Label(rowUsers, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));	

		Label lblSelect = new Label(rowUsers, SWT.NONE);
		lblSelect.setText("Manage Existing User:");
		lblSelect.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		final Combo comboUsers = new Combo(rowUsers, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboUsers.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		refreshCombo(comboUsers);

		final Button btnViewUser = new Button(rowUsers, SWT.NONE);
		btnViewUser.setEnabled(false);
		btnViewUser.setText("View");
		final Button btnEditUser = new Button(rowUsers, SWT.NONE);
		btnEditUser.setEnabled(false);
		btnEditUser.setText("Edit");
		final Button btnDelUser = new Button(rowUsers, SWT.NONE);
		btnDelUser.setEnabled(false);
		btnDelUser.setText("Delete");

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
					btnViewUser.setEnabled(false);
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
					btnViewUser.setEnabled(false);
					btnEditUser.setEnabled(false);
					btnDelUser.setEnabled(false);
				} else {
					LogController.log("User " + selectedUser + " not deleted");
				}
			}			
		});




		sep = new Label(rowUsers, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));	


		comboUsers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectedUser = (Integer) comboUsers.getData(comboUsers.getText());
				LogController.log("Selected user ID: "+selectedUser);
				//btnViewUser.setEnabled(selectedUser > 0);
				btnEditUser.setEnabled(selectedUser > 0);
				btnDelUser.setEnabled(selectedUser > 0);
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

		// row 1 - name, id, description fields
		Group rowRight1 = new Group(compR, SWT.NONE);
		rowRight1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_rowRight1 = new GridLayout(2, false);
		gl_rowRight1.marginBottom = 5;
		gl_rowRight1.marginHeight = 0;
		rowRight1.setLayout(gl_rowRight1);
		rowRight1.setBackground(C.APP_BGCOLOR);


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
		panels.setWeights(new int[] { 1, 2 });
		parent.layout();
		parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_ARROW));
	}
}
