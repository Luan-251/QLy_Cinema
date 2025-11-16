package entity;

public class Ghe {
	private String maGhe;
	private String tenGhe;
	private double giaGhe;
	private LoaiGhe loaiGhe;
	private TrangThai trangThai;
	private Phong phong;

	public Ghe() {
	}

	public Ghe(String maGhe, String tenGhe, double giaGhe, LoaiGhe loaiGhe, TrangThai trangThai, Phong phong) {
		this.maGhe = maGhe;
		this.tenGhe = tenGhe;
		this.giaGhe = giaGhe;
		this.loaiGhe = loaiGhe;
		this.trangThai = trangThai;
		this.phong = phong;
	}

	public String getMaGhe() {
		return maGhe;
	}

	public void setMaGhe(String maGhe) {
		this.maGhe = maGhe;
	}

	public String getTenGhe() {
		return tenGhe;
	}

	public void setTenGhe(String tenGhe) {
		this.tenGhe = tenGhe;
	}

	public double getGiaGhe() {
		return giaGhe;
	}

	public void setGiaGhe(double giaGhe) {
		this.giaGhe = giaGhe;
	}

	public LoaiGhe getLoaiGhe() {
		return loaiGhe;
	}

	public void setLoaiGhe(LoaiGhe loaiGhe) {
		this.loaiGhe = loaiGhe;
	}

	public TrangThai getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(TrangThai trangThai) {
		this.trangThai = trangThai;
	}

	public Phong getPhong() {
		return phong;
	}

	public void setPhong(Phong phong) {
		this.phong = phong;
	}

	@Override
	public String toString() {
		return "Ghe [maGhe=" + maGhe + ", tenGhe=" + tenGhe + ", giaGhe=" + giaGhe + ", loaiGhe=" + loaiGhe
				+ ", trangThai=" + trangThai + ", phong=" + phong + "]";
	}

}
