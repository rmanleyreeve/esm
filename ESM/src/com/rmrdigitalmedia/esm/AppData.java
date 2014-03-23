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

	public void setData(Hashtable<String, Object> data) {
		this.data = data;
	}
	
	public Object getField(String field) {
		if (data.get(field)!=null) {
			return data.get(field);
		}
		return new String("");
	}
	
	public void setField(String field, Object value) {
		data.put(field, value);
		LogController.log("APPDATA: " + this.data.toString());
	}
	
	public void serialize() throws SQLException {
		// loop through hashtable & save to db		
		Enumeration<String> enumKey = data.keys();
		while(enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    String val = (String)data.get(key);
		    AppdataTable.Row row = AppdataTable.getRow();
		    row.setKey(key);
		    row.setValue(val);
		    row.insert();
		}
		data.clear();
	}
	
	public void unserialize() throws SQLException {
		// load data from db
		data.clear();
		AppdataTable.Row[] rows = AppdataTable.getAllRows();
		for (AppdataTable.Row row:rows){
			data.put(row.getKey(), row.getValue());
		}		
	}

}
