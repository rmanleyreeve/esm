package com.rmrdigitalmedia.esm.test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.rmrdigitalmedia.esm.Constants;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;
import com.rmrdigitalmedia.esm.models.SpacesTable.Row;

import de.ralfebert.rcputils.properties.BaseValue;
import de.ralfebert.rcputils.properties.PropertyValue;
import de.ralfebert.rcputils.tables.ColumnBuilder;
import de.ralfebert.rcputils.tables.ICellFormatter;
import de.ralfebert.rcputils.tables.TableViewerBuilder;

//http://www.eclipse.org/swt/snippets/

@SuppressWarnings("unused")
public class AppWindowController {

	// layout items
	protected Shell shell;
	Display display;
	EsmUsersTable.Row user;
	Composite container, header, titleBar;
	static Composite formHolder;
	Composite page0;
	static Composite page1;
	Composite page2;
	static Label lblPage;
	String displayName;
	Button b0, b1, b2;
	SpacesTable.Row[] rows;
	
	static StackLayout slayout;
	Composite[] pages;
	int appHeight, appWidth;
	private static int currentSpaceId = 0;

	// layout constants
	int headerH = 85;
	int titleH = 40;
	int footerH = 20;

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			AppWindowController window = new AppWindowController(EsmUsersTable.getRow("USERNAME", "admin"));
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AppWindowController(EsmUsersTable.Row user) {
		this.user = user;
		this.displayName = user.getRank() + " " + user.getForename() + " " + user.getSurname();
	  	LogController.log("Running class " + this.getClass().getName());
	  	LogController.log("Logged in user: " + displayName);
	}

	public void open() {
		// set up main window
		display = Display.getDefault();
		shell = new Shell();
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setText(Constants.APP_NAME);
		shell.setLayout(new FillLayout(SWT.VERTICAL));		
		// size & position
		Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		//shell.setSize(800,600);
		shell.setSize(bounds.width-300, bounds.height-200); // almost fill screen
		Rectangle rect = shell.getBounds ();
		appWidth = rect.width;
		appHeight = rect.height;
		LogController.log("Size: " + appWidth + "x" + appHeight);
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation (x, y);		
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
		LogController.log(Constants.EXIT_MSG);
	}

