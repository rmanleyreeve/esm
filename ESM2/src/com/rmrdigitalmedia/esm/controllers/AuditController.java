package com.rmrdigitalmedia.esm.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.models.EntrypointChecklistAuditTable;
import com.rmrdigitalmedia.esm.models.EntrypointClassificationAuditTable;
import com.rmrdigitalmedia.esm.models.EntrypointsTable;
import com.rmrdigitalmedia.esm.models.SpaceChecklistAuditTable;
import com.rmrdigitalmedia.esm.models.SpaceClassificationAuditTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;

@SuppressWarnings("unused")
public class AuditController {

	public static void init() {
		// do initial audit calculations here
		long startTime = System.currentTimeMillis();
		LogController.log("Initial Space audits calculation started");
		try {
			for (SpacesTable.Row sRow : SpacesTable.getAllRows()) {
				int spaceID = sRow.getID();
				AuditController.calculateSpaceChecklistCompletion(spaceID);
				AuditController.calculateSpaceClassificationCompletion(spaceID);
			}
			LogController.log("Space audits calculation completed in " + (System.currentTimeMillis() - startTime) + "ms");
			startTime = System.currentTimeMillis();
			LogController.log("Initial Entry audits calculation started");
			for (EntrypointsTable.Row epRow : EntrypointsTable.getAllRows()) {
				int entryID = epRow.getID();
				AuditController.calculateEntryChecklistCompletion(entryID);
				AuditController.calculateEntryClassificationCompletion(entryID);
			}
			LogController.log("Entry audits calculation completed in " + (System.currentTimeMillis() - startTime) + "ms");
		} catch (SQLException e1) {
			LogController.logEvent(AuditController.class, C.FATAL, "Initial Space audits calculation", e1);
		}
		// System.out.println("Elapsed: "+ (System.currentTimeMillis() - startTime));
	}

	// INTERNAL SPACE CHECKLIST
	public static void calculateSpaceChecklistCompletion(int spaceID) {
		long startTime = System.currentTimeMillis();
		double percent = 0;
		int progress = 0;
		int max = 0;
		int score = 0;
		SpaceChecklistAuditTable.Row row = null;
		try {
			row = SpaceChecklistAuditTable.getRow("SPACE_ID", "" + spaceID);
		} catch (SQLException ex) {
			LogController.logEvent(AuditController.class, C.FATAL, ex);
		}
		if (row != null) {
			if (C.notNullOrEmpty(row.getQ1DimsH())
					&& C.notNullOrEmpty(row.getQ1DimsL())
					&& C.notNullOrEmpty(row.getQ1DimsW())) {
				score += 1;
			}
			String q2 = row.getQ2Boolean();
			if (isN(q2) || (isY(q2) && C.notNullOrEmpty(row.getQ2Desc()))) {
				score += 1;
			}
			if (row.getQ3Boolean() != null) {
				score += 1;
			}
			String q4 = row.getQ4Boolean();
			if (isN(q4)
					|| (isY(q4) && C.notNullOrEmpty(row.getQ4DimsH()) && C
							.notNullOrEmpty(row.getQ4DimsW()))) {
				score += 1;
			}
			if (row.getQ5Boolean() != null) {
				score += 1;
			}
			if (row.getQ6Boolean() != null) {
				score += 1;
			}
			String q7 = row.getQ7Boolean();
			if (isN(q7) || (isY(q7) && row.getQ7Rating() != 0)) {
				score += 1;
			}
			String q8 = row.getQ8Boolean();
			if (isN(q8) || (isY(q8) && row.getQ8Rating() != 0)) {
				score += 1;
			}
			String q9 = row.getQ9Boolean();
			if (isN(q9) || (isY(q9) && row.getQ9Rating() != 0)) {
				score += 1;
			}
			String q10 = row.getQ10Boolean();
			if (isN(q10) || (isY(q10) && row.getQ10Rating() != 0)) {
				score += 1;
			}
			if (row.getQ11Boolean() != null) {
				score += 1;
			}
			if (row.getQ12Boolean() != null) {
				score += 1;
			}
			if (row.getQ13Boolean() != null) {
				score += 1;
			}
			if (row.getQ14Boolean() != null) {
				score += 1;
			}
			if (row.getQ15Boolean() != null) {
				score += 1;
			}
			if (row.getQ16Boolean() != null) {
				score += 1;
			}
			if (row.getQ17Boolean() != null) {
				score += 1;
			}
			max = 17;
			if (q7 != null && isN(q7)) {
				max = 14;
			}
			percent = Math.round(((float) score / max) * 100);
			progress = (int) Math.floor(percent / 10) * 10;
		}
		EsmApplication.appData.setField("SPACE_CHK_" + spaceID, progress);
		// System.out.println("Calculating space "+userID+" checklist: " + score + "/" + max + "=" + percent + "% -> progress=" + progress );
		// System.out.println("Elapsed: "+ (System.currentTimeMillis() - startTime));
	}

