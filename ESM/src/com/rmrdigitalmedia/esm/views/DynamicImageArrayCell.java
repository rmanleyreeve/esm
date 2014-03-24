package com.rmrdigitalmedia.esm.views;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import com.rmrdigitalmedia.esm.Constants;

import de.ralfebert.rcputils.properties.IValue;

/**
 * DynamicImageCell generates a cell image using a defined image name
 * @param imageName an {@link IValue} object that returns a String
 * 
 */

@SuppressWarnings("javadoc")
public class DynamicImageArrayCell extends StyledCellLabelProvider {
	
	private Image image;
	private IValue imageName;

	public DynamicImageArrayCell(IValue imageName) {
		this.imageName = imageName;
	}		
	
	@Override
	protected void paint(Event event, Object element) {
		String[] imgName = (String[])imageName.getValue(element);
		int numImages = imgName.length;
		
		image = Constants.getImage(imgName[0]);
		super.paint(event, element);
		if (image == null || numImages > 5) {
			return;
		}
		Rectangle cellBounds = getViewerCellBounds(event);
		if (cellBounds == null) {
			return;
		}
		Rectangle bounds = image.getBounds();
		int centreX = cellBounds.x + Math.max(0, (cellBounds.width - bounds.width) / 2);
		int x = cellBounds.x;
		int y = cellBounds.y + Math.max(0, (cellBounds.height - bounds.height) / 2);
		switch(numImages){
		case 1:
			x = centreX;
			break;
		case 2:
			x = (centreX - 20);
			break;
		case 3:
			x = (centreX - 30);
			break;
		case 4:
			x = (centreX - 40);
			break;
		case 5:
			x = (centreX - 50);
			break;
		}				
		for (int i=0;i<numImages;i++){
			image = Constants.getImage(imgName[i]);
			event.gc.drawImage(image, x, y);
			x += 25;
		}
	}

	private static Rectangle getViewerCellBounds(Event event) {
		if (event.item instanceof TableItem) {
			return ((TableItem) event.item).getBounds(event.index);
		}
		return ((TreeItem) event.item).getBounds(event.index);
	}  	
	
	
}
