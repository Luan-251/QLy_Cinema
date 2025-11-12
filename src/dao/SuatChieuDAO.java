package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;

import connectDB.connectDB;
import entity.LoaiSuatChieu;
import entity.Phim;
import entity.Phong;
import entity.SuatChieu;

public class SuatChieuDAO {

	public ArrayList<SuatChieu> getAllSuatChieu() {
		ArrayList<SuatChieu> dsSuat = new ArrayList<>();
		String sql = "SELECT maSuatChieu, thoiGianBatDau, thoiGianKetThuc, maPhim, maPhong, maLoaiSuat FROM SuatChieu";

		try (Connection con = connectDB.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()) {

			while (rs.next()) {
				String maSuat = rs.getString("maSuatChieu");
				LocalDateTime batDau = rs.getTimestamp("thoiGianBatDau").toLocalDateTime();
				LocalDateTime ketThuc = rs.getTimestamp("thoiGianKetThuc").toLocalDateTime();

				// Các quan hệ tạm thời chỉ tạo bằng mã (chưa cần load đầy đủ thông tin)
				Phim phim = new Phim();
				phim.setMaPhim(rs.getString("maPhim"));

				Phong phong = new Phong();
				phong.setMaPhong(rs.getString("maPhong"));

				LoaiSuatChieu loaiSuat = new LoaiSuatChieu();
				loaiSuat.setMaLoaiSuat(rs.getString("maLoaiSuat"));

				SuatChieu suat = new SuatChieu(maSuat, batDau, ketThuc, phim, phong, loaiSuat);
				dsSuat.add(suat);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsSuat;
	}

	public ArrayList<SuatChieu> getSuatChieuByPhim(Phim phim) {
		ArrayList<SuatChieu> dsSuat = new ArrayList<>();
		String sql = "SELECT maSuatChieu, thoiGianBatDau, thoiGianKetThuc, maPhim, maPhong, maLoaiSuat "
				+ "FROM SuatChieu WHERE maPhim = ?";

		try (Connection con = connectDB.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

			pst.setString(1, phim.getMaPhim());
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				String maSuat = rs.getString("maSuatChieu");
				LocalDateTime batDau = rs.getTimestamp("thoiGianBatDau").toLocalDateTime();
				LocalDateTime ketThuc = rs.getTimestamp("thoiGianKetThuc").toLocalDateTime();

				Phong phong = new Phong();
				phong.setMaPhong(rs.getString("maPhong"));

				LoaiSuatChieu loaiSuat = new LoaiSuatChieu();
				loaiSuat.setMaLoaiSuat(rs.getString("maLoaiSuat"));

				SuatChieu suat = new SuatChieu(maSuat, batDau, ketThuc, phim, phong, loaiSuat);
				dsSuat.add(suat);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dsSuat;
	}

}
