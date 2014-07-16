package com.rmrdigitalmedia.esm.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.google.common.io.Files;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;

public class UploadController {

	static Display display = Display.getCurrent();
	private static String uploadPath = "";

	public static String[] uploadSpaceImageDialog() {
		final FileDialog dialog = new FileDialog(new Shell(), SWT.OPEN
				| SWT.ON_TOP);
		dialog.setText("Choose an image");
		String platform = SWT.getPlatform();
		String[] filterNames = new String[] { "Image Files", "All Files (*)" };
		String[] filterExtensions = new String[] {
				"*.gif;*.png;*.xpm;*.jpg;*.jpeg;*.tiff", "*" };
		String filterPath = C.HOME_DIR + C.SEP + "Documents";
		if (platform.equals("win32") || platform.equals("wpf")) {
			filterNames = new String[] { "Image Files", "All Files (*.*)" };
			filterExtensions = new String[] {
					"*.gif;*.png;*.bmp;*.jpg;*.jpeg;*.tiff", "*.*" };
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

	public static String[] uploadSpaceDocumentDialog() {
		final FileDialog dialog = new FileDialog(new Shell(), SWT.OPEN);
		dialog.setText("Choose a document");
		String platform = SWT.getPlatform();
		String[] filterNames = new String[] { "Document Files", "All Files (*)" };
		String[] filterExtensions = new String[] {
				"*.doc;*.docx;*.pdf;*.xls;*.xlsx;*.txt", "*" };
		String filterPath = C.HOME_DIR + C.SEP + "Documents";
		if (platform.equals("win32") || platform.equals("wpf")) {
			filterNames = new String[] { "Document Files", "All Files (*.*)" };
		}
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(filterPath);
		dialog.open();
		if (!dialog.getFileName().equals("")) {
			String fullPath = dialog.getFilterPath() + C.SEP
					+ dialog.getFileName();
			return new String[] { fullPath, dialog.getFileName() };
		}
		return null;
	}

	public static boolean uploadSpaceImage(int spaceID, String[] imgDetails) {
		boolean ok = false;
		if (imgDetails == null) {
			imgDetails = uploadSpaceImageDialog();
		}
		if (imgDetails != null) {
			String imgToUploadName = imgDetails[1];
			String ext = Files.getFileExtension(imgToUploadName);
			String ts = "" + new Date().getTime();
			try {
				final File src = new File(imgDetails[0]);
				BufferedImage bimg = ImageIO.read(src);
				final int srcW = bimg.getWidth();
				final int srcH = bimg.getHeight();
				imgToUploadName = ts + "." + ext;
				String savePathFull = C.IMG_DIR + C.SEP + spaceID + C.SEP
						+ "full" + C.SEP + imgToUploadName;
				String savePathThumb = C.IMG_DIR + C.SEP + spaceID + C.SEP
						+ "thumb" + C.SEP + imgToUploadName;
				uploadPath = savePathThumb;
				final File destFull = new File(savePathFull);
				final File destThumb = new File(savePathThumb);
				LogController.log("File to upload: " + src + " -> "
						+ src.length() + " bytes");
				Runnable job = new Runnable() {
					@Override
					public void run() {
						if (srcW > C.IMG_WIDTH || srcH > C.IMG_HEIGHT) {
							// larger image, resize
							try {
								Thumbnails.of(src)
										.size(C.IMG_WIDTH, C.IMG_HEIGHT)
										.toFile(destFull);
							} catch (IOException ex) {
								LogController.logEvent(this, C.WARNING, ex);
							}
						} else {
							// image smaller, just copy
							try {
								final FileInputStream is = new FileInputStream(
										src);
								final FileOutputStream os = new FileOutputStream(
										destFull);
								int currentbyte = is.read();
								while (currentbyte != -1) {
									os.write(currentbyte);
									currentbyte = is.read();
								}
								is.close();
								os.close();
							} catch (IOException ex) {
								LogController.logEvent(this, C.WARNING, ex);
								;
							}
						}
						LogController.log("File uploaded: " + destFull + " -> "
								+ destFull.length() + " bytes");
						// thumbnail
						try {
							Thumbnails.of(destFull)
									.size(C.THUMB_WIDTH, C.THUMB_HEIGHT)
									.toFile(destThumb);
						} catch (IOException ex) {
							LogController.logEvent(this, C.WARNING, ex);
						}
					}

				};
				BusyIndicator.showWhile(display, job);
				EsmApplication.alert("The image was uploaded!");
				ok = true;
			} catch (Exception ex) {
				LogController.log(ex.toString());
			}
		}
		return ok;
	}

	public static String uploadSpaceImagePath(int spaceID, String[] imgDetails) {
		String s = "";
		if (uploadSpaceImage(spaceID, imgDetails)) {
			s = uploadPath;
		}
		return s;
	}

	public static boolean uploadSpaceDocument(int spaceID, String[] docDetails) {
		boolean ok = false;
		if (docDetails == null) {
			docDetails = uploadSpaceDocumentDialog();
		}
		if (docDetails != null) {
			try {
				File src = new File(docDetails[0]);
				String savePath = C.DOC_DIR + C.SEP + spaceID + C.SEP
						+ docDetails[1];
				final File dest = new File(savePath);
				final FileInputStream is = new FileInputStream(src);
				final FileOutputStream os = new FileOutputStream(dest);
				LogController.log("File to upload: " + src + " -> "
						+ src.length() + " bytes");
				Runnable job = new Runnable() {
					@Override
					public void run() {
						try {
							int currentbyte = is.read();
							while (currentbyte != -1) {
								os.write(currentbyte);
								currentbyte = is.read();
							}
						} catch (IOException ex) {
							LogController.logEvent(this, C.ERROR, ex);
							;
						}
						LogController.log("File uploaded: " + dest + " -> "
								+ dest.length() + " bytes");
						try {
							is.close();
							os.close();
						} catch (IOException e) {
							LogController.logEvent(this, C.ERROR, e);
						}
					}

				};
				BusyIndicator.showWhile(display, job);
				EsmApplication.alert("The document was uploaded!");
				ok = true;
			} catch (IOException ex) {
				LogController.log(ex.toString());
			}
		}
		return ok;
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
