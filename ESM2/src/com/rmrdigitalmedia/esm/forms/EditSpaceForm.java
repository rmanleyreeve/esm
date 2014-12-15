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
import com.rmrdigitalmedia.esm.models.SpacesTable;

public class EditSpaceForm {

	Shell myshell;
	boolean formOK = false;
	Text s_name, s_description;
	int spaceID, headerH = 40;
	private Label sep;
	SpacesTable.Row sRow;
	private static Object me;

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main (String [] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			EditSpaceForm esf = new EditSpaceForm(1);
			esf.complete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EditSpaceForm(int _spaceID) {
		me = this;
		LogController.log("Running class " + this.getClass().getName());
		spaceID = _spaceID;
	}	

	public boolean complete() {	

		Display display = Display.getDefault();
		final Shell shell = new Shell (display, SWT.DIALOG_TRIM);
		this.myshell = shell;
		shell.setSize(400, 280);
		shell.setText(C.SHELL_TITLE);
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
		lblImg.setImage(C.getImage("space_icon.png"));
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
		lblTitle.setText("EDIT SPACE DETAILS");

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

		try {
			sRow = SpacesTable.getRow(spaceID);
		} catch (SQLException e2) {
			LogController.logEvent(me, C.ERROR, e2);
		}

		//FORM LABELS & FIELDS ==================================================================	
		Label lblSName = new Label(form, SWT.NONE);
		lblSName.setBackground(C.APP_BGCOLOR);
		lblSName.setText("Space Name:");		
		s_name = new Text(form, SWT.BORDER);
		s_name.setText(sRow.getName());
		GridData gd_name = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_name.widthHint = 230;
		s_name.setLayoutData(gd_name);
		s_name.setFocus();

		Label lblSDesc = new Label(form, SWT.NONE);
		lblSDesc.setBackground(C.APP_BGCOLOR);
		lblSDesc.setText("Space\nDescription:");	
		s_description = new Text(form, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		s_description.setText(sRow.getDescription());
		GridData gd_sdesc = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_sdesc.heightHint = 80;
		gd_sdesc.widthHint = 230;
		s_description.setLayoutData(gd_sdesc);

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
				Text[] fields = {s_name,s_description}; Validation.validateFields(fields);				
				if( Validation.validateFields(fields) ) {
					try {
						sRow.setName(s_name.getText());
						sRow.setDescription(s_description.getText());
						sRow.setUpdateDate(new Timestamp(new Date().getTime()));
						sRow.update();
						LogController.log("Space "+ spaceID +" details updated");
						formOK = true;
					} catch (Exception e1) {
						e1.printStackTrace();
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

		shell.setDefaultButton(ok);
		Button cancel = new Button (form, SWT.NONE);
		cancel.setFont(C.FONT_10);
		cancel.setText ("Cancel");
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.dispose();
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
		LogController.log("Edit Space form closed");	
		return formOK;
	}
}
