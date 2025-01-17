package com.rmrdigitalmedia.esm.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.models.EntrypointChecklistAuditTable;
import com.rmrdigitalmedia.esm.models.EntrypointsTable;
import com.rmrdigitalmedia.esm.models.SpaceChecklistAuditTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;

@SuppressWarnings("unused")
public class AuditController {

	public static void init() {
		// do initial audit calculations here
		long startTime = System.currentTimeMillis();
		LogController.log("Initial Space Audits calculation started");
		try {
			for (SpacesTable.Row sRow : SpacesTable.getRows("DELETED=FALSE")) {
				int spaceID = sRow.getID();
				AuditController.calculateSpaceChecklistCompletion(spaceID);
				AuditController.calculateSpaceClassificationCompletion(spaceID);
			}
			LogController.log("Space audits calculation completed in " + (System.currentTimeMillis() - startTime) + "ms");
			startTime = System.currentTimeMillis();
			LogController.log("Initial Entry audits calculation started");
			for (EntrypointsTable.Row epRow : EntrypointsTable.getRows("DELETED=FALSE")) {
				int entryID = epRow.getID();
				AuditController.calculateEntryChecklistCompletion(entryID);
				AuditController.calculateEntryClassificationCompletion(entryID);
			}
			LogController.log("Entry audits calculation completed in " + (System.currentTimeMillis() - startTime) + "ms");
		} catch (SQLException e1) {
			LogController.logEvent(AuditController.class, C.FATAL, "Initial Space audits calculation", e1);
		}
		// System.out.println("Elapsed: "+ (System.currentTimeMillis() - startTime));
		EsmApplication.appData.setField("INIT", 1);
		//LogController.log("INIT COMPLETE");
	}

