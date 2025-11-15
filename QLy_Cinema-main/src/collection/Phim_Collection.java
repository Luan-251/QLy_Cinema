package collection;

import java.util.ArrayList;

import dao.PhimDAO;
import entity.Phim;

public class Phim_Collection {

	private PhimDAO phimDAO;
	
	public Phim_Collection() {
		this.phimDAO = new PhimDAO();
	}
	
	// ========== LẤY TẤT CẢ PHIM ==========
	public ArrayList<Phim> getAllPhim() {
		return phimDAO.getAllPhim();
	}
	
	// ========== THÊM PHIM ==========
	public boolean insertPhim(Phim phim) {
		return phimDAO.insertPhim(phim);
	}
	
	// ========== CẬP NHẬT PHIM ==========
	public boolean updatePhim(Phim phim) {
		return phimDAO.updatePhim(phim);
	}
	
	// ========== XÓA PHIM ==========
	public boolean deletePhim(String maPhim) {
		return phimDAO.deletePhim(maPhim);
	}

	// ========== TÌM PHIM THEO MÃ ==========
	public Phim findPhimByMa(String maPhim) {
		return phimDAO.findPhimByMa(maPhim);
	}

	// ========== TÌM PHIM THEO TÊN (LIKE %) ==========
	public ArrayList<Phim> findPhimByMaLike(String tenPhim) {
		return phimDAO.findPhimByTen(tenPhim);
	}
	
	// ========== LỌC PHIM THEO SUẤT CHIẾU ==========
	public ArrayList<Phim> getPhimTheoSuatChieu(String maSuatChieu) {
	    return phimDAO.getPhimTheoSuatChieu(maSuatChieu);
	}
	
	public ArrayList<String> getSuatChieuCuaPhim(String maSuatChieu) {
	    return phimDAO.getSuatChieuCuaPhim(maSuatChieu);
	}
	
	public boolean assignSuatChieuToPhim(String maPhim, String selectedSuat) {
		// TODO Auto-generated method stub
		return phimDAO.assignSuatChieuToPhim(maPhim, selectedSuat);
	}
}
