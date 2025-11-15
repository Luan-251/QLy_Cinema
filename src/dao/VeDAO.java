package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import connectDB.connectDB;
import entity.Ve;

public class VeDAO {

	// Tạo mã vé mới tự động V0001, V0002,...
	public String taoMaVeMoi() {
		String maVe = "V0001";
		String sql = "SELECT TOP 1 MaVe FROM Ve WHERE MaVe IS NOT NULL ORDER BY CAST(SUBSTRING(MaVe, 2, LEN(MaVe)-1) AS INT) DESC";
		try (Connection con = connectDB.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()) {
			if (rs.next()) {
				String last = rs.getString("MaVe"); // ví dụ V0010
				int num = Integer.parseInt(last.substring(1)) + 1;
				maVe = String.format("V%04d", num); // V0011
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maVe;
	}

	// Lấy danh sách ghế đã đặt cho một suất chiếu
	public ArrayList<String> getGheDaDatBySuatChieu(String maSuatChieu, String maPhong) {
		ArrayList<String> dsGheDaDat = new ArrayList<>();
		String sql = "SELECT MaGhe FROM Ve WHERE MaSuatChieu = ? AND MaPhong = ? AND TrangThai = 'DA_DAT'";
		try (Connection con = connectDB.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, maSuatChieu);
			pst.setString(2, maPhong);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				dsGheDaDat.add(rs.getString("MaGhe"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsGheDaDat;
	}

	// Thêm vé vào database
	public boolean themVe(Ve ve) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = connectDB.getConnection();
			// Bảng Ve có cấu trúc: MaVe, MaSuatChieu, MaGhe, MaPhong, TrangThai
			String sql = "INSERT INTO Ve (MaVe, MaSuatChieu, MaGhe, MaPhong, TrangThai) VALUES (?, ?, ?, ?, ?)";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, ve.getMaVe());
			stmt.setString(2, ve.getSuatChieu().getMaSuatChieu());
			stmt.setString(3, ve.getGhe().getMaGhe());
			stmt.setString(4, ve.getGhe().getPhong().getMaPhong());
			stmt.setString(5, "DA_DAT"); // Trạng thái mặc định là ĐÃ ĐẶT
			int rows = stmt.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			System.out.println("❌ Lỗi khi thêm vé: " + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
		}
	}
}