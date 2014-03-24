package com.rmrdigitalmedia.esm.controllers;

import java.io.File;

import com.rmrdigitalmedia.esm.AppLoader;
import com.rmrdigitalmedia.esm.C;

public class FilesystemController {

	public static String current = System.getProperty("user.dir");
	public static File datadir;
	public static File imgdir;
	public static File docdir;
	public static File logdir;

	public FilesystemController() {
	}
		
	public void checkFS() {		
		LogController.log("Running class " + this.getClass().getName());
		// set up filesystem
		LogController.log("Platform: " + C.OS);
		datadir = new File(C.USER_DOCS_DIR + C.SEP + C.DATA_DIR + C.SEP + C.DATA_DIR_NAME);
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
		imgdir = new File(datadir + C.SEP + C.IMG_DIR);
		LogController.log("Images folder: " + imgdir);
		if(imgdir.mkdir() ) {
			LogController.log("CREATED");
		} else {
			LogController.log("EXISTS");
		} 
		docdir = new File(datadir + C.SEP + C.DOC_DIR);
		LogController.log("Docs folder: " + docdir);
		if(docdir.mkdir() ) {
			LogController.log("CREATED");
		} else {
			LogController.log("EXISTS");
		}		
		logdir = new File(datadir + C.SEP + C.LOG_DIR);
		LogController.log("Logs folder: " + logdir);
		if(logdir.mkdir() ) {
			LogController.log("CREATED");
		} else {
			LogController.log("EXISTS");
		}		

		LogController.log("File system integrity check complete");
		
	}
}
