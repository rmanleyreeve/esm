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
public class DynamicImageCell extends StyledCellLabelProvider {
	
	private Image image;
	private IValue imageName;

	public DynamicImageCell(IValue imageName) {
		this.imageName = imageName;
	}		
	
	@Override
	protected void paint(Event event, Object element) {
		String imgName = imageName.getValue(element).toString();
		//System.out.println(imgName);
		image = Constants.getImage(imgName);
		super.paint(event, element);
		if (image == null) {
			return;
		}
		Rectangle cellBounds = getViewerCellBounds(event);
		if (cellBounds == null) {
			return;
		}
		Rectangle bounds = image.getBounds();
		int x = cellBounds.x + Math.max(0, (cellBounds.width - bounds.width) / 2);
		int y = cellBounds.y + Math.max(0, (cellBounds.height - bounds.height) / 2);
		event.gc.drawImage(image, x, y);
	}

	private static Rectangle getViewerCellBounds(Event event) {
		if (event.item instanceof TableItem) {
			return ((TableItem) event.item).getBounds(event.index);
		}
		return ((TreeItem) event.item).getBounds(event.index);
	}  	
	
	
}
