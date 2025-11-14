package dao;

import java.math.BigDecimal;
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
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = connectDB.getConnection();
            // Phù hợp với bảng Phim trong CinemaDB.sql
            String sql = "SELECT MaPhim, TenPhim, ThoiLuong, TenDaoDien, TenCacDienVien, TheLoai, GiaPhim FROM Phim ORDER BY TenPhim";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                dsPhim.add(mapPhim(rs));
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
        PreparedStatement stmt = null;
        int n = 0;
        try {
            con = connectDB.getConnection();
            // Lưu ý TenCacDienVien và GiaPhim DECIMAL(18,0)
            String sql = "INSERT INTO Phim (MaPhim, TenPhim, ThoiLuong, TenDaoDien, TenCacDienVien, TheLoai, GiaPhim) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, p.getMaPhim());
            stmt.setString(2, p.getTenPhim());
            stmt.setInt(3, p.getThoiLuong());
            stmt.setString(4, p.getTenDaoDien());
            stmt.setString(5, /* chỉnh theo entity của bạn */ p.getTenDienVien()); // hoặc p.getTenCacDienVien()
            stmt.setString(6, p.getTheLoai());

            // Nếu entity dùng double, chuyển sang BigDecimal cho an toàn
            BigDecimal gia = BigDecimal.valueOf(p.getGiaPhim());
            stmt.setBigDecimal(7, gia);

            n = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(con, stmt, null);
        }
        return n > 0;
    }

    // ========================= CẬP NHẬT PHIM =========================
    public boolean updatePhim(Phim p) {
        Connection con = null;
        PreparedStatement stmt = null;
        int n = 0;
        try {
            con = connectDB.getConnection();
            String sql = "UPDATE Phim SET TenPhim = ?, ThoiLuong = ?, TenDaoDien = ?, TenCacDienVien = ?, TheLoai = ?, GiaPhim = ? "
                       + "WHERE MaPhim = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, p.getTenPhim());
            stmt.setInt(2, p.getThoiLuong());
            stmt.setString(3, p.getTenDaoDien());
            stmt.setString(4, /* chỉnh theo entity của bạn */ p.getTenDienVien()); // hoặc p.getTenCacDienVien()
            stmt.setString(5, p.getTheLoai());
            stmt.setBigDecimal(6, BigDecimal.valueOf(p.getGiaPhim()));
            stmt.setString(7, p.getMaPhim());

            n = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(con, stmt, null);
        }
        return n > 0;
    }

    // ========================= XÓA PHIM (transaction, đúng với schema) =========================
    public boolean deletePhim(String maPhim) {
        Connection con = null;
        PreparedStatement pstDelCTHD = null;
        PreparedStatement pstDelVe = null;
        PreparedStatement pstDelSuat = null;
        PreparedStatement pstDelPhim = null;
        PreparedStatement pstDelHoaDonRong = null;
        boolean success = false;

        try {
            con = connectDB.getConnection();
            con.setAutoCommit(false);

            // 1) XÓA CHI TIẾT HÓA ĐƠN của các vé thuộc các suất chiếu của phim này
            //    ChiTietHoaDon(MaHoaDon, MaVe) JOIN Ve JOIN SuatChieu WHERE SuatChieu.MaPhim = ?
            String sqlDelCTHD =
                "DELETE cthd " +
                "FROM ChiTietHoaDon cthd " +
                "JOIN Ve v ON cthd.MaVe = v.MaVe " +
                "JOIN SuatChieu sc ON v.MaSuatChieu = sc.MaSuatChieu " +
                "WHERE sc.MaPhim = ?";
            pstDelCTHD = con.prepareStatement(sqlDelCTHD);
            pstDelCTHD.setString(1, maPhim);
            pstDelCTHD.executeUpdate();

            // 2) (TUỲ CHỌN) XÓA HÓA ĐƠN RỖNG sau khi đã xóa chi tiết
            String sqlDelHoaDonRong =
                "DELETE FROM HoaDon " +
                "WHERE NOT EXISTS (SELECT 1 FROM ChiTietHoaDon c WHERE c.MaHoaDon = HoaDon.MaHoaDon)";
            pstDelHoaDonRong = con.prepareStatement(sqlDelHoaDonRong);
            pstDelHoaDonRong.executeUpdate();

            // 3) XÓA VÉ thuộc các suất chiếu của phim
            String sqlDelVe =
                "DELETE v " +
                "FROM Ve v " +
                "JOIN SuatChieu sc ON v.MaSuatChieu = sc.MaSuatChieu " +
                "WHERE sc.MaPhim = ?";
            pstDelVe = con.prepareStatement(sqlDelVe);
            pstDelVe.setString(1, maPhim);
            pstDelVe.executeUpdate();

            // 4) XÓA SUẤT CHIẾU của phim
            String sqlDelSuat = "DELETE FROM SuatChieu WHERE MaPhim = ?";
            pstDelSuat = con.prepareStatement(sqlDelSuat);
            pstDelSuat.setString(1, maPhim);
            pstDelSuat.executeUpdate();

            // 5) XÓA PHIM
            String sqlDelPhim = "DELETE FROM Phim WHERE MaPhim = ?";
            pstDelPhim = con.prepareStatement(sqlDelPhim);
            pstDelPhim.setString(1, maPhim);
            pstDelPhim.executeUpdate();

            con.commit();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            try { if (con != null) con.rollback(); } catch (Exception ex) { /* ignore */ }
        } finally {
            try { if (pstDelCTHD != null) pstDelCTHD.close(); } catch (Exception ignore) {}
            try { if (pstDelVe != null) pstDelVe.close(); } catch (Exception ignore) {}
            try { if (pstDelSuat != null) pstDelSuat.close(); } catch (Exception ignore) {}
            try { if (pstDelPhim != null) pstDelPhim.close(); } catch (Exception ignore) {}
            try { if (pstDelHoaDonRong != null) pstDelHoaDonRong.close(); } catch (Exception ignore) {}
            try { if (con != null) connectDB.closeConnection(con); } catch (Exception ignore) {}
        }
        return success;
    }

    // ========================= TÌM PHIM THEO MÃ =========================
    public Phim findPhimByMa(String maPhim) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Phim p = null;
        try {
            con = connectDB.getConnection();
            String sql = "SELECT MaPhim, TenPhim, ThoiLuong, TenDaoDien, TenCacDienVien, TheLoai, GiaPhim FROM Phim WHERE MaPhim = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maPhim);
            rs = stmt.executeQuery();
            if (rs.next()) {
                p = mapPhim(rs);
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
            String sql = "SELECT MaPhim, TenPhim, ThoiLuong, TenDaoDien, TenCacDienVien, TheLoai, GiaPhim "
                       + "FROM Phim WHERE TenPhim LIKE ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tenPhim + "%");
            rs = stmt.executeQuery();
            while (rs.next()) {
                dsPhim.add(mapPhim(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(con, stmt, rs);
        }
        return dsPhim;
    }

    // ========================= GÁN SUẤT CHIẾU CHO PHIM =========================
    public boolean assignSuatChieuToPhim(String maPhim, String maSuatChieu) {
        String sql = "UPDATE SuatChieu SET MaPhim = ? WHERE MaSuatChieu = ?";
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
            String sql = "SELECT p.MaPhim, p.TenPhim, p.ThoiLuong, p.TenDaoDien, p.TenCacDienVien, p.TheLoai, p.GiaPhim "
                       + "FROM Phim p JOIN SuatChieu sc ON p.MaPhim = sc.MaPhim "
                       + "WHERE sc.MaSuatChieu = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maSuatChieu);
            rs = stmt.executeQuery();
            while (rs.next()) {
                dsPhim.add(mapPhim(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(con, stmt, rs);
        }
        return dsPhim;
    }

    // ========================= Lấy tất cả mã suất chiếu của 1 phim =========================
    public ArrayList<String> getSuatChieuCuaPhim(String maPhim) {
        ArrayList<String> dsSuat = new ArrayList<>();
        String sql = "SELECT MaSuatChieu FROM SuatChieu WHERE MaPhim = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, maPhim);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    dsSuat.add(rs.getString("MaSuatChieu"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsSuat;
    }

    // ========================= MAP RESULTSET -> PHIM =========================
    private Phim mapPhim(ResultSet rs) throws Exception {
        String maPhim = rs.getString("MaPhim");
        String tenPhim = rs.getString("TenPhim");
        int thoiLuong = rs.getInt("ThoiLuong");
        String tenDaoDien = rs.getString("TenDaoDien");
        String tenCacDienVien = rs.getString("TenCacDienVien"); // đúng tên cột
        String theLoai = rs.getString("TheLoai");

        // GiaPhim DECIMAL(18,0) -> đọc BigDecimal rồi convert về double nếu entity dùng double
        BigDecimal giaPhimDec = rs.getBigDecimal("GiaPhim");
        double giaPhim = (giaPhimDec != null ? giaPhimDec.doubleValue() : 0d);

        // Tùy entity.Phim của bạn nhận tham số nào:
        // new Phim(maPhim, tenPhim, thoiLuong, tenDaoDien, tenCacDienVien, theLoai, giaPhim)
        return new Phim(maPhim, tenPhim, thoiLuong, tenDaoDien, tenCacDienVien, theLoai, giaPhim);
    }

    // ========================= HÀM ĐÓNG TÀI NGUYÊN =========================
    private void closeResource(Connection con, java.sql.Statement stmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (Exception e) { /* ignore */ }
        try { if (stmt != null) stmt.close(); } catch (Exception e) { /* ignore */ }
        try { if (con != null) connectDB.closeConnection(con); } catch (Exception e) { /* ignore */ }
    }
}