package com.rmrdigitalmedia.esm.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.models.EntrypointChecklistAuditTable;
import com.rmrdigitalmedia.esm.models.EntrypointChecklistQuestionsTable;
import com.rmrdigitalmedia.esm.models.EntrypointClassificationAuditTable;
import com.rmrdigitalmedia.esm.models.EntrypointClassificationQuestionsTable;
import com.rmrdigitalmedia.esm.models.EntrypointsTable;
import com.rmrdigitalmedia.esm.models.EsmUsersTable;
import com.rmrdigitalmedia.esm.models.PhotoMetadataTable;
import com.rmrdigitalmedia.esm.models.SpaceChecklistAuditTable;
import com.rmrdigitalmedia.esm.models.SpaceChecklistQuestionsTable;
import com.rmrdigitalmedia.esm.models.SpaceClassificationAuditTable;
import com.rmrdigitalmedia.esm.models.SpaceClassificationQuestionsTable;
import com.rmrdigitalmedia.esm.models.SpaceCommentsTable;
import com.rmrdigitalmedia.esm.models.SpacesTable;
import com.rmrdigitalmedia.esm.models.VesselTable;
import com.rmrdigitalmedia.esm.test.PdfTest;

public class PdfController {

	static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
	private static String path = C.TMP_DIR;
	static String fPath;

