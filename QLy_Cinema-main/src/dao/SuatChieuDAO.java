package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.Phong;
import entity.Phim;
import entity.SuatChieu;
import connectDB.connectDB;

public class SuatChieuDAO {

    private PhimDAO phimDAO = new PhimDAO();
    private PhongDAO phongDAO = new PhongDAO();

    // ===== Lấy tất cả suất chiếu =====
    public List<SuatChieu> getAll() {
        List<SuatChieu> ds = new ArrayList<>();
        String sql = "SELECT * FROM SuatChieu";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String maSuat = rs.getString("MaSuatChieu");
                Timestamp ts = rs.getTimestamp("ThoiGianBatDau");
                Phim phim = phimDAO.findPhimByMa(rs.getString("MaPhim"));
                Phong phong = phongDAO.findPhongByMa(rs.getString("MaPhong"));

                SuatChieu sc = new SuatChieu(maSuat, ts, phim, phong);
                ds.add(sc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    // ===== Tìm theo mã suất chiếu =====
    public SuatChieu findById(String maSuat) {
        String sql = "SELECT * FROM SuatChieu WHERE MaSuatChieu=?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSuat);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Phim phim = phimDAO.findPhimByMa(rs.getString("MaPhim"));
                Phong phong = phongDAO.findPhongByMa(rs.getString("MaPhong"));
                return new SuatChieu(
                        rs.getString("MaSuatChieu"),
                        rs.getTimestamp("ThoiGianBatDau"),
                        phim,
                        phong
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===== Thêm suất chiếu =====
    public boolean insert(SuatChieu sp) {
        String sql = "INSERT INTO SuatChieu(MaSuatChieu, ThoiGianBatDau, MaPhong, MaPhim) VALUES (?, ?, ?, ?)";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, sp.getMaSuatChieu());
            ps.setTimestamp(2, new Timestamp(sp.getThoiGianBatDau().getTime()));
            ps.setString(3, sp.getMaPhong().getMaPhong());
            ps.setString(4, sp.getMaPhim().getMaPhim());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===== Cập nhật suất chiếu =====
    public boolean update(SuatChieu sp) {
        String sql = "UPDATE SuatChieu SET ThoiGianBatDau=?, MaPhong=?, MaPhim=? WHERE MaSuatChieu=?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, new Timestamp(sp.getThoiGianBatDau().getTime()));
            ps.setString(2, sp.getMaPhong().getMaPhong());
            ps.setString(3, sp.getMaPhim().getMaPhim());
            ps.setString(4, sp.getMaSuatChieu());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===== Xóa suất chiếu =====
    public boolean delete(String maSuat) {
        String sql = "DELETE FROM SuatChieu WHERE MaSuatChieu=?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maSuat);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
