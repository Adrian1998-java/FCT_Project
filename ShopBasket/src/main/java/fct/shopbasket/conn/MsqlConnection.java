package fct.shopbasket.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MsqlConnection {

	public static Connection MySQLConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");

		String serverUrl = "jdbc:mysql://localhost:3306/";
		String username = "root";
		String password = "";

		Connection conn = DriverManager.getConnection(serverUrl, username, password);
		System.out.println("Connected Succesfully (MySQL)");

		conn.prepareStatement("use shopbasket").executeUpdate();
		return conn;
	}
}