	protected void createContents() {
		
		// set up container layout
		container = new Composite(shell,SWT.NONE);
		container.setBackground(Constants.APP_BGCOLOR);		
		FormLayout layout = new FormLayout();
		container.setLayout (layout);
		
		// set up row elements ================================================
		header = new Composite(container,SWT.NONE);
		header.setBackground(Constants.APP_BGCOLOR);
		header.setLayout(new FormLayout());
		
		titleBar = new Composite(container,SWT.NONE);
		titleBar.setBackground(Constants.TITLEBAR_BGCOLOR);
		titleBar.setLayout(new FormLayout());	
		
		lblPage = new Label(titleBar, SWT.NONE);
		lblPage.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		lblPage.setBackground(Constants.TITLEBAR_BGCOLOR);
		lblPage.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		formHolder = new Composite(container,SWT.NONE);
		formHolder.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		FormData fd_formHolder = new FormData();
		fd_formHolder.left = new FormAttachment(0);
		fd_formHolder.top = new FormAttachment(titleBar,0);
		fd_formHolder.right = new FormAttachment(100);
		fd_formHolder.bottom = new FormAttachment(100);
		formHolder.setLayoutData(fd_formHolder);
		slayout = new StackLayout ();
		formHolder.setLayout(slayout);
		
		//===page contents here==================================================================
		
		// page 0==========		
		page0 = new Composite (formHolder, SWT.NONE);
					
		
		//http://www.ralfebert.de/archive/eclipse_rcp/tableviewerbuilder/
		final String[] titles = { "ID", "Name", "Completion Status","Internal Classification","Entry Points Classification", "SignedOff" };
		int[] colWidths = {30,400,200,200,200,100};
		TableViewerBuilder tvb = new TableViewerBuilder(page0, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		
		try {
			rows = SpacesTable.getAllRows();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		ColumnBuilder col;
		// ID
		col = tvb.createColumn("ID");
  	col.bindToProperty("ID");
  	col.setPixelWidth(75);
  	col.useAsDefaultSortColumn();
  	//col.sortBy(new PropertyValue("ID"));
   	col.format(new ICellFormatter() {
  	    @Override
		public void formatCell(ViewerCell cell, Object value) {
  	    	//cell.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
  	    }
  	});    	
  	col.build();	
  	// name
  	col = tvb.createColumn("Name");
  	col.bindToProperty("name");
  	col.setPercentWidth(30);
  	col.format(new ICellFormatter() {
  	    @Override
		public void formatCell(ViewerCell cell, Object value) {
  	    	//cell.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
  	    	//cell.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
  	    	//cell.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
  	    }
  	});    	
  	col.build();
  	// completion status
  	col = tvb.createColumn("Completion Status");
  	col.bindToValue(new BaseValue<Row>() {
  	    @Override
  	    public Object get(Row r) {
  	    	int id = r.getID();
  	        return "TBC " + id;
  	    }
  	});
  	col.setPercentWidth(15);
  	col.alignCenter();
  	col.format(new ICellFormatter() {
  	    @Override
		public void formatCell(ViewerCell cell, Object value) {
  	    	//cell.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
  	    }
  	});    	
  	col.build();
  	// internal classification
  	col = tvb.createColumn("Internal Classification");
  	col.bindToValue(new BaseValue<Row>() {
  	    @Override
  	    public Object get(Row r) {
  	        return "TBC";
  	    }
  	});
  	col.setPercentWidth(15);
  	col.format(new ICellFormatter() {
  	    @Override
		public void formatCell(ViewerCell cell, Object value) {
  	    	//cell.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
  	    }
  	});    	
  	col.build();
  	// internal classification
  	col = tvb.createColumn("Entry Points Classification");
  	col.bindToValue(new BaseValue<Row>() {
  	    @Override
  	    public Object get(Row r) {
  	        return "TBC";
  	    }
  	});
  	col.setPercentWidth(15);
  	col.format(new ICellFormatter() {
  	    @Override
		public void formatCell(ViewerCell cell, Object value) {
  	    	//cell.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
  	    }
  	});	
  	col.build();
  	// signed off
  	col = tvb.createColumn("Signed Off");
  	col.bindToValue(new BaseValue<Row>() {
  	    @Override
  	    public Object get(Row r) {
  	        return "TBC";
  	    }
  	});
  	col.setPercentWidth(10);
  	col.format(new ICellFormatter() {
  	    @Override
		public void formatCell(ViewerCell cell, Object value) {
  	    	//cell.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
  	    }
  	});    	
  	col.build();
 	
  	tvb.setInput(Arrays.asList(rows));
   
    TableViewer tv = tvb.getTableViewer();
    final Table table = tvb.getTable();
    table.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
    	    
    table.addListener(SWT.MouseDoubleClick, new Listener() {
        public void handleEvent(Event e) {	
          TableItem[] selection = table.getSelection();
          String s = selection[0].getText();
          System.out.println("Selection={" + s + "}");
          int _id = Integer.parseInt(s);
          pageSpaceDetail(_id);
        }
      });
    
     
  	lblPage.setText("Spaces");
    
    
    
		// page 1========
		page1 = new Composite (formHolder, SWT.NONE);
		page1.setLayout(new FillLayout(SWT.VERTICAL));
		SashForm panels = new SashForm(page1,SWT.BORDER);
		panels.setBackground(Constants.TITLEBAR_BGCOLOR);
		panels.setLayout(new FillLayout());
		Composite mainpanel = new Composite(panels,SWT.NONE);
		mainpanel.setBackground(Constants.APP_BGCOLOR);
		mainpanel.setLayout(new FillLayout());
		Composite rightpanel = new Composite(panels,SWT.NONE);
		rightpanel.setBackground(Constants.APP_BGCOLOR);
		rightpanel.setLayout(new FillLayout());
		panels.setWeights(new int[] {550, 250});
		
		// page 2==========
		page2 = new Composite (formHolder, SWT.NONE);
		page2.setLayout(new FillLayout(SWT.VERTICAL));
		panels = new SashForm(page2,SWT.BORDER);
		panels.setBackground(Constants.TITLEBAR_BGCOLOR);
		panels.setLayout(new FillLayout());
		mainpanel = new Composite(panels,SWT.NONE);
		mainpanel.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		mainpanel.setLayout(new FillLayout());
		rightpanel = new Composite(panels,SWT.NONE);
		rightpanel.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		rightpanel.setLayout(new FillLayout());
		panels.setWeights(new int[] {250, 550});
				
		Composite footer = new Composite(container,SWT.BORDER);
		footer.setBackground(Constants.APP_BGCOLOR);
		footer.setLayout(new FillLayout());				
		
		// set up row element positions =======================================================
		FormData fd_header = new FormData();
		fd_header.top = new FormAttachment(container,0);
		fd_header.right = new FormAttachment(100,0);
		fd_header.bottom = new FormAttachment(container,headerH);
		fd_header.left = new FormAttachment(0,0);
		header.setLayoutData(fd_header);
		
		FormData fd_title = new FormData();
		fd_title.height = titleH;
		fd_title.top = new FormAttachment(header);
		fd_title.right = new FormAttachment(100,0);
		fd_title.left = new FormAttachment(0,0);
		titleBar.setLayoutData(fd_title);
		
		b0 = new Button(titleBar, SWT.NONE);
		b0.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				pageSpacesList();
			}

		});
		FormData fd_b0 = new FormData();
		fd_b0.left = new FormAttachment(80);
		fd_b0.top = new FormAttachment(titleBar,titleH/5);
		b0.setLayoutData(fd_b0);
		b0.setText("Spaces");
		
