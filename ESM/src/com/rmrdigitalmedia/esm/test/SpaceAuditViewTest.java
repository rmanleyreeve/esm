package com.rmrdigitalmedia.esm.test;

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
public class SpaceAuditViewTest {

	static Row user = WindowController.user;
	static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
	private static Label sep;
	private static int qNum;
	static int rowHeight = 45;
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
			SpaceAuditViewTest.buildPage(comp, 1);
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
		gl_comp.marginBottom = 5;
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

		GridData gd;

		// start loop through audit checklist questions
		//-------------------------------------------------------------------------------------------------------
		qNum = 7;
		final Label q7_col1 = new Label(tbl, SWT.WRAP);
		gd = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd.horizontalIndent = 5;
		q7_col1.setLayoutData(gd);
		q7_col1.setBackground(C.APP_BGCOLOR);
		q7_col1.setFont(C.FONT_11);
		q7_col1.setText("Are there internal vertical ladders present?\n\n");
				
		Label q7_col2 = new Label(tbl, SWT.NONE);
		q7_col2.setToolTipText("Click for Hint Text");
		q7_col2.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		q7_col2.setBackground(C.APP_BGCOLOR);
		q7_col2.setImage(C.getImage("/img/hint.png"));
		q7_col2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				EsmApplication.alert("Hint text here");
			}
		});
		Composite q7_col3 = new Composite(tbl, SWT.NONE);
		GridLayout gl_q7_col3 = new GridLayout(3, false);
		gl_q7_col3.marginHeight = 1;
		gl_q7_col3.verticalSpacing = 1;
		q7_col3.setLayout(gl_q7_col3);
		q7_col3.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		q7_col3.setBackground(C.APP_BGCOLOR);
		
		final Button q7_check = new Button(q7_col3, SWT.CHECK);
		q7_check.setBackground(C.APP_BGCOLOR);
		q7_check.setText("Yes");
		q7_check.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));		
		final Button q7_radio1 = new Button(q7_col3, SWT.RADIO);
		q7_radio1.setText("1");
		q7_radio1.setBackground(C.APP_BGCOLOR);
		final Button q7_radio2 = new Button(q7_col3, SWT.RADIO);
		q7_radio2.setText("2");
		q7_radio2.setBackground(C.APP_BGCOLOR);
		final Button q7_radio3 = new Button(q7_col3, SWT.RADIO);
		q7_radio3.setText("3");
		q7_radio3.setBackground(C.APP_BGCOLOR);
		q7_radio1.setVisible(false);
		q7_radio2.setVisible(false);
		q7_radio3.setVisible(false);
		q7_check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(q7_check.getSelection()) {
					q7_radio1.setVisible(true);
					q7_radio2.setVisible(true);
					q7_radio3.setVisible(true);
					q7_col1.setText("Are there internal vertical ladders present?\nRate the condition of these (1=poor, 3=good)");
				} else {
					q7_radio1.setVisible(false);
					q7_radio2.setVisible(false);
					q7_radio3.setVisible(false);
					q7_col1.setText("Are there internal vertical ladders present?\n");
				}
			}
		});
		Text q7_col4 = new Text(tbl, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		q7_col4.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		q7_col4.setEditable(true);
		q7_col4.setFont(C.FONT_10);
		q7_col4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd.heightHint = rowHeight;
		gd.widthHint = 200;
		q7_col4.setLayoutData(gd);
		
		Label q7_separator = new Label(tbl, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.CENTER);
		q7_separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));		
		//-------------------------------------------------------------------------------------------------------
		
		qNum = 8;		
		final Label q8_col1 = new Label(tbl, SWT.NONE);
		q8_col1.setBackground(C.APP_BGCOLOR);
		q8_col1.setFont(C.FONT_11);
		q8_col1.setText("Do they have staging points/landings?\n\n");
		final GridData gd_q8_col1 = new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1);
		gd_q8_col1.exclude = true;
		//gd_q8_col1.heightHint = 1;
		q8_col1.setLayoutData(gd_q8_col1);

