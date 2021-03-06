package google.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLDB {

	private final boolean online;
	private final String url;
	private Statement statement;
	
	public SQLDB() throws ClassNotFoundException{
		this.online = false;
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); 
		this.url = "jdbc:odbc:contact_mgr"; 
	}
	
	public SQLDB(String database) throws ClassNotFoundException{
		this.online = true;
		Class.forName("com.imaginary.sql.msql.MsqlDriver");
		this.url = "jdbc:msql://" + database + ":1114/contact_mgr"; 
	}
	
	public final boolean isOnline(){
		return online;
	}
	
	public final boolean login(String username, String password){
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			this.statement = connection.createStatement();
			return true;
		} catch (SQLException exception) {
			exception.printStackTrace();
			return false;
		}		
	}
	
	public final void execute(String command) throws SQLException{
		statement.executeUpdate(command);
	}
	
}
