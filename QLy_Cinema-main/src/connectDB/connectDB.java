package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectDB {

    public static Connection getConnection() {
        Connection conn = null;
        try {
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        	String url = "jdbc:sqlserver://localhost\\MSSQLSERVER1:1433;databaseName=CinemaDB;encrypt=false";
            String user = "sa";
            String password = "12345";
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối thành công!");
        } catch (Exception e) {
            System.out.println("Kết nối thất bại!");
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Đã đóng kết nối.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
