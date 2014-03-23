package com.rmrdigitalmedia.esm.views;

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

import com.rmrdigitalmedia.esm.Constants;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.controllers.WindowController;
import com.rmrdigitalmedia.esm.models.SpacesTable;
import com.rmrdigitalmedia.esm.models.SpacesTable.Row;

import de.ralfebert.rcputils.properties.BaseValue;
import de.ralfebert.rcputils.tables.ColumnBuilder;
import de.ralfebert.rcputils.tables.ICellFormatter;
import de.ralfebert.rcputils.tables.TableViewerBuilder;


public class SpacesListView {
	
	private static Row[] rows;

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			Composite comp = new Composite(shell, SWT.BORDER);
			SpacesListView.buildTable(comp);
			shell.open();
			try {
				rows = SpacesTable.getAllRows();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			SpacesListView.getTVB().setInput(Arrays.asList(rows));
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
	  	
		final String[] titles = { "ID", "Name", "Completion Status","Internal Classification","Entry Points Classification", "SignedOff" };
		tvb = new TableViewerBuilder(parent, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		
		ColumnBuilder col;
		
		
		// ID --------------------------------------------------------------------------------------
		col = tvb.createColumn("ID");
		// set spacer image to force row height
	  	col.format(new ICellFormatter() {
		    @Override
			public void formatCell(ViewerCell c, Object value) {
		    	c.setImage(Constants.getImage("/img/1x36.png"));
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
	  	        int v = (r.getID() * 20);
	  	    	return "/img/Percent-"+ v +".png";
	  	    }
	  	}));
	  	col.build();
	  	
	  	// internal classification --------------------------------------------------------------------------------------
	  	col = tvb.createColumn("Internal Classification");
	  	col.setPercentWidth(15);
	  	col.alignCenter();
	  	col.setCustomLabelProvider(new ImageCell(Constants.getImage("/img/comment-edit16.png"))); 
	  	col.build();
	  	
	  	// entry points classification --------------------------------------------------------------------------------------
	  	col = tvb.createColumn("Entry Points Classification");
	  	col.bindToValue(new BaseValue<Row>() {
	  	    @Override
	  	    public Object get(Row r) {
	  	        return "TBC";
	  	    }
	  	});
	  	col.setPixelWidth(150);
	  	col.alignCenter();
	  	col.setCustomLabelProvider(new ImageCell(Constants.getImage("/img/comment-edit16.png"))); 
	  	col.build();
	  	
	  	// signed off --------------------------------------------------------------------------------------
	  	col = tvb.createColumn("Signed Off");
	  	
	  	col.setPercentWidth(10);
	  	col.alignCenter();
	  	col.setCustomLabelProvider(new ImageCell(Constants.getImage("/img/OK32.png"))); 
	  	col.build();

	  	
	  	//==========================================================================
	 	  	
	    tv = tvb.getTableViewer();
	    table = tvb.getTable();
	    table.setFont(Constants.FONT_12);
	    	    	    
	    table.addListener(SWT.Selection, new Listener() {
	        @Override
			public void handleEvent(Event e) {	
	        	WindowController.btnEditSpace.setEnabled(true);
	        	WindowController.btnDeleteSpace.setEnabled(true);        	
	        }
	    });
	    
	   table.addListener(SWT.MouseDoubleClick, new Listener() {
	        @Override
			public void handleEvent(Event e) {	
	          TableItem[] selection = table.getSelection();
	          String s = selection[0].getText();
	          LogController.log("Selection={" + s + "}");
	          int _id = Integer.parseInt(s);
	          WindowController.showSpaceDetail(_id);
	        }
	      });
  	
  	
	}


}



/*
 
//  	col.format(new ICellFormatter() {
//  	    @Override
//			public void formatCell(ViewerCell c, Object value) {
//  	    	//cell.setFont(Constants.FONT_12);
//  	    	//c.setImage(Constants.getImage("/img/Percent-40.png"));
//  	    	//c.getViewerRow().setImage(0, Constants.getImage("/img/Percent-40.png"));
//  	    	System.out.println(c.getImageBounds());
//  	    }
//  	});    	

*/
