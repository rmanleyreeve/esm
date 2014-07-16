package com.rmrdigitalmedia.esm;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.models.AppdataTable;

public class AppData {

	public Hashtable<String, Object> data;

	public AppData() {
		this.data = new Hashtable<String, Object>();
	}

	public Hashtable<String, Object> getData() {
		return data;
	}

	public void setData(Hashtable<String, Object> _data) {
		this.data = _data;
	}

	public Object getField(String field) {
		if (this.data.get(field) != null) {
			return this.data.get(field);
		}
		return new String("");
	}

	public void setField(String field, Object value) {
		this.data.put(field, value);
	}

	public String dump() {
		LogController.log("APPDATA: " + this.data.toString());
		return this.data.toString();
	}

	public void serialize() throws SQLException {
		// loop through hashtable & save to db
		Enumeration<String> enumKey = this.data.keys();
		AppdataTable.delete("ID", "IS NOT NULL");
		while (enumKey.hasMoreElements()) {
			String key = enumKey.nextElement();
			String val = (String) this.data.get(key);
			AppdataTable.Row row = AppdataTable.getRow();
			row.setKey(key);
			row.setValue(val);
			row.insert();
		}
		this.data.clear();
	}

	public void unserialize() throws SQLException {
		// load data from db
		this.data.clear();
		AppdataTable.Row[] rows = AppdataTable.getAllRows();
		for (AppdataTable.Row row : rows) {
			this.data.put(row.getKey(), row.getValue());
		}
	}

}
