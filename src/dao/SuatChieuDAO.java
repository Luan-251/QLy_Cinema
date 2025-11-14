package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import connectDB.connectDB;
import entity.Phim;
import entity.Phong;
import entity.SuatChieu;

public class SuatChieuDAO {

	public ArrayList<SuatChieu> getSuatChieuByPhim(Phim phim) {
		ArrayList<SuatChieu> ds = new ArrayList<>();
		String sql = "SELECT maSuatChieu, thoiGianBatDau, maPhong " + "FROM SuatChieu WHERE maPhim = ?";

		try (Connection con = connectDB.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

			pst.setString(1, phim.getMaPhim());
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				SuatChieu sc = new SuatChieu(rs.getString("maSuatChieu"),
						rs.getTimestamp("thoiGianBatDau").toLocalDateTime(), null, // không có thoiGianKetThuc
						phim, new Phong(rs.getString("maPhong"), null) // tạo phòng từ maPhong
				);
				ds.add(sc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}
}
