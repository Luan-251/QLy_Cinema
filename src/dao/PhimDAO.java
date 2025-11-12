package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import connectDB.connectDB;
import entity.Phim;

public class PhimDAO {
	
	// ========================= LẤY TẤT CẢ PHIM =========================
	public ArrayList<Phim> getAllPhim() {
		ArrayList<Phim> dsPhim = new ArrayList<>();
		Connection con = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		
		try {
			con = connectDB.getConnection();
			stmt = con.prepareCall("{Call GetAllPhim}");
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				Phim p = new Phim(
					rs.getString("maPhim"),
					rs.getString("tenPhim"),
					rs.getInt("thoiLuong"),
					rs.getString("tenDaoDien"),
					rs.getString("tenDienVien"),
					rs.getString("theLoai"),
					rs.getDouble("giaPhim")
				);
				dsPhim.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(con, stmt, rs);
		}
		return dsPhim;
	}

	// ========================= THÊM PHIM =========================
	public boolean insertPhim(Phim p) {
	    Connection con = null;
	    CallableStatement stmt = null;
	    int n = 0;
	    try {
	        con = connectDB.getConnection();
	        stmt = con.prepareCall("{call InsertPhim(?, ?, ?, ?, ?, ?, ?)}");
	        stmt.setString(1, p.getMaPhim());
	        stmt.setString(2, p.getTenPhim());
	        stmt.setInt(3, p.getThoiLuong());
	        stmt.setString(4, p.getTenDaoDien());
	        stmt.setString(5, p.getTenDienVien());
	        stmt.setString(6, p.getTheLoai());
	        stmt.setDouble(7, p.getGiaPhim());
	        n = stmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        closeResource(con, stmt, null);
	    }
	    return n > 0;
	}

	// ======== XÓA PHIM (cập nhật: xóa luôn SuatChieu liên quan) =======
	public boolean deletePhim(String maPhim) {
	    Connection con = null;
	    CallableStatement stmt = null;
	    boolean success = false;

	    try {
	        con = connectDB.getConnection();
	        con.setAutoCommit(false); // bắt đầu transaction

	        // 1. Lấy tất cả suất chiếu của phim
	        ArrayList<String> dsSuat = new ArrayList<>();
	        String sqlSuat = "SELECT maSuatChieu FROM SuatChieu WHERE maPhim = ?";
	        try (PreparedStatement pst = con.prepareStatement(sqlSuat)) {
	            pst.setString(1, maPhim);
	            ResultSet rs = pst.executeQuery();
	            while (rs.next()) {
	                dsSuat.add(rs.getString("maSuatChieu"));
	            }
	        }

	        // 2. Xóa tất cả hóa đơn của vé liên quan đến các suất chiếu
	        String sqlDeleteHoaDon = "DELETE FROM HoaDon WHERE maVe IN (SELECT maVe FROM Ve WHERE maSuatChieu = ?)";
	        try (PreparedStatement pst = con.prepareStatement(sqlDeleteHoaDon)) {
	            for (String maSuat : dsSuat) {
	                pst.setString(1, maSuat);
	                pst.executeUpdate();
	            }
	        }

	        // 3. Xóa tất cả vé của các suất chiếu
	        String sqlDeleteVe = "DELETE FROM Ve WHERE maSuatChieu = ?";
	        try (PreparedStatement pst = con.prepareStatement(sqlDeleteVe)) {
	            for (String maSuat : dsSuat) {
	                pst.setString(1, maSuat);
	                pst.executeUpdate();
	            }
	        }

	        // 4. Xóa tất cả suất chiếu của phim
	        String sqlDeleteSuat = "DELETE FROM SuatChieu WHERE maPhim = ?";
	        try (PreparedStatement pst = con.prepareStatement(sqlDeleteSuat)) {
	            pst.setString(1, maPhim);
	            pst.executeUpdate();
	        }

	        // 5. Xóa phim
	        stmt = con.prepareCall("{call DeletePhim(?)}");
	        stmt.setString(1, maPhim);
	        stmt.executeUpdate();

	        con.commit();
	        success = true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        try { if (con != null) con.rollback(); } catch (Exception ex) {}
	    } finally {
	        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
	        try { if (con != null) connectDB.closeConnection(con); } catch (Exception e) {}
	    }

	    return success;
	}