	// INTERNAL SPACE CHECKLIST
	public static void calculateSpaceChecklistCompletion(int spaceID) throws SQLException {
		long startTime = System.currentTimeMillis();
		double percent = 0;
		int progress = 0;
		int max = 0;
		int score = 0;
		Connection conn = DatabaseController.createConnection();
		PreparedStatement ps = null;
		ResultSet row = null;
		String sql = "SELECT * FROM SPACE_CHECKLIST_AUDIT WHERE SPACE_ID=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, spaceID);
			row = ps.executeQuery();
		} catch (SQLException e) {
			LogController.logEvent(AuditController.class, C.FATAL, e);		
		}
		if(row!=null && row.next()) {			
			if (C.notNullOrEmpty(row.getString("Q1_DIMS_H"))
					&& C.notNullOrEmpty(row.getString("Q1_DIMS_L"))
					&& C.notNullOrEmpty(row.getString("Q1_DIMS_W"))) {
				score += 1;
			}
			String q2 = row.getString("Q2_BOOLEAN");
			if (isN(q2) || (isY(q2) && C.notNullOrEmpty(row.getString("Q2_DESC")))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q3_BOOLEAN"))) {
				score += 1;
			}
			String q4 = row.getString("Q4_BOOLEAN");
			if (isN(q4)
					|| (isY(q4) && C.notNullOrEmpty(row.getString("Q4_DIMS_H")) && C.notNullOrEmpty(row.getString("Q4_DIMS_W")))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q5_BOOLEAN"))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q6_BOOLEAN"))) {
				score += 1;
			}
			String q7 = row.getString("Q7_BOOLEAN");
			if (isN(q7) || (isY(q7) && row.getInt("Q7_RATING") != 0)) {
				score += 1; 
			}
			String q8 = row.getString("Q8_BOOLEAN");
			if (isN(q8) || (isY(q8) && row.getInt("Q8_RATING") != 0)) {
				score += 1;
			}
			String q9 = row.getString("Q9_BOOLEAN");
			if (isN(q9) || (isY(q9) && row.getInt("Q9_RATING") != 0)) {
				score += 1; 
			}
			String q10 = row.getString("Q10_BOOLEAN");
			if (isN(q10) || (isY(q10) && row.getInt("Q10_RATING") != 0)) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q11_BOOLEAN"))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q12_BOOLEAN"))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q13_BOOLEAN"))) {
				score += 1; 
			}
			if (C.notNullOrEmpty(row.getString("Q14_BOOLEAN"))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q15_BOOLEAN"))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q16_BOOLEAN"))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q17_BOOLEAN"))) {
				score += 1;
			}
			max = 17;
			if (q7 != null && isN(q7)) {
				max = 14;
			}
			percent = Math.round(((float) score / max) * 100);
			progress = (int) Math.floor(percent / 10) * 10;
			ps.close();
			row.close();
			conn.close();
		}		
		if(progress > 100) { progress = 100; }
		EsmApplication.appData.setField("SPACE_CHK_" + spaceID, progress);
		//LogController.log("Calculating space "+spaceID+" checklist: " + score + "/" + max + "=" + percent + "% -> progress=" + progress );
		//LogController.log("Elapsed: "+ (System.currentTimeMillis() - startTime));
	}

	// INTERNAL SPACE CLASSIFICATION
	public static void calculateSpaceClassificationCompletion(int spaceID) throws SQLException {
		long startTime = System.currentTimeMillis();
		double percent = 0;
		int progress = 0;
		int max = 0;
		int score = 0;
		ArrayList<Integer> status = new ArrayList<Integer>();
		String light = "null";		
		Connection conn = DatabaseController.createConnection();
		PreparedStatement ps = null;
		ResultSet row = null;
		String sql = "SELECT * FROM SPACE_CLASSIFICATION_AUDIT WHERE SPACE_ID=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, spaceID);
			row = ps.executeQuery();
		} catch (SQLException e) {
			LogController.logEvent(AuditController.class, C.FATAL, e);		
		}
		if(row!=null && row.next()) {			
			int q1 = row.getInt("Q1_VALUE");
			if (q1 != 0) {
				score += 1;
				status.add(q1);
			}
			int q2 = row.getInt("Q2_VALUE");
			if (q2 != 0) {
				score += 1;
				status.add(q2);
			}
			int q3 = row.getInt("Q3_VALUE");
			if (q3 != 0) {
				score += 1;
				status.add(q3);
			}
			int q4 = row.getInt("Q4_VALUE");
			if (q4 != 0) {
				score += 1;
				if (q4 == 5) {
					status.add(3);
				} else if (q4 == 1) {
					status.add(1);
				} else {
					status.add(2);
				}
			}
			int q5 = row.getInt("Q5_VALUE");
			if (q5 != 0) {
				score += 1;
				status.add(q5);
			}
			int q6 = row.getInt("Q6_VALUE");
			if (q6 != 0) {
				score += 1;
				status.add(q6);
			}
			String q7 = row.getString("Q7_BOOLEAN");
			if (C.notNullOrEmpty(q7)) {
				score += 1;
				if (isY(q7)) {
					status.add(1);
				} else if (isN(q7)) {
					status.add(3);
				}
			} // N = green
			String q8 = row.getString("Q8_BOOLEAN");
			if (C.notNullOrEmpty(q8)) {
				score += 1;
				if (isY(q8)) {
					status.add(1);
				} else if (isN(q8)) {
					status.add(3);
				}
			} // N = green
			max = 8;
			try {
				if (isN(SpaceChecklistAuditTable.getRow("SPACE_ID", ""+spaceID).getQ7Boolean())) {
					max = 7;
				}
			} catch (SQLException ex) {
			}
			percent = Math.round(((float) score / max) * 100);
			progress = (int) Math.floor(percent / 10) * 10;
			ps.close();
			row.close();
			conn.close();
		}
		if(status.contains(1)) {
			light = "red";
		}
		if (status.contains(2) && !status.contains(1)) {
			light = "amber";
		}
		if (status.contains(3) && !status.contains(1) && !status.contains(2)) {
			light = "green";
		}
		if(progress > 100) { progress = 100; }
		EsmApplication.appData.setField("SPACE_CLASS_" + spaceID, progress);
		EsmApplication.appData.setField("SPACE_STATUS_" + spaceID, light);
		//LogController.log("Calculating space "+spaceID+" classification: " + score + "/" + max + "=" + percent + "% -> progress=" + progress );
		//LogController.log("Space " + spaceID + " status array: " + status.toString());
		//LogController.log("Elapsed: "+ (System.currentTimeMillis() - startTime));
	}

	// =========================================================================================================================

	// ENTRY POINT CHECKLIST
	public static void calculateEntryChecklistCompletion(int entryID) throws SQLException {
		long startTime = System.currentTimeMillis();
		double percent = 0;
		int progress = 0;
		int max = 0;
		int score = 0;
		Connection conn = DatabaseController.createConnection();
		PreparedStatement ps = null;
		ResultSet row = null;
		String sql = "SELECT * FROM ENTRYPOINT_CHECKLIST_AUDIT WHERE ENTRYPOINT_ID=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, entryID);
			row = ps.executeQuery();
		} catch (SQLException e) {
			LogController.logEvent(AuditController.class, C.FATAL, e);		
		}
		if(row!=null && row.next()) {			
			String q1 = row.getString("Q1_VALUE");
			if (q1 != null) {
				score += 1;
			}
			if (q1 != null && q1.equals("OUTSIDE") && C.notNullOrEmpty(row.getString("Q2_BOOLEAN"))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q3_BOOLEAN"))) {
				score += 1;
			}
			if (row.getString("Q4_VALUE") != null) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q5_DIMS_H")) && C.notNullOrEmpty(row.getString("Q5_DIMS_W"))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q6_BOOLEAN"))) {
				score += 1;
			}
			String q7 = row.getString("Q7_VALUE");
			if (q7 != null) {
				score += 1;
			}
			if (q7 != null && q7.equals("VERTICAL")	&& row.getString("Q8_BOOLEAN") != null) {
				score += 1;
			}
			if (q7 != null && q7.equals("VERTICAL")	&& row.getString("Q9_BOOLEAN") != null) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q10_BOOLEAN"))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q11_BOOLEAN"))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q12_BOOLEAN"))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q13_BOOLEAN"))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q14_BOOLEAN"))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q15_BOOLEAN"))) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getString("Q16_BOOLEAN"))) {
				score += 1;
			}
			max = 16;
			if (q1 != null && q1.equals("INSIDE")) {
				max--;
			}
			if (q7 != null && q7.equals("HORIZONTAL")) {
				max--;
				max--;
			}
			percent = Math.round(((float) score / max) * 100);
			progress = (int) Math.floor(percent / 10) * 10;
			ps.close();
			row.close();
			conn.close();
		}
		if(progress > 100) { progress = 100; }
		EsmApplication.appData.setField("ENTRY_CHK_" + entryID, progress);
		//LogController.log("Calculating entry "+entryID+" checklist: " + score + "/" + max + "=" + percent + "% -> progress=" + progress);
		//LogController.log("Elapsed: "+ (System.currentTimeMillis() - startTime));
	}

	// ENTRY POINT CLASSIFICATION
	public static void calculateEntryClassificationCompletion(int entryID) throws SQLException {
		long startTime = System.currentTimeMillis();
		double percent = 0;
		int progress = 0;
		int max = 0;
		int score = 0;
		ArrayList<Integer> status = new ArrayList<Integer>();
		String light = "null";
		Connection conn = DatabaseController.createConnection();
		PreparedStatement ps = null;
		ResultSet row = null;
		String sql = "SELECT * FROM ENTRYPOINT_CLASSIFICATION_AUDIT WHERE ENTRYPOINT_ID=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, entryID);
			row = ps.executeQuery();
		} catch (SQLException e) {
			LogController.logEvent(AuditController.class, C.FATAL, e);		
		}
		if(row!=null && row.next()) {			
			int q1 = row.getInt("Q1_VALUE");
			if (q1 != 0) {
				score += 1;
				status.add(q1);
			}
			int q2 = row.getInt("Q2_VALUE");
			if (q2 != 0) {
				score += 1;
				status.add(q2);
			}
			String q3 = row.getString("Q3_BOOLEAN");
			if (C.notNullOrEmpty(q3)) {
				score += 1;
				if (isY(q3)) {
					status.add(3);
				} else if (isN(q3)) {
					status.add(1);
				}
			} // Y = green
			int q4 = row.getInt("Q4_VALUE");
			if (q4 != 0) {
				score += 1;
				if (q4 == 5) {
					status.add(3);
				} else if (q4 == 1) {
					status.add(1);
				} else {
					status.add(2);
				}
			}
			String q5 = row.getString("Q5_BOOLEAN");
			if (C.notNullOrEmpty(q5)) {
				score += 1;
				if (isY(q5)) {
					status.add(3);
				} else if (isN(q5)) {
					status.add(1);
				}
			} // Y = green
			String q6 = row.getString("Q6_BOOLEAN");
			if (C.notNullOrEmpty(q6)) {
				score += 1;
				if (isY(q6)) {
					status.add(3);
				} else if (isN(q6)) {
					status.add(1);
				}
			} // Y = green
			int q7 = row.getInt("Q7_VALUE");
			if (q7 != 0) {
				score += 1;
				status.add(q7);
			}				
			max = 7;
			String vh = EntrypointChecklistAuditTable.getRow("ENTRYPOINT_ID", ""+entryID).getQ7Value();
			if(vh!=null && vh.equals("HORIZONTAL") ) {
				max--;
			}
			percent = Math.round(((float) score / max) * 100);
			progress = (int) Math.floor(percent / 10) * 10;
			ps.close();
			row.close();
			conn.close();
		}
		if(status.contains(1)) {
			light = "red";
		}
		if (status.contains(2) && !status.contains(1)) {
			light = "amber";
		}
		if (status.contains(3) && !status.contains(1) && !status.contains(2)) {
			light = "green";
		}
		if(progress > 100) { progress = 100; }
		EsmApplication.appData.setField("ENTRY_CLASS_" + entryID, progress);
		EsmApplication.appData.setField("ENTRY_STATUS_" + entryID, light);
		//LogController.log("Calculating entry "+entryID+" classification: " + score + "/" + max + "=" + percent + "% -> progress=" + progress );
		//LogController.log("Entry " + entryID + " status array: " + status.toString());
		//LogController.log("Elapsed: "+ (System.currentTimeMillis() - startTime));
	}

	public static boolean isSpaceSignedOff(int spaceID) {
		boolean so = false;
		try {
			String s = SpacesTable.getRow(spaceID).getSignedOff();
			so = (s!=null && s.equals("TRUE"));
		} catch (SQLException e) {
			LogController.logEvent(AuditController.class, C.ERROR, e);
		}
		return so;
	}

	public static int calculateSpaceCompletionStatus(int spaceID) {
		int progress = 0;
		//System.out.println("Calculating for " + spaceID);
		try {
			progress += (Integer) EsmApplication.appData.getField("SPACE_CHK_" + spaceID);
			progress += (Integer) EsmApplication.appData.getField("SPACE_CLASS_" + spaceID);
		} catch (Exception ex) {}
		return (int) Math.floor((progress / 2) / 10) * 10;
	}

	public static int calculateEntryCompletionStatus(int epID) {
		int progress = 0;
		//System.out.println("Calculating for " + epID);
		try {
			progress += (Integer) EsmApplication.appData.getField("ENTRY_CHK_" + epID);
			progress += (Integer) EsmApplication.appData.getField("ENTRY_CLASS_" + epID);
		} catch (Exception ex) {}
		return (int) Math.floor((progress / 2) / 10) * 10;
	}

	public static int calculateOverallCompletionStatus(int spaceID) {
		int progress = 0;
		int num = 2;
		//System.out.println("Calculating overall for " + spaceID);
		try {
			progress += (Integer) EsmApplication.appData.getField("SPACE_CHK_" + spaceID);
			progress += (Integer) EsmApplication.appData.getField("SPACE_CLASS_" + spaceID);
		} catch (Exception ex) {}		
		try {
			Connection conn = DatabaseController.createConnection();
			PreparedStatement ps = null;
			ResultSet epRow = null;
			String sql = "SELECT ID FROM ENTRYPOINTS WHERE DELETED=FALSE AND SPACE_ID=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, spaceID);
			epRow = ps.executeQuery();
			while(epRow!=null && epRow.next()) {
				int epID = epRow.getInt("ID");
				try {
					progress += (Integer) EsmApplication.appData.getField("ENTRY_CHK_" + epID);
					progress += (Integer) EsmApplication.appData.getField("ENTRY_CLASS_" + epID);
					num = num+2;
				} catch (Exception ex) { 
					LogController.logEvent(AuditController.class, C.ERROR, "appdata set EP FAILED " + epID); 
				}
			}
			ps.close();
			epRow.close();
			conn.close();
		} catch (SQLException e) {
			LogController.logEvent(AuditController.class, C.FATAL, e);		
		}
		//System.out.println(progress + "/" + num);
		return (int) Math.floor((progress / num) / 10) * 10;
	}

	public static boolean isSpaceComplete(int spaceID) {
		boolean complete = true;
		if ((Integer) EsmApplication.appData.getField("SPACE_CHK_" + spaceID) < 100) {
			complete = false;
			//System.out.println("space "+spaceID+" check<100");
		}
		if ((Integer) EsmApplication.appData.getField("SPACE_CLASS_" + spaceID) < 100) {
			complete = false;
			//System.out.println("space "+spaceID+" class<100");
		}		
		try {
			Connection conn = DatabaseController.createConnection();
			PreparedStatement ps = null;
			ResultSet epRow = null;
			String sql = "SELECT ID FROM ENTRYPOINTS WHERE DELETED=FALSE AND SPACE_ID=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, spaceID);
			epRow = ps.executeQuery();
			while(epRow.next()) {
				int epID = epRow.getInt("ID");
				if ((Integer) EsmApplication.appData.getField("ENTRY_CHK_" + epID) < 100) {
					complete = false;
					//System.out.println("entry "+epID+" check<100");
				}
				if ((Integer) EsmApplication.appData.getField("ENTRY_CLASS_" + epID) < 100) {
					complete = false;
					//System.out.println("entry "+epID+" class<100");
				}
			}
			ps.close();
			epRow.close();
			conn.close();
		} catch (SQLException ex) {
			complete = false;
			LogController.logEvent(AuditController.class, C.ERROR, "DB error on completion query", ex);
		}
		return complete;
	}

	private static boolean isY(String s) {
		return (s != null && s.equals("Y"));
	}

	private static boolean isN(String s) {
		return (s != null && s.equals("N"));
	}

	public static HashMap<String, Object> getResultsAsHashmap(String sql) {
		HashMap<String, Object> vals = new HashMap<String, Object>();
		try {
			Connection conn = DatabaseController.createConnection();
			ResultSet row = null;
			Statement s = conn.createStatement();
			row = s.executeQuery(sql);
			ResultSetMetaData md = row.getMetaData();
			int columns = md.getColumnCount();
			vals = new HashMap<String, Object>(columns);
			if (row!=null && row.next()){
				for(int i=1; i<=columns; ++i){           
					vals.put(md.getColumnName(i), row.getObject(i));
				}
			}
			s.close();
			row.close();
			conn.close();
		} catch (SQLException e) {
			LogController.logEvent(AuditController.class, C.ERROR, e);		
		}
		return vals;
	}


	public static HashMap<String, Object> getSpaceChecklistArray(int spaceID) {
		HashMap<String, Object> vals = new HashMap<String, Object>();
		if(!AuditController.isSpaceSignedOff(spaceID)) {
			return vals;
		}		
		String sql = "SELECT ID,SPACE_ID,Q1_DIMS_H,Q1_DIMS_W,Q1_DIMS_L,Q1_COMMENTS,Q2_BOOLEAN,Q2_DESC,Q3_BOOLEAN,Q3_COMMENTS,Q4_BOOLEAN,Q4_DIMS_H,Q4_DIMS_W,Q4_COMMENTS,Q5_BOOLEAN,Q5_COMMENTS,Q6_BOOLEAN,Q6_COMMENTS,Q7_BOOLEAN,Q7_RATING,Q7_COMMENTS,Q8_BOOLEAN,Q8_RATING,Q8_COMMENTS,Q9_BOOLEAN,Q9_RATING,Q9_COMMENTS,Q10_BOOLEAN,Q10_RATING,Q10_COMMENTS,Q11_BOOLEAN,Q11_COMMENTS,Q12_BOOLEAN,Q12_COMMENTS,Q13_BOOLEAN,Q13_COMMENTS,Q14_BOOLEAN,Q14_COMMENTS,Q15_BOOLEAN,Q15_COMMENTS,Q16_BOOLEAN,Q16_COMMENTS,Q17_BOOLEAN,Q17_COMMENTS FROM SPACE_CHECKLIST_AUDIT WHERE SPACE_ID="+spaceID;
		vals = getResultsAsHashmap(sql);
		return vals;
	}

	public static HashMap<String, Object> getSpaceClassificationArray(int spaceID) {
		HashMap<String, Object> vals = new HashMap<String, Object>();
		if(!AuditController.isSpaceSignedOff(spaceID)) {
			return vals;
		}
		String sql = "SELECT ID,SPACE_ID,Q1_VALUE,Q1_COMMENTS,Q2_VALUE,Q2_COMMENTS,Q3_VALUE,Q3_COMMENTS,Q4_VALUE,Q4_COMMENTS,Q5_VALUE,Q5_COMMENTS,Q6_VALUE,Q6_COMMENTS,Q7_BOOLEAN,Q7_COMMENTS,Q8_BOOLEAN,Q8_COMMENTS FROM SPACE_CLASSIFICATION_AUDIT WHERE SPACE_ID="+spaceID;
		vals = getResultsAsHashmap(sql);
		return vals;
	}
	public static HashMap<String, Object> getEntrypointChecklistArray(int entryID, int spaceID) {
		HashMap<String, Object> vals = new HashMap<String, Object>();
		if(!AuditController.isSpaceSignedOff(spaceID)) {
			return vals;
		}
		String sql = "SELECT ID,ENTRYPOINT_ID,Q1_VALUE,Q1_COMMENTS,Q2_BOOLEAN,Q2_COMMENTS,Q3_BOOLEAN,Q3_COMMENTS,Q4_VALUE,Q4_COMMENTS,Q5_DIMS_H,Q5_DIMS_W,Q5_COMMENTS,Q6_BOOLEAN,Q6_COMMENTS,Q7_VALUE,Q7_COMMENTS,Q8_BOOLEAN,Q8_COMMENTS,Q9_BOOLEAN,Q9_COMMENTS,Q10_BOOLEAN,Q10_COMMENTS,Q11_BOOLEAN,Q11_COMMENTS,Q12_BOOLEAN,Q12_COMMENTS,Q13_BOOLEAN,Q13_COMMENTS,Q14_BOOLEAN,Q14_COMMENTS,Q15_BOOLEAN,Q15_COMMENTS,Q16_BOOLEAN,Q16_COMMENTS FROM ENTRYPOINT_CHECKLIST_AUDIT WHERE ENTRYPOINT_ID="+entryID;
		vals = getResultsAsHashmap(sql);
		return vals;
	}
	public static HashMap<String, Object> getEntrypointClassificationArray(int entryID, int spaceID) {
		HashMap<String, Object> vals = new HashMap<String, Object>();
		if(!AuditController.isSpaceSignedOff(spaceID)) {
			return vals;
		}
		String sql = "SELECT ID,ENTRYPOINT_ID,Q1_VALUE,Q1_COMMENTS,Q2_VALUE,Q2_COMMENTS,Q3_BOOLEAN,Q3_COMMENTS,Q4_VALUE,Q4_COMMENTS,Q5_BOOLEAN,Q5_COMMENTS,Q6_BOOLEAN,Q6_COMMENTS,Q7_VALUE,Q7_COMMENTS FROM ENTRYPOINT_CLASSIFICATION_AUDIT WHERE ENTRYPOINT_ID="+entryID;
		vals = getResultsAsHashmap(sql);
		return vals;
	}

	public static void revokeSignOff(int spaceID) {
		try {
			SpacesTable.Row sRow = SpacesTable.getRow(spaceID);
			sRow.setSignedOff("FALSE");
			sRow.setSignoffDate(null);
			sRow.setSignoffID(null);
			sRow.setUpdateDate(new Timestamp(new Date().getTime()));
			sRow.update();
			LogController.log("Space "+spaceID+" Sign-off Revoked");
		} catch (SQLException e) {
			LogController.logEvent(AuditController.class, C.ERROR, e);			
		}
	}

}
