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

			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenda?serverTimezone=UTC", "alex", "Mercedes1896.");
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
	
	/**
	 * Metodo para imprimir la informacion de una Excepcion SQL y poder depurar errores f�cilmente
	 * @param ex
	 */
	public static void printSQLException(SQLException e) {
        
        while (e != null) {
			if (e instanceof SQLException) {
				//Estado ANSI
				e.printStackTrace(System.err);
				System.err.println("SQLState: "
						+ ((SQLException) e).getSQLState());
				//C�dio de error propio de cada gestor de BD
				System.err.println("Error Code: "
						+ ((SQLException) e).getErrorCode());
				//Mensaje textual
				System.err.println("Message: " + e.getMessage());

				//Objetos desencadenantes de la excepci�n
				Throwable t = e.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
				//Cualquier otra excepci�n encadenada
				e = e.getNextException();				
				
			}
		}
	}
}