	// ========================= CẬP NHẬT PHIM =========================
	public boolean updatePhim(Phim p) {
	    Connection con = null;
	    CallableStatement stmt = null;
	    int n = 0;
	    try {
	        con = connectDB.getConnection();
	        stmt = con.prepareCall("{call UpdatePhim(?, ?, ?, ?, ?, ?, ?)}");
	        stmt.setString(1, p.getMaPhim());
	        stmt.setString(2, p.getTenPhim());
	        stmt.setInt(3, p.getThoiLuong());
	        stmt.setString(4, p.getTenDaoDien());
	        stmt.setString(5, p.getTenDienVien());
	        stmt.setString(6, p.getTheLoai());
	        stmt.setDouble(7, p.getGiaPhim());
	        n = stmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	    	closeResource(con, stmt, null);
	    }
	    return n > 0;
	}

	// ========================= TÌM PHIM THEO MÃ =========================
	public Phim findPhimByMa(String maPhim) {
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    Phim p = null;

	    try {
	        con = connectDB.getConnection();
	        stmt = con.prepareStatement("SELECT * FROM Phim WHERE maPhim = ?");
	        stmt.setString(1, maPhim);
	        rs = stmt.executeQuery();

	        if (rs.next()) {
	            p = new Phim(
	                rs.getString("maPhim"),
	                rs.getString("tenPhim"),
	                rs.getInt("thoiLuong"),
	                rs.getString("tenDaoDien"),
	                rs.getString("tenDienVien"),
	                rs.getString("theLoai"),
	                rs.getDouble("giaPhim")
	            );
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	    	closeResource(con, stmt, rs);
	    }
	    return p;
	}

	// ========================= TÌM PHIM THEO TÊN (LIKE %) =========================
	public ArrayList<Phim> findPhimByTen(String tenPhim) {
	    ArrayList<Phim> dsPhim = new ArrayList<>();
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        con = connectDB.getConnection();
	        stmt = con.prepareStatement("SELECT * FROM Phim WHERE tenPhim LIKE ?");
	        stmt.setString(1, "%" + tenPhim + "%");
	        rs = stmt.executeQuery();

	        while (rs.next()) {
	            dsPhim.add(new Phim(
	                rs.getString("maPhim"),
	                rs.getString("tenPhim"),
	                rs.getInt("thoiLuong"),
	                rs.getString("tenDaoDien"),
	                rs.getString("tenDienVien"),
	                rs.getString("theLoai"),
	                rs.getDouble("giaPhim")
	            ));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	    	closeResource(con, stmt, rs);
	    }
	    return dsPhim;
	}
	
	public boolean assignSuatChieuToPhim(String maPhim, String maSuatChieu) {
	    String sql = "UPDATE SuatChieu SET maPhim = ? WHERE maSuatChieu = ?";
	    try (Connection con = connectDB.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {
	        pst.setString(1, maPhim);
	        pst.setString(2, maSuatChieu);
	        return pst.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}



	// ========================= LỌC PHIM THEO SUẤT CHIẾU =========================
	public ArrayList<Phim> getPhimTheoSuatChieu(String maSuatChieu) {
	    ArrayList<Phim> dsPhim = new ArrayList<>();
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        con = connectDB.getConnection();
	        String sql = "SELECT p.* FROM Phim p JOIN SuatChieu sc ON p.maPhim = sc.maPhim WHERE sc.maSuatChieu = ?";
	        stmt = con.prepareStatement(sql);
	        stmt.setString(1, maSuatChieu);
	        rs = stmt.executeQuery();

	        while (rs.next()) {
	            Phim p = new Phim(
	                rs.getString("maPhim"),
	                rs.getString("tenPhim"),
	                rs.getInt("thoiLuong"),
	                rs.getString("tenDaoDien"),
	                rs.getString("tenDienVien"),
	                rs.getString("theLoai"),
	                rs.getDouble("giaPhim")
	            );
	            dsPhim.add(p);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        connectDB.closeConnection(con);
	    }
	    return dsPhim;
	}
	
	// Lấy tất cả suất chiếu của 1 phim
	public ArrayList<String> getSuatChieuCuaPhim(String maPhim) {
	    ArrayList<String> dsSuat = new ArrayList<>();
	    String sql = "SELECT maSuatChieu, thoiGianBatDau FROM SuatChieu WHERE maPhim = ?";
	    try (Connection con = connectDB.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {
	        pst.setString(1, maPhim);
	        ResultSet rs = pst.executeQuery();
	        while (rs.next()) {
	            String ma = rs.getString("maSuatChieu");
	            //String time = rs.getString("thoiGianBatDau");
	            dsSuat.add(ma);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return dsSuat;
	}
	
	// ========================= HÀM ĐÓNG TÀI NGUYÊN =========================
	private void closeResource(Connection con, java.sql.Statement stmt, ResultSet rs) {
		try { if (rs != null) rs.close(); } catch (Exception e) {}
		try { if (stmt != null) stmt.close(); } catch (Exception e) {}
		try { if (con != null) connectDB.closeConnection(con); } catch (Exception e) {}
	}
}
