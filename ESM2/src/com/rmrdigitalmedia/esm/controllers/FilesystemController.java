package com.rmrdigitalmedia.esm.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

import com.rmrdigitalmedia.esm.AppLoader;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;


public class FilesystemController {

	public static String current = System.getProperty("user.dir");
	public static File datadir, logdir, tmpdir, docdir, imgdir;

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
		AppLoader.splashMessage("Creating System Directories");
		logdir = new File(C.LOG_DIR);
		LogController.log("Logs folder: " + logdir);
		if (logdir.mkdir()) {
			LogController.log("CREATED");
		} else {
			LogController.log("EXISTS");
		}
		EsmApplication.appData.setField("LOGDIR", logdir);
		docdir = new File(C.DOC_DIR);
		LogController.log("Docs folder: " + docdir);
		if (docdir.mkdir()) {
			LogController.log("CREATED");
		} else {
			LogController.log("EXISTS");
		}
		EsmApplication.appData.setField("DOCDIR", docdir);
		tmpdir = new File(C.TMP_DIR);
		LogController.log("Temp folder: " + tmpdir);
		if (tmpdir.mkdir()) {
			LogController.log("CREATED");
		} else {
			LogController.log("EXISTS");
		}
		EsmApplication.appData.setField("TMPDIR", tmpdir);

		LogController.log("File system integrity check complete.");
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

	public static void createLogDir() {
		logdir = new File(C.LOG_DIR);
		if (logdir.mkdir()) {
			System.out.println("LOG DIR CREATED: " + logdir);
		} else {
			System.out.println("Did not create LOG DIR: " + logdir);
		}
	}

	public static String getMimeType(File f) {
		String mt = "";
		try {
			TikaConfig tika;
			tika = new TikaConfig();
			Metadata metadata = new Metadata();
			metadata.set(Metadata.RESOURCE_NAME_KEY, f.toString());
			MediaType mimetype = tika.getDetector().detect(TikaInputStream.get(f), metadata);
			mt = mimetype.toString();
		} catch (Exception e) {
			LogController.logEvent(FilesystemController.class, C.WARNING, "Error getting mime type from file", e);
		}
		return mt;
	}
}
