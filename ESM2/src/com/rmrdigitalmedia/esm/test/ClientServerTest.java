package com.rmrdigitalmedia.esm.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClientServerTest {

	public static void main(String[] args) {

		try {		

			/*
			for H2
			need to run this code on server:
			>java -classpath "C:\Program Files (x86)\H2\bin\h2-1.3.176.jar" org.h2.tools.Server -tcp  -tcpAllowOthers
			Class.forName("org.h2.Driver");
			String connStr = "jdbc:h2:tcp://192.168.1.2:9092//dbtest/ESM;";
			*/
			
			// for mysql
			Class.forName("com.mysql.jdbc.Driver");							
			String connStr = "jdbc:mysql://www.rmrdigitalmedia.co.uk/ta6rma_esm";
			Connection conn = DriverManager.getConnection(connStr, "ta6rma_hostuser", "theking");
			System.out.println("Connected OK");
			String sql = "SELECT NAME FROM _VESSEL_TYPES ORDER BY NAME ASC;";
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
