package com.rmrdigitalmedia.esm.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.eclipse.swt.*;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.wb.swt.SWTResourceManager;

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.LogController;

public class Test {
	
	static String datadir = "ESM Data";
	static String sep = "/";
	static String home = System.getProperty("user.home");	
	static String current = System.getProperty("user.dir");
	static File imgdir;
	
	static Shell shell;
	static Display display;
	private static MenuItem item_1;
	static String text = "";
	
	
	public static void main (String [] args) {
		
		// set up filesystem
		LogController.log("Platform: " + SWT.getPlatform());
		if(SWT.getPlatform() == ("win32")) {
			sep = "\\";
		}
		LogController.log("PWD: "+current);
		File dir = new File(home + sep + datadir);
		LogController.log("App Data Dir: " + dir);

		if(dir.mkdir() ) {
			LogController.log("Data folder: " + dir);
		}		
		imgdir = new File(dir + sep + "images");
		if(imgdir.mkdir() ) {
			LogController.log("Images folder: " + imgdir);
		}	

		// build main app window
		display = new Display ();
		shell = new Shell (display);
		shell.setText("ESM App Test");
		
		// size & position
		shell.setSize(800, 600);
		//shell.setSize(bounds.width-100, bounds.height-100); // almost fill screen
		Rectangle rect = shell.getBounds ();
		LogController.log("Size: " + rect.width + ":" + rect.height);
		Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation (x, y);
		
		// set UI styling etc
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		//Image bgimg = new Image(display, new Test().getClass().getClassLoader().getResource("assets/bg.gif").getFile());
		//shell.setBackgroundImage(bgimg);	
		
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		shell.setLayout(gridLayout);
		
		
		
		// set up menu bar
		Menu bar = new Menu (shell, SWT.BAR);
		shell.setMenuBar (bar);
		
		// set up menu items
		MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
		fileItem.setText ("&File");
		Menu submenu1 = new Menu (shell, SWT.DROP_DOWN);
		fileItem.setMenu (submenu1);
		MenuItem item = new MenuItem (submenu1, SWT.PUSH);
		item.addListener (SWT.Selection, new Listener () {
			@Override
			public void handleEvent (Event e) {
				System.out.println ("Select All");
			}
		});
		item.setText ("Select &All\tCtrl+A");
		item.setAccelerator (SWT.MOD1 + 'A');

		item = new MenuItem (submenu1, SWT.PUSH);
		item.addListener (SWT.Selection, new Listener () {
			@Override
			public void handleEvent (Event e) {
				System.out.println (C.EXIT_MSG);
        shell.getDisplay().dispose();
        System.exit(0);
			}
		});
		item.setText ("&Quit\tCtrl+Q");
		item.setAccelerator (SWT.MOD1 + 'Q');
		
		MenuItem dataItem = new MenuItem(bar, SWT.CASCADE);
		dataItem.setText("&Data");
		Menu submenu2 = new Menu (shell, SWT.DROP_DOWN);
		dataItem.setMenu (submenu2);
		item_1 = new MenuItem (submenu2, SWT.PUSH);
		item_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
      	try {
					showDBX();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		item_1.setText ("DatabaseController Query");
		
		MenuItem mntmExport = new MenuItem(bar, SWT.CASCADE);
		mntmExport.setText("&Export");
		
		Menu menu = new Menu(mntmExport);
		mntmExport.setMenu(menu);
		
		MenuItem mntmAdministration = new MenuItem(bar, SWT.CASCADE);
		mntmAdministration.setText("&Administration");

		
		
		
		
		
		
		
		
	shell.open ();				
    
    Button btnNetConnect = new Button(shell, SWT.NONE);
    btnNetConnect.setToolTipText("Test network connection");
    btnNetConnect.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
    btnNetConnect.setText("Net Connect");
    btnNetConnect.setBounds(274, 514, 100, 30);   
    btnNetConnect.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent arg0) {
    		try {
				doConnect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    });
		
		// file upload button
    Button browse = new Button(shell, SWT.PUSH);
    browse.setText("Upload Image");   
    browse.setBounds(574, 514, 100, 30);   
    browse.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
      	uploadImage();
      }
    });

		// db query button
    Button db = new Button(shell, SWT.PUSH);
    db.setToolTipText("Run a query on the database");
    db.setFont(SWTResourceManager.getFont("Lucida Grande", 10, SWT.NORMAL));
    db.setImage(SWTResourceManager.getImage(Test.class, "/com/sun/java/swing/plaf/windows/icons/Inform.gif"));
    db.setText("DatabaseController Query");   
    db.setBounds(400, 514, 150, 30);   
    db.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
      	try {
					showDBX();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
      }
    });	   
    
    // quit button
    Button quit = new Button(shell, SWT.PUSH);
    quit.setFont(SWTResourceManager.getFont("Lucida Grande", 10, SWT.NORMAL));
    quit.setImage(SWTResourceManager.getImage(Test.class, "/com/sun/java/swing/plaf/windows/icons/Warn.gif"));
    quit.setText("Quit");   
    quit.setBounds(702, 514, 80, 30);
    quit.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
          shell.getDisplay().dispose();
          System.exit(0);
      }
    });
		
		
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}	
	
	
	public static void showDBX() throws SQLException {	
		Runnable job = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Class.forName("org.h2.Driver");
				} catch (ClassNotFoundException e) {
					LogController.log(e.toString());
				}
				Connection conn = null;
				try {
					conn = DriverManager.getConnection("jdbc:h2:~/"+datadir+"/esm", "sa", "");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				Statement stmt = null;
				try {
				    stmt = conn.createStatement();
				    String sql = "CREATE TABLE IF NOT EXISTS Test(ID INT PRIMARY KEY, NAME VARCHAR(255)); INSERT INTO TEST VALUES(1, 'Hello'); INSERT INTO TEST VALUES(2, 'World');";
				    stmt.execute(sql);
				} catch (SQLException e) {
					//
				}
				try {
					String result = "";
				    stmt = conn.createStatement();
				    ResultSet rs = stmt.executeQuery("SELECT * FROM Test ORDER BY ID;");
				    while (rs.next()) {
				    	result += rs.getString("NAME") + ", ";
				    }
				    text = result;
				} catch (SQLException e ) {
					LogController.log(e.toString());
				}
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		};
		BusyIndicator.showWhile(display, job);
	    doAlert(text);
	    text = "";
	}
	
	public static void doAlert(String msg) {		
	    MessageBox mb = new MessageBox(shell, SWT.OK);
	    mb.setText("Alert");
	    mb.setMessage(msg);
	    mb.open();		
	}
	
	public static void uploadImage() {	
		final FileDialog dialog = new FileDialog (shell, SWT.OPEN);
		dialog.setText("Choose an image");
		String platform = SWT.getPlatform();
		String [] filterNames = new String [] {"Image Files", "All Files (*)"};
		String [] filterExtensions = new String [] {"*.gif;*.png;*.xpm;*.jpg;*.jpeg;*.tiff", "*"};
		String filterPath = home + sep + "Desktop";
		if (platform.equals("win32") || platform.equals("wpf")) {
			filterNames = new String [] {"Image Files", "All Files (*.*)"};
			filterExtensions = new String [] {"*.gif;*.png;*.bmp;*.jpg;*.jpeg;*.tiff", "*.*"};
		}
		dialog.setFilterNames (filterNames);
		dialog.setFilterExtensions (filterExtensions);
		dialog.setFilterPath (filterPath);
		dialog.open();		
		final String fn = dialog.getFilterPath() + sep + dialog.getFileName();	
		
		if(!dialog.getFileName().equals("")) {
			try {  				
			    File src = new File(fn);   
			    final File dest = new File(imgdir + sep + dialog.getFileName());   	      
			    final FileInputStream is = new FileInputStream(src);   
			    final FileOutputStream os = new FileOutputStream(dest);   	
				LogController.log(src);
			    LogController.log(src.length());  
			    LogController.log(dest);
			    Runnable job = new Runnable() {
			    	@Override
					public void run() {
				      try {  
					      int currentbyte = is.read();  
					      while (currentbyte != -1) {  
						      os.write (currentbyte);  
						      currentbyte = is.read();  
					      }
						} catch (IOException ex){
							LogController.log(ex.toString());
						}
						LogController.log(dest.length()); 
						try {
							is.close();
							os.close();   
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}   
					}
				};
				BusyIndicator.showWhile(display, job);
			    doAlert("Image copied, " + dest.length() + " bytes");				
			} catch(IOException ex){
				LogController.log(ex.toString());
			} 
		}
	}		
		
	 
	 
	public final static String USER_AGENT = "Mozilla/5.0";
 
	public static void doConnect() throws Exception {	 	 
		LogController.log("Testing 1 - Send Http GET request");
		sendGet();
 
		LogController.log("\nTesting 2 - Send Http POST request");
		sendPost();	 
	}
 
	// HTTP GET request
	private static void sendGet() throws Exception {
 
		String url = "http://www.google.com/search?q=thyn+lizzy";
 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		LogController.log("\nSending 'GET' request to URL : " + url);
		LogController.log("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		renderHTML(response.toString());	 
	}
 
	// HTTP POST request
	private static void sendPost() throws Exception {	 
		String url = "https://selfsolve.apple.com/wcResults.do";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();	 
		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");	 
		String urlParameters = "sn=C02G6140DRJM&cn=&locale=&caller=&num=12345";	 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();	 
		int responseCode = con.getResponseCode();
		LogController.log("\nSending 'POST' request to URL : " + url);
		LogController.log("Post parameters : " + urlParameters);
		LogController.log("Response Code : " + responseCode);	 
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();	 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();	 
		//print result
		renderHTML(response.toString());
	}
	 
		
	
	public static void renderHTML(String html) {
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Browser browser;
		try {
			browser = new Browser(shell, SWT.NONE);
		} catch (SWTError e) {
			LogController.log("Could not instantiate Browser: " + e.getMessage());
			display.dispose();
			return;
		}
		browser.setText(html);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		//display.dispose();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
