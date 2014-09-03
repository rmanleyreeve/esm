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
import com.itextpdf.text.DocumentException;
import com.rmrdigitalmedia.esm.AppLoader;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.LicenseTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;
import com.rmrdigitalmedia.esm.models.VesselCategoriesTable;
import com.rmrdigitalmedia.esm.models.VesselTable;
import com.rmrdigitalmedia.esm.models.VesselTypesTable;
import com.rmrdigitalmedia.esm.test.PdfTest;

public class DatabaseController {

	public ProgressBar bar;

	public DatabaseController() {
		LogController.log("Running class " + this.getClass().getName());
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			LogController.logEvent(DatabaseController.class, C.FATAL, "H2 Driver error", e);
		}
	}

	public void checkDB() {
		LogController.log("Checking Database...");
		if (!testConnection()) {
			LogController.log("DB DOES NOT EXIST!");
			createDB();
		} else {
			LogController.log("DB EXISTS");
		}
		LogController.log("DB check completed.");
	}

	public boolean testConnection() {
		boolean ok = false;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(C.DB_CONN_STR, "sa", "");
			ok = true;
			close(conn);
		} catch (SQLException e) {
			LogController.logEvent(DatabaseController.class, C.FATAL, "testConnection", e);
		}
		return ok;
	}

	public static Connection createConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(C.DB_CONN_STR, "sa", "");
		} catch (SQLException e) {
			LogController.logEvent(DatabaseController.class, C.FATAL, "createConnection", e);
		}
		return conn;
	}

	public void createDB() {
		LogController.log("Creating new DB... ");
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(C.DB_CONN_STR_SETUP, "sa", "");
			LogController.log("OK");
			loadRunSqlFile("SETUP.sql");

			// TODO for development ONLY
			//loadRunSqlFile("DEMO.sql");

		} catch (SQLException e) {
			LogController.logEvent(DatabaseController.class, C.FATAL, "DB SETUP FAILED", e);
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
			LogController.logEvent(DatabaseController.class, C.ERROR, e);
		}
		try {
			runQuery(sql);
			LogController.log("OK");
		} catch (SQLException e) {
			LogController.logEvent(DatabaseController.class, C.ERROR, "SQL LOAD FAILED", e);
			AppLoader.die("Failed to load SQL file '" + fName + "'");
		}
	}

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
			LogController.logEvent(DatabaseController.class, C.FATAL, "login verify", e);
			e.printStackTrace();
		} finally {
			close(conn);
		}
		return ok;
	}

	// DB BINARY FILE METHODS =============================================================================================================

	public static int insertDocument(File f, int spaceID, int authorID) throws FileNotFoundException {
		long id = 0;
		String mimeType = FilesystemController.getMimeType(f);
		Connection conn = createConnection();
		String sql = "INSERT INTO DOC_DATA (DATA, SPACE_ID, TITLE, MIME_TYPE, AUTHOR_ID, CREATED_DATE,REMOTE_IDENTIFIER) VALUES (?,?,?,?,?,?,?)";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			InputStream is = new FileInputStream (f);
			ps.setBinaryStream (1, is, (int)f.length());
			ps.setInt(2, spaceID);
			ps.setString(3, f.getName());
			ps.setString(4, mimeType);
			ps.setInt(5, authorID);
			ps.setTimestamp(6, new Timestamp(new Date().getTime()));
			ps.setString(7, (String) EsmApplication.appData.getField("LICENSE"));
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getLong(1);
			}			
		} catch (SQLException ex) {
			LogController.logEvent(DatabaseController.class, C.ERROR, "Error inserting document", ex);
			ex.printStackTrace();
		}
		LogController.log("Document inserted into DB OK");			
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
		String mimeType = FilesystemController.getMimeType(f);
		Connection conn = createConnection();
		BufferedImage bimg = ImageIO.read(f);
		int srcW = bimg.getWidth();
		int srcH = bimg.getHeight();
		OutputStream osF = new ByteArrayOutputStream();
		try {
			String sql = "INSERT INTO PHOTO_DATA (DATA_FULL, DATA_THUMB, MIME_TYPE, CREATED_DATE, REMOTE_IDENTIFIER) VALUES (?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			InputStream is = null;
			int l = 0;
			// full size image
			if (srcW > C.IMG_WIDTH || srcH > C.IMG_HEIGHT) {
				// larger image, resize
				try {
					Thumbnails.of(f).outputQuality(0.5).size(C.IMG_WIDTH, C.IMG_HEIGHT).toOutputStream(osF);
					ByteArrayOutputStream baos = (ByteArrayOutputStream) osF;
					is = new ByteArrayInputStream((baos).toByteArray());
					l = baos.size();
				} catch (IOException ex) { 
					LogController.logEvent(DatabaseController.class, C.ERROR, "insert full image", ex);
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
				LogController.logEvent(DatabaseController.class, C.ERROR, "insert thumbnail", ex);				
			}
			ps.setBinaryStream (2, is, l);
			ps.setString(3, mimeType);
			ps.setTimestamp(4, new Timestamp(new Date().getTime()));
			ps.setString(5, (String) EsmApplication.appData.getField("LICENSE"));
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getLong(1);
			}			
			LogController.log("Image data inserted into database");			
		} catch (SQLException e) {
			LogController.logEvent(DatabaseController.class, C.ERROR, "Error inserting image data", e);
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


	// DB EXPORT METHODS =============================================================================================================

	public static File generateZipFile() {
		boolean csv = false;
		boolean pdf = true;
		String license = "";
		try {
			license = LicenseTable.getAllRows()[0].getLicensekey();
		} catch (SQLException ex) {
			LogController.logEvent(DatabaseController.class, C.FATAL, "Could not get license key", ex);
		}
		String dir = C.TMP_DIR + C.SEP + license;
		
		// CSV data from DB tables
		try {
			Connection conn = createConnection();
			String[] tables = {
					"DOC_DATA",
					"PHOTO_DATA",
					"VESSEL",
					"LICENSE",
					"ESM_USERS",
					"SPACES",
					"ENTRYPOINTS",
					"SPACE_COMMENTS",
					"PHOTO_METADATA",
					"SPACE_CHECKLIST_AUDIT",
					"ENTRYPOINT_CHECKLIST_AUDIT",
					"SPACE_CLASSIFICATION_AUDIT",
					"ENTRYPOINT_CLASSIFICATION_AUDIT"
			};
			for(String table:tables) {
				String fn = dir + C.SEP + table + ".csv";
				String sql = "CALL CSVWRITE('" + fn + "', 'SELECT * FROM "+table+"', 'charset=UTF-8');";
				Statement stmt = conn.createStatement();
				stmt.executeQuery(sql);
				stmt.close();
				csv = true;
				LogController.log("Exported: " + fn);
			}
			close(conn);
		} catch (SQLException ex) {
			LogController.logEvent(DatabaseController.class, C.FATAL, "Error creating CSV file", ex);		
		}
		
		// PDF docs
		PdfController.setPath(dir);
		try {
			for(SpacesTable.Row sRow:SpacesTable.getRows("DELETED=FALSE")) {
				if(!sRow.isSignoffIDNull()) {
					if(!PdfController.buildAudit(sRow.getID())) {
						pdf = false;
					}
				}
			}
		} catch (SQLException e) {
			LogController.logEvent(PdfTest.class, C.ERROR, "Error getting DB data for PDF document", e);
		} catch (DocumentException e) {
			LogController.logEvent(PdfTest.class, C.ERROR, "Error getting PDF document", e);
		}				
		
		// zip up folder
		File zf = null;
		if(pdf && csv) {
			zf = ZipController.createZipFile(new File(dir));
		}		
		return zf;
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
			LogController.log("Full DB Backup exported to: " + fn);
		} catch (SQLException ex) {
			LogController.logEvent(DatabaseController.class, C.ERROR, "Error dumping database", ex);		}
		return ok;
	}

	// DB CHECK METHODS =============================================================================================================

	public static boolean checkAdmin() {
		boolean ok = false;
		LogController.log("Checking Admin User...");
		try {
			EsmUsersTable.Row[] rows = EsmUsersTable.getRows("ACCESS_LEVEL=9 AND DELETED=FALSE");
			LogController.log("Found: " + rows.length);
			if (rows.length == 1) {
				EsmUsersTable.Row row = rows[0];
				ok = true;
				LogController.log("Admin Found");
				EsmApplication.appData.setField("ADMIN", row.getUsername());
			} else {
				LogController.log("Admin NOT Found");
			}
		} catch (SQLException e) {
			LogController.logEvent(DatabaseController.class, C.ERROR, "Admin check", e);
			e.printStackTrace();
		}
		return ok;
	}

	public static boolean checkVessel() {
		boolean ok = false;
		LogController.log("Checking Vessel/Installation Details...");
		try {
			VesselTable.Row[] rows = VesselTable.getAllRows();
			LogController.log("Found " + rows.length);
			if (rows.length == 1) {
				VesselTable.Row row = rows[0];
				ok = true;
				String vName = row.getName();
				String cName = VesselCategoriesTable.getRow(VesselTypesTable.getRow(row.getTypeID()).getCategoryID()).getName();
				LogController.log("Vessel/Installation " + vName + " Found");
				EsmApplication.appData.setField("LOCATION_NAME", vName);
				EsmApplication.appData.setField("LOCATION_TYPE", cName);

			} else {
				LogController.log("Vessel NOT Found");
			}
		} catch (SQLException e) {
			LogController.logEvent(DatabaseController.class, C.ERROR, "Vessel check", e);
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
			LogController.log("Found: " + rows.length);
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
			LogController.logEvent(DatabaseController.class, C.ERROR, "License check", e);
		}
		return ok;
	}

	// DB utility methods =================================================================================================
	public static ResultSet getResultSet(Connection conn, String sql)
			throws SQLException {
		ResultSet rs;
		PreparedStatement st = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
				LogController.logEvent(DatabaseController.class, C.WARNING, e);
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
				LogController.logEvent(DatabaseController.class, C.WARNING, e);
			}
		}
	}









	/*
	public static boolean exportSql() {
		boolean ok = false;
		String license = "";
		try {
			license = LicenseTable.getAllRows()[0].getLicensekey();
		} catch (SQLException ex) {
			LogController.logEvent(DatabaseController.class, C.FATAL, "Could not get license key", ex);
		}
		String fn = C.TMP_DIR + C.SEP;
		fn +=  license + "_"; 
		fn += new Date().getTime() + "_export.sql";
		String sql = "SCRIPT NOSETTINGS TO '"+fn+"' CHARSET 'UTF-8' TABLE ";
		sql +="VESSEL,LICENSE,SPACES,ENTRYPOINTS,SPACE_COMMENTS,DOC_DATA,PHOTO_DATA,PHOTO_METADATA,SPACE_CHECKLIST_AUDIT,ENTRYPOINT_CHECKLIST_AUDIT,SPACE_CLASSIFICATION_AUDIT,ENTRYPOINT_CLASSIFICATION_AUDIT";
		System.out.println(sql);
		try {
			Connection conn = createConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.execute();
			st.close();
			close(conn);
			ok = true;
		} catch (SQLException e) {
			LogController.logEvent(DatabaseController.class, C.ERROR, "Error exporting SQL file", e);
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
			LogController.log("DB script file exported to: " + fn);
		} catch (SQLException ex) {
			LogController.logEvent(DatabaseController.class, C.ERROR, "Error exporting DB script file", ex);
		}
		return ok;
	}

	public static File exportDataFile() {
		File f = null;
		String license = "";
		try {
			license = LicenseTable.getAllRows()[0].getLicensekey();
		} catch (SQLException ex) {
			LogController.logEvent(DatabaseController.class, C.FATAL, "Could not get license key", ex);
		}
		String fn = C.TMP_DIR + C.SEP;
		fn +=  license + "_"; 
		fn += new Date().getTime() + "_export.zip";
		String[] bkp = {
				"-url", C.DB_CONN_STR, 
				"-user", "sa", 
				"-password","", 
				"-script", fn, 
				"-options", "compression", "zip"
		};
		try {
			org.h2.tools.Script.main(bkp);
			LogController.log("DB script file exported to: " + fn);
			f = new File(fn);
		} catch (SQLException ex) {
			LogController.logEvent(DatabaseController.class, C.ERROR, "Error exporting DB script file", ex);
		}
		return f;
	}

	public static File exportSqlFile() {
		File f = null;
		String license = "";
		try {
			license = LicenseTable.getAllRows()[0].getLicensekey();
		} catch (SQLException ex) {
			LogController.logEvent(DatabaseController.class, C.FATAL, "Could not get license key", ex);
		}
		String fn = C.TMP_DIR + C.SEP;
		fn +=  license + "_"; 
		fn += new Date().getTime() + "_export.sql";
		String sql = "SCRIPT NOSETTINGS TO '"+fn+"' CHARSET 'UTF-8' TABLE ";
		sql +="LICENSE,VESSEL,SPACES,ENTRYPOINTS,SPACE_COMMENTS,DOC_DATA,PHOTO_DATA,PHOTO_METADATA,SPACE_CHECKLIST_AUDIT,ENTRYPOINT_CHECKLIST_AUDIT,SPACE_CLASSIFICATION_AUDIT,ENTRYPOINT_CLASSIFICATION_AUDIT";
		System.out.println(sql);
		try {
			Connection conn = createConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.execute();
			st.close();
			close(conn);
			LogController.log("DB script file exported to: " + fn);
			f = new File(fn);
		} catch (SQLException e) {
			LogController.logEvent(DatabaseController.class, C.ERROR, "Error exporting SQL file", e);
		}
		return f;
	}
	 */


}
