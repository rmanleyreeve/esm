package com.rmrdigitalmedia.esm.test;

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
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.models.EsmUsersTable.Row;
import com.rmrdigitalmedia.esm.models.SpaceChecklistAuditTable;
import com.rmrdigitalmedia.esm.models.SpaceChecklistQuestionsTable;

@SuppressWarnings("unused")
public class SpaceAuditChecklistViewTest {

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
			SpaceAuditChecklistViewTest.buildPage(comp, 1);
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

	public static void buildPage(final Composite parent, final int spaceID) {

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
		SpaceChecklistAuditTable.Row aRow = null;
		try {
			aRow = SpaceChecklistAuditTable.getRow("SPACE_ID", ""+spaceID);
		} catch (SQLException e1) {
			LogController.logEvent(SpaceAuditChecklistViewTest.class, C.ERROR, e1);
		}
		boolean empty = (aRow==null);
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
		//-------------------------------------------------------------------------------------------------------
		qNum = 7;
		final String q4_txt = "Are there any restrictive crawl through holes - i.e. lightening holes etc?\n";
		final String q4_txt_2 = " * Rate the condition of these (1=poor, 3=good)";
		final Label q4_col1 = new Label(tbl, SWT.WRAP);
		GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd.horizontalIndent = 5;
		gd.exclude = false;
		q4_col1.setLayoutData(gd);
		q4_col1.setBackground(C.APP_BGCOLOR);
		q4_col1.setFont(C.FONT_11);
		q4_col1.setText(q4_txt);
		Label q4_col2 = new Label(tbl, SWT.NONE);
		q4_col2.setToolTipText("Click for Hint Text");
		gd = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		gd.exclude = false;
		q4_col2.setLayoutData(gd);		
		q4_col2.setBackground(C.APP_BGCOLOR);
		q4_col2.setImage(C.getImage("/img/hint.png"));
		
		Composite q4_col3 = new Composite(tbl, SWT.NONE);
		GridLayout gl_optionsCell = new GridLayout(4, false);
		gl_optionsCell.marginHeight = 1;
		gl_optionsCell.verticalSpacing = 1;
		q4_col3.setLayout(gl_optionsCell);
		gd = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		gd.exclude = false;
		q4_col3.setLayoutData(gd);		
		q4_col3.setBackground(C.APP_BGCOLOR);
		
		final Button q4_radio1 = new Button(q4_col3, SWT.RADIO);
		q4_radio1.setText("Yes");
		q4_radio1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
		q4_radio1.setBackground(C.APP_BGCOLOR);
		if(!empty) q4_radio1.setSelection(aRow.getQ4Boolean().equals("Y")); 
		final Button q4_radio2 = new Button(q4_col3, SWT.RADIO);
		q4_radio2.setText("No");
		q4_radio2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		q4_radio2.setBackground(C.APP_BGCOLOR);
		if(!empty) q4_radio2.setSelection(aRow.getQ4Boolean().equals("N")); 
		final Label q4_lblH = new Label(q4_col3, SWT.NONE);
		q4_lblH.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q4_lblH.setBackground(C.APP_BGCOLOR);
		q4_lblH.setFont(C.FONT_8);		
		q4_lblH.setText("Height:");	
		q4_lblH.setVisible(q4_radio1.getSelection());
		final Text q4_txtH = new Text(q4_col3, SWT.BORDER);
		GridData gd_q4_txtH = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_q4_txtH.heightHint = 10;
		gd_q4_txtH.widthHint = dimBoxW;
		q4_txtH.setLayoutData(gd_q4_txtH);
		q4_txtH.setFont(C.FONT_8);
		if(!empty) { q4_txtH.setText(C.notNull(aRow.getQ4DimsH())); }
		q4_txtH.setVisible(q4_radio1.getSelection());
		final Label q4_lblW = new Label(q4_col3, SWT.NONE);
		q4_lblW.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q4_lblW.setBackground(C.APP_BGCOLOR);
		q4_lblW.setFont(C.FONT_8);		
		q4_lblW.setText("Width:");		
		q4_lblW.setVisible(q4_radio1.getSelection());
		final Text q4_txtW = new Text(q4_col3, SWT.BORDER);
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
		Text q4_col4 = new Text(tbl, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		q4_col4.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		q4_col4.setEditable(true);
		q4_col4.setFont(C.FONT_10);
		q4_col4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd.heightHint = rowHeight;
		gd.widthHint = 200;
		gd.exclude = false;
		q4_col4.setLayoutData(gd);
		if(!empty) { q4_col4.setText( C.notNull(aRow.getQ4Comments()) ); }
		Label separator = new Label(tbl, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.CENTER);
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gd.exclude = false;
		separator.setLayoutData(gd);		
		if(q4_radio1.getSelection()) q4_col1.setText(q4_txt+q4_txt_2);
		//-------------------------------------------------------------------------------------------------------
		//-------------------------------------------------------------------------------------------------------		
		// end loop

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
