package connectDB;

import connectDB.connectDB;
import java.sql.Connection;

public class TestConnectDB {
    public static void main(String[] args) {
        Connection conn = connectDB.getConnection(); // Gọi phương thức kết nối
        if (conn != null) {
            System.out.println("Đã kết nối tới cơ sở dữ liệu!");
        } else {
            System.out.println("Không thể kết nối đến cơ sở dữ liệu!");
        }

        // Đóng kết nối sau khi kiểm tra
        connectDB.closeConnection(conn);
    }
}
