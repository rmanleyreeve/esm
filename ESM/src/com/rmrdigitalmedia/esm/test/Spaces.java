package com.rmrdigitalmedia.esm.test;

import java.sql.SQLException;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.models.SpacesTable;

public class Spaces {
		
  public class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
    public String getColumnText(Object element, int columnIndex) {
      SpacesTable.Row r = (SpacesTable.Row) element;
      String result = "";
      switch(columnIndex){
  			case 0:
  				result = r.getID()+"";
  				break;
  			case 1:
  				result = r.getName();
  				break;
  			default:
  				//should not reach here
  				result = "";
  			}
  			return result;      
  		}

		@Override
		public Image getColumnImage(Object arg0, int arg1) {
			// TODO Auto-generated method stub
			return null;
		}
  }

  static SpacesTable.Row[] rows = null;
	public static SpacesTable.Row[] getRows() {
		return rows;
	}
	public static void setRows(SpacesTable.Row[] rows) {
		Spaces.rows = rows;
	}

	private static TableViewer viewer;
	public static String[] titles = { "ID", "Name", "Completion Status","Internal Classification","Entry Points Classification", "SignedOff" };
	public static Table table;
	public static Table getTable() {
		return table;
	}		
		
  static void createViewer(Composite parent) {
  	parent.setLayout(new GridLayout());

  	TableColumnLayout tableColumnLayout = new TableColumnLayout();
  	parent.setLayout(tableColumnLayout);
    viewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER | SWT.HIDE_SELECTION);
    
    viewer.setContentProvider(new ArrayContentProvider());
    try {
    	rows = SpacesTable.getAllRows();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}        
    viewer.setInput(rows);
  
    TableViewerColumn column;
    TableColumn col;
    //ID
    column = new TableViewerColumn(viewer, SWT.NONE);
  	col = column.getColumn();
    col.setText("ID");
    viewer.getTable().setSortColumn(col);
		viewer.getTable().setSortDirection(SWT.UP);
    tableColumnLayout.setColumnData(col, new ColumnPixelData(30));
    col.setResizable(false);
    col.setMoveable(false);
    //name
    column = new TableViewerColumn(viewer, SWT.NONE);
  	col = column.getColumn();
    col.setText("Name");
    tableColumnLayout.setColumnData(col, new ColumnWeightData(40));
    col.setResizable(false);
    col.setMoveable(false);
    // completion status
    column = new TableViewerColumn(viewer, SWT.NONE);
  	col = column.getColumn();
    col.setText("Completion Status");
    tableColumnLayout.setColumnData(col, new ColumnWeightData(10));
    col.setResizable(false);
    col.setMoveable(false);
    // internal classification
    column = new TableViewerColumn(viewer, SWT.NONE);
  	col = column.getColumn();
    col.setText("Internal Classification");
    tableColumnLayout.setColumnData(col, new ColumnWeightData(10));
    col.setResizable(false);
    col.setMoveable(false);
    
    TableLabelProvider tlp = new Spaces().new TableLabelProvider();
    viewer.setLabelProvider(tlp);

    final Table table = viewer.getTable();
    table.setHeaderVisible(true);
    table.setLinesVisible(true);
    LogController.log(table.getColumnCount());
    table.setFont(C.FONT_12);

  	table.addListener(SWT.MouseDoubleClick, new Listener() {
  		@Override
  		public void handleEvent(Event event) {
  			Point pt = new Point(event.x, event.y);
  			TableItem item = table.getItem(pt);
  			if (item == null)
  				return;
  			for (int i = 0; i < titles.length; i++) {
  				Rectangle rect = item.getBounds(i);
  				if (rect.contains(pt)) {
  					int index = table.indexOf(item);
  					int _id = rows[index].getID();
  					LogController.log(_id + ":" + rows[index].getName());
  					//WindowController.pageSpaceDetail(_id);  				
  				}
  			}
  		}
  	});
    // define layout for the viewer    
    GridData gridData = new GridData();
    gridData.verticalAlignment = GridData.FILL;
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = false;
    gridData.horizontalAlignment = GridData.FILL;
    //viewer.getControl().setLayoutData(gridData);
    
    //viewer.getControl().setFocus();
  }


}
