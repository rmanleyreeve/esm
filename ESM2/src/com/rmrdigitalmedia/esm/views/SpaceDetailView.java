package com.rmrdigitalmedia.esm.views;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import com.google.common.io.Files;
import com.itextpdf.text.DocumentException;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.AuditController;
import com.rmrdigitalmedia.esm.controllers.DatabaseController;
import com.rmrdigitalmedia.esm.controllers.FilesystemController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.PdfController;
import com.rmrdigitalmedia.esm.controllers.UploadController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.forms.AddEntrypointForm;
import com.rmrdigitalmedia.esm.forms.AddSpaceCommentForm;
import com.rmrdigitalmedia.esm.forms.AddSpacePhotoForm;
import com.rmrdigitalmedia.esm.forms.ApproveSpaceCommentForm;
import com.rmrdigitalmedia.esm.forms.DeleteDocumentDialog;
import com.rmrdigitalmedia.esm.forms.DeleteEntrypointDialog;
import com.rmrdigitalmedia.esm.forms.DeleteSpaceCommentDialog;
import com.rmrdigitalmedia.esm.forms.EditEntrypointForm;
import com.rmrdigitalmedia.esm.forms.EditSpaceCommentForm;
import com.rmrdigitalmedia.esm.forms.EditSpaceForm;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;

public class SpaceDetailView {
	static Object me = new SpaceDetailView();
	static EsmUsersTable.Row user = WindowController.user;
	static int access = user.getAccessLevel();

	static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
	private static Label sep;

	private static String df(Timestamp ts) {
		SimpleDateFormat d = new SimpleDateFormat("dd - MM - yyyy");
		SimpleDateFormat t = new SimpleDateFormat("kk:mm");
		return new String( "Date: " + d.format(ts) + "  Time: " + t.format(ts) );
	}


	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			shell.setSize(1380, 750);
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			Composite comp = new Composite(shell, SWT.BORDER);
			user = EsmUsersTable.getRow(1);
			SpaceDetailView.buildPage(comp,2);
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

