package com.rmrdigitalmedia.esm.views;

import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.models.SpacesTable;

import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;

public class SpaceDetailView {

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			Composite comp = new Composite(shell, SWT.BORDER);
			SpaceDetailView.buildPage(comp,1);
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

	
	public static void buildPage(Composite parent, int spaceID) {
		
		SpacesTable.Row row = null;
		try {
			row = SpacesTable.getRow(spaceID);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		SashForm panels = new SashForm(parent,SWT.NONE);
		panels.setBackground(C.TITLEBAR_BGCOLOR);
		panels.setLayout(new FillLayout());
		Composite mainpanel = new Composite(panels,SWT.NONE);
		mainpanel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		mainpanel.setLayout(new FillLayout(SWT.VERTICAL));
		
		Composite grid = new Composite(mainpanel, SWT.NONE);
		grid.setBackground(C.APP_BGCOLOR);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 10;
		gridLayout.marginHeight = 10;
		gridLayout.numColumns = 10;
		gridLayout.horizontalSpacing = 10;
		grid.setLayout(gridLayout);
		
		Label lblNname = new Label(grid, SWT.NONE);
		lblNname.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.BOLD));
		lblNname.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 7, 1));
		lblNname.setBackground(C.APP_BGCOLOR);
		lblNname.setText("Name:");		
		
		Label lblID = new Label(grid, SWT.NONE);
		lblID.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.BOLD));
		lblID.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
		lblID.setText("ID:");
		
		
		Label name = new Label(grid, SWT.NONE);
		name.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_name = new GridData(SWT.LEFT, SWT.CENTER, true, false, 7, 1);
		gd_name.minimumWidth = 300;
		name.setLayoutData(gd_name);
		name.setText(row.getName());

		Label id = new Label(grid, SWT.NONE);
		id.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		id.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		id.setText(""+row.getID());

		
		
		
		
		
		
		
		
		
		
		
		
		
		Composite rightpanel = new Composite(panels,SWT.NONE);
		rightpanel.setBackground(C.APP_BGCOLOR);
		rightpanel.setLayout(new FillLayout());
		panels.setWeights(new int[] {550, 250});	
		
		parent.layout();
		
	}
}
