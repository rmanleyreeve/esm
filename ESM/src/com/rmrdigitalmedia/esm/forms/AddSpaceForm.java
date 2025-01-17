package com.rmrdigitalmedia.esm.forms;

import java.io.File;
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
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.models.EntrypointsTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;
import com.rmrdigitalmedia.esm.models.VesselTable;

public class AddSpaceForm {

	Shell myshell;
	boolean formOK = false;
	Text s_name, s_description, ep_name, ep_description;
	int authorID, headerH = 40;
	private Label sep;

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main (String [] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			AddSpaceForm nsf = new AddSpaceForm(1);
			nsf.complete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AddSpaceForm(int _authorID) {
		LogController.log("Running class " + this.getClass().getName());
		authorID = _authorID;
	}	

	public boolean complete() {	

		Display display = Display.getDefault();
		final Shell shlVideotelEsm = new Shell (display, SWT.DIALOG_TRIM);
		this.myshell = shlVideotelEsm;
		shlVideotelEsm.setSize(400, 420);
		shlVideotelEsm.setText("Videotel ESM");
		shlVideotelEsm.setImages(new Image[] { C.getImage(C.APP_ICON_16), C.getImage(C.APP_ICON_32) }); // 16x16 & 32x32
		shlVideotelEsm.setLayout(new FillLayout(SWT.VERTICAL));

		Composite container = new Composite(shlVideotelEsm,SWT.NONE);
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
		lblImg.setImage(C.getImage("space_icon.png"));
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
		lblTitle.setText("ENTER SPACE / ENTRYPOINT DETAILS");

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
		Label lblSName = new Label(form, SWT.NONE);
		lblSName.setBackground(C.APP_BGCOLOR);
		lblSName.setText("Space Name:");		
		s_name = new Text(form, SWT.BORDER);
		GridData gd_name = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_name.widthHint = 230;
		s_name.setLayoutData(gd_name);
		s_name.setFocus();

		Label lblSDesc = new Label(form, SWT.NONE);
		lblSDesc.setBackground(C.APP_BGCOLOR);
		lblSDesc.setText("Space\nDescription:");	
		s_description = new Text(form, SWT.BORDER | SWT.MULTI);
		GridData gd_sdesc = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_sdesc.heightHint = 80;
		gd_sdesc.widthHint = 230;
		s_description.setLayoutData(gd_sdesc);		

		sep = new Label(form, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));		

		Label lblEName = new Label(form, SWT.NONE);
		lblEName.setBackground(C.APP_BGCOLOR);
		lblEName.setText("Entry Point Name:");		
		ep_name = new Text(form, SWT.BORDER);
		GridData gd_ename = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_ename.widthHint = 230;
		ep_name.setLayoutData(gd_ename);

		Label lblEDesc = new Label(form, SWT.NONE);
		lblEDesc.setBackground(C.APP_BGCOLOR);
		lblEDesc.setText("Entry Point\nDescription:");	
		ep_description = new Text(form, SWT.BORDER | SWT.MULTI);
		GridData gd_edesc = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_edesc.heightHint = 80;
		gd_edesc.widthHint = 230;
		ep_description.setLayoutData(gd_edesc);		

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
				Text[] fields = {s_name,s_description,ep_name,ep_description}; Validation.validateFields(fields);				
				if( Validation.validateFields(fields) ) {
					try {
						// create space DB record
						SpacesTable.Row sRow = SpacesTable.getRow();
						sRow.setName(s_name.getText());
						sRow.setDescription(s_description.getText());
						sRow.setVesselName(VesselTable.getAllRows()[0].getName());
						sRow.setAuthorID(authorID);
						sRow.setCreatedDate(new Timestamp(new Date().getTime()));
						sRow.setUpdateDate(new Timestamp(new Date().getTime()));
						sRow.setDeleted("FALSE");
						int spaceID = (int) sRow.insert();
						LogController.log("Space "+spaceID+" added to database.");
						// create empty space audit records
						EsmApplication.appData.setField("SPACE_CHK_" + spaceID,0);
						EsmApplication.appData.setField("SPACE_CLASS_" + spaceID, 0);
						EsmApplication.appData.setField("SPACE_STATUS_" + spaceID, "red");
						// create media directories
						new File( C.DOC_DIR + C.SEP + spaceID + C.SEP ).mkdir(); // docs						
						new File( C.IMG_DIR + C.SEP + spaceID + C.SEP ).mkdir(); // image base dir
						new File( C.IMG_DIR + C.SEP + spaceID + C.SEP + "full" + C.SEP ).mkdir(); // full
						new File( C.IMG_DIR + C.SEP + spaceID + C.SEP + "thumb" + C.SEP).mkdir(); // thumbs
						// create entry DB record
						EntrypointsTable.Row epRow = EntrypointsTable.getRow();
						epRow.setName(ep_name.getText());
						epRow.setDescription(ep_description.getText());
						epRow.setSpaceID(spaceID);
						epRow.setCreatedDate(new Timestamp(new Date().getTime()));
						epRow.setUpdateDate(new Timestamp(new Date().getTime()));
						epRow.setAuthorID(authorID);
						epRow.setDeleted("FALSE");
						int epID = (int) epRow.insert();
						LogController.log("Entry Point "+epID+" added to database.");				        
						// create empty entry audit records
						EsmApplication.appData.setField("ENTRY_CHK_" + epID, 0);						
						EsmApplication.appData.setField("ENTRY_CLASS_" + epID, 0);
						EsmApplication.appData.setField("ENTRY_STATUS_" + epID, "red");
						formOK = true;
					} catch (Exception e1) {
						e1.printStackTrace();
					}					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {}
					shlVideotelEsm.close ();
				} else {
					Validation.validateError(myshell);
				}
			}
		});	

		Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		Rectangle rect = shlVideotelEsm.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shlVideotelEsm.setLocation (x, y);		  		
		shlVideotelEsm.setDefaultButton (ok);		

		shlVideotelEsm.open ();
		shlVideotelEsm.layout();

		while (!shlVideotelEsm.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		LogController.log("New Space form closed");	
		return formOK;
	}
}
