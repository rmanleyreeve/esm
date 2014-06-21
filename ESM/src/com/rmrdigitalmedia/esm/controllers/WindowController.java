package com.rmrdigitalmedia.esm.controllers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;
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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.forms.DeleteSpaceDialog;
import com.rmrdigitalmedia.esm.forms.AddSpaceForm;
import com.rmrdigitalmedia.esm.models.EntrypointsTable;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;
import com.rmrdigitalmedia.esm.views.AdministrationView;
import com.rmrdigitalmedia.esm.views.EntryAuditChecklistView;
import com.rmrdigitalmedia.esm.views.EntryAuditClassificationView;
import com.rmrdigitalmedia.esm.views.PhotoViewer;
import com.rmrdigitalmedia.esm.views.SpaceAlert;
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
	int appHeight, appWidth,headerH = 100,titleH = 40,footerH = 15;
	Composite container, header, titleBar;
	static Composite formHolder, pageSpacesList, pageSpaceDetail, pageAdministration, pageSpaceAudit, pageEntryAudit;
	static Label pageTitle, onlineStatus;
	String displayName;
	public static Button btnAddSpace, btnDeleteSpace, btnAdmin, btnViewSpaceDetails;
	static Button btnSpacesList, btnAddEntry, btnEditEntry, btnDeleteEntry, btnEntryList;	
	static StackLayout stackLayout;
	public static int currentSpaceId = 0;
	public static EsmUsersTable.Row user;
	SpacesTable.Row[] rows;
	private Label lblVtLogo;

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
		shell.setImages(new Image[] { C.getImage(C.APP_ICON_16), C.getImage(C.APP_ICON_32) }); // 16x16 & 32x32
		// size & position
		Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		//shell.setSize(800,600);
		shell.setSize(bounds.width-200, bounds.height-100); // almost fill screen
		Rectangle rect = shell.getBounds ();
		appWidth = rect.width;
		appHeight = rect.height;
		LogController.log("Size: " + appWidth + "x" + appHeight);
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
		SpacesListView.buildTable(pageSpacesList);

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

		onlineStatus = new Label(titleBar, SWT.NONE);
		onlineStatus.setImage(C.getImage("16_globe.png"));
		onlineStatus.setBackground(C.TITLEBAR_BGCOLOR);
		FormData fd_onlineStatus = new FormData();
		fd_onlineStatus.top = new FormAttachment(titleBar,(titleH/5)+5);
		fd_onlineStatus.right = new FormAttachment(100, -10);
		onlineStatus.setLayoutData(fd_onlineStatus);
		onlineStatus.setToolTipText("Application is online");
		onlineStatus.setEnabled(InternetController.checkNetAccess());

		btnAdmin = new Button(titleBar, SWT.PUSH);
		btnAdmin.setToolTipText("Administration Menu (authorized users only)");
		btnAdmin.setImage(C.getImage("16_padlock.png"));
		btnAdmin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showAdministration();
			}
		});
		btnAdmin.setText("Administration");
		btnAdmin.setFont(C.BUTTON_FONT);
		FormData fd_btnAdmin = new FormData();
		fd_btnAdmin.right = new FormAttachment(onlineStatus, -15);
		fd_btnAdmin.top = new FormAttachment(titleBar,titleH/5);
		btnAdmin.setLayoutData(fd_btnAdmin);
		btnAdmin.setEnabled(false);

		btnAddSpace = new Button(titleBar, SWT.PUSH);
		btnAddSpace.setToolTipText("Add a new Enclosed Space");
		btnAddSpace.setImage(C.getImage("16_add.png"));
		btnAddSpace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				AddSpaceForm asf = new AddSpaceForm(user.getID());					
				if(asf.complete()) {
					LogController.log("New Space & Entry Point saved in database");
					showSpacesList();					
				}
			}
		});
		btnAddSpace.setText("Add");
		btnAddSpace.setFont(C.BUTTON_FONT);
		FormData fd_btnAddSpace = new FormData();
		fd_btnAddSpace.top = new FormAttachment(titleBar,titleH/5);
		fd_btnAddSpace.right = new FormAttachment(btnAdmin,-25);
		btnAddSpace.setLayoutData(fd_btnAddSpace);

		btnViewSpaceDetails = new Button(titleBar, SWT.PUSH);
		btnViewSpaceDetails.setToolTipText("View details for the selected Enclosed Space");
		btnViewSpaceDetails.setImage(C.getImage("16_edit.png"));
		btnViewSpaceDetails.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				TableItem[] selection = SpacesListView.getTable().getSelection();
				String s = selection[0].getText();
				LogController.log("Details Selection={" + s + "}");
				int _id = Integer.parseInt(s);
				checkSpaceAlert(_id);
			}
		});
		btnViewSpaceDetails.setText("Details");
		btnViewSpaceDetails.setFont(C.BUTTON_FONT);
		FormData fd_btnEditSpace = new FormData();
		fd_btnEditSpace.top = new FormAttachment(titleBar,titleH/5);
		fd_btnEditSpace.right = new FormAttachment(btnAddSpace,-5);
		btnViewSpaceDetails.setLayoutData(fd_btnEditSpace);
		btnViewSpaceDetails.setEnabled(false);

		btnDeleteSpace = new Button(titleBar, SWT.PUSH);
		btnDeleteSpace.setToolTipText("Delete the selected Enclosed Space");
		btnDeleteSpace.setImage(C.getImage("16_delete.png"));
		btnDeleteSpace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				TableItem[] selection = SpacesListView.getTable().getSelection();
				String s = selection[0].getText();
				LogController.log("Delete Selection={" + s + "}");
				int _id = Integer.parseInt(s);		          
				DeleteSpaceDialog dsd = new DeleteSpaceDialog();					
				if(dsd.deleteOK(_id)) {
					LogController.log("Space "+_id+" marked as deleted in database");
					showSpacesList();						
				} else {
					LogController.log("Space " + _id + " not deleted");
				}
			}			
		});
		btnDeleteSpace.setText("Delete");
		btnDeleteSpace.setFont(C.BUTTON_FONT);
		FormData fd_btnDeleteSpace = new FormData();
		fd_btnDeleteSpace.top = new FormAttachment(titleBar,titleH/5);
		fd_btnDeleteSpace.right = new FormAttachment(btnViewSpaceDetails,-5);
		btnDeleteSpace.setLayoutData(fd_btnDeleteSpace);
		btnDeleteSpace.setEnabled(false);

		btnSpacesList = new Button(titleBar, SWT.PUSH);
		btnSpacesList.setToolTipText("View the list of Enclosed Spaces");
		btnSpacesList.setImage(C.getImage("16c_list.png"));
		btnSpacesList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showSpacesList();
			}
		});
		btnSpacesList.setText("Spaces List");
		btnSpacesList.setFont(C.BUTTON_FONT);
		FormData fd_btnSpacesList = new FormData();
		fd_btnSpacesList.top = new FormAttachment(titleBar,titleH/5);
		fd_btnSpacesList.right = new FormAttachment(btnAdmin,-25);
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
		logo.setImage(C.getImage("esm-logo-horiz.png"));
		logo.setBackground(C.APP_BGCOLOR);
		FormData fd = new FormData();
		fd.width = 250;
		fd.left = new FormAttachment(0, 5);
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
		fd_lblH.left = new FormAttachment(logo, 10);
		fd_lblH.top = new FormAttachment((headerH/2)-10);
		lblH.setLayoutData(fd_lblH);
		lblH.setFont(C.HEADER_FONT);
		lblH.setAlignment(SWT.LEFT);
		lblH.setBackground(C.APP_BGCOLOR);
		lblH.setText(txt);

		lblVtLogo = new Label(header, SWT.NONE);
		lblVtLogo.setImage(C.getImage("vt_web_logo.png"));
		lblVtLogo.setBackground(C.APP_BGCOLOR);
		FormData fd_lblVtLogo = new FormData();
		fd_lblVtLogo.top = new FormAttachment(25);
		fd_lblVtLogo.right = new FormAttachment(100, -10);
		lblVtLogo.setLayoutData(fd_lblVtLogo);

		// read text from disk
		txt = C.APP_NAME;
		try {
			txt += "\tVersion " + CharStreams.toString(new InputStreamReader(this.getClass().getResourceAsStream("/txt/version.txt"), Charsets.UTF_8));
		} catch (IOException e) {
			LogController.logEvent(me,C.WARNING,e);
		}		
		Label lblF = new Label(footer,SWT.HORIZONTAL);
		lblF.setAlignment(SWT.CENTER);
		lblF.setBackground(C.TITLEBAR_BGCOLOR);
		lblF.setFont(C.FONT_8);
		lblF.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblF.setText(txt + C.COPYRIGHT);				

		if(user.getAccessLevel()==9) {
			btnAdmin.setEnabled(true);
		}

		FormData fd_foo = new FormData();
		fd_foo.right = new FormAttachment(120);
		foo.setLayoutData(fd_foo);
		shell.setDefaultButton(foo);

		showSpacesList();
	}

	// methods to display pages etc
	void showSpacesList(){
		LogController.log("Displaying Space List page");
		try {
			rows = SpacesTable.getRows("DELETED=FALSE");
		} catch (SQLException e) {
			LogController.logEvent(me, C.ERROR, e);
		}
		SpacesListView.getTVB().setInput(Arrays.asList(rows));
		//onlineStatus.setEnabled(InternetController.checkNetAccess());
		btnAddSpace.setVisible(true);
		btnDeleteSpace.setVisible(true);
		btnDeleteSpace.setEnabled(false);
		btnViewSpaceDetails.setVisible(true);
		btnViewSpaceDetails.setEnabled(false);
		btnSpacesList.setVisible(false);
		btnAdmin.setEnabled(true);
		stackLayout.topControl = pageSpacesList;
		pageTitle.setText(C.SPACES_LIST_TITLE);
		SpacesListView.getTV().refresh(true);
		formHolder.layout();
	}

	public static void showAdministration() {
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		onlineStatus.setEnabled(InternetController.checkNetAccess());
		btnAddSpace.setVisible(false);	
		btnViewSpaceDetails.setVisible(false);
		btnDeleteSpace.setVisible(false);
		btnSpacesList.setVisible(true);
		btnAdmin.setEnabled(false);
		LogController.log("Displaying Administration page");
		AdministrationView.buildPage(pageAdministration);
		stackLayout.topControl = pageAdministration;
		pageTitle.setText(C.ADMIN_PAGE_TITLE);
		formHolder.layout();
	}
	public static void showSpaceDetail(int spaceID) {
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		//onlineStatus.setEnabled(InternetController.checkNetAccess());
		currentSpaceId = spaceID;
		btnAddSpace.setVisible(false);
		btnViewSpaceDetails.setVisible(false);
		btnDeleteSpace.setVisible(false);
		btnSpacesList.setVisible(true);
		btnAdmin.setEnabled(true);
		LogController.log("Loading Space Detail page for user selection: Space ID "+spaceID);
		SpaceDetailView.buildPage(pageSpaceDetail, spaceID);
		stackLayout.topControl = pageSpaceDetail;
		try {
			pageTitle.setText("Space " + spaceID + ": " + SpacesTable.getRow("ID", ""+spaceID).getName());
		} catch (SQLException e) {
			LogController.logEvent(me, C.WARNING, e);
		}
		formHolder.layout();
	}
	public static void showSpaceAuditChecklist(int spaceID) {
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		//onlineStatus.setEnabled(InternetController.checkNetAccess());
		currentSpaceId = spaceID;
		btnAddSpace.setVisible(false);	
		btnViewSpaceDetails.setVisible(false);
		btnDeleteSpace.setVisible(false);
		btnSpacesList.setVisible(false);
		btnAdmin.setEnabled(true);
		LogController.log("Displaying Internal Space Audit Checklist for ID:" + spaceID);
		SpaceAuditChecklistView.buildPage(pageSpaceAudit, spaceID);
		stackLayout.topControl = pageSpaceAudit;
		String title = C.SPACE_AUDIT_CHECKLIST_PAGE_TITLE;
		try {
			title += " for " + SpacesTable.getRow(spaceID).getName();
		} catch (SQLException ex) {}
		pageTitle.setText(title);
		formHolder.layout();
	}
	public static void showSpaceAuditClassification(int spaceID) {
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		//onlineStatus.setEnabled(InternetController.checkNetAccess());
		currentSpaceId = spaceID;
		btnAddSpace.setVisible(false);	
		btnViewSpaceDetails.setVisible(false);
		btnDeleteSpace.setVisible(false);
		btnSpacesList.setVisible(false);
		btnAdmin.setEnabled(true);
		LogController.log("Displaying Internal Space Audit Classification for ID:" + spaceID);
		SpaceAuditClassificationView.buildPage(pageSpaceAudit, spaceID);
		stackLayout.topControl = pageSpaceAudit;
		String title = C.SPACE_AUDIT_CLASSIFICATION_PAGE_TITLE;
		try {
			title += " for " + SpacesTable.getRow(spaceID).getName();
		} catch (SQLException ex) {}
		pageTitle.setText(title);
		formHolder.layout();
	}
	public static void showEntryAuditChecklist(int entryID) {
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		//onlineStatus.setEnabled(InternetController.checkNetAccess());
		btnAddSpace.setVisible(false);	
		btnViewSpaceDetails.setVisible(false);
		btnDeleteSpace.setVisible(false);
		btnSpacesList.setVisible(false);
		btnAdmin.setEnabled(true);
		LogController.log("Displaying Entry Point Audit Checklist for ID:" + entryID);
		EntryAuditChecklistView.buildPage(pageEntryAudit, entryID);
		stackLayout.topControl = pageEntryAudit;
		String title = C.ENTRY_AUDIT_CHECKLIST_PAGE_TITLE;
		try {
			title += " for " + EntrypointsTable.getRow(entryID).getName();
		} catch (SQLException ex) {}
		pageTitle.setText(title);
		formHolder.layout();
	}
	public static void showEntryAuditClassification(int entryID) {
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		//onlineStatus.setEnabled(InternetController.checkNetAccess());
		btnAddSpace.setVisible(false);	
		btnViewSpaceDetails.setVisible(false);
		btnDeleteSpace.setVisible(false);
		btnSpacesList.setVisible(false);
		btnAdmin.setEnabled(true);
		LogController.log("Displaying Entry Point Audit Classification for ID:" + entryID);
		EntryAuditClassificationView.buildPage(pageEntryAudit, entryID);
		stackLayout.topControl = pageEntryAudit;
		String title = C.ENTRY_AUDIT_CLASSIFICATION_PAGE_TITLE;
		try {
			title += " for " + EntrypointsTable.getRow(entryID).getName();
		} catch (SQLException ex) {}
		pageTitle.setText(title);
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
	public static void showPhotoViewer(int spaceID, String fullPath, String thumbPath) {
		new PhotoViewer(shell, fullPath, thumbPath, spaceID);			
	}


}
