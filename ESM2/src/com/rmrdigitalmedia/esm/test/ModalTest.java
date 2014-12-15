package com.rmrdigitalmedia.esm.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.rmrdigitalmedia.esm.C;

public class ModalTest {

	public ModalTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.ON_TOP);
		shell.setSize(300,50);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
		Label lblMsg = new Label(shell, SWT.NONE);
		lblMsg.setBounds(0, 15, 300, 40);
		lblMsg.setAlignment(SWT.CENTER);
		lblMsg.setFont(C.FONT_12B);
		lblMsg.setText("Working, please wait...");
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}

}
