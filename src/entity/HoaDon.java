package entity;

import java.time.LocalDateTime;

public class HoaDon {
	private String maHoaDon;
	private LocalDateTime ngayLap;
	private double tongTien;
	private int soVe;
	private NhanVien nhanVien;
	private Ve ve;

	public HoaDon() {
	}

	public HoaDon(String maHoaDon, LocalDateTime ngayLap, double tongTien, int soVe, NhanVien nhanVien, Ve ve) {
		this.maHoaDon = maHoaDon;
		this.ngayLap = ngayLap;
		this.tongTien = tongTien;
		this.soVe = soVe;
		this.nhanVien = nhanVien;
		this.ve = ve;
	}

	public String getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}

	public LocalDateTime getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(LocalDateTime ngayLap) {
		this.ngayLap = ngayLap;
	}

	public double getTongTien() {
		return tongTien;
	}

	public void setTongTien(double tongTien) {
		this.tongTien = tongTien;
	}

	public int getSoVe() {
		return soVe;
	}

	public void setSoVe(int soVe) {
		this.soVe = soVe;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public Ve getVe() {
		return ve;
	}

	public void setVe(Ve ve) {
		this.ve = ve;
	}

	@Override
	public String toString() {
		return "HoaDon [maHoaDon=" + maHoaDon + ", ngayLap=" + ngayLap + ", tongTien=" + tongTien + ", soVe=" + soVe
				+ ", nhanVien=" + nhanVien + ", ve=" + ve + "]";
	}

}
