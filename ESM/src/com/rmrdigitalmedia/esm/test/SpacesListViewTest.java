package com.rmrdigitalmedia.esm.test;

import java.sql.SQLException;
import java.util.Arrays;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.table.DynamicImageArrayCell;
import com.rmrdigitalmedia.esm.table.DynamicImageCell;
import com.rmrdigitalmedia.esm.table.ImageCell;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.models.SpacesTable;
import com.rmrdigitalmedia.esm.models.SpacesTable.Row;

import de.ralfebert.rcputils.properties.BaseValue;
import de.ralfebert.rcputils.tables.ColumnBuilder;
import de.ralfebert.rcputils.tables.ICellFormatter;
import de.ralfebert.rcputils.tables.TableViewerBuilder;


public class SpacesListViewTest {

	private static Row[] rows;

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			Composite comp = new Composite(shell, SWT.BORDER);
			SpacesListViewTest.buildTable(comp);
			shell.open();
			try {
				rows = SpacesTable.getRows("DELETED=FALSE");
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			SpacesListViewTest.getTVB().setInput(Arrays.asList(rows));
			while (!shell.isDisposed()) {
				if (!Display.getDefault().readAndDispatch()) {
					Display.getDefault().sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	static TableViewerBuilder tvb;
	static TableViewer tv;
	static Table table;

	public static TableViewerBuilder getTVB() {
		return tvb;
	}
	public static TableViewer getTV() {
		return tv;
	}
	public static Table getTable() {
		return table;
	}

	@SuppressWarnings("unused")
	public static void buildTable(Composite parent) {

		// based on http://www.ralfebert.de/archive/eclipse_rcp/tableviewerbuilder/

		final String[] titles = { "ID", "Name", "Completion Status","Internal Classification","Entry Points Classification", "S/O" };
		tvb = new TableViewerBuilder(parent, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);		
		ColumnBuilder col;		

		// ID --------------------------------------------------------------------------------------
		col = tvb.createColumn("ID");
		// set spacer image using format() to force row height
		col.format(new ICellFormatter() {
			@Override
			public void formatCell(ViewerCell c, Object value) {
				c.setImage(C.getImage("/img/1x36.png"));
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
				// we have the row ID so we can perform the completion status calculations here
				// get the % value and return the appropriate image
				return "/img/Percent_"+ (id*20) +".png";
			}
		}));
		col.build();

		// internal classification --------------------------------------------------------------------------------------
		col = tvb.createColumn("Internal Classification");
		col.setPixelWidth(165);
		col.alignCenter();
		//col.setCustomLabelProvider(new ImageCell(C.getImage("/img/red.png"))); 	  	
		col.setCustomLabelProvider(new DynamicImageCell(new BaseValue<Row>() {
			@Override
			public Object get(Row r) {
				int id = r.getID();
				// we have the row ID so we can return the appropriate image
				String light = (String) EsmApplication.appData.getField("SPACE_STATUS_"+id);				
				return "/img/"+light+".png";
			}
		}));
		col.build();

		// entry points classification --------------------------------------------------------------------------------------
		col = tvb.createColumn("Entry Points Classification");
		col.setPixelWidth(180);
		col.alignCenter();
		col.setCustomLabelProvider(new DynamicImageArrayCell(new BaseValue<Row>() {
			@Override
			public Object get(Row r) {
				int id = r.getID();
				// we have the row ID so we can perform the entrypoint audit calculations here
				// get the statuses and return the appropriate images as an array
				return new String[] {"/img/green.png","/img/amber.png","/img/red.png","/img/green.png","/img/amber.png"};
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
				// we have the row ID so we can work out the signoff status calculations here
				// get the value and return the appropriate image
				return "/img/bluetick.png";
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


	}

}
