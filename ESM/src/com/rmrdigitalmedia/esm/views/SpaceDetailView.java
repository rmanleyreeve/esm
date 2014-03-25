package com.rmrdigitalmedia.esm.views;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SpaceDetailView {

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			shell.setSize(1380, 750);
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			Composite comp = new Composite(shell, SWT.BORDER);
			SpaceDetailView.buildPage(comp,3);
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
		Composite mainpanel = new Composite(panels,SWT.NONE);
		mainpanel.setBackground(C.APP_BGCOLOR);
		FillLayout fl_mainpanel = new FillLayout(SWT.VERTICAL);
		fl_mainpanel.marginWidth = 10;
		fl_mainpanel.marginHeight = 10;
		mainpanel.setLayout(fl_mainpanel);
		int rw = (parent.getBounds().width/3);
		int lw = (rw*2);
		
		final Composite grid = new Composite(mainpanel, SWT.NONE);
		grid.setBackground(C.APP_BGCOLOR);
		GridLayout gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.numColumns = 4;
		gridLayout.horizontalSpacing = 0;
		grid.setLayout(gridLayout);
		
		Label lblNname = new Label(grid, SWT.NONE);
		lblNname.setFont(C.FONT_12B);
		lblNname.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
		lblNname.setBackground(C.APP_BGCOLOR);
		lblNname.setText("Name:");		
		
		Label lblID = new Label(grid, SWT.NONE);
		GridData gd_lblID = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblID.horizontalIndent = 10;
		lblID.setLayoutData(gd_lblID);
		lblID.setFont(C.FONT_12B);
		lblID.setBackground(C.APP_BGCOLOR);
		lblID.setText("ID:");
		
		
		Label name = new Label(grid, SWT.BORDER);
		name.setFont(C.FONT_12);
		name.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_name = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		gd_name.widthHint = 800;
		gd_name.minimumWidth = 400;
		name.setLayoutData(gd_name);
		name.setText(row.getName());

		Label id = new Label(grid, SWT.BORDER);
		id.setFont(C.FONT_12);
		id.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_id = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_id.horizontalIndent = 10;
		gd_id.widthHint = 150;
		id.setLayoutData(gd_id);
		id.setText(""+row.getID());
		
		
		Label lblDesc = new Label(grid, SWT.NONE);
		lblDesc.setFont(C.FONT_12B);
		lblDesc.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1));
		lblDesc.setBackground(C.APP_BGCOLOR);
		lblDesc.setText("Description:");		
	
		final Text description = new Text(grid, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		description.setEditable(false);
		description.setFont(C.FONT_12);
		description.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_description = new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1);
		gd_description.widthHint = 1000;
		gd_description.heightHint = 100;
		description.setLayoutData(gd_description);
		description.setText(row.getDescription());
		
		Label lblComments = new Label(grid, SWT.NONE);
		GridData gd_lblComments = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblComments.verticalIndent = 10;
		lblComments.setLayoutData(gd_lblComments);
		lblComments.setFont(C.FONT_12B);
		lblComments.setBackground(C.APP_BGCOLOR);
		lblComments.setText("Comments:");		
		
		Button btnAdd = new Button(grid, SWT.NONE);
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
		GridData gd_btnAdd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_btnAdd.verticalIndent = 8;
		gd_btnAdd.horizontalIndent = 20;
		btnAdd.setLayoutData(gd_btnAdd);
		btnAdd.setText("Add");
		
		// loop through and display comments
		Text comment;
		Label lblAuthor,lblPosted;
		GridData gd_comment, gd_lblAuthor,gd_lblPosted;
		try {
			for (SpaceCommentsTable.Row spaceComment:SpaceCommentsTable.getRows("DELETED=FALSE AND SPACE_ID="+spaceID)) {
				
				EsmUsersTable.Row author = EsmUsersTable.getRow(spaceComment.getAuthorID());
				lblAuthor = new Label(grid, SWT.NONE);
				gd_lblAuthor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
				gd_lblAuthor.verticalIndent = 10;
				lblAuthor.setLayoutData(gd_lblAuthor);
				lblAuthor.setFont(C.FONT_9);
				lblAuthor.setBackground(C.APP_BGCOLOR);
				lblAuthor.setText(author.getForename() + " " + author.getSurname());		
				
				comment = new Text(grid, SWT.BORDER | SWT.WRAP | SWT.MULTI);
				comment.setText(spaceComment.getComment());
				comment.setEditable(false);
				comment.setFont(C.FONT_11);
				comment.setBackground(C.FIELD_BGCOLOR);
				gd_comment = new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1);
				gd_comment.widthHint = 1000;
				gd_comment.heightHint = 60;
				comment.setLayoutData(gd_comment);
				
				lblPosted = new Label(grid, SWT.NONE);
				gd_lblPosted = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
				lblPosted.setLayoutData(gd_lblPosted);
				lblPosted.setFont(C.FONT_9);
				lblPosted.setBackground(C.APP_BGCOLOR);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yy");
				lblPosted.setText("Posted " + sdf.format(spaceComment.getUpdateDate()));		

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		
		
		
		
		
		
		
		
		
		Composite rightpanel = new Composite(panels,SWT.NONE);
		rightpanel.setBackground(C.APP_BGCOLOR);
		rightpanel.setLayout(new FillLayout());
		panels.setWeights(new int[] {2, 1});	
		
		panels.addListener(SWT.Resize, new Listener() {
      @Override
      public void handleEvent(Event arg0) {
      	description.setText(description.getText());
      	description.update();
      	grid.layout();
      }
		});

		
		parent.layout();
		
	}
}
