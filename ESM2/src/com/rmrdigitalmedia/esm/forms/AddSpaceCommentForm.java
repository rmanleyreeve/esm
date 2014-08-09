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
import com.rmrdigitalmedia.esm.models.SpaceCommentsTable;

public class AddSpaceCommentForm {

	static Shell myshell;
	boolean formOK = false;
	Text s_comment;
	int spaceID, authorID, headerH = 40;
	private Label sep;

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main (String [] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			AddSpaceCommentForm nscf = new AddSpaceCommentForm(1,1);
			nscf.complete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AddSpaceCommentForm(int _spaceID, int _authorID) {
		LogController.log("Running class " + this.getClass().getName());
		spaceID = _spaceID;
		authorID = _authorID;
	}

	public boolean complete() {

		Display display = Display.getDefault();
		final Shell shlVideotelEsm = new Shell (display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		AddSpaceCommentForm.myshell = shlVideotelEsm;
		shlVideotelEsm.setSize(500, 250);
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
		lblTitle.setText("ENTER SPACE COMMENT");

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
		gridLayout.numColumns = 3;
		gridLayout.horizontalSpacing = 20;
		gridLayout.verticalSpacing = 10;
		form.setLayout(gridLayout);

		//FORM LABELS & FIELDS ==================================================================	
		Label lblSComment = new Label(form, SWT.NONE);
		lblSComment.setBackground(C.APP_BGCOLOR);
		lblSComment.setText("Space\nComment:");		
		s_comment = new Text(form, SWT.BORDER | SWT.MULTI);
		GridData gd_comment = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_comment.heightHint = 100;
		gd_comment.widthHint = 400;
		s_comment.setLayoutData(gd_comment);
		s_comment.setFocus();

		sep = new Label(form, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));		


		//==================================================================		

		Button ok = new Button (form, SWT.PUSH);
		ok.setToolTipText("Click to save your Comment");
		ok.setFont(C.FONT_10);
		ok.setText ("Submit");
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				Text[] fields = {s_comment}; Validation.validateFields(fields);				
				if( Validation.validateFields(fields) ) {
					try {
						SpaceCommentsTable.Row sRow = SpaceCommentsTable.getRow();
						sRow.setSpaceID(spaceID);
						sRow.setComment(s_comment.getText());
						sRow.setAuthorID(authorID);
						sRow.setCreatedDate(new Timestamp(new Date().getTime()));
						sRow.setUpdateDate(new Timestamp(new Date().getTime()));
						boolean unmod = (EsmUsersTable.getRow("ID", ""+authorID).getAccessLevel() >= 2);
						if(unmod) {
							sRow.setApproved("TRUE");
						} else {
							sRow.setApproved("FALSE");
						}
						sRow.setDeleted("FALSE");
						sRow.insert();
						LogController.log("Space comment added to database.");		        
						formOK = true;
						if(!unmod) {
							EsmApplication.alert("Your comment has been added and will be published when approved.");
						}
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
		LogController.log("New Space Comment form closed");	

		return formOK;
	}

}