package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import connectDB.connectDB;
import entity.Ghe;
import entity.LoaiGhe;
import entity.Phong;

public class GheDAO {

	// Lấy thông tin ghế theo mã ghế và mã phòng
	public Ghe getGheByMaGheAndMaPhong(String maGhe, String maPhong) {
		String sql = "SELECT * FROM Ghe WHERE MaGhe = ? AND MaPhong = ?";
		try (Connection con = connectDB.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, maGhe);
			pst.setString(2, maPhong);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				Ghe ghe = new Ghe();
				ghe.setMaGhe(rs.getString("MaGhe"));
				ghe.setTenGhe(rs.getString("MaGhe")); // TenGhe = MaGhe
				ghe.setGiaGhe(rs.getDouble("GiaGhe"));

				// Tạo đối tượng Phong
				Phong phong = new Phong(maPhong, null);
				ghe.setPhong(phong);

				// Thêm LoaiGhe
				String maLoaiGhe = rs.getString("MaLoaiGhe");
				LoaiGhe loaiGhe = new LoaiGhe(maLoaiGhe, maLoaiGhe);
				ghe.setLoaiGhe(loaiGhe);

				return ghe;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}