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
import com.rmrdigitalmedia.esm.audits.SpaceAuditPage1;
import com.rmrdigitalmedia.esm.audits.SpaceAuditPage2;
import com.rmrdigitalmedia.esm.audits.SpaceAuditPage3;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.models.EsmUsersTable.Row;
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
public class SpaceAuditView {

	private static Row user = WindowController.user;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
	private static Label sep;
	private static int qNum;
	private static int rowHeight = 35;
	private static int colHeaderH = 40;
	private static int dimBoxW = 30;
	static Group tbl;
	static int pageNum;
	static int numPages = 3;

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

	public static void buildPage(final Composite parent, final int spaceID) {

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
		pageNum = 1;				
		tbl = SpaceAuditPage1.buildPage(comp, spaceID);

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
		btnB.setToolTipText("Save changes and go to previous page");
		btnB.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		btnB.setBackground(C.APP_BGCOLOR);
		btnB.setFont(C.FONT_11B);
		btnB.setText("<<");

		final Label pageLoc = new Label(footerRow, SWT.NONE);
		pageLoc.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		pageLoc.setBackground(C.APP_BGCOLOR);
		pageLoc.setFont(C.FONT_11B);
		pageLoc.setText("Page "+pageNum+" of "+numPages);

		final Button btnF = new Button(footerRow, SWT.NONE);
		btnF.setToolTipText("Save changes and go to next page");
		btnF.setBackground(C.APP_BGCOLOR);
		btnF.setFont(C.FONT_11B);
		btnF.setText(">>");

		btnB.setEnabled(pageNum>1);
		btnF.setEnabled(pageNum<3);


		btnB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				pageNum--;
				if(pageNum > 0) {
					for (Control c:tbl.getChildren()) {
						c.dispose();
					}
					if(pageNum==1) {
						btnB.setEnabled(false);
						btnF.setEnabled(true);
						SpaceAuditPage1.buildPage(tbl, spaceID);
					} else if(pageNum==2) {
						btnB.setEnabled(true);
						btnF.setEnabled(true);
						SpaceAuditPage2.buildPage(tbl, spaceID);
						tbl.setBackground(C.AUDIT_COLHEADER_BGCOLOR);
					}
					tbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
					GridLayout gl_tbl = new GridLayout(4, false);
					tbl.setLayout(gl_tbl);
					pageLoc.setText("Page "+pageNum+" of "+numPages);
					Rectangle r = scrollPanel.getClientArea();
					scrollPanel.setMinSize(comp.computeSize(r.width, SWT.DEFAULT));
					tbl.layout();
					parent.layout();	
				}
			}
		});

		btnF.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// validate form

				// save to DB

				// load screen 2				
				pageNum++;
				btnB.setEnabled(true);
				if(pageNum<4) {
					for (Control c:tbl.getChildren()) {
						c.dispose();
					}
					if(pageNum==2) {
						btnF.setEnabled(true);
						SpaceAuditPage2.buildPage(tbl, spaceID);
						tbl.setBackground(C.AUDIT_COLHEADER_BGCOLOR);
					} else if(pageNum==3) {
						btnF.setEnabled(false);
						SpaceAuditPage3.buildPage(tbl, spaceID);
						tbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					}
					tbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
					GridLayout gl_tbl = new GridLayout(4, false);
					tbl.setLayout(gl_tbl);
					pageLoc.setText("Page "+pageNum+" of "+numPages);
					Rectangle r = scrollPanel.getClientArea();
					scrollPanel.setMinSize(comp.computeSize(r.width, SWT.DEFAULT));
					tbl.layout();
					parent.layout();	
				}
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