	public static String getPath() {
		return path;
	}
	public static void setPath(String _path) {
		PdfController.path = _path;
	}
	private static boolean isY(String s) {
		return (s != null && s.equals("Y"));
	}
	private static boolean isN(String s) {
		return (s != null && s.equals("N"));
	}
	public static byte[] getBytes(InputStream is) throws IOException {
		int len;
		int size = 1024;
		byte[] buf;
		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			while ((len = is.read(buf, 0, size)) != -1)
				bos.write(buf, 0, len);
			buf = bos.toByteArray();
		}
		return buf;
	}
	public static Image getImageFromPath(String path) throws BadElementException, MalformedURLException, IOException {
		Image img;
		InputStream is = PdfTest.class.getResourceAsStream(path);
		img = Image.getInstance( getBytes(is) );
		img.setCompressionLevel(7);
		return img;
	}
	public static Image getImageFromDB(int id) throws BadElementException, MalformedURLException, IOException, SQLException {
		Image img = null;
		Connection conn = DatabaseController.createConnection();
		InputStream is = null;
		try {
			String sql = "SELECT DATA_THUMB FROM PHOTO_DATA WHERE ID=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				is = rs.getBinaryStream("DATA_THUMB");
				img = Image.getInstance( getBytes(is) );
				img.setCompressionLevel(7);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}	
		return img;
	}
	public static String s(int n) {
		String o = "";
		for(int i=0;i<n;i++) {
			o += " ";
		}
		return o;
	}

	@SuppressWarnings("unused")
	public static boolean buildAudit(int spaceID) throws DocumentException, SQLException {
		Display.getCurrent().getActiveShell().setCursor(new Cursor(Display.getCurrent(), SWT.CURSOR_WAIT));
		boolean ok = false;
		if(!AuditController.isSpaceSignedOff(spaceID)) {
			return false;
		}
		SpacesTable.Row sRow = SpacesTable.getRow(spaceID);
		String locType = (String)EsmApplication.appData.getField("LOCATION_TYPE");
		String locName = (String)EsmApplication.appData.getField("LOCATION_NAME");
		EsmUsersTable.Row so = EsmUsersTable.getRow(sRow.getSignoffID());
		String signOffName = so.getRank() + " " + so.getForename() + " " + so.getSurname();
		EsmUsersTable.Row au = EsmUsersTable.getRow(sRow.getAuthorID());
		String authorName = au.getForename() + " " + so.getSurname();
		String imo = VesselTable.getAllRows()[0].getImoNumber();
		SpaceChecklistAuditTable.Row spaceCheckRow = SpaceChecklistAuditTable.getRow("SPACE_ID", "" + spaceID);
		SpaceClassificationAuditTable.Row spaceClassRow = SpaceClassificationAuditTable.getRow("SPACE_ID", "" + spaceID);
		SpaceChecklistQuestionsTable.Row[] qRowsSpaceCheck = null;
		Vector<String> qTextSpaceCheck = new Vector<String>();
		SpaceClassificationQuestionsTable.Row[] qRowsSpaceClass = null;
		Vector<String> qTextSpaceClass = new Vector<String>();
		try {
			qTextSpaceCheck.add(0, null);
			qRowsSpaceCheck = SpaceChecklistQuestionsTable.getRows("1=1 ORDER BY SEQUENCE ASC");
			for(SpaceChecklistQuestionsTable.Row row:qRowsSpaceCheck) {
				qTextSpaceCheck.add(row.getSequence(), row.getQText());
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		try {
			qTextSpaceClass.add(0, null);
			qRowsSpaceClass = SpaceClassificationQuestionsTable.getRows("1=1 ORDER BY SEQUENCE ASC");
			for(SpaceClassificationQuestionsTable.Row row:qRowsSpaceClass) {
				qTextSpaceClass.add(row.getSequence(), row.getQText());
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		// doc properties
		Document document = new Document(PageSize.A4, 20, 20, 20, 20);
		document.addAuthor("Videotel ESM"); 
		document.addSubject("Enclosed Space Report"); 
		Rectangle r = document.getPageSize();
		int h = (int) r.getHeight(); //842
		int w = (int) r.getWidth(); //595
		
		// constants
		int ls = 10, lH = 15, lW = 15, imgPad = 5, v, c = 0;
		Font font1 = new Font(Font.HELVETICA, 15, Font.BOLD);
		Font font2 = new Font(Font.HELVETICA, 9,  Font.ITALIC);
		Font font3 = new Font(Font.HELVETICA, 9, Font.BOLD);
		Font font4 = new Font(Font.HELVETICA, 9, Font.NORMAL);
		Font font5 = new Font(Font.HELVETICA, 8, Font.NORMAL);
		Paragraph p;
		Phrase ph;
		PdfPTable tblSpaceCheck, tblSpaceClass, tblEntryCheck, tblEntryClass, tblEntryOverallStatus;
		float[] colsSpaceCheck = {50,15,35};
		float[] colsSpaceClass = {50,15,10,25};
		float[] colsEntryCheck = {50,15,35};
		float[] colsEntryClass = {50,15,10,25};
		PdfPCell c1, c2, c3, c4, h1, h2, h3, h4, os, statusHeader;
		boolean q7Check = false;
		String[] diff = {"","Very difficult","Quite difficult","Not difficult"};
		String[] diff2 = {"","No", "Yes with difficulty", "Yes without difficulty"};		
		String s, light, vs, sLight;
		Image tl;
		ArrayList<Integer> status = new ArrayList<Integer>();
		java.awt.Color cellBG = new java.awt.Color(222, 224, 226);

		try {
			// set up output file
			fPath = getPath() + C.SEP + "SPACE_"+spaceID+"_AUDIT.pdf";
			File f = new File(fPath);
			PdfWriter.getInstance(document, new FileOutputStream(f));
			document.open();

			// header
			Image logo = getImageFromPath("/img/esm-logo-horiz.png");
			logo.setAbsolutePosition(10, h-70);
			document.add(logo);

			// summary of space
			p = new Paragraph("Enclosed Space Report", font1);
			p.setSpacingBefore(60);
			document.add(p);
			p = new Paragraph(C.DISCLAIMER, font2);
			p.setSpacingAfter(ls);
			document.add(p);			
			document.add(new Paragraph("Date generated: " + sdf.format(new Date()) + s(10) + "Authorised By: " + signOffName, font3));

			p = new Paragraph("Vessel Information", font1);
			p.setSpacingBefore(ls*3);
			document.add(p);			
			p = new Paragraph("The following information in this report was generated on the following "+locType+":", font2);
			p.setSpacingAfter(ls);
			document.add(p);
			document.add(new Paragraph(locType +" Name: " + locName  + s(10) + "IMO Number: " + imo, font3));

			p = new Paragraph("The Space", font1);
			p.setSpacingBefore(ls*3);
			document.add(p);			
			p = new Paragraph("The space described in this report:", font2);
			p.setSpacingAfter(ls);
			document.add(p);
			p = new Paragraph("Name: " + sRow.getName() + s(10) + "ID: " + spaceID, font3);
			p.setSpacingAfter(ls);
			document.add(p);
			p = new Paragraph("Created By: " + authorName + s(5)
					+ "Created Date: " + sdf.format(sRow.getCreatedDate()) + s(5)
					+ "Last Modified: " + sdf.format(sRow.getUpdateDate()), 
					font3);
			p.setSpacingAfter(ls);
			document.add(p);
			p = new Paragraph("Description", font3);
			p.setSpacingAfter(ls);
			document.add(p);
			document.add(new Paragraph(sRow.getDescription(), font4));

			document.newPage();

			// INTERNAL SPACE AUDIT ===============================================================

			p = new Paragraph("Internal Space Audit - " + sRow.getName(), font1);
			p.setSpacingBefore(ls);
			document.add(p);			
			p = new Paragraph("The space has been audited and classified as follows:", font2);
			p.setSpacingAfter(ls);
			document.add(p);

			// SPACE CHECKLIST table =============================================================
			tblSpaceCheck = new PdfPTable(colsSpaceCheck);
			tblSpaceCheck.setTotalWidth(560);
			tblSpaceCheck.setLockedWidth(true);
			h1 = new PdfPCell(new Paragraph("Checklist Questions",font3));
			h1.setBackgroundColor(cellBG);
			tblSpaceCheck.addCell(h1);
			h2 = new PdfPCell(new Paragraph("Answers",font3));
			h2.setBackgroundColor(cellBG);
			tblSpaceCheck.addCell(h2);
			h3 = new PdfPCell(new Paragraph("Comments",font3));
			h3.setBackgroundColor(cellBG);
			tblSpaceCheck.addCell(h3);
			// start loop through questions

			//Q1
			c = 1;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
			c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
			ph = new Phrase("H: " + spaceCheckRow.getQ1DimsH()
					+ "\nW: " + spaceCheckRow.getQ1DimsW()
					+ "\nL: " + spaceCheckRow.getQ1DimsL(),font5);
			c2.addElement(ph);
			c3.addElement(new Phrase(spaceCheckRow.getQ1Comments(),font5));
			tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
			//Q2
			c = 2;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
			c2.addElement(new Phrase(spaceCheckRow.getQ2Boolean(),font5));
			c3.addElement(new Phrase(spaceCheckRow.getQ2Desc(),font5));
			tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
			//Q3
			c = 3;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
			c2.addElement(new Phrase(spaceCheckRow.getQ3Boolean(),font5));
			c3.addElement(new Phrase(spaceCheckRow.getQ3Comments(),font5));
			tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
			//Q4
			c = 4;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
			s = spaceCheckRow.getQ4Boolean();
			if(isY(s)) {
				s += "\nH: " + spaceCheckRow.getQ4DimsH(); 
				s += "\nW: " + spaceCheckRow.getQ4DimsW(); 
			}
			c2.addElement(new Phrase(s,font5));
			c3.addElement(new Phrase(spaceCheckRow.getQ4Comments(),font5));
			tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
			//Q5
			c = 5;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
			c2.addElement(new Phrase(spaceCheckRow.getQ3Boolean(),font5));
			c3.addElement(new Phrase(spaceCheckRow.getQ5Comments(),font5));
			tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
			//Q6
			c = 6;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
			c2.addElement(new Phrase(spaceCheckRow.getQ6Boolean(),font5));
			c3.addElement(new Phrase(spaceCheckRow.getQ6Comments(),font5));
			tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
			//Q7
			c = 7;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
			s = spaceCheckRow.getQ7Boolean();
			if(isY(s)) {
				q7Check = true;
				s += "\nRating: " + spaceCheckRow.getQ7Rating(); 
			}
			c2.addElement(new Phrase(s,font5));
			c3.addElement(new Phrase(spaceCheckRow.getQ7Comments(),font5));
			tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);

			if(q7Check) {
				//Q8
				c = 8;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
				if(isY(spaceCheckRow.getQ8Boolean())) {
					s += "\nRating: " + spaceCheckRow.getQ8Rating(); 
				}
				c2.addElement(new Phrase(s,font5));
				c3.addElement(new Phrase(spaceCheckRow.getQ8Comments(),font5));
				tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
				// Q9
				c = 9;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
				if(isY(spaceCheckRow.getQ9Boolean())) {
					s += "\nRating: " + spaceCheckRow.getQ9Rating(); 
				}
				c2.addElement(new Phrase(s,font5));
				c3.addElement(new Phrase(spaceCheckRow.getQ9Comments(),font5));
				tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
				//Q10
				c = 10;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
				if(isY(spaceCheckRow.getQ10Boolean())) {
					s += "\nRating: " + spaceCheckRow.getQ10Rating(); 
				}
				c2.addElement(new Phrase(s,font5));
				c3.addElement(new Phrase(spaceCheckRow.getQ10Comments(),font5));
				tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
			}
			//Q11
			c = 11;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
			c2.addElement(new Phrase(spaceCheckRow.getQ11Boolean(),font5));
			c3.addElement(new Phrase(spaceCheckRow.getQ11Comments(),font5));
			tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
			//Q12
			c = 12;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
			c2.addElement(new Phrase(spaceCheckRow.getQ12Boolean(),font5));
			c3.addElement(new Phrase(spaceCheckRow.getQ12Comments(),font5));
			tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
			//Q13
			c = 13;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
			c2.addElement(new Phrase(spaceCheckRow.getQ13Boolean(),font5));
			c3.addElement(new Phrase(spaceCheckRow.getQ13Comments(),font5));
			tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
			//Q14
			c = 14;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
			c2.addElement(new Phrase(spaceCheckRow.getQ14Boolean(),font5));
			c3.addElement(new Phrase(spaceCheckRow.getQ14Comments(),font5));
			tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
			//Q15
			c = 15;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
			c2.addElement(new Phrase(spaceCheckRow.getQ15Boolean(),font5));
			c3.addElement(new Phrase(spaceCheckRow.getQ15Comments(),font5));
			tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
			//Q16
			c = 16;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
			c2.addElement(new Phrase(spaceCheckRow.getQ16Boolean(),font5));
			c3.addElement(new Phrase(spaceCheckRow.getQ16Comments(),font5));
			tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
			//Q17
			c = 17;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
			c2.addElement(new Phrase(spaceCheckRow.getQ17Boolean(),font5));
			c3.addElement(new Phrase(spaceCheckRow.getQ17Comments(),font5));
			tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);

			tblSpaceCheck.setSpacingAfter(40);
			document.add(tblSpaceCheck);

			// SPACE CLASSIFICATION table =============================================================
			tblSpaceClass = new PdfPTable(colsSpaceClass);
			tblSpaceClass.setTotalWidth(560);
			tblSpaceClass.setLockedWidth(true);
			h1 = new PdfPCell(new Paragraph("Classification Questions",font3));
			h1.setBackgroundColor(cellBG);
			tblSpaceClass.addCell(h1);
			h2 = new PdfPCell(new Paragraph("Answers",font3));
			h2.setBackgroundColor(cellBG);
			tblSpaceClass.addCell(h2);
			statusHeader = new PdfPCell(new Paragraph("Status",font3));
			statusHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
			statusHeader.setBackgroundColor(cellBG);
			tblSpaceClass.addCell(statusHeader);
			h4 = new PdfPCell(new Paragraph("Comments",font3));
			h4.setBackgroundColor(cellBG);
			tblSpaceClass.addCell(h4);

			// start loop through questions
			//Q1
			c = 1;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c4 = new PdfPCell();
			c1.addElement(new Phrase((String)qTextSpaceClass.elementAt(c),font5));
			v = spaceClassRow.getQ1Value();
			c2.addElement(new Phrase(diff[v],font5));
			status.add(v);
			light = "null";
			if(v==1) light="red";
			if(v==2) light="amber";
			if(v==3) light="green";
			tl = getImageFromPath("/img/"+light+".png");
			tl.scaleAbsolute(15, 15);
			c3 = new PdfPCell(tl,false); c3.setPadding(imgPad);
			c3.setHorizontalAlignment(Element.ALIGN_CENTER); c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c4.addElement(new Phrase(spaceClassRow.getQ1Comments(),font5));
			tblSpaceClass.addCell(c1); tblSpaceClass.addCell(c2); tblSpaceClass.addCell(c3); tblSpaceClass.addCell(c4);
			//Q2
			c = 2;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c4 = new PdfPCell();
			c1.addElement(new Phrase((String)qTextSpaceClass.elementAt(c),font5));
			v = spaceClassRow.getQ2Value();
			c2.addElement(new Phrase(diff[v],font5));
			status.add(v);
			light = "null";
			if(v==1) light="red";
			if(v==2) light="amber";
			if(v==3) light="green";
			tl = getImageFromPath("/img/"+light+".png");
			tl.scaleAbsolute(15, 15);
			c3 = new PdfPCell(tl,false); c3.setPadding(imgPad);
			c3.setHorizontalAlignment(Element.ALIGN_CENTER); c3.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			c4.addElement(new Phrase(spaceClassRow.getQ2Comments(),font5));
			tblSpaceClass.addCell(c1); tblSpaceClass.addCell(c2); tblSpaceClass.addCell(c3); tblSpaceClass.addCell(c4);
			if(q7Check) {
				//Q3
				c = 3;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c4 = new PdfPCell();
				c1.addElement(new Phrase((String)qTextSpaceClass.elementAt(c),font5));
				v = spaceClassRow.getQ3Value();
				c2.addElement(new Phrase(diff[v],font5));
				status.add(v);
				light = "null";
				if(v==1) light="red";
				if(v==2) light="amber";
				if(v==3) light="green";
				tl = getImageFromPath("/img/"+light+".png");
				tl.scaleAbsolute(lW,lH);
				c3 = new PdfPCell(tl,false); c3.setPadding(imgPad);
				c3.setHorizontalAlignment(Element.ALIGN_CENTER); c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c4.addElement(new Phrase(spaceClassRow.getQ3Comments(),font5));
				tblSpaceClass.addCell(c1); tblSpaceClass.addCell(c2); tblSpaceClass.addCell(c3); tblSpaceClass.addCell(c4);
			}
			//Q4
			c = 4;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c4 = new PdfPCell();
			c1.addElement(new Phrase((String)qTextSpaceClass.elementAt(c),font5));
			v = spaceClassRow.getQ4Value();
			c2.addElement(new Phrase(""+v,font5));	
			status.add(v);
			light = "null";
			if(v==1) light="red";
			if(v==2 || v==3 | v==4) light="amber";
			if(v==5) light="green";
			tl = getImageFromPath("/img/"+light+".png");
			tl.scaleAbsolute(lW,lH);
			c3 = new PdfPCell(tl,false); c3.setPadding(imgPad);
			c3.setHorizontalAlignment(Element.ALIGN_CENTER); c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c4.addElement(new Phrase(spaceClassRow.getQ4Comments(),font5));
			tblSpaceClass.addCell(c1); tblSpaceClass.addCell(c2); tblSpaceClass.addCell(c3); tblSpaceClass.addCell(c4);
			//Q5
			c = 5;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c4 = new PdfPCell();
			c1.addElement(new Phrase((String)qTextSpaceClass.elementAt(c),font5));
			v = spaceClassRow.getQ5Value();
			c2.addElement(new Phrase(diff[v],font5));
			status.add(v);
			light = "null";
			if(v==1) light="red";
			if(v==2) light="amber";
			if(v==3) light="green";
			tl = getImageFromPath("/img/"+light+".png");
			tl.scaleAbsolute(lW,lH);
			c3 = new PdfPCell(tl,false); c3.setPadding(imgPad);
			c3.setHorizontalAlignment(Element.ALIGN_CENTER); c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c4.addElement(new Phrase(spaceClassRow.getQ5Comments(),font5));
			tblSpaceClass.addCell(c1); tblSpaceClass.addCell(c2); tblSpaceClass.addCell(c3); tblSpaceClass.addCell(c4);
			//Q6
			c = 6;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c4 = new PdfPCell();
			c1.addElement(new Phrase((String)qTextSpaceClass.elementAt(c),font5));
			v = spaceClassRow.getQ6Value();
			c2.addElement(new Phrase(diff[v],font5));
			status.add(v);
			light = "null";
			if(v==1) light="red";
			if(v==2) light="amber";
			if(v==3) light="green";
			tl = getImageFromPath("/img/"+light+".png");
			tl.scaleAbsolute(lW,lH);
			c3 = new PdfPCell(tl,false); c3.setPadding(imgPad);
			c3.setHorizontalAlignment(Element.ALIGN_CENTER); c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c4.addElement(new Phrase(spaceClassRow.getQ6Comments(),font5));
			tblSpaceClass.addCell(c1); tblSpaceClass.addCell(c2); tblSpaceClass.addCell(c3); tblSpaceClass.addCell(c4);
			//Q7
			c = 7;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c4 = new PdfPCell();
			c1.addElement(new Phrase((String)qTextSpaceClass.elementAt(c),font5));
			vs = spaceClassRow.getQ7Boolean();
			c2.addElement(new Phrase(vs,font5));
			light = "null";
			if(isY(vs)) { light="red"; status.add(1); }
			if(isN(vs)) { light="green"; status.add(3); }
			tl = getImageFromPath("/img/"+light+".png");
			tl.scaleAbsolute(lW,lH);
			c3 = new PdfPCell(tl,false); c3.setPadding(imgPad);
			c3.setHorizontalAlignment(Element.ALIGN_CENTER); c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c4.addElement(new Phrase(spaceClassRow.getQ7Comments(),font5));
			tblSpaceClass.addCell(c1); tblSpaceClass.addCell(c2); tblSpaceClass.addCell(c3); tblSpaceClass.addCell(c4);
			//Q8
			c = 8;	
			c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c4 = new PdfPCell();
			c1.addElement(new Phrase((String)qTextSpaceClass.elementAt(c),font5));
			vs = spaceClassRow.getQ8Boolean();
			c2.addElement(new Phrase(vs,font5));
			light = "null";
			if(isY(vs)) { light="red"; status.add(1); }
			if(isN(vs)) { light="green"; status.add(3); }
			tl = getImageFromPath("/img/"+light+".png");
			tl.scaleAbsolute(lW,lH);
			c3 = new PdfPCell(tl,false); c3.setPadding(imgPad);
			c3.setHorizontalAlignment(Element.ALIGN_CENTER); c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c4.addElement(new Phrase(spaceClassRow.getQ8Comments(),font5));
			tblSpaceClass.addCell(c1); tblSpaceClass.addCell(c2); tblSpaceClass.addCell(c3); tblSpaceClass.addCell(c4);

			tblSpaceClass.setSpacingAfter(40);
			document.add(tblSpaceClass);

			// overall classification stuff here...
			sLight = "null";
			if(status.contains(1)) {
				sLight = "red";
				s = C.SPACE_OVERALL_STATUS_MSG_RED;		
			} 
			else if (status.contains(2) && !status.contains(1)) {
				sLight = "amber";
				s = C.SPACE_OVERALL_STATUS_MSG_AMBER;		
			}
			else if (status.contains(3) && !status.contains(1) && !status.contains(2)) {
				sLight = "green";
				s = C.SPACE_OVERALL_STATUS_MSG_GREEN;		
			}		
			float[] colsSpaceOverall = {90,10};
			PdfPTable tblSpaceOverallStatus = new PdfPTable(colsSpaceOverall);
			tblSpaceOverallStatus.setTotalWidth(560);
			tblSpaceOverallStatus.setLockedWidth(true);
			tblSpaceOverallStatus.addCell(new PdfPCell(new Paragraph("Overall Classification",font3)));
			tblSpaceOverallStatus.addCell(statusHeader);		
			tblSpaceOverallStatus.addCell(new PdfPCell(new Paragraph("The overall classification of this space is " + sLight.toUpperCase() + ".\n" + s, font2)));
			tl = getImageFromPath("/img/"+sLight+".png");
			tl.scaleAbsolute(lW,lH);
			os = new PdfPCell(tl,false);
			os.setHorizontalAlignment(Element.ALIGN_CENTER); os.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tblSpaceOverallStatus.addCell(os);
			tblSpaceOverallStatus.setSpacingAfter(20);
			document.add(tblSpaceOverallStatus);



			// ENTRY POINTS AUDIT=================================================================================
			EntrypointChecklistAuditTable.Row entryCheckRow;
			EntrypointClassificationAuditTable.Row entryClassRow;
			EntrypointChecklistQuestionsTable.Row[] qRowsEntryCheck = null;
			Vector<String> qTextEntryCheck = new Vector<String>();
			EntrypointClassificationQuestionsTable.Row[] qRowsEntryClass = null;
			Vector<String> qTextEntryClass = new Vector<String>();
			try {
				qTextEntryCheck.add(0, null);
				qRowsEntryCheck = EntrypointChecklistQuestionsTable.getRows("1=1 ORDER BY SEQUENCE ASC");
				for(EntrypointChecklistQuestionsTable.Row row:qRowsEntryCheck) {
					qTextEntryCheck.add(row.getSequence(), row.getQText());
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			try {
				qTextEntryClass.add(0, null);
				qRowsEntryClass = EntrypointClassificationQuestionsTable.getRows("1=1 ORDER BY SEQUENCE ASC");
				for(EntrypointClassificationQuestionsTable.Row row:qRowsEntryClass) {
					qTextEntryClass.add(row.getSequence(), row.getQText());
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			// LOOP THROUGH ENTRY POINTS						
			EntrypointsTable.Row[] epRows = EntrypointsTable.getRows("DELETED=FALSE AND SPACE_ID="+spaceID);
			for (EntrypointsTable.Row epRow:epRows) {

				document.newPage();
				p = new Paragraph("Entry Point Audit - " + epRow.getName(), font1);
				p.setSpacingBefore(ls);
				document.add(p);			
				p = new Paragraph("The entry point has been audited and classified as follows:", font2);
				p.setSpacingAfter(ls);
				document.add(p);

				int epID = epRow.getID();
				entryCheckRow = EntrypointChecklistAuditTable.getRow("ENTRYPOINT_ID", "" + epID);
				entryClassRow = EntrypointClassificationAuditTable.getRow("ENTRYPOINT_ID", "" + epID);	
				String q7EntryCheck = "";

				// ENTRYPOINT CHECKLIST table =============================================================
				tblEntryCheck = new PdfPTable(colsEntryCheck);
				tblEntryCheck.setTotalWidth(560);
				tblEntryCheck.setLockedWidth(true);
				h1 = new PdfPCell(new Paragraph("Checklist Questions",font3));
				h1.setBackgroundColor(cellBG);
				tblEntryCheck.addCell(h1);
				h2 = new PdfPCell(new Paragraph("Answers",font3));
				h2.setBackgroundColor(cellBG);
				tblEntryCheck.addCell(h2);
				h3 = new PdfPCell(new Paragraph("Comments",font3));
				h3.setBackgroundColor(cellBG);
				tblEntryCheck.addCell(h3);

				// start loop through questions
				//Q1
				c = 1;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
				c1.addElement(new Phrase((String)qTextEntryCheck.elementAt(c),font5));
				c2.addElement(new Phrase(entryCheckRow.getQ1Value(),font5));
				c3.addElement(new Phrase(entryCheckRow.getQ1Comments(),font5));
				tblEntryCheck.addCell(c1); tblEntryCheck.addCell(c2); tblEntryCheck.addCell(c3);
				if(entryCheckRow.getQ1Value().equals("OUTSIDE")) {
					//Q2
					c = 2;	
					c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
					c1.addElement(new Phrase((String)qTextEntryCheck.elementAt(c),font5));
					c2.addElement(new Phrase(entryCheckRow.getQ2Boolean(),font5));
					c3.addElement(new Phrase(entryCheckRow.getQ2Comments(),font5));
					tblEntryCheck.addCell(c1); tblEntryCheck.addCell(c2); tblEntryCheck.addCell(c3);
				}
				//3
				c = 3;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
				c1.addElement(new Phrase((String)qTextEntryCheck.elementAt(c),font5));
				c2.addElement(new Phrase(entryCheckRow.getQ3Boolean(),font5));
				c3.addElement(new Phrase(entryCheckRow.getQ3Comments(),font5));
				tblEntryCheck.addCell(c1); tblEntryCheck.addCell(c2); tblEntryCheck.addCell(c3);
				//Q4
				c = 4;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
				c1.addElement(new Phrase((String)qTextEntryCheck.elementAt(c),font5));
				c2.addElement(new Phrase(entryCheckRow.getQ4Value(),font5));
				c3.addElement(new Phrase(entryCheckRow.getQ4Comments(),font5));
				tblEntryCheck.addCell(c1); tblEntryCheck.addCell(c2); tblEntryCheck.addCell(c3);
				//Q5
				c = 5;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.addElement(new Phrase((String)qTextSpaceCheck.elementAt(c),font5));
				s = "\nH: " + entryCheckRow.getQ5DimsH() + "\nW: " + entryCheckRow.getQ5DimsW(); 
				c2.addElement(new Phrase(s,font5));
				c3.addElement(new Phrase(spaceCheckRow.getQ5Comments(),font5));
				tblSpaceCheck.addCell(c1); tblSpaceCheck.addCell(c2); tblSpaceCheck.addCell(c3);
				//Q6
				c = 6;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
				c1.addElement(new Phrase((String)qTextEntryCheck.elementAt(c),font5));
				c2.addElement(new Phrase(entryCheckRow.getQ6Boolean(),font5));
				c3.addElement(new Phrase(entryCheckRow.getQ6Comments(),font5));
				tblEntryCheck.addCell(c1); tblEntryCheck.addCell(c2); tblEntryCheck.addCell(c3);
				//Q7
				c = 7;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
				c1.addElement(new Phrase((String)qTextEntryCheck.elementAt(c),font5));
				q7EntryCheck = entryCheckRow.getQ7Value();
				c2.addElement(new Phrase(q7EntryCheck,font5));
				c3.addElement(new Phrase(entryCheckRow.getQ7Comments(),font5));
				tblEntryCheck.addCell(c1); tblEntryCheck.addCell(c2); tblEntryCheck.addCell(c3);
				if(q7EntryCheck.equals("VERTICAL")) {
					//Q8
					c = 8;	
					c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
					c1.addElement(new Phrase((String)qTextEntryCheck.elementAt(c),font5));
					c2.addElement(new Phrase(entryCheckRow.getQ8Boolean(),font5));
					c3.addElement(new Phrase(entryCheckRow.getQ8Comments(),font5));
					tblEntryCheck.addCell(c1); tblEntryCheck.addCell(c2); tblEntryCheck.addCell(c3);
				}
				//Q9
				c = 9;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
				c1.addElement(new Phrase((String)qTextEntryCheck.elementAt(c),font5));
				c2.addElement(new Phrase(entryCheckRow.getQ9Boolean(),font5));
				c3.addElement(new Phrase(entryCheckRow.getQ9Comments(),font5));
				tblEntryCheck.addCell(c1); tblEntryCheck.addCell(c2); tblEntryCheck.addCell(c3);
				//10
				c = 10;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
				c1.addElement(new Phrase((String)qTextEntryCheck.elementAt(c),font5));
				c2.addElement(new Phrase(entryCheckRow.getQ10Boolean(),font5));
				c3.addElement(new Phrase(entryCheckRow.getQ10Comments(),font5));
				tblEntryCheck.addCell(c1); tblEntryCheck.addCell(c2); tblEntryCheck.addCell(c3);
				//11
				c = 11;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
				c1.addElement(new Phrase((String)qTextEntryCheck.elementAt(c),font5));
				c2.addElement(new Phrase(entryCheckRow.getQ11Boolean(),font5));
				c3.addElement(new Phrase(entryCheckRow.getQ11Comments(),font5));
				tblEntryCheck.addCell(c1); tblEntryCheck.addCell(c2); tblEntryCheck.addCell(c3);
				//12
				c = 12;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
				c1.addElement(new Phrase((String)qTextEntryCheck.elementAt(c),font5));
				c2.addElement(new Phrase(entryCheckRow.getQ12Boolean(),font5));
				c3.addElement(new Phrase(entryCheckRow.getQ12Comments(),font5));
				tblEntryCheck.addCell(c1); tblEntryCheck.addCell(c2); tblEntryCheck.addCell(c3);
				//13
				c = 13;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
				c1.addElement(new Phrase((String)qTextEntryCheck.elementAt(c),font5));
				c2.addElement(new Phrase(entryCheckRow.getQ13Boolean(),font5));
				c3.addElement(new Phrase(entryCheckRow.getQ13Comments(),font5));
				tblEntryCheck.addCell(c1); tblEntryCheck.addCell(c2); tblEntryCheck.addCell(c3);
				//14
				c = 14;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
				c1.addElement(new Phrase((String)qTextEntryCheck.elementAt(c),font5));
				c2.addElement(new Phrase(entryCheckRow.getQ14Boolean(),font5));
				c3.addElement(new Phrase(entryCheckRow.getQ14Comments(),font5));
				tblEntryCheck.addCell(c1); tblEntryCheck.addCell(c2); tblEntryCheck.addCell(c3);
				//15
				c = 15;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
				c1.addElement(new Phrase((String)qTextEntryCheck.elementAt(c),font5));
				c2.addElement(new Phrase(entryCheckRow.getQ15Boolean(),font5));
				c3.addElement(new Phrase(entryCheckRow.getQ15Comments(),font5));
				tblEntryCheck.addCell(c1); tblEntryCheck.addCell(c2); tblEntryCheck.addCell(c3);
				//16
				c = 16;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); 
				c1.addElement(new Phrase((String)qTextEntryCheck.elementAt(c),font5));
				c2.addElement(new Phrase(entryCheckRow.getQ16Boolean(),font5));
				c3.addElement(new Phrase(entryCheckRow.getQ16Comments(),font5));
				tblEntryCheck.addCell(c1); tblEntryCheck.addCell(c2); tblEntryCheck.addCell(c3);

				tblEntryCheck.setSpacingAfter(40);
				document.add(tblEntryCheck);


				// ENTRY CLASSIFICATION table =============================================================
				tblEntryClass = new PdfPTable(colsEntryClass);
				tblEntryClass.setTotalWidth(560);
				tblEntryClass.setLockedWidth(true);
				h1 = new PdfPCell(new Paragraph("Classification Questions",font3));
				h1.setBackgroundColor(cellBG);
				tblEntryClass.addCell(h1);
				h2 = new PdfPCell(new Paragraph("Answers",font3));
				h2.setBackgroundColor(cellBG);
				tblEntryClass.addCell(h2);
				statusHeader = new PdfPCell(new Paragraph("Status",font3));
				statusHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
				statusHeader.setBackgroundColor(cellBG);
				tblEntryClass.addCell(statusHeader);
				h4 = new PdfPCell(new Paragraph("Comments",font3));
				h4.setBackgroundColor(cellBG);
				tblEntryClass.addCell(h4);
				status.clear();

				// start loop through questions
				//Q1
				c = 1;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c4 = new PdfPCell();
				c1.addElement(new Phrase((String)qTextEntryClass.elementAt(c),font5));
				v = entryClassRow.getQ1Value();
				c2.addElement(new Phrase(diff[v],font5));
				status.add(v);
				light = "null";
				if(v==1) light="red";
				if(v==2) light="amber";
				if(v==3) light="green";
				tl = getImageFromPath("/img/"+light+".png");
				tl.scaleAbsolute(lW,lH);
				c3 = new PdfPCell(tl,false); c3.setPadding(imgPad);
				c3.setHorizontalAlignment(Element.ALIGN_CENTER); c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c4.addElement(new Phrase(entryClassRow.getQ1Comments(),font5));
				tblEntryClass.addCell(c1); tblEntryClass.addCell(c2); tblEntryClass.addCell(c3); tblEntryClass.addCell(c4);
				if(q7EntryCheck.equals("VERTICAL")) {
					//Q2
					c = 2;	
					c1 = new PdfPCell(); c2 = new PdfPCell(); c4 = new PdfPCell();
					c1.addElement(new Phrase((String)qTextEntryClass.elementAt(c),font5));
					v = entryClassRow.getQ2Value();
					c2.addElement(new Phrase(diff2[v],font5));			
					status.add(v);
					light = "null";
					if(v==1) light="red";
					if(v==2) light="amber";
					if(v==3) light="green";
					tl = getImageFromPath("/img/"+light+".png");
					tl.scaleAbsolute(lW,lH);
					c3 = new PdfPCell(tl,false); c3.setPadding(imgPad);
					c3.setHorizontalAlignment(Element.ALIGN_CENTER); c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c4.addElement(new Phrase(entryClassRow.getQ2Comments(),font5));
					tblEntryClass.addCell(c1); tblEntryClass.addCell(c2); tblEntryClass.addCell(c3); tblEntryClass.addCell(c4);
				}
				//Q3
				c = 3;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c4 = new PdfPCell();
				c1.addElement(new Phrase((String)qTextEntryClass.elementAt(c),font5));
				vs = entryClassRow.getQ3Boolean();
				c2.addElement(new Phrase(vs,font5));			
				status.add(v);
				light = "null";
				if(isN(vs)) light="red";
				if(isY(vs)) light="green";
				tl = getImageFromPath("/img/"+light+".png");
				tl.scaleAbsolute(lW,lH);
				c3 = new PdfPCell(tl,false); c3.setPadding(imgPad);
				c3.setHorizontalAlignment(Element.ALIGN_CENTER); c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c4.addElement(new Phrase(entryClassRow.getQ3Comments(),font5));
				tblEntryClass.addCell(c1); tblEntryClass.addCell(c2); tblEntryClass.addCell(c3); tblEntryClass.addCell(c4);
				//Q4
				c = 4;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c4 = new PdfPCell();
				c1.addElement(new Phrase((String)qTextEntryClass.elementAt(c),font5));
				v = entryClassRow.getQ4Value();
				c2.addElement(new Phrase(""+v,font5));			
				light = "null";
				if(v==1) { light="red"; status.add(v); }
				if(v==2 || v==3 || v==4) { light="amber"; status.add(2); }
				if(v==5) { light="green"; status.add(3); }
				tl = getImageFromPath("/img/"+light+".png");
				tl.scaleAbsolute(lW,lH);
				c3 = new PdfPCell(tl,false); c3.setPadding(imgPad);
				c3.setHorizontalAlignment(Element.ALIGN_CENTER); c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c4.addElement(new Phrase(entryClassRow.getQ4Comments(),font5));
				tblEntryClass.addCell(c1); tblEntryClass.addCell(c2); tblEntryClass.addCell(c3); tblEntryClass.addCell(c4);
				//Q5
				c = 5;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c4 = new PdfPCell();
				c1.addElement(new Phrase((String)qTextEntryClass.elementAt(c),font5));
				vs = entryClassRow.getQ5Boolean();
				c2.addElement(new Phrase(vs,font5));			
				light = "null";
				if(isN(vs)) { light="red"; status.add(1); }
				if(isY(vs)) { light="green"; status.add(3); }
				tl = getImageFromPath("/img/"+light+".png");
				tl.scaleAbsolute(lW,lH);
				c3 = new PdfPCell(tl,false); c3.setPadding(imgPad);
				c3.setHorizontalAlignment(Element.ALIGN_CENTER); c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c4.addElement(new Phrase(entryClassRow.getQ5Comments(),font5));
				tblEntryClass.addCell(c1); tblEntryClass.addCell(c2); tblEntryClass.addCell(c3); tblEntryClass.addCell(c4);
				//Q6
				c = 6;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c3 = new PdfPCell(); c4 = new PdfPCell();
				c1.addElement(new Phrase((String)qTextEntryClass.elementAt(c),font5));
				vs = entryClassRow.getQ6Boolean();
				c2.addElement(new Phrase(vs,font5));			
				light = "null";
				if(isN(vs)) { light="red"; status.add(1); }
				if(isY(vs)) { light="green"; status.add(3); }
				tl = getImageFromPath("/img/"+light+".png");
				tl.scaleAbsolute(lW,lH);
				c3 = new PdfPCell(tl,false); c3.setPadding(imgPad);
				c3.setHorizontalAlignment(Element.ALIGN_CENTER); c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c4.addElement(new Phrase(entryClassRow.getQ6Comments(),font5));
				tblEntryClass.addCell(c1); tblEntryClass.addCell(c2); tblEntryClass.addCell(c3); tblEntryClass.addCell(c4);
				//Q7
				c = 7;	
				c1 = new PdfPCell(); c2 = new PdfPCell(); c4 = new PdfPCell();
				c1.addElement(new Phrase((String)qTextEntryClass.elementAt(c),font5));
				v = entryClassRow.getQ7Value();
				c2.addElement(new Phrase(diff[v],font5));
				status.add(v);
				light = "null";
				if(v==1) light="red";
				if(v==2) light="amber";
				if(v==3) light="green";
				tl = getImageFromPath("/img/"+light+".png");
				tl.scaleAbsolute(lW,lH);
				c3 = new PdfPCell(tl,false); c3.setPadding(imgPad);
				c3.setHorizontalAlignment(Element.ALIGN_CENTER); c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c4.addElement(new Phrase(entryClassRow.getQ7Comments(),font5));
				tblEntryClass.addCell(c1); tblEntryClass.addCell(c2); tblEntryClass.addCell(c3); tblEntryClass.addCell(c4);

				tblEntryClass.setSpacingAfter(40);
				document.add(tblEntryClass);

				// overall classification stuff here...
				sLight = "null";
				if(status.contains(1)) {
					sLight = "red";
					s = C.ENTRY_OVERALL_STATUS_MSG_RED;		
				}
				else if (status.contains(2) && !status.contains(1)) {
					sLight = "amber";
					s = C.ENTRY_OVERALL_STATUS_MSG_AMBER;		
				}
				else if (status.contains(3) && !status.contains(1) && !status.contains(2)) {
					sLight = "green";
					s = C.ENTRY_OVERALL_STATUS_MSG_GREEN;		
				}		
				float[] colsEntryOverall = {90,10};
				tblEntryOverallStatus = new PdfPTable(colsEntryOverall);
				tblEntryOverallStatus.setTotalWidth(560);
				tblEntryOverallStatus.setLockedWidth(true);
				h1 = new PdfPCell(new Paragraph("Overall Classification",font3));
				h1.setBackgroundColor(cellBG);
				tblEntryOverallStatus.addCell(h1);
				tblEntryOverallStatus.addCell(statusHeader);		
				tblEntryOverallStatus.addCell(new PdfPCell(new Paragraph("The overall classification of this entry point is " + sLight.toUpperCase() + ".\n" + s, font2)));
				tl = getImageFromPath("/img/"+sLight+".png");
				tl.scaleAbsolute(lW,lH);
				os = new PdfPCell(tl,false);
				os.setHorizontalAlignment(Element.ALIGN_CENTER); os.setVerticalAlignment(Element.ALIGN_MIDDLE);
				tblEntryOverallStatus.addCell(os);
				tblEntryOverallStatus.setSpacingAfter(20);
				document.add(tblEntryOverallStatus);

			} // end entry points loop


			// add comments & photos =========================================================================================
			document.newPage();

			p = new Paragraph("Additional Information", font1);
			p.setSpacingBefore(ls);
			document.add(p);			
			p = new Paragraph("The following information comprises of comments and photographs supplied by the crew:", font2);
			p.setSpacingAfter(ls);
			document.add(p);

			SpaceCommentsTable.Row[] cRows = SpaceCommentsTable.getRows("DELETED=FALSE AND SPACE_ID="+spaceID+" ORDER BY ID DESC");
			for(SpaceCommentsTable.Row cRow:cRows) {				
				EsmUsersTable.Row author = EsmUsersTable.getRow(cRow.getAuthorID());
				String auName = author.getForename() + " " + author.getSurname();
				document.add(new Paragraph(auName,font3));
				document.add(new Paragraph(cRow.getComment(),font5));
				p = new Paragraph("Posted: " + sdf.format(cRow.getUpdateDate()),font3);
				p.setSpacingAfter(ls);
				document.add(p);				
			} // end comments
			document.add(new Paragraph(" "));

			PhotoMetadataTable.Row[] pRows = PhotoMetadataTable.getRows("DELETED=FALSE AND SPACE_ID="+spaceID+" ORDER BY ID DESC");
			for(PhotoMetadataTable.Row pRow:pRows) {
				EsmUsersTable.Row author = EsmUsersTable.getRow(pRow.getAuthorID());
				String auName = author.getForename() + " " + author.getSurname();
				document.add(new Paragraph(auName,font3));
				document.add(new Paragraph(pRow.getTitle(),font4));
				int dataID = pRow.getDataID();
				Image img = getImageFromDB(dataID);
				img.setCompressionLevel(7);
				img.scaleToFit(100, 100);
				document.add(img);
				p = new Paragraph("Posted: " + sdf.format(pRow.getUpdateDate()),font3);
				p.setSpacingAfter(ls);
				document.add(p);				
			}
			document.add(new Paragraph(" "));

			document.close(); // no need to close PDFwriter?
			ok = true;

		} catch (DocumentException e) {
			ok = false;
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			ok = false;
			e.printStackTrace();
		} catch (MalformedURLException e) {
			ok = false;
			e.printStackTrace();
		} catch (IOException e) {
			ok = false;
			e.printStackTrace();
		}

		LogController.log("Created PDF: "+fPath);
		Display.getCurrent().getActiveShell().setCursor(new Cursor(Display.getCurrent(), SWT.CURSOR_ARROW));
		return ok;
	}




}
