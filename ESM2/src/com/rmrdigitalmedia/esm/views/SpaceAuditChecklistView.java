package com.rmrdigitalmedia.esm.views;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.AuditController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.SpaceChecklistAuditTable;
import com.rmrdigitalmedia.esm.models.SpaceChecklistQuestionsTable;

@SuppressWarnings("unused")
public class SpaceAuditChecklistView {

	private static EsmUsersTable.Row user;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
	private static Label sep;
	private static int qNum;
	private static int rowHeight = 35;
	private static int colHeaderH = 40;
	private static int dimBoxW = 30;
	private static boolean empty;
	// form fields
	private static Text q1_txtH;
	private static Text q1_txtW;
	private static Text q1_txtL;
	private static Text q1_col4;
	private static Button q2_radio1;
	private static Button q2_radio2;
	private static Text q2_col4;
	private static Button q3_radio1;
	private static Button q3_radio2;
	private static Text q3_col4;
	static Button q4_radio1;
	private static Button q4_radio2;
	static Text q4_txtH;
	static Label q4_lblW;
	static Text q4_txtW;
	private static Text q4_col4;
	private static Button q5_radio1;
	private static Button q5_radio2;
	private static Text q5_col4;
	private static Button q6_radio1;
	private static Button q6_radio2;
	private static Text q6_col4;
	static Button q7_radio1;
	private static Button q7_radio2;
	private static Button q7_rate_radio1;
	private static Button q7_rate_radio2;
	private static Button q7_rate_radio3;
	private static Text q7_col4;
	static Button q8_radio1;
	private static Button q8_radio2;
	private static Button q8_rate_radio1;
	private static Button q8_rate_radio2;
	private static Button q8_rate_radio3;
	static Text q8_col4;
	static Button q9_radio1;
	private static Button q9_radio2;
	private static Button q9_rate_radio1;
	private static Button q9_rate_radio2;
	private static Button q9_rate_radio3;
	static Text q9_col4;
	static Button q10_radio1;
	private static Button q10_radio2;
	private static Button q10_rate_radio1;
	private static Button q10_rate_radio2;
	private static Button q10_rate_radio3;
	static Text q10_col4;
	private static Button q11_radio1;
	private static Button q11_radio2;
	private static Text q11_col4;
	private static Button q12_radio1;
	private static Button q12_radio2;
	private static Text q12_col4;
	private static Button q13_radio1;
	private static Button q13_radio2;
	private static Text q13_col4;
	private static Button q14_radio1;
	private static Button q14_radio2;
	private static Text q14_col4;
	private static Button q15_radio1;
	private static Button q15_radio2;
	private static Text q15_col4;
	private static Button q16_radio1;
	private static Button q16_radio2;
	private static Text q16_col4;
	private static Button q17_radio1;
	private static Button q17_radio2;
	private static Text q17_col4;

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
			SpaceAuditChecklistView.buildPage(comp, 1);
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
	private static Label MakeColumn1(Composite comp, String text, boolean hide) {		
		Label question = new Label(comp, SWT.WRAP);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd.horizontalIndent = 5;
		gd.exclude = hide;
		question.setLayoutData(gd);
		question.setBackground(C.APP_BGCOLOR);
		question.setFont(C.FONT_10);
		question.setText(text);
		return question;
	}	
	// hint icon & text
	private static Label makeColumn2(Composite comp, final String text, boolean hide) {
		Label hint = new Label(comp, SWT.NONE);
		hint.setToolTipText("Click for Hint Text");
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd.exclude = hide;
		hint.setLayoutData(gd);		
		hint.setBackground(C.APP_BGCOLOR);
		hint.setImage(C.getImage("hint.png"));
		hint.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				EsmApplication.hint(text);
			}
		});
		return hint;
	}	
	// options control cell to hold form objects
	private static Composite makeColumn3(Composite comp, int numCols, boolean hide) {
		Composite optionsCell = new Composite(comp, SWT.NONE);
		GridLayout gl_optionsCell = new GridLayout(numCols, false);
		gl_optionsCell.marginHeight = 1;
		gl_optionsCell.verticalSpacing = 1;
		optionsCell.setLayout(gl_optionsCell);
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd.exclude = hide;
		optionsCell.setLayoutData(gd);		
		optionsCell.setBackground(C.APP_BGCOLOR);
		return optionsCell;
	}
	// comments field
	private static Text MakeColumn4(Composite comp, boolean hide) {
		Text comments = new Text(comp, SWT.BORDER | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		comments.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		comments.setEditable(true);
		comments.setFont(C.FONT_10);
		comments.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd.heightHint = rowHeight;
		gd.widthHint = 200;
		gd.exclude = hide;
		comments.setLayoutData(gd);
		return comments;
	}
	private static Label Separator(Composite comp, boolean hide) {
		Label separator = new Label(comp, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.CENTER);
		final GridData gd = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gd.exclude = hide;
		separator.setLayoutData(gd);		
		return separator;
	}

	static void toggle(Label l, boolean hide) {
		GridData gd = (GridData) l.getLayoutData();
		gd.exclude = hide;
		l.setVisible(!hide);
	}
	static void toggle(Composite c, boolean hide) {
		GridData gd = (GridData) c.getLayoutData();
		gd.exclude = hide;
		c.setVisible(!hide);
	}
	static void toggle(Text t, boolean hide) {
		GridData gd = (GridData) t.getLayoutData();
		gd.exclude = hide;
		t.setVisible(!hide);
	}


	public static void buildPage(final Composite parent, final int spaceID) {
		user = WindowController.getUser();
		
		for (Control c:parent.getChildren()) {
			c.dispose();
		}
		parent.setLayout(new FillLayout(SWT.VERTICAL));

		// scrolling frame to hold the grid panel
		final ScrolledComposite scrollPanel = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.BORDER);
		scrollPanel.setExpandHorizontal(true);

		// the grid panel that holds the various info rows
		final Composite comp = new Composite(scrollPanel, SWT.NONE);
		GridLayout gl_comp = new GridLayout(1, true);
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
		lblStatus.setText("Checklist Progress");		

		Label lblStatusImg = new Label(headerRow,SWT.NONE);
		GridData gd_lblStatusImg = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_lblStatusImg.horizontalIndent = 10;
		lblStatusImg.setLayoutData(gd_lblStatusImg);
		// progress image
		final int progress = (Integer) EsmApplication.appData.getField("SPACE_CHK_"+spaceID);
		//final int complete = AuditController.calculateSpaceCompletionStatus(spaceID);
		lblStatusImg.setImage(C.getImage("Percent_"+progress+".png"));

		//table layout
		final Group tbl = new Group(comp, SWT.BORDER);
		tbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gl_tbl = new GridLayout(4, false);
		gl_tbl.marginTop = -15;
		gl_tbl.marginHeight = 0;
		gl_tbl.marginWidth = 0;
		gl_tbl.verticalSpacing = 1;
		gl_tbl.horizontalSpacing = 1;
		tbl.setLayout(gl_tbl);
		tbl.setBackground(C.APP_BGCOLOR);

		// column headers
		CLabel lblChecklist = new CLabel(tbl, SWT.NONE);
		GridData gd_lblChecklist = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_lblChecklist.heightHint = colHeaderH;
		lblChecklist.setLayoutData(gd_lblChecklist);
		lblChecklist.setBackground(C.AUDIT_COLHEADER_BGCOLOR);
		lblChecklist.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblChecklist.setFont(C.FONT_12B);
		lblChecklist.setText("Checklist");
		CLabel lblHint = new CLabel(tbl, SWT.CENTER);
		GridData gd_lblHint = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblHint.widthHint = 50;
		gd_lblHint.heightHint = colHeaderH;
		lblHint.setLayoutData(gd_lblHint);
		lblHint.setBackground(C.AUDIT_COLHEADER_BGCOLOR);
		lblHint.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblHint.setFont(C.FONT_12B);
		lblHint.setText("Hint");
		CLabel lblOptions = new CLabel(tbl, SWT.CENTER);
		GridData gd_lblOptions = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblOptions.widthHint = 150;
		gd_lblOptions.heightHint = colHeaderH;
		lblOptions.setLayoutData(gd_lblOptions);
		lblOptions.setBackground(C.AUDIT_COLHEADER_BGCOLOR);
		lblOptions.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblOptions.setText("Options");
		lblOptions.setFont(C.FONT_12B);
		CLabel lblComments = new CLabel(tbl, SWT.NONE);
		GridData gd_lblComments = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblComments.heightHint = colHeaderH;
		lblComments.setLayoutData(gd_lblComments);
		lblComments.setBackground(C.AUDIT_COLHEADER_BGCOLOR);
		lblComments.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblComments.setText("Comments");
		lblComments.setFont(C.FONT_12B);
		sep = new Label(tbl, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.CENTER);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));		

		// get from DB
		SpaceChecklistAuditTable.Row aRow = null;
		try {
			aRow = SpaceChecklistAuditTable.getRow("SPACE_ID", ""+spaceID);
		} catch (SQLException e1) {
			LogController.logEvent(SpaceAuditChecklistView.class, C.ERROR, e1);
		}
		empty = (aRow==null);
		SpaceChecklistQuestionsTable.Row[] qRows = null;
		Vector<String> qText = new Vector<String>();
		Vector<String> qHints = new Vector<String>();
		try {
			qText.add(0, null); qHints.add(0,null);
			qRows = SpaceChecklistQuestionsTable.getRows("1=1 ORDER BY SEQUENCE ASC");
			for(SpaceChecklistQuestionsTable.Row qRow:qRows) {
				qText.add(qRow.getSequence(), qRow.getQText());
				qHints.add(qRow.getSequence(), qRow.getQHint());
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		// start loop through audit checklist questions
		qNum = 1;
		Label q1_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q1_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q1_col3 = makeColumn3(tbl,6, false);
		Label q1_lblH = new Label(q1_col3, SWT.NONE);
		q1_lblH.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q1_lblH.setBackground(C.APP_BGCOLOR);
		q1_lblH.setFont(C.FONT_9);		
		q1_lblH.setText("Height:");		
		q1_txtH = new Text(q1_col3, SWT.BORDER);
		GridData gd_q1_txtH = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_q1_txtH.heightHint = 10;
		gd_q1_txtH.widthHint = dimBoxW;
		q1_txtH.setLayoutData(gd_q1_txtH);
		q1_txtH.setFont(C.FONT_8);
		if(!empty) { q1_txtH.setText(C.notNull(aRow.getQ1DimsH())); }
		Label q1_lblW = new Label(q1_col3, SWT.NONE);
		q1_lblW.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q1_lblW.setBackground(C.APP_BGCOLOR);
		q1_lblW.setFont(C.FONT_9);		
		q1_lblW.setText("Width:");		
		q1_txtW = new Text(q1_col3, SWT.BORDER);
		GridData gd_q1_txtW = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_q1_txtW.heightHint = 10;
		gd_q1_txtW.widthHint = dimBoxW;
		q1_txtW.setLayoutData(gd_q1_txtW);
		q1_txtW.setFont(C.FONT_8);		
		if(!empty) { q1_txtW.setText(C.notNull(aRow.getQ1DimsW())); }
		Label q1_lblL = new Label(q1_col3, SWT.NONE);
		q1_lblL.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q1_lblL.setBackground(C.APP_BGCOLOR);
		q1_lblL.setFont(C.FONT_9);		
		q1_lblL.setText("Length:");		
		q1_txtL = new Text(q1_col3, SWT.BORDER);
		GridData gd_q1_txtL = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_q1_txtL.heightHint = 10;
		gd_q1_txtL.widthHint = dimBoxW;
		q1_txtL.setLayoutData(gd_q1_txtL);
		q1_txtL.setFont(C.FONT_8);		
		if(!empty) { q1_txtL.setText(C.notNull(aRow.getQ1DimsL())); }
		q1_col4 = MakeColumn4(tbl,false);
		if(!empty) { q1_col4.setText( C.notNull(aRow.getQ1Comments()) ); }
		sep = Separator(tbl, false);
		if( !empty && ( C.isNullOrEmpty(aRow.getQ1DimsW()) || C.isNullOrEmpty(aRow.getQ1DimsH()) || C.isNullOrEmpty(aRow.getQ1DimsL()) ) ) {
			q1_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		//-------------------------------------------------------------------------------------------------------
		qNum = 2;
		Label q2_col1 = MakeColumn1(tbl,"Is the enclosed space Compartmentalised?\n(If so, describe internal layout in the Comments field)", false);
		Label q2_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q2_col3 = makeColumn3(tbl,2, false);
		q2_radio1 = new Button(q2_col3, SWT.RADIO);
		q2_radio1.setText("Yes");
		q2_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q2_radio1.setSelection(aRow.getQ2Boolean()!=null && aRow.getQ2Boolean().equals("Y")); 
		q2_radio2 = new Button(q2_col3, SWT.RADIO);
		q2_radio2.setText("No");
		q2_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q2_radio2.setSelection(aRow.getQ2Boolean()!=null && aRow.getQ2Boolean().equals("N")); 
		q2_col4 = MakeColumn4(tbl, false);
		if(!empty) { q2_col4.setText( C.notNull(aRow.getQ2Desc()) ); }
		sep = Separator(tbl, false);
		if(	(!empty && C.isNullOrEmpty(aRow.getQ2Boolean())) ||	(q2_radio1.getSelection() && q2_col4.getText().equals("")) ){
			q2_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		//-------------------------------------------------------------------------------------------------------
		qNum = 3;
		Label q3_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q3_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q3_col3 = makeColumn3(tbl,2, false);
		q3_radio1 = new Button(q3_col3, SWT.RADIO);
		q3_radio1.setText("Yes");
		q3_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q3_radio1.setSelection(aRow.getQ3Boolean()!=null && aRow.getQ3Boolean().equals("Y")); 
		q3_radio2 = new Button(q3_col3, SWT.RADIO);
		q3_radio2.setText("No");
		q3_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q3_radio2.setSelection(aRow.getQ3Boolean()!=null && aRow.getQ3Boolean().equals("N")); 
		q3_col4 = MakeColumn4(tbl, false);
		if(!empty) { q3_col4.setText( C.notNull(aRow.getQ3Comments()) ); }
		sep = Separator(tbl, false);
		if(!empty && C.isNullOrEmpty(aRow.getQ3Boolean())) {
			q3_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		//-------------------------------------------------------------------------------------------------------
		qNum = 4;
		final String q4_txt = "Are there any restrictive crawl through holes - i.e. lightening holes etc?\n";
		final Label q4_col1 = MakeColumn1(tbl,q4_txt, false);
		Label q4_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q4_col3 = makeColumn3(tbl,4, false);
		q4_radio1 = new Button(q4_col3, SWT.RADIO);
		q4_radio1.setText("Yes");
		q4_radio1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
		q4_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q4_radio1.setSelection(aRow.getQ4Boolean()!=null && aRow.getQ4Boolean().equals("Y")); 
		q4_radio2 = new Button(q4_col3, SWT.RADIO);
		q4_radio2.setText("No");
		q4_radio2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		q4_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q4_radio2.setSelection(aRow.getQ4Boolean()!=null && aRow.getQ4Boolean().equals("N")); 
		final Label q4_lblH = new Label(q4_col3, SWT.NONE);
		q4_lblH.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q4_lblH.setBackground(C.APP_BGCOLOR);
		q4_lblH.setFont(C.FONT_8);		
		q4_lblH.setText("Height:");	
		q4_lblH.setVisible(q4_radio1.getSelection());
		q4_txtH = new Text(q4_col3, SWT.BORDER);
		GridData gd_q4_txtH = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_q4_txtH.heightHint = 10;
		gd_q4_txtH.widthHint = dimBoxW;
		q4_txtH.setLayoutData(gd_q4_txtH);
		q4_txtH.setFont(C.FONT_8);
		if(!empty) { q4_txtH.setText(C.notNull(aRow.getQ4DimsH())); }
		q4_txtH.setVisible(q4_radio1.getSelection());
		q4_lblW = new Label(q4_col3, SWT.NONE);
		q4_lblW.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q4_lblW.setBackground(C.APP_BGCOLOR);
		q4_lblW.setFont(C.FONT_8);		
		q4_lblW.setText("Width:");		
		q4_lblW.setVisible(q4_radio1.getSelection());
		q4_txtW = new Text(q4_col3, SWT.BORDER);
		GridData gd_q4_txtW = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_q4_txtW.heightHint = 10;
		gd_q4_txtW.widthHint = dimBoxW;
		q4_txtW.setLayoutData(gd_q4_txtW);
		q4_txtW.setFont(C.FONT_8);				
		if(!empty) { q4_txtW.setText(C.notNull(aRow.getQ4DimsW())); }
		q4_txtW.setVisible(q4_radio1.getSelection());
		q4_radio1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(q4_radio1.getSelection()) {
					q4_lblH.setVisible(true); q4_lblW.setVisible(true); q4_txtH.setVisible(true); q4_txtW.setVisible(true);				
					q4_col1.setText(q4_txt+" * Please state the dimensions of the holes (H,W)");
				} else {
					q4_lblH.setVisible(false); q4_lblW.setVisible(false); q4_txtH.setVisible(false); q4_txtW.setVisible(false);
					q4_col1.setText(q4_txt);
				}
				tbl.layout();
			}
		});
		q4_col4 = MakeColumn4(tbl, false);
		if(!empty) { q4_col4.setText( C.notNull(aRow.getQ4Comments()) ); }
		sep = Separator(tbl, false);
		if(q4_radio1.getSelection()) {
			q4_col1.setText(q4_txt+" * Please state the dimensions of the holes (H,W)");
		}
		if( (!empty && C.isNullOrEmpty(aRow.getQ4Boolean())) ||	(q4_radio1.getSelection() && (C.isNullOrEmpty(aRow.getQ4DimsH()) || C.isNullOrEmpty(aRow.getQ4DimsW()))) ){
			q4_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		//-------------------------------------------------------------------------------------------------------
		qNum = 5;
		Label q5_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q5_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q5_col3 = makeColumn3(tbl,2, false);
		q5_radio1 = new Button(q5_col3, SWT.RADIO);
		q5_radio1.setText("Yes");
		q5_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q5_radio1.setSelection(aRow.getQ5Boolean()!=null && aRow.getQ5Boolean().equals("Y")); 
		q5_radio2 = new Button(q5_col3, SWT.RADIO);
		q5_radio2.setText("No");
		q5_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q5_radio2.setSelection(aRow.getQ5Boolean()!=null && aRow.getQ5Boolean().equals("N")); 
		q5_col4 = MakeColumn4(tbl, false);
		if(!empty) { q5_col4.setText( C.notNull(aRow.getQ5Comments()) ); }
		sep = Separator(tbl, false);
		if(!empty && C.isNullOrEmpty(aRow.getQ5Boolean())) {
			q5_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		//-------------------------------------------------------------------------------------------------------
		qNum = 6;
		Label q6_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q6_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q6_col3 = makeColumn3(tbl,2, false);
		q6_radio1 = new Button(q6_col3, SWT.RADIO);
		q6_radio1.setText("Yes");
		q6_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q6_radio1.setSelection(aRow.getQ6Boolean()!=null && aRow.getQ6Boolean().equals("Y")); 
		q6_radio2 = new Button(q6_col3, SWT.RADIO);
		q6_radio2.setText("No");
		q6_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q6_radio2.setSelection(aRow.getQ6Boolean()!=null && aRow.getQ6Boolean().equals("N")); 
		q6_col4 = MakeColumn4(tbl, false);
		if(!empty) { q6_col4.setText( C.notNull(aRow.getQ6Comments()) ); }
		sep = Separator(tbl, false);
		if(!empty && C.isNullOrEmpty(aRow.getQ6Boolean())) {
			q6_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		//-------------------------------------------------------------------------------------------------------
		qNum = 7;
		final String q7_txt = "Are there internal vertical ladders present?\n";
		final String q7_txt_2 = " * Rate the condition of these (1=poor, 3=good)";
		final Label q7_col1 = MakeColumn1(tbl,q7_txt, false);
		Label q7_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q7_col3 = makeColumn3(tbl,3, false);
		q7_radio1 = new Button(q7_col3, SWT.RADIO);
		q7_radio1.setText("Yes");
		q7_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q7_radio1.setSelection(aRow.getQ7Boolean()!=null && aRow.getQ7Boolean().equals("Y")); 
		q7_radio1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));		
		q7_radio2 = new Button(q7_col3, SWT.RADIO);
		q7_radio2.setText("No");
		q7_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q7_radio2.setSelection(aRow.getQ7Boolean()!=null && aRow.getQ7Boolean().equals("N")); 
		q7_radio2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		final Composite q7_ratings = new Composite(q7_col3, SWT.BORDER);
		q7_ratings.setBackground(C.APP_BGCOLOR);
		q7_ratings.setLayout(new FillLayout(SWT.HORIZONTAL));
		q7_ratings.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		q7_ratings.setVisible(q7_radio1.getSelection());
		q7_rate_radio1 = new Button(q7_ratings, SWT.RADIO);
		q7_rate_radio1.setText("1");
		q7_rate_radio1.setData(new Integer(1));
		q7_rate_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q7_rate_radio1.setSelection(aRow.getQ7Rating()==1); 
		q7_rate_radio2 = new Button(q7_ratings, SWT.RADIO);
		q7_rate_radio2.setText("2");
		q7_rate_radio2.setData(new Integer(2));
		q7_rate_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q7_rate_radio2.setSelection(aRow.getQ7Rating()==2);
		q7_rate_radio3 = new Button(q7_ratings, SWT.RADIO);
		q7_rate_radio3.setText("3");
		q7_rate_radio3.setData(new Integer(3));
		q7_rate_radio3.setBackground(C.APP_BGCOLOR);
		if(!empty) q7_rate_radio3.setSelection(aRow.getQ7Rating()==3);
		q7_radio1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(q7_radio1.getSelection()) {
					q7_col1.setText(q7_txt+q7_txt_2);
					q7_ratings.setVisible(true);
				} else {
					q7_col1.setText(q7_txt);
					q7_ratings.setVisible(false);
				}
				tbl.layout();
			}
		});
		q7_col4 = MakeColumn4(tbl, false);
		if(!empty) { q7_col4.setText( C.notNull(aRow.getQ7Comments()) ); }
		sep = Separator(tbl, false);
		if(q7_radio1.getSelection()) q7_col1.setText(q7_txt+q7_txt_2);
		if(!empty && ( C.isNullOrEmpty(aRow.getQ7Boolean()) || (q7_radio1.getSelection() && aRow.getQ7Rating()==0)) ) {
			q7_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		//-------------------------------------------------------------------------------------------------------

		qNum = 8;
		final String q8_txt = "\t> Do they have staging points/landings?\n";
		final String q8_txt_2 = "\t * Rate the condition of these (1=poor, 3=good)";
		final Label q8_col1 = MakeColumn1(tbl,q8_txt, !q7_radio1.getSelection());
		final Label q8_col2 = makeColumn2(tbl, qHints.elementAt(qNum), !q7_radio1.getSelection());
		final Composite q8_col3 = makeColumn3(tbl,3, !q7_radio1.getSelection());
		q8_radio1 = new Button(q8_col3, SWT.RADIO);
		q8_radio1.setText("Yes");
		q8_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q8_radio1.setSelection(aRow.getQ8Boolean()!=null && aRow.getQ8Boolean().equals("Y")); 
		q8_radio1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));		
		q8_radio2 = new Button(q8_col3, SWT.RADIO);
		q8_radio2.setText("No");
		q8_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q8_radio2.setSelection(aRow.getQ8Boolean()!=null && aRow.getQ8Boolean().equals("N")); 
		q8_radio2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));						
		final Composite q8_ratings = new Composite(q8_col3, SWT.BORDER);
		q8_ratings.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		q8_ratings.setBackground(C.APP_BGCOLOR);
		q8_ratings.setLayout(new FillLayout(SWT.HORIZONTAL));
		q8_ratings.setVisible(q8_radio1.getSelection());
		q8_rate_radio1 = new Button(q8_ratings, SWT.RADIO);
		q8_rate_radio1.setText("1"); 
		q8_rate_radio1.setData(new Integer(1));
		q8_rate_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q8_rate_radio1.setSelection(aRow.getQ8Rating()==1);
		q8_rate_radio2 = new Button(q8_ratings, SWT.RADIO);
		q8_rate_radio2.setText("2");
		q8_rate_radio2.setData(new Integer(2));
		q8_rate_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q8_rate_radio2.setSelection(aRow.getQ8Rating()==2);
		q8_rate_radio3 = new Button(q8_ratings, SWT.RADIO);
		q8_rate_radio3.setText("3");
		q8_rate_radio3.setData(new Integer(3));
		q8_rate_radio3.setBackground(C.APP_BGCOLOR);
		if(!empty) q8_rate_radio3.setSelection(aRow.getQ8Rating()==3);
		q8_radio1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(q8_radio1.getSelection()) {
					q8_ratings.setVisible(true); 
					q8_col1.setText(q8_txt+q8_txt_2);
				} else {
					q8_ratings.setVisible(false); 
					q8_col1.setText(q8_txt);
				}
				tbl.layout();
			}
		});
		q8_col4 = MakeColumn4(tbl, !q7_radio1.getSelection());
		if(!empty) { q8_col4.setText( C.notNull(aRow.getQ8Comments()) ); }
		final Label q8_sep = Separator(tbl, !q7_radio1.getSelection());
		if(q8_radio1.getSelection()) q8_col1.setText(q8_txt+q8_txt_2);
		if(!empty && q7_radio1.getSelection() && (C.isNullOrEmpty(aRow.getQ8Boolean()) || (q8_radio1.getSelection() && aRow.getQ8Rating()==0)) ) {
			q8_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}		
		//-------------------------------------------------------------------------------------------------------
		qNum = 9;
		final String q9_txt = "\t> Do they have safety hoops?\n";
		final Label q9_col1 = MakeColumn1(tbl,q9_txt, !q7_radio1.getSelection());
		final Label q9_col2 = makeColumn2(tbl, qHints.elementAt(qNum), !q7_radio1.getSelection());
		final Composite q9_col3 = makeColumn3(tbl, 3, !q7_radio1.getSelection());
		q9_radio1 = new Button(q9_col3, SWT.RADIO);
		q9_radio1.setText("Yes");
		q9_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q9_radio1.setSelection(aRow.getQ9Boolean()!=null && aRow.getQ9Boolean().equals("Y")); 
		q9_radio1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));		
		q9_radio2 = new Button(q9_col3, SWT.RADIO);
		q9_radio2.setText("No");
		q9_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q9_radio2.setSelection(aRow.getQ9Boolean()!=null && aRow.getQ9Boolean().equals("N")); 
		q9_radio2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		final Composite q9_ratings = new Composite(q9_col3, SWT.BORDER);
		q9_ratings.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		q9_ratings.setBackground(C.APP_BGCOLOR);
		q9_ratings.setLayout(new FillLayout(SWT.HORIZONTAL));
		q9_ratings.setVisible(q9_radio1.getSelection());
		q9_rate_radio1 = new Button(q9_ratings, SWT.RADIO);
		q9_rate_radio1.setText("1");
		q9_rate_radio1.setData(new Integer(1));
		q9_rate_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q9_rate_radio1.setSelection(aRow.getQ9Rating()==1);
		q9_rate_radio2 = new Button(q9_ratings, SWT.RADIO);
		q9_rate_radio2.setText("2");
		q9_rate_radio2.setData(new Integer(2));
		q9_rate_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q9_rate_radio2.setSelection(aRow.getQ9Rating()==2);
		q9_rate_radio3 = new Button(q9_ratings, SWT.RADIO);
		q9_rate_radio3.setText("3");
		q9_rate_radio3.setData(new Integer(3));
		q9_rate_radio3.setBackground(C.APP_BGCOLOR);
		if(!empty) q9_rate_radio3.setSelection(aRow.getQ9Rating()==3);
		q9_radio1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(q9_radio1.getSelection()) {
					q9_ratings.setVisible(true);
					q9_col1.setText(q9_txt + q8_txt_2);
				} else {
					q9_ratings.setVisible(false);
					q9_col1.setText(q9_txt);
				}
				tbl.layout();
			}
		});
		q9_col4 = MakeColumn4(tbl, !q7_radio1.getSelection());
		if(!empty) { q9_col4.setText( C.notNull(aRow.getQ9Comments()) ); }
		final Label q9_sep = Separator(tbl, !q7_radio1.getSelection());
		if(q9_radio1.getSelection()) q9_col1.setText(q9_txt+q8_txt_2);
		if(!empty && q7_radio1.getSelection() && (C.isNullOrEmpty(aRow.getQ9Boolean()) || (q9_radio1.getSelection() && aRow.getQ9Rating()==0)) ) {
			q9_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}		
		//-------------------------------------------------------------------------------------------------------
		qNum = 10;
		final String q10_txt = "\t> Do they have handrails at landing points?\n";
		final Label q10_col1 = MakeColumn1(tbl,q10_txt, !q7_radio1.getSelection());
		final Label q10_col2 = makeColumn2(tbl, qHints.elementAt(qNum), !q7_radio1.getSelection());
		final Composite q10_col3 = makeColumn3(tbl,3, !q7_radio1.getSelection());
		q10_radio1 = new Button(q10_col3, SWT.RADIO);
		q10_radio1.setText("Yes");
		q10_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q10_radio1.setSelection(aRow.getQ10Boolean()!=null && aRow.getQ10Boolean().equals("Y")); 
		q10_radio1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));		
		q10_radio2 = new Button(q10_col3, SWT.RADIO);
		q10_radio2.setText("No");
		q10_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q10_radio2.setSelection(aRow.getQ10Boolean()!=null && aRow.getQ10Boolean().equals("N")); 
		q10_radio2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));				
		final Composite q10_ratings = new Composite(q10_col3, SWT.BORDER);
		q10_ratings.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		q10_ratings.setBackground(C.APP_BGCOLOR);
		q10_ratings.setLayout(new FillLayout(SWT.HORIZONTAL));
		q10_ratings.setVisible(q10_radio1.getSelection());
		q10_rate_radio1 = new Button(q10_ratings, SWT.RADIO);
		q10_rate_radio1.setText("1");
		q10_rate_radio1.setData(new Integer(1));
		q10_rate_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q10_rate_radio1.setSelection(aRow.getQ10Rating()==1);
		q10_rate_radio2 = new Button(q10_ratings, SWT.RADIO);
		q10_rate_radio2.setText("2");
		q10_rate_radio2.setData(new Integer(2));
		q10_rate_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q10_rate_radio2.setSelection(aRow.getQ10Rating()==2);
		q10_rate_radio3 = new Button(q10_ratings, SWT.RADIO);
		q10_rate_radio3.setText("3");
		q10_rate_radio3.setData(new Integer(3));
		q10_rate_radio3.setBackground(C.APP_BGCOLOR);
		if(!empty) q10_rate_radio3.setSelection(aRow.getQ10Rating()==3);
		q10_radio1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(q10_radio1.getSelection()) {
					q10_ratings.setVisible(true);
					q10_col1.setText(q10_txt+q8_txt_2);
					tbl.layout();
				} else {
					q10_ratings.setVisible(false);
					q10_col1.setText(q10_txt);
					tbl.layout();
				}
			}
		});
		q10_col4 = MakeColumn4(tbl, !q7_radio1.getSelection());
		if(!empty) { q10_col4.setText( C.notNull(aRow.getQ10Comments()) ); }
		final Label q10_sep = Separator(tbl, !q7_radio1.getSelection());
		if(q10_radio1.getSelection()) q10_col1.setText(q10_txt+q8_txt_2);
		if(!empty && q7_radio1.getSelection() && (C.isNullOrEmpty(aRow.getQ10Boolean()) || (q10_radio1.getSelection() && aRow.getQ10Rating()==0)) ) {
			q10_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}		
		//-------------------------------------------------------------------------------------------------------		
		qNum = 11;
		Label q11_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q11_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q11_col3 = makeColumn3(tbl,2, false);
		q11_radio1 = new Button(q11_col3, SWT.RADIO);
		q11_radio1.setText("Yes");
		q11_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q11_radio1.setSelection(aRow.getQ11Boolean()!=null && aRow.getQ11Boolean().equals("Y")); 
		q11_radio2 = new Button(q11_col3, SWT.RADIO);
		q11_radio2.setText("No");
		q11_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q11_radio2.setSelection(aRow.getQ11Boolean()!=null && aRow.getQ11Boolean().equals("N")); 
		q11_col4 = MakeColumn4(tbl, false);
		if(!empty) { q11_col4.setText( C.notNull(aRow.getQ11Comments()) ); }
		sep = Separator(tbl, false);
		if(!empty && C.isNullOrEmpty(aRow.getQ11Boolean())) {
			q11_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		//-------------------------------------------------------------------------------------------------------
		qNum = 12;
		Label q12_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q12_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q12_col3 = makeColumn3(tbl,2, false);
		q12_radio1 = new Button(q12_col3, SWT.RADIO);
		q12_radio1.setText("Yes");
		q12_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q12_radio1.setSelection(aRow.getQ12Boolean()!=null && aRow.getQ12Boolean().equals("Y")); 
		q12_radio2 = new Button(q12_col3, SWT.RADIO);
		q12_radio2.setText("No");
		q12_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q12_radio2.setSelection(aRow.getQ12Boolean()!=null && aRow.getQ12Boolean().equals("N")); 
		q12_col4 = MakeColumn4(tbl, false);
		if(!empty) { q12_col4.setText( C.notNull(aRow.getQ12Comments()) ); }
		sep = Separator(tbl, false);
		if(!empty && C.isNullOrEmpty(aRow.getQ12Boolean())) {
			q12_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		//-------------------------------------------------------------------------------------------------------
		qNum = 13;
		Label q13_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q13_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q13_col3 = makeColumn3(tbl,2, false);
		q13_radio1 = new Button(q13_col3, SWT.RADIO);
		q13_radio1.setText("Yes");
		q13_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q13_radio1.setSelection(aRow.getQ13Boolean()!=null && aRow.getQ13Boolean().equals("Y")); 
		q13_radio2 = new Button(q13_col3, SWT.RADIO);
		q13_radio2.setText("No");
		q13_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q13_radio2.setSelection(aRow.getQ13Boolean()!=null && aRow.getQ13Boolean().equals("N")); 
		q13_col4 = MakeColumn4(tbl, false);
		if(!empty) { q13_col4.setText( C.notNull(aRow.getQ13Comments()) ); }
		sep = Separator(tbl, false);
		if(!empty && C.isNullOrEmpty(aRow.getQ13Boolean())) {
			q13_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		//-------------------------------------------------------------------------------------------------------
		qNum = 14;
		Label q14_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q14_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q14_col3 = makeColumn3(tbl,2, false);
		q14_radio1 = new Button(q14_col3, SWT.RADIO);
		q14_radio1.setText("Yes");
		q14_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q14_radio1.setSelection(aRow.getQ14Boolean()!=null && aRow.getQ14Boolean().equals("Y")); 
		q14_radio2 = new Button(q14_col3, SWT.RADIO);
		q14_radio2.setText("No");
		q14_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q14_radio2.setSelection(aRow.getQ14Boolean()!=null && aRow.getQ14Boolean().equals("N")); 
		q14_col4 = MakeColumn4(tbl, false);
		if(!empty) { q14_col4.setText( C.notNull(aRow.getQ14Comments()) ); }
		sep = Separator(tbl, false);
		if(!empty && C.isNullOrEmpty(aRow.getQ14Boolean())) {
			q14_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		//-------------------------------------------------------------------------------------------------------
		qNum = 15;
		Label q15_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q15_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q15_col3 = makeColumn3(tbl,2, false);
		q15_radio1 = new Button(q15_col3, SWT.RADIO);
		q15_radio1.setText("Yes");
		q15_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q15_radio1.setSelection(aRow.getQ15Boolean()!=null && aRow.getQ15Boolean().equals("Y")); 
		q15_radio2 = new Button(q15_col3, SWT.RADIO);
		q15_radio2.setText("No");
		q15_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q15_radio2.setSelection(aRow.getQ15Boolean()!=null && aRow.getQ15Boolean().equals("N")); 
		q15_col4 = MakeColumn4(tbl, false);
		if(!empty) { q15_col4.setText( C.notNull(aRow.getQ15Comments()) ); }
		sep = Separator(tbl, false);
		if(!empty && C.isNullOrEmpty(aRow.getQ15Boolean())) {
			q15_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		//-------------------------------------------------------------------------------------------------------		
		qNum = 16;
		Label q16_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q16_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q16_col3 = makeColumn3(tbl,2, false);
		q16_radio1 = new Button(q16_col3, SWT.RADIO);
		q16_radio1.setText("Yes");
		q16_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q16_radio1.setSelection(aRow.getQ16Boolean()!=null && aRow.getQ16Boolean().equals("Y")); 
		q16_radio2 = new Button(q16_col3, SWT.RADIO);
		q16_radio2.setText("No");
		q16_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q16_radio2.setSelection(aRow.getQ16Boolean()!=null && aRow.getQ16Boolean().equals("N")); 
		q16_col4 = MakeColumn4(tbl, false);
		if(!empty) { q16_col4.setText( C.notNull(aRow.getQ16Comments()) ); }
		sep = Separator(tbl, false);
		if(!empty && C.isNullOrEmpty(aRow.getQ16Boolean())) {
			q16_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		//-------------------------------------------------------------------------------------------------------		
		qNum = 17;
		Label q17_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q17_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q17_col3 = makeColumn3(tbl,2, false);
		q17_radio1 = new Button(q17_col3, SWT.RADIO);
		q17_radio1.setText("Yes");
		q17_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q17_radio1.setSelection(aRow.getQ17Boolean()!=null && aRow.getQ17Boolean().equals("Y")); 
		q17_radio2 = new Button(q17_col3, SWT.RADIO);
		q17_radio2.setText("No");
		q17_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q17_radio2.setSelection(aRow.getQ17Boolean()!=null && aRow.getQ17Boolean().equals("N")); 
		q17_col4 = MakeColumn4(tbl, false);
		if(!empty) { q17_col4.setText( C.notNull(aRow.getQ17Comments()) ); }
		sep = Separator(tbl, false);
		if(!empty && C.isNullOrEmpty(aRow.getQ17Boolean())) {
			q17_col1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		//-------------------------------------------------------------------------------------------------------		
		// end loop

		// Q7 toggle for Q8 Q9 Q10 rows
		q7_radio1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				boolean selected = !q7_radio1.getSelection();
				toggle(q8_col1,selected); toggle(q8_col2,selected); toggle(q8_col3,selected); toggle(q8_col4,selected); toggle(q8_sep,selected);					
				toggle(q9_col1,selected); toggle(q9_col2,selected); toggle(q9_col3,selected); toggle(q9_col4,selected); toggle(q9_sep,selected);					
				toggle(q10_col1,selected); toggle(q10_col2,selected); toggle(q10_col3,selected); toggle(q10_col4,selected); toggle(q10_sep,selected);					
				Rectangle r = comp.getParent().getClientArea();
				((ScrolledComposite) comp.getParent()).setMinSize(comp.computeSize(r.width, SWT.DEFAULT));
				tbl.layout();			
			}
		});		

		aRow = null;

		// footer row
		Group footerRow = new Group(comp, SWT.NONE);
		footerRow.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		GridLayout gl_footerRow = new GridLayout(3, false);
		gl_footerRow.verticalSpacing = 0;
		gl_footerRow.horizontalSpacing = 10;
		gl_footerRow.marginWidth = 0;
		footerRow.setLayout(gl_footerRow);
		footerRow.setBackground(C.APP_BGCOLOR);

		final Button btnReturn = new Button(footerRow, SWT.NONE);
		btnReturn.setToolTipText("Save and return to Space Details");
		btnReturn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		btnReturn.setBackground(C.APP_BGCOLOR);
		btnReturn.setFont(C.FONT_11B);
		btnReturn.setImage(C.getImage("back.png"));		
		btnReturn.setText("Back to Details");
		btnReturn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_WAIT));
				// save to DB
				saveAudit(spaceID);
				WindowController.showSpaceDetail(spaceID);
			}
		});

		final Button btnSave = new Button(footerRow, SWT.NONE);
		btnSave.setToolTipText("Save Checklist");
		btnSave.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		btnSave.setBackground(C.APP_BGCOLOR);
		btnSave.setFont(C.FONT_11B);
		btnSave.setText("Save Checklist");
		btnSave.setImage(C.getImage("16_save.png"));
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_WAIT));
				// save to DB
				saveAudit(spaceID);
				// reload screen
				WindowController.showSpaceAuditChecklist(spaceID);
			}
		});

		final Button btnProceed = new Button(footerRow, SWT.NONE | SWT.RIGHT_TO_LEFT);
		btnProceed.setImage(C.getImage("next.png"));
		btnProceed.setToolTipText("Save Checklist and proceed to Classification");
		btnProceed.setBackground(C.APP_BGCOLOR);
		btnProceed.setFont(C.FONT_11B);
		btnProceed.setText("Space Classification");
		btnProceed.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_WAIT));
				// save to DB
				saveAudit(spaceID);
				int _progress = (Integer) EsmApplication.appData.getField("SPACE_CHK_"+spaceID);
				// next screen
				if(_progress<100) {
					EsmApplication.alert("Checklist not completed!");
					parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_ARROW));
					WindowController.showSpaceAuditChecklist(spaceID);
				} else {
					WindowController.showSpaceAuditClassification(spaceID);
				}
			}
		});
		btnProceed.setEnabled(progress >= 100);

		scrollPanel.setContent(comp);
		scrollPanel.setExpandVertical(true);
		
		// redraw panel on window resize
		scrollPanel.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle r = scrollPanel.getClientArea();
				scrollPanel.setMinSize(comp.computeSize(r.width, SWT.DEFAULT));
			}
		});

		// final layout settings	
		parent.layout();
		parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_ARROW));
	}

	static void saveAudit(int spaceID) {
		// insert new row if empty
		if(empty) {
			try {
				SpaceChecklistAuditTable.Row r = SpaceChecklistAuditTable.getRow();
				r.setSpaceID(spaceID);
				r.setRemoteIdentifier((String)EsmApplication.appData.getField("LICENSE"));
				r.insert();
			} catch (SQLException e1) {
				LogController.logEvent(SpaceAuditChecklistView.class, C.FATAL, "ERROR INSERT SPACE CHECKLIST ROW", e1);
			}			
		}
		SpaceChecklistAuditTable.Row aRow = null;
		try {
			aRow = SpaceChecklistAuditTable.getRow("SPACE_ID", ""+spaceID);
		} catch (Exception ex) {
			LogController.logEvent(SpaceAuditChecklistView.class, C.FATAL, "ERROR SELECT SPACE CHECKLIST ROW", ex);
		}
		if(aRow != null) {
			try {
				HashMap<String,Object> currentVals = AuditController.getSpaceChecklistArray(spaceID);
				//1
				aRow.setQ1DimsH(q1_txtH.getText());
				aRow.setQ1DimsH(q1_txtH.getText());
				aRow.setQ1DimsW(q1_txtW.getText());
				aRow.setQ1DimsL(q1_txtL.getText());
				if(q1_col4.getText()!=null) aRow.setQ1Comments(q1_col4.getText());
				//2
				aRow.setQ2Boolean( C.getRB(q2_radio1,q2_radio2) );
				if(q2_col4.getText()!=null) aRow.setQ2Desc(q2_col4.getText());
				//3
				aRow.setQ3Boolean( C.getRB(q3_radio1,q3_radio2) );
				if(q3_col4.getText()!=null) aRow.setQ3Comments(q3_col4.getText());
				//4
				aRow.setQ4Boolean( C.getRB(q4_radio1,q4_radio2) );
				aRow.setQ4DimsH( (q4_radio1.getSelection()) ? q4_txtH.getText():"" );
				aRow.setQ4DimsW( (q4_radio1.getSelection()) ? q4_txtW.getText():"" );
				if(q4_col4.getText()!=null) aRow.setQ4Comments(q4_col4.getText());
				//5
				aRow.setQ5Boolean( C.getRB(q5_radio1,q5_radio2) );
				if(q5_col4.getText()!=null) aRow.setQ5Comments(q5_col4.getText());
				//6
				aRow.setQ6Boolean( C.getRB(q6_radio1,q6_radio2) );
				if(q6_col4.getText()!=null) aRow.setQ6Comments(q6_col4.getText());
				//7
				aRow.setQ7Boolean( C.getRB(q7_radio1,q7_radio2) );
				if(q7_radio1.getSelection()) {
					if(q7_rate_radio1.getSelection()) { aRow.setQ7Rating(1); }
					else if(q7_rate_radio2.getSelection()) { aRow.setQ7Rating(2); }
					else if(q7_rate_radio3.getSelection()) { aRow.setQ7Rating(3); }						
				} else {
					aRow.setQ7Rating(0);
				}					
				if(q7_col4.getText()!=null) aRow.setQ7Comments(q7_col4.getText());
				//8
				if(q7_radio1.getSelection()) { 
					aRow.setQ8Boolean( C.getRB(q8_radio1,q8_radio2) ); 
					if(q8_radio1.getSelection()) {
						if(q8_rate_radio1.getSelection()) { aRow.setQ8Rating(1); }
						else if(q8_rate_radio2.getSelection()) { aRow.setQ8Rating(2); }
						else if(q8_rate_radio3.getSelection()) { aRow.setQ8Rating(3); }						
					} else {
						aRow.setQ8Rating(0);
					}	
				} else { 
					aRow.setQ8Boolean(""); aRow.setQ8Rating(0);
				}
				if(q8_col4.getText()!=null) aRow.setQ8Comments(q8_col4.getText());
				//9
				if(q7_radio1.getSelection()) { 
					aRow.setQ9Boolean( C.getRB(q9_radio1,q9_radio2) ); 
					if(q9_radio1.getSelection()) {
						if(q9_rate_radio1.getSelection()) { aRow.setQ9Rating(1); }
						else if(q9_rate_radio2.getSelection()) { aRow.setQ9Rating(2); }
						else if(q9_rate_radio3.getSelection()) { aRow.setQ9Rating(3); }						
					} else {
						aRow.setQ9Rating(0);
					}	
				} else { 
					aRow.setQ9Boolean(""); aRow.setQ9Rating(0);
				}
				if(q9_col4.getText()!=null) aRow.setQ9Comments(q9_col4.getText());
				//10
				if(q7_radio1.getSelection()) { 
					aRow.setQ10Boolean( C.getRB(q10_radio1,q10_radio2) ); 
					if(q10_radio1.getSelection()) {
						if(q10_rate_radio1.getSelection()) { aRow.setQ10Rating(1); }
						else if(q10_rate_radio2.getSelection()) { aRow.setQ10Rating(2); }
						else if(q10_rate_radio3.getSelection()) { aRow.setQ10Rating(3); }						
					} else {
						aRow.setQ10Rating(0); 
					}	
				} else { 
					aRow.setQ10Boolean(""); aRow.setQ10Rating(0);
				}
				if(q10_col4.getText()!=null) aRow.setQ10Comments(q10_col4.getText());
				//11
				aRow.setQ11Boolean( C.getRB(q11_radio1,q11_radio2) );
				if(q11_col4.getText()!=null) aRow.setQ11Comments(q11_col4.getText());
				//12
				aRow.setQ12Boolean( C.getRB(q12_radio1,q12_radio2) );
				if(q12_col4.getText()!=null) aRow.setQ12Comments(q12_col4.getText());
				//13
				aRow.setQ13Boolean( C.getRB(q13_radio1,q13_radio2) );
				if(q13_col4.getText()!=null) aRow.setQ13Comments(q13_col4.getText());
				//14
				aRow.setQ14Boolean( C.getRB(q14_radio1,q14_radio2) );
				if(q14_col4.getText()!=null) aRow.setQ14Comments(q14_col4.getText());
				//15
				aRow.setQ15Boolean( C.getRB(q15_radio1,q15_radio2) );
				if(q15_col4.getText()!=null) aRow.setQ15Comments(q15_col4.getText());
				//16
				aRow.setQ16Boolean( C.getRB(q16_radio1,q16_radio2) );
				if(q16_col4.getText()!=null) aRow.setQ16Comments(q16_col4.getText());
				//17
				aRow.setQ17Boolean( C.getRB(q17_radio1,q17_radio2) );
				if(q17_col4.getText()!=null) aRow.setQ17Comments(q17_col4.getText());		
				// commit the transaction
				aRow.setUpdateDate(new Timestamp(new Date().getTime()));
				aRow.update();
				HashMap<String,Object> newVals = AuditController.getSpaceChecklistArray(spaceID);
				if(AuditController.isSpaceSignedOff(spaceID) && !newVals.equals(currentVals)) {
					LogController.log("Old: " + currentVals.toString());
					LogController.log("New: " + newVals.toString());
					EsmApplication.alert(C.SIGNOFF_REVOKE_MESSAGE);
					AuditController.revokeSignOff(spaceID);
				}
			} catch (SQLException e) {
				LogController.logEvent(SpaceAuditChecklistView.class, C.FATAL, "ERROR UPDATE SPACE CHECKLIST ROW", e);
			}
			try {
				AuditController.calculateSpaceChecklistCompletion(spaceID);
			} catch (SQLException e) {
				LogController.logEvent(SpaceAuditChecklistView.class, C.FATAL, "ERROR CALC SPACE CHECKLIST COMPLETION", e);			}
		}
	}

}
