package com.rmrdigitalmedia.esm.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClientServerTest {

	public static void main(String[] args) {

		try {		
			String connStr = "jdbc:h2:tcp://192.168.1.11//dbtest/ESM;";
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection(connStr, "sa", "");
			System.out.println("Connected OK");
			String sql = "SELECT NAME FROM VESSEL_TYPES;";
			PreparedStatement st = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("NAME"));
			}
			rs.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
