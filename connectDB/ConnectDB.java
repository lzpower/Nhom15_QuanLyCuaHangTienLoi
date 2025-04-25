package connectDB;//aaaaa

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
	public static Connection con = null;
	private static ConnectDB instance = new ConnectDB();

	public static ConnectDB getInstance() {
		return instance;
	}

	public void connect() throws SQLException {
<<<<<<< HEAD
		String url = "jdbc:sqlserver://localhost: 1433; databasename=CuaHangTienLoi2";
		String user = "sa";
		String password = "sapassword";
=======
		String url = "jdbc:sqlserver://localhost: 1433; databasename=CuaHangTienLoidm";
		String user = "sa";
		String password = "12345";
>>>>>>> 8d30f36358accfa122b287ec8e3a21af0446811d
		con = DriverManager.getConnection(url, user, password);
	}

	public void disconnect() {
		if (con != null)
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public static Connection getConnection() {
		return con;
	}
}