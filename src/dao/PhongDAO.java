package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.connectDB;
import entity.Phong;

public class PhongDAO {
	public ArrayList<Phong> getPhongByPhim(String maPhim) {
		ArrayList<Phong> dsPhong = new ArrayList<>();
		String sql = "SELECT DISTINCT p.MaPhong, p.TenPhong "
				+ "FROM SuatChieu sc JOIN Phong p ON sc.MaPhong = p.MaPhong " + "WHERE sc.MaPhim = ?";
		try (Connection con = connectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, maPhim);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
//				dsPhong.add(new Phong(rs.getString("MaPhong"), rs.getString("TenPhong")));
				dsPhong.add(new Phong(rs.getString("Ma phong"), rs.getString("Ten Phong"), 0, 0));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dsPhong;
	}
}
