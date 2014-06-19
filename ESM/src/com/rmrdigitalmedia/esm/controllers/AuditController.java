package com.rmrdigitalmedia.esm.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.models.EntrypointChecklistAuditTable;
import com.rmrdigitalmedia.esm.models.EntrypointClassificationAuditTable;
import com.rmrdigitalmedia.esm.models.SpaceChecklistAuditTable;
import com.rmrdigitalmedia.esm.models.SpaceClassificationAuditTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;

public class AuditController {

	private static boolean isY(String s){
		return (s !=null && s.equals("Y"));
	}
	private static boolean isN(String s){
		return (s !=null && s.equals("N"));
	}

	// INTERNAL SPACE CHECKLIST
	public static int calculateSpaceChecklistCompletion(int spaceID) {
		double percent = 0;
		int progress = 0;
		int max = 0;
		int score = 0;
		SpaceChecklistAuditTable.Row row = null;
		try {
			row = SpaceChecklistAuditTable.getRow("SPACE_ID", ""+spaceID);
		} catch (SQLException ex) {
			LogController.logEvent(AuditController.class, C.FATAL, ex);		
		}
		if(row != null) {
			if ( C.notNullOrEmpty(row.getQ1DimsH()) && C.notNullOrEmpty(row.getQ1DimsL()) && C.notNullOrEmpty(row.getQ1DimsW()) ) { score +=1; }
			if( isN(row.getQ2Boolean()) || (isY(row.getQ2Boolean()) && C.notNullOrEmpty(row.getQ2Desc()))) { score +=1; }
			if(row.getQ3Boolean()!=null) { score +=1; }
			if ( isN(row.getQ4Boolean()) || ( isY(row.getQ4Boolean()) && C.notNullOrEmpty(row.getQ4DimsH()) && C.notNullOrEmpty(row.getQ4DimsW()) ))  { score +=1; }
			if(row.getQ5Boolean()!=null) { score +=1; }
			if(row.getQ6Boolean()!=null) { score +=1; }
			if (isN(row.getQ7Boolean()) || ( isY(row.getQ7Boolean()) && row.getQ7Rating()!=0 )) { score +=1; }
			if (isN(row.getQ8Boolean()) || ( isY(row.getQ8Boolean()) && row.getQ8Rating()!=0 )) { score +=1; }
			if (isN(row.getQ9Boolean()) || ( isY(row.getQ9Boolean()) && row.getQ9Rating()!=0 )) { score +=1; }
			if (isN(row.getQ10Boolean()) || ( isY(row.getQ10Boolean()) && row.getQ10Rating()!=0 )) { score +=1; }
			if(row.getQ11Boolean()!=null) { score +=1; }
			if(row.getQ12Boolean()!=null) { score +=1; }
			if(row.getQ13Boolean()!=null) { score +=1; }
			if(row.getQ14Boolean()!=null) { score +=1; }
			if(row.getQ15Boolean()!=null) { score +=1; }
			if(row.getQ16Boolean()!=null) { score +=1; }
			if(row.getQ17Boolean()!=null) { score +=1; }
			max = 17;
			if (row.getQ7Boolean()!=null && isN(row.getQ7Boolean())) {
				max = 14;
			}
			percent = Math.round( ( (float)score/max ) * 100 );
			progress = (int) Math.floor(percent/10 ) * 10;			
		}
		System.out.println("Space "+spaceID+" checklist: " + score + "/" + max + "=" + percent + "% -> progress=" + progress );
		EsmApplication.appData.setField("SPACE_CHK_"+spaceID, progress);
		return progress;		
	}
	
	
	// ENTRY POINT CHECKLIST
	public static int calculateEntryChecklistCompletion(int entryID) {
		double percent = 0;
		int progress = 0;
		int max = 0;
		int score = 0;
		EntrypointChecklistAuditTable.Row row = null;
		try {
			row = EntrypointChecklistAuditTable.getRow("ENTRYPOINT_ID", ""+entryID);
		} catch (SQLException ex) {
			LogController.logEvent(AuditController.class, C.FATAL, ex);		
		}
		if(row != null) {
			if( row.getQ1Value()!=null ) { score +=1; }
			if( row.getQ1Value()!=null && row.getQ1Value().equals("OUTSIDE") && row.getQ2Boolean()!=null) { score +=1; }
			if(row.getQ3Boolean()!=null) { score +=1; }
			if( row.getQ4Value()!=null ) { score +=1; }
			if( C.notNullOrEmpty(row.getQ5DimsH()) && C.notNullOrEmpty(row.getQ5DimsW()) ) { score +=1; }
			if(row.getQ6Boolean()!=null) { score +=1; }
			if( row.getQ7Value()!=null ) { score +=1; }
			if( row.getQ7Value()!=null && row.getQ7Value().equals("VERTICAL") && row.getQ8Boolean()!=null) { score +=1; }
			if( row.getQ7Value()!=null && row.getQ7Value().equals("VERTICAL") && row.getQ9Boolean()!=null) { score +=1; }
			if(row.getQ10Boolean()!=null) { score +=1; }
			if(row.getQ11Boolean()!=null) { score +=1; }
			if(row.getQ12Boolean()!=null) { score +=1; }
			if(row.getQ13Boolean()!=null) { score +=1; }
			if(row.getQ14Boolean()!=null) { score +=1; }
			if(row.getQ15Boolean()!=null) { score +=1; }
			if(row.getQ16Boolean()!=null) { score +=1; }
			max = 16;
			if (row.getQ1Value()!=null && row.getQ1Value().equals("INSIDE")) {
				max--;
			}
			if (row.getQ7Value()!=null && row.getQ7Value().equals("HORIZONTAL")) {
				max--;max--;
			}
			percent = Math.round( ( (float)score/max ) * 100 );
			progress = (int) Math.floor(percent/10 ) * 10;
		}
		System.out.println("Entry "+entryID+" checklist: " + score + "/" + max + "=" + percent + "% -> progress=" + progress);		
		EsmApplication.appData.setField("ENTRY_CHK_"+entryID, progress);
		return progress;		
	}