	// INTERNAL SPACE CLASSIFICATION
	public static void calculateSpaceClassificationCompletion(int spaceID) {
		long startTime = System.currentTimeMillis();
		double percent = 0;
		int progress = 0;
		int max = 0;
		int score = 0;
		ArrayList<Integer> status = new ArrayList<Integer>();
		String light = "null";
		SpaceClassificationAuditTable.Row row = null;
		try {
			row = SpaceClassificationAuditTable.getRow("SPACE_ID", "" + spaceID);
		} catch (SQLException ex) {
			LogController.logEvent(AuditController.class, C.FATAL, ex);
		}
		if (row != null) {
			int q1 = row.getQ1Value();
			if (q1 != 0) {
				score += 1;
				status.add(q1);
			}
			int q2 = row.getQ2Value();
			if (q2 != 0) {
				score += 1;
				status.add(q2);
			}
			int q3 = row.getQ3Value();
			if (q3 != 0) {
				score += 1;
				status.add(q3);
			}
			int q4 = row.getQ4Value();
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
			int q5 = row.getQ5Value();
			if (q5 != 0) {
				score += 1;
				status.add(q5);
			}
			int q6 = row.getQ6Value();
			if (q6 != 0) {
				score += 1;
				status.add(q6);
			}
			String q7 = row.getQ7Boolean();
			if (q7 != null) {
				score += 1;
				if (isY(q7)) {
					status.add(1);
				} else if (isN(q7)) {
					status.add(3);
				}
			} // N = green
			String q8 = row.getQ8Boolean();
			if (q8 != null) {
				score += 1;
				if (isY(q8)) {
					status.add(1);
				} else if (isN(q8)) {
					status.add(3);
				}
			} // N = green
			max = 8;
			try {
				if (isN(SpaceChecklistAuditTable.getRow("SPACE_ID", "" + spaceID).getQ7Boolean())) {
					max = 7;
				}
			} catch (SQLException ex) {
			}
			percent = Math.round(((float) score / max) * 100);
			progress = (int) Math.floor(percent / 10) * 10;
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
		EsmApplication.appData.setField("SPACE_CLASS_" + spaceID, progress);
		EsmApplication.appData.setField("SPACE_STATUS_" + spaceID, light);
		// System.out.println("Calculating space "+userID+" classification: " + score + "/" + max + "=" + percent + "% -> progress=" + progress );
		// System.out.println("Space " + userID + " status array: " + status.toString());
		// System.out.println("Elapsed: "+ (System.currentTimeMillis() - startTime));
	}

	// =========================================================================================================================

	// ENTRY POINT CHECKLIST
	public static void calculateEntryChecklistCompletion(int entryID) {
		long startTime = System.currentTimeMillis();
		double percent = 0;
		int progress = 0;
		int max = 0;
		int score = 0;
		EntrypointChecklistAuditTable.Row row = null;
		try {
			row = EntrypointChecklistAuditTable.getRow("ENTRYPOINT_ID", "" + entryID);
		} catch (SQLException ex) {
			LogController.logEvent(AuditController.class, C.FATAL, ex);
		}
		if (row != null) {
			String q1 = row.getQ1Value();
			if (row.getQ1Value() != null) {
				score += 1;
			}
			if (q1 != null && q1.equals("OUTSIDE")
					&& row.getQ2Boolean() != null) {
				score += 1;
			}
			if (row.getQ3Boolean() != null) {
				score += 1;
			}
			if (row.getQ4Value() != null) {
				score += 1;
			}
			if (C.notNullOrEmpty(row.getQ5DimsH())
					&& C.notNullOrEmpty(row.getQ5DimsW())) {
				score += 1;
			}
			if (row.getQ6Boolean() != null) {
				score += 1;
			}
			String q7 = row.getQ7Value();
			if (q7 != null) {
				score += 1;
			}
			if (q7 != null && q7.equals("VERTICAL")
					&& row.getQ8Boolean() != null) {
				score += 1;
			}
			if (q7 != null && q7.equals("VERTICAL")
					&& row.getQ9Boolean() != null) {
				score += 1;
			}
			if (row.getQ10Boolean() != null) {
				score += 1;
			}
			if (row.getQ11Boolean() != null) {
				score += 1;
			}
			if (row.getQ12Boolean() != null) {
				score += 1;
			}
			if (row.getQ13Boolean() != null) {
				score += 1;
			}
			if (row.getQ14Boolean() != null) {
				score += 1;
			}
			if (row.getQ15Boolean() != null) {
				score += 1;
			}
			if (row.getQ16Boolean() != null) {
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
		}
		EsmApplication.appData.setField("ENTRY_CHK_" + entryID, progress);
		// System.out.println("Calculating entry "+entryID+" checklist: " + score + "/" + max + "=" + percent + "% -> progress=" + progress);
		// System.out.println("Elapsed: "+ (System.currentTimeMillis() - startTime));
	}

	// ENTRY POINT CLASSIFICATION
	public static void calculateEntryClassificationCompletion(int entryID) {
		long startTime = System.currentTimeMillis();
		double percent = 0;
		int progress = 0;
		int max = 0;
		int score = 0;
		ArrayList<Integer> status = new ArrayList<Integer>();
		String light = "null";
		EntrypointClassificationAuditTable.Row row = null;
		try {
			row = EntrypointClassificationAuditTable.getRow("ENTRYPOINT_ID", ""
					+ entryID);
		} catch (SQLException ex) {
			LogController.logEvent(AuditController.class, C.FATAL, ex);
		}
		if (row != null) {
			int q1 = row.getQ1Value();
			if (q1 != 0) {
				score += 1;
				status.add(q1);
			}
			int q2 = row.getQ2Value();
			if (q2 != 0) {
				score += 1;
				status.add(q2);
			}
			String q3 = row.getQ3Boolean();
			if (q3 != null) {
				score += 1;
				if (isY(q3)) {
					status.add(3);
				} else if (isN(q3)) {
					status.add(1);
				}
			} // Y = green
			int q4 = row.getQ4Value();
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
			String q5 = row.getQ5Boolean();
			if (q5 != null) {
				score += 1;
				if (isY(q5)) {
					status.add(3);
				} else if (isN(q5)) {
					status.add(1);
				}
			} // Y = green
			String q6 = row.getQ6Boolean();
			if (q6 != null) {
				score += 1;
				if (isY(q6)) {
					status.add(3);
				} else if (isN(q6)) {
					status.add(1);
				}
			} // Y = green
			int q7 = row.getQ7Value();
			if (q7 != 0) {
				score += 1;
				status.add(q7);
			}				
			max = 7;
			percent = Math.round(((float) score / max) * 100);
			progress = (int) Math.floor(percent / 10) * 10;
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
		EsmApplication.appData.setField("ENTRY_CLASS_" + entryID, progress);
		EsmApplication.appData.setField("ENTRY_STATUS_" + entryID, light);
		//System.out.println("Calculating entry "+entryID+" classification: " + score + "/" + max + "=" + percent + "% -> progress=" + progress );
		//System.out.println("Entry " + entryID + " status array: " + status.toString());
	}

	public static boolean isSpaceSignedOff(int spaceID) {
		boolean so = false;
		try {
			String s = SpacesTable.getRow(spaceID).getSignedOff();
			so = (s!=null && s.equals("TRUE"));
		} catch (SQLException e) {
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
			for (EntrypointsTable.Row epRow : EntrypointsTable.getRows("DELETED=FALSE AND SPACE_ID="+spaceID)) {
				int epID = epRow.getID();
				try {
					progress += (Integer) EsmApplication.appData.getField("ENTRY_CHK_" + epID);
					progress += (Integer) EsmApplication.appData.getField("ENTRY_CLASS_" + epID);
					num = num+2;
				} catch (Exception ex) { 
					LogController.logEvent(AuditController.class, C.ERROR, "appdata set EP FAILED " + epID); 
				}
			}
		} catch (SQLException ex) {}
		//System.out.println(progress + "/" + num);
		return (int) Math.floor((progress / num) / 10) * 10;
	}

	public static boolean isSpaceComplete(int spaceID) {
		boolean complete = true;
		if ((Integer) EsmApplication.appData.getField("SPACE_CHK_" + spaceID) < 100) {
			complete = false;
		}
		if ((Integer) EsmApplication.appData.getField("SPACE_CLASS_" + spaceID) < 100) {
			complete = false;
		}
		try {
			for (EntrypointsTable.Row epRow : EntrypointsTable.getRows("SPACE_ID", spaceID)) {
				int epID = epRow.getID();
				if ((Integer) EsmApplication.appData.getField("ENTRY_CHK_" + epID) < 100) {
					complete = false;
				}
				if ((Integer) EsmApplication.appData.getField("ENTRY_CLASS_" + epID) < 100) {
					complete = false;
				}
			}
		} catch (SQLException ex) {
			complete = false;
		}
		return complete;
	}

	private static boolean isY(String s) {
		return (s != null && s.equals("Y"));
	}

	private static boolean isN(String s) {
		return (s != null && s.equals("N"));
	}

}