/*		
  	GridData gd_q8_check = new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1);
		q8_check.setLayoutData(gd_q8_check);		
		final Button q8_radio1 = new Button(q8_col3, SWT.RADIO);
		q8_radio1.setText("1");
		q8_radio1.setBackground(C.APP_BGCOLOR);
		final Button q8_radio2 = new Button(q8_col3, SWT.RADIO);
		q8_radio2.setText("2");
		q8_radio2.setBackground(C.APP_BGCOLOR);
		final Button q8_radio3 = new Button(q8_col3, SWT.RADIO);
		q8_radio3.setText("3");
		q8_radio3.setBackground(C.APP_BGCOLOR);
		q8_radio1.setVisible(false);
		q8_radio2.setVisible(false);
		q8_radio3.setVisible(false);
		q8_check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(q8_check.getSelection()) {
					q8_radio1.setVisible(true);
					q8_radio2.setVisible(true);
					q8_radio3.setVisible(true);
					q8_col1.setText("Do they have staging points/landings?\nRate the condition of these (1=poor, 3=good)");
				} else {
					q8_radio1.setVisible(false);
					q8_radio2.setVisible(false);
					q8_radio3.setVisible(false);
					q8_col1.setText("Do they have staging points/landings?\n");
				}
			}
		});
*/		

		final  Text q8_col4 = new Text(tbl, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		q8_col4.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		q8_col4.setEditable(true);
		q8_col4.setFont(C.FONT_10);
		q8_col4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		final GridData gd_q8_col4 = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_q8_col4.exclude = true;
		gd_q8_col4.heightHint = rowHeight;
		gd_q8_col4.widthHint = 200;
		q8_col4.setLayoutData(gd_q8_col4);
		
		final Label q8_sep = new Label(tbl, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.CENTER);
		final GridData gd_q8_sep = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gd_q8_sep.exclude = true;
		q8_sep.setLayoutData(gd_q8_sep);		
		
		q8_col1.setVisible(false);
		q8_col4.setVisible(false);
		q8_sep.setVisible(false);
		tbl.layout();
		//-------------------------------------------------------------------------------------------------------
		//-------------------------------------------------------------------------------------------------------
		qNum = 9;
		final Label q9_col1 = new Label(tbl, SWT.WRAP);
		gd = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd.horizontalIndent = 5;
		q9_col1.setLayoutData(gd);
		q9_col1.setBackground(C.APP_BGCOLOR);
		q9_col1.setFont(C.FONT_11);
		q9_col1.setText("Foo bar foo bar?\n\n");
				
		Label q9_col2 = new Label(tbl, SWT.NONE);
		q9_col2.setToolTipText("Click for Hint Text");
		q9_col2.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		q9_col2.setBackground(C.APP_BGCOLOR);
		q9_col2.setImage(C.getImage("/img/hint.png"));
		q9_col2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				EsmApplication.alert("Hint text here");
			}
		});
		Composite q9_col3 = new Composite(tbl, SWT.NONE);
		GridLayout gl_q9_col3 = new GridLayout(3, false);
		gl_q9_col3.marginHeight = 1;
		gl_q9_col3.verticalSpacing = 1;
		q9_col3.setLayout(gl_q9_col3);
		q9_col3.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		q9_col3.setBackground(C.APP_BGCOLOR);
		
		final Button q9_check = new Button(q9_col3, SWT.CHECK);
		q9_check.setBackground(C.APP_BGCOLOR);
		q9_check.setText("Yes");
		new Label(q9_col3, SWT.NONE);
		new Label(q9_col3, SWT.NONE);
		Text q9_col4 = new Text(tbl, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		q9_col4.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		q9_col4.setEditable(true);
		q9_col4.setFont(C.FONT_10);
		q9_col4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd.heightHint = rowHeight;
		gd.widthHint = 200;
		q9_col4.setLayoutData(gd);
		
		Label q9_separator = new Label(tbl, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.CENTER);
		q9_separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		new Label(tbl, SWT.NONE);
		new Label(tbl, SWT.NONE);
		new Label(tbl, SWT.NONE);
		new Label(tbl, SWT.NONE);
		
		Button btnRadioButton = new Button(tbl, SWT.RADIO);
		btnRadioButton.setText("Radio Button");
		btnRadioButton.setData(new Integer(1));
		new Label(tbl, SWT.NONE);
		new Label(tbl, SWT.NONE);
		new Label(tbl, SWT.NONE);
		
		
		//q7 dependent fields
		q7_check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(q7_check.getSelection()){
					q8_col1.setVisible(true); 
					q8_col4.setVisible(true); 
					q8_sep.setVisible(true); 
					gd_q8_col1.exclude = false;
					gd_q8_col4.exclude = false;
					gd_q8_sep.exclude = false;
					q8_col4.setLayoutData(gd_q8_col4);
					tbl.layout();
				} else {
					q8_col1.setVisible(false);
					q8_col4.setVisible(false);
					q8_sep.setVisible(false); 
					gd_q8_col1.exclude = true;
					gd_q8_col4.exclude = true;
					gd_q8_sep.exclude = true;
					q8_col4.setLayoutData(gd_q8_col4); 
					tbl.layout();
				}
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
