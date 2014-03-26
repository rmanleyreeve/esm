package com.rmrdigitalmedia.esm.test;

/*******************************************************************************
 * Copyright (c) 2006-2007 Nicolas Richeton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors :
 *    Nicolas Richeton (nicolas.richeton@gmail.com) - initial implementation
 *******************************************************************************/

import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.rmrdigitalmedia.esm.C;

/**
 * This widget displays a simple gallery with some content.<br/>
 * Scrolling is vertical.<br/>
 * <br/>
 * 
 * <p>
 * NOTE: THIS WIDGET AND ITS API ARE STILL UNDER DEVELOPMENT. THIS IS A
 * PRE-RELEASE ALPHA VERSION. USERS SHOULD EXPECT API CHANGES IN FUTURE
 * VERSIONS.
 * </p>
 * 
 * @author Nicolas Richeton (nicolas.richeton@gmail.com)
 */

public class SnippetSimple {
	
	

	public SnippetSimple() {
	}

	public static void main(String[] args) {
		Display display = new Display();
		Image itemImage = new Image(display, Program.findProgram("jpg").getImageData()); //$NON-NLS-1$

		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Gallery gallery = new Gallery(shell, SWT.V_SCROLL | SWT.MULTI);

		// Renderers
		//DefaultGalleryGroupRenderer gr = new DefaultGalleryGroupRenderer();
		NoGroupRenderer gr = new NoGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemHeight(150);
		gr.setItemWidth(150);
		gr.setAutoMargin(true);
		gallery.setGroupRenderer(gr);

		DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
		gallery.setItemRenderer(ir);
		
		GalleryItem group = new GalleryItem(gallery, SWT.NONE);

//		GalleryItem group = new GalleryItem(gallery, SWT.NONE);
//		group.setText("Group"); //$NON-NLS-1$
//		group.setExpanded(true);

		for (int i = 0; i < 10; i++) {
			GalleryItem item = new GalleryItem(group, SWT.NONE);
			if (itemImage != null) {
				item.setImage(itemImage);
			}
			item.setText("Item " + i); //$NON-NLS-1$
		}
	
		shell.setSize(470, 200);
		//shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		if (itemImage != null)
			itemImage.dispose();
		display.dispose();
	}
}