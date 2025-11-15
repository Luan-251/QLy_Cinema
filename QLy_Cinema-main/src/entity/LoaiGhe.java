package entity;

public class LoaiGhe {
    private String maLoaiGhe;
    private String tenLoaiGhe;

    public LoaiGhe() {}

    public LoaiGhe(String maLoaiGhe, String tenLoaiGhe) {
        this.maLoaiGhe = maLoaiGhe;
        this.tenLoaiGhe = tenLoaiGhe;
    }

	public String getMaLoaiGhe() {
		return maLoaiGhe;
	}

	public void setMaLoaiGhe(String maLoaiGhe) {
		this.maLoaiGhe = maLoaiGhe;
	}

	public String getTenLoaiGhe() {
		return tenLoaiGhe;
	}

	public void setTenLoaiGhe(String tenLoaiGhe) {
		this.tenLoaiGhe = tenLoaiGhe;
	}

	@Override
	public String toString() {
		return "LoaiGhe [maLoaiGhe=" + maLoaiGhe + ", tenLoaiGhe=" + tenLoaiGhe + "]";
	}

    
}
