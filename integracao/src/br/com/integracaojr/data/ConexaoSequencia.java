package br.com.integracaojr.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexaoSequencia {

	private static Connection connection = null;	

	public static Connection getConnection(String dataBase) {
		
			try {
				Properties prop = new Properties();

				String user = "postgres";
				String password = "jrb@nc0Post";

				// Class.forName("com.mysql.jdbc.Driver");
				Class.forName("org.postgresql.Driver");
				// connection =
				// DriverManager.getConnection("jdbc:mysql//localhost:3306/elotech",user,
				// password);
				connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/"+dataBase, user, password);

				System.out.println("conectado com sucesso!!");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("fudeu!!");
			}
			return connection;
		}


	private String  getBanco(String Banco) {	
		return Banco;
		
		
	}

}


