package com.rmrdigitalmedia.esm.table;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

public class ImageCell extends StyledCellLabelProvider {

	private Image image;

	public ImageCell(Image img) {
		image = img;
	}

	@Override
	protected void paint(Event event, Object element) {
		super.paint(event, element);
		if (image == null) {
			return;
		}
		Rectangle cellBounds = getViewerCellBounds(event);
		if (cellBounds == null) {
			return;
		}
		Rectangle bounds = image.getBounds();
		// center the image in the given space
		int x = cellBounds.x
				+ Math.max(0, (cellBounds.width - bounds.width) / 2);
		int y = cellBounds.y
				+ Math.max(0, (cellBounds.height - bounds.height) / 2);
		event.gc.drawImage(image, x, y);
	}

	private static Rectangle getViewerCellBounds(Event event) {
		if (event.item instanceof TableItem) {
			return ((TableItem) event.item).getBounds(event.index);
		}
		return ((TreeItem) event.item).getBounds(event.index);
	}

}
