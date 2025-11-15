package entity;

import java.util.Date;

public class SuatChieu {

    private String maSuatChieu;
    private Date thoiGianBatDau;
    private Phim maPhim;
    private Phong maPhong;

    public SuatChieu() {}

    public SuatChieu(String maSuatChieu, Date thoiGianBatDau,
                    Phim maPhim, Phong maPhong) {
        this.maSuatChieu = maSuatChieu;
        this.thoiGianBatDau = thoiGianBatDau;
        this.maPhim = maPhim;
        this.maPhong = maPhong;
    }

    public String getMaSuatChieu() {
        return maSuatChieu;
    }

    public void setMaSuatChieu(String maSuatChieu) {
        this.maSuatChieu = maSuatChieu;
    }

    public Date getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(Date thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public Phim getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(Phim maPhim) {
        this.maPhim = maPhim;
    }

    public Phong getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(Phong maPhong) {
        this.maPhong = maPhong;
    }
}
