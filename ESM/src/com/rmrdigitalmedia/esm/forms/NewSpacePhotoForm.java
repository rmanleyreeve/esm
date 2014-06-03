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
import com.rmrdigitalmedia.esm.controllers.UploadController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.PhotoMetadataTable;

public class NewSpacePhotoForm {

	static Shell myshell;
	boolean formOK = false;
	Text p_title, p_comment;
	int spaceID, authorID, headerH = 40;
	private Label sep;
	static String imgToUploadPath = null, imgToUploadName = null;
	static Text imgSelected;

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main (String [] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			NewSpacePhotoForm nspf = new NewSpacePhotoForm(1,1);
			nspf.complete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public NewSpacePhotoForm(int _spaceID, int _authorID) {
		LogController.log("Running class " + this.getClass().getName());
		spaceID = _spaceID;
		authorID = _authorID;
	}

	public boolean complete() {

		Display display = Display.getDefault();
		final Shell shlVideotelEsm = new Shell (display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		NewSpacePhotoForm.myshell = shlVideotelEsm;
		shlVideotelEsm.setSize(500, 340);
		shlVideotelEsm.setText("Videotel ESM");
		shlVideotelEsm.setImages(new Image[] { C.getImage("/img/appicon16.png"), C.getImage("/img/appicon32.png") }); // 16x16 & 32x32
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
		lblImg.setImage(C.getImage("/img/space_icon.png"));
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
		lblTitle.setText("ADD SPACE PHOTO / COMMENT");

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
		Label lblUploadImg = new Label(form, SWT.NONE);
		lblUploadImg.setBackground(C.APP_BGCOLOR);
		lblUploadImg.setText("Select Photo:");
		// file upload button
		Button browseImg = new Button(form, SWT.PUSH);
		browseImg.setToolTipText("Choose an image related to this Space");
		browseImg.setText("Choose...");        
		browseImg.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] imgDetails = UploadController.uploadSpaceImageDialog();
				try {
					imgToUploadPath = imgDetails[0];
					imgToUploadName = imgDetails[1];
					imgSelected.setText(imgToUploadName);
				} catch (Exception e1) {}
			}
		});

		imgSelected = new Text(form, SWT.NONE);
		imgSelected.setEditable(false);
		imgSelected.setBackground(C.APP_BGCOLOR);
		imgSelected.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		imgSelected.setFont(C.FONT_8);

		sep = new Label(form, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		Label lblPTitle = new Label(form, SWT.NONE);
		lblPTitle.setBackground(C.APP_BGCOLOR);
		lblPTitle.setText("Photo Title:");		
		p_title = new Text(form, SWT.BORDER | SWT.MULTI);
		GridData gd_title = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_title.heightHint = 20;
		gd_title.widthHint = 400;
		p_title.setLayoutData(gd_title);
		p_title.setFocus();

		sep = new Label(form, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));		

		Label lblPComment = new Label(form, SWT.NONE);
		lblPComment.setBackground(C.APP_BGCOLOR);
		lblPComment.setText("Photo Comment:");		
		p_comment = new Text(form, SWT.BORDER | SWT.MULTI);
		GridData gd_comment = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_comment.heightHint = 80;
		gd_comment.widthHint = 400;
		p_comment.setLayoutData(gd_comment);
		p_comment.setFocus();

		sep = new Label(form, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));		

		//==================================================================		

		Button ok = new Button (form, SWT.PUSH);
		ok.setToolTipText("Click to save your Photo");
		ok.setFont(C.FONT_10);
		ok.setText ("Submit");
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				Text[] fields = {p_title, p_comment,imgSelected}; Validation.validateFields(fields);				
				String path = "";
				if( Validation.validateFields(fields) ) {
					try {
						path = UploadController.uploadSpaceImagePath(spaceID, new String[]{imgToUploadPath,imgToUploadName});
					} catch (Exception ex) {}
					if(!path.equals("")) {
						try {
							PhotoMetadataTable.Row pRow = PhotoMetadataTable.getRow();
							pRow.setSpaceID(spaceID);
							pRow.setTitle(p_title.getText());
							pRow.setComment(p_comment.getText());
							pRow.setAuthorID(authorID);
							pRow.setPath(path);
							pRow.setCreatedDate(new Timestamp(new Date().getTime()));
							pRow.setUpdateDate(new Timestamp(new Date().getTime()));
							if( EsmUsersTable.getRow("ID", ""+authorID).getAccessLevel() >= 2 ) {
								pRow.setApproved("TRUE");
							}
							pRow.setDeleted("FALSE");
							pRow.insert();
							LogController.log("Photo comment added to database.");		        
						} catch (Exception e1) {
							LogController.logEvent("Photo Comment upload", 1, e1);
						}		
						formOK = true;
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
		new Label(form, SWT.NONE);
		new Label(form, SWT.NONE);

		shlVideotelEsm.open ();
		shlVideotelEsm.layout();

		while (!shlVideotelEsm.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		LogController.log("New Space Photo form closed");	

		return formOK;
	}

}
