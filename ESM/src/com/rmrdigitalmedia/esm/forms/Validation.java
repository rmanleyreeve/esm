package com.rmrdigitalmedia.esm.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class Validation {

	public static boolean validateFields(Text[] req) {
		boolean ok = true;
		for (Text t : req) {
			t.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			if (t.getText() == null || t.getText().equals("")) {
				t.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
				ok = false;
			}
		}
		return ok;
	}

	public static boolean validateDates(Combo[] req) {
		boolean ok = true;
		for (Combo c : req) {
			c.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			if (c.getText().equals("") || c.getText().equals("0")
					|| c.getText().equals("DAY") || c.getText().equals("MONTH")
					|| c.getText().equals("YEAR")) {
				ok = false;
				c.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			}
		}
		return ok;
	}

	public static boolean validateDropdowns(Combo[] req) {
		boolean ok = true;
		for (Combo c : req) {
			c.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			if (c.getText().equals("") || c.getText().equals("Select...")) {
				ok = false;
				c.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			}
		}
		return ok;
	}

	public static void validateError(Shell sh) {
		MessageBox mb = new MessageBox(sh, SWT.OK);
		mb.setText("Alert");
		mb.setMessage("Please complete the form");
		mb.open();
	}

}
