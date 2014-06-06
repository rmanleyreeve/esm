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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

@SuppressWarnings("unused")
public class SpaceAuditView {

	static Row user = WindowController.user;
	static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
	private static Label sep;
	private static int qNum;
	static int rowHeight = 45;

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

	// checklist text column
	private static Label MakeColumn1(Composite comp, String text) {		
		Label question = new Label(comp, SWT.WRAP);
		GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd.horizontalIndent = 5;
		question.setLayoutData(gd);
		question.setBackground(C.APP_BGCOLOR);
		question.setFont(C.FONT_11);
		question.setText(text + "\n\n");
		return question;
	}	
	// hint icon & text
	private static Label makeColumn2(Composite comp, final String text) {
		Label hint = new Label(comp, SWT.NONE);
		hint.setToolTipText("Click for Hint Text");
		hint.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		hint.setBackground(C.APP_BGCOLOR);
		hint.setImage(C.getImage("/img/hint.png"));
		hint.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				EsmApplication.alert(text);
			}
		});
		return hint;
	}	
	private static Composite makeColumn3(Composite comp, int numCols) {
		// options control cell to hold form objects
		Composite optionsCell = new Composite(comp, SWT.NONE);
		GridLayout gl_optionsCell = new GridLayout(numCols, false);
		gl_optionsCell.marginHeight = 1;
		gl_optionsCell.verticalSpacing = 1;
		optionsCell.setLayout(gl_optionsCell);
		optionsCell.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		optionsCell.setBackground(C.APP_BGCOLOR);
		return optionsCell;
	}
	// comments field
	private static Text MakeColumn4(Composite comp) {
		Text comments = new Text(comp, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		comments.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		comments.setEditable(true);
		comments.setFont(C.FONT_10);
		comments.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd.heightHint = rowHeight;
		gd.widthHint = 200;
		comments.setLayoutData(gd);
		return comments;
	}
	private static Label Separator(Composite comp) {
		Label separator = new Label(comp, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.CENTER);
		separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));		
		return separator;
	}

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
		scrollPanel.setExpandHorizontal(true);

		// the grid panel that holds the various info rows
		final Composite comp = new Composite(scrollPanel, SWT.NONE);
		GridLayout gl_comp = new GridLayout(1, true);
		gl_comp.marginBottom = 50;
		comp.setLayout(gl_comp);
		comp.setBackground(C.APP_BGCOLOR);

		// header row
		Group headerRow = new Group(comp, SWT.BORDER);
		headerRow.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_headerRow = new GridLayout(4, false);
		gl_headerRow.horizontalSpacing = 0;
		gl_headerRow.marginWidth = 0;
		gl_headerRow.verticalSpacing = 0;
		gl_headerRow.marginHeight = 0;
		headerRow.setLayout(gl_headerRow);
		headerRow.setBackground(C.APP_BGCOLOR);

		// header labels
		Label lblName = new Label(headerRow, SWT.NONE);
		lblName.setFont(C.FONT_12B);
		lblName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblName.setBackground(C.APP_BGCOLOR);
		lblName.setText("Internal Space Checklist");		

		Label lblStatus = new Label(headerRow, SWT.NONE);
		lblStatus.setFont(C.FONT_12B);
		lblStatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblStatus.setBackground(C.APP_BGCOLOR);
		lblStatus.setText("Internal Space Completion");		
		
		Label lblStatusImg = new Label(headerRow,SWT.NONE);
		GridData gd_lblStatusImg = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_lblStatusImg.horizontalIndent = 10;
		lblStatusImg.setLayoutData(gd_lblStatusImg);
		lblStatusImg.setImage(C.getImage("/img/Percent_40.png"));

		//table layout
		final Group tbl = new Group(comp, SWT.BORDER);
		tbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gl_tbl = new GridLayout(4, false);
		gl_tbl.verticalSpacing = 1;
		gl_tbl.marginHeight = 0;
		gl_tbl.horizontalSpacing = 1;
		gl_tbl.marginWidth = 0;
		tbl.setLayout(gl_tbl);
		tbl.setBackground(C.APP_BGCOLOR);
		
		// column headers
		CLabel lblChecklist = new CLabel(tbl, SWT.NONE);
		lblChecklist.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblChecklist.setBackground(C.AUDIT_COLHEADER_BGCOLOR);
		lblChecklist.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblChecklist.setFont(C.FONT_12B);
		lblChecklist.setText("Checklist");
		CLabel lblHint = new CLabel(tbl, SWT.CENTER);
		GridData gd_lblHint = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblHint.widthHint = 50;
		lblHint.setLayoutData(gd_lblHint);
		lblHint.setBackground(C.AUDIT_COLHEADER_BGCOLOR);
		lblHint.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblHint.setFont(C.FONT_12B);
		lblHint.setText("Hint");
		CLabel lblOptions = new CLabel(tbl, SWT.NONE);
		GridData gd_lblOptions = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblOptions.widthHint = 150;
		lblOptions.setLayoutData(gd_lblOptions);
		lblOptions.setBackground(C.AUDIT_COLHEADER_BGCOLOR);
		lblOptions.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblOptions.setText("Options");
		lblOptions.setFont(C.FONT_12B);
		CLabel lblComments = new CLabel(tbl, SWT.NONE);
		lblComments.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblComments.setBackground(C.AUDIT_COLHEADER_BGCOLOR);
		lblComments.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblComments.setText("Comments");
		lblComments.setFont(C.FONT_12B);
		sep = new Label(tbl, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.CENTER);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));				



		// start loop through audit checklist questions
		qNum = 1;
		Label q1_col1 = MakeColumn1(tbl,"What are the internal dimensions of the space: H, W, L?");
		Label q1_col2 = makeColumn2(tbl, "Hint text here");
		Composite q1_col3 = makeColumn3(tbl,2);

		Label q1_lblH = new Label(q1_col3, SWT.NONE);
		q1_lblH.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q1_lblH.setBackground(C.APP_BGCOLOR);
		q1_lblH.setFont(C.FONT_9);		
		q1_lblH.setText("Height:");		
		Text q1_txtH = new Text(q1_col3, SWT.BORDER);
		GridData gd_q1_txtH = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_q1_txtH.heightHint = 10;
		q1_txtH.setLayoutData(gd_q1_txtH);
		q1_txtH.setFont(C.FONT_9);		
		Label q1_lblW = new Label(q1_col3, SWT.NONE);
		q1_lblW.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q1_lblW.setBackground(C.APP_BGCOLOR);
		q1_lblW.setFont(C.FONT_9);		
		q1_lblW.setText("Width:");		
		Text q1_txtW = new Text(q1_col3, SWT.BORDER);
		GridData gd_q1_txtW = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_q1_txtW.heightHint = 10;
		q1_txtW.setLayoutData(gd_q1_txtW);
		q1_txtW.setFont(C.FONT_9);		
		Label q1_lblL = new Label(q1_col3, SWT.NONE);
		q1_lblL.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q1_lblL.setBackground(C.APP_BGCOLOR);
		q1_lblL.setFont(C.FONT_9);		
		q1_lblL.setText("Length:");		
		Text q1_txtL = new Text(q1_col3, SWT.BORDER);
		GridData gd_q1_txtL = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_q1_txtL.heightHint = 10;
		q1_txtL.setLayoutData(gd_q1_txtL);
		q1_txtL.setFont(C.FONT_9);
		
		Text q1_col4 = MakeColumn4(tbl);
		sep = Separator(tbl);
		//-------------------------------------------------------------------------------------------------------
		qNum = 2;
		Label q2_col1 = MakeColumn1(tbl,"Is the enclosed space Compartmentalised?\n(If so, describe internal layout)");
		Label q2_col2 = makeColumn2(tbl, "Hint text here");
		Composite q2_col3 = makeColumn3(tbl,1);
		Button q2_check = new Button(q2_col3, SWT.CHECK);
		q2_check.setBackground(C.APP_BGCOLOR);
		q2_check.setText("Yes");
		Text q2_col4 = MakeColumn4(tbl);
		sep = Separator(tbl);
		//-------------------------------------------------------------------------------------------------------
		qNum = 3;
		Label q3_col1 = MakeColumn1(tbl,"Are internal obstacles present (baffles, pipes etc)?");
		Label q3_col2 = makeColumn2(tbl, "Hint text here");
		Composite q3_col3 = makeColumn3(tbl,1);
		Button q3_check = new Button(q3_col3, SWT.CHECK);
		q3_check.setBackground(C.APP_BGCOLOR);
		q3_check.setText("Yes");
		Text q3_col4 = MakeColumn4(tbl);
		sep = Separator(tbl);
		//-------------------------------------------------------------------------------------------------------
		qNum = 4;
		final Label q4_col1 = MakeColumn1(tbl,"Are there any restrictive crawl through holes - i.e. lightening holes etc?");
		Label q4_col2 = makeColumn2(tbl, "Hint text here");
		Composite q4_col3 = makeColumn3(tbl,2);
		final Button q4_check = new Button(q4_col3, SWT.CHECK);
		q4_check.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		q4_check.setBackground(C.APP_BGCOLOR);
		q4_check.setText("Yes");

		final Label q4_lblH = new Label(q4_col3, SWT.NONE);
		q4_lblH.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q4_lblH.setBackground(C.APP_BGCOLOR);
		q4_lblH.setFont(C.FONT_9);		
		q4_lblH.setText("Height:");	
		final Text q4_txtH = new Text(q4_col3, SWT.BORDER);
		GridData gd_q4_txtH = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_q4_txtH.heightHint = 10;
		q4_txtH.setLayoutData(gd_q4_txtH);
		q4_txtH.setFont(C.FONT_9);
		final Label q4_lblW = new Label(q4_col3, SWT.NONE);
		q4_lblW.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q4_lblW.setBackground(C.APP_BGCOLOR);
		q4_lblW.setFont(C.FONT_9);		
		q4_lblW.setText("Width:");		
		final Text q4_txtW = new Text(q4_col3, SWT.BORDER);
		GridData gd_q4_txtW = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_q4_txtW.heightHint = 10;
		q4_txtW.setLayoutData(gd_q4_txtW);
		q4_txtW.setFont(C.FONT_9);				
		q4_lblH.setVisible(false);
		q4_lblW.setVisible(false);
		q4_txtH.setVisible(false);
		q4_txtW.setVisible(false);
		q4_check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(q4_check.getSelection()) {
					q4_lblH.setVisible(true);
					q4_lblW.setVisible(true);
					q4_txtH.setVisible(true);
					q4_txtW.setVisible(true);				
					q4_col1.setText("Are there any restrictive crawl through holes - i.e. lightening holes etc?\nPlease state the dimensions of the holes (H,W)\n");
				} else {
					q4_lblH.setVisible(false);
					q4_lblW.setVisible(false);
					q4_txtH.setVisible(false);
					q4_txtW.setVisible(false);
					q4_col1.setText("Are there any restrictive crawl through holes - i.e. lightening holes etc?\n\n");
				}
			}
		});
		Text q4_col4 = MakeColumn4(tbl);
		sep = Separator(tbl);
		//-------------------------------------------------------------------------------------------------------
		qNum = 5;
		Label q5_col1 = MakeColumn1(tbl,"Are there any pipes running through the space that could contain hazardous liquids or gases?");
		Label q5_col2 = makeColumn2(tbl, "Hint text here");
		Composite q5_col3 = makeColumn3(tbl,1);
		Button q5_check = new Button(q5_col3, SWT.CHECK);
		q5_check.setBackground(C.APP_BGCOLOR);
		q5_check.setText("Yes");
		Text q5_col4 = MakeColumn4(tbl);
		sep = Separator(tbl);
		//-------------------------------------------------------------------------------------------------------
		qNum = 6;
		Label q6_col1 = MakeColumn1(tbl,"Are there electrical cables running through the space?");
		Label q6_col2 = makeColumn2(tbl, "Hint text here");
		Composite q6_col3 = makeColumn3(tbl,1);
		Button q6_check = new Button(q6_col3, SWT.CHECK);
		q6_check.setBackground(C.APP_BGCOLOR);
		q6_check.setText("Yes");
		Text q6_col4 = MakeColumn4(tbl);
		sep = Separator(tbl);
		//-------------------------------------------------------------------------------------------------------
		qNum = 7;
		Label q7_col1 = MakeColumn1(tbl,"Are there internal vertical ladders present?");
		Label q7_col2 = makeColumn2(tbl, "Hint text here");
		Composite q7_col3 = makeColumn3(tbl,1);
		final Button q7_check = new Button(q7_col3, SWT.CHECK);
		q7_check.setBackground(C.APP_BGCOLOR);
		q7_check.setText("Yes");
		Text q7_col4 = MakeColumn4(tbl);
		sep = Separator(tbl);
		//-------------------------------------------------------------------------------------------------------
		qNum = 8;
		final Label q8_col1 = MakeColumn1(tbl,"Are there internal rungs?");
		final Label q8_col2 = makeColumn2(tbl, "Hint text here");
		final Composite q8_col3 = makeColumn3(tbl,1);
		Button q8_check = new Button(q8_col3, SWT.CHECK);
		q8_check.setBackground(C.APP_BGCOLOR);
		q8_check.setText("Yes");
		final Text q8_col4 = MakeColumn4(tbl);
		final GridData gd_q8_col4 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		q8_col1.setVisible(false);
		q8_col2.setVisible(false);
		q8_col3.setVisible(false);
		q8_col4.setVisible(false);
		gd_q8_col4.heightHint = 1;
		gd_q8_col4.grabExcessVerticalSpace = true;
		q8_col4.setLayoutData(gd_q8_col4);
		tbl.layout();
		sep = Separator(tbl);
		q7_check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(q7_check.getSelection()){
					q8_col1.setVisible(true);
					q8_col2.setVisible(true);
					q8_col3.setVisible(true);
					q8_col4.setVisible(true);
					gd_q8_col4.heightHint = rowHeight;
					gd_q8_col4.grabExcessVerticalSpace = false;
					q8_col4.setLayoutData(gd_q8_col4);
					tbl.layout();
				} else {
					q8_col1.setVisible(false);
					q8_col2.setVisible(false);
					q8_col3.setVisible(false);
					q8_col4.setVisible(false);
					gd_q8_col4.heightHint = 1;
					gd_q8_col4.grabExcessVerticalSpace = true;
					q8_col4.setLayoutData(gd_q8_col4);
					tbl.layout();
				}
			}
		});
		//-------------------------------------------------------------------------------------------------------

		
		
		
		
		
		
		
		
		
		
		
		
		
		

/*		Label x = new Label(tbl, SWT.NONE);
		x.setText("FOO");
		x.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		new Label(tbl, SWT.NONE);
		Composite foo = new Composite(tbl, SWT.BORDER);
		GridLayout gl_foo = new GridLayout(15, false);
		gl_foo.marginHeight = 1;
		gl_foo.verticalSpacing = 1;
		foo.setLayout(gl_foo);
		GridData gd_foo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_foo.heightHint = 50;
		foo.setLayoutData(gd_foo);
		foo.setBackground(C.APP_BGCOLOR);
		
		Button btnCheckButton = new Button(foo, SWT.CHECK);
		btnCheckButton.setText("Check Button");
*/		

		//end loop 		

		scrollPanel.setContent(comp);
		scrollPanel.setExpandVertical(true);
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
