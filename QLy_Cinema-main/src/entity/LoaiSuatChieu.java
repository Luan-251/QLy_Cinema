package entity;

public class LoaiSuatChieu {
    private String maLoaiSuat;
    private String tenLoaiSuat;

    public LoaiSuatChieu() {}

    public LoaiSuatChieu(String maLoaiSuat, String tenLoaiSuat) {
        this.maLoaiSuat = maLoaiSuat;
        this.tenLoaiSuat = tenLoaiSuat;
    }

	public String getMaLoaiSuat() {
		return maLoaiSuat;
	}

	public void setMaLoaiSuat(String maLoaiSuat) {
		this.maLoaiSuat = maLoaiSuat;
	}

	public String getTenLoaiSuat() {
		return tenLoaiSuat;
	}

	public void setTenLoaiSuat(String tenLoaiSuat) {
		this.tenLoaiSuat = tenLoaiSuat;
	}

	@Override
	public String toString() {
		return "LoaiSuatChieu [maLoaiSuat=" + maLoaiSuat + ", tenLoaiSuat=" + tenLoaiSuat + "]";
	}

}
