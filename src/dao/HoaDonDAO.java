package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import connectDB.connectDB;

public class HoaDonDAO {

	// Tạo mã hóa đơn mới tự động HD0001, HD0002,...
	public String taoMaHDMoi() {
		String maHD = "HD0001";
		String sql = "SELECT TOP 1 maHD FROM HoaDon WHERE maHD IS NOT NULL ORDER BY CAST(SUBSTRING(maHD, 3, LEN(maHD)-2) AS INT) DESC";

		try (Connection con = connectDB.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()) {

			if (rs.next()) {
				String last = rs.getString("maHD"); // ví dụ HD0010
				int num = Integer.parseInt(last.substring(2)) + 1;
				maHD = String.format("HD%04d", num); // HD0011
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return maHD;
	}

	// Thêm hóa đơn mới
	public boolean themHoaDon(String maHD, LocalDateTime ngayLap, String maNV, double khuyenMai, double giaVe,
			double tongTien, int trangThai) {
		String sql = "INSERT INTO HoaDon(maHD, ngayLap, maNV, khuyenMai, giaVe, tongTien, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = connectDB.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

			pst.setString(1, maHD);
			pst.setTimestamp(2, Timestamp.valueOf(ngayLap));
			pst.setString(3, maNV);
			pst.setDouble(4, khuyenMai);
			pst.setDouble(5, giaVe);
			pst.setDouble(6, tongTien);
			pst.setInt(7, trangThai);

			int kq = pst.executeUpdate();
			return kq > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
