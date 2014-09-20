package com.rmrdigitalmedia.esm.test;

import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

public class IoTest {

	public static void main(String[] args) {
		Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("JPEG");
		while (readers.hasNext()) {
		    System.out.println("reader: " + readers.next());
		}
	}

}
