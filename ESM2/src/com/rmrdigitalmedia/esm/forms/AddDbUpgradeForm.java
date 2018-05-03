package com.rmrdigitalmedia.esm.forms;

import java.io.File;
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
import com.rmrdigitalmedia.esm.controllers.DatabaseController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.UploadController;

public class AddDbUpgradeForm {

	static Shell myshell;
	boolean formOK = false;
	int spaceID, authorID, headerH = 40;
	private Label sep;
	static String imgToUploadPath = null, imgToUploadName = null;
	static Text fileSelected;

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main (String [] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			AddDbUpgradeForm nduf = new AddDbUpgradeForm(1);
			nduf.complete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AddDbUpgradeForm(int _authorID) {
		LogController.log("Running class " + this.getClass().getName());
		authorID = _authorID;
	}

	public boolean complete() {

		Display display = Display.getDefault();
		final Shell shell = new Shell (display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		AddDbUpgradeForm.myshell = shell;
		shell.setSize(350, 180);
		shell.setText("Videotel ESM");
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
		lblTitle.setText("SELECT DATABASE UPGRADE FILE");

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
		Label lblUploadFile = new Label(form, SWT.NONE);
		lblUploadFile.setBackground(C.APP_BGCOLOR);
		lblUploadFile.setText("Select File:");
		// file upload button
		Button browseFile = new Button(form, SWT.PUSH);
		browseFile.setToolTipText("Choose an image related to this Space");
		browseFile.setText("Choose...");        
		browseFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] imgDetails = UploadController.uploadSpaceImageDialog();
				try {
					imgToUploadPath = imgDetails[0];
					imgToUploadName = imgDetails[1];
					fileSelected.setText(imgToUploadName);
				} catch (Exception e1) {}
			}
		});

		fileSelected = new Text(form, SWT.NONE);
		fileSelected.setEditable(false);
		fileSelected.setBackground(C.APP_BGCOLOR);
		fileSelected.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		fileSelected.setFont(C.FONT_8);

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
					try {
						formOK = DatabaseController.upgradeDatabase(new File(imgToUploadPath));
					} catch (Exception ex) {
						LogController.logEvent(new DatabaseController(), C.ERROR, "method 'databaseUpgrade' threw exception:", ex);
						ex.printStackTrace();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {}
					shell.close ();
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
		LogController.log("Database Upgrade form closed");	

		return formOK;
	}

}
