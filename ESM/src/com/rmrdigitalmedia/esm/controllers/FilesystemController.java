package com.rmrdigitalmedia.esm.controllers;

import java.io.File;

import com.rmrdigitalmedia.esm.AppLoader;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;

public class FilesystemController {

	public static String current = System.getProperty("user.dir");
	public static File datadir, imgdir, docdir, logdir;

	public FilesystemController() {
	}

	public void checkFS() {		
		EsmApplication.appData.setField("LOGDIR",logdir);
		LogController.log("Running class " + this.getClass().getName());
		// set up filesystem
		LogController.log("Platform: " + C.OS);
		datadir = new File(C.DATA_DIR);
		LogController.log("PWD: "+current);
		// create data dir
		LogController.log("App Data folder: " + datadir);
		if(datadir.mkdir() ) {
			LogController.log("CREATED");
		} else {
			LogController.log("EXISTS");
		}
		AppLoader.message("Creating System Directories");
		// create sub-dirs====================================
		imgdir = new File(C.IMG_DIR);
		LogController.log("Images folder: " + imgdir);
		if(imgdir.mkdir() ) {
			LogController.log("CREATED");
		} else {
			LogController.log("EXISTS");
		}
		EsmApplication.appData.setField("IMGDIR",imgdir);
		docdir = new File(C.DOC_DIR);
		LogController.log("Docs folder: " + docdir);
		if(docdir.mkdir() ) {
			LogController.log("CREATED");
		} else {
			LogController.log("EXISTS");
		}		
		EsmApplication.appData.setField("DOCDIR",docdir);

		LogController.log("File system integrity check complete");

	}

	public static boolean deleteDirRecursive(File dir) { 
		if (dir.isDirectory())  { 
			String[] children = dir.list(); 
			for (int i=0; i<children.length; i++) { 
				boolean success = deleteDirRecursive(new File(dir, children[i])); 
				if (!success)  {  
					return false; 
				} 
			} 
			// The directory is now empty so delete it 
		} 
		return dir.delete(); 
	}

	// TODO for development ONLY
	public void deleteDataDir() {
		datadir = new File(C.DATA_DIR);
		if(datadir.exists()){
			if(deleteDirRecursive(datadir)){
				System.out.println("DELETED DATA DIR: " + datadir);
			}
		}
	}

	public void createLogDir() {
		datadir = new File(C.DATA_DIR);
		logdir = new File(C.LOG_DIR);
		if(datadir.mkdir() && logdir.mkdir() ) {
			System.out.println("LOG DIR CREATED: " + logdir);
		} else {
			System.out.println("LOG DIR: " + logdir);
		}
	}
}
