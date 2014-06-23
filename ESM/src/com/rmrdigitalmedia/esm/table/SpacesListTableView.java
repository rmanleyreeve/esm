package com.rmrdigitalmedia.esm.table;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.rmrdigitalmedia.esm.AppData;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.views.SpacesListView;
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


@SuppressWarnings("unused")
public class SpacesListTableView {

	private static SpacesTable.Row[] rows;
	static TableViewerBuilder tvb;
	static TableViewer tv;
	static Table table;

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			Composite comp = new Composite(shell, SWT.BORDER);
			shell.open();
			SpacesListView.buildTable(comp);
			try {
				rows = SpacesTable.getRows("DELETED=FALSE");
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			SpacesListTableView.getTVB().setInput(Arrays.asList(rows));
			while (!shell.isDisposed()) {
				if (!Display.getDefault().readAndDispatch()) {
					Display.getDefault().sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static TableViewerBuilder getTVB() {
		return tvb;
	}
	public static TableViewer getTV() {
		return tv;
	}
	public static Table getTable() {
		return table;
	}

	// based on http://www.ralfebert.de/archive/eclipse_rcp/tableviewerbuilder/
	public static void buildTable(Composite parent) {
		LogController.log("Building Space List page");
		parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_WAIT));
		final String[] titles = { "ID", "Name", "Completion Status","Internal Classification","Entry Points Classification", "S/O" };
		tvb = new TableViewerBuilder(parent, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.VIRTUAL);		
		ColumnBuilder col;

		// ID --------------------------------------------------------------------------------------
		col = tvb.createColumn("ID");
		// set spacer image using format() to force row height
		col.format(new ICellFormatter() {
			@Override
			public void formatCell(ViewerCell c, Object value) {
				c.setImage(C.getImage("spacer_1x36.png"));
			}
		});    	
		col.bindToProperty("ID");
		col.setPixelWidth(75);
		col.useAsDefaultSortColumn();
		col.build();	

		// name --------------------------------------------------------------------------------------
		col = tvb.createColumn("Name");
		col.bindToProperty("name");
		col.setPercentWidth(30);
		col.build();	  	

		//completion status --------------------------------------------------------------------------------------
		col = tvb.createColumn("Completion Status");
		col.setPixelWidth(160);
		col.alignCenter();
		col.setCustomLabelProvider(new DynamicImageCell(new BaseValue<Row>() {
			@Override
			public Object get(Row r) {
				int id = r.getID();
				// we have the row ID so we can return the appropriate image
				int cs = AuditController.calculateOverallCompletionStatus(id);
				return "Percent_"+ cs +".png";			
			}
		}));
		col.build();

		// internal classification --------------------------------------------------------------------------------------
		col = tvb.createColumn("Internal Classification");
		col.setPixelWidth(165);
		col.alignCenter();
		//col.setCustomLabelProvider(new ImageCell(C.getImage("red.png"))); 	  	
		col.setCustomLabelProvider(new DynamicImageCell(new BaseValue<Row>() {
			@Override
			public Object get(Row r) {
				int id = r.getID();
				// we have the row ID so we can return the appropriate image
				String light = (String) EsmApplication.appData.getField("SPACE_STATUS_"+id);				
				return light+".png";
			}
		}));
		col.build();

		// entry points classification --------------------------------------------------------------------------------------
		col = tvb.createColumn("Entry Points Classification");
		col.setPixelWidth(220);
		col.alignCenter();
		col.setCustomLabelProvider(new DynamicImageArrayCell(new BaseValue<Row>() {			
			@Override
			public Object get(Row r) {
				int id = r.getID();			
				// we have the row ID so we can calculate each entrypoint classification status & return the appropriate images array
				Vector<String> epImgs = new Vector<String>();
				try {
					for (EntrypointsTable.Row eRow : EntrypointsTable.getRows("SPACE_ID", id)) {
						String epTL = (String) EsmApplication.appData.getField("ENTRY_STATUS_"+eRow.getID());
						if(epTL.equals("")) { epTL = "red"; }
						epImgs.add(epTL+".png");
					}
				} catch (SQLException ex) {}				
				return epImgs.toArray(new String[epImgs.size()]);
			}
		}));


		col.build();

		// signed off --------------------------------------------------------------------------------------
		col = tvb.createColumn("S/O");	  	
		col.setPercentWidth(5);
		col.alignCenter();
		col.setCustomLabelProvider(new DynamicImageCell(new BaseValue<Row>() {
			@Override
			public Object get(Row r) {
				int id = r.getID();
				// we have the row ID so we can return the appropriate image
				return (AuditController.isSpaceSignedOff(id)) ? "bluetick.png" : "null.png";
			}
		}));
		col.build();

		//==========================================================================

		tv = tvb.getTableViewer();
		table = tvb.getTable();
		table.setFont(C.FONT_12);

		table.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {	
				WindowController.btnViewSpaceDetails.setEnabled(true);
				if(WindowController.user.getAccessLevel()==9) {
					WindowController.btnDeleteSpace.setEnabled(true);   
				}
			}
		});

		table.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(Event e) {	
				TableItem[] selection = table.getSelection();
				String s = selection[0].getText();
				LogController.log("User clicked item " + s);
				int _id = Integer.parseInt(s);
				WindowController.checkSpaceAlert(_id);
			}
		});

		parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_ARROW));
		parent.layout();

	}

}
