package entity;

public class Phong {
    private String maPhong;
    private String tenPhong;
    private int soPhong;
    private int soGhe;

    public Phong() {}

    public Phong(String maPhong, String tenPhong, int soPhong, int soGhe) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.soPhong = soPhong;
        this.soGhe = soGhe;
    }

	public String getMaPhong() {
		return maPhong;
	}

	public void setMaPhong(String maPhong) {
		this.maPhong = maPhong;
	}

	public String getTenPhong() {
		return tenPhong;
	}

	public void setTenPhong(String tenPhong) {
		this.tenPhong = tenPhong;
	}

	public int getSoPhong() {
		return soPhong;
	}

	public void setSoPhong(int soPhong) {
		this.soPhong = soPhong;
	}

	public int getSoGhe() {
		return soGhe;
	}

	public void setSoGhe(int soGhe) {
		this.soGhe = soGhe;
	}

	@Override
	public String toString() {
		return "Phong [maPhong=" + maPhong + ", tenPhong=" + tenPhong + ", soPhong=" + soPhong + ", soGhe=" + soGhe
				+ "]";
	}

}
