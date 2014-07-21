package com.rmrdigitalmedia.esm.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
		LogController.log("PWD: " + current);

		// create data dir
		LogController.log("App Data folder: " + datadir);
		if (datadir.mkdir()) {
			LogController.log("CREATED");
		} else {
			LogController.log("EXISTS");
		}

		// create sub-dirs====================================
		AppLoader.message("Creating System Directories");
		imgdir = new File(C.IMG_DIR);
		LogController.log("Images folder: " + imgdir);
		if (imgdir.mkdir()) {
			LogController.log("CREATED");
		} else {
			LogController.log("EXISTS");
		}
		EsmApplication.appData.setField("IMGDIR", imgdir);
		docdir = new File(C.DOC_DIR);
		LogController.log("Docs folder: " + docdir);
		if (docdir.mkdir()) {
			LogController.log("CREATED");
		} else {
			LogController.log("EXISTS");
		}
		EsmApplication.appData.setField("DOCDIR", docdir);

		LogController.log("File system integrity check complete");
	}

	public static void createBlankSpaceForm() {
		try {
			InputStream is = EsmApplication.class.getResourceAsStream("/pdf/" + C.BLANK_SPACE_FORM);
			File dest = new File(docdir + C.SEP + C.BLANK_SPACE_FORM);
			FileOutputStream os = new FileOutputStream(dest);
			int currentbyte = is.read();
			while (currentbyte != -1) {
				os.write(currentbyte);
				currentbyte = is.read();
			}
			is.close();
			os.close();
			LogController.log("Created blank space form");
		} catch (IOException ex) {
			LogController.logEvent(FilesystemController.class, C.ERROR, "Error creating blank space form from rsc file",ex);
		}
	}

	public static void createBlankEntryForm() {
		try {
			InputStream is = EsmApplication.class.getResourceAsStream("/pdf/" + C.BLANK_ENTRY_FORM);
			File dest = new File(docdir + C.SEP + C.BLANK_ENTRY_FORM);
			FileOutputStream os = new FileOutputStream(dest);
			int currentbyte = is.read();
			while (currentbyte != -1) {
				os.write(currentbyte);
				currentbyte = is.read();
			}
			is.close();
			os.close();
			LogController.log("Created blank entry form");
		} catch (IOException ex) {
			LogController.logEvent(FilesystemController.class, C.ERROR, "Error creating blank entry form from rsc file", ex);
		}
	}

	public static boolean deleteDirRecursive(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDirRecursive(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
			// The directory is now empty so delete it
		}
		return dir.delete();
	}

	public void deleteDataDir() {
		datadir = new File(C.DATA_DIR);
		if (datadir.exists()) {
			if (deleteDirRecursive(datadir)) {
				System.out.println("DELETED DATA DIR: " + datadir);
			}
		}
	}

	public void createLogDir() {
		logdir = new File(C.LOG_DIR);
		if (logdir.mkdir()) {
			System.out.println("LOG DIR CREATED: " + logdir);
		} else {
			System.out.println("Did not create LOG DIR: " + logdir);
		}
	}
}
