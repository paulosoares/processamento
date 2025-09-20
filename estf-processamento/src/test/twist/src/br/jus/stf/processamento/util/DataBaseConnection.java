package br.jus.stf.processamento.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

@Component
public class DataBaseConnection {
	
	Connection connection;
	
	public DataBaseConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("*** DataBaseConnection: Não foi possível carregar o driver do banco.");
			e.printStackTrace();
			
			return ;
		}
	}
	
	public Connection connect() throws SQLException {
		
		String urlConnection = "jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = bd-orcl-qual.stf.jus.br)(PORT = 1521)) ) (CONNECT_DATA = (SERVICE_NAME = stfq_ovm) (SERVER = DEDICATED) ) )";
		if(connection == null) {
			connection = DriverManager.getConnection(urlConnection, "QUALIDADE", "testequalidade");
		}
		
		return connection;
		
	}

}

