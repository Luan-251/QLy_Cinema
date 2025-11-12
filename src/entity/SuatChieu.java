package entity;

import java.time.LocalDateTime;

public class SuatChieu {
    private String maSuatChieu;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;
    private Phim phim;
    private Phong phong;
    private LoaiSuatChieu loaiSuat;

    public SuatChieu() {}

    public SuatChieu(String maSuatChieu, LocalDateTime thoiGianBatDau,
                     LocalDateTime thoiGianKetThuc, Phim phim, Phong phong, LoaiSuatChieu loaiSuat) {
        this.maSuatChieu = maSuatChieu;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.phim = phim;
        this.phong = phong;
        this.loaiSuat = loaiSuat;
    }

	public String getMaSuatChieu() {
		return maSuatChieu;
	}

	public void setMaSuatChieu(String maSuatChieu) {
		this.maSuatChieu = maSuatChieu;
	}

	public LocalDateTime getThoiGianBatDau() {
		return thoiGianBatDau;
	}

	public void setThoiGianBatDau(LocalDateTime thoiGianBatDau) {
		this.thoiGianBatDau = thoiGianBatDau;
	}

	public LocalDateTime getThoiGianKetThuc() {
		return thoiGianKetThuc;
	}

	public void setThoiGianKetThuc(LocalDateTime thoiGianKetThuc) {
		this.thoiGianKetThuc = thoiGianKetThuc;
	}

	public Phim getPhim() {
		return phim;
	}

	public void setPhim(Phim phim) {
		this.phim = phim;
	}

	public Phong getPhong() {
		return phong;
	}

	public void setPhong(Phong phong) {
		this.phong = phong;
	}

	public LoaiSuatChieu getLoaiSuat() {
		return loaiSuat;
	}

	public void setLoaiSuat(LoaiSuatChieu loaiSuat) {
		this.loaiSuat = loaiSuat;
	}

	@Override
	public String toString() {
		return "SuatChieu [maSuatChieu=" + maSuatChieu + ", thoiGianBatDau=" + thoiGianBatDau + ", thoiGianKetThuc="
				+ thoiGianKetThuc + ", phim=" + phim + ", phong=" + phong + ", loaiSuat=" + loaiSuat + "]";
	}

}
