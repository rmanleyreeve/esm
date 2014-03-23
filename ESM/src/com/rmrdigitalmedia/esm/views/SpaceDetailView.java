package com.rmrdigitalmedia.esm.views;

import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.rmrdigitalmedia.esm.Constants;
import com.rmrdigitalmedia.esm.models.SpacesTable;

import org.eclipse.swt.widgets.Label;

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
		
		System.out.println("BUILDING "+spaceID);
		
		SpacesTable.Row row = null;
		try {
			row = SpacesTable.getRow(spaceID);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		SashForm panels = new SashForm(parent,SWT.NONE);
		panels.setBackground(Constants.TITLEBAR_BGCOLOR);
		panels.setLayout(new FillLayout());
		Composite mainpanel = new Composite(panels,SWT.NONE);
		mainpanel.setBackground(Constants.APP_BGCOLOR);
		mainpanel.setLayout(new FillLayout());
		
		Label lblNewLabel = new Label(mainpanel, SWT.NONE);
		lblNewLabel.setText(row.getDescription());
		Composite rightpanel = new Composite(panels,SWT.NONE);
		rightpanel.setBackground(Constants.APP_BGCOLOR);
		rightpanel.setLayout(new FillLayout());
		panels.setWeights(new int[] {550, 250});	
		
		parent.layout();
		
	}
}
