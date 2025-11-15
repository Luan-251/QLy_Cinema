package entity;

public class TrangThai {
    private String maTrangThai;
    private String tenTrangThai;

    public TrangThai() {}

    public TrangThai(String maTrangThai, String tenTrangThai) {
        this.maTrangThai = maTrangThai;
        this.tenTrangThai = tenTrangThai;
    }

	public String getMaTrangThai() {
		return maTrangThai;
	}

	public void setMaTrangThai(String maTrangThai) {
		this.maTrangThai = maTrangThai;
	}

	public String getTenTrangThai() {
		return tenTrangThai;
	}

	public void setTenTrangThai(String tenTrangThai) {
		this.tenTrangThai = tenTrangThai;
	}

	@Override
	public String toString() {
		return "TrangThai [maTrangThai=" + maTrangThai + ", tenTrangThai=" + tenTrangThai + "]";
	}

}
