package com.rmrdigitalmedia.esm.test;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
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

	static int w = 900;
	
	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			shell.setSize(1380, 750);
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			Composite comp = new Composite(shell, SWT.BORDER);
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

	
	@SuppressWarnings("unused")
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

	    final ScrolledComposite scrollComposite = new ScrolledComposite(panels, SWT.V_SCROLL | SWT.BORDER);
	    
	    final Composite comp = new Composite(scrollComposite, SWT.NONE);
	    GridLayout gl_comp = new GridLayout(1, true);
	    gl_comp.marginRight = 10;
	    comp.setLayout(gl_comp);
	    comp.setBackground(C.APP_BGCOLOR);
	    
	    // row 1 - name, id, description fields
	    Group row1 = new Group(comp, SWT.NONE);
	    row1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	    row1.setLayout(new GridLayout(4, true));
	    row1.setBackground(C.APP_BGCOLOR);

		Label lblNname = new Label(row1, SWT.NONE);
		lblNname.setFont(C.FONT_12B);
		lblNname.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		lblNname.setBackground(C.APP_BGCOLOR);
		lblNname.setText("Name:");		
		
		Label lblID = new Label(row1, SWT.NONE);
		GridData gd_lblID = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_lblID.horizontalIndent = 10;
		lblID.setLayoutData(gd_lblID);
		lblID.setFont(C.FONT_12B);
		lblID.setBackground(C.APP_BGCOLOR);
		lblID.setText("ID:");		
		
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

	    // row 2 - comments header button bar
		Composite row2 = new Composite(comp, SWT.NONE);
		row2.setLayout(new GridLayout(2, false));
		row2.setBackground(C.APP_BGCOLOR);		
		
		Label lblComments = new Label(row2, SWT.NONE);
		lblComments.setFont(C.FONT_12B);
		lblComments.setBackground(C.APP_BGCOLOR);
		lblComments.setText("Comments:");		
		
		Button btnAdd = new Button(row2, SWT.NONE);
		GridData gd_btnAdd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
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
		btnAdd.setImage(SWTResourceManager.getImage(SpaceDetailView.class, "/img/Add.png"));
		btnAdd.setText("Add");
		
		// loop through and display comments
		
		try {
			for (SpaceCommentsTable.Row spaceComment:SpaceCommentsTable.getRows("DELETED=FALSE AND SPACE_ID="+spaceID)) {
				
				Group commentRow = new Group(comp, SWT.NONE);
				commentRow.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			    GridLayout gl_commentRow = new GridLayout(1, true);
			    gl_commentRow.marginHeight = 0;
			    commentRow.setLayout(gl_commentRow);

			    EsmUsersTable.Row author = EsmUsersTable.getRow(spaceComment.getAuthorID());
				Label lblAuthor = new Label(commentRow, SWT.NONE);
				GridData gd_lblAuthor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
				lblAuthor.setLayoutData(gd_lblAuthor);
				lblAuthor.setFont(C.FONT_9);
				//lblAuthor.setBackground(C.APP_BGCOLOR);
				lblAuthor.setText(author.getForename() + " " + author.getSurname());		
				
				Text comment = new Text(commentRow, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI | SWT.READ_ONLY);
				comment.setText(spaceComment.getComment());
				comment.setEditable(false);
				comment.setFont(C.FONT_11);
				comment.setBackground(C.FIELD_BGCOLOR);
				GridData gd_comment = new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1);
				gd_comment.widthHint = 1000;
				int h = comment.computeSize(1000,SWT.DEFAULT,true).y;
				gd_comment.heightHint = (h>60) ? h:60;
				comment.setLayoutData(gd_comment);
				
				Label lblPosted = new Label(commentRow, SWT.NONE);
				GridData gd_lblPosted = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
				lblPosted.setLayoutData(gd_lblPosted);
				lblPosted.setFont(C.FONT_9);
				//lblPosted.setBackground(C.APP_BGCOLOR);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yy");
				lblPosted.setText("Posted " + sdf.format(spaceComment.getUpdateDate()));		

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
	    		
	    
	    scrollComposite.setContent(comp);
	    scrollComposite.setExpandVertical(true);
	    scrollComposite.setExpandHorizontal(true);
	    scrollComposite.addControlListener(new ControlAdapter() {
	      @Override
	      public void controlResized(ControlEvent e) {
	        Rectangle r = scrollComposite.getClientArea();
	        scrollComposite.setMinSize(comp.computeSize(r.width, SWT.DEFAULT));
	      }
	    });
		
		
		
		
		
		
		
		
		
		
		
		Composite rightpanel = new Composite(panels,SWT.NONE);
		rightpanel.setBackground(C.APP_BGCOLOR);
		rightpanel.setLayout(new FillLayout());
		panels.setWeights(new int[] {2, 1});	
				
		parent.layout();
		
	}
}
