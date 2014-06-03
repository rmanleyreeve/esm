package com.rmrdigitalmedia.esm.test;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
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
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.controllers.UploadController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.forms.EditSpaceForm;
import com.rmrdigitalmedia.esm.forms.NewSpaceCommentForm;
import com.rmrdigitalmedia.esm.forms.NewSpacePhotoForm;
import com.rmrdigitalmedia.esm.models.EntrypointsTable;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.SpaceCommentsTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;

public class SpaceDetailViewTest {
	
	static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
	private static Label sep;

	private static String df(Timestamp ts) {
		SimpleDateFormat d = new SimpleDateFormat("dd - MM - yyyy");
		SimpleDateFormat t = new SimpleDateFormat("kk:mm");
		return new String( "Date: " + d.format(ts) + "  Time:" + t.format(ts) );
	}

	
	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			shell.setSize(1380, 750);
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			Composite comp = new Composite(shell, SWT.BORDER);
			for (Point p:Display.getDefault().getIconSizes()) {
				System.out.println(p);
			}
			
			SpaceDetailViewTest.buildPage(comp,1);
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
		
		SpacesTable.Row row = null;
		try {
			row = SpacesTable.getRow(spaceID);
		} catch (SQLException ex) {
			ex.printStackTrace();
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
		name.setText(row.getName());
	
		Label id = new Label(row1, SWT.BORDER);
		id.setFont(C.FONT_12);
		id.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_id = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_id.horizontalIndent = 10;
		id.setLayoutData(gd_id);
		id.setText(""+row.getID());
	
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
		description.setText(row.getDescription());		
		
		if( 9==9 )	{	
			Button btnEditSpace = new Button(row1, SWT.RIGHT);
			btnEditSpace.setText("Edit");
			btnEditSpace.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1));
			btnEditSpace.setToolTipText("Edit details for this Enclosed Space");
			btnEditSpace.setImage(C.getImage("/img/16_edit.png"));
			btnEditSpace.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					EditSpaceForm nsf = new EditSpaceForm(spaceID);					
					if(nsf.complete()) {
						WindowController.showSpaceDetail(spaceID);									
					}
				}
			});		
		}
		
		
	
		// row 2 - comments header & button bar
		Composite row2 = new Composite(compL, SWT.NONE);
		//row2.setLayout(new GridLayout(3, false));
		row2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		row2.setLayout(new GridLayout(4, true));
		row2.setBackground(C.APP_BGCOLOR);		
			
		CLabel lblComments = new CLabel(row2, SWT.NONE);
		lblComments.setImage(C.getImage("/img/16_comment.png"));
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
				NewSpaceCommentForm nscf = new NewSpaceCommentForm(spaceID, 1);					
				if(nscf.complete()) {
					LogController.log("New Space Comment saved in database");
					WindowController.showSpaceDetail(spaceID);					
				}
			}
		});
		btnAddComment.setImage(C.getImage("/img/16_comment_add.png"));
		btnAddComment.setText("Add");
			
		// loop through and display comments in descending order	
		try {
				SpaceCommentsTable.Row spaceComment = SpaceCommentsTable.getRow(1);
				
				Group commentRow = new Group(compL, SWT.NONE);
				commentRow.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			    GridLayout gl_commentRow = new GridLayout(4, false);
			    gl_commentRow.marginHeight = 0;
			    gl_commentRow.horizontalSpacing = 0;
			    gl_commentRow.verticalSpacing = 0;
			    commentRow.setLayout(gl_commentRow);
			    commentRow.setBackground(C.APP_BGCOLOR);
		
			    EsmUsersTable.Row author = EsmUsersTable.getRow(spaceComment.getAuthorID());
				Label lblAuthor = new Label(commentRow, SWT.NONE);
				GridData gd_lblAuthor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
				gd_lblAuthor.horizontalIndent = 1;
				lblAuthor.setLayoutData(gd_lblAuthor);
				lblAuthor.setFont(C.FONT_9);
				lblAuthor.setBackground(C.APP_BGCOLOR);
				lblAuthor.setText(author.getForename() + " " + author.getSurname());		
				
				Text comment = new Text(commentRow, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI | SWT.READ_ONLY);
				comment.setText(spaceComment.getComment());
				comment.setEditable(false);
				comment.setFont(C.FONT_11);
				comment.setBackground(C.FIELD_BGCOLOR);
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
				lblPosted.setText("Posted " + sdf.format(spaceComment.getUpdateDate()));
									
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
						//
					}
				});
				
				Button btnDeleteComment = new Button(commentRow, SWT.NONE);
				btnDeleteComment.setText("Delete");
				GridData gd_btnDeleteComment = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
				gd_btnDeleteComment.horizontalIndent = 5;
				btnDeleteComment.setLayoutData(gd_btnDeleteComment);
				btnDeleteComment.setToolTipText("Delete this comment");
				btnDeleteComment.setFont(C.FONT_9);
				new Label(commentRow, SWT.NONE);
				btnDeleteComment.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						//
					}
				});
				
	
			
		} catch (SQLException e) {
			e.printStackTrace();
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
			author = EsmUsersTable.getRow(row.getAuthorID());
		} catch (SQLException e1) {
			LogController.logEvent(SpaceDetailViewTest.class, 1, e1);
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
		lblCreatedDate.setText(df(row.getCreatedDate()));
	
		Label lblUpdated = new Label(rowRight1, SWT.NONE);
		lblUpdated.setFont(C.FONT_10B);
		lblUpdated.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		lblUpdated.setBackground(C.APP_BGCOLOR);
		lblUpdated.setText("Created:");		
	
		Label lblUpdatedDate = new Label(rowRight1, SWT.WRAP);
		lblUpdatedDate.setFont(C.FONT_10);
		lblUpdatedDate.setBackground(C.APP_BGCOLOR);
		lblUpdatedDate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		lblUpdatedDate.setText(df(row.getUpdateDate()));
	 
		Label lblCompletion = new Label(rowRight1, SWT.NONE);
		lblCompletion.setFont(C.FONT_10B);
		lblCompletion.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		lblCompletion.setBackground(C.APP_BGCOLOR);
		lblCompletion.setText("Completion Status:");		
	
		Label lblCompletionImg = new Label(rowRight1, SWT.NONE);
		// work out completion status based on id
		int cs = (row.getID()*20);
		lblCompletionImg.setImage(C.getImage("/img/Percent_"+ cs +".png"));
		lblCompletionImg.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		lblCompletionImg.setBackground(C.APP_BGCOLOR);		
		
	  // row 2 - audit header & button bar		
    Group rowRight2 = new Group(compR, SWT.NONE);
    rowRight2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    GridLayout gl_rowRight2 = new GridLayout(4, false);
    gl_rowRight2.marginBottom = 5;
    gl_rowRight2.marginHeight = 0;
    rowRight2.setLayout(gl_rowRight2);
    rowRight2.setBackground(C.APP_BGCOLOR);
		
		Label lblAudits = new Label(rowRight2, SWT.NONE);
		GridData gd_lblAudits = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_lblAudits.widthHint = 120;
		lblAudits.setLayoutData(gd_lblAudits);
		lblAudits.setFont(C.FONT_12B);
		lblAudits.setBackground(C.APP_BGCOLOR);
		lblAudits.setText("Audits");	
		
	   
		Label lblSpaceAudit = new Label(rowRight2, SWT.NONE);
		lblSpaceAudit.setFont(C.FONT_10B);
		lblSpaceAudit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblSpaceAudit.setBackground(C.APP_BGCOLOR);
		lblSpaceAudit.setText(row.getName());		
	
		Label lblSpaceAuditImg = new Label(rowRight2, SWT.NONE);
		// work out completion status based on id
		int scs = (row.getID()*20);
		lblSpaceAuditImg.setImage(C.getImage("/img/Percent_"+ scs +".png"));
		GridData gd_lblSpaceAuditImg = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblSpaceAuditImg.widthHint = 160;
		lblSpaceAuditImg.setLayoutData(gd_lblSpaceAuditImg);
		lblSpaceAuditImg.setBackground(C.APP_BGCOLOR);
	   
		Label lblSpaceAuditLight = new Label(rowRight2, SWT.RIGHT);
		lblSpaceAuditLight.setImage(C.getImage("/img/red.png"));
		GridData gd_lblSpaceAuditLight = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_lblSpaceAuditLight.horizontalIndent = 10;
		lblSpaceAuditLight.setLayoutData(gd_lblSpaceAuditLight);
		lblSpaceAuditLight.setBackground(C.APP_BGCOLOR);
		
		Button btnEditSpaceAudit = new Button(rowRight2, SWT.NONE);
		btnEditSpaceAudit.setToolTipText("Launch the Internal Space Audit for " + row.getName());
		GridData gd_btnEditSpaceAudit = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_btnEditSpaceAudit.verticalIndent = 3;
		btnEditSpaceAudit.setLayoutData(gd_btnEditSpaceAudit);
		btnEditSpaceAudit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//
			}
		});
		btnEditSpaceAudit.setImage(C.getImage("/img/16_edit.png"));
		btnEditSpaceAudit.setText("Audit");
		
		
		sep = new Label(rowRight2, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));		
		
		CLabel lblEntryPoint;
		Label lblEntryPointAuditImg, lblEntryPointAuditLight;
		GridData gd_lblEntryPointAuditImg, gd_lblEntryPointAuditLight, gd_btnEditAudit, gd_lblEntryPoint;
		Button btnEditAudit;
		EntrypointsTable.Row[] epRows = null;
		int rh = 20;
		
		try {
			epRows = EntrypointsTable.getRows("SPACE_ID=" + spaceID);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// for loop
		for (EntrypointsTable.Row epRow:epRows) {
		
			lblEntryPoint = new CLabel(rowRight2, SWT.NONE);
			lblEntryPoint.setFont(C.FONT_10);
			gd_lblEntryPoint = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			//gd_lblEntryPoint.horizontalIndent = 10;
			gd_lblEntryPoint.heightHint = rh;
			lblEntryPoint.setLayoutData(gd_lblEntryPoint);
			lblEntryPoint.setBackground(C.APP_BGCOLOR);
			lblEntryPoint.setImage(C.getImage("/img/16_door.png"));
			lblEntryPoint.setText(epRow.getName());
			
			// work out completion status based on id
			int epcs = (row.getID()*20);
			lblEntryPointAuditImg = new Label(rowRight2, SWT.NONE);
			lblEntryPointAuditImg.setImage(C.getImage("/img/Percent_"+ epcs +".png"));
			gd_lblEntryPointAuditImg = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
			gd_lblEntryPointAuditImg.widthHint = 160;
			gd_lblEntryPointAuditImg.heightHint = rh;
			lblEntryPointAuditImg.setLayoutData(gd_lblEntryPointAuditImg);
			lblEntryPointAuditImg.setBackground(C.APP_BGCOLOR);
		   
			lblEntryPointAuditLight = new Label(rowRight2, SWT.RIGHT);
			lblEntryPointAuditLight.setImage(C.getImage("/img/amber.png"));
			gd_lblEntryPointAuditLight = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
			gd_lblEntryPointAuditLight.horizontalIndent = 10;
			gd_lblEntryPointAuditLight.heightHint = rh;
			lblEntryPointAuditLight.setLayoutData(gd_lblEntryPointAuditLight);
			lblEntryPointAuditLight.setBackground(C.APP_BGCOLOR);
			
			btnEditAudit = new Button(rowRight2, SWT.NONE);
			btnEditAudit.setToolTipText("Launch the Entry Point Audit for " + epRow.getName());
			gd_btnEditAudit = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
			//gd_btnEditAudit.verticalIndent = 3;
			gd_btnEditAudit.heightHint = rh;
			btnEditAudit.setLayoutData(gd_btnEditAudit);
			btnEditAudit.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					//
				}
			});
			//btnEditAudit.setImage(C.getImage("/img/16_edit.png"));
			btnEditAudit.setFont(C.FONT_9);
			btnEditAudit.setText("Audit");
				
		} // end for
		
		sep = new Label(rowRight2, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));		
		
		Button btnAddAudit = new Button(rowRight2, SWT.NONE);
		btnAddAudit.setToolTipText("Add a new Entry Point to this Space");
		GridData gd_btnAddAudit = new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1);
		gd_btnAddAudit.verticalIndent = 3;
		btnAddAudit.setLayoutData(gd_btnAddAudit);
		btnAddAudit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//
			}
		});
		btnAddAudit.setImage(C.getImage("/img/16_CircledPlus.png"));
		btnAddAudit.setText("Add Entry Audit");
		
		
		
		
		
		
		
		
		
		
	
	  // row 3 - photos header & button bar		
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
		lblPhotos.setImage(C.getImage("/img/16_camera.png"));
		lblPhotos.setText("Photos");	
		
		Button btnAddPhoto = new Button(rowRight3, SWT.NONE);
		btnAddPhoto.setToolTipText("Add a new Photo for this Enclosed Space");
		GridData gd_btnAddPhoto = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_btnAddPhoto.verticalIndent = 3;
		btnAddPhoto.setLayoutData(gd_btnAddPhoto);
		btnAddPhoto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				NewSpacePhotoForm nspf = new NewSpacePhotoForm(spaceID, 1);					
				if(nspf.complete()) {
					LogController.log("New Space Photo & Comment saved in database");
					WindowController.showSpaceDetail(spaceID);					
				}
			}
		});
		btnAddPhoto.setImage(C.getImage("/img/16_image_add.png"));
		btnAddPhoto.setText("Add");
			
		
		// PHOTOS ===============================
		String imgDir = C.IMG_DIR + C.SEP + spaceID + C.SEP;
		final String imgDirFull =  imgDir + "full";
		String imgDirThumb = imgDir + "thumb";
		new File(imgDir).mkdir();
		new File(imgDirThumb).mkdir();
		new File(imgDirFull).mkdir();
		if (new File(imgDirThumb).listFiles().length > 0) {
			// photos exist - show gallery
			Composite gallHolder = new Composite(rowRight3, SWT.NONE);
			GridData gd_gallHolder = new GridData(SWT.FILL, SWT.FILL, true, false);
			gd_gallHolder.horizontalSpan = 3;
			gallHolder.setLayoutData(gd_gallHolder);
			gallHolder.setLayout(new GridLayout(1, true));
			gallHolder.setBackground(C.FIELD_BGCOLOR);
				
			final Gallery gallery = new Gallery(gallHolder, SWT.MULTI | SWT.H_SCROLL);
			GridData gd_gallery = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			gd_gallery.minimumHeight = 150;
			gallery.setLayoutData(gd_gallery);
			gallery.setBackground(C.FIELD_BGCOLOR);
		
			NoGroupRenderer gr = new NoGroupRenderer();
			gr.setMinMargin(0);
			gr.setItemHeight(150);
			gr.setItemWidth(150);
			gr.setAutoMargin(true);
			gallery.setGroupRenderer(gr);
			gallery.setToolTipText("Double-click a thumbnail image to open full size");
		
			DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
			ir.setShowRoundedSelectionCorners(false);
			gallery.setItemRenderer(ir);
			
			GalleryItem group = new GalleryItem(gallery, SWT.NONE);
			for (File f:new File(imgDirThumb).listFiles()) {
				LogController.log("Image found: " + f);
				Image itemImage = C.getExtImage(f.getPath());		
				if (itemImage != null) {
					GalleryItem item = new GalleryItem(group, SWT.NONE);
					item.setImage(itemImage);
					item.setData("file", f.getName());
					item.setText(f.getName()); 
				}
			}		
			gallery.addMouseListener(new MouseListener() {
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					GalleryItem[] selection = gallery.getSelection();
					if (selection == null)
						return;
					GalleryItem item = selection[0];			
					String fullImg = imgDirFull + C.SEP +(String)item.getData("file");
					LogController.log("Opening Image={" + fullImg + "}");
					Program.launch(fullImg);
				}
				@Override
				public void mouseDown(MouseEvent e) {}
				@Override
				public void mouseUp(MouseEvent e) {}
			});
			
		} // endif files > 0
			
		
		  // row 4 - docs header & button bar		
	    Group rowRight4 = new Group(compR, SWT.NONE);
	    rowRight4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	    GridLayout gl_rowRight4 = new GridLayout(3, false);
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
		lblDocs.setImage(C.getImage("/img/16_document_text.png"));
		lblDocs.setText("Documents");	
		
		Button btnAddDoc = new Button(rowRight4, SWT.NONE);
		btnAddDoc.setToolTipText("Add a new document for this Enclosed Space");
		GridData gd_btnAddDoc = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_btnAddDoc.verticalIndent = 3;
		btnAddDoc.setLayoutData(gd_btnAddDoc);
		btnAddDoc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(UploadController.uploadSpaceDocument(spaceID,null)) {
					WindowController.showSpaceDetail(spaceID);	
				}
			}
		});
		btnAddDoc.setImage(C.getImage("/img/16_document_text_add.png"));
		btnAddDoc.setText("Add");		

		// DOCS ===============================
		final String docDir = C.DOC_DIR + C.SEP + spaceID + C.SEP;
		new File(docDir).mkdir();
		if (new File(docDir).listFiles().length > 0) {
			// docs exist - show table
		    final Table table = new Table(rowRight4, SWT.NONE | SWT.FULL_SELECTION);
		    table.setLayout(new FillLayout());
		    table.setBackground(C.FIELD_BGCOLOR);
			GridData gd_table = new GridData(GridData.FILL_BOTH);
			gd_table.grabExcessVerticalSpace = true;
			gd_table.grabExcessHorizontalSpace=true;
			gd_table.heightHint = 50;
			gd_table.horizontalSpan = 3;
			table.setLayoutData(gd_table);
			table.setToolTipText("Double-click a document link to open");
			table.addListener(SWT.MeasureItem, new Listener() {
			   @Override
			   public void handleEvent(Event event) {
			      event.height = 20;
			   }
			});
			// show files
			for (File f:new File(docDir).listFiles()) {
				if(!f.isHidden()) {
					LogController.log("Document found: " + f);
					String ext = Files.getFileExtension(f.getName());			
					ImageData iconData = Program.findProgram(ext).getImageData();
					Image itemImage = new Image(Display.getCurrent(), iconData);
			    TableItem item = new TableItem(table, SWT.NONE); 
			    item.setBackground(C.FIELD_BGCOLOR);
			    item.setText(f.getName());
			    item.setImage(itemImage);
				}
			}		
		   table.addListener(SWT.MouseDoubleClick, new Listener() {
				@Override
				public void handleEvent(Event e) {	
				TableItem[] selection = table.getSelection();
				String s = selection[0].getText();
				LogController.log("Opening Document={" + s + "}");
				String doc = docDir + C.SEP + s;
				Program.launch(doc);
		        }
		     });
		} // endif files > 0	
	
		// row 5 - signoff header 		
	    Group rowRight5 = new Group(compR, SWT.NONE);
	    rowRight5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	    GridLayout gl_rowRight5 = new GridLayout(3, false);
	    gl_rowRight5.marginBottom = 5;
	    gl_rowRight5.marginHeight = 0;
	    rowRight5.setLayout(gl_rowRight5);
	    rowRight5.setBackground(C.APP_BGCOLOR);
		
		CLabel lblSignoff = new CLabel(rowRight5, SWT.NONE);
		lblSignoff.setImage(C.getImage("/img/16_new_edit.png"));
		GridData gd_lblSignoff = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblSignoff.widthHint = 120;
		lblSignoff.setLayoutData(gd_lblSignoff);
		lblSignoff.setFont(C.FONT_12B);
		lblSignoff.setBackground(C.APP_BGCOLOR);
		lblSignoff.setText("Sign Off");	
	
		Button btnSignOff = new Button(rowRight5, SWT.NONE);
		btnSignOff.setToolTipText("Mark this space as Signed Off (authorized users only)");
		GridData gd_btnSignOff = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_btnSignOff.verticalIndent = 3;
		btnSignOff.setLayoutData(gd_btnSignOff);
		btnSignOff.setImage(C.getImage("/img/bluetick.png"));
		btnSignOff.setText("Authorize");
		btnSignOff.setEnabled(true);
		
		Label lblAuthBy = new Label(rowRight5, SWT.NONE);
		lblAuthBy.setFont(C.FONT_10B);
		lblAuthBy.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		lblAuthBy.setBackground(C.APP_BGCOLOR);
		lblAuthBy.setText("Authorized By:");		
	
		final Label lblAuthName = new Label(rowRight5, SWT.WRAP);
		lblAuthName.setFont(C.FONT_10);
		lblAuthName.setBackground(C.APP_BGCOLOR);
		lblAuthName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		lblAuthName.setText(C.SPACE_NOT_AUTH);
		new Label(rowRight5, SWT.NONE);
		
		btnSignOff.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// do database stuff
				lblAuthName.setText("XXXX");
			}
		});
	
		
		
		
		
			
			
			
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
		panels.setWeights(new int[] {2, 1});				
		parent.layout();
		
		parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_ARROW));
	}

	
}