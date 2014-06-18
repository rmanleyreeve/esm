package com.rmrdigitalmedia.esm.views;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.AuditController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.models.EntrypointClassificationAuditTable;
import com.rmrdigitalmedia.esm.models.EntrypointClassificationQuestionsTable;
import com.rmrdigitalmedia.esm.models.EntrypointsTable;
import com.rmrdigitalmedia.esm.models.SpaceClassificationAuditTable;
import com.rmrdigitalmedia.esm.models.EsmUsersTable.Row;

@SuppressWarnings("unused")
public class EntryAuditClassificationView {

	private static Row user = WindowController.user;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
	private static Label sep;
	private static int qNum;
	private static int rowHeight = 40;
	private static int colHeaderH = 40;
	private static int dimBoxW = 30;
	private static boolean empty;
	// form fields
	private static Button q1_radio1;
	private static Button q1_radio2;
	private static Button q1_radio3;
	private static Text q1_col5;
	private static Button q2_radio1;
	private static Button q2_radio2;
	private static Button q2_radio3;
	private static Text q2_col5;
	private static Button q3_radio1;
	private static Button q3_radio2;
	private static Button q3_radio3;
	private static Text q3_col5;
	private static Button q4_radio1;
	private static Button q4_radio2;
	private static Text q4_col5;
	private static Button q5_radio1;
	private static Button q5_radio2;
	private static Button q5_radio3;
	private static Text q5_col5;

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
			EntryAuditClassificationView.buildPage(comp, 1);
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
		question.setFont(C.FONT_11);
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
		hint.setImage(C.getImage("/img/hint.png"));
		hint.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				EsmApplication.alert(text);
			}
		});
		return hint;
	}	
	// options control cell to hold form objects
	private static Composite makeColumn3(Composite comp, int numCols, boolean hide) {
		Composite optionsCell = new Composite(comp, SWT.NONE);
		GridLayout gl_optionsCell = new GridLayout(numCols, false);
		gl_optionsCell.marginHeight = 6;
		gl_optionsCell.verticalSpacing = 3;
		optionsCell.setLayout(gl_optionsCell);
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd.exclude = hide;
		optionsCell.setLayoutData(gd);		
		optionsCell.setBackground(C.APP_BGCOLOR);
		return optionsCell;
	}
	// classification icon
	private static Label makeColumn4(Composite comp, boolean hide) {
		Label classification = new Label(comp, SWT.NONE);
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd.exclude = hide;
		classification.setLayoutData(gd);		
		classification.setBackground(C.APP_BGCOLOR);
		classification.setImage(C.getImage("/img/red.png"));
		return classification;
	}	
	// comments field
	private static Text MakeColumn5(Composite comp, boolean hide) {
		Text comments = new Text(comp, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		comments.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		comments.setEditable(true);
		comments.setFont(C.FONT_10);
		comments.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd.heightHint = rowHeight;
		gd.widthHint = 250;
		gd.exclude = hide;
		comments.setLayoutData(gd);
		return comments;
	}

	private static Label Separator(Composite comp, boolean hide) {
		Label separator = new Label(comp, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.CENTER);
		final GridData gd = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
		gd.exclude = hide;
		separator.setLayoutData(gd);		
		return separator;
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
		lblName.setText("Entry Point Classification Questions");		

		Label lblStatus = new Label(headerRow, SWT.NONE);
		lblStatus.setFont(C.FONT_12B);
		lblStatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblStatus.setBackground(C.APP_BGCOLOR);
		lblStatus.setText("Overall Classification");		

		Label lblStatusImg = new Label(headerRow,SWT.NONE);
		GridData gd_lblStatusImg = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_lblStatusImg.horizontalIndent = 10;
		lblStatusImg.setLayoutData(gd_lblStatusImg);
		// progress image
		int progress = AuditController.calculateEntryClassificationCompletion(entryID);
		lblStatusImg.setImage(C.getImage("/img/Percent_"+progress+".png"));

		//table layout
		final Group tbl = new Group(comp, SWT.BORDER);
		tbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gl_tbl = new GridLayout(5, false);
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
		lblChecklist.setText("Classification Question");
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
		CLabel lblClassification = new CLabel(tbl, SWT.CENTER);
		GridData gd_lblClassification = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblClassification.widthHint = 120;
		gd_lblClassification.heightHint = colHeaderH;
		lblClassification.setLayoutData(gd_lblClassification);
		lblClassification.setBackground(C.AUDIT_COLHEADER_BGCOLOR);
		lblClassification.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblClassification.setFont(C.FONT_12B);
		lblClassification.setText("Classification");
		CLabel lblComments = new CLabel(tbl, SWT.NONE);
		GridData gd_lblComments = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_lblComments.heightHint = colHeaderH;
		lblComments.setLayoutData(gd_lblComments);
		lblComments.setBackground(C.AUDIT_COLHEADER_BGCOLOR);
		lblComments.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblComments.setText("Comments");
		lblComments.setFont(C.FONT_12B);
		sep = new Label(tbl, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.CENTER);
		sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1));				

		// get from DB
		EntrypointClassificationAuditTable.Row aRow = null;
		try {
			aRow = EntrypointClassificationAuditTable.getRow("ENTRYPOINT_ID", ""+entryID);
		} catch (SQLException e1) {
			LogController.logEvent(EntryAuditClassificationView.class, C.ERROR, e1);
		}
		empty = (aRow==null);
		EntrypointClassificationQuestionsTable.Row[] qRows = null;
		Vector<String> qText = new Vector<String>();
		Vector<String> qHints = new Vector<String>();
		try {
			qText.add(0, null); qHints.add(0,null);
			qRows = EntrypointClassificationQuestionsTable.getRows("1=1 ORDER BY SEQUENCE ASC");
			for(EntrypointClassificationQuestionsTable.Row qRow:qRows) {
				qText.add(qRow.getSequence(), qRow.getQText());
				qHints.add(qRow.getSequence(), qRow.getQHint());
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		// start loop through audit classification questions
		qNum = 1;
		Label q1_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q1_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q1_col3 = makeColumn3(tbl,1, false);
		q1_radio1 = new Button(q1_col3, SWT.RADIO);
		q1_radio1.setText("Very difficult");
		q1_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q1_radio1.setSelection(aRow.getQ1Value()==1); 
		q1_radio2 = new Button(q1_col3, SWT.RADIO);
		q1_radio2.setText("Quite difficult");
		q1_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q1_radio2.setSelection(aRow.getQ1Value()==2); 
		q1_radio3 = new Button(q1_col3, SWT.RADIO);
		q1_radio3.setText("Not difficult");
		q1_radio3.setBackground(C.APP_BGCOLOR);
		if(!empty) q1_radio3.setSelection(aRow.getQ1Value()==3); 
		Label q1_col4 = makeColumn4(tbl, false);	
		if(q1_radio2.getSelection()) { q1_col4.setImage(C.getImage("/img/amber.png")); }
		if(q1_radio3.getSelection()) { q1_col4.setImage(C.getImage("/img/green.png")); }
		q1_col5 = MakeColumn5(tbl, false);
		if(!empty) q1_col5.setText( C.notNull(aRow.getQ1Comments()) );
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 2;
		Label q2_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q2_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q2_col3 = makeColumn3(tbl,1, false);
		q2_radio1 = new Button(q2_col3, SWT.RADIO);
		q2_radio1.setText("Very difficult");
		q2_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q2_radio1.setSelection(aRow.getQ2Value()==1); 
		q2_radio2 = new Button(q2_col3, SWT.RADIO);
		q2_radio2.setText("Quite difficult");
		q2_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q2_radio2.setSelection(aRow.getQ2Value()==2); 
		q2_radio3 = new Button(q2_col3, SWT.RADIO);
		q2_radio3.setText("Not difficult");
		q2_radio3.setBackground(C.APP_BGCOLOR);
		if(!empty) q2_radio3.setSelection(aRow.getQ2Value()==3); 
		Label q2_col4 = makeColumn4(tbl, false);	
		if(q2_radio2.getSelection()) { q2_col4.setImage(C.getImage("/img/amber.png")); }
		if(q2_radio3.getSelection()) { q2_col4.setImage(C.getImage("/img/green.png")); }
		q2_col5 = MakeColumn5(tbl, false);
		if(!empty) q2_col5.setText( C.notNull(aRow.getQ2Comments()) );
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 3;		
		Label q3_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q3_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q3_col3 = makeColumn3(tbl,1, false);
		q3_radio1 = new Button(q3_col3, SWT.RADIO);
		q3_radio1.setText("Very difficult");
		q3_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q3_radio1.setSelection(aRow.getQ3Value()==1); 
		q3_radio2 = new Button(q3_col3, SWT.RADIO);
		q3_radio2.setText("Quite difficult");
		q3_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q3_radio2.setSelection(aRow.getQ2Value()==2); 
		q3_radio3 = new Button(q3_col3, SWT.RADIO);
		q3_radio3.setText("Not difficult");
		q3_radio3.setBackground(C.APP_BGCOLOR);
		if(!empty) q3_radio3.setSelection(aRow.getQ3Value()==3); 
		Label q3_col4 = makeColumn4(tbl, false);	
		if(q3_radio2.getSelection()) { q3_col4.setImage(C.getImage("/img/amber.png")); }
		if(q3_radio3.getSelection()) { q3_col4.setImage(C.getImage("/img/green.png")); }
		q3_col5 = MakeColumn5(tbl, false);
		if(!empty) q3_col5.setText( C.notNull(aRow.getQ3Comments()) );
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 4;
		Label q4_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q4_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q4_col3 = makeColumn3(tbl,1, false);
		q4_radio1 = new Button(q4_col3, SWT.RADIO);
		q4_radio1.setText("Yes");
		q4_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q4_radio1.setSelection(aRow.getQ4Boolean()!=null && aRow.getQ4Boolean().equals("Y")); 
		q4_radio2 = new Button(q4_col3, SWT.RADIO);
		q4_radio2.setText("No");
		q4_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q4_radio2.setSelection(aRow.getQ4Boolean()!=null && aRow.getQ4Boolean().equals("N")); 
		Label q4_col4 = makeColumn4(tbl, false);	
		if(q4_radio2.getSelection()) { q4_col4.setImage(C.getImage("/img/green.png")); }
		q4_col5 = MakeColumn5(tbl, false);
		if(!empty) q4_col5.setText( C.notNull(aRow.getQ4Comments()) );
		sep = Separator(tbl, false);	
		//-------------------------------------------------------------------------------------------------------
		qNum = 5;
		Label q5_col1 = MakeColumn1(tbl,qText.elementAt(qNum), false);
		Label q5_col2 = makeColumn2(tbl, qHints.elementAt(qNum), false);
		Composite q5_col3 = makeColumn3(tbl,1, false);
		q5_radio1 = new Button(q5_col3, SWT.RADIO);
		q5_radio1.setText("Very difficult");
		q5_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q5_radio1.setSelection(aRow.getQ5Value()==1); 
		q5_radio2 = new Button(q5_col3, SWT.RADIO);
		q5_radio2.setText("Fairly difficult");
		q5_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q5_radio2.setSelection(aRow.getQ5Value()==2); 
		q5_radio3 = new Button(q5_col3, SWT.RADIO);
		q5_radio3.setText("Not difficult");
		q5_radio3.setBackground(C.APP_BGCOLOR);
		if(!empty) q5_radio3.setSelection(aRow.getQ5Value()==3); 
		Label q5_col4 = makeColumn4(tbl, false);	
		if(q5_radio2.getSelection()) { q5_col4.setImage(C.getImage("/img/amber.png")); }
		if(q5_radio3.getSelection()) { q5_col4.setImage(C.getImage("/img/green.png")); }
		q5_col5 = MakeColumn5(tbl, false);
		if(!empty) q5_col5.setText( C.notNull(aRow.getQ5Comments()) );
		sep = Separator(tbl, false);

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
		btnReturn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		btnReturn.setBackground(C.APP_BGCOLOR);
		btnReturn.setFont(C.FONT_11B);
		btnReturn.setText("<< Back to Details");
		btnReturn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// save to DB
				saveAudit(entryID);
				int spaceID = WindowController.currentSpaceId;
				try {
					spaceID = EntrypointsTable.getRow(entryID).getSpaceID();
				} catch (SQLException e) {}
				WindowController.showSpaceDetail(spaceID);
			}
		});

		final Button btnSave = new Button(footerRow, SWT.NONE);
		btnSave.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		btnSave.setBackground(C.APP_BGCOLOR);
		btnSave.setFont(C.FONT_11B);
		btnSave.setText("Save Classification");
		btnSave.setImage(C.getImage("/img/16_save.png"));
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// save to DB
				saveAudit(entryID);
				// reload screen
				WindowController.showEntryAuditClassification(entryID);
			}
		});

		final Button btnProceed = new Button(footerRow, SWT.NONE);
		btnProceed.setToolTipText("Save Classification and go to Checklist");
		btnProceed.setBackground(C.APP_BGCOLOR);
		btnProceed.setFont(C.FONT_11B);
		btnProceed.setText("Entry Checklist");
		btnProceed.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// save to DB
				saveAudit(entryID);
				// next screen
				WindowController.showEntryAuditChecklist(entryID);
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
		parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_ARROW));
	}

	static void saveAudit(int entryID) {
		// insert new row if empty
		if(empty) {
			try {
				EntrypointClassificationAuditTable.Row r = EntrypointClassificationAuditTable.getRow();
				r.setEntrypointID(entryID);
				r.insert();
			} catch (SQLException e1) {
				LogController.logEvent(EntryAuditClassificationView.class, C.FATAL, "ERROR INSERT ENTRY CLASSIFICATION ROW", e1);
			}			
		}
		EntrypointClassificationAuditTable.Row aRow = null;
		try {
			aRow = EntrypointClassificationAuditTable.getRow("ENTRYPOINT_ID", ""+entryID);
		} catch (Exception ex) {
			LogController.logEvent(EntryAuditClassificationView.class, C.FATAL, "ERROR SELECT ENTRY CLASSIFICATION ROW", ex);
		}
		if(aRow != null) {
			try {
				//1
				if(q1_radio1.getSelection()) { aRow.setQ1Value(1); }
				else if(q1_radio2.getSelection()) { aRow.setQ1Value(2); }
				else if(q1_radio3.getSelection()) { aRow.setQ1Value(3); }	
				else { aRow.setQ1Value(0); }
				//2
				if(q2_radio1.getSelection()) { aRow.setQ2Value(1); }
				else if(q2_radio2.getSelection()) { aRow.setQ2Value(2); }
				else if(q2_radio3.getSelection()) { aRow.setQ2Value(3); }	
				else { aRow.setQ2Value(0); }
				//3
				if(q3_radio1.getSelection()) { aRow.setQ3Value(1); }
				else if(q3_radio2.getSelection()) { aRow.setQ3Value(2); }
				else if(q3_radio3.getSelection()) { aRow.setQ3Value(3); }	
				else { aRow.setQ3Value(0); }
				//4
				aRow.setQ4Boolean( C.getRB(q4_radio1,q4_radio2) );
				//5
				if(q5_radio1.getSelection()) { aRow.setQ5Value(1); }
				else if(q5_radio2.getSelection()) { aRow.setQ5Value(2); }
				else if(q5_radio3.getSelection()) { aRow.setQ5Value(3); }	
				else { aRow.setQ5Value(0); }
				// commit the transaction
				aRow.update();
			} catch (SQLException e) {
				LogController.logEvent(EntryAuditClassificationView.class, C.FATAL, "ERROR UPDATE SPACE CLASSIFICATION ROW", e);
			}
		}
	}


}
