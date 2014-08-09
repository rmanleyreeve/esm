package com.rmrdigitalmedia.esm.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.rmrdigitalmedia.esm.AppLoader;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.LicenseTable;
import com.rmrdigitalmedia.esm.models.VesselCategoriesTable;
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
			LogController.logEvent(me, C.FATAL, "H2 Driver error", e);
		}
	}

	public void checkDB() {
		LogController.log("Checking DB...");
		if (!testConnection()) {
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
			LogController.logEvent(me, C.FATAL, "testConnection", e);
		}
		return ok;
	}

	public static Connection createConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(C.DB_CONN_STR, "sa", "");
		} catch (SQLException e) {
			LogController.logEvent(me, C.FATAL, "createConnection", e);
		}
		return conn;
	}

	public void createDB() {
		LogController.log("Creating new DB... ");
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(C.DB_CONN_STR_SETUP, "sa", "");
			LogController.log("OK");
			//loadRunSqlFile("SETUP.sql");

			// TODO for development ONLY
			loadRunSqlFile("DEMO.sql");

		} catch (SQLException e) {
			LogController.logEvent(me, C.FATAL, "DB SETUP FAILED", e);
			// e.printStackTrace();
			System.exit(0);
		}
		close(conn);
	}

	public void loadRunSqlFile(String fName) {
		LogController.log("Loading SQL file '" + fName + "'... ");
		String sql = "";
		try {
			sql = CharStreams.toString(new InputStreamReader(DatabaseController.class.getResourceAsStream("/sql/" + fName), Charsets.UTF_8));
		} catch (IOException e) {
			LogController.logEvent(me, C.ERROR, e);
		}
		try {
			runQuery(sql);
			LogController.log("OK");
		} catch (SQLException e) {
			LogController.logEvent(me, C.ERROR, "SQL LOAD FAILED", e);
			AppLoader.die("Failed to load SQL file '" + fName + "'");
		}
	}

	// DB methods using entity classes where possible==============================================

	public static boolean verifyLogin(String username, String password) {
		boolean ok = false;
		Connection conn = createConnection();
		try {
			String sql = "SELECT ID FROM ESM_USERS WHERE (USERNAME='" + username + "' AND PASSWORD='" + password + "');";
			ResultSet rs = getResultSet(conn, sql);
			while (rs.next()) {
				ok = true;
			}
			close(rs);
		} catch (SQLException e) {
			LogController.logEvent(me, C.FATAL, "login verify", e);
			e.printStackTrace();
		} finally {
			close(conn);
		}
		return ok;
	}

	public static int insertDocument(File f, int spaceID, int authorID) throws FileNotFoundException {
		long id = 0;
		Connection conn = createConnection();
		String sql = "INSERT INTO DOC_DATA (DATA, SPACE_ID, TITLE,AUTHOR_ID, CREATED_DATE) VALUES (?,?,?,?,?)";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			InputStream is = new FileInputStream (f);
			ps.setBinaryStream (1, is, (int)f.length());
			ps.setInt(2, spaceID);
			ps.setString(3, f.getName());
			ps.setInt(4, authorID);
			ps.setTimestamp(5, new Timestamp(new Date().getTime()));
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getLong(1);
			}			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		System.out.println("Document inserted into DB OK");			
		return (int) id;
	}

	public static File readDocument(int id) throws IOException {
		Connection conn = createConnection();
		File f = null;
		try {
			String sql = "SELECT DATA, TITLE FROM DOC_DATA WHERE ID=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				String title = new Date().getTime() + "_" + rs.getString("TITLE");
				Blob blob = rs.getBlob("DATA");
				byte [] array = blob.getBytes( 1, ( int ) blob.length() );
				f = new File(C.TMP_DIR + C.SEP + title);
				FileOutputStream out = new FileOutputStream(f);
				out.write(array);
				out.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn);
		}		
		return f;
	}

	public static int insertImageData(File f) throws IOException {
		long id = 0;
		Connection conn = createConnection();
		BufferedImage bimg = ImageIO.read(f);
		int srcW = bimg.getWidth();
		int srcH = bimg.getHeight();
		OutputStream osF = new ByteArrayOutputStream();
		try {
			String sql = "INSERT INTO PHOTO_DATA (DATA_FULL, DATA_THUMB, CREATED_DATE) VALUES (?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			InputStream is = null;
			int l = 0;
			// full size image
			if (srcW > C.IMG_WIDTH || srcH > C.IMG_HEIGHT) {
				// larger image, resize
				try {
					Thumbnails.of(f).size(C.IMG_WIDTH, C.IMG_HEIGHT).toOutputStream(osF);
					ByteArrayOutputStream baos = (ByteArrayOutputStream) osF;
					is = new ByteArrayInputStream((baos).toByteArray());
					l = baos.size();
				} catch (IOException ex) { 
					LogController.logEvent(me, C.ERROR, "insert full image", ex);
				}
			} else {
				is = new FileInputStream (f);
				l = (int) f.length();
			}
			ps.setBinaryStream (1, is, l);
			is = null;
			// thumbs
			OutputStream osT = new ByteArrayOutputStream();
			try {
				Thumbnails.of(f).size(C.THUMB_WIDTH, C.THUMB_HEIGHT).toOutputStream(osT);
				ByteArrayOutputStream baos = (ByteArrayOutputStream) osT;
				is = new ByteArrayInputStream((baos).toByteArray());
				l = baos.size();
			} catch (IOException ex) { 
				LogController.logEvent(me, C.ERROR, "insert thumbnail", ex);				
			}
			ps.setBinaryStream (2, is, l);
			ps.setTimestamp(3, new Timestamp(new Date().getTime()));
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getLong(1);
			}			
			System.out.println("Images inserted into DB OK");			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn);
		}
		return (int) id;
	}

	public static Image readImageDataFull(int id) {
		Connection conn = createConnection();
		Image img = null;
		try {
			String sql = "SELECT DATA_FULL FROM PHOTO_DATA WHERE ID=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				InputStream is = rs.getBinaryStream("DATA_FULL");
				img = new Image(Display.getCurrent(), is);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn);
		}		
		return img;
	}

	public static Image readImageDataThumb(int id) {
		Connection conn = createConnection();
		Image img = null;
		try {
			String sql = "SELECT DATA_THUMB FROM PHOTO_DATA WHERE ID=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				InputStream is = rs.getBinaryStream("DATA_THUMB");
				img = new Image(Display.getCurrent(), is);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn);
		}		
		return img;
	}

	public static boolean dumpDatabase() {
		boolean ok = false;
		try {
			Connection conn = createConnection();
			String fn = C.TMP_DIR + C.SEP + new Date().getTime() + "_dump.zip";
			String sql = "BACKUP TO '" + fn + "';";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			ok = true;
			System.out.println("Exported to " + fn);
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return ok;
	}

	public static boolean exportData() {
		boolean ok = false;
		String fn = C.TMP_DIR + C.SEP + new Date().getTime() + "_export.zip";
		String[] bkp = {
				"-url", C.DB_CONN_STR, 
				"-user", "sa", 
				"-password","", 
				"-script", fn, 
				"-options", "compression", "zip"
		};

		try {
			org.h2.tools.Script.main(bkp);
			ok = true;
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return ok;
	}

	public static boolean checkAdmin() {
		boolean ok = false;
		LogController.log("Checking Admin User...");
		try {
			EsmUsersTable.Row[] rows = EsmUsersTable.getRows("ACCESS_LEVEL=9 AND DELETED=FALSE");
			LogController.log("checkAdmin Row Count: " + rows.length);
			if (rows.length == 1) {
				EsmUsersTable.Row row = rows[0];
				ok = true;
				LogController.log("Admin Found");
				EsmApplication.appData.setField("ADMIN", row.getUsername());
			} else {
				LogController.log("Admin NOT Found");
			}
		} catch (SQLException e) {
			LogController.logEvent(me, C.ERROR, "Admin check", e);
			e.printStackTrace();
		}
		return ok;
	}

	public static boolean checkVessel() {
		boolean ok = false;
		LogController.log("Checking Vessel/Installation Details...");
		try {
			VesselTable.Row[] rows = VesselTable.getAllRows();
			LogController.log("checkVessel Row Count: " + rows.length);
			if (rows.length == 1) {
				VesselTable.Row row = rows[0];
				ok = true;
				String vName = row.getName();
				String tName = VesselCategoriesTable.getRow(row.getTypeID()).getName();
				LogController.log("Vessel/Installation " + vName + " Found");
				EsmApplication.appData.setField("LOCATION_NAME", vName);
				EsmApplication.appData.setField("LOCATION_TYPE", tName);

			} else {
				LogController.log("Vessel NOT Found");
			}
		} catch (SQLException e) {
			LogController.logEvent(me, C.ERROR, "Vessel check", e);
			e.printStackTrace();
		}
		return ok;
	}

	public static boolean checkLicenseKey() {
		boolean ok = false;
		LogController.log("Checking License Key...");
		String key = "";
		try {
			LicenseTable.Row[] rows = LicenseTable
					.getRows("LICENSEKEY IS NOT NULL");
			LogController.log("checkLicenseKey Row Count: " + rows.length);
			if (rows.length == 1) {
				LicenseTable.Row row = rows[0];
				key = row.getLicensekey();
				ok = true;
				LogController.log("License OK: " + key);
				EsmApplication.appData.setField("LICENSE", key);
			} else {
				LogController.log("License NOT found");
			}
		} catch (SQLException e) {
			LogController.logEvent(me, C.ERROR, "License check", e);
		}
		return ok;
	}

	// DB utility methods
	// ==========================================================================
	public static ResultSet getResultSet(Connection conn, String sql)
			throws SQLException {
		ResultSet rs;
		PreparedStatement st = conn.prepareStatement(sql,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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

	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				LogController.logEvent(me, C.WARNING, e);
			}
		}
	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				Statement st = rs.getStatement();
				rs.close();
				st.close();
			} catch (SQLException e) {
				LogController.logEvent(me, C.WARNING, e);
			}
		}
	}

}
