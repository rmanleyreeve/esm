package com.rmrdigitalmedia.esm;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import com.rmrdigitalmedia.esm.controllers.LogController;
import com.rmrdigitalmedia.esm.models.AppdataTable;

/**
 * The Class AppData.
 */
public class AppData {

	public Hashtable<String, Object> data;

	public AppData() {
		this.data = new Hashtable<String, Object>();
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Hashtable<String, Object> getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param _data the _data
	 */
	public void setData(Hashtable<String, Object> _data) {
		this.data = _data;
	}

	/**
	 * Gets the field.
	 *
	 * @param field the field to get
	 * @return the field's value
	 */
	public Object getField(String field) {
		if (this.data.get(field) != null) {
			return this.data.get(field);
		}
		return new String("");
	}

	/**
	 * Sets the field.
	 *
	 * @param field the field name
	 * @param value the value to set
	 */
	public void setField(String field, Object value) {
		this.data.put(field, value);
	}

	/**
	 * Delete field.
	 *
	 * @param field the field to delete
	 */
	public void deleteField(String field) {
		this.data.remove(field);
	}

	/**
	 * Dump.
	 *
	 * @return the string
	 */
	public String dump() {
		LogController.log("APPDATA: " + this.data.toString());
		return this.data.toString();
	}

	/**
	 * Serialize.
	 * writes the current hashtable to database as key/value pairs
	 * @throws SQLException the SQL exception
	 */
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

	/**
	 * Unserialize.
	 * loads the saved data back into a hashtable
	 * @throws SQLException the SQL exception
	 */
	public void unserialize() throws SQLException {
		// load data from db
		this.data.clear();
		AppdataTable.Row[] rows = AppdataTable.getAllRows();
		for (AppdataTable.Row row : rows) {
			this.data.put(row.getKey(), row.getValue());
		}
	}

}
