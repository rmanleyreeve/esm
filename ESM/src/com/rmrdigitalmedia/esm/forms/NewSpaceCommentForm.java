package com.rmrdigitalmedia.esm.forms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.FilesystemController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.models.SpaceCommentsTable;

public class NewSpaceCommentForm {

	static Shell myshell;
	boolean formOK = false;
	Text s_comment;
	int spaceID;
	int authorID;
	// form layout  guides
	int headerH = 40;
	private Label sep;
	static String imgToUploadPath;
	static String imgToUploadName;
	private static Text imgSelected;

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main (String [] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			NewSpaceCommentForm nscf = new NewSpaceCommentForm(1,1);
			nscf.complete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public NewSpaceCommentForm(int _spaceID, int _authorID) {
		LogController.log("Running class " + this.getClass().getName());
		spaceID = _spaceID;
		authorID = _authorID;
	}

	public static void uploadImage() {	
		final FileDialog dialog = new FileDialog (myshell, SWT.OPEN);
		dialog.setText("Choose an image");
		String platform = SWT.getPlatform();
		String [] filterNames = new String [] {"Image Files", "All Files (*)"};
		String [] filterExtensions = new String [] {"*.gif;*.png;*.xpm;*.jpg;*.jpeg;*.tiff", "*"};
		String filterPath = C.HOME_DIR + C.SEP + "Desktop";
		if (platform.equals("win32") || platform.equals("wpf")) {
			filterNames = new String [] {"Image Files", "All Files (*.*)"};
			filterExtensions = new String [] {"*.gif;*.png;*.bmp;*.jpg;*.jpeg;*.tiff", "*.*"};
		}
		dialog.setFilterNames (filterNames);
		dialog.setFilterExtensions (filterExtensions);
		dialog.setFilterPath (filterPath);
		dialog.open();		
		final String fn = dialog.getFilterPath() + C.SEP + dialog.getFileName();		
		if(!dialog.getFileName().equals("")) {
			imgToUploadPath = fn;
			imgToUploadName = dialog.getFileName();
			imgSelected.setText(imgToUploadName);
		}
	}		

	public boolean complete() {
		
		Display display = Display.getDefault();
		final Shell shell = new Shell (display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.ON_TOP);
		NewSpaceCommentForm.myshell = shell;
		shell.setSize(500, 300);
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
		lblSComment.setText("Comment:");		
		s_comment = new Text(form, SWT.BORDER | SWT.MULTI);
		GridData gd_comment = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_comment.heightHint = 100;
		gd_comment.widthHint = 400;
		s_comment.setLayoutData(gd_comment);
		s_comment.setFocus();
		
		sep = new Label(form, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));		
		
		Label lblUploadImg = new Label(form, SWT.NONE);
		lblUploadImg.setBackground(C.APP_BGCOLOR);
		lblUploadImg.setText("Upload Image:");
		// file upload button
    Button browse = new Button(form, SWT.PUSH);
    browse.setText("Choose...");        
    browse.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
      	uploadImage();
      }
    });
		
		imgSelected = new Text(form, SWT.NONE);
		imgSelected.setEditable(false);
		imgSelected.setBackground(C.APP_BGCOLOR);
		imgSelected.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		imgSelected.setFont(C.FONT_8);

		sep = new Label(form, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));		
		
		
		//==================================================================		
		
		Button ok = new Button (form, SWT.PUSH);
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
						sRow.setDeleted("FALSE");
		        sRow.insert();
		        LogController.log("Space comment added to database.");
		        
		        if(imgToUploadPath != null) {
		        	
		    			try {  				
		    			    File src = new File(imgToUploadPath);   
		    			    final File dest = new File(
		    			    		FilesystemController.imgdir.toString() +  
		    			    		C.SEP + 
		    			    		imgToUploadName
		    			    );   	      
		    			    final FileInputStream is = new FileInputStream(src);   
		    			    final FileOutputStream os = new FileOutputStream(dest);   	
		    			    LogController.log(src);
		    			    LogController.log(src.length());  
		    			    LogController.log(dest);
		    			    Runnable job = new Runnable() {
		    			    	@Override
		    					public void run() {
		    				      try {  
		    					      int currentbyte = is.read();  
		    					      while (currentbyte != -1) {  
		    						      os.write (currentbyte);  
		    						      currentbyte = is.read();  
		    					      }
		    						} catch (IOException ex){
		    							LogController.log(ex.toString());
		    						}
		    						LogController.log(dest.length()); 
		    						try {
		    							is.close();
		    							os.close();   
		    						} catch (IOException e) {
		    							e.printStackTrace();
		    						}   
		    					}
		    				};
		    				BusyIndicator.showWhile(myshell.getDisplay(), job);
		    			    EsmApplication.alert("Image copied, " + dest.length() + " bytes");				
		    			} catch(IOException ex){
		    				LogController.log(ex.toString());
		    			} 		    			
		    		}
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
		LogController.log("New Space form closed");	
		
		return formOK;
	}

}
