package com.rmrdigitalmedia.esm.views;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.models.EsmUsersTable.Row;
import com.rmrdigitalmedia.esm.models.SpacesTable;
import org.eclipse.swt.widgets.Button;

public class SpaceAuditView {

	static Row user = WindowController.user;

	static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
	private static Label sep;

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

	public static void buildPage(Composite parent, final int spaceID) {
		
		SpacesTable.Row sRow = null;
		try {
			sRow = SpacesTable.getRow(spaceID);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		SashForm panels = new SashForm(parent,SWT.NONE);
		panels.setBackground(C.TITLEBAR_BGCOLOR);
		panels.setLayout(new FillLayout());
				
		
		Composite leftpanel = new Composite(panels,SWT.NONE);
		leftpanel.setBackground(C.APP_BGCOLOR);
		FillLayout fl_leftpanel = new FillLayout();
		fl_leftpanel.type = SWT.VERTICAL;
		leftpanel.setLayout(fl_leftpanel);
		
		Button btnPage1 = new Button(leftpanel, SWT.PUSH);
		btnPage1.setText("Page 1");
		Button btnPage2 = new Button(leftpanel, SWT.PUSH);
		btnPage2.setText("Page 2");
		
		
		Composite rightpanel = new Composite(panels,SWT.NONE);
		rightpanel.setBackground(C.APP_BGCOLOR);
		rightpanel.setLayout(new FillLayout());


		// final layout settings	
		panels.setWeights(new int[] {1, 9});				
		parent.layout();

	}
}
