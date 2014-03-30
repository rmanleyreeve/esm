package com.rmrdigitalmedia.esm.graphics;

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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.common.io.Files;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.LogController;



public class ImageUtils {
	
	static Display display = Display.getCurrent();
	static Shell shell = display.getShells()[0];

	public static String[] uploadSpaceImageDialog() {
		final FileDialog dialog = new FileDialog (shell, SWT.OPEN);
		dialog.setText("Choose an image");
		String platform = SWT.getPlatform();
		String [] filterNames = new String [] {"Image Files", "All Files (*)"};
		String [] filterExtensions = new String [] {"*.gif;*.png;*.xpm;*.jpg;*.jpeg;*.tiff", "*"};
		String filterPath = C.HOME_DIR + C.SEP + "Desktop";
		if (platform.equals("win32") || platform.equals("wpf")) {
			filterNames = new String [] {"Image Files", "All Files (*.*)"};
			filterExtensions = new String [] {"*.gif;*.png;*.bmp;*.jpg;*.jpeg;*.tiff", "*.*"};
		}
		dialog.setFilterNames (filterNames);
		dialog.setFilterExtensions (filterExtensions);
		dialog.setFilterPath (filterPath);
		dialog.open();		
		if(!dialog.getFileName().equals("")) {
			String fn = dialog.getFilterPath() + C.SEP + dialog.getFileName();
			return new String[] {fn, dialog.getFileName()};
		}
		return null;
	}		

	public static void uploadSpaceImage(int spaceID, String[] imgDetails) {
		if(imgDetails == null) {
			imgDetails = uploadSpaceImageDialog();		
		}
		if(imgDetails != null) {
			String imgToUploadName = imgDetails[1];
			String ext = Files.getFileExtension(imgToUploadName);
			String ts = "" + new Date().getTime();
			try {  				
			    File src = new File(imgDetails[0]);  
			    imgToUploadName = ts + "." + ext;
			    String savePathFull = C.IMG_DIR + C.SEP + spaceID + C.SEP + "full" + C.SEP + imgToUploadName;
			    String savePathThumb = C.IMG_DIR + C.SEP + spaceID + C.SEP + "thumb" + C.SEP + imgToUploadName;
			    final File destFull = new File(savePathFull);  	      
			    final File destThumb = new File(savePathThumb);  	      
			    final FileInputStream is = new FileInputStream(src);   
			    final FileOutputStream os = new FileOutputStream(destFull);   	
				LogController.log("File to upload: " + src + " -> " + src.length() + " bytes");
			    Runnable job = new Runnable() {
			    	@Override
					public void run() {
				      try {  
					      int currentbyte = is.read();  
					      while (currentbyte != -1) {  
						      os.write (currentbyte);  
						      currentbyte = is.read();  
					      }
						} catch (IOException ex){
							LogController.logEvent(this, 1, ex);;
						}
						LogController.log("File uploaded: " + destFull + " -> " + destFull.length() + " bytes");

						// thumbnail
						try {
							Thumbnails.of(destFull).size(150, 150).toFile(destThumb);
						} catch (IOException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
						try {
							is.close();
							os.close();   
						} catch (IOException e) {
							LogController.logEvent(this, 1, e);
						}   
					}
			    	
				};		    	
				BusyIndicator.showWhile(display, job);
				EsmApplication.alert("The image was uploaded!");				
			} catch(IOException ex){
				LogController.log(ex.toString());
			} 
		}
	}		

	
	public static boolean IsImageFile(File f){
		boolean valid = true;
		try {
		    BufferedImage image = ImageIO.read(f);
		    if (image == null) {
		        valid = false;
		    }
		} catch(IOException ex) {
		    valid=false;
		}		
		return valid;
	}


	public static Image getImage(String imgpath) {
		return SWTResourceManager.getImage(ImageUtils.class, imgpath);	
	}


	public static Image getExtImage(String imgpath) {
		// test for non-image or system files
		try {
		    BufferedImage image = ImageIO.read(new File(imgpath));
		    if (image == null) {
		        return null;
		    }
		} catch(IOException ex) {
		    return null;
		}	
		return SWTResourceManager.getImage(imgpath);
	}
	
	
	
}
