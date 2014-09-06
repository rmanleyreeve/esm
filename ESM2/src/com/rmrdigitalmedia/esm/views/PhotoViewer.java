package com.rmrdigitalmedia.esm.views;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.controllers.DatabaseController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.forms.DeletePhotoDialog;
import com.rmrdigitalmedia.esm.forms.EditPhotoMetadataForm;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.PhotoMetadataTable;

public class PhotoViewer {
	private static Object me;
	PhotoMetadataTable.Row pRow = null;

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			new PhotoViewer(shell, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PhotoViewer(final Shell appwin, final int dataID) {
		me = this;
		try {
			pRow = PhotoMetadataTable.getRow("DATA_ID", ""+dataID);
		} catch (SQLException e) {
			LogController.logEvent(me, C.ERROR, e);
		}

		LogController.log("Running class " + this.getClass().getName());
		Display display = Display.getDefault();
		final Shell shell = new Shell(SWT.DIALOG_TRIM);

		if (pRow != null) {

			final int spaceID = pRow.getSpaceID();
			final int metaID = pRow.getID();
			shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
			FillLayout rl_shell = new FillLayout();
			rl_shell.marginHeight = 10;
			rl_shell.spacing = 5;
			rl_shell.marginWidth = 10;
			shell.setLayout(rl_shell);
			shell.setImage(C.getImage(C.APP_ICON_16));
			shell.setText("Videotel ESM");
			shell.setSize(820, 750);

			final Composite imgHolder = new Composite(shell, SWT.NONE);
			GridLayout gl_imgHolder = new GridLayout(3, true);
			gl_imgHolder.horizontalSpacing = 0;
			gl_imgHolder.marginWidth = 0;
			gl_imgHolder.verticalSpacing = 0;
			gl_imgHolder.marginHeight = 0;
			imgHolder.setLayout(gl_imgHolder);

			final Label imgTitle = new Label(imgHolder, SWT.CENTER);
			GridData gd_imgTitle = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
			// gd_imgTitle.verticalIndent = 5;
			imgTitle.setLayoutData(gd_imgTitle);
			imgTitle.setFont(C.FONT_12B);
			imgTitle.setText(pRow.getTitle());

			final Label imgMeta = new Label(imgHolder, SWT.CENTER);
			GridData gd_imgMeta = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
			// gd_imgTitle.verticalIndent = 5;
			imgMeta.setLayoutData(gd_imgMeta);
			imgMeta.setFont(C.FONT_9);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
			String strMeta = "";
			strMeta = "Posted " + sdf.format(pRow.getUpdateDate());
			try { 
				strMeta += " by " + EsmUsersTable.getRow(pRow.getAuthorID()).getForename() + " " + EsmUsersTable.getRow(pRow.getAuthorID()).getSurname();
			} catch (SQLException e2) {
				LogController.logEvent(me, C.WARNING, e2);
			}
			imgMeta.setText(strMeta);

			final Label imgPic = new Label(imgHolder, SWT.BORDER | SWT.CENTER);
			imgPic.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 3, 1));
			GridData gd_imgPic = new GridData(SWT.CENTER, SWT.TOP, true, true, 3, 1);
			gd_imgPic.verticalIndent = 5;
			imgPic.setLayoutData(gd_imgPic);
			imgPic.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
			Image img = DatabaseController.readImageDataFull(dataID);
			imgPic.setImage(img);

			final Label imgComment = new Label(imgHolder, SWT.BORDER | SWT.WRAP | SWT.CENTER);
			GridData gd_imgComment = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
			gd_imgComment.verticalIndent = 10;
			imgComment.setLayoutData(gd_imgComment);
			imgComment.setFont(C.FONT_11);
			imgComment.setText(pRow.getComment());

			EsmUsersTable.Row user = WindowController.user;
			if (user.getAccessLevel() == 9 || user.getID() == pRow.getAuthorID()) {

				Button btnEditComment = new Button(imgHolder, SWT.NONE);
				GridData gd_btnEditComment = new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1);
				gd_btnEditComment.verticalIndent = 5;
				gd_btnEditComment.horizontalIndent = 5;
				btnEditComment.setLayoutData(gd_btnEditComment);
				btnEditComment.setText("Edit Details");
				btnEditComment.setToolTipText("Edit this comment");
				btnEditComment.setFont(C.FONT_9);
				btnEditComment.setImage(C.getImage("edit.png"));
				btnEditComment.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						int _id = pRow.getID();
						EditPhotoMetadataForm epcf = new EditPhotoMetadataForm(_id);
						if (epcf.complete()) {
							WindowController.showSpaceDetail(pRow.getSpaceID());
							try {
								pRow = PhotoMetadataTable.getRow(_id);
								imgTitle.setText(pRow.getTitle());
								imgComment.setText(pRow.getComment());
								imgHolder.update();
								imgPic.update();
								shell.pack();
								Rectangle bounds = appwin.getBounds();
								Rectangle rect = shell.getBounds();
								int x = bounds.x + ((bounds.width / 2) - (rect.width / 2));
								int y = bounds.y + ((bounds.height / 2) - (rect.height / 2));
								shell.setLocation(x, y);
							} catch (SQLException e) {}
						}
					}
				});

				Label foo = new Label(imgHolder, SWT.NONE);
				foo.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));

				Button btnDeletePhoto = new Button(imgHolder, SWT.NONE);
				btnDeletePhoto.setText("Delete Photo");
				GridData gd_btnDeleteComment = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
				gd_btnDeleteComment.verticalIndent = 5;
				gd_btnDeleteComment.horizontalIndent = 5;
				btnDeletePhoto.setLayoutData(gd_btnDeleteComment);
				btnDeletePhoto.setToolTipText("Delete this photo and attached comments");
				btnDeletePhoto.setFont(C.FONT_9);
				btnDeletePhoto.setImage(C.getImage("delete.png"));
				btnDeletePhoto.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						DeletePhotoDialog dpd = new DeletePhotoDialog();
						if (dpd.deleteOK(metaID)) {
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
			Rectangle bounds = appwin.getBounds();
			Rectangle rect = shell.getBounds();
			int x = bounds.x + ((bounds.width / 2) - (rect.width / 2));
			int y = bounds.y + ((bounds.height / 2) - (rect.height / 2));
			shell.setLocation(x, y);
			shell.open();

			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}

		}
		LogController.log("Photo viewer closed");
		shell.dispose();
	}
}
