package com.rmrdigitalmedia.esm.controllers;

import java.sql.SQLException;

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.models.SpaceChecklistAuditTable;

public class AuditController {


	private static boolean isY(String s){
		return (s !=null && s.equals("Y"));
	}
	private static boolean isN(String s){
		return (s !=null && s.equals("N"));
	}

	public static int calculateSpaceChecklistCompletion(int spaceID) {
		int percent = 0;
		int max;
		int score = 0;
		SpaceChecklistAuditTable.Row row = null;
		try {
			row = SpaceChecklistAuditTable.getRow("SPACE_ID", ""+spaceID);
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			//ex.printStackTrace();
		}
		if(row != null) {
			score += ( C.notNullOrEmpty(row.getQ1DimsH()) && C.notNullOrEmpty(row.getQ1DimsL()) && C.notNullOrEmpty(row.getQ1DimsW()) ) ? 1:0;
			score += (row.getQ2Boolean().equals("Y") && C.notNullOrEmpty(row.getQ2Desc())) ? 1:0;
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
			if (isN(row.getQ7Boolean())) {
				max = 14;
			}
			percent = Math.round((score / max) * 100);
		}
		return percent;		
	}

	public static int calculateEntryChecklistCompletion(int entryID) {




		return 0;		
	}



}

