package entity;

import java.time.LocalDateTime;

public class SuatChieu {
	private String maSuatChieu;
	private LocalDateTime thoiGianBatDau;
	private LocalDateTime thoiGianKetThuc; // có thể null
	private Phim phim;
	private Phong phong;

	// Constructor
	public SuatChieu(String maSuatChieu, LocalDateTime thoiGianBatDau, LocalDateTime thoiGianKetThuc, Phim phim,
			Phong phong) {
		this.maSuatChieu = maSuatChieu;
		this.thoiGianBatDau = thoiGianBatDau;
		this.thoiGianKetThuc = thoiGianKetThuc;
		this.phim = phim;
		this.phong = phong;
	}

	// Getter
	public String getMaSuatChieu() {
		return maSuatChieu;
	}

	public LocalDateTime getThoiGianBatDau() {
		return thoiGianBatDau;
	}

	public LocalDateTime getThoiGianKetThuc() {
		return thoiGianKetThuc;
	}

	public Phim getPhim() {
		return phim;
	}

	public Phong getPhong() {
		return phong;
	}

	// toString để hiển thị JComboBox
	@Override
	public String toString() {
		return thoiGianBatDau.toLocalTime().toString(); // ví dụ: "09:00"
	}
}
