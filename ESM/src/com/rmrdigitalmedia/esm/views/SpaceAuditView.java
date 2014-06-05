package com.rmrdigitalmedia.esm.views;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.models.EsmUsersTable.Row;
import com.rmrdigitalmedia.esm.models.SpacesTable;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.custom.CLabel;

public class SpaceAuditView {

	static Row user = WindowController.user;

	static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
	private static Label sep, col1;
	private static Label col2;
	private static Text col4;

	@SuppressWarnings("unused")
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
			SpaceAuditView.buildPage(comp, 1);
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
	public static void buildPage(Composite parent, final int spaceID) {

		SpacesTable.Row sRow = null;
		try {
			sRow = SpacesTable.getRow(spaceID);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		parent.setLayout(new FillLayout(SWT.VERTICAL));

		// scrolling frame to hold the grid panel
		final ScrolledComposite scrollPanel = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.BORDER);

		// the panel that holds the various info rows
		final Composite comp = new Composite(scrollPanel, SWT.NONE);
		GridLayout gl_comp = new GridLayout(1, true);
		gl_comp.marginBottom = 50;
		gl_comp.marginRight = 10;
		comp.setLayout(gl_comp);
		comp.setBackground(C.APP_BGCOLOR);



		// table layout
		Group headerRow = new Group(comp, SWT.BORDER);
		headerRow.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_headerRow = new GridLayout(2, false);
		gl_headerRow.horizontalSpacing = 0;
		gl_headerRow.marginWidth = 0;
		gl_headerRow.verticalSpacing = 0;
		gl_headerRow.marginHeight = 0;
		headerRow.setLayout(gl_headerRow);
		headerRow.setBackground(C.APP_BGCOLOR);

		// header row
		Label lblName = new Label(headerRow, SWT.NONE);
		lblName.setFont(C.FONT_12B);
		lblName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblName.setBackground(C.APP_BGCOLOR);
		lblName.setText("Internal Space Checklist");		

		Label lblStatus = new Label(headerRow, SWT.NONE);
		lblStatus.setFont(C.FONT_12B);
		lblStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblStatus.setBackground(C.APP_BGCOLOR);
		lblStatus.setText("Internal Space Completion");		

		//table layout
		Group auditTable = new Group(comp, SWT.BORDER);
		auditTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gl_auditTable = new GridLayout(4, false);
		gl_auditTable.verticalSpacing = 1;
		gl_auditTable.marginHeight = 0;
		gl_auditTable.horizontalSpacing = 1;
		gl_auditTable.marginWidth = 0;
		auditTable.setLayout(gl_auditTable);
		auditTable.setBackground(C.APP_BGCOLOR);

		// column headers
		CLabel lblChecklist = new CLabel(auditTable, SWT.NONE);
		lblChecklist.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblChecklist.setBackground(C.BAR_BGCOLOR);
		lblChecklist.setFont(C.FONT_12B);
		lblChecklist.setText("Checklist");

		CLabel lblHint = new CLabel(auditTable, SWT.CENTER);
		GridData gd_lblHint = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblHint.widthHint = 50;
		lblHint.setLayoutData(gd_lblHint);
		lblHint.setBackground(C.BAR_BGCOLOR);
		lblHint.setFont(C.FONT_12B);
		lblHint.setText("Hint");

		CLabel lblOptions = new CLabel(auditTable, SWT.NONE);
		lblOptions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblOptions.setBackground(C.BAR_BGCOLOR);
		lblOptions.setText("Options");
		lblOptions.setFont(C.FONT_12B);

		CLabel lblComments = new CLabel(auditTable, SWT.NONE);
		lblComments.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblComments.setBackground(C.BAR_BGCOLOR);
		lblComments.setText("Comments");
		lblComments.setFont(C.FONT_12B);

		sep = new Label(auditTable, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.CENTER);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));		

		System.out.println(spaceID);
		
		// start loop through audit checklist questions
		for(int i=0; i<10; i++) {
			
			// checklist text column
			col1 = new Label(auditTable, SWT.NONE);
			GridData gd_col1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			gd_col1.horizontalIndent = 5;
			col1.setLayoutData(gd_col1);
			col1.setBackground(C.APP_BGCOLOR);
			col1.setFont(C.FONT_10);
			col1.setText("What are the internal dimensions of the space?");		
			
			// hint icon
			col2 = new Label(auditTable, SWT.NONE);
			col2.setToolTipText("Click for Hint Text");
			col2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent arg0) {
					EsmApplication.alert("Hint Text Here");
				}
			});
			col2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			col2.setBackground(C.APP_BGCOLOR);
			col2.setImage(C.getImage("/img/hint.png"));

			// options control cell to hold form objects
			Composite optionsCell = new Composite(auditTable, SWT.BORDER | SWT.NO_BACKGROUND);
			GridData gd_optionsCell = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			gd_optionsCell.heightHint = 40;
			optionsCell.setLayoutData(gd_optionsCell);
			optionsCell.setBackground(C.APP_BGCOLOR);


			// comments field
			col4 = new Text(auditTable, SWT.BORDER | SWT.WRAP | SWT.MULTI);
			col4.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
			col4.setEditable(true);
			col4.setFont(C.FONT_10);
			col4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			GridData gd_col4 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			gd_col4.widthHint = 200;
			gd_col4.heightHint = 30;
			col4.setLayoutData(gd_col4);

			sep = new Label(auditTable, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.CENTER);
			sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));		

		}
		//end loop 		

		scrollPanel.setContent(comp);
		scrollPanel.setExpandVertical(true);
		scrollPanel.setExpandHorizontal(true);
		scrollPanel.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle r = scrollPanel.getClientArea();
				scrollPanel.setMinSize(comp.computeSize(r.width, SWT.DEFAULT));
			}
		});

		// final layout settings	
		parent.layout();

	}
}
