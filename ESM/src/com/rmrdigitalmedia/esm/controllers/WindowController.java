package com.rmrdigitalmedia.esm.controllers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
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
import org.eclipse.swt.widgets.Control;
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
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.forms.NewSpaceForm;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;
import com.rmrdigitalmedia.esm.models.SpacesTable.Row;
import com.rmrdigitalmedia.esm.views.AdministrationView;
import com.rmrdigitalmedia.esm.views.SpaceAlert;
import com.rmrdigitalmedia.esm.views.SpaceDetailView;
import com.rmrdigitalmedia.esm.views.SpacesListView;

//http://www.eclipse.org/swt/snippets/

@SuppressWarnings("unused")
public class WindowController {

	private static Object me;
	protected static Shell shell;
	static Display display;
	int appHeight, appWidth,headerH = 85,titleH = 40,footerH = 20;
	Composite container, header, titleBar;
	static Composite formHolder;
	static Composite pageSpacesList, pageSpaceDetail, pageAdministration;
	static Label pageTitle;
	String displayName;
	public static Button btnAddSpace, btnEditSpace, btnDeleteSpace, btnAdmin;
	static Button btnSpacesList;	
	static Button btnAddEntry, btnEditEntry, btnDeleteEntry, btnEntryList;	
	static StackLayout stackLayout;
	private static int currentSpaceId = 0;
	public static EsmUsersTable.Row user;
	SpacesTable.Row[] rows;

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			WindowController window = new WindowController(EsmUsersTable.getRow("USERNAME", "admin"));
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public WindowController(EsmUsersTable.Row user) {
		me = this;
		WindowController.user = user;
		this.displayName = user.getRank() + " " + user.getForename() + " " + user.getSurname();
	  	LogController.log("Running class " + this.getClass().getName());
	  	LogController.log("Logged in user: " + displayName);
	}

	public void open() {
		// set up main window
		display = Display.getDefault();
		shell = new Shell();
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setText(C.APP_NAME);
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
		LogController.log(C.EXIT_MSG);
	}

