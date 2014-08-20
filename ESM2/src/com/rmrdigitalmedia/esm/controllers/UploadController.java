package com.rmrdigitalmedia.esm.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import com.rmrdigitalmedia.esm.C;

public class UploadController {

	static Display display = Display.getCurrent();

	public static String[] uploadSpaceImageDialog() {
		LogController.log("Opening photo select dialog");
		final FileDialog dialog = new FileDialog(new Shell(), SWT.OPEN | SWT.ON_TOP);
		dialog.setText("Choose an image");
		String platform = SWT.getPlatform();
		String[] filterNames = new String[] { "Image Files", "All Files (*)" };
		String[] filterExtensions = new String[] { "*.gif;*.png;*.xpm;*.jpg;*.jpeg;*.tiff", "*" };
		String filterPath = C.HOME_DIR + C.SEP + "Documents";
		if (platform.equals("win32") || platform.equals("wpf")) {
			filterNames = new String[] { "Image Files", "All Files (*.*)" };
			filterExtensions = new String[] { "*.gif;*.png;*.bmp;*.jpg;*.jpeg;*.tiff", "*.*" };
		}
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(filterPath);
		dialog.open();
		if (!dialog.getFileName().equals("")) {
			String fn = dialog.getFilterPath() + C.SEP + dialog.getFileName();
			return new String[] { fn, dialog.getFileName() };
		}
		return null;
	}	

	public static String uploadSpaceDocumentDialog() {
		LogController.log("Opening document select dialog");
		final FileDialog dialog = new FileDialog(new Shell(), SWT.OPEN);
		dialog.setText("Choose a document");
		String platform = SWT.getPlatform();
		String[] filterNames = new String[] { "Document Files", "All Files (*)" };
		String[] filterExtensions = new String[] { "*.doc;*.docx;*.pdf;*.xls;*.xlsx;*.txt", "*" };
		String filterPath = C.HOME_DIR + C.SEP + "Documents";
		if (platform.equals("win32") || platform.equals("wpf")) {
			filterNames = new String[] { "Document Files", "All Files (*.*)" };
		}
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(filterPath);
		dialog.open();
		if (!dialog.getFileName().equals("")) {
			String fullPath = dialog.getFilterPath() + C.SEP + dialog.getFileName();
			return fullPath;
		}
		return null;
	}


	public static boolean uploadSpaceDocument(int spaceID, int authorID) {
		int id = 0;
		String fn = uploadSpaceDocumentDialog();
		if(fn != null) {
			File f = new File(fn);
			try {
				id = DatabaseController.insertDocument(f, spaceID, authorID);
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		return (id > 0);
	}

	public static boolean IsImageFile(File f) {
		boolean valid = true;
		try {
			BufferedImage image = ImageIO.read(f);
			if (image == null) {
				valid = false;
			}
		} catch (IOException ex) {
			valid = false;
		}
		return valid;
	}

}
