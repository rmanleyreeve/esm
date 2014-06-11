package com.rmrdigitalmedia.esm.views;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.models.EsmUsersTable.Row;
import com.rmrdigitalmedia.esm.models.EntrypointChecklistAuditTable;
import com.rmrdigitalmedia.esm.models.EntrypointChecklistQuestionsTable;
import com.rmrdigitalmedia.esm.models.SpaceChecklistAuditTable;
import com.rmrdigitalmedia.esm.models.SpaceChecklistQuestionsTable;
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
public class EntryAuditChecklistView {

	private static Row user = WindowController.user;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
	private static Label sep;
	private static int qNum;
	private static int rowHeight = 35;
	private static int colHeaderH = 40;
	private static int dimBoxW = 30;

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
			EntryAuditChecklistView.buildPage(comp, 1);
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
		GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd.horizontalIndent = 5;
		gd.exclude = hide;
		question.setLayoutData(gd);
		question.setBackground(C.APP_BGCOLOR);
		question.setFont(C.FONT_11);
		question.setText(text);
		return question;
	}	
	// hint icon & text
	private static Label makeColumn2(Composite comp, final String text, boolean hide) {
		Label hint = new Label(comp, SWT.NONE);
		hint.setToolTipText("Click for Hint Text");
		GridData gd = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		gd.exclude = hide;
		hint.setLayoutData(gd);		
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
	private static Composite makeColumn3(Composite comp, int numCols, boolean hide) {
		// options control cell to hold form objects
		Composite optionsCell = new Composite(comp, SWT.NONE);
		GridLayout gl_optionsCell = new GridLayout(numCols, false);
		gl_optionsCell.marginHeight = 1;
		gl_optionsCell.verticalSpacing = 1;
		optionsCell.setLayout(gl_optionsCell);
		GridData gd = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		gd.exclude = hide;
		optionsCell.setLayoutData(gd);		
		optionsCell.setBackground(C.APP_BGCOLOR);
		return optionsCell;
	}
	// comments field
	private static Text MakeColumn4(Composite comp, boolean hide) {
		Text comments = new Text(comp, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		comments.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		comments.setEditable(true);
		comments.setFont(C.FONT_10);
		comments.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
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

	public static void buildPage(final Composite parent, final int entryID) {

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
		lblName.setText("Entry Point Space Checklist");		

		Label lblStatus = new Label(headerRow, SWT.NONE);
		lblStatus.setFont(C.FONT_12B);
		lblStatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblStatus.setBackground(C.APP_BGCOLOR);
		lblStatus.setText("Entry Point Space Completion");		

		Label lblStatusImg = new Label(headerRow,SWT.NONE);
		GridData gd_lblStatusImg = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_lblStatusImg.horizontalIndent = 10;
		lblStatusImg.setLayoutData(gd_lblStatusImg);
		lblStatusImg.setImage(C.getImage("/img/Percent_40.png"));

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
		GridData gd_lblChecklist = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
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
		CLabel lblOptions = new CLabel(tbl, SWT.NONE);
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
		EntrypointChecklistAuditTable.Row aRow = null;
		try {
			aRow = EntrypointChecklistAuditTable.getRow("ENTRYPOINT_ID", ""+entryID);
		} catch (SQLException e1) {
			LogController.logEvent(EntryAuditChecklistView.class, C.ERROR, e1);
		}
		boolean empty = (aRow==null);
		EntrypointChecklistQuestionsTable.Row[] qRows = null;
		Vector<String> qText = new Vector<String>();
		Vector<String> qHints = new Vector<String>();
		try {
			qText.add(0, null); qHints.add(0,null);
			qRows = EntrypointChecklistQuestionsTable.getRows("1=1 ORDER BY SEQUENCE ASC");
			for(EntrypointChecklistQuestionsTable.Row qRow:qRows) {
				qText.add(qRow.getSequence(), qRow.getQText());
				qHints.add(qRow.getSequence(), qRow.getQHint());
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		System.out.println(empty);
		// start loop through audit checklist questions
		qNum = 1;
		Label q1_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q1_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q1_col3 = makeColumn3(tbl,2, false);
		final Button q1_radio1 = new Button(q1_col3, SWT.RADIO);
		q1_radio1.setText("Inside");
		q1_radio1.setData(new String("INSIDE"));
		q1_radio1.setBackground(C.APP_BGCOLOR);
		//if(!empty) q1_radio1.setSelection(aRow.getQ1Value().equals("INSIDE")); 
		final Button q1_radio2 = new Button(q1_col3, SWT.RADIO);
		q1_radio2.setText("Outside");
		q1_radio2.setData(new String("OUTSIDE"));
		q1_radio2.setBackground(C.APP_BGCOLOR);
		//if(!empty) q1_radio2.setSelection(aRow.getQ1Value().equals("OUTSIDE")); 
		Text q1_col4 = MakeColumn4(tbl,false);
		if(!empty) { q1_col4.setText( C.notNull(aRow.getQ1Comments()) ); }
		sep = Separator(tbl, false);
		q1_radio1.setSelection(false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 2;
		final Label q2_col1 = MakeColumn1(tbl,"\t> "+qText.elementAt(qNum), !q1_radio2.getSelection());
		final Label q2_col2 = makeColumn2(tbl, qHints.elementAt(qNum), !q1_radio2.getSelection());
		final Composite q2_col3 = makeColumn3(tbl,1, !q1_radio2.getSelection());
		final Button q2_check = new Button(q2_col3, SWT.CHECK);
		q2_check.setBackground(C.APP_BGCOLOR);
		q2_check.setText("Yes");
		if(!empty) { q2_check.setSelection(aRow.getQ3Checkbox().equals("TRUE")); }
		final Text q2_col4 = MakeColumn4(tbl, !q1_radio2.getSelection());
		if(!empty) { q2_col4.setText( C.notNull(aRow.getQ2Comments()) ); }
		final Label q2_sep = Separator(tbl, !q1_radio2.getSelection());
		//-------------------------------------------------------------------------------------------------------
		qNum = 3;
		Label q3_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q3_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q3_col3 = makeColumn3(tbl,1, false);
		Button q3_check = new Button(q3_col3, SWT.CHECK);
		q3_check.setBackground(C.APP_BGCOLOR);
		q3_check.setText("Yes");
		if(!empty) { q3_check.setSelection(aRow.getQ3Checkbox().equals("TRUE")); }
		Text q3_col4 = MakeColumn4(tbl, false);
		if(!empty) { q3_col4.setText( C.notNull(aRow.getQ3Comments()) ); }
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 4;
		Label q4_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q4_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q4_col3 = makeColumn3(tbl,3, false);
		final Button q4_radio1 = new Button(q4_col3, SWT.RADIO);
		q4_radio1.setText("Hatch");
		q4_radio1.setData(new String("HATCH"));
		q4_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q4_radio1.setSelection(aRow.getQ4Value().equals("HATCH")); 
		final Button q4_radio2 = new Button(q4_col3, SWT.RADIO);
		q4_radio2.setText("Door");
		q4_radio2.setData(new String("DOOR"));
		q4_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q4_radio2.setSelection(aRow.getQ4Value().equals("DOOR")); 
		final Button q4_radio3 = new Button(q4_col3, SWT.RADIO);
		q4_radio3.setText("Manhole");
		q4_radio3.setData(new String("MANHOLE"));
		q4_radio3.setBackground(C.APP_BGCOLOR);
		if(!empty) q4_radio3.setSelection(aRow.getQ4Value().equals("MANHOLE")); 
		Text q4_col4 = MakeColumn4(tbl, false);
		if(!empty) { q4_col4.setText( C.notNull(aRow.getQ4Comments()) ); }
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 5;
		Label q5_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q5_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q5_col3 = makeColumn3(tbl,4, false);
		Label q5_lblH = new Label(q5_col3, SWT.NONE);
		q5_lblH.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q5_lblH.setBackground(C.APP_BGCOLOR);
		q5_lblH.setFont(C.FONT_9);		
		q5_lblH.setText("Height:");		
		Text q5_txtH = new Text(q5_col3, SWT.BORDER);
		GridData gd_q5_txtH = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_q5_txtH.heightHint = 10;
		gd_q5_txtH.widthHint = dimBoxW;
		q5_txtH.setLayoutData(gd_q5_txtH);
		q5_txtH.setFont(C.FONT_8);
		if(!empty) { q5_txtH.setText(C.notNull(aRow.getQ5DimsH())); }
		Label q5_lblW = new Label(q5_col3, SWT.NONE);
		q5_lblW.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q5_lblW.setBackground(C.APP_BGCOLOR);
		q5_lblW.setFont(C.FONT_9);		
		q5_lblW.setText("Width:");		
		Text q5_txtW = new Text(q5_col3, SWT.BORDER);
		GridData gd_q5_txtW = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_q5_txtW.heightHint = 10;
		gd_q5_txtW.widthHint = dimBoxW;
		q5_txtW.setLayoutData(gd_q5_txtW);
		q5_txtW.setFont(C.FONT_8);		
		if(!empty) { q5_txtW.setText(C.notNull(aRow.getQ5DimsW())); }
		Text q5_col4 = MakeColumn4(tbl,false);
		if(!empty) { q5_col4.setText( C.notNull(aRow.getQ5Comments()) ); }
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 6;
		Label q6_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q6_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q6_col3 = makeColumn3(tbl,1, false);
		Button q6_check = new Button(q6_col3, SWT.CHECK);
		q6_check.setBackground(C.APP_BGCOLOR);
		q6_check.setText("Yes");
		if(!empty) { q6_check.setSelection(aRow.getQ6Checkbox().equals("TRUE")); }
		Text q6_col4 = MakeColumn4(tbl, false);
		if(!empty) { q6_col4.setText( C.notNull(aRow.getQ6Comments()) ); }
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 7;
		Label q7_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q7_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q7_col3 = makeColumn3(tbl,2, false);
		final Button q7_radio1 = new Button(q7_col3, SWT.RADIO);
		q7_radio1.setText("Vertical");
		q7_radio1.setData(new String("VERTIcAL"));
		q7_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q7_radio1.setSelection(aRow.getQ7Value().equals("VERTICAL")); 
		final Button q7_radio2 = new Button(q7_col3, SWT.RADIO);
		q7_radio2.setText("Horizontal");
		q7_radio2.setData(new String("HORIZONTAL"));
		q7_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q7_radio2.setSelection(aRow.getQ7Value().equals("HORIZONTAL")); 
		Text q7_col4 = MakeColumn4(tbl, false);
		if(!empty) { q7_col4.setText( C.notNull(aRow.getQ7Comments()) ); }
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 8;
		final Label q8_col1 = MakeColumn1(tbl,"\t> "+qText.elementAt(qNum), !q7_radio1.getSelection());
		final Label q8_col2 = makeColumn2(tbl, qHints.elementAt(qNum), !q7_radio1.getSelection());
		final Composite q8_col3 = makeColumn3(tbl,1, !q7_radio1.getSelection());
		Button q8_check = new Button(q8_col3, SWT.CHECK);
		q8_check.setBackground(C.APP_BGCOLOR);
		q8_check.setText("Yes");
		if(!empty) { q8_check.setSelection(aRow.getQ8Checkbox().equals("TRUE")); }
		final Text q8_col4 = MakeColumn4(tbl, !q7_radio1.getSelection());
		if(!empty) { q8_col4.setText( C.notNull(aRow.getQ8Comments()) ); }
		final Label q8_sep = Separator(tbl, !q7_radio1.getSelection());
		//-------------------------------------------------------------------------------------------------------
		qNum = 9;
		final Label q9_col1 = MakeColumn1(tbl,"\t> "+qText.elementAt(qNum), !q7_radio1.getSelection());
		final Label q9_col2 = makeColumn2(tbl, qHints.elementAt(qNum), !q7_radio1.getSelection());
		final Composite q9_col3 = makeColumn3(tbl,1, !q7_radio1.getSelection());
		Button q9_check = new Button(q9_col3, SWT.CHECK);
		q9_check.setBackground(C.APP_BGCOLOR);
		q9_check.setText("Yes");
		if(!empty) { q9_check.setSelection(aRow.getQ9Checkbox().equals("TRUE")); }
		final Text q9_col4 = MakeColumn4(tbl, !q7_radio1.getSelection());
		if(!empty) { q9_col4.setText( C.notNull(aRow.getQ9Comments()) ); }
		final Label q9_sep = Separator(tbl, !q7_radio1.getSelection());
		//-------------------------------------------------------------------------------------------------------
		qNum = 10;
		Label q10_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q10_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q10_col3 = makeColumn3(tbl,1, false);
		Button q10_check = new Button(q10_col3, SWT.CHECK);
		q10_check.setBackground(C.APP_BGCOLOR);
		q10_check.setText("Yes");
		if(!empty) { q10_check.setSelection(aRow.getQ10Checkbox().equals("TRUE")); }
		Text q10_col4 = MakeColumn4(tbl, false);
		if(!empty) { q10_col4.setText( C.notNull(aRow.getQ10Comments()) ); }
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 12;
		Label q12_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q12_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q12_col3 = makeColumn3(tbl,1, false);
		Button q12_check = new Button(q12_col3, SWT.CHECK);
		q12_check.setBackground(C.APP_BGCOLOR);
		q12_check.setText("Yes");
		if(!empty) { q12_check.setSelection(aRow.getQ12Checkbox().equals("TRUE")); }
		Text q12_col4 = MakeColumn4(tbl, false);
		if(!empty) { q12_col4.setText( C.notNull(aRow.getQ12Comments()) ); }
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 13;
		Label q13_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q13_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q13_col3 = makeColumn3(tbl,1, false);
		Button q13_check = new Button(q13_col3, SWT.CHECK);
		q13_check.setBackground(C.APP_BGCOLOR);
		q13_check.setText("Yes");
		if(!empty) { q13_check.setSelection(aRow.getQ13Checkbox().equals("TRUE")); }
		Text q13_col4 = MakeColumn4(tbl, false);
		if(!empty) { q13_col4.setText( C.notNull(aRow.getQ13Comments()) ); }
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 14;
		Label q14_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q14_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q14_col3 = makeColumn3(tbl,1, false);
		Button q14_check = new Button(q14_col3, SWT.CHECK);
		q14_check.setBackground(C.APP_BGCOLOR);
		q14_check.setText("Yes");
		if(!empty) { q14_check.setSelection(aRow.getQ14Checkbox().equals("TRUE")); }
		Text q14_col4 = MakeColumn4(tbl, false);
		if(!empty) { q14_col4.setText( C.notNull(aRow.getQ14Comments()) ); }
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 15;
		Label q15_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q15_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q15_col3 = makeColumn3(tbl,1, false);
		Button q15_check = new Button(q15_col3, SWT.CHECK);
		q15_check.setBackground(C.APP_BGCOLOR);
		q15_check.setText("Yes");
		if(!empty) { q15_check.setSelection(aRow.getQ15Checkbox().equals("TRUE")); }
		Text q15_col4 = MakeColumn4(tbl, false);
		if(!empty) { q15_col4.setText( C.notNull(aRow.getQ15Comments()) ); }
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------		
		qNum = 16;
		Label q16_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q16_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q16_col3 = makeColumn3(tbl,1, false);
		Button q16_check = new Button(q16_col3, SWT.CHECK);
		q16_check.setBackground(C.APP_BGCOLOR);
		q16_check.setText("Yes");
		if(!empty) { q16_check.setSelection(aRow.getQ16Checkbox().equals("TRUE")); }
		Text q16_col4 = MakeColumn4(tbl, false);
		if(!empty) { q16_col4.setText( C.notNull(aRow.getQ16Comments()) ); }
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------				
		// end loop


		q1_radio1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {	
				toggle(q2_col1,q1_radio1.getSelection());
				toggle(q2_col2,q1_radio1.getSelection());
				toggle(q2_col3,q1_radio1.getSelection());
				toggle(q2_col4,q1_radio1.getSelection());
				toggle(q2_sep,q1_radio1.getSelection());					
				Rectangle r = comp.getParent().getClientArea();
				((ScrolledComposite) comp.getParent()).setMinSize(comp.computeSize(r.width, SWT.DEFAULT));
				tbl.layout();			
			}
		});
		q1_radio2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				toggle(q2_col1,!q1_radio2.getSelection());
				toggle(q2_col2,!q1_radio2.getSelection());
				toggle(q2_col3,!q1_radio2.getSelection());
				toggle(q2_col4,!q1_radio2.getSelection());
				toggle(q2_sep,!q1_radio2.getSelection());					
				Rectangle r = comp.getParent().getClientArea();
				((ScrolledComposite) comp.getParent()).setMinSize(comp.computeSize(r.width, SWT.DEFAULT));
				tbl.layout();			
			}
		});

		//q7 checkbox event handler to toggle dependent fields & redraw panel
		q7_radio1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				boolean selected = !q7_radio1.getSelection();
				toggle(q8_col1,selected);
				toggle(q8_col2,selected);
				toggle(q8_col3,selected);
				toggle(q8_col4,selected);
				toggle(q8_sep,selected);					
				toggle(q9_col1,selected);
				toggle(q9_col2,selected);
				toggle(q9_col3,selected);
				toggle(q9_col4,selected);
				toggle(q9_sep,selected);					
				Rectangle r = comp.getParent().getClientArea();
				((ScrolledComposite) comp.getParent()).setMinSize(comp.computeSize(r.width, SWT.DEFAULT));
				tbl.layout();			
			}
		});
		
		// footer row
		Group footerRow = new Group(comp, SWT.NONE);
		footerRow.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		GridLayout gl_footerRow = new GridLayout(3, false);
		gl_footerRow.verticalSpacing = 0;
		gl_footerRow.horizontalSpacing = 10;
		gl_footerRow.marginWidth = 0;
		footerRow.setLayout(gl_footerRow);
		footerRow.setBackground(C.APP_BGCOLOR);

		final Button btnB = new Button(footerRow, SWT.NONE);
		btnB.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		btnB.setBackground(C.APP_BGCOLOR);
		btnB.setFont(C.FONT_11B);
		btnB.setText("<<");
		btnB.setEnabled(false);

		final Label pageLoc = new Label(footerRow, SWT.NONE);
		pageLoc.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		pageLoc.setBackground(C.APP_BGCOLOR);
		pageLoc.setFont(C.FONT_11B);
		pageLoc.setText("Page 1 of 2");

		final Button btnF = new Button(footerRow, SWT.NONE);
		btnF.setToolTipText("Save Checklist and go to Classification");
		btnF.setBackground(C.APP_BGCOLOR);
		btnF.setFont(C.FONT_11B);
		btnF.setText(">>");
		btnF.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				WindowController.showEntryAuditClassification(entryID);
			}
		});

		// redraw panel on window resize
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
