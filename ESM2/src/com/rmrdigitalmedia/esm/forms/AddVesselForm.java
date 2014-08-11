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
import com.rmrdigitalmedia.esm.models.VesselCategoriesTable;
import com.rmrdigitalmedia.esm.models.VesselTable;
import com.rmrdigitalmedia.esm.models.VesselTypesTable;

public class AddVesselForm {

	Shell myshell;
	boolean formOK = false;
	Text name, imo, owner;
	Combo category, type;
	Label sep;	
	// form layout  guides
	int headerH = 40;
	static Object me;

	public static void main (String [] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			AddVesselForm nvf = new AddVesselForm();
			nvf.complete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AddVesselForm() {
		me = this;
		LogController.log("Running class " + this.getClass().getName());
	}	

	public boolean complete() {	

		Display display = Display.getDefault();
		final Shell shell = new Shell (display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.myshell = shell;
		shell.setSize(360, 360);
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
		lblImg.setImage(C.getImage("vessel.png"));
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
		lblTitle.setText("ENTER VESSEL/INSTALLATION DETAILS");

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
		gridLayout.numColumns = 2;
		gridLayout.horizontalSpacing = 20;
		gridLayout.verticalSpacing = 10;
		form.setLayout(gridLayout);

		//FORM LABELS & FIELDS ==================================================================	
		Label lblName = new Label(form, SWT.NONE);
		lblName.setBackground(C.APP_BGCOLOR);
		lblName.setText("Name:");		
		name = new Text(form, SWT.BORDER);
		GridData gd_name = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_name.widthHint = 240;
		name.setLayoutData(gd_name);
		name.setFocus();

		Label lblIMO = new Label(form, SWT.NONE);
		lblIMO.setBackground(C.APP_BGCOLOR);
		lblIMO.setText("IMO:");		
		imo = new Text(form, SWT.BORDER);
		GridData gd_imo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_imo.widthHint = 240;
		imo.setLayoutData(gd_imo);

		Label lblCategory = new Label(form, SWT.NONE);
		lblCategory.setBackground(C.APP_BGCOLOR);
		lblCategory.setText("Category:");
		category = new Combo(form, SWT.DROP_DOWN);
		category.add("Select...");
		category.setData("Select...", 0);
		GridData gd_category = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_category.widthHint = 240;
		category.setLayoutData(gd_category);			
		try {
			for (VesselCategoriesTable.Row cRow : VesselCategoriesTable.getRows("DELETED=FALSE ORDER BY NAME ASC")) {
				category.add(cRow.getName());
				category.setData(cRow.getName(),cRow.getID());
			}
		} catch (SQLException ex) {
			LogController.logEvent(me, C.ERROR, "getting Vessel Categories", ex);
		}
		category.select(0);

		Label lblSubType = new Label(form, SWT.NONE);
		lblSubType.setBackground(C.APP_BGCOLOR);
		lblSubType.setText("Type:");	;		
		type = new Combo(form, SWT.DROP_DOWN);
		GridData gd_type = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_type.widthHint = 240;
		type.setLayoutData(gd_type);
		type.setEnabled(false);


		category.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int cat_id = (Integer) category.getData(category.getText());
				type.removeAll();
				type.add("Select...");
				type.setData("Select...", 0);				
				if(cat_id > 0) {
					try {
						for (VesselTypesTable.Row tRow : VesselTypesTable.getRows("DELETED=FALSE AND CATEGORY_ID="+ cat_id + " ORDER BY NAME ASC")) {
							type.add(tRow.getName());
							type.setData(tRow.getName(),tRow.getID());
						}
						type.setEnabled(true);
						type.select(0);
					} catch (SQLException ex) {
						LogController.logEvent(me, C.ERROR, "getting Vessel Types", ex);
					}					
				} else {
					type.setEnabled(false);
				}
			}
		});

		Label lblOwner = new Label(form, SWT.NONE);
		lblOwner.setBackground(C.APP_BGCOLOR);
		lblOwner.setText("Owner:");	
		owner = new Text(form, SWT.BORDER | SWT.MULTI);
		GridData gd_owner = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_owner.heightHint = 60;
		gd_owner.widthHint = 240;
		owner.setLayoutData(gd_owner);		

		sep = new Label(form, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));		

		//==================================================================		

		Button ok = new Button (form, SWT.PUSH);
		ok.setToolTipText("Click to save these details");
		ok.setFont(C.FONT_10);
		ok.setText ("Submit");
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				Text[] fields = {name,imo,owner}; Validation.validateFields(fields);
				Combo[] combos = {category,type}; Validation.validateDropdowns(combos);
				if( Validation.validateFields(fields) && Validation.validateDropdowns(combos) ) {
					try {
						VesselTable.Row row = VesselTable.getRow();
						row.setID(1);
						row.setName(name.getText());
						row.setImoNumber(imo.getText());
						row.setTypeID((Integer) type.getData(type.getText()));
						row.setOwner(owner.getText());
						row.setCreatedDate(new Timestamp(new Date().getTime()));
						row.setUpdateDate(new Timestamp(new Date().getTime()));
						row.insert();						
						formOK = true;
						LogController.log("Vessel added to database");
						EsmApplication.appData.setField("LOCATION_NAME",name.getText());
						EsmApplication.appData.setField("LOCATION_TYPE", category.getText());
					} catch (Exception e1) {
						LogController.logEvent(this, C.ERROR, e1);
					}					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
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
		new Label(form, SWT.NONE);

		shell.open ();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		LogController.log("Add vessel form closed");	
		return formOK;
	}
}
