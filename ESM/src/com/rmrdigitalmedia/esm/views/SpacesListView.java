package com.rmrdigitalmedia.esm.views;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import com.rmrdigitalmedia.esm.AppData;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.table.DynamicImageArrayCell;
import com.rmrdigitalmedia.esm.table.DynamicImageCell;
import com.rmrdigitalmedia.esm.controllers.AuditController;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.models.EntrypointsTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;
import com.rmrdigitalmedia.esm.models.SpacesTable.Row;
import de.ralfebert.rcputils.properties.BaseValue;
import de.ralfebert.rcputils.tables.ColumnBuilder;
import de.ralfebert.rcputils.tables.ICellFormatter;
import de.ralfebert.rcputils.tables.TableViewerBuilder;
import org.eclipse.swt.layout.RowLayout;


@SuppressWarnings("unused")
public class SpacesListView {

	private static SpacesTable.Row[] rows;
	private static Label sep;
	private static int rowHeight = 35;
	private static int colHeaderH = 40;
	
	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			shell.setSize(1380, 750);
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			Composite comp = new Composite(shell, SWT.BORDER);
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
		parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_WAIT));
		
		for (Control c:parent.getChildren()) {
			c.dispose();
		}
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
		final Group tbl = new Group(comp, SWT.BORDER);
		tbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gl_tbl = new GridLayout(6, false);
		gl_tbl.marginTop = -15;
		gl_tbl.marginHeight = 0;
		gl_tbl.marginWidth = 0;
		gl_tbl.verticalSpacing = 1;
		gl_tbl.horizontalSpacing = 1;
		tbl.setLayout(gl_tbl);
		tbl.setBackground(C.APP_BGCOLOR);
		
		// column headers
		// final String[] titles = { "ID", "Name", "Completion Status","Internal Classification","Entry Points Classification", "S/O" };

		CLabel lblHeaderID = new CLabel(tbl, SWT.BORDER);
		GridData gd_lblHeaderID = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblHeaderID.widthHint = 75;
		gd_lblHeaderID.heightHint = colHeaderH;
		lblHeaderID.setLayoutData(gd_lblHeaderID);
		lblHeaderID.setBackground(C.BAR_BGCOLOR);
		lblHeaderID.setFont(C.FONT_12B);
		lblHeaderID.setText("ID");
		
		CLabel lblHeaderName = new CLabel(tbl, SWT.BORDER);
		GridData gd_lblHeaderName = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_lblHeaderName.minimumWidth = 200;
		gd_lblHeaderName.widthHint = 200;
		gd_lblHeaderName.heightHint = colHeaderH;
		lblHeaderName.setLayoutData(gd_lblHeaderName);
		lblHeaderName.setBackground(C.BAR_BGCOLOR);
		lblHeaderName.setFont(C.FONT_12B);
		lblHeaderName.setText("Name");
		
		CLabel lblHeaderCS = new CLabel(tbl, SWT.BORDER | SWT.CENTER);
		GridData gd_lblHeaderCS = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblHeaderCS.widthHint = 160;
		gd_lblHeaderCS.heightHint = colHeaderH;
		lblHeaderCS.setLayoutData(gd_lblHeaderCS);
		lblHeaderCS.setBackground(C.BAR_BGCOLOR);
		lblHeaderCS.setFont(C.FONT_12B);
		lblHeaderCS.setText("Completion Status");
		
		CLabel lblHeaderIC = new CLabel(tbl, SWT.BORDER | SWT.CENTER);
		GridData gd_lblHeaderIC = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblHeaderIC.widthHint = 220;
		gd_lblHeaderIC.heightHint = colHeaderH;
		lblHeaderIC.setLayoutData(gd_lblHeaderIC);
		lblHeaderIC.setBackground(C.BAR_BGCOLOR);
		lblHeaderIC.setText("Internal Classification");
		lblHeaderIC.setFont(C.FONT_12B);
		
		CLabel lblHeaderEPC = new CLabel(tbl, SWT.BORDER);
		lblHeaderEPC.setAlignment(SWT.CENTER);
		GridData gd_lblHeaderEPC = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblHeaderEPC.widthHint = 220;
		gd_lblHeaderEPC.heightHint = colHeaderH;
		lblHeaderEPC.setLayoutData(gd_lblHeaderEPC);
		lblHeaderEPC.setBackground(C.BAR_BGCOLOR);
		lblHeaderEPC.setText("Entry Points Classification");
		lblHeaderEPC.setFont(C.FONT_12B);
		
		CLabel lblHeaderSO = new CLabel(tbl, SWT.BORDER);
		lblHeaderSO.setAlignment(SWT.CENTER);
		GridData gd_lblHeaderSO = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblHeaderSO.widthHint = 75;
		gd_lblHeaderSO.heightHint = colHeaderH;
		lblHeaderSO.setLayoutData(gd_lblHeaderSO);
		lblHeaderSO.setBackground(C.BAR_BGCOLOR);
		lblHeaderSO.setText("S/O");
		lblHeaderSO.setFont(C.FONT_12B);
		
		// start loop through spaces rows
		CLabel lblLoopID = new CLabel(tbl, SWT.BORDER);
		GridData gd_lblLoopID = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblLoopID.widthHint = 75;
		gd_lblLoopID.heightHint = colHeaderH;
		lblLoopID.setLayoutData(gd_lblLoopID);
		lblLoopID.setBackground(C.FIELD_BGCOLOR);
		lblLoopID.setFont(C.FONT_11);
		lblLoopID.setText("001");
		
		CLabel lblLoopName = new CLabel(tbl, SWT.BORDER);
		GridData gd_lblLoopName = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_lblLoopName.minimumWidth = 200;
		gd_lblLoopName.widthHint = 200;
		gd_lblLoopName.heightHint = colHeaderH;
		lblLoopName.setLayoutData(gd_lblLoopName);
		lblLoopName.setBackground(C.FIELD_BGCOLOR);
		lblLoopName.setFont(C.FONT_11);
		lblLoopName.setText("Forepeak Tank");
		
		CLabel lblLoopCS = new CLabel(tbl, SWT.BORDER | SWT.CENTER);
		GridData gd_lblLoopCS = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblLoopCS.widthHint = 160;
		gd_lblLoopCS.heightHint = colHeaderH;
		lblLoopCS.setLayoutData(gd_lblLoopCS);
		lblLoopCS.setBackground(C.FIELD_BGCOLOR);
		lblLoopCS.setImage(C.getImage("Percent_20.png"));
		
		CLabel lblLoopIC = new CLabel(tbl, SWT.BORDER | SWT.CENTER);
		GridData gd_lblLoopIC = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblLoopIC.widthHint = 220;
		gd_lblLoopIC.heightHint = colHeaderH;
		lblLoopIC.setLayoutData(gd_lblLoopIC);
		lblLoopIC.setBackground(C.FIELD_BGCOLOR);
		lblLoopIC.setImage(C.getImage("red.png"));
		
		CLabel lblLoopEPC = new CLabel(tbl, SWT.BORDER | SWT.CENTER);
		GridData gd_lblLoopEPC = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblLoopEPC.widthHint = 220;
		gd_lblLoopEPC.heightHint = colHeaderH;
		lblLoopEPC.setLayoutData(gd_lblLoopEPC);
		lblLoopEPC.setBackground(C.FIELD_BGCOLOR);
		
		Image img = new Image(Display.getDefault(),120,22);
		
		GC gc = new GC(img);
		gc.setBackground(C.FIELD_BGCOLOR);
		gc.drawImage(C.getImage("red.png"), 0, 0);
		gc.drawImage(C.getImage("amber.png"), 25, 0);
				
		lblLoopEPC.setImage(img);	
		
		CLabel lblLoopSO = new CLabel(tbl, SWT.BORDER);
		lblLoopSO.setAlignment(SWT.CENTER);
		GridData gd_lblLoopSO = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblLoopSO.widthHint = 75;
		gd_lblLoopSO.heightHint = colHeaderH;
		lblLoopSO.setLayoutData(gd_lblLoopSO);
		lblLoopSO.setBackground(C.FIELD_BGCOLOR);
		lblLoopSO.setImage(C.getImage("bluetick.png"));
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
		parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_ARROW));
	}
}