	protected void createContents() {
		
		// set up container layout
		container = new Composite(shell,SWT.NONE);
		container.setBackground(C.APP_BGCOLOR);		
		FormLayout layout = new FormLayout();
		container.setLayout (layout);
		
		// set up row elements ================================================
		header = new Composite(container,SWT.NONE);
		header.setBackground(C.APP_BGCOLOR);
		header.setLayout(new FormLayout());
		
		titleBar = new Composite(container,SWT.NONE);
		titleBar.setBackground(C.TITLEBAR_BGCOLOR);
		titleBar.setLayout(new FormLayout());	
		
		pageTitle = new Label(titleBar, SWT.NONE);
		pageTitle.setFont(C.HEADER_FONT);
		pageTitle.setBackground(C.TITLEBAR_BGCOLOR);
		pageTitle.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		formHolder = new Composite(container,SWT.NONE);
		formHolder.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		FormData fd_formHolder = new FormData();
		fd_formHolder.left = new FormAttachment(0);
		fd_formHolder.top = new FormAttachment(titleBar,0);
		fd_formHolder.right = new FormAttachment(100);
		fd_formHolder.bottom = new FormAttachment(100);
		formHolder.setLayoutData(fd_formHolder);
		stackLayout = new StackLayout ();
		formHolder.setLayout(stackLayout);
				
		// SPACES LISTING PAGE ======================================================
		pageSpacesList = new Composite (formHolder, SWT.NONE);
		SpacesListView.buildTable(pageSpacesList);
    
		// SPACE DETAIL PAGE ======================================================
		pageSpaceDetail = new Composite (formHolder, SWT.NONE);
		//SpaceDetailView.buildPage(pageSpaceDetail, 0);
		
		// ADMIN PAGE ===============================================================
		pageAdministration = new Composite (formHolder, SWT.NONE);
		AdministrationView.buildPage(pageAdministration);
		
		
		
		
		
		
				
		Composite footer = new Composite(container,SWT.BORDER);
		footer.setBackground(C.APP_BGCOLOR);
		footer.setLayout(new FillLayout());				
		
		// set up row element positions =======================
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
		
	
		// buttons ====================================================================================================
	
		Button foo = new Button(titleBar, SWT.NONE); // dummy button to take default
		
		btnAdmin = new Button(titleBar, SWT.PUSH);
		btnAdmin.setImage(C.getImage("/img/Secrecy.png"));
		btnAdmin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showAdministration();
			}
		});
		btnAdmin.setText("Administration");
		btnAdmin.setFont(C.BUTTON_FONT);
		FormData fd_btnAdmin = new FormData();
		fd_btnAdmin.top = new FormAttachment(titleBar,titleH/5);
		fd_btnAdmin.right = new FormAttachment(100,-20);
		btnAdmin.setLayoutData(fd_btnAdmin);
		btnAdmin.setEnabled(false);

		btnAddSpace = new Button(titleBar, SWT.PUSH);
		btnAddSpace.setImage(C.getImage("/img/Add.png"));
		btnAddSpace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				NewSpaceForm nsf = new NewSpaceForm(user.getID());					
				if(nsf.complete()) {
					LogController.log("New Space & Entry Point saved in database");
					showSpacesList();					
				}
			}
		});
		btnAddSpace.setText("Add");
		btnAddSpace.setFont(C.BUTTON_FONT);
		FormData fd_btnAddSpace = new FormData();
		fd_btnAddSpace.right = new FormAttachment(btnAdmin,-25);
		fd_btnAddSpace.top = new FormAttachment(titleBar,titleH/5);
		btnAddSpace.setLayoutData(fd_btnAddSpace);

		btnEditSpace = new Button(titleBar, SWT.PUSH);
		btnEditSpace.setImage(C.getImage("/img/Page_white_edit.png"));
		btnEditSpace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
		          TableItem[] selection = SpacesListView.getTable().getSelection();
		          String s = selection[0].getText();
		          LogController.log("Edit Selection={" + s + "}");
		          int _id = Integer.parseInt(s);
		          checkSpaceAlert(_id);
			}
		});
		btnEditSpace.setText("Edit");
		btnEditSpace.setFont(C.BUTTON_FONT);
		FormData fd_btnEditSpace = new FormData();
		fd_btnEditSpace.right = new FormAttachment(btnAddSpace,-5);
		fd_btnEditSpace.top = new FormAttachment(titleBar,titleH/5);
		btnEditSpace.setLayoutData(fd_btnEditSpace);
		btnEditSpace.setEnabled(false);

		btnDeleteSpace = new Button(titleBar, SWT.PUSH);
		btnDeleteSpace.setImage(C.getImage("/img/delete-file16.png"));
		btnDeleteSpace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
		          TableItem[] selection = SpacesListView.getTable().getSelection();
		          String s = selection[0].getText();
		          LogController.log("Delete Selection={" + s + "}");
		          int _id = Integer.parseInt(s);		          
		          	DeleteSpaceController dsc = new DeleteSpaceController();					
					if(dsc.deleteOK(_id)) {
						LogController.log("Space "+_id+" marked as deleted in database");
						showSpacesList();						
					} else {
						LogController.log("Error occurred deleting space " + _id);
					}
				}
			
		});
		btnDeleteSpace.setText("Delete");
		btnDeleteSpace.setFont(C.BUTTON_FONT);
		FormData fd_btnDeleteSpace = new FormData();
		fd_btnDeleteSpace.right = new FormAttachment(btnEditSpace,-5);
		fd_btnDeleteSpace.top = new FormAttachment(titleBar,titleH/5);
		btnDeleteSpace.setLayoutData(fd_btnDeleteSpace);
		btnDeleteSpace.setEnabled(false);

		
		btnSpacesList = new Button(titleBar, SWT.PUSH);
		btnSpacesList.setImage(C.getImage("/img/List.png"));
		btnSpacesList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showSpacesList();
			}
		});
		btnSpacesList.setText("Spaces List");
		btnSpacesList.setFont(C.BUTTON_FONT);
		FormData fd_btnSpacesList = new FormData();
		fd_btnSpacesList.right = new FormAttachment(btnAdmin,-25);
		fd_btnSpacesList.top = new FormAttachment(titleBar,titleH/5);
		btnSpacesList.setLayoutData(fd_btnSpacesList);

		// layout settings
		FormData fd_pageTitle = new FormData();
		fd_pageTitle.right = new FormAttachment(50);
		fd_pageTitle.top = new FormAttachment(titleBar,titleH/5);
		fd_pageTitle.left = new FormAttachment(1);
		pageTitle.setLayoutData(fd_pageTitle);

		FormData fd_footer = new FormData();
		fd_footer.top = new FormAttachment(100,-footerH);
		fd_footer.right = new FormAttachment(100,0);
		fd_footer.bottom = new FormAttachment(100,0);
		fd_footer.left = new FormAttachment(0,0);
		footer.setLayoutData(fd_footer);				
		
		// graphic elements etc
		Label logo = new Label(header, SWT.TRANSPARENT);
		logo.setAlignment(SWT.LEFT);
		logo.setImage(C.getImage("/img/esm-horiz.png"));
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
		lblH.setForeground(C.TITLEBAR_BGCOLOR);
		FormData fd_lblH = new FormData();
		fd_lblH.left = new FormAttachment(logo);
		fd_lblH.top = new FormAttachment((headerH/2)-5);
		lblH.setLayoutData(fd_lblH);
		lblH.setFont(C.HEADER_FONT);
		lblH.setAlignment(SWT.LEFT);
		lblH.setBackground(C.APP_BGCOLOR);
		lblH.setText(txt);
		
		// read text from disk		
		try {
			txt = "Version " + CharStreams.toString(new InputStreamReader(this.getClass().getResourceAsStream("/txt/version.txt"), Charsets.UTF_8));
		} catch (IOException e) {
			LogController.logEvent(me,2,e);
		}		
		Label lblF = new Label(footer,SWT.WRAP);
		lblF.setAlignment(SWT.CENTER);
		lblF.setBackground(C.APP_BGCOLOR);
		lblF.setText(txt + " (c)rmrdigitalmedia");				

		if(user.getAccessLevel()==9) {
			btnAdmin.setEnabled(true);
		}
		
		FormData fd_foo = new FormData();
		fd_foo.right = new FormAttachment(120);
		foo.setLayoutData(fd_foo);
		shell.setDefaultButton(foo);
		
		showSpacesList();

	}

	// methods to display pages, alerts etc
	void showSpacesList(){
		try {
			rows = SpacesTable.getRows("DELETED=FALSE");
		} catch (SQLException e) {
			LogController.logEvent(me, 2, e);
		}
		SpacesListView.getTVB().setInput(Arrays.asList(rows));
		btnAddSpace.setVisible(true);
		btnEditSpace.setVisible(true);
		btnDeleteSpace.setVisible(true);
		btnEditSpace.setEnabled(false);
		btnDeleteSpace.setEnabled(false);
		btnSpacesList.setVisible(false);
		stackLayout.topControl = pageSpacesList;
		pageTitle.setText(C.SPACES_LIST_TITLE);
		formHolder.layout();
	}
	public static void checkSpaceAlert(int id) {
		boolean showAlert = false;
		// get internal classification status from ID	
		//showAlert = true;
		if(showAlert){			
			new SpaceAlert(shell);			
		}
		showSpaceDetail(id);
	}
	public static void showSpaceDetail(int id) {
		btnAddSpace.setVisible(false);
		btnEditSpace.setVisible(false);
		btnDeleteSpace.setVisible(false);
		btnSpacesList.setVisible(true);
		for (Control c:pageSpaceDetail.getChildren()) {
			c.dispose();
		}
		SpaceDetailView.buildPage(pageSpaceDetail, id);
		stackLayout.topControl = pageSpaceDetail;
		LogController.log("User selected Space ID "+id);
		currentSpaceId = id;
		try {
			pageTitle.setText("Space " + id + ": " + SpacesTable.getRow("ID", ""+id).getName());
		} catch (SQLException e) {
			LogController.logEvent(me, 2, e);
		}
		formHolder.layout();
	}
	void showAdministration() {
		btnAddSpace.setVisible(false);	
		btnEditSpace.setVisible(false);
		btnDeleteSpace.setVisible(false);
		btnSpacesList.setVisible(true);
		stackLayout.topControl = pageAdministration;
		pageTitle.setText(C.ADMIN_PAGE_TITLE);
		formHolder.layout();
	}

	
}