	public static void buildPage(final Composite parent, final int spaceID) {
		LogController.log("Building Space Detail page");
		parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_WAIT));
		SpacesTable.Row sRow = null;
		try {
			sRow = SpacesTable.getRow(spaceID);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		for (Control c:parent.getChildren()) {
			c.dispose();
		}
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		SashForm panels = new SashForm(parent,SWT.NONE);
		panels.setBackground(C.TITLEBAR_BGCOLOR);
		panels.setLayout(new FillLayout());

		// scrolling frame to hold the LH space comments panel
		final ScrolledComposite scrollPanelLeft = new ScrolledComposite(panels, SWT.V_SCROLL | SWT.BORDER);

		// the panel that holds the various info rows
		final Composite compL = new Composite(scrollPanelLeft, SWT.NONE);
		GridLayout gl_compL = new GridLayout(1, true);
		gl_compL.marginBottom = 50;
		gl_compL.marginRight = 10;
		compL.setLayout(gl_compL);
		compL.setBackground(C.APP_BGCOLOR);

		// row 1 - name, id, description fields
		Group row1 = new Group(compL, SWT.NONE);
		row1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		row1.setLayout(new GridLayout(4, true));
		row1.setBackground(C.APP_BGCOLOR);

		Label lblNname = new Label(row1, SWT.NONE);
		lblNname.setFont(C.FONT_12B);
		lblNname.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		lblNname.setBackground(C.APP_BGCOLOR);
		lblNname.setText("Name");		

		Label lblID = new Label(row1, SWT.NONE);
		GridData gd_lblID = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_lblID.horizontalIndent = 10;
		lblID.setLayoutData(gd_lblID);
		lblID.setFont(C.FONT_12B);
		lblID.setBackground(C.APP_BGCOLOR);
		lblID.setText("ID");		

		Label name = new Label(row1, SWT.BORDER);
		name.setFont(C.FONT_12);
		name.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		name.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		name.setText(sRow.getName());

		Label id = new Label(row1, SWT.BORDER);
		id.setFont(C.FONT_12);
		id.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_id = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_id.horizontalIndent = 10;
		id.setLayoutData(gd_id);
		id.setText(""+sRow.getID());

		Label lblDesc = new Label(row1, SWT.NONE);
		lblDesc.setFont(C.FONT_12B);
		lblDesc.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1));
		lblDesc.setBackground(C.APP_BGCOLOR);
		lblDesc.setText("Description:");		

		Text description = new Text(row1, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		description.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		description.setEditable(false);
		description.setFont(C.FONT_12);
		description.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_description = new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1);
		gd_description.widthHint = 1000;
		gd_description.heightHint = 100;
		description.setLayoutData(gd_description);
		description.setText(sRow.getDescription());		

		if(access == 9)	{	
			Button btnEditSpace = new Button(row1, SWT.RIGHT);
			btnEditSpace.setText("Edit");
			btnEditSpace.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1));
			btnEditSpace.setToolTipText("Edit details for this Enclosed Space");
			btnEditSpace.setImage(C.getImage("edit.png"));
			btnEditSpace.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					EditSpaceForm esf = new EditSpaceForm(spaceID);					
					if(esf.complete()) {
						WindowController.showSpaceDetail(spaceID);									
					}
				}
			});		
		}

		sep = new Label(compL, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));		


		// row 2 - comments header & button bar
		Composite row2 = new Composite(compL, SWT.NONE);
		//row2.setLayout(new GridLayout(3, false));
		row2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		row2.setLayout(new GridLayout(4, true));
		row2.setBackground(C.APP_BGCOLOR);		

		CLabel lblComments = new CLabel(row2, SWT.NONE);
		lblComments.setImage(C.getImage("comment.png"));
		lblComments.setFont(C.FONT_12B);
		lblComments.setBackground(C.APP_BGCOLOR);
		lblComments.setText("Comments");		

		Button btnAddComment = new Button(row2, SWT.RIGHT);
		btnAddComment.setToolTipText("Add a new Comment for this Enclosed Space");
		GridData gd_btnAdd = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_btnAdd.verticalIndent = 3;
		btnAddComment.setLayoutData(gd_btnAdd);
		btnAddComment.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				AddSpaceCommentForm ascf = new AddSpaceCommentForm(spaceID, user.getID());					
				if(ascf.complete()) {
					LogController.log("New Space Comment saved in database");
					WindowController.showSpaceDetail(spaceID);					
				}
			}
		});
		btnAddComment.setImage(C.getImage("comment-add.png"));
		btnAddComment.setText("Add");
		new Label(row2, SWT.NONE);
		new Label(row2, SWT.NONE);

		// loop through and display comments in descending order	
		try {
			Connection conn = DatabaseController.createConnection();
			PreparedStatement  ps = null;
			String sql = "SELECT SPACE_COMMENTS.*, ESM_USERS.FORENAME, ESM_USERS.SURNAME FROM SPACE_COMMENTS "
					+ "INNER JOIN ESM_USERS ON ESM_USERS.ID = SPACE_COMMENTS.AUTHOR_ID "
					+ "WHERE (SPACE_COMMENTS.DELETED = FALSE AND SPACE_COMMENTS.SPACE_ID=?) "
					+ "ORDER BY SPACE_COMMENTS.ID DESC";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, spaceID);
			final ResultSet spaceComment = ps.executeQuery();
			while(spaceComment.next()) {			
				boolean canApprove = (!spaceComment.getBoolean("APPROVED") && (access==3 || access==9));
				final int commentID = spaceComment.getInt("ID");

				if(spaceComment.getBoolean("APPROVED") || canApprove){

					Group commentRow = new Group(compL, SWT.NONE);
					commentRow.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
					GridLayout gl_commentRow = new GridLayout(4, false);
					gl_commentRow.marginHeight = 0;
					gl_commentRow.horizontalSpacing = 0;
					gl_commentRow.verticalSpacing = 0;
					commentRow.setLayout(gl_commentRow);
					commentRow.setBackground(C.APP_BGCOLOR);

					Label lblAuthor = new Label(commentRow, SWT.NONE);
					GridData gd_lblAuthor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
					gd_lblAuthor.horizontalIndent = 1;
					lblAuthor.setLayoutData(gd_lblAuthor);
					lblAuthor.setFont(C.FONT_9);
					lblAuthor.setBackground(C.APP_BGCOLOR);
					lblAuthor.setText(spaceComment.getString("FORENAME") + " " + spaceComment.getString("SURNAME"));		

					Text comment = new Text(commentRow, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI | SWT.READ_ONLY);
					comment.setText(spaceComment.getString("COMMENT"));
					comment.setEditable(false);
					comment.setFont(C.FONT_11);
					comment.setBackground(C.FIELD_BGCOLOR);
					if(canApprove) { comment.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED)); }
					GridData gd_comment = new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1);
					gd_comment.widthHint = 1000;
					int h = comment.computeSize(1000,SWT.DEFAULT,true).y;
					gd_comment.heightHint = (h>40) ? h:40;
					comment.setLayoutData(gd_comment);

					Label lblPosted = new Label(commentRow, SWT.NONE);
					GridData gd_lblPosted = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
					gd_lblPosted.horizontalIndent = 1;
					lblPosted.setLayoutData(gd_lblPosted);
					lblPosted.setFont(C.FONT_9);
					lblPosted.setBackground(C.APP_BGCOLOR);
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
					lblPosted.setText("Posted: " + sdf.format(spaceComment.getTimestamp("UPDATE_DATE")));

					if( access==9 || user.getID()==spaceComment.getInt("AUTHOR_ID"))	{	
						Button btnEditComment = new Button(commentRow, SWT.NONE);
						GridData gd_btnEditComment = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
						gd_btnEditComment.horizontalIndent = 5;
						btnEditComment.setLayoutData(gd_btnEditComment);
						btnEditComment.setText("Edit");
						btnEditComment.setToolTipText("Edit this comment");
						btnEditComment.setFont(C.FONT_9);
						btnEditComment.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent arg0) {
								EditSpaceCommentForm esf = new EditSpaceCommentForm(commentID);					
								if(esf.complete()) {
									WindowController.showSpaceDetail(spaceID);									
								}
							}
						});

						Button btnDeleteComment = new Button(commentRow, SWT.NONE);
						btnDeleteComment.setText("Delete");
						GridData gd_btnDeleteComment = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
						gd_btnDeleteComment.horizontalIndent = 5;
						btnDeleteComment.setLayoutData(gd_btnDeleteComment);
						btnDeleteComment.setToolTipText("Delete this comment");
						btnDeleteComment.setFont(C.FONT_9);
						btnDeleteComment.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent arg0) {
								int _id = commentID;
								DeleteSpaceCommentDialog dscd = new DeleteSpaceCommentDialog();					
								if(dscd.deleteOK(_id)) {
									LogController.log("Space Comment "+_id+" marked as deleted in database");
									WindowController.showSpaceDetail(spaceID);						
								} else {
									LogController.log("Space Comment " + _id + " not deleted");
								}
							}
						});
					}
					if(canApprove) {
						Button btnApproveComment = new Button(commentRow, SWT.NONE);
						btnApproveComment.setText("Approve");
						GridData gd_btnApproveComment = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
						gd_btnApproveComment.horizontalIndent = 5;
						btnApproveComment.setLayoutData(gd_btnApproveComment);
						btnApproveComment.setToolTipText("Approve this comment");
						btnApproveComment.setFont(C.FONT_9);
						btnApproveComment.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent arg0) {
								int id = commentID;
								ApproveSpaceCommentForm ascf = new ApproveSpaceCommentForm(id);
								if(ascf.complete()) {
									EsmApplication.alert("The comment was approved!");
									WindowController.showSpaceDetail(spaceID);
								}
							}
						});
					} else {
						new Label(commentRow, SWT.NONE);
					}

				}

			}	// end loop
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LogController.logEvent(me, C.ERROR, e);
		}

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

		//===========================================================================================================================================

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

		EsmUsersTable.Row author = null;
		try {
			author = EsmUsersTable.getRow(sRow.getAuthorID());
		} catch (SQLException e1) {
			LogController.logEvent(me, C.ERROR, e1);
		}
		Label lblCreatedBy = new Label(rowRight1, SWT.NONE);
		lblCreatedBy.setFont(C.FONT_10B);
		lblCreatedBy.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		lblCreatedBy.setBackground(C.APP_BGCOLOR);
		lblCreatedBy.setText("Created By:");		

		Label lblAuthorName = new Label(rowRight1, SWT.WRAP);
		lblAuthorName.setFont(C.FONT_10);
		lblAuthorName.setBackground(C.APP_BGCOLOR);
		lblAuthorName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		lblAuthorName.setText(author.getForename() + " " + author.getSurname());

		Label lblCreated = new Label(rowRight1, SWT.NONE);
		lblCreated.setFont(C.FONT_10B);
		lblCreated.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		lblCreated.setBackground(C.APP_BGCOLOR);
		lblCreated.setText("Created:");		

		Label lblCreatedDate = new Label(rowRight1, SWT.WRAP);
		lblCreatedDate.setFont(C.FONT_10);
		lblCreatedDate.setBackground(C.APP_BGCOLOR);
		lblCreatedDate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		lblCreatedDate.setText(df(sRow.getCreatedDate()));

		Label lblUpdated = new Label(rowRight1, SWT.NONE);
		lblUpdated.setFont(C.FONT_10B);
		lblUpdated.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		lblUpdated.setBackground(C.APP_BGCOLOR);
		lblUpdated.setText("Updated:");		

		Label lblUpdatedDate = new Label(rowRight1, SWT.WRAP);
		lblUpdatedDate.setFont(C.FONT_10);
		lblUpdatedDate.setBackground(C.APP_BGCOLOR);
		lblUpdatedDate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		lblUpdatedDate.setText(df(sRow.getUpdateDate()));

		Label lblOverallCompletion = new Label(rowRight1, SWT.NONE);
		lblOverallCompletion.setFont(C.FONT_10B);
		lblOverallCompletion.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		lblOverallCompletion.setBackground(C.APP_BGCOLOR);
		lblOverallCompletion.setText("Completion Status:");		

		Label lblOverallCompletionImg = new Label(rowRight1, SWT.NONE);
		// work out completion status based on id
		int cs = AuditController.calculateOverallCompletionStatus(spaceID);
		lblOverallCompletionImg.setImage(C.getImage("Percent_"+ cs +".png"));
		lblOverallCompletionImg.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		lblOverallCompletionImg.setBackground(C.APP_BGCOLOR);		


		// row 6 - print blank forms header ===============================================================
		Group rowRight6 = new Group(compR, SWT.NONE);
		rowRight6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_rowRight6 = new GridLayout(3, false);
		gl_rowRight6.marginBottom = 5;
		gl_rowRight6.marginHeight = 0;
		rowRight6.setLayout(gl_rowRight6);
		rowRight6.setBackground(C.APP_BGCOLOR);

		CLabel lblPrintDocs = new CLabel(rowRight6, SWT.NONE);
		lblPrintDocs.setImage(C.getImage("print.png"));
		lblPrintDocs.setFont(C.FONT_12B);
		lblPrintDocs.setBackground(C.APP_BGCOLOR);
		lblPrintDocs.setText("Printable Audit Forms");	

		final Button btnPrintSpaceDoc = new Button(rowRight6, SWT.NONE);
		btnPrintSpaceDoc.setToolTipText("Open a blank Enclosed Space Audit Form for printing");
		GridData gd_btnPrintSpaceDoc = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnPrintSpaceDoc.widthHint = 110;
		gd_btnPrintSpaceDoc.verticalIndent = 3;
		btnPrintSpaceDoc.setLayoutData(gd_btnPrintSpaceDoc);
		btnPrintSpaceDoc.setText("Space Audit");
		btnPrintSpaceDoc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				File pdf =  new File(C.DOC_DIR + C.SEP + "blank_space_form.pdf");
				if(!pdf.exists()) {
					FilesystemController.createBlankSpaceForm();
				}
				if( Program.launch(pdf.getPath()) ) {
					LogController.log("Opening blank Space Audit Form");
				} else {
					LogController.logEvent(me, C.ERROR, "Cannot open blank Space Audit Form!");
				}
			}
		});		

		final Button btnPrintEntryDoc = new Button(rowRight6, SWT.NONE);
		btnPrintEntryDoc.setToolTipText("Open a blank Entry Point Audit Form for printing");
		GridData gd_btnPrintEntryDoc = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_btnPrintEntryDoc.verticalIndent = 3;
		btnPrintEntryDoc.setLayoutData(gd_btnPrintEntryDoc);
		btnPrintEntryDoc.setText("Entry Point Audit");
		btnPrintEntryDoc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				File pdf =  new File(C.DOC_DIR + C.SEP + "blank_entry_form.pdf");
				if(!pdf.exists()) {
					FilesystemController.createBlankEntryForm();
				}
				if( Program.launch(pdf.getPath()) ) {
					LogController.log("Opening blank Entry Audit Form");
				} else {
					LogController.logEvent(me, C.ERROR, "Cannot open blank Entry Audit Form!");
				}
			}
		});



		// row 2 - audit header & button bar	==========================================================================	
		Group rowRight2 = new Group(compR, SWT.NONE);
		rowRight2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_rowRight2 = new GridLayout(6, false); // 6 column grid layout
		gl_rowRight2.marginBottom = 5;
		gl_rowRight2.marginHeight = 0;
		rowRight2.setLayout(gl_rowRight2);
		rowRight2.setBackground(C.APP_BGCOLOR);

		Label lblAudits = new Label(rowRight2, SWT.NONE);
		GridData gd_lblAudits = new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1);
		gd_lblAudits.widthHint = 300;
		lblAudits.setLayoutData(gd_lblAudits);
		lblAudits.setFont(C.FONT_12B);
		lblAudits.setBackground(C.APP_BGCOLOR);
		lblAudits.setText("Internal Space && Entry Point Audits");	

		Label lblSpaceAudit = new Label(rowRight2, SWT.NONE);
		lblSpaceAudit.setFont(C.FONT_10B);
		lblSpaceAudit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblSpaceAudit.setBackground(C.APP_BGCOLOR);
		lblSpaceAudit.setText(sRow.getName());		

		Label lblSpaceAuditImg = new Label(rowRight2, SWT.NONE);
		// work out completion status based on id
		int scs = AuditController.calculateSpaceCompletionStatus(spaceID);
		lblSpaceAuditImg.setImage(C.getImage("Percent_"+ scs +".png"));
		GridData gd_lblSpaceAuditImg = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblSpaceAuditImg.widthHint = 160;
		lblSpaceAuditImg.setLayoutData(gd_lblSpaceAuditImg);
		lblSpaceAuditImg.setBackground(C.APP_BGCOLOR);

		Label lblSpaceAuditLight = new Label(rowRight2, SWT.RIGHT);
		String sTL = (String) EsmApplication.appData.getField("SPACE_STATUS_"+spaceID);
		lblSpaceAuditLight.setImage(C.getImage(""+sTL+".png"));
		GridData gd_lblSpaceAuditLight = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_lblSpaceAuditLight.horizontalIndent = 10;
		lblSpaceAuditLight.setLayoutData(gd_lblSpaceAuditLight);
		lblSpaceAuditLight.setBackground(C.APP_BGCOLOR);

		Button btnShowSpaceAudit = new Button(rowRight2, SWT.NONE);
		btnShowSpaceAudit.setToolTipText("Launch the Internal Space Audit for " + sRow.getName());
		GridData gd_btnShowSpaceAudit = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		gd_btnShowSpaceAudit.verticalIndent = 3;
		btnShowSpaceAudit.setLayoutData(gd_btnShowSpaceAudit);
		btnShowSpaceAudit.setImage(C.getImage("edit.png"));
		btnShowSpaceAudit.setText("Audit");
		btnShowSpaceAudit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				WindowController.showSpaceAuditChecklist(spaceID);
			}
		});
		sep = new Label(rowRight2, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));		

		CLabel lblEntryPoint;
		Label lblEntryPointAuditImg, lblEntryPointAuditLight;
		GridData gd_lblEntryPointAuditImg, gd_lblEntryPointAuditLight, gd_btnShowEntryAudit, gd_lblEntryPoint, gd_btnEditEntry, gd_btnDeleteEntry;
		Button btnShowEntryAudit, btnEditEntry, btnDeleteEntry;
		int rh = 20;		

		try {
			Connection conn = DatabaseController.createConnection();
			String sql = "SELECT * FROM ENTRYPOINTS WHERE DELETED=FALSE AND SPACE_ID=? ORDER BY ID DESC";
			PreparedStatement ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, spaceID);
			ResultSet epRow = ps.executeQuery();
			int rowCount = 0;
			if (epRow.last()) {
				rowCount = epRow.getRow();
				epRow.beforeFirst();
			}				
			// for loop
			while(epRow.next()) {	
				final int epID = epRow.getInt("ID");
				String epName = epRow.getString("NAME");
				lblEntryPoint = new CLabel(rowRight2, SWT.NONE);
				lblEntryPoint.setFont(C.FONT_10);
				gd_lblEntryPoint = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
				//gd_lblEntryPoint.horizontalIndent = 10;
				gd_lblEntryPoint.heightHint = rh;
				lblEntryPoint.setLayoutData(gd_lblEntryPoint);
				lblEntryPoint.setBackground(C.APP_BGCOLOR);
				lblEntryPoint.setImage(C.getImage("entry.png"));
				lblEntryPoint.setText(epName);
				lblEntryPoint.setToolTipText(epRow.getString("DESCRIPTION"));
				lblEntryPointAuditImg = new Label(rowRight2, SWT.NONE);
				// work out completion status based on id
				int epcs = AuditController.calculateEntryCompletionStatus(epID);
				lblEntryPointAuditImg.setImage(C.getImage("Percent_"+ epcs +".png"));
				gd_lblEntryPointAuditImg = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
				gd_lblEntryPointAuditImg.widthHint = 160;
				gd_lblEntryPointAuditImg.heightHint = rh;
				lblEntryPointAuditImg.setLayoutData(gd_lblEntryPointAuditImg);
				lblEntryPointAuditImg.setBackground(C.APP_BGCOLOR);
				// status light
				lblEntryPointAuditLight = new Label(rowRight2, SWT.RIGHT);
				try {
					AuditController.calculateEntryClassificationCompletion(epID);
				} catch (SQLException e1) {
					LogController.logEvent(me, C.FATAL, "ERROR CALC ENTRY CLASSIFICATION COMPLETION", e1);
				}
				String epTL = (String) EsmApplication.appData.getField("ENTRY_STATUS_"+epID);
				if(epTL.equals("")) { epTL = "null"; }
				lblEntryPointAuditLight.setImage(C.getImage(""+epTL+".png"));
				gd_lblEntryPointAuditLight = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
				gd_lblEntryPointAuditLight.horizontalIndent = 10;
				gd_lblEntryPointAuditLight.heightHint = rh;
				lblEntryPointAuditLight.setLayoutData(gd_lblEntryPointAuditLight);
				lblEntryPointAuditLight.setBackground(C.APP_BGCOLOR);
				// audit button
				btnShowEntryAudit = new Button(rowRight2, SWT.NONE);
				btnShowEntryAudit.setToolTipText("Launch the Entry Point Audit for " + epName);
				gd_btnShowEntryAudit = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
				//gd_btnEditAudit.verticalIndent = 3;
				gd_btnShowEntryAudit.heightHint = rh;
				//gd_btnShowEntryAudit.widthHint = 70;
				btnShowEntryAudit.setLayoutData(gd_btnShowEntryAudit);
				btnShowEntryAudit.setFont(C.FONT_9);
				btnShowEntryAudit.setText("Audit");
				btnShowEntryAudit.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						WindowController.showEntryAuditChecklist(epID);
					}
				});
				// edit button
				btnEditEntry = new Button(rowRight2, SWT.NONE);
				btnEditEntry.setToolTipText("Edit details of the Entry Point");
				gd_btnEditEntry = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
				//gd_btnEditEntry.verticalIndent = 3;
				gd_btnEditEntry.heightHint = rh;
				//gd_btnEditEntry.widthHint = 60;
				btnEditEntry.setLayoutData(gd_btnEditEntry);
				btnEditEntry.setFont(C.FONT_9);
				btnEditEntry.setText("Edit");
				btnEditEntry.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						EditEntrypointForm eef = new EditEntrypointForm(epID);					
						if(eef.complete()) {
							WindowController.showSpaceDetail(spaceID);									
						}
					}
				});
				// delete button
				btnDeleteEntry = new Button(rowRight2, SWT.NONE);
				btnDeleteEntry.setToolTipText("Delete this Entry Point");
				gd_btnDeleteEntry = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
				//gd_btnDeleteEntry.verticalIndent = 3;
				gd_btnDeleteEntry.heightHint = rh;
				//gd_btnDeleteEntry.widthHint = 60;
				btnDeleteEntry.setLayoutData(gd_btnDeleteEntry);
				btnDeleteEntry.setFont(C.FONT_9);
				btnDeleteEntry.setText("Delete");
				btnDeleteEntry.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						DeleteEntrypointDialog ded = new DeleteEntrypointDialog();					
						if(ded.deleteOK(epID)) {
							LogController.log("Entrypoint " + epID + " marked as deleted in database");
							EsmApplication.alert("The entry point was deleted!");
							WindowController.showSpaceDetail(spaceID);									
						} else {
							LogController.log("Entrypoint " + epID + " not deleted");
						}
					}
				});
				btnDeleteEntry.setEnabled( rowCount>1 && (access==9 || user.getID()==epRow.getInt("AUTHOR_ID")) );

			} // end for

		} catch (SQLException e) {
			e.printStackTrace();
		}


		sep = new Label(rowRight2, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));		

		Button btnAddEntry = new Button(rowRight2, SWT.NONE);
		btnAddEntry.setToolTipText("Add a new Entry Point to this Space");
		GridData gd_btnAddAudit = new GridData(SWT.LEFT, SWT.CENTER, true, false, 6, 1);
		gd_btnAddAudit.verticalIndent = 3;
		btnAddEntry.setLayoutData(gd_btnAddAudit);
		btnAddEntry.setImage(C.getImage("add.png"));
		btnAddEntry.setText("Add Entry Point");
		btnAddEntry.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				AddEntrypointForm aef = new AddEntrypointForm(spaceID, user.getID());					
				if(aef.complete()) {
					LogController.log("New Entry Point saved in database");
					WindowController.showSpaceDetail(spaceID);					
				}
			}
		});

		// row 3 - photos header & button bar	=================================
		Group rowRight3 = new Group(compR, SWT.NONE);
		rowRight3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_rowRight3 = new GridLayout(3, false);
		gl_rowRight3.marginBottom = 5;
		gl_rowRight3.marginHeight = 0;
		rowRight3.setLayout(gl_rowRight3);
		rowRight3.setBackground(C.APP_BGCOLOR);

		CLabel lblPhotos = new CLabel(rowRight3, SWT.NONE);
		GridData gd_lblPhotos = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblPhotos.widthHint = 120;
		lblPhotos.setLayoutData(gd_lblPhotos);
		lblPhotos.setFont(C.FONT_12B);
		lblPhotos.setBackground(C.APP_BGCOLOR);
		lblPhotos.setImage(C.getImage("photo.png"));
		lblPhotos.setText("Photos");	

		Button btnAddPhoto = new Button(rowRight3, SWT.NONE);
		btnAddPhoto.setToolTipText("Add a new photo for this Enclosed Space");
		GridData gd_btnAddPhoto = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnAddPhoto.verticalIndent = 3;
		btnAddPhoto.setLayoutData(gd_btnAddPhoto);
		btnAddPhoto.setImage(C.getImage("photo-add.png"));
		btnAddPhoto.setText("Add");
		btnAddPhoto.setEnabled(access > 1);
		btnAddPhoto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				AddSpacePhotoForm aspf = new AddSpacePhotoForm(spaceID, user.getID());					
				if(aspf.complete()) {
					LogController.log("New Space Photo & Comment saved in database");
					WindowController.showSpaceDetail(spaceID);					
				}
			}
		});

		final Button btnOpenPhoto = new Button(rowRight3, SWT.NONE);
		btnOpenPhoto.setAlignment(SWT.LEFT);
		btnOpenPhoto.setToolTipText("View the selected photo");
		GridData gd_btnOpenPhoto = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_btnOpenPhoto.verticalIndent = 3;
		btnOpenPhoto.setLayoutData(gd_btnOpenPhoto);
		btnOpenPhoto.setText("View");
		btnOpenPhoto.setEnabled(false);
		btnOpenPhoto.setVisible(false);

		// PHOTOS ===============================
		try {
			Connection conn = DatabaseController.createConnection();
			PreparedStatement ps = null;
			ResultSet pRow = null;
			String sql = "SELECT * FROM PHOTO_METADATA WHERE DELETED=FALSE AND SPACE_ID=? ORDER BY ID DESC";
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, spaceID);
			pRow = ps.executeQuery();
			int rowcount = 0;
			if (pRow.last()) {
				rowcount = pRow.getRow();
				pRow.beforeFirst();
			}				
			if (rowcount > 0) {
				// photos exist - show gallery
				btnOpenPhoto.setVisible(true);
				Composite gallHolder = new Composite(rowRight3, SWT.NONE);
				GridData gd_gallHolder = new GridData(SWT.FILL, SWT.FILL, true, false);
				gd_gallHolder.horizontalSpan = 3;
				gallHolder.setLayoutData(gd_gallHolder);
				gallHolder.setLayout(new GridLayout(1, true));
				gallHolder.setBackground(C.FIELD_BGCOLOR);

				final Gallery gallery = new Gallery(gallHolder, SWT.MULTI | SWT.H_SCROLL);
				GridData gd_gallery = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
				gd_gallery.minimumHeight = C.THUMB_HEIGHT + 15;
				gallery.setLayoutData(gd_gallery);
				gallery.setBackground(C.FIELD_BGCOLOR);

				NoGroupRenderer gr = new NoGroupRenderer();
				gr.setMinMargin(0);
				gr.setItemHeight(C.THUMB_HEIGHT);
				gr.setItemWidth(C.THUMB_WIDTH);
				gr.setAutoMargin(true);
				gallery.setGroupRenderer(gr);
				gallery.setToolTipText("Double-click a thumbnail image to open full size view");

				DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
				ir.setShowRoundedSelectionCorners(false);
				gallery.setItemRenderer(ir);

				GalleryItem group = new GalleryItem(gallery, SWT.NONE);

				while(pRow.next()) {
					int dataID = pRow.getInt("DATA_ID");
					Image itemImage = DatabaseController.readImageDataThumb(dataID);	
					if (itemImage != null) {
						GalleryItem item = new GalleryItem(group, SWT.NONE);
						item.setImage(itemImage);
						item.setData("id", dataID);
						item.setText(pRow.getString("TITLE")); 
					}
				}		
				gallery.addMouseListener(new MouseListener() {
					@Override
					public void mouseDoubleClick(MouseEvent e) {
						GalleryItem[] selection = gallery.getSelection();
						if (selection == null || selection.length == 0)
							return;
						GalleryItem item = selection[0];
						int _dataID = (Integer) item.getData("id");
						LogController.log("Opening Image " + _dataID);
						WindowController.showPhotoViewer(_dataID);
					}
					@Override
					public void mouseDown(MouseEvent e) {
						btnOpenPhoto.setEnabled( (gallery.getSelection().length > 0) );
					}
					@Override
					public void mouseUp(MouseEvent e) {}
				});

				btnOpenPhoto.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						GalleryItem[] selection = gallery.getSelection();
						if (selection == null)
							return;
						GalleryItem item = selection[0];
						int _dataID = (Integer) item.getData("id");
						LogController.log("Opening Image " + _dataID);
						WindowController.showPhotoViewer(_dataID);					
					}
				});

			} // endif photos > 0
		} catch (SQLException ex) {
			LogController.logEvent(me, C.ERROR, "Error getting photo metadata from database", ex);		
		}


		// row 4 - docs header & button bar		
		Group rowRight4 = new Group(compR, SWT.NONE);
		rowRight4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_rowRight4 = new GridLayout(4, false);
		gl_rowRight4.marginBottom = 5;
		gl_rowRight4.marginHeight = 0;
		rowRight4.setLayout(gl_rowRight4);
		rowRight4.setBackground(C.APP_BGCOLOR);

		CLabel lblDocs = new CLabel(rowRight4, SWT.NONE);
		GridData gd_lblDocs = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblDocs.widthHint = 120;
		lblDocs.setLayoutData(gd_lblDocs);
		lblDocs.setFont(C.FONT_12B);
		lblDocs.setBackground(C.APP_BGCOLOR);
		lblDocs.setImage(C.getImage("document.png"));
		lblDocs.setText("Documents");	

		Button btnAddDoc = new Button(rowRight4, SWT.NONE);
		btnAddDoc.setToolTipText("Add a new document for this Enclosed Space");
		GridData gd_btnAddDoc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnAddDoc.verticalIndent = 3;
		btnAddDoc.setLayoutData(gd_btnAddDoc);
		btnAddDoc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(UploadController.uploadSpaceDocument(spaceID, user.getID())) {
					WindowController.showSpaceDetail(spaceID);	
				}
			}
		});
		btnAddDoc.setImage(C.getImage("document-add.png"));
		btnAddDoc.setText("Add");
		btnAddDoc.setEnabled(access>1);

		final Button btnOpenDoc = new Button(rowRight4, SWT.NONE);
		btnOpenDoc.setAlignment(SWT.LEFT);
		btnOpenDoc.setToolTipText("View the selected document");
		GridData gd_btnOpenDoc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnOpenDoc.verticalIndent = 3;
		btnOpenDoc.setLayoutData(gd_btnOpenDoc);
		btnOpenDoc.setText("View");
		btnOpenDoc.setEnabled(false);
		btnOpenDoc.setVisible(false);

		final Button btnDeleteDoc = new Button(rowRight4, SWT.NONE);
		btnDeleteDoc.setAlignment(SWT.LEFT);
		btnDeleteDoc.setToolTipText("Delete this document");
		GridData gd_btnDeleteDoc = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_btnDeleteDoc.verticalIndent = 3;
		btnDeleteDoc.setLayoutData(gd_btnDeleteDoc);
		btnDeleteDoc.setText("Delete");
		btnDeleteDoc.setEnabled(false);
		btnDeleteDoc.setVisible(false);

		// DOCS ===============================
		try {
			Connection conn = DatabaseController.createConnection();
			PreparedStatement ps = null;
			ResultSet dRow = null;
			String sql = "SELECT DOC_DATA.*, ESM_USERS.FORENAME, ESM_USERS.SURNAME FROM DOC_DATA "
					+ "INNER JOIN ESM_USERS ON ESM_USERS.ID = DOC_DATA.AUTHOR_ID "
					+ "WHERE (DOC_DATA.DELETED = FALSE AND DOC_DATA.SPACE_ID=?) "
					+ "ORDER BY DOC_DATA.ID DESC";
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, spaceID);
			dRow = ps.executeQuery();
			int rowcount = 0;
			if (dRow.last()) {
				rowcount = dRow.getRow();
				dRow.beforeFirst();
			}				
			if (rowcount > 0) {
				// docs exist - show table
				btnOpenDoc.setVisible(true);
				btnDeleteDoc.setVisible(access==9);
				final Table table = new Table(rowRight4, SWT.NONE | SWT.FULL_SELECTION);
				table.setLayout(new FillLayout());
				table.setBackground(C.FIELD_BGCOLOR);
				GridData gd_table = new GridData(GridData.FILL_BOTH);
				gd_table.grabExcessVerticalSpace = true;
				gd_table.grabExcessHorizontalSpace=true;
				gd_table.heightHint = 50;
				gd_table.horizontalSpan = 4;
				table.setLayoutData(gd_table);
				table.setToolTipText("Double-click a document link to open");
				table.addListener(SWT.MeasureItem, new Listener() {
					@Override
					public void handleEvent(Event event) {
						event.height = 20;
					}
				});
				// show files
				// for loop
				while(dRow.next()) {
					int docID = dRow.getInt("ID");
					String title = dRow.getString("TITLE");
					String ext = Files.getFileExtension(title);	
					title += "    (posted " + sdf.format(dRow.getTimestamp("CREATED_DATE"));
					try { 
						title += " by " + dRow.getString("FORENAME") + " " + dRow.getString("SURNAME");
					} catch (SQLException e2) {
						LogController.logEvent(me, C.WARNING, e2);
					}
					title += ")";
					//LogController.log("Document found: " + docID);
					ImageData iconData = C.getImage("default-doc.png").getImageData();
					try {
						iconData = Program.findProgram(ext).getImageData();
					} catch (Exception ex) {}
					Image itemImage = new Image(Display.getCurrent(), iconData);
					TableItem item = new TableItem(table, SWT.NONE); 
					item.setBackground(C.FIELD_BGCOLOR);
					item.setText(title);
					item.setData("id", docID);
					item.setImage(itemImage);
				}	

				table.addMouseListener(new MouseListener() {
					@Override
					public void mouseDoubleClick(MouseEvent e) {
						TableItem[] selection = table.getSelection();
						int id = (Integer) selection[0].getData("id");
						LogController.log("Opening Document " + id);
						try {
							File f = DatabaseController.readDocument(id);
							Program.launch(f.getPath());
							f.deleteOnExit();
						} catch (IOException ex1) {
							ex1.printStackTrace();
						}
					}
					@Override
					public void mouseDown(MouseEvent e) {	
						btnOpenDoc.setEnabled(true);
						btnDeleteDoc.setEnabled(true);
					}
					@Override
					public void mouseUp(MouseEvent arg0) {
					}
				});
				btnOpenDoc.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						TableItem[] selection = table.getSelection();
						int id = (Integer) selection[0].getData("id");
						LogController.log("Opening Document " + id);
						try {
							File f = DatabaseController.readDocument(id);
							Program.launch(f.getPath());
							f.deleteOnExit();
						} catch (IOException ex1) {
							ex1.printStackTrace();
						}
					}
				});
				btnDeleteDoc.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						TableItem[] selection = table.getSelection();
						int id = (Integer) selection[0].getData("id");
						LogController.log("Deleting Document " + id);
						DeleteDocumentDialog ddd = new DeleteDocumentDialog();
						if(ddd.deleteOK(id)) {
							WindowController.showSpaceDetail(spaceID);	
						}
					}
				});

			} // endif docs > 0	
		} catch (SQLException e) {
			e.printStackTrace();
		}


		// row 5 - signoff header 		
		Group rowRight5 = new Group(compR, SWT.NONE);
		rowRight5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_rowRight5 = new GridLayout(3, false);
		gl_rowRight5.marginBottom = 5;
		gl_rowRight5.marginHeight = 0;
		rowRight5.setLayout(gl_rowRight5);
		rowRight5.setBackground(C.APP_BGCOLOR);

		CLabel lblSignoff = new CLabel(rowRight5, SWT.NONE);
		lblSignoff.setImage(C.getImage("signoff.png"));
		GridData gd_lblSignoff = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblSignoff.widthHint = 120;
		lblSignoff.setLayoutData(gd_lblSignoff);
		lblSignoff.setFont(C.FONT_12B);
		lblSignoff.setBackground(C.APP_BGCOLOR);
		lblSignoff.setText("Sign Off");	

		boolean signedoff = AuditController.isSpaceSignedOff(spaceID);
		final Button btnSignOff = new Button(rowRight5, SWT.NONE);
		btnSignOff.setToolTipText("Mark this space as Signed Off (authorized users only)");
		GridData gd_btnSignOff = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_btnSignOff.verticalIndent = 3;
		btnSignOff.setLayoutData(gd_btnSignOff);
		btnSignOff.setImage(C.getImage("bluetick.png"));
		btnSignOff.setText("Authorize");
		boolean showSO = (access>2 && !signedoff && AuditController.isSpaceComplete(spaceID));
		btnSignOff.setEnabled(showSO);
		btnSignOff.setVisible(showSO);

		Label lblAuthBy = new Label(rowRight5, SWT.NONE);
		lblAuthBy.setFont(C.FONT_10B);
		lblAuthBy.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		lblAuthBy.setBackground(C.APP_BGCOLOR);
		lblAuthBy.setText("Authorized By:");		

		final Label lblAuthName = new Label(rowRight5, SWT.WRAP);
		lblAuthName.setFont(C.FONT_10);
		lblAuthName.setBackground(C.APP_BGCOLOR);
		lblAuthName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		if(signedoff) {
			try {
				EsmUsersTable.Row authUser = EsmUsersTable.getRow(sRow.getSignoffID());
				String so = authUser.getForename() + " " + authUser.getSurname();
				so += "  " + sdf.format(sRow.getSignoffDate());
				lblAuthName.setText(so);
			} catch (SQLException e1) {}						
		} else {
			lblAuthName.setText(C.SPACE_NOT_AUTH);
		}

		final Button btnPrint = new Button(rowRight5, SWT.NONE);
		btnPrint.setToolTipText("Produce a printable PDF of the completed audit for this space and its entry points");
		btnPrint.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		btnPrint.setImage(C.getImage("print.png"));
		btnPrint.setText("Create Audit PDF");
		btnPrint.setEnabled(signedoff);
		btnPrint.setVisible(signedoff);

		btnSignOff.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// do database stuff
				try {
					SpacesTable.Row uRow = SpacesTable.getRow(spaceID);
					uRow.setSignedOff("TRUE");
					uRow.setSignoffID(user.getID());
					uRow.setSignoffDate(new Timestamp(new Date().getTime()));
					uRow.update();
					lblAuthName.setText(user.getForename() + " " + user.getSurname());
					btnSignOff.setEnabled(false);
					btnPrint.setEnabled(true);
					btnPrint.setVisible(true);
				} catch (SQLException e) {
					LogController.logEvent(me, C.ERROR, "Error saving Space "+spaceID+" signoff data", e);
				}				
			}
		});

		btnPrint.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) { 
				try {
					PdfController.setPath(C.TMP_DIR);
					if (PdfController.buildAudit(spaceID, true)) {
						Program.launch(C.TMP_DIR);
						Program.launch(C.TMP_DIR + C.SEP + "SPACE_"+spaceID+"_AUDIT.pdf");					
					} else {
						LogController.log("Failed to generate PDF");
					}
				} catch (DocumentException e) {
					LogController.logEvent(SpaceDetailView.class, C.ERROR, "Error getting PDF document", e);
				} catch (SQLException e) {
					LogController.logEvent(SpaceDetailView.class, C.ERROR, "Error getting DB data for PDF document", e);
				}



			}
		});





		//===============================================================================================

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




		// final layout settings	
		panels.setWeights(new int[] {11, 9});				
		parent.layout();
		parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_ARROW));
	}
}
