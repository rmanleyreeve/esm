package com.rmrdigitalmedia.esm.views;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.forms.NewSpaceCommentForm;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.SpaceCommentsTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;

public class SpaceDetailView {
	
	static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");

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
			SpaceDetailView.buildPage(comp,1);
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
		
		Button btnAdd = new Button(row2, SWT.RIGHT);
		GridData gd_btnAdd = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_btnAdd.verticalIndent = 3;
		btnAdd.setLayoutData(gd_btnAdd);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				NewSpaceCommentForm nscf = new NewSpaceCommentForm(spaceID, WindowController.user.getID());					
				if(nscf.complete()) {
					LogController.log("New Space Comment saved in database");
					WindowController.showSpaceDetail(spaceID);					
				}
			}
		});
		btnAdd.setImage(C.getImage("/img/16_comment_add.png"));
		btnAdd.setText("Add");
		new Label(row2, SWT.NONE);
		new Label(row2, SWT.NONE);
		
		// loop through and display comments in descending order	
		try {
			for (SpaceCommentsTable.Row spaceComment:SpaceCommentsTable.getRows("DELETED=FALSE AND SPACE_ID="+spaceID + " ORDER BY ID DESC")) {
				
				Group commentRow = new Group(compL, SWT.NONE);
				commentRow.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		    GridLayout gl_commentRow = new GridLayout(1, true);
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
				GridData gd_lblPosted = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
				gd_lblPosted.horizontalIndent = 1;
				lblPosted.setLayoutData(gd_lblPosted);
				lblPosted.setFont(C.FONT_9);
				lblPosted.setBackground(C.APP_BGCOLOR);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
				lblPosted.setText("Posted " + sdf.format(spaceComment.getUpdateDate()));		

			}
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
    gl_rowRight1.marginBottom = 10;
    gl_rowRight1.marginHeight = 0;
    rowRight1.setLayout(gl_rowRight1);
    rowRight1.setBackground(C.APP_BGCOLOR);

    EsmUsersTable.Row author = null;
		try {
			author = EsmUsersTable.getRow(row.getAuthorID());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
    GridLayout gl_rowRight2 = new GridLayout(3, false);
    gl_rowRight2.marginBottom = 20;
    gl_rowRight2.marginHeight = 0;
    rowRight2.setLayout(gl_rowRight2);
    rowRight2.setBackground(C.APP_BGCOLOR);
		
		Label lblAudits = new Label(rowRight2, SWT.NONE);
		lblAudits.setFont(C.FONT_12B);
		lblAudits.setBackground(C.APP_BGCOLOR);
		lblAudits.setText("Audits");	
		
		Button btnAddAudit = new Button(rowRight2, SWT.NONE);
		GridData gd_btnAddAudit = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1);
		gd_btnAddAudit.verticalIndent = 3;
		btnAddAudit.setLayoutData(gd_btnAddAudit);
		btnAddAudit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//
			}
		});
		btnAddAudit.setImage(C.getImage("/img/16_CircledPlus.png"));
		btnAddAudit.setText("Add");
   
		Label lblSpaceAudit = new Label(rowRight2, SWT.NONE);
		lblSpaceAudit.setFont(C.FONT_10B);
		lblSpaceAudit.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		lblSpaceAudit.setBackground(C.APP_BGCOLOR);
		lblSpaceAudit.setText("Internal Space");		

		Label lblSpaceAuditImg = new Label(rowRight2, SWT.NONE);
		// work out completion status based on id
		int scs = (row.getID()*20);
		lblSpaceAuditImg.setImage(C.getImage("/img/Percent_"+ cs +".png"));
		GridData gd_lblSpaceAuditImg = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblSpaceAuditImg.widthHint = 160;
		lblSpaceAuditImg.setLayoutData(gd_lblSpaceAuditImg);
		lblSpaceAuditImg.setBackground(C.APP_BGCOLOR);
   
		Label lblSpaceAuditLight = new Label(rowRight2, SWT.RIGHT);
		lblSpaceAuditLight.setImage(C.getImage("/img/Red.png"));
		lblSpaceAuditLight.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblSpaceAuditLight.setBackground(C.APP_BGCOLOR);
		
		Label lblEntryPoint = new Label(rowRight2, SWT.NONE);
		lblEntryPoint.setFont(C.FONT_10B);
		lblEntryPoint.setBackground(C.APP_BGCOLOR);
		lblEntryPoint.setText("Entry Point");
		
		Label lblEntryPointAuditImg = new Label(rowRight2, SWT.NONE);
		// work out completion status based on id
		int epcs = (row.getID()*20);
		lblEntryPointAuditImg.setImage(C.getImage("/img/Percent_"+ epcs +".png"));
		GridData gd_lblEntryPointAuditImg = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblEntryPointAuditImg.widthHint = 160;
		lblEntryPointAuditImg.setLayoutData(gd_lblEntryPointAuditImg);
		lblEntryPointAuditImg.setBackground(C.APP_BGCOLOR);
   
		Label lblEntryPointAuditLight = new Label(rowRight2, SWT.RIGHT);
		lblEntryPointAuditLight.setImage(C.getImage("/img/Amber.png"));
		lblEntryPointAuditLight.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblEntryPointAuditLight.setBackground(C.APP_BGCOLOR);
		

	
	  // row 3 - photos header & button bar		
    Group rowRight3 = new Group(compR, SWT.NONE);
    rowRight3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    GridLayout gl_rowRight3 = new GridLayout(3, false);
    gl_rowRight3.marginBottom = 20;
    gl_rowRight3.marginHeight = 0;
    rowRight3.setLayout(gl_rowRight3);
    rowRight3.setBackground(C.APP_BGCOLOR);
		
		CLabel lblPhotos = new CLabel(rowRight3, SWT.NONE);
		lblPhotos.setFont(C.FONT_12B);
		lblPhotos.setBackground(C.APP_BGCOLOR);
		lblPhotos.setImage(C.getImage("/img/16_camera.png"));
		lblPhotos.setText("Photos");	
		
		Button btnAddPhoto = new Button(rowRight3, SWT.NONE);
		GridData gd_btnAddPhoto = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1);
		gd_btnAddPhoto.verticalIndent = 3;
		btnAddPhoto.setLayoutData(gd_btnAddPhoto);
		btnAddPhoto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//
			}
		});
		btnAddPhoto.setImage(C.getImage("/img/16_image_add.png"));
		btnAddPhoto.setText("Add");

	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
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

    
		
		
		
		
		
		panels.setWeights(new int[] {2, 1});				
		parent.layout();
		
	}
}
