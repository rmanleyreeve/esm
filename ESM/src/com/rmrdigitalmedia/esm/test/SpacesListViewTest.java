package com.rmrdigitalmedia.esm.test;

import java.awt.Color;
import java.awt.event.PaintEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import com.rmrdigitalmedia.esm.AppData;
import com.rmrdigitalmedia.esm.C;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.models.SpacesTable;


@SuppressWarnings("unused")
public class SpacesListViewTest {

	private static SpacesTable.Row[] rows;
	private static Label sep;
	private static int rowHeight = 35;
	private static int colHeaderH = 40;

	public static void main(String[] args) {
		// FOR WINDOW BUILDER DESIGN VIEW
		try {
			Shell shell = new Shell();
			shell.setSize(1380, 750);
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			Composite comp1 = new Composite(shell, SWT.BORDER);
			shell.open();
			buildTable(comp1);
			while (!shell.isDisposed()) {
				if (!Display.getDefault().readAndDispatch()) {
					Display.getDefault().sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	public static void buildTable(Composite parent) {
		LogController.log("Building Space List page");
		parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_WAIT));

		for (Control c:parent.getChildren()) {
			c.dispose();
		}
		parent.setLayout(new FillLayout(SWT.VERTICAL));

		// scrolling frame to hold the grid panel
		final ScrolledComposite scrollPanel = new ScrolledComposite(parent, SWT.V_SCROLL);
		scrollPanel.setExpandHorizontal(true);

		// the grid panel that holds the various info rows
		final Composite comp = new Composite(scrollPanel, SWT.NONE);
		GridLayout gl_comp = new GridLayout(1, true);
		gl_comp.horizontalSpacing = 0;
		gl_comp.verticalSpacing = 0;
		gl_comp.marginWidth = 0;
		gl_comp.marginHeight = 0;
		comp.setLayout(gl_comp);
		comp.setBackground(C.APP_BGCOLOR);

		//table layout
		final Composite tbl = new Composite(comp, SWT.BORDER);
		tbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gl_tbl = new GridLayout(6, false);
		gl_tbl.marginTop = 0;
		gl_tbl.marginHeight = 0;
		gl_tbl.marginWidth = 0;
		gl_tbl.verticalSpacing = 1;
		gl_tbl.horizontalSpacing = 1;
		tbl.setLayout(gl_tbl);
		tbl.setBackground(C.APP_BGCOLOR);

		// column headers
		// final String[] titles = { "ID", "Name", "Completion Status","Internal Classification","Entry Points Classification", "S/O" };

		CLabel lblHeaderID = new CLabel(tbl, SWT.BORDER);
		lblHeaderID.setLeftMargin(5);
		GridData gd_lblHeaderID = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblHeaderID.widthHint = 75;
		gd_lblHeaderID.heightHint = colHeaderH;
		lblHeaderID.setLayoutData(gd_lblHeaderID);
		lblHeaderID.setBackground(C.BAR_BGCOLOR);
		lblHeaderID.setFont(C.FONT_12B);
		lblHeaderID.setText("ID");

		CLabel lblHeaderName = new CLabel(tbl, SWT.BORDER);
		lblHeaderName.setLeftMargin(5);
		GridData gd_lblHeaderName = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_lblHeaderName.minimumWidth = 200;
		gd_lblHeaderName.widthHint = 200;
		gd_lblHeaderName.heightHint = colHeaderH;
		lblHeaderName.setLayoutData(gd_lblHeaderName);
		lblHeaderName.setBackground(C.BAR_BGCOLOR);
		lblHeaderName.setFont(C.FONT_12B);
		lblHeaderName.setText("Name");

		CLabel lblHeaderCS = new CLabel(tbl, SWT.BORDER | SWT.CENTER);
		lblHeaderCS.setLeftMargin(5);
		GridData gd_lblHeaderCS = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblHeaderCS.widthHint = 160;
		gd_lblHeaderCS.heightHint = colHeaderH;
		lblHeaderCS.setLayoutData(gd_lblHeaderCS);
		lblHeaderCS.setBackground(C.BAR_BGCOLOR);
		lblHeaderCS.setFont(C.FONT_12B);
		lblHeaderCS.setText("Completion Status");

		CLabel lblHeaderIC = new CLabel(tbl, SWT.BORDER | SWT.CENTER);
		lblHeaderIC.setLeftMargin(5);
		GridData gd_lblHeaderIC = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblHeaderIC.widthHint = 220;
		gd_lblHeaderIC.heightHint = colHeaderH;
		lblHeaderIC.setLayoutData(gd_lblHeaderIC);
		lblHeaderIC.setBackground(C.BAR_BGCOLOR);
		lblHeaderIC.setText("Internal Classification");
		lblHeaderIC.setFont(C.FONT_12B);

		CLabel lblHeaderEPC = new CLabel(tbl, SWT.BORDER);
		lblHeaderEPC.setLeftMargin(5);
		lblHeaderEPC.setAlignment(SWT.CENTER);
		GridData gd_lblHeaderEPC = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblHeaderEPC.widthHint = 220;
		gd_lblHeaderEPC.heightHint = colHeaderH;
		lblHeaderEPC.setLayoutData(gd_lblHeaderEPC);
		lblHeaderEPC.setBackground(C.BAR_BGCOLOR);
		lblHeaderEPC.setText("Entry Points Classification");
		lblHeaderEPC.setFont(C.FONT_12B);

		CLabel lblHeaderSO = new CLabel(tbl, SWT.BORDER);
		lblHeaderSO.setRightMargin(5);
		lblHeaderSO.setAlignment(SWT.CENTER);
		GridData gd_lblHeaderSO = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblHeaderSO.widthHint = 75;
		gd_lblHeaderSO.heightHint = colHeaderH;
		lblHeaderSO.setLayoutData(gd_lblHeaderSO);
		lblHeaderSO.setBackground(C.BAR_BGCOLOR);
		lblHeaderSO.setText("S/O");
		lblHeaderSO.setFont(C.FONT_12B);

		// start loop through spaces rows
		final CLabel lblLoopID = new CLabel(tbl, SWT.BORDER);
		lblLoopID.setLeftMargin(5);
		GridData gd_lblLoopID = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblLoopID.widthHint = 75;
		gd_lblLoopID.heightHint = colHeaderH;
		lblLoopID.setLayoutData(gd_lblLoopID);
		lblLoopID.setBackground(C.FIELD_BGCOLOR);
		lblLoopID.setFont(C.FONT_11);
		lblLoopID.setText("001");

		final CLabel lblLoopName = new CLabel(tbl, SWT.BORDER);
		lblLoopName.setLeftMargin(5);
		GridData gd_lblLoopName = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_lblLoopName.minimumWidth = 200;
		gd_lblLoopName.widthHint = 200;
		gd_lblLoopName.heightHint = colHeaderH;
		lblLoopName.setLayoutData(gd_lblLoopName);
		lblLoopName.setBackground(C.FIELD_BGCOLOR);
		lblLoopName.setFont(C.FONT_11);
		lblLoopName.setText("Forepeak Tank");

		final CLabel lblLoopCS = new CLabel(tbl, SWT.BORDER | SWT.CENTER);
		lblLoopCS.setLeftMargin(5);
		GridData gd_lblLoopCS = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblLoopCS.widthHint = 160;
		gd_lblLoopCS.heightHint = colHeaderH;
		lblLoopCS.setLayoutData(gd_lblLoopCS);
		lblLoopCS.setBackground(C.FIELD_BGCOLOR);
		lblLoopCS.setImage(C.getImage("Percent_20.png"));

		final CLabel lblLoopIC = new CLabel(tbl, SWT.BORDER | SWT.CENTER);
		lblLoopIC.setLeftMargin(5);
		GridData gd_lblLoopIC = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblLoopIC.widthHint = 220;
		gd_lblLoopIC.heightHint = colHeaderH;
		lblLoopIC.setLayoutData(gd_lblLoopIC);
		lblLoopIC.setBackground(C.FIELD_BGCOLOR);
		lblLoopIC.setImage(C.getImage("red.png"));

		final CLabel lblLoopEPC = new CLabel(tbl, SWT.BORDER | SWT.CENTER);
		lblLoopEPC.setLeftMargin(5);
		GridData gd_lblLoopEPC = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblLoopEPC.widthHint = 220;
		gd_lblLoopEPC.heightHint = colHeaderH;
		lblLoopEPC.setLayoutData(gd_lblLoopEPC);
		lblLoopEPC.setBackground(C.FIELD_BGCOLOR);

		ImageData ideaData = new ImageData(new AppData().getClass().getResourceAsStream("/img/blank.jpg"));		
		int whitePixel = ideaData.palette.getPixel(new RGB(255,255,255));
		ideaData.transparentPixel = whitePixel;
		Image transparentIdeaImage = new Image(Display.getDefault(),ideaData);
		GC gc = new GC(transparentIdeaImage);
		gc.setAntialias(SWT.ON);
		gc.drawImage(C.getImage("red.png"), 70, 0);
		gc.setBackground(C.AMBER);
		gc.fillOval(50, 0, 18, 18);
		lblLoopEPC.setImage(transparentIdeaImage);

		final CLabel lblLoopSO = new CLabel(tbl, SWT.BORDER);
		lblLoopSO.setRightMargin(5);
		lblLoopSO.setAlignment(SWT.CENTER);
		GridData gd_lblLoopSO = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblLoopSO.widthHint = 75;
		gd_lblLoopSO.heightHint = colHeaderH;
		lblLoopSO.setLayoutData(gd_lblLoopSO);
		lblLoopSO.setBackground(C.FIELD_BGCOLOR);
		lblLoopSO.setImage(C.getImage("bluetick.png"));

		lblLoopID.setData(false);
		lblLoopName.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent arg0) {
				if(!(Boolean) lblLoopID.getData()) {
					lblLoopID.setBackground(C.ROW_HIGHLIGHT);
					lblLoopName.setBackground(C.ROW_HIGHLIGHT);
					lblLoopCS.setBackground(C.ROW_HIGHLIGHT);
					lblLoopIC.setBackground(C.ROW_HIGHLIGHT);
					lblLoopEPC.setBackground(C.ROW_HIGHLIGHT);
					lblLoopSO.setBackground(C.ROW_HIGHLIGHT);
				}
			}
			@Override
			public void mouseExit(MouseEvent arg0) {
				if(!(Boolean) lblLoopID.getData()) {
					lblLoopID.setBackground(C.FIELD_BGCOLOR);
					lblLoopName.setBackground(C.FIELD_BGCOLOR);
					lblLoopCS.setBackground(C.FIELD_BGCOLOR);
					lblLoopIC.setBackground(C.FIELD_BGCOLOR);
					lblLoopEPC.setBackground(C.FIELD_BGCOLOR);
					lblLoopSO.setBackground(C.FIELD_BGCOLOR);
				}
			}
		});
		lblLoopName.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				lblLoopID.setData( ! (Boolean) lblLoopID.getData());
				lblLoopID.setBackground(C.ROW_SELECTED);
				lblLoopName.setBackground(C.ROW_SELECTED);
				lblLoopCS.setBackground(C.ROW_SELECTED);
				lblLoopIC.setBackground(C.ROW_SELECTED);
				lblLoopEPC.setBackground(C.ROW_SELECTED);
				lblLoopSO.setBackground(C.ROW_SELECTED);
			}
		});



		// end loop



		// redraw panel on window resize
		scrollPanel.setContent(comp);
		scrollPanel.setExpandVertical(true);
		scrollPanel.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle r = scrollPanel.getClientArea();
				scrollPanel.setMinSize(comp.computeSize(r.width, SWT.DEFAULT));
			}
		});

		// final layout settings	
		parent.layout();
		parent.getShell().setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_ARROW));
	}
}
