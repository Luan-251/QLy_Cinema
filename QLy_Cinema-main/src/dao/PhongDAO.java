package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.connectDB;
import entity.Phong;

public class PhongDAO {

    // Lấy danh sách phòng theo mã phim
    public ArrayList<Phong> getPhongByPhim(String maPhim) {
        ArrayList<Phong> dsPhong = new ArrayList<>();
        String sql = "SELECT DISTINCT p.MaPhong, p.TenPhong "
                   + "FROM SuatChieu sc JOIN Phong p ON sc.MaPhong = p.MaPhong "
                   + "WHERE sc.MaPhim = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maPhim);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dsPhong.add(new Phong(
                    rs.getString("MaPhong"),
                    rs.getString("TenPhong"),
                    0, 0  // nếu cần giá trị khác có thể sửa
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPhong;
    }

    // Tìm phòng theo mã phòng
    public Phong findPhongByMa(String maPhong) {
        String sql = "SELECT * FROM Phong WHERE MaPhong = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maPhong);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Phong(
                    rs.getString("MaPhong"),
                    rs.getString("TenPhong"),
                    rs.getInt("SoGhe"),   // hoặc cột khác phù hợp với table
                    0
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // không tìm thấy
    }
}
