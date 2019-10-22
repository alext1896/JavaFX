package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {
	public String dbms = "mysql";
	public String dbName =  "alex";
	public String userName = "alex";
	public String password = "Mercedes1896.";
	public String urlString;
	
	public Connection getConnection() throws SQLException {

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);

		if (this.dbms.equals("mysql")) {

			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/netflixo?serverTimezone=UTC", "alex", "Mercedes1896.");
		} else if (this.dbms.equals("derby")) {
			conn = DriverManager.getConnection("jdbc:" + this.dbms + ":"
					+ this.dbName + ";create=true", connectionProps);
		}
		System.out.println("Connectado a BD");
		System.out.println("-------------------------------------------------------------");
		return conn;
	}
	
	public static void closeConnection(Connection connArg) {
		try {
			if (connArg != null) {
				connArg.close();
				connArg = null;
			}
		} catch (SQLException sqle) {
			System.err.println(sqle);
		}
	}

}
