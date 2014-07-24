package com.rmrdigitalmedia.esm.views;

import java.sql.SQLException;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.rmrdigitalmedia.esm.AppData;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.AuditController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.models.EntrypointsTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;


@SuppressWarnings("unused")
public class SpacesListView {

	private static SpacesTable.Row[] sRows;
	private static Label sep;
	private static int rowHeight = 35;
	private static int colHeaderH = 40;
	private static int imgStatusW = 160;
	static HashMap<Integer,CLabel[]> tableRows = new HashMap<Integer,CLabel[]>();
	static int selectedRow = 0;

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			shell.setSize(1380, 750);
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			Composite comp = new Composite(shell, SWT.NONE);
			shell.open();
			SpacesListView.buildTable(comp);
			while (!shell.isDisposed()) {
				if (!Display.getDefault().readAndDispatch()) {
					Display.getDefault().sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	public static void buildTable(Composite parent) {
		LogController.log("Building Space List page");
		// clean up
		for (Control c:parent.getChildren()) {
			c.dispose();
		}
		tableRows.clear();
		selectedRow = 0;
		
		parent.setLayout(new FillLayout(SWT.VERTICAL));

		// scrolling frame to hold the grid panel
		final ScrolledComposite scrollPanel = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.BORDER);
		scrollPanel.setExpandHorizontal(true);

		// the grid panel that holds the various info rows
		final Composite comp = new Composite(scrollPanel, SWT.NONE);
		GridLayout gl_comp = new GridLayout(1, true);
		comp.setLayout(gl_comp);
		comp.setBackground(C.APP_BGCOLOR);

		//table layout
		final Composite tbl = new Composite(comp, SWT.NONE);
		tbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gl_tbl = new GridLayout(6, false);
		gl_tbl.marginTop = 0;
		gl_tbl.marginHeight = 0;
		gl_tbl.marginWidth = 0;
		gl_tbl.verticalSpacing = 1;
		gl_tbl.horizontalSpacing = 1;
		tbl.setLayout(gl_tbl);
		tbl.setBackground(C.APP_BGCOLOR);

		// column headers
		// final String[] titles = { "ID", "Name", "Completion Status","Internal Classification","Entry Points Classification", "S/O" };

		// ID HEADER
		CLabel lblHeaderID = new CLabel(tbl, SWT.NONE);
		GridData gd_lblHeaderID = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		lblHeaderID.setLeftMargin(5);
		gd_lblHeaderID.widthHint = 75;
		gd_lblHeaderID.heightHint = colHeaderH;
		lblHeaderID.setLayoutData(gd_lblHeaderID);
		lblHeaderID.setBackground(C.BAR_BGCOLOR);
		lblHeaderID.setFont(C.FONT_12B);
		lblHeaderID.setText("ID");
		// NAME
		CLabel lblHeaderName = new CLabel(tbl, SWT.NONE);
		lblHeaderName.setLeftMargin(5);
		GridData gd_lblHeaderName = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_lblHeaderName.minimumWidth = 200;
		gd_lblHeaderName.widthHint = 200;
		gd_lblHeaderName.heightHint = colHeaderH;
		lblHeaderName.setLayoutData(gd_lblHeaderName);
		lblHeaderName.setBackground(C.BAR_BGCOLOR);
		lblHeaderName.setFont(C.FONT_12B);
		lblHeaderName.setText("Name");
		// COMPLETION STATUS
		CLabel lblHeaderCS = new CLabel(tbl, SWT.NONE | SWT.CENTER);
		lblHeaderCS.setLeftMargin(5);
		GridData gd_lblHeaderCS = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblHeaderCS.widthHint = 160;
		gd_lblHeaderCS.heightHint = colHeaderH;
		lblHeaderCS.setLayoutData(gd_lblHeaderCS);
		lblHeaderCS.setBackground(C.BAR_BGCOLOR);
		lblHeaderCS.setFont(C.FONT_12B);
		lblHeaderCS.setText("Completion Status");
		// INTERNAL CLASSIFICATION
		CLabel lblHeaderIC = new CLabel(tbl, SWT.NONE | SWT.CENTER);
		lblHeaderIC.setLeftMargin(5);
		GridData gd_lblHeaderIC = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblHeaderIC.widthHint = 220;
		gd_lblHeaderIC.heightHint = colHeaderH;
		lblHeaderIC.setLayoutData(gd_lblHeaderIC);
		lblHeaderIC.setBackground(C.BAR_BGCOLOR);
		lblHeaderIC.setText("Internal Classification");
		lblHeaderIC.setFont(C.FONT_12B);
		// ENTRY POINTS CLASSIFICATION
		CLabel lblHeaderEPC = new CLabel(tbl, SWT.NONE);
		lblHeaderEPC.setLeftMargin(5);
		lblHeaderEPC.setAlignment(SWT.CENTER);
		GridData gd_lblHeaderEPC = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblHeaderEPC.widthHint = 220;
		gd_lblHeaderEPC.heightHint = colHeaderH;
		lblHeaderEPC.setLayoutData(gd_lblHeaderEPC);
		lblHeaderEPC.setBackground(C.BAR_BGCOLOR);
		lblHeaderEPC.setText("Entry Points Classification");
		lblHeaderEPC.setFont(C.FONT_12B);
		// SIGNED OFF
		CLabel lblHeaderSO = new CLabel(tbl, SWT.NONE);
		lblHeaderSO.setRightMargin(5);
		lblHeaderSO.setAlignment(SWT.CENTER);
		GridData gd_lblHeaderSO = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblHeaderSO.widthHint = 75;
		gd_lblHeaderSO.heightHint = colHeaderH;
		lblHeaderSO.setLayoutData(gd_lblHeaderSO);
		lblHeaderSO.setBackground(C.BAR_BGCOLOR);
		lblHeaderSO.setText("S/O");
		lblHeaderSO.setFont(C.FONT_12B);

		// get spaces from DB
		try {
			sRows = SpacesTable.getRows("DELETED=FALSE");
		} catch (SQLException ex) {
			LogController.logEvent(SpacesListView.class, C.ERROR, "Error loading spaces from DB",ex);		
		}
		// start loop through spaces rows ==============================================
		for (SpacesTable.Row sRow : sRows) {
			final int spaceID = sRow.getID();
			// ID
			final CLabel lblLoopID = new CLabel(tbl, SWT.NONE);
			lblLoopID.setLeftMargin(5);
			GridData gd_lblLoopID = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
			gd_lblLoopID.widthHint = 75;
			gd_lblLoopID.heightHint = colHeaderH;
			lblLoopID.setLayoutData(gd_lblLoopID);
			lblLoopID.setBackground(C.FIELD_BGCOLOR);
			lblLoopID.setFont(C.FONT_11B);
			lblLoopID.setText(""+spaceID);
			// NAME
			final CLabel lblLoopName = new CLabel(tbl, SWT.NONE);
			lblLoopName.setLeftMargin(5);
			GridData gd_lblLoopName = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			gd_lblLoopName.minimumWidth = 200;
			gd_lblLoopName.widthHint = 200;
			gd_lblLoopName.heightHint = colHeaderH;
			lblLoopName.setLayoutData(gd_lblLoopName);
			lblLoopName.setBackground(C.FIELD_BGCOLOR);
			lblLoopName.setFont(C.FONT_11B);
			//lblLoopName.setToolTipText("Double-click to view details of this enclosed space");
			lblLoopName.setText(sRow.getName());
			// COMPLETION STATUS
			final CLabel lblLoopCS = new CLabel(tbl, SWT.NONE | SWT.CENTER);
			lblLoopCS.setLeftMargin(5);
			GridData gd_lblLoopCS = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
			gd_lblLoopCS.widthHint = 160;
			gd_lblLoopCS.heightHint = colHeaderH;
			lblLoopCS.setLayoutData(gd_lblLoopCS);
			lblLoopCS.setBackground(C.FIELD_BGCOLOR);
			int cs = AuditController.calculateOverallCompletionStatus(spaceID);
			lblLoopCS.setImage(C.getImage("Percent_"+ cs +".png"));
			// INTERNAL CLASSIFICATION
			final CLabel lblLoopIC = new CLabel(tbl, SWT.NONE | SWT.CENTER);
			lblLoopIC.setLeftMargin(5);
			GridData gd_lblLoopIC = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
			gd_lblLoopIC.widthHint = 220;
			gd_lblLoopIC.heightHint = colHeaderH;
			lblLoopIC.setLayoutData(gd_lblLoopIC);
			lblLoopIC.setBackground(C.FIELD_BGCOLOR);
			String light = (String) EsmApplication.appData.getField("SPACE_STATUS_"+spaceID);
			if(light.equals("")) { light = "null"; }
			lblLoopIC.setImage(C.getImage(light+".png"));
			// ENTRY POINTS CLASSIFICATION
			final CLabel lblLoopEPC = new CLabel(tbl, SWT.NONE | SWT.CENTER);
			lblLoopEPC.setLeftMargin(5);
			GridData gd_lblLoopEPC = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
			gd_lblLoopEPC.widthHint = 220;
			gd_lblLoopEPC.heightHint = colHeaderH;
			lblLoopEPC.setLayoutData(gd_lblLoopEPC);
			lblLoopEPC.setBackground(C.FIELD_BGCOLOR);
			// build transparent image array
			ImageData imgData = new ImageData(new AppData().getClass().getResourceAsStream("/img/blank.jpg"));		
			int whitePixel = imgData.palette.getPixel(new RGB(255,255,255));
			imgData.transparentPixel = whitePixel;
			Image img = new Image(Display.getDefault(),imgData);
			GC gc = new GC(img);
			gc.setAntialias(SWT.ON);
			gc.setBackground(C.FIELD_BGCOLOR);
			EntrypointsTable.Row[] eRows;
			try {
				eRows = EntrypointsTable.getRows("DELETED=FALSE AND SPACE_ID="+spaceID);
				int centreX = (imgStatusW/2) - 10;
				int x=0;
				switch (eRows.length) {
					case 1:
						x = centreX;
						break;
					case 2:
						x = (centreX - 20);
						break;
					case 3:
						x = (centreX - 30);
						break;
					case 4:
						x = (centreX - 40);
						break;
					case 5:
						x = (centreX - 50);
						break;
				}
				for (EntrypointsTable.Row eRow : eRows) {
					String epTL = (String) EsmApplication.appData.getField("ENTRY_STATUS_"+eRow.getID());
					if(epTL.equals("")) { epTL = "red"; }
					gc.drawImage(C.getImage(epTL+".png"), x, 0);
					//gc.setBackground(C.TRAFFICLIGHTS.get(epTL));
					//gc.fillOval(x, 0, 18, 18);
					x += 25;
				}
			} catch (SQLException ex) {
				LogController.logEvent(SpacesListView.class, C.ERROR, "Error loading entry points from DB",ex);		
			}
			lblLoopEPC.setImage(img);	
			// SIGNED OFF
			final CLabel lblLoopSO = new CLabel(tbl, SWT.NONE);
			lblLoopSO.setRightMargin(5);
			lblLoopSO.setAlignment(SWT.CENTER);
			GridData gd_lblLoopSO = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
			gd_lblLoopSO.widthHint = 75;
			gd_lblLoopSO.heightHint = colHeaderH;
			lblLoopSO.setLayoutData(gd_lblLoopSO);
			lblLoopSO.setBackground(C.FIELD_BGCOLOR);
			lblLoopSO.setImage( (AuditController.isSpaceSignedOff(spaceID)) ? C.getImage("bluetick.png") : C.getImage("null.png") );

			CLabel[] tableRow = { lblLoopID,lblLoopName,lblLoopCS,lblLoopIC,lblLoopEPC,lblLoopSO };
			tableRows.put(spaceID, tableRow);

			// table row hover behaviour
			lblLoopName.addMouseTrackListener(new MouseTrackAdapter() {
				@Override
				public void mouseEnter(MouseEvent arg0) {
					for (CLabel l : tableRows.get(spaceID)) {
						if(spaceID != selectedRow) {
							l.setBackground(C.ROW_HIGHLIGHT);
						}
					}
				}
				@Override
				public void mouseExit(MouseEvent arg0) {
					for (CLabel l : tableRows.get(spaceID)) {
						if(spaceID != selectedRow) {
							l.setBackground(C.FIELD_BGCOLOR);
						}
					}
				}
			});
			// click behaviour
			lblLoopName.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseUp(MouseEvent arg0) {
					selectedRow = (selectedRow != spaceID) ? spaceID : 0;
					for (int key : tableRows.keySet()) {
						if(key == selectedRow) {
							LogController.log("Space " + spaceID + " selected");
							for (CLabel l : tableRows.get(key)) {
								// SELECT THIS ROW
								l.setBackground(C.ROW_SELECTED);
								l.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
								WindowController.currentSpaceId = spaceID;
								WindowController.btnViewSpaceDetails.setEnabled(true);
								if(WindowController.user.getAccessLevel()==9) {
									WindowController.btnDeleteSpace.setEnabled(true);   
								}
							}
						} else {
							for (CLabel l : tableRows.get(key)) {
								l.setBackground(C.FIELD_BGCOLOR);
								l.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
								if(selectedRow==0) {
									WindowController.currentSpaceId = 0;
									WindowController.btnViewSpaceDetails.setEnabled(false);
									WindowController.btnDeleteSpace.setEnabled(false);  
								}
							}
						}
					} // end for keyset
				}
			});
			// double click behaviour
			lblLoopName.addListener(SWT.MouseDoubleClick, new Listener() {
				@Override
				public void handleEvent(Event e) {
					WindowController.currentSpaceId = spaceID;
					LogController.log("User clicked item " + spaceID);
					WindowController.checkSpaceAlert(spaceID);
				}
			});

		}
		// end loop


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
