package com.rmrdigitalmedia.esm.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class Validation {
	
	public static String errMsg = "Please complete the form";

	public static boolean validateFields(Text[] req) {
		boolean ok = true;
		for (Text t : req) {
			t.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			if (t.getText() == null || t.getText().equals("")) {
				t.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
				errMsg = "Please complete the form";
				ok = false;
			}
		}
		return ok;
	}

	public static boolean checkMatch(Text[] req) {
		if(req.length != 2) return false;
		boolean ok = true;
		Text t1, t2;
		t1 = req[0]; t2 = req[1];
		t1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		if (t1.getText() == null || t2.getText() == null ||
				!t1.getText().equals(t2.getText())
				) {
			t1.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			t2.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			errMsg = "Values do not match!";
			ok = false;

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
				errMsg = "Please complete the form";
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
				errMsg = "Please complete the form!";
			}
		}
		return ok;
	}

	public static void validateError(Shell sh) {
		MessageBox mb = new MessageBox(sh, SWT.OK);
		mb.setText("Alert");
		mb.setMessage(errMsg);
		mb.open();
	}

}
