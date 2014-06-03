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
		logdir = new File(C.LOG_DIR);
		LogController.log("Logs folder: " + logdir);
		if(logdir.mkdir() ) {
			LogController.log("CREATED");
		} else {
			LogController.log("EXISTS");
		}		
		EsmApplication.appData.setField("LOGDIR",logdir);

		LogController.log("File system integrity check complete");

	}
}
