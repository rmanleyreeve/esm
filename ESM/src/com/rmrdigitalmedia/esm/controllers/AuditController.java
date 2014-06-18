package com.rmrdigitalmedia.esm.controllers;

import java.sql.SQLException;
import com.rmrdigitalmedia.esm.C;
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
			score += ( C.notNullOrEmpty(row.getQ1DimsH()) && C.notNullOrEmpty(row.getQ1DimsL()) && C.notNullOrEmpty(row.getQ1DimsW()) ) ? 1:0;
			score += ( isN(row.getQ2Boolean()) || (isY(row.getQ2Boolean()) && C.notNullOrEmpty(row.getQ2Desc()))) ? 1:0;
			score += (row.getQ3Boolean()!=null) ? 1:0;
			score += isN(row.getQ4Boolean()) || ( isY(row.getQ4Boolean()) && C.notNullOrEmpty(row.getQ4DimsH()) && C.notNullOrEmpty(row.getQ4DimsW()) )  ? 1:0;
			score += (row.getQ5Boolean()!=null) ? 1:0;
			score += (row.getQ6Boolean()!=null) ? 1:0;
			score += isN(row.getQ7Boolean()) || ( isY(row.getQ7Boolean()) && row.getQ7Rating()!=0 ) ? 1:0;
			score += isN(row.getQ8Boolean()) || ( isY(row.getQ8Boolean()) && row.getQ8Rating()!=0 ) ? 1:0;
			score += isN(row.getQ9Boolean()) || ( isY(row.getQ9Boolean()) && row.getQ9Rating()!=0 ) ? 1:0;
			score += isN(row.getQ10Boolean()) || ( isY(row.getQ10Boolean()) && row.getQ10Rating()!=0 ) ? 1:0;
			score += (row.getQ11Boolean()!=null) ? 1:0;
			score += (row.getQ12Boolean()!=null) ? 1:0;
			score += (row.getQ13Boolean()!=null) ? 1:0;
			score += (row.getQ14Boolean()!=null) ? 1:0;
			score += (row.getQ15Boolean()!=null) ? 1:0;
			score += (row.getQ16Boolean()!=null) ? 1:0;
			score += (row.getQ17Boolean()!=null) ? 1:0;
			max = 17;
			if (row.getQ7Boolean()!=null && isN(row.getQ7Boolean())) {
				max = 14;
			}
			percent = Math.round( ( (float)score/max ) * 100 );
			progress = (int) Math.floor(percent/10 ) * 10;
			
		}
		System.out.println("Space "+spaceID+" checklist: " + score + "/" + max + "=" + percent + "% -> progress=" + progress );		
		return progress;		
	}
	
	

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
			score += ( row.getQ1Value()!=null ) ? 1:0;
			score += ( row.getQ1Value()!=null && row.getQ1Value().equals("OUTSIDE") && row.getQ2Boolean()!=null) ? 1:0;
			score += (row.getQ3Boolean()!=null) ? 1:0;
			score += ( row.getQ4Value()!=null ) ? 1:0;
			score += ( C.notNullOrEmpty(row.getQ5DimsH()) && C.notNullOrEmpty(row.getQ5DimsW()) ) ? 1:0;
			score += (row.getQ6Boolean()!=null) ? 1:0;
			score += ( row.getQ7Value()!=null ) ? 1:0;
			score += ( row.getQ7Value()!=null && row.getQ7Value().equals("VERTICAL") && row.getQ8Boolean()!=null) ? 1:0;
			score += ( row.getQ7Value()!=null && row.getQ7Value().equals("VERTICAL") && row.getQ9Boolean()!=null) ? 1:0;
			score += (row.getQ10Boolean()!=null) ? 1:0;
			score += (row.getQ11Boolean()!=null) ? 1:0;
			score += (row.getQ12Boolean()!=null) ? 1:0;
			score += (row.getQ13Boolean()!=null) ? 1:0;
			score += (row.getQ14Boolean()!=null) ? 1:0;
			score += (row.getQ15Boolean()!=null) ? 1:0;
			score += (row.getQ16Boolean()!=null) ? 1:0;
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
		return progress;		
	}

	public static int calculateSpaceClassificationCompletion(int spaceID) {
		double percent = 0;
		int progress = 0;
		int max = 0;
		int score = 0;
		SpaceClassificationAuditTable.Row row = null;
		try {
			row = SpaceClassificationAuditTable.getRow("SPACE_ID", ""+spaceID);
		} catch (SQLException ex) {
			LogController.logEvent(AuditController.class, C.FATAL, ex);		
		}
		if(row != null) {
			score += ( row.getQ1Value()!=0 ) ? 1:0;
			score += ( row.getQ2Value()!=0 ) ? 1:0;
			score += ( row.getQ3Value()!=0 ) ? 1:0;
			score += ( row.getQ4Value()!=0 ) ? 1:0;
			score += ( row.getQ5Value()!=0 ) ? 1:0;
			score += ( row.getQ6Value()!=0 ) ? 1:0;
			score += (row.getQ7Boolean()!=null) ? 1:0;
			score += (row.getQ8Boolean()!=null) ? 1:0;
			max = 8;
			try {
				if( isN(SpaceChecklistAuditTable.getRow("SPACE_ID", ""+spaceID).getQ7Boolean())  ) {
					max = 7;
				}
			} catch (SQLException ex) {}
			percent = Math.round( ( (float)score/max ) * 100 );
			progress = (int) Math.floor(percent/10 ) * 10;
			
		}
		System.out.println("Space "+spaceID+" classification: " + score + "/" + max + "=" + percent + "% -> progress=" + progress );		
		return progress;		
	}

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
			score += ( row.getQ1Value()!=0 ) ? 1:0;
			score += ( row.getQ2Value()!=0 ) ? 1:0;
			score += ( row.getQ3Value()!=0 ) ? 1:0;
			score += (row.getQ4Boolean()!=null) ? 1:0;
			score += ( row.getQ5Value()!=0 ) ? 1:0;
			max = 5;
			percent = Math.round( ( (float)score/max ) * 100 );
			progress = (int) Math.floor(percent/10 ) * 10;
			
		}
		System.out.println("Entry "+entryID+" classification: " + score + "/" + max + "=" + percent + "% -> progress=" + progress );		
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
		//
		return progress;		
	}

	
	
}

