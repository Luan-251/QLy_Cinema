package dao;

import connectDB.connectDB;
import entity.NhanVien;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng NhanVien: CRUD, tìm kiếm, kiểm tra tồn tại email, đếm số NV, đổi mật khẩu.
 */
public class NhanVienDAO {

    // Lấy tất cả nhân viên
    public List<NhanVien> getAll() {
        String sql = "SELECT MaNhanVien, TenNhanVien, Email, MatKhau, ThoiGianTaoTaiKhoan " +
                     "FROM NhanVien ORDER BY TenNhanVien";
        List<NhanVien> list = new ArrayList<>();
        try (Connection con = connectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm theo mã
    public NhanVien findById(String maNV) {
        String sql = "SELECT MaNhanVien, TenNhanVien, Email, MatKhau, ThoiGianTaoTaiKhoan " +
                     "FROM NhanVien WHERE MaNhanVien = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, maNV);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm theo tên (LIKE %keyword%)
    public List<NhanVien> searchByName(String keyword) {
        String sql = "SELECT MaNhanVien, TenNhanVien, Email, MatKhau, ThoiGianTaoTaiKhoan " +
                     "FROM NhanVien WHERE TenNhanVien LIKE ? ORDER BY TenNhanVien";
        List<NhanVien> list = new ArrayList<>();
        try (Connection con = connectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm nhân viên
    public boolean insert(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (MaNhanVien, TenNhanVien, Email, MatKhau, ThoiGianTaoTaiKhoan) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = connectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nv.getMaNhanVien());
            pst.setNString(2, nv.getTenNhanVien()); // NVARCHAR -> setNString
            pst.setString(3, nv.getEmail());
            pst.setNString(4, nv.getMatKhau());     // NVARCHAR -> setNString
            LocalDateTime t = nv.getThoiGianTaoTaiKhoan() != null ? nv.getThoiGianTaoTaiKhoan() : LocalDateTime.now();
            pst.setTimestamp(5, Timestamp.valueOf(t));
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật nhân viên
    public boolean update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET TenNhanVien = ?, Email = ?, MatKhau = ?, ThoiGianTaoTaiKhoan = ? " +
                     "WHERE MaNhanVien = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setNString(1, nv.getTenNhanVien());
            pst.setString(2, nv.getEmail());
            pst.setNString(3, nv.getMatKhau());
            LocalDateTime t = nv.getThoiGianTaoTaiKhoan() != null ? nv.getThoiGianTaoTaiKhoan() : LocalDateTime.now();
            pst.setTimestamp(4, Timestamp.valueOf(t));
            pst.setString(5, nv.getMaNhanVien());
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa nhân viên
    public boolean delete(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE MaNhanVien = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, maNV);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra email đã tồn tại
    public boolean existsEmail(String email) {
        String sql = "SELECT 1 FROM NhanVien WHERE Email = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Đếm số lượng nhân viên
    public int count() {
        String sql = "SELECT COUNT(*) FROM NhanVien";
        try (Connection con = connectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Đổi mật khẩu (ví dụ có thể hash nhẹ MD5 cho demo; thực tế nên dùng BCrypt/Argon2)
    public boolean changePassword(String maNV, String newPlainPassword) {
        String sql = "UPDATE NhanVien SET MatKhau = ? WHERE MaNhanVien = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setNString(1, newPlainPassword); // hoặc hashMD5(newPlainPassword)
            pst.setString(2, maNV);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Map ResultSet -> NhanVien
    private NhanVien map(ResultSet rs) throws Exception {
        String ma = rs.getString("MaNhanVien");
        String ten = rs.getNString("TenNhanVien");
        String email = rs.getString("Email");
        String mk = rs.getNString("MatKhau");
        Timestamp ts = rs.getTimestamp("ThoiGianTaoTaiKhoan");
        LocalDateTime t = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();
        return new NhanVien(ma, ten, email, mk, t);
    }

    // (Tuỳ chọn) demo hash MD5 — KHÔNG khuyến nghị cho sản phẩm thật
    @SuppressWarnings("unused")
    private String hashMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes("UTF-8"));
            BigInteger bi = new BigInteger(1, digest);
            return String.format("%032x", bi);
        } catch (Exception e) {
            return input;
        }
    }
    
 // ======= ĐĂNG NHẬP THEO TÊN NHÂN VIÊN =======
    public NhanVien loginByName(String tenNhanVien, String plainPassword) {
        String sql = "SELECT MaNhanVien, TenNhanVien, Email, MatKhau, ThoiGianTaoTaiKhoan " +
                     "FROM NhanVien WHERE TenNhanVien = ? AND MatKhau = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            // NVARCHAR -> nên dùng setNString
            pst.setNString(1, tenNhanVien != null ? tenNhanVien.trim() : null);
            pst.setNString(2, plainPassword); // Nếu có hash thì đổi ở đây
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs); // map(ResultSet) đã có trong DAO trước đó
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ======= ĐĂNG NHẬP THEO MÃ NHÂN VIÊN =======
    public NhanVien loginById(String maNV, String plainPassword) {
        String sql = "SELECT MaNhanVien, TenNhanVien, Email, MatKhau, ThoiGianTaoTaiKhoan " +
                     "FROM NhanVien WHERE MaNhanVien = ? AND MatKhau = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, maNV);
            pst.setNString(2, plainPassword); // Hash nếu cần
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}