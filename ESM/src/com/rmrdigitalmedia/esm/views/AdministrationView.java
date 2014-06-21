package com.rmrdigitalmedia.esm.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.rmrdigitalmedia.esm.C;

public class AdministrationView {

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			Composite comp = new Composite(shell, SWT.BORDER);
			AdministrationView.buildPage(comp);
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

	public static void buildPage(Composite parent) {

		for (Control c : parent.getChildren()) {
			c.dispose();
		}

		parent.setLayout(new FillLayout(SWT.VERTICAL));
		SashForm panels = new SashForm(parent, SWT.NONE);
		panels.setBackground(C.TITLEBAR_BGCOLOR);
		panels.setLayout(new FillLayout());
		Composite mainpanel = new Composite(panels, SWT.NONE);
		mainpanel.setBackground(C.APP_BGCOLOR);
		mainpanel.setLayout(new FillLayout());
		Composite rightpanel = new Composite(panels, SWT.NONE);
		rightpanel.setBackground(C.APP_BGCOLOR);
		rightpanel.setLayout(new FillLayout());
		panels.setWeights(new int[] { 1, 3 });

		// final layout settings
		panels.setWeights(new int[] { 2, 1 });
		parent.layout();
		parent.getShell().setCursor(
				new Cursor(parent.getDisplay(), SWT.CURSOR_ARROW));
	}
}
