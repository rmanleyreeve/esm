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
	static int rowHeight = 35;
	private static Group tbl;

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
	private static Label MakeColumn1(Composite comp, String text, boolean hide) {		
		Label question = new Label(comp, SWT.WRAP);
		GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd.horizontalIndent = 5;
		gd.exclude = hide;
		question.setLayoutData(gd);
		question.setBackground(C.APP_BGCOLOR);
		question.setFont(C.FONT_11);
		question.setText(text + "\n");
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

	private static void toggle(Label l, boolean hide) {
		GridData gd = (GridData) l.getLayoutData();
		gd.exclude = hide;
		l.setVisible(!hide);
		//l.getParent().layout(false);
	}
	private static void toggle(Composite c, boolean hide) {
		GridData gd = (GridData) c.getLayoutData();
		gd.exclude = hide;
		c.setVisible(!hide);
		//c.getParent().layout(false);
	}
	private static void toggle(Text t, boolean hide) {
		GridData gd = (GridData) t.getLayoutData();
		gd.exclude = hide;
		t.setVisible(!hide);
		//t.getParent().layout(false);
	}

	
//*****************************************************************************************************************
	
	
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
		//gl_comp.marginBottom = 10;
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
		tbl = new Group(comp, SWT.BORDER);
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
		Label q1_col1 = MakeColumn1(tbl,"What are the internal dimensions of the space: H, W, L?", false);
		Label q1_col2 = makeColumn2(tbl, "Hint text here", false);
		Composite q1_col3 = makeColumn3(tbl,6, false);
		Label q1_lblH = new Label(q1_col3, SWT.NONE);
		q1_lblH.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q1_lblH.setBackground(C.APP_BGCOLOR);
		q1_lblH.setFont(C.FONT_9);		
		q1_lblH.setText("Height:");		
		Text q1_txtH = new Text(q1_col3, SWT.BORDER);
		GridData gd_q1_txtH = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_q1_txtH.heightHint = 10;
		gd_q1_txtH.widthHint = 20;
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
		gd_q1_txtW.widthHint = 20;
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
		gd_q1_txtL.widthHint = 20;
		q1_txtL.setLayoutData(gd_q1_txtL);
		q1_txtL.setFont(C.FONT_9);		
		Text q1_col4 = MakeColumn4(tbl, false);
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 2;
		Label q2_col1 = MakeColumn1(tbl,"Is the enclosed space Compartmentalised?\n(If so, describe internal layout)", false);
		Label q2_col2 = makeColumn2(tbl, "Hint text here", false);
		Composite q2_col3 = makeColumn3(tbl,1, false);
		Button q2_check = new Button(q2_col3, SWT.CHECK);
		q2_check.setBackground(C.APP_BGCOLOR);
		q2_check.setText("Yes");
		Text q2_col4 = MakeColumn4(tbl, false);
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 3;
		Label q3_col1 = MakeColumn1(tbl,"Are internal obstacles present (baffles, pipes etc)?", false);
		Label q3_col2 = makeColumn2(tbl, "Hint text here", false);
		Composite q3_col3 = makeColumn3(tbl,1, false);
		Button q3_check = new Button(q3_col3, SWT.CHECK);
		q3_check.setBackground(C.APP_BGCOLOR);
		q3_check.setText("Yes");
		Text q3_col4 = MakeColumn4(tbl, false);
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 4;
		final String q4_txt = "Are there any restrictive crawl through holes - i.e. lightening holes etc?\n";
		final Label q4_col1 = MakeColumn1(tbl,q4_txt, false);
		Label q4_col2 = makeColumn2(tbl, "Hint text here", false);
		Composite q4_col3 = makeColumn3(tbl,4, false);
		final Button q4_check = new Button(q4_col3, SWT.CHECK);
		q4_check.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 4, 1));
		q4_check.setBackground(C.APP_BGCOLOR);
		q4_check.setText("Yes");
		final Label q4_lblH = new Label(q4_col3, SWT.NONE);
		q4_lblH.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q4_lblH.setBackground(C.APP_BGCOLOR);
		q4_lblH.setFont(C.FONT_9);		
		q4_lblH.setText("Height:");	
		q4_lblH.setVisible(false);
		final Text q4_txtH = new Text(q4_col3, SWT.BORDER);
		GridData gd_q4_txtH = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_q4_txtH.heightHint = 10;
		gd_q4_txtH.widthHint = 20;
		q4_txtH.setLayoutData(gd_q4_txtH);
		q4_txtH.setFont(C.FONT_9);
		q4_txtH.setVisible(false);
		final Label q4_lblW = new Label(q4_col3, SWT.NONE);
		q4_lblW.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		q4_lblW.setBackground(C.APP_BGCOLOR);
		q4_lblW.setFont(C.FONT_9);		
		q4_lblW.setText("Width:");		
		q4_lblW.setVisible(false);
		final Text q4_txtW = new Text(q4_col3, SWT.BORDER);
		GridData gd_q4_txtW = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_q4_txtW.heightHint = 10;
		gd_q4_txtW.widthHint = 20;
		q4_txtW.setLayoutData(gd_q4_txtW);
		q4_txtW.setFont(C.FONT_9);				
		q4_txtW.setVisible(false);
		q4_check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(q4_check.getSelection()) {
					q4_lblH.setVisible(true); q4_lblW.setVisible(true); q4_txtH.setVisible(true); q4_txtW.setVisible(true);				
					q4_col1.setText(q4_txt+" * Please state the dimensions of the holes (H,W)");
					tbl.layout();
				} else {
					q4_lblH.setVisible(false); q4_lblW.setVisible(false); q4_txtH.setVisible(false); q4_txtW.setVisible(false);
					q4_col1.setText(q4_txt);
					tbl.layout();
				}
			}
		});
		Text q4_col4 = MakeColumn4(tbl, false);
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 5;
		Label q5_col1 = MakeColumn1(tbl,"Are there any pipes running through the space that could contain hazardous liquids or gases?", false);
		Label q5_col2 = makeColumn2(tbl, "Hint text here", false);
		Composite q5_col3 = makeColumn3(tbl,1, false);
		Button q5_check = new Button(q5_col3, SWT.CHECK);
		q5_check.setBackground(C.APP_BGCOLOR);
		q5_check.setText("Yes");
		Text q5_col4 = MakeColumn4(tbl, false);
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 6;
		Label q6_col1 = MakeColumn1(tbl,"Are there electrical cables running through the space?", false);
		Label q6_col2 = makeColumn2(tbl, "Hint text here", false);
		Composite q6_col3 = makeColumn3(tbl,1, false);
		Button q6_check = new Button(q6_col3, SWT.CHECK);
		q6_check.setBackground(C.APP_BGCOLOR);
		q6_check.setText("Yes");
		Text q6_col4 = MakeColumn4(tbl, false);
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 7;
		final String q7_txt = "Are there internal vertical ladders present?\n";
		final Label q7_col1 = MakeColumn1(tbl,q7_txt, false);
		Label q7_col2 = makeColumn2(tbl, "Hint text here", false);
		Composite q7_col3 = makeColumn3(tbl,3, false);
		final Button q7_check = new Button(q7_col3, SWT.CHECK);
		q7_check.setBackground(C.APP_BGCOLOR);
		q7_check.setText("Yes");
		GridData gd_q7_check = new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1);
		q7_check.setLayoutData(gd_q7_check);		
		final Button q7_radio1 = new Button(q7_col3, SWT.RADIO);
		q7_radio1.setText("1");
		q7_radio1.setBackground(C.APP_BGCOLOR);
		q7_radio1.setVisible(false);
		final Button q7_radio2 = new Button(q7_col3, SWT.RADIO);
		q7_radio2.setText("2");
		q7_radio2.setBackground(C.APP_BGCOLOR);
		q7_radio2.setVisible(false);
		final Button q7_radio3 = new Button(q7_col3, SWT.RADIO);
		q7_radio3.setText("3");
		q7_radio3.setBackground(C.APP_BGCOLOR);
		q7_radio3.setVisible(false);
		q7_check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(q7_check.getSelection()) {
					q7_radio1.setVisible(true); q7_radio2.setVisible(true); q7_radio3.setVisible(true);
					q7_col1.setText(q7_txt+" * Rate the condition of these (1=poor, 3=good)");
				} else {
					q7_radio1.setVisible(false); q7_radio2.setVisible(false); q7_radio3.setVisible(false);
					q7_col1.setText(q7_txt);
				}
			}
		});
		Text q7_col4 = MakeColumn4(tbl, false);
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 8;
		final String q8_txt = "\t> Do they have staging points/landings?\n";
		final Label q8_col1 = MakeColumn1(tbl,q8_txt, true);
		final Label q8_col2 = makeColumn2(tbl, "Hint text here", true);
		final Composite q8_col3 = makeColumn3(tbl,3, true);
		final Button q8_check = new Button(q8_col3, SWT.CHECK);
		q8_check.setBackground(C.APP_BGCOLOR);
		q8_check.setText("Yes");
		GridData gd_q8_check = new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1);
		q8_check.setLayoutData(gd_q8_check);		
		final Button q8_radio1 = new Button(q8_col3, SWT.RADIO);
		q8_radio1.setText("1");
		q8_radio1.setBackground(C.APP_BGCOLOR);
		q8_radio1.setVisible(false); 
		final Button q8_radio2 = new Button(q8_col3, SWT.RADIO);
		q8_radio2.setText("2");
		q8_radio2.setBackground(C.APP_BGCOLOR);
		q8_radio2.setVisible(false); 
		final Button q8_radio3 = new Button(q8_col3, SWT.RADIO);
		q8_radio3.setText("3");
		q8_radio3.setBackground(C.APP_BGCOLOR);
		q8_radio3.setVisible(false);
		q8_check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(q8_check.getSelection()) {
					q8_radio1.setVisible(true); q8_radio2.setVisible(true); q8_radio3.setVisible(true);
					q8_col1.setText(q8_txt+"\t * Rate the condition of these (1=poor, 3=good)");
					tbl.layout();
				} else {
					q8_radio1.setVisible(false); q8_radio2.setVisible(false); q8_radio3.setVisible(false);
					q8_col1.setText(q8_txt);
					tbl.layout();
				}
			}
		});
		final Text q8_col4 = MakeColumn4(tbl, true);
		final Label q8_sep = Separator(tbl, true);
		//-------------------------------------------------------------------------------------------------------
		qNum = 9;
		final String q9_txt = "\t> Do they have safety hoops?\n";
		final Label q9_col1 = MakeColumn1(tbl,q9_txt, true);
		final Label q9_col2 = makeColumn2(tbl, "Hint text here", true);
		final Composite q9_col3 = makeColumn3(tbl, 3, true);
		final Button q9_check = new Button(q9_col3, SWT.CHECK);
		q9_check.setBackground(C.APP_BGCOLOR);
		q9_check.setText("Yes");
		GridData gd_q9_check = new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1);
		q9_check.setLayoutData(gd_q9_check);		
		final Button q9_radio1 = new Button(q9_col3, SWT.RADIO);
		q9_radio1.setText("1");
		q9_radio1.setBackground(C.APP_BGCOLOR);
		q9_radio1.setVisible(false); 
		final Button q9_radio2 = new Button(q9_col3, SWT.RADIO);
		q9_radio2.setText("2");
		q9_radio2.setBackground(C.APP_BGCOLOR);
		q9_radio2.setVisible(false); 
		final Button q9_radio3 = new Button(q9_col3, SWT.RADIO);
		q9_radio3.setText("3");
		q9_radio3.setBackground(C.APP_BGCOLOR);
		q9_radio3.setVisible(false);
		q9_check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(q9_check.getSelection()) {
					q9_radio1.setVisible(true);	q9_radio2.setVisible(true);	q9_radio3.setVisible(true);
					q9_col1.setText(q9_txt + "\t * Rate the condition of these (1=poor, 3=good)");
					tbl.layout();
				} else {
					q9_radio1.setVisible(false); q9_radio2.setVisible(false); q9_radio3.setVisible(false);
					q9_col1.setText(q9_txt);
					tbl.layout();
				}
			}
		});
		final Text q9_col4 = MakeColumn4(tbl, true);
		final Label q9_sep = Separator(tbl, true);
		//-------------------------------------------------------------------------------------------------------
		qNum = 10;
		final String q10_txt = "\t> Do they have handrails at landing points?\n";
		final Label q10_col1 = MakeColumn1(tbl,q10_txt, true);
		final Label q10_col2 = makeColumn2(tbl, "Hint text here", true);
		final Composite q10_col3 = makeColumn3(tbl,3, true);
		final Button q10_check = new Button(q10_col3, SWT.CHECK);
		q10_check.setBackground(C.APP_BGCOLOR);
		q10_check.setText("Yes");
		GridData gd_q10_check = new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1);
		q10_check.setLayoutData(gd_q10_check);		
		final Button q10_radio1 = new Button(q10_col3, SWT.RADIO);
		q10_radio1.setText("1");
		q10_radio1.setBackground(C.APP_BGCOLOR);
		q10_radio1.setVisible(false); 
		final Button q10_radio2 = new Button(q10_col3, SWT.RADIO);
		q10_radio2.setText("2");
		q10_radio2.setBackground(C.APP_BGCOLOR);
		q10_radio2.setVisible(false); 
		final Button q10_radio3 = new Button(q10_col3, SWT.RADIO);
		q10_radio3.setText("3");
		q10_radio3.setBackground(C.APP_BGCOLOR);
		q10_radio3.setVisible(false);
		q10_check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(q10_check.getSelection()) {
					q10_radio1.setVisible(true); q10_radio2.setVisible(true); q10_radio3.setVisible(true);
					q10_col1.setText(q10_txt+"\t * Rate the condition of these (1=poor, 3=good)");
					tbl.layout();
				} else {
					q10_radio1.setVisible(false); q10_radio2.setVisible(false); q10_radio3.setVisible(false);
					q10_col1.setText(q10_txt);
					tbl.layout();
				}
			}
		});
		final Text q10_col4 = MakeColumn4(tbl, true);
		final Label q10_sep = Separator(tbl, true);
		//-------------------------------------------------------------------------------------------------------		
		qNum = 11;
		Label q11_col1 = MakeColumn1(tbl,"Are internal anchorage points fitted?", false);
		Label q11_col2 = makeColumn2(tbl, "Hint text here", false);
		Composite q11_col3 = makeColumn3(tbl,1, false);
		Button q11_check = new Button(q11_col3, SWT.CHECK);
		q11_check.setBackground(C.APP_BGCOLOR);
		q11_check.setText("Yes");
		Text q11_col4 = MakeColumn4(tbl, false);
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 12;
		Label q12_col1 = MakeColumn1(tbl,"Does the space contain sloped or curved floors?", false);
		Label q12_col2 = makeColumn2(tbl, "Hint text here", false);
		Composite q12_col3 = makeColumn3(tbl,1, false);
		Button q12_check = new Button(q12_col3, SWT.CHECK);
		q12_check.setBackground(C.APP_BGCOLOR);
		q12_check.setText("Yes");
		Text q12_col4 = MakeColumn4(tbl, false);
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 13;
		Label q13_col1 = MakeColumn1(tbl,"Is internal lighting fitted into the space?", false);
		Label q13_col2 = makeColumn2(tbl, "Hint text here", false);
		Composite q13_col3 = makeColumn3(tbl,1, false);
		Button q13_check = new Button(q13_col3, SWT.CHECK);
		q13_check.setBackground(C.APP_BGCOLOR);
		q13_check.setText("Yes");
		Text q13_col4 = MakeColumn4(tbl, false);
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 14;
		Label q14_col1 = MakeColumn1(tbl,"Are there any power points present in the space?", false);
		Label q14_col2 = makeColumn2(tbl, "Hint text here", false);
		Composite q14_col3 = makeColumn3(tbl,1, false);
		Button q14_check = new Button(q14_col3, SWT.CHECK);
		q14_check.setBackground(C.APP_BGCOLOR);
		q14_check.setText("Yes");
		Text q14_col4 = MakeColumn4(tbl, false);
		sep = Separator(tbl, false);
		//-------------------------------------------------------------------------------------------------------
		qNum = 15;
		Label q15_col1 = MakeColumn1(tbl,"Is there potential for communications black spots (steel lined containers)?", false);
		Label q15_col2 = makeColumn2(tbl, "Hint text here", false);
		Composite q15_col3 = makeColumn3(tbl,1, false);
		Button q15_check = new Button(q15_col3, SWT.CHECK);
		q15_check.setBackground(C.APP_BGCOLOR);
		q15_check.setText("Yes");
		Text q15_col4 = MakeColumn4(tbl, false);
		sep = Separator(tbl, false);
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
		
		Button btnB = new Button(footerRow, SWT.NONE);
		btnB.setToolTipText("Previous page");
		btnB.setEnabled(false);
		btnB.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		btnB.setBackground(C.APP_BGCOLOR);
		btnB.setFont(C.FONT_11B);
		btnB.setText("<<");

		Label pageL = new Label(footerRow, SWT.NONE);
		pageL.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		pageL.setBackground(C.APP_BGCOLOR);
		pageL.setFont(C.FONT_11B);
		pageL.setText("Page 1 of 3");
		
		Button btnF = new Button(footerRow, SWT.NONE);
		btnF.setToolTipText("Next page");
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

		//q7 checkbox event handler to toggle dependent fields & redraw panel
		q7_check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				boolean selected = !q7_check.getSelection();
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
				toggle(q10_col1,selected);
				toggle(q10_col2,selected);
				toggle(q10_col3,selected);
				toggle(q10_col4,selected);
				toggle(q10_sep,selected);					
				Rectangle r = scrollPanel.getClientArea();
				scrollPanel.setMinSize(comp.computeSize(r.width, SWT.DEFAULT));
				tbl.layout();			
			}
		});

		
		// final layout settings	
		parent.layout();

	}
}
