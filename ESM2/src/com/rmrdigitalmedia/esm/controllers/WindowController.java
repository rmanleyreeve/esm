package com.rmrdigitalmedia.esm.controllers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.rmrdigitalmedia.esm.AppData;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.dialogs.DeleteSpaceDialog;
import com.rmrdigitalmedia.esm.dialogs.SpaceAlert;
import com.rmrdigitalmedia.esm.forms.AddSpaceForm;
import com.rmrdigitalmedia.esm.models.EntrypointsTable;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;
import com.rmrdigitalmedia.esm.views.AdministrationView;
import com.rmrdigitalmedia.esm.views.EntryAuditChecklistView;
import com.rmrdigitalmedia.esm.views.EntryAuditClassificationView;
import com.rmrdigitalmedia.esm.views.PhotoDisplayView;
import com.rmrdigitalmedia.esm.views.SpaceAuditChecklistView;
import com.rmrdigitalmedia.esm.views.SpaceAuditClassificationView;
import com.rmrdigitalmedia.esm.views.SpaceDetailView;
import com.rmrdigitalmedia.esm.views.SpacesListView;

public class WindowController {

	private static Object me;
	protected static Shell shell;
	public static Shell getShell() {
		return shell;
	}
	static Display display;
	int appHeight, appWidth;
	static int headerH = 80;
	int titleH = 40;
	int footerH = 15;
	int buttonTop = 5;
	Composite container;
	static Composite header;
	Composite titleBar;
	static Composite formHolder, pageSpacesList, pageSpaceDetail, pageAdministration, pageSpaceAudit, pageEntryAudit;
	static Label pageTitle, logo, lblH;
	static String displayName;
	public static Button btnAddSpace, btnDeleteSpace, btnAdmin, btnViewSpaceDetails, btnHelp;
	static Button btnSpacesList, btnAddEntry, btnEditEntry, btnDeleteEntry, btnEntryList;	
	static StackLayout stackLayout;
	public static int currentSpaceId = 0;
	public static String helpfile = C.HELPFILE_GENERIC;
	static EsmUsersTable.Row user;
	SpacesTable.Row[] rows;
	private Label lblCustomerLogo;
	private static boolean isAdmin = false;
	private static boolean isApproved = true;
	public static String searchFilter = "";

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			EsmApplication.appData = new AppData();
			WindowController window = new WindowController(EsmUsersTable.getRow("USERNAME", "admin"));
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public WindowController(EsmUsersTable.Row user) {
		this.setUser(user);
		isAdmin = (user.getAccessLevel()==9);
		isApproved = (user.getAccessLevel()>1);
		displayName = user.getRank() + " " + user.getForename() + " " + user.getSurname();
		LogController.log("Running class " + this.getClass().getName());
		LogController.log("Logged in user: " + displayName);
	}
	public static EsmUsersTable.Row getUser() {
		return user;
	}
	public static void setUser(EsmUsersTable.Row _user){
		user = _user;
	}
	@SuppressWarnings("static-access")
	public void killUser() {
		this.user = null;
	}
	public static void getUserName() {
		try {
			EsmUsersTable.Row r = EsmUsersTable.getRow(user.getID());
			displayName = r.getRank() + " " + r.getForename() + " " + r.getSurname();
		} catch (SQLException e) {}
	}
	public void open() {
		// set up main window
		display = Display.getCurrent();
		for(Shell sh:display.getShells()) {
			sh.dispose();
		}
		shell = new Shell(display);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setText(C.APP_NAME);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.setImages(new Image[] { C.getImage(C.APP_ICON_16), C.getImage(C.APP_ICON_32) }); // 16x16 & 32x32
		// size & position
		Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		//shell.setSize(800,600);
		shell.setSize(bounds.width-200, bounds.height-100); // almost fill screen
		Rectangle rect = shell.getBounds ();
		appWidth = rect.width;
		appHeight = rect.height;
		LogController.log("Main Window Size: " + appWidth + "x" + appHeight);
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation (x, y);		
		shell.open();
		createContents();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
		LogController.log(C.EXIT_MSG);
	}

