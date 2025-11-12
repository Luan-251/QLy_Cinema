package entity;

import java.util.Objects;

public class Phim {
    private String maPhim;
    private String tenPhim;
    private int thoiLuong;
    private String tenDaoDien;
    private String tenDienVien;
    private String theLoai;
    private double giaPhim;

    public Phim() {}

    public Phim(String maPhim, String tenPhim, int thoiLuong, String tenDaoDien,
                String tenDienVien, String theLoai, double giaPhim) {
        this.maPhim = maPhim;
        this.tenPhim = tenPhim;
        this.thoiLuong = thoiLuong;
        this.tenDaoDien = tenDaoDien;
        this.tenDienVien = tenDienVien;
        this.theLoai = theLoai;
        this.giaPhim = giaPhim;
    }

    // Getters & Setters
    public String getMaPhim() { return maPhim; }
    public void setMaPhim(String maPhim) { this.maPhim = maPhim; }
    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }
    public int getThoiLuong() { return thoiLuong; }
    public void setThoiLuong(int thoiLuong) { this.thoiLuong = thoiLuong; }
    public String getTenDaoDien() { return tenDaoDien; }
    public void setTenDaoDien(String tenDaoDien) { this.tenDaoDien = tenDaoDien; }
    public String getTenDienVien() { return tenDienVien; }
    public void setTenDienVien(String tenDienVien) { this.tenDienVien = tenDienVien; }
    public String getTheLoai() { return theLoai; }
    public void setTheLoai(String theLoai) { this.theLoai = theLoai; }
    public double getGiaPhim() { return giaPhim; }
    public void setGiaPhim(double giaPhim) { this.giaPhim = giaPhim; }

    @Override
    public String toString() {
        return "Phim [maPhim=" + maPhim + ", tenPhim=" + tenPhim + "]";
    }

    // ========= equals và hashCode =========
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // cùng tham chiếu
        if (obj == null || getClass() != obj.getClass()) return false;
        Phim other = (Phim) obj;
        return maPhim != null && maPhim.equals(other.maPhim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPhim);
    }
}
