package entity;

public class Ve {
    private String maVe;
    private double giaVe;
    private SuatChieu suatChieu;
    private Ghe ghe;

    public Ve() {}

    public Ve(String maVe, double giaVe, SuatChieu suatChieu, Ghe ghe) {
        this.maVe = maVe;
        this.giaVe = giaVe;
        this.suatChieu = suatChieu;
        this.ghe = ghe;
    }

	public String getMaVe() {
		return maVe;
	}

	public void setMaVe(String maVe) {
		this.maVe = maVe;
	}

	public double getGiaVe() {
		return giaVe;
	}

	public void setGiaVe(double giaVe) {
		this.giaVe = giaVe;
	}

	public SuatChieu getSuatChieu() {
		return suatChieu;
	}

	public void setSuatChieu(SuatChieu suatChieu) {
		this.suatChieu = suatChieu;
	}

	public Ghe getGhe() {
		return ghe;
	}

	public void setGhe(Ghe ghe) {
		this.ghe = ghe;
	}

	@Override
	public String toString() {
		return "Ve [maVe=" + maVe + ", giaVe=" + giaVe + ", suatChieu=" + suatChieu + ", ghe=" + ghe + "]";
	}

}