	public static void setHeaderLabelText() {
		String txt = "";
		int offset = 5;
		try {
			txt = (String)EsmApplication.appData.getField("LOCATION_TYPE") + ": " + (String)EsmApplication.appData.getField("LOCATION_NAME") + "\n";
		} catch (Exception e1) { }
		getUserName();
		txt += "Current User: " + displayName;
		lblH.setText(txt);
		FormData fd_lblH = new FormData();
		fd_lblH.right = new FormAttachment(logo, 410, SWT.RIGHT);
		fd_lblH.bottom = new FormAttachment(100, -3);
		fd_lblH.left = new FormAttachment(logo, 10);
		fd_lblH.top = new FormAttachment(offset);
		lblH.setLayoutData(fd_lblH);
		header.layout();
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
		fd_formHolder.bottom = new FormAttachment(100,-footerH);
		formHolder.setLayoutData(fd_formHolder);
		stackLayout = new StackLayout ();
		formHolder.setLayout(stackLayout);

		// SPACES LISTING PAGE ======================================================

		pageSpacesList = new Composite (formHolder, SWT.NONE);

		// SPACE DETAIL PAGE ======================================================
		pageSpaceDetail = new Composite (formHolder, SWT.NONE);

		// ADMIN PAGE ===============================================================
		pageAdministration = new Composite (formHolder, SWT.NONE);

		// SPACE AUDIT PAGE ===============================================================
		pageSpaceAudit = new Composite (formHolder, SWT.NONE);

		// ENTRY AUDIT PAGE ===============================================================
		pageEntryAudit = new Composite (formHolder, SWT.NONE);

		Composite footer = new Composite(container,SWT.NONE);
		footer.setBackground(C.APP_BGCOLOR);
		FillLayout fl_footer = new FillLayout();
		footer.setLayout(fl_footer);				

		// set up row element positions =======================
		FormData fd_header = new FormData();
		fd_header.bottom = new FormAttachment(0, 60);
		fd_header.top = new FormAttachment(container,0);
		fd_header.right = new FormAttachment(100,0);
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

		btnHelp = new Button(titleBar, SWT.NONE);
		btnHelp.setImage(C.getImage("hint.png"));
		btnHelp.setBackground(C.TITLEBAR_BGCOLOR);
		FormData fd_btnHelp = new FormData();
		fd_btnHelp.top = new FormAttachment(titleBar,buttonTop);
		fd_btnHelp.right = new FormAttachment(100, -10);
		btnHelp.setLayoutData(fd_btnHelp);
		btnHelp.setToolTipText("Help for this page");
		btnHelp.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// read text from disk
				LogController.log("Help File: "+helpfile);
				String html = "";
				try {
					html += CharStreams.toString(new InputStreamReader(this.getClass().getResourceAsStream("/htm/"+helpfile), Charsets.UTF_8));
				} catch (IOException e) {
					LogController.logEvent(me,C.WARNING,e);
				}	
				if(!html.equals("")) {
					InternetController.renderHTML(html);
				}


			}
		});

		btnAdmin = new Button(titleBar, SWT.PUSH);
		btnAdmin.setToolTipText("Administration Menu (authorized users only)");
		btnAdmin.setImage(C.getImage("locked.png"));
		btnAdmin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showAdministration();
			}
		});
		btnAdmin.setText("Administration");
		btnAdmin.setFont(C.BUTTON_FONT);
		btnAdmin.setBackground(C.TITLEBAR_BGCOLOR);
		FormData fd_btnAdmin = new FormData();
		fd_btnAdmin.right = new FormAttachment(btnHelp, -15);
		fd_btnAdmin.top = new FormAttachment(titleBar,buttonTop);
		btnAdmin.setLayoutData(fd_btnAdmin);
		btnAdmin.setEnabled(false);

		btnAddSpace = new Button(titleBar, SWT.PUSH);
		btnAddSpace.setToolTipText("Add a new Enclosed Space");
		btnAddSpace.setImage(C.getImage("add.png"));
		btnAddSpace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				AddSpaceForm asf = new AddSpaceForm(user.getID());					
				if(asf.complete()) {
					LogController.log("New Space & Entry Point saved in database");
					showSpacesList(searchFilter);					
				}
			}
		});
		btnAddSpace.setText("Add");
		btnAddSpace.setFont(C.BUTTON_FONT);
		btnAddSpace.setBackground(C.TITLEBAR_BGCOLOR);
		FormData fd_btnAddSpace = new FormData();
		fd_btnAddSpace.top = new FormAttachment(titleBar,buttonTop);
		fd_btnAddSpace.right = new FormAttachment(btnAdmin,-25);
		btnAddSpace.setLayoutData(fd_btnAddSpace);
		btnAddSpace.setEnabled(isApproved);

		btnViewSpaceDetails = new Button(titleBar, SWT.PUSH);
		btnViewSpaceDetails.setToolTipText("View details for the selected Enclosed Space");
		btnViewSpaceDetails.setImage(C.getImage("edit.png"));
		btnViewSpaceDetails.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				LogController.log("Details Selection={" + currentSpaceId + "}");
				checkSpaceAlert(currentSpaceId);
			}
		});	
		btnViewSpaceDetails.setText("Details");
		btnViewSpaceDetails.setFont(C.BUTTON_FONT);
		btnViewSpaceDetails.setBackground(C.TITLEBAR_BGCOLOR);
		FormData fd_btnEditSpace = new FormData();
		fd_btnEditSpace.top = new FormAttachment(titleBar,buttonTop);
		fd_btnEditSpace.right = new FormAttachment(btnAddSpace,-5);
		btnViewSpaceDetails.setLayoutData(fd_btnEditSpace);
		btnViewSpaceDetails.setEnabled(false);

		btnDeleteSpace = new Button(titleBar, SWT.PUSH);
		btnDeleteSpace.setToolTipText("Delete the selected Enclosed Space");
		btnDeleteSpace.setImage(C.getImage("delete.png"));
		btnDeleteSpace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				LogController.log("Delete Selection={" + currentSpaceId + "}");
				DeleteSpaceDialog dsd = new DeleteSpaceDialog();					
				if(dsd.deleteOK(currentSpaceId)) {
					LogController.log("Space "+currentSpaceId+" marked as deleted in database");
					showSpacesList(searchFilter);						
				} else {
					LogController.log("Space " + currentSpaceId + " not deleted");
				}
			}			
		});
		btnDeleteSpace.setText("Delete");
		btnDeleteSpace.setFont(C.BUTTON_FONT);
		btnDeleteSpace.setBackground(C.TITLEBAR_BGCOLOR);
		FormData fd_btnDeleteSpace = new FormData();
		fd_btnDeleteSpace.top = new FormAttachment(titleBar,buttonTop);
		fd_btnDeleteSpace.right = new FormAttachment(btnViewSpaceDetails,-5);
		btnDeleteSpace.setLayoutData(fd_btnDeleteSpace);
		btnDeleteSpace.setEnabled(false);

		btnSpacesList = new Button(titleBar, SWT.PUSH);
		btnSpacesList.setToolTipText("View the list of Enclosed Spaces");
		btnSpacesList.setImage(C.getImage("list.png"));
		btnSpacesList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showSpacesList(searchFilter);
			}
		});
		btnSpacesList.setText("Spaces List");
		btnSpacesList.setFont(C.BUTTON_FONT);
		btnSpacesList.setBackground(C.TITLEBAR_BGCOLOR);
		FormData fd_btnSpacesList = new FormData();
		fd_btnSpacesList.top = new FormAttachment(titleBar,buttonTop);
		fd_btnSpacesList.right = new FormAttachment(btnAdmin,-25);
		btnSpacesList.setLayoutData(fd_btnSpacesList);

		// layout settings
		FormData fd_pageTitle = new FormData();
		fd_pageTitle.right = new FormAttachment(50);
		fd_pageTitle.top = new FormAttachment(titleBar,10);
		fd_pageTitle.left = new FormAttachment(1);
		pageTitle.setLayoutData(fd_pageTitle);

		FormData fd_footer = new FormData();
		fd_footer.top = new FormAttachment(100,-footerH);
		fd_footer.right = new FormAttachment(100,0);
		fd_footer.bottom = new FormAttachment(100,0);
		fd_footer.left = new FormAttachment(0,0);
		footer.setLayoutData(fd_footer);				

		// graphic elements etc
		logo = new Label(header, SWT.TRANSPARENT);
		logo.setImage(C.getImage("esm-logo-horiz.png"));
		logo.setBackground(C.APP_BGCOLOR);
		FormData fd_logo = new FormData();
		fd_logo.top = new FormAttachment(0);
		fd_logo.height = 65;
		fd_logo.width = 120;
		fd_logo.left = new FormAttachment(0, 5);
		fd_logo.bottom = new FormAttachment(100);
		logo.setLayoutData (fd_logo);

		lblH = new Label(header,SWT.WRAP);
		lblH.setForeground(C.TITLEBAR_BGCOLOR);
		lblH.setFont(C.HEADER_FONT);
		lblH.setAlignment(SWT.LEFT);
		lblH.setBackground(C.APP_BGCOLOR);
		setHeaderLabelText();

		Button btnLogOut = new Button(header, SWT.NONE);
		btnLogOut.setImage(SWTResourceManager.getImage(WindowController.class, "/img/logoff.png"));
		btnLogOut.setText("Log Out");
		FormData fd_btnLogOut = new FormData();
		fd_btnLogOut.top = new FormAttachment(0, 15);
		fd_btnLogOut.left = new FormAttachment(lblH, 10);
		btnLogOut.setLayoutData (fd_btnLogOut);
		btnLogOut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(EsmApplication.confirm(C.LOG_OUT_MSG)) {
					LogController.log("Logging Out");
					user = null;
					EsmApplication.appLogout(shell);
					shell.dispose();
				} else {
					LogController.log("Logout cancelled");
				}
			}
		});

		// customer logo area (500 x 50)		
		lblCustomerLogo = new Label(header, SWT.NONE);
		lblCustomerLogo.setBackground(C.APP_BGCOLOR);
		FormData fd_lblmrmLogo = new FormData();
		// look for customer logo
		Image ci = C.getExtImage("customer_logo.png");
		if(ci != null) {
			lblCustomerLogo.setImage(ci);
			LogController.log("Loading customer logo image");
		} else {
			lblCustomerLogo.setImage(C.getImage("default_logo.png"));
			LogController.log("No customer logo found, using default");
		}
		fd_lblmrmLogo.top = new FormAttachment(8);
		fd_lblmrmLogo.right = new FormAttachment(100, -5);
		lblCustomerLogo.setLayoutData(fd_lblmrmLogo);

		// read text from disk
		String txt = C.APP_NAME + "\t" + C.COPYRIGHT;
		try {
			txt += "\tVersion " + CharStreams.toString(new InputStreamReader(this.getClass().getResourceAsStream("/txt/version.txt"), Charsets.UTF_8)).replaceAll("(\\r|\\n)", " ");
		} catch (IOException e) {
			LogController.logEvent(me,C.WARNING,e);
		}		
		Label lblF = new Label(footer,SWT.BORDER | SWT.HORIZONTAL | SWT.CENTER);
		lblF.setAlignment(SWT.CENTER);
		lblF.setBackground(C.TITLEBAR_BGCOLOR);
		lblF.setFont(C.FONT_8);
		lblF.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblF.setText(txt);

		btnAdmin.setEnabled(isAdmin);

		FormData fd_foo = new FormData();
		fd_foo.right = new FormAttachment(120);
		foo.setLayoutData(fd_foo);
		shell.setDefaultButton(foo);

		showSpacesList(""); // show all spaces
	}



	// methods to display pages etc
	public static void showSpacesList(String searchText){
		searchFilter = searchText;
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		LogController.log("Displaying Spaces List, filter='" + searchFilter+"'");
		SpacesListView.buildTable(pageSpacesList);
		stackLayout.topControl = pageSpacesList;
		pageTitle.setText(C.SPACES_LIST_TITLE);
		helpfile = C.HELPFILE_SPACESLIST;
		btnAddSpace.setVisible(true);
		btnAddSpace.setEnabled(isApproved);
		btnDeleteSpace.setVisible(true);
		btnDeleteSpace.setEnabled(false);
		btnViewSpaceDetails.setVisible(true);
		btnViewSpaceDetails.setEnabled(false);
		btnSpacesList.setVisible(false);
		btnAdmin.setEnabled(isAdmin);
		formHolder.layout();
		shell.setCursor(new Cursor(display, SWT.CURSOR_ARROW));
	}


	public static void showAdministration() {
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		LogController.log("Displaying Administration");
		AdministrationView.buildPage(pageAdministration);
		stackLayout.topControl = pageAdministration;
		pageTitle.setText(C.ADMIN_PAGE_TITLE);
		helpfile = C.HELPFILE_ADMIN;
		btnAddSpace.setVisible(false);	
		btnViewSpaceDetails.setVisible(false);
		btnDeleteSpace.setVisible(false);
		btnSpacesList.setVisible(true);
		btnAdmin.setEnabled(false);
		formHolder.layout();
	}
	public static void showSpaceDetail(int spaceID) {
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		currentSpaceId = spaceID;
		LogController.log("Displaying Space Detail for ID "+spaceID);
		SpaceDetailView.buildPage(pageSpaceDetail, spaceID);
		stackLayout.topControl = pageSpaceDetail;
		try {
			pageTitle.setText("Space " + spaceID + ": " + SpacesTable.getRow("ID", ""+spaceID).getName());
		} catch (SQLException e) {
			LogController.logEvent(me, C.WARNING, e);
		}
		helpfile = C.HELPFILE_SPACEDETAIL;
		btnAddSpace.setVisible(false);
		btnViewSpaceDetails.setVisible(false);
		btnDeleteSpace.setVisible(false);
		btnSpacesList.setVisible(true);
		btnAdmin.setEnabled(isAdmin);
		formHolder.layout();
	}
	public static void showSpaceAuditChecklist(int spaceID) {
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		currentSpaceId = spaceID;
		LogController.log("Displaying Internal Space Audit Checklist for ID:" + spaceID);
		SpaceAuditChecklistView.buildPage(pageSpaceAudit, spaceID);
		stackLayout.topControl = pageSpaceAudit;
		String title = C.SPACE_AUDIT_CHECKLIST_PAGE_TITLE;
		try {
			title += " for " + SpacesTable.getRow(spaceID).getName();
		} catch (SQLException ex) {}
		pageTitle.setText(title);
		helpfile = C.HELPFILE_SPACE_AUDIT_CHECK;
		btnAddSpace.setVisible(false);	
		btnViewSpaceDetails.setVisible(false);
		btnDeleteSpace.setVisible(false);
		btnSpacesList.setVisible(false);
		btnAdmin.setEnabled(isAdmin);
		formHolder.layout();
	}
	public static void showSpaceAuditClassification(int spaceID) {
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		currentSpaceId = spaceID;
		LogController.log("Displaying Internal Space Audit Classification for ID:" + spaceID);
		SpaceAuditClassificationView.buildPage(pageSpaceAudit, spaceID);
		stackLayout.topControl = pageSpaceAudit;
		String title = C.SPACE_AUDIT_CLASSIFICATION_PAGE_TITLE;
		try {
			title += " for " + SpacesTable.getRow(spaceID).getName();
		} catch (SQLException ex) {}
		pageTitle.setText(title);
		helpfile = C.HELPFILE_SPACE_AUDIT_CLASS;
		btnAddSpace.setVisible(false);	
		btnViewSpaceDetails.setVisible(false);
		btnDeleteSpace.setVisible(false);
		btnSpacesList.setVisible(false);
		btnAdmin.setEnabled(isAdmin);
		formHolder.layout();
	}
	public static void showEntryAuditChecklist(int entryID) {
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		LogController.log("Displaying Entry Point Audit Checklist for ID:" + entryID);
		EntryAuditChecklistView.buildPage(pageEntryAudit, entryID);
		stackLayout.topControl = pageEntryAudit;
		String title = C.ENTRY_AUDIT_CHECKLIST_PAGE_TITLE;
		try {
			title += " for " + EntrypointsTable.getRow(entryID).getName();
		} catch (SQLException ex) {}
		pageTitle.setText(title);
		helpfile = C.HELPFILE_EP_AUDIT_CHECK;
		btnAddSpace.setVisible(false);	
		btnViewSpaceDetails.setVisible(false);
		btnDeleteSpace.setVisible(false);
		btnSpacesList.setVisible(false);
		btnAdmin.setEnabled(isAdmin);
		formHolder.layout();
	}
	public static void showEntryAuditClassification(int entryID) {
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		LogController.log("Displaying Entry Point Audit Classification for ID:" + entryID);
		EntryAuditClassificationView.buildPage(pageEntryAudit, entryID);
		stackLayout.topControl = pageEntryAudit;
		String title = C.ENTRY_AUDIT_CLASSIFICATION_PAGE_TITLE;
		try {
			title += " for " + EntrypointsTable.getRow(entryID).getName();
		} catch (SQLException ex) {}
		pageTitle.setText(title);
		helpfile = C.HELPFILE_EP_AUDIT_CLASS;
		btnAddSpace.setVisible(false);	
		btnViewSpaceDetails.setVisible(false);
		btnDeleteSpace.setVisible(false);
		btnSpacesList.setVisible(false);
		btnAdmin.setEnabled(isAdmin);
		formHolder.layout();
	}

	// methods to display alerts etc
	public static void checkSpaceAlert(int spaceID) {
		boolean showAlert = false;
		// get internal classification status from ID	
		showAlert = (EsmApplication.appData.getField("SPACE_STATUS_"+spaceID).equals("red"));
		if(showAlert){			
			new SpaceAlert(shell);			
		}
		showSpaceDetail(spaceID);
	}
	public static void showPhotoViewer(int dataID) {
		new PhotoDisplayView(shell, dataID);			
	}


}
