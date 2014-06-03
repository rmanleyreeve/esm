package com.rmrdigitalmedia.esm.views;

import java.sql.SQLException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.forms.DeletePhotoDialog;
import com.rmrdigitalmedia.esm.forms.EditPhotoMetadataForm;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.PhotoMetadataTable;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class PhotoViewer {

	PhotoMetadataTable.Row pRow = null;

	public static void main (String [] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			new PhotoViewer(shell,"C:\\Users\\All Users\\ESM Data\\images\\1\\full\\xxxx.jpg", "C:\\Users\\All Users\\ESM Data\\images\\1\\thumb\\xxxx.jpg", 1);						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public PhotoViewer(final Shell appwin, final String fullPath, final String thumbPath, final int spaceID) {

		try {
			pRow = PhotoMetadataTable.getRow("path", thumbPath);
			System.out.println(pRow.getID());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LogController.log("Running class " + this.getClass().getName());
		Display display = Display.getDefault();
		final Shell shell = new Shell(SWT.DIALOG_TRIM);

		if(pRow != null) {

			shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
			FillLayout rl_shell = new FillLayout();
			rl_shell.marginHeight = 5;
			rl_shell.spacing = 5;
			rl_shell.marginWidth = 5;
			shell.setLayout(rl_shell);
			shell.setSize(820,750);

			final Composite imgHolder = new Composite(shell, SWT.NONE);
			GridLayout gl_imgHolder = new GridLayout(3, true);
			gl_imgHolder.horizontalSpacing = 0;
			gl_imgHolder.marginWidth = 0;
			gl_imgHolder.verticalSpacing = 0;
			gl_imgHolder.marginHeight = 0;
			imgHolder.setLayout(gl_imgHolder);
			
			Label imgTitle = new Label(imgHolder, SWT.CENTER);
			GridData gd_imgTitle = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
			gd_imgTitle.verticalIndent = 5;
			imgTitle.setLayoutData(gd_imgTitle);
			imgTitle.setFont(C.FONT_12B);
			imgTitle.setText(pRow.getTitle());

			final Label imgPic = new Label(imgHolder, SWT.BORDER | SWT.CENTER);
			imgPic.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 3, 1));
			imgPic.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
			imgPic.setImage(C.getExtImage(fullPath));

			final Label imgComment = new Label(imgHolder, SWT.BORDER | SWT.WRAP | SWT.CENTER);
			GridData gd_imgComment = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
			gd_imgComment.verticalIndent = 10;
			imgComment.setLayoutData(gd_imgComment);
			imgComment.setFont(C.FONT_11);
			imgComment.setText(pRow.getComment());

			EsmUsersTable.Row user =  WindowController.user;
			if(user.getAccessLevel()==9 || user.getID()==pRow.getAuthorID())	{	

				Button btnEditComment = new Button(imgHolder, SWT.NONE);
				GridData gd_btnEditComment = new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1);
				gd_btnEditComment.verticalIndent = 5;
				gd_btnEditComment.horizontalIndent = 5;
				btnEditComment.setLayoutData(gd_btnEditComment);
				btnEditComment.setText("Edit Details");
				btnEditComment.setToolTipText("Edit this comment");
				btnEditComment.setFont(C.FONT_9);
				btnEditComment.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						int _id = pRow.getID();
						EditPhotoMetadataForm epcf = new EditPhotoMetadataForm(_id);					
						if(epcf.complete()) {
							try {
								pRow = PhotoMetadataTable.getRow(_id);
								imgComment.setText(pRow.getComment());									
								imgHolder.update();
								imgPic.update();
								shell.pack();
								Rectangle bounds = appwin.getBounds ();
								Rectangle rect = shell.getBounds ();
								int x = bounds.x + ((bounds.width / 2) - (rect.width/2));
								int y = bounds.y + ((bounds.height / 2) - (rect.height/2));
								System.out.println(bounds);
								System.out.println(rect);
								System.out.println(x + "," + y);
								shell.setLocation (x, y);
							} catch (SQLException e) { }
						}
					}
				});


				Label foo = new Label(imgHolder, SWT.NONE);
				foo.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));

				
				Button btnDeleteComment = new Button(imgHolder, SWT.NONE);
				btnDeleteComment.setText("Delete Photo");
				GridData gd_btnDeleteComment = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
				gd_btnDeleteComment.verticalIndent = 5;
				gd_btnDeleteComment.horizontalIndent = 5;
				btnDeleteComment.setLayoutData(gd_btnDeleteComment);
				btnDeleteComment.setToolTipText("Delete this photo and attached comments");
				btnDeleteComment.setFont(C.FONT_9);
				btnDeleteComment.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						DeletePhotoDialog dpd = new DeletePhotoDialog();					
						if(dpd.deleteOK(thumbPath,fullPath)) {
							LogController.log("Files & Metadata deleted");
							try {
								Thread.sleep(100);
							} catch (InterruptedException e1) {}
							WindowController.showSpaceDetail(spaceID);
							shell.dispose();
						} else {
							LogController.log("Files & Metadata not deleted");
						}
					}
				});
			}	

			shell.pack();
			Rectangle bounds = appwin.getBounds ();
			Rectangle rect = shell.getBounds ();
			int x = bounds.x + ((bounds.width / 2) - (rect.width/2));
			int y = bounds.y + ((bounds.height / 2) - (rect.height/2));
			System.out.println(bounds);
			System.out.println(rect);
			System.out.println(x + "," + y);
			shell.setLocation (x, y);
			shell.open();
			
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}

		}
		LogController.log("Photo viewer closed");	
		shell.dispose();
	}
}
