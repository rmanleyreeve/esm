package com.rmrdigitalmedia.esm.controllers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.swt.widgets.ProgressBar;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.rmrdigitalmedia.esm.AppLoader;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.LicenseTable;
import com.rmrdigitalmedia.esm.models.VesselTable;

public class DatabaseController {

	public ProgressBar bar;
	private static Object me;

	public DatabaseController() {
		me = this;
		LogController.log("Running class " + this.getClass().getName());
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			LogController.logEvent(me,C.FATAL,"H2 Driver error",e);
		}
	}

	public void checkDB() {
		LogController.log("Checking DB...");
		if(!testConnection()) {
			LogController.log("DB DOES NOT EXIST!");		
			createDB();
		} else {
			LogController.log("DB EXISTS");
		}
		LogController.log("DB check completed");
	}

	public boolean testConnection() {
		boolean ok = false;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(C.DB_CONN_STR, "sa", "");
			ok = true;
			close(conn);
		} catch (SQLException e) {
			LogController.logEvent(me,C.FATAL,"testConnection",e);
		}
		return ok;
	}

	public static Connection createConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(C.DB_CONN_STR, "sa", "");
		} catch (SQLException e) {
			LogController.logEvent(me,C.FATAL,"createConnection",e);
		}
		return conn;
	}

	public void createDB() {
		LogController.log("Creating new DB... ");
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(C.DB_CONN_STR_SETUP, "sa", "");
			LogController.log("OK");

			// TODO FOR DEVELOPMENT ONLY <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			loadRunSqlFile("DEMO.sql"); 

			//loadRunSqlFile("SETUP.sql");
		} catch (SQLException e) {
			LogController.logEvent(me,C.FATAL,"DB SETUP FAILED",e);
			//e.printStackTrace();
			System.exit(0);
		}
		close(conn);
	}

	public void loadRunSqlFile(String fName) {
		LogController.log("Loading SQL file '"+fName+"'... ");
		String sql = "";
		try {
			sql = CharStreams.toString(new InputStreamReader(DatabaseController.class.getResourceAsStream("/sql/"+fName), Charsets.UTF_8));
		} catch (IOException e) {
			LogController.logEvent(me,C.ERROR,e);
		}
		try {
			runQuery(sql);
			LogController.log("OK");
		} catch (SQLException e) {
			LogController.logEvent(me,C.ERROR,"SQL LOAD FAILED",e);
			AppLoader.die("Failed to load SQL file '"+fName+"'");
		}
	}


	// DB check methods using entity classes where possible==============================================

	public static boolean verifyLogin(String username, String password) {
		boolean ok = false;
		Connection conn = createConnection();
		try {
			String sql = "SELECT ID FROM ESM_USERS WHERE (USERNAME='"+username+"' AND PASSWORD='"+password+"');";
			ResultSet rs = getResultSet(conn,sql);
			while (rs.next()) {
				ok = true;
			}
			close(rs);
		} catch (SQLException e ) {
			LogController.logEvent(me,C.FATAL,"login verify",e);
			e.printStackTrace();
		} finally {
			close(conn);
		}
		return ok;
	}	

	public static boolean checkAdmin() {
		boolean ok = false;
		LogController.log("Checking Admin User...");
		try {
			EsmUsersTable.Row[] rows = EsmUsersTable.getRows("ACCESS_LEVEL=9 AND DELETED=FALSE");
			LogController.log("checkAdmin Row Count: "+rows.length);
			if(rows.length==1){
				EsmUsersTable.Row row = rows[0];
				ok = true;	
				LogController.log("Admin Found");
				EsmApplication.appData.setField("ADMIN",row.getUsername());
			} else {
				LogController.log("Admin NOT Found");
			}			
		} catch (SQLException e ) {			
			LogController.logEvent(me,C.ERROR,"Admin check",e);
			e.printStackTrace();
		}
		return ok;	
	}

	public static boolean checkVessel() {
		boolean ok = false;
		LogController.log("Checking Vessel Details...");
		try {
			VesselTable.Row[] rows = VesselTable.getAllRows();
			LogController.log("checkVessel Row Count: "+rows.length);
			if(rows.length==1){
				VesselTable.Row row = rows[0];
				ok = true;	
				String vName = row.getName();
				LogController.log("Vessel "+vName+" Found");
				EsmApplication.appData.setField("VESSEL",vName);
			} else {
				LogController.log("Vessel NOT Found");
			}						
		} catch (SQLException e ) {
			LogController.logEvent(me,C.ERROR,"Vessel check",e);
			e.printStackTrace();
		}
		return ok;	
	}	

	public static boolean checkLicenseKey() {
		boolean ok = false;
		LogController.log("Checking License Key...");
		String key = "";
		try {
			LicenseTable.Row[] rows = LicenseTable.getRows("LICENSEKEY IS NOT NULL");
			LogController.log("checkLicenseKey Row Count: "+rows.length);
			if(rows.length==1) {
				LicenseTable.Row row = rows[0];
				key = row.getLicensekey();
				ok = true;
				LogController.log("License OK: " + key);
				EsmApplication.appData.setField("LICENSE",key);
			} else {
				LogController.log("License NOT found");
			}
		} catch (SQLException e ) {
			LogController.logEvent(me,C.ERROR,"License check",e);
		}
		return ok;	
	}

	// DB utility methods ==========================================================================
	public static ResultSet getResultSet(Connection conn,String sql) throws SQLException {
		ResultSet rs;
		PreparedStatement st = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		rs = st.executeQuery();
		return rs;
	}
	public static void runQuery(String sql) throws SQLException {
		Connection conn = createConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		st.executeUpdate();
		st.close();
		close(conn);
	}
	public static void close(Connection conn){
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e){
				LogController.logEvent(me, C.WARNING, e);
			}
		}
	}
	public static void close(ResultSet rs){
		if (rs != null) {
			try {
				Statement st = rs.getStatement();
				rs.close(); 
				st.close();    	
			} catch (SQLException e){
				LogController.logEvent(me, C.WARNING, e);
			}
		}
	}



}