	// INTERNAL SPACE CLASSIFICATION
	public static int calculateSpaceClassificationCompletion(int spaceID) {
		double percent = 0;
		int progress = 0;
		int max = 0;
		int score = 0;
		ArrayList<Integer> status = new ArrayList<Integer>();
		String light = "red";
		SpaceClassificationAuditTable.Row row = null;
		try {
			row = SpaceClassificationAuditTable.getRow("SPACE_ID", ""+spaceID);
		} catch (SQLException ex) {
			LogController.logEvent(AuditController.class, C.FATAL, ex);		
		}
		if(row != null) {
			int q1 = row.getQ1Value(); if( q1!=0 ) { score +=1; status.add(q1);} 
			int q2 = row.getQ2Value(); if( q2!=0 ) { score +=1; status.add(q2); }
			int q3 = row.getQ3Value(); if( q3!=0 ) { score +=1; status.add(q3); }
			int q4 = row.getQ4Value(); if( q4!=0 ) { score +=1; if(q4==5) { status.add(3); } else if (q4==1) { status.add(1); } else { status.add(2); } }
			int q5 = row.getQ5Value(); if( q5!=0 ) { score +=1; status.add(q5); }
			int q6 = row.getQ6Value(); if( q6!=0 ) { score +=1; status.add(q6); }
			String q7 = row.getQ7Boolean(); if(q7!=null) { score +=1; if(isY(q7)) { status.add(1); } else if(isN(q7)) { status.add(3); } }
			String q8 = row.getQ8Boolean(); if(q8!=null) { score +=1; if(isY(q8)) { status.add(1); } else if(isN(q8)) { status.add(3); } }
			max = 8;
			try {
				if( isN(SpaceChecklistAuditTable.getRow("SPACE_ID", ""+spaceID).getQ7Boolean())  ) {
					max = 7;
				}
			} catch (SQLException ex) {}
			percent = Math.round( ( (float)score/max ) * 100 );
			progress = (int) Math.floor(percent/10 ) * 10;			
		}
		System.out.println(status.toString());
		if(status.contains(3) && !status.contains(1) && !status.contains(2)) { light = "green"; }
		if(status.contains(2) && !status.contains(1)) { light = "amber"; }	
		
		System.out.println(status.toString());
		System.out.println("Space "+spaceID+" classification: " + score + "/" + max + "=" + percent + "% -> progress=" + progress );		
		EsmApplication.appData.setField("SPACE_CLASS_"+spaceID, progress);
		EsmApplication.appData.setField("SPACE_STATUS_"+spaceID, light);
		
		return progress;		
	}

	// ENTRY POINT CLASSIFICATION
	public static int calculateEntryClassificationCompletion(int entryID) {
		double percent = 0;
		int progress = 0;
		int max = 0;
		int score = 0;
		EntrypointClassificationAuditTable.Row row = null;
		try {
			row = EntrypointClassificationAuditTable.getRow("ENTRYPOINT_ID", ""+entryID);
		} catch (SQLException ex) {
			LogController.logEvent(AuditController.class, C.FATAL, ex);		
		}
		if(row != null) {
			if( row.getQ1Value()!=0 ) { score +=1; }
			if( row.getQ2Value()!=0 ) { score +=1; }
			if( row.getQ3Value()!=0 ) { score +=1; }
			if(row.getQ4Boolean()!=null) { score +=1; }
			if( row.getQ5Value()!=0 ) { score +=1; }
			max = 5;
			percent = Math.round( ( (float)score/max ) * 100 );
			progress = (int) Math.floor(percent/10 ) * 10;			
		}
		System.out.println("Entry "+entryID+" classification: " + score + "/" + max + "=" + percent + "% -> progress=" + progress );		
		EsmApplication.appData.setField("ENTRY_CLASS_"+entryID, progress);
		return progress;		
	}

	public static boolean isSpaceSignedOff(int spaceID) {
		boolean so = false;
		try {
			so = (SpacesTable.getRow(spaceID).getSignedOff().equals("TRUE"));
		} catch (SQLException e) {}
		return so;
	}

	public static int calculateOverallCompletionStatus(int spaceID) {
		int progress = 0;
		progress += calculateSpaceChecklistCompletion(spaceID);
		progress += calculateSpaceClassificationCompletion(spaceID);
		return (int) Math.floor((progress/2)/10 ) * 10;

	}
	
	public static boolean isSpaceComplete(int spaceID) {
		// TODO Auto-generated method stub
		return true;
	}

	
	
}

