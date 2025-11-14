package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import connectDB.connectDB;
import entity.Phim;

public class PhimDAO {

	public ArrayList<Phim> getAllPhim() {
		ArrayList<Phim> dsPhim = new ArrayList<>();
		String sql = "SELECT * FROM Phim";

		try (Connection con = connectDB.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()) {

			while (rs.next()) {
				Phim p = new Phim(rs.getString("maPhim"), rs.getString("tenPhim"), rs.getInt("thoiLuong"),
						rs.getString("tenDaoDien"), rs.getString("tenCacDienVien"), rs.getString("theLoai"),
						rs.getDouble("giaPhim"));

				dsPhim.add(p);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dsPhim;
	}
}