		b1 = new Button(titleBar, SWT.NONE);
		b1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				pageX();
			}
		});
		b1.setText("Page 1");
		FormData fd_b1 = new FormData();
		fd_b1.top = new FormAttachment(b0, 0, SWT.TOP);
		fd_b1.left = new FormAttachment(b0,5);
		b1.setLayoutData(fd_b1);
						
		FormData fd_lblPage = new FormData();
		fd_lblPage.right = new FormAttachment(70);
		fd_lblPage.top = new FormAttachment(titleBar,titleH/5);
		fd_lblPage.left = new FormAttachment(1);
		lblPage.setLayoutData(fd_lblPage);

		FormData fd_footer = new FormData();
		fd_footer.top = new FormAttachment(100,-footerH);
		fd_footer.right = new FormAttachment(100,0);
		fd_footer.bottom = new FormAttachment(100,0);
		fd_footer.left = new FormAttachment(0,0);
		footer.setLayoutData(fd_footer);				
		
		// graphic elements etc
		Label logo = new Label(header, SWT.TRANSPARENT);
		logo.setAlignment(SWT.LEFT);
		logo.setImage(Constants.getImage("/img/esm-horiz.png"));
		FormData fd = new FormData();
		fd.width = 250;
		fd.left = new FormAttachment(0);
		fd.bottom = new FormAttachment(95);
		logo.setLayoutData (fd);

		String txt = "";
		try {
			txt = "Vessel: " +(String)EsmApplication.appData.getField("VESSEL") + "       ";
		} catch (Exception e1) { }
		txt += "Current User: " + displayName;
		Label lblH = new Label(header,SWT.WRAP);
		lblH.setForeground(Constants.TITLEBAR_BGCOLOR);
		FormData fd_lblH = new FormData();
		fd_lblH.left = new FormAttachment(logo);
		fd_lblH.top = new FormAttachment((headerH/2)-5);
		lblH.setLayoutData(fd_lblH);
		lblH.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		lblH.setAlignment(SWT.LEFT);
		lblH.setBackground(Constants.APP_BGCOLOR);
		lblH.setText(txt);
		
		// read text from disk		
		try {
			txt = "Version " + CharStreams.toString(new InputStreamReader(this.getClass().getResourceAsStream("/txt/version.txt"), Charsets.UTF_8));
		} catch (IOException e) {
			LogController.log(e.toString());
		}		
		Label lblF = new Label(footer,SWT.WRAP);
		lblF.setAlignment(SWT.CENTER);
		lblF.setBackground(Constants.APP_BGCOLOR);
		lblF.setText(txt + " (c)rmrdigitalmedia");				

		slayout.topControl = page0;

	}


	void pageSpacesList(){
		slayout.topControl = page0;
		lblPage.setText(Constants.SPACES_LIST_TITLE);
		formHolder.layout();
	}
	public static void pageSpaceDetail(int id) {
		System.out.println("User selected space "+id);
		currentSpaceId = id;
		try {
			lblPage.setText("Space " + id + ": " + SpacesTable.getRow("ID", ""+id).getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		slayout.topControl = page1;
		formHolder.layout();
	}
	void pageX() {
		// TODO Auto-generated method stub
		slayout.topControl = page2;
		formHolder.layout();
		lblPage.setText("New Page");
	}

	
}
