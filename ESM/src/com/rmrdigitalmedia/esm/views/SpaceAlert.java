package com.rmrdigitalmedia.esm.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.controllers.LogController;

public class SpaceAlert {

	public static void main (String [] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			new SpaceAlert(new Shell());			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public SpaceAlert(Shell appwin) {
		
		Display display = Display.getDefault();
	    final Shell shell = new Shell(SWT.APPLICATION_MODAL | SWT.ON_TOP);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
	    shell.setSize(400, 250);
		Rectangle bounds = appwin.getBounds ();
		Rectangle rect = shell.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + 200;//(bounds.height - rect.height) / 2;
		shell.setLocation (x, y);
		RowLayout rl_shell = new RowLayout(SWT.HORIZONTAL);
		rl_shell.marginTop = 10;
		rl_shell.marginRight = 10;
		rl_shell.marginLeft = 10;
		rl_shell.marginBottom = 10;
		shell.setLayout(rl_shell);
		
		Composite compTitle = new Composite(shell, SWT.NONE);
		compTitle.setLayoutData(new RowData(380, SWT.DEFAULT));
		FillLayout fl_compTitle = new FillLayout(SWT.HORIZONTAL);
		compTitle.setLayout(fl_compTitle);
		
		Label lblTitle = new Label(compTitle, SWT.NONE);
		lblTitle.setAlignment(SWT.CENTER);
		lblTitle.setFont(C.ALERT_TITLE);
		lblTitle.setText("CAUTION!");
		
		Composite compIcon = new Composite(shell, SWT.NONE);
		compIcon.setLayoutData(new RowData(380, SWT.DEFAULT));
		compIcon.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Label lblIcon = new Label(compIcon, SWT.NONE);
		lblIcon.setAlignment(SWT.CENTER);
		lblIcon.setImage(SWTResourceManager.getImage(SpaceAlert.class, "/img/alert-icon-red.png"));
		
		Composite compMsg = new Composite(shell, SWT.NONE);
		compMsg.setLayoutData(new RowData(380, SWT.DEFAULT));
		FillLayout fl_compMsg = new FillLayout(SWT.HORIZONTAL);
		fl_compMsg.marginHeight = 5;
		compMsg.setLayout(fl_compMsg);
		
		Label lblMsg = new Label(compMsg, SWT.NONE);
		lblMsg.setAlignment(SWT.CENTER);
		lblMsg.setFont(C.FONT_11);
		lblMsg.setText(C.SPACE_ALERT_RED);
		
		Composite compBtn = new Composite(shell, SWT.NONE);
		compBtn.setLayoutData(new RowData(380, 40));
		compBtn.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Button btnContinue = new Button(compBtn, SWT.NONE);
		btnContinue.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		btnContinue.setText("Continue");
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		LogController.log("Space alert closed");	
		shell.dispose();
	}
}
