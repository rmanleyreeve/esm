package com.rmrdigitalmedia.esm.controllers;

import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import com.rmrdigitalmedia.esm.Constants;
import com.rmrdigitalmedia.esm.models.EntrypointCommentsTable;
import com.rmrdigitalmedia.esm.models.EntrypointsTable;
import com.rmrdigitalmedia.esm.models.SpaceCommentsTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;

@SuppressWarnings("unused")
public class DeleteSpaceController {
	
	private FormData fd_lblAProgramUpdate;
	int spaceID;
	boolean formOK = false;
	
	
	public static void main (String [] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			DeleteSpaceController dsc = new DeleteSpaceController();
			dsc.deleteOK(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DeleteSpaceController() {
	  	LogController.log("Running class " + this.getClass().getName());		
	}
	
	
	public boolean deleteOK(int _id) {
		this.spaceID = _id;
		Display display = Display.getDefault();
		final Shell dialog = new Shell (display,SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.ON_TOP);
		dialog.setSize(250, 130);
		dialog.setText("ESM Alert");
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10;
		formLayout.spacing = 10;
		dialog.setLayout (formLayout);

		Label lblAProgramUpdate = new Label (dialog, SWT.NONE);
		lblAProgramUpdate.setFont(Constants.FONT_10);
		lblAProgramUpdate.setText ("Are you sure?");
		FormData data;
		fd_lblAProgramUpdate = new FormData ();
		lblAProgramUpdate.setLayoutData (fd_lblAProgramUpdate);

		Button cancel = new Button (dialog, SWT.PUSH);
		cancel.setFont(Constants.FONT_10);
		cancel.setText ("Cancel");
		data = new FormData ();
		data.width = 60;
		data.top = new FormAttachment(lblAProgramUpdate);
		data.right = new FormAttachment(100);
		data.bottom = new FormAttachment(100, 0);
		cancel.setLayoutData(data);
		cancel.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				LogController.log("User cancelled delete dialog");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {}
				dialog.close ();
			}
		});		

		Button ok = new Button (dialog, SWT.PUSH);
		ok.setFont(Constants.FONT_10);
		ok.setText ("OK");
		data = new FormData ();
		data.width = 60;
		data.right = new FormAttachment(cancel);
		data.bottom = new FormAttachment(100, 0);
		ok.setLayoutData(data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				try {
					/*
					// THE DESTRUCTIVE WAY
					for(EntrypointsTable.Row entryPoint:EntrypointsTable.getRows("SPACE_ID="+spaceID+" AND DELETED=FALSE")) {
						for(EntrypointCommentsTable.Row entryComment:EntrypointCommentsTable.getRows("ENTRYPOINT_ID="+entryPoint.getID()+" AND DELETED=FALSE")) {
							LogController.log("Deleted entrypoint comment " + entryComment.getID());
							//entryComment.delete();
						}
						LogController.log("Deleted entrypoint " + entryPoint.getID());
						//entryPoint.delete();
					}
					for (SpaceCommentsTable.Row spaceComment:SpaceCommentsTable.getRows("SPACE_ID="+spaceID+" AND DELETED=FALSE")) {
						LogController.log("Deleted space comment " + spaceComment.getID());
						//spaceComment.delete();						
					}
					SpacesTable.Row space = SpacesTable.getRow(spaceID);
					LogController.log("Deleted space " + spaceID);
					//space.delete();					
					formOK = true;
					*/
					//NON-DESTRUCTIVE
					SpacesTable.Row space = SpacesTable.getRow(spaceID);
					space.setDeleted("TRUE");
					space.update();
					LogController.log("Marked space " + spaceID + " as deleted");
					formOK = true;
				} catch (SQLException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}				
				dialog.close();
			}
		});
		dialog.setDefaultButton (cancel);
		dialog.pack ();
		Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		Rectangle rect = dialog.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		dialog.setLocation (x, y);		  		
		dialog.open ();
		
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		LogController.log("Delete dialog closed");
		return formOK;
	}

	
	
	
	
	

}
