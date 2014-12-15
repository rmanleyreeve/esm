package com.rmrdigitalmedia.esm.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.models.LicenseTable;

public class ZipController {

	public static void main(String[] args) {

	}

	public static File createZipFile(File dir, int spaceID) {
		String t = ""+new Date().getTime();
		String license = "";
		try {
			license = LicenseTable.getAllRows()[0].getLicensekey();
		} catch (SQLException ex) {
			LogController.logEvent(DatabaseController.class, C.FATAL, "Could not get license key", ex);
		}
		String zipName = (spaceID==0) ? dir.getParent() + C.SEP + license + "_" + t + ".zip" : dir.getParent() + C.SEP + license + "_SPACE_" + spaceID + "_" + t + ".zip";
		try {
			FileOutputStream fos = new FileOutputStream(zipName);
			ZipOutputStream zos = new ZipOutputStream(fos);			
			// loop through folder & zip
			for (File f:dir.listFiles()) {
				if(!f.isHidden()) {
					addToZipFile(f,zos);
				}
			}		
			zos.close();
			fos.close();
			File f = new File(zipName);
			if(f.exists()) {
				try {
					Thread.sleep(1000);
					FilesystemController.deleteDirRecursive(dir);
				} catch (InterruptedException e) {}
				return f;
			} 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void addToZipFile(File file, ZipOutputStream zos) throws FileNotFoundException, IOException {
		LogController.log("Writing '" + file.getName() + "' to ZIP file");
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(file.getName());
		zos.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}
		zos.closeEntry();
		fis.close();
	}	

}

